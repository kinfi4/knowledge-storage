from src.ui import Window
from src.db.engines import MysqlEngine
from src.db.repositories import CustomerRepository, ProductRepository, OrderRepository


def _initialize_database() -> None:
    engine = MysqlEngine()

    customer_repository = CustomerRepository(engine)
    product_repository = ProductRepository(engine)
    order_repository = OrderRepository(engine)

    order_repository.drop_table()
    customer_repository.drop_table()
    product_repository.drop_table()

    customer_repository.create_table()
    product_repository.create_table()
    order_repository.create_table()

    customer_repository.add({"id": 1, "full_name": "Nick", "email": "espozito@dog.com"})
    customer_repository.add({"id": 2, "full_name": "John", "email": "foaspi@dog.com"})

    product_repository.add({"id": 1, "name": "Ball", "price": 9.5, "description": "Ball for football"})
    product_repository.add({"id": 2, "name": "Hog", "price": 15, "description": "Hog for farming"})

    order_repository.add({"id": 1, "customer_id": 1, "product_id": 1, "qty": 1})


if __name__ == "__main__":
    _initialize_database()

    window = Window()
    window.run()
