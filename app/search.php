<?php
$host = 'mysql';
$user = 'root';
$password = 'test-password';
$dbname = 'mydb';
$conn = mysqli_connect($host, $user, $password, $dbname);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

error_reporting(E_ERROR | E_PARSE);

$searchTerm = $_GET["search"];
?>

<form method="get" action="search.php">
  <label for="searchTerm">Search:</label>
  <input type="text" id="searchTerm" name="search">
  <input type="submit" value="Search">
</form>

<?php
if($searchTerm) {
    $sql = "SELECT * FROM book WHERE lower(title) LIKE lower('%$searchTerm%')";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0) {
        while ($row = mysqli_fetch_assoc($result)) {
            echo "ID: " . $row["id"] . "<br>";
            echo "Title: " . $row["title"] . "<br>";
            echo "<hr>";
        }
    } else {
        echo "No results found.<br>";
    }
}

echo "<a href='./index.php'>GO BACK</a>";

mysqli_close($conn);
?>
