import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;

public class FileLengthAnalyzerTask extends RecursiveTask<HashMap<Integer, Integer>> {
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
    protected HashMap<Integer, Integer> compute() {
        if(end - start < 4_000_000) {
            return this.getWordsLength();
        }

        // splitting the task into 2

        int middleIdx = (this.end + this.start) / 2;

        FileLengthAnalyzerTask leftTask = new FileLengthAnalyzerTask(this.filePath, this.wordsList, this.start, middleIdx);
        leftTask.fork();

        FileLengthAnalyzerTask rightTask = new FileLengthAnalyzerTask(this.filePath, this.wordsList, middleIdx, this.end);

        System.out.println("Splitting file " + this.filePath + " into two sections...");

        return this.updateFirstWithSecond(leftTask.join(), rightTask.compute());
    }

    private HashMap<Integer, Integer> updateFirstWithSecond(HashMap<Integer, Integer> firstMap, HashMap<Integer, Integer> secondMap) {
        for(int lengthKey : secondMap.keySet()) {
            if (firstMap.containsKey(lengthKey)) {
                int wordsLengthsCount = firstMap.get(lengthKey);
                firstMap.put(lengthKey, wordsLengthsCount + secondMap.get(lengthKey));
            } else {
                firstMap.put(lengthKey, secondMap.get(lengthKey));
            }
        }

        return firstMap;
    }

    private HashMap<Integer, Integer> getWordsLength() {
//        System.out.println("GETTING LENGTHS: " + this.start + " " + this.end + " " + this.wordsList.size());
        List<String> wordsSubList = this.wordsList.subList(this.start, this.end);

        HashMap<Integer, Integer> lengthsMapper = new HashMap<>();

        for (String word : wordsSubList) {
            if (lengthsMapper.containsKey(word.length())) {
                int wordsLengthsCount = lengthsMapper.get(word.length());
                lengthsMapper.put(word.length(), wordsLengthsCount + 1);
            } else {
                lengthsMapper.put(word.length(), 1);
            }
        }

        return lengthsMapper;
    }
}
