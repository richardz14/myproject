﻿Type=Activity
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
	Dim list_all_info As List
	Dim list_bloodgroup As List
	Dim list_donated As List
	Dim list_is_gender As List
	Dim list_day,list_month,list_year As List
	Dim users_string_login As String
	
	Dim blood_selected As String : blood_selected = "A" 
	Dim bday_day_selected As String : bday_day_selected = "1"
	Dim bday_month_selected As String : bday_month_selected = "1"
	Dim bday_year_selected As String : bday_year_selected = DateTime.GetYear(DateTime.Now)
	Dim location_brgy_selected As String : location_brgy_selected = "Brgy 1"
	Dim location_street_selected As String : location_street_selected = "Rizal St."
	Dim gender_string_data As String : gender_string_data = "Male"
	Dim is_donated As String : is_donated = "Yes"
	Dim donated_index As Int : donated_index = 0
	Dim is_gender_index As Int : is_gender_index = 0
	Dim lat As String : lat = "10.098014"
	Dim lng As String : lng = "122.869168"
	Dim brgy_index As Int : brgy_index = 0
	Dim street_index As Int : street_index = 0
	Dim list_location_b,list_location_s,list_location_p As List
	Dim optionSelected As String
	
	Dim isDonateDate As String
	Dim donate_m_pos,donate_d_pos,donate_y_pos As Int
	Private image_container As String
	Private panel_click_ As Int : panel_click_ = 0
	Private edit_panel_click_ As Int : edit_panel_click_ = 0
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim location_spin_street As Spinner
	Dim location_spin_brgy As Spinner
	Dim spin_bloodgroup As Spinner
	Dim spin_day,spin_month,spin_year As Spinner
	Dim spin_donated As Spinner
	Dim spin_gender As Spinner
	Dim dlgFileExpl As ClsExplorer	
		
		
	Private search_blood As Button
	Private about As Button
	Private help As Button
	Private exit_btn As Button
	Private profile As Button
	Private src_blood_pnl As Panel
	Private users_panel As Panel
	Private profile_pnl As Panel
	Private about_pnl As Panel
	Private help_pnl As Panel
	Private exit_pnl As Panel
	
	Private users_out_lbl As Label
	Private users_lbl As Label
	Private ban_logo As ImageView
	Private ban_picture As ImageView
	Private users_heading As Panel
	Private srch_blood_img As ImageView
	Private profile_img As ImageView
	Private about_img As ImageView
	Private help_img As ImageView
	Private exit_img As ImageView
	
	Dim profile_panel As Panel
	Dim scroll_profile_pnl As ScrollView
		Dim profile_all_body As Panel
	Private all_inputs As Panel
	Dim pnl_body As Panel
	Dim pnl_blood_body As Panel
	Dim pnl_bday_body As Panel
	Dim pnl_donated_body As Panel
	Dim pnl_gender_body As Panel
	''update infos
	Private all_info_query As HttpJob
	Private update_job As HttpJob
	'' for label
	Private lab_fullname As Label
	Private lab_bloodgroup As Label
	Private lab_email As Label
	Private lab_phonenumber As Label
	Private lab_phonenumber2 As Label
	Private lab_location As Label
	Private lab_question As Label
	Private lab_donate_confirm As Label
	Private lab_bday As Label
	'' for edit text
	
	Private text_fn As EditText
	Private text_blood As EditText
	Private text_email As EditText
	Private text_phonenumber As EditText
	Private text_phonenumber2 As EditText
	Private text_bday As EditText
	Private text_location As EditText
	Private text_answer As EditText
	Private text_donated As EditText
	Private cancel_btn As Button
	Private update_btn As Button
	Private usr_img As ImageView
	Private all_inputs_top As Panel
	
	'' scrolling
		Private scroll_myprof As ScrollView2D
	Private all_inputs_down As Panel
	Private tittle As Label
	Private donated_edit As Label
	Private bday_edit As Label
	Private locate_edit As Label
	Private blood_edit As Label
	Private lab_gender As Label
	Private text_gender As EditText
	Private edit_gender As Label
	
	Private about_us_pnl As Panel
	Private help_us_pnl As Panel
	
	Dim about_sc2d As ScrollView2D
	Dim help_sc2d As ScrollView2D
End Sub
Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("menu_form")
	Activity.LoadLayout ("menu_frame")
	Activity.Title = "MENU"
	load_activity_layout
End Sub
Sub load_activity_layout
	Dim text_temp As calculations
	
	text_temp.Initialize
	Log("name: "&login_form.name_query)
	'Log("id: "&login_form.id_query)
	users_out_lbl.text = login_form.name_query
	ban_picture.SetBackgroundImage(LoadBitmap(File.DirAssets,"banner01.jpg"))
	ban_logo.SetBackgroundImage(LoadBitmap(File.DirAssets,"logo1.jpg"))
	Activity.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	users_panel.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	src_blood_pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	profile_pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	about_pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	help_pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	exit_pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	
			srch_blood_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"menu_search.png"))
	 		profile_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"emyprofile.png"))
	 		about_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"eaboutus.png"))
			 help_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"ehelp.png"))
			 exit_img.SetBackgroundImage(LoadBitmap(File.DirAssets,"eexit.png"))
	
	users_heading.Color = Colors.Transparent
	''width
	ban_picture.Width = 80%x
	ban_logo.Width = 20%x
	users_panel.Width = Activity.Width
	src_blood_pnl.Width = Activity.Width
	profile_pnl.Width = Activity.Width
	about_pnl.Width = Activity.Width
	help_pnl.Width = Activity.Width
	exit_pnl.Width = Activity.Width
	users_heading.Width = Activity.Width
	''heigth
	users_heading.Height = 9%y
	users_panel.Height = 18%y
	ban_picture.Height = users_panel.Height
	ban_logo.Height = users_panel.Height
	src_blood_pnl.Height = 12%y
	profile_pnl.Height = 12%y
	about_pnl.Height = 12%y
	help_pnl.Height = 12%y
	exit_pnl.Height = 12%y
	''left
	ban_logo.Left = 0
	ban_picture.Left = ban_logo.Left + ban_logo.Width
	users_panel.Left = 0
	src_blood_pnl.Left = 0
	profile_pnl.Left = 0
	about_pnl.Left = 0
	help_pnl.Left = 0
	exit_pnl.Left = 0
	users_heading.Left = 0
	''top
	users_panel.Top = 0
	ban_picture.Top = 0
	ban_logo.Top = 0
	users_heading.Top = users_panel.Top + users_panel.Height
	src_blood_pnl.Top = users_heading.Top + users_heading.Height
	profile_pnl.Top = src_blood_pnl.Top + src_blood_pnl.Height
	about_pnl.Top = profile_pnl.Top + profile_pnl.Height
	help_pnl.Top = about_pnl.Top + about_pnl.Height
	exit_pnl.Top = help_pnl.Top + help_pnl.Height
	'src_blood_pnl.Top = users_panel.Top + users_panel.Height + 1%Y
	'profile_pnl.Top = src_blood_pnl.Top + src_blood_pnl.Height + 1%Y
	'about_pnl.Top = profile_pnl.Top + profile_pnl.Height+ 1%Y
	'help_pnl.Top = about_pnl.Top + about_pnl.Height+ 1%Y
	'exit_pnl.Top = help_pnl.Top + help_pnl.Height+ 1%Y
	
	'' buttons height, width, left, top
	'width
		search_blood.Width = Activity.Width - 60%x
		about.Width = Activity.Width - 60%x
		help.Width = Activity.Width - 60%x
		profile.Width = Activity.Width - 60%x
		exit_btn.Width = Activity.Width - 60%x
			srch_blood_img.Width = Activity.Width - 85%x
	 		profile_img.Width = Activity.Width - 85%x
	 		about_img.Width = Activity.Width - 85%x
			 help_img.Width = Activity.Width - 85%x
			 exit_img.Width = Activity.Width - 85%x
	'height
		search_blood.Height = 9%y
		about.Height = 9%y
		help.Height = 9%y
		profile.Height = 9%y
		exit_btn.Height = 9%y
			 srch_blood_img.Height = 9%y
	 		profile_img.Height = 9%y
	 		about_img.Height = 9%y
			 help_img.Height = 9%y
			 exit_img.Height = 9%y
	'left
	users_lbl.Left = 2%x
	users_out_lbl.Left = users_lbl.Left + users_lbl.Width
		search_blood.Left = ((src_blood_pnl.Width/2)/2)/2
	    profile.Left = ((profile_pnl.Width/2)/2)	
		about.Left = (help_pnl.Width/2)
		help.Left = ((about_pnl.Width/2)/2)
		exit_btn.Left = ((exit_pnl.Width/2)/2)/2
		
		srch_blood_img.Left = search_blood.Left + search_blood.Width
	 		profile_img.Left = profile.Left + profile.Width
	 		about_img.Left = about.Left - about_img.Width
			 help_img.Left = help.Left + help.Width
			 exit_img.Left = exit_btn.Left + exit_btn.Width
	'top
	users_out_lbl.Top = ((users_heading.Height/2)/2)/2
	users_lbl.Top = users_out_lbl.Top
	
		search_blood.Top = ((src_blood_pnl.Height/2)/2)/2
		about.Top = ((about_pnl.Height/2)/2)/2
		help.Top = ((help_pnl.Height/2)/2)/2
		profile.Top = ((profile_pnl.Height/2)/2)/2
		exit_btn.Top = ((exit_pnl.Height/2)/2)/2
		
		srch_blood_img.Top = ((src_blood_pnl.Height/2)/2)/2
	 		profile_img.Top = ((about_pnl.Height/2)/2)/2
	 		about_img.Top = ((help_pnl.Height/2)/2)/2
			 help_img.Top = ((profile_pnl.Height/2)/2)/2
			 exit_img.Top = ((exit_pnl.Height/2)/2)/2
	''--------- button images ------------------''''
	search_blood.SetBackgroundImage(LoadBitmap(File.DirAssets,"SEARCH.png"))
		about.SetBackgroundImage(LoadBitmap(File.DirAssets,"ABOUT_US.png"))
		help.SetBackgroundImage(LoadBitmap(File.DirAssets,"HELP.png"))
		profile.SetBackgroundImage(LoadBitmap(File.DirAssets,"my_profile.png"))
		exit_btn.SetBackgroundImage(LoadBitmap(File.DirAssets,"EXIT.png"))
	
End Sub
Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub
Sub search_blood_Click
	StartActivity("search_frame")
End Sub
Sub profile_Click
	ProgressDialogShow2("Please Wait..",False)
	panel_click_ = 1
	edit_panel_click_ = 0
	optionSelected = "pofileView"
	Dim TextWriters As TextWriter
	TextWriters.Initialize(File.OpenOutput(File.DirInternalCache, "users_all_info.txt", False))
	
		Dim url_back As calculations 
		Dim update_top_pnl As Panel
		update_top_pnl.Initialize("update_top_pnl")
		url_back.Initialize
	Dim all_users_info As String
	all_inputs_down.Initialize("all_inputs_down")
	profile_all_body.Initialize("profile_all_body")
	'Log(url_back.users_id)
	all_info_query.Initialize("all_info_query",Me)
	all_users_info = url_back.php_email_url("search_all_users_data.php")
	all_info_query.Download2(all_users_info,Array As String("all_info","SELECT * FROM `person_info` where `id`='"&login_form.id_query&"';"))
		'scroll_profile_pnl.Initialize(100%x,100%y,"scroll_pnl")
		'scroll_profile_pnl.Initialize(90%y)
		'profile_panel.Initialize("profile_panel")
		'	scroll_profile_pnl.Panel.LoadLayout("update_all_inputs")
		'scroll_profile_pnl.Color = Colors.Transparent
		'''
		scroll_myprof.Initialize(90%x,73%y,"scroll_myprof")
		scroll_myprof.Panel.LoadLayout("update_all_inputs")
		scroll_myprof.Color = Colors.Transparent
		scroll_myprof.ScrollbarsVisibility(False,False)
		update_top_pnl.LoadLayout("update_all_top")
		
		scroll_myprof.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
		all_inputs.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
		all_inputs_top.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
		all_inputs_down.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	 lab_fullname.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-4-user.png"))
	 lab_bloodgroup.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-93-tint.png"))
	 lab_email.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-social-40-e-mail.png"))
	 lab_phonenumber.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-354-nameplate-alt1.png"))
	 lab_phonenumber2.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-354-nameplate-alt2.png"))
	 lab_location.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-21-home.png"))
	 lab_question.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-353-nameplate.png"))
	 lab_donate_confirm.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-152-new-window.png"))
	 lab_bday.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-46-calendar.png"))
	 lab_gender.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-25-parents.png"))
		
		all_inputs_top.Width = 90%x
		all_inputs_top.Height = 32%y
			tittle.SetLayout(1%x,1%y,88%x,9%y)
			usr_img.SetLayout(((88%x/2)/2)+3%x,tittle.Top+tittle.Height,40%x,19%y)
		''
		all_inputs.Width = 90%x
		all_inputs.Height = 73%y	
		  lab_fullname.SetLayout(2%x,2%y,7%x,6%y)	
		  lab_bloodgroup.SetLayout(2%x,lab_fullname.Top+lab_fullname.Height+1%y,7%x,6%y)	
		  lab_email.SetLayout(2%x,lab_bloodgroup.Top+lab_bloodgroup.Height+1%y,7%x,6%y)	
		  lab_phonenumber.SetLayout(2%x,lab_email.Top+lab_email.Height+1%y,7%x,6%y)	
		  lab_phonenumber2.SetLayout(2%x,lab_phonenumber.Top+lab_phonenumber.Height+1%y,7%x,6%y)	
		  lab_bday.SetLayout(2%x,lab_phonenumber2.Top+lab_phonenumber2.Height+1%y,7%x,6%y)	
		  lab_location.SetLayout(2%x,lab_bday.Top+lab_bday.Height+1%y,7%x,6%y)	
		  lab_question.SetLayout(2%x,lab_location.Top+lab_location.Height+1%y,7%x,6%y)	
		  lab_donate_confirm.SetLayout(2%x,lab_question.Top+lab_question.Height+1%y,7%x,6%y)	
		  lab_gender.SetLayout(2%x,lab_donate_confirm.Top+lab_donate_confirm.Height+1%y,7%x,6%y)	
		  
			text_fn.SetLayout(lab_fullname.Left+lab_fullname.Width,2%y,79%x,6%y)
			text_blood.SetLayout(lab_fullname.Left+lab_fullname.Width,lab_bloodgroup.Top,69%x,6%y)
			text_email.SetLayout(lab_fullname.Left+lab_fullname.Width,lab_email.Top,79%x,6%y)
			text_phonenumber.SetLayout(lab_fullname.Left+lab_fullname.Width,lab_phonenumber.Top,79%x,6%y)
			text_phonenumber2.SetLayout(lab_fullname.Left+lab_fullname.Width,lab_phonenumber2.Top,79%x,6%y)
			text_bday.SetLayout(lab_fullname.Left+lab_fullname.Width,lab_bday.Top,69%x,6%y)
			text_location.SetLayout(lab_fullname.Left+lab_fullname.Width,lab_location.Top,69%x,6%y)
			text_answer.SetLayout(lab_fullname.Left+lab_fullname.Width,lab_question.Top,79%x,6%y)
			text_donated.SetLayout(lab_fullname.Left+lab_fullname.Width,lab_donate_confirm.Top,69%x,6%y)
			text_gender.SetLayout(lab_fullname.Left+lab_fullname.Width,lab_gender.Top,69%x,6%y)
				
				blood_edit.SetLayout(text_blood.Left+text_blood.Width,text_blood.Top,10%x,6%y)
				bday_edit.SetLayout(text_bday.Left+text_bday.Width,text_bday.Top,10%x,6%y)
				locate_edit.SetLayout(text_location.Left+text_location.Width,text_location.Top,10%x,6%y)
				donated_edit.SetLayout(text_donated.Left+text_donated.Width,text_donated.Top,10%x,6%y)
				edit_gender.SetLayout(text_gender.Left+text_gender.Width,text_gender.Top,10%x,6%y)
				
		update_btn.Initialize("update_btn")
		cancel_btn.Initialize("cancel_btn")	
			update_btn.Text = "UPDATE"
			cancel_btn.Text = "CANCEL"	
				all_inputs_down.AddView(update_btn,6%x,1%y,35%x,9%y)
				all_inputs_down.AddView(cancel_btn,update_btn.Left+update_btn.Width+5%x,1%y,35%x,9%y)
					
		profile_all_body.Color = Colors.ARGB(128,128,128,.50)
		'profile_all_body.AddView(scroll_profile_pnl,5%x,3%y,90%x,90%y) 'update_top_pnl
		profile_all_body.AddView(update_top_pnl,5%x,2%y,90%x,90%y)
		profile_all_body.AddView(scroll_myprof,5%x,all_inputs_top.Top + all_inputs_top.Height+2%y,90%x,47%y)
		profile_all_body.AddView(all_inputs_down,5%x,scroll_myprof.Top+scroll_myprof.Height - 1%y,90%x,13%y)
		Activity.AddView(profile_all_body,0,0,100%x,100%y)
			
			Dim V_btn,C_btn As GradientDrawable
			Dim colorG(2) As Int
			colorG(0) = Colors.White
			colorG(1) = Colors.Red
			C_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.CornerRadius = 50dip
			C_btn.CornerRadius = 50dip
		update_btn.Background = V_btn
		cancel_btn.Background = C_btn
End Sub
Sub update_all_inputs_click
	''don't delete this sub
End Sub
Sub profile_all_body_click
	''don't delete this sub
End Sub
Sub all_input_on_list
	list_all_info.Initialize
	Dim TextReader_phone2 As TextReader
    TextReader_phone2.Initialize(File.OpenInput(File.DirInternalCache, "users_all_info.txt"))
    Dim line_phone2 As String
    line_phone2 = TextReader_phone2.ReadLine    
    Do While line_phone2 <> Null
		list_all_info.Add(line_phone2)
        line_phone2 = TextReader_phone2.ReadLine
    Loop
    TextReader_phone2.Close
	
	text_fn.Text = list_all_info.Get(0)
	text_blood.Text = list_all_info.Get(1)
	blood_selected = list_all_info.Get(1)
	text_email.Text = list_all_info.Get(2)
	text_phonenumber.Text = list_all_info.Get(3)
	text_phonenumber2.Text = list_all_info.Get(4)
	text_bday.Text = list_all_info.Get(5)&"/"&list_all_info.Get(6)&"/"&list_all_info.Get(7)
	bday_month_selected = list_all_info.Get(5)
	bday_day_selected = list_all_info.Get(6)
	bday_year_selected = list_all_info.Get(7)
	text_location.Text = list_all_info.Get(8)&", "&list_all_info.Get(9)
		location_brgy_selected = list_all_info.Get(8)
  		 location_street_selected = list_all_info.Get(9)
	text_answer.Text = list_all_info.Get(10)
	text_donated.Text = list_all_info.Get(11)
	is_donated = list_all_info.Get(11)
	text_gender.Text = list_all_info.Get(13)
	gender_string_data = list_all_info.Get(13)
	
	Dim inp As InputStream
	Dim bmp As Bitmap
	Dim su As StringUtils
	Dim bytes() As Byte
	bytes = su.DecodeBase64(list_all_info.Get(12))
	image_container = list_all_info.Get(12)
	inp.InitializeFromBytesArray(bytes,0,bytes.Length)
	bmp.Initialize2(inp)
	usr_img.SetBackgroundImage(bmp)
	'Log(list_all_info.Get(12))
	
	ProgressDialogHide
End Sub
Sub Activity_KeyPress(KeyCode As Int) As Boolean
	' Not mandatory, it depends on your app and device
	If dlgFileExpl.IsInitialized Then
		If dlgFileExpl.IsActive Then Return True
	End If
	 If KeyCode == KeyCodes.KEYCODE_BACK Then
		If panel_click_ == 0 Then
		''for exiting application
				Dim confirm As Int
			confirm = Msgbox2("Would you to log out your account?","C O N F I R M A T I O N","YES","","NO",Null)
			If confirm == DialogResponse.POSITIVE Then
				login_form.is_log_in = False
				ExitApplication
				StartActivity("login_form")
				'ExitApplication
			Else
			
			End If
			''------------------
		else if panel_click_ == 1 Then
		  	'''
			If edit_panel_click_ == 1 Then
					pnl_blood_body.RemoveView
					edit_panel_click_ = 0
			else if edit_panel_click_ == 2 Then
				pnl_bday_body.RemoveView
				 edit_panel_click_ = 0
			else if edit_panel_click_ == 3 Then
				pnl_body.RemoveView
				edit_panel_click_  = 0
			else if edit_panel_click_ == 4 Then
				pnl_donated_body.RemoveView
				edit_panel_click_ = 0
			else if edit_panel_click_ == 5 Then
				pnl_gender_body.RemoveView
				edit_panel_click_ = 0
			Else
				profile_all_body.RemoveView
				edit_panel_click_ = 0
				 panel_click_ = 0
			End If
			''''
		 else if panel_click_ == 2 Then
		 	about_us_pnl.RemoveView
			panel_click_ = 0
		else if panel_click_ == 3 Then
			help_us_pnl.RemoveView
			panel_click_ = 0
		End If

	End If
	
    Return True
End Sub
Sub usr_img_click
	Try
	dlgFileExpl.Initialize(Activity, "/mnt/sdcard", ".bmp,.gif,.jpg,.png", True, False, "OK")
	dlgFileExpl.FastScrollEnabled = True
	dlgFileExpl.Explorer2(True)
	If Not(dlgFileExpl.Selection.Canceled Or dlgFileExpl.Selection.ChosenFile = "") Then
	'Msgbox("Picture:" & CRLF & dlgFileExpl.Selection.ChosenPath & "/" & dlgFileExpl.Selection.ChosenFile, "Your choice")
					Dim img_string As String
					Dim su As StringUtils
					Dim out1 As OutputStream
				
					out1.InitializeToBytesArray(0) 'size not really important
					File.Copy2(File.OpenInput(dlgFileExpl.Selection.ChosenPath, dlgFileExpl.Selection.ChosenFile), out1)
					img_string=su.EncodeBase64(out1.ToBytesArray)
					Log(img_string)
					'image_container = img_string
					'''
	Dim inp As InputStream
	Dim bmp As Bitmap
	Dim su As StringUtils
	Dim bytes() As Byte
	bytes = su.DecodeBase64(img_string)
	inp.InitializeFromBytesArray(bytes,0,bytes.Length)
	bmp.Initialize2(inp)
	''
	Dim bd As BitmapDrawable
	bd.Initialize(bmp)
	'usr_img.Bitmap = bd
	usr_img.Background = bd
	'usr_img.SetBackgroundImage(bmp)
	'Log(img_string)
	End If
	Catch
		Log(LastException.Message)
	End Try
End Sub
Sub ResizeBitmap(original As Bitmap, width As Int, height As Int) As Bitmap
    Dim new As Bitmap
    new.InitializeMutable(width, height)
    Dim c As Canvas
    c.Initialize2(new)
    Dim destRect As Rect
    destRect.Initialize(0, 0, width, height)
    c.DrawBitmap(original, Null, destRect)
    Return new
End Sub
Public Sub JobDone(job As HttpJob)
	If job.Success Then
		Select job.JobName
			Case "all_info_query"
			  Dim user_all_info As TextWriter
   					 user_all_info.Initialize(File.OpenOutput(File.DirInternalCache, "users_all_info.txt", False))
    		    user_all_info.WriteLine(job.GetString.Trim)
  			  user_all_info.Close
			  'Log(job.GetString.Trim)
			End Select
		''''''''''''''''''''''''''''''''''''''''''''''''''
			If optionSelected == "pofileView" Then
				all_input_on_list
			Else
				ProgressDialogHide	
				profile_all_body.RemoveView
				Dim confirmR As Int
				confirmR = Msgbox2("Successfuly Update!","C O N F I R M A T I O N","OK","","",Null)
				If confirmR == DialogResponse.POSITIVE Then
		  		users_out_lbl.text = text_answer.Text
		'' login_form.name_query = text_answer.Text
			Else
			End If
			End If
		else If job.Success == False Then
			ProgressDialogHide
			profile_all_body.RemoveView
			Msgbox("Error connecting to server, try again!","C O N F I R M A T I O N")	
	End If
	
End Sub
Sub profiled_Click
	'' profile of person's user		
		profile_panel.Initialize("profile_panel")
		'scroll_profile_pnl.Initialize(100%x,100%y,"scroll_pnl") 
		profile_panel.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
		Dim title As Label : title.Initialize("")
		Dim fullN As EditText : fullN.Initialize("")
		Dim blood_sel As Label : blood_sel.Initialize("")
		Dim email As EditText : email.Initialize("")
		Dim phone1 As EditText : phone1.Initialize("")
		Dim phone2 As EditText : phone2.Initialize("")
		Dim location As Label : location.Initialize("")
		Dim bday As Label : bday.Initialize("")
		Dim nickN As EditText : nickN.Initialize("")
		Dim isDonated As Label : isDonated.Initialize("")
			Dim img_fullN As ImageView : img_fullN.Initialize("")
			Dim img_blood_sel As ImageView : img_blood_sel.Initialize("")
			Dim img_email As ImageView : img_email.Initialize("")
			Dim img_phone1 As ImageView : img_phone1.Initialize("")
			Dim img_phone2 As ImageView : img_phone2.Initialize("")
			Dim img_location As ImageView : img_location.Initialize("")
			Dim img_bday As ImageView : img_bday.Initialize("")
			Dim img_nickN As ImageView : img_nickN.Initialize("")
			Dim img_isDonated As ImageView : img_isDonated.Initialize("")
			
		title.Text = "My Information" '' titte
				title.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		title.Gravity = Gravity.CENTER
		title.TextSize = 20 '''-------
		''images 
		blood_sel.Text = "A"
		bday.Text = "may/13/1993"
		location.Text = "hinigaran neg occ"
			img_fullN.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-4-user.png"))
			img_blood_sel.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-93-tint.png"))
			img_email.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-social-40-e-mail.png"))
			img_phone1.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-354-nameplate-alt1.png"))
			img_phone2.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-354-nameplate-alt2.png"))
			img_location.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-21-home.png"))
			img_bday.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-40-notes.png"))
			img_nickN.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-265-vcard.png"))
			img_isDonated.SetBackgroundImage(LoadBitmap(File.DirAssets,"glyphicons-91-eyedropper.png"))
		''----
		
	   	profile_panel.AddView(title,0,1%y,90%x,8%y) '' tittle
		profile_panel.AddView(img_fullN,5%x, title.Top+title.Height, 10%x, 10%y) ''full name image
		profile_panel.AddView(fullN, img_fullN.Left+img_fullN.Width, title.Top+title.Height, 60%x, 8%y) ''full name 
		
		profile_panel.AddView(img_blood_sel,5%x,fullN.Top+fullN.Height,10%x,10%y) ''blooad image
		profile_panel.AddView(blood_sel,img_blood_sel.Left+img_blood_sel.Width,img_blood_sel.Top,10%x,8%y) ''blooad name 
		
		profile_panel.AddView(img_email,5%x,img_blood_sel.Top+img_blood_sel.Height,10%x,10%y) ''email image
		profile_panel.AddView(email,img_email.Left+img_email.Width,img_email.Top,60%x, 8%y) ''email name 
		
		profile_panel.AddView(img_phone1,5%x,img_email.Top+img_email.Height,10%x,10%y) ''phone1 image
		profile_panel.AddView(phone1,img_phone1.Left+img_phone1.Width,img_phone1.Top,60%x, 8%y) ''phone1 name 
		
		profile_panel.AddView(img_phone2,5%x,img_phone1.Top+img_phone1.Height,10%x,10%y) ''phone2 image
		profile_panel.AddView(phone2,img_phone2.Left+img_phone2.Width,img_phone2.Top,60%x, 8%y) ''phone2 name 
		
		profile_panel.AddView(img_location,5%x,img_phone2.Top+img_phone2.Height,10%x,10%y) ''location image
		profile_panel.AddView(location,img_location.Left+img_location.Width,img_location.Top,60%x, 8%y) ''location name 
		
		profile_panel.AddView(img_bday,5%x,img_location.Top+img_location.Height,10%x,10%y) ''bday image
		profile_panel.AddView(bday,img_bday.Left+img_bday.Width,img_bday.Top,10%x,8%y) ''bday name 
		
		profile_panel.AddView(img_nickN,5%x,img_bday.Top+img_bday.Height,10%x,10%y) ''nickN image
		profile_panel.AddView(nickN,img_nickN.Left+img_nickN.Width,img_nickN.Top,60%x, 8%y) ''bdnickNay name 
		
		profile_panel.AddView(img_isDonated,5%x,img_nickN.Top+img_nickN.Height,10%x,10%y) ''nickN image
		profile_panel.AddView(isDonated,img_isDonated.Left+img_isDonated.Width,img_isDonated.Top,60%x, 8%y) ''bdnickNay name 
			'scroll_profile_pnl.FullScroll(100%y,True,true)
			
		scroll_profile_pnl.Panel.AddView(profile_panel,0,0,90%x,90%y)
		Activity.AddView(scroll_profile_pnl,5%x,3%y,90%x,90%y)
	'' end of person's infox
End Sub
Sub exit_btn_Click
	If login_form.is_log_in == True Then
		Dim confirm As Int
		confirm = Msgbox2("Would you to log out your account, and exit the application?","C O N F I R M A T I O N","YES","","NO",Null)
		If confirm == DialogResponse.POSITIVE Then
			login_form.is_log_in = False
				
			ExitApplication
		Else
			
		End If
			
	Else
		
	End If
End Sub
Sub cancel_btn_Click
	profile_all_body.RemoveView
End Sub

Sub update_btn_Click
	ProgressDialogShow2("Updating Please wait...",False)
	optionSelected = "updated_click"
	update_job.Initialize("update_job",Me)
	Dim url_back As calculations 
	Dim male_c,female_c As String
	male_c = File.GetText(File.DirAssets, "male_string.txt")	
	female_c = File.GetText(File.DirAssets, "female_string.txt")
	
	If image_container == male_c Or image_container == female_c Then
					Dim img_string As String
					Dim su As StringUtils
					Dim out1 As OutputStream
		If is_gender_index == 0 Then
					out1.InitializeToBytesArray(0) 'size not really important
					File.Copy2(File.OpenInput(File.DirAssets, "male_clip.png"), out1)
					img_string=su.EncodeBase64(out1.ToBytesArray)
					'Log(img_string.Length)
					image_container = img_string
		Else	
				out1.InitializeToBytesArray(0) 'size not really important
					File.Copy2(File.OpenInput(File.DirAssets, "female_clip.png"), out1)
					img_string=su.EncodeBase64(out1.ToBytesArray)
					'Log(img_string.Length)
					image_container = img_string
		End If
		
	Else
		''
	End If
			'' age calculating
				Dim Nmonth,Nday,Nyear,ageGet As Int
	   			Nday = DateTime.GetDayOfMonth(DateTime.Now)
	 			 Nmonth = DateTime.GetMonth(DateTime.Now)
	  			Nyear = DateTime.GetYear(DateTime.Now)
	  
				Dim age,Pyear,Pmonth,Pday As Int
				Pyear = bday_year_selected
				Pmonth = bday_month_selected
				Pday = bday_day_selected
				age = Nyear - Pyear
				If Pmonth <= Nmonth And Pday <= Nday Then
          		 'year_sub = year_sub+1;
		  		 ageGet = age
      			 Else
           		 ageGet = age-1
      			 End If
			''-----------------
			
		url_back.Initialize
	'Dim all_users_info As String
	Dim ins,m_1,m_2,m_3,merge As String

    If text_fn.Text == ""  Or text_email.Text == "" Or text_phonenumber2.Text == "" Or text_phonenumber.Text == "" Or text_answer.Text == "" Then
		ProgressDialogHide
		Msgbox("Error: Fill up those empty fields before you update!","C O N F I R M A T I O N")
	Else
		m_1 = "UPDATE `person_info` SET `full_name`='"&text_fn.Text&"',`blood_type`='"&blood_selected&"', `phone_number1`='"&text_phonenumber.Text&"', `phone_number2`='"&text_phonenumber2.Text&"', `location_brgy`='"&location_brgy_selected&"', `location_street`='"&location_street_selected&"', "
		m_2 = "`location_purok`='', `bday_month`='"&bday_month_selected&"',`bday_day`='"&bday_day_selected&"', `bday_year`='"&bday_year_selected&"', `nick_name`='"&text_answer.Text&"', `donate_boolean`='"&is_donated&"', `lat`='"&lat&"', `long`='"&lng&"', `image`='"&image_container&"', "
		m_3 = "`age`='"&ageGet&"',`date_donated`='"&isDonateDate&"',`gender`='"&gender_string_data&"' WHERE `id`="&login_form.id_query&";"
		merge = m_1&m_2&m_3
		ins = url_back.php_email_url("updating.php")
		update_job.Download2(ins,Array As String("update",""&merge))
		
	End If
	
End Sub
Sub edit_gender_Click
	edit_panel_click_ = 5
	list_is_gender.Initialize
	spin_gender.Initialize("spin_gender")
	list_is_gender.Add("Male")
	list_is_gender.Add("Female")
	spin_gender.AddAll(list_is_gender)
	Dim pnl As Panel
	Dim edit_ok_btn,edit_can_btn As Button
	Dim title_lbl As Label
	edit_ok_btn.Initialize("edit_gender_ok_btn")
	edit_can_btn.Initialize("edit_gender_can_btn")
	title_lbl.Initialize("")
	edit_ok_btn.Text = "OK"
	edit_can_btn.Text = "CANCEL"
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
	title_lbl.Text = "SELECT GENDER"
			edit_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		edit_can_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		title_lbl.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	title_lbl.Gravity = Gravity.CENTER
	pnl.Initialize("pnl")
	pnl_gender_body.Initialize("pnl_gender_body")
	pnl_gender_body.Color = Colors.Transparent
	pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)
	pnl.AddView(spin_gender,2%x,title_lbl.Top+title_lbl.Height+1%y,68%x,8%y)
	pnl.AddView(edit_ok_btn,15%x,spin_gender.Top+spin_gender.Height+3%y,20%x,8%y)
	pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok_btn.Width+2%x,spin_gender.Top+spin_gender.Height+3%y,20%x,8%y)

	pnl_gender_body.AddView(pnl,13%x,((Activity.Height/2)/2),74%x,33%y)
	pnl_gender_body.BringToFront
	'pnl_body.Enabled = False
		'pnl_donated_body.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(pnl_gender_body,0,0,100%x,100%y)	
End Sub

Sub spin_gender_ItemClick (Position As Int, Value As Object)
	is_gender_index = Position
End Sub
Sub edit_gender_ok_btn_click
	'is_gender_index = 0
	text_gender.Text = list_is_gender.Get(is_gender_index)
	gender_string_data = list_is_gender.Get(is_gender_index)
	pnl_gender_body.RemoveView
End Sub
Sub edit_gender_can_btn_click
	pnl_gender_body.RemoveView
End Sub
Sub pnl_gender_body_click
	''
End Sub
Sub donated_edit_Click
	edit_panel_click_ = 4
	list_donated.Initialize
	spin_donated.Initialize("spin_donated")
	list_donated.Add("NO")
	list_donated.Add("YES")
	spin_donated.AddAll(list_donated)
	Dim pnl As Panel
	Dim edit_ok_btn,edit_can_btn As Button
	Dim title_lbl As Label
	edit_ok_btn.Initialize("edit_donated_ok_btn")
	edit_can_btn.Initialize("edit_donated_can_btn")
	title_lbl.Initialize("")
	edit_ok_btn.Text = "OK"
	edit_can_btn.Text = "CANCEL"
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
	title_lbl.Text = "SELECT DONATED STATUS"
			edit_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		edit_can_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		title_lbl.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	title_lbl.Gravity = Gravity.CENTER
	pnl.Initialize("pnl")
	pnl_donated_body.Initialize("pnl_donated_body")
	pnl_donated_body.Color = Colors.Transparent
	pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)
	pnl.AddView(spin_donated,2%x,title_lbl.Top+title_lbl.Height+1%y,68%x,8%y)
	pnl.AddView(edit_ok_btn,15%x,spin_donated.Top+spin_donated.Height+3%y,20%x,8%y)
	pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok_btn.Width+2%x,spin_donated.Top+spin_donated.Height+3%y,20%x,8%y)

	pnl_donated_body.AddView(pnl,13%x,((Activity.Height/2)/2),74%x,33%y)
	pnl_donated_body.BringToFront
	'pnl_body.Enabled = False
		'pnl_donated_body.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(pnl_donated_body,0,0,100%x,100%y)	
End Sub
Sub spin_donated_ItemClick (Position As Int, Value As Object)
	Log(Position)
	is_donated = Value
	donated_index = Position
End Sub
Sub edit_donated_ok_btn_click
	If donated_index == 0 Then
		is_donated = "NO"
		isDonateDate = "NONE"
		text_donated.Text = "NO"
	Else
		isDonate_edit_
	End If
	
	
	pnl_donated_body.RemoveView
End Sub
Sub isDonate_edit_
	donated_index = 0
	'edit_panel_click_ = 4
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
			edit_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		edit_can_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
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
	'is_donate_date.Text = "("&isDonateDate&")"		
	Msgbox(""&month&"/"&day&"/"&year,"Date Selected")
		text_donated.Text = is_donated
	Log(isDonateDate)
	pnl_bday_body.RemoveView
End Sub
Sub isdonated_can_btn_click
	pnl_bday_body.RemoveView
	If text_donated.Text == "NO" Then
	text_donated.Text = "NO"
	Else
	text_donated.Text = "YES"
	End If
	
End Sub

'''
Sub edit_donated_can_btn_click
	pnl_donated_body.RemoveView
End Sub
Sub pnl_donated_body_click 
	'' this is for modal body... please don't delete.
End Sub
Sub bday_edit_Click
		edit_panel_click_ = 2
		list_day.Initialize
		list_month.Initialize
		list_year.Initialize
		spin_day.Initialize("spin_day")	
		spin_month.Initialize("spin_month")	
		spin_year.Initialize("spin_year")	
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
	edit_ok_btn.Initialize("edit_bday_ok_btn")
	edit_can_btn.Initialize("edit_bday_can_btn")
	title_lbl.Initialize("")
	edit_ok_btn.Text = "OK"
	edit_can_btn.Text = "CANCEL"
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
	title_lbl.Text = "SELECT BIRTH DATE"
				edit_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		edit_can_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
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
Sub spin_day_ItemClick (Position As Int, Value As Object)
	bday_day_selected = Value
	Log("day: "&Value)
End Sub
Sub spin_month_ItemClick (Position As Int, Value As Object)
  bday_month_selected = Value
  	Log("month: "&Value)
End Sub
Sub spin_year_ItemClick (Position As Int, Value As Object)
	 bday_year_selected =  Value
	 	Log("year: "&Value)
End Sub
Sub edit_bday_ok_btn_click
	text_bday.Text = bday_month_selected&"/"&bday_day_selected&"/"&bday_year_selected
		Log("date: "& bday_month_selected&"/"&bday_day_selected&"/"&bday_year_selected)
	pnl_bday_body.RemoveView
End Sub
Sub edit_bday_can_btn_click
	pnl_bday_body.RemoveView
End Sub
Sub pnl_bday_body_click
	'' this is for modal.. please do not delete
End Sub
Sub blood_edit_Click
	'Log("its me")
	edit_panel_click_ = 1
	list_bloodgroup.Initialize
	spin_bloodgroup.Initialize("spin_bloodgroup")
	list_bloodgroup.Add("A")
	list_bloodgroup.Add("B")
	list_bloodgroup.Add("O")
	list_bloodgroup.Add("AB")
	'list_bloodgroup.Add("A+")
	'list_bloodgroup.Add("B+")
	'list_bloodgroup.Add("O+")
	'list_bloodgroup.Add("AB+")
	list_bloodgroup.Add("A-")
	list_bloodgroup.Add("B-")
	list_bloodgroup.Add("O-")
	list_bloodgroup.Add("AB-")
	spin_bloodgroup.AddAll(list_bloodgroup)
	Dim pnl As Panel
	Dim edit_ok_btn,edit_can_btn As Button
	Dim title_lbl As Label
	title_lbl.Initialize("")
	edit_ok_btn.Initialize("edit_blood_ok_btn")
	edit_can_btn.Initialize("edit_blood_can_btn")
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
	edit_ok_btn.Text = "OK"
	edit_can_btn.Text = "CANCEL"
	pnl.Initialize("pnl")
	pnl_blood_body.Initialize("pnl_blood_body")
	pnl_blood_body.Color = Colors.Transparent
	pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	title_lbl.Text = "SELECT BLOOD TYPE"
			edit_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		edit_can_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		title_lbl.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	title_lbl.Gravity = Gravity.CENTER
	pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)
	pnl.AddView(spin_bloodgroup,2%x,title_lbl.Top + title_lbl.Height + 1%y,68%x,8%y)
	pnl.AddView(edit_ok_btn,15%x,spin_bloodgroup.Top+spin_bloodgroup.Height+3%y,20%x,8%y)
	pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok_btn.Width+2%x,spin_bloodgroup.Top+spin_bloodgroup.Height+3%y,20%x,8%y)

	pnl_blood_body.AddView(pnl,13%x,((Activity.Height/2)/2),74%x,33%y)
	pnl_blood_body.BringToFront
	'pnl_body.Enabled = False
	'pnl_blood_body.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(pnl_blood_body,0,0,100%x,100%y)	
End Sub
Sub edit_blood_ok_btn_click
	text_blood.Text = blood_selected
	pnl_blood_body.RemoveView
End Sub
Sub edit_blood_can_btn_click
	pnl_blood_body.RemoveView
End Sub
Sub pnl_blood_body_click
	'this is for modal of editing blood type. don't delete
End Sub
Sub spin_bloodgroup_ItemClick (Position As Int, Value As Object)
	blood_selected = Value
End Sub

Sub locate_edit_Click
		edit_panel_click_ = 3
	list_location_b.Initialize
		location_spin_street.Initialize("location_spin_street")
		location_spin_brgy.Initialize("location_spin_brgy")
			list_location_s.Initialize
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
	location_spin_brgy.AddAll(list_location_b)
	
	list_location_s.Add("Rizal st.")
	list_location_s.Add("Valega st.")
	list_location_s.Add("Sarmiento st.")
	list_location_s.Add("Bantolinao st.")
	list_location_s.Add("Versoza st.")
	list_location_s.Add("Jimenez st.")
	list_location_s.Add("Olmedo st.")
	list_location_s.Add("Burgos st.")
	location_spin_street.AddAll(list_location_s)
	'street_lat_lng
	Dim pnl As Panel
	Dim edit_ok_btn,edit_can_btn As Button
	Dim title_lbl As Label
	edit_ok_btn.Initialize("edit_ok_btn")
	edit_can_btn.Initialize("edit_can_btn")
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
	title_lbl.Initialize("")
	edit_ok_btn.Text = "OK"
	edit_can_btn.Text = "CANCEL"
	title_lbl.Text = "SELECT LOCATION"
		edit_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		edit_can_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
		title_lbl.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	title_lbl.Gravity =  Gravity.CENTER
	pnl.Initialize("pnl")
	pnl_body.Initialize("pnl_body")
	pnl_body.Color = Colors.Transparent
	pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)
	pnl.AddView(location_spin_brgy,2%x,title_lbl.Top + title_lbl.Height + 1%y,33%x,8%y)
	pnl.AddView(location_spin_street,location_spin_brgy.Left+location_spin_brgy.Width + 2%x,location_spin_brgy.Top,33%x,8%y)
	pnl.AddView(edit_ok_btn,15%x,location_spin_street.Top+location_spin_street.Height+3%y,20%x,8%y)
	pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok_btn.Width+2%x,location_spin_street.Top+location_spin_street.Height+3%y,20%x,8%y)

	pnl_body.AddView(pnl,13%x,((Activity.Height/2)/2),74%x,33%y)
	pnl_body.BringToFront
	'pnl_body.Enabled = False
	'pnl_body.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(pnl_body,0,0,100%x,100%y)	
End Sub
Sub edit_can_btn_click
	pnl_body.RemoveView
End Sub
Sub edit_ok_btn_click
	Dim click_string As String
	click_string = location_spin_brgy.GetItem(brgy_index)&", "&location_spin_street.GetItem(street_index)
	location_brgy_selected = location_spin_brgy.GetItem(brgy_index)
    location_street_selected = location_spin_street.GetItem(street_index)
	text_location.Text = click_string
	pnl_body.RemoveView
End Sub
Sub pnl_body_click
	''this is to click the body of location modal do not delete
End Sub
Sub all_inputs_click
	''this is to click the body of location modal do not delete
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
	
	Log("lat: "&lat&CRLF&"lng: "&lng)
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
Sub about_Click
	panel_click_ = 2
	Dim pnl As Panel
	Dim about_ok_btn As Button
	Dim title_lbl,about_data,for_h As Label
	'Dim h1,h2,hh As String
	for_h.Initialize("")
	Dim sus As StringUtils
	
	'h1 = "•	{ib}Becgrajhon2013{ib} was the developer and designer of the LIFEBLOOD WITH GIS mobile app. It was started when the developer saw the posted in NONESCOST IT building that there was a boy that need a certain blood to help cure his illness."
	'h2 = "•	{iib}Philippines{iib} is facing a blood shortage and even Himamaylan City. People not only have to run from their respective place to other barangay or other city as well because they need some donor, they will be go to Bacolod City to process the blood donation procedure of the government. This LIFEBLOOD WITH GIS will help the NOT only in people of HIMAMAYLAN CITY but in other neighboring municipalities and cities in finding blood donors."
	'hh = h1&h2
	about_us_pnl.Initialize("about_us_pnl")
	about_ok_btn.Initialize("about_ok_btn")
	title_lbl.Initialize("")
	about_data.Initialize("")
	about_ok_btn.Text = "OK"
	about_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
			Dim V_btn,C_btn As GradientDrawable
			Dim colorG(2) As Int
			colorG(0) = Colors.White
			colorG(1) = Colors.Red
			C_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.CornerRadius = 50dip
		about_ok_btn.Background = V_btn
	title_lbl.Text = "ABOUT"
	title_lbl.TextSize = 25
	title_lbl.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	'title_lbl.Background = C_btn
	title_lbl.SetBackgroundImage(LoadBitmap(File.DirAssets,"bgs.jpg"))
	title_lbl.TextColor = Colors.White
	''for string of about us design...
	Dim rs As RichString
	Dim f_string,s_string As String
	f_string = CRLF&"•	{ib}{bg1}Becgrajhon2013{bg1}{ib} was the developer and designer of the LIFEBLOOD WITH GIS mobile app. It was started when the developer saw the posted in NONESCOST IT building that there was a boy that need a certain blood to help cure his illness."
	s_string = CRLF&CRLF&"•	{iib}{bg2}Philippines{bg2}{iib} is facing a blood shortage and even Himamaylan City. People not only have to run from their respective place to other barangay or other city as well because they need some donor, they will be go to Bacolod City to process the blood donation procedure of the government. This LIFEBLOOD WITH GIS will help the NOT only in people of HIMAMAYLAN CITY but in other neighboring municipalities and cities in finding blood donors."
	rs.Initialize(f_string&s_string)
	rs.Style2(rs.STYLE_BOLD_ITALIC, "{ib}")
	rs.Style2(rs.STYLE_BOLD_ITALIC, "{iib}")
	'rs.BackColor2(Colors.DarkGray,"{bg1}")
	'rs.BackColor2(Colors.DarkGray,"{bg2}")
	rs.Underscore2("{bg1}")
	rs.Underscore2("{bg2}")
	about_data.Text = rs '' to set the string output
	about_data.Typeface = Typeface.LoadFromAssets("ZINGBISD.otf")
	about_data.TextSize = 17
		for_h.Text = rs
		about_us_pnl.AddView(for_h,0,0,50%x,50%y)
		for_h.Visible = False
		Dim string_h As Int : string_h= sus.MeasureMultilineTextHeight(for_h,for_h.Text)
	''-------------------------------
			about_sc2d.Initialize(68%x,string_h+7%Y,"about_sc2d")
			about_sc2d.ScrollbarsVisibility(False,False)
	title_lbl.Gravity = Gravity.CENTER
	pnl.Initialize("pnl")
	about_us_pnl.Color = Colors.Transparent
	pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	pnl.AddView(title_lbl,2%x,2%y,70.5%x,8%y)
	
	about_sc2d.Panel.AddView(about_data,2%x,0,66%x,string_h + 10%Y)
	pnl.AddView(about_sc2d,2%x,title_lbl.Top + title_lbl.Height,70%x,60%y)
	pnl.AddView(about_ok_btn,1%x,about_sc2d.Top + about_sc2d.Height,72%x,8%y)
	
	about_us_pnl.AddView(pnl,13%x,((((Activity.Height/2)/2)/2)/2),74%x,80%y)
	about_us_pnl.BringToFront
		
	'pnl_body.Enabled = False
	'pnl_bday_body.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(about_us_pnl,0,0,100%x,100%y)	
End Sub
Sub about_ok_btn_click
	about_us_pnl.RemoveView
End Sub
Sub about_us_pnl_click
	''
End Sub
Sub help_Click
	panel_click_ = 3
		Dim pnl As Panel
	Dim help_ok_btn As Button
	Dim title_lbl,help_data,for_h As Label
	for_h.Initialize("")
	Dim sus As StringUtils
	help_ok_btn.Initialize("help_ok_btn")
	title_lbl.Initialize("")
	help_data.Initialize("")
	help_ok_btn.Text = "OK"
	help_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
			Dim V_btn,C_btn As GradientDrawable
			Dim colorG(2) As Int
			colorG(0) = Colors.White
			colorG(1) = Colors.Red
			C_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.Initialize("TOP_BOTTOM",colorG)
			V_btn.CornerRadius = 50dip
			'C_btn.CornerRadius = 50dip
		help_ok_btn.Background = V_btn
	title_lbl.Text = "HELP"
	title_lbl.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	title_lbl.TextSize = 25
	'title_lbl.Background = C_btn
	title_lbl.SetBackgroundImage(LoadBitmap(File.DirAssets,"bgs.jpg"))
	title_lbl.TextColor = Colors.White
	''for string of about us design...
		help_us_pnl.Initialize("help_us_pnl")
	Dim rs As RichString
	Dim f_string,s_string,t_string,fo_string,fi_string,s_string As String
	f_string = CRLF&"•	Once you gave your name and your contact number, all your information will be shown to all this mobile app users.  "
	s_string = CRLF&CRLF&"•  Your mobile number will be call you once the recipient found you that you are one of the possible donors. "
	t_string = CRLF&CRLF&"•  DO NOT do a search and contact just to test app. If you do TEST the SEARCH button and contact the donor and tell the donor that you only testing the app the donor might be disappointed. You will waste not only your time and effort but you will waste a patient’s who is really need a donor."
	fo_string = CRLF&CRLF&"•  Using call button is more effective than sending message to the donor. It saves time and effort."
	fi_string = CRLF&CRLF&"•  Select the correct Feedback given to this app so that it will help in boosting the confident of the developer and will be making more community based app like this LIFEBLOOD WITH GIS."
	s_string = CRLF&CRLF&"•  Please share this app and let us build a community that cares."
	rs.Initialize(f_string&s_string&t_string&fo_string&fi_string&s_string)
	help_data.Text = rs '' to set the string output
	help_data.Typeface = Typeface.LoadFromAssets("ZINGBISD.otf")
	help_data.TextSize = 17
		for_h.Text = rs
		help_us_pnl.AddView(for_h,0,0,50%x,50%y)
		for_h.Visible = False
		Dim string_h As Int : string_h= sus.MeasureMultilineTextHeight(for_h,for_h.Text)
	''-------------------------------
			help_sc2d.Initialize(68%x,string_h+15%Y,"help_sc2d")
			help_sc2d.ScrollbarsVisibility(False,False)
	title_lbl.Gravity = Gravity.CENTER
	pnl.Initialize("pnl")
	'help_us_pnl.Initialize("help_us_pnl")
	help_us_pnl.Color = Colors.Transparent
	pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	pnl.AddView(title_lbl,2%x,2%y,70%x,8%y)
	
	pnl.AddView(help_sc2d,2%x,title_lbl.Top + title_lbl.Height,70%x,60%y)
	help_sc2d.Panel.AddView(help_data,2%x,0,66%x,string_h+15%Y)
	pnl.AddView(help_ok_btn,1%x,help_sc2d.Top + help_sc2d.Height,72%x,8%y)

	help_us_pnl.AddView(pnl,13%x,((((Activity.Height/2)/2)/2)/2),74%x,80%y)
	help_us_pnl.BringToFront
	'pnl_body.Enabled = False
	'pnl_bday_body.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(help_us_pnl,0,0,100%x,100%y)	
End Sub
Sub help_ok_btn_click
	help_us_pnl.RemoveView
End Sub
Sub help_us_pnl_click
	''
End Sub