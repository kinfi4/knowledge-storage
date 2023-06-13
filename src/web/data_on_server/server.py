from multiprocessing import Pool

import numpy as np
from flask import Flask

app = Flask(__name__)

MATRIX_SIZE = 999
PROCESSORS = 8

MATRIX = np.random.randint(0, 10000, (MATRIX_SIZE, MATRIX_SIZE))


def find_average_per_row(matrix: np.ndarray) -> np.ndarray:
    averages: list[float] = []

    for row in matrix:
        averages.append(np.sum(row) / len(row))

    return np.array(averages)


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


@app.route("/request", methods=["POST"])
def matrix_static_multiply() -> dict:
    chunks = _get_matrix_chunks(MATRIX)
    chunks = [(chunk,) for chunk in chunks]

    with Pool() as pool:
        results = pool.starmap(find_average_per_row, chunks)

    result = np.concatenate([matrix_multiplication_result for matrix_multiplication_result in results])

    return {"result_array": result.tolist()}


if __name__ == "__main__":
    app.run(host="localhost", port=5000, debug=True)
