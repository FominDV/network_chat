package ru.geekbrains.java_two.lesson_e.online;

public class Main {
    private static class MyThread extends Thread {
        public MyThread(String name) {
            super(name);
            start();
        }
        @Override public void run() {
//            throw new RuntimeException("exc");
//            while (!isInterrupted()) {
//                System.out.println("Hello from " + getName());
//            }
            System.out.println("Thread started");
            for (long i = 0; i < 5_000_000_000L; i++) {
                long a = i * 432;
            }
            System.out.println("Thread finished");
        }
    }

    private static int a = 0;
    private static int b = 0;
    private static int c = 0;

    private static Object mon = new Object();

    private synchronized static void incAllVars1() {
        for (int i = 0; i < 1_000_000; i++) {
            a = a + 1;
            b = b + 1;
            c = c + 1;
        }
        String vars = String.format("a=%d, b=%d, c=%d", a, b, c);
        System.out.println(vars);
    }

    private synchronized static void incAllVars2() {
        for (int i = 0; i < 1_000_000; i++) {
            a = a + 1;
            b = b + 1;
            c = c + 1;
        }
        String vars = String.format("a=%d, b=%d, c=%d", a, b, c);
        System.out.println(vars);
    }

    private synchronized static void incAllVars3() {
        for (int i = 0; i < 1_000_000; i++) {
            a = a + 1;
            b = b + 1;
            c = c + 1;
        }
        String vars = String.format("a=%d, b=%d, c=%d", a, b, c);
        System.out.println(vars);
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                incAllVars1();
            }
        };
        new Thread(r, "Thread-0").start();
        new Thread(r, "Thread-1").start();
        new Thread(r, "Thread-2").start();
    }

    private static void threadExamples() {
        //        System.out.println("hello from " + Thread.currentThread().getName());
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("hello from " + Thread.currentThread().getName());
//            }
//        };
//        new Thread(r).start();
        MyThread mt1 = new MyThread("MT1");
//        mt1.interrupt();

        try {
            mt1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("User, here is your photo!");

        //MyThread mt2 = new MyThread("MT2");
    }
}
