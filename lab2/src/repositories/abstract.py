from abc import ABC
# used to define abstract base classes in Python, which are classes that cannot be instantiated directly 
# but instead define a set of methods that must be implemented by subclasses

from typing import Any


from sqlalchemy import Engine, update
from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError, DataError
# Engine: a class that represents a database connection engine
# update: a function that generates an SQL UPDATE statement
# Session: a class that provides a high-level interface for interacting with a database
# IntegrityError: an exception that is raised when an integrity constraint is violated
# DataError: an exception that is raised when data in a database column does not match the expected format or data type.

from src.exceptions import InfrastructureException, InvalidDataError, RelationError
# Exceptions needed for handling

from src.tables import Base
# base class for all declarative models in SQLAlchemy


class IRepository(ABC):
    _table_obj: Base

    def __init__(self, engine: Engine):
        self._engine = engine

    # The methods add(), delete(), update(), get_all(), and get_one() are implemented with SQLAlchemy ORM queries. 
    # These methods provide a CRUD set of operations that are generally needed for interacting with a database.

    def add(self, data: dict[str, Any]) -> None:
    	"""
    	    Implementation for adding new record to the database
    	"""
        with Session(self._engine) as session:
            product_obj = self._table_obj(**data)

            try:
                session.add(product_obj)
                session.commit()
            except IntegrityError:
                raise RelationError(f"Impossible to add this {self._table_obj.__name__}")
            except DataError:
                raise InvalidDataError("Invalid input")
            except Exception as err:
                raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def delete(self, _id: int) -> None:
    	"""
    	    Implementation for deleting the record from the table
    	"""
        with Session(self._engine) as session:
            product_to_delete = session.query(self._table_obj).get(_id)

            try:
                session.delete(product_to_delete)
                session.commit()
            except Exception as err:
                raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def update(self, _id: int, data: dict[str, Any]):
    	"""
    	    Implementation for updating the record by id
    	"""
        with Session(self._engine) as session:
            try:
                session.execute(
                    update(self._table_obj).where(self._table_obj.id == _id).values(**data)
                )
                session.commit()
            except IntegrityError:
                raise RelationError(f"Impossible to add this {self._table_obj.__name__}")
            except DataError:
                raise InvalidDataError("Invalid input")
            except Exception as err:
                raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def get_all(self) -> list[dict[str, Any]]:
    	"""
    	    Method for getting all rows of the table
    	    Returns: list of all records in the table
    	"""
        with Session(self._engine) as session:
            try:
                return [row.__dict__ for row in session.query(self._table_obj).all()]
            except Exception as err:
                raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def get_one(self, _id: int) -> dict[str, Any]:
    	"""
    	    Method for getting a single record from the database
    	    Returns: a dict representing a single
    	"""
        with Session(self._engine) as session:
            try:
                return session.query(self._table_obj).get(_id)
            except Exception as err:
                raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")
