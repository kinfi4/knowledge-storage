<html>
<head>
    <title>Author form LAB</title>

    <style>
        .my-form {
            margin-top: 50px;
            width: 100%;
            display: flex;
            justify-content: center;
        }
        .my-form form {
            padding: 25px;
            box-shadow: 0 0 10px purple;
        }
    </style>
</head>
<body>
<?php

error_reporting(E_ERROR | E_PARSE);


$authorId = $_GET["id"];
$authorName = $_GET["name"];
$authorAddress = $_GET["address"];


echo "
<div class='my-form'>
    <form action='./updateAuthor.php' method='POST'>
        Author Id: <br>
        <input type='number' id='authorId' placeholder='Id' name='authorId' required value='$authorId'> <br><br>
        
        Name: <br>
        <input type='text' id='authorName' placeholder='Name' name='authorName' required value='$authorName'> <br><br>
        
        Address: <br>
        <input type='text' id='authorAddress' placeholder='Address' name='authorAddress' required value='$authorAddress'><br><br>
        
        <input type='submit'>
        
        <br><br>
        <a href='./index.php'>GO BACK</a>
    </form>
</div>
";


?>

</body>
</html>
