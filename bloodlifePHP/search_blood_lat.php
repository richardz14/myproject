<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$lat = $_REQUEST["lat"];
$sql_query = mysql_query($lat);

       while($row = mysql_fetch_array($sql_query)){
		    printf(''.$row["lat"]."\xA");
       }
        
?>