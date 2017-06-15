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

$sql = json_decode($_POST["insert"]);
$urlPost=array();
foreach ($sql as $value) { 
    $urlPost['text_fn']=$value->text_fn;
    $urlPost['blood_type']=$value->blood_type;
    $urlPost['email']=$value->email;
    $urlPost['password'] = $value->password;
    $urlPost['phone_number1']=$value->phone_number1;
    $urlPost['phone_number2']=$value->phone_number2;
    $urlPost['location_brgy']=$value->location_brgy;
    $urlPost['location_street']=$value->location_street;
    $urlPost['location_purok']=$value->location_purok;
    $urlPost['bday_month']=$value->bday_month;
    $urlPost['bday_day']=$value->bday_day;
    $urlPost['bday_year']=$value->bday_year;
    $urlPost['nick_name']=$value->nick_name;
    $urlPost['donate_boolean']=$value->donate_boolean;
    $urlPost['lat']=$value->lat;
    $urlPost['long']=$value->long;
    $urlPost['image']=$value->image;
    $urlPost['age']=$value->age;
    $urlPost['date_donated']=$value->date_donated;
    $urlPost['gender']=$value->gender;
    $urlPost['id']=$value->id;
}
$image_encode = str_replace(' ', '+', $urlPost['image']);
$blood_encode = str_replace(' ', '+', $urlPost['blood_type']);
$query = "INSERT INTO `person_info` (`full_name`, `blood_type`, `email`,`password`, `phone_number1`, `phone_number2`,`location_brgy`, `location_street`, `location_purok`, `bday_month`,`bday_day`, `bday_year`,`nick_name`, `donate_boolean`,`lat`,`long`,`image`,`age`,`date_donated`,`gender`) VALUES ('".$urlPost['text_fn']."', '".$blood_encode."','".$urlPost['email']."',ENCODE('".$urlPost['password']."','goroy'),'".$urlPost['phone_number1']."','".$urlPost['phone_number2']."','".$urlPost['location_brgy']."','".$urlPost['location_street']."', '".$urlPost['location_purok']."','".$urlPost['bday_month']."','".$urlPost['bday_day']."','".$urlPost['bday_year']."','".$urlPost['nick_name']."','".$urlPost['donate_boolean']."','".$urlPost['lat']."','".$urlPost['long']."','".$image_encode ."','".$urlPost['age']."','".$urlPost['date_donated']."','".$urlPost['gender']."');";


mysqli_query($con,$query);
mysqli_close($con);

        
?>