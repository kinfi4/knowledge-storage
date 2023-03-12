import java.util.Random;

public class Consumer implements Runnable {
    private final Item drop;

    public Consumer(Item drop) {
        this.drop = drop;
    }

    public void run() {
        Random random = new Random();
        int message = drop.take();

        while(message != -1) {
            try {
                System.out.println("MESSAGE RECEIVED: " + message);
                message = drop.take();

                Thread.sleep(random.nextInt(50));
            } catch (InterruptedException ignored) {}

        }
    }
}

