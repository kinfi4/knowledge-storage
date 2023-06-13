package pools.queueSystem;

import java.util.Random;

public class QueuingSystemProducerRunnable implements Runnable {
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
