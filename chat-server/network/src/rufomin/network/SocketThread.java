package rufomin.network;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Base64;

public class SocketThread extends Thread {
    private final static byte CODE_NUMBER = 77;
    private final SocketThreadListener listener;
    private final Socket socket;
    private DataOutputStream out;
    DataInputStream in;
    private final static Base64.Encoder ENCODER = Base64.getEncoder();
    private final static Base64.Decoder DECODER = Base64.getDecoder();

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
                msg = decoding(msg);
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
        try {
            msg = encoding(msg);
            out.writeUTF(msg);
            out.flush();
            return true;
        } catch (IOException e) {
            listener.onSocketException(this, e);
            close();
            return false;
        }
    }

    private String encoding(String msg) {
        byte[] code = ENCODER.encode(msg.getBytes());
        codingByteArray(code);
        return new String(code);
    }

    private String decoding(String msg) {
        byte[] code=msg.getBytes();
        codingByteArray(code);
        code=DECODER.decode(code);
        return new String(code);
    }
private void codingByteArray(byte[] code){
    for (int i = 0; i < code.length; i++) {
        code[i] ^= CODE_NUMBER;
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
}
