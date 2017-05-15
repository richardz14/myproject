<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$image = $_REQUEST["image"];
$sql_query = mysql_query($image);

       while($row = mysql_fetch_array($sql_query)){
           //echo ''.$row["phone2"];
		   printf(''.$row["image"]."\xA");
       }
        
?>