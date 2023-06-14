package MPI.ProductOfSum;

import java.util.Arrays;
import java.util.Random;

import  mpi.*;


public class MPI__CalculateProductOfSum__MAYBE_NOT_WORKING {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int MASTER = 0;
        int MATRIX_SIZE = 1000;

        int processesCount = MPI.COMM_WORLD.Size();
        int processRank = MPI.COMM_WORLD.Rank();

        int workers = processesCount - 1;
        int rowsPerProcess = MATRIX_SIZE / workers;

        if(processRank == MASTER){
            int[][] A = new int[MATRIX_SIZE][MATRIX_SIZE];
            int[][] B = new int[MATRIX_SIZE][MATRIX_SIZE];
            int[] C = new int[MATRIX_SIZE];

            Random random = new Random();
            for (int i = 0; i < MATRIX_SIZE; i++) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    A[i][j] = random.nextInt(100);
                    B[i][j] = random.nextInt(100);
                }
            }

            for (int process = 1; process <= workers; process++) {
                int first = (process-1) * rowsPerProcess;
                int last = first + rowsPerProcess;

                int[][] subA = Arrays.copyOfRange(A, first, last);
                int[] sendBufferA = get1dfrom2d(subA);

                int[][] subB = Arrays.copyOfRange(B, first, last);
                int[] sendBufferB = get1dfrom2d(subB);

                MPI.COMM_WORLD.Send(sendBufferA, 0, rowsPerProcess * MATRIX_SIZE, MPI.INT, process, 1);
                MPI.COMM_WORLD.Send(sendBufferB, 0, rowsPerProcess * MATRIX_SIZE, MPI.INT, process, 2);
            }

            for (int source = 1; source <= workers; source++) {
                int[] offsetBuffer = new int[1];
                MPI.COMM_WORLD.Recv(offsetBuffer, 0, 1, MPI.INT, source, 4);

                int[] recvBufferMatrix = new int[rowsPerProcess];
                MPI.COMM_WORLD.Recv(recvBufferMatrix, 0, rowsPerProcess, MPI.INT, source, 5);

                System.arraycopy(recvBufferMatrix, 0, C, offsetBuffer[0] * rowsPerProcess, rowsPerProcess);
            }

            System.out.println("Matrix C:" + Arrays.toString(C));
        }
        else {
            int[] offset = new int[1];
            MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, MASTER, 0);

            int[] recvBufferA = new int[rowsPerProcess * MATRIX_SIZE];
            MPI.COMM_WORLD.Recv(recvBufferA, 0, rowsPerProcess * MATRIX_SIZE, MPI.INT, MASTER, 1);
            int[][] rowsOfA = get2dFrom1d(recvBufferA, rowsPerProcess, MATRIX_SIZE);


            int[] recvBufferB = new int[rowsPerProcess * MATRIX_SIZE];
            MPI.COMM_WORLD.Recv(recvBufferB, 0, rowsPerProcess * MATRIX_SIZE, MPI.INT, MASTER, 2);
            int[][] rowsOfB = new int[rowsPerProcess][MATRIX_SIZE];
            int subBIdx = 0;
            for (int i = 0; i < rowsPerProcess; i++) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    rowsOfB[i][j] = recvBufferB[subBIdx];
                    subBIdx++;
                }
            }

            int[] C = new int[rowsPerProcess];
            for(int i = 0; i < rowsOfA.length; i++) {
                int sumA = 0, sumB = 0;
                for(int j = 0; j < rowsOfA[i].length; j++) {
                    sumA += rowsOfA[i][j];
                    sumB += rowsOfB[i][j];
                }
                C[i] = sumA * sumB;
            }

            MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, MASTER, 4);
            MPI.COMM_WORLD.Send(C, 0, rowsPerProcess, MPI.INT, MASTER, 5);
        }

        MPI.Finalize();
    }

    public static int[] get1dfrom2d(int[][] matrix) {
        int[] arr1D = new int[matrix.length * matrix[0].length];

        int index = 0;
        for (int[] row : matrix) {
            for (int element : row) {
                arr1D[index++] = element;
            }
        }
        return arr1D;
    }

    public static int[][] get2dFrom1d(int[] arr1D, int rows, int cols) {
        int[][] matrix = new int[rows][cols];

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = arr1D[index++];
            }
        }
        return matrix;
    }

}