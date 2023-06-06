import java.util.Random;

public class SyncMatrixMultiplication {
    public static void main(String[] args) {
        final int MATRIX_SIZE_1 = 1000;
        final int MATRIX_SIZE_2 = 1000;
        final int MATRIX_SIZE_3 = 1000;

        long startTime = System.nanoTime();

        double[][] A = generateRandomMatrix(MATRIX_SIZE_1, MATRIX_SIZE_2);
        double[][] B = generateRandomMatrix(MATRIX_SIZE_2, MATRIX_SIZE_3);

        double[][] C = matrixMultiplication(A, B);

        long endTime = System.nanoTime();
        System.out.printf("SYNC HAS FINISHED IN: %f SECONDS FOR %dx%d MATRIX%n", (endTime - startTime) / 1e9, MATRIX_SIZE_1, MATRIX_SIZE_3);
    }

    public static double[][] generateRandomMatrix(int rows, int columns) {
        double[][] matrix = new double[rows][columns];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextDouble() * 100;
            }
        }
        return matrix;
    }

    public static double[][] matrixMultiplication(double[][] A, double[][] B) {
        int n = A.length;
        int m = B[0].length;
        double[][] C = new double[n][m];
        int p = B.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < p; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    public static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] array = new int[size];

        for(int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(100);
        }

        return array;
    }
}
