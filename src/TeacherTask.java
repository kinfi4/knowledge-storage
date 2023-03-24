import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class TeacherTask extends RecursiveTask<int[][]> {
    private final int[][] groupsToPlaceMarks;
    TeacherTask(int[][] groupsToPlaceMarks) {
        this.groupsToPlaceMarks = groupsToPlaceMarks;
    }

    @Override
    protected int[][] compute() {
        if (this.isHandleable()) {
            int[][] grades = new int[this.groupsToPlaceMarks.length][this.groupsToPlaceMarks[0].length];

            for (int i = 0; i < this.groupsToPlaceMarks.length; i++) {
                grades[i] = this.getGrades(this.groupsToPlaceMarks[0].length);
            }

            return grades;
        }

        List<TeacherTask> tasks = new ArrayList<>();

        for (int[] group : this.groupsToPlaceMarks) {
            TeacherTask task = new TeacherTask(new int[][]{group});
            tasks.add(task);
            task.fork();
        }

        ArrayList<int[]> grades = new ArrayList<>();
        for (TeacherTask task : tasks) {
            int[][] groups = task.join();
            grades.addAll(Arrays.asList(groups));
        }

        return grades.toArray(new int[grades.size()][]);
    }

    private boolean isHandleable() {
        return this.groupsToPlaceMarks.length <= 1000;
    }

    private int[] getGrades(int numberOfStudents) {
        int[] results = new int[numberOfStudents];
        Random random = new Random();

        Arrays.setAll(results, i -> random.nextInt(1, 101));

        return results;
    }
}
