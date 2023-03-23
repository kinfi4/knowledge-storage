<html>
<head>
    <title>Book form LAB</title>

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


$bookId = $_GET["bookId"];
$bookTitle = $_GET["bookTitle"];
$bookPrice = $_GET["bookPrice"];
$bookAuthorId = $_GET["bookAuthorId"];
$bookPublishYear = $_GET["bookPublishYear"];

echo "
<div class='my-form'>
    <form action='./addBook.php' method='POST'>
        Book Id: <br>
        <input type='number' id='bookId' placeholder='Id' name='bookId' required value='$bookId'> <br><br>
        
        Title: <br>
        <input type='text' id='bookTitle' placeholder='Title' name='bookTitle' required value='$bookTitle'> <br><br>
        
        Price: <br>
        <input type='number' id='bookPrice' placeholder='Price' name='bookPrice' required value='$bookPrice'><br><br>
        
        Publishing Year: <br>
        <input type='number' id='bookPublishYear' placeholder='Publishing Year' name='bookPublishYear' required value='$bookPublishYear'> <br><br>
        
        Author ID <br>
        <input type='number' id='bookAuthorId' placeholder='Author Id' name='bookAuthorId' required value='$bookAuthorId'> <br><br>
        
        <input type='submit'>
        
        <br> <br>
        <a href='./index.php'>GO BACK</a>
    </form>
</div>
";


?>

</body>
</html>
