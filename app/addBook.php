<?php
$host = 'mysql';
$user = 'root';
$password = 'test-password';
$dbname = 'mydb';
$conn = mysqli_connect($host, $user, $password, $dbname);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$bookId = $_POST["bookId"];
$title = $_POST["bookTitle"];
$price = $_POST["bookPrice"];
$publishYear = $_POST["bookPublishYear"];
$authorId = $_POST["bookAuthorId"];

if($bookId) {
    $updateBookQuery = "
        UPDATE book SET
            title = '$title',
            publishYear = $publishYear,
            price = $price,
            authorId = $authorId
        WHERE id = $bookId
    ";
    $response = mysqli_query($conn, $updateBookQuery);

    if($response) {
        echo "BOOK WAS UPDATED SUCCESSFULLY  <br>";
    } else {
        $error = mysqli_error($conn);
        echo "ERROR OCCURRED DURING UPDATING BOOK: $error <br>";
    }
} else {
    $insertNewBook = "INSERT INTO book (title, publishYear, price, authorId) VALUES ('$title', $publishYear, $price, $authorId);";
    $response = mysqli_query($conn, $insertNewBook);

    if($response) {
        echo "BOOK WAS ADDED SUCCESSFULLY  <br>";
    } else {
        $error = mysqli_error($conn);
        echo "ERROR OCCURRED DURING ADDING BOOK: $error <br>";
    }
}

echo "<br>";
echo "<a href='./index.php'>GO BACK</a>";
mysqli_close($conn);
