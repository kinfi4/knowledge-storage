package MPI;

import  mpi.*;

import java.util.Arrays;

public class Send_Sort_SendFirstElements_STRINGS {
    static final int MASTER = 0;
    static final int STRINGS_COUNT = 20;
    static final int STRING_SIZE = 5;

    public static void main(String[] args) {
        MPI.Init(args);

        int currentProcess = MPI.COMM_WORLD.Rank();
        int processesCount = MPI.COMM_WORLD.Size();

        if(STRINGS_COUNT % processesCount != 0){
            System.out.println("The number of words must be a multiple of the number of processes!");
            MPI.Finalize();
            return;
        }

        String[] strings = new String[STRINGS_COUNT];
        if(currentProcess == MASTER) {
            for (int i = 0; i < strings.length; i++) {
                strings[i] = generateRandomString(STRING_SIZE);
            }
            System.out.println("All: " + Arrays.toString(strings));
        }

        char[] sendStringsInCharFormat = new char[strings.length * STRING_SIZE];
        if(currentProcess == MASTER) {
            sendStringsInCharFormat = StringArrayToCharArray(strings);
        }

        int charsPerProcess = sendStringsInCharFormat.length / processesCount;
        char[] recvStringsInCharFormat = new char[charsPerProcess];
        MPI.COMM_WORLD.Scatter(sendStringsInCharFormat, 0, charsPerProcess, MPI.CHAR, recvStringsInCharFormat, 0, charsPerProcess, MPI.CHAR, MASTER);

        String[] recvStrings = CharArrayToStringArray(recvStringsInCharFormat, STRING_SIZE);
        Arrays.sort(recvStrings);

        char[] resultInCharFormat = new char[processesCount * STRING_SIZE];
        MPI.COMM_WORLD.Gather(recvStrings[0].toCharArray(), 0, STRING_SIZE, MPI.CHAR, resultInCharFormat, 0, STRING_SIZE, MPI.CHAR, MASTER);

        if(currentProcess == MASTER) {
            String[] result = CharArrayToStringArray(resultInCharFormat, STRING_SIZE);

            System.out.println("Processed: " + Arrays.toString(result));
        }

        MPI.Finalize();
    }

    private static char[] StringArrayToCharArray(String[] strings) {
        StringBuilder builder = new StringBuilder();

        for (String str : strings) {
            builder.append(str);
        }

        return builder.toString().toCharArray();
    }

    private static String[] CharArrayToStringArray(char[] chars, int stringSize) {
        int arrayLength = chars.length / stringSize;
        String[] strings = new String[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < stringSize; j++) {
                int index = i * stringSize + j;
                builder.append(chars[index]);
            }

            strings[i] = builder.toString();
        }

        return strings;
    }

    private static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }
}