import random
import asyncio


MAX_QUEUE_SIZE = 10


async def consumer(queue: asyncio.Queue) -> None:
    while True:
        item = await queue.get()
        await asyncio.sleep(random.random()/2)
        queue.task_done()  # Notify the queue that the item has been processed


async def producer(queue: asyncio.Queue) -> None:
    for i in range(100):
        if queue.qsize() > MAX_QUEUE_SIZE:
            print(f"Queue size is {queue.qsize()}. Waiting for consumers")
            await queue.join()
            print(f"Queue size is {queue.qsize()}. Resuming")

        await queue.put(i)


if __name__ == "__main__":
    q = asyncio.Queue()

    loop = asyncio.get_event_loop()

    loop.run_until_complete(
        asyncio.gather(
            producer(q),
            consumer(q)
        ),
    )