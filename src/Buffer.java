public class Buffer {
    private final int[] items = new int[20];
    private int currentPutIdx = 0;
    private int currentTakeIdx = 0;
    private int itemsCount = 0;

    public synchronized int take() {
        while (itemsCount == 0) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        int item = this.items[currentTakeIdx];
        this.currentTakeIdx += 1;

        if(this.currentTakeIdx == this.items.length) {
            this.currentTakeIdx = 0;
        }

        this.itemsCount -= 1;

        notifyAll();

        return item;
    }

    public synchronized void put(int item) {
        while (itemsCount == this.items.length) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        this.items[this.currentPutIdx] = item;

        this.currentPutIdx += 1;

        if(this.currentPutIdx == this.items.length) {
            this.currentPutIdx = 0;
        }

        this.itemsCount += 1;

        notifyAll();
    }

}
