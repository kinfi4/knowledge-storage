package MPI.MatrixMultiplication;

import mpi.*;

public class MatrixMultUnblocked {
    public static void main(String[] args) throws MPIException {

    }

//    public static void main(String[] args) throws MPIException {
//    MPI.Init(args);
//
//    var comm = MPI.COMM_WORLD;
//
//    int rank = comm.Rank();
//    int processors = comm.Size();
//
//    final int MASTER = 0;
//    final int MATRIX_SIZE_1 = 1000;
//    final int MATRIX_SIZE_2 = 1000;
//    final int MATRIX_SIZE_3 = 1000;
//
//    if (rank == MASTER) {
//        System.out.println("MPI HAS STARTED WITH: " + processors + " PROCESSES");
//
//        long start_time = System.nanoTime();
//
//        double[][] A = SyncMultiplication.generateRandomMatrix(MATRIX_SIZE_1, MATRIX_SIZE_2);
//        double[][] B = SyncMultiplication.generateRandomMatrix(MATRIX_SIZE_2, MATRIX_SIZE_3);
//        double[][] C = new double[MATRIX_SIZE_1][MATRIX_SIZE_3];
//
//        int worker_processes = processors - 1;
//        int rows_per_process = MATRIX_SIZE_1 / worker_processes;
//        int extra_rows = MATRIX_SIZE_1 % worker_processes;
//
//        int current_lower_bound = 0;
//
//        ArrayList<Request> requests = new ArrayList<>();
//
//        for (int destination_process = 1; destination_process < processors; destination_process++) {
//            int rows_to_send = rows_per_process + ((destination_process <= extra_rows) ? 1 : 0);
//            int upper_bound = current_lower_bound + rows_to_send;
//
//            int[] rowsToSend = new int[] { rows_to_send };
//            comm.Send(rowsToSend, 0, 1, MPI.INT, destination_process, 0);
//
//            double[][] subRows = Arrays.copyOfRange(A, current_lower_bound, upper_bound);
//            double[] A_buffer = new double[rows_to_send * MATRIX_SIZE_2];
//            double[] B_buffer = new double[MATRIX_SIZE_2 * MATRIX_SIZE_3];
//
//            for (int i = 0; i < rows_to_send; i++) {
//                System.arraycopy(subRows[i], 0, A_buffer, i * 1000, MATRIX_SIZE_2);
//            }
//
//            for (int i = 0; i < MATRIX_SIZE_2; i++) {
//                System.arraycopy(B[i], 0, B_buffer, i * 1000, MATRIX_SIZE_3);
//            }
//
//            requests.add(comm.Isend(A_buffer, 0, A_buffer.length, MPI.DOUBLE, destination_process, 1));
//            requests.add(comm.Isend(B_buffer, 0, B_buffer.length, MPI.DOUBLE, destination_process, 2));
//
//            current_lower_bound = upper_bound;
//        }
//
//        current_lower_bound = 0;
//        for (int worker_process = 1; worker_process < processors; worker_process++) {
//            int rows_to_receive = rows_per_process + ((worker_process <= extra_rows) ? 1 : 0);
//            int upper_bound = current_lower_bound + rows_to_receive;
//
//            double[] recvBufferFlattened = new double[rows_to_receive * MATRIX_SIZE_3];
//            comm.Recv(recvBufferFlattened, 0, recvBufferFlattened.length, MPI.DOUBLE, worker_process, 3);
//
//            double[][] recvBuffer = new double[rows_to_receive][MATRIX_SIZE_3];
//            for (int i = 0; i < rows_to_receive; i++) {
//                System.arraycopy(recvBufferFlattened, i * 1000, recvBuffer[i], 0, MATRIX_SIZE_3);
//            }
//
//            if (upper_bound - current_lower_bound >= 0)
//                System.arraycopy(recvBuffer, 0, C, current_lower_bound, upper_bound - current_lower_bound);
//
//            current_lower_bound = upper_bound;
//        }
//
//        long end_time = System.nanoTime();
//        System.out.printf("MPI HAS FINISHED IN: %f SECONDS FOR %d PROCESSES FOR %dx%d MATRIX%n", (end_time - start_time) / 1e9, processors, MATRIX_SIZE_1, MATRIX_SIZE_3);
//        System.out.println("VERIFYING THE RESULT: " + Arrays.deepEquals(C, SyncMultiplication.matrixMultiplication(A, B)));
//    } else {
//        int[] rows_count_buffer = new int[1];
//        comm.Recv(rows_count_buffer, 0, 1, MPI.INT, MASTER, 0);
//        int rows_count = rows_count_buffer[0];
//
//        double[][] A = new double[rows_count][MATRIX_SIZE_2];
//        double[][] B = new double[MATRIX_SIZE_2][MATRIX_SIZE_3];
//
//        double[] A_buffer = new double[rows_count * MATRIX_SIZE_2];
//        double[] B_buffer = new double[MATRIX_SIZE_2 * MATRIX_SIZE_3];
//
//        comm.Recv(A_buffer, 0, rows_count * MATRIX_SIZE_2, MPI.DOUBLE, MASTER, 1);
//        comm.Recv(B_buffer, 0, MATRIX_SIZE_2 * MATRIX_SIZE_3, MPI.DOUBLE, MASTER, 2);
//
//        for (int i = 0; i < rows_count; i++) {
//            System.arraycopy(A_buffer, i * 1000, A[i], 0, MATRIX_SIZE_2);
//        }
//        for (int i = 0; i < MATRIX_SIZE_2; i++) {
//            System.arraycopy(B_buffer, i * 1000, B[i], 0, MATRIX_SIZE_3);
//        }
//
//        // Matrix multiplication
//        double[][] C = new double[rows_count][MATRIX_SIZE_3];
//        for (int i = 0; i < rows_count; i++) {
//            for (int j = 0; j < MATRIX_SIZE_3; j++) {
//                double sum = 0;
//                for (int k = 0; k < MATRIX_SIZE_2; k++) {
//                    sum += A[i][k] * B[k][j];
//                }
//                C[i][j] = sum;
//            }
//        }
//
//        double[] C_buffer = new double[rows_count * MATRIX_SIZE_3];
//        for (int i = 0; i < rows_count; i++) {
//            System.arraycopy(C[i], 0, C_buffer, i * 1000, MATRIX_SIZE_3);
//        }
//        // Send the result back to the master process
//        comm.Send(C_buffer, 0, C_buffer.length, MPI.DOUBLE, MASTER, 3);
//    }
//
//    MPI.Finalize();
//}

}