from sqlalchemy import Engine, Table

from src.repositories.abstract import IRepository

from src.tables import Base
# base class for all database models

from src.repositories import ProductRepository, CustomerRepository, OrderRepository
# IRepositories previously made


_PROJECT_REPOSITORIES = [ProductRepository, CustomerRepository, OrderRepository]


def recreate_tables(engine: Engine) -> None:

    """
	The purpose of this function is to delete all the tables in the connected database 
	and recreate them using the table definitions defined in the Base declarative base class.
    """
    with engine.connect() as conn:
        tbl: Table
        for tbl in Base.metadata.sorted_tables:
            try:
                conn.execute(tbl.delete())
                conn.commit()
            except Exception:  # we use Exception class here, because error may differ from one driver to another
                pass

    Base.metadata.create_all(engine)



def export_data(from_engine: Engine, to_engine: Engine, to_db_name: str = "Postgres") -> None:
    """
	The purpose of this function is to export data from one database to another using the repository pattern. 
    	The from_engine argument represents the source database, while the to_engine argument represents the destination database.
    """
    
    print("EXPORTING DATA TO THE " + to_db_name)

    recreate_tables(to_engine)

    for repository in _PROJECT_REPOSITORIES:
        _export_repository(repository(from_engine), repository(to_engine))


def _export_repository(from_repo: IRepository, to_repo: IRepository) -> None:
    """
	The purpose of this function is to export data from one repository to another. 
	The from_repo argument represents the source repository, while the to_repo argument represents the destination repository.
    """
    records = from_repo.get_all()

    print("EXPORTED RECORDS FOR TABLE: " + to_repo.__class__.__name__.replace("Repository", ""))

    for record in records:
        record.pop("_sa_instance_state")

        print(record)
        to_repo.add(record)

    print("===" * 20)

