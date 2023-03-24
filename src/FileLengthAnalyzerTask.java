import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;

public class FileLengthAnalyzerTask extends RecursiveTask<int[]> {
    private final String filePath;
    private final List<String> wordsList;
    private final int start;
    private final int end;

    FileLengthAnalyzerTask(String filePath) {
        try {
            this.filePath = filePath;

            Scanner scanner = new Scanner(Paths.get(filePath), StandardCharsets.UTF_8);
            String content = scanner.useDelimiter("\\A").next();
            scanner.close();

            this.wordsList = List.of(content.split("\\s+"));
            this.start = 0;
            this.end = this.wordsList.size();
        } catch (IOException ignored) {
            System.err.println("COULDN'T READ THE FILE: " + ignored);
            throw new RuntimeException();
        }
    }

    FileLengthAnalyzerTask(String filePath, List<String> wordsList, int start, int end) {
        this.filePath = filePath;
        this.wordsList = wordsList;
        this.start = start;
        this.end = end;
    }

    @Override
    protected int[] compute() {
        if(this.isWordListHandleable()) {
            return this.getWordsLength();
        }

        // splitting the task into 2

        int middleIdx = (this.end + this.start) / 2;

        FileLengthAnalyzerTask leftTask = new FileLengthAnalyzerTask(this.filePath, this.wordsList, this.start, middleIdx);
        leftTask.fork();

        FileLengthAnalyzerTask rightTask = new FileLengthAnalyzerTask(this.filePath, this.wordsList, middleIdx, this.end);

        System.out.println("Splitting file " + this.filePath + " into two sections...");

        return this.mergeResults(leftTask.join(), rightTask.compute());
    }

    private int[] mergeResults(int[] firstArray, int[] secondArray) {
        int[] result = new int[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, result, 0, firstArray.length);
        System.arraycopy(secondArray, 0, result, firstArray.length, secondArray.length);
        return result;
    }

    private boolean isWordListHandleable() {
        return end - start < 5000;
    }

    private int[] getWordsLength() {
//        System.out.println("GETTING LENGTHS: " + this.start + " " + this.end + " " + this.wordsList.size());
        List<String> wordsSubList = this.wordsList.subList(this.start, this.end);

        int[] lengths = new int[wordsSubList.size()];

        for (int i = 0; i < wordsSubList.size(); i++) {
            lengths[i] = wordsSubList.get(i).length();
        }

        return lengths;
    }
}
