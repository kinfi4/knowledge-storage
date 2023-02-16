public class Sync {
    private String currentSymbol;
    private int counter;
    private boolean isStopped;

    public Sync(String initSymbol) {
        this.currentSymbol = initSymbol;
        this.counter = 0;
        this.isStopped = false;
    }

    public synchronized boolean wasStopped() {
        return this.isStopped;
    }

    private synchronized String getCurrentSymbol() {
        return this.currentSymbol;
    }

    public synchronized void printSymbol(String s) {
        while(!this.getCurrentSymbol().equals(s)) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }

        System.out.print(s);

        this.counter += 1;

        if(this.counter % 100 == 0) {
            System.out.println();
        }

        if(this.counter == 10000 - 1) {
            this.isStopped = true;
        }

        toggleSymbol();
        notifyAll();
    }

    private synchronized void toggleSymbol() {
        if(this.currentSymbol.equals("|")) {
            this.currentSymbol = "-";
        } else {
            this.currentSymbol = "|";
        }
    }
}
