import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class AverageWordLength extends RecursiveTask<Double> {
    private ArrayList<String> list;
    private int start;
    private int end;

    public int totalLength;
    public int totalCount;

    public AverageWordLength(ArrayList<String> list, int start, int end) {
        this.list = list;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Double compute() {
        if (end - start <= 1000) { // якщо об'єм даних менше 1000, обчислюємо середнє значення без ділення на довжину списку
            for (int i = start; i < end; i++) {
                String word = list.get(i);
                totalLength += word.length();
                totalCount++;
            }
        } else { // розділяємо дані на менші частини та запускаємо обчислення в окремих потоках
            int mid = (start + end) / 2;
            AverageWordLength left = new AverageWordLength(list, start, mid);
            AverageWordLength right = new AverageWordLength(list, mid, end);
            left.fork();

            totalLength = left.totalLength + right.totalLength;
            totalCount = left.totalCount + right.totalCount;
        }

        return (double) totalLength / totalCount;
    }

    public static double defineAverageWordLength(ArrayList<String> list) {
        ForkJoinPool pool = new ForkJoinPool(10);
        AverageWordLength task = new AverageWordLength(list, 0, list.size());
        return pool.invoke(task);
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("orange");
        list.add("grape");
        list.add("pineapple");
        list.add("watermelon");
        list.add("kiwi");
        list.add("strawberry");
        list.add("mango");
        list.add("peach");

        double average = defineAverageWordLength(list);
        System.out.println("Average word length: " + average);
    }
}
