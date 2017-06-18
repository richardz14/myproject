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

//$sql = $_REQUEST["update"];
$imgS = urlencode($_POST["update_img"]);
$sql = "UPDATE `person_info` SET `image`='".trim($imgS)."';";
mysqli_query($con,$sql);

//$update = $_REQUEST["update"];
//mysql_query($update);

mysqli_close($con);

        
?>