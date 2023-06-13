from time import perf_counter
from multiprocessing import Pool

import numpy as np
from flask import Flask, request

app = Flask(__name__)

MATRIX_SIZE = 2000
PROCESSORS = 8


def _get_matrix_chunks(matrix: np.ndarray) -> list[np.ndarray]:
    rows_per_process = MATRIX_SIZE // PROCESSORS
    extra_rows = MATRIX_SIZE % PROCESSORS

    offset = 0
    chunks = []
    for process in range(PROCESSORS):
        rows = rows_per_process + (1 if process < extra_rows else 0)
        chunks.append(matrix[offset:offset + rows])
        offset += rows

    return chunks


@app.route("/mult-matrix", methods=["POST"])
def matrix_multiply() -> dict:
    start_time = perf_counter()

    mat1 = np.array(request.json["matrix1"])
    mat2 = np.array(request.json["matrix2"])

    chunks = _get_matrix_chunks(mat1)

    with Pool() as pool:
        arguments = zip(chunks, [mat2] * len(chunks))
        results = pool.starmap(matrix_multiplication_subtask, arguments)

    result = np.concatenate([matrix_multiplication_result for matrix_multiplication_result in results])

    return {"result_matrix": result.tolist(), "server_start_time": start_time}
