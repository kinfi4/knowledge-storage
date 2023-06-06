package pools;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class RunnableTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread name: " + Thread.currentThread().getName());
    }
}

class CallableTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("Thread name: \" + Thread.currentThread().getName();");
        return "Thread name: " + Thread.currentThread().getName();
    }
}

public class SimplePool {
    public static void main(String[] args) throws InterruptedException {
//        createPool();
        createPoolCallable();
    }

    public static void createPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ArrayList<RunnableTask> runnableTasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            runnableTasks.add(new RunnableTask());
        }

        for (RunnableTask runnableTask : runnableTasks) {
            executorService.execute(runnableTask);
        }

        executorService.shutdown();
    }

    public static void createPoolCallable() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ArrayList<CallableTask> callableTasks = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            callableTasks.add(new CallableTask());
        }

        executorService.invokeAll(callableTasks);

        executorService.shutdown();
    }

}
