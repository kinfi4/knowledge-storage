import numpy as np
from mpi4py import MPI


if __name__ == '__main__':
    # MPI.Init()

    comm = MPI.COMM_WORLD
    rank = comm.Get_rank()
    processors = comm.Get_size()

    MASTER = 0
    MATRIX_SIZE_1 = 1000
    MATRIX_SIZE_2 = 1000
    MATRIX_SIZE_3 = 1000

    if rank == MASTER:
        print(f"MPI HAS STARTED WITH: {processors} PROCESSES")

        start_time = MPI.Wtime()

        A = np.random.random((MATRIX_SIZE_1, MATRIX_SIZE_2)) * 100
        B = np.random.random((MATRIX_SIZE_2, MATRIX_SIZE_3)) * 100
        C = np.zeros((MATRIX_SIZE_1, MATRIX_SIZE_3))

        worker_processes = processors - 1
        rows_per_process = MATRIX_SIZE_1 // worker_processes
        extra_rows = MATRIX_SIZE_1 % worker_processes

        requests: list[MPI.Request] = []

        current_lower_bound = 0
        for destination_process in range(1, processors):
            rows_to_send = rows_per_process + (1 if destination_process <= extra_rows else 0)
            upper_bound = current_lower_bound + rows_to_send

            comm.isend(rows_to_send, dest=destination_process, tag=0)

            requests.append(comm.Isend([A[current_lower_bound:upper_bound, :], MPI.DOUBLE], dest=destination_process, tag=1))
            requests.append(comm.Isend([B, MPI.DOUBLE], dest=destination_process, tag=2))

            current_lower_bound = upper_bound

        current_lower_bound = 0
        for worker_process in range(1, processors):
            rows_to_receive = rows_per_process + (1 if worker_process <= extra_rows else 0)
            upper_bound = current_lower_bound + rows_to_receive

            requests.append(comm.Irecv([C[current_lower_bound:upper_bound, :], MPI.DOUBLE], source=worker_process, tag=3))

            current_lower_bound = upper_bound

        end_time = MPI.Wtime()
        print(
            f"MPI HAS FINISHED IN: {end_time - start_time} SECONDS FOR {processors} "
            f"PROCESSES FOR {(MATRIX_SIZE_1, MATRIX_SIZE_2)}x{MATRIX_SIZE_2, MATRIX_SIZE_3} MATRIX"
        )

        MPI.Request.Waitall(requests)

        print("VERIFYING THE RESULT: ", np.allclose(C, np.dot(A, B)))
    else:
        rows_count = comm.recv(source=MASTER, tag=0)
        A = np.empty((rows_count, MATRIX_SIZE_2), dtype=np.double)
        B = np.empty((MATRIX_SIZE_2, MATRIX_SIZE_3), dtype=np.double)

        request_a = comm.Irecv([A, MPI.DOUBLE], source=MASTER, tag=1)
        request_b = comm.Irecv([B, MPI.DOUBLE], source=MASTER, tag=2)

        # print("RECEIVED MATRIX PART FROM MASTER PROCESS: ", A.shape, B.shape)

        request_a.wait()
        request_b.wait()

        C = np.dot(A, B)

        comm.Send([C, MPI.DOUBLE], dest=0, tag=3)

    # MPI.Finalize()
