import numpy as np
from mpi4py import MPI

if __name__ == '__main__':
    # MPI.Init()

    comm = MPI.COMM_WORLD
    rank = comm.Get_rank()
    processors_count = comm.Get_size()

    MASTER = 0
    MATRIX_SIZE_1 = 5000
    MATRIX_SIZE_2 = 5000
    MATRIX_SIZE_3 = 5000

    start_time = MPI.Wtime()
    A = np.zeros((MATRIX_SIZE_1, MATRIX_SIZE_2), dtype=np.int32)
    B = np.zeros((MATRIX_SIZE_2, MATRIX_SIZE_3), dtype=np.int32)

    if rank == MASTER:
        print(f"MPI HAS STARTED WITH: {processors_count} PROCESSES")

        A = np.random.randint(0, 10, (MATRIX_SIZE_1, MATRIX_SIZE_2), dtype=np.int32)
        B = np.random.randint(0, 10, (MATRIX_SIZE_2, MATRIX_SIZE_3), dtype=np.int32)

    A_flattened = A.flatten()

    assert len(A_flattened) == MATRIX_SIZE_1 * MATRIX_SIZE_2

    rows_per_process = MATRIX_SIZE_1 // processors_count
    extra_rows = MATRIX_SIZE_1 % processors_count

    counts = np.array([
        (rows_per_process + 1) * MATRIX_SIZE_2 if process_rank < extra_rows else rows_per_process * MATRIX_SIZE_2
        for process_rank in range(processors_count)
    ], dtype=np.int32)
    displs = np.zeros(processors_count, dtype=np.int32)
    displs[1:] = np.cumsum(counts[:-1])

    sub_matrix_A = np.empty(counts[rank], dtype=np.int32)
    comm.Scatterv([A_flattened, counts, displs, MPI.INT], sub_matrix_A, root=MASTER)
    sub_matrix_A = sub_matrix_A.reshape((-1, MATRIX_SIZE_2))

    comm.Bcast([B, MPI.INT], root=MASTER)

    product = np.dot(sub_matrix_A, B)
    product_flattened = product.flatten()

    C_flattened = np.empty(MATRIX_SIZE_1 * MATRIX_SIZE_3, dtype=np.int32)
    comm.Allgatherv(product_flattened, [C_flattened, counts, displs, MPI.INT])

    C = C_flattened.reshape((MATRIX_SIZE_1, MATRIX_SIZE_3))

    if rank == MASTER:
        end_time = MPI.Wtime()
        print(
            f"MPI HAS FINISHED IN: {end_time - start_time} SECONDS FOR {processors_count} "
            f"PROCESSES FOR {(MATRIX_SIZE_1, MATRIX_SIZE_2)}x{MATRIX_SIZE_2, MATRIX_SIZE_3} MATRIX"
        )

        print("VERIFYING THE RESULT: ", np.allclose(C, np.dot(A, B)))
    else:
        assert C.size != 0

    # MPI.Finalize()

# one2many: scatter, bcast
# many2one: gather, reduce
# many2many: scatter, allgather
