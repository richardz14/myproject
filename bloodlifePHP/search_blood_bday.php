<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$bday = $_REQUEST["bday"];
$sql_query = mysql_query($bday);

       while($row = mysql_fetch_array($sql_query)){
           echo ''.$row["bday_month"].'-'.$row["bday_day"].'-'.$row["bday_year"];
       }
        
?>