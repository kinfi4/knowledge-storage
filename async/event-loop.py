import asyncio
from datetime import datetime


def trampoline(name: str) -> None:
    print(f"Now in {name}: {datetime.now()}")
    loop.call_later(0.5, trampoline, name)


if __name__ == '__main__':
    loop = asyncio.get_event_loop()

    loop.call_soon(trampoline, 'First')
    loop.call_soon(trampoline, 'Second')
    loop.call_soon(trampoline, 'Third')
    loop.call_later(5, loop.stop)

    loop.run_forever()
