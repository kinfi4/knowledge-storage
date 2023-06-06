package MPI;

import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.Random;

public class Send_Sort_SendFirstElements_INTS {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int MATRIX_SIZE = 100;

        int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

        if (rank == 0) {
            Random random = new Random();
            for (int i = 0; i < MATRIX_SIZE; i++) {
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    matrix[i][j] = random.nextInt(10000) + 1;
                }
            }

            int rowsPerProcess = MATRIX_SIZE / (size-1);
            int extraRows = MATRIX_SIZE % (size-1);

            int currentOffset = 0;
            for (int processes = 1; processes < size; processes++) {
                int rowsCount = rowsPerProcess + (processes < extraRows ? 1 : 0);
                int[] arr1D = convert2DTo1D(Arrays.copyOfRange(matrix, currentOffset, currentOffset + rowsCount));

                MPI.COMM_WORLD.Send(arr1D, 0, arr1D.length, MPI.INT, processes, 0);

                currentOffset += rowsCount;
            }

            int[] totalFirsts = new int[MATRIX_SIZE];
            currentOffset = 0;
            for (int process = 1; process < size; process++) {
                int rowsCount = rowsPerProcess + (process < extraRows ? 1 : 0);
                int[] sortedFirsts = new int[rowsCount];

                MPI.COMM_WORLD.Recv(sortedFirsts, 0, sortedFirsts.length, MPI.INT, process, 1);

                System.arraycopy(sortedFirsts, 0, totalFirsts, currentOffset, rowsCount);

                currentOffset += rowsCount;
            }

            System.out.println(Arrays.toString(totalFirsts));

            int[] validatedFirsts = new int[MATRIX_SIZE];
            for (int i = 0; i < MATRIX_SIZE; i++) {
                int min = Integer.MAX_VALUE;
                for (int j = 0; j < MATRIX_SIZE; j++) {
                    if (matrix[i][j] < min) {
                        min = matrix[i][j];
                    }
                }
                validatedFirsts[i] = min;
            }
            System.out.println(Arrays.toString(validatedFirsts));
        } else {
            int rowsPerProcess = MATRIX_SIZE / (size-1);
            int extraRows = MATRIX_SIZE % (size-1);
            int rowsCount = rowsPerProcess + (rank < extraRows ? 1 : 0);

            int[] arr1D = new int[rowsCount * MATRIX_SIZE];
            MPI.COMM_WORLD.Recv(arr1D, 0, arr1D.length, MPI.INT, 0, 0);

            int[][] arr2D = convert1DTo2D(arr1D, rowsCount, MATRIX_SIZE);

            int[] sortedFirsts = new int[rowsCount];
            for (int i = 0; i < rowsCount; i++) {
                var currentRow = arr2D[i];
                Arrays.sort(currentRow);
                sortedFirsts[i] = currentRow[0];
            }

            MPI.COMM_WORLD.Send(sortedFirsts, 0, sortedFirsts.length, MPI.INT, 0, 1);
        }
    }

    public static int[] convert2DTo1D(int[][] arr2D) {
        int rows = arr2D.length;
        int cols = arr2D[0].length;
        int[] arr1D = new int[rows * cols];
        int index = 0;
        for (int[] doubles : arr2D) {
            for (int j = 0; j < cols; j++) {
                arr1D[index++] = doubles[j];
            }
        }

        return arr1D;
    }
    public static int[][] convert1DTo2D(int[] arr1D, int rows, int cols) {
        int[][] arr2D = new int[rows][cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr2D[i][j] = arr1D[index++];
            }
        }

        return arr2D;
    }


}
