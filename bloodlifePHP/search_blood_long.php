<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$long = $_REQUEST["long"];
$sql_query = mysql_query($long);

       while($row = mysql_fetch_array($sql_query)){
		printf(''.$row["long"]."\xA");
       }
        
?>