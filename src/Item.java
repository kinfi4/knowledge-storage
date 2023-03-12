public class Item {
    private int item;
    private boolean empty = true;

    public synchronized int take() {
        while (this.empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        this.empty = true;
        notifyAll();

        return this.item;
    }

    public synchronized void put(int item) {
        while (!this.empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        this.empty = false;
        this.item = item;
        notifyAll();
    }

}
