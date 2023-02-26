<?php

$host = 'mysql';
$user = 'root';
$password = 'test-password';
$conn = mysqli_connect($host, $user, $password);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

echo "Connection with MySQL server done. \n";

$queryCreateUser = "CREATE USER 'admin'@'%' IDENTIFIED BY 'test-password'";
$queryGrantRights = "GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%'";

$result = mysqli_query($conn, $queryCreateUser);

if($result) {
    echo "User created successfully! \n";

    $result = mysqli_query($conn, $queryGrantRights);

    if($result) {
        echo "User rights successfully added.! \n";
    } else {
        echo "Failed to add rights for user! \n";
        echo ("ERROR: " . mysqli_error($conn));
    }

} else {
    echo "Failed to create user! \n";
    echo ("ERROR: " . mysqli_error($conn));
}

mysqli_close($conn);

?>
