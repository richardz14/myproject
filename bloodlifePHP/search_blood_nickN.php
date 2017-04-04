<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$nick = $_REQUEST["nick"];
$sql_query = mysql_query($nick);

       while($row = mysql_fetch_array($sql_query)){
           //echo ''.$row["nick_name"];
		   printf(''.$row["nick_name"]."\xA");
       }
        
?>