import numpy as np
from mpi4py import MPI


if __name__ == '__main__':
    # MPI.Init()

    comm = MPI.COMM_WORLD
    rank = comm.Get_rank()
    processors = comm.Get_size()

    MASTER = 0
    MATRIX_SIZE_1 = 2000
    MATRIX_SIZE_2 = 2000
    MATRIX_SIZE_3 = 2000

    if rank == MASTER:
        print(f"MPI HAS STARTED WITH: {processors} PROCESSES")

        start_time = MPI.Wtime()

        A = np.random.random((MATRIX_SIZE_1, MATRIX_SIZE_2)) * 100
        B = np.random.random((MATRIX_SIZE_2, MATRIX_SIZE_3)) * 100
        C = np.zeros((MATRIX_SIZE_1, MATRIX_SIZE_3))

        worker_processes = processors - 1
        rows_per_process = MATRIX_SIZE_1 // worker_processes
        extra_rows = MATRIX_SIZE_1 % worker_processes

        current_lower_bound = 0
        for destination_process in range(1, processors):
            rows_to_send = rows_per_process + (1 if destination_process <= extra_rows else 0)
            upper_bound = current_lower_bound + rows_to_send

            comm.send(rows_to_send, dest=destination_process, tag=0)

            sub_matrix_rows = []
            for rows_idx in range(current_lower_bound, upper_bound):
                sub_matrix_rows.append(A[rows_idx, :])

            sub_matrix_rows = np.array(sub_matrix_rows)

            comm.Send([sub_matrix_rows, len(sub_matrix_rows)*MATRIX_SIZE_2, MPI.DOUBLE], dest=destination_process, tag=1)
            comm.Send([B, MPI.DOUBLE], dest=destination_process, tag=2)

            current_lower_bound = upper_bound

        current_lower_bound = 0
        for worker_process in range(1, processors):
            rows_to_receive = rows_per_process + (1 if worker_process <= extra_rows else 0)
            upper_bound = current_lower_bound + rows_to_receive

            recv_buffer = np.zeros((rows_to_receive, MATRIX_SIZE_3), dtype=np.double)
            comm.Recv([recv_buffer, MPI.DOUBLE], source=worker_process, tag=3)

            C[current_lower_bound:upper_bound, :] = recv_buffer

            current_lower_bound = upper_bound

        end_time = MPI.Wtime()
        print(
            f"MPI HAS FINISHED IN: {end_time - start_time} SECONDS FOR {processors} "
            f"PROCESSES FOR {(MATRIX_SIZE_1, MATRIX_SIZE_2)}x{MATRIX_SIZE_2, MATRIX_SIZE_3} MATRIX"
        )

        print("VERIFYING THE RESULT: ", np.allclose(C, np.dot(A, B)))
    else:
        rows_count = comm.recv(source=MASTER, tag=0)
        A = np.empty((rows_count, MATRIX_SIZE_2), dtype=np.double)
        B = np.empty((MATRIX_SIZE_2, MATRIX_SIZE_3), dtype=np.double)

        comm.Recv([A, MPI.DOUBLE], source=MASTER, tag=1)
        comm.Recv([B, MPI.DOUBLE], source=MASTER, tag=2)

        s = MPI.Wtime()
        C = np.dot(A, B)
        e = MPI.Wtime()

        print("TIME TOOK TO MULTIPLY: ", e - s, " SIZE: ", A.shape, B.shape)

        comm.Send([C, MPI.DOUBLE], dest=0, tag=3)

    # MPI.Finalize()
