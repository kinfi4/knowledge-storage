import java.util.Random;

public class QueuingSystemProducerThread extends Thread {
    private final QueuingSystemManager manager;

    QueuingSystemProducerThread(QueuingSystemManager manager) {
        this.manager = manager;
    }

    @Override
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
