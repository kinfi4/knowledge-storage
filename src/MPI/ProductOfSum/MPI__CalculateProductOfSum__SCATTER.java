package MPI.ProductOfSum;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.Random;


public class MPI__CalculateProductOfSum__SCATTER {

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int MASTER = 0;
        int MATRIX_SIZE = 1000;

        int processesCount = MPI.COMM_WORLD.Size();
        int processRank = MPI.COMM_WORLD.Rank();

        int rowsPerProcess = MATRIX_SIZE / processesCount;

        int[][] A = new int[MATRIX_SIZE][MATRIX_SIZE];
        int[][] B = new int[MATRIX_SIZE][MATRIX_SIZE];
        int[] C = new int[MATRIX_SIZE];

        if (processRank == MASTER) {
            Random random = new Random();
            for (int i = 0; i < MATRIX_SIZE; i++) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    A[i][j] = random.nextInt(100);
                    B[i][j] = random.nextInt(100);
                }
            }
        }

        int[] subMatrixA = new int[rowsPerProcess * MATRIX_SIZE];
        int[] subMatrixB = new int[rowsPerProcess * MATRIX_SIZE];

        MPI.COMM_WORLD.Scatter(get1dfrom2d(A), 0, rowsPerProcess * MATRIX_SIZE, MPI.INT, subMatrixA, 0, rowsPerProcess * MATRIX_SIZE, MPI.INT, MASTER);
        MPI.COMM_WORLD.Scatter(get1dfrom2d(B), 0, rowsPerProcess * MATRIX_SIZE, MPI.INT, subMatrixB, 0, rowsPerProcess * MATRIX_SIZE, MPI.INT, MASTER);

        int[] subMatrixC = new int[rowsPerProcess];

        int sumA = 0;
        int sumB = 0;

        for (int i = 0; i < rowsPerProcess; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                sumA += subMatrixA[i * MATRIX_SIZE + j];
                sumB += subMatrixB[i * MATRIX_SIZE + j];
            }
            subMatrixC[i] = sumA * sumB;
        }

        MPI.COMM_WORLD.Gather(subMatrixC, 0, rowsPerProcess, MPI.INT, C, 0, rowsPerProcess, MPI.INT, MASTER);

        if (processRank == MASTER) {
            System.out.println(Arrays.toString(C));

            int[] resultArray = validateResult(A, B);

            System.out.println(Arrays.toString(resultArray));
        }

        MPI.Finalize();
    }

    public static int[] validateResult(int[][] firstMatrix, int[][] secondMatrix) {
        int sumA = 0;
        int sumB = 0;

        int[] resultArray = new int[firstMatrix.length];

        for (int i = 0; i < firstMatrix.length; i++) {
            for (int j = 0; j < firstMatrix.length; j++) {
                sumA += firstMatrix[i][j];
                sumB += secondMatrix[i][j];
            }

            resultArray[i] = sumA * sumB;
        }

        return resultArray;
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

}