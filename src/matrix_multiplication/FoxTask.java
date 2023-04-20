package matrix_multiplication;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoxTask {
    public void run() {
        int[][] A = _generateRandomMatrix(1000, 1000);
        int[][] B = _generateRandomMatrix(1000, 1000);

        Instant start = Instant.now();
        int[][] C = matrixMultiplication(A, B);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.print("Sync multiplication: " + timeElapsed.toMillis());

        start = Instant.now();
        int[][] C2 = foxMatrixMultiplication(A, B, 100);
        end = Instant.now();
        timeElapsed = Duration.between(start, end);
        System.out.println("  Fox multiplication: " + timeElapsed.toMillis());
    }

    public static int[][] matrixMultiplication(int[][] matrix1, int[][] matrix2) {
        int matrix1Height = matrix1.length;
        int matrix1Width = matrix1[0].length;

        int matrix2Height = matrix2.length;
        int matrix2Width = matrix2[0].length;

        int[][] resultMatrix = new int[matrix1Height][matrix2Width];

        if(matrix1Width != matrix2Height) {
            throw new RuntimeException("Matrix's have invalid sizes!");
        }

        for (int row = 0; row < matrix1Height; row++) {
            for (int i = 0; i < matrix2Width; i++) {
                int sum = 0;

                for (int k = 0; k < matrix1Width; k++) {
                    sum += matrix1[row][k] * matrix2[k][i];
                }

                resultMatrix[row][i] = sum;
            }
        }

        return resultMatrix;
    }

    public static int[][] foxMatrixMultiplication(int[][] matrix1, int[][] matrix2, int blockSize) {
        if(matrix1.length != matrix1[0].length || matrix2.length != matrix2[0].length || matrix1.length != matrix2.length) {
            throw new RuntimeException("Matrix must be squared!");
        }

        int size = matrix1.length;

        if(size % blockSize != 0) {
            throw new RuntimeException("Block size is invalid for this matrix!");
        }

        int[][] resultMatrix = new int[size][size];
        List<Thread> threads = new ArrayList<>();

        int gridSize = size / blockSize;

        int[][][][] leftBlocks = _divideMatrix(matrix1, gridSize);
        int[][][][] rightBlocks = _divideMatrix(matrix2, gridSize);

        SyncFox sync = new SyncFox(leftBlocks, rightBlocks);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Thread thread = new FoxMultiplicationThread(i, j, gridSize, sync, resultMatrix);

                threads.add(thread);
                thread.start();
            }
        }

        for (Thread th : threads) {
            try{
                th.join();
            } catch (InterruptedException ignored) {}
        }

        return resultMatrix;
    }
    public static int[][] _generateRandomMatrix(int n, int m) {
        Random random = new Random();
        int[][] resultMatrix = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < m; k++) {
                resultMatrix[i][k] = random.nextInt(5);
            }
        }

        return resultMatrix;
    }
    public static boolean _checkMatrixAreEqual(int[][] mat1, int[][] mat2) {
        if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
            return false;
        }

        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat1[0].length; j++) {
                if(mat1[i][j] != mat2[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }
    private static int[][][][] _divideMatrix(int[][] matrix, int gridSize) {
        int blockSize = matrix.length / gridSize;
        int[][][][] blocks = new int[gridSize][gridSize][blockSize][blockSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                for (int k = 0; k < blockSize; k++) {
                    System.arraycopy(matrix[i * blockSize + k], j * blockSize, blocks[i][j][k], 0, blockSize);
                }
            }
        }

        return blocks;
    }
}
