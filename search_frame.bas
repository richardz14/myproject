Type=Activity
Version=5.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'#AdditionalRes: <android sdk>\extras\google\google_play_services\libproject\google-play-services_lib\res, com.google.android.gms
#Region  Activity Attributes 
	#FullScreen: true
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
Dim list_bloodgroup As List

Dim id_list As List
Dim fullN_llist As List
Dim location_list As List
Dim lat_list As List
Dim lng_list As List
Dim donated_list As List
Dim email_list As List
Dim nickname_list As List
Dim phone1_list As List
Dim phone2_list As List
Dim image_list As List
Dim age_list As List
Dim gender_list As List
Dim is_complete As Int : is_complete = 0

Dim gpsClient As GPS
Dim userLocation As Location
Dim is_check_true As Boolean : is_check_true = False

Dim row_click As String

	Dim earth_radius As Float	: earth_radius = 6373 'default earth rotational radius
	Dim pi As Float : pi = 3.1416 'the default value of pi in matematical expression
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Public spin_item_click As String : 
	Private toolkit_pnl As Panel
	Private search_lbl As Label
	Private search_spiner As Spinner
	Private search_btn As Button
	Private map_webview As WebView
	Private list_btn As Button
	Private list_panel As Panel
	Private map_extras As WebViewExtras
	
	Dim scrolllista As ScrollView
		Dim item As Int
	Dim dialog_panel As Panel	
	Dim dialog_all_panel As Panel	
	''http job
	Private data_query_id As HttpJob
	Private data_query_fullN As HttpJob
	Private data_query_location As HttpJob
	Private query_lat As HttpJob
	Private query_lng As HttpJob
	Private data_query_donated As HttpJob
	Private data_query_email As HttpJob
	Private data_query_nickname As HttpJob
	Private data_query_phone1 As HttpJob
	Private data_query_phone2 As HttpJob
	Private data_query_image As HttpJob
	Private data_query_age As HttpJob
	Private data_query_gender As HttpJob
	Private query_marker As HttpJob
	Private isGPSon As CheckBox
	Dim view_info_pnl As Panel
	Dim view_data_info_person As Panel
	
	Dim scroll_view_info As ScrollView2D
	Dim user_img_panl As Panel
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("search_frame")
	data_query_id.Initialize("data_query_id_get",Me)
	data_query_fullN.Initialize("data_query_fullN_get",Me)
	data_query_location.Initialize("data_query_location_get",Me)
	query_lat.Initialize("data_query_lat_get",Me)
	query_lng.Initialize("data_query_lng_get",Me)
	data_query_donated.Initialize("data_query_donated_get",Me)
	data_query_email.Initialize("data_query_email_get",Me)
	data_query_nickname.Initialize("data_query_nickname_get",Me)
	data_query_phone1.Initialize("data_query_phone1_get",Me)
	data_query_phone2.Initialize("data_query_phone2_get",Me)
	data_query_image.Initialize("data_query_image",Me)
	data_query_age.Initialize("data_query_age_get",Me)
	data_query_gender.Initialize("data_query_gender_get",Me)
    query_marker.Initialize("query_marker_get",Me)
	map_extras.addJavascriptInterface(map_webview,"B4A")
	
	If FirstTime Then
		gpsClient.Initialize("gpsClient")
		userLocation.Initialize
	End If
	is_initialize
	all_layout_load
	load_list
End Sub

Sub all_layout_load
	Activity.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	toolkit_pnl.Color = Colors.Transparent
	list_panel.Color = Colors.Transparent
	search_btn.SetBackgroundImage(LoadBitmap(File.DirAssets,"esearch.png"))
	list_btn.SetBackgroundImage(LoadBitmap(File.DirAssets,"view all.png"))
	''width
	  toolkit_pnl.Width = Activity.Width
	  list_panel.Width = Activity.Width
	  map_webview.Width = Activity.Width
	  
	  list_btn.Width = 50%x
	  search_lbl.Width = 14%x
	  search_btn.Width = 14%x
	  search_spiner.Width = ((toolkit_pnl.Width - search_btn.Width)-search_lbl.Width) - 10%x
	''height
	  toolkit_pnl.Height = 14%y
	  list_panel.Height = 11%y
	  map_webview.Height =((Activity.Height - toolkit_pnl.Height)-list_panel.Height) 
	  
	  list_btn.Height = 9%y
	   search_lbl.Height = 10%y
	  search_btn.Height = 10%y
	  search_spiner.Height = 10%y
	''left
	 toolkit_pnl.Left = 0
	 list_panel.Left = 0
	 map_webview.Left = 0
	 isGPSon.Left = map_webview.Width - isGPSon.Width - 5%x
	 
	 list_btn.Left = ((list_panel.Width/2)/2)
	  search_lbl.Left = ((toolkit_pnl.Left + 3%x)+2%x)
	  search_spiner.Left = (search_lbl.Left + search_lbl.Width)
	    search_btn.Left = (search_spiner.Left + search_spiner.Width)
	''top
	 toolkit_pnl.Top = 0
	 isGPSon.Top = toolkit_pnl.Top + toolkit_pnl.Height + 1%y
	map_webview.Top = (toolkit_pnl.Top + toolkit_pnl.Height)
	 list_panel.Top = (map_webview.Top + map_webview.Height)
	 
	 list_btn.Top = 1%y'(list_panel.Top + 1%Y)
	 search_lbl.top = ((toolkit_pnl.Height/2)/3)
	  search_btn.top = ((toolkit_pnl.Height/2)/3)
	  search_spiner.top = ((toolkit_pnl.Height/2)/3)
End Sub
Sub load_list
	list_bloodgroup.Initialize
	list_bloodgroup.Add("A")
	list_bloodgroup.Add("B")
	list_bloodgroup.Add("O")
	list_bloodgroup.Add("AB")
	list_bloodgroup.Add("A+")
	list_bloodgroup.Add("B+")
	list_bloodgroup.Add("O+")
	list_bloodgroup.Add("AB+")
	list_bloodgroup.Add("A-")
	list_bloodgroup.Add("B-")
	list_bloodgroup.Add("O-")
	list_bloodgroup.Add("AB-")
	search_spiner.AddAll(list_bloodgroup)
	spin_item_click = "A";
End Sub


Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub
Sub search_btn_Click
	ProgressDialogShow2("please wait.!!",False)
	
	Dim url_back As calculations
	Dim url_id,full_name,location,lat,lng,donated,email,nickname,phone1,phone2,image,age,gender As String
	url_back.Initialize
	url_id = url_back.php_email_url("/bloodlifePHP/search_blood_id.php")
	full_name = url_back.php_email_url("/bloodlifePHP/search_blood_fullN.php")
	location = url_back.php_email_url("/bloodlifePHP/search_blood_location.php")
	lat = url_back.php_email_url("/bloodlifePHP/search_blood_lat.php")
	lng = url_back.php_email_url("/bloodlifePHP/search_blood_long.php")
	donated = url_back.php_email_url("/bloodlifePHP/search_blood_donateB.php")
	email = url_back.php_email_url("/bloodlifePHP/search_blood_email.php")
	nickname = url_back.php_email_url("/bloodlifePHP/search_blood_nickN.php")
	phone1 = url_back.php_email_url("/bloodlifePHP/search_blood_phone1.php")
	phone2 = url_back.php_email_url("/bloodlifePHP/search_blood_phone2.php")
	image = url_back.php_email_url("/bloodlifePHP/search_blood_image.php")
	age = url_back.php_email_url("/bloodlifePHP/search_blood_age.php")
	gender = url_back.php_email_url("/bloodlifePHP/search_blood_gender.php")
	
	''url_id = url_back.php_email_url("/bloodlifePHP/search_blood_id.php")
	'Log(full_name&" "&spin_item_click)
	data_query_id.Download2(url_id,Array As String("id","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	data_query_fullN.Download2(full_name,Array As String("full_name","SELECT full_name FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	data_query_location.Download2(location,Array As String("location","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	query_lat.Download2(lat,Array As String("lat","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	query_lng.Download2(lng,Array As String("long","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	
	data_query_donated.Download2(donated,Array As String("donate_b","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	data_query_email.Download2(email,Array As String("email","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	data_query_nickname.Download2(nickname,Array As String("nick","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	data_query_phone1.Download2(phone1,Array As String("phone1","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	data_query_phone2.Download2(phone2,Array As String("phone2","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	
	data_query_image.Download2(image,Array As String("image","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	
	data_query_age.Download2(age,Array As String("age","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	data_query_gender.Download2(gender,Array As String("gender","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	'data_query_id.Initialize("data_query_id_get",Me)
	'data_query_id.Initialize("data_query_fullN_get",Me)
	'data_query_id.Initialize("data_query_location_get",Me)

End Sub
Sub isGPSon_CheckedChange(Checked As Boolean)
	is_check_true = Checked
	If Checked == True Then
		If gpsClient.GPSEnabled=False Then
		ToastMessageShow("Please enable your device's GPS capabilities", True)
		isGPSon.Checked = False
        StartActivity(gpsClient.LocationSettingsIntent)
		Else
		gpsClient.Start(10.1799469, 122.9068577)
		ProgressDialogShow("Waiting for GPS location")
		End If
	Else
		create_map
	End If
End Sub
Sub gpsClient_LocationChanged (gpsLocation As Location)
	ProgressDialogHide
	userLocation=gpsLocation

	gpsClient.Stop
	create_map
	'isGPSon.Checked = False
End Sub
Sub create_map
	ProgressDialogShow2("Creating the map, please wait...",True)
	Dim htmlString1,htmlString1_1,htmlString2,htmlString3 As String
	Dim GPSlat,GPSlng,TOlat,TOlng,distance As Double
	Dim distanceMeter As Int
	htmlString1 = File.GetText(File.DirAssets, "location_top.txt")
	htmlString1 = htmlString1 & " var markers=[]; var contents = []; var infowindows = []; "
	Dim location As TextWriter
   					 location.Initialize(File.OpenOutput(File.DirInternalCache, "all_marker_location.txt", False))
    		    location.WriteLine(htmlString1)
		If is_check_true == True Then
		GPSlat = userLocation.Latitude
		GPSlng = userLocation.Longitude
		Log("lat: "&GPSlat)
		Log("long: "&GPSlng)
  		'htmlString1_1 = " new google.maps.Marker({ position: new google.maps.LatLng("&userLocation.Latitude&", "&userLocation.Longitude&"), map: map, title: 'My Location', content: 'I am here!', clickable: True, icon: 'http://www.google.com/mapfiles/arrow.png' }); " 'markers
		htmlString1_1 = " new google.maps.Marker({ position: new google.maps.LatLng("&GPSlat&", "&GPSlng&"), map: map, title: 'My Location', clickable: true, icon: 'http://www.google.com/mapfiles/dd-end.png' }); "
		location.WriteLine(htmlString1_1)
		Else		
	
		End If
		
	For i=0 To id_list.Size-1
		If is_check_true == True Then		
			TOlat = lat_list.Get(i)
			TOlng = lng_list.Get(i)
		
			distance = earth_radius * ACos( Cos( ( 90 - GPSlat ) * ( pi / 180 ) ) * Cos( ( 90 - TOlat ) * ( pi / 180 ) ) +  Sin( ( 90 - GPSlat ) * ( pi / 180 ) ) * Sin( ( 90 - TOlat ) * ( pi / 180 ) ) * Cos( ( GPSlng - TOlng ) * ( pi / 180 ) ) ) 
			distanceMeter = distance*1000
			'htmlString2 = "markers["&i&"] = new google.maps.Marker({position: new google.maps.LatLng("&lat_list.Get(i)&" , "&lng_list.Get(i)&"), map: map, title: '"&fullN_llist.Get(i)&"', icon: 'https://raw.githubusercontent.com/richardz14/myproject/master/Files/heart_arrow_img.png', clickable: true }); markers["&i&"].index = "&i&"; contents["&i&"] = '<div class=""well""><b><h3><center>"&fullN_llist.Get(i)&"</center></h3></b><h4>Blood Type: <b>"&spin_item_click&"</b></h4><h4>Email Address: <b>"&email_list.Get(i)&"</b></h4><h4>Location: <b>"&location_list.Get(i)&"</b></h4><h4>Phone Number 1: <b>"&phone1_list.Get(i)&"</b></h4><h4>Phone Number 2: <b>"&phone2_list.Get(i)&"</b></h4><h4>Donated: <b>"&donated_list.Get(i)&"</b></h4></div>'; infowindows["&i&"] = new google.maps.InfoWindow({ content: contents["&i&"], maxWidth: 500 }); google.maps.event.addListener(markers["&i&"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); "
			htmlString2 = "markers["&i&"] = new google.maps.Marker({position: new google.maps.LatLng("&lat_list.Get(i)&" , "&lng_list.Get(i)&"), map: map, title: '"&fullN_llist.Get(i)&"', icon: 'https://raw.githubusercontent.com/richardz14/myproject/master/Files/heart_arrow_img.png', clickable: true }); markers["&i&"].index = "&i&"; contents["&i&"] = '<div class=""iw-container""><div class=""iw-title"">Personal Information</div><div class=""iw-content""><b><h2><center>"&fullN_llist.Get(i)&"</center></h2></b><h4>Blood Type: <b>"&spin_item_click&"</b></h4><h4>Age: <b>"&age_list.Get(i)&"</b></h4><h4>Age: <b>"&gender_list.Get(i)&"</b></h4><h4>Email Address: <b>"&email_list.Get(i)&"</b></h4><h4>Location: <b>"&location_list.Get(i)&"</b></h4><h4>Phone Number 1: <b>"&phone1_list.Get(i)&"</b></h4><h4>Phone Number 2: <b>"&phone2_list.Get(i)&"</b></h4><h4>Donated: <b>"&donated_list.Get(i)&"</b></h4><h4><b>You are "&distanceMeter&"m away from the donor!</b></h4></div></div>'; infowindows["&i&"] = new google.maps.InfoWindow({ content: contents["&i&"], maxWidth: 350 }); google.maps.event.addListener(markers["&i&"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); "
			location.WriteLine(htmlString2)	
		Else
			htmlString2 = "markers["&i&"] = new google.maps.Marker({position: new google.maps.LatLng("&lat_list.Get(i)&" , "&lng_list.Get(i)&"), map: map, title: '"&fullN_llist.Get(i)&"', icon: 'https://raw.githubusercontent.com/richardz14/myproject/master/Files/heart_arrow_img.png', clickable: true }); markers["&i&"].index = "&i&"; contents["&i&"] = '<div class=""iw-container""><div class=""iw-title"">Personal Information</div><div class=""iw-content""><b><h2><center>"&fullN_llist.Get(i)&"</center></h2></b><h4>Blood Type: <b>"&spin_item_click&"</b></h4><h4>Age: <b>"&age_list.Get(i)&"</b></h4><h4>Age: <b>"&gender_list.Get(i)&"</b></h4><h4>Email Address: <b>"&email_list.Get(i)&"</b></h4><h4>Location: <b>"&location_list.Get(i)&"</b></h4><h4>Phone Number 1: <b>"&phone1_list.Get(i)&"</b></h4><h4>Phone Number 2: <b>"&phone2_list.Get(i)&"</b></h4><h4>Donated: <b>"&donated_list.Get(i)&"</b></h4></div></div>'; infowindows["&i&"] = new google.maps.InfoWindow({ content: contents["&i&"], maxWidth: 350}); google.maps.event.addListener(markers["&i&"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); "
			location.WriteLine(htmlString2)
		End If
		
	'distance = earth_radius * ACos( Cos( ( 90 - lats1 ) * ( pi / 180 ) ) * Cos( ( 90 - lats2 ) * ( pi / 180 ) ) +  Sin( ( 90 - lats1 ) * ( pi / 180 ) ) * Sin( ( 90 - lats2 ) * ( pi / 180 ) ) * Cos( ( lon1 - lon2 ) * ( pi / 180 ) ) )
	'htmlString2 = "markers["&i&"] = new google.maps.Marker({position: new google.maps.LatLng("&lat_list.Get(i)&" , "&lng_list.Get(i)&"), map: map, title: '"&fullN_llist.Get(i)&"', icon: 'http://www.google.com/mapfiles/dd-end.png', clickable: true }); markers["&i&"].index = "&i&"; contents["&i&"] = '<div class=""well""><b><h3><center>"&fullN_llist.Get(i)&"</center></h3></b><h4>Blood Type: <b>"&spin_item_click&"</b></h4><h4>Email Address: <b>"&email_list.Get(i)&"</b></h4><h4>Location: <b>"&location_list.Get(i)&"</b></h4><h4>Phone Number 1: <b>"&phone1_list.Get(i)&"</b></h4><h4>Phone Number 2: <b>"&phone2_list.Get(i)&"</b></h4><h4>Donated: <b>"&donated_list.Get(i)&"</b></h4></div>'; infowindows["&i&"] = new google.maps.InfoWindow({ content: contents["&i&"], maxWidth: 500 }); google.maps.event.addListener(markers["&i&"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); "
	'location.WriteLine(htmlString2)
	
	'Log(id_list.Get(i))
	'htmlString = htmlString & " var marker"&i&" = new google.maps.Marker({	position: new google.maps.LatLng("&lat_list.Get(i)&","&lng_list.Get(i)&"),map: map, title: '"&fullN_llist.Get(i)&"',clickable: true,icon: 'http://www.google.com/mapfiles/dd-end.png' });"
	'html_content = "<div align='left'><b><h2>Full Name: "&fullN_llist.Get(i)&"</h2></b><br>"
	'html_content = html_content & " <b><h4>Location: "&location_list.Get(i)&"</h4></b><br>"
	'html_content = html_content & " <b><h4>Blood Type: </h4></b><br>"
	'html_content = html_content & " <b><h4>Donated: </h4></b><br>"
	'html_content = html_content & " <b><h4>Email: </h4></b><br>"
	'html_content = html_content & " <b><h4>Phone Number 1: </h4></b><br>"
	'html_content = html_content & " <b><h4>Phone Number 2: </h4></b><br></div>"
	'htmlString = htmlString & " markers["&i&"] = new google.maps.Marker({ position: new google.maps.LatLng("&lat_list.Get(i)&" , "&lng_list.Get(i)&"), map: map, title: '"&fullN_llist.Get(i)&"', clickable: true, icon: 'http://www.google.com/mapfiles/dd-end.png' }); "
	'htmlString = htmlString & " markers["&i&"].index = "&i&"; contents["&i&"] = '<div class='alert alert-info'><b><h2>Full Name: "&fullN_llist.Get(i)&"</h2></b><b><h4>Location: "&location_list.Get(i)&"</h4></b><b><h4>Blood Type: "&spin_item_click&"</h4></b><b><h4>Donated: "&donated_list.get(i)&"</h4></b><b><h4>Email: "&email_list.Get(i)&"</h4></b><b><h4>Nickname: "&nickname_list.Get(i)&"</h4></b><b><h4>Phone Number 1: "&phone1_list.Get(i)&"</h4></b><b><h4>Phone Number 2: "&phone2_list.Get(i)&"</h4></b></div>'; "
	'htmlString = htmlString & " infowindows["&i&"] = new google.maps.InfoWindow({ content: contents["&i&"], maxWidth: 350 }); "
	'htmlString = htmlString & " google.maps.event.addListener(markers["&i&"], 'click', function() { infowindows[this.index].open(map, markers[this.index]); map.panTo(markers[this.index].getPosition()); }); "
    'htmlString = htmlString & " markers["&i&"] = new google.maps.Marker({"
    'htmlString = htmlString & " position: new google.maps.LatLng(40+Math.random()*5, 4+Math.random()*5),"
    'htmlString = htmlString & " map: map,"
    'htmlString = htmlString & " title: 'samplemarker',"
	'htmlString = htmlString & " icon: 'http://www.google.com/mapfiles/dd-end.png',"
	'htmlString = htmlString & " clickable: True"
   ' htmlString = htmlString & " });"

   ' htmlString = htmlString & " markers["&i&"].index = "&i&";"
   ' htmlString = htmlString & " contents["&i&"] = '<span class='help-block'></span><div class='alert alert-info'><b><h1>adsad</h1></b><b><h1>adsadqweqweqwewqe</h1></b></div>';"

   ' htmlString = htmlString & " infowindows["&i&"] = new google.maps.InfoWindow({"
   ' htmlString = htmlString & " content: contents["&i&"],"
   ' htmlString = htmlString & " maxWidth: 500"
   ' htmlString = htmlString & " });"

  '  htmlString = htmlString & " google.maps.event.addListener(markers["&i&"], 'click', function() {"
  ' htmlString = htmlString & " infowindows[this.index].open(map,markers[this.index]);"
  '   htmlString = htmlString & " map.panTo(markers[this.index].getPosition());"
   '  htmlString = htmlString & " });"
   
	Next			
	htmlString3 = File.GetText(File.DirAssets, "location_buttom.txt")		
	location.WriteLine(htmlString3)
	 location.Close
	 		'Dim TextReader_fullN As TextReader
    		'TextReader_fullN.Initialize(File.OpenInput(File.DirRootExternal, "users_all_info.txt"))
			'map_webview.LoadHtml(TextReader_fullN.ReadAll)
			'Log(TextReader_fullN.ReadAll)
	  	 ' TextReader_fullN.Close
	map_webview.LoadHtml(File.ReadString(File.DirInternalCache,"all_marker_location.txt"))
	
	'Log(File.ReadString(File.DirInternalCache,"all_marker_location.txt"))

End Sub
Sub map_shows_PageFinished (Url As String)
	ProgressDialogHide
End Sub	
Sub is_initialize
	Dim TextWriters As TextWriter
	TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_id.txt", False))
	TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_fullN.txt", False))
	 TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_location.txt", False))
	  TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_lat.txt", False))
	  TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_lng.txt", False))
	  TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_donated.txt", False))
	TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_email.txt", False))
	 TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_nickname.txt", False))
	  TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_phone1.txt", False))
	  TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_phone2.txt", False))
	    TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_image.txt", False))
		TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_age.txt", False))
		TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_gender.txt", False))
End Sub
Public Sub JobDone(job As HttpJob)
	If job.Success Then
		Select job.JobName
			Case "data_query_id_get" 
				    Dim TextWriter_id As TextWriter
   					 TextWriter_id.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_id.txt", False))
    		    TextWriter_id.WriteLine(job.GetString.Trim)
  			  TextWriter_id.Close
				'Log("1")
			Case "data_query_fullN_get"
				 Dim TextWriter_full As TextWriter
   					 TextWriter_full.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_fullN.txt", False))
    		    TextWriter_full.WriteLine(job.GetString.Trim)
  			  TextWriter_full.Close
			'Log("2")
			Case "data_query_location_get"
				Dim TextWriter_location As TextWriter
   					 TextWriter_location.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_location.txt", False))
    		    TextWriter_location.WriteLine(job.GetString.Trim)
  			  TextWriter_location.Close
			  'Log("33")
			Case "data_query_lat_get"
				Dim TextWriter_lat As TextWriter
   					 TextWriter_lat.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_lat.txt", False))
    		    TextWriter_lat.WriteLine(job.GetString.Trim)
				'Log("3")
  			  TextWriter_lat.Close
			 Case "data_query_lng_get"
				Dim TextWriter_lng As TextWriter
   					 TextWriter_lng.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_lng.txt", False))
    		    TextWriter_lng.WriteLine(job.GetString.Trim)
				'Log("4")
  			  TextWriter_lng.Close 
			  Case "data_query_donated_get"
			  	Dim TextWriter_donate As TextWriter
   					 TextWriter_donate.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_donated.txt", False))
    		    TextWriter_donate.WriteLine(job.GetString)
				TextWriter_donate.Close 
				'Log("5")
			  Case "data_query_email_get"
			  	Dim TextWriter_email As TextWriter
   					 TextWriter_email.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_email.txt", False))
    		    TextWriter_email.WriteLine(job.GetString.Trim)
				TextWriter_email.Close 
				'Log("6")
			  Case "data_query_nickname_get"
			  	Dim TextWriter_nickname As TextWriter
   					 TextWriter_nickname.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_nickname.txt", False))
    		    TextWriter_nickname.WriteLine(job.GetString.Trim)
				TextWriter_nickname.Close 
				'Log("7")
			  Case "data_query_phone1_get"
			  	Dim TextWriter_phone1 As TextWriter
   					 TextWriter_phone1.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_phone1.txt", False))
    		    TextWriter_phone1.WriteLine(job.GetString.Trim)
				TextWriter_phone1.Close 
				'Log("8")
			  Case "data_query_phone2_get"
			  	Dim TextWriter_phone2 As TextWriter
   					 TextWriter_phone2.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_phone2.txt", False))
    		    TextWriter_phone2.WriteLine(job.GetString.Trim)
				TextWriter_phone2.Close 
				'Log("9")
			  Case "data_query_image"
			  	Dim TextWriter_image As TextWriter
   					 TextWriter_image.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_image.txt", False))
    		    TextWriter_image.WriteLine(job.GetString.Trim)
				TextWriter_image.Close 	
			  Case "data_query_age_get"
			  	Dim TextWriter_age As TextWriter
   					 TextWriter_age.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_age.txt", False))
    		    TextWriter_age.WriteLine(job.GetString.Trim)
				TextWriter_age.Close 	
			  Case "data_query_gender_get"
			  	Dim TextWriter_gender As TextWriter
   					 TextWriter_gender.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_gender.txt", False))
    		    TextWriter_gender.WriteLine(job.GetString.Trim)
				TextWriter_gender.Close 	
			End Select	
		
			Log("datas: "&is_complete)
	If is_complete == 12 Then
		ProgressDialogHide
		is_complete = 0
		Try
		reading_txt
		create_map
		Catch
			Log(LastException.Message)
		End Try
	End If
	is_complete = is_complete + 1
		Else If job.Success == False Then
		ProgressDialogHide
		is_complete = 0
		Msgbox("Error: Error connecting to server,please try again.!","C O N F I R M A T I O N")
		

	End If

End Sub

Sub search_spiner_ItemClick (Position As Int, Value As Object)
	spin_item_click = search_spiner.GetItem(Position).Trim
End Sub
Sub lv_ItemClick (Position As Int, Value As Object)
	Dim calc As calculations
	calc.users_id = id_list.Get(Position)
	''Msgbox(""&id_list.Get(Position),"confirm")
	''Log(""&id_list.Get(Position))
End Sub

Sub list_btn_BACKUP_Click
	ProgressDialogShow2("Loading data, Please Wait...",False)
	If scrolllista.IsInitialized == True Then
	scrolllista.RemoveView
	dialog_panel.RemoveView
	End If
	scrolllista.Initialize(500)
	dialog_panel.Initialize("dialog_panel")
	Dim cd As CustomDialog2
	Dim pnl As Panel
	pnl.Initialize("pnl")
	Dim bgnd As ColorDrawable
	bgnd.Initialize(Colors.Cyan, 5dip)
	pnl.Background = bgnd
	'''Dim lv As ListView
	'''lv.Initialize("lv")
	''----------------------------
	reading_txt
		'''For i=0 To id_list.Size - 1
		'''	 	lv.AddTwoLinesAndBitmap(fullN_llist.Get(i),location_list.Get(i),Null)
		'''Next
	''---------------------------
	''lv.AddTwoLinesAndBitmap(""&i, "one", Null)

	'''pnl.AddView(lv, 1%x, 1%y, 75%x, 78%y)
	
	''''''''''''''''''''''''''''''''''
	'scrolllista.Panel.RemoveAllViews   ' Write this line if you have many text files and one scrolview. Clear Previous List
	Dim Bitmap1 As Bitmap
	Dim Panel0 As Panel
	Dim PanelTop, PanelHeight  As Int
	''Dim lista As List
	
	''lista=File.ReadList(File.DirAssets,"listlabel.txt")
	
	Bitmap1.Initialize(File.DirAssets,"banner1.png") ' First image of list
	PanelTop=1%y
	Panel0=scrolllista.Panel
	Panel0.Color=Colors.argb(0,0,0,0)  'sets the invisible panel 
	
	For i=0 To id_list.Size-1
	
		If i>0 And i<3 Then Bitmap1.Initialize(File.DirAssets,"banner.png")  'Images beyond the first. Only if you use 2 images and 2 label
		Dim ImageView1 As ImageView
		ImageView1.Initialize("data_list")		
		PanelHeight=12%y
		
		Panel0.AddView(ImageView1,1%x,PanelTop,71%x,PanelHeight)
		ImageView1.Tag=i&"1"
		ImageView1.Bitmap=Bitmap1
		ImageView1.Gravity=Gravity.fill
		
		Dim Label1, Label2 As Label
		Label1.Initialize("")
		Label2.Initialize("")
		Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,PanelHeight)
		Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,PanelHeight)
		
		Label1.TextColor= Colors.black
		Label1.TextSize= 17	
		Label1.Gravity=Gravity.CENTER
		Label1.Color=Colors.argb(0,0,0,0)
		Label1.Text=fullN_llist.Get(i) 'set data from list
	
		Label2.TextColor= Colors.black
		Label2.TextSize= 15	
		Label2.Gravity=Gravity.CENTER
		Label2.Color=Colors.argb(0,0,0,0)
		Label2.Text=location_list.Get(i) 'set data from list
			
		
		If i > id_list.size-1 Then i = id_list.size-1
		
		
		PanelTop=PanelTop+PanelHeight
	Next
	Panel0.Height=PanelTop
	''''''''''''''''''''''''''''''''''
	ProgressDialogHide 
	''dialog_panel.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	dialog_panel.AddView(scrolllista,1%x,1%y,75%x,78%y)
	cd.AddView(dialog_panel,75%x,78%y)
	''cd.AddView(pnl, 77%x, 80%y) ' sizing relative to the screen size is probably best
	cd.Show("List of people", "CANCEL", "VIEW", "", Null)		

End Sub

Sub data_list_Click
	Dim Send As View
	Dim row As Int
	Send=Sender
	row=Floor(Send.Tag/10) '20
		item=row
		row_click = row
	Log(row)	
	'Log(CRLF&"Item "&item)
	'' for modal VIEW and CANCEL 
	'''''''''''''''''''''''''''''''''''
	If view_info_pnl.IsInitialized == True Then
		view_info_pnl.RemoveView
	End If
	view_info_pnl.Initialize("view_info_pnl")
	Dim view_panl As Panel
	Dim vie_btn,can_btn As Button
	Dim lbl_tittle As Label
	lbl_tittle.Initialize("")
	view_panl.Initialize("view_panl")
	vie_btn.Initialize("vie_btn")
	can_btn.Initialize("can_btn")
	vie_btn.Text = "VIEW"
	can_btn.Text = "CANCEL"
			Dim V_btn,C_btn As GradientDrawable
			Dim colorG(2) As Int
			colorG(0) = Colors.White
			colorG(1) = Colors.Red
			C_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.CornerRadius = 50dip
			C_btn.CornerRadius = 50dip
		vie_btn.Background = V_btn
		can_btn.Background = C_btn
	lbl_tittle.Text = "SELECT ACTION"
	lbl_tittle.Gravity = Gravity.CENTER
	lbl_tittle.TextColor = Colors.White
	view_panl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	view_panl.AddView(lbl_tittle,1%x,2%y,72%x,8%y)
	view_panl.AddView(vie_btn,5%x,lbl_tittle.Top + lbl_tittle.Height + 1%y,31%x,8%y)
	view_panl.AddView(can_btn,vie_btn.Left+vie_btn.Width+2%x,vie_btn.Top,31%x,8%y)
	view_info_pnl.AddView(view_panl,13%x,((Activity.Height/2)/2),74%x,20%y)
	Activity.AddView(view_info_pnl,0,0,100%x,100%y)
End Sub
Sub view_info_pnl_click
	''don't delete this line
End Sub
Sub vie_btn_click

	view_info_pnl.RemoveView	
		If view_data_info_person.IsInitialized == True Then
		view_data_info_person.RemoveView	
		scroll_view_info.RemoveView		
	Else
	End If
	view_data_info_person.Initialize("view_data_info_person")
	scroll_view_info.Initialize(74%x,57%y,"scroll_view_info")
		
	Dim view_panl,view_for_image,view_for_btn As Panel
	Dim tittle,fullname,location,donated,email,phone1,phone2,age,gender As Label
	Dim fn_pnl,loc_pnl,don_pnl,ema_pnl,ph1_pnl,ph2_pnl,btn_pnl,age_pnl,gender_pnl As Panel
	Dim user_image,fn_img,loc_img,don_img,ema_img,ph1_img,ph2_img,age_img,gender_img As ImageView
					fn_img.Initialize("")
					loc_img.Initialize("")
					don_img.Initialize("")
					ema_img.Initialize("")
					ph1_img.Initialize("")
					ph2_img.Initialize("")
					user_image.Initialize("user_image")
					age_img.Initialize("")
					gender_img.Initialize("")
			fn_pnl.Initialize("")
			loc_pnl.Initialize("")
			don_pnl.Initialize("")
			ema_pnl.Initialize("")
			ph1_pnl.Initialize("")
			ph2_pnl.Initialize("")
			btn_pnl.Initialize("")
			age_pnl.Initialize("")
			gender_pnl.Initialize("")
	Dim ok_vie_btn As Button
		ok_vie_btn.Initialize("ok_vie_btn")
		tittle.Initialize("")
		fullname.Initialize("")
		location.Initialize("")
		donated.Initialize("")
		email.Initialize("")
		phone1.Initialize("")
		phone2.Initialize("")
		age.Initialize("")
		gender.Initialize("")
	view_panl.Initialize("")
	view_for_image.Initialize("")
	view_for_btn.Initialize("")
	fullname.Text = fullN_llist.Get(row_click)			'string outputs
	location.Text = ": "&location_list.Get(row_click)
	donated.Text = ": "&donated_list.Get(row_click)
	email.Text = ": "&email_list.Get(row_click)
	phone1.Text = ": "&phone1_list.Get(row_click)
	phone2.Text = ": "&phone2_list.Get(row_click)   	'string outputs
	age.Text = ": "&age_list.Get(row_click) 
	gender.Text = ": "&gender_list.Get(row_click) 
			location.Gravity = Gravity.CENTER_VERTICAL
			donated.Gravity = Gravity.CENTER_VERTICAL
			email.Gravity = Gravity.CENTER_VERTICAL
			phone1.Gravity = Gravity.CENTER_VERTICAL
			phone2.Gravity = Gravity.CENTER_VERTICAL
			age.Gravity = Gravity.CENTER_VERTICAL
			gender.Gravity = Gravity.CENTER_VERTICAL
				fullname.TextColor = Colors.Black
				location.TextColor = Colors.Black
				donated.TextColor = Colors.Black
				email.TextColor = Colors.Black
				phone1.TextColor = Colors.Black
				phone2.TextColor = Colors.Black
				ok_vie_btn.TextColor = Colors.Black
				age.TextColor = Colors.Black
				gender.TextColor = Colors.Black
	'tittle.Text = "Pofile Info"
	'tittle.Gravity = Gravity.CENTER
	fullname.Gravity = Gravity.CENTER
	ok_vie_btn.Text = "OK"
	view_panl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	'view_panl.AddView(tittle,1%x,2%y,72%x,8%y) ' title of modal
				'loc_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-21-home.png"))
				'don_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-152-new-window.png"))
				'ema_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-social-40-e-mail.png"))
				'ph1_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-354-nameplate-alt1.png"))
				'ph2_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-354-nameplate-alt2.png"))
				Dim loc_bitd,don_bitd,ema_bitd,ph1_bitd,ph2_bitd,age_bitd,gender_bitd As BitmapDrawable
				Dim loc_bit,don_bit,ema_bit,ph1_bit,ph2_bit,age_bit,gender_bit  As Bitmap
					loc_bit.Initialize(File.DirAssets,"glyphicons-21-home.png")
					don_bit.Initialize(File.DirAssets,"glyphicons-152-new-window.png")
					ema_bit.Initialize(File.DirAssets,"glyphicons-social-40-e-mail.png")
					ph1_bit.Initialize(File.DirAssets,"glyphicons-354-nameplate-alt1.png")
					ph2_bit.Initialize(File.DirAssets,"glyphicons-354-nameplate-alt2.png")
					age_bit.Initialize(File.DirAssets,"glyphicons-577-uk-rat-r18.png")
					gender_bit.Initialize(File.DirAssets,"glyphicons-25-parents.png")
						loc_bitd.Initialize(loc_bit)
						don_bitd.Initialize(don_bit)
						ema_bitd.Initialize(ema_bit)
						ph1_bitd.Initialize(ph1_bit)
						ph2_bitd.Initialize(ph2_bit)
						age_bitd.Initialize(age_bit)
						gender_bitd.Initialize(gender_bit)
				loc_img.Background = loc_bitd
				don_img.Background = don_bitd
				ema_img.Background = ema_bitd
				ph1_img.Background = ph1_bitd
				ph2_img.Background = ph2_bitd
				age_img.Background = age_bitd
				gender_img.Background = gender_bitd
	Dim inp As InputStream
	Dim bmp As Bitmap
	Dim su As StringUtils
	Dim bytes() As Byte
	bytes = su.DecodeBase64(image_list.Get(row_click))
	inp.InitializeFromBytesArray(bytes,0,bytes.Length)
	bmp.Initialize2(inp)
	''
	Dim bd As BitmapDrawable
	bd.Initialize(bmp)
	user_image.Background = bd
	'user_image.SetBackgroundImage(bmp)
		
			Dim fn_grad,don_grad,ema_grad,ph1_grad,ph2_grad,loc_grad,btn_grad,ok_btn_grad,age_grad,gender_grad As GradientDrawable
			Dim colorG(2),btn_color(2),panl_btn(2) As Int
			colorG(0) = Colors.White
			colorG(1) = Colors.Red
				btn_color(0) = Colors.Red
				btn_color(1) = Colors.White
					panl_btn(0) = Colors.Gray
					panl_btn(1) = Colors.Red
			fn_grad.Initialize("TOP_BOTTOM",colorG)
			don_grad.Initialize("TOP_BOTTOM",colorG)
			ema_grad.Initialize("TOP_BOTTOM",colorG)
			ph1_grad.Initialize("TOP_BOTTOM",colorG)
			ph2_grad.Initialize("TOP_BOTTOM",colorG)
			loc_grad.Initialize("TOP_BOTTOM",colorG)
			age_grad.Initialize("TOP_BOTTOM",colorG)
			gender_grad.Initialize("TOP_BOTTOM",colorG)
				btn_grad.Initialize("TOP_BOTTOM",panl_btn)
				ok_btn_grad.Initialize("TOP_BOTTOM",btn_color)
			fn_grad.CornerRadius = 10dip
			ok_btn_grad.CornerRadius = 50dip
		fn_pnl.Background = fn_grad		'fn_pnl.Color = Colors.LightGray
		don_pnl.Background = don_grad		'don_pnl.Color = Colors.LightGray
		ema_pnl.Background = ema_grad		'ema_pnl.Color = Colors.LightGray
		ph1_pnl.Background = ph1_grad		'ph1_pnl.Color = Colors.LightGray
		ph2_pnl.Background = ph2_grad		'ph2_pnl.Color = Colors.LightGray
		loc_pnl.Background = loc_grad		'loc_pnl.Color = Colors.LightGray
		age_pnl.Background = loc_grad
		gender_pnl.Background = loc_grad
			btn_pnl.Background = btn_grad
		ok_vie_btn.Background = ok_btn_grad	
		
		
	view_for_image.AddView(fn_pnl,0,0,74%x,30%y) ' full name
		'fn_pnl.AddView(fullname,0,1%y,72%x,8%y) ' full name image
		fn_pnl.AddView(user_image,((fn_pnl.Width/2)/2)-2%x,1.2%y,39%x,17%y)
		fn_pnl.AddView(fullname,0,user_image.Top + user_image.Height,72%x,10%y) ' full name
		
		fullname.TextSize = 25
		''
	view_panl.AddView(age_pnl,1%x,0,72%x,8%y) 
		age_pnl.AddView(age_img,4%x,1%y,6%x,6%y) ''  image of age boolean
		age_pnl.AddView(age,age_img.Left + age_img.Width + 1%x,1%y,50%x,6%y) 
		''
	view_panl.AddView(gender_pnl,1%x,age_pnl.Top + age_pnl.Height,72%x,8%y) 
		gender_pnl.AddView(gender_img,4%x,1%y,6%x,6%y) '' image of gender boolean
		gender_pnl.AddView(gender,gender_img.Left + gender_img.Width + 1%x,1%y,50%x,6%y) 
		''	
	view_panl.AddView(don_pnl,1%x,gender_pnl.Top + gender_pnl.Height,72%x,8%y) 
		don_pnl.AddView(don_img,4%x,1%y,6%x,6%y) '' image of donation boolean
		don_pnl.AddView(donated,don_img.Left + don_img.Width + 1%x,1%y,50%x,6%y) 
		''
	view_panl.AddView(ema_pnl,1%x,don_pnl.Top + don_pnl.Height,72%x,8%y) 
		ema_pnl.AddView(ema_img,4%x,1%y,6%x,6%y) '' image of email address
		ema_pnl.AddView(email,ema_img.Left + ema_img.Width + 1%x,1%y,50%x,6%y) 
		''
	view_panl.AddView(ph1_pnl,1%x,ema_pnl.Top + ema_pnl.Height,72%x,8%y) 
		ph1_pnl.AddView(ph1_img,4%x,1%y,6%x,6%y) '' image of phone number 1
		ph1_pnl.AddView(phone1,ph1_img.Left + ph1_img.Width + 1%x,1%y,50%x,6%y) 
		''
	view_panl.AddView(ph2_pnl,1%x,ph1_pnl.Top + ph1_pnl.Height,72%x,8%y) 
		ph2_pnl.AddView(ph2_img,4%x,1%y,6%x,6%y) '' image of phone number 2
		ph2_pnl.AddView(phone2,ph2_img.Left + ph2_img.Width + 1%x,1%y,50%x,6%y) 
		''
	view_panl.AddView(loc_pnl,1%x,ph2_pnl.Top + ph2_pnl.Height,72%x,8%y) 
		loc_pnl.AddView(loc_img,4%x,1%y,6%x,6%y) '' image of location
		loc_pnl.AddView(location,loc_img.Left + loc_img.Width + 1%x,1%y,50%x,6%y) 
			'btn_pnl
	'view_for_btn.AddView(btn_pnl,13%x,loc_pnl.Top + loc_pnl.Height,72%x,10%y) 
	btn_pnl.AddView(ok_vie_btn,((74%x/2)/2),1%y,37%x,8%y) 
	
		view_data_info_person.Color = Colors.ARGB(128,128,128,.70)
	'scroll_view_info.Panel.AddView(view_panl,13%x,((((Activity.Height/2)/2)/2)/2),74%x,83%y)
	'view_data_info_person.AddView(scroll_view_info,0,0,74%x,83%y)
		scroll_view_info.ScrollbarsVisibility(False,False)
		scroll_view_info.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	scroll_view_info.Panel.AddView(view_panl,0,0,74%x,57%y)
	view_data_info_person.AddView(view_for_image,13%x,((((Activity.Height/2)/2)/2)/2),74%x,31%y)
	view_data_info_person.AddView(scroll_view_info,13%x,view_for_image.Top + view_for_image.Height - .7%y,74%x,40%y)
	view_data_info_person.AddView(btn_pnl,13%x,scroll_view_info.Top + scroll_view_info.Height,74%x,10%y)
	
	Activity.AddView(view_data_info_person,0,0,100%x,100%y)
End Sub
Sub ok_vie_btn_click
	view_data_info_person.RemoveView
End Sub
Sub ok_vie_btn_LongClick
	view_data_info_person.RemoveView
End Sub
Sub can_btn_click
	view_info_pnl.RemoveView
End Sub
Sub user_image_click
	If user_img_panl.IsInitialized == True Then
		user_img_panl.RemoveView
	End If
	user_img_panl.Initialize("user_img_panl")
	Dim pnl As Panel
	Dim user_imgClose_btn As Button
	Dim img_user_webview As WebView
	Dim user_img_view As ImageView
	user_imgClose_btn.Initialize("user_imgClose_btn")
	user_img_view.Initialize("user_img_view")
	user_imgClose_btn.Text = "CLOSE"
			Dim V_btn,C_btn As GradientDrawable
			Dim colorG(2) As Int
			colorG(0) = Colors.White
			colorG(1) = Colors.Red
			C_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.CornerRadius = 50dip
			C_btn.CornerRadius = 50dip
		user_imgClose_btn.Background = V_btn
	pnl.Initialize("pnl")
	img_user_webview.Initialize("img_user_webview")
	user_img_panl.Color = Colors.Transparent
	pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	
	Dim inp As InputStream
	Dim bmp As Bitmap
	Dim su As StringUtils
	Dim bytes() As Byte
	bytes = su.DecodeBase64(image_list.Get(row_click))
	inp.InitializeFromBytesArray(bytes,0,bytes.Length)
	bmp.Initialize2(inp)
	''
	Dim bd As BitmapDrawable
	bd.Initialize(bmp)
	'usr_img.Bitmap = bd
	user_img_view.Background = bd
	

	'img_user_webview.LoadHtml("<html><body><img src='"&bd&"'></body></html>")
	
	pnl.AddView(user_img_view,1%x,1%y,86%x,75%y)
	pnl.AddView(user_imgClose_btn,1%x,user_img_view.Top + user_img_view.Height + 1%y,86%x,8%y)
	user_img_panl.AddView(pnl,6%x,(((((Activity.Height/2)/2)/2)/2)/2),88%x,89%y)
	user_img_panl.BringToFront
	'pnl_body.Enabled = False
		'pnl_donated_body.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(user_img_panl,0,0,100%x,100%y)	
End Sub
Sub user_imgClose_btn_click
	user_img_panl.RemoveView
End Sub
Sub user_img_panl_click
	'' don't delete this sub
End Sub
Sub view_data_info_person_click
	''don't delete this SUB
End Sub
Sub reading_txt
		id_list.Initialize
		fullN_llist.Initialize
		location_list.Initialize
		lat_list.Initialize
		lng_list.Initialize
	    donated_list.Initialize
		email_list.Initialize
		nickname_list.Initialize
		phone1_list.Initialize
		phone2_list.Initialize
		image_list.Initialize
		age_list.Initialize
		gender_list.Initialize
	''for id
	Dim TextReader_id As TextReader
    TextReader_id.Initialize(File.OpenInput(File.DirInternalCache, "data_query_id.txt"))
    Dim line_id As String
    line_id = TextReader_id.ReadLine    
    Do While line_id <> Null
		id_list.Add(line_id)
        line_id = TextReader_id.ReadLine
    Loop
    TextReader_id.Close
	''for full name
		Dim TextReader_fullN As TextReader
    TextReader_fullN.Initialize(File.OpenInput(File.DirInternalCache, "data_query_fullN.txt"))
    Dim line_fullN As String
    line_fullN = TextReader_fullN.ReadLine    
    Do While line_fullN <> Null
        'Log(line_fullN) 'write the line to LogCat
		fullN_llist.Add(line_fullN)
        line_fullN = TextReader_fullN.ReadLine
    Loop
    TextReader_fullN.Close
	''for location
		Dim TextReader_location As TextReader
    TextReader_location.Initialize(File.OpenInput(File.DirInternalCache, "data_query_location.txt"))
    Dim line_location As String
    line_location = TextReader_location.ReadLine    
    Do While line_location <> Null
        'Log(line_location) 'write the line to LogCat
		location_list.Add(line_location)
        line_location = TextReader_location.ReadLine
    Loop
    TextReader_location.Close
	
		Dim TextReader_lat As TextReader
    TextReader_lat.Initialize(File.OpenInput(File.DirInternalCache, "data_query_lat.txt"))
    Dim line_lat As String
    line_lat = TextReader_lat.ReadLine    
    Do While line_lat <> Null
		lat_list.Add(line_lat)
        line_lat = TextReader_lat.ReadLine
    Loop
    TextReader_lat.Close
	
		Dim TextReader_lng As TextReader
    TextReader_lng.Initialize(File.OpenInput(File.DirInternalCache, "data_query_lng.txt"))
    Dim line_lng As String
    line_lng = TextReader_lng.ReadLine    
    Do While line_lng <> Null
		lng_list.Add(line_lng)
        line_lng = TextReader_lng.ReadLine
    Loop
    TextReader_lng.Close
	
	Dim TextReader_donate As TextReader
    TextReader_donate.Initialize(File.OpenInput(File.DirInternalCache, "data_query_donated.txt"))
    Dim line_donate As String
    line_donate = TextReader_donate.ReadLine    
    Do While line_donate <> Null
		donated_list.Add(line_donate)
        line_donate = TextReader_donate.ReadLine
    Loop
    TextReader_donate.Close
	
	Dim TextReader_email As TextReader
    TextReader_email.Initialize(File.OpenInput(File.DirInternalCache, "data_query_email.txt"))
    Dim line_email As String
    line_email = TextReader_email.ReadLine    
    Do While line_email <> Null
		email_list.Add(line_email)
        line_email = TextReader_email.ReadLine
    Loop
    TextReader_email.Close
	
	Dim TextReader_nickname As TextReader
    TextReader_nickname.Initialize(File.OpenInput(File.DirInternalCache, "data_query_nickname.txt"))
    Dim line_nickname As String
    line_nickname = TextReader_nickname.ReadLine    
    Do While line_nickname <> Null
		nickname_list.Add(line_nickname)
        line_nickname = TextReader_nickname.ReadLine
    Loop
    TextReader_nickname.Close
	
	Dim TextReader_phone1 As TextReader
    TextReader_phone1.Initialize(File.OpenInput(File.DirInternalCache, "data_query_phone1.txt"))
    Dim line_phone1 As String
    line_phone1 = TextReader_phone1.ReadLine    
    Do While line_phone1 <> Null
		phone1_list.Add(line_phone1)
        line_phone1 = TextReader_phone1.ReadLine
    Loop
    TextReader_phone1.Close
	
	Dim TextReader_phone2 As TextReader
    TextReader_phone2.Initialize(File.OpenInput(File.DirInternalCache, "data_query_phone2.txt"))
    Dim line_phone2 As String
    line_phone2 = TextReader_phone2.ReadLine    
    Do While line_phone2 <> Null
		phone2_list.Add(line_phone2)
        line_phone2 = TextReader_phone2.ReadLine
    Loop
    TextReader_phone2.Close
	
		Dim TextReader_image As TextReader
    TextReader_image.Initialize(File.OpenInput(File.DirInternalCache, "data_query_image.txt"))
    Dim line_image As String
    line_image = TextReader_image.ReadLine    
    Do While line_image <> Null
		image_list.Add(line_image)
        line_image = TextReader_image.ReadLine
    Loop
    TextReader_image.Close
	
		Dim TextReader_age As TextReader
    TextReader_age.Initialize(File.OpenInput(File.DirInternalCache, "data_query_age.txt"))
    Dim line_age As String
    line_age = TextReader_age.ReadLine    
    Do While line_age <> Null
		age_list.Add(line_age)
        line_age = TextReader_age.ReadLine
    Loop
    TextReader_age.Close
	
	Dim TextReader_gender As TextReader
    TextReader_gender.Initialize(File.OpenInput(File.DirInternalCache, "data_query_gender.txt"))
    Dim line_gender As String
    line_gender = TextReader_gender.ReadLine    
    Do While line_gender <> Null
		gender_list.Add(line_gender)
        line_gender = TextReader_gender.ReadLine
    Loop
    TextReader_gender.Close
End Sub
Sub list_btn_Click
	ProgressDialogShow2("Loading data, Please Wait...",False)
	If scrolllista.IsInitialized == True Then
	scrolllista.RemoveView
	dialog_all_panel.RemoveView
	End If
	scrolllista.Initialize(500)
	dialog_panel.Initialize("dialog_panel")
	dialog_all_panel.Initialize("dialog_all_panel")
	'Dim cd As CustomDialog2
	Dim pnl As Panel
	pnl.Initialize("pnl")
	Dim bgnd As ColorDrawable
	bgnd.Initialize(Colors.Cyan, 5dip)
	pnl.Background = bgnd
	dialog_all_panel.Color = Colors.Transparent
	'''Dim lv As ListView
	'''lv.Initialize("lv")
	''----------------------------
	reading_txt
		'''For i=0 To id_list.Size - 1
		'''	 	lv.AddTwoLinesAndBitmap(fullN_llist.Get(i),location_list.Get(i),Null)
		'''Next
	''---------------------------
	''lv.AddTwoLinesAndBitmap(""&i, "one", Null)

	'''pnl.AddView(lv, 1%x, 1%y, 75%x, 78%y)
	
	''''''''''''''''''''''''''''''''''
	'scrolllista.Panel.RemoveAllViews   ' Write this line if you have many text files and one scrolview. Clear Previous List
	Dim Bitmap1 As Bitmap
	Dim Panel0 As Panel
	Dim PanelTop, PanelHeight  As Int
	''Dim lista As List
	
	''lista=File.ReadList(File.DirAssets,"listlabel.txt")
	
	Bitmap1.Initialize(File.DirAssets,"banner1.png") ' First image of list
	PanelTop=1%y
	Panel0=scrolllista.Panel
	Panel0.Color=Colors.argb(0,0,0,0)  'sets the invisible panel 
	
	For i=0 To id_list.Size-1
	
		If i>0 And i<3 Then Bitmap1.Initialize(File.DirAssets,"banner.png")  'Images beyond the first. Only if you use 2 images and 2 label
		Dim ImageView1 As ImageView
		ImageView1.Initialize("data_list")		
		PanelHeight=12%y
		
		Panel0.AddView(ImageView1,1%x,PanelTop,71%x,PanelHeight)
		ImageView1.Tag=i&"1"
		ImageView1.Bitmap=Bitmap1
		ImageView1.Gravity=Gravity.fill
		
		Dim Label1, Label2 As Label
		Label1.Initialize("")
		Label2.Initialize("")
		Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,PanelHeight)
		Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,PanelHeight)
		
		Label1.TextColor= Colors.White
		Label1.TextSize= 18
		Label1.Typeface = Typeface.DEFAULT_BOLD
		Label1.Gravity=Gravity.CENTER
		Label2.Color=Colors.argb(0,0,0,0)
		Label1.Text=fullN_llist.Get(i) 'set data from list
	
		Label2.TextColor= Colors.White
		Label2.TextSize= 15	
		Label2.Gravity=Gravity.CENTER
		Label2.Color=Colors.argb(0,0,0,0)
		Label2.Text=location_list.Get(i) 'set data from list
			
		
		If i > id_list.size-1 Then i = id_list.size-1
		
		
		PanelTop=PanelTop+PanelHeight
	Next
	Panel0.Height=PanelTop
	''''''''''''''''''''''''''''''''''
	ProgressDialogHide 
	dialog_panel.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	''dialog_panel.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	Dim dialog_panel_can_btn As Button
	Dim dialog_panel_tittle As Label
	Dim btn_panel As Panel
	btn_panel.Initialize("")
	btn_panel.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	dialog_panel_can_btn.Initialize("dialog_panel_can_btn")
	dialog_panel_tittle.Initialize("dialog_panel_tittle")
	dialog_panel_tittle.Text = "LIST OF PEOPLE"
	dialog_panel_can_btn.Text = "SEARCH"
			Dim se_btn As GradientDrawable
			Dim colorG(2) As Int
			colorG(0) = Colors.White
			colorG(1) = Colors.Red
			se_btn.Initialize("TOP_BOTTOM",colorG)
			se_btn.CornerRadius = 50dip
		dialog_panel_can_btn.Background = se_btn
	dialog_panel_tittle.TextSize = 30
	dialog_panel_tittle.Gravity = Gravity.CENTER
	dialog_panel_can_btn.Gravity = Gravity.CENTER
	dialog_panel.AddView(dialog_panel_tittle,1%x,2%y,83%x,8%y)
	dialog_panel.AddView(scrolllista,5%x,dialog_panel_tittle.Top + dialog_panel_tittle.Height+1%y,75%x,69%y)
	dialog_panel.AddView(btn_panel,1%x,79%y,83%x,10%y)
	btn_panel.AddView(dialog_panel_can_btn,((btn_panel.Width/2)/2),1%y,42%x,8%y)
	dialog_all_panel.AddView(dialog_panel,7.5%x,5%y,85%x,90%y)
	dialog_all_panel.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(dialog_all_panel,0,0,100%x,100%y)
	'cd.AddView(dialog_panel,75%x,78%y)
	''cd.AddView(pnl, 77%x, 80%y) ' sizing relative to the screen size is probably best
	'cd.Show("List of people", "CANCEL", "VIEW", "", Null)		

End Sub
Sub dialog_panel_can_btn_click
	dialog_all_panel.RemoveView
End Sub
Sub dialog_all_panel_click
	''don't delete this SUB
End Sub
