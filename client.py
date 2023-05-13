from time import perf_counter
from typing import Optional

import requests
import numpy as np


def _generate_matrix(matrix_size: int) -> np.ndarray:
    return np.random.randint(0, 1000, size=(matrix_size, matrix_size))


def test_endpoint_speed(endpoint: str, data: Optional[dict]) -> None:
    start_time = perf_counter()
    response = requests.post(endpoint, json=data)

    end_time = perf_counter()

    print(f"Time taken: {end_time - start_time}")

    server_start_time = response.json().get("server_start_time")

    if server_start_time is not None:
        print("TIME TAKEN FOR DATA TRANSFER: ", server_start_time - start_time)
        print("VALIDATION: ", np.array_equal(np.dot(data["matrix1"], data["matrix2"]), np.array(response.json()["result_matrix"])))


if __name__ == "__main__":
    # test_endpoint_speed('http://localhost:5000/mult-static-matrix', {})
    test_endpoint_speed('http://localhost:5000/mult-matrix', data={
        "matrix1": _generate_matrix(1000).tolist(),
        "matrix2": _generate_matrix(1000).tolist()
    })
