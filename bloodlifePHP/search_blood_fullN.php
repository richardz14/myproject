<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$full_name = $_REQUEST["full_name"];
$sql_query = mysql_query($full_name);

       while($row = mysql_fetch_array($sql_query)){
          //echo ''.$row["full_name"]."<br>";
		   printf(''.$row["full_name"]."\xA");
       }
        
?>