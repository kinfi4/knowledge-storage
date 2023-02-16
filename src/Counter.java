import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private int value;
    private final Object blockingObj = new Object();

    public AtomicInteger atomicValue;

    public Counter() {
        this.value = 0;
        this.atomicValue = new AtomicInteger(0);
    }

    public void increment() {
        this.value++;
    }

    public void decrement() {
        this.value--;
    }

    public synchronized void incrementSync() {
        this.value++;
    }

    public void incrementSyncBlock() {
        synchronized(blockingObj) {
            this.value++;
        }
    }

    public synchronized void decrementSync() {
        this.value--;
    }

    public void decrementSyncBlock() {
        synchronized(blockingObj) {
            this.value--;
        }
    }

    public void incrementAtomic() {
        this.atomicValue.incrementAndGet();
    }

    public void decrementAtomic() {
        this.atomicValue.decrementAndGet();
    }

    public int getValue() {
        return value;
    }
}
