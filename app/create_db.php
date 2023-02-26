<?php

// Connect to the MySQL database
$host = 'mysql';
$user = 'root';
$password = 'test-password';
$conn = mysqli_connect($host, $user, $password);

// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

echo "Connection with MySQL server done. \n";

$dbname = "bookShelf";
$queryCreateDatabase = "CREATE DATABASE $dbname";

$result = mysqli_query($conn, $queryCreateDatabase);

if($result) {
    echo "Database created successfully! \n";
} else {
    echo "Failed to create database! \n";
    echo ("ERROR: " . mysqli_error($conn));
}

mysqli_close($conn);

?>
