package pools.queueSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class QueuingSystem {

    public static void main(String[] args) {
        QueuingSystemManager manager = new QueuingSystemManager();

        QueuingSystemStatisticsCollectorRunnable statisticsCollectorRunnable = new QueuingSystemStatisticsCollectorRunnable(manager);
        QueuingSystemProducerRunnable producerRunnable = new QueuingSystemProducerRunnable(manager);

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < 5; i++) {
            Runnable consumer = new QueuingSystemConsumerRunnable(manager);
            executor.execute(consumer);
        }

        executor.execute(producerRunnable);
        executor.execute(statisticsCollectorRunnable);

        executor.shutdown();

        System.out.println("QUEUING SYSTEM HAS STARTED...");

        try {
            boolean ok = executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("SOMETHING WENT WRONG");
            throw new RuntimeException();
        }

        System.out.println("THE PERCENTAGE OF REJECTED MESSAGES IS: " + manager.calculateRejectedPercentage());
        System.out.println("THE AVERAGE NUMBER OF MESSAGES IN THE QUEUE IS: " + statisticsCollectorRunnable.getAverageQueueLength());
    }
}
