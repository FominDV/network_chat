package homework4_task1;

import static java.lang.Thread.sleep;

public class Task1 {
    public static void main(String[] args) {
        startThreeThreads();
    }

    static void startThreeThreads() {
        for (int i = 0; i < 3; i++) getThread(i).start();
    }

    static Thread getThread(int number) {
        return new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    synchronized (lock) {
                        while (flagOfThread != number)
                            lock.wait();
                        System.out.print((char) ('A' + number));
                        flagOfThread= (byte) (++flagOfThread % 3);
                        sleep(300);
                        lock.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static byte flagOfThread = 0;
    static Object lock = new Object();
}
