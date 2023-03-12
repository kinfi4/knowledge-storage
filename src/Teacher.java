import java.util.Random;

public class Teacher extends Thread {
    private static final int STUDENTS_PER_GROUP = 20;
    GradesBook book;

    Teacher(GradesBook book) {
        this.book = book;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while(!this.book.allMarksPlaced()) {
            int randomGroup = rand.nextInt(0, 3);
            int randomStudent = rand.nextInt(0, STUDENTS_PER_GROUP);

            int[] group = this.book.getGroup(randomGroup);

            if(group[randomStudent] == 0) {
                int randomMark = rand.nextInt(1, 101);
                group[randomStudent] = randomMark;
            }

            this.book.unBlockGroup(randomGroup);

            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
    }
}
