package ru.geekbrains.java_two.lesson_f.online;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) {
        System.out.println("server starting");
        try (ServerSocket server = new ServerSocket(8189);
             Socket currentClient = server.accept()) {
            System.out.println("client connected");
            DataInputStream din = new DataInputStream(currentClient.getInputStream());
            DataOutputStream dout = new DataOutputStream(currentClient.getOutputStream());
            String b = din.readUTF();
            System.out.println("client sent us: " + b);
            dout.writeUTF("Echo: " + b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
