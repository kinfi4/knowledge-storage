package MPI;

import mpi.MPI;
import support.MatrixMethods;


public class ScatterSend {
    void sendTest() {
        MPI.Init(null);
        int rank = MPI.COMM_WORLD.Rank();
        int MASTER = 0;
        int processorsCount = MPI.COMM_WORLD.Size();

        int matrixSize = 100;
        int[][] A = new int[matrixSize][matrixSize];

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
        int[] A1D = MatrixMethods.get1dfrom2d(A);

        MPI.COMM_WORLD.Scatterv(A1D, 0, sendcounts, displs, MPI.INT, bufferA, 0, sendcounts[rank], MPI.INT, MASTER);

        int[][] blocksA = Send_Sort_SendFirstElements_INTS__MATRIX.convert1DTo2D(bufferA, blockSize + (rank < extraRows ? 1 : 0), matrixSize);

        MPI.Finalize();
    }
}
