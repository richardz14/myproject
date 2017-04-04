<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$location = $_REQUEST["location"];
$sql_query = mysql_query($location);

       while($row = mysql_fetch_array($sql_query)){
          // echo ''.$row["location_brgy"].'. '.$row["location_street"].', '.$row["location_purok"]."<br>";
		    printf(''.$row["location_brgy"].'. '.$row["location_street"].', '.$row["location_purok"]."\xA");
	  }
	  
        
?>