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

while($row=mysqli_fetch_array($sql_query ,MYSQLI_ASSOC)){
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
mysqli_close($con);

        
?>