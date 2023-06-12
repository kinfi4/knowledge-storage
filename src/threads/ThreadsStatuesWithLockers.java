package threads;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadsStatuesWithLockers {
    private static final ReentrantLock locker = new ReentrantLock();
    private static final Condition condition = locker.newCondition();
    private static final AtomicBoolean stopEverything = new AtomicBoolean(false);
    private static boolean state = false;

    public static void main(String[] args) throws InterruptedException {
        Thread setState = new Thread(() -> {
            while(!stopEverything.get()) {
                locker.lock();
                try {
                    state = !state;
                    System.out.println("\nState changed to: " + state);
                    condition.signalAll();
                } finally {
                    locker.unlock();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        });

        Thread readState = new Thread(() -> {
            int[] randomInts = new int[50];

            Random rand = new Random();
            for (int i = 0; i < 50; i++) {
                randomInts[i] = rand.nextInt();
            }

            int curIndex = 0;
            while(!stopEverything.get()) {
                locker.lock();

                while(!state) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {}
                }

                System.out.print("| " + curIndex + " |");
                locker.unlock();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}

                curIndex += 1;
                if (curIndex == 50) {
                    stopEverything.set(true);
                }
            }
        });

        setState.start();
        readState.start();
    }
}
