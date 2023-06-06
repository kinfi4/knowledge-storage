package pools.queueSystem;

import java.util.Random;

public class QueuingSystemConsumerRunnable extends Thread {
    private final QueuingSystemManager manager;

    QueuingSystemConsumerRunnable(QueuingSystemManager manager) {
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
