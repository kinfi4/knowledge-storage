package MPI;

import MPI.SendSendFirstBack.Send_Sort_SendFirstElements_INTS__MATRIX;
import mpi.*;

import java.util.Arrays;
import support.MatrixMethods;


public class Send_SumMult_SendBack {
    public static void main(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int processorsCount = MPI.COMM_WORLD.Size();
        int MASTER = 0;

        int matrixSize = 100;
        int[][] A = new int[matrixSize][matrixSize];
        int[][] B = new int[matrixSize][matrixSize];
        int[] C = new int[matrixSize];

        if (rank == 0) {
            A = MatrixMethods.generateRandomMatrix();
            B = MatrixMethods.generateRandomMatrix();

            for (int i = 0; i < matrixSize; i++) {
                C[i] = (int) (Math.random() * 10);
            }
        }

        int[] A1D = Send_Sort_SendFirstElements_INTS__MATRIX.convert2DTo1D(A);
        int[] B1D = Send_Sort_SendFirstElements_INTS__MATRIX.convert2DTo1D(B);

        int[] sendcounts = new int[matrixSize]; // send count array for Scatterv
        int[] displs = new int[matrixSize]; // displacement array for Scatterv

        int blockSize = matrixSize / processorsCount;
        int extraRows = matrixSize % processorsCount;
        displs[0] = 0;
        sendcounts[0] = blockSize * matrixSize + (extraRows > 0 ? matrixSize : 0);
        for (int i = 1; i < matrixSize; i++) {
            sendcounts[i] = blockSize * matrixSize;
            if (i < extraRows) {
                sendcounts[i] += matrixSize;
            }
            displs[i] = displs[i - 1] + sendcounts[i - 1];
        }

        int[] bufferA = new int[sendcounts[rank]];
        int[] bufferB = new int[sendcounts[rank]];

        MPI.COMM_WORLD.Scatterv(A1D, 0, sendcounts, displs, MPI.INT, bufferA, 0, sendcounts[rank], MPI.INT, MASTER);
        MPI.COMM_WORLD.Scatterv(B1D, 0, sendcounts, displs, MPI.INT, bufferB, 0, sendcounts[rank], MPI.INT, MASTER);

        int numberOfItems = blockSize + (rank < extraRows ? 1 : 0);

        int[][] blocksA = Send_Sort_SendFirstElements_INTS__MATRIX.convert1DTo2D(bufferA, numberOfItems, matrixSize);
        int[][] blocksB = Send_Sort_SendFirstElements_INTS__MATRIX.convert1DTo2D(bufferB, numberOfItems, matrixSize);

        int[] resultSums = new int[numberOfItems];
        for (int i = 0; i < numberOfItems; i++) {
            int sumA = 0;
            int sumB = 0;
            for (int j = 0; j < matrixSize; j++) {
                sumA += blocksA[i][j];
                sumB += blocksB[i][j];
            }

            resultSums[i] = sumA * sumB;
        }

        int[] sendcounts2 = new int[processorsCount];
        int[] displs2 = new int[processorsCount];
        displs2[0] = 0;
        sendcounts2[0] = blockSize + (extraRows > 0 ? 1 : 0);
        for (int i = 1; i < processorsCount; i++) {
            sendcounts2[i] = blockSize;
            if (i < extraRows) {
                sendcounts2[i] += 1;
            }
            displs2[i] = displs2[i - 1] + sendcounts2[i - 1];
        }

        MPI.COMM_WORLD.Gatherv(resultSums, 0, numberOfItems, MPI.INT, C, 0, sendcounts2, displs2, MPI.INT, MASTER);

        if (rank == MASTER) {
            System.out.println("The result is: " + Arrays.toString(C));

            int[] result = new int[matrixSize];
            for (int i = 0; i < matrixSize; i++) {
                int sumA = 0;
                int sumB = 0;
                for (int j = 0; j < matrixSize; j++) {
                    sumA += A[i][j];
                    sumB += B[i][j];
                }
                result[i] = sumA * sumB;
            }
            System.out.println("VALIDATING THE RESULT: " + Arrays.equals(C, result));
        }

        MPI.Finalize();
    }
}