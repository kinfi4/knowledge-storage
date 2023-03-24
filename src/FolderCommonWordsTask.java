import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class FolderCommonWordsTask extends RecursiveTask<Set<String>> {
    private final String folderPath;

    private List<String> filePaths;
    private List<String> subFolders;

    FolderCommonWordsTask(String folderPath) {
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
    protected Set<String> compute() {
        List<RecursiveTask<Set<String>>> tasks = new ArrayList<>();

        // counting sub folders
        for(String folderPath : this.subFolders) {
            FolderCommonWordsTask task = new FolderCommonWordsTask(folderPath);
            tasks.add(task);

            task.fork();
        }

        // counting inner files

        for (int i = 0; i < this.filePaths.size(); i += 2) {
            ArrayList<String> filesToPass = new ArrayList<>();
            CommonWordsFileAnalyzerTask task;

            if(i + 1 >= this.filePaths.size()) {
                filesToPass.add(this.filePaths.get(i));
            } else {
                filesToPass.add(this.filePaths.get(i));
                filesToPass.add(this.filePaths.get(i + 1));
            }

            task = new CommonWordsFileAnalyzerTask(filesToPass);

            tasks.add(task);

            task.fork();
        }

        ArrayList<Set<String>> fileWords = new ArrayList<>();

        for (RecursiveTask<Set<String>> task : tasks) {
            fileWords.add(task.join());
        }

        return this.findSetsIntersection(fileWords);
    }

    private Set<String> findSetsIntersection(ArrayList<Set<String>> fileWords) {
        Set<String> result = new HashSet<>(fileWords.get(0));

        for (int i = 1; i < fileWords.size(); i++) {
            result.retainAll(fileWords.get(i));
        }

        return result;
    }
}
