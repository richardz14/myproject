Type=Class
Version=5.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'Class module
Sub Class_Globals
	Public scroll_height As Double	
	Public scroll_width As Double
	Public scroll_top As Double
	Public scroll_left As Double
	Public name As String
	Private ip As String
	ip = "https://bloodlifegoroy.000webhostapp.com/bloodlifePHP/"	'For domain
	'ip = "http://192.168.43.2:80/bloodlifePHP/" 'for localhost
	Public users_id As String : users_id = 1
	Public users_nick_name As String
	'Private full_name As HttpJob
	'Private name As String
End Sub

'Initializes the object. You can add parameters to this method if needed.
Public Sub Initialize
	'full_name.Initialize("get_full_name",Me)
End Sub
'Public Sub getting_fulln(url As String,email As String,pass As String) As String
	'	full_name.Download2(url,Array As String("full_name","SELECT full_name FROM `person_info` where `email`='"&email&"' and password='"&pass&"';"))
'	Return name
'End Sub
'Public Sub jobDone(job As HttpJob)
'	If job.Success Then
'		Select job.JobName
'			Case "get_full_name"
'			Log(job.GetString.Trim)
'			name = job.GetString.Trim
'		End Select
'	End If
'End Sub
Public Sub sums_height(h As Double) As Double
		scroll_height = h
	Return scroll_height
End Sub
Public Sub sums_width(w As Double) As Double
		scroll_width = w
	Return scroll_width
End Sub	
Public Sub sums_top(t As Double) As Double
		scroll_top = t
	Return scroll_top
End Sub	
Public Sub sums_left(l As Double) As Double
		scroll_left = l
	Return scroll_left
End Sub	

Public Sub php_email_url(email As String) As String
	Dim merge_url As String
	merge_url = ip&email
	Return merge_url
End Sub
Public Sub name_user(n As String) As String
	
Return n
End Sub