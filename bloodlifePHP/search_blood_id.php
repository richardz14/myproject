<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$id = $_REQUEST["id"];
$sql_query = mysql_query($id);

       while($row = mysql_fetch_array($sql_query)){
           //echo ''.$row["id"]."";
		   //echo ''.$row["id"]. "\xA";
		   printf(''.$row["id"]."\xA");
       }
        
?>