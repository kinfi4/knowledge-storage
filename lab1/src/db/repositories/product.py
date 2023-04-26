from src.db.repositories.abstract import IRepository


class ProductRepository(IRepository):
    _table_name = "product"
    _columns = ["id", "name", "price", "description"]

    @property
    def _create_table_query(self) -> str:
        return f"""
            CREATE TABLE {self._table_name} (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255),
                price DECIMAL(6, 2),
                description TEXT
            );
        """
