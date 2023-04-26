from src.repositories.abstract import IRepository


# class which inherits from IRepository and uses to interact with Order collection in MongoDB.
class OrderRepository(IRepository):
    _collection_name = "Order"
