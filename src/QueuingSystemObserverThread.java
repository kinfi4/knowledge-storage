public class QueuingSystemObserverThread extends Thread {
    private QueuingSystemManager manager;
    QueuingSystemObserverThread(QueuingSystemManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        while(!manager.queueIsClosed) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

            System.out.println("Queue length: " + this.manager.getCurrentQueueLength() + ", rejected percentage: " + this.manager.calculateRejectedPercentage());
        }
    }
}
