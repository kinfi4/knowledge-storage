from src.repositories.abstract import IRepository


# class which inherits from IRepository and uses to interact with Customer collection in MongoDB.
class CustomerRepository(IRepository):
    _collection_name = "Customer"
