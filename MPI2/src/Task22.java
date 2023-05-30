import mpi.MPI;
import mpi.MPIException;

import java.util.Arrays;
import java.util.Random;

public class Task22 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int n = 10;

        int[][] matrix = new int[n][n];

        if (rank == 0) {
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = random.nextInt(10) + 1;
                }
            }

            int rowsPerProcess = n / (size-1);
            int extraRows = n % (size-1);

            int currentOffset = 0;
            for (int processes = 1; processes < size; processes++) {
                int rows = rowsPerProcess + (processes < extraRows ? 1 : 0);
                System.out.println(currentOffset + " " + (currentOffset + rows));
                int[] arr1D = convert2DTo1D(Arrays.copyOfRange(matrix, currentOffset, currentOffset + rows));

                MPI.COMM_WORLD.Send(arr1D, 0, arr1D.length, MPI.INT, processes, 0);

                currentOffset += rows;
            }

            int[] totalFirsts = new int[n];
            currentOffset = 0;
            for (int process = 1; process < size; process++) {
                int rows = rowsPerProcess + (process < extraRows ? 1 : 0);
                int[] sortedFirsts = new int[rows];

                MPI.COMM_WORLD.Recv(sortedFirsts, 0, sortedFirsts.length, MPI.INT, process, 1);

                System.arraycopy(sortedFirsts, 0, totalFirsts, currentOffset, rows);

                currentOffset += rows;
            }

            System.out.println(Arrays.toString(totalFirsts));
        } else {
            int rowsPerProcess = n / (size-1);
            int extraRows = n % (size-1);
            int rows = rowsPerProcess + (rank < extraRows ? 1 : 0);

            int[] arr1D = new int[rows * n];
            MPI.COMM_WORLD.Recv(arr1D, 0, arr1D.length, MPI.INT, 0, 0);

            int[][] arr2D = convert1DTo2D(arr1D, rows, n);
            System.out.println("Process " + rank + " received " + Arrays.deepToString(arr2D));

            int[] sortedFirsts = new int[rows];
            for (int i = 0; i < rows; i++) {
                var subArr = arr2D[i];
                Arrays.sort(subArr);
                sortedFirsts[i] = subArr[0];
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

        System.out.println(Arrays.toString(arr1D));

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
