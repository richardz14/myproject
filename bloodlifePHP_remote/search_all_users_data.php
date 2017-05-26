<?php

//$server = "localhost";
//$user = "root";
//$pass = "";
//$database = "bloodlife_db";

//include "database.php");
$server = "https://databases.000webhost.com";
$user = "id1598109_root";
$pass = "goroy2017";
$database = "id1598109_bloodlife_db";  

 $con = mysqli_connect("localhost",$user,$pass,$database) or die("error connecting");
if (mysqli_connect_errno()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
}

$sql = $_REQUEST["all_info"];
$sql_query = mysqli_query($con,$sql);

$row=mysqli_fetch_array($sql_query ,MYSQLI_ASSOC);
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

mysqli_close($con);

        
?>