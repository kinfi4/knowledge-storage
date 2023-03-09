<html>
<head>
    <title>Authors LAB</title>

    <style>
        .authorInfo {
            margin-top: 50px;
            width: 100%;
            display: flex;
            justify-content: center;
        }
        .authorInfo .inner {
            font-family: Arial, Helvetica, sans-serif;
            padding: 25px;
            box-shadow: 0 0 10px purple;
        }

    </style>
</head>
<body>
<?php

error_reporting(E_ERROR | E_PARSE);


$host = 'mysql';
$user = 'root';
$password = 'test-password';
$dbname = 'books';
$conn = mysqli_connect($host, $user, $password, $dbname);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$authorId = $_GET["author"];

$getAuthorQuery = "SELECT * FROM author WHERE id = " . $authorId . ";";
$responseAuthorInfo = mysqli_fetch_assoc($conn->query($getAuthorQuery));

$authorName = $responseAuthorInfo["name"];
$authorAddress = $responseAuthorInfo["address"];

echo "<div class='authorInfo'>";
echo "<div class='inner'>";

    echo "Author Id: $authorId <br>";
    echo "Author Name: $authorName <br>";
    echo "Author Address: $authorAddress <br>";

    $getAuthorBooksQuery = "SELECT * FROM book WHERE authorId = $authorId;";

    echo "BOOKS: -- ";
    $response = mysqli_query($conn, $getAuthorBooksQuery);
    while ($book = mysqli_fetch_assoc($response)) {
        echo "<b>" . $book["title"] . "</b>, ";
    }

    echo "<br> <br>";

    echo "<a href='./updateAuthorForm.php?name=$authorName&id=$authorId&address=$authorAddress'>UPDATE</a> <br>";
    echo "<form method='post' action='index.php'><input type='hidden' name='authorId' value='$authorId'><button type='submit' name='deleteAuthor'>Delete</button></form>";
    echo "<a href='./index.php'>GO BACK</a>";

echo "</div>";
echo "</div>";

mysqli_close($conn);
?>

</body>
</html>
