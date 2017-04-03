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
	
	Dim calcs As calculations
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("login_form")
	''http utils initialization
	calcs.Initialize
		h_email.Initialize("email_get",Me)
		h_pass.Initialize("pass_get",Me)
		h_fullname.Initialize("full_name_get",Me)
		user_id.Initialize("user_id_get",Me)
		nick_name.Initialize("nick_name_get",Me)
	''
	all_settings_layout
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
			'Case "full_name_get"
			'	Log(job.GetString)
		'	name = job.GetString.Trim
			'Case "user_id_get"
			'	Log(job.GetString)
			'calcs.users_id = job.GetString.Trim
		'	Case "nick_name_get"
		'		Log(job.GetString)
		'	calcs.users_nick_name = job.GetString.Trim
			End Select
		''''''''''''''''''''''''''''''''''''''''''''''''''
	booleanCount = booleanCount+1
		If booleanCount = 3 Then '''''''' 1st statement
			ProgressDialogHide
			If text_email.Text == Null Or text_password.Text == Null Then ''''' 2nd statement
 						Msgbox("Error email address or password.!","Confirmation")
			Else If text_email.Text == "" And text_password.Text == "" Then
						Msgbox("Error empty field.!","Confirmation")
			Else
				If text_email.Text == Email And text_password.Text == pass Then ''''''' 3rd statement
							'Dim calc As calculations
							'calc.Initialize
							'name = calc.getting_fulln(calc.php_email_url("/bloodlifePHP/index3.php"),text_email.Text,text_password.Text)
						Msgbox("Welcome "&name,"Confirmation")
						booleanCount = 0
						StartActivity("menu_form")
				Else
						Msgbox("Error email address or password.!","Confirmation")
						booleanCount = 0
				End If ''''''''''''' 3rd statement
			End If	''''''''' 2nd statement
		Else
		
		End If	'''''''''''''''''''' 1st statement		
		'''''''''''''''''''''''''''''''''''''''''''''''''''
	Else If job.Success == False Then
	ProgressDialogHide
		If booleanCount = 3 Then
		Msgbox("Error: Error connecting to server, try again laiter.!","Confirmation")
		booleanCount = 0
		Else
		booleanCount = 3
		End If
	
	End If


		
	
End Sub
Sub log_in_button_click
ProgressDialogShow2("please wait.!!",False)
	booleanCount = 0
	Dim url_back As calculations
	Dim url_email,url_pass,full_name,id,nickname As String
	url_back.Initialize
	url_email = url_back.php_email_url("/bloodlifePHP/index.php")
	url_pass = url_back.php_email_url("/bloodlifePHP/index1.php")
	'full_name = url_back.php_email_url("/bloodlifePHP/search_blood_fullN.php")
	'id = url_back.php_email_url("/bloodlifePHP/search_blood_id.php")
	'nickname = url_back.php_email_url("/bloodlifePHP/search_blood_nickN.php")
	'h_fullname.Download2(url_pass,Array As String("full_name","SELECT full_name FROM `bloodlife_db`.`person_info` where `email`='"&text_email.Text&"';"))
	'user_id.Download2(url_pass,Array As String("id","SELECT id FROM `bloodlife_db`.`person_info` where `email`='"&text_email.Text&"';"))
	'nick_name.Download2(url_pass,Array As String("nick","SELECT nick_name FROM `bloodlife_db`.`person_info` where `email`='"&text_email.Text&"';"))
	
	h_email.Download2(url_email,Array As String("email","SELECT email FROM `bloodlife_db`.`person_info` where `email`='"&text_email.Text&"';"))
	h_pass.Download2(url_pass,Array As String("pass","SELECT decode(password,'goroy') FROM `bloodlife_db`.`person_info` where `email`='"&text_email.Text&"';"))
	
End Sub
Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Public Sub all_settings_layout
	Activity.SetBackgroundImage(LoadBitmap(File.DirAssets,"bg.jpg"))
	ban_picture.SetBackgroundImage(LoadBitmap(File.DirAssets,"banner01.jpg"))
	log_in_button.SetBackgroundImage(LoadBitmap(File.DirAssets,"LOG_IN.png"))
	new_acc_button.SetBackgroundImage(LoadBitmap(File.DirAssets,"CREATE_ACOUNT.png"))
	
	ban_tools.Color = Colors.Transparent
	ban_create.Color = Colors.Transparent
	''width
		ban_panel.Width = 100%x
		ban_picture.Width = ban_panel.Width
		ban_tools.Width = 100%x
		ban_create.Width = 100%x
		label_email.Width = 20%x
		label_password.Width = 20%x
		text_email.Width = 45%x
		text_password.Width = 45%x
		label_forgot.Width = 30%x
		log_in_button.Width = 30%x
		new_acc_button.Width = 35%x
	''height
		ban_panel.Height = 25%y
		ban_picture.Height = ban_panel.Height - 3dip
		ban_create.Height = 16%y
		ban_tools.Height = Activity.Height - ban_panel.Height - ban_create.Height - 10dip
		label_email.Height = 6%y
		label_password.Height = 6%y
		text_email.Height = 6%y
		text_password.Height = 6%y
		label_forgot.Height = 6%y
		log_in_button.Height = 10%y
		new_acc_button.Height = 10%y
	''top
		ban_panel.Top = Activity.Top
		ban_picture.Top = ban_panel.Top + 3dip
		ban_tools.Top = ban_panel.Height + 2dip
		ban_create.Top = ban_panel.Height + ban_tools.Height + 3dip
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
		label_email.left = left/2
		label_password.left = left/2
		text_email.left =  label_email.left + label_email.Width
		text_password.left = label_password.left + label_email.Width
		label_forgot.left = text_password.left + (text_password.Width/2)
		log_in_button.left = text_password.left + 10dip
		new_acc_button.left = 5%x
End Sub

Sub new_acc_button_Click
	StartActivity ("create_account")
End Sub