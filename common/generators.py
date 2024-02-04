# method send(), throw(), next(), close()
# send() - send data to generator
# throw() - throw exception to generator
# next() - get next value from generator
# close() - close generator

def gen():
    """
    Generator function gets 5 values and print them.
    """

    for i in range(5):
        try:
            x = yield
            print(x)
        except Exception as e:
            print(e)


g = gen()
g.send(None)  # the same as next(g). The first call of generator must be without parameter
g.send(10)
g.send("String")

g.throw(ValueError("HELLO"))

g.close()

try:
    g.send("HE")
except StopIteration:
    print("Generator is closed")
