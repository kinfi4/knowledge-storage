from src.db.repositories.abstract import IRepository
from src.db.repositories import ProductRepository, CustomerRepository, OrderRepository
from src.db.engines import AbstractEngine, SqliteEngine, PostgresEngine, MysqlEngine


_PROJECT_REPOSITORIES = [ProductRepository, CustomerRepository, OrderRepository]


def export_data(from_engine: AbstractEngine, to_engine: AbstractEngine) -> None:
    print("EXPORTING DATA TO THE " + to_engine.__class__.__name__.replace("Engine", ""))

    OrderRepository(to_engine).drop_table()
    ProductRepository(to_engine).drop_table()
    CustomerRepository(to_engine).drop_table()

    for repository in _PROJECT_REPOSITORIES:
        _export_repository(repository(from_engine), repository(to_engine))


def _export_repository(from_repo: IRepository, to_repo: IRepository) -> None:
    to_repo.create_table()

    records = from_repo.get_all()

    print("EXPORTED RECORDS FOR TABLE: " + to_repo.__class__.__name__.replace("Repository", ""))

    for record in records:
        print(record)
        to_repo.add(record)

    print("===" * 20)
