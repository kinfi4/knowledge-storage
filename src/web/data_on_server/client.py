from time import perf_counter

import requests


def test_endpoint_speed(endpoint: str) -> None:
    start_time = perf_counter()
    response = requests.post(endpoint)

    end_time = perf_counter()

    print(f"Time taken: {end_time - start_time}")
    print("Response: ", response.json())


if __name__ == "__main__":
    test_endpoint_speed('http://localhost:5000/request')
