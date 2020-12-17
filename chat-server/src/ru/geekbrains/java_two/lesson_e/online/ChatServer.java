package ru.geekbrains.java_two.lesson_e.online;

public class ChatServer {
    ServerSocketThread thread;

    public void start(int port) {
        if (thread != null && thread.isAlive()) {
            System.out.println("Server already started");
        } else {
            thread = new ServerSocketThread("Thread of server", 8189, 2000);
        }
    }

    public void stop() {
        if (thread == null || !thread.isAlive()) {
            System.out.println("Server is not running");
        } else {
            thread.interrupt();
        }
    }
}
