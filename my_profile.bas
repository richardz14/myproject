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
Activity.LoadLayout ("my_profile")
Dim Panel1 As Panel
Dim ImageView1 As ImageView
Dim ImageView3 As ImageView
Dim Label1 As Label
Dim Button1 As Button
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("my_profile")
	
	Dim Panel1 As Panel
	
	Panel1.Width = Activity.Width
	Panel1.Height = Activity.Height
	Panel1.Color = Colors.Red
	
	

End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub Button1_Click
StartActivity ("menu_frame")
	
End Sub