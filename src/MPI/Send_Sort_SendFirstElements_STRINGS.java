package MPI;

import  mpi.*;
import java.util.Arrays;
import java.util.Random;

public class Send_Sort_SendFirstElements_STRINGS {
    static final int MASTER = 0;
    static final int STRINGS_COUNT = 15;
    static final int MIN_LENGTH = 3;
    static final int MAX_LENGTH = 10;

    public static void main(String[] args) {
        MPI.Init(args);

        int currentProcess = MPI.COMM_WORLD.Rank();
        int processesCount = MPI.COMM_WORLD.Size();

        if(STRINGS_COUNT % processesCount != 0){
            System.out.println("The number of words must be a multiple of the number of processes!");
            MPI.Finalize();
            return;
        }

        String[] sendStrings = new String[STRINGS_COUNT];
        if(currentProcess == MASTER) {
            sendStrings = getRandomStringsArray(STRINGS_COUNT);
            System.out.println("All: " + Arrays.toString(sendStrings));
        }

        int stringsPerProcess = sendStrings.length / processesCount;
        String[] recvStrings = new String[stringsPerProcess];
        MPI.COMM_WORLD.Scatter(sendStrings, 0, stringsPerProcess, MPI.OBJECT, recvStrings, 0, stringsPerProcess, MPI.OBJECT, MASTER);

        Arrays.sort(recvStrings);

        String[] resultStrings = new String[processesCount];
        MPI.COMM_WORLD.Gather(recvStrings, 0, 1, MPI.OBJECT, resultStrings, 0, 1, MPI.OBJECT, MASTER);

        if(currentProcess == MASTER) {
            System.out.println("Processed: " + Arrays.toString(resultStrings));
        }

        MPI.Finalize();
    }
    private static String[] getRandomStringsArray(int stringCount) {
        Random random = new Random();
        String[] strings = new String[stringCount];
        for (int i = 0; i < strings.length; i++)
            strings[i] =  getRandomString(random.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH);
        return strings;
    }
    private static String getRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }
}