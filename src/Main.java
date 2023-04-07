import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        task1();
//        task2();
//        task3();
//        task4();
    }

    public static void task1() throws IOException {
        Instant start1 = Instant.now();
        _syncWordsLengthCount("data-folder/programming");
        Instant end1 = Instant.now();
        Duration timeElapsed1 = Duration.between(start1, end1);
        System.out.println("SYNC TIME TOOK: " + timeElapsed1.toMillis());

        System.out.println("=====================================================");

        Instant start = Instant.now();
        _parallelWordsLengthCount("data-folder/programming");
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("PARALLEL TIME TOOK: " + timeElapsed.toMillis());
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
        FolderFileSearchAnalyzerTask task = new FolderFileSearchAnalyzerTask("./data-folder/programming", wordToFind);

        ArrayList<String> filePaths = pool.invoke(task);
        pool.shutdown();

        System.out.println("Word '" + wordToFind + "' was found in files: " + filePaths);
    }
    public static void _syncWordsLengthCount(String dirPath) throws IOException {
        var paths = listFilesRecursively(dirPath);
        double k=0, l = 400;
        HashMap<Integer, Integer> map = new HashMap<>();
        ArrayList<ArrayList<String>> listWordsList = new ArrayList<>();
        ArrayList<String> contents = new ArrayList<>();

        for (String filePath : paths) {
            Scanner scanner = new Scanner(Paths.get(filePath), StandardCharsets.UTF_8);
            String content = scanner.useDelimiter("\\A").next();
            scanner.close();

            contents.add(content);
        }

        for(String cont : contents) {
            ArrayList<String> wordsList = new ArrayList<>();
            var list = cont.split("\\s+");
            for (int i = 0; i < list.length; i++) {
                wordsList.add(list[i]);
            }

            listWordsList.add(wordsList);
        }

        for (int i = 0; i < listWordsList.size(); i++) {
            ArrayList<String> list = listWordsList.get(i);

            for(String word : list) {for (int j=0; j < l; j++){k = k -3;}
                if (map.containsKey(word.length())) {
                    int wordsLengthsCount = map.get(word.length());
                    map.put(word.length(), wordsLengthsCount + 1);
                } else {
                    map.put(word.length(), 1);
                }
            }
        }

        _showMapStats(map);
    }
    public static void _parallelWordsLengthCount(String dirPath) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        FolderLengthAnalyzerTask task = new FolderLengthAnalyzerTask(dirPath);

        var result = pool.invoke(task);

        pool.shutdown();
        _showMapStats(result);
    }
    public static ArrayList<String> listFilesRecursively(String dirPath) {
        ArrayList<String> filePaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(dirPath))) {
            paths.filter(Files::isRegularFile).forEach((filePath) -> filePaths.add(filePath.toFile().getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePaths;
    }
    public static void _showMapStats(HashMap<Integer, Integer> map) {
        double totalNumberOfWords = 0;
        double totalWordsLength = 0;

        for(int lengthKey : map.keySet()) {
            totalNumberOfWords += map.get(lengthKey);
            totalWordsLength += map.get(lengthKey) * lengthKey;
        }

        double meadWordsLength = totalWordsLength / totalNumberOfWords;
        double dispersion = 0;
        for(int lengthKey : map.keySet()) {
            for (int i = 0; i < map.get(lengthKey); i++) {
                dispersion += Math.pow(lengthKey - meadWordsLength, 2);
            }
        }

        dispersion /= totalNumberOfWords;

        System.out.println("TOTAL NUMBER OF WORDS: " + totalNumberOfWords);
        System.out.println("MEAN WORDS LENGTH: " + meadWordsLength);
        System.out.println("DISPERSION OF WORDS LENGTHS: " + dispersion);
        System.out.println("STD OF WORDS LENGTHS: " + Math.sqrt(dispersion));
    }
}
