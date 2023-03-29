import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.HashMap;
import java.util.stream.Stream;

public class FolderLengthAnalyzerTask extends RecursiveTask<HashMap<Integer, Integer>> {
    private final List<String> filePaths;

    FolderLengthAnalyzerTask(String folderPath) {
        this.filePaths = this._listFilesRecursively(folderPath);
    }

    @Override
    protected HashMap<Integer, Integer> compute() {
        List<FileLengthAnalyzerTask> tasks = new ArrayList<>();

        for(String filePath : this.filePaths) {
            FileLengthAnalyzerTask task = new FileLengthAnalyzerTask(filePath);
            task.fork();

            tasks.add(task);
        }

        HashMap<Integer, Integer> finalResult = new HashMap<>();

        for(FileLengthAnalyzerTask task : tasks) {
            this.mergeResults(finalResult, task.join());
        }

        return finalResult;
    }

    private void mergeResults(HashMap<Integer, Integer> firstMap, HashMap<Integer, Integer> secondMap) {
        for(int lengthKey : secondMap.keySet()) {
            if (firstMap.containsKey(lengthKey)) {
                int wordsLengthsCount = firstMap.get(lengthKey);
                firstMap.put(lengthKey, wordsLengthsCount + secondMap.get(lengthKey));
            } else {
                firstMap.put(lengthKey, secondMap.get(lengthKey));
            }
        }
    }

    public ArrayList<String> _listFilesRecursively(String dirPath) {
        ArrayList<String> filePaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(dirPath))) {
            paths.filter(Files::isRegularFile).forEach((filePath) -> filePaths.add(filePath.toFile().getAbsolutePath()));
        } catch (IOException ignored) {}

        return filePaths;
    }
}
