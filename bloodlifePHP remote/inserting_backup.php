<?php

//$server = "localhost";
//$user = "root";
//$pass = "";
//$database = "bloodlife_db";

//include "database.php");
$server = "https://databases.000webhost.com";
$user = "id1598109_root";
$pass = "goroy2017";
$database = "id1598109_bloodlife_db";  

 $con = mysqli_connect("localhost",$user,$pass,$database) or die("error connecting");
if (mysqli_connect_errno()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
}

//$sql = $_REQUEST["insert"];
$sql = $_POST["insert"];
mysqli_query($con,$sql);

//$insert = $_REQUEST["insert"];
//mysql_query($insert);

mysqli_close($con);

        
?>