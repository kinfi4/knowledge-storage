import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

public class QueuingSystemCallable implements Callable<double[]> {
    private boolean withObservation;

    QueuingSystemCallable() {
        this.withObservation = false;
    }
    QueuingSystemCallable(boolean withObservation) {
        this.withObservation = withObservation;
    }

    public double[] call() {

        QueuingSystemManager manager = new QueuingSystemManager();
        QueuingSystemStatisticsCollectorThread statisticsCollectorThread = new QueuingSystemStatisticsCollectorThread(manager);
        QueuingSystemProducerThread producerThread = new QueuingSystemProducerThread(manager);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < 5; i++) {
            Thread thread = new QueuingSystemConsumerThread(manager);
            executor.execute(thread);
        }

        if(withObservation) {
            QueuingSystemObserverThread observer = new QueuingSystemObserverThread(manager);
            executor.execute(observer);
        }

        executor.execute(producerThread);
        executor.execute(statisticsCollectorThread);

        executor.shutdown();

        System.out.println("QUEUING SYSTEM HAS STARTED...");

        try {
            boolean ok = executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("SOMETHING WENT WRONG");
            throw new RuntimeException();
        }

//        System.out.println("THE PERCENTAGE OF REJECTED MESSAGES IS: " + manager.calculateRejectedPercentage());
//        System.out.println("THE AVERAGE NUMBER OF MESSAGES IN THE QUEUE IS: " + statisticsCollectorThread.getAverageQueueLength());

        return new double[] {manager.calculateRejectedPercentage(), statisticsCollectorThread.getAverageQueueLength()};
    }
}
