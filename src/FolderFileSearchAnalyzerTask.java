import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;


public class FolderFileSearchAnalyzerTask extends RecursiveTask<ArrayList<String>> {
    private final String folderPath;
    private List<String> filePaths;
    private List<String> subFolders;
    private final String wordToFind;

    FolderFileSearchAnalyzerTask(String folderPath, String wordToFind) {
        this.folderPath = folderPath;
        this.wordToFind = wordToFind;

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
    protected ArrayList<String> compute() {
        List<FolderFileSearchAnalyzerTask> subFoldersTasks = new ArrayList<>();
        List<FileSearchTask> filesTasks = new ArrayList<>();

        for(String folderPath : this.subFolders) {
            FolderFileSearchAnalyzerTask task = new FolderFileSearchAnalyzerTask(folderPath, this.wordToFind);
            subFoldersTasks.add(task);

            task.fork();
        }

        for(String filePath : this.filePaths) {
            FileSearchTask task = new FileSearchTask(filePath, this.wordToFind);
            filesTasks.add(task);

            task.fork();
        }

        ArrayList<String> results = new ArrayList<>();

        for (FileSearchTask task : filesTasks) {
            if(task.join()) {
                results.add(task.filePath);
            }
        }

        for(FolderFileSearchAnalyzerTask task : subFoldersTasks) {
            results.addAll(task.join());
        }

        return results;
    }
}
