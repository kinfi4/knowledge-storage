import mpi.MPI;

import java.util.Random;


public class Single4Many {
    public static void main(String args[]) throws Exception {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int processorsCount = MPI.COMM_WORLD.Size();
        var comm = MPI.COMM_WORLD;

        // matrix multiplication using mpi
        if (rank == 0) {
            int[][] matrixA = generateRandomMatrix(100, 100);
            int[][] matrixB = generateRandomMatrix(100, 100);
            int[][] matrixC = new int[matrixA.length][matrixB[0].length];

            int[] row = new int[matrixA[0].length];

            var rowsPerProcessor = matrixA.length / processorsCount;
            var rowsLeft = matrixA.length % processorsCount;


        } else {
            int[][] matrixA = null;
            int[][] matrixB = null;
            int[][] matrixC = null;

            int[] row = null;
            int[] result = null;
        }


        MPI.Finalize();
    }

    public static int[][] generateRandomMatrix(int rows, int columns) {
        int[][] matrix = new int[rows][columns];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }
}
