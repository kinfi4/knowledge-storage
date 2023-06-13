package threads;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class QueueSystem {
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition bufferIsFull = lock.newCondition();
    public static Condition bufferIsEmpty = lock.newCondition();
    public static int totalOperations = 0;


    public static void main(String[] args) {
        LinkedList<Object> buffer = new LinkedList<>();
        int bufferSize = 10;

        Thread A = new Thread(() -> {
            while(totalOperations < 1000) {
                lock.lock();
                try {
                    while (buffer.size() == bufferSize) {
                        try {
                            bufferIsFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Object obj = new Object();
                    buffer.add(obj);

                    System.out.println("Thread A added object " + obj);

                    bufferIsEmpty.signalAll();
                    totalOperations++;

                    System.out.println(totalOperations);
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread P = new Thread(() -> {
            while(totalOperations < 1000) {
                lock.lock();
                try {
                    while (buffer.size() == bufferSize) {
                        try {
                            bufferIsFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Object obj = new Object();
                    buffer.add(obj);

                    System.out.println("Thread P added object " + obj);

                    bufferIsEmpty.signalAll();
                    totalOperations++;
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread D = new Thread(() -> {
            while(totalOperations < 1000) {
                lock.lock();

                try{
                    while (buffer.isEmpty()) {
                        try {
                            bufferIsEmpty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Object obj = buffer.remove();
                    System.out.println("Thread D removed object " + obj);

                    bufferIsFull.signalAll();

                    totalOperations++;
                } finally {
                    lock.unlock();
                }
            }
        });

        A.start();
        P.start();
        D.start();
    }
}
