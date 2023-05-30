import mpi.*;

import java.util.Arrays;
import java.util.Random;

public class Blocked {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        var comm = MPI.COMM_WORLD;

        int rank = comm.Rank();
        int processors = comm.Size();

        final int MASTER = 0;
        final int MATRIX_SIZE_1 = 1000;
        final int MATRIX_SIZE_2 = 1000;
        final int MATRIX_SIZE_3 = 1000;

        if (rank == MASTER) {
            System.out.println("MPI HAS STARTED WITH: " + processors + " PROCESSES");

            long start_time = System.nanoTime();

            double[][] A = generateRandomMatrix(MATRIX_SIZE_1, MATRIX_SIZE_2);
            double[][] B = generateRandomMatrix(MATRIX_SIZE_2, MATRIX_SIZE_3);
            double[][] C = new double[MATRIX_SIZE_1][MATRIX_SIZE_3];

            int worker_processes = processors - 1;
            int rows_per_process = MATRIX_SIZE_1 / worker_processes;
            int extra_rows = MATRIX_SIZE_1 % worker_processes;

            int current_lower_bound = 0;
            for (int destination_process = 1; destination_process < processors; destination_process++) {
                int rows_to_send = rows_per_process + ((destination_process <= extra_rows) ? 1 : 0);
                int upper_bound = current_lower_bound + rows_to_send;

                int[] rowsToSend = new int[] { rows_to_send };
                comm.Send(rowsToSend, 0, 1, MPI.INT, destination_process, 0);

                double[][] send_buffer = Arrays.copyOfRange(A, current_lower_bound, upper_bound);
                comm.Send(send_buffer, 0, send_buffer.length, MPI.OBJECT, destination_process, 1);
                comm.Send(B, 0, B.length, MPI.OBJECT, destination_process, 2);

                current_lower_bound = upper_bound;
            }

            current_lower_bound = 0;
            for (int worker_process = 1; worker_process < processors; worker_process++) {
                int rows_to_receive = rows_per_process + ((worker_process <= extra_rows) ? 1 : 0);
                int upper_bound = current_lower_bound + rows_to_receive;

                double[][] recv_buffer = new double[rows_to_receive][MATRIX_SIZE_3];
                comm.Recv(recv_buffer, 0, recv_buffer.length, MPI.OBJECT, worker_process, 3);

                for (int i = current_lower_bound; i < upper_bound; i++) {
                    C[i] = recv_buffer[i - current_lower_bound];
                }

                current_lower_bound = upper_bound;
            }

            long end_time = System.nanoTime();
            System.out.println(String.format("MPI HAS FINISHED IN: %f SECONDS FOR %d PROCESSES FOR %dx%d MATRIX",
                    (end_time - start_time) / 1e9, processors, MATRIX_SIZE_1, MATRIX_SIZE_3));

             System.out.println("VERIFYING THE RESULT: " + Arrays.deepEquals(C, matrixMultiplication(A, B)));
        } else {
            int[] rows_count_buffer = new int[1];
            comm.Recv(rows_count_buffer, 0, 1, MPI.INT, MASTER, 0);
            int rows_count = rows_count_buffer[0];

            double[][] A = new double[rows_count][MATRIX_SIZE_2];
            double[][] B = new double[MATRIX_SIZE_2][MATRIX_SIZE_3];

            comm.Recv(A, 0, rows_count * MATRIX_SIZE_2, MPI.DOUBLE, MASTER, 1);
            comm.Recv(B, 0, MATRIX_SIZE_2 * MATRIX_SIZE_3, MPI.DOUBLE, MASTER, 2);

            // Matrix multiplication
            double[][] C = new double[rows_count][MATRIX_SIZE_3];
            for (int i = 0; i < rows_count; i++) {
                for (int j = 0; j < MATRIX_SIZE_3; j++) {
                    double sum = 0;
                    for (int k = 0; k < MATRIX_SIZE_2; k++) {
                        sum += A[i][k] * B[k][j];
                    }
                    C[i][j] = sum;
                }
            }

            // Send the result back to the master process
            comm.Send(C, 0, rows_count * MATRIX_SIZE_3, MPI.DOUBLE, MASTER, 3);
        }

        MPI.Finalize();
    }

    // Generates a random matrix of given size
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


}