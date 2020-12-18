package ru.fomin.chat.server.core;

import ru.fomin.chat.common.Library;
import rufomin.network.ServerSocketThread;
import rufomin.network.ServerSocketThreadListener;
import rufomin.network.SocketThread;
import rufomin.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private final ChatServerListener listener;
    private final Vector<SocketThread> clients;
    private ServerSocketThread thread;

    public ChatServer(ChatServerListener listener) {
        this.listener = listener;
        this.clients = new Vector<>();
    }

    public void start(int port) {
        if (thread != null && thread.isAlive()) {
            putLog("Server already started");
        } else {
            thread = new ServerSocketThread(this, "Thread of server", port, 2000);
        }
    }

    public void stop() {
        if (thread == null || !thread.isAlive()) {
            putLog("Server is not running");
        } else {
            thread.interrupt();
        }
    }

    private void putLog(String msg) {
        msg = DATE_FORMAT.format(System.currentTimeMillis()) +
                Thread.currentThread().getName() + ": " + msg;
        listener.onChatServerMessage(msg);
    }

    private void handleNonAuthMessage(ClientThread client, String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        switch (arr[0]){
            case Library.AUTH_REQUEST:
                String login = arr[1];
                String password = arr[2];
                String nickname = SqlClient.getNickname(login, password);
                if (nickname == null) {
                    putLog("Invalid login attempt: " + login);
                    client.authFail();
                    return;
                } else {
                    ClientThread oldClient = findClientByLogin(login);
                    if(findClientByNickname(nickname)!=null)nickname= client.getNicknameWithIncrement(nickname);
                    client.authAccept(nickname, login);
                    if (oldClient == null) {
                        sendToAllAuthorizedClients(Library.getTypeBroadcast("Server", nickname + " connected"));
                    } else {
                        oldClient.reconnect();
                        clients.remove(oldClient);
                    }
                }
                sendToAllAuthorizedClients(Library.getUserList(getUsers()));
                break;
            case Library.REGISTRATION:
                try{
                if(SqlClient.registration(arr[1],arr[2],arr[3])) client.sendMessage(Library.REGISTRATION_SUCCESSFULLY);
                else client.sendMessage(Library.REGISTRATION_NOT_SUCCESSFULLY);}catch (SQLException e){
                    client.sendMessage(Library.REGISTRATION_NOT_SUCCESSFULLY);
                }
                break;
            default:
                client.msgFormatError(msg);
        }

    }

    private ClientThread findClientByLogin(String login) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            if (client.getLogin().equals(login))
                return client;
        }
        return null;
    }

    private void handleAuthMessage(ClientThread client, String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Library.TYPE_BCAST_CLIENT:
                sendToAllAuthorizedClients(Library.getTypeBroadcast(
                        client.getNickname(), arr[1]));
                break;
            case Library.TYPE_PRIVATE:
                sendPrivateMessage(arr[1], client, Library.getTypePrivate(client.getNickname(), arr[2]));
                break;
            case Library.CHANGING_NICKNAME:
                    if(SqlClient.changeNickname(arr[1],client.getLogin())) client.sendMessage(Library.NICKNAME_WAS_CHANGED);
                break;
            default:
                client.msgFormatError(msg);

        }
    }

    private void sendPrivateMessage(String destination, ClientThread src, String msg) {
        ClientThread destinationClientThread = findClientByNickname(destination);
        if (destinationClientThread.equals(src)) {
            src.sendMessage(Library.getErrorBySendingYourself());
            return;
        }
        src.sendMessage(msg);
        destinationClientThread.sendMessage(msg);
    }


    private void sendToAllAuthorizedClients(String msg) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread recipient = (ClientThread) clients.get(i);
            if (!recipient.isAuthorized()) continue;
            recipient.sendMessage(msg);
        }
    }

    private String getUsers() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            sb.append(client.getNickname()).append(Library.DELIMITER);
        }
        return sb.toString();
    }

    private synchronized ClientThread findClientByNickname(String nickname) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            if (client.getNickname().equals(nickname))
                return client;
        }
        return null;
    }

    /**
     * Server methods
     */

    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server thread started");
        SqlClient.connect();
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Server thread stopped");
        SqlClient.disconnect();
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).close();
        }
    }

    @Override
    public void onServerSocketCreated(ServerSocketThread thread, ServerSocket server) {
        putLog("Server socket created");

    }

    @Override
    public void onServerTimeout(ServerSocketThread thread, ServerSocket server) {
      //  putLog("Server timeout");

    }

    @Override
    public void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Client connected");
        String name = "SocketThread " + socket.getInetAddress() + ":" + socket.getPort();
        new ClientThread(this, name, socket);

    }

    @Override
    public void onServerException(ServerSocketThread thread, Throwable exception) {
        exception.printStackTrace();
    }

    /**
     * Socket methods
     */

    @Override
    public synchronized void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Socket created");

    }

    @Override
    public synchronized void onSocketStop(SocketThread thread) {
        putLog("Socket stopped");
        clients.remove(thread);
        ClientThread client = (ClientThread) thread;
        if (client.isAuthorized() && !client.isReconnecting()) {
            sendToAllAuthorizedClients(Library.getTypeBroadcast("Server",
                    client.getNickname() + " disconnected"));
        }
        sendToAllAuthorizedClients(Library.getUserList(getUsers()));

    }

    @Override
    public synchronized void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Socket ready");
        clients.add(thread);
    }

    @Override
    public synchronized void onReceiveString(SocketThread thread, Socket socket, String msg) {
        ClientThread client = (ClientThread) thread;
        if (client.isAuthorized())
            handleAuthMessage(client, msg);
        else
            handleNonAuthMessage(client, msg);
    }

    @Override
    public synchronized void onSocketException(SocketThread thread, Exception exception) {
        exception.printStackTrace();
    }

}
