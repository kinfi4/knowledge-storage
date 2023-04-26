from src.ui import Window  # Import the Window class that defines a GUI window using the tkinter library
# Import repositories - classes responsible for interaction with mongoDB
from src.repositories import CustomerRepository, ProductRepository, OrderRepository


def _initialize_database() -> None:
    """
        Script for initializing database with test data.
    """

    # Creating repositories
    customer_repository = CustomerRepository()
    product_repository = ProductRepository()
    order_repository = OrderRepository()

    # Truncating our database from garbage
    customer_repository.delete_all()
    product_repository.delete_all()
    order_repository.delete_all()

    # adding some records to database
    customer_repository.add({"full_name": "Nick", "email": "espozito@dog.com"})
    customer_repository.add({"full_name": "John", "email": "foaspi@dog.com"})

    product_repository.add({"name": "Ball", "price": 9.5, "description": "Ball for football"})
    product_repository.add({"name": "Hog", "price": 15, "description": "Hog for farming"})

    order_repository.add({"customer_id": 1, "product_id": 1, "qty": 1})


if __name__ == "__main__":
    _initialize_database()

    window = Window()
    window.run()
