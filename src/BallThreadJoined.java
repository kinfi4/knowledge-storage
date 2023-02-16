public class BallThreadJoined extends BallThread {
    Thread threadToJoin;
    public BallThreadJoined(Ball ball, Thread threadToJoin) {
        super(ball);
        this.threadToJoin = threadToJoin;
    }

    @Override
    public void run(){
        try {
            this.threadToJoin.join();
        } catch (InterruptedException e) {
            super.run();
            return;
        }

        super.run();
    }
}
