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

$sql = $_REQUEST["pass"];
$sql_query = mysqli_query($con,$sql);

$row=mysqli_fetch_row ($sql_query);
  //printf(''.$row["password"]); //
printf(''.$row[0]);
  //$row = mysql_fetch_array($sql_query);
  //echo ''.$row[0];
mysqli_close($con);

        
?>