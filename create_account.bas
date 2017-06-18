Type=Activity
Version=5.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: true
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim list_bloodgroup As List
	Dim list_donate_confirm As List
	Dim list_bday_m,list_bday_d,list_bday_y As List
	Dim list_location_b,list_location_s,list_location_p As List
		Dim list_day,list_month,list_year As List
		Dim list_gender As List
		
	Dim lat As String : lat = "10.098014"
	Dim lng As String : lng = "122.869168"
	Dim brgy_index As Int : brgy_index = 0
	Dim street_index As Int : street_index = 0
	Dim gender_index As Int : gender_index = 0
	Dim ageGet As Int
	
		Dim isDonateDate As String
		Dim spin_donate_pos As Int : spin_donate_pos =0
		Dim donate_m_pos,donate_d_pos,donate_y_pos As Int
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module
	Dim h,w,t,l As Double
	Private scrool_2d As ScrollView2D
	Private ban_panel As Panel
	Private ban_logo As ImageView
	Private ban_picture As ImageView
	Private uptext_panel As Panel
	Private indicator As Label
	Private all_inputs As Panel
	Private create_panel As Panel
	Private lab_fullname As Label
	Private text_fn As EditText
	Private lab_bloodgroup As Label
	Private spin_bloodgroup As Spinner
	Private lab_email As Label
	Private text_email As EditText
	Private lab_password As Label
	Private text_password As EditText
	Private lab_phonenumber As Label
	Private text_phonenumber As EditText

	Private lab_needReset As Label
	Private lab_donate_confirm As Label
	Private spin_donate_confirm As Spinner
	Private reg_button As Button
	Private lab_question As Label
	Private text_answer As EditText
	
	Private lab_location As Label
	
	Private location_spin_brgy As Spinner
	Private location_spin_street As Spinner
'	Private location_spin_purok As Spinner
	Private bday As Label
	Private bday_spin_month As Spinner
	Private bday_spin_day As Spinner
	Private bday_spin_year As Spinner
		Dim spin_day,spin_month,spin_year As Spinner
	'Private text_question As EditText
	Private bday_panel As Panel
	Private location_panel As Panel
	Private text_password2 As EditText
	Private text_phonenumber2 As EditText
	
	Private insert_job As HttpJob
	Private existing_email As HttpJob
	Private email_exists As String
	'Private email_list As List
	
	Dim pnl_bday_body As Panel
	Private is_donate_date As Label
	Private spin_gender As Spinner
	
		Dim a1 As Animation
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("create_account")
	Activity.Title = "CREATE ACCOUNT"
	all_settings_layout
	scrolling
	for_btn_animation
		lab_needReset.Visible = False 'for the need reset label can't see..
		location_panel.Color = Colors.Transparent
	bday_panel.Color = Colors.Transparent
	insert_job.Initialize("inserting",Me)
	existing_email.Initialize("email_exist",Me)
End Sub
Sub for_btn_animation
	
	a1.InitializeAlpha("", 1, 0)
	reg_button.Tag = a1
	a1.Duration = 400
	a1.RepeatCount = 1
	a1.RepeatMode = a1.REPEAT_REVERSE
End Sub
Private Sub email_existance
	ProgressDialogShow2("Please wait...",False)	
	Dim email_url As String
	Dim url_back As calculations
	url_back.Initialize
	email_url = url_back.php_email_url("index.php")
	'email_list.Initialize
	existing_email.Download2(email_url,Array As String("email","SELECT email FROM `person_info` where `email`='"&text_email.Text&"';"))
End Sub
Public Sub JobDone(job As HttpJob)

	If job.Success Then
		Select job.JobName
			Case "email_exist"
			email_exists = job.GetString.Trim
			'Log(email_exists)
			If email_exists.Contains(text_email.Text) == True Then
				ProgressDialogHide
					Msgbox("Error: Email address are already existed.!","C O N F I R M A T I O N")
				Else
					existing_result
			End If
			'email_list.Add(job.GetString)
			'Log(email_list.Get(0))
		End Select
	
	Else If job.Success == False Then
		ProgressDialogHide
		Msgbox("Error: Error connecting to server, try again later.!","C O N F I R M A T I O N")	
	End If
	ProgressDialogHide
End Sub
Sub reg_button_Click
	a1.Start(reg_button)
	
	Dim full_name As String : full_name = text_fn.Text
	Dim blood_type As String : blood_type = spin_bloodgroup.SelectedItem
	Dim email As String : email = text_email.Text
	Dim password1 As String : password1 = text_password.Text
	Dim password2 As String : password2 = text_password2.Text
	Dim phone_number1 As String :  phone_number1 = text_phonenumber.Text
	Dim phone_number2 As String : phone_number2 = text_phonenumber2.Text
	Dim brgy,street,purok As String
	brgy = location_spin_brgy.SelectedItem
	street = location_spin_street.SelectedItem
'	purok = location_spin_purok.SelectedItem
	Dim month,day,year As String
	month = bday_spin_month.SelectedItem
	day = bday_spin_day.SelectedItem
	year = bday_spin_year.SelectedItem
	Dim answer As String : answer = text_answer.Text
	Dim donate_boolean As String : donate_boolean = spin_donate_confirm.SelectedItem
	
	Dim Nmonth,Nday,Nyear As Int
	   Nday = DateTime.GetDayOfMonth(DateTime.Now)
	  Nmonth = DateTime.GetMonth(DateTime.Now)
	  Nyear = DateTime.GetYear(DateTime.Now)
	  
		Dim age,Pyear,Pmonth,Pday As Int
		Pyear = year
		Pmonth = month
		Pday = day
		age = Nyear - Pyear
		If Pmonth <= Nmonth And Pday <= Nday Then
           'year_sub = year_sub+1;
		   ageGet = age
       Else
            ageGet = age-1
       End If
	   
	  'Log("date: "&Nmonth&"/"&Nday&"/"&Nyear&"  =  "&ageGet)
	'' registering process...
		''comparing empty fields...
	If text_fn.Text == ""  Or text_email.Text == "" Or text_password.Text == "" Or text_password2.Text == "" Or text_phonenumber.Text == "" Or text_phonenumber2.Text == "" Or text_password2.Text == "" Or text_answer.Text == "" Then
		ProgressDialogHide
		Msgbox("Error: Fill up those empty fields before you registered!","C O N F I R M A T I O N")
		Else
		'' compairing password matching..
		If password1.Contains(password2) == False Then
			ProgressDialogHide
			text_password.Text = ""
			text_password2.Text = ""
			Msgbox("Error: Password did not match!","C O N F I R M A T I O N")
		else if text_email.Text.IndexOf("@") == -1 Or text_email.Text.IndexOf(".") == -1 Then
			ProgressDialogHide
			text_password.Text = ""
			text_password2.Text = ""
			Msgbox("Error: Must be a valid email address!","C O N F I R M A T I O N")
		else if text_phonenumber.Text.Length <= 10 Or text_phonenumber2.Text.Length <= 10 Then
			ProgressDialogHide
			text_password.Text = ""
			text_password2.Text = ""
			Msgbox("Error: Phone number must be 11 digits!","C O N F I R M A T I O N")
		else if text_password.Text.Length <= 5 Or text_password2.Text.Length <= 5 Then
			ProgressDialogHide
			Msgbox("Error: Password must contain a minimum of 6 letters and above!","C O N F I R M A T I O N")
		Else if ageGet <= 18 Then
			ProgressDialogHide	
			Msgbox("Error: Age must be 18 above to register!","C O N F I R M A T I O N")
			Else
			ProgressDialogHide
			email_existance
			'	Msgbox("Error: Email Address are already existed.!","Confirmation")
			''
			'	ProgressDialogShow2("Registering, Please wait...",False)
			'	Dim url_back As calculations
			'	Dim ins,m_1,m_2,merge As String
			'	url_back.Initialize	
			'	m_1 = "INSERT INTO `person_info` (`full_name`,`blood_type`,`email`,`password`,`phone_number1`,`phone_number2`,`location_brgy`,`location_street`,`location_purok`,`bday_month`,`bday_day`,`bday_year`,`nick_name`,`donate_boolean`) "
			'	m_2 = "VALUES ('"&full_name&"', '"&blood_type&"','"&email&"',ENCODE('"&password2&"','goroy'),'"&phone_number1&"','"&phone_number2&"','"&brgy&"','"&street&"','"&purok&"','"&month&"','"&day&"','"&year&"','"&answer&"','"&donate_boolean&"');"
			'	merge = m_1&m_2
			'	ins = url_back.php_email_url("inserting.php")
			'	insert_job.Download2(ins,Array As String("insert",""&merge))
			''
			'Dim choose As Int : choose = Msgbox2("Sucessfuly Registered","Confirmation","OK","","",Null)
				'Select choose
				'	Case DialogResponse.POSITIVE
				'		Activity.Finish
				'		StartActivity("login_form")
				'End Select
		End If '' end compairing password matching..
	End If	'' end of compairing fields...
	''''''---------------------------
	'' stop registering process...
End Sub

Private Sub existing_result

	Dim full_name As String : full_name = text_fn.Text
	Dim blood_type As String : blood_type = spin_bloodgroup.SelectedItem
	Dim email As String : email = text_email.Text
	Dim password1 As String : password1 = text_password.Text
	Dim password2 As String : password2 = text_password2.Text
	Dim phone_number1 As String :  phone_number1 = text_phonenumber.Text
	Dim phone_number2 As String : phone_number2 = text_phonenumber2.Text
	Dim brgy,street,purok As String
	brgy = location_spin_brgy.SelectedItem
	street = location_spin_street.SelectedItem
'	purok = location_spin_purok.SelectedItem
	Dim month,day,year As String
	month = bday_spin_month.SelectedItem
	day = bday_spin_day.SelectedItem
	year = bday_spin_year.SelectedItem
	Dim answer As String : answer = text_answer.Text
	Dim donate_boolean As String : donate_boolean = spin_donate_confirm.SelectedItem
	
		If spin_donate_pos == 0 Then
			isDonateDate = "NONE"
		Else
			
		End If
	''
				'ProgressDialogShow2("Registering, Please wait...",False)
				Dim url_back As calculations
				Dim ins,m_1,m_2,merge As String
				Dim gender_string As String
				url_back.Initialize	
				
				gender_string = spin_gender.GetItem(gender_index)
				Dim img_string As String
					Dim su As StringUtils
					Dim out1 As OutputStream
					
					out1.InitializeToBytesArray(0) 'size not really important
					If gender_index == 0 Then
					File.Copy2(File.OpenInput(File.DirAssets, "male_clip.png"), out1)
					Else
					File.Copy2(File.OpenInput(File.DirAssets, "female_clip.png"), out1)	
					End If
					img_string=su.EncodeBase64(out1.ToBytesArray)
					'Log(img_string)
					
				'street_lat_lng
				m_1 = "INSERT INTO `person_info` (`full_name`,`blood_type`,`email`,`password`,`phone_number1`,`phone_number2`,`location_brgy`,`location_street`,`bday_month`,`bday_day`,`bday_year`,`nick_name`,`donate_boolean`,`lat`,`long`,`image`,`age`,`date_donated`,`gender`) "
				m_2 = "VALUES ('"&full_name&"', '"&blood_type&"','"&email&"',ENCODE('"&password2&"','goroy'),'"&phone_number1&"','"&phone_number2&"','"&brgy&"','"&street&"','"&month&"','"&day&"','"&year&"','"&answer&"','"&donate_boolean&"','"&lat&"','"&lng&"','"&img_string&"','"&ageGet&"','"&isDonateDate&"','"&gender_string&"');"
				merge = m_1&m_2
				ins = url_back.php_email_url("inserting.php")
				
				'insert_job.PostString(ins,"insert="&merge)
				'insert_job.Download2(ins,Array As String("insert",m_1&m_2))

				'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
		Dim htp As JSONGenerator
		Dim hps As JSONParser ''not used!!
		Dim maps As Map
		Dim JSONList As List
		JSONList.Initialize
			maps.Initialize
			maps.Put("text_fn",full_name)
			maps.Put("blood_type",blood_type)
			maps.Put("email",email)
			maps.Put("password",password2)
			maps.Put("phone_number1",phone_number1)
			maps.Put("phone_number2",phone_number2)
			maps.Put("location_brgy",brgy)
			maps.Put("location_street",street)
			maps.Put("location_purok","Him. City")
			maps.Put("bday_month",month)
			maps.Put("bday_day",day)
			maps.Put("bday_year",year)
			maps.Put("nick_name",answer)
			maps.Put("donate_boolean",donate_boolean)
			maps.Put("lat",lat)
			maps.Put("long",lng)
			maps.Put("age",ageGet)
			maps.Put("date_donated",isDonateDate)
			maps.Put("gender",gender_string)
			maps.Put("id",login_form.id_query)
			maps.Put("image",img_string)
		JSONList.Add(maps)
		'htp.Initialize(JSONList)
		htp.Initialize2(JSONList)
		Dim JSONstring As String
   		JSONstring = htp.ToString
		'update_img_job.Download2(ins,Array As String("JSONdata",JSONstring))
		insert_job.PostString(ins,"insert="&htp.ToString)
		
		'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
				
				
				
				ProgressDialogHide
			''
			Dim choose As Int : choose = Msgbox2("Sucessfuly Registered, this will be redirect to login!","C O N F I R M A T I O N","OK","","",Null)
				Select choose
					Case DialogResponse.POSITIVE
						Activity.Finish
						StartActivity("login_form")
				End Select
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)
	
End Sub

Public Sub all_settings_layout
	Activity.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
    uptext_panel.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	create_panel.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	ban_picture.SetBackgroundImage(LoadBitmap(File.DirAssets,"banner01.jpg"))
	ban_logo.SetBackgroundImage(LoadBitmap(File.DirAssets,"logo1.jpg"))
	
	'' width
	ban_panel.Width = Activity.Width
	'all_inputs.Width = Activity.Width
	create_panel.Width = Activity.Width
	uptext_panel.Width = Activity.Width
	ban_picture.Width = 80%x
	ban_logo.Width = ban_panel.Width - ban_picture.Width
	indicator.Width = 100%x
	
	reg_button.Width = 50%x
		'location_panel
	'location_spin_brgy.Width = location_panel.Width - 52%x
	'location_spin_street.Width = location_panel.Width - 51%x
	'' height
	ban_panel.Height = 17%y
	create_panel.Height = 12%y
	uptext_panel.Height = 8%y
	'all_inputs.Height = Activity.Height - ban_panel.Height - uptext_panel.Height - create_panel.Height
	ban_picture.Height =ban_panel.Height
	ban_logo.Height = ban_panel.Height
	'' top
	ban_picture.Top = Activity.Top
	ban_logo.Top = Activity.Top
	ban_panel.Top = Activity.Top
	create_panel.Top = Activity.Height - create_panel.Height
	uptext_panel.Top = ban_panel.Top + ban_panel.Height
	indicator.Top = (uptext_panel.Height/2) - 8dip
	reg_button.Top = ((create_panel.Height/2)/2) '4%y
	
	'' left
	ban_panel.Left = Activity.Left
	'all_inputs.Left = Activity.Left
	create_panel.Left = Activity.Left
	uptext_panel.Left = Activity.Left
	ban_logo.Left = Activity.Left
	ban_picture.Left = ban_logo.Left + ban_logo.Width
	'indicator.Left = uptext_panel.Left + ((uptext_panel.Width/2) - (uptext_panel.Width/8.5))
	indicator.Left = uptext_panel.Left
	indicator.Gravity = Gravity.CENTER_HORIZONTAL
	reg_button.Left = 25%x'create_panel.Left + ((create_panel.Width/2) - (create_panel.Width/5))
	
	'location_spin_brgy.Left = 2%X
	'location_spin_street.Left = location_spin_brgy.Left + location_spin_brgy.Width
	''
	Dim gradiant As GradientDrawable
		Dim col(2) As Int
		col(0) = Colors.Red
		col(1) = Colors.LightGray
		gradiant.Initialize("TOP_BOTTOM",col)
		gradiant.CornerRadius = 5dip
		'ban_create.Background = gradiant
		reg_button.Background = gradiant
		
	reg_button.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")	
	indicator.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")	
	indicator.TextSize = 18
	Dim calc As calculations
	calc.Initialize
	h = calc.sums_height(Activity.Height - ban_panel.Height - uptext_panel.Height - create_panel.Height)
	w = calc.sums_width(Activity.Width)
	l = calc.sums_left(Activity.Left)
	t = calc.sums_top(uptext_panel.Top - uptext_panel.Height)
End Sub
Sub spinners_list_data
	list_bloodgroup.Initialize
	list_donate_confirm.Initialize
	list_bday_m.Initialize
	list_bday_d.Initialize
	list_bday_y.Initialize
	list_location_b.Initialize
	list_location_s.Initialize
	list_gender.Initialize
	'list_location_p.Initialize
	''
	list_bloodgroup.Add("A+")
	list_bloodgroup.Add("B+")
	list_bloodgroup.Add("O+")
	list_bloodgroup.Add("AB+")
	'list_bloodgroup.Add("A+")
	'list_bloodgroup.Add("B+")
	'list_bloodgroup.Add("O+")
	'list_bloodgroup.Add("AB+")
	list_bloodgroup.Add("A-")
	list_bloodgroup.Add("B-")
	list_bloodgroup.Add("O-")
	list_bloodgroup.Add("AB-")
	spin_bloodgroup.AddAll(list_bloodgroup)
	''
	list_donate_confirm.Add("NO")
	list_donate_confirm.Add("YES")
	spin_donate_confirm.AddAll(list_donate_confirm)
	''
	list_gender.Add("Male")
	list_gender.Add("Female")
	spin_gender.AddAll(list_gender)
	''
	 For i = 1 To 31
	  	list_bday_d.Add(i)
	  Next
	 Dim iNowYear As Int
	 iNowYear = DateTime.GetYear(DateTime.Now)
	  For ii = 1950 To DateTime.GetYear(DateTime.Now)
	  	list_bday_y.Add(iNowYear)
		iNowYear = iNowYear-1
	  Next
		For iii = 1 To 12
			list_bday_m.Add(iii)
		Next
	bday_spin_month.AddAll(list_bday_m)
	bday_spin_day.AddAll(list_bday_d)
	bday_spin_year.AddAll(list_bday_y)
	''
	list_location_b.Add("Barangay  1") 'index 0
	list_location_b.Add("Barangay 2") 'index 1
	list_location_b.Add("Barangay 3") 'index 2
	list_location_b.Add("Barangay 4") 'index 3
	list_location_b.Add("Aguisan") 'index 4
	list_location_b.Add("caradio-an") 'index 5
	list_location_b.Add("Buenavista") 'index 6
	list_location_b.Add("Cabadiangan") 'index 7 
	list_location_b.Add("Cabanbanan") 'index 8
	list_location_b.Add("Carabalan") 'index 9
	list_location_b.Add("Libacao") 'index 10
	list_location_b.Add("Mahalang") 'index 11
	list_location_b.Add("Mambagaton") 'index 12
	list_location_b.Add("Nabalian") 'index 13
	list_location_b.Add("San Antonio") 'index 14
	list_location_b.Add("Saraet") 'index 15
	list_location_b.Add("Suay") 'index 16
	list_location_b.Add("Talaban") 'index 17
	list_location_b.Add("Tooy") 'index 18
	
	'list_location_s.Add("Gonzaga")
'	list_location_p.Add("Gwapo")
	
	
	list_location_s.Add("Rizal st.")
	list_location_s.Add("Valega st.")
	list_location_s.Add("Sarmiento st.")
	list_location_s.Add("Bantolinao st.")
	list_location_s.Add("Versoza st.")
	list_location_s.Add("Jimenez st.")
	list_location_s.Add("Olmedo st.")
	list_location_s.Add("Burgos st.")
	
	location_spin_street.AddAll(list_location_s)
	location_spin_brgy.AddAll(list_location_b)

End Sub
Sub scrolling
	'scrool_2d.Initialize(100%x,Activity.Height - ban_panel.Height - uptext_panel.Height - create_panel.Height + 5%y,"scroll2d")
	scrool_2d.Initialize(100%x,112%y,"scroll2d")
	'scrool_2d.Panel.AddView(create_all_inputs,0,uptext_panel.Top - uptext_panel.Height,100%x,Activity.Height - ban_panel.Height - uptext_panel.Height - create_panel.Height)
	scrool_2d.Panel.LoadLayout("create_all_inputs")
	scrool_2d.ScrollbarsVisibility(False,False)
	all_inputs.Width = 100%x
	all_inputs.Height = 100%y
	scrool_2d.Panel.SendToBack
	'scrool_2d.Panel.Height = Activity.Height - ban_panel.Height - uptext_panel.Height - create_panel.Height - 10%y
	all_inputs.SendToBack
	all_inputs.Color = Colors.Transparent
	Activity.AddView(scrool_2d,0,uptext_panel.Top + uptext_panel.Height ,100%x, Activity.Height - ban_panel.Height - uptext_panel.Height - create_panel.Height)
	spinners_list_data
End Sub
Sub street_lat_lng
    If brgy_index == 0 And street_index == 0 Then 'brgy 1
	lat = "10.098014"
	lng = "122.869168"
	else If brgy_index == 0 And street_index == 1 Then
	lat = "10.097226"
	lng = "122.870659"
	else If brgy_index == 0 And street_index == 2 Then
	lat = "10.097711"
	lng = "122.868378"
	else If brgy_index == 0 And street_index == 3 Then
	lat = "10.098293"
	lng = "122.868977"
	else If brgy_index == 0 And street_index == 4 Then
	lat = "10.097031"
	lng = "122.868764"
	else If brgy_index == 0 And street_index == 5 Then
	lat = "10.096021"
	lng = "122.869737"
	else If brgy_index == 0 And street_index == 6 Then
	lat = "10.095142"
	lng = "122.868317"
	else If brgy_index == 0 And street_index == 7 Then
	lat = "10.095303"
	lng = "122.869509"
	End If
	
	If brgy_index == 1 And street_index == 0 Then 'brgy 2
	lat = "10.101356"
	lng = "122.870075"
	else If brgy_index == 1 And street_index == 1 Then
	lat = "10.100583"
	lng = "122.870176"
	else If brgy_index == 1 And street_index == 2 Then
	lat = "10.100031"
	lng = "122.870623"
	else If brgy_index == 1 And street_index == 3 Then
	lat = "10.101327"
	lng = "122.871177"
	else If brgy_index == 1 And street_index == 4 Then
	lat = "10.103330"
	lng = "122.871391"
	else If brgy_index == 1 And street_index == 5 Then
	lat = "10.102317"
	lng = "122.870755"
	else If brgy_index == 1 And street_index == 6 Then
	lat = "10.104250"
	lng = "122.882834"
	else If brgy_index == 1 And street_index == 7 Then
	lat = "10.104943"
	lng = "122.885207"
	else If brgy_index == 1 And street_index == 8 Then
	lat = "10.101843"
	lng = "122.871020"
	else If brgy_index == 1 And street_index == 9 Then
	lat = "10.103477"
	lng = "122.870042"
	else If brgy_index == 1 And street_index == 10 Then
	lat = "10.100710"
	lng = "122.870889"
	End If
	
	If brgy_index == 2 And street_index == 0 Then 'brgy 3
	lat = "10.095478"
	lng = "122.871176"
	else If brgy_index == 2 And street_index == 1 Then
	lat = "10.098599"
	lng = "122.871761"
	else If brgy_index == 2 And street_index == 2 Then
	lat = "10.094573"
	lng = "122.870340"
	else If brgy_index == 2 And street_index == 3 Then
	lat = "10.098313"
	lng = "122.875223"
	else If brgy_index == 2 And street_index == 4 Then
	lat = "10.092235"
	lng = "122.874356"
	else If brgy_index == 2 And street_index == 5 Then
	lat = "10.103982"
	lng = "122.885996"
	else If brgy_index == 2 And street_index == 6 Then
	lat = "10.102170"
	lng = "122.882390"
	else If brgy_index == 2 And street_index == 7 Then
	lat = "10.103272"
	lng = "122.883948"
	else If brgy_index == 2 And street_index == 8 Then
	lat = "10.103849"
	lng = "122.884602"
	else If brgy_index == 2 And street_index == 9 Then
	lat = "10.101033"
	lng = "122.874480"
	End If
	
		If brgy_index == 3 And street_index == 0 Then 'brgy 4
	lat = "10.121855"
	lng = "122.872266"
	else If brgy_index == 3 And street_index == 1 Then
	lat = "10.116699"
	lng = "122.871783"
	else If brgy_index == 3 And street_index == 2 Then
	lat = "10.116024"
	lng = "122.872477"
	else If brgy_index == 3 And street_index == 3 Then
	lat = "10.114588"
	lng = "122.872515"
	else If brgy_index == 3 And street_index == 4 Then
	lat = "10.112140"
	lng = "122.872161"
	else If brgy_index == 3 And street_index == 5 Then
	lat = "10.111531"
	lng = "122.871542"
	else If brgy_index == 3 And street_index == 6 Then
	lat = "10.107168"
	lng = "122.871766"
	else If brgy_index == 3 And street_index == 7 Then
	lat = "10.106570"
	lng = "122.875197"
	else If brgy_index == 3 And street_index == 8 Then
	lat = "10.105759"
	lng = "122.871537"
	End If
	
		If brgy_index == 4 And street_index == 0 Then 'Aguisan
	lat = "10.165214"
	lng = "122.865433"
	else If brgy_index == 4 And street_index == 1 Then
	lat = "10.154170"
	lng = "122.867255"
	else If brgy_index == 4 And street_index == 2 Then
	lat = "10.161405"
	lng = "122.862692"
	else If brgy_index == 4 And street_index == 3 Then
	lat = "10.168471"
	lng = "122.860955"
	else If brgy_index == 4 And street_index == 4 Then
	lat = "10.172481"
	lng = "122.858629"
	else If brgy_index == 4 And street_index == 5 Then
	lat = "10.166561"
	lng = "122.859428"
	else If brgy_index == 4 And street_index == 6 Then
	lat = "10.163510"
	lng = "122.860074"
	else If brgy_index == 4 And street_index == 7 Then
	lat = "10.161033"
	lng = "122.859773"
	else If brgy_index == 4 And street_index == 8 Then
	lat = "10.159280"
	lng = "122.861621"
	else If brgy_index == 4 And street_index == 9 Then
	lat = "10.159062"
	lng = "122.860209"
	else If brgy_index == 4 And street_index == 10 Then
	lat = "10.181112"
	lng = "122.864670"
	else If brgy_index == 4 And street_index == 11 Then
	lat = "10.167295"
	lng = "122.857858"
	End If
	
	If brgy_index == 5 And street_index == 0 Then 'caradio-an
	lat = "10.092993"
	lng = "122.861694"
	else If brgy_index == 5 And street_index == 1 Then
	lat = "10.090587"
	lng = "122.868414"
	else If brgy_index == 5 And street_index == 2 Then
	lat = "10.091551"
	lng = "122.869249"
	else If brgy_index == 5 And street_index == 3 Then
	lat = "10.086452"
	lng = "122.865742"
	else If brgy_index == 5 And street_index == 4 Then
	lat = "10.083507"
	lng = "122.858928"
	else If brgy_index == 5 And street_index == 5 Then
	lat = "10.077131"
	lng = "122.864236"
	else If brgy_index == 5 And street_index == 6 Then
	lat = "10.081722"
	lng = "122.882661"
	else If brgy_index == 5 And street_index == 7 Then
	lat = "10.081822"
	lng = "122.868295"
	else If brgy_index == 5 And street_index == 8 Then
	lat = "10.079513"
	lng = "122.876610"
	else If brgy_index == 5 And street_index == 9 Then
	lat = "10.068560"
	lng = "122.887366"
	else If brgy_index == 5 And street_index == 10 Then
	lat = "10.066934"
	lng = "122.871963"
	else If brgy_index == 5 And street_index == 11 Then
	lat = "10.064251"
	lng = "122.883023"
	else If brgy_index == 5 And street_index == 12 Then
	lat = "10.058546"
	lng = "122.882968"
	else If brgy_index == 5 And street_index == 13 Then
	lat = "10.054104"
	lng = "122.885506"
	else If brgy_index == 5 And street_index == 14 Then
	lat = "10.049464"
	lng = "122.885667"
	else If brgy_index == 5 And street_index == 15 Then
	lat = "10.041580"
	lng = "122.900269"
	else If brgy_index == 5 And street_index == 16 Then
	lat = "10.041395"
	lng = "122.906248"
	End If
	
	If brgy_index == 6 And street_index == 0 Then 'Buenavista
	lat = "10.035728"
	lng = "122.847547"
	else If brgy_index == 6 And street_index == 1 Then
	lat = "10.000603"
	lng = "122.885243"
	else If brgy_index == 6 And street_index == 2 Then
		lat = "10.000521"
	lng = "122.895867"
	else If brgy_index == 6 And street_index == 3 Then
		lat = "9.943276"
	lng = "122.975801"
	End If
	
			If brgy_index == 7 And street_index == 0 Then 'Cabadiangan
	lat = "10.156301"
	lng = "122.941207"
	else If brgy_index == 7 And street_index == 1 Then
		lat = "10.142692"
	lng = "122.947560"
	else If brgy_index == 7 And street_index == 2 Then
		lat = "10.139494"
	lng = "122.942788"
	else If brgy_index == 7 And street_index == 3 Then
		lat = "10.110265"
	lng = "122.947908"
	else If brgy_index == 7 And street_index == 4 Then
		lat = "10.127828"
	lng = "122.950197"
	else If brgy_index == 7 And street_index == 5 Then
		lat = "10.125287"
	lng = "122.945735"
	else If brgy_index == 7 And street_index == 6 Then
		lat = "10.143975"
	lng = "122.930610"
	else If brgy_index == 7 And street_index == 7 Then
		lat = "10.137563"
	lng = "122.939870"
	else If brgy_index == 7 And street_index == 8 Then
		lat = "10.150449"
	lng = "122.933761"
	else If brgy_index == 7 And street_index == 9 Then
		lat = "10.150286"
	lng = "122.948956"
	else If brgy_index == 7 And street_index == 10 Then
		lat = "10.148481"
	lng = "122.943230"
	else If brgy_index == 7 And street_index == 11 Then
		lat = "10.106200"
	lng = "122.948051"
	else If brgy_index == 7 And street_index == 12 Then
		lat = "10.152073"
	lng = "122.926593"
	else If brgy_index == 7 And street_index == 13 Then
		lat = "10.120798"
	lng = "122.938371"
	else If brgy_index == 7 And street_index == 14 Then
		lat = "10.153217"
	lng = "122.951714"
	End If
	
     If brgy_index == 8 And street_index == 0 Then 'Cabanbanan
	lat = "10.157177"
	lng = "122.895986"
	else If brgy_index == 8 And street_index == 1 Then
		lat = "10.180004"
	lng = "122.897999"
	else If brgy_index == 8 And street_index == 2 Then
		lat = "10.192848"
	lng = "122.900234"
	else If brgy_index == 8 And street_index == 3 Then
		lat = "10.179993"
	lng = "122.904299"
	else If brgy_index == 8 And street_index == 4 Then
		lat = "10.183439"
	lng = "122.889622"
	End If
	
	If brgy_index == 9 And street_index == 0 Then 'Carabalan
	lat = "10.074128"
	lng = "122.981978"
	else If brgy_index == 9 And street_index == 1 Then
		lat = "10.109208"
	lng = "122.896717"
	else If brgy_index == 9 And street_index == 2 Then
		lat = "10.097119"
	lng = "122.947066"
	else If brgy_index == 9 And street_index == 3 Then
		lat = "10.099023"
	lng = "122.971723"
	else If brgy_index == 9 And street_index == 4 Then
		lat = "10.119761"
	lng = "122.901613"
	else If brgy_index == 9 And street_index == 5 Then
		lat = "10.099402"
	lng = "122.896454"
	else If brgy_index == 9 And street_index == 6 Then
	lat = "10.097102"
	lng = "122.922368"
	else If brgy_index == 9 And street_index == 7 Then
		lat = "10.095304"
	lng = "122.929242"
	else If brgy_index == 9 And street_index == 8 Then
		lat = "10.114128"
	lng = "122.893868"
	End If
	
	If brgy_index == 10 And street_index == 0 Then 'Libacao
	lat = "10.1799469"
	lng = "122.9068577"
	else If brgy_index == 10 And street_index == 1 Then
		lat = "10.180524"
	lng = "122.906798"
	else If brgy_index == 10 And street_index == 2 Then
		lat = "10.173336"
	lng = "122.9118842"
	else If brgy_index == 10 And street_index == 3 Then
		lat = "10.177359"
	lng = "122.913033"
	else If brgy_index == 10 And street_index == 4 Then
		lat = "10.179847"
	lng = "122.914160"
	else If brgy_index == 10 And street_index == 5 Then
		lat = "10.182718"
	lng = "122.915228"
	else If brgy_index == 10 And street_index == 6 Then
		lat = "10.186454"
	lng = "122.916278"
	else If brgy_index == 10 And street_index == 7 Then
		lat = "10.168057"
	lng = "122.924501"
	End If
	
	If brgy_index == 11 And street_index == 0 Then 'Mahalang
	lat = "10.050418"
	lng = "122.867097"
	else If brgy_index == 11 And street_index == 1 Then
		lat = "10.027855"
	lng = "122.906833"
	else If brgy_index == 11 And street_index == 2 Then
		lat = "10.027522"
	lng = "122.876637"
	else If brgy_index == 11 And street_index == 3 Then
		lat = "10.017254"
	lng = "122.900969"
	else If brgy_index == 11 And street_index == 4 Then
		lat = "10.028535"
	lng = "122.900364"
	else If brgy_index == 11 And street_index == 5 Then
		lat = "10.025485"
	lng = "122.890023"
	End If
		
	If brgy_index == 12 And street_index == 0 Then 'Mambagaton
	lat = "10.137572"
	lng = "122.939888"
	else If brgy_index == 12 And street_index == 1 Then
		lat = "10.132195"
	lng = "122.899837"
	else If brgy_index == 12 And street_index == 2 Then
		lat = "10.123430"
	lng = "122.892250"
	else If brgy_index == 12 And street_index == 3 Then
		lat = "10.130383"
	lng = "122.893010"
	else If brgy_index == 12 And street_index == 4 Then
		lat = "10.123127"
	lng = "122.887952"
	else If brgy_index == 12 And street_index == 5 Then
		lat = "10.131098"
	lng = "122.879801"
	else If brgy_index == 12 And street_index == 6 Then
		lat = "10.137485"
	lng = "122.911434"
	else If brgy_index == 12 And street_index == 7 Then
		lat = "10.106803"
	lng = "122.885727"
	else If brgy_index == 12 And street_index == 8 Then
		lat = "10.115220"
	lng = "122.890515"
	else If brgy_index == 12 And street_index == 9 Then
		lat = "10.108754"
	lng = "122.894130"
	else If brgy_index == 12 And street_index == 10 Then
		lat = "10.149506"
	lng = "122.897389"
	else If brgy_index == 12 And street_index == 11 Then
		lat = "10.122215"
	lng = "122.892160"
	else If brgy_index == 12 And street_index == 12 Then
		lat = "10.142698"
	lng = "122.898168"
	End If

	If brgy_index == 13 And street_index == 0 Then 'Nabalian
	lat = "10.161629"
	lng = "122.872772"
	else If brgy_index == 13 And street_index == 1 Then
		lat = "10.161863"
	lng = "122.876192"
	else If brgy_index == 13 And street_index == 2 Then
		lat = "10.157407"
	lng = "122.885663"
	else If brgy_index == 13 And street_index == 3 Then
		lat = "10.167497"
	lng = "122.879777"
	else If brgy_index == 13 And street_index == 4 Then
		lat = "10.176260"
	lng = "122.880815"
	else If brgy_index == 13 And street_index == 5 Then
		lat = "10.170524"
	lng = "122.883603"
	End If

	If brgy_index == 14 And street_index == 0 Then 'San Antonio
	lat = "10.071514"
	lng = "122.916010"
	else If brgy_index == 14 And street_index == 1 Then
		lat = "10.069622"
	lng = "122.909890"
	else If brgy_index == 14 And street_index == 2 Then
		lat = "10.076890"
	lng = "122.894231"
	else If brgy_index == 14 And street_index == 3 Then
		lat = "10.086207"
	lng = "122.914044"
	else If brgy_index == 14 And street_index == 4 Then
		lat = "10.067393"
	lng = "122.900935"
	else If brgy_index == 14 And street_index == 5 Then
		lat = "10.071900"
	lng = "122.906250"
	else If brgy_index == 14 And street_index == 6 Then
		lat = "10.061702"
	lng = "122.896226"
	else If brgy_index == 14 And street_index == 7 Then
		lat = "10.054802"
	lng = "122.938688"
	else If brgy_index == 14 And street_index == 8 Then
		lat = "10.071827"
	lng = "122.921092"
	else If brgy_index == 14 And street_index == 9 Then
		lat = "10.050849"
	lng = "122.907632"
	End If

	If brgy_index == 15 And street_index == 0 Then 'Saraet
	lat = "10.155844"
	lng = "122.861129"
	else If brgy_index == 15 And street_index == 1 Then
		lat = "10.152073"
	lng = "122.861669"
	else If brgy_index == 15 And street_index == 2 Then
		lat = "10.147663"
	lng = "122.862471"
	else If brgy_index == 15 And street_index == 3 Then
		lat = "10.144440"
	lng = "122.862524"
	End If
	
	If brgy_index == 16 And street_index == 0 Then 'Suay
	lat = "10.053680"
	lng = "122.843876"
	else If brgy_index == 16 And street_index == 1 Then
		lat = "10.055961"
	lng = "122.841980"
	else If brgy_index == 16 And street_index == 2 Then
		lat = "10.053363"
	lng = "122.843295"
	else If brgy_index == 16 And street_index == 3 Then
		lat = "10.053032"
	lng = "122.842594"
	else If brgy_index == 16 And street_index == 4 Then
		lat = "10.052328"
	lng = "122.842835"
	else If brgy_index == 16 And street_index == 5 Then
		lat = "10.052573"
	lng = "122.844229"
	else If brgy_index == 16 And street_index == 6 Then
		lat = "10.046957"
	lng = "122.839610"
	else If brgy_index == 16 And street_index == 7 Then
		lat = "10.035813"
	lng = "122.835364"
	End If
	
	If brgy_index == 17 And street_index == 0 Then 'Talaban
	lat = "10.148233"
	lng = "122.869741"
	else If brgy_index == 17 And street_index == 1 Then
		lat = "10.139867"
	lng = "122.869882"
	else If brgy_index == 17 And street_index == 2 Then
		lat = "10.126453"
	lng = "122.868927"
	else If brgy_index == 17 And street_index == 3 Then
		lat = "10.127470"
	lng = "122.862942"
	else If brgy_index == 17 And street_index == 4 Then
		lat = "10.117998"
	lng = "122.866817"
	else If brgy_index == 17 And street_index == 5 Then
		lat = "10.108173"
	lng = "122.864592"
	else If brgy_index == 17 And street_index == 6 Then
		lat = "10.126115"
	lng = "122.871073"
	else If brgy_index == 17 And street_index == 7 Then
		lat = "10.129412"
	lng = "122.869408"
	else If brgy_index == 17 And street_index == 8 Then
		lat = "10.134647"
	lng = "122.871841"
	else If brgy_index == 17 And street_index == 9 Then
		lat = "10.124801"
	lng = "122.868277"
	else If brgy_index == 17 And street_index == 10 Then
		lat = "10.124422"
	lng = "122.866917"
	End If

	If brgy_index == 18 And street_index == 0 Then 'Tooy
	lat = "10.065086"
	lng = "122.843793"
	else If brgy_index == 18 And street_index == 1 Then
		lat = "10.071356"
	lng = "122.853102"
	else If brgy_index == 18 And street_index == 2 Then
		lat = "10.060206"
	lng = "122.850172"
	else If brgy_index == 18 And street_index == 3 Then
		lat = "10.057640"
	lng = "122.859242"
	End If
	
	'Log("lat: "&lat&CRLF&"lng: "&lng)
	
	
End Sub

Sub location_spin_brgy_ItemClick (Position As Int, Value As Object)
	list_location_s.Clear
	location_spin_street.Clear
    ''list_location_s.Initialize
	'list_location_p.Initialize
	If Position == 0 Then
	list_location_s.Add("Rizal st.")
	list_location_s.Add("Valega st.")
	list_location_s.Add("Sarmiento st.")
	list_location_s.Add("Bantolinao st.")
	list_location_s.Add("Versoza st.")
	list_location_s.Add("Jimenez st.")
	list_location_s.Add("Olmedo st.")
	list_location_s.Add("Burgos st.")
	else if Position == 1 Then

	list_location_s.Add("Rizal st.")
	list_location_s.Add("Monton st.")
	list_location_s.Add("Carabalan road")
	list_location_s.Add("Purok star apple")
	list_location_s.Add("Gatuslao st.")
	list_location_s.Add("Bungyod")
	list_location_s.Add("Tabino st.")
	list_location_s.Add("River side")	
	list_location_s.Add("Arroyan st")	
	else if Position == 2 Then
	list_location_s.Add("Segovia st.")
	list_location_s.Add("Vasquez st.")
	list_location_s.Add("Olmedo st.")
	list_location_s.Add("Old relis st.")
	list_location_s.Add("Wayang")
	list_location_s.Add("Valencia")
	list_location_s.Add("Bungyod")
	list_location_s.Add("Bingig")	
	list_location_s.Add("Bajay")	
	list_location_s.Add("Carabalan road")		
	else if Position == 3 Then
	list_location_s.Add("Crusher")
	list_location_s.Add("Bangga mayok")
	list_location_s.Add("Villa julita")
	list_location_s.Add("Greenland subdivision")
	list_location_s.Add("Bangga 3c")
	list_location_s.Add("Cambugnon")
	list_location_s.Add("Menez")
	list_location_s.Add("Relis")	
	list_location_s.Add("Bangga patyo")	
	else if Position == 4 Then
    list_location_s.Add("Purok 1")	
	list_location_s.Add("Purok 2")	
	list_location_s.Add("Purok 3")	
	list_location_s.Add("Purok 4")	
	list_location_s.Add("Purok 5")	
	list_location_s.Add("Purok 6")	
	list_location_s.Add("Purok 7")	
	list_location_s.Add("Purok 8")	
	list_location_s.Add("Purok 9")	
	list_location_s.Add("Purok 10")	
	list_location_s.Add("Purok 11")	
	list_location_s.Add("Purok 12")	
	else if Position == 5 Then
		list_location_s.Add("Malusay")	
		list_location_s.Add("Nasug ong")	
		list_location_s.Add("Lugway")	
		list_location_s.Add("Ubay")	
		list_location_s.Add("Fisheries")	
		list_location_s.Add("Proper")	
		list_location_s.Add("Calasa")	
		list_location_s.Add("Hda. Serafin")	
		list_location_s.Add("Patay na suba")	
		list_location_s.Add("Lumanog")	
		list_location_s.Add("San agustin")	
		list_location_s.Add("San jose")	
		list_location_s.Add("Maglantay")	
		list_location_s.Add("San juan")	
		list_location_s.Add("Magsaha")	
		list_location_s.Add("Tagmanok")	
		list_location_s.Add("Butong")	
	else if Position == 6 Then
		list_location_s.Add("Proper")	
		list_location_s.Add("Saisi")	
		list_location_s.Add("Paloypoy")	
		list_location_s.Add("Tigue")	
	else if Position == 7 Then
		list_location_s.Add("Proper")	
		list_location_s.Add("Tonggo")	
		list_location_s.Add("Iling iling")	
		list_location_s.Add("Campayas")	
		list_location_s.Add("Palayan")	
		list_location_s.Add("Guia")	
		list_location_s.Add("An-an")	
		list_location_s.Add("An-an 2")	
		list_location_s.Add("Sta. rita")	
		list_location_s.Add("Benedicto")	
		list_location_s.Add("Sta. cruz/ bunggol")	
		list_location_s.Add("Olalia")	
		list_location_s.Add("Banuyo")	
		list_location_s.Add("Carmen")	
		list_location_s.Add("Riverside")	
	else if Position == 8 Then
		list_location_s.Add("Balangga-an")	
		list_location_s.Add("Proper")	
		list_location_s.Add("Proper")	
		list_location_s.Add("Ruiz")	
		list_location_s.Add("Bakyas")	
    else if Position == 9 Then
		list_location_s.Add("Cunalom")	
		list_location_s.Add("Tara")	
		list_location_s.Add("Proper")	
		list_location_s.Add("Casipungan")	
		list_location_s.Add("Carmen")	
		list_location_s.Add("Lanipga")	
		list_location_s.Add("Bulod")	
		list_location_s.Add("Bonton")	
		list_location_s.Add("Poblador")	
	else if Position == 10 Then
		list_location_s.Add("Ruiz")	
		list_location_s.Add("Balisong")	
		list_location_s.Add("Purok 1")	
		list_location_s.Add("Purok 2")	
		list_location_s.Add("Purok 3")	
		list_location_s.Add("Purok 4")	
		list_location_s.Add("Dubdub")	
		list_location_s.Add("Hda. San jose valing")	
	else if Position == 11 Then
		list_location_s.Add("Acapulco")	
		list_location_s.Add("Liki")	
		list_location_s.Add("500")	
		list_location_s.Add("Aglatong")	
		list_location_s.Add("Proper")	
		list_location_s.Add("Baptist")	
	else if Position == 12 Then
		list_location_s.Add("Lizares")	
		list_location_s.Add("Pakol")	
		list_location_s.Add("Proper")	
		list_location_s.Add("Lanete")	
		list_location_s.Add("Kasoy")	
		list_location_s.Add("Bato")	
		list_location_s.Add("Frande")	
		list_location_s.Add("Bajay")	
		list_location_s.Add("Poblador")	
		list_location_s.Add("Culban")	
		list_location_s.Add("Calansi")	
		list_location_s.Add("Carmen")	
		list_location_s.Add("Dama")	
	else if Position == 13 Then
		list_location_s.Add("Purok 1")	
		list_location_s.Add("Purok 2")	
		list_location_s.Add("Purok 3")	
		list_location_s.Add("Purok 4")	
		list_location_s.Add("Purok 5")	
		list_location_s.Add("Purok 6")	
	else if Position == 14 Then
		list_location_s.Add("Proper")	
		list_location_s.Add("Calubihan")	
		list_location_s.Add("Mapulang duta")	
		list_location_s.Add("Abud")	
		list_location_s.Add("Molo")	
		list_location_s.Add("Balabag")	
		list_location_s.Add("Pandan")	
		list_location_s.Add("Nahulop")	
		list_location_s.Add("Cubay")	
		list_location_s.Add("Aglaoa")	
	else if Position == 15 Then
		list_location_s.Add("Purok 1")	
		list_location_s.Add("Purok 2")	
		list_location_s.Add("Purok 3")	
		list_location_s.Add("Purok 4")	
	else if Position == 16 Then
		list_location_s.Add("ORS")	
		list_location_s.Add("Aloe vera")	
		list_location_s.Add("SCAD")	
		list_location_s.Add("Proper")	
		list_location_s.Add("Sampaguita")	
		list_location_s.Add("Bonguinvilla")	
		list_location_s.Add("Cagay")	
		list_location_s.Add("Naga")	
	else if Position == 17 Then
		list_location_s.Add("Hda. Naval")	
		list_location_s.Add("Antipolo")	
		list_location_s.Add("Rizal st.")	
		list_location_s.Add("Punta talaban")	
		list_location_s.Add("Batang guwaan")	
		list_location_s.Add("Batang sulod")	
		list_location_s.Add("Mabini st.")	
		list_location_s.Add("Cubay")	
		list_location_s.Add("Hacienda silos")	
		list_location_s.Add("Lopez jeana 1")	
		list_location_s.Add("Lopez jeana 2")	
	else if Position == 18 Then
		list_location_s.Add("Ilawod")	
		list_location_s.Add("Buhian")	
		list_location_s.Add("Proper")	
		list_location_s.Add("Mambato")	
	'list_location_s.Add("")	
	End If
	brgy_index = Position
	location_spin_street.AddAll(list_location_s)
	'location_spin_purok.AddAll(list_location_p)
	
End Sub
Sub location_spin_street_ItemClick (Position As Int, Value As Object)
	street_index = Position
	street_lat_lng
End Sub
Sub spin_donate_confirm_ItemClick (Position As Int, Value As Object)
	spin_donate_pos = Position
	Log("pos: "&spin_donate_pos)
	If Position == 0 Then
			is_donate_date.Text = " "
			isDonateDate = "NONE"
	Else
		isDonate_edit_
	End If
End Sub
Sub isDonate_edit_
		list_day.Initialize
		list_month.Initialize
		list_year.Initialize
		spin_day.Initialize("donate_spin_day")	
		spin_month.Initialize("donate_spin_month")	
		spin_year.Initialize("donate_spin_year")	
	  For i = 1 To 31
	  	list_day.Add(i)
	  Next
	   Dim iNowYear As Int
	 iNowYear = DateTime.GetYear(DateTime.Now)
	  For ii = 1950 To DateTime.GetYear(DateTime.Now)
	  	list_year.Add(iNowYear)
		iNowYear = iNowYear-1
	  Next
		For iii = 1 To 12
			list_month.Add(iii)
		Next
		spin_day.AddAll(list_day)
		spin_month.AddAll(list_month)
		spin_year.AddAll(list_year)
	Dim pnl As Panel
	Dim edit_ok_btn,edit_can_btn As Button
	Dim title_lbl As Label
	edit_ok_btn.Initialize("isdonated_ok_btn")
	edit_can_btn.Initialize("isdonated_can_btn")
	title_lbl.Initialize("")
	edit_ok_btn.Text = "OK"
	edit_can_btn.Text = "CANCEL"
	edit_can_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	edit_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
			Dim V_btn,C_btn As GradientDrawable
			Dim colorG(2) As Int
			colorG(0) = Colors.White
			colorG(1) = Colors.Red
			C_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.CornerRadius = 50dip
			C_btn.CornerRadius = 50dip
		edit_ok_btn.Background = V_btn
		edit_can_btn.Background = C_btn
	title_lbl.Text = "SELECT DONATED DATE"
	title_lbl.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	title_lbl.Gravity = Gravity.CENTER
	pnl.Initialize("pnl")
	pnl_bday_body.Initialize("pnl_bday_body")
	pnl_bday_body.Color = Colors.Transparent
	pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)
	pnl.AddView(spin_day,2%x,title_lbl.Top + title_lbl.Height + 1%y,20%x,8%y)
	pnl.AddView(spin_month,spin_day.Left+spin_day.Width+1%X,spin_day.Top,20%x,8%y)
	pnl.AddView(spin_year,spin_month.Left+spin_month.Width+1%x,spin_month.Top,20%x,8%y)
	pnl.AddView(edit_ok_btn,15%x,spin_year.Top+spin_year.Height+3%y,20%x,8%y)
	pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok_btn.Width+2%x,spin_year.Top+spin_year.Height+3%y,20%x,8%y)

	pnl_bday_body.AddView(pnl,13%x,((Activity.Height/2)/2),74%x,33%y)
	pnl_bday_body.BringToFront
	'pnl_body.Enabled = False
	'pnl_bday_body.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(pnl_bday_body,0,0,100%x,100%y)	
End Sub
Sub pnl_bday_body_click
	''can't delete
End Sub
Sub donate_spin_day_ItemClick (Position As Int, Value As Object)
	donate_d_pos = Position
	'donate_m_pos,donate_d_pos,donate_y_pos
End Sub
Sub donate_spin_month_ItemClick (Position As Int, Value As Object)
	donate_m_pos = Position
	'donate_m_pos,donate_d_pos,donate_y_pos
End Sub
Sub donate_spin_year_ItemClick (Position As Int, Value As Object)
	donate_y_pos = Position
	'donate_m_pos,donate_d_pos,donate_y_pos
End Sub
Sub isdonated_ok_btn_click
	Dim day,month,year As String
	day = spin_day.GetItem(donate_d_pos)
	month = spin_month.GetItem(donate_m_pos)
	year = spin_year.GetItem(donate_y_pos)
	isDonateDate = month&"/"&day&"/"&year
	is_donate_date.Text = "("&isDonateDate&")"		
	Msgbox(""&month&"/"&day&"/"&year,"Date Selected")
	'Log(isDonateDate)
	pnl_bday_body.RemoveView
End Sub
Sub isdonated_can_btn_click
	pnl_bday_body.RemoveView
End Sub

Sub spin_gender_ItemClick (Position As Int, Value As Object)
	gender_index = Position
End Sub