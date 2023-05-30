import mpi.*;

public class CollectSum {
    public static void main(String[] args) {
        int myRank, numProcs;
        int[] arr = {1, 2, 3, 4, 5};
        int localSum = 0;

        MPI.Init(args);
        myRank = MPI.COMM_WORLD.Rank();

        for (int j : arr) {
            localSum += j;
        }

        int[] masterSum = new int[1];
        MPI.COMM_WORLD.Reduce(new int[] {localSum}, 0, masterSum, 0, 1, MPI.INT, MPI.SUM, 0);
        int SUM = masterSum[0];

        if (myRank == 0) {
            System.out.println("Total sum: " + SUM);
        }

        MPI.Finalize();
    }
}
