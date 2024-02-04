import sys


CONSTANT = 42

class MyClass:
    pass


def function(var: str) -> None:
    inside_var = 23
    locals()['inside_var'] = 42  # doesn't work, inside_var will still be 23
    print(locals())


obj = MyClass()

print(globals())
function("HELLO")
