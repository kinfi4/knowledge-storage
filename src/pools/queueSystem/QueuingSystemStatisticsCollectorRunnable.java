package pools.queueSystem;

public class QueuingSystemStatisticsCollectorRunnable implements Runnable {
    private final QueuingSystemManager manager;
    private int sumQueuesLengths;
    private int countQueueLengthsCalculations;

    QueuingSystemStatisticsCollectorRunnable(QueuingSystemManager manager) {
        this.manager = manager;
        this.sumQueuesLengths = 0;
        this.countQueueLengthsCalculations = 0;
    }

    public void run() {
        while(!this.manager.queueIsClosed) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}

            this.sumQueuesLengths += this.manager.getCurrentQueueLength();
            this.countQueueLengthsCalculations += 1;
        }
    }

    public double getAverageQueueLength() {
        return (double) this.sumQueuesLengths / this.countQueueLengthsCalculations;
    }
}
