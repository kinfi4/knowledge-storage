from sqlalchemy import select, func
from sqlalchemy.orm import Session

from tables import (
    User, UserLogin, Product, Order, OrderItem, Category, Review
)
from session_manager import SessionManager


def get_unique_usernames(session_manager: SessionManager) -> None:
    with session_manager.session() as session:
        session: Session
        values = session.execute(select(User.username)).scalars().unique().all()

        for user in values:
            print(user)  # strings


def get_user_and_total_bought_items(session_manager: SessionManager) -> None:
    with manager.session() as session:
        session: Session
        values = session.execute(
            select(User.username, func.sum(OrderItem.quantity))
            .join(Order)
            .join(OrderItem)
            .group_by(User.username)
        ).scalars().all()

        print(values)


if __name__ == "__main__":
    manager = SessionManager("postgresql+psycopg2://postgres:postgres@localhost/alchemy_train")

    # get_unique_usernames(manager)
    get_user_and_total_bought_items(manager)
