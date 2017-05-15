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
		   printf(''.$row["bday_month"]."\xA"); //5
		    printf(''.$row["bday_day"]."\xA"); //6
			printf(''.$row["bday_year"]."\xA"); //7 
		   printf(''.$row["location_brgy"]."\xA"); //8
		   printf(''.$row["location_street"]."\xA"); //9
		   printf(''.$row["nick_name"]."\xA"); //10
		   printf(''.$row["donate_boolean"]."\xA"); //11
		   printf(''.$row["image"]."\xA"); //12
		    printf(''.$row["gender"]."\xA"); // 13
       }
        
?>