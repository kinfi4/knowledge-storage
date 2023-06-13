package forkJoin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

class WordLengthCalculator extends RecursiveTask<long[]> {
    private final ArrayList<String> list;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 5;

    public WordLengthCalculator(ArrayList<String> list, int start, int end) {
        this.list = list;
        this.start = start;
        this.end = end;
    }

    @Override
    protected long[] compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return calculate();
        } else {
            WordLengthCalculator firstSubTask = new WordLengthCalculator(list, start, start + length/2);
            WordLengthCalculator secondSubTask = new WordLengthCalculator(list, start + length/2, end);
            firstSubTask.fork();
            long[] secondSubResult = secondSubTask.compute();
            long[] firstSubResult = firstSubTask.join();

            return new long[] {firstSubResult[0] + secondSubResult[0], firstSubResult[1] + secondSubResult[1]};
        }
    }

    private long[] calculate() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += list.get(i).length();
        }
        return new long[] {sum, end-start};
    }
}

public class WordsAvgLengthFromFileOrFromArray {
    public static void main(String[] args) {
        double average = getAverageWordLengthFromArray();
//        double average = getAverageWordLengthFromFile("src/forkJoin/words.txt");
        System.out.println("Average word length: " + average);
    }

    public static double getAverageWordLengthFromArray() {
        String[] words = {"Hello", "World", "in", "Java", "Fork", "Join", "Parallelism", "is", "easy", "to", "use"};
        ArrayList<String> list = new ArrayList<>(Arrays.asList(words));

        ForkJoinPool pool = new ForkJoinPool();
        WordLengthCalculator task = new WordLengthCalculator(list, 0, list.size());
        long[] result = pool.invoke(task);
        double average = 1.0*result[0]/result[1];

        double validate = 0;
        for (String word : words) {
            validate += word.length();
        }

        System.out.println("Validate: " + validate / words.length);

        return average;
    }

    public static double getAverageWordLengthFromFile(String filePath) {
        ArrayList<String> list = new ArrayList<>();

        try {
            File file = new File("filePath");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                list.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ForkJoinPool pool = new ForkJoinPool();
        WordLengthCalculator task = new WordLengthCalculator(list, 0, list.size());
        long[] result = pool.invoke(task);
        double average = 1.0*result[0]/result[1];

        return average;
    }
}
