import java.util.Random;

public class QueuingSystemConsumerThread extends Thread {
    private final QueuingSystemManager manager;

    QueuingSystemConsumerThread(QueuingSystemManager manager) {
        this.manager = manager;
    }

    @Override
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
