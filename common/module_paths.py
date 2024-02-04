import sys

print(sys.path)

sys.path.append("../async")

import async_http_requests
print(async_http_requests.main)
