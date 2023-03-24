import java.util.ArrayList;
import java.util.Random;

public class TeacherThread extends Thread {
    GradesBook book;
    private final ArrayList<Integer> groupIndexes;

    TeacherThread(GradesBook book, ArrayList<Integer> groupIdxes) {
        this.book = book;
        this.groupIndexes = groupIdxes;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int groupIdx : this.groupIndexes) {
            int[] group = this.book.getGroup(groupIdx);

            for (int i = 0; i < group.length; i++) {
                group[i] = random.nextInt(1, 101);
            }

            this.book.unBlockGroup(groupIdx);
        }
    }
}
