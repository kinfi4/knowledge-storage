<?php
$host = 'mysql';
$user = 'root';
$password = 'test-password';
$dbname = 'books';
$conn = mysqli_connect($host, $user, $password, $dbname);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$authorId = $_POST["authorId"];
$authorName = $_POST["authorName"];
$authorAddress = $_POST["authorAddress"];

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

echo "<br>";
echo "<a href='./index.php'>GO BACK</a>";
mysqli_close($conn);
