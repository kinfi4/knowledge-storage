package MPI;
import mpi.MPI;


public class ModifiedCascadeSum__one2one {
    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int processorsCount = MPI.COMM_WORLD.Size();
        int workersCount = processorsCount - 1;
        int MASTER = 0;
        int ARR_SIZE = 1000;

        int[] arr = new int[ARR_SIZE];

        int elementsPerProcess = ARR_SIZE / workersCount;
        int extraElements = ARR_SIZE % workersCount;

        if (rank == MASTER) {
            for (int i = 0; i < ARR_SIZE; i++) {  // filling the array
                arr[i] = i + 1;
            }

            int offset = 0;
            for (int process = 1; process < processorsCount; process++) {
                int sendCount = elementsPerProcess + (process - 1 < extraElements ? 1 : 0);

                MPI.COMM_WORLD.Send(arr, offset, sendCount, MPI.INT, process, 0);

                offset += sendCount;
            }

            int[] localSums = new int[workersCount];
            for (int process = 1; process < processorsCount; process++) {
                MPI.COMM_WORLD.Recv(localSums, process - 1, 1, MPI.INT, process, 1);
            }

            int totalSum = 0;
            for (int localSum : localSums) {
                totalSum += localSum;
            }

            System.out.println("Total sum: " + totalSum);

            int validationSum = 0;
            for (int i = 0; i < ARR_SIZE; i++) {
                validationSum += arr[i];
            }

            System.out.println("Validation sum: " + validationSum);
        } else {
            int[] recvBuf = new int[elementsPerProcess + (rank - 1 < extraElements ? 1 : 0)];

            MPI.COMM_WORLD.Recv(recvBuf, 0, recvBuf.length, MPI.INT, MASTER, 0);

            int localSum = 0;

            for (int j : recvBuf) {
                localSum += j;
            }

//            System.out.println("Local sum: " + localSum);

            MPI.COMM_WORLD.Send(new int[] { localSum }, 0, 1, MPI.INT, MASTER, 1);
        }

        MPI.Finalize();
    }

}
