package threads;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class QueueSystemMultipleWorkers {
    public static void main(String[] args) {
        QueuingSystemManager manager = new QueuingSystemManager();

        QueuingSystemProducerRunnable producerRunnable = new QueuingSystemProducerRunnable(manager);

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < 5; i++) {
            Runnable consumer = new QueuingSystemConsumerRunnable(manager);
            executor.execute(consumer);
        }

        executor.execute(producerRunnable);

        executor.shutdown();

        System.out.println("QUEUING SYSTEM HAS STARTED...");

        try {
            boolean ok = executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("SOMETHING WENT WRONG");
            throw new RuntimeException();
        }

        System.out.println("THE PERCENTAGE OF REJECTED MESSAGES IS: " + manager.calculateRejectedPercentage());

    }
}

class QueuingSystemManager {
    private final int MAX_QUEUE_SIZE = 20;
    private int countRejected;
    private int countConsumed;
    private final Queue<Integer> queue;
    public boolean queueIsClosed;

    QueuingSystemManager() {
        this.countConsumed = 0;
        this.countRejected = 0;
        this.queueIsClosed = false;
        this.queue = new ArrayDeque<>();
    }

    public synchronized void push(int item) {
        if(this.queue.size() >= this.MAX_QUEUE_SIZE) {
            this.countRejected += 1;
            return;
        }

        this.queue.add(item);
        notifyAll();
    }

    public synchronized int getNextItem() {
        while(this.queue.size() == 0) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }

        return this.queue.poll();
    }

    public synchronized void countConsumedMessage() {
        this.countConsumed += 1;
    }

    public synchronized double calculateRejectedPercentage() {
        return (double) this.countRejected / (this.countRejected + this.countConsumed);
    }

    public synchronized int getCurrentQueueLength () {
        return this.queue.size();
    }
}

class QueuingSystemProducerRunnable implements Runnable {
    private final QueuingSystemManager manager;

    QueuingSystemProducerRunnable(QueuingSystemManager manager) {
        this.manager = manager;
    }

    public void run() {
        Random random = new Random();
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        while (elapsedTime < 10_000) {
            this.manager.push(random.nextInt(100));

            try {
                Thread.sleep(random.nextInt(15));
            } catch (InterruptedException ignored) {}

            elapsedTime = System.currentTimeMillis() - startTime;
        }

        this.manager.queueIsClosed = true;
    }
}

class QueuingSystemConsumerRunnable implements Runnable {
    private final QueuingSystemManager manager;

    QueuingSystemConsumerRunnable(QueuingSystemManager manager) {
        this.manager = manager;
    }

    public void run() {
        Random random = new Random();

        while(!this.manager.queueIsClosed) {
            int newItem = this.manager.getNextItem();

            // doing some work...

            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException ignored) {}

            this.manager.countConsumedMessage();
        }
    }

}

