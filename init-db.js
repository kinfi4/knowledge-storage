function initializeDatabase(db) {
    db.serialize(() => {
        db.run(`
    CREATE TABLE author (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name VARCHAR(255) NOT NULL,
        address VARCHAR(255) NOT NULL
    );
  `);

        db.run(`
    CREATE TABLE book (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        title VARCHAR(255) NOT NULL,
        publish_year INT NOT NULL,
        price INT NOT NULL,
        author_id INTEGER NOT NULL,
        FOREIGN KEY(author_id) REFERENCES author(id)
    );
  `);

        db.run(`
        INSERT INTO author (name, address) VALUES
        ('Artem', 'Kiev 120.4'),
        ('Tom', 'Chernigiv Pyhov 130');
  `);

        db.run(`
        INSERT INTO book (title, publish_year, price, author_id) VALUES
        ('Underground 3', 2012, 120, 1),
        ('Hello World', 2000, 145, 2);
  `);
    });

}

module.exports = {initializeDatabase};