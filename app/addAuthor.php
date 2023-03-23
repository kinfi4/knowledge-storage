<?php
$host = 'mysql';
$user = 'root';
$password = 'test-password';
$dbname = 'mydb';
$conn = mysqli_connect($host, $user, $password, $dbname);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$authorId = $_POST["authorId"];
$authorName = $_POST["authorName"];
$authorAddress = $_POST["authorAddress"];


if($authorId) {
    $updateBookQuery = "
        UPDATE author SET
            name = '$authorName',
            address = '$authorAddress'
        WHERE id = $authorId;
    ";
    $response = mysqli_query($conn, $updateBookQuery);

    if($response) {
        echo "AUTHOR WAS UPDATED SUCCESSFULLY  <br>";
    } else {
        $error = mysqli_error($conn);
        echo "ERROR OCCURRED DURING UPDATING AUTHOR: $error <br>";
    }
} else {
    $insertNewBook = "INSERT INTO author (name, address) VALUES ('$authorName', '$authorAddress');";
    $response = mysqli_query($conn, $insertNewBook);

    if($response) {
        $createdAuthorInfo = mysqli_query($conn, "SELECT * FROM author WHERE id = (SELECT max(id) FROM author);");
        $r = mysqli_fetch_array($createdAuthorInfo);
        $createdAuthorId = $r["id"];
        echo "AUTHOR WAS ADDED SUCCESSFULLY WITH ID: $createdAuthorId <br>";
    } else {
        $error = mysqli_error($conn);
        echo "ERROR OCCURRED DURING ADDING AUTHOR: $error <br>";
    }
}

echo "<br>";
echo "<a href='./index.php'>GO BACK</a>";
mysqli_close($conn);