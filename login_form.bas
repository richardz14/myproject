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
	Public id_query As String : id_query = "1"
	Public name_query As String : name_query = "Me"
	Private log_click As Boolean
	Public is_log_in As Boolean : is_log_in = True
	Dim sqlLite As SQL
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private ban_picture As ImageView
	Private ban_panel As Panel
	Private ban_tools As Panel
	Private label_email As Label
	Private label_password As Label
	Private text_email As EditText
	Private text_password As EditText
	Private label_forgot As Label
	Private log_in_button As Button
	Private ban_create As Panel
	Private new_acc_button As Button
	
	Private h_email As HttpJob
	Private h_pass As HttpJob
	Private h_fullname As HttpJob
	Private user_id As HttpJob
	Private nick_name As HttpJob
	Dim Email,pass,name,true_false="false" As String
	Private booleanCount = 0 As Int
	Private booleanforgotcount = 0 As Int
	
	Dim calcs As calculations
	Private forgot_pass_pnl As Panel
	Dim email_forgot,nickN_forgot,newPass,renewPass As EditText
	Dim forgot_email_job As HttpJob
	Dim forgot_nick_job As HttpJob
	Dim forgot_recover_job As HttpJob
	Dim email_for,nickName_for As String
	
	Dim a1, a2, a3 As Animation
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("login_form")
	''http utils initialization
	Activity.Title = "LOG IN"
	calcs.Initialize
		h_email.Initialize("email_get",Me)
		h_pass.Initialize("pass_get",Me)
		h_fullname.Initialize("full_name_get",Me)
		user_id.Initialize("user_id_get",Me)
		nick_name.Initialize("nick_name_get",Me)
		forgot_email_job.Initialize("forgot_email_get",Me)
		forgot_nick_job.Initialize("forgot_nick_get",Me)
		forgot_recover_job.Initialize("forgot_recover_job",Me)
	''
	all_settings_layout
		email_forgot.Initialize("")
	nickN_forgot.Initialize("")
	newPass.Initialize("")
	renewPass.Initialize("")
	for_btn_animation
	database_init
	sqlLite.Initialize(File.DirInternal,"mydb.db",True)
End Sub
Sub for_btn_animation
	
	a1.InitializeAlpha("", 1, 0)
	a2.InitializeAlpha("", 1, 0)
	a3.InitializeAlpha("", 1, 0)
	new_acc_button.Tag = a2
	log_in_button.Tag = a1
	label_forgot.Tag = a3
		Dim animations() As Animation
	animations = Array As Animation(a1, a2, a3)
	For i = 0 To animations.Length - 1
		animations(i).Duration = 400
		animations(i).RepeatCount = 1
		animations(i).RepeatMode = animations(i).REPEAT_REVERSE
	Next
End Sub
Sub database_init
		If File.Exists(File.DirInternal,"mydb.db") = False Then
	File.Copy(File.DirAssets,"mydb.db",File.DirInternal,"mydb.db")
	Else
	End If
End Sub
Sub Activity_KeyPress(KeyCode As Int) As Boolean
	' Not mandatory, it depends on your app and device	
	If KeyCode == KeyCodes.KEYCODE_BACK Then
		Dim set_cursor As Cursor
	set_cursor = sqlLite.ExecQuery("select * from data where `id`=1;")
						For i = 0 To set_cursor.RowCount - 1
							set_cursor.Position = i	
								If set_cursor.GetString("isStart") == 1 Then
									Dim choose As Int
									choose = Msgbox2("would you like to exit this application?","C O N F I R M A T I O N","YES","","NO",Null)		
									If choose == DialogResponse.POSITIVE Then
										ExitApplication
									Else
									End If
								Else
									
							End If
						Next
		Else
	End If
    
	Return True
End Sub
Public Sub JobDone(job As HttpJob)
	If job.Success Then
		Select job.JobName
			Case "email_get"
			Log(job.GetString)
			Email = job.GetString.Trim
			Case "pass_get"
				Log(job.GetString)
			pass = job.GetString.Trim
			Case "full_name_get"
				Log(job.GetString)
			name_query = job.GetString.Trim
			name = job.GetString.Trim
			Case "user_id_get"
				Log(job.GetString)
				id_query = job.GetString.Trim
			'id_query = job.GetString.Trim
	     	Case "nick_name_get"
				Log(job.GetString)
				'name_query = job.GetString.Trim
				calcs.name = job.GetString.Trim
			Case "forgot_email_get"
				email_for = job.GetString.Trim
			Case "forgot_nick_get"
				nickName_for = job.GetString.Trim
			End Select
		''''''''''''''''''''''''''''''''''''''''''''''''''
	booleanforgotcount = booleanforgotcount + 1
	booleanCount = booleanCount+1
	
	If log_click == True Then
		If booleanCount = 4 Then '''''''' 1st statement
			ProgressDialogHide
			If text_email.Text == "" Or text_password.Text == "" Then ''''' 2nd statement
 						Msgbox("Error empty field.!","C O N F I R M A T I O N")
			Else
				If text_email.Text.CompareTo(Email) == 0 And text_password.Text.CompareTo(pass) == 0  Then ''''''' 3rd statement
							'Dim calc As calculations
							'calc.Initialize
							'name = calc.getting_fulln(calc.php_email_url("index3.php"),text_email.Text,text_password.Text)
						Dim dialogR As Int
						dialogR = Msgbox2("Welcome "&name,"C O N F I R M A T I O N","OK","","",Null)
						booleanCount = 0
						If  dialogR == DialogResponse.POSITIVE Then
							text_password.Text = ""
							text_email.text = ""
							is_log_in = True
						StartActivity("menu_form")
						Else
						End If
				Else
						Msgbox("Error email address or password.!","C O N F I R M A T I O N")
						text_password.Text = ""
						booleanCount = 0
				End If ''''''''''''' 3rd statement
			End If	''''''''' 2nd statement
		Else
		
		End If	'''''''''''''''''''' 1st statement	
	End If		
		'''''''''''''''''''''''''''''''''''''''''''''''''''
		''for recover account
	If log_click == False Then
		If booleanforgotcount == 1 Then
			ProgressDialogHide
			If email_for.CompareTo(email_forgot.Text) == 0 And nickName_for.CompareTo(nickN_forgot.Text) == 0 Then
				'ToastMessageShow("good",False)
				Dim url_back As calculations
				Dim forgot_string As String
				url_back.Initialize
				forgot_string = url_back.php_email_url("forgot_pass.php")
				forgot_recover_job.Download2(forgot_string,Array As String("recover","UPDATE `person_info` SET `password`=ENCODE('"&newPass.Text&"','goroy') WHERE  `email`='"&email_for&"' and `nick_name`='"&nickName_for&"';"))
				ToastMessageShow("Successfully Recovered Account!",True)
				forgot_pass_pnl.RemoveView
			Else
			Msgbox("Error: Information not existed!","C O N F I R M A T I O N")
			End If
		End If
	End If
		
	Else If job.Success == False Then
	ProgressDialogHide
		If booleanCount = 4 And booleanforgotcount = 1 Then
		Msgbox("Error: Error connecting to server, try again later.!","C O N F I R M A T I O N")
		booleanCount = 0
		booleanforgotcount = 0
		Else
		booleanCount = 4
		booleanforgotcount = 1
		End If
				'If booleanforgotcount = 1 Then
				'Msgbox("Error: Error connecting to server, try again later.!","C O N F I R M A T I O N")
				
				'Else
				
				'End If
	End If

End Sub
Public Sub back_up_JobDone(job As HttpJob)
	If job.Success Then
		Select job.JobName
			Case "email_get"
			Log(job.GetString)
			Email = job.GetString.Trim
			Case "pass_get"
				Log(job.GetString)
			pass = job.GetString.Trim
			Case "full_name_get"
				Log(job.GetString)
			name_query = job.GetString.Trim
			name = job.GetString.Trim
			Case "user_id_get"
				Log(job.GetString)
				id_query = job.GetString.Trim
			'id_query = job.GetString.Trim
	     	Case "nick_name_get"
				Log(job.GetString)
				'name_query = job.GetString.Trim
				calcs.name = job.GetString.Trim
			Case "forgot_email_get"
				email_for = job.GetString.Trim
			Case "forgot_nick_get"
				nickName_for = job.GetString.Trim
			End Select
		''''''''''''''''''''''''''''''''''''''''''''''''''
	booleanforgotcount = booleanforgotcount + 1
	booleanCount = booleanCount+1
	
	If log_click == True Then
		If booleanCount = 4 Then '''''''' 1st statement
			ProgressDialogHide
			If text_email.Text == "" Or text_password.Text == "" Then ''''' 2nd statement
 						Msgbox("Error empty field.!","C O N F I R M A T I O N")
			Else
				If text_email.Text.CompareTo(Email) == 0 And text_password.Text.CompareTo(pass) == 0  Then ''''''' 3rd statement
							'Dim calc As calculations
							'calc.Initialize
							'name = calc.getting_fulln(calc.php_email_url("index3.php"),text_email.Text,text_password.Text)
						Dim dialogR As Int
						dialogR = Msgbox2("Welcome "&name,"C O N F I R M A T I O N","OK","","",Null)
						booleanCount = 0
						If  dialogR == DialogResponse.POSITIVE Then
							text_password.Text = ""
							text_email.text = ""
							is_log_in = True
						StartActivity("menu_form")
						Else
						End If
				Else
						Msgbox("Error email address or password.!","C O N F I R M A T I O N")
						text_password.Text = ""
						booleanCount = 0
				End If ''''''''''''' 3rd statement
			End If	''''''''' 2nd statement
		Else
		
		End If	'''''''''''''''''''' 1st statement	
	End If		
		'''''''''''''''''''''''''''''''''''''''''''''''''''
		''for recover account
	If log_click == False Then
		If booleanforgotcount == 1 Then
			ProgressDialogHide
			If email_for.CompareTo(email_forgot.Text) == 0 And nickName_for.CompareTo(nickN_forgot.Text) == 0 Then
				'ToastMessageShow("good",False)
				Dim url_back As calculations
				Dim forgot_string As String
				url_back.Initialize
				forgot_string = url_back.php_email_url("forgot_pass.php")
				forgot_recover_job.Download2(forgot_string,Array As String("recover","UPDATE `person_info` SET `password`=ENCODE('"&newPass.Text&"','goroy') WHERE  `email`='"&email_for&"' and `nick_name`='"&nickName_for&"';"))
				ToastMessageShow("Successfully Recovered Account!",True)
				forgot_pass_pnl.RemoveView
			Else
			Msgbox("Error: Information not existed!","C O N F I R M A T I O N")
			End If
		End If
	End If
		
	Else If job.Success == False Then
	ProgressDialogHide
		If booleanCount = 4 And booleanforgotcount = 1 Then
		Msgbox("Error: Error connecting to server, try again later.!","C O N F I R M A T I O N")
		booleanCount = 0
		booleanforgotcount = 0
		Else
		booleanCount = 4
		booleanforgotcount = 1
		End If
				'If booleanforgotcount = 1 Then
				'Msgbox("Error: Error connecting to server, try again later.!","C O N F I R M A T I O N")
				
				'Else
				
				'End If
	End If

End Sub
Sub log_in_button_click
	a1.Start(log_in_button)
ProgressDialogShow2("please wait.!!",False)
Email = ""
pass = ""
log_click = True
	booleanCount = 0
	Dim url_back As calculations
	Dim url_email,url_pass,full_name,id,nickname As String
	url_back.Initialize
	url_email = url_back.php_email_url("index.php")
	url_pass = url_back.php_email_url("index1.php")
	full_name = url_back.php_email_url("search_blood_fullN.php")
	id = url_back.php_email_url("search_blood_id.php")
	nickname = url_back.php_email_url("search_blood_nickN.php")
	
	h_fullname.Download2(full_name,Array As String("full_name","SELECT full_name FROM `person_info` where `email`='"&text_email.Text&"';"))
	user_id.Download2(id,Array As String("id","SELECT id FROM `person_info` where `email`='"&text_email.Text&"';"))
	'nick_name.Download2(nickname,Array As String("nick","SELECT nick_name FROM `person_info` where `email`='"&text_email.Text&"';"))
	
	h_email.Download2(url_email,Array As String("email","SELECT email FROM `person_info` where `email`='"&text_email.Text&"';"))
	h_pass.Download2(url_pass,Array As String("pass","SELECT decode(password,'goroy') FROM `person_info` where `email`='"&text_email.Text&"';"))
	
End Sub
Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Public Sub all_settings_layout
	Activity.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	ban_picture.SetBackgroundImage(LoadBitmap(File.DirAssets,"banner01.jpg"))
	'log_in_button.SetBackgroundImage(LoadBitmap(File.DirAssets,"LOG_IN.png"))
	'new_acc_button.SetBackgroundImage(LoadBitmap(File.DirAssets,"CREATE_ACOUNT.png"))
	new_acc_button.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")	
	log_in_button.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")	
	label_forgot.Typeface = Typeface.LoadFromAssets("ZINGHABI.otf")
	label_email.Typeface = Typeface.LoadFromAssets("ZINGHABI.otf")
	label_password.Typeface = Typeface.LoadFromAssets("ZINGHABI.otf")
	
	ban_tools.Color = Colors.Transparent
	ban_create.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	
	label_email.Gravity = Gravity.CENTER
	label_password.Gravity = Gravity.CENTER
	label_forgot.Gravity = Gravity.CENTER
	''width
		ban_panel.Width = 100%x
		ban_picture.Width = ban_panel.Width
		ban_tools.Width = 100%x
		ban_create.Width = 100%x
		label_email.Width = 30%x
		label_password.Width = 30%x
		text_email.Width = 50%x
		text_password.Width = 50%x
		label_forgot.Width = 60%x
		log_in_button.Width = 40%x
		new_acc_button.Width = 64%x
	''height
		ban_panel.Height = 25%y
		ban_picture.Height = ban_panel.Height
		ban_create.Height = 16%y
		ban_tools.Height = Activity.Height - ban_panel.Height - ban_create.Height - 10dip
		label_email.Height = 8%y
		label_password.Height = 8%y
		text_email.Height = 8%y
		text_password.Height = 8%y
		label_forgot.Height = 6%y
		log_in_button.Height = 10%y
		new_acc_button.Height = 10%y
	''top
		ban_panel.Top = Activity.Top
		ban_picture.Top = ban_panel.Top
		ban_tools.Top = ban_panel.Height + 2dip
		ban_create.Top = ban_tools.Top + ban_tools.Height + 2%y 'ban_panel.Height + ban_tools.Height + 3dip
		label_email.Top = 10%y
		label_password.Top = 18%y
		text_email.Top = 10%y
		text_password.Top = 18%y
		label_forgot.Top = text_password.Top + text_password.Height + 3dip
		log_in_button.Top = label_forgot.Top + label_forgot.Height + 17dip
		Dim sums As Double
		sums= ban_create.Height/2
		new_acc_button.Top = sums - (sums/2)
	''left
		Dim left_sums,left As Double
		left_sums = (Activity.Width/2)
		left =  left_sums - (left_sums/2)
		ban_panel.left = 0
		ban_picture.left = ban_panel.left
		ban_create.left = 0
		ban_tools.left = 0
		label_email.left = 5%x'left/2
		label_password.left = 5%x'left/2
		text_email.left =  label_email.left + label_email.Width
		text_password.left = label_password.left + label_email.Width
		label_forgot.left = text_password.left '+ (text_password.Width/2)
		log_in_button.left = 30%x 'text_password.left + 10dip
		new_acc_button.left = 18%x
		
		Dim gradiant,log_grad As GradientDrawable
		Dim col(2) As Int
		col(0) = Colors.Red
		col(1) = Colors.LightGray
		gradiant.Initialize("TOP_BOTTOM",col)
		log_grad.Initialize("TOP_BOTTOM",col)
		gradiant.CornerRadius = 5dip
		log_grad.CornerRadius = 5dip
		'ban_create.Background = gradiant
		new_acc_button.Background = gradiant
		log_in_button.Background = log_grad
		log_in_button.Text = "LOG IN"
		new_acc_button.Text = "CREATE ACCOUNT"
End Sub

Sub new_acc_button_Click
	a2.Start(new_acc_button)
	StartActivity ("create_account")
End Sub
Sub forgot_recover_btn_click
	booleanforgotcount = 0	
	nickName_for = ""
	email_for = ""
	If email_forgot.Text == "" Or nickN_forgot.Text == "" Or newPass.Text == "" Or renewPass.Text == "" Then
		Msgbox("Error: Empty fields. Needed to fill all required fields to recover your account!","C O N F I R M A T I O N")	
	Else
		If newPass.Text.Length <= 5 Then
		Msgbox("Error: Password lenght must be 6 letter and above!","C O N F I R M A T I O N")	
		Else If newPass.Text.Contains(renewPass.Text) == False Then
		Msgbox("Error: Password did not match!","C O N F I R M A T I O N")	
		Else
			ProgressDialogShow2("please wait.!!",False)
			log_click = False
			Dim url_back As calculations
			Dim url_email,nickname As String
			url_back.Initialize
			url_email = url_back.php_email_url("index.php")
			nickname = url_back.php_email_url("search_blood_nickN.php")
			forgot_email_job.Download2(url_email,Array As String("email","SELECT email FROM `person_info` where `email`='"&email_forgot.Text&"';"))
			forgot_nick_job.Download2(nickname,Array As String("nick","SELECT nick_name FROM `person_info` where `nick_name`='"&nickN_forgot.Text&"';"))	
		End If
	End If
	
End Sub
Sub label_forgot_Click
	'booleanforgotcount = 0
	a3.Start(label_forgot)
	Dim pnl As Panel
	Dim edit_ok_btn,edit_can_btn As Button
	Dim title_lbl As Label
	title_lbl.Initialize("")
	edit_ok_btn.Initialize("forgot_recover_btn")
	edit_can_btn.Initialize("forgot_can_btn")
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
	edit_ok_btn.Text = "RECOVER"
	edit_can_btn.Text = "CANCEL"
	edit_ok_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	edit_can_btn.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	pnl.Initialize("pnl")
	forgot_pass_pnl.Initialize("forgot_pass_pnl")
	forgot_pass_pnl.Color = Colors.Transparent
	pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,"modal_bg.png"))
	title_lbl.Text = "RECOVERING ACCOUNT"
	title_lbl.Typeface = Typeface.LoadFromAssets("HipHopDemi.ttf")
	title_lbl.Gravity = Gravity.CENTER
	
	email_forgot.Initialize("")
	nickN_forgot.Initialize("")
	newPass.Initialize("")
	renewPass.Initialize("")
		email_forgot.Hint = "Enter your Email Address"
		nickN_forgot.Hint = "Enter your Nick Name"
		newPass.Hint = "Enter your New Password"
		renewPass.Hint = "Retype New Password"
		newPass.PasswordMode = True
		renewPass.PasswordMode = True
	pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)
	pnl.AddView(email_forgot,2%x,title_lbl.Top + title_lbl.Height + 1%y,68%x,7%y)
	pnl.AddView(nickN_forgot,2%x,email_forgot.Top + email_forgot.Height + 1%y,68%x,7%y)
	pnl.AddView(newPass,2%x,nickN_forgot.Top + nickN_forgot.Height + 1%y,68%x,7%y)
	pnl.AddView(renewPass,2%x,newPass.Top + newPass.Height + 1%y,68%x,7%y)
	
	pnl.AddView(edit_ok_btn,8%x,renewPass.Top+renewPass.Height+3%y,28%x,8%y)
	pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok_btn.Width+2%x,renewPass.Top+renewPass.Height+3%y,28%x,8%y)
	
	forgot_pass_pnl.AddView(pnl,13%x,(((Activity.Height/2)/2)/2),74%x,58%y)
	forgot_pass_pnl.BringToFront
	'pnl_body.Enabled = False
	'pnl_blood_body.Color = Colors.ARGB(128,128,128,.50)
	Activity.AddView(forgot_pass_pnl,0,0,100%x,100%y)	
End Sub
Sub forgot_can_btn_click
	forgot_pass_pnl.RemoveView
End Sub
Sub forgot_pass_pnl_click
	''don't delete this line
End Sub