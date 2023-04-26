from src.db.repositories.abstract import IRepository


class CustomerRepository(IRepository):
    _table_name = "customer"
    _columns = ["id", "full_name", "email"]

    @property
    def _create_table_query(self) -> str:
        return f"""
            CREATE TABLE {self._table_name} (
                id SERIAL PRIMARY KEY,
                full_name VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL
            );
        """
