import javax.swing.*;

public class Bounce {
    public static void main(String[] args) {
//        firstTask();
//        secondTask();
//        thirdTask();
//        fourthTask();
//        fifthTask();
//        fifthTaskSync();
        sixthTask();
        sixthTaskSync();
    }

    public static void firstTask() {
        BounceFrame frame = new BounceFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void secondTask() {
        BounceFrame frame = new BounceFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.canvas.holesPosition.add(new Integer[] {300, 200});
        frame.canvas.holesPosition.add(new Integer[] {200, 100});
        frame.canvas.holesPosition.add(new Integer[] {100, 250});
    }

    public static void thirdTask() {
        BounceFrame frame = new BounceFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void fourthTask() {
        BounceFrame frame = new BounceFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.canvas.holesPosition.add(new Integer[] {100, 250});
    }

    public static void fifthTask() {
        var dashThread = new Thread(() -> _printSymbols("-"));
        var lineThread = new Thread(() -> _printSymbols("|"));

        dashThread.start();
        lineThread.start();
    }

    public static void fifthTaskSync() {
        Sync syncController = new Sync("-");

        SyncPrintThread thread1 = new SyncPrintThread("-", syncController);
        SyncPrintThread thread2 = new SyncPrintThread("|", syncController);

        thread1.start();
        thread2.start();
    }

    public static void sixthTask() {
        Counter counter = new Counter();

        Thread incrementThread = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                counter.increment();
            }
        });

        Thread decrementThread = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                counter.decrement();
            }
        });

        incrementThread.start();
        decrementThread.start();

        try {
            incrementThread.join();
            decrementThread.join();
        } catch (InterruptedException ignored) {}

        System.out.println("The final value of counter is: " + counter.getValue());
    }

    public static void sixthTaskSync() {
        Counter counter = new Counter();

        Thread incrementThread = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
//                counter.incrementSync();
                counter.incrementSyncBlock();
//                counter.incrementAtomic();
            }
        });

        Thread decrementThread = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
//                counter.decrementSync();
                counter.decrementSyncBlock();
//                counter.decrementAtomic();
            }
        });

        incrementThread.start();
        decrementThread.start();

        try {
            incrementThread.join();
            decrementThread.join();
        } catch (InterruptedException ignored) {}

        System.out.println("The final value of counter is: " + counter.getValue());
//        System.out.println("The final value of counter is: " + counter.atomicValue.get());
    }

    private static void _printSymbols(String symbol) {
        for (int k = 0; k < 100; k++) {
            for (int i = 0; i < 100; i++) {
                System.out.print(symbol);
            }

            System.out.println();
        }
    }
}