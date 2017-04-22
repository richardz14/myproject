package b4a.example;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class menu_form extends Activity implements B4AActivity{
	public static menu_form mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.menu_form");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (menu_form).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.menu_form");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.menu_form", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (menu_form) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (menu_form) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return menu_form.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (menu_form) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (menu_form) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.collections.List _list_all_info = null;
public static anywheresoftware.b4a.objects.collections.List _list_bloodgroup = null;
public static anywheresoftware.b4a.objects.collections.List _list_donated = null;
public static anywheresoftware.b4a.objects.collections.List _list_day = null;
public static anywheresoftware.b4a.objects.collections.List _list_month = null;
public static anywheresoftware.b4a.objects.collections.List _list_year = null;
public static String _users_string_login = "";
public static String _blood_selected = "";
public static String _bday_day_selected = "";
public static String _bday_month_selected = "";
public static String _bday_year_selected = "";
public static String _location_brgy_selected = "";
public static String _location_street_selected = "";
public static String _is_donated = "";
public static String _lat = "";
public static String _lng = "";
public static int _brgy_index = 0;
public static int _street_index = 0;
public static anywheresoftware.b4a.objects.collections.List _list_location_b = null;
public static anywheresoftware.b4a.objects.collections.List _list_location_s = null;
public static anywheresoftware.b4a.objects.collections.List _list_location_p = null;
public static String _optionselected = "";
public static String _image_container = "";
public anywheresoftware.b4a.objects.SpinnerWrapper _location_spin_street = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _location_spin_brgy = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_bloodgroup = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_day = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_month = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_year = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_donated = null;
public b4a.example.clsexplorer _dlgfileexpl = null;
public anywheresoftware.b4a.objects.ButtonWrapper _search_blood = null;
public anywheresoftware.b4a.objects.ButtonWrapper _about = null;
public anywheresoftware.b4a.objects.ButtonWrapper _help = null;
public anywheresoftware.b4a.objects.ButtonWrapper _exit_btn = null;
public anywheresoftware.b4a.objects.ButtonWrapper _profile = null;
public anywheresoftware.b4a.objects.PanelWrapper _src_blood_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _users_panel = null;
public anywheresoftware.b4a.objects.PanelWrapper _profile_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _about_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _help_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _exit_pnl = null;
public anywheresoftware.b4a.objects.LabelWrapper _users_out_lbl = null;
public anywheresoftware.b4a.objects.LabelWrapper _users_lbl = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ban_logo = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ban_picture = null;
public anywheresoftware.b4a.objects.PanelWrapper _users_heading = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _srch_blood_img = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _profile_img = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _about_img = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _help_img = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _exit_img = null;
public anywheresoftware.b4a.objects.PanelWrapper _profile_panel = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scroll_profile_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _profile_all_body = null;
public anywheresoftware.b4a.objects.PanelWrapper _all_inputs = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnl_body = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnl_blood_body = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnl_bday_body = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnl_donated_body = null;
public b4a.example.httpjob _all_info_query = null;
public b4a.example.httpjob _update_job = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_fullname = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_bloodgroup = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_email = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_phonenumber = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_phonenumber2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_location = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_question = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_donate_confirm = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_bday = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_fn = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_blood = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_email = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_phonenumber = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_phonenumber2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_bday = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_location = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_answer = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_donated = null;
public anywheresoftware.b4a.objects.ButtonWrapper _cancel_btn = null;
public anywheresoftware.b4a.objects.ButtonWrapper _update_btn = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _usr_img = null;
public b4a.example.main _main = null;
public b4a.example.login_form _login_form = null;
public b4a.example.create_account _create_account = null;
public b4a.example.search_frame _search_frame = null;
public b4a.example.httputils2service _httputils2service = null;
public b4a.example.my_profile _my_profile = null;
public b4a.example.about_frame _about_frame = null;
public b4a.example.help_frame _help_frame = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _about_click() throws Exception{
 //BA.debugLineNum = 489;BA.debugLine="Sub about_Click";
 //BA.debugLineNum = 491;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 102;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 105;BA.debugLine="Activity.LoadLayout (\"menu_frame\")";
mostCurrent._activity.LoadLayout("menu_frame",mostCurrent.activityBA);
 //BA.debugLineNum = 106;BA.debugLine="load_activity_layout";
_load_activity_layout();
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 349;BA.debugLine="Sub Activity_KeyPress(KeyCode As Int) As Boolean";
 //BA.debugLineNum = 351;BA.debugLine="If dlgFileExpl.IsInitialized Then";
if (mostCurrent._dlgfileexpl.IsInitialized()) { 
 //BA.debugLineNum = 352;BA.debugLine="If dlgFileExpl.IsActive Then Return True";
if (mostCurrent._dlgfileexpl._isactive()) { 
if (true) return anywheresoftware.b4a.keywords.Common.True;};
 };
 //BA.debugLineNum = 354;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 242;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 244;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 238;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 240;BA.debugLine="End Sub";
return "";
}
public static String  _all_input_on_list() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_phone2 = null;
String _line_phone2 = "";
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _inp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
anywheresoftware.b4a.objects.StringUtils _su = null;
byte[] _bytes = null;
 //BA.debugLineNum = 307;BA.debugLine="Sub all_input_on_list";
 //BA.debugLineNum = 308;BA.debugLine="list_all_info.Initialize";
_list_all_info.Initialize();
 //BA.debugLineNum = 309;BA.debugLine="Dim TextReader_phone2 As TextReader";
_textreader_phone2 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 310;BA.debugLine="TextReader_phone2.Initialize(File.OpenInput(Fi";
_textreader_phone2.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"users_all_info.txt").getObject()));
 //BA.debugLineNum = 311;BA.debugLine="Dim line_phone2 As String";
_line_phone2 = "";
 //BA.debugLineNum = 312;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 //BA.debugLineNum = 313;BA.debugLine="Do While line_phone2 <> Null";
while (_line_phone2!= null) {
 //BA.debugLineNum = 314;BA.debugLine="list_all_info.Add(line_phone2)";
_list_all_info.Add((Object)(_line_phone2));
 //BA.debugLineNum = 315;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 }
;
 //BA.debugLineNum = 317;BA.debugLine="TextReader_phone2.Close";
_textreader_phone2.Close();
 //BA.debugLineNum = 319;BA.debugLine="text_fn.Text = list_all_info.Get(0)";
mostCurrent._text_fn.setText(_list_all_info.Get((int) (0)));
 //BA.debugLineNum = 320;BA.debugLine="text_blood.Text = list_all_info.Get(1)";
mostCurrent._text_blood.setText(_list_all_info.Get((int) (1)));
 //BA.debugLineNum = 321;BA.debugLine="blood_selected = list_all_info.Get(1)";
_blood_selected = BA.ObjectToString(_list_all_info.Get((int) (1)));
 //BA.debugLineNum = 322;BA.debugLine="text_email.Text = list_all_info.Get(2)";
mostCurrent._text_email.setText(_list_all_info.Get((int) (2)));
 //BA.debugLineNum = 323;BA.debugLine="text_phonenumber.Text = list_all_info.Get(3)";
mostCurrent._text_phonenumber.setText(_list_all_info.Get((int) (3)));
 //BA.debugLineNum = 324;BA.debugLine="text_phonenumber2.Text = list_all_info.Get(4)";
mostCurrent._text_phonenumber2.setText(_list_all_info.Get((int) (4)));
 //BA.debugLineNum = 325;BA.debugLine="text_bday.Text = list_all_info.Get(5)&\"/\"&list_al";
mostCurrent._text_bday.setText((Object)(BA.ObjectToString(_list_all_info.Get((int) (5)))+"/"+BA.ObjectToString(_list_all_info.Get((int) (6)))+"/"+BA.ObjectToString(_list_all_info.Get((int) (7)))));
 //BA.debugLineNum = 326;BA.debugLine="bday_month_selected = list_all_info.Get(5)";
_bday_month_selected = BA.ObjectToString(_list_all_info.Get((int) (5)));
 //BA.debugLineNum = 327;BA.debugLine="bday_day_selected = list_all_info.Get(6)";
_bday_day_selected = BA.ObjectToString(_list_all_info.Get((int) (6)));
 //BA.debugLineNum = 328;BA.debugLine="bday_year_selected = list_all_info.Get(7)";
_bday_year_selected = BA.ObjectToString(_list_all_info.Get((int) (7)));
 //BA.debugLineNum = 329;BA.debugLine="text_location.Text = list_all_info.Get(8)&\", \"&li";
mostCurrent._text_location.setText((Object)(BA.ObjectToString(_list_all_info.Get((int) (8)))+", "+BA.ObjectToString(_list_all_info.Get((int) (9)))));
 //BA.debugLineNum = 330;BA.debugLine="location_brgy_selected = list_all_info.Get(8)";
_location_brgy_selected = BA.ObjectToString(_list_all_info.Get((int) (8)));
 //BA.debugLineNum = 331;BA.debugLine="location_street_selected = list_all_info.Get(";
_location_street_selected = BA.ObjectToString(_list_all_info.Get((int) (9)));
 //BA.debugLineNum = 332;BA.debugLine="text_answer.Text = list_all_info.Get(10)";
mostCurrent._text_answer.setText(_list_all_info.Get((int) (10)));
 //BA.debugLineNum = 333;BA.debugLine="text_donated.Text = list_all_info.Get(11)";
mostCurrent._text_donated.setText(_list_all_info.Get((int) (11)));
 //BA.debugLineNum = 334;BA.debugLine="is_donated = list_all_info.Get(11)";
_is_donated = BA.ObjectToString(_list_all_info.Get((int) (11)));
 //BA.debugLineNum = 336;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 337;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 338;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 339;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 340;BA.debugLine="bytes = su.DecodeBase64(list_all_info.Get(12))";
_bytes = _su.DecodeBase64(BA.ObjectToString(_list_all_info.Get((int) (12))));
 //BA.debugLineNum = 341;BA.debugLine="image_container = list_all_info.Get(12)";
_image_container = BA.ObjectToString(_list_all_info.Get((int) (12)));
 //BA.debugLineNum = 342;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 343;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 344;BA.debugLine="usr_img.SetBackgroundImage(bmp)";
mostCurrent._usr_img.SetBackgroundImage((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 347;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 348;BA.debugLine="End Sub";
return "";
}
public static String  _all_inputs_click() throws Exception{
 //BA.debugLineNum = 811;BA.debugLine="Sub all_inputs_click";
 //BA.debugLineNum = 813;BA.debugLine="End Sub";
return "";
}
public static String  _bday_edit_click() throws Exception{
int _i = 0;
int _ii = 0;
int _iii = 0;
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_ok_btn = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 580;BA.debugLine="Sub bday_edit_Click";
 //BA.debugLineNum = 581;BA.debugLine="list_day.Initialize";
_list_day.Initialize();
 //BA.debugLineNum = 582;BA.debugLine="list_month.Initialize";
_list_month.Initialize();
 //BA.debugLineNum = 583;BA.debugLine="list_year.Initialize";
_list_year.Initialize();
 //BA.debugLineNum = 584;BA.debugLine="spin_day.Initialize(\"spin_day\")";
mostCurrent._spin_day.Initialize(mostCurrent.activityBA,"spin_day");
 //BA.debugLineNum = 585;BA.debugLine="spin_month.Initialize(\"spin_month\")";
mostCurrent._spin_month.Initialize(mostCurrent.activityBA,"spin_month");
 //BA.debugLineNum = 586;BA.debugLine="spin_year.Initialize(\"spin_year\")";
mostCurrent._spin_year.Initialize(mostCurrent.activityBA,"spin_year");
 //BA.debugLineNum = 587;BA.debugLine="For i = 1 To 31";
{
final int step7 = 1;
final int limit7 = (int) (31);
for (_i = (int) (1) ; (step7 > 0 && _i <= limit7) || (step7 < 0 && _i >= limit7); _i = ((int)(0 + _i + step7)) ) {
 //BA.debugLineNum = 588;BA.debugLine="list_day.Add(i)";
_list_day.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 590;BA.debugLine="For ii = 1940 To 2017";
{
final int step10 = 1;
final int limit10 = (int) (2017);
for (_ii = (int) (1940) ; (step10 > 0 && _ii <= limit10) || (step10 < 0 && _ii >= limit10); _ii = ((int)(0 + _ii + step10)) ) {
 //BA.debugLineNum = 591;BA.debugLine="list_year.Add(ii)";
_list_year.Add((Object)(_ii));
 }
};
 //BA.debugLineNum = 593;BA.debugLine="For iii = 1 To 12";
{
final int step13 = 1;
final int limit13 = (int) (12);
for (_iii = (int) (1) ; (step13 > 0 && _iii <= limit13) || (step13 < 0 && _iii >= limit13); _iii = ((int)(0 + _iii + step13)) ) {
 //BA.debugLineNum = 594;BA.debugLine="list_month.Add(iii)";
_list_month.Add((Object)(_iii));
 }
};
 //BA.debugLineNum = 596;BA.debugLine="spin_day.AddAll(list_day)";
mostCurrent._spin_day.AddAll(_list_day);
 //BA.debugLineNum = 597;BA.debugLine="spin_month.AddAll(list_month)";
mostCurrent._spin_month.AddAll(_list_month);
 //BA.debugLineNum = 598;BA.debugLine="spin_year.AddAll(list_year)";
mostCurrent._spin_year.AddAll(_list_year);
 //BA.debugLineNum = 599;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 600;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 601;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 602;BA.debugLine="edit_ok_btn.Initialize(\"edit_bday_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_bday_ok_btn");
 //BA.debugLineNum = 603;BA.debugLine="edit_can_btn.Initialize(\"edit_bday_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_bday_can_btn");
 //BA.debugLineNum = 604;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 605;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 606;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 607;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 608;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 609;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 610;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 611;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 612;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 613;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 614;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 615;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 616;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 617;BA.debugLine="title_lbl.Text = \"SELECT BIRTH DATE\"";
_title_lbl.setText((Object)("SELECT BIRTH DATE"));
 //BA.debugLineNum = 618;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 619;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 620;BA.debugLine="pnl_bday_body.Initialize(\"pnl_bday_body\")";
mostCurrent._pnl_bday_body.Initialize(mostCurrent.activityBA,"pnl_bday_body");
 //BA.debugLineNum = 621;BA.debugLine="pnl_bday_body.Color = Colors.Transparent";
mostCurrent._pnl_bday_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 622;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 623;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 624;BA.debugLine="pnl.AddView(spin_day,2%x,title_lbl.Top + title_lb";
_pnl.AddView((android.view.View)(mostCurrent._spin_day.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 625;BA.debugLine="pnl.AddView(spin_month,spin_day.Left+spin_day.Wid";
_pnl.AddView((android.view.View)(mostCurrent._spin_month.getObject()),(int) (mostCurrent._spin_day.getLeft()+mostCurrent._spin_day.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_day.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 626;BA.debugLine="pnl.AddView(spin_year,spin_month.Left+spin_month.";
_pnl.AddView((android.view.View)(mostCurrent._spin_year.getObject()),(int) (mostCurrent._spin_month.getLeft()+mostCurrent._spin_month.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_month.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 627;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_year.Top+spin_y";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 628;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 630;BA.debugLine="pnl_bday_body.AddView(pnl,13%x,((Activity.Height/";
mostCurrent._pnl_bday_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 631;BA.debugLine="pnl_bday_body.BringToFront";
mostCurrent._pnl_bday_body.BringToFront();
 //BA.debugLineNum = 634;BA.debugLine="Activity.AddView(pnl_bday_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_bday_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 635;BA.debugLine="End Sub";
return "";
}
public static String  _blood_edit_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_ok_btn = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 659;BA.debugLine="Sub blood_edit_Click";
 //BA.debugLineNum = 661;BA.debugLine="list_bloodgroup.Initialize";
_list_bloodgroup.Initialize();
 //BA.debugLineNum = 662;BA.debugLine="spin_bloodgroup.Initialize(\"spin_bloodgroup\")";
mostCurrent._spin_bloodgroup.Initialize(mostCurrent.activityBA,"spin_bloodgroup");
 //BA.debugLineNum = 663;BA.debugLine="list_bloodgroup.Add(\"A\")";
_list_bloodgroup.Add((Object)("A"));
 //BA.debugLineNum = 664;BA.debugLine="list_bloodgroup.Add(\"B\")";
_list_bloodgroup.Add((Object)("B"));
 //BA.debugLineNum = 665;BA.debugLine="list_bloodgroup.Add(\"O\")";
_list_bloodgroup.Add((Object)("O"));
 //BA.debugLineNum = 666;BA.debugLine="list_bloodgroup.Add(\"AB\")";
_list_bloodgroup.Add((Object)("AB"));
 //BA.debugLineNum = 667;BA.debugLine="list_bloodgroup.Add(\"A+\")";
_list_bloodgroup.Add((Object)("A+"));
 //BA.debugLineNum = 668;BA.debugLine="list_bloodgroup.Add(\"B+\")";
_list_bloodgroup.Add((Object)("B+"));
 //BA.debugLineNum = 669;BA.debugLine="list_bloodgroup.Add(\"O+\")";
_list_bloodgroup.Add((Object)("O+"));
 //BA.debugLineNum = 670;BA.debugLine="list_bloodgroup.Add(\"AB+\")";
_list_bloodgroup.Add((Object)("AB+"));
 //BA.debugLineNum = 671;BA.debugLine="list_bloodgroup.Add(\"A-\")";
_list_bloodgroup.Add((Object)("A-"));
 //BA.debugLineNum = 672;BA.debugLine="list_bloodgroup.Add(\"B-\")";
_list_bloodgroup.Add((Object)("B-"));
 //BA.debugLineNum = 673;BA.debugLine="list_bloodgroup.Add(\"O-\")";
_list_bloodgroup.Add((Object)("O-"));
 //BA.debugLineNum = 674;BA.debugLine="list_bloodgroup.Add(\"AB-\")";
_list_bloodgroup.Add((Object)("AB-"));
 //BA.debugLineNum = 675;BA.debugLine="spin_bloodgroup.AddAll(list_bloodgroup)";
mostCurrent._spin_bloodgroup.AddAll(_list_bloodgroup);
 //BA.debugLineNum = 676;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 677;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 678;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 679;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 680;BA.debugLine="edit_ok_btn.Initialize(\"edit_blood_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_blood_ok_btn");
 //BA.debugLineNum = 681;BA.debugLine="edit_can_btn.Initialize(\"edit_blood_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_blood_can_btn");
 //BA.debugLineNum = 682;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 683;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 684;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 685;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 686;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 687;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 688;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 689;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 690;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 691;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 692;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 693;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 694;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 695;BA.debugLine="pnl_blood_body.Initialize(\"pnl_blood_body\")";
mostCurrent._pnl_blood_body.Initialize(mostCurrent.activityBA,"pnl_blood_body");
 //BA.debugLineNum = 696;BA.debugLine="pnl_blood_body.Color = Colors.Transparent";
mostCurrent._pnl_blood_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 697;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 698;BA.debugLine="title_lbl.Text = \"SELECT BLOOD TYPE\"";
_title_lbl.setText((Object)("SELECT BLOOD TYPE"));
 //BA.debugLineNum = 699;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 700;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 701;BA.debugLine="pnl.AddView(spin_bloodgroup,2%x,title_lbl.Top + t";
_pnl.AddView((android.view.View)(mostCurrent._spin_bloodgroup.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 702;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_bloodgroup.Top+";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_bloodgroup.getTop()+mostCurrent._spin_bloodgroup.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 703;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_bloodgroup.getTop()+mostCurrent._spin_bloodgroup.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 705;BA.debugLine="pnl_blood_body.AddView(pnl,13%x,((Activity.Height";
mostCurrent._pnl_blood_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 706;BA.debugLine="pnl_blood_body.BringToFront";
mostCurrent._pnl_blood_body.BringToFront();
 //BA.debugLineNum = 709;BA.debugLine="Activity.AddView(pnl_blood_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_blood_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 710;BA.debugLine="End Sub";
return "";
}
public static String  _cancel_btn_click() throws Exception{
 //BA.debugLineNum = 499;BA.debugLine="Sub cancel_btn_Click";
 //BA.debugLineNum = 500;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 501;BA.debugLine="End Sub";
return "";
}
public static String  _donated_edit_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_ok_btn = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 526;BA.debugLine="Sub donated_edit_Click";
 //BA.debugLineNum = 527;BA.debugLine="list_donated.Initialize";
_list_donated.Initialize();
 //BA.debugLineNum = 528;BA.debugLine="spin_donated.Initialize(\"spin_donated\")";
mostCurrent._spin_donated.Initialize(mostCurrent.activityBA,"spin_donated");
 //BA.debugLineNum = 529;BA.debugLine="list_donated.Add(\"YES\")";
_list_donated.Add((Object)("YES"));
 //BA.debugLineNum = 530;BA.debugLine="list_donated.Add(\"NO\")";
_list_donated.Add((Object)("NO"));
 //BA.debugLineNum = 531;BA.debugLine="spin_donated.AddAll(list_donated)";
mostCurrent._spin_donated.AddAll(_list_donated);
 //BA.debugLineNum = 532;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 533;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 534;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 535;BA.debugLine="edit_ok_btn.Initialize(\"edit_donated_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_donated_ok_btn");
 //BA.debugLineNum = 536;BA.debugLine="edit_can_btn.Initialize(\"edit_donated_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_donated_can_btn");
 //BA.debugLineNum = 537;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 538;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 539;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 540;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 541;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 542;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 543;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 544;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 545;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 546;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 547;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 548;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 549;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 550;BA.debugLine="title_lbl.Text = \"SELECT DONATED STATUS\"";
_title_lbl.setText((Object)("SELECT DONATED STATUS"));
 //BA.debugLineNum = 551;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 552;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 553;BA.debugLine="pnl_donated_body.Initialize(\"pnl_donated_body\")";
mostCurrent._pnl_donated_body.Initialize(mostCurrent.activityBA,"pnl_donated_body");
 //BA.debugLineNum = 554;BA.debugLine="pnl_donated_body.Color = Colors.Transparent";
mostCurrent._pnl_donated_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 555;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 556;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 557;BA.debugLine="pnl.AddView(spin_donated,2%x,title_lbl.Top+title_";
_pnl.AddView((android.view.View)(mostCurrent._spin_donated.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 558;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_donated.Top+spi";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_donated.getTop()+mostCurrent._spin_donated.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 559;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_donated.getTop()+mostCurrent._spin_donated.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 561;BA.debugLine="pnl_donated_body.AddView(pnl,13%x,((Activity.Heig";
mostCurrent._pnl_donated_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 562;BA.debugLine="pnl_donated_body.BringToFront";
mostCurrent._pnl_donated_body.BringToFront();
 //BA.debugLineNum = 565;BA.debugLine="Activity.AddView(pnl_donated_body,0,0,100%x,100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_donated_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 566;BA.debugLine="End Sub";
return "";
}
public static String  _edit_bday_can_btn_click() throws Exception{
 //BA.debugLineNum = 653;BA.debugLine="Sub edit_bday_can_btn_click";
 //BA.debugLineNum = 654;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 655;BA.debugLine="End Sub";
return "";
}
public static String  _edit_bday_ok_btn_click() throws Exception{
 //BA.debugLineNum = 648;BA.debugLine="Sub edit_bday_ok_btn_click";
 //BA.debugLineNum = 649;BA.debugLine="text_bday.Text = bday_month_selected&\"/\"&bday_day";
mostCurrent._text_bday.setText((Object)(_bday_month_selected+"/"+_bday_day_selected+"/"+_bday_year_selected));
 //BA.debugLineNum = 650;BA.debugLine="Log(\"date: \"& bday_month_selected&\"/\"&bday_day_s";
anywheresoftware.b4a.keywords.Common.Log("date: "+_bday_month_selected+"/"+_bday_day_selected+"/"+_bday_year_selected);
 //BA.debugLineNum = 651;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 652;BA.debugLine="End Sub";
return "";
}
public static String  _edit_blood_can_btn_click() throws Exception{
 //BA.debugLineNum = 715;BA.debugLine="Sub edit_blood_can_btn_click";
 //BA.debugLineNum = 716;BA.debugLine="pnl_blood_body.RemoveView";
mostCurrent._pnl_blood_body.RemoveView();
 //BA.debugLineNum = 717;BA.debugLine="End Sub";
return "";
}
public static String  _edit_blood_ok_btn_click() throws Exception{
 //BA.debugLineNum = 711;BA.debugLine="Sub edit_blood_ok_btn_click";
 //BA.debugLineNum = 712;BA.debugLine="text_blood.Text = blood_selected";
mostCurrent._text_blood.setText((Object)(_blood_selected));
 //BA.debugLineNum = 713;BA.debugLine="pnl_blood_body.RemoveView";
mostCurrent._pnl_blood_body.RemoveView();
 //BA.debugLineNum = 714;BA.debugLine="End Sub";
return "";
}
public static String  _edit_can_btn_click() throws Exception{
 //BA.debugLineNum = 797;BA.debugLine="Sub edit_can_btn_click";
 //BA.debugLineNum = 798;BA.debugLine="pnl_body.RemoveView";
mostCurrent._pnl_body.RemoveView();
 //BA.debugLineNum = 799;BA.debugLine="End Sub";
return "";
}
public static String  _edit_donated_can_btn_click() throws Exception{
 //BA.debugLineNum = 574;BA.debugLine="Sub edit_donated_can_btn_click";
 //BA.debugLineNum = 575;BA.debugLine="pnl_donated_body.RemoveView";
mostCurrent._pnl_donated_body.RemoveView();
 //BA.debugLineNum = 576;BA.debugLine="End Sub";
return "";
}
public static String  _edit_donated_ok_btn_click() throws Exception{
 //BA.debugLineNum = 570;BA.debugLine="Sub edit_donated_ok_btn_click";
 //BA.debugLineNum = 571;BA.debugLine="text_donated.Text = is_donated";
mostCurrent._text_donated.setText((Object)(_is_donated));
 //BA.debugLineNum = 572;BA.debugLine="pnl_donated_body.RemoveView";
mostCurrent._pnl_donated_body.RemoveView();
 //BA.debugLineNum = 573;BA.debugLine="End Sub";
return "";
}
public static String  _edit_ok_btn_click() throws Exception{
String _click_string = "";
 //BA.debugLineNum = 800;BA.debugLine="Sub edit_ok_btn_click";
 //BA.debugLineNum = 801;BA.debugLine="Dim click_string As String";
_click_string = "";
 //BA.debugLineNum = 802;BA.debugLine="click_string = location_spin_brgy.GetItem(brgy_in";
_click_string = mostCurrent._location_spin_brgy.GetItem(_brgy_index)+", "+mostCurrent._location_spin_street.GetItem(_street_index);
 //BA.debugLineNum = 803;BA.debugLine="location_brgy_selected = location_spin_brgy.GetIt";
_location_brgy_selected = mostCurrent._location_spin_brgy.GetItem(_brgy_index);
 //BA.debugLineNum = 804;BA.debugLine="location_street_selected = location_spin_stree";
_location_street_selected = mostCurrent._location_spin_street.GetItem(_street_index);
 //BA.debugLineNum = 805;BA.debugLine="text_location.Text = click_string";
mostCurrent._text_location.setText((Object)(_click_string));
 //BA.debugLineNum = 806;BA.debugLine="pnl_body.RemoveView";
mostCurrent._pnl_body.RemoveView();
 //BA.debugLineNum = 807;BA.debugLine="End Sub";
return "";
}
public static String  _exit_btn_click() throws Exception{
 //BA.debugLineNum = 495;BA.debugLine="Sub exit_btn_Click";
 //BA.debugLineNum = 497;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 32;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 35;BA.debugLine="Dim location_spin_street As Spinner";
mostCurrent._location_spin_street = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Dim location_spin_brgy As Spinner";
mostCurrent._location_spin_brgy = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim spin_bloodgroup As Spinner";
mostCurrent._spin_bloodgroup = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim spin_day,spin_month,spin_year As Spinner";
mostCurrent._spin_day = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spin_month = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spin_year = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim spin_donated As Spinner";
mostCurrent._spin_donated = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Dim dlgFileExpl As ClsExplorer";
mostCurrent._dlgfileexpl = new b4a.example.clsexplorer();
 //BA.debugLineNum = 43;BA.debugLine="Private search_blood As Button";
mostCurrent._search_blood = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private about As Button";
mostCurrent._about = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private help As Button";
mostCurrent._help = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private exit_btn As Button";
mostCurrent._exit_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private profile As Button";
mostCurrent._profile = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private src_blood_pnl As Panel";
mostCurrent._src_blood_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private users_panel As Panel";
mostCurrent._users_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private profile_pnl As Panel";
mostCurrent._profile_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private about_pnl As Panel";
mostCurrent._about_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private help_pnl As Panel";
mostCurrent._help_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private exit_pnl As Panel";
mostCurrent._exit_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private users_out_lbl As Label";
mostCurrent._users_out_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private users_lbl As Label";
mostCurrent._users_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private ban_logo As ImageView";
mostCurrent._ban_logo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private ban_picture As ImageView";
mostCurrent._ban_picture = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private users_heading As Panel";
mostCurrent._users_heading = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private srch_blood_img As ImageView";
mostCurrent._srch_blood_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private profile_img As ImageView";
mostCurrent._profile_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private about_img As ImageView";
mostCurrent._about_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private help_img As ImageView";
mostCurrent._help_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private exit_img As ImageView";
mostCurrent._exit_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Dim profile_panel As Panel";
mostCurrent._profile_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Dim scroll_profile_pnl As ScrollView";
mostCurrent._scroll_profile_pnl = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Dim profile_all_body As Panel";
mostCurrent._profile_all_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private all_inputs As Panel";
mostCurrent._all_inputs = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Dim pnl_body As Panel";
mostCurrent._pnl_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Dim pnl_blood_body As Panel";
mostCurrent._pnl_blood_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Dim pnl_bday_body As Panel";
mostCurrent._pnl_bday_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Dim pnl_donated_body As Panel";
mostCurrent._pnl_donated_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private all_info_query As HttpJob";
mostCurrent._all_info_query = new b4a.example.httpjob();
 //BA.debugLineNum = 76;BA.debugLine="Private update_job As HttpJob";
mostCurrent._update_job = new b4a.example.httpjob();
 //BA.debugLineNum = 78;BA.debugLine="Private lab_fullname As Label";
mostCurrent._lab_fullname = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Private lab_bloodgroup As Label";
mostCurrent._lab_bloodgroup = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private lab_email As Label";
mostCurrent._lab_email = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private lab_phonenumber As Label";
mostCurrent._lab_phonenumber = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private lab_phonenumber2 As Label";
mostCurrent._lab_phonenumber2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Private lab_location As Label";
mostCurrent._lab_location = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Private lab_question As Label";
mostCurrent._lab_question = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 85;BA.debugLine="Private lab_donate_confirm As Label";
mostCurrent._lab_donate_confirm = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private lab_bday As Label";
mostCurrent._lab_bday = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Private text_fn As EditText";
mostCurrent._text_fn = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Private text_blood As EditText";
mostCurrent._text_blood = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 91;BA.debugLine="Private text_email As EditText";
mostCurrent._text_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private text_phonenumber As EditText";
mostCurrent._text_phonenumber = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private text_phonenumber2 As EditText";
mostCurrent._text_phonenumber2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private text_bday As EditText";
mostCurrent._text_bday = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private text_location As EditText";
mostCurrent._text_location = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private text_answer As EditText";
mostCurrent._text_answer = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Private text_donated As EditText";
mostCurrent._text_donated = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 98;BA.debugLine="Private cancel_btn As Button";
mostCurrent._cancel_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private update_btn As Button";
mostCurrent._update_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Private usr_img As ImageView";
mostCurrent._usr_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _help_click() throws Exception{
 //BA.debugLineNum = 492;BA.debugLine="Sub help_Click";
 //BA.debugLineNum = 494;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(b4a.example.httpjob _job) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _user_all_info = null;
int _confirmr = 0;
 //BA.debugLineNum = 382;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 383;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 384;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"all_info_query")) {
case 0: {
 //BA.debugLineNum = 386;BA.debugLine="Dim user_all_info As TextWriter";
_user_all_info = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 387;BA.debugLine="user_all_info.Initialize(File.OpenOutput(";
_user_all_info.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"users_all_info.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 388;BA.debugLine="user_all_info.WriteLine(job.GetString.Tr";
_user_all_info.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 389;BA.debugLine="user_all_info.Close";
_user_all_info.Close();
 break; }
}
;
 //BA.debugLineNum = 393;BA.debugLine="If optionSelected == \"pofileView\" Then";
if ((_optionselected).equals("pofileView")) { 
 //BA.debugLineNum = 394;BA.debugLine="all_input_on_list";
_all_input_on_list();
 }else {
 //BA.debugLineNum = 396;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 397;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 398;BA.debugLine="Dim confirmR As Int";
_confirmr = 0;
 //BA.debugLineNum = 399;BA.debugLine="confirmR = Msgbox2(\"Successfuly Update!\",\"C O";
_confirmr = anywheresoftware.b4a.keywords.Common.Msgbox2("Successfuly Update!","C O N F I R M A T I O N","OK","","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 400;BA.debugLine="If confirmR == DialogResponse.POSITIVE Then";
if (_confirmr==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 401;BA.debugLine="users_out_lbl.text = text_answer.Text";
mostCurrent._users_out_lbl.setText((Object)(mostCurrent._text_answer.getText()));
 }else {
 };
 };
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 407;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 408;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 409;BA.debugLine="Msgbox(\"Error connecting to server, try again!\"";
anywheresoftware.b4a.keywords.Common.Msgbox("Error connecting to server, try again!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 412;BA.debugLine="End Sub";
return "";
}
public static String  _load_activity_layout() throws Exception{
b4a.example.calculations _text_temp = null;
 //BA.debugLineNum = 108;BA.debugLine="Sub load_activity_layout";
 //BA.debugLineNum = 109;BA.debugLine="Dim text_temp As calculations";
_text_temp = new b4a.example.calculations();
 //BA.debugLineNum = 111;BA.debugLine="text_temp.Initialize";
_text_temp._initialize(processBA);
 //BA.debugLineNum = 112;BA.debugLine="Log(\"name: \"&login_form.name_query)";
anywheresoftware.b4a.keywords.Common.Log("name: "+mostCurrent._login_form._name_query);
 //BA.debugLineNum = 114;BA.debugLine="users_out_lbl.text = login_form.name_query";
mostCurrent._users_out_lbl.setText((Object)(mostCurrent._login_form._name_query));
 //BA.debugLineNum = 115;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 116;BA.debugLine="ban_logo.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._ban_logo.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logo1.jpg").getObject()));
 //BA.debugLineNum = 117;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 118;BA.debugLine="users_panel.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._users_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 119;BA.debugLine="src_blood_pnl.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._src_blood_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 120;BA.debugLine="profile_pnl.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._profile_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 121;BA.debugLine="about_pnl.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._about_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 122;BA.debugLine="help_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._help_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 123;BA.debugLine="exit_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._exit_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 125;BA.debugLine="srch_blood_img.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._srch_blood_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu_search.png").getObject()));
 //BA.debugLineNum = 126;BA.debugLine="profile_img.SetBackgroundImage(LoadBitmap(File";
mostCurrent._profile_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"emyprofile.png").getObject()));
 //BA.debugLineNum = 127;BA.debugLine="about_img.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._about_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eaboutus.png").getObject()));
 //BA.debugLineNum = 128;BA.debugLine="help_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._help_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ehelp.png").getObject()));
 //BA.debugLineNum = 129;BA.debugLine="exit_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._exit_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eexit.png").getObject()));
 //BA.debugLineNum = 131;BA.debugLine="users_heading.Color = Colors.Transparent";
mostCurrent._users_heading.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 133;BA.debugLine="ban_picture.Width = 80%x";
mostCurrent._ban_picture.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 134;BA.debugLine="ban_logo.Width = 20%x";
mostCurrent._ban_logo.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 135;BA.debugLine="users_panel.Width = Activity.Width";
mostCurrent._users_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 136;BA.debugLine="src_blood_pnl.Width = Activity.Width";
mostCurrent._src_blood_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 137;BA.debugLine="profile_pnl.Width = Activity.Width";
mostCurrent._profile_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 138;BA.debugLine="about_pnl.Width = Activity.Width";
mostCurrent._about_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 139;BA.debugLine="help_pnl.Width = Activity.Width";
mostCurrent._help_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 140;BA.debugLine="exit_pnl.Width = Activity.Width";
mostCurrent._exit_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 141;BA.debugLine="users_heading.Width = Activity.Width";
mostCurrent._users_heading.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 143;BA.debugLine="users_heading.Height = 9%y";
mostCurrent._users_heading.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 144;BA.debugLine="users_panel.Height = 18%y";
mostCurrent._users_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 145;BA.debugLine="ban_picture.Height = users_panel.Height";
mostCurrent._ban_picture.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 146;BA.debugLine="ban_logo.Height = users_panel.Height";
mostCurrent._ban_logo.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 147;BA.debugLine="src_blood_pnl.Height = 12%y";
mostCurrent._src_blood_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 148;BA.debugLine="profile_pnl.Height = 12%y";
mostCurrent._profile_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 149;BA.debugLine="about_pnl.Height = 12%y";
mostCurrent._about_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 150;BA.debugLine="help_pnl.Height = 12%y";
mostCurrent._help_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 151;BA.debugLine="exit_pnl.Height = 12%y";
mostCurrent._exit_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 153;BA.debugLine="ban_logo.Left = 0";
mostCurrent._ban_logo.setLeft((int) (0));
 //BA.debugLineNum = 154;BA.debugLine="ban_picture.Left = ban_logo.Left + ban_logo.Width";
mostCurrent._ban_picture.setLeft((int) (mostCurrent._ban_logo.getLeft()+mostCurrent._ban_logo.getWidth()));
 //BA.debugLineNum = 155;BA.debugLine="users_panel.Left = 0";
mostCurrent._users_panel.setLeft((int) (0));
 //BA.debugLineNum = 156;BA.debugLine="src_blood_pnl.Left = 0";
mostCurrent._src_blood_pnl.setLeft((int) (0));
 //BA.debugLineNum = 157;BA.debugLine="profile_pnl.Left = 0";
mostCurrent._profile_pnl.setLeft((int) (0));
 //BA.debugLineNum = 158;BA.debugLine="about_pnl.Left = 0";
mostCurrent._about_pnl.setLeft((int) (0));
 //BA.debugLineNum = 159;BA.debugLine="help_pnl.Left = 0";
mostCurrent._help_pnl.setLeft((int) (0));
 //BA.debugLineNum = 160;BA.debugLine="exit_pnl.Left = 0";
mostCurrent._exit_pnl.setLeft((int) (0));
 //BA.debugLineNum = 161;BA.debugLine="users_heading.Left = 0";
mostCurrent._users_heading.setLeft((int) (0));
 //BA.debugLineNum = 163;BA.debugLine="users_panel.Top = 0";
mostCurrent._users_panel.setTop((int) (0));
 //BA.debugLineNum = 164;BA.debugLine="ban_picture.Top = 0";
mostCurrent._ban_picture.setTop((int) (0));
 //BA.debugLineNum = 165;BA.debugLine="ban_logo.Top = 0";
mostCurrent._ban_logo.setTop((int) (0));
 //BA.debugLineNum = 166;BA.debugLine="users_heading.Top = users_panel.Top + users_panel";
mostCurrent._users_heading.setTop((int) (mostCurrent._users_panel.getTop()+mostCurrent._users_panel.getHeight()));
 //BA.debugLineNum = 167;BA.debugLine="src_blood_pnl.Top = users_heading.Top + users_hea";
mostCurrent._src_blood_pnl.setTop((int) (mostCurrent._users_heading.getTop()+mostCurrent._users_heading.getHeight()));
 //BA.debugLineNum = 168;BA.debugLine="profile_pnl.Top = src_blood_pnl.Top + src_blood_p";
mostCurrent._profile_pnl.setTop((int) (mostCurrent._src_blood_pnl.getTop()+mostCurrent._src_blood_pnl.getHeight()));
 //BA.debugLineNum = 169;BA.debugLine="about_pnl.Top = profile_pnl.Top + profile_pnl.Hei";
mostCurrent._about_pnl.setTop((int) (mostCurrent._profile_pnl.getTop()+mostCurrent._profile_pnl.getHeight()));
 //BA.debugLineNum = 170;BA.debugLine="help_pnl.Top = about_pnl.Top + about_pnl.Height";
mostCurrent._help_pnl.setTop((int) (mostCurrent._about_pnl.getTop()+mostCurrent._about_pnl.getHeight()));
 //BA.debugLineNum = 171;BA.debugLine="exit_pnl.Top = help_pnl.Top + help_pnl.Height";
mostCurrent._exit_pnl.setTop((int) (mostCurrent._help_pnl.getTop()+mostCurrent._help_pnl.getHeight()));
 //BA.debugLineNum = 180;BA.debugLine="search_blood.Width = Activity.Width - 60%x";
mostCurrent._search_blood.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 181;BA.debugLine="about.Width = Activity.Width - 60%x";
mostCurrent._about.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 182;BA.debugLine="help.Width = Activity.Width - 60%x";
mostCurrent._help.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 183;BA.debugLine="profile.Width = Activity.Width - 60%x";
mostCurrent._profile.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 184;BA.debugLine="exit_btn.Width = Activity.Width - 60%x";
mostCurrent._exit_btn.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 185;BA.debugLine="srch_blood_img.Width = Activity.Width - 85%x";
mostCurrent._srch_blood_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 186;BA.debugLine="profile_img.Width = Activity.Width - 85%x";
mostCurrent._profile_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 187;BA.debugLine="about_img.Width = Activity.Width - 85%x";
mostCurrent._about_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 188;BA.debugLine="help_img.Width = Activity.Width - 85%x";
mostCurrent._help_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 189;BA.debugLine="exit_img.Width = Activity.Width - 85%x";
mostCurrent._exit_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 191;BA.debugLine="search_blood.Height = 9%y";
mostCurrent._search_blood.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 192;BA.debugLine="about.Height = 9%y";
mostCurrent._about.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 193;BA.debugLine="help.Height = 9%y";
mostCurrent._help.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 194;BA.debugLine="profile.Height = 9%y";
mostCurrent._profile.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 195;BA.debugLine="exit_btn.Height = 9%y";
mostCurrent._exit_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 196;BA.debugLine="srch_blood_img.Height = 9%y";
mostCurrent._srch_blood_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 197;BA.debugLine="profile_img.Height = 9%y";
mostCurrent._profile_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 198;BA.debugLine="about_img.Height = 9%y";
mostCurrent._about_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 199;BA.debugLine="help_img.Height = 9%y";
mostCurrent._help_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 200;BA.debugLine="exit_img.Height = 9%y";
mostCurrent._exit_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 202;BA.debugLine="users_lbl.Left = 2%x";
mostCurrent._users_lbl.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA));
 //BA.debugLineNum = 203;BA.debugLine="users_out_lbl.Left = users_lbl.Left + users_lbl.W";
mostCurrent._users_out_lbl.setLeft((int) (mostCurrent._users_lbl.getLeft()+mostCurrent._users_lbl.getWidth()));
 //BA.debugLineNum = 204;BA.debugLine="search_blood.Left = ((src_blood_pnl.Width/2)/2)/";
mostCurrent._search_blood.setLeft((int) (((mostCurrent._src_blood_pnl.getWidth()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 205;BA.debugLine="profile.Left = ((profile_pnl.Width/2)/2)";
mostCurrent._profile.setLeft((int) (((mostCurrent._profile_pnl.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 206;BA.debugLine="about.Left = (help_pnl.Width/2)";
mostCurrent._about.setLeft((int) ((mostCurrent._help_pnl.getWidth()/(double)2)));
 //BA.debugLineNum = 207;BA.debugLine="help.Left = ((about_pnl.Width/2)/2)";
mostCurrent._help.setLeft((int) (((mostCurrent._about_pnl.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 208;BA.debugLine="exit_btn.Left = ((exit_pnl.Width/2)/2)/2";
mostCurrent._exit_btn.setLeft((int) (((mostCurrent._exit_pnl.getWidth()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 210;BA.debugLine="srch_blood_img.Left = search_blood.Left + search";
mostCurrent._srch_blood_img.setLeft((int) (mostCurrent._search_blood.getLeft()+mostCurrent._search_blood.getWidth()));
 //BA.debugLineNum = 211;BA.debugLine="profile_img.Left = profile.Left + profile.Widt";
mostCurrent._profile_img.setLeft((int) (mostCurrent._profile.getLeft()+mostCurrent._profile.getWidth()));
 //BA.debugLineNum = 212;BA.debugLine="about_img.Left = about.Left - about_img.Width";
mostCurrent._about_img.setLeft((int) (mostCurrent._about.getLeft()-mostCurrent._about_img.getWidth()));
 //BA.debugLineNum = 213;BA.debugLine="help_img.Left = help.Left + help.Width";
mostCurrent._help_img.setLeft((int) (mostCurrent._help.getLeft()+mostCurrent._help.getWidth()));
 //BA.debugLineNum = 214;BA.debugLine="exit_img.Left = exit_btn.Left + exit_btn.Width";
mostCurrent._exit_img.setLeft((int) (mostCurrent._exit_btn.getLeft()+mostCurrent._exit_btn.getWidth()));
 //BA.debugLineNum = 216;BA.debugLine="users_out_lbl.Top = ((users_heading.Height/2)/2)/";
mostCurrent._users_out_lbl.setTop((int) (((mostCurrent._users_heading.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 217;BA.debugLine="users_lbl.Top = users_out_lbl.Top";
mostCurrent._users_lbl.setTop(mostCurrent._users_out_lbl.getTop());
 //BA.debugLineNum = 219;BA.debugLine="search_blood.Top = ((src_blood_pnl.Height/2)/2)/";
mostCurrent._search_blood.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 220;BA.debugLine="about.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._about.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 221;BA.debugLine="help.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._help.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 222;BA.debugLine="profile.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._profile.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 223;BA.debugLine="exit_btn.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_btn.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 225;BA.debugLine="srch_blood_img.Top = ((src_blood_pnl.Height/2)/2";
mostCurrent._srch_blood_img.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 226;BA.debugLine="profile_img.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._profile_img.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 227;BA.debugLine="about_img.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._about_img.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 228;BA.debugLine="help_img.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._help_img.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 229;BA.debugLine="exit_img.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_img.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 231;BA.debugLine="search_blood.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._search_blood.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"SEARCH.png").getObject()));
 //BA.debugLineNum = 232;BA.debugLine="about.SetBackgroundImage(LoadBitmap(File.DirAsse";
mostCurrent._about.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ABOUT_US.png").getObject()));
 //BA.debugLineNum = 233;BA.debugLine="help.SetBackgroundImage(LoadBitmap(File.DirAsset";
mostCurrent._help.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"HELP.png").getObject()));
 //BA.debugLineNum = 234;BA.debugLine="profile.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._profile.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"my_profile.png").getObject()));
 //BA.debugLineNum = 235;BA.debugLine="exit_btn.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._exit_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"EXIT.png").getObject()));
 //BA.debugLineNum = 237;BA.debugLine="End Sub";
return "";
}
public static String  _locate_edit_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_ok_btn = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 725;BA.debugLine="Sub locate_edit_Click";
 //BA.debugLineNum = 726;BA.debugLine="list_location_b.Initialize";
_list_location_b.Initialize();
 //BA.debugLineNum = 727;BA.debugLine="location_spin_street.Initialize(\"location_spin_s";
mostCurrent._location_spin_street.Initialize(mostCurrent.activityBA,"location_spin_street");
 //BA.debugLineNum = 728;BA.debugLine="location_spin_brgy.Initialize(\"location_spin_brg";
mostCurrent._location_spin_brgy.Initialize(mostCurrent.activityBA,"location_spin_brgy");
 //BA.debugLineNum = 729;BA.debugLine="list_location_s.Initialize";
_list_location_s.Initialize();
 //BA.debugLineNum = 730;BA.debugLine="list_location_b.Add(\"Barangay  1\") 'index 0";
_list_location_b.Add((Object)("Barangay  1"));
 //BA.debugLineNum = 731;BA.debugLine="list_location_b.Add(\"Barangay 2\") 'index 1";
_list_location_b.Add((Object)("Barangay 2"));
 //BA.debugLineNum = 732;BA.debugLine="list_location_b.Add(\"Barangay 3\") 'index 2";
_list_location_b.Add((Object)("Barangay 3"));
 //BA.debugLineNum = 733;BA.debugLine="list_location_b.Add(\"Barangay 4\") 'index 3";
_list_location_b.Add((Object)("Barangay 4"));
 //BA.debugLineNum = 734;BA.debugLine="list_location_b.Add(\"Aguisan\") 'index 4";
_list_location_b.Add((Object)("Aguisan"));
 //BA.debugLineNum = 735;BA.debugLine="list_location_b.Add(\"caradio-an\") 'index 5";
_list_location_b.Add((Object)("caradio-an"));
 //BA.debugLineNum = 736;BA.debugLine="list_location_b.Add(\"Buenavista\") 'index 6";
_list_location_b.Add((Object)("Buenavista"));
 //BA.debugLineNum = 737;BA.debugLine="list_location_b.Add(\"Cabadiangan\") 'index 7";
_list_location_b.Add((Object)("Cabadiangan"));
 //BA.debugLineNum = 738;BA.debugLine="list_location_b.Add(\"Cabanbanan\") 'index 8";
_list_location_b.Add((Object)("Cabanbanan"));
 //BA.debugLineNum = 739;BA.debugLine="list_location_b.Add(\"Carabalan\") 'index 9";
_list_location_b.Add((Object)("Carabalan"));
 //BA.debugLineNum = 740;BA.debugLine="list_location_b.Add(\"Libacao\") 'index 10";
_list_location_b.Add((Object)("Libacao"));
 //BA.debugLineNum = 741;BA.debugLine="list_location_b.Add(\"Mahalang\") 'index 11";
_list_location_b.Add((Object)("Mahalang"));
 //BA.debugLineNum = 742;BA.debugLine="list_location_b.Add(\"Mambagaton\") 'index 12";
_list_location_b.Add((Object)("Mambagaton"));
 //BA.debugLineNum = 743;BA.debugLine="list_location_b.Add(\"Nabalian\") 'index 13";
_list_location_b.Add((Object)("Nabalian"));
 //BA.debugLineNum = 744;BA.debugLine="list_location_b.Add(\"San Antonio\") 'index 14";
_list_location_b.Add((Object)("San Antonio"));
 //BA.debugLineNum = 745;BA.debugLine="list_location_b.Add(\"Saraet\") 'index 15";
_list_location_b.Add((Object)("Saraet"));
 //BA.debugLineNum = 746;BA.debugLine="list_location_b.Add(\"Suay\") 'index 16";
_list_location_b.Add((Object)("Suay"));
 //BA.debugLineNum = 747;BA.debugLine="list_location_b.Add(\"Talaban\") 'index 17";
_list_location_b.Add((Object)("Talaban"));
 //BA.debugLineNum = 748;BA.debugLine="list_location_b.Add(\"Tooy\") 'index 18";
_list_location_b.Add((Object)("Tooy"));
 //BA.debugLineNum = 749;BA.debugLine="location_spin_brgy.AddAll(list_location_b)";
mostCurrent._location_spin_brgy.AddAll(_list_location_b);
 //BA.debugLineNum = 751;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 752;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 753;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 754;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 755;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 756;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 757;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 758;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 //BA.debugLineNum = 759;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 761;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 762;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 763;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 764;BA.debugLine="edit_ok_btn.Initialize(\"edit_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_ok_btn");
 //BA.debugLineNum = 765;BA.debugLine="edit_can_btn.Initialize(\"edit_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_can_btn");
 //BA.debugLineNum = 766;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 767;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 768;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 769;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 770;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 771;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 772;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 773;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 774;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 775;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 776;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 777;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 778;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 779;BA.debugLine="title_lbl.Text = \"SELECT LOCATION\"";
_title_lbl.setText((Object)("SELECT LOCATION"));
 //BA.debugLineNum = 780;BA.debugLine="title_lbl.Gravity =  Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 781;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 782;BA.debugLine="pnl_body.Initialize(\"pnl_body\")";
mostCurrent._pnl_body.Initialize(mostCurrent.activityBA,"pnl_body");
 //BA.debugLineNum = 783;BA.debugLine="pnl_body.Color = Colors.Transparent";
mostCurrent._pnl_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 784;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 785;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 786;BA.debugLine="pnl.AddView(location_spin_brgy,2%x,title_lbl.Top";
_pnl.AddView((android.view.View)(mostCurrent._location_spin_brgy.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 787;BA.debugLine="pnl.AddView(location_spin_street,location_spin_br";
_pnl.AddView((android.view.View)(mostCurrent._location_spin_street.getObject()),(int) (mostCurrent._location_spin_brgy.getLeft()+mostCurrent._location_spin_brgy.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),mostCurrent._location_spin_brgy.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 788;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,location_spin_street";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._location_spin_street.getTop()+mostCurrent._location_spin_street.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 789;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._location_spin_street.getTop()+mostCurrent._location_spin_street.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 791;BA.debugLine="pnl_body.AddView(pnl,13%x,((Activity.Height/2)/2)";
mostCurrent._pnl_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 792;BA.debugLine="pnl_body.BringToFront";
mostCurrent._pnl_body.BringToFront();
 //BA.debugLineNum = 795;BA.debugLine="Activity.AddView(pnl_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 796;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_brgy_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1366;BA.debugLine="Sub location_spin_brgy_ItemClick (Position As Int,";
 //BA.debugLineNum = 1367;BA.debugLine="list_location_s.Clear";
_list_location_s.Clear();
 //BA.debugLineNum = 1369;BA.debugLine="location_spin_street.Clear";
mostCurrent._location_spin_street.Clear();
 //BA.debugLineNum = 1372;BA.debugLine="If Position == 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 1373;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1374;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 1375;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 1376;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 1377;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 1378;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 1379;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 1380;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 }else if(_position==1) { 
 //BA.debugLineNum = 1383;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1384;BA.debugLine="list_location_s.Add(\"Monton st.\")";
_list_location_s.Add((Object)("Monton st."));
 //BA.debugLineNum = 1385;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 //BA.debugLineNum = 1386;BA.debugLine="list_location_s.Add(\"Purok star apple\")";
_list_location_s.Add((Object)("Purok star apple"));
 //BA.debugLineNum = 1387;BA.debugLine="list_location_s.Add(\"Gatuslao st.\")";
_list_location_s.Add((Object)("Gatuslao st."));
 //BA.debugLineNum = 1388;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 1389;BA.debugLine="list_location_s.Add(\"Tabino st.\")";
_list_location_s.Add((Object)("Tabino st."));
 //BA.debugLineNum = 1390;BA.debugLine="list_location_s.Add(\"River side\")";
_list_location_s.Add((Object)("River side"));
 //BA.debugLineNum = 1391;BA.debugLine="list_location_s.Add(\"Arroyan st\")";
_list_location_s.Add((Object)("Arroyan st"));
 }else if(_position==2) { 
 //BA.debugLineNum = 1393;BA.debugLine="list_location_s.Add(\"Segovia st.\")";
_list_location_s.Add((Object)("Segovia st."));
 //BA.debugLineNum = 1394;BA.debugLine="list_location_s.Add(\"Vasquez st.\")";
_list_location_s.Add((Object)("Vasquez st."));
 //BA.debugLineNum = 1395;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 1396;BA.debugLine="list_location_s.Add(\"Old relis st.\")";
_list_location_s.Add((Object)("Old relis st."));
 //BA.debugLineNum = 1397;BA.debugLine="list_location_s.Add(\"Wayang\")";
_list_location_s.Add((Object)("Wayang"));
 //BA.debugLineNum = 1398;BA.debugLine="list_location_s.Add(\"Valencia\")";
_list_location_s.Add((Object)("Valencia"));
 //BA.debugLineNum = 1399;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 1400;BA.debugLine="list_location_s.Add(\"Bingig\")";
_list_location_s.Add((Object)("Bingig"));
 //BA.debugLineNum = 1401;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 1402;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 }else if(_position==3) { 
 //BA.debugLineNum = 1404;BA.debugLine="list_location_s.Add(\"Crusher\")";
_list_location_s.Add((Object)("Crusher"));
 //BA.debugLineNum = 1405;BA.debugLine="list_location_s.Add(\"Bangga mayok\")";
_list_location_s.Add((Object)("Bangga mayok"));
 //BA.debugLineNum = 1406;BA.debugLine="list_location_s.Add(\"Villa julita\")";
_list_location_s.Add((Object)("Villa julita"));
 //BA.debugLineNum = 1407;BA.debugLine="list_location_s.Add(\"Greenland subdivision\")";
_list_location_s.Add((Object)("Greenland subdivision"));
 //BA.debugLineNum = 1408;BA.debugLine="list_location_s.Add(\"Bangga 3c\")";
_list_location_s.Add((Object)("Bangga 3c"));
 //BA.debugLineNum = 1409;BA.debugLine="list_location_s.Add(\"Cambugnon\")";
_list_location_s.Add((Object)("Cambugnon"));
 //BA.debugLineNum = 1410;BA.debugLine="list_location_s.Add(\"Menez\")";
_list_location_s.Add((Object)("Menez"));
 //BA.debugLineNum = 1411;BA.debugLine="list_location_s.Add(\"Relis\")";
_list_location_s.Add((Object)("Relis"));
 //BA.debugLineNum = 1412;BA.debugLine="list_location_s.Add(\"Bangga patyo\")";
_list_location_s.Add((Object)("Bangga patyo"));
 }else if(_position==4) { 
 //BA.debugLineNum = 1414;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1415;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1416;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1417;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1418;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 1419;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 //BA.debugLineNum = 1420;BA.debugLine="list_location_s.Add(\"Purok 7\")";
_list_location_s.Add((Object)("Purok 7"));
 //BA.debugLineNum = 1421;BA.debugLine="list_location_s.Add(\"Purok 8\")";
_list_location_s.Add((Object)("Purok 8"));
 //BA.debugLineNum = 1422;BA.debugLine="list_location_s.Add(\"Purok 9\")";
_list_location_s.Add((Object)("Purok 9"));
 //BA.debugLineNum = 1423;BA.debugLine="list_location_s.Add(\"Purok 10\")";
_list_location_s.Add((Object)("Purok 10"));
 //BA.debugLineNum = 1424;BA.debugLine="list_location_s.Add(\"Purok 11\")";
_list_location_s.Add((Object)("Purok 11"));
 //BA.debugLineNum = 1425;BA.debugLine="list_location_s.Add(\"Purok 12\")";
_list_location_s.Add((Object)("Purok 12"));
 }else if(_position==5) { 
 //BA.debugLineNum = 1427;BA.debugLine="list_location_s.Add(\"Malusay\")";
_list_location_s.Add((Object)("Malusay"));
 //BA.debugLineNum = 1428;BA.debugLine="list_location_s.Add(\"Nasug ong\")";
_list_location_s.Add((Object)("Nasug ong"));
 //BA.debugLineNum = 1429;BA.debugLine="list_location_s.Add(\"Lugway\")";
_list_location_s.Add((Object)("Lugway"));
 //BA.debugLineNum = 1430;BA.debugLine="list_location_s.Add(\"Ubay\")";
_list_location_s.Add((Object)("Ubay"));
 //BA.debugLineNum = 1431;BA.debugLine="list_location_s.Add(\"Fisheries\")";
_list_location_s.Add((Object)("Fisheries"));
 //BA.debugLineNum = 1432;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1433;BA.debugLine="list_location_s.Add(\"Calasa\")";
_list_location_s.Add((Object)("Calasa"));
 //BA.debugLineNum = 1434;BA.debugLine="list_location_s.Add(\"Hda. Serafin\")";
_list_location_s.Add((Object)("Hda. Serafin"));
 //BA.debugLineNum = 1435;BA.debugLine="list_location_s.Add(\"Patay na suba\")";
_list_location_s.Add((Object)("Patay na suba"));
 //BA.debugLineNum = 1436;BA.debugLine="list_location_s.Add(\"Lumanog\")";
_list_location_s.Add((Object)("Lumanog"));
 //BA.debugLineNum = 1437;BA.debugLine="list_location_s.Add(\"San agustin\")";
_list_location_s.Add((Object)("San agustin"));
 //BA.debugLineNum = 1438;BA.debugLine="list_location_s.Add(\"San jose\")";
_list_location_s.Add((Object)("San jose"));
 //BA.debugLineNum = 1439;BA.debugLine="list_location_s.Add(\"Maglantay\")";
_list_location_s.Add((Object)("Maglantay"));
 //BA.debugLineNum = 1440;BA.debugLine="list_location_s.Add(\"San juan\")";
_list_location_s.Add((Object)("San juan"));
 //BA.debugLineNum = 1441;BA.debugLine="list_location_s.Add(\"Magsaha\")";
_list_location_s.Add((Object)("Magsaha"));
 //BA.debugLineNum = 1442;BA.debugLine="list_location_s.Add(\"Tagmanok\")";
_list_location_s.Add((Object)("Tagmanok"));
 //BA.debugLineNum = 1443;BA.debugLine="list_location_s.Add(\"Butong\")";
_list_location_s.Add((Object)("Butong"));
 }else if(_position==6) { 
 //BA.debugLineNum = 1445;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1446;BA.debugLine="list_location_s.Add(\"Saisi\")";
_list_location_s.Add((Object)("Saisi"));
 //BA.debugLineNum = 1447;BA.debugLine="list_location_s.Add(\"Paloypoy\")";
_list_location_s.Add((Object)("Paloypoy"));
 //BA.debugLineNum = 1448;BA.debugLine="list_location_s.Add(\"Tigue\")";
_list_location_s.Add((Object)("Tigue"));
 }else if(_position==7) { 
 //BA.debugLineNum = 1450;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1451;BA.debugLine="list_location_s.Add(\"Tonggo\")";
_list_location_s.Add((Object)("Tonggo"));
 //BA.debugLineNum = 1452;BA.debugLine="list_location_s.Add(\"Iling iling\")";
_list_location_s.Add((Object)("Iling iling"));
 //BA.debugLineNum = 1453;BA.debugLine="list_location_s.Add(\"Campayas\")";
_list_location_s.Add((Object)("Campayas"));
 //BA.debugLineNum = 1454;BA.debugLine="list_location_s.Add(\"Palayan\")";
_list_location_s.Add((Object)("Palayan"));
 //BA.debugLineNum = 1455;BA.debugLine="list_location_s.Add(\"Guia\")";
_list_location_s.Add((Object)("Guia"));
 //BA.debugLineNum = 1456;BA.debugLine="list_location_s.Add(\"An-an\")";
_list_location_s.Add((Object)("An-an"));
 //BA.debugLineNum = 1457;BA.debugLine="list_location_s.Add(\"An-an 2\")";
_list_location_s.Add((Object)("An-an 2"));
 //BA.debugLineNum = 1458;BA.debugLine="list_location_s.Add(\"Sta. rita\")";
_list_location_s.Add((Object)("Sta. rita"));
 //BA.debugLineNum = 1459;BA.debugLine="list_location_s.Add(\"Benedicto\")";
_list_location_s.Add((Object)("Benedicto"));
 //BA.debugLineNum = 1460;BA.debugLine="list_location_s.Add(\"Sta. cruz/ bunggol\")";
_list_location_s.Add((Object)("Sta. cruz/ bunggol"));
 //BA.debugLineNum = 1461;BA.debugLine="list_location_s.Add(\"Olalia\")";
_list_location_s.Add((Object)("Olalia"));
 //BA.debugLineNum = 1462;BA.debugLine="list_location_s.Add(\"Banuyo\")";
_list_location_s.Add((Object)("Banuyo"));
 //BA.debugLineNum = 1463;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1464;BA.debugLine="list_location_s.Add(\"Riverside\")";
_list_location_s.Add((Object)("Riverside"));
 }else if(_position==8) { 
 //BA.debugLineNum = 1466;BA.debugLine="list_location_s.Add(\"Balangga-an\")";
_list_location_s.Add((Object)("Balangga-an"));
 //BA.debugLineNum = 1467;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1468;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1469;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 1470;BA.debugLine="list_location_s.Add(\"Bakyas\")";
_list_location_s.Add((Object)("Bakyas"));
 }else if(_position==9) { 
 //BA.debugLineNum = 1472;BA.debugLine="list_location_s.Add(\"Cunalom\")";
_list_location_s.Add((Object)("Cunalom"));
 //BA.debugLineNum = 1473;BA.debugLine="list_location_s.Add(\"Tara\")";
_list_location_s.Add((Object)("Tara"));
 //BA.debugLineNum = 1474;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1475;BA.debugLine="list_location_s.Add(\"Casipungan\")";
_list_location_s.Add((Object)("Casipungan"));
 //BA.debugLineNum = 1476;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1477;BA.debugLine="list_location_s.Add(\"Lanipga\")";
_list_location_s.Add((Object)("Lanipga"));
 //BA.debugLineNum = 1478;BA.debugLine="list_location_s.Add(\"Bulod\")";
_list_location_s.Add((Object)("Bulod"));
 //BA.debugLineNum = 1479;BA.debugLine="list_location_s.Add(\"Bonton\")";
_list_location_s.Add((Object)("Bonton"));
 //BA.debugLineNum = 1480;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 }else if(_position==10) { 
 //BA.debugLineNum = 1482;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 1483;BA.debugLine="list_location_s.Add(\"Balisong\")";
_list_location_s.Add((Object)("Balisong"));
 //BA.debugLineNum = 1484;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1485;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1486;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1487;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1488;BA.debugLine="list_location_s.Add(\"Dubdub\")";
_list_location_s.Add((Object)("Dubdub"));
 //BA.debugLineNum = 1489;BA.debugLine="list_location_s.Add(\"Hda. San jose valing\")";
_list_location_s.Add((Object)("Hda. San jose valing"));
 }else if(_position==11) { 
 //BA.debugLineNum = 1491;BA.debugLine="list_location_s.Add(\"Acapulco\")";
_list_location_s.Add((Object)("Acapulco"));
 //BA.debugLineNum = 1492;BA.debugLine="list_location_s.Add(\"Liki\")";
_list_location_s.Add((Object)("Liki"));
 //BA.debugLineNum = 1493;BA.debugLine="list_location_s.Add(\"500\")";
_list_location_s.Add((Object)("500"));
 //BA.debugLineNum = 1494;BA.debugLine="list_location_s.Add(\"Aglatong\")";
_list_location_s.Add((Object)("Aglatong"));
 //BA.debugLineNum = 1495;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1496;BA.debugLine="list_location_s.Add(\"Baptist\")";
_list_location_s.Add((Object)("Baptist"));
 }else if(_position==12) { 
 //BA.debugLineNum = 1498;BA.debugLine="list_location_s.Add(\"Lizares\")";
_list_location_s.Add((Object)("Lizares"));
 //BA.debugLineNum = 1499;BA.debugLine="list_location_s.Add(\"Pakol\")";
_list_location_s.Add((Object)("Pakol"));
 //BA.debugLineNum = 1500;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1501;BA.debugLine="list_location_s.Add(\"Lanete\")";
_list_location_s.Add((Object)("Lanete"));
 //BA.debugLineNum = 1502;BA.debugLine="list_location_s.Add(\"Kasoy\")";
_list_location_s.Add((Object)("Kasoy"));
 //BA.debugLineNum = 1503;BA.debugLine="list_location_s.Add(\"Bato\")";
_list_location_s.Add((Object)("Bato"));
 //BA.debugLineNum = 1504;BA.debugLine="list_location_s.Add(\"Frande\")";
_list_location_s.Add((Object)("Frande"));
 //BA.debugLineNum = 1505;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 1506;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 //BA.debugLineNum = 1507;BA.debugLine="list_location_s.Add(\"Culban\")";
_list_location_s.Add((Object)("Culban"));
 //BA.debugLineNum = 1508;BA.debugLine="list_location_s.Add(\"Calansi\")";
_list_location_s.Add((Object)("Calansi"));
 //BA.debugLineNum = 1509;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1510;BA.debugLine="list_location_s.Add(\"Dama\")";
_list_location_s.Add((Object)("Dama"));
 }else if(_position==13) { 
 //BA.debugLineNum = 1512;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1513;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1514;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1515;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1516;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 1517;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 }else if(_position==14) { 
 //BA.debugLineNum = 1519;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1520;BA.debugLine="list_location_s.Add(\"Calubihan\")";
_list_location_s.Add((Object)("Calubihan"));
 //BA.debugLineNum = 1521;BA.debugLine="list_location_s.Add(\"Mapulang duta\")";
_list_location_s.Add((Object)("Mapulang duta"));
 //BA.debugLineNum = 1522;BA.debugLine="list_location_s.Add(\"Abud\")";
_list_location_s.Add((Object)("Abud"));
 //BA.debugLineNum = 1523;BA.debugLine="list_location_s.Add(\"Molo\")";
_list_location_s.Add((Object)("Molo"));
 //BA.debugLineNum = 1524;BA.debugLine="list_location_s.Add(\"Balabag\")";
_list_location_s.Add((Object)("Balabag"));
 //BA.debugLineNum = 1525;BA.debugLine="list_location_s.Add(\"Pandan\")";
_list_location_s.Add((Object)("Pandan"));
 //BA.debugLineNum = 1526;BA.debugLine="list_location_s.Add(\"Nahulop\")";
_list_location_s.Add((Object)("Nahulop"));
 //BA.debugLineNum = 1527;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 1528;BA.debugLine="list_location_s.Add(\"Aglaoa\")";
_list_location_s.Add((Object)("Aglaoa"));
 }else if(_position==15) { 
 //BA.debugLineNum = 1530;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1531;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1532;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1533;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 }else if(_position==16) { 
 //BA.debugLineNum = 1535;BA.debugLine="list_location_s.Add(\"ORS\")";
_list_location_s.Add((Object)("ORS"));
 //BA.debugLineNum = 1536;BA.debugLine="list_location_s.Add(\"Aloe vera\")";
_list_location_s.Add((Object)("Aloe vera"));
 //BA.debugLineNum = 1537;BA.debugLine="list_location_s.Add(\"SCAD\")";
_list_location_s.Add((Object)("SCAD"));
 //BA.debugLineNum = 1538;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1539;BA.debugLine="list_location_s.Add(\"Sampaguita\")";
_list_location_s.Add((Object)("Sampaguita"));
 //BA.debugLineNum = 1540;BA.debugLine="list_location_s.Add(\"Bonguinvilla\")";
_list_location_s.Add((Object)("Bonguinvilla"));
 //BA.debugLineNum = 1541;BA.debugLine="list_location_s.Add(\"Cagay\")";
_list_location_s.Add((Object)("Cagay"));
 //BA.debugLineNum = 1542;BA.debugLine="list_location_s.Add(\"Naga\")";
_list_location_s.Add((Object)("Naga"));
 }else if(_position==17) { 
 //BA.debugLineNum = 1544;BA.debugLine="list_location_s.Add(\"Hda. Naval\")";
_list_location_s.Add((Object)("Hda. Naval"));
 //BA.debugLineNum = 1545;BA.debugLine="list_location_s.Add(\"Antipolo\")";
_list_location_s.Add((Object)("Antipolo"));
 //BA.debugLineNum = 1546;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1547;BA.debugLine="list_location_s.Add(\"Punta talaban\")";
_list_location_s.Add((Object)("Punta talaban"));
 //BA.debugLineNum = 1548;BA.debugLine="list_location_s.Add(\"Batang guwaan\")";
_list_location_s.Add((Object)("Batang guwaan"));
 //BA.debugLineNum = 1549;BA.debugLine="list_location_s.Add(\"Batang sulod\")";
_list_location_s.Add((Object)("Batang sulod"));
 //BA.debugLineNum = 1550;BA.debugLine="list_location_s.Add(\"Mabini st.\")";
_list_location_s.Add((Object)("Mabini st."));
 //BA.debugLineNum = 1551;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 1552;BA.debugLine="list_location_s.Add(\"Hacienda silos\")";
_list_location_s.Add((Object)("Hacienda silos"));
 //BA.debugLineNum = 1553;BA.debugLine="list_location_s.Add(\"Lopez jeana 1\")";
_list_location_s.Add((Object)("Lopez jeana 1"));
 //BA.debugLineNum = 1554;BA.debugLine="list_location_s.Add(\"Lopez jeana 2\")";
_list_location_s.Add((Object)("Lopez jeana 2"));
 }else if(_position==18) { 
 //BA.debugLineNum = 1556;BA.debugLine="list_location_s.Add(\"Ilawod\")";
_list_location_s.Add((Object)("Ilawod"));
 //BA.debugLineNum = 1557;BA.debugLine="list_location_s.Add(\"Buhian\")";
_list_location_s.Add((Object)("Buhian"));
 //BA.debugLineNum = 1558;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1559;BA.debugLine="list_location_s.Add(\"Mambato\")";
_list_location_s.Add((Object)("Mambato"));
 };
 //BA.debugLineNum = 1562;BA.debugLine="brgy_index = Position";
_brgy_index = _position;
 //BA.debugLineNum = 1563;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 1566;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_street_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1567;BA.debugLine="Sub location_spin_street_ItemClick (Position As In";
 //BA.debugLineNum = 1568;BA.debugLine="street_index = Position";
_street_index = _position;
 //BA.debugLineNum = 1569;BA.debugLine="street_lat_lng";
_street_lat_lng();
 //BA.debugLineNum = 1570;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_bday_body_click() throws Exception{
 //BA.debugLineNum = 656;BA.debugLine="Sub pnl_bday_body_click";
 //BA.debugLineNum = 658;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_blood_body_click() throws Exception{
 //BA.debugLineNum = 718;BA.debugLine="Sub pnl_blood_body_click";
 //BA.debugLineNum = 720;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_body_click() throws Exception{
 //BA.debugLineNum = 808;BA.debugLine="Sub pnl_body_click";
 //BA.debugLineNum = 810;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_donated_body_click() throws Exception{
 //BA.debugLineNum = 577;BA.debugLine="Sub pnl_donated_body_click";
 //BA.debugLineNum = 579;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim list_all_info As List";
_list_all_info = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 10;BA.debugLine="Dim list_bloodgroup As List";
_list_bloodgroup = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 11;BA.debugLine="Dim list_donated As List";
_list_donated = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 12;BA.debugLine="Dim list_day,list_month,list_year As List";
_list_day = new anywheresoftware.b4a.objects.collections.List();
_list_month = new anywheresoftware.b4a.objects.collections.List();
_list_year = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 13;BA.debugLine="Dim users_string_login As String";
_users_string_login = "";
 //BA.debugLineNum = 15;BA.debugLine="Dim blood_selected As String : blood_selected = \"";
_blood_selected = "";
 //BA.debugLineNum = 15;BA.debugLine="Dim blood_selected As String : blood_selected = \"";
_blood_selected = "A";
 //BA.debugLineNum = 16;BA.debugLine="Dim bday_day_selected As String : bday_day_select";
_bday_day_selected = "";
 //BA.debugLineNum = 16;BA.debugLine="Dim bday_day_selected As String : bday_day_select";
_bday_day_selected = "1";
 //BA.debugLineNum = 17;BA.debugLine="Dim bday_month_selected As String : bday_month_se";
_bday_month_selected = "";
 //BA.debugLineNum = 17;BA.debugLine="Dim bday_month_selected As String : bday_month_se";
_bday_month_selected = "1";
 //BA.debugLineNum = 18;BA.debugLine="Dim bday_year_selected As String : bday_year_sele";
_bday_year_selected = "";
 //BA.debugLineNum = 18;BA.debugLine="Dim bday_year_selected As String : bday_year_sele";
_bday_year_selected = "2017";
 //BA.debugLineNum = 19;BA.debugLine="Dim location_brgy_selected As String : location_b";
_location_brgy_selected = "";
 //BA.debugLineNum = 19;BA.debugLine="Dim location_brgy_selected As String : location_b";
_location_brgy_selected = "Brgy 1";
 //BA.debugLineNum = 20;BA.debugLine="Dim location_street_selected As String : location";
_location_street_selected = "";
 //BA.debugLineNum = 20;BA.debugLine="Dim location_street_selected As String : location";
_location_street_selected = "Rizal St.";
 //BA.debugLineNum = 21;BA.debugLine="Dim is_donated As String : is_donated = \"Yes\"";
_is_donated = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim is_donated As String : is_donated = \"Yes\"";
_is_donated = "Yes";
 //BA.debugLineNum = 22;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 23;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "122.869168";
 //BA.debugLineNum = 24;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = 0;
 //BA.debugLineNum = 24;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = (int) (0);
 //BA.debugLineNum = 25;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = 0;
 //BA.debugLineNum = 25;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = (int) (0);
 //BA.debugLineNum = 26;BA.debugLine="Dim list_location_b,list_location_s,list_location";
_list_location_b = new anywheresoftware.b4a.objects.collections.List();
_list_location_s = new anywheresoftware.b4a.objects.collections.List();
_list_location_p = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 27;BA.debugLine="Dim optionSelected As String";
_optionselected = "";
 //BA.debugLineNum = 29;BA.debugLine="Private image_container As String";
_image_container = "";
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _profile_all_body_click() throws Exception{
 //BA.debugLineNum = 304;BA.debugLine="Sub profile_all_body_click";
 //BA.debugLineNum = 306;BA.debugLine="End Sub";
return "";
}
public static String  _profile_click() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriters = null;
b4a.example.calculations _url_back = null;
String _all_users_info = "";
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 248;BA.debugLine="Sub profile_Click";
 //BA.debugLineNum = 249;BA.debugLine="ProgressDialogShow2(\"Please Wait..\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Please Wait..",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 250;BA.debugLine="optionSelected = \"pofileView\"";
_optionselected = "pofileView";
 //BA.debugLineNum = 251;BA.debugLine="Dim TextWriters As TextWriter";
_textwriters = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 252;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"users_all_info.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 254;BA.debugLine="Dim url_back As calculations";
_url_back = new b4a.example.calculations();
 //BA.debugLineNum = 255;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 256;BA.debugLine="Dim all_users_info As String";
_all_users_info = "";
 //BA.debugLineNum = 258;BA.debugLine="profile_all_body.Initialize(\"profile_all_body\")";
mostCurrent._profile_all_body.Initialize(mostCurrent.activityBA,"profile_all_body");
 //BA.debugLineNum = 260;BA.debugLine="all_info_query.Initialize(\"all_info_query\",Me)";
mostCurrent._all_info_query._initialize(processBA,"all_info_query",menu_form.getObject());
 //BA.debugLineNum = 261;BA.debugLine="all_users_info = url_back.php_email_url(\"/bloodli";
_all_users_info = _url_back._php_email_url("/bloodlifePHP/search_all_users_data.php");
 //BA.debugLineNum = 262;BA.debugLine="all_info_query.Download2(all_users_info,Array As";
mostCurrent._all_info_query._download2(_all_users_info,new String[]{"all_info","SELECT * FROM `bloodlife_db`.`person_info` where `id`='"+mostCurrent._login_form._id_query+"';"});
 //BA.debugLineNum = 264;BA.debugLine="scroll_profile_pnl.Initialize(90%y)";
mostCurrent._scroll_profile_pnl.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 265;BA.debugLine="profile_panel.Initialize(\"profile_panel\")";
mostCurrent._profile_panel.Initialize(mostCurrent.activityBA,"profile_panel");
 //BA.debugLineNum = 266;BA.debugLine="scroll_profile_pnl.Panel.LoadLayout(\"update_all";
mostCurrent._scroll_profile_pnl.getPanel().LoadLayout("update_all_inputs",mostCurrent.activityBA);
 //BA.debugLineNum = 267;BA.debugLine="scroll_profile_pnl.Color = Colors.Transparent";
mostCurrent._scroll_profile_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 268;BA.debugLine="all_inputs.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._all_inputs.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 270;BA.debugLine="lab_fullname.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._lab_fullname.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-4-user.png").getObject()));
 //BA.debugLineNum = 271;BA.debugLine="lab_bloodgroup.SetBackgroundImage(LoadBitmap(Fil";
mostCurrent._lab_bloodgroup.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-93-tint.png").getObject()));
 //BA.debugLineNum = 272;BA.debugLine="lab_email.SetBackgroundImage(LoadBitmap(File.Dir";
mostCurrent._lab_email.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-social-40-e-mail.png").getObject()));
 //BA.debugLineNum = 273;BA.debugLine="lab_phonenumber.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._lab_phonenumber.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt1.png").getObject()));
 //BA.debugLineNum = 274;BA.debugLine="lab_phonenumber2.SetBackgroundImage(LoadBitmap(F";
mostCurrent._lab_phonenumber2.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt2.png").getObject()));
 //BA.debugLineNum = 275;BA.debugLine="lab_location.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._lab_location.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-21-home.png").getObject()));
 //BA.debugLineNum = 276;BA.debugLine="lab_question.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._lab_question.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-353-nameplate.png").getObject()));
 //BA.debugLineNum = 277;BA.debugLine="lab_donate_confirm.SetBackgroundImage(LoadBitmap";
mostCurrent._lab_donate_confirm.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-152-new-window.png").getObject()));
 //BA.debugLineNum = 278;BA.debugLine="lab_bday.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._lab_bday.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-46-calendar.png").getObject()));
 //BA.debugLineNum = 281;BA.debugLine="all_inputs.Width = scroll_profile_pnl.Panel.Widt";
mostCurrent._all_inputs.setWidth(mostCurrent._scroll_profile_pnl.getPanel().getWidth());
 //BA.debugLineNum = 282;BA.debugLine="all_inputs.Height = scroll_profile_pnl.Panel.Hei";
mostCurrent._all_inputs.setHeight(mostCurrent._scroll_profile_pnl.getPanel().getHeight());
 //BA.debugLineNum = 286;BA.debugLine="profile_all_body.Color = Colors.ARGB(128,128,128";
mostCurrent._profile_all_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (128),(int) (128),(int) (.50)));
 //BA.debugLineNum = 287;BA.debugLine="profile_all_body.AddView(scroll_profile_pnl,5%x,";
mostCurrent._profile_all_body.AddView((android.view.View)(mostCurrent._scroll_profile_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 288;BA.debugLine="Activity.AddView(profile_all_body,0,0,100%x,100%";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._profile_all_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 290;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 291;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 292;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 293;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 294;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 295;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 296;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 297;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 298;BA.debugLine="update_btn.Background = V_btn";
mostCurrent._update_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 299;BA.debugLine="cancel_btn.Background = C_btn";
mostCurrent._cancel_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 300;BA.debugLine="End Sub";
return "";
}
public static String  _profiled_click() throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _title = null;
anywheresoftware.b4a.objects.EditTextWrapper _fulln = null;
anywheresoftware.b4a.objects.LabelWrapper _blood_sel = null;
anywheresoftware.b4a.objects.EditTextWrapper _email = null;
anywheresoftware.b4a.objects.EditTextWrapper _phone1 = null;
anywheresoftware.b4a.objects.EditTextWrapper _phone2 = null;
anywheresoftware.b4a.objects.LabelWrapper _location = null;
anywheresoftware.b4a.objects.LabelWrapper _bday = null;
anywheresoftware.b4a.objects.EditTextWrapper _nickn = null;
anywheresoftware.b4a.objects.LabelWrapper _isdonated = null;
anywheresoftware.b4a.objects.ImageViewWrapper _img_fulln = null;
anywheresoftware.b4a.objects.ImageViewWrapper _img_blood_sel = null;
anywheresoftware.b4a.objects.ImageViewWrapper _img_email = null;
anywheresoftware.b4a.objects.ImageViewWrapper _img_phone1 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _img_phone2 = null;
anywheresoftware.b4a.objects.ImageViewWrapper _img_location = null;
anywheresoftware.b4a.objects.ImageViewWrapper _img_bday = null;
anywheresoftware.b4a.objects.ImageViewWrapper _img_nickn = null;
anywheresoftware.b4a.objects.ImageViewWrapper _img_isdonated = null;
 //BA.debugLineNum = 413;BA.debugLine="Sub profiled_Click";
 //BA.debugLineNum = 415;BA.debugLine="profile_panel.Initialize(\"profile_panel\")";
mostCurrent._profile_panel.Initialize(mostCurrent.activityBA,"profile_panel");
 //BA.debugLineNum = 417;BA.debugLine="profile_panel.SetBackgroundImage(LoadBitmap(File";
mostCurrent._profile_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 418;BA.debugLine="Dim title As Label : title.Initialize(\"\")";
_title = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 418;BA.debugLine="Dim title As Label : title.Initialize(\"\")";
_title.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 419;BA.debugLine="Dim fullN As EditText : fullN.Initialize(\"\")";
_fulln = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 419;BA.debugLine="Dim fullN As EditText : fullN.Initialize(\"\")";
_fulln.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 420;BA.debugLine="Dim blood_sel As Label : blood_sel.Initialize(\"\"";
_blood_sel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 420;BA.debugLine="Dim blood_sel As Label : blood_sel.Initialize(\"\"";
_blood_sel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 421;BA.debugLine="Dim email As EditText : email.Initialize(\"\")";
_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 421;BA.debugLine="Dim email As EditText : email.Initialize(\"\")";
_email.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 422;BA.debugLine="Dim phone1 As EditText : phone1.Initialize(\"\")";
_phone1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 422;BA.debugLine="Dim phone1 As EditText : phone1.Initialize(\"\")";
_phone1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 423;BA.debugLine="Dim phone2 As EditText : phone2.Initialize(\"\")";
_phone2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 423;BA.debugLine="Dim phone2 As EditText : phone2.Initialize(\"\")";
_phone2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 424;BA.debugLine="Dim location As Label : location.Initialize(\"\")";
_location = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 424;BA.debugLine="Dim location As Label : location.Initialize(\"\")";
_location.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 425;BA.debugLine="Dim bday As Label : bday.Initialize(\"\")";
_bday = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 425;BA.debugLine="Dim bday As Label : bday.Initialize(\"\")";
_bday.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 426;BA.debugLine="Dim nickN As EditText : nickN.Initialize(\"\")";
_nickn = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 426;BA.debugLine="Dim nickN As EditText : nickN.Initialize(\"\")";
_nickn.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 427;BA.debugLine="Dim isDonated As Label : isDonated.Initialize(\"\"";
_isdonated = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 427;BA.debugLine="Dim isDonated As Label : isDonated.Initialize(\"\"";
_isdonated.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 428;BA.debugLine="Dim img_fullN As ImageView : img_fullN.Initiali";
_img_fulln = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 428;BA.debugLine="Dim img_fullN As ImageView : img_fullN.Initiali";
_img_fulln.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 429;BA.debugLine="Dim img_blood_sel As ImageView : img_blood_sel.";
_img_blood_sel = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 429;BA.debugLine="Dim img_blood_sel As ImageView : img_blood_sel.";
_img_blood_sel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 430;BA.debugLine="Dim img_email As ImageView : img_email.Initiali";
_img_email = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 430;BA.debugLine="Dim img_email As ImageView : img_email.Initiali";
_img_email.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 431;BA.debugLine="Dim img_phone1 As ImageView : img_phone1.Initia";
_img_phone1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 431;BA.debugLine="Dim img_phone1 As ImageView : img_phone1.Initia";
_img_phone1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 432;BA.debugLine="Dim img_phone2 As ImageView : img_phone2.Initia";
_img_phone2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 432;BA.debugLine="Dim img_phone2 As ImageView : img_phone2.Initia";
_img_phone2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 433;BA.debugLine="Dim img_location As ImageView : img_location.In";
_img_location = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 433;BA.debugLine="Dim img_location As ImageView : img_location.In";
_img_location.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 434;BA.debugLine="Dim img_bday As ImageView : img_bday.Initialize";
_img_bday = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 434;BA.debugLine="Dim img_bday As ImageView : img_bday.Initialize";
_img_bday.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 435;BA.debugLine="Dim img_nickN As ImageView : img_nickN.Initiali";
_img_nickn = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 435;BA.debugLine="Dim img_nickN As ImageView : img_nickN.Initiali";
_img_nickn.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 436;BA.debugLine="Dim img_isDonated As ImageView : img_isDonated.";
_img_isdonated = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 436;BA.debugLine="Dim img_isDonated As ImageView : img_isDonated.";
_img_isdonated.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 438;BA.debugLine="title.Text = \"My Information\" '' titte";
_title.setText((Object)("My Information"));
 //BA.debugLineNum = 439;BA.debugLine="title.Gravity = Gravity.CENTER";
_title.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 440;BA.debugLine="title.TextSize = 20 '''-------";
_title.setTextSize((float) (20));
 //BA.debugLineNum = 442;BA.debugLine="blood_sel.Text = \"A\"";
_blood_sel.setText((Object)("A"));
 //BA.debugLineNum = 443;BA.debugLine="bday.Text = \"may/13/1993\"";
_bday.setText((Object)("may/13/1993"));
 //BA.debugLineNum = 444;BA.debugLine="location.Text = \"hinigaran neg occ\"";
_location.setText((Object)("hinigaran neg occ"));
 //BA.debugLineNum = 445;BA.debugLine="img_fullN.SetBackgroundImage(LoadBitmap(File.Di";
_img_fulln.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-4-user.png").getObject()));
 //BA.debugLineNum = 446;BA.debugLine="img_blood_sel.SetBackgroundImage(LoadBitmap(Fil";
_img_blood_sel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-93-tint.png").getObject()));
 //BA.debugLineNum = 447;BA.debugLine="img_email.SetBackgroundImage(LoadBitmap(File.Di";
_img_email.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-social-40-e-mail.png").getObject()));
 //BA.debugLineNum = 448;BA.debugLine="img_phone1.SetBackgroundImage(LoadBitmap(File.D";
_img_phone1.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt1.png").getObject()));
 //BA.debugLineNum = 449;BA.debugLine="img_phone2.SetBackgroundImage(LoadBitmap(File.D";
_img_phone2.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt2.png").getObject()));
 //BA.debugLineNum = 450;BA.debugLine="img_location.SetBackgroundImage(LoadBitmap(File";
_img_location.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-21-home.png").getObject()));
 //BA.debugLineNum = 451;BA.debugLine="img_bday.SetBackgroundImage(LoadBitmap(File.Dir";
_img_bday.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-40-notes.png").getObject()));
 //BA.debugLineNum = 452;BA.debugLine="img_nickN.SetBackgroundImage(LoadBitmap(File.Di";
_img_nickn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-265-vcard.png").getObject()));
 //BA.debugLineNum = 453;BA.debugLine="img_isDonated.SetBackgroundImage(LoadBitmap(Fil";
_img_isdonated.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-91-eyedropper.png").getObject()));
 //BA.debugLineNum = 456;BA.debugLine="profile_panel.AddView(title,0,1%y,90%x,8%y) '";
mostCurrent._profile_panel.AddView((android.view.View)(_title.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 457;BA.debugLine="profile_panel.AddView(img_fullN,5%x, title.Top+t";
mostCurrent._profile_panel.AddView((android.view.View)(_img_fulln.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_title.getTop()+_title.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 458;BA.debugLine="profile_panel.AddView(fullN, img_fullN.Left+img_";
mostCurrent._profile_panel.AddView((android.view.View)(_fulln.getObject()),(int) (_img_fulln.getLeft()+_img_fulln.getWidth()),(int) (_title.getTop()+_title.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 460;BA.debugLine="profile_panel.AddView(img_blood_sel,5%x,fullN.To";
mostCurrent._profile_panel.AddView((android.view.View)(_img_blood_sel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_fulln.getTop()+_fulln.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 461;BA.debugLine="profile_panel.AddView(blood_sel,img_blood_sel.Le";
mostCurrent._profile_panel.AddView((android.view.View)(_blood_sel.getObject()),(int) (_img_blood_sel.getLeft()+_img_blood_sel.getWidth()),_img_blood_sel.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 463;BA.debugLine="profile_panel.AddView(img_email,5%x,img_blood_se";
mostCurrent._profile_panel.AddView((android.view.View)(_img_email.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_blood_sel.getTop()+_img_blood_sel.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 464;BA.debugLine="profile_panel.AddView(email,img_email.Left+img_e";
mostCurrent._profile_panel.AddView((android.view.View)(_email.getObject()),(int) (_img_email.getLeft()+_img_email.getWidth()),_img_email.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 466;BA.debugLine="profile_panel.AddView(img_phone1,5%x,img_email.T";
mostCurrent._profile_panel.AddView((android.view.View)(_img_phone1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_email.getTop()+_img_email.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 467;BA.debugLine="profile_panel.AddView(phone1,img_phone1.Left+img";
mostCurrent._profile_panel.AddView((android.view.View)(_phone1.getObject()),(int) (_img_phone1.getLeft()+_img_phone1.getWidth()),_img_phone1.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 469;BA.debugLine="profile_panel.AddView(img_phone2,5%x,img_phone1.";
mostCurrent._profile_panel.AddView((android.view.View)(_img_phone2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_phone1.getTop()+_img_phone1.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 470;BA.debugLine="profile_panel.AddView(phone2,img_phone2.Left+img";
mostCurrent._profile_panel.AddView((android.view.View)(_phone2.getObject()),(int) (_img_phone2.getLeft()+_img_phone2.getWidth()),_img_phone2.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 472;BA.debugLine="profile_panel.AddView(img_location,5%x,img_phone";
mostCurrent._profile_panel.AddView((android.view.View)(_img_location.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_phone2.getTop()+_img_phone2.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 473;BA.debugLine="profile_panel.AddView(location,img_location.Left";
mostCurrent._profile_panel.AddView((android.view.View)(_location.getObject()),(int) (_img_location.getLeft()+_img_location.getWidth()),_img_location.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 475;BA.debugLine="profile_panel.AddView(img_bday,5%x,img_location.";
mostCurrent._profile_panel.AddView((android.view.View)(_img_bday.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_location.getTop()+_img_location.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 476;BA.debugLine="profile_panel.AddView(bday,img_bday.Left+img_bda";
mostCurrent._profile_panel.AddView((android.view.View)(_bday.getObject()),(int) (_img_bday.getLeft()+_img_bday.getWidth()),_img_bday.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 478;BA.debugLine="profile_panel.AddView(img_nickN,5%x,img_bday.Top";
mostCurrent._profile_panel.AddView((android.view.View)(_img_nickn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_bday.getTop()+_img_bday.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 479;BA.debugLine="profile_panel.AddView(nickN,img_nickN.Left+img_n";
mostCurrent._profile_panel.AddView((android.view.View)(_nickn.getObject()),(int) (_img_nickn.getLeft()+_img_nickn.getWidth()),_img_nickn.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 481;BA.debugLine="profile_panel.AddView(img_isDonated,5%x,img_nick";
mostCurrent._profile_panel.AddView((android.view.View)(_img_isdonated.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_nickn.getTop()+_img_nickn.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 482;BA.debugLine="profile_panel.AddView(isDonated,img_isDonated.Le";
mostCurrent._profile_panel.AddView((android.view.View)(_isdonated.getObject()),(int) (_img_isdonated.getLeft()+_img_isdonated.getWidth()),_img_isdonated.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 485;BA.debugLine="scroll_profile_pnl.Panel.AddView(profile_panel,0";
mostCurrent._scroll_profile_pnl.getPanel().AddView((android.view.View)(mostCurrent._profile_panel.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 486;BA.debugLine="Activity.AddView(scroll_profile_pnl,5%x,3%y,90%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._scroll_profile_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 488;BA.debugLine="End Sub";
return "";
}
public static String  _search_blood_click() throws Exception{
 //BA.debugLineNum = 245;BA.debugLine="Sub search_blood_Click";
 //BA.debugLineNum = 246;BA.debugLine="StartActivity(\"search_frame\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("search_frame"));
 //BA.debugLineNum = 247;BA.debugLine="End Sub";
return "";
}
public static String  _spin_bloodgroup_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 721;BA.debugLine="Sub spin_bloodgroup_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 722;BA.debugLine="blood_selected = Value";
_blood_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 723;BA.debugLine="End Sub";
return "";
}
public static String  _spin_day_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 636;BA.debugLine="Sub spin_day_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 637;BA.debugLine="bday_day_selected = Value";
_bday_day_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 638;BA.debugLine="Log(\"day: \"&Value)";
anywheresoftware.b4a.keywords.Common.Log("day: "+BA.ObjectToString(_value));
 //BA.debugLineNum = 639;BA.debugLine="End Sub";
return "";
}
public static String  _spin_donated_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 567;BA.debugLine="Sub spin_donated_ItemClick (Position As Int, Value";
 //BA.debugLineNum = 568;BA.debugLine="is_donated = Value";
_is_donated = BA.ObjectToString(_value);
 //BA.debugLineNum = 569;BA.debugLine="End Sub";
return "";
}
public static String  _spin_month_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 640;BA.debugLine="Sub spin_month_ItemClick (Position As Int, Value A";
 //BA.debugLineNum = 641;BA.debugLine="bday_month_selected = Value";
_bday_month_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 642;BA.debugLine="Log(\"month: \"&Value)";
anywheresoftware.b4a.keywords.Common.Log("month: "+BA.ObjectToString(_value));
 //BA.debugLineNum = 643;BA.debugLine="End Sub";
return "";
}
public static String  _spin_year_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 644;BA.debugLine="Sub spin_year_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 645;BA.debugLine="bday_year_selected =  Value";
_bday_year_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 646;BA.debugLine="Log(\"year: \"&Value)";
anywheresoftware.b4a.keywords.Common.Log("year: "+BA.ObjectToString(_value));
 //BA.debugLineNum = 647;BA.debugLine="End Sub";
return "";
}
public static String  _street_lat_lng() throws Exception{
 //BA.debugLineNum = 815;BA.debugLine="Sub street_lat_lng";
 //BA.debugLineNum = 816;BA.debugLine="If brgy_index == 0 And street_index == 0 Then";
if (_brgy_index==0 && _street_index==0) { 
 //BA.debugLineNum = 817;BA.debugLine="lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 818;BA.debugLine="lng = \"122.869168\"";
_lng = "122.869168";
 }else if(_brgy_index==0 && _street_index==1) { 
 //BA.debugLineNum = 820;BA.debugLine="lat = \"10.097226\"";
_lat = "10.097226";
 //BA.debugLineNum = 821;BA.debugLine="lng = \"122.870659\"";
_lng = "122.870659";
 }else if(_brgy_index==0 && _street_index==2) { 
 //BA.debugLineNum = 823;BA.debugLine="lat = \"10.097711\"";
_lat = "10.097711";
 //BA.debugLineNum = 824;BA.debugLine="lng = \"122.868378\"";
_lng = "122.868378";
 }else if(_brgy_index==0 && _street_index==3) { 
 //BA.debugLineNum = 826;BA.debugLine="lat = \"10.098293\"";
_lat = "10.098293";
 //BA.debugLineNum = 827;BA.debugLine="lng = \"122.868977\"";
_lng = "122.868977";
 }else if(_brgy_index==0 && _street_index==4) { 
 //BA.debugLineNum = 829;BA.debugLine="lat = \"10.097031\"";
_lat = "10.097031";
 //BA.debugLineNum = 830;BA.debugLine="lng = \"122.868764\"";
_lng = "122.868764";
 }else if(_brgy_index==0 && _street_index==5) { 
 //BA.debugLineNum = 832;BA.debugLine="lat = \"10.096021\"";
_lat = "10.096021";
 //BA.debugLineNum = 833;BA.debugLine="lng = \"122.869737\"";
_lng = "122.869737";
 }else if(_brgy_index==0 && _street_index==6) { 
 //BA.debugLineNum = 835;BA.debugLine="lat = \"10.095142\"";
_lat = "10.095142";
 //BA.debugLineNum = 836;BA.debugLine="lng = \"122.868317\"";
_lng = "122.868317";
 }else if(_brgy_index==0 && _street_index==7) { 
 //BA.debugLineNum = 838;BA.debugLine="lat = \"10.095303\"";
_lat = "10.095303";
 //BA.debugLineNum = 839;BA.debugLine="lng = \"122.869509\"";
_lng = "122.869509";
 };
 //BA.debugLineNum = 842;BA.debugLine="If brgy_index == 1 And street_index == 0 Then 'br";
if (_brgy_index==1 && _street_index==0) { 
 //BA.debugLineNum = 843;BA.debugLine="lat = \"10.101356\"";
_lat = "10.101356";
 //BA.debugLineNum = 844;BA.debugLine="lng = \"122.870075\"";
_lng = "122.870075";
 }else if(_brgy_index==1 && _street_index==1) { 
 //BA.debugLineNum = 846;BA.debugLine="lat = \"10.100583\"";
_lat = "10.100583";
 //BA.debugLineNum = 847;BA.debugLine="lng = \"122.870176\"";
_lng = "122.870176";
 }else if(_brgy_index==1 && _street_index==2) { 
 //BA.debugLineNum = 849;BA.debugLine="lat = \"10.100031\"";
_lat = "10.100031";
 //BA.debugLineNum = 850;BA.debugLine="lng = \"122.870623\"";
_lng = "122.870623";
 }else if(_brgy_index==1 && _street_index==3) { 
 //BA.debugLineNum = 852;BA.debugLine="lat = \"10.101327\"";
_lat = "10.101327";
 //BA.debugLineNum = 853;BA.debugLine="lng = \"122.871177\"";
_lng = "122.871177";
 }else if(_brgy_index==1 && _street_index==4) { 
 //BA.debugLineNum = 855;BA.debugLine="lat = \"10.103330\"";
_lat = "10.103330";
 //BA.debugLineNum = 856;BA.debugLine="lng = \"122.871391\"";
_lng = "122.871391";
 }else if(_brgy_index==1 && _street_index==5) { 
 //BA.debugLineNum = 858;BA.debugLine="lat = \"10.102317\"";
_lat = "10.102317";
 //BA.debugLineNum = 859;BA.debugLine="lng = \"122.870755\"";
_lng = "122.870755";
 }else if(_brgy_index==1 && _street_index==6) { 
 //BA.debugLineNum = 861;BA.debugLine="lat = \"10.104250\"";
_lat = "10.104250";
 //BA.debugLineNum = 862;BA.debugLine="lng = \"122.882834\"";
_lng = "122.882834";
 }else if(_brgy_index==1 && _street_index==7) { 
 //BA.debugLineNum = 864;BA.debugLine="lat = \"10.104943\"";
_lat = "10.104943";
 //BA.debugLineNum = 865;BA.debugLine="lng = \"122.885207\"";
_lng = "122.885207";
 }else if(_brgy_index==1 && _street_index==8) { 
 //BA.debugLineNum = 867;BA.debugLine="lat = \"10.101843\"";
_lat = "10.101843";
 //BA.debugLineNum = 868;BA.debugLine="lng = \"122.871020\"";
_lng = "122.871020";
 }else if(_brgy_index==1 && _street_index==9) { 
 //BA.debugLineNum = 870;BA.debugLine="lat = \"10.103477\"";
_lat = "10.103477";
 //BA.debugLineNum = 871;BA.debugLine="lng = \"122.870042\"";
_lng = "122.870042";
 }else if(_brgy_index==1 && _street_index==10) { 
 //BA.debugLineNum = 873;BA.debugLine="lat = \"10.100710\"";
_lat = "10.100710";
 //BA.debugLineNum = 874;BA.debugLine="lng = \"122.870889\"";
_lng = "122.870889";
 };
 //BA.debugLineNum = 877;BA.debugLine="If brgy_index == 2 And street_index == 0 Then 'br";
if (_brgy_index==2 && _street_index==0) { 
 //BA.debugLineNum = 878;BA.debugLine="lat = \"10.095478\"";
_lat = "10.095478";
 //BA.debugLineNum = 879;BA.debugLine="lng = \"122.871176\"";
_lng = "122.871176";
 }else if(_brgy_index==2 && _street_index==1) { 
 //BA.debugLineNum = 881;BA.debugLine="lat = \"10.098599\"";
_lat = "10.098599";
 //BA.debugLineNum = 882;BA.debugLine="lng = \"122.871761\"";
_lng = "122.871761";
 }else if(_brgy_index==2 && _street_index==2) { 
 //BA.debugLineNum = 884;BA.debugLine="lat = \"10.094573\"";
_lat = "10.094573";
 //BA.debugLineNum = 885;BA.debugLine="lng = \"122.870340\"";
_lng = "122.870340";
 }else if(_brgy_index==2 && _street_index==3) { 
 //BA.debugLineNum = 887;BA.debugLine="lat = \"10.098313\"";
_lat = "10.098313";
 //BA.debugLineNum = 888;BA.debugLine="lng = \"122.875223\"";
_lng = "122.875223";
 }else if(_brgy_index==2 && _street_index==4) { 
 //BA.debugLineNum = 890;BA.debugLine="lat = \"10.092235\"";
_lat = "10.092235";
 //BA.debugLineNum = 891;BA.debugLine="lng = \"122.874356\"";
_lng = "122.874356";
 }else if(_brgy_index==2 && _street_index==5) { 
 //BA.debugLineNum = 893;BA.debugLine="lat = \"10.103982\"";
_lat = "10.103982";
 //BA.debugLineNum = 894;BA.debugLine="lng = \"122.885996\"";
_lng = "122.885996";
 }else if(_brgy_index==2 && _street_index==6) { 
 //BA.debugLineNum = 896;BA.debugLine="lat = \"10.102170\"";
_lat = "10.102170";
 //BA.debugLineNum = 897;BA.debugLine="lng = \"122.882390\"";
_lng = "122.882390";
 }else if(_brgy_index==2 && _street_index==7) { 
 //BA.debugLineNum = 899;BA.debugLine="lat = \"10.103272\"";
_lat = "10.103272";
 //BA.debugLineNum = 900;BA.debugLine="lng = \"122.883948\"";
_lng = "122.883948";
 }else if(_brgy_index==2 && _street_index==8) { 
 //BA.debugLineNum = 902;BA.debugLine="lat = \"10.103849\"";
_lat = "10.103849";
 //BA.debugLineNum = 903;BA.debugLine="lng = \"122.884602\"";
_lng = "122.884602";
 }else if(_brgy_index==2 && _street_index==9) { 
 //BA.debugLineNum = 905;BA.debugLine="lat = \"10.101033\"";
_lat = "10.101033";
 //BA.debugLineNum = 906;BA.debugLine="lng = \"122.874480\"";
_lng = "122.874480";
 };
 //BA.debugLineNum = 909;BA.debugLine="If brgy_index == 3 And street_index == 0 Then 'b";
if (_brgy_index==3 && _street_index==0) { 
 //BA.debugLineNum = 910;BA.debugLine="lat = \"10.121855\"";
_lat = "10.121855";
 //BA.debugLineNum = 911;BA.debugLine="lng = \"122.872266\"";
_lng = "122.872266";
 }else if(_brgy_index==3 && _street_index==1) { 
 //BA.debugLineNum = 913;BA.debugLine="lat = \"10.116699\"";
_lat = "10.116699";
 //BA.debugLineNum = 914;BA.debugLine="lng = \"122.871783\"";
_lng = "122.871783";
 }else if(_brgy_index==3 && _street_index==2) { 
 //BA.debugLineNum = 916;BA.debugLine="lat = \"10.116024\"";
_lat = "10.116024";
 //BA.debugLineNum = 917;BA.debugLine="lng = \"122.872477\"";
_lng = "122.872477";
 }else if(_brgy_index==3 && _street_index==3) { 
 //BA.debugLineNum = 919;BA.debugLine="lat = \"10.114588\"";
_lat = "10.114588";
 //BA.debugLineNum = 920;BA.debugLine="lng = \"122.872515\"";
_lng = "122.872515";
 }else if(_brgy_index==3 && _street_index==4) { 
 //BA.debugLineNum = 922;BA.debugLine="lat = \"10.112140\"";
_lat = "10.112140";
 //BA.debugLineNum = 923;BA.debugLine="lng = \"122.872161\"";
_lng = "122.872161";
 }else if(_brgy_index==3 && _street_index==5) { 
 //BA.debugLineNum = 925;BA.debugLine="lat = \"10.111531\"";
_lat = "10.111531";
 //BA.debugLineNum = 926;BA.debugLine="lng = \"122.871542\"";
_lng = "122.871542";
 }else if(_brgy_index==3 && _street_index==6) { 
 //BA.debugLineNum = 928;BA.debugLine="lat = \"10.107168\"";
_lat = "10.107168";
 //BA.debugLineNum = 929;BA.debugLine="lng = \"122.871766\"";
_lng = "122.871766";
 }else if(_brgy_index==3 && _street_index==7) { 
 //BA.debugLineNum = 931;BA.debugLine="lat = \"10.106570\"";
_lat = "10.106570";
 //BA.debugLineNum = 932;BA.debugLine="lng = \"122.875197\"";
_lng = "122.875197";
 }else if(_brgy_index==3 && _street_index==8) { 
 //BA.debugLineNum = 934;BA.debugLine="lat = \"10.105759\"";
_lat = "10.105759";
 //BA.debugLineNum = 935;BA.debugLine="lng = \"122.871537\"";
_lng = "122.871537";
 };
 //BA.debugLineNum = 938;BA.debugLine="If brgy_index == 4 And street_index == 0 Then 'A";
if (_brgy_index==4 && _street_index==0) { 
 //BA.debugLineNum = 939;BA.debugLine="lat = \"10.165214\"";
_lat = "10.165214";
 //BA.debugLineNum = 940;BA.debugLine="lng = \"122.865433\"";
_lng = "122.865433";
 }else if(_brgy_index==4 && _street_index==1) { 
 //BA.debugLineNum = 942;BA.debugLine="lat = \"10.154170\"";
_lat = "10.154170";
 //BA.debugLineNum = 943;BA.debugLine="lng = \"122.867255\"";
_lng = "122.867255";
 }else if(_brgy_index==4 && _street_index==2) { 
 //BA.debugLineNum = 945;BA.debugLine="lat = \"10.161405\"";
_lat = "10.161405";
 //BA.debugLineNum = 946;BA.debugLine="lng = \"122.862692\"";
_lng = "122.862692";
 }else if(_brgy_index==4 && _street_index==3) { 
 //BA.debugLineNum = 948;BA.debugLine="lat = \"10.168471\"";
_lat = "10.168471";
 //BA.debugLineNum = 949;BA.debugLine="lng = \"122.860955\"";
_lng = "122.860955";
 }else if(_brgy_index==4 && _street_index==4) { 
 //BA.debugLineNum = 951;BA.debugLine="lat = \"10.172481\"";
_lat = "10.172481";
 //BA.debugLineNum = 952;BA.debugLine="lng = \"122.858629\"";
_lng = "122.858629";
 }else if(_brgy_index==4 && _street_index==5) { 
 //BA.debugLineNum = 954;BA.debugLine="lat = \"10.166561\"";
_lat = "10.166561";
 //BA.debugLineNum = 955;BA.debugLine="lng = \"122.859428\"";
_lng = "122.859428";
 }else if(_brgy_index==4 && _street_index==6) { 
 //BA.debugLineNum = 957;BA.debugLine="lat = \"10.163510\"";
_lat = "10.163510";
 //BA.debugLineNum = 958;BA.debugLine="lng = \"122.860074\"";
_lng = "122.860074";
 }else if(_brgy_index==4 && _street_index==7) { 
 //BA.debugLineNum = 960;BA.debugLine="lat = \"10.161033\"";
_lat = "10.161033";
 //BA.debugLineNum = 961;BA.debugLine="lng = \"122.859773\"";
_lng = "122.859773";
 }else if(_brgy_index==4 && _street_index==8) { 
 //BA.debugLineNum = 963;BA.debugLine="lat = \"10.159280\"";
_lat = "10.159280";
 //BA.debugLineNum = 964;BA.debugLine="lng = \"122.861621\"";
_lng = "122.861621";
 }else if(_brgy_index==4 && _street_index==9) { 
 //BA.debugLineNum = 966;BA.debugLine="lat = \"10.159062\"";
_lat = "10.159062";
 //BA.debugLineNum = 967;BA.debugLine="lng = \"122.860209\"";
_lng = "122.860209";
 }else if(_brgy_index==4 && _street_index==10) { 
 //BA.debugLineNum = 969;BA.debugLine="lat = \"10.181112\"";
_lat = "10.181112";
 //BA.debugLineNum = 970;BA.debugLine="lng = \"122.864670\"";
_lng = "122.864670";
 }else if(_brgy_index==4 && _street_index==11) { 
 //BA.debugLineNum = 972;BA.debugLine="lat = \"10.167295\"";
_lat = "10.167295";
 //BA.debugLineNum = 973;BA.debugLine="lng = \"122.857858\"";
_lng = "122.857858";
 };
 //BA.debugLineNum = 976;BA.debugLine="If brgy_index == 5 And street_index == 0 Then 'ca";
if (_brgy_index==5 && _street_index==0) { 
 //BA.debugLineNum = 977;BA.debugLine="lat = \"10.092993\"";
_lat = "10.092993";
 //BA.debugLineNum = 978;BA.debugLine="lng = \"122.861694\"";
_lng = "122.861694";
 }else if(_brgy_index==5 && _street_index==1) { 
 //BA.debugLineNum = 980;BA.debugLine="lat = \"10.090587\"";
_lat = "10.090587";
 //BA.debugLineNum = 981;BA.debugLine="lng = \"122.868414\"";
_lng = "122.868414";
 }else if(_brgy_index==5 && _street_index==2) { 
 //BA.debugLineNum = 983;BA.debugLine="lat = \"10.091551\"";
_lat = "10.091551";
 //BA.debugLineNum = 984;BA.debugLine="lng = \"122.869249\"";
_lng = "122.869249";
 }else if(_brgy_index==5 && _street_index==3) { 
 //BA.debugLineNum = 986;BA.debugLine="lat = \"10.086452\"";
_lat = "10.086452";
 //BA.debugLineNum = 987;BA.debugLine="lng = \"122.865742\"";
_lng = "122.865742";
 }else if(_brgy_index==5 && _street_index==4) { 
 //BA.debugLineNum = 989;BA.debugLine="lat = \"10.083507\"";
_lat = "10.083507";
 //BA.debugLineNum = 990;BA.debugLine="lng = \"122.858928\"";
_lng = "122.858928";
 }else if(_brgy_index==5 && _street_index==5) { 
 //BA.debugLineNum = 992;BA.debugLine="lat = \"10.077131\"";
_lat = "10.077131";
 //BA.debugLineNum = 993;BA.debugLine="lng = \"122.864236\"";
_lng = "122.864236";
 }else if(_brgy_index==5 && _street_index==6) { 
 //BA.debugLineNum = 995;BA.debugLine="lat = \"10.081722\"";
_lat = "10.081722";
 //BA.debugLineNum = 996;BA.debugLine="lng = \"122.882661\"";
_lng = "122.882661";
 }else if(_brgy_index==5 && _street_index==7) { 
 //BA.debugLineNum = 998;BA.debugLine="lat = \"10.081822\"";
_lat = "10.081822";
 //BA.debugLineNum = 999;BA.debugLine="lng = \"122.868295\"";
_lng = "122.868295";
 }else if(_brgy_index==5 && _street_index==8) { 
 //BA.debugLineNum = 1001;BA.debugLine="lat = \"10.079513\"";
_lat = "10.079513";
 //BA.debugLineNum = 1002;BA.debugLine="lng = \"122.876610\"";
_lng = "122.876610";
 }else if(_brgy_index==5 && _street_index==9) { 
 //BA.debugLineNum = 1004;BA.debugLine="lat = \"10.068560\"";
_lat = "10.068560";
 //BA.debugLineNum = 1005;BA.debugLine="lng = \"122.887366\"";
_lng = "122.887366";
 }else if(_brgy_index==5 && _street_index==10) { 
 //BA.debugLineNum = 1007;BA.debugLine="lat = \"10.066934\"";
_lat = "10.066934";
 //BA.debugLineNum = 1008;BA.debugLine="lng = \"122.871963\"";
_lng = "122.871963";
 }else if(_brgy_index==5 && _street_index==11) { 
 //BA.debugLineNum = 1010;BA.debugLine="lat = \"10.064251\"";
_lat = "10.064251";
 //BA.debugLineNum = 1011;BA.debugLine="lng = \"122.883023\"";
_lng = "122.883023";
 }else if(_brgy_index==5 && _street_index==12) { 
 //BA.debugLineNum = 1013;BA.debugLine="lat = \"10.058546\"";
_lat = "10.058546";
 //BA.debugLineNum = 1014;BA.debugLine="lng = \"122.882968\"";
_lng = "122.882968";
 }else if(_brgy_index==5 && _street_index==13) { 
 //BA.debugLineNum = 1016;BA.debugLine="lat = \"10.054104\"";
_lat = "10.054104";
 //BA.debugLineNum = 1017;BA.debugLine="lng = \"122.885506\"";
_lng = "122.885506";
 }else if(_brgy_index==5 && _street_index==14) { 
 //BA.debugLineNum = 1019;BA.debugLine="lat = \"10.049464\"";
_lat = "10.049464";
 //BA.debugLineNum = 1020;BA.debugLine="lng = \"122.885667\"";
_lng = "122.885667";
 }else if(_brgy_index==5 && _street_index==15) { 
 //BA.debugLineNum = 1022;BA.debugLine="lat = \"10.041580\"";
_lat = "10.041580";
 //BA.debugLineNum = 1023;BA.debugLine="lng = \"122.900269\"";
_lng = "122.900269";
 }else if(_brgy_index==5 && _street_index==16) { 
 //BA.debugLineNum = 1025;BA.debugLine="lat = \"10.041395\"";
_lat = "10.041395";
 //BA.debugLineNum = 1026;BA.debugLine="lng = \"122.906248\"";
_lng = "122.906248";
 };
 //BA.debugLineNum = 1029;BA.debugLine="If brgy_index == 6 And street_index == 0 Then 'Bu";
if (_brgy_index==6 && _street_index==0) { 
 //BA.debugLineNum = 1030;BA.debugLine="lat = \"10.035728\"";
_lat = "10.035728";
 //BA.debugLineNum = 1031;BA.debugLine="lng = \"122.847547\"";
_lng = "122.847547";
 }else if(_brgy_index==6 && _street_index==1) { 
 //BA.debugLineNum = 1033;BA.debugLine="lat = \"10.000603\"";
_lat = "10.000603";
 //BA.debugLineNum = 1034;BA.debugLine="lng = \"122.885243\"";
_lng = "122.885243";
 }else if(_brgy_index==6 && _street_index==2) { 
 //BA.debugLineNum = 1036;BA.debugLine="lat = \"10.000521\"";
_lat = "10.000521";
 //BA.debugLineNum = 1037;BA.debugLine="lng = \"122.895867\"";
_lng = "122.895867";
 }else if(_brgy_index==6 && _street_index==3) { 
 //BA.debugLineNum = 1039;BA.debugLine="lat = \"9.943276\"";
_lat = "9.943276";
 //BA.debugLineNum = 1040;BA.debugLine="lng = \"122.975801\"";
_lng = "122.975801";
 };
 //BA.debugLineNum = 1043;BA.debugLine="If brgy_index == 7 And street_index == 0 Then '";
if (_brgy_index==7 && _street_index==0) { 
 //BA.debugLineNum = 1044;BA.debugLine="lat = \"10.156301\"";
_lat = "10.156301";
 //BA.debugLineNum = 1045;BA.debugLine="lng = \"122.941207\"";
_lng = "122.941207";
 }else if(_brgy_index==7 && _street_index==1) { 
 //BA.debugLineNum = 1047;BA.debugLine="lat = \"10.142692\"";
_lat = "10.142692";
 //BA.debugLineNum = 1048;BA.debugLine="lng = \"122.947560\"";
_lng = "122.947560";
 }else if(_brgy_index==7 && _street_index==2) { 
 //BA.debugLineNum = 1050;BA.debugLine="lat = \"10.139494\"";
_lat = "10.139494";
 //BA.debugLineNum = 1051;BA.debugLine="lng = \"122.942788\"";
_lng = "122.942788";
 }else if(_brgy_index==7 && _street_index==3) { 
 //BA.debugLineNum = 1053;BA.debugLine="lat = \"10.110265\"";
_lat = "10.110265";
 //BA.debugLineNum = 1054;BA.debugLine="lng = \"122.947908\"";
_lng = "122.947908";
 }else if(_brgy_index==7 && _street_index==4) { 
 //BA.debugLineNum = 1056;BA.debugLine="lat = \"10.127828\"";
_lat = "10.127828";
 //BA.debugLineNum = 1057;BA.debugLine="lng = \"122.950197\"";
_lng = "122.950197";
 }else if(_brgy_index==7 && _street_index==5) { 
 //BA.debugLineNum = 1059;BA.debugLine="lat = \"10.125287\"";
_lat = "10.125287";
 //BA.debugLineNum = 1060;BA.debugLine="lng = \"122.945735\"";
_lng = "122.945735";
 }else if(_brgy_index==7 && _street_index==6) { 
 //BA.debugLineNum = 1062;BA.debugLine="lat = \"10.143975\"";
_lat = "10.143975";
 //BA.debugLineNum = 1063;BA.debugLine="lng = \"122.930610\"";
_lng = "122.930610";
 }else if(_brgy_index==7 && _street_index==7) { 
 //BA.debugLineNum = 1065;BA.debugLine="lat = \"10.137563\"";
_lat = "10.137563";
 //BA.debugLineNum = 1066;BA.debugLine="lng = \"122.939870\"";
_lng = "122.939870";
 }else if(_brgy_index==7 && _street_index==8) { 
 //BA.debugLineNum = 1068;BA.debugLine="lat = \"10.150449\"";
_lat = "10.150449";
 //BA.debugLineNum = 1069;BA.debugLine="lng = \"122.933761\"";
_lng = "122.933761";
 }else if(_brgy_index==7 && _street_index==9) { 
 //BA.debugLineNum = 1071;BA.debugLine="lat = \"10.150286\"";
_lat = "10.150286";
 //BA.debugLineNum = 1072;BA.debugLine="lng = \"122.948956\"";
_lng = "122.948956";
 }else if(_brgy_index==7 && _street_index==10) { 
 //BA.debugLineNum = 1074;BA.debugLine="lat = \"10.148481\"";
_lat = "10.148481";
 //BA.debugLineNum = 1075;BA.debugLine="lng = \"122.943230\"";
_lng = "122.943230";
 }else if(_brgy_index==7 && _street_index==11) { 
 //BA.debugLineNum = 1077;BA.debugLine="lat = \"10.106200\"";
_lat = "10.106200";
 //BA.debugLineNum = 1078;BA.debugLine="lng = \"122.948051\"";
_lng = "122.948051";
 }else if(_brgy_index==7 && _street_index==12) { 
 //BA.debugLineNum = 1080;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 1081;BA.debugLine="lng = \"122.926593\"";
_lng = "122.926593";
 }else if(_brgy_index==7 && _street_index==13) { 
 //BA.debugLineNum = 1083;BA.debugLine="lat = \"10.120798\"";
_lat = "10.120798";
 //BA.debugLineNum = 1084;BA.debugLine="lng = \"122.938371\"";
_lng = "122.938371";
 }else if(_brgy_index==7 && _street_index==14) { 
 //BA.debugLineNum = 1086;BA.debugLine="lat = \"10.153217\"";
_lat = "10.153217";
 //BA.debugLineNum = 1087;BA.debugLine="lng = \"122.951714\"";
_lng = "122.951714";
 };
 //BA.debugLineNum = 1090;BA.debugLine="If brgy_index == 8 And street_index == 0 Then";
if (_brgy_index==8 && _street_index==0) { 
 //BA.debugLineNum = 1091;BA.debugLine="lat = \"10.157177\"";
_lat = "10.157177";
 //BA.debugLineNum = 1092;BA.debugLine="lng = \"122.895986\"";
_lng = "122.895986";
 }else if(_brgy_index==8 && _street_index==1) { 
 //BA.debugLineNum = 1094;BA.debugLine="lat = \"10.180004\"";
_lat = "10.180004";
 //BA.debugLineNum = 1095;BA.debugLine="lng = \"122.897999\"";
_lng = "122.897999";
 }else if(_brgy_index==8 && _street_index==2) { 
 //BA.debugLineNum = 1097;BA.debugLine="lat = \"10.192848\"";
_lat = "10.192848";
 //BA.debugLineNum = 1098;BA.debugLine="lng = \"122.900234\"";
_lng = "122.900234";
 }else if(_brgy_index==8 && _street_index==3) { 
 //BA.debugLineNum = 1100;BA.debugLine="lat = \"10.179993\"";
_lat = "10.179993";
 //BA.debugLineNum = 1101;BA.debugLine="lng = \"122.904299\"";
_lng = "122.904299";
 }else if(_brgy_index==8 && _street_index==4) { 
 //BA.debugLineNum = 1103;BA.debugLine="lat = \"10.183439\"";
_lat = "10.183439";
 //BA.debugLineNum = 1104;BA.debugLine="lng = \"122.889622\"";
_lng = "122.889622";
 };
 //BA.debugLineNum = 1107;BA.debugLine="If brgy_index == 9 And street_index == 0 Then 'Ca";
if (_brgy_index==9 && _street_index==0) { 
 //BA.debugLineNum = 1108;BA.debugLine="lat = \"10.074128\"";
_lat = "10.074128";
 //BA.debugLineNum = 1109;BA.debugLine="lng = \"122.981978\"";
_lng = "122.981978";
 }else if(_brgy_index==9 && _street_index==1) { 
 //BA.debugLineNum = 1111;BA.debugLine="lat = \"10.109208\"";
_lat = "10.109208";
 //BA.debugLineNum = 1112;BA.debugLine="lng = \"122.896717\"";
_lng = "122.896717";
 }else if(_brgy_index==9 && _street_index==2) { 
 //BA.debugLineNum = 1114;BA.debugLine="lat = \"10.097119\"";
_lat = "10.097119";
 //BA.debugLineNum = 1115;BA.debugLine="lng = \"122.947066\"";
_lng = "122.947066";
 }else if(_brgy_index==9 && _street_index==3) { 
 //BA.debugLineNum = 1117;BA.debugLine="lat = \"10.099023\"";
_lat = "10.099023";
 //BA.debugLineNum = 1118;BA.debugLine="lng = \"122.971723\"";
_lng = "122.971723";
 }else if(_brgy_index==9 && _street_index==4) { 
 //BA.debugLineNum = 1120;BA.debugLine="lat = \"10.119761\"";
_lat = "10.119761";
 //BA.debugLineNum = 1121;BA.debugLine="lng = \"122.901613\"";
_lng = "122.901613";
 }else if(_brgy_index==9 && _street_index==5) { 
 //BA.debugLineNum = 1123;BA.debugLine="lat = \"10.099402\"";
_lat = "10.099402";
 //BA.debugLineNum = 1124;BA.debugLine="lng = \"122.896454\"";
_lng = "122.896454";
 }else if(_brgy_index==9 && _street_index==6) { 
 //BA.debugLineNum = 1126;BA.debugLine="lat = \"10.097102\"";
_lat = "10.097102";
 //BA.debugLineNum = 1127;BA.debugLine="lng = \"122.922368\"";
_lng = "122.922368";
 }else if(_brgy_index==9 && _street_index==7) { 
 //BA.debugLineNum = 1129;BA.debugLine="lat = \"10.095304\"";
_lat = "10.095304";
 //BA.debugLineNum = 1130;BA.debugLine="lng = \"122.929242\"";
_lng = "122.929242";
 }else if(_brgy_index==9 && _street_index==8) { 
 //BA.debugLineNum = 1132;BA.debugLine="lat = \"10.114128\"";
_lat = "10.114128";
 //BA.debugLineNum = 1133;BA.debugLine="lng = \"122.893868\"";
_lng = "122.893868";
 };
 //BA.debugLineNum = 1136;BA.debugLine="If brgy_index == 10 And street_index == 0 Then 'L";
if (_brgy_index==10 && _street_index==0) { 
 //BA.debugLineNum = 1137;BA.debugLine="lat = \"10.1799469\"";
_lat = "10.1799469";
 //BA.debugLineNum = 1138;BA.debugLine="lng = \"122.9068577\"";
_lng = "122.9068577";
 }else if(_brgy_index==10 && _street_index==1) { 
 //BA.debugLineNum = 1140;BA.debugLine="lat = \"10.180524\"";
_lat = "10.180524";
 //BA.debugLineNum = 1141;BA.debugLine="lng = \"122.906798\"";
_lng = "122.906798";
 }else if(_brgy_index==10 && _street_index==2) { 
 //BA.debugLineNum = 1143;BA.debugLine="lat = \"10.173336\"";
_lat = "10.173336";
 //BA.debugLineNum = 1144;BA.debugLine="lng = \"122.9118842\"";
_lng = "122.9118842";
 }else if(_brgy_index==10 && _street_index==3) { 
 //BA.debugLineNum = 1146;BA.debugLine="lat = \"10.177359\"";
_lat = "10.177359";
 //BA.debugLineNum = 1147;BA.debugLine="lng = \"122.913033\"";
_lng = "122.913033";
 }else if(_brgy_index==10 && _street_index==4) { 
 //BA.debugLineNum = 1149;BA.debugLine="lat = \"10.179847\"";
_lat = "10.179847";
 //BA.debugLineNum = 1150;BA.debugLine="lng = \"122.914160\"";
_lng = "122.914160";
 }else if(_brgy_index==10 && _street_index==5) { 
 //BA.debugLineNum = 1152;BA.debugLine="lat = \"10.182718\"";
_lat = "10.182718";
 //BA.debugLineNum = 1153;BA.debugLine="lng = \"122.915228\"";
_lng = "122.915228";
 }else if(_brgy_index==10 && _street_index==6) { 
 //BA.debugLineNum = 1155;BA.debugLine="lat = \"10.186454\"";
_lat = "10.186454";
 //BA.debugLineNum = 1156;BA.debugLine="lng = \"122.916278\"";
_lng = "122.916278";
 }else if(_brgy_index==10 && _street_index==7) { 
 //BA.debugLineNum = 1158;BA.debugLine="lat = \"10.168057\"";
_lat = "10.168057";
 //BA.debugLineNum = 1159;BA.debugLine="lng = \"122.924501\"";
_lng = "122.924501";
 };
 //BA.debugLineNum = 1162;BA.debugLine="If brgy_index == 11 And street_index == 0 Then 'M";
if (_brgy_index==11 && _street_index==0) { 
 //BA.debugLineNum = 1163;BA.debugLine="lat = \"10.050418\"";
_lat = "10.050418";
 //BA.debugLineNum = 1164;BA.debugLine="lng = \"122.867097\"";
_lng = "122.867097";
 }else if(_brgy_index==11 && _street_index==1) { 
 //BA.debugLineNum = 1166;BA.debugLine="lat = \"10.027855\"";
_lat = "10.027855";
 //BA.debugLineNum = 1167;BA.debugLine="lng = \"122.906833\"";
_lng = "122.906833";
 }else if(_brgy_index==11 && _street_index==2) { 
 //BA.debugLineNum = 1169;BA.debugLine="lat = \"10.027522\"";
_lat = "10.027522";
 //BA.debugLineNum = 1170;BA.debugLine="lng = \"122.876637\"";
_lng = "122.876637";
 }else if(_brgy_index==11 && _street_index==3) { 
 //BA.debugLineNum = 1172;BA.debugLine="lat = \"10.017254\"";
_lat = "10.017254";
 //BA.debugLineNum = 1173;BA.debugLine="lng = \"122.900969\"";
_lng = "122.900969";
 }else if(_brgy_index==11 && _street_index==4) { 
 //BA.debugLineNum = 1175;BA.debugLine="lat = \"10.028535\"";
_lat = "10.028535";
 //BA.debugLineNum = 1176;BA.debugLine="lng = \"122.900364\"";
_lng = "122.900364";
 }else if(_brgy_index==11 && _street_index==5) { 
 //BA.debugLineNum = 1178;BA.debugLine="lat = \"10.025485\"";
_lat = "10.025485";
 //BA.debugLineNum = 1179;BA.debugLine="lng = \"122.890023\"";
_lng = "122.890023";
 };
 //BA.debugLineNum = 1182;BA.debugLine="If brgy_index == 12 And street_index == 0 Then 'M";
if (_brgy_index==12 && _street_index==0) { 
 //BA.debugLineNum = 1183;BA.debugLine="lat = \"10.137572\"";
_lat = "10.137572";
 //BA.debugLineNum = 1184;BA.debugLine="lng = \"122.939888\"";
_lng = "122.939888";
 }else if(_brgy_index==12 && _street_index==1) { 
 //BA.debugLineNum = 1186;BA.debugLine="lat = \"10.132195\"";
_lat = "10.132195";
 //BA.debugLineNum = 1187;BA.debugLine="lng = \"122.899837\"";
_lng = "122.899837";
 }else if(_brgy_index==12 && _street_index==2) { 
 //BA.debugLineNum = 1189;BA.debugLine="lat = \"10.123430\"";
_lat = "10.123430";
 //BA.debugLineNum = 1190;BA.debugLine="lng = \"122.892250\"";
_lng = "122.892250";
 }else if(_brgy_index==12 && _street_index==3) { 
 //BA.debugLineNum = 1192;BA.debugLine="lat = \"10.130383\"";
_lat = "10.130383";
 //BA.debugLineNum = 1193;BA.debugLine="lng = \"122.893010\"";
_lng = "122.893010";
 }else if(_brgy_index==12 && _street_index==4) { 
 //BA.debugLineNum = 1195;BA.debugLine="lat = \"10.123127\"";
_lat = "10.123127";
 //BA.debugLineNum = 1196;BA.debugLine="lng = \"122.887952\"";
_lng = "122.887952";
 }else if(_brgy_index==12 && _street_index==5) { 
 //BA.debugLineNum = 1198;BA.debugLine="lat = \"10.131098\"";
_lat = "10.131098";
 //BA.debugLineNum = 1199;BA.debugLine="lng = \"122.879801\"";
_lng = "122.879801";
 }else if(_brgy_index==12 && _street_index==6) { 
 //BA.debugLineNum = 1201;BA.debugLine="lat = \"10.137485\"";
_lat = "10.137485";
 //BA.debugLineNum = 1202;BA.debugLine="lng = \"122.911434\"";
_lng = "122.911434";
 }else if(_brgy_index==12 && _street_index==7) { 
 //BA.debugLineNum = 1204;BA.debugLine="lat = \"10.106803\"";
_lat = "10.106803";
 //BA.debugLineNum = 1205;BA.debugLine="lng = \"122.885727\"";
_lng = "122.885727";
 }else if(_brgy_index==12 && _street_index==8) { 
 //BA.debugLineNum = 1207;BA.debugLine="lat = \"10.115220\"";
_lat = "10.115220";
 //BA.debugLineNum = 1208;BA.debugLine="lng = \"122.890515\"";
_lng = "122.890515";
 }else if(_brgy_index==12 && _street_index==9) { 
 //BA.debugLineNum = 1210;BA.debugLine="lat = \"10.108754\"";
_lat = "10.108754";
 //BA.debugLineNum = 1211;BA.debugLine="lng = \"122.894130\"";
_lng = "122.894130";
 }else if(_brgy_index==12 && _street_index==10) { 
 //BA.debugLineNum = 1213;BA.debugLine="lat = \"10.149506\"";
_lat = "10.149506";
 //BA.debugLineNum = 1214;BA.debugLine="lng = \"122.897389\"";
_lng = "122.897389";
 }else if(_brgy_index==12 && _street_index==11) { 
 //BA.debugLineNum = 1216;BA.debugLine="lat = \"10.122215\"";
_lat = "10.122215";
 //BA.debugLineNum = 1217;BA.debugLine="lng = \"122.892160\"";
_lng = "122.892160";
 }else if(_brgy_index==12 && _street_index==12) { 
 //BA.debugLineNum = 1219;BA.debugLine="lat = \"10.142698\"";
_lat = "10.142698";
 //BA.debugLineNum = 1220;BA.debugLine="lng = \"122.898168\"";
_lng = "122.898168";
 };
 //BA.debugLineNum = 1223;BA.debugLine="If brgy_index == 13 And street_index == 0 Then 'N";
if (_brgy_index==13 && _street_index==0) { 
 //BA.debugLineNum = 1224;BA.debugLine="lat = \"10.161629\"";
_lat = "10.161629";
 //BA.debugLineNum = 1225;BA.debugLine="lng = \"122.872772\"";
_lng = "122.872772";
 }else if(_brgy_index==13 && _street_index==1) { 
 //BA.debugLineNum = 1227;BA.debugLine="lat = \"10.161863\"";
_lat = "10.161863";
 //BA.debugLineNum = 1228;BA.debugLine="lng = \"122.876192\"";
_lng = "122.876192";
 }else if(_brgy_index==13 && _street_index==2) { 
 //BA.debugLineNum = 1230;BA.debugLine="lat = \"10.157407\"";
_lat = "10.157407";
 //BA.debugLineNum = 1231;BA.debugLine="lng = \"122.885663\"";
_lng = "122.885663";
 }else if(_brgy_index==13 && _street_index==3) { 
 //BA.debugLineNum = 1233;BA.debugLine="lat = \"10.167497\"";
_lat = "10.167497";
 //BA.debugLineNum = 1234;BA.debugLine="lng = \"122.879777\"";
_lng = "122.879777";
 }else if(_brgy_index==13 && _street_index==4) { 
 //BA.debugLineNum = 1236;BA.debugLine="lat = \"10.176260\"";
_lat = "10.176260";
 //BA.debugLineNum = 1237;BA.debugLine="lng = \"122.880815\"";
_lng = "122.880815";
 }else if(_brgy_index==13 && _street_index==5) { 
 //BA.debugLineNum = 1239;BA.debugLine="lat = \"10.170524\"";
_lat = "10.170524";
 //BA.debugLineNum = 1240;BA.debugLine="lng = \"122.883603\"";
_lng = "122.883603";
 };
 //BA.debugLineNum = 1243;BA.debugLine="If brgy_index == 14 And street_index == 0 Then 'S";
if (_brgy_index==14 && _street_index==0) { 
 //BA.debugLineNum = 1244;BA.debugLine="lat = \"10.071514\"";
_lat = "10.071514";
 //BA.debugLineNum = 1245;BA.debugLine="lng = \"122.916010\"";
_lng = "122.916010";
 }else if(_brgy_index==14 && _street_index==1) { 
 //BA.debugLineNum = 1247;BA.debugLine="lat = \"10.069622\"";
_lat = "10.069622";
 //BA.debugLineNum = 1248;BA.debugLine="lng = \"122.909890\"";
_lng = "122.909890";
 }else if(_brgy_index==14 && _street_index==2) { 
 //BA.debugLineNum = 1250;BA.debugLine="lat = \"10.076890\"";
_lat = "10.076890";
 //BA.debugLineNum = 1251;BA.debugLine="lng = \"122.894231\"";
_lng = "122.894231";
 }else if(_brgy_index==14 && _street_index==3) { 
 //BA.debugLineNum = 1253;BA.debugLine="lat = \"10.086207\"";
_lat = "10.086207";
 //BA.debugLineNum = 1254;BA.debugLine="lng = \"122.914044\"";
_lng = "122.914044";
 }else if(_brgy_index==14 && _street_index==4) { 
 //BA.debugLineNum = 1256;BA.debugLine="lat = \"10.067393\"";
_lat = "10.067393";
 //BA.debugLineNum = 1257;BA.debugLine="lng = \"122.900935\"";
_lng = "122.900935";
 }else if(_brgy_index==14 && _street_index==5) { 
 //BA.debugLineNum = 1259;BA.debugLine="lat = \"10.071900\"";
_lat = "10.071900";
 //BA.debugLineNum = 1260;BA.debugLine="lng = \"122.906250\"";
_lng = "122.906250";
 }else if(_brgy_index==14 && _street_index==6) { 
 //BA.debugLineNum = 1262;BA.debugLine="lat = \"10.061702\"";
_lat = "10.061702";
 //BA.debugLineNum = 1263;BA.debugLine="lng = \"122.896226\"";
_lng = "122.896226";
 }else if(_brgy_index==14 && _street_index==7) { 
 //BA.debugLineNum = 1265;BA.debugLine="lat = \"10.054802\"";
_lat = "10.054802";
 //BA.debugLineNum = 1266;BA.debugLine="lng = \"122.938688\"";
_lng = "122.938688";
 }else if(_brgy_index==14 && _street_index==8) { 
 //BA.debugLineNum = 1268;BA.debugLine="lat = \"10.071827\"";
_lat = "10.071827";
 //BA.debugLineNum = 1269;BA.debugLine="lng = \"122.921092\"";
_lng = "122.921092";
 }else if(_brgy_index==14 && _street_index==9) { 
 //BA.debugLineNum = 1271;BA.debugLine="lat = \"10.050849\"";
_lat = "10.050849";
 //BA.debugLineNum = 1272;BA.debugLine="lng = \"122.907632\"";
_lng = "122.907632";
 };
 //BA.debugLineNum = 1275;BA.debugLine="If brgy_index == 15 And street_index == 0 Then 'S";
if (_brgy_index==15 && _street_index==0) { 
 //BA.debugLineNum = 1276;BA.debugLine="lat = \"10.155844\"";
_lat = "10.155844";
 //BA.debugLineNum = 1277;BA.debugLine="lng = \"122.861129\"";
_lng = "122.861129";
 }else if(_brgy_index==15 && _street_index==1) { 
 //BA.debugLineNum = 1279;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 1280;BA.debugLine="lng = \"122.861669\"";
_lng = "122.861669";
 }else if(_brgy_index==15 && _street_index==2) { 
 //BA.debugLineNum = 1282;BA.debugLine="lat = \"10.147663\"";
_lat = "10.147663";
 //BA.debugLineNum = 1283;BA.debugLine="lng = \"122.862471\"";
_lng = "122.862471";
 }else if(_brgy_index==15 && _street_index==3) { 
 //BA.debugLineNum = 1285;BA.debugLine="lat = \"10.144440\"";
_lat = "10.144440";
 //BA.debugLineNum = 1286;BA.debugLine="lng = \"122.862524\"";
_lng = "122.862524";
 };
 //BA.debugLineNum = 1289;BA.debugLine="If brgy_index == 16 And street_index == 0 Then 'S";
if (_brgy_index==16 && _street_index==0) { 
 //BA.debugLineNum = 1290;BA.debugLine="lat = \"10.053680\"";
_lat = "10.053680";
 //BA.debugLineNum = 1291;BA.debugLine="lng = \"122.843876\"";
_lng = "122.843876";
 }else if(_brgy_index==16 && _street_index==1) { 
 //BA.debugLineNum = 1293;BA.debugLine="lat = \"10.055961\"";
_lat = "10.055961";
 //BA.debugLineNum = 1294;BA.debugLine="lng = \"122.841980\"";
_lng = "122.841980";
 }else if(_brgy_index==16 && _street_index==2) { 
 //BA.debugLineNum = 1296;BA.debugLine="lat = \"10.053363\"";
_lat = "10.053363";
 //BA.debugLineNum = 1297;BA.debugLine="lng = \"122.843295\"";
_lng = "122.843295";
 }else if(_brgy_index==16 && _street_index==3) { 
 //BA.debugLineNum = 1299;BA.debugLine="lat = \"10.053032\"";
_lat = "10.053032";
 //BA.debugLineNum = 1300;BA.debugLine="lng = \"122.842594\"";
_lng = "122.842594";
 }else if(_brgy_index==16 && _street_index==4) { 
 //BA.debugLineNum = 1302;BA.debugLine="lat = \"10.052328\"";
_lat = "10.052328";
 //BA.debugLineNum = 1303;BA.debugLine="lng = \"122.842835\"";
_lng = "122.842835";
 }else if(_brgy_index==16 && _street_index==5) { 
 //BA.debugLineNum = 1305;BA.debugLine="lat = \"10.052573\"";
_lat = "10.052573";
 //BA.debugLineNum = 1306;BA.debugLine="lng = \"122.844229\"";
_lng = "122.844229";
 }else if(_brgy_index==16 && _street_index==6) { 
 //BA.debugLineNum = 1308;BA.debugLine="lat = \"10.046957\"";
_lat = "10.046957";
 //BA.debugLineNum = 1309;BA.debugLine="lng = \"122.839610\"";
_lng = "122.839610";
 }else if(_brgy_index==16 && _street_index==7) { 
 //BA.debugLineNum = 1311;BA.debugLine="lat = \"10.035813\"";
_lat = "10.035813";
 //BA.debugLineNum = 1312;BA.debugLine="lng = \"122.835364\"";
_lng = "122.835364";
 };
 //BA.debugLineNum = 1315;BA.debugLine="If brgy_index == 17 And street_index == 0 Then 'T";
if (_brgy_index==17 && _street_index==0) { 
 //BA.debugLineNum = 1316;BA.debugLine="lat = \"10.148233\"";
_lat = "10.148233";
 //BA.debugLineNum = 1317;BA.debugLine="lng = \"122.869741\"";
_lng = "122.869741";
 }else if(_brgy_index==17 && _street_index==1) { 
 //BA.debugLineNum = 1319;BA.debugLine="lat = \"10.139867\"";
_lat = "10.139867";
 //BA.debugLineNum = 1320;BA.debugLine="lng = \"122.869882\"";
_lng = "122.869882";
 }else if(_brgy_index==17 && _street_index==2) { 
 //BA.debugLineNum = 1322;BA.debugLine="lat = \"10.126453\"";
_lat = "10.126453";
 //BA.debugLineNum = 1323;BA.debugLine="lng = \"122.868927\"";
_lng = "122.868927";
 }else if(_brgy_index==17 && _street_index==3) { 
 //BA.debugLineNum = 1325;BA.debugLine="lat = \"10.127470\"";
_lat = "10.127470";
 //BA.debugLineNum = 1326;BA.debugLine="lng = \"122.862942\"";
_lng = "122.862942";
 }else if(_brgy_index==17 && _street_index==4) { 
 //BA.debugLineNum = 1328;BA.debugLine="lat = \"10.117998\"";
_lat = "10.117998";
 //BA.debugLineNum = 1329;BA.debugLine="lng = \"122.866817\"";
_lng = "122.866817";
 }else if(_brgy_index==17 && _street_index==5) { 
 //BA.debugLineNum = 1331;BA.debugLine="lat = \"10.108173\"";
_lat = "10.108173";
 //BA.debugLineNum = 1332;BA.debugLine="lng = \"122.864592\"";
_lng = "122.864592";
 }else if(_brgy_index==17 && _street_index==6) { 
 //BA.debugLineNum = 1334;BA.debugLine="lat = \"10.126115\"";
_lat = "10.126115";
 //BA.debugLineNum = 1335;BA.debugLine="lng = \"122.871073\"";
_lng = "122.871073";
 }else if(_brgy_index==17 && _street_index==7) { 
 //BA.debugLineNum = 1337;BA.debugLine="lat = \"10.129412\"";
_lat = "10.129412";
 //BA.debugLineNum = 1338;BA.debugLine="lng = \"122.869408\"";
_lng = "122.869408";
 }else if(_brgy_index==17 && _street_index==8) { 
 //BA.debugLineNum = 1340;BA.debugLine="lat = \"10.134647\"";
_lat = "10.134647";
 //BA.debugLineNum = 1341;BA.debugLine="lng = \"122.871841\"";
_lng = "122.871841";
 }else if(_brgy_index==17 && _street_index==9) { 
 //BA.debugLineNum = 1343;BA.debugLine="lat = \"10.124801\"";
_lat = "10.124801";
 //BA.debugLineNum = 1344;BA.debugLine="lng = \"122.868277\"";
_lng = "122.868277";
 }else if(_brgy_index==17 && _street_index==10) { 
 //BA.debugLineNum = 1346;BA.debugLine="lat = \"10.124422\"";
_lat = "10.124422";
 //BA.debugLineNum = 1347;BA.debugLine="lng = \"122.866917\"";
_lng = "122.866917";
 };
 //BA.debugLineNum = 1350;BA.debugLine="If brgy_index == 18 And street_index == 0 Then 'T";
if (_brgy_index==18 && _street_index==0) { 
 //BA.debugLineNum = 1351;BA.debugLine="lat = \"10.065086\"";
_lat = "10.065086";
 //BA.debugLineNum = 1352;BA.debugLine="lng = \"122.843793\"";
_lng = "122.843793";
 }else if(_brgy_index==18 && _street_index==1) { 
 //BA.debugLineNum = 1354;BA.debugLine="lat = \"10.071356\"";
_lat = "10.071356";
 //BA.debugLineNum = 1355;BA.debugLine="lng = \"122.853102\"";
_lng = "122.853102";
 }else if(_brgy_index==18 && _street_index==2) { 
 //BA.debugLineNum = 1357;BA.debugLine="lat = \"10.060206\"";
_lat = "10.060206";
 //BA.debugLineNum = 1358;BA.debugLine="lng = \"122.850172\"";
_lng = "122.850172";
 }else if(_brgy_index==18 && _street_index==3) { 
 //BA.debugLineNum = 1360;BA.debugLine="lat = \"10.057640\"";
_lat = "10.057640";
 //BA.debugLineNum = 1361;BA.debugLine="lng = \"122.859242\"";
_lng = "122.859242";
 };
 //BA.debugLineNum = 1364;BA.debugLine="Log(\"lat: \"&lat&CRLF&\"lng: \"&lng)";
anywheresoftware.b4a.keywords.Common.Log("lat: "+_lat+anywheresoftware.b4a.keywords.Common.CRLF+"lng: "+_lng);
 //BA.debugLineNum = 1365;BA.debugLine="End Sub";
return "";
}
public static String  _update_all_inputs_click() throws Exception{
 //BA.debugLineNum = 301;BA.debugLine="Sub update_all_inputs_click";
 //BA.debugLineNum = 303;BA.debugLine="End Sub";
return "";
}
public static String  _update_btn_click() throws Exception{
b4a.example.calculations _url_back = null;
String _ins = "";
String _m_1 = "";
String _m_2 = "";
String _merge = "";
 //BA.debugLineNum = 503;BA.debugLine="Sub update_btn_Click";
 //BA.debugLineNum = 504;BA.debugLine="ProgressDialogShow2(\"Updating Please wait...\",Fal";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Updating Please wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 505;BA.debugLine="optionSelected = \"updated_click\"";
_optionselected = "updated_click";
 //BA.debugLineNum = 506;BA.debugLine="update_job.Initialize(\"update_job\",Me)";
mostCurrent._update_job._initialize(processBA,"update_job",menu_form.getObject());
 //BA.debugLineNum = 507;BA.debugLine="Dim url_back As calculations";
_url_back = new b4a.example.calculations();
 //BA.debugLineNum = 508;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 510;BA.debugLine="Dim ins,m_1,m_2,merge As String";
_ins = "";
_m_1 = "";
_m_2 = "";
_merge = "";
 //BA.debugLineNum = 512;BA.debugLine="If text_fn.Text == \"\"  Or text_email.Text == \"";
if ((mostCurrent._text_fn.getText()).equals("") || (mostCurrent._text_email.getText()).equals("") || (mostCurrent._text_phonenumber2.getText()).equals("") || (mostCurrent._text_phonenumber.getText()).equals("") || (mostCurrent._text_answer.getText()).equals("")) { 
 //BA.debugLineNum = 513;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 514;BA.debugLine="Msgbox(\"Error: Fill up those empty fields before";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Fill up those empty fields before you update!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 516;BA.debugLine="m_1 = \"UPDATE `bloodlife_db`.`person_info` SET `";
_m_1 = "UPDATE `bloodlife_db`.`person_info` SET `full_name`='"+mostCurrent._text_fn.getText()+"',`blood_type`='"+_blood_selected+"', `phone_number1`='"+mostCurrent._text_phonenumber.getText()+"', `phone_number2`='"+mostCurrent._text_phonenumber2.getText()+"', `location_brgy`='"+_location_brgy_selected+"', `location_street`='"+_location_street_selected+"', ";
 //BA.debugLineNum = 517;BA.debugLine="m_2 = \"`location_purok`='', `bday_month`='\"&bday";
_m_2 = "`location_purok`='', `bday_month`='"+_bday_month_selected+"',`bday_day`='"+_bday_day_selected+"', `bday_year`='"+_bday_year_selected+"', `nick_name`='"+mostCurrent._text_answer.getText()+"', `donate_boolean`='"+_is_donated+"', `lat`='"+_lat+"', `long`='"+_lng+"', `image`='"+_image_container+"' WHERE  `id`="+mostCurrent._login_form._id_query+";";
 //BA.debugLineNum = 518;BA.debugLine="merge = m_1&m_2";
_merge = _m_1+_m_2;
 //BA.debugLineNum = 519;BA.debugLine="ins = url_back.php_email_url(\"/bloodlifePHP/upda";
_ins = _url_back._php_email_url("/bloodlifePHP/updating.php");
 //BA.debugLineNum = 520;BA.debugLine="update_job.Download2(ins,Array As String(\"update";
mostCurrent._update_job._download2(_ins,new String[]{"update",""+_merge});
 };
 //BA.debugLineNum = 524;BA.debugLine="End Sub";
return "";
}
public static String  _usr_img_click() throws Exception{
String _img_string = "";
anywheresoftware.b4a.objects.StringUtils _su = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out1 = null;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _inp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
byte[] _bytes = null;
 //BA.debugLineNum = 355;BA.debugLine="Sub usr_img_click";
 //BA.debugLineNum = 356;BA.debugLine="dlgFileExpl.Initialize(Activity, \"/mnt/sdcard\", \"";
mostCurrent._dlgfileexpl._initialize(mostCurrent.activityBA,mostCurrent._activity,"/mnt/sdcard",".bmp,.gif,.jpg,.png",anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.False,"OK");
 //BA.debugLineNum = 357;BA.debugLine="dlgFileExpl.FastScrollEnabled = True";
mostCurrent._dlgfileexpl._fastscrollenabled = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 358;BA.debugLine="dlgFileExpl.Explorer2(True)";
mostCurrent._dlgfileexpl._explorer2(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 359;BA.debugLine="If Not(dlgFileExpl.Selection.Canceled Or dlgFileE";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._dlgfileexpl._selection.Canceled || (mostCurrent._dlgfileexpl._selection.ChosenFile).equals(""))) { 
 //BA.debugLineNum = 361;BA.debugLine="Dim img_string As String";
_img_string = "";
 //BA.debugLineNum = 362;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 363;BA.debugLine="Dim out1 As OutputStream";
_out1 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 365;BA.debugLine="out1.InitializeToBytesArray(0) 'size not real";
_out1.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 366;BA.debugLine="File.Copy2(File.OpenInput(dlgFileExpl.Selecti";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(mostCurrent._dlgfileexpl._selection.ChosenPath,mostCurrent._dlgfileexpl._selection.ChosenFile).getObject()),(java.io.OutputStream)(_out1.getObject()));
 //BA.debugLineNum = 367;BA.debugLine="img_string=su.EncodeBase64(out1.ToBytesArray)";
_img_string = _su.EncodeBase64(_out1.ToBytesArray());
 //BA.debugLineNum = 368;BA.debugLine="image_container = img_string";
_image_container = _img_string;
 //BA.debugLineNum = 370;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 371;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 372;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 373;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 374;BA.debugLine="bytes = su.DecodeBase64(img_string)";
_bytes = _su.DecodeBase64(_img_string);
 //BA.debugLineNum = 375;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 376;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 377;BA.debugLine="usr_img.SetBackgroundImage(bmp)";
mostCurrent._usr_img.SetBackgroundImage((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 378;BA.debugLine="Log(img_string)";
anywheresoftware.b4a.keywords.Common.Log(_img_string);
 };
 //BA.debugLineNum = 381;BA.debugLine="End Sub";
return "";
}
}
