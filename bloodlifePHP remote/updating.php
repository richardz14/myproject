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

//$sql = json_decode($_REQUEST["update"]);
$sql = json_decode($_POST["update"]);
$urlPost=array();
foreach ($sql as $value) { 
    $urlPost['text_fn']=$value->text_fn;
    $urlPost['blood_type']=$value->blood_type;
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
//$image_decode = urldecode($image_encode);
$query = "UPDATE `person_info` SET `full_name`='".$urlPost['text_fn']."',`blood_type`='".$blood_encode."', `phone_number1`='".$urlPost['phone_number1']."', `phone_number2`='".$urlPost['phone_number2']."', `location_brgy`='".$urlPost['location_brgy']."', `location_street`='".$urlPost['location_street']."', `location_purok`='".$urlPost['location_purok']."', `bday_month`='".$urlPost['bday_month']."', `bday_day`='".$urlPost['bday_day']."', `bday_year`='".$urlPost['bday_year']."', `nick_name`='".$urlPost['nick_name']."', `donate_boolean`='".$urlPost['donate_boolean']."', `lat`='".$urlPost['lat']."', `long`='".$urlPost['long']."', `image`='".$image_encode."', `age`='".$urlPost['age']."', `date_donated`='".$urlPost['date_donated']."', `gender`='".$urlPost['gender']."' WHERE `id`=".$urlPost['id']."; ";

mysqli_query($con,$query);

mysqli_close($con);

        
?>