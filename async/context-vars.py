import asyncio
import threading
import time
import random
from contextvars import ContextVar

request_id = ContextVar('request_id', default='no-id')


def do_word_sync(request: str) -> None:
    request_id.set(request)

    print(f"DOING WORK FOR REQUEST {request_id.get()}")
    time.sleep(random.randint(1, 4))
    print(f"FINISHED WORK FOR REQUEST {request_id.get()}")


async def do_work_async(request: str) -> None:
    pass
