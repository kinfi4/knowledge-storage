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


echo "
<div class='my-form'>
    <form action='addAuthor.php' method='POST'>        
        Name: <br>
        <input type='text' id='authorName' placeholder='Name' name='authorName' required> <br><br>
        
        Address: <br>
        <input type='text' id='authorAddress' placeholder='Address' name='authorAddress' required><br><br>
        
        <input type='submit'>
        
        <br><br>
        <a href='./index.php'>GO BACK</a>
    </form>
</div>
";
?>

</body>
</html>
