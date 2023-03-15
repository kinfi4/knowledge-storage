import java.util.Random;
import java.util.stream.IntStream;

public class Producer implements Runnable {
    private final Buffer drop;

    public Producer(Buffer drop) {
        this.drop = drop;
    }

    public void run() {
        int[] range = IntStream.iterate(1, n -> n + 1).limit(100).toArray();
        Random random = new Random();

        for (int i : range) {
            drop.put(i);

            try {
                Thread.sleep(random.nextInt(50));
            } catch (InterruptedException ignored) {}
        }

        drop.put(-1);
    }
}
