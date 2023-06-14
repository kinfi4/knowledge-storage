package MPI.CascadeSum;

import mpi.MPI;
import mpi.MPIException;

public class CascadeSum__many2many {
    static final int MASTER = 0;
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();

        int arraySize = 16;
        int[] array = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            array[i] = i;
        }

        int localSum = 0;

        int[] recvBuf = new int[2];
        MPI.COMM_WORLD.Scatter(array, 0, 2, MPI.INT, recvBuf, 0, 2, MPI.INT, MASTER);
        for (int i=0; i<2; i++){
            localSum += recvBuf[i];
        }
        System.out.println(localSum);

        int[] globalSum = new int[1];
        MPI.COMM_WORLD.Reduce(new int[] { localSum }, 0, globalSum, 0,  1, MPI.INT, MPI.SUM, 0);

        if (rank == 0) {
            System.out.println("Global sum: " + globalSum[0]);
        }

        MPI.Finalize();
    }
}