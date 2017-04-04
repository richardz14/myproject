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
Dim is_complete As Int : is_complete = 0
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
Public spin_item_click As String
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
	''http job
	Private data_query_id As HttpJob
	Private data_query_fullN As HttpJob
	Private data_query_location As HttpJob
	Private query_lat As HttpJob
	Private query_lng As HttpJob
	Private query_marker As HttpJob
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("search_frame")
	data_query_id.Initialize("data_query_id_get",Me)
	data_query_fullN.Initialize("data_query_fullN_get",Me)
	data_query_location.Initialize("data_query_location_get",Me)
	query_lat.Initialize("data_query_lat_get",Me)
	query_lng.Initialize("data_query_lng_get",Me)
    query_marker.Initialize("query_marker_get",Me)
	map_extras.addJavascriptInterface(map_webview,"B4A")
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
	 
	 list_btn.Left = ((list_panel.Width/2)/2)
	  search_lbl.Left = ((toolkit_pnl.Left + 3%x)+2%x)
	  search_spiner.Left = (search_lbl.Left + search_lbl.Width)
	    search_btn.Left = (search_spiner.Left + search_spiner.Width)
	''top
	 toolkit_pnl.Top = 0
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
	Dim url_id,full_name,location,lat,lng As String
	url_back.Initialize
	url_id = url_back.php_email_url("/bloodlifePHP/search_blood_id.php")
	full_name = url_back.php_email_url("/bloodlifePHP/search_blood_fullN.php")
	location = url_back.php_email_url("/bloodlifePHP/search_blood_location.php")
	lat = url_back.php_email_url("/bloodlifePHP/search_blood_lat.php")
	lng = url_back.php_email_url("/bloodlifePHP/search_blood_long.php")
	''url_id = url_back.php_email_url("/bloodlifePHP/search_blood_id.php")
	'Log(full_name&" "&spin_item_click)
	data_query_id.Download2(url_id,Array As String("id","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	data_query_fullN.Download2(full_name,Array As String("full_name","SELECT full_name FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	data_query_location.Download2(location,Array As String("location","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	query_lat.Download2(lat,Array As String("lat","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	query_lng.Download2(lng,Array As String("long","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"&spin_item_click&"';"))
	'data_query_id.Initialize("data_query_id_get",Me)
	'data_query_id.Initialize("data_query_fullN_get",Me)
	'data_query_id.Initialize("data_query_location_get",Me)
	
End Sub
Sub create_map
	ProgressDialogShow2("Creating the map, please wait...",False)
		Dim htmlString,html_content As String
	htmlString = File.GetText(File.DirAssets, "location_top.txt")
	htmlString = htmlString & " var markers=[]; var contents = []; var infowindows = [];"
	For i=0 To id_list.Size-1
	'Log(id_list.Get(i))
	'htmlString = htmlString & " var marker"&i&" = new google.maps.Marker({	position: new google.maps.LatLng("&lat_list.Get(i)&","&lng_list.Get(i)&"),map: map, title: '"&fullN_llist.Get(i)&"',clickable: true,icon: 'http://www.google.com/mapfiles/dd-end.png' });"
	'html_content = "<div align='left'><b><h2>Full Name: "&fullN_llist.Get(i)&"</h2></b><br>"
	'html_content = html_content & " <b><h4>Location: "&location_list.Get(i)&"</h4></b><br>"
	'html_content = html_content & " <b><h4>Blood Type: </h4></b><br>"
	'html_content = html_content & " <b><h4>Donated: </h4></b><br>"
	'html_content = html_content & " <b><h4>Email: </h4></b><br>"
	'html_content = html_content & " <b><h4>Phone Number 1: </h4></b><br>"
	'html_content = html_content & " <b><h4>Phone Number 2: </h4></b><br></div>"
	htmlString = htmlString & " markers["&i&"] = new google.maps.Marker({ position: new google.maps.LatLng("&lat_list.Get(i)&","&lng_list.Get(i)&"), map: map, title: '"&fullN_llist.Get(i)&"',clickable: true,icon: 'http://www.google.com/mapfiles/dd-end.png' }); "
	htmlString = htmlString & " markers["&i&"].index = "&i&";  contents["&i&"] = '<div class='alert alert-info'><b><h2>Full Name: "&fullN_llist.Get(i)&"</h2></b><b><h4>Location: "&location_list.Get(i)&"</h4></b><b><h4>Blood Type: </h4></b><b><h4>Donated: </h4></b><b><h4>Email: </h4></b><b><h4>Email: </h4></b><b><h4>Phone Number 1: </h4></b><b><h4>Phone Number 2: </h4></b></div>';"
	htmlString = htmlString & " infowindows["&i&"] = new google.maps.InfoWindow({ content: contents["&i&"], maxWidth: 350 });"
	htmlString = htmlString & " google.maps.event.addListener(markers["&i&"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); "
	Next
	
	htmlString = htmlString&File.GetText(File.DirAssets, "location_buttom.txt")		
	map_webview.LoadHtml(htmlString)
	Log(htmlString)
		
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
End Sub
Public Sub JobDone(job As HttpJob)
	If job.Success Then
		Select job.JobName
			Case "data_query_id_get" 
				    Dim TextWriter_id As TextWriter
   					 TextWriter_id.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_id.txt", False))
    		    TextWriter_id.WriteLine(job.GetString.Trim)
  			  TextWriter_id.Close
			'Email = job.GetString.Trim
			Case "data_query_fullN_get"
				 Dim TextWriter_full As TextWriter
   					 TextWriter_full.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_fullN.txt", False))
    		    TextWriter_full.WriteLine(job.GetString.Trim)
  			  TextWriter_full.Close
			'pass = job.GetString.Trim
			Case "data_query_location_get"
				Dim TextWriter_location As TextWriter
   					 TextWriter_location.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_location.txt", False))
    		    TextWriter_location.WriteLine(job.GetString.Trim)
  			  TextWriter_location.Close
			Case "data_query_lat_get"
				Dim TextWriter_lat As TextWriter
   					 TextWriter_lat.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_lat.txt", False))
    		    TextWriter_lat.WriteLine(job.GetString.Trim)
							 ' Log(job.GetString.Trim)
  			  TextWriter_lat.Close
			 Case "data_query_lng_get"
				Dim TextWriter_lng As TextWriter
   					 TextWriter_lng.Initialize(File.OpenOutput(File.DirInternalCache, "data_query_lng.txt", False))
    		    TextWriter_lng.WriteLine(job.GetString.Trim)
							'  Log(job.GetString.Trim)
  			  TextWriter_lng.Close 
			End Select	
			
	If is_complete == 4 Then
		ProgressDialogHide
		is_complete = 0
		reading_txt
		create_map
	End If
	is_complete = is_complete + 1
		Else If job.Success == False Then
		ProgressDialogHide
		is_complete = 0
		Msgbox("Error: Error connecting to server,please try again.!","Confirmation")
		
			
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

Sub list_btn_Click
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
	Log(row)
	Log(CRLF&"Item "&item)

End Sub

Sub reading_txt
		id_list.Initialize
		fullN_llist.Initialize
		location_list.Initialize
		lat_list.Initialize
		lng_list.Initialize
	''for id
	Dim TextReader_id As TextReader
    TextReader_id.Initialize(File.OpenInput(File.DirInternalCache, "data_query_id.txt"))
    Dim line_id As String
    line_id = TextReader_id.ReadLine    
    Do While line_id <> Null
        'Log(line_id) 'write the line to LogCat
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
	
	
	
End Sub
