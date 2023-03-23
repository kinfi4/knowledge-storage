<?php

$host = 'mysql';
$user = 'root';
$password = 'test-password';
$dbname = 'mydb';
$conn = mysqli_connect($host, $user, $password, $dbname);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$totalBookQuery = 'SELECT count(*) AS totalBooks FROM book';
$totalAuthorsQuery = 'SELECT count(*) AS totalAuthor FROM author';
$totalBooksAddedLastMonthQuery = '
    SELECT 
        count(*) AS totalBooksLastMonth 
    FROM book
    WHERE dateAdded >= DATE_SUB(NOW(), INTERVAL 1 MONTH);
';
$totalAuthorsAddedLastMonthQuery = '
    SELECT 
        count(*) AS totalAuthorsAdded 
    FROM author
    WHERE authorDateAdd >= DATE_SUB(NOW(), INTERVAL 1 MONTH);
';
$theLastBookAddedQuery = 'SELECT * FROM book ORDER BY dateAdded DESC LIMIT 1';
$authorWithMostBooksQuery = '
    SELECT
        count(*) AS booksCount,
        author.id AS id,
        author.name AS name
    FROM author
         JOIN book ON author.id = authorId
    GROUP BY author.id
    ORDER BY booksCount DESC 
    LIMIT 1;
';

###############################################################
$response = mysqli_query($conn, $totalBookQuery);
$totalBooks = mysqli_fetch_array($response);

echo "<h4>Total books in database: ".$totalBooks["totalBooks"]."</h4>";

###############################################################
$response = mysqli_query($conn, $totalAuthorsQuery);
$totalAuthors = mysqli_fetch_array($response);

echo "<h4>Total authors in database: ".$totalAuthors["totalAuthor"]."</h4>";

###############################################################
$response = mysqli_query($conn, $totalBooksAddedLastMonthQuery);
$totalBooksLastMonth = mysqli_fetch_array($response);

echo "<h4>Total books added to database in the last month: ".$totalBooksLastMonth["totalBooksLastMonth"]."</h4>";

###############################################################
$response = mysqli_query($conn, $totalAuthorsAddedLastMonthQuery);
$totalAuthorsLastMonth = mysqli_fetch_array($response);

echo "<h4>Total authors added to database in the last month: ".$totalAuthorsLastMonth["totalAuthorsAdded"]."</h4>";

###############################################################
$response = mysqli_query($conn, $theLastBookAddedQuery);
$lastBookAdded = mysqli_fetch_array($response);

echo "<h4>The book last added to database: ".$lastBookAdded["title"]." was added on: ".$lastBookAdded["dateAdded"]."</h4>";

###############################################################
$response = mysqli_query($conn, $authorWithMostBooksQuery);
$author = mysqli_fetch_array($response);

echo "<h4>Author with the most books: <a href=\"./author.php?author=" . $author["id"] . "\">" . $author["name"] . "</a>"." books count: ".$author["booksCount"]."</h4>";

echo "<a href='./index.php'>GO BACK</a>";
