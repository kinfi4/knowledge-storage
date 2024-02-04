# import importlib
# import pkgutil
#
#
# print(list(pkgutil.iter_modules(["../async"])))  # get the list of modules in the package

import sys

print("math" in sys.modules)  # False

import math
print("math" in sys.modules)  # True

math_module = sys.modules['math']
print(math_module.sqrt(9))  # Output: 3.0

# Manipulating sys.modules (not recommended)
sys.modules['math'] = None  # Removing math module from cache
print(math.sqrt(9))  # we still have access to math module, but it will be reloaded again if we import it again
