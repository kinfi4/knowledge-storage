import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
//        task1();
        task2();
//        task3();
//        task4();
    }

    public static void task1() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        FolderLengthAnalyzerTask task = new FolderLengthAnalyzerTask("./data-folder");

        int[] result = pool.invoke(task);

        pool.shutdown();
        _showArrayStats(result);
    }

    public static void task2() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        GradesBook book = new GradesBook();

        Instant start = Instant.now();

        TeacherTask task = new TeacherTask(book.gradesBook);
        book.gradesBook = pool.invoke(task);
        pool.shutdown();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("FORK JOIN TIME NEEDED: " + timeElapsed.toMillis());
        System.out.println("FORK JOIN CHECKING IF ALL MARKS PLACED: " + book.allMarksPlaced());

        Instant start2 = Instant.now();

        GradesBook book2 = new GradesBook();

        ArrayList<Thread> threads = new ArrayList<>();
        int batchSize = book2.NUMBER_OF_GROUPS / 10;
        for (int i = 0; i < book2.NUMBER_OF_GROUPS; i += batchSize) {
            int upperIdx = Math.min(i + batchSize, book2.NUMBER_OF_GROUPS);
            ArrayList<Integer> groupsIndexes = new ArrayList<>();
            for (int j = i; j < upperIdx; j++) {
                groupsIndexes.add(j);
            }

            Thread tr = new TeacherThread(book2, groupsIndexes);
            threads.add(tr);
        }

        for (Thread tr : threads) {
            tr.start();
        }

        for (Thread tr : threads) {
            try {
                tr.join();
            } catch (InterruptedException ignored) {}
        }

        Instant end2 = Instant.now();
        Duration timeElapsed2 = Duration.between(start2, end2);
        System.out.println("THREAD TIME NEEDED: " + timeElapsed2.toMillis());
        System.out.println("THREAD CHECKING IF ALL MARKS PLACED: " + book2.allMarksPlaced());
    }

    public static void task3() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        FolderCommonWordsTask task = new FolderCommonWordsTask("./common-words");

        Set<String> words = pool.invoke(task);
        pool.shutdown();

        System.out.println("Common words for all these files are: " + words.toString());
    }

    public static void task4() {
        String wordToFind = "python";

        ForkJoinPool pool = ForkJoinPool.commonPool();
        FolderFileSearchAnalyzerTask task = new FolderFileSearchAnalyzerTask("data-folder", wordToFind);

        ArrayList<String> filePaths = pool.invoke(task);
        pool.shutdown();

        System.out.println("Word '" + wordToFind + "' was found in files: " + filePaths);
    }

    public static void _showArrayStats(int[] arr) {
        // Mean
        double sum = 0;
        for (int i : arr) {
            sum += i;
        }
        double mean = sum / arr.length;
        System.out.println("Mean: " + mean);

        // Median
        Arrays.sort(arr);
        double median;
        if (arr.length % 2 == 0) {
            median = (arr[arr.length/2] + arr[arr.length/2 - 1]) / 2.0;
        } else {
            median = arr[arr.length/2];
        }
        System.out.println("Median: " + median);

        // Mode
        int mode = arr[0];
        int maxCount = 0;
        for (int i : arr) {
            int count = 0;
            for (int j : arr) {
                if (i == j) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                mode = i;
            }
        }
        System.out.println("Mode: " + mode);

        // Standard Deviation
        double sd = 0;
        for (int i : arr) {
            sd += Math.pow(i - mean, 2);
        }
        sd = Math.sqrt(sd / arr.length);
        System.out.println("Standard Deviation: " + sd);
    }

}
