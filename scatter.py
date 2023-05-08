from mpi4py import MPI
import numpy as np

comm = MPI.COMM_WORLD
rank = comm.Get_rank()

# Define the array to scatter
if rank == 0:
    a = np.arange(1000, dtype=np.int32)
else:
    a = None

# Define the sendcounts and displs arrays
sendcounts = np.array([200, 400, 300, 100], dtype=np.int32)
displs = np.array([0, 200, 600, 900], dtype=np.int32)

# Scatter the array using Scatterv
recvbuf = np.empty(sendcounts[rank], dtype=np.int32)
comm.Scatterv([a, sendcounts, displs, MPI.INT], recvbuf, root=0)

print(recvbuf)
