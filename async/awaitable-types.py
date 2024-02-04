import asyncio
from datetime import datetime


async def print_now(name: str) -> None:
    while True:
        print(f"Now in {name}: {datetime.now()}")
        await asyncio.sleep(0.5)


async def get_now_string(name: str) -> str:
    now = datetime.now()
    now_string = f"Now in {name}: {now}"
    await asyncio.sleep(0.5)
    return now_string


async def main() -> None:
    awaitable = asyncio.wait_for(print_now("First"), 1)
    awaitable_task = asyncio.create_task(get_now_string("Second"))

    try:
        print(awaitable)
        print(awaitable_task)
        result = await awaitable_task
        print("The result is:", result)

        await awaitable
    except asyncio.TimeoutError:
        print("Timeout!")


if __name__ == '__main__':
    asyncio.run(main())
