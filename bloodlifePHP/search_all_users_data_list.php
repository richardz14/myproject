<?php

$server = "localhost";
$user = "root";
$pass = "";
$database = "bloodlife_db";

$conection = mysql_connect($server, $user, $pass);
mysql_select_db($database,$conection);

$all_info = $_REQUEST["all_info"];
$sql_query = mysql_query($all_info);

       while($row = mysql_fetch_array($sql_query)){
           //echo ''.$row["phone2"];
		    printf(''.$row["full_name"]."\xA"); //0
		   printf(''.$row["blood_type"]."\xA"); //1
		   printf(''.$row["email"]."\xA"); //2
		   printf(''.$row["phone_number1"]."\xA"); //3
		   printf(''.$row["phone_number2"]."\xA"); //4
		   printf(''.$row["bday_month"].'-'.$row["bday_day"].'-'.$row["bday_year"]."\xA"); //5
		   printf(''.$row["location_brgy"].'. '.$row["location_street"].', '.$row["location_purok"]."\xA"); //6
		   printf(''.$row["nick_name"]."\xA"); //7
		   printf(''.$row["donate_boolean"]." (".$row["date_donated"].")"."\xA"); //8
		   printf(''.$row["gender"]."\xA"); // 9
		   printf(''.$row["id"]."\xA"); // 10
		   printf(''.$row["age"]."\xA"); // 11
		   printf(''.$row["lat"]."\xA"); // 12
		   printf(''.$row["long"]."\xA"); // 13
		   printf(''.$row["image"]."\xA"); //14
		   //echo ''.$row["bday_month"].'-'.$row["bday_day"].'-'.$row["bday_year"];
       }
        
?>