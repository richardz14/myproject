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

$sql = $_REQUEST["id"];
$sql_query = mysqli_query($con,$sql);

$row=mysqli_fetch_array($sql_query ,MYSQLI_ASSOC);
  printf(''.$row["id"]."\xA"); //

mysqli_close($con);

        
?>