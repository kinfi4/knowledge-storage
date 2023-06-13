from multiprocessing import Pool

import numpy as np
from flask import Flask, request

app = Flask(__name__)

PROCESSORS = 8


def calculate_average_per_day(traffic_data: np.ndarray) -> np.ndarray:
    average_data = []

    for day in traffic_data:
        average_data.append(np.mean(day))

    return np.array(average_data)


def _get_matrix_chunks(matrix: np.ndarray) -> list[np.ndarray]:
    rows_per_process = len(matrix) // PROCESSORS
    extra_rows = len(matrix) % PROCESSORS

    offset = 0
    chunks = []
    for process in range(PROCESSORS):
        rows = rows_per_process + (1 if process < extra_rows else 0)
        chunks.append(matrix[offset:offset + rows])
        offset += rows

    return chunks


@app.route("/request", methods=["POST"])
def matrix_multiply() -> dict:
    traffic_data = np.array(request.json["traffic_data"])

    chunks = _get_matrix_chunks(traffic_data)
    chunks = [(chunk,) for chunk in chunks]

    with Pool() as pool:
        results = pool.starmap(calculate_average_per_day, chunks)

    result = np.concatenate([matrix_multiplication_result for matrix_multiplication_result in results])

    return {"daily_average": result.tolist()}


if __name__ == "__main__":
    app.run(host="localhost", port=5000, debug=True)
