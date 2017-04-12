<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$donate_b = $_REQUEST["donate_b"];
$sql_query = mysql_query($donate_b);

       while($row = mysql_fetch_array($sql_query)){
           //echo ''.$row["donate_boolean"];
		    printf(''.$row["donate_boolean"]."\xA");
       }
        
?>