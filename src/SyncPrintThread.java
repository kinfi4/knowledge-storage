public class SyncPrintThread extends Thread {
    private final String symbol;
    private final Sync syncController;

    public SyncPrintThread(String symbol, Sync syncController) {
        this.symbol = symbol;
        this.syncController = syncController;
    }

    @Override
    public void run() {
        while(true) {
            this.syncController.printSymbol(this.symbol);

            if(this.syncController.wasStopped()) {
                return;
            }
        }
    }
}
