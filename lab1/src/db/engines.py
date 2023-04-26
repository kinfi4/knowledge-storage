import sqlite3
from abc import ABC, abstractmethod
from typing import Any
from urllib.parse import urlparse

import psycopg2
from pymysql import connect

from src.config import POSTGRES_URL, MYSQL_URL, SQLITE_URL

DatabaseResult = tuple[tuple[Any, ...], ...]


class AbstractEngine(ABC):
    @abstractmethod
    def query(self, query_sql: str) -> DatabaseResult:
        pass


class PostgresEngine(AbstractEngine):
    def __init__(self):
        self._connection = psycopg2.connect(POSTGRES_URL)

    def query(self, query_sql: str) -> DatabaseResult | None:
        if "UNSIGNED" in query_sql:
            query_sql = query_sql.replace("UNSIGNED", "")

        self._connection = psycopg2.connect(POSTGRES_URL)

        with self._connection.cursor() as cursor:
            try:
                cursor.execute(query_sql)
                self._connection.commit()

                return cursor.fetchall()
            except Exception:
                return
            finally:
                self._connection.close()


class MysqlEngine(AbstractEngine):
    def __init__(self):
        params = urlparse(MYSQL_URL)
        db_name = MYSQL_URL.split("/")[-1]

        self._connection = connect(
            user=params.username,
            password=params.password,
            host=params.hostname,
            port=params.port,
            db=db_name,
        )

    def query(self, query_sql: str) -> DatabaseResult | None:
        params = urlparse(MYSQL_URL)
        db_name = MYSQL_URL.split("/")[-1]
        self._connection = connect(
            user=params.username,
            password=params.password,
            host=params.hostname,
            port=params.port,
            db=db_name,
        )

        with self._connection.cursor() as cursor:
            try:
                cursor.execute(query_sql)
                self._connection.commit()

                return cursor.fetchall()
            except Exception:
                return
            finally:
                self._connection.close()


class SqliteEngine(AbstractEngine):
    def __init__(self):
        self._connection = sqlite3.connect(SQLITE_URL)

    def query(self, query_sql: str) -> DatabaseResult | None:
        self._connection = sqlite3.connect(SQLITE_URL)

        try:
            res = self._connection.execute(query_sql)
            self._connection.commit()

            return res
        finally:
            self._connection.close()
