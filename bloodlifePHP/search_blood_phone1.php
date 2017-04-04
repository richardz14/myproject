<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$phone1 = $_REQUEST["phone1"];
$sql_query = mysql_query($phone1);

       while($row = mysql_fetch_array($sql_query)){
           echo ''.$row["phone1"];
       }
        
?>