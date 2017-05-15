<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$age = $_REQUEST["age"];
$sql_query = mysql_query($age);

       while($row = mysql_fetch_array($sql_query)){
		   printf(''.$row["age"]."\xA");
       }
        
?>