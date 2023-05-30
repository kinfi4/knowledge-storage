import mpi.*;

import java.util.Arrays;

public class Collect {
    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int[] temperatureData = new int[30];
        int sourceRank = 3;
        int tag = 0;

        if (rank == sourceRank) {
            for (int i = 0; i < temperatureData.length; i++) {
                temperatureData[i] = i + 1;
            }

            MPI.COMM_WORLD.Send(temperatureData, 0, temperatureData.length, MPI.INT, 0, tag);
        } else if (rank == 0) {
            MPI.COMM_WORLD.Recv(temperatureData, 0, temperatureData.length, MPI.INT, sourceRank, tag);

            System.out.println("Temperature data received " + Arrays.toString(temperatureData));
        }
        MPI.Finalize();
    }
}
