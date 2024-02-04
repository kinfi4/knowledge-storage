import threading
import asyncio


async def async_task() -> None:
    print(f"Async task is running from thread: {threading.current_thread().name} the event loop id: {id(asyncio.get_event_loop())}")
    await asyncio.sleep(1)
    print(f"Async task from thread: {threading.current_thread().name} is done")


def another_thread_task() -> None:
    new_loop = asyncio.new_event_loop()
    asyncio.set_event_loop(new_loop)

    print("Another thread is running")
    new_loop.run_until_complete(async_task())


if __name__ == '__main__':
    loop = asyncio.get_event_loop()

    t1 = threading.Thread(target=another_thread_task)
    t2 = threading.Thread(target=another_thread_task)
    t1.start()
    t2.start()

    loop.run_until_complete(async_task())

    t1.join()
    t2.join()
