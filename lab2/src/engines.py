from sqlalchemy import create_engine, Engine

# The second import statement imports the Engine class from SQLAlchemy. 
# This class represents a connection to a database and provides methods for executing SQL commands and interacting with the database.

from src.config import MYSQL_URL, POSTGRES_URL, SQLITE_URL
# Urls config

def mysql_engine_factory() -> Engine:
    return create_engine(MYSQL_URL)


def postgres_engine_factory() -> Engine:
    return create_engine(POSTGRES_URL)


def sqlite_engine_factory() -> Engine:
    return create_engine(SQLITE_URL)
