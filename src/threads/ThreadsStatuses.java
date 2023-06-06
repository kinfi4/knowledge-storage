package threads;


public class ThreadsStatuses {
    public static void main(String[] args) {
        ThreadState state = new ThreadState();
        ThreadChangeState thread = new ThreadChangeState(state);
        ThreadReadState threadReadState = new ThreadReadState(state);

        thread.start();
        threadReadState.start();

        try {
            thread.join();
            threadReadState.join();
        } catch (InterruptedException e) {}
    }
}

class ThreadState {
    private boolean state = true;
    public boolean stop = false;

    public synchronized void toggleState() {
        this.state = !this.state;
        notifyAll();
    }

    public synchronized void validateState() {
        while (!this.state) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
    }

}

class ThreadChangeState extends Thread {
    private final ThreadState state;

    ThreadChangeState(ThreadState state) {
        this.state = state;
    }

    public void run() {
        while (!this.state.stop) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}

            this.state.toggleState();
        }
    }
}

class ThreadReadState extends Thread {
    private final ThreadState state;

    ThreadReadState(ThreadState state) {
        this.state = state;
    }

    public void run() {
        int counter = 30;

        while (true) {
            this.state.validateState();

            System.out.println("Counter is equal to " + counter--);

            if (counter == 0) {
                this.state.stop = true;
                break;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
    }
}