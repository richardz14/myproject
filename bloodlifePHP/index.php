<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$email = $_REQUEST["email"];
$sql_query = mysql_query($email);

       while($row = mysql_fetch_array($sql_query)){
           echo ''.$row["email"];
       }
        
?>