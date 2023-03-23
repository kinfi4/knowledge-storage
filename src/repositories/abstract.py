from abc import ABC
# used to define abstract base classes in Python, which are classes that cannot be instantiated directly 
# but instead define a set of methods that must be implemented by subclasses
from typing import Any

from bson import ObjectId
from pymongo import MongoClient  # mongo db client for interacting with database
from pymongo.collection import Collection

from src.exceptions import InfrastructureException
from src.config import MONGO_DB_CONNECTION_STRING


class IRepository(ABC):
    """
        The methods add(), delete(), update(), get_all(), and get_one() are implemented with pymongo.
        These methods provide a CRUD set of operations that are generally needed for interacting with a MongoDB.
    """

    _collection_name: str

    def __init__(self):
        self._client = MongoClient(MONGO_DB_CONNECTION_STRING)
        self._db = self._client.shop

        self._collection: Collection = self._db[self._collection_name]

    def add(self, data: dict[str, Any]) -> None:
        """
            Implementation for adding new record to the database
        """

        try:
            self._collection.insert_one(data)
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def delete(self, _id: str) -> None:
        """
            Implementation for deleting the record from the table
        """

        try:
            self._collection.delete_one({"_id": ObjectId(_id)})
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def update(self, _id: str, data: dict[str, Any]):
        """
            Implementation for updating the record by id
        """
        try:
            self._collection.replace_one({"_id": ObjectId(_id)}, data)
        except Exception as err:
            print(err)
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def get_all(self) -> list[dict[str, Any]]:
        """
            Method for getting all rows of the table
            Returns: list of all records in the table
        """

        try:
            return list(self._collection.find())
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def get_one(self, _id: str) -> dict[str, Any]:
        """
            Method for getting a single record from the database
            Returns: a dict representing a single
        """

        try:
            return self._collection.find_one({"_id": ObjectId(_id)})
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def delete_all(self) -> None:
        """
            Deletes all records from collection
        """

        try:
            return self._collection.delete_many({})
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")
