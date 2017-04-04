Type=Activity
Version=5.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

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
End Sub
Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("menu_form")
	Activity.LoadLayout ("menu_frame")
	load_activity_layout
End Sub
Sub load_activity_layout
	Dim text_temp As calculations
	
	text_temp.Initialize
	'Log("name: "&login_form.name_query)
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
	users_lbl.Left = users_out_lbl.Top
	
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
	
End Sub
Sub about_Click
	
End Sub
Sub help_Click
	
End Sub
Sub exit_btn_Click
	
End Sub