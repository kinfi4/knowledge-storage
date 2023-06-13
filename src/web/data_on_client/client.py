import random
from time import perf_counter

import requests


def _generate_traffic_data() -> list:
    days = 1000
    frames = 288

    return [
        [random.randint(0, 3) for _ in range(frames)] for _ in range(days)
    ]


def test_endpoint_speed(endpoint: str, data: dict) -> None:
    print(data["traffic_data"])
    start_time = perf_counter()

    response = requests.post(endpoint, json=data)

    end_time = perf_counter()

    print(f"Time taken: {end_time - start_time}")
    print("Response: ", response.json())


if __name__ == "__main__":
    test_endpoint_speed('http://localhost:5000/request', data={
        "traffic_data": _generate_traffic_data()
    })
