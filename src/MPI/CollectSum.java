package MPI;

import mpi.*;

public class CollectSum {
    public static void main(String[] args) {
        int myRank;
        int[] arr = {1, 2, 3, 4, 5};
        int localSum = 0;

        MPI.Init(args);
        myRank = MPI.COMM_WORLD.Rank();

        for (int j : arr) {
            localSum += j;
        }

        int[] collectSum = new int[1];
        MPI.COMM_WORLD.Allreduce(new int[] {localSum}, 0, collectSum, 0, 1, MPI.INT, MPI.SUM);
        int SUM = collectSum[0];

        if (myRank == 0) {
            System.out.println("Total sum: " + SUM);
        }

        MPI.Finalize();
    }
}
