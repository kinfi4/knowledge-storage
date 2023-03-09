<html>
<head>
    <title>Books LAB</title>

    <style>
        body {
            display: flex;
            flex-direction: column;
        }

        #books {
            font-family: Arial, Helvetica, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }

        #books td, #books th {
            border: 1px solid #ddd;
            padding: 8px;
        }

        #books tr:hover {background-color: #ddd;}

        #books th {
            padding-top: 12px;
            padding-bottom: 12px;
            text-align: left;
            color: white;
        }

        a {
            color: #65b277;
            text-decoration: none;
        }

        .addButton {
            padding: 10px 20px;
            color: white;
            background-color: #7a7a7a;
            cursor: pointer;
            border-radius: 5px;
            display: block;
            width: 120px;
            margin-bottom: 10px;
        }

        #books tr:first-child{background-color: #a26464!important;}

        #books tr:nth-child(even){background-color: #6a6868;}
        #books tr:nth-child(odd){background-color: #383838;}
    </style>
</head>
<body>
<?php
    error_reporting(E_ERROR | E_PARSE);

    $orderBy = $_GET["orderBy"];

    $host = 'mysql';
    $user = 'root';
    $password = 'test-password';
    $dbname = 'books';
    $conn = mysqli_connect($host, $user, $password, $dbname);

    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    if(isset($_POST['delete'])) {
        $id = $_POST['id'];
        $deleteBookQuery = "DELETE FROM book WHERE id = $id";
        mysqli_query($conn, $deleteBookQuery);
    }

    if(isset($_POST['deleteAuthor'])) {
        $authorId = $_POST['authorId'];
        $deleteAuthorQuery = "DELETE FROM author WHERE id = $authorId;";
        mysqli_query($conn, $deleteAuthorQuery);
    }

    if ($orderBy) {
        $getAllBooksQuery = "SELECT book.id, title, publishYear, price, name, authorId FROM book JOIN author ON book.authorId = author.id ORDER BY $orderBy";
    } else {
        $getAllBooksQuery = "SELECT book.id, title, publishYear, price, name, authorId FROM book JOIN author ON book.authorId = author.id";
    }

    $response = mysqli_query($conn, $getAllBooksQuery);

    echo "<a class='addButton' href='addBookForm.php'>Add book</a>";

    echo "<table id=\"books\">";
        echo "<tr>";
            echo "<th><a href='./index.php?orderBy=book.id'>ID</a></th>";
            echo "<th><a href='./index.php?orderBy=title'>Title</a></th>";
            echo "<th><a href='./index.php?orderBy=publishYear'>Publish Year</a></th>";
            echo "<th><a href='./index.php?orderBy=price'>Price</a></th>";
            echo "<th><a href='./index.php?orderBy=name'>Author</a></th>";
            echo "<th></th>";
            echo "<th></th>";
        echo "</tr>";

        while ($book = mysqli_fetch_assoc($response)) {
            echo "<tr>";
                echo "<th>";
                    echo $book["id"];
                echo "</th>";
                echo "<th>";
                    echo $book["title"];
                echo "</th>";
                echo "<th>";
                    echo $book["publishYear"];
                echo "</th>";
                echo "<th>";
                    echo $book["price"];
                echo "</th>";
                echo "<th>";
                    echo "<a href=\"./author.php?author=" . $book["authorId"] . "\">" . $book["name"] . "</a>";
                echo "</th>";
                echo "<th>";
                    echo "<a href=\"./updateBookForm.php?bookAuthorId=" . $book["authorId"] . "&bookTitle=" . $book["title"] . "&bookPrice=" . $book["price"] . "&bookPublishYear=" . $book["publishYear"] . "&bookId=" . $book["id"] . "\">" . EDIT . "</a>";
                echo "</th>";
                echo "<th>";
                    echo "<form method='post'><input type='hidden' name='id' value='" . $book['id'] . "'><button type='submit' name='delete'>Delete</button></form>";
                echo "</th>";
            echo "</tr>";
        }
    echo "</table>";

    mysqli_close($conn);
?>

</body>
</html>
