public class BallThread extends Thread {
    protected final Ball b;

    public BallThread(Ball ball){
        b = ball;
    }

    @Override
    public void run(){
        System.out.println("Created a thread with name:" + Thread.currentThread().getName());

        try{
            while(true) {
                b.move();
                b.sleep();

                if(b.interceptedHole()) {
                    b.removeMeFromCanvas();
                    return;
                }
            }
        } catch(InterruptedException ignored){}
    }
}

