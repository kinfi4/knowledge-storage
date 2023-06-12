package MPI;

import mpi.MPI;
import support.MatrixMethods;

import java.util.Arrays;

public class Send_FindMin_Send__INTS_ARRAY {
    public static void main(String[] args) {
        MPI.Init(args);

        var rank = MPI.COMM_WORLD.Rank();
        var MASTER = 0;
        var processorsCount = MPI.COMM_WORLD.Size();

        int arraySize = 100;
        int[] arrayToSend = new int[arraySize];

        if (rank == MASTER) {
            for (int i = 0; i < arraySize; i++) {
                arrayToSend[i] = (int) (Math.random() * 100);
            }
        }

        int[] sendcounts = new int[processorsCount];
        int[] displs = new int[processorsCount];

        int elementsPerProcess = arraySize / processorsCount;
        int extraElements = arraySize % processorsCount;

        displs[0] = 0;
        sendcounts[0] = elementsPerProcess + (extraElements > 0 ? 1 : 0);

        for (int i = 1; i < processorsCount; i++) {
            sendcounts[i] = elementsPerProcess;

            if (i < extraElements) {
                sendcounts[i] += 1;
            }

            displs[i] = displs[i - 1] + sendcounts[i - 1];
        }

        int[] bufferA = new int[sendcounts[rank]];
        MPI.COMM_WORLD.Scatterv(arrayToSend, 0, sendcounts, displs, MPI.INT, bufferA, 0, sendcounts[rank], MPI.INT, MASTER);

        int min = Integer.MAX_VALUE;
        for (int j : bufferA) {
            if (j < min) {
                min = j;
            }
        }

        System.out.println("Array: " + Arrays.toString(bufferA) + " with min " + min + " from process " + rank);

        int[] result = new int[1];
        MPI.COMM_WORLD.Reduce(new int[]{min}, 0, result, 0, 1, MPI.INT, MPI.MIN, MASTER);

        if (rank == MASTER) {
            System.out.println("TOTAL MIN: " + result[0]);
        }

        MPI.Finalize();
    }

}
