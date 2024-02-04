def function(a, b, c, *args, **kwargs):
    print(a, b, c)
    print(args)
    print(kwargs)


function(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, key1="value1", key2="value2")
function(2, 3, c=4, d=3, key1="value1", key2="value2")
