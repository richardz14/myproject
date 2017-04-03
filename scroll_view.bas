Type=Class
Version=3
@EndOfDesignText@
'Class module
Sub Class_Globals
	Dim ScrollView1 As ScrollView
	Dim btnTransform As Button
	Dim lbl, lbl2 As Label
End Sub

'Initializes the object. You can add parameters to this method if needed.

	ScrollView1.Initialize(960dip, 600dip, "sv2d")
	ScrollView1.Panel.SetBackgroundImage(LoadBitmap(File.DirAssets, "florian.jpg"))
	lbl2.Initialize("")
	lbl2.TextSize = 30
	lbl2.TextColor = Colors.White
	lbl2.Text = "SCROLLVIEW2D"
	ScrollView1.Panel.AddView(lbl2, 32dip, 32dip, 300dip, 50dip)
	lbl.Initialize("")
	lbl.TextSize = 30
	lbl.TextColor = Colors.Red
	lbl.Text = lbl2.Text
	ScrollView1.Panel.AddView(lbl, 30dip, 30dip, 300dip, 50dip)
	For i = 0 To 4
		Dim edt As EditText: edt.Initialize("")
		edt.Text = "SCROLLVIEW2D"
		edt.SingleLine = True
		ScrollView1.Panel.AddView(edt, 170dip * i + 80dip, 120dip, 150dip, 60dip)
		Dim edt2 As EditText: edt2.Initialize("")
		edt2.Text = "SCROLLVIEW2D"
		edt2.SingleLine = True
		ScrollView1.Panel.AddView(edt2, 170dip * i + 80dip, 500dip, 150dip, 60dip)
	Next
	Activity.AddView(ScrollView1, 0, 0, 100%x, 50%y)

	Dim btnFSH As Button: btnFSH.Initialize("btnFSH")
	btnFSH.Text = "Horizontal FullScroll"
	btnFSH.TextSize = 16
	Activity.AddView(btnFSH, 0, ScrollView1.height, 100dip, 65dip)
	Dim btnFSV As Button: btnFSV.Initialize("btnFSV")
	btnFSV.Text = "Vertical FullScroll"
	btnFSV.TextSize = 16
	Activity.AddView(btnFSV, 110dip, ScrollView1.height, 100dip, 65dip)
	btnTransform.Initialize("btnTransform")
	btnTransform.Text = "Transform in SV"
	btnTransform.TextSize = 16
	Activity.AddView(btnTransform, 220dip, ScrollView1.height, 100dip, 65dip)
End Sub

Sub btnFSH_Click
	If ScrollView1.HorizontalScrollPosition > 0 Then
		ScrollView1.FullScroll(ScrollView1.FS_DIR_HORZ, False, True)
	Else
		ScrollView1.FullScroll(ScrollView1.FS_DIR_HORZ, ScrollView1.FS_ToTheEND, True)
	End If
	ScrollView1.GiveFocusToFirstVisible
End Sub

Sub btnFSV_Click
	If ScrollView1.VerticalScrollPosition > 0 Then
		ScrollView1.FullScroll(ScrollView1.FS_DIR_VERT, False, True)
	Else
		ScrollView1.FullScroll(ScrollView1.FS_DIR_VERT, ScrollView1.FS_ToTheEND, True)
	End If
	ScrollView1.GiveFocusToFirstVisible
End Sub

Sub btnTransform_Click
	If btnTransform.Text = "Transform in SV" Then
		lbl.Text = "Vertical ScrollView"
		ScrollView1.Panel.Width = sv2d.Width
		btnTransform.Text = "Transform in H_SV"
	Else If btnTransform.Text = "Transform in H_SV" Then
		lbl.Text = "Horizontal ScrollView"
		ScrollView1.Panel.Width = 960dip
		ScrollView1.Panel.Height = sv2d.Height
		btnTransform.Text = "Transform in SV_2D"
	Else
		lbl.Text = "ScrollView 2D"
		ScrollView1.Panel.Height = 600dip
		btnTransform.Text = "Transform in SV"
	End If
	lbl2.Text = lbl.Text
End Sub

Sub Activity_Resume
End Sub

Sub Activity_Pause (UserClosed As Boolean)
End Sub

Sub sv2d_ScrollChanged(PosX As Int, PosY As Int)
	Log(PosX & " / " & PosY)	
End Sub