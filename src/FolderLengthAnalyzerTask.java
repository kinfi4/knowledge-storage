import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.HashMap;

public class FolderLengthAnalyzerTask extends RecursiveTask<HashMap<Integer, Integer>> {
    private final List<String> filePaths;
    private final List<String> subFolders;

    FolderLengthAnalyzerTask(String folderPath) {

        File directory = new File(folderPath);
        File[] filesAndFolders = directory.listFiles();

        this.filePaths = new ArrayList<>();
        this.subFolders = new ArrayList<>();

        if(filesAndFolders == null) {
            return;
        }

        for (File file : filesAndFolders) {
            if (file.isDirectory()) {
                this.subFolders.add(file.getAbsolutePath());
            } else {
//                System.out.println("READING FILE: " + file.getAbsolutePath());

                this.filePaths.add(file.getAbsolutePath());
            }
        }

    }

    @Override
    protected HashMap<Integer, Integer> compute() {
        List<RecursiveTask<HashMap<Integer, Integer>>> tasks = new ArrayList<>();

        for(String folderPath : this.subFolders) {
            FolderLengthAnalyzerTask task = new FolderLengthAnalyzerTask(folderPath);
            tasks.add(task);

            task.fork();
        }

        for(String filePath : this.filePaths) {
            FileLengthAnalyzerTask task = new FileLengthAnalyzerTask(filePath);
            tasks.add(task);

            task.fork();
        }

        HashMap<Integer, Integer> finalResult = new HashMap<>();

        for(RecursiveTask<HashMap<Integer, Integer>> task : tasks) {
            finalResult = this.mergeResults(finalResult, task.join());
        }

        return finalResult;
    }

    private HashMap<Integer, Integer> mergeResults(HashMap<Integer, Integer> firstMap, HashMap<Integer, Integer> secondMap) {
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
}
