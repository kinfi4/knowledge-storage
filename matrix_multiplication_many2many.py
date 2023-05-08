import numpy as np
from mpi4py import MPI

if __name__ == '__main__':
    # MPI.Init()

    comm = MPI.COMM_WORLD
    rank = comm.Get_rank()
    processors_count = comm.Get_size()

    MASTER = 0
    MATRIX_SIZE_1 = 2000
    MATRIX_SIZE_2 = 2000
    MATRIX_SIZE_3 = 2000

    start_time = MPI.Wtime()
    A = np.zeros((MATRIX_SIZE_1, MATRIX_SIZE_2))
    B = np.zeros((MATRIX_SIZE_2, MATRIX_SIZE_3))

    if rank == MASTER:
        print(f"MPI HAS STARTED WITH: {processors_count} PROCESSES")

        A = np.random.random((MATRIX_SIZE_1, MATRIX_SIZE_2)) * 100
        B = np.random.random((MATRIX_SIZE_2, MATRIX_SIZE_3)) * 100

    rows_per_process = MATRIX_SIZE_1 // processors_count
    elements_per_process = (MATRIX_SIZE_1*MATRIX_SIZE_2) // processors_count

    sub_matrix_A = np.empty((rows_per_process, MATRIX_SIZE_2))
    comm.Scatter([A, elements_per_process, MPI.DOUBLE], sub_matrix_A, root=MASTER)

    comm.Bcast([B, MATRIX_SIZE_2*MATRIX_SIZE_3, MPI.DOUBLE], root=MASTER)

    product = np.dot(sub_matrix_A, B)

    C = np.empty((MATRIX_SIZE_1, MATRIX_SIZE_3))
    comm.Allgatherv([product, product.size, MPI.DOUBLE], [C, elements_per_process, MPI.DOUBLE])

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
