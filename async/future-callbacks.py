import asyncio


# this can't be async
def print_callback(tsk: asyncio.Task) -> None:
    print("CALLBACKS IS RUNNING")


async def async_task(name: str) -> int:
    print(f"RUNNING {name} BEFORE SLEEP")
    await asyncio.sleep(1)
    print(f"RUNNING {name} AFTER SLEEP")

    return 1


async def main() -> None:
    print("THE MAIN STARTED")

    task = asyncio.Task(async_task("some-name"))
    task.add_done_callback(print_callback)

    result = await task

    print("WE GOT THE RESULT:", result)


if __name__ == "__main__":
    asyncio.run(main())
