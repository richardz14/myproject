<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$Btype = $_REQUEST["Btype"];
$sql_query = mysql_query($Btype);

       while($row = mysql_fetch_array($sql_query)){
           echo ''.$row["blood_type"];
       }
        
?>