import matrix_multiplication.FoxTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        task1();
//        task2();
//        task3();
        task4();
    }

    public static void task1() throws InterruptedException {
        QueuingSystemCallable task = new QueuingSystemCallable();
        double[] results = task.call();

        System.out.println("THE PERCENTAGE OF REJECTED MESSAGES IS: " + results[0]);
        System.out.println("THE AVERAGE NUMBER OF MESSAGES IN THE QUEUE IS: " + results[1]);
    }
    public static void task2() throws InterruptedException, ExecutionException {
        int poolsCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<double[]>> tasks = new ArrayList<>();

        for (int i = 0; i < poolsCount; i++) {
            QueuingSystemCallable task = new QueuingSystemCallable();
            tasks.add(task);
        }

        var results = executor.invokeAll(tasks);
        executor.shutdown();

        double totalAveragesMessages = 0;
        double totalPercentages = 0;
        for(Future<double[]> taskResult : results) {
            double[] result = taskResult.get();
            totalAveragesMessages += result[1];
            totalPercentages += result[0];
        }

        System.out.println("THE PERCENTAGE OF REJECTED MESSAGES IS AFTER " + poolsCount + " PARALLEL RUNS: " + totalPercentages / results.size());
        System.out.println("THE AVERAGE NUMBER OF MESSAGES IN THE QUEUE AFTER " + poolsCount + " PARALLEL RUNS: " + totalAveragesMessages / results.size());
    }
    public static void task3() {
        QueuingSystemCallable task = new QueuingSystemCallable(true);
        double[] results = task.call();

        System.out.println("THE PERCENTAGE OF REJECTED MESSAGES IS: " + results[0]);
        System.out.println("THE AVERAGE NUMBER OF MESSAGES IN THE QUEUE IS: " + results[1]);
    }
    public static void task4() {
        FoxTask task = new FoxTask();
        task.run();
    }
}