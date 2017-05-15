<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$gender = $_REQUEST["gender"];
$sql_query = mysql_query($gender);

       while($row = mysql_fetch_array($sql_query)){
		   printf(''.$row["gender"]."\xA");
       }
        
?>