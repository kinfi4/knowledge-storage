<?php

$host = 'mysql';
$user = 'admin';
$password = 'test-password';
$dbname = "bookShelf";
$conn = mysqli_connect($host, $user, $password, $dbname);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

echo "Connection with MySQL server done. \n";

$queryCreateTable = "
    CREATE TABLE author (
        id INT NOT NULL AUTO_INCREMENT,
        name VARCHAR(255),
        address VARCHAR(255),
        
        PRIMARY KEY (id)
    );
";

$result = mysqli_query($conn, $queryCreateTable);

if($result) {
    echo "Table created successfully! \n";
} else {
    echo "Failed to create table! \n";
    echo ("ERROR: " . mysqli_error($conn));
}

mysqli_close($conn);

?>
