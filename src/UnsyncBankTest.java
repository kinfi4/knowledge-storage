import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnsyncBankTest {
    public static final int NACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10000;

    public static void main(String[] args) {
//        task1();
        task2();
//        task3();
    }

    public static void task3() {
        GradesBook book = new GradesBook();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Thread teacher = new Teacher(book);
            threads.add(teacher);

            teacher.start();
        }

        for (Thread tr: threads) {
            try {
                tr.join();
            } catch (InterruptedException ignored) {}
        }

        book.printMarks();
    }

    public static void task2() {
        Buffer buffer = new Buffer();
        (new Thread(new Producer(buffer))).start();
        (new Thread(new Consumer(buffer))).start();
    }

    public static void task1() {
        Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
        for (int i = 0; i < NACCOUNTS; i++){
            TransferThread t = new TransferThread(b, i, INITIAL_BALANCE);
            t.setPriority(Thread.NORM_PRIORITY + i % 2);
            t.start();
        }
    }
}

class Bank {
    private final Object sync = new Object();

    private final Lock lock = new ReentrantLock();
    public static final int NTEST = 10000;
    private final int[] accounts;
    private long ntransacts = 0;

    public Bank(int n, int initialBalance){
        accounts = new int[n];

        Arrays.fill(accounts, initialBalance);

        ntransacts = 0;
    }

    public void transfer(int from, int to, int amount) throws InterruptedException {
        accounts[from] -= amount;
        accounts[to] += amount;
        ntransacts++;

        if (ntransacts % NTEST == 0) {
            test();
        }
    }

    public synchronized void syncTransfer(int from, int to, int amount) throws InterruptedException {
        accounts[from] -= amount;
        accounts[to] += amount;
        ntransacts++;

        if (ntransacts % NTEST == 0) {
            test();
        }
    }
    public synchronized void syncBlockTransfer(int from, int to, int amount) throws InterruptedException {
        synchronized (sync) {
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;

            if (ntransacts % NTEST == 0) {
                test();
            }
        }
    }
    public synchronized void lockTransfer(int from, int to, int amount) throws InterruptedException {

        this.lock.lock();
        try {
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;

            if (ntransacts % NTEST == 0) {
                test();
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void test(){
        int sum = 0;

        for (int account : accounts) {
            sum += account;
        }

        System.out.println("Transactions:" + ntransacts + " Sum: " + sum);
    }

    public int size(){
        return accounts.length;
    }
}

class TransferThread extends Thread {
    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;
    private static final int REPS = 1000;

    public TransferThread(Bank b, int from, int max){
        bank = b;
        fromAccount = from;
        maxAmount = max;
    }

    public void run(){
        try{
            while (!interrupted()){
                for (int i = 0; i < REPS; i++){
                    int toAccount = (int)(bank.size()*Math.random());
                    int amount = (int)(maxAmount * Math.random()/REPS);
//                    bank.transfer(fromAccount, toAccount, amount);
//                    bank.syncTransfer(fromAccount, toAccount, amount);
//                    bank.syncBlockTransfer(fromAccount, toAccount, amount);
                    bank.lockTransfer(fromAccount, toAccount, amount);
                    Thread.sleep(1);
                }
            }
        } catch(InterruptedException ignored) {}
    }
}