<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$password = $_REQUEST["pass"];
$sql_query = mysql_query($password);
    
	//while($row = mysql_fetch_array($sql_query)){
          //echo ''.$row["full_name"]."<br>";
	//	   printf(''.$row["password"]."\xA");
      // }
    $row = mysql_fetch_array($sql_query);
    echo ''.$row[0];
	
?>