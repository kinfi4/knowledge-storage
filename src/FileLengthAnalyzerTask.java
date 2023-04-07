import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;

public class FileLengthAnalyzerTask extends RecursiveTask<HashMap<Integer, Integer>> {
    public final String filePath;
    private List<String> wordsList;
    private int start;
    private int end;
    private final boolean generateFromFile;

    FileLengthAnalyzerTask(String filePath) {
        this.filePath = filePath;
        this.generateFromFile = true;
    }

    FileLengthAnalyzerTask(String filePath, List<String> wordsList, int start, int end) {
        this.filePath = filePath;
        this.wordsList = wordsList;
        this.start = start;
        this.end = end;
        this.generateFromFile = false;
    }

    @Override
    protected HashMap<Integer, Integer> compute() {
        if(this.generateFromFile) {
            this._initWordsList();
        }

//        return this.dumbCount();

        if(end - start < 1_000_000) {
            return this.getWordsLength();
        }

        int middleIdx = (this.end + this.start) / 2;

        FileLengthAnalyzerTask leftTask = new FileLengthAnalyzerTask(this.filePath, this.wordsList, this.start, middleIdx);
        leftTask.fork();

        FileLengthAnalyzerTask rightTask = new FileLengthAnalyzerTask(this.filePath, this.wordsList, middleIdx, this.end);

//        System.out.println("Splitting file " + this.filePath + " into two sections...");

        return this.updateFirstWithSecond(rightTask.compute(), leftTask.join());
    }

    private HashMap<Integer, Integer> updateFirstWithSecond(HashMap<Integer, Integer> firstMap, HashMap<Integer, Integer> secondMap) {
        for(int lengthKey : secondMap.keySet()) {
            if (firstMap.containsKey(lengthKey)) {
                firstMap.put(lengthKey, firstMap.get(lengthKey) + secondMap.get(lengthKey));
            } else {
                firstMap.put(lengthKey, secondMap.get(lengthKey));
            }
        }

        return firstMap;
    }

    private HashMap<Integer, Integer> getWordsLength() {
//        System.out.println("GETTING LENGTHS: " + this.start + " " + this.end + " " + this.wordsList.size());
        HashMap<Integer, Integer> lengthsMapper = new HashMap<>();

        for (int i = this.start; i < this.end; i++) {
            String word = this.wordsList.get(i);
            int wordLength = word.length();

            if (lengthsMapper.containsKey(wordLength)) {
                lengthsMapper.put(wordLength, lengthsMapper.get(wordLength) + 1);
            } else {
                lengthsMapper.put(wordLength, 1);
            }

        }

        return lengthsMapper;
    }
    private HashMap<Integer, Integer> dumbCount() {
        HashMap<Integer, Integer> map = new HashMap<>();

        try {
            Scanner scanner = new Scanner(Paths.get(filePath), StandardCharsets.UTF_8);
            String content = scanner.useDelimiter("\\A").next();
            scanner.close();

            var words = List.of(content.split("\\s+"));

            for(String word : words) {
                if (map.containsKey(word.length())) {
                    int wordsLengthsCount = map.get(word.length());
                    map.put(word.length(), wordsLengthsCount + 1);
                } else {
                    map.put(word.length(), 1);
                }
            }
        } catch (IOException ignored) {
            System.err.println("COULDN'T READ THE FILE: " + ignored);
            throw new RuntimeException();
        }

        return map;
    }
    private void _initWordsList() {
        try {
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
}
