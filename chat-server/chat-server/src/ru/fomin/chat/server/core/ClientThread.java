package ru.fomin.chat.server.core;

import ru.fomin.chat.common.Library;
import rufomin.network.SocketThread;
import rufomin.network.SocketThreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {

    private String nickname, login;
    private boolean isAuthorized, isReconnecting;
    private int count = 0;

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public boolean isReconnecting() {
        return isReconnecting;
    }

    void reconnect() {
        isReconnecting = true;
        close();
    }

    void authAccept(String nickname, String login) {
        String postfixNickName;
        isAuthorized = true;
        if (count == 0) postfixNickName = "";
        else postfixNickName = "(" + count + ")";
        this.nickname = nickname + postfixNickName;
        this.login = login;
        sendMessage(Library.getAuthAccept(nickname));
    }

    void authFail() {
        sendMessage(Library.getAuthDenied());
        close();
    }

    void msgFormatError(String msg) {
        sendMessage(Library.getMsgFormatError(msg));
        close();
    }


    public void incrementCount() {
        count++;
    }
}
