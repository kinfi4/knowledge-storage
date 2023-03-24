import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderLengthAnalyzerTask extends RecursiveTask<int[]> {
    private final String folderPath;
    private List<String> filePaths;
    private List<String> subFolders;

    FolderLengthAnalyzerTask(String folderPath) {
        this.folderPath = folderPath;

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
                System.out.println("READING FILE: " + file.getAbsolutePath());

                this.filePaths.add(file.getAbsolutePath());
            }
        }

    }

    @Override
    protected int[] compute() {
        List<RecursiveTask<int[]>> tasks = new ArrayList<>();

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

        int[] finalResult = new int[0];

        for(RecursiveTask<int[]> task : tasks) {
            finalResult = this.mergeResults(finalResult, task.join());
        }

        return finalResult;
    }

    private int[] mergeResults(int[] firstArray, int[] secondArray) {
        int[] result = new int[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, result, 0, firstArray.length);
        System.arraycopy(secondArray, 0, result, firstArray.length, secondArray.length);
        return result;
    }
}
