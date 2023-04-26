import sqlalchemy as sq
# popular Python library for working with SQL databases

from sqlalchemy.orm import declarative_base
# base class that provides some of the boilerplate code for defining SQLAlchemy models

Base = declarative_base()


class Customer(Base):                   # SQLAlchemy database model representing a Customer table.
    __tablename__ = "customer"

    id = sq.Column(sq.Integer, primary_key=True)
    full_name = sq.Column(sq.String(255), nullable=False)
    email = sq.Column(sq.String(255), nullable=False)


class Product(Base):                   # SQLAlchemy database model representing a Product table.
    __tablename__ = "product"
    __table_args__ = (sq.CheckConstraint("price > 0"),)

    id = sq.Column(sq.Integer, primary_key=True)
    name = sq.Column(sq.String(255), nullable=False)
    price = sq.Column(sq.Float, nullable=False)
    description = sq.Column(sq.Text, nullable=True)


class Order(Base):                   # SQLAlchemy database model representing a Order table.
    __tablename__ = "order"

    id = sq.Column(sq.Integer, primary_key=True)
    qty = sq.Column(sq.Integer, nullable=False)
    customer_id = sq.Column(sq.Integer, sq.ForeignKey("customer.id", ondelete="CASCADE"))
    product_id = sq.Column(sq.Integer, sq.ForeignKey("product.id", ondelete="CASCADE"))
