from src.ui import Window
# Import the Window class that defines a GUI window using the tkinter library

from src.export import recreate_tables
# The recreate_tables function drops all tables in the database connected 
# to a given engine and recreates them based on the metadata defined in the src.tables.Base object.

from src.engines import mysql_engine_factory
# The mysql_engine_factory function is likely a factory function that creates a new Engine object for 
# connecting to a MySQL database. 

from src.repositories import CustomerRepository, ProductRepository, OrderRepository


def _initialize_database() -> None:
    # creating an engine for MYSQL
    engine = mysql_engine_factory()

    # creating tables in database
    recreate_tables(engine)

    customer_repository = CustomerRepository(engine)
    product_repository = ProductRepository(engine)
    order_repository = OrderRepository(engine)


    # adding some records to database
    customer_repository.add({"id": 1, "full_name": "Nick", "email": "espozito@dog.com"})
    customer_repository.add({"id": 2, "full_name": "John", "email": "foaspi@dog.com"})

    product_repository.add({"id": 1, "name": "Ball", "price": 9.5, "description":"Ball for football"})
    product_repository.add({"id": 2, "name": "Hog", "price": 15, "description":"Hog for farming"})

    order_repository.add({"id": 1, "customer_id": 1, "product_id": 1, "qty": 1})


if __name__ == "__main__":
    _initialize_database()

    window = Window()
    window.run()
