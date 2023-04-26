from src.repositories.abstract import IRepository
from src.tables import Product

# class which inherits from IRepository and uses the Product SQLAlchemy Table object.
class ProductRepository(IRepository):
    _table_obj = Product
