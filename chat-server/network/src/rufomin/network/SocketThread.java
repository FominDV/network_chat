package rufomin.network;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SocketThread extends Thread {
    private final SocketThreadListener listener;
    private final Socket socket;
    private DataOutputStream out;
    DataInputStream in;
    private static final byte KEY=77;
    public SocketThread(SocketThreadListener listener, String name, Socket socket) {
        super(name);
        this.socket = socket;
        this.listener = listener;
        start();
    }


    @Override
    public void run() {
        try {
            listener.onSocketStart(this, socket);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            listener.onSocketReady(this, socket);
            while (!isInterrupted()) {
                String msg = in.readUTF();
                msg=coding(msg);
                listener.onReceiveString(this, socket, msg);
            }
        } catch (EOFException | SocketException e) {
        } catch (IOException e) {
            listener.onSocketException(this, e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                listener.onSocketException(this, e);
            }
            listener.onSocketStop(this);
        }
    }


    public synchronized boolean sendMessage(String msg) {
        msg=coding(msg);
        try {
            out.writeUTF(msg);
            out.flush();
            return true;
        } catch (IOException e) {
            listener.onSocketException(this, e);
            close();
            return false;
        }
    }

    public synchronized void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            listener.onSocketException(this, e);
        }
        interrupt();
    }
     private String coding(String message){
        byte[] bytes=message.getBytes(StandardCharsets.UTF_16);
        for(int i=2;i<bytes.length;i++) bytes[i]^=KEY;
        return new String(bytes, StandardCharsets.UTF_16);
    }
}
