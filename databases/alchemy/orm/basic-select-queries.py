from datetime import datetime, timedelta

from sqlalchemy import select, func, and_, alias, desc, column
from sqlalchemy.orm import Session

from tables import (
    User, UserLogin, Product, Order, OrderItem, Category, Review
)
from session_manager import SessionManager


def get_unique_usernames() -> None:
    with manager.session() as session:
        session: Session
        values = session.execute(select(User.username)).scalars().unique().all()

        for user in values:
            print(user)  # strings


def get_users_within_time_range__and__username_startswith() -> None:
    yesterday = datetime.now() - timedelta(days=1)
    today = datetime.now()

    with manager.session() as session:
        session: Session
        users = session.execute(
            select(User)
            .where(
                and_(
                    User.created_at.between(yesterday, today),
                    User.username.startswith("j")
                )
            )
        ).scalars().all()

        for user in users:
            print(user.username, user.created_at, user.email)


def get_user_and_total_bought_items() -> None:
    """
        JOIN three tables, group by and use aggregate function
        then order by
    :return:
    """
    with manager.session() as session:
        session: Session
        values = session.execute(
            select(User.username, func.sum(OrderItem.quantity).label("total sum"))
            .join(Order, User.id == Order.user_id)
            .join(OrderItem, OrderItem.order_id == Order.id)
            .group_by(User.username)
            .order_by(desc("total sum"))
        ).all()

        print(values)
        print(values[0])  # tuple of username and total sum


if __name__ == "__main__":
    manager = SessionManager("postgresql+psycopg2://postgres:postgres@localhost/alchemy_train")

    # get_unique_usernames()
    # get_users_within_time_range__and__username_startswith()
    # get_user_and_total_bought_items()
