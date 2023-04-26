from src.repositories.abstract import IRepository
from src.tables import Order

# class which inherits from IRepository and uses the Order SQLAlchemy Table object.
class OrderRepository(IRepository):
    _table_obj = Order
