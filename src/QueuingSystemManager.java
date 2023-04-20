import java.util.ArrayDeque;
import java.util.Queue;

public class QueuingSystemManager {
    private final int MAX_QUEUE_SIZE = 100;
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

    public double calculateRejectedPercentage() {
        return (double) this.countRejected / (this.countRejected + this.countConsumed);
    }

    public synchronized int getCurrentQueueLength () {
        return this.queue.size();
    }
}
