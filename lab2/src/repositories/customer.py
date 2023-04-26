from src.repositories.abstract import IRepository
from src.tables import Customer

# class which inherits from IRepository and uses the Customer SQLAlchemy Table object.
class CustomerRepository(IRepository):
    _table_obj = Customer
