import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    static Random rand = new Random();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int N = 500;
        int[] array = new int[50000];


        Arrays.setAll(array, i -> Main.rand.nextInt(0, 100));
        ExecutorService executor = Executors.newFixedThreadPool(4);
        ArrayList<SumTask> tasks = new ArrayList<>();

        int batch = array.length / N;
        for (int i = 0; i < N; i++) {
            int start = i * batch;
            int end = (i + 1) * batch;
            if (i == N-1) {
                end = array.length;
            }

            SumTask task = new SumTask(array, start, end);
            tasks.add(task);
        }

        var sums = executor.invokeAll(tasks);

        executor.shutdown();

        long total = 0;

        for (Future<Long> result : sums) {
            total += result.get();
        }

        System.out.println("TOTAL SUM IS: " + total);

    }
}

class SumTask implements Callable<Long> {
    private final int[] arrayToSum;
    private final int start;
    private final int end;
    private long totalSum;

    public SumTask(int[] arrayToSum, int start, int end) {
        this.arrayToSum = arrayToSum;
        this.start = start;
        this.end = end;
    }

    @Override
    public Long call() {
        for (int i = start; i < end; i++) {
            totalSum += arrayToSum[i];
        }
        return this.totalSum;
    }
}

