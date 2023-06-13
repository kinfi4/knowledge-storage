package pools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BankTask {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Bank bank = new Bank(100);

        for (int i = 0; i < 50; i++) {
            executor.execute(new Incrementor(bank));
            executor.execute(new Decrementor(bank));
        }

        executor.shutdown();

        try {
            executor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Final amount: " + bank.getMoney());
    }
}

class Bank {
    private int money;

    public Bank(int money) {
        this.money = money;
    }

    public synchronized void decrementMoney(int amount) {
        this.money -= amount;
    }

    public synchronized void incrementMoney(int amount) {
        this.money += amount;
    }

    public int getMoney() {
        return this.money;
    }
}


class Incrementor implements Runnable {
    private Bank bank;

    public Incrementor(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            this.bank.incrementMoney(100);
        }
    }
}

class Decrementor implements Runnable {
    private Bank bank;

    public Decrementor(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            this.bank.decrementMoney(100);
        }
    }
}