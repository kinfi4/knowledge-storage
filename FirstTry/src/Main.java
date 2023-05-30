import java.util.ArrayList;
import java.util.Random;

public class Main {
    static int N = 10;

    public static void main(String[] args) throws InterruptedException {
        Buffer buffer = new Buffer();

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            var list = new ArrayList<String>();

            for (int j = 0; j < 10; j++) {
                String generatedString = RandomStringGenerator.generateRandomString();
                    list.add(generatedString);
            }

            Thread newTr = new ThreadAdder(list, buffer);
            threads.add(newTr);

            newTr.start();
        }

        for (Thread tr : threads) {
            tr.join();
        }

        buffer.printList();
    }

}

class Buffer {
    ArrayList<String> mainList = new ArrayList<>();

    public synchronized void addString (String text) {
        this.mainList.add(text);
    }

    public void printList() {
        System.out.println(mainList);
    }

}

class ThreadAdder extends Thread {
    ArrayList<String> strList;
    Buffer b;

    ThreadAdder(ArrayList<String> list, Buffer b) {
        this.strList = list;
        this.b = b;
    }

    @Override
    public void run() {
        for (String str : this.strList) {
            this.b.addString(str);
        }
    }
}

class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 10;
    private static final Random random = new Random();

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
