package support;

import java.util.Random;

public class MatrixMethods {
    public static int[][] generateRandomMatrix() {
        int n = 10;
        int[][] matrix = new int[n][n];

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = random.nextInt(10) + 1;
            }
        }

        return matrix;
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
