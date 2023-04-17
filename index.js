const express = require("express");
const sqlite3 = require("sqlite3").verbose();
const bodyParser = require("body-parser");
const initDb = require("./init-db");

const app = express();
const port = 3000;

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

const db = new sqlite3.Database(":memory:");

app.get("/authors", (req, response) => {
    db.all("SELECT * FROM author;", (error, data) => {
        if (error) {
            response.status(500).json({ error: error.message });
            return;
        }

        response.json(data);
    });
});

app.get("/authors/:id", (req, response) => {
    const { id } = req.params;

    db.get("SELECT * FROM author WHERE id = ?;", id, (error, row) => {
        if (error) {
            response.status(500).json({ error: error.message });
            return;
        }

        if (!row) {
            response.status(404).json({ error: "Author not found" });
            return;
        }

        response.json(row);
    });
});

app.post("/authors", (req, response) => {
    const { name, address } = req.body;

    db.run(
        "INSERT INTO author (name, address) VALUES (?, ?)",
        name,
        address,
        function (error) {
            if (error) {
                response.status(500).json({ error: error.message });
                return;
            }

            response.json({ message: "Author was added." });
        }
    );
});

app.put("/authors/:id", (req, response) => {
    const { id } = req.params;
    const { name, address } = req.body;

    db.run(
        "UPDATE author SET name = ?, address = ? WHERE id = ?;",
        name,
        address,
        id,
        function (error) {
            if (error) {
                response.status(500).json({ error: error.message });
                return;
            }

            if (this.changes === 0) {
                response.status(404).json({ error: "Author not found" });
                return;
            }

            response.json({ message: "Author updated successfully" });
        }
    );
});

app.delete("/authors/:id", (req, response) => {
    const { id } = req.params;

    db.run("DELETE FROM author WHERE id = ?;", id, function (error) {
        if (error) {
            response.status(500).json({ error: error.message });
            return;
        }

        if (this.changes === 0) {
            response.status(404).json({ error: "Author not found" });
            return;
        }

        response.json({ message: "Author deleted successfully" });
    });
});


app.get("/books", (req, response) => {
    db.all("SELECT * FROM book;", (error, data) => {
        if (error) {
            response.status(500).json({ error: error.message });
            return;
        }

        response.json(data);
    });
});

app.get("/books/:id", (req, response) => {
    const { id } = req.params;

    db.get("SELECT * FROM book WHERE id = ?;", id, (error, row) => {
        if (error) {
            response.status(500).json({ error: error.message });
            return;
        }

        if (!row) {
            response.status(404).json({ error: "Book not found" });
            return;
        }

        response.json(row);
    });
});

app.post("/books", (req, response) => {
    const { title, publish_year, price, author_id } = req.body;

    db.run(
        "INSERT INTO book (title, publish_year, price, author_id) VALUES (?, ?, ?, ?)",
        title,
        publish_year,
        price,
        author_id,
        function (error) {
            if (error) {
                response.status(500).json({ error: error.message });
                return;
            }

            response.json({ message: "Book was added." });
        }
    );
});

app.delete("/books/:id", (req, response) => {
    const { id } = req.params;

    db.run("DELETE FROM book WHERE id = ?;", id, function (error) {
        if (error) {
            response.status(500).json({ error: error.message });
            return;
        }

        if (this.changes === 0) {
            response.status(404).json({ error: "Book not found" });
            return;
        }

        response.json({ message: "Book deleted successfully" });
    });
});

app.put("/books/:id", (req, response) => {
    const { id } = req.params;
    const { title, publish_year, price, author_id } = req.body;

    db.run(
        "UPDATE book SET title = ?, publish_year = ?, price = ?, author_id = ? WHERE id = ?;",
        title,
        publish_year,
        price,
        author_id,
        id,
        function (error) {
            if (error) {
                response.status(500).json({ error: error.message });
                return;
            }

            if (this.changes === 0) {
                response.status(404).json({ error: "Book not found" });
                return;
            }

            response.json({ message: "Book updated successfully" });
        }
    );
});

initDb.initializeDatabase(db);
app.listen(port, '0.0.0.0',() => {
    console.log(`Server running on port ${port}`);
});
