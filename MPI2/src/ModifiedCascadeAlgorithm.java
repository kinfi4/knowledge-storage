
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;


public class Main {
    public static void main(String[] args) {
        int ARRAY_SIZE = 1000;
        double[] array = new double[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = Math.random() + 5;
        }
        ForkJoinPool pool = new ForkJoinPool();
        ModCascadeSum task = new ModCascadeSum(array, 0, ARRAY_SIZE);
        System.out.println("RESULT SUM: " + pool.invoke(task));
        double sum = 0;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sum += array[i];
        }
        System.out.println("VALIDATE SUM: " + sum);
    }
}


class ModCascadeSum extends RecursiveTask<Double> {
    private int THRESH = 100;
    private double[] array;
    private int first;
    private int last;

    public ModCascadeSum(double[] array, int first, int last) {
        this.array = array;
        this.first = first;
        this.last = last;
    }

    @Override
    protected Double compute() {
        if (last - first <= THRESH) {
            double sum = 0;
            for (int i = first; i < last; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            int divide = first + THRESH;
            var leftTask = new ModCascadeSum(array, first, divide);
            var rightTask = new ModCascadeSum(array, divide, last);
            rightTask.fork();

            double first = leftTask.compute();
            double second = rightTask.join();

            return first + second;
        }
    }
}



import java.util.concurrent.RecursiveTask;
        import java.util.concurrent.ForkJoinPool;


public class Main {
    public static void main(String[] args) {
        int ARRAY_SIZE = 1000;
        double[] array = new double[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = Math.random() + 5;
        }
        ForkJoinPool pool = new ForkJoinPool();
        ModCascadeSum task = new ModCascadeSum(array, 0, ARRAY_SIZE);
        System.out.println("RESULT SUM: " + pool.invoke(task));
        double sum = 0;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sum += array[i];
        }
        System.out.println("VALIDATE SUM: " + sum);
    }
}


class ModCascadeSum extends RecursiveTask<Double> {
    private int THRESH = 100;
    private double[] array;
    private int first;
    private int last;

    public ModCascadeSum(double[] array, int first, int last) {
        this.array = array;
        this.first = first;
        this.last = last;
    }

    @Override
    protected Double compute() {
        if (last - first <= THRESH) {
            double sum = 0;
            for (int i = first; i < last; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            int divide = first + THRESH;
            var leftTask = new ModCascadeSum(array, first, divide);
            var rightTask = new ModCascadeSum(array, divide, last);
            rightTask.fork();

            double first = leftTask.compute();
            double second = rightTask.join();

            return first + second;
        }
    }
}

