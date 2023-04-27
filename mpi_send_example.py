from mpi4py import MPI

comm = MPI.COMM_WORLD

rank = comm.Get_rank()
size = comm.Get_size()


if rank == 0:
    print("TOTAL SIZE OF PROCESS: ", size)
    data = {'a': 7, 'b': 3.14}
    comm.send(data, dest=1, tag=11)
    comm.send(data, dest=2, tag=12)
elif rank == 1:
    data = comm.recv(source=0, tag=11)
    print(f"Process {rank} received data: {data}")
elif rank == 2:
    data = comm.recv(source=0, tag=12)
    print(f"Process {rank} received data: {data}")
else:
    print(f"Process {rank} does not receive data")
