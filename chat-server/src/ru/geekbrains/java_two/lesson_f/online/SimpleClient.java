package ru.geekbrains.java_two.lesson_f.online;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SimpleClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 8189)) {
            System.out.println("connected to server");
            DataInputStream din = new DataInputStream(socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF("Hello, server");
            String b = din.readUTF();
            System.out.println(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
