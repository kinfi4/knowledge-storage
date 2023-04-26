from src.db.repositories.abstract import IRepository


class OrderRepository(IRepository):
    _table_name = "order"
    _columns = ["id", "qty", "customer_id", "product_id"]

    @property
    def _create_table_query(self) -> str:
        return f"""
            CREATE TABLE {self._table_name} (
                id SERIAL PRIMARY KEY,
                qty INT NOT NULL,
                customer_id BIGINT UNSIGNED,
                product_id BIGINT UNSIGNED,
        
                FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
                FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
            );
        """
