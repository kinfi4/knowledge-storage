<?php

// Connect to the MySQL database
$host = 'mysql';
$user = 'root';
$password = 'test-password';
$dbname = 'mydb';
$conn = mysqli_connect($host, $user, $password, $dbname);

// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// Print "Hello, World!" message
echo "Hello, World!";

// Close connection
mysqli_close($conn);

?>

// mysql --host=localhost --user=root -p mydb
// password: test-password