from src.repositories.abstract import IRepository


# class which inherits from IRepository and uses to interact with Product collection in MongoDB.
class ProductRepository(IRepository):
    _collection_name = "Product"
