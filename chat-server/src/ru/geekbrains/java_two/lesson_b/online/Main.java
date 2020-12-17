package ru.geekbrains.java_two.lesson_b.online;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;

public class Main {

    public interface Bull extends Animal {
        void voice();
        default void walk() {
            System.out.println("walks on hooves");
        }
    }

    public interface Human extends Animal {
        default void walk() {
            System.out.println("walks on legs");
        }
        void talk();
    }

    public interface Minotaur extends Bull, Human {
        void fightTesseus();

        @Override
        default void walk() {
            Human.super.walk();
        }
    }

    public static class Cat implements Animal {
        String name;
        @Override
        public void breathe() {
            System.out.println(name + " breathes");
        }

        @Override
        public void look() {
            System.out.println("cat looks");
        }
    }

    public static class Beast implements Minotaur {

        @Override
        public void voice() {

        }

        @Override
        public void talk() {

        }

        @Override
        public void walk() {
            System.out.println("beast is beast i fucking fly");
        }

        @Override
        public void fightTesseus() {

        }

        @Override
        public void breathe() {

        }

        @Override
        public void look() {

        }
    }

    private static void method1() {
        ArithmeticException a = new ArithmeticException("my own / by zero");
        System.out.println(a.getMessage());
        System.out.println(Arrays.toString(a.getStackTrace()));
        throw a;
    }

    private static int div(int a, int b) {
        return a / b;
    }

    private static class IOWorker implements Closeable {
        IOWorker() {
            System.out.println("Object created");
        }

        void open() throws FileNotFoundException {
            System.out.println("i search for a file");
            if (false) {
                throw new FileNotFoundException("File not found");
            }
            System.out.println("opened a file");
        }

        void read() throws SQLException {
            System.out.println("i try to read from a file");
            if (true) {
                throw new SQLException("File is corrupted");
            }
            System.out.println("read from file");
        }

        public void close() throws IOException {
            System.out.println("close file");
            throw new EOFException("something went wrong");
        }
    }

    public static void main(String[] args) {
        //exceptions

        try (IOWorker w = new IOWorker()) {
            w.open();
            w.read();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

        //anonymous classes
        class MouseListenerImpl implements MouseListener {
            @Override
            public void mouseClicked() {
                System.out.println("Here we click!");
            }
        }
        MouseListenerImpl m = new MouseListenerImpl();

        addMouseListener(m);
        addMouseListener(new MouseListenerImpl());
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked() {
                System.out.println("Here we click!");
            }
        });
        addMouseListener(() -> System.out.println("Here we click!") );
    }

    public static void addMouseListener(MouseListener l) {
        l.mouseClicked();
    }

    @FunctionalInterface
    public interface MouseListener {
        void mouseClicked();
    }
}
