import gc
import sys


class B:
    def __init__(self, a):
        self.a = a


class A:
    def __init__(self):
        self.b = B(self)


def does_object_exists(object_address):
    for obj in gc.get_objects():
        if hex(id(obj)) == object_address:
            return "Object exists"

    return "Object does not exist"


if __name__ == "__main__":
    gc.disable()

    obj_ref = A()

    a_addr = hex(id(obj_ref))
    b_addr = hex(id(obj_ref.b))

    print("The addresses:")
    print(hex(id(obj_ref)))
    print(hex(id(obj_ref.b)))
    print(hex(id(obj_ref.b.a)))
    print("====================================")

    print("The reference count:")
    print(sys.getrefcount(obj_ref))
    print(sys.getrefcount(obj_ref.b))
    print("====================================")

    obj_ref = None

    print(does_object_exists(a_addr))
    print(does_object_exists(b_addr))

    print("Collecting...")
    gc.collect()

    print(does_object_exists(a_addr))
    print(does_object_exists(b_addr))
