from abc import ABC, abstractmethod
from typing import Any, Union

from src.db.engines import AbstractEngine, MysqlEngine, PostgresEngine, SqliteEngine
from src.exceptions import InfrastructureException


class IRepository(ABC):
    _table_name: str
    _columns: list[str]

    def __init__(self, engine: AbstractEngine):
        self._engine = engine

        if isinstance(engine, (MysqlEngine, SqliteEngine)):
            self._table_name = f"`{self._table_name}`"
        if isinstance(engine, (PostgresEngine,)):
            self._table_name = f"\"{self._table_name}\""

    @property
    @abstractmethod
    def _create_table_query(self) -> str:
        pass

    def add(self, data: dict[str, Any]) -> None:
        values_to_insert = self._map_python_types_to_sql(list(data.values()))
        add_query = f"""
            INSERT INTO {self._table_name} ({", ".join(list(data.keys()))}) VALUES 
            ({", ".join(values_to_insert)});
        """

        # print(add_query)

        try:
            self._engine.query(add_query)
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def delete(self, _id: int) -> None:
        delete_query = f"DELETE FROM {self._table_name} WHERE id = {_id};"

        # print(delete_query)

        try:
            self._engine.query(delete_query)
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def update(self, _id: int, data: dict[str, Any]):
        values_to_insert = self._map_python_types_to_sql(list(data.values()))
        zipped_values = zip(data.keys(), values_to_insert)

        # print(data)

        update_query = f"""
            UPDATE {self._table_name} SET {", ".join(f"{name} = {value}" for name, value in zipped_values)} WHERE id = {_id};
        """

        # print(update_query)

        try:
            self._engine.query(update_query)
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def get_all(self) -> list[dict[str, Any]]:
        select_query = f"SELECT * FROM {self._table_name};"

        # print(select_query)

        try:
            result = self._engine.query(select_query)

            return [self._map_row_to_dict(row) for row in result]
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def get_one(self, _id: int) -> dict[str, Any]:
        select_query = f"SELECT * FROM {self._table_name} WHERE id = {_id};"

        # print(select_query)

        try:
            result = self._engine.query(select_query)[0]

            return self._map_row_to_dict(result)
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def drop_table(self) -> None:
        drop_table_query = f"DROP TABLE {self._table_name};"

        # print(drop_table_query)

        try:
            self._engine.query(drop_table_query)
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def create_table(self) -> None:

        # print(self._create_table_query)

        try:
            self._engine.query(self._create_table_query)
        except Exception as err:
            raise InfrastructureException(f"Something went wrong with {err.__class__.__name__}: {str(err)}")

    def _map_row_to_dict(self, row: tuple[Any, ...]) -> dict[str, Any]:
        return {
            column_name: value for column_name, value in zip(self._columns, row)
        }

    def _map_python_types_to_sql(self, data: list[Union[str, int]]) -> list[Union[str, int]]:
        result = []

        for value in data:
            if isinstance(value, str):
                result.append(f"'{value}'")
            else:
                result.append(f"{value}")

        return result
