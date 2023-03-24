import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class CommonWordsFileAnalyzerTask extends RecursiveTask<Set<String>> {
    private final ArrayList<String> filePaths;

    CommonWordsFileAnalyzerTask(ArrayList<String> filePaths) {
        this.filePaths = filePaths;
    }

    @Override
    protected Set<String> compute() {
        ArrayList<Set<String>> setsToIntersect = new ArrayList<>();

        for (String filePath : this.filePaths) {
            setsToIntersect.add(this.readFileWordsToSet(filePath));
        }

        return this.findSetsIntersection(setsToIntersect);
    }

    private Set<String> readFileWordsToSet(String filePath) {
        Set<String> words = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split(" ");
                for (String word : lineWords) {
                    word = word.toLowerCase().replaceAll("[^a-z]", ""); // lowercase and remove non-letter characters
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }

    private Set<String> findSetsIntersection(ArrayList<Set<String>> fileWords) {
        Set<String> result = new HashSet<>(fileWords.get(0));

        for (int i = 1; i < fileWords.size(); i++) {
            result.retainAll(fileWords.get(i));
        }

        return result;
    }
}
