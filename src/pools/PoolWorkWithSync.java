package pools;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolWorkWithSync {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ArrayList<Runnable> runnableTasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            runnableTasks.add(new IncrementCounter(counter));
        }
        for (int i = 0; i < 10; i++) {
            runnableTasks.add(new DecrementCounter(counter));
        }

        for (Runnable runnableTask : runnableTasks) {
            executorService.execute(runnableTask);
        }

        executorService.shutdown();

        while(true) {
            System.out.println(counter.getCount());
            Thread.sleep(20);
        }

    }
}


class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }
    public synchronized void decrementByTen() {
        count -= 10;
    }
    public synchronized int getCount() {
        return count;
    }
}

class IncrementCounter implements Runnable {
    private final Counter counter;

    IncrementCounter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        while(true) {
            this.counter.increment();
        }
    }
}
class DecrementCounter implements Runnable {
    private final Counter counter;

    DecrementCounter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        while(true) {
            this.counter.decrementByTen();
        }
    }
}
