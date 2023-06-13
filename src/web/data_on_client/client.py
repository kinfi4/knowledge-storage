from time import perf_counter

import requests
import numpy as np


def _generate_matrix(matrix_size: int) -> np.ndarray:
    return np.random.randint(0, 1000, size=(matrix_size, matrix_size))


def test_endpoint_speed(endpoint: str, data: dict) -> None:
    start_time = perf_counter()

    response = requests.post(endpoint, json=data)

    end_time = perf_counter()

    print(f"Time taken: {end_time - start_time}")
    print("Response: ", response.json())


if __name__ == "__main__":
    test_endpoint_speed('http://localhost:5000/mult-matrix', data={
        "matrix1": _generate_matrix(1000).tolist(),
        "matrix2": _generate_matrix(1000).tolist()
    })
