import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Pattern;

public class FileSearchTask extends RecursiveTask<Boolean> {
    public final String filePath;
    private final List<String> wordsList;
    private final int start;
    private final int end;
    private final String wordToFind;

    FileSearchTask(String filePath, String wordToFind) {
        try {
            this.filePath = filePath;

            Scanner scanner = new Scanner(Paths.get(filePath), StandardCharsets.UTF_8);
            String content = scanner.useDelimiter("\\A").next();
            scanner.close();

            this.wordsList = List.of(content.split("\\s+"));
            this.start = 0;
            this.end = this.wordsList.size();
            this.wordToFind = wordToFind;
        } catch (IOException ignored) {
            System.err.println("COULDN'T READ THE FILE: " + ignored);
            throw new RuntimeException();
        }
    }

    FileSearchTask(String filePath, String wordToFind, List<String> wordsList, int start, int end) {
        this.filePath = filePath;
        this.wordsList = wordsList;
        this.start = start;
        this.end = end;
        this.wordToFind = wordToFind;
    }

    @Override
    protected Boolean compute() {
        if(this.isWordListHandleable()) {
            return this._checkIfWordListContains();
        }

        // splitting the task into 2

        int middleIdx = (this.end + this.start) / 2;

        FileSearchTask leftTask = new FileSearchTask(this.filePath, this.wordToFind, this.wordsList, this.start, middleIdx);
        leftTask.fork();

        FileSearchTask rightTask = new FileSearchTask(this.filePath, this.wordToFind, this.wordsList, middleIdx, this.end);

//        System.out.println("Splitting file " + this.filePath + " into two sections...");

        return leftTask.join() || rightTask.compute();
    }

    private boolean _checkIfWordListContains() {
        Pattern pattern = Pattern.compile("\\p{Punct}");
        for (String str : this.wordsList) {
            String strWithoutPunct = pattern.matcher(str).replaceAll("").toLowerCase();
            if (strWithoutPunct.contains(this.wordToFind.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    private boolean isWordListHandleable() {
        return end - start < 400_000;
    }

}

