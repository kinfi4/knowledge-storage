import java.util.ArrayList;
import java.util.List;

public class GradesBook {
    public final int STUDENTS_PER_GROUP = 1000;
    public final int NUMBER_OF_GROUPS = 10000;
    public int[][] gradesBook;

    private final List<Integer> blockedGroups = new ArrayList<>();

    GradesBook() {
        this.gradesBook = new int[this.NUMBER_OF_GROUPS][this.STUDENTS_PER_GROUP];
    }

    public synchronized boolean allMarksPlaced() {
        for (int[] group : this.gradesBook) {
            for (int mark : group) {
                if (mark == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public int[] getGroup(int groupId) {
        synchronized (this) {
            while(this.blockedGroups.contains(groupId)) {
                try {
                    wait();
                } catch (InterruptedException ignored) {}
            }

            this.blockedGroups.add(groupId);

            return this.gradesBook[groupId];
        }
    }

    public void unBlockGroup(int groupId) {
        synchronized (this) {
            this.blockedGroups.remove((Integer) groupId);
            notifyAll();
        }
    }

    public void printMarks() {
        for (int i = 0; i < this.gradesBook.length; i++) {
            System.out.print("GROUP " + i + " -- ");
            for(int studentMark : this.gradesBook[i]) {
                System.out.print(studentMark + " ");
            }

            System.out.println();
        }
    }

}
