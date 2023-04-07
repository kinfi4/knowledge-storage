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
    TeacherTask(int[][] groupsToPlaceMarks, int start, int end) {
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

        int batchSize = this.groupsToPlaceMarks.length / 10;

        for (int i = 0; i < this.groupsToPlaceMarks.length; i += batchSize) {
            int upperIdx = Math.min(i + batchSize, this.groupsToPlaceMarks.length);

            TeacherTask task = new TeacherTask(Arrays.copyOfRange(this.groupsToPlaceMarks, i, upperIdx));
            tasks.add(task);
            task.fork();
        }

        ArrayList<int[]> grades = new ArrayList<>();
        for (TeacherTask task : tasks) {
            int[][] groups = task.join();
            grades.addAll(Arrays.asList(groups));
        }

        return grades.toArray(new int[grades.size()][]);

//        int middle = (this.end + this.start) / 2;
//
//        TeacherTask task1 = new TeacherTask(groupsToPlaceMarks, start, middle);
//        task1.fork();
//
//
//        TeacherTask task2 = new TeacherTask(groupsToPlaceMarks, middle, end);
//        task2.fork();
//
//        return this.mergeArrays(task1.join(), task2.join());
    }

    private int[][] mergeArrays(int[][] arr1, int[][] arr2) {
        int[][] resultArray = new int[arr1.length + arr2.length][arr1[0].length];

        System.arraycopy(arr1, 0, resultArray, 0, arr1.length);
        System.arraycopy(arr2, 0, resultArray, arr1.length, arr2.length);

        return resultArray;
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
