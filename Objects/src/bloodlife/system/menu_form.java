package bloodlife.system;


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
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "bloodlife.system", "bloodlife.system.menu_form");
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
		activityBA = new BA(this, layout, processBA, "bloodlife.system", "bloodlife.system.menu_form");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "bloodlife.system.menu_form", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public static anywheresoftware.b4a.objects.collections.List _list_is_gender = null;
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
public static String _gender_string_data = "";
public static String _is_donated = "";
public static int _donated_index = 0;
public static int _is_gender_index = 0;
public static String _lat = "";
public static String _lng = "";
public static int _brgy_index = 0;
public static int _street_index = 0;
public static anywheresoftware.b4a.objects.collections.List _list_location_b = null;
public static anywheresoftware.b4a.objects.collections.List _list_location_s = null;
public static anywheresoftware.b4a.objects.collections.List _list_location_p = null;
public static String _optionselected = "";
public static String _isdonatedate = "";
public static int _donate_m_pos = 0;
public static int _donate_d_pos = 0;
public static int _donate_y_pos = 0;
public static String _image_container = "";
public static int _panel_click_ = 0;
public static int _edit_panel_click_ = 0;
public anywheresoftware.b4a.objects.SpinnerWrapper _location_spin_street = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _location_spin_brgy = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_bloodgroup = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_day = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_month = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_year = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_donated = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_gender = null;
public bloodlife.system.clsexplorer _dlgfileexpl = null;
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
public anywheresoftware.b4a.objects.PanelWrapper _pnl_gender_body = null;
public bloodlife.system.httpjob _all_info_query = null;
public bloodlife.system.httpjob _update_job = null;
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
public anywheresoftware.b4a.objects.PanelWrapper _all_inputs_top = null;
public flm.b4a.scrollview2d.ScrollView2DWrapper _scroll_myprof = null;
public anywheresoftware.b4a.objects.PanelWrapper _all_inputs_down = null;
public anywheresoftware.b4a.objects.LabelWrapper _tittle = null;
public anywheresoftware.b4a.objects.LabelWrapper _donated_edit = null;
public anywheresoftware.b4a.objects.LabelWrapper _bday_edit = null;
public anywheresoftware.b4a.objects.LabelWrapper _locate_edit = null;
public anywheresoftware.b4a.objects.LabelWrapper _blood_edit = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_gender = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_gender = null;
public anywheresoftware.b4a.objects.LabelWrapper _edit_gender = null;
public anywheresoftware.b4a.objects.PanelWrapper _about_us_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _help_us_pnl = null;
public flm.b4a.scrollview2d.ScrollView2DWrapper _about_sc2d = null;
public flm.b4a.scrollview2d.ScrollView2DWrapper _help_sc2d = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a1 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a2 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a3 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a4 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a5 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _userimage = null;
public bloodlife.system.main _main = null;
public bloodlife.system.login_form _login_form = null;
public bloodlife.system.create_account _create_account = null;
public bloodlife.system.search_frame _search_frame = null;
public bloodlife.system.help_frame _help_frame = null;
public bloodlife.system.httputils2service _httputils2service = null;
public bloodlife.system.my_profile _my_profile = null;
public bloodlife.system.about_frame _about_frame = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _about_click() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper _aa1 = null;
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _about_ok_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.LabelWrapper _about_data = null;
anywheresoftware.b4a.objects.LabelWrapper _for_h = null;
anywheresoftware.b4a.objects.StringUtils _sus = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
anywheresoftware.b4a.agraham.richstring.RichStringBuilder.RichString _rs = null;
String _f_string = "";
String _s_string = "";
int _string_h = 0;
 //BA.debugLineNum = 2021;BA.debugLine="Sub about_Click";
 //BA.debugLineNum = 2022;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 2023;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 2024;BA.debugLine="about_img.Tag = aa1";
mostCurrent._about_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 2025;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 2026;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 2027;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 2028;BA.debugLine="a3.Start(about)";
mostCurrent._a3.Start((android.view.View)(mostCurrent._about.getObject()));
 //BA.debugLineNum = 2029;BA.debugLine="aa1.Start(about_img)";
_aa1.Start((android.view.View)(mostCurrent._about_img.getObject()));
 //BA.debugLineNum = 2030;BA.debugLine="panel_click_ = 2";
_panel_click_ = (int) (2);
 //BA.debugLineNum = 2031;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2032;BA.debugLine="Dim about_ok_btn As Button";
_about_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 2033;BA.debugLine="Dim title_lbl,about_data,for_h As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_about_data = new anywheresoftware.b4a.objects.LabelWrapper();
_for_h = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 2035;BA.debugLine="for_h.Initialize(\"\")";
_for_h.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2036;BA.debugLine="Dim sus As StringUtils";
_sus = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 2041;BA.debugLine="about_us_pnl.Initialize(\"about_us_pnl\")";
mostCurrent._about_us_pnl.Initialize(mostCurrent.activityBA,"about_us_pnl");
 //BA.debugLineNum = 2042;BA.debugLine="about_ok_btn.Initialize(\"about_ok_btn\")";
_about_ok_btn.Initialize(mostCurrent.activityBA,"about_ok_btn");
 //BA.debugLineNum = 2043;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2044;BA.debugLine="about_data.Initialize(\"\")";
_about_data.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2045;BA.debugLine="about_ok_btn.Text = \"OK\"";
_about_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 2046;BA.debugLine="about_ok_btn.Typeface = Typeface.LoadFromAssets(\"";
_about_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2047;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 2048;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 2049;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 2050;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 2051;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2052;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2053;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 2054;BA.debugLine="about_ok_btn.Background = V_btn";
_about_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 2055;BA.debugLine="title_lbl.Text = \"ABOUT\"";
_title_lbl.setText((Object)("ABOUT"));
 //BA.debugLineNum = 2056;BA.debugLine="title_lbl.TextSize = 25";
_title_lbl.setTextSize((float) (25));
 //BA.debugLineNum = 2057;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hip";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2059;BA.debugLine="title_lbl.SetBackgroundImage(LoadBitmap(File.DirA";
_title_lbl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bgs.jpg").getObject()));
 //BA.debugLineNum = 2060;BA.debugLine="title_lbl.TextColor = Colors.White";
_title_lbl.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 2062;BA.debugLine="Dim rs As RichString";
_rs = new anywheresoftware.b4a.agraham.richstring.RichStringBuilder.RichString();
 //BA.debugLineNum = 2063;BA.debugLine="Dim f_string,s_string As String";
_f_string = "";
_s_string = "";
 //BA.debugLineNum = 2064;BA.debugLine="f_string = CRLF&\"•	{ib}{bg1}Becgrajhon2013{bg1}{i";
_f_string = anywheresoftware.b4a.keywords.Common.CRLF+"•	{ib}{bg1}Becgrajhon2013{bg1}{ib} was the developer and designer of the LIFEBLOOD WITH GIS mobile app. It was started when the developer saw the posted in NONESCOST IT building that there was a boy that need a certain blood to help cure his illness.";
 //BA.debugLineNum = 2065;BA.debugLine="s_string = CRLF&CRLF&\"•	{iib}{bg2}Philippines{bg2";
_s_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•	{iib}{bg2}Philippines{bg2}{iib} is facing a blood shortage and even Himamaylan City. People not only have to run from their respective place to other barangay or other city as well because they need some donor, they will be go to Bacolod City to process the blood donation procedure of the government. This LIFEBLOOD WITH GIS will help the NOT only in people of HIMAMAYLAN CITY but in other neighboring municipalities and cities in finding blood donors.";
 //BA.debugLineNum = 2066;BA.debugLine="rs.Initialize(f_string&s_string)";
_rs.Initialize((java.lang.CharSequence)(_f_string+_s_string));
 //BA.debugLineNum = 2067;BA.debugLine="rs.Style2(rs.STYLE_BOLD_ITALIC, \"{ib}\")";
_rs.Style2(_rs.STYLE_BOLD_ITALIC,"{ib}");
 //BA.debugLineNum = 2068;BA.debugLine="rs.Style2(rs.STYLE_BOLD_ITALIC, \"{iib}\")";
_rs.Style2(_rs.STYLE_BOLD_ITALIC,"{iib}");
 //BA.debugLineNum = 2071;BA.debugLine="rs.Underscore2(\"{bg1}\")";
_rs.Underscore2("{bg1}");
 //BA.debugLineNum = 2072;BA.debugLine="rs.Underscore2(\"{bg2}\")";
_rs.Underscore2("{bg2}");
 //BA.debugLineNum = 2073;BA.debugLine="about_data.Text = rs '' to set the string output";
_about_data.setText((Object)(_rs.getObject()));
 //BA.debugLineNum = 2074;BA.debugLine="about_data.Typeface = Typeface.LoadFromAssets(\"ZI";
_about_data.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGBISD.otf"));
 //BA.debugLineNum = 2075;BA.debugLine="about_data.TextSize = 17";
_about_data.setTextSize((float) (17));
 //BA.debugLineNum = 2076;BA.debugLine="for_h.Text = rs";
_for_h.setText((Object)(_rs.getObject()));
 //BA.debugLineNum = 2077;BA.debugLine="about_us_pnl.AddView(for_h,0,0,50%x,50%y)";
mostCurrent._about_us_pnl.AddView((android.view.View)(_for_h.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 2078;BA.debugLine="for_h.Visible = False";
_for_h.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2079;BA.debugLine="Dim string_h As Int : string_h= sus.MeasureMulti";
_string_h = 0;
 //BA.debugLineNum = 2079;BA.debugLine="Dim string_h As Int : string_h= sus.MeasureMulti";
_string_h = _sus.MeasureMultilineTextHeight((android.widget.TextView)(_for_h.getObject()),_for_h.getText());
 //BA.debugLineNum = 2081;BA.debugLine="about_sc2d.Initialize(68%x,string_h+7%Y,\"about_";
mostCurrent._about_sc2d.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),(int) (_string_h+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA)),"about_sc2d");
 //BA.debugLineNum = 2082;BA.debugLine="about_sc2d.ScrollbarsVisibility(False,False)";
mostCurrent._about_sc2d.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2083;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2084;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 2085;BA.debugLine="about_us_pnl.Color = Colors.Transparent";
mostCurrent._about_us_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 2086;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 2087;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,70.5%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70.5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2089;BA.debugLine="about_sc2d.Panel.AddView(about_data,2%x,0,66%x,st";
mostCurrent._about_sc2d.getPanel().AddView((android.view.View)(_about_data.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (66),mostCurrent.activityBA),(int) (_string_h+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA)));
 //BA.debugLineNum = 2090;BA.debugLine="pnl.AddView(about_sc2d,2%x,title_lbl.Top + title_";
_pnl.AddView((android.view.View)(mostCurrent._about_sc2d.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (60),mostCurrent.activityBA));
 //BA.debugLineNum = 2091;BA.debugLine="pnl.AddView(about_ok_btn,1%x,about_sc2d.Top + abo";
_pnl.AddView((android.view.View)(_about_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (mostCurrent._about_sc2d.getTop()+mostCurrent._about_sc2d.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2093;BA.debugLine="about_us_pnl.AddView(pnl,13%x,((((Activity.Height";
mostCurrent._about_us_pnl.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 2094;BA.debugLine="about_us_pnl.BringToFront";
mostCurrent._about_us_pnl.BringToFront();
 //BA.debugLineNum = 2098;BA.debugLine="Activity.AddView(about_us_pnl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._about_us_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 2099;BA.debugLine="End Sub";
return "";
}
public static String  _about_ok_btn_click() throws Exception{
 //BA.debugLineNum = 2100;BA.debugLine="Sub about_ok_btn_click";
 //BA.debugLineNum = 2101;BA.debugLine="about_us_pnl.RemoveView";
mostCurrent._about_us_pnl.RemoveView();
 //BA.debugLineNum = 2102;BA.debugLine="End Sub";
return "";
}
public static String  _about_us_pnl_click() throws Exception{
 //BA.debugLineNum = 2103;BA.debugLine="Sub about_us_pnl_click";
 //BA.debugLineNum = 2105;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 134;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 137;BA.debugLine="Activity.LoadLayout (\"menu_frame\")";
mostCurrent._activity.LoadLayout("menu_frame",mostCurrent.activityBA);
 //BA.debugLineNum = 138;BA.debugLine="Activity.Title = \"MENU\"";
mostCurrent._activity.setTitle((Object)("MENU"));
 //BA.debugLineNum = 139;BA.debugLine="load_activity_layout";
_load_activity_layout();
 //BA.debugLineNum = 140;BA.debugLine="for_btn_animation";
_for_btn_animation();
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _confirm = 0;
 //BA.debugLineNum = 474;BA.debugLine="Sub Activity_KeyPress(KeyCode As Int) As Boolean";
 //BA.debugLineNum = 476;BA.debugLine="If dlgFileExpl.IsInitialized Then";
if (mostCurrent._dlgfileexpl.IsInitialized()) { 
 //BA.debugLineNum = 477;BA.debugLine="If dlgFileExpl.IsActive Then Return True";
if (mostCurrent._dlgfileexpl._isactive()) { 
if (true) return anywheresoftware.b4a.keywords.Common.True;};
 };
 //BA.debugLineNum = 479;BA.debugLine="If KeyCode == KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 480;BA.debugLine="If panel_click_ == 0 Then";
if (_panel_click_==0) { 
 //BA.debugLineNum = 482;BA.debugLine="Dim confirm As Int";
_confirm = 0;
 //BA.debugLineNum = 483;BA.debugLine="confirm = Msgbox2(\"Would you to log out your ac";
_confirm = anywheresoftware.b4a.keywords.Common.Msgbox2("Would you to log out your account?","C O N F I R M A T I O N","YES","","NO",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 484;BA.debugLine="If confirm == DialogResponse.POSITIVE Then";
if (_confirm==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 485;BA.debugLine="login_form.is_log_in = False";
mostCurrent._login_form._is_log_in = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 486;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 //BA.debugLineNum = 487;BA.debugLine="StartActivity(\"login_form\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("login_form"));
 }else {
 };
 }else if(_panel_click_==1) { 
 //BA.debugLineNum = 495;BA.debugLine="If edit_panel_click_ == 1 Then";
if (_edit_panel_click_==1) { 
 //BA.debugLineNum = 496;BA.debugLine="pnl_blood_body.RemoveView";
mostCurrent._pnl_blood_body.RemoveView();
 //BA.debugLineNum = 497;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 }else if(_edit_panel_click_==2) { 
 //BA.debugLineNum = 499;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 500;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 }else if(_edit_panel_click_==3) { 
 //BA.debugLineNum = 502;BA.debugLine="pnl_body.RemoveView";
mostCurrent._pnl_body.RemoveView();
 //BA.debugLineNum = 503;BA.debugLine="edit_panel_click_  = 0";
_edit_panel_click_ = (int) (0);
 }else if(_edit_panel_click_==4) { 
 //BA.debugLineNum = 505;BA.debugLine="pnl_donated_body.RemoveView";
mostCurrent._pnl_donated_body.RemoveView();
 //BA.debugLineNum = 506;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 }else if(_edit_panel_click_==5) { 
 //BA.debugLineNum = 508;BA.debugLine="pnl_gender_body.RemoveView";
mostCurrent._pnl_gender_body.RemoveView();
 //BA.debugLineNum = 509;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 }else {
 //BA.debugLineNum = 511;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 512;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 //BA.debugLineNum = 513;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 };
 }else if(_panel_click_==2) { 
 //BA.debugLineNum = 517;BA.debugLine="about_us_pnl.RemoveView";
mostCurrent._about_us_pnl.RemoveView();
 //BA.debugLineNum = 518;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 }else if(_panel_click_==3) { 
 //BA.debugLineNum = 520;BA.debugLine="help_us_pnl.RemoveView";
mostCurrent._help_us_pnl.RemoveView();
 //BA.debugLineNum = 521;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 };
 };
 //BA.debugLineNum = 526;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 527;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 295;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 297;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 291;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 293;BA.debugLine="End Sub";
return "";
}
public static String  _all_input_on_list() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_phone2 = null;
String _line_phone2 = "";
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _inp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
anywheresoftware.b4a.objects.StringUtils _su = null;
byte[] _bytes = null;
 //BA.debugLineNum = 430;BA.debugLine="Sub all_input_on_list";
 //BA.debugLineNum = 431;BA.debugLine="list_all_info.Initialize";
_list_all_info.Initialize();
 //BA.debugLineNum = 432;BA.debugLine="Dim TextReader_phone2 As TextReader";
_textreader_phone2 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 433;BA.debugLine="TextReader_phone2.Initialize(File.OpenInput(Fi";
_textreader_phone2.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"users_all_info.txt").getObject()));
 //BA.debugLineNum = 434;BA.debugLine="Dim line_phone2 As String";
_line_phone2 = "";
 //BA.debugLineNum = 435;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 //BA.debugLineNum = 436;BA.debugLine="Do While line_phone2 <> Null";
while (_line_phone2!= null) {
 //BA.debugLineNum = 437;BA.debugLine="list_all_info.Add(line_phone2)";
_list_all_info.Add((Object)(_line_phone2));
 //BA.debugLineNum = 438;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 }
;
 //BA.debugLineNum = 440;BA.debugLine="TextReader_phone2.Close";
_textreader_phone2.Close();
 //BA.debugLineNum = 442;BA.debugLine="text_fn.Text = list_all_info.Get(0)";
mostCurrent._text_fn.setText(_list_all_info.Get((int) (0)));
 //BA.debugLineNum = 443;BA.debugLine="text_blood.Text = list_all_info.Get(1)";
mostCurrent._text_blood.setText(_list_all_info.Get((int) (1)));
 //BA.debugLineNum = 444;BA.debugLine="blood_selected = list_all_info.Get(1)";
_blood_selected = BA.ObjectToString(_list_all_info.Get((int) (1)));
 //BA.debugLineNum = 445;BA.debugLine="text_email.Text = list_all_info.Get(2)";
mostCurrent._text_email.setText(_list_all_info.Get((int) (2)));
 //BA.debugLineNum = 446;BA.debugLine="text_phonenumber.Text = list_all_info.Get(3)";
mostCurrent._text_phonenumber.setText(_list_all_info.Get((int) (3)));
 //BA.debugLineNum = 447;BA.debugLine="text_phonenumber2.Text = list_all_info.Get(4)";
mostCurrent._text_phonenumber2.setText(_list_all_info.Get((int) (4)));
 //BA.debugLineNum = 448;BA.debugLine="text_bday.Text = list_all_info.Get(5)&\"/\"&list_al";
mostCurrent._text_bday.setText((Object)(BA.ObjectToString(_list_all_info.Get((int) (5)))+"/"+BA.ObjectToString(_list_all_info.Get((int) (6)))+"/"+BA.ObjectToString(_list_all_info.Get((int) (7)))));
 //BA.debugLineNum = 449;BA.debugLine="bday_month_selected = list_all_info.Get(5)";
_bday_month_selected = BA.ObjectToString(_list_all_info.Get((int) (5)));
 //BA.debugLineNum = 450;BA.debugLine="bday_day_selected = list_all_info.Get(6)";
_bday_day_selected = BA.ObjectToString(_list_all_info.Get((int) (6)));
 //BA.debugLineNum = 451;BA.debugLine="bday_year_selected = list_all_info.Get(7)";
_bday_year_selected = BA.ObjectToString(_list_all_info.Get((int) (7)));
 //BA.debugLineNum = 452;BA.debugLine="text_location.Text = list_all_info.Get(8)&\", \"&li";
mostCurrent._text_location.setText((Object)(BA.ObjectToString(_list_all_info.Get((int) (8)))+", "+BA.ObjectToString(_list_all_info.Get((int) (9)))));
 //BA.debugLineNum = 453;BA.debugLine="location_brgy_selected = list_all_info.Get(8)";
_location_brgy_selected = BA.ObjectToString(_list_all_info.Get((int) (8)));
 //BA.debugLineNum = 454;BA.debugLine="location_street_selected = list_all_info.Get(";
_location_street_selected = BA.ObjectToString(_list_all_info.Get((int) (9)));
 //BA.debugLineNum = 455;BA.debugLine="text_answer.Text = list_all_info.Get(10)";
mostCurrent._text_answer.setText(_list_all_info.Get((int) (10)));
 //BA.debugLineNum = 456;BA.debugLine="text_donated.Text = list_all_info.Get(11)";
mostCurrent._text_donated.setText(_list_all_info.Get((int) (11)));
 //BA.debugLineNum = 457;BA.debugLine="is_donated = list_all_info.Get(11)";
_is_donated = BA.ObjectToString(_list_all_info.Get((int) (11)));
 //BA.debugLineNum = 458;BA.debugLine="text_gender.Text = list_all_info.Get(13)";
mostCurrent._text_gender.setText(_list_all_info.Get((int) (13)));
 //BA.debugLineNum = 459;BA.debugLine="gender_string_data = list_all_info.Get(13)";
_gender_string_data = BA.ObjectToString(_list_all_info.Get((int) (13)));
 //BA.debugLineNum = 461;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 462;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 463;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 464;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 465;BA.debugLine="bytes = su.DecodeBase64(list_all_info.Get(12))";
_bytes = _su.DecodeBase64(BA.ObjectToString(_list_all_info.Get((int) (12))));
 //BA.debugLineNum = 466;BA.debugLine="image_container = list_all_info.Get(12)";
_image_container = BA.ObjectToString(_list_all_info.Get((int) (12)));
 //BA.debugLineNum = 467;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 468;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 469;BA.debugLine="usr_img.SetBackgroundImage(bmp)";
mostCurrent._usr_img.SetBackgroundImage((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 472;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 473;BA.debugLine="End Sub";
return "";
}
public static String  _all_inputs_click() throws Exception{
 //BA.debugLineNum = 1261;BA.debugLine="Sub all_inputs_click";
 //BA.debugLineNum = 1263;BA.debugLine="End Sub";
return "";
}
public static String  _bday_edit_click() throws Exception{
int _i = 0;
int _inowyear = 0;
int _ii = 0;
int _iii = 0;
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_ok_btn = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 1015;BA.debugLine="Sub bday_edit_Click";
 //BA.debugLineNum = 1016;BA.debugLine="edit_panel_click_ = 2";
_edit_panel_click_ = (int) (2);
 //BA.debugLineNum = 1017;BA.debugLine="list_day.Initialize";
_list_day.Initialize();
 //BA.debugLineNum = 1018;BA.debugLine="list_month.Initialize";
_list_month.Initialize();
 //BA.debugLineNum = 1019;BA.debugLine="list_year.Initialize";
_list_year.Initialize();
 //BA.debugLineNum = 1020;BA.debugLine="spin_day.Initialize(\"spin_day\")";
mostCurrent._spin_day.Initialize(mostCurrent.activityBA,"spin_day");
 //BA.debugLineNum = 1021;BA.debugLine="spin_month.Initialize(\"spin_month\")";
mostCurrent._spin_month.Initialize(mostCurrent.activityBA,"spin_month");
 //BA.debugLineNum = 1022;BA.debugLine="spin_year.Initialize(\"spin_year\")";
mostCurrent._spin_year.Initialize(mostCurrent.activityBA,"spin_year");
 //BA.debugLineNum = 1023;BA.debugLine="For i = 1 To 31";
{
final int step8 = 1;
final int limit8 = (int) (31);
for (_i = (int) (1) ; (step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8); _i = ((int)(0 + _i + step8)) ) {
 //BA.debugLineNum = 1024;BA.debugLine="list_day.Add(i)";
_list_day.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 1026;BA.debugLine="Dim iNowYear As Int";
_inowyear = 0;
 //BA.debugLineNum = 1027;BA.debugLine="iNowYear = DateTime.GetYear(DateTime.Now)";
_inowyear = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1028;BA.debugLine="For ii = 1950 To DateTime.GetYear(DateTime.Now)";
{
final int step13 = 1;
final int limit13 = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
for (_ii = (int) (1950) ; (step13 > 0 && _ii <= limit13) || (step13 < 0 && _ii >= limit13); _ii = ((int)(0 + _ii + step13)) ) {
 //BA.debugLineNum = 1029;BA.debugLine="list_year.Add(iNowYear)";
_list_year.Add((Object)(_inowyear));
 //BA.debugLineNum = 1030;BA.debugLine="iNowYear = iNowYear-1";
_inowyear = (int) (_inowyear-1);
 }
};
 //BA.debugLineNum = 1032;BA.debugLine="For iii = 1 To 12";
{
final int step17 = 1;
final int limit17 = (int) (12);
for (_iii = (int) (1) ; (step17 > 0 && _iii <= limit17) || (step17 < 0 && _iii >= limit17); _iii = ((int)(0 + _iii + step17)) ) {
 //BA.debugLineNum = 1033;BA.debugLine="list_month.Add(iii)";
_list_month.Add((Object)(_iii));
 }
};
 //BA.debugLineNum = 1035;BA.debugLine="spin_day.AddAll(list_day)";
mostCurrent._spin_day.AddAll(_list_day);
 //BA.debugLineNum = 1036;BA.debugLine="spin_month.AddAll(list_month)";
mostCurrent._spin_month.AddAll(_list_month);
 //BA.debugLineNum = 1037;BA.debugLine="spin_year.AddAll(list_year)";
mostCurrent._spin_year.AddAll(_list_year);
 //BA.debugLineNum = 1038;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1039;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1040;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1041;BA.debugLine="edit_ok_btn.Initialize(\"edit_bday_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_bday_ok_btn");
 //BA.debugLineNum = 1042;BA.debugLine="edit_can_btn.Initialize(\"edit_bday_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_bday_can_btn");
 //BA.debugLineNum = 1043;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1044;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1045;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1046;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1047;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1048;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1049;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1050;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1051;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1052;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1053;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1054;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1055;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1056;BA.debugLine="title_lbl.Text = \"SELECT BIRTH DATE\"";
_title_lbl.setText((Object)("SELECT BIRTH DATE"));
 //BA.debugLineNum = 1057;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1058;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1059;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1060;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1061;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1062;BA.debugLine="pnl_bday_body.Initialize(\"pnl_bday_body\")";
mostCurrent._pnl_bday_body.Initialize(mostCurrent.activityBA,"pnl_bday_body");
 //BA.debugLineNum = 1063;BA.debugLine="pnl_bday_body.Color = Colors.Transparent";
mostCurrent._pnl_bday_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1064;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1065;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1066;BA.debugLine="pnl.AddView(spin_day,2%x,title_lbl.Top + title_lb";
_pnl.AddView((android.view.View)(mostCurrent._spin_day.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1067;BA.debugLine="pnl.AddView(spin_month,spin_day.Left+spin_day.Wid";
_pnl.AddView((android.view.View)(mostCurrent._spin_month.getObject()),(int) (mostCurrent._spin_day.getLeft()+mostCurrent._spin_day.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_day.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1068;BA.debugLine="pnl.AddView(spin_year,spin_month.Left+spin_month.";
_pnl.AddView((android.view.View)(mostCurrent._spin_year.getObject()),(int) (mostCurrent._spin_month.getLeft()+mostCurrent._spin_month.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_month.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1069;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_year.Top+spin_y";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1070;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1072;BA.debugLine="pnl_bday_body.AddView(pnl,13%x,((Activity.Height/";
mostCurrent._pnl_bday_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1073;BA.debugLine="pnl_bday_body.BringToFront";
mostCurrent._pnl_bday_body.BringToFront();
 //BA.debugLineNum = 1076;BA.debugLine="Activity.AddView(pnl_bday_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_bday_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1077;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1101;BA.debugLine="Sub blood_edit_Click";
 //BA.debugLineNum = 1103;BA.debugLine="edit_panel_click_ = 1";
_edit_panel_click_ = (int) (1);
 //BA.debugLineNum = 1104;BA.debugLine="list_bloodgroup.Initialize";
_list_bloodgroup.Initialize();
 //BA.debugLineNum = 1105;BA.debugLine="spin_bloodgroup.Initialize(\"spin_bloodgroup\")";
mostCurrent._spin_bloodgroup.Initialize(mostCurrent.activityBA,"spin_bloodgroup");
 //BA.debugLineNum = 1106;BA.debugLine="list_bloodgroup.Add(\"A\")";
_list_bloodgroup.Add((Object)("A"));
 //BA.debugLineNum = 1107;BA.debugLine="list_bloodgroup.Add(\"B\")";
_list_bloodgroup.Add((Object)("B"));
 //BA.debugLineNum = 1108;BA.debugLine="list_bloodgroup.Add(\"O\")";
_list_bloodgroup.Add((Object)("O"));
 //BA.debugLineNum = 1109;BA.debugLine="list_bloodgroup.Add(\"AB\")";
_list_bloodgroup.Add((Object)("AB"));
 //BA.debugLineNum = 1114;BA.debugLine="list_bloodgroup.Add(\"A-\")";
_list_bloodgroup.Add((Object)("A-"));
 //BA.debugLineNum = 1115;BA.debugLine="list_bloodgroup.Add(\"B-\")";
_list_bloodgroup.Add((Object)("B-"));
 //BA.debugLineNum = 1116;BA.debugLine="list_bloodgroup.Add(\"O-\")";
_list_bloodgroup.Add((Object)("O-"));
 //BA.debugLineNum = 1117;BA.debugLine="list_bloodgroup.Add(\"AB-\")";
_list_bloodgroup.Add((Object)("AB-"));
 //BA.debugLineNum = 1118;BA.debugLine="spin_bloodgroup.AddAll(list_bloodgroup)";
mostCurrent._spin_bloodgroup.AddAll(_list_bloodgroup);
 //BA.debugLineNum = 1119;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1120;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1121;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1122;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1123;BA.debugLine="edit_ok_btn.Initialize(\"edit_blood_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_blood_ok_btn");
 //BA.debugLineNum = 1124;BA.debugLine="edit_can_btn.Initialize(\"edit_blood_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_blood_can_btn");
 //BA.debugLineNum = 1125;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1126;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1127;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1128;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1129;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1130;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1131;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1132;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1133;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1134;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1135;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1136;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1137;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1138;BA.debugLine="pnl_blood_body.Initialize(\"pnl_blood_body\")";
mostCurrent._pnl_blood_body.Initialize(mostCurrent.activityBA,"pnl_blood_body");
 //BA.debugLineNum = 1139;BA.debugLine="pnl_blood_body.Color = Colors.Transparent";
mostCurrent._pnl_blood_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1140;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1141;BA.debugLine="title_lbl.Text = \"SELECT BLOOD TYPE\"";
_title_lbl.setText((Object)("SELECT BLOOD TYPE"));
 //BA.debugLineNum = 1142;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1143;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1144;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1145;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1146;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1147;BA.debugLine="pnl.AddView(spin_bloodgroup,2%x,title_lbl.Top + t";
_pnl.AddView((android.view.View)(mostCurrent._spin_bloodgroup.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1148;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_bloodgroup.Top+";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_bloodgroup.getTop()+mostCurrent._spin_bloodgroup.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1149;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_bloodgroup.getTop()+mostCurrent._spin_bloodgroup.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1151;BA.debugLine="pnl_blood_body.AddView(pnl,13%x,((Activity.Height";
mostCurrent._pnl_blood_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1152;BA.debugLine="pnl_blood_body.BringToFront";
mostCurrent._pnl_blood_body.BringToFront();
 //BA.debugLineNum = 1155;BA.debugLine="Activity.AddView(pnl_blood_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_blood_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1156;BA.debugLine="End Sub";
return "";
}
public static String  _cancel_btn_click() throws Exception{
 //BA.debugLineNum = 714;BA.debugLine="Sub cancel_btn_Click";
 //BA.debugLineNum = 715;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 716;BA.debugLine="End Sub";
return "";
}
public static String  _donate_spin_day_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 974;BA.debugLine="Sub donate_spin_day_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 975;BA.debugLine="donate_d_pos = Position";
_donate_d_pos = _position;
 //BA.debugLineNum = 977;BA.debugLine="End Sub";
return "";
}
public static String  _donate_spin_month_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 978;BA.debugLine="Sub donate_spin_month_ItemClick (Position As Int,";
 //BA.debugLineNum = 979;BA.debugLine="donate_m_pos = Position";
_donate_m_pos = _position;
 //BA.debugLineNum = 981;BA.debugLine="End Sub";
return "";
}
public static String  _donate_spin_year_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 982;BA.debugLine="Sub donate_spin_year_ItemClick (Position As Int, V";
 //BA.debugLineNum = 983;BA.debugLine="donate_y_pos = Position";
_donate_y_pos = _position;
 //BA.debugLineNum = 985;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 848;BA.debugLine="Sub donated_edit_Click";
 //BA.debugLineNum = 849;BA.debugLine="edit_panel_click_ = 4";
_edit_panel_click_ = (int) (4);
 //BA.debugLineNum = 850;BA.debugLine="list_donated.Initialize";
_list_donated.Initialize();
 //BA.debugLineNum = 851;BA.debugLine="spin_donated.Initialize(\"spin_donated\")";
mostCurrent._spin_donated.Initialize(mostCurrent.activityBA,"spin_donated");
 //BA.debugLineNum = 852;BA.debugLine="list_donated.Add(\"NO\")";
_list_donated.Add((Object)("NO"));
 //BA.debugLineNum = 853;BA.debugLine="list_donated.Add(\"YES\")";
_list_donated.Add((Object)("YES"));
 //BA.debugLineNum = 854;BA.debugLine="spin_donated.AddAll(list_donated)";
mostCurrent._spin_donated.AddAll(_list_donated);
 //BA.debugLineNum = 855;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 856;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 857;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 858;BA.debugLine="edit_ok_btn.Initialize(\"edit_donated_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_donated_ok_btn");
 //BA.debugLineNum = 859;BA.debugLine="edit_can_btn.Initialize(\"edit_donated_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_donated_can_btn");
 //BA.debugLineNum = 860;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 861;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 862;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 863;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 864;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 865;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 866;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 867;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 868;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 869;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 870;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 871;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 872;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 873;BA.debugLine="title_lbl.Text = \"SELECT DONATED STATUS\"";
_title_lbl.setText((Object)("SELECT DONATED STATUS"));
 //BA.debugLineNum = 874;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 875;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 876;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 877;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 878;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 879;BA.debugLine="pnl_donated_body.Initialize(\"pnl_donated_body\")";
mostCurrent._pnl_donated_body.Initialize(mostCurrent.activityBA,"pnl_donated_body");
 //BA.debugLineNum = 880;BA.debugLine="pnl_donated_body.Color = Colors.Transparent";
mostCurrent._pnl_donated_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 881;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 882;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 883;BA.debugLine="pnl.AddView(spin_donated,2%x,title_lbl.Top+title_";
_pnl.AddView((android.view.View)(mostCurrent._spin_donated.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 884;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_donated.Top+spi";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_donated.getTop()+mostCurrent._spin_donated.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 885;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_donated.getTop()+mostCurrent._spin_donated.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 887;BA.debugLine="pnl_donated_body.AddView(pnl,13%x,((Activity.Heig";
mostCurrent._pnl_donated_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 888;BA.debugLine="pnl_donated_body.BringToFront";
mostCurrent._pnl_donated_body.BringToFront();
 //BA.debugLineNum = 891;BA.debugLine="Activity.AddView(pnl_donated_body,0,0,100%x,100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_donated_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 892;BA.debugLine="End Sub";
return "";
}
public static String  _edit_bday_can_btn_click() throws Exception{
 //BA.debugLineNum = 1095;BA.debugLine="Sub edit_bday_can_btn_click";
 //BA.debugLineNum = 1096;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 1097;BA.debugLine="End Sub";
return "";
}
public static String  _edit_bday_ok_btn_click() throws Exception{
 //BA.debugLineNum = 1090;BA.debugLine="Sub edit_bday_ok_btn_click";
 //BA.debugLineNum = 1091;BA.debugLine="text_bday.Text = bday_month_selected&\"/\"&bday_day";
mostCurrent._text_bday.setText((Object)(_bday_month_selected+"/"+_bday_day_selected+"/"+_bday_year_selected));
 //BA.debugLineNum = 1092;BA.debugLine="Log(\"date: \"& bday_month_selected&\"/\"&bday_day_s";
anywheresoftware.b4a.keywords.Common.Log("date: "+_bday_month_selected+"/"+_bday_day_selected+"/"+_bday_year_selected);
 //BA.debugLineNum = 1093;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 1094;BA.debugLine="End Sub";
return "";
}
public static String  _edit_blood_can_btn_click() throws Exception{
 //BA.debugLineNum = 1161;BA.debugLine="Sub edit_blood_can_btn_click";
 //BA.debugLineNum = 1162;BA.debugLine="pnl_blood_body.RemoveView";
mostCurrent._pnl_blood_body.RemoveView();
 //BA.debugLineNum = 1163;BA.debugLine="End Sub";
return "";
}
public static String  _edit_blood_ok_btn_click() throws Exception{
 //BA.debugLineNum = 1157;BA.debugLine="Sub edit_blood_ok_btn_click";
 //BA.debugLineNum = 1158;BA.debugLine="text_blood.Text = blood_selected";
mostCurrent._text_blood.setText((Object)(_blood_selected));
 //BA.debugLineNum = 1159;BA.debugLine="pnl_blood_body.RemoveView";
mostCurrent._pnl_blood_body.RemoveView();
 //BA.debugLineNum = 1160;BA.debugLine="End Sub";
return "";
}
public static String  _edit_can_btn_click() throws Exception{
 //BA.debugLineNum = 1247;BA.debugLine="Sub edit_can_btn_click";
 //BA.debugLineNum = 1248;BA.debugLine="pnl_body.RemoveView";
mostCurrent._pnl_body.RemoveView();
 //BA.debugLineNum = 1249;BA.debugLine="End Sub";
return "";
}
public static String  _edit_donated_can_btn_click() throws Exception{
 //BA.debugLineNum = 1009;BA.debugLine="Sub edit_donated_can_btn_click";
 //BA.debugLineNum = 1010;BA.debugLine="pnl_donated_body.RemoveView";
mostCurrent._pnl_donated_body.RemoveView();
 //BA.debugLineNum = 1011;BA.debugLine="End Sub";
return "";
}
public static String  _edit_donated_ok_btn_click() throws Exception{
 //BA.debugLineNum = 898;BA.debugLine="Sub edit_donated_ok_btn_click";
 //BA.debugLineNum = 899;BA.debugLine="If donated_index == 0 Then";
if (_donated_index==0) { 
 //BA.debugLineNum = 900;BA.debugLine="is_donated = \"NO\"";
_is_donated = "NO";
 //BA.debugLineNum = 901;BA.debugLine="isDonateDate = \"NONE\"";
_isdonatedate = "NONE";
 //BA.debugLineNum = 902;BA.debugLine="text_donated.Text = \"NO\"";
mostCurrent._text_donated.setText((Object)("NO"));
 }else {
 //BA.debugLineNum = 904;BA.debugLine="isDonate_edit_";
_isdonate_edit_();
 };
 //BA.debugLineNum = 908;BA.debugLine="pnl_donated_body.RemoveView";
mostCurrent._pnl_donated_body.RemoveView();
 //BA.debugLineNum = 909;BA.debugLine="End Sub";
return "";
}
public static String  _edit_gender_can_btn_click() throws Exception{
 //BA.debugLineNum = 842;BA.debugLine="Sub edit_gender_can_btn_click";
 //BA.debugLineNum = 843;BA.debugLine="pnl_gender_body.RemoveView";
mostCurrent._pnl_gender_body.RemoveView();
 //BA.debugLineNum = 844;BA.debugLine="End Sub";
return "";
}
public static String  _edit_gender_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_ok_btn = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 787;BA.debugLine="Sub edit_gender_Click";
 //BA.debugLineNum = 788;BA.debugLine="edit_panel_click_ = 5";
_edit_panel_click_ = (int) (5);
 //BA.debugLineNum = 789;BA.debugLine="list_is_gender.Initialize";
_list_is_gender.Initialize();
 //BA.debugLineNum = 790;BA.debugLine="spin_gender.Initialize(\"spin_gender\")";
mostCurrent._spin_gender.Initialize(mostCurrent.activityBA,"spin_gender");
 //BA.debugLineNum = 791;BA.debugLine="list_is_gender.Add(\"Male\")";
_list_is_gender.Add((Object)("Male"));
 //BA.debugLineNum = 792;BA.debugLine="list_is_gender.Add(\"Female\")";
_list_is_gender.Add((Object)("Female"));
 //BA.debugLineNum = 793;BA.debugLine="spin_gender.AddAll(list_is_gender)";
mostCurrent._spin_gender.AddAll(_list_is_gender);
 //BA.debugLineNum = 794;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 795;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 796;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 797;BA.debugLine="edit_ok_btn.Initialize(\"edit_gender_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_gender_ok_btn");
 //BA.debugLineNum = 798;BA.debugLine="edit_can_btn.Initialize(\"edit_gender_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_gender_can_btn");
 //BA.debugLineNum = 799;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 800;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 801;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 802;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 803;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 804;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 805;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 806;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 807;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 808;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 809;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 810;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 811;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 812;BA.debugLine="title_lbl.Text = \"SELECT GENDER\"";
_title_lbl.setText((Object)("SELECT GENDER"));
 //BA.debugLineNum = 813;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 814;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 815;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 816;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 817;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 818;BA.debugLine="pnl_gender_body.Initialize(\"pnl_gender_body\")";
mostCurrent._pnl_gender_body.Initialize(mostCurrent.activityBA,"pnl_gender_body");
 //BA.debugLineNum = 819;BA.debugLine="pnl_gender_body.Color = Colors.Transparent";
mostCurrent._pnl_gender_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 820;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 821;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 822;BA.debugLine="pnl.AddView(spin_gender,2%x,title_lbl.Top+title_l";
_pnl.AddView((android.view.View)(mostCurrent._spin_gender.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 823;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_gender.Top+spin";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_gender.getTop()+mostCurrent._spin_gender.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 824;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_gender.getTop()+mostCurrent._spin_gender.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 826;BA.debugLine="pnl_gender_body.AddView(pnl,13%x,((Activity.Heigh";
mostCurrent._pnl_gender_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 827;BA.debugLine="pnl_gender_body.BringToFront";
mostCurrent._pnl_gender_body.BringToFront();
 //BA.debugLineNum = 830;BA.debugLine="Activity.AddView(pnl_gender_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_gender_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 831;BA.debugLine="End Sub";
return "";
}
public static String  _edit_gender_ok_btn_click() throws Exception{
 //BA.debugLineNum = 836;BA.debugLine="Sub edit_gender_ok_btn_click";
 //BA.debugLineNum = 838;BA.debugLine="text_gender.Text = list_is_gender.Get(is_gender_i";
mostCurrent._text_gender.setText(_list_is_gender.Get(_is_gender_index));
 //BA.debugLineNum = 839;BA.debugLine="gender_string_data = list_is_gender.Get(is_gender";
_gender_string_data = BA.ObjectToString(_list_is_gender.Get(_is_gender_index));
 //BA.debugLineNum = 840;BA.debugLine="pnl_gender_body.RemoveView";
mostCurrent._pnl_gender_body.RemoveView();
 //BA.debugLineNum = 841;BA.debugLine="End Sub";
return "";
}
public static String  _edit_ok_btn_click() throws Exception{
String _click_string = "";
 //BA.debugLineNum = 1250;BA.debugLine="Sub edit_ok_btn_click";
 //BA.debugLineNum = 1251;BA.debugLine="Dim click_string As String";
_click_string = "";
 //BA.debugLineNum = 1252;BA.debugLine="click_string = location_spin_brgy.GetItem(brgy_in";
_click_string = mostCurrent._location_spin_brgy.GetItem(_brgy_index)+", "+mostCurrent._location_spin_street.GetItem(_street_index);
 //BA.debugLineNum = 1253;BA.debugLine="location_brgy_selected = location_spin_brgy.GetIt";
_location_brgy_selected = mostCurrent._location_spin_brgy.GetItem(_brgy_index);
 //BA.debugLineNum = 1254;BA.debugLine="location_street_selected = location_spin_stree";
_location_street_selected = mostCurrent._location_spin_street.GetItem(_street_index);
 //BA.debugLineNum = 1255;BA.debugLine="text_location.Text = click_string";
mostCurrent._text_location.setText((Object)(_click_string));
 //BA.debugLineNum = 1256;BA.debugLine="pnl_body.RemoveView";
mostCurrent._pnl_body.RemoveView();
 //BA.debugLineNum = 1257;BA.debugLine="End Sub";
return "";
}
public static String  _exit_btn_click() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper _aa1 = null;
int _confirm = 0;
 //BA.debugLineNum = 689;BA.debugLine="Sub exit_btn_Click";
 //BA.debugLineNum = 690;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 691;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 692;BA.debugLine="exit_img.Tag = aa1";
mostCurrent._exit_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 693;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 694;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 695;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 696;BA.debugLine="a5.Start(exit_btn)";
mostCurrent._a5.Start((android.view.View)(mostCurrent._exit_btn.getObject()));
 //BA.debugLineNum = 697;BA.debugLine="aa1.Start(exit_img)";
_aa1.Start((android.view.View)(mostCurrent._exit_img.getObject()));
 //BA.debugLineNum = 699;BA.debugLine="If login_form.is_log_in == True Then";
if (mostCurrent._login_form._is_log_in==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 700;BA.debugLine="Dim confirm As Int";
_confirm = 0;
 //BA.debugLineNum = 701;BA.debugLine="confirm = Msgbox2(\"Would you to log out your acc";
_confirm = anywheresoftware.b4a.keywords.Common.Msgbox2("Would you to log out your account, and exit the application?","C O N F I R M A T I O N","YES","","NO",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 702;BA.debugLine="If confirm == DialogResponse.POSITIVE Then";
if (_confirm==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 703;BA.debugLine="login_form.is_log_in = False";
mostCurrent._login_form._is_log_in = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 705;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 };
 }else {
 };
 //BA.debugLineNum = 713;BA.debugLine="End Sub";
return "";
}
public static String  _for_btn_animation() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper[] _animations = null;
int _i = 0;
 //BA.debugLineNum = 142;BA.debugLine="Sub for_btn_animation";
 //BA.debugLineNum = 143;BA.debugLine="a1.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 144;BA.debugLine="a2.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a2.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 145;BA.debugLine="a3.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a3.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 146;BA.debugLine="a4.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a4.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 147;BA.debugLine="a5.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a5.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 148;BA.debugLine="search_blood.Tag = a1";
mostCurrent._search_blood.setTag((Object)(mostCurrent._a1.getObject()));
 //BA.debugLineNum = 149;BA.debugLine="profile.Tag = a2";
mostCurrent._profile.setTag((Object)(mostCurrent._a2.getObject()));
 //BA.debugLineNum = 150;BA.debugLine="about.Tag = a3";
mostCurrent._about.setTag((Object)(mostCurrent._a3.getObject()));
 //BA.debugLineNum = 151;BA.debugLine="help.Tag = a4";
mostCurrent._help.setTag((Object)(mostCurrent._a4.getObject()));
 //BA.debugLineNum = 152;BA.debugLine="exit_btn.Tag = a5";
mostCurrent._exit_btn.setTag((Object)(mostCurrent._a5.getObject()));
 //BA.debugLineNum = 153;BA.debugLine="Dim animations() As Animation";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[(int) (0)];
{
int d0 = _animations.length;
for (int i0 = 0;i0 < d0;i0++) {
_animations[i0] = new anywheresoftware.b4a.objects.AnimationWrapper();
}
}
;
 //BA.debugLineNum = 154;BA.debugLine="animations = Array As Animation(a1, a2, a3, a4, a";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[]{mostCurrent._a1,mostCurrent._a2,mostCurrent._a3,mostCurrent._a4,mostCurrent._a5};
 //BA.debugLineNum = 155;BA.debugLine="For i = 0 To animations.Length - 1";
{
final int step13 = 1;
final int limit13 = (int) (_animations.length-1);
for (_i = (int) (0) ; (step13 > 0 && _i <= limit13) || (step13 < 0 && _i >= limit13); _i = ((int)(0 + _i + step13)) ) {
 //BA.debugLineNum = 156;BA.debugLine="animations(i).Duration = 200";
_animations[_i].setDuration((long) (200));
 //BA.debugLineNum = 157;BA.debugLine="animations(i).RepeatCount = 1";
_animations[_i].setRepeatCount((int) (1));
 //BA.debugLineNum = 158;BA.debugLine="animations(i).RepeatMode = animations(i).REPEAT_";
_animations[_i].setRepeatMode(_animations[_i].REPEAT_REVERSE);
 }
};
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 43;BA.debugLine="Dim location_spin_street As Spinner";
mostCurrent._location_spin_street = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Dim location_spin_brgy As Spinner";
mostCurrent._location_spin_brgy = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Dim spin_bloodgroup As Spinner";
mostCurrent._spin_bloodgroup = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Dim spin_day,spin_month,spin_year As Spinner";
mostCurrent._spin_day = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spin_month = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spin_year = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Dim spin_donated As Spinner";
mostCurrent._spin_donated = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Dim spin_gender As Spinner";
mostCurrent._spin_gender = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Dim dlgFileExpl As ClsExplorer";
mostCurrent._dlgfileexpl = new bloodlife.system.clsexplorer();
 //BA.debugLineNum = 52;BA.debugLine="Private search_blood As Button";
mostCurrent._search_blood = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private about As Button";
mostCurrent._about = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private help As Button";
mostCurrent._help = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private exit_btn As Button";
mostCurrent._exit_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private profile As Button";
mostCurrent._profile = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private src_blood_pnl As Panel";
mostCurrent._src_blood_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private users_panel As Panel";
mostCurrent._users_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private profile_pnl As Panel";
mostCurrent._profile_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private about_pnl As Panel";
mostCurrent._about_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private help_pnl As Panel";
mostCurrent._help_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private exit_pnl As Panel";
mostCurrent._exit_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private users_out_lbl As Label";
mostCurrent._users_out_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private users_lbl As Label";
mostCurrent._users_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private ban_logo As ImageView";
mostCurrent._ban_logo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Private ban_picture As ImageView";
mostCurrent._ban_picture = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Private users_heading As Panel";
mostCurrent._users_heading = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private srch_blood_img As ImageView";
mostCurrent._srch_blood_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Private profile_img As ImageView";
mostCurrent._profile_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private about_img As ImageView";
mostCurrent._about_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Private help_img As ImageView";
mostCurrent._help_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private exit_img As ImageView";
mostCurrent._exit_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Dim profile_panel As Panel";
mostCurrent._profile_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Dim scroll_profile_pnl As ScrollView";
mostCurrent._scroll_profile_pnl = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Dim profile_all_body As Panel";
mostCurrent._profile_all_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Private all_inputs As Panel";
mostCurrent._all_inputs = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Dim pnl_body As Panel";
mostCurrent._pnl_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Dim pnl_blood_body As Panel";
mostCurrent._pnl_blood_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Dim pnl_bday_body As Panel";
mostCurrent._pnl_bday_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Dim pnl_donated_body As Panel";
mostCurrent._pnl_donated_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Dim pnl_gender_body As Panel";
mostCurrent._pnl_gender_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 85;BA.debugLine="Private all_info_query As HttpJob";
mostCurrent._all_info_query = new bloodlife.system.httpjob();
 //BA.debugLineNum = 86;BA.debugLine="Private update_job As HttpJob";
mostCurrent._update_job = new bloodlife.system.httpjob();
 //BA.debugLineNum = 88;BA.debugLine="Private lab_fullname As Label";
mostCurrent._lab_fullname = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Private lab_bloodgroup As Label";
mostCurrent._lab_bloodgroup = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Private lab_email As Label";
mostCurrent._lab_email = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 91;BA.debugLine="Private lab_phonenumber As Label";
mostCurrent._lab_phonenumber = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private lab_phonenumber2 As Label";
mostCurrent._lab_phonenumber2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Private lab_location As Label";
mostCurrent._lab_location = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Private lab_question As Label";
mostCurrent._lab_question = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 95;BA.debugLine="Private lab_donate_confirm As Label";
mostCurrent._lab_donate_confirm = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private lab_bday As Label";
mostCurrent._lab_bday = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private text_fn As EditText";
mostCurrent._text_fn = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Private text_blood As EditText";
mostCurrent._text_blood = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 101;BA.debugLine="Private text_email As EditText";
mostCurrent._text_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 102;BA.debugLine="Private text_phonenumber As EditText";
mostCurrent._text_phonenumber = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 103;BA.debugLine="Private text_phonenumber2 As EditText";
mostCurrent._text_phonenumber2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 104;BA.debugLine="Private text_bday As EditText";
mostCurrent._text_bday = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 105;BA.debugLine="Private text_location As EditText";
mostCurrent._text_location = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 106;BA.debugLine="Private text_answer As EditText";
mostCurrent._text_answer = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 107;BA.debugLine="Private text_donated As EditText";
mostCurrent._text_donated = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 108;BA.debugLine="Private cancel_btn As Button";
mostCurrent._cancel_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 109;BA.debugLine="Private update_btn As Button";
mostCurrent._update_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 110;BA.debugLine="Private usr_img As ImageView";
mostCurrent._usr_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 111;BA.debugLine="Private all_inputs_top As Panel";
mostCurrent._all_inputs_top = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 114;BA.debugLine="Private scroll_myprof As ScrollView2D";
mostCurrent._scroll_myprof = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 115;BA.debugLine="Private all_inputs_down As Panel";
mostCurrent._all_inputs_down = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 116;BA.debugLine="Private tittle As Label";
mostCurrent._tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 117;BA.debugLine="Private donated_edit As Label";
mostCurrent._donated_edit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 118;BA.debugLine="Private bday_edit As Label";
mostCurrent._bday_edit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 119;BA.debugLine="Private locate_edit As Label";
mostCurrent._locate_edit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 120;BA.debugLine="Private blood_edit As Label";
mostCurrent._blood_edit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 121;BA.debugLine="Private lab_gender As Label";
mostCurrent._lab_gender = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 122;BA.debugLine="Private text_gender As EditText";
mostCurrent._text_gender = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 123;BA.debugLine="Private edit_gender As Label";
mostCurrent._edit_gender = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 125;BA.debugLine="Private about_us_pnl As Panel";
mostCurrent._about_us_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 126;BA.debugLine="Private help_us_pnl As Panel";
mostCurrent._help_us_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 128;BA.debugLine="Dim about_sc2d As ScrollView2D";
mostCurrent._about_sc2d = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 129;BA.debugLine="Dim help_sc2d As ScrollView2D";
mostCurrent._help_sc2d = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 131;BA.debugLine="Dim a1, a2, a3, a4, a5, userImage As Animation";
mostCurrent._a1 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a2 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a3 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a4 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a5 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._userimage = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return "";
}
public static String  _help_click() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper _aa1 = null;
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _help_ok_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.LabelWrapper _help_data = null;
anywheresoftware.b4a.objects.LabelWrapper _for_h = null;
anywheresoftware.b4a.objects.StringUtils _sus = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
anywheresoftware.b4a.agraham.richstring.RichStringBuilder.RichString _rs = null;
String _f_string = "";
String _s_string = "";
String _t_string = "";
String _fo_string = "";
String _fi_string = "";
int _string_h = 0;
 //BA.debugLineNum = 2106;BA.debugLine="Sub help_Click";
 //BA.debugLineNum = 2107;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 2108;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 2109;BA.debugLine="help_img.Tag = aa1";
mostCurrent._help_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 2110;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 2111;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 2112;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 2113;BA.debugLine="a4.Start(help)";
mostCurrent._a4.Start((android.view.View)(mostCurrent._help.getObject()));
 //BA.debugLineNum = 2114;BA.debugLine="aa1.Start(help_img)";
_aa1.Start((android.view.View)(mostCurrent._help_img.getObject()));
 //BA.debugLineNum = 2115;BA.debugLine="panel_click_ = 3";
_panel_click_ = (int) (3);
 //BA.debugLineNum = 2116;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2117;BA.debugLine="Dim help_ok_btn As Button";
_help_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 2118;BA.debugLine="Dim title_lbl,help_data,for_h As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_help_data = new anywheresoftware.b4a.objects.LabelWrapper();
_for_h = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 2119;BA.debugLine="for_h.Initialize(\"\")";
_for_h.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2120;BA.debugLine="Dim sus As StringUtils";
_sus = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 2121;BA.debugLine="help_ok_btn.Initialize(\"help_ok_btn\")";
_help_ok_btn.Initialize(mostCurrent.activityBA,"help_ok_btn");
 //BA.debugLineNum = 2122;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2123;BA.debugLine="help_data.Initialize(\"\")";
_help_data.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2124;BA.debugLine="help_ok_btn.Text = \"OK\"";
_help_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 2125;BA.debugLine="help_ok_btn.Typeface = Typeface.LoadFromAssets(\"H";
_help_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2126;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 2127;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 2128;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 2129;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 2130;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2131;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2132;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 2134;BA.debugLine="help_ok_btn.Background = V_btn";
_help_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 2135;BA.debugLine="title_lbl.Text = \"HELP\"";
_title_lbl.setText((Object)("HELP"));
 //BA.debugLineNum = 2136;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hip";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2137;BA.debugLine="title_lbl.TextSize = 25";
_title_lbl.setTextSize((float) (25));
 //BA.debugLineNum = 2139;BA.debugLine="title_lbl.SetBackgroundImage(LoadBitmap(File.DirA";
_title_lbl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bgs.jpg").getObject()));
 //BA.debugLineNum = 2140;BA.debugLine="title_lbl.TextColor = Colors.White";
_title_lbl.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 2142;BA.debugLine="help_us_pnl.Initialize(\"help_us_pnl\")";
mostCurrent._help_us_pnl.Initialize(mostCurrent.activityBA,"help_us_pnl");
 //BA.debugLineNum = 2143;BA.debugLine="Dim rs As RichString";
_rs = new anywheresoftware.b4a.agraham.richstring.RichStringBuilder.RichString();
 //BA.debugLineNum = 2144;BA.debugLine="Dim f_string,s_string,t_string,fo_string,fi_strin";
_f_string = "";
_s_string = "";
_t_string = "";
_fo_string = "";
_fi_string = "";
_s_string = "";
 //BA.debugLineNum = 2145;BA.debugLine="f_string = CRLF&\"•	Once you gave your name and yo";
_f_string = anywheresoftware.b4a.keywords.Common.CRLF+"•	Once you gave your name and your contact number, all your information will be shown to all this mobile app users.  ";
 //BA.debugLineNum = 2146;BA.debugLine="s_string = CRLF&CRLF&\"•  Your mobile number will";
_s_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Your mobile number will be call you once the recipient found you that you are one of the possible donors. ";
 //BA.debugLineNum = 2147;BA.debugLine="t_string = CRLF&CRLF&\"•  DO NOT do a search and c";
_t_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  DO NOT do a search and contact just to test app. If you do TEST the SEARCH button and contact the donor and tell the donor that you only testing the app the donor might be disappointed. You will waste not only your time and effort but you will waste a patient’s who is really need a donor.";
 //BA.debugLineNum = 2148;BA.debugLine="fo_string = CRLF&CRLF&\"•  Using call button is mo";
_fo_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Using call button is more effective than sending message to the donor. It saves time and effort.";
 //BA.debugLineNum = 2149;BA.debugLine="fi_string = CRLF&CRLF&\"•  Select the correct Feed";
_fi_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Select the correct Feedback given to this app so that it will help in boosting the confident of the developer and will be making more community based app like this LIFEBLOOD WITH GIS.";
 //BA.debugLineNum = 2150;BA.debugLine="s_string = CRLF&CRLF&\"•  Please share this app an";
_s_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Please share this app and let us build a community that cares.";
 //BA.debugLineNum = 2151;BA.debugLine="rs.Initialize(f_string&s_string&t_string&fo_strin";
_rs.Initialize((java.lang.CharSequence)(_f_string+_s_string+_t_string+_fo_string+_fi_string+_s_string));
 //BA.debugLineNum = 2152;BA.debugLine="help_data.Text = rs '' to set the string output";
_help_data.setText((Object)(_rs.getObject()));
 //BA.debugLineNum = 2153;BA.debugLine="help_data.Typeface = Typeface.LoadFromAssets(\"ZIN";
_help_data.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGBISD.otf"));
 //BA.debugLineNum = 2154;BA.debugLine="help_data.TextSize = 17";
_help_data.setTextSize((float) (17));
 //BA.debugLineNum = 2155;BA.debugLine="for_h.Text = rs";
_for_h.setText((Object)(_rs.getObject()));
 //BA.debugLineNum = 2156;BA.debugLine="help_us_pnl.AddView(for_h,0,0,50%x,50%y)";
mostCurrent._help_us_pnl.AddView((android.view.View)(_for_h.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 2157;BA.debugLine="for_h.Visible = False";
_for_h.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2158;BA.debugLine="Dim string_h As Int : string_h= sus.MeasureMulti";
_string_h = 0;
 //BA.debugLineNum = 2158;BA.debugLine="Dim string_h As Int : string_h= sus.MeasureMulti";
_string_h = _sus.MeasureMultilineTextHeight((android.widget.TextView)(_for_h.getObject()),_for_h.getText());
 //BA.debugLineNum = 2160;BA.debugLine="help_sc2d.Initialize(68%x,string_h+15%Y,\"help_s";
mostCurrent._help_sc2d.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),(int) (_string_h+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA)),"help_sc2d");
 //BA.debugLineNum = 2161;BA.debugLine="help_sc2d.ScrollbarsVisibility(False,False)";
mostCurrent._help_sc2d.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2162;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2163;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 2165;BA.debugLine="help_us_pnl.Color = Colors.Transparent";
mostCurrent._help_us_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 2166;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 2167;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,70%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2169;BA.debugLine="pnl.AddView(help_sc2d,2%x,title_lbl.Top + title_l";
_pnl.AddView((android.view.View)(mostCurrent._help_sc2d.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (60),mostCurrent.activityBA));
 //BA.debugLineNum = 2170;BA.debugLine="help_sc2d.Panel.AddView(help_data,2%x,0,66%x,stri";
mostCurrent._help_sc2d.getPanel().AddView((android.view.View)(_help_data.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (66),mostCurrent.activityBA),(int) (_string_h+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA)));
 //BA.debugLineNum = 2171;BA.debugLine="pnl.AddView(help_ok_btn,1%x,help_sc2d.Top + help_";
_pnl.AddView((android.view.View)(_help_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (mostCurrent._help_sc2d.getTop()+mostCurrent._help_sc2d.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2173;BA.debugLine="help_us_pnl.AddView(pnl,13%x,((((Activity.Height/";
mostCurrent._help_us_pnl.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 2174;BA.debugLine="help_us_pnl.BringToFront";
mostCurrent._help_us_pnl.BringToFront();
 //BA.debugLineNum = 2177;BA.debugLine="Activity.AddView(help_us_pnl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._help_us_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 2178;BA.debugLine="End Sub";
return "";
}
public static String  _help_ok_btn_click() throws Exception{
 //BA.debugLineNum = 2179;BA.debugLine="Sub help_ok_btn_click";
 //BA.debugLineNum = 2180;BA.debugLine="help_us_pnl.RemoveView";
mostCurrent._help_us_pnl.RemoveView();
 //BA.debugLineNum = 2181;BA.debugLine="End Sub";
return "";
}
public static String  _help_us_pnl_click() throws Exception{
 //BA.debugLineNum = 2182;BA.debugLine="Sub help_us_pnl_click";
 //BA.debugLineNum = 2184;BA.debugLine="End Sub";
return "";
}
public static String  _isdonate_edit_() throws Exception{
int _i = 0;
int _inowyear = 0;
int _ii = 0;
int _iii = 0;
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_ok_btn = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 910;BA.debugLine="Sub isDonate_edit_";
 //BA.debugLineNum = 911;BA.debugLine="donated_index = 0";
_donated_index = (int) (0);
 //BA.debugLineNum = 913;BA.debugLine="list_day.Initialize";
_list_day.Initialize();
 //BA.debugLineNum = 914;BA.debugLine="list_month.Initialize";
_list_month.Initialize();
 //BA.debugLineNum = 915;BA.debugLine="list_year.Initialize";
_list_year.Initialize();
 //BA.debugLineNum = 916;BA.debugLine="spin_day.Initialize(\"donate_spin_day\")";
mostCurrent._spin_day.Initialize(mostCurrent.activityBA,"donate_spin_day");
 //BA.debugLineNum = 917;BA.debugLine="spin_month.Initialize(\"donate_spin_month\")";
mostCurrent._spin_month.Initialize(mostCurrent.activityBA,"donate_spin_month");
 //BA.debugLineNum = 918;BA.debugLine="spin_year.Initialize(\"donate_spin_year\")";
mostCurrent._spin_year.Initialize(mostCurrent.activityBA,"donate_spin_year");
 //BA.debugLineNum = 919;BA.debugLine="For i = 1 To 31";
{
final int step8 = 1;
final int limit8 = (int) (31);
for (_i = (int) (1) ; (step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8); _i = ((int)(0 + _i + step8)) ) {
 //BA.debugLineNum = 920;BA.debugLine="list_day.Add(i)";
_list_day.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 922;BA.debugLine="Dim iNowYear As Int";
_inowyear = 0;
 //BA.debugLineNum = 923;BA.debugLine="iNowYear = DateTime.GetYear(DateTime.Now)";
_inowyear = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 924;BA.debugLine="For ii = 1950 To DateTime.GetYear(DateTime.Now)";
{
final int step13 = 1;
final int limit13 = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
for (_ii = (int) (1950) ; (step13 > 0 && _ii <= limit13) || (step13 < 0 && _ii >= limit13); _ii = ((int)(0 + _ii + step13)) ) {
 //BA.debugLineNum = 925;BA.debugLine="list_year.Add(iNowYear)";
_list_year.Add((Object)(_inowyear));
 //BA.debugLineNum = 926;BA.debugLine="iNowYear = iNowYear-1";
_inowyear = (int) (_inowyear-1);
 }
};
 //BA.debugLineNum = 928;BA.debugLine="For iii = 1 To 12";
{
final int step17 = 1;
final int limit17 = (int) (12);
for (_iii = (int) (1) ; (step17 > 0 && _iii <= limit17) || (step17 < 0 && _iii >= limit17); _iii = ((int)(0 + _iii + step17)) ) {
 //BA.debugLineNum = 929;BA.debugLine="list_month.Add(iii)";
_list_month.Add((Object)(_iii));
 }
};
 //BA.debugLineNum = 931;BA.debugLine="spin_day.AddAll(list_day)";
mostCurrent._spin_day.AddAll(_list_day);
 //BA.debugLineNum = 932;BA.debugLine="spin_month.AddAll(list_month)";
mostCurrent._spin_month.AddAll(_list_month);
 //BA.debugLineNum = 933;BA.debugLine="spin_year.AddAll(list_year)";
mostCurrent._spin_year.AddAll(_list_year);
 //BA.debugLineNum = 934;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 935;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 936;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 937;BA.debugLine="edit_ok_btn.Initialize(\"isdonated_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"isdonated_ok_btn");
 //BA.debugLineNum = 938;BA.debugLine="edit_can_btn.Initialize(\"isdonated_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"isdonated_can_btn");
 //BA.debugLineNum = 939;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 940;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 941;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 942;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 943;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 944;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 945;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 946;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 947;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 948;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 949;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 950;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 951;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 952;BA.debugLine="title_lbl.Text = \"SELECT DONATED DATE\"";
_title_lbl.setText((Object)("SELECT DONATED DATE"));
 //BA.debugLineNum = 953;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 954;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 955;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 956;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 957;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 958;BA.debugLine="pnl_bday_body.Initialize(\"pnl_bday_body\")";
mostCurrent._pnl_bday_body.Initialize(mostCurrent.activityBA,"pnl_bday_body");
 //BA.debugLineNum = 959;BA.debugLine="pnl_bday_body.Color = Colors.Transparent";
mostCurrent._pnl_bday_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 960;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 961;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 962;BA.debugLine="pnl.AddView(spin_day,2%x,title_lbl.Top + title_lb";
_pnl.AddView((android.view.View)(mostCurrent._spin_day.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 963;BA.debugLine="pnl.AddView(spin_month,spin_day.Left+spin_day.Wid";
_pnl.AddView((android.view.View)(mostCurrent._spin_month.getObject()),(int) (mostCurrent._spin_day.getLeft()+mostCurrent._spin_day.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_day.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 964;BA.debugLine="pnl.AddView(spin_year,spin_month.Left+spin_month.";
_pnl.AddView((android.view.View)(mostCurrent._spin_year.getObject()),(int) (mostCurrent._spin_month.getLeft()+mostCurrent._spin_month.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_month.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 965;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_year.Top+spin_y";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 966;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 968;BA.debugLine="pnl_bday_body.AddView(pnl,13%x,((Activity.Height/";
mostCurrent._pnl_bday_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 969;BA.debugLine="pnl_bday_body.BringToFront";
mostCurrent._pnl_bday_body.BringToFront();
 //BA.debugLineNum = 972;BA.debugLine="Activity.AddView(pnl_bday_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_bday_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 973;BA.debugLine="End Sub";
return "";
}
public static String  _isdonated_can_btn_click() throws Exception{
 //BA.debugLineNum = 998;BA.debugLine="Sub isdonated_can_btn_click";
 //BA.debugLineNum = 999;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 1000;BA.debugLine="If text_donated.Text == \"NO\" Then";
if ((mostCurrent._text_donated.getText()).equals("NO")) { 
 //BA.debugLineNum = 1001;BA.debugLine="text_donated.Text = \"NO\"";
mostCurrent._text_donated.setText((Object)("NO"));
 }else {
 //BA.debugLineNum = 1003;BA.debugLine="text_donated.Text = \"YES\"";
mostCurrent._text_donated.setText((Object)("YES"));
 };
 //BA.debugLineNum = 1006;BA.debugLine="End Sub";
return "";
}
public static String  _isdonated_ok_btn_click() throws Exception{
String _day = "";
String _month = "";
String _year = "";
 //BA.debugLineNum = 986;BA.debugLine="Sub isdonated_ok_btn_click";
 //BA.debugLineNum = 987;BA.debugLine="Dim day,month,year As String";
_day = "";
_month = "";
_year = "";
 //BA.debugLineNum = 988;BA.debugLine="day = spin_day.GetItem(donate_d_pos)";
_day = mostCurrent._spin_day.GetItem(_donate_d_pos);
 //BA.debugLineNum = 989;BA.debugLine="month = spin_month.GetItem(donate_m_pos)";
_month = mostCurrent._spin_month.GetItem(_donate_m_pos);
 //BA.debugLineNum = 990;BA.debugLine="year = spin_year.GetItem(donate_y_pos)";
_year = mostCurrent._spin_year.GetItem(_donate_y_pos);
 //BA.debugLineNum = 991;BA.debugLine="isDonateDate = month&\"/\"&day&\"/\"&year";
_isdonatedate = _month+"/"+_day+"/"+_year;
 //BA.debugLineNum = 993;BA.debugLine="Msgbox(\"\"&month&\"/\"&day&\"/\"&year,\"Date Selected\")";
anywheresoftware.b4a.keywords.Common.Msgbox(""+_month+"/"+_day+"/"+_year,"Date Selected",mostCurrent.activityBA);
 //BA.debugLineNum = 994;BA.debugLine="text_donated.Text = is_donated";
mostCurrent._text_donated.setText((Object)(_is_donated));
 //BA.debugLineNum = 995;BA.debugLine="Log(isDonateDate)";
anywheresoftware.b4a.keywords.Common.Log(_isdonatedate);
 //BA.debugLineNum = 996;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 997;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(bloodlife.system.httpjob _job) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _user_all_info = null;
int _confirmr = 0;
 //BA.debugLineNum = 581;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 582;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 583;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"all_info_query")) {
case 0: {
 //BA.debugLineNum = 585;BA.debugLine="Dim user_all_info As TextWriter";
_user_all_info = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 586;BA.debugLine="user_all_info.Initialize(File.OpenOutput(";
_user_all_info.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"users_all_info.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 587;BA.debugLine="user_all_info.WriteLine(job.GetString.Tr";
_user_all_info.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 588;BA.debugLine="user_all_info.Close";
_user_all_info.Close();
 break; }
}
;
 //BA.debugLineNum = 592;BA.debugLine="If optionSelected == \"pofileView\" Then";
if ((_optionselected).equals("pofileView")) { 
 //BA.debugLineNum = 593;BA.debugLine="all_input_on_list";
_all_input_on_list();
 }else {
 //BA.debugLineNum = 595;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 596;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 597;BA.debugLine="Dim confirmR As Int";
_confirmr = 0;
 //BA.debugLineNum = 598;BA.debugLine="confirmR = Msgbox2(\"Successfuly Update!\",\"C O";
_confirmr = anywheresoftware.b4a.keywords.Common.Msgbox2("Successfuly Update!","C O N F I R M A T I O N","OK","","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 599;BA.debugLine="If confirmR == DialogResponse.POSITIVE Then";
if (_confirmr==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 600;BA.debugLine="users_out_lbl.text = text_answer.Text";
mostCurrent._users_out_lbl.setText((Object)(mostCurrent._text_answer.getText()));
 }else {
 };
 };
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 606;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 607;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 608;BA.debugLine="Msgbox(\"Error connecting to server, try again!\"";
anywheresoftware.b4a.keywords.Common.Msgbox("Error connecting to server, try again!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 611;BA.debugLine="End Sub";
return "";
}
public static String  _load_activity_layout() throws Exception{
bloodlife.system.calculations _text_temp = null;
 //BA.debugLineNum = 161;BA.debugLine="Sub load_activity_layout";
 //BA.debugLineNum = 162;BA.debugLine="Dim text_temp As calculations";
_text_temp = new bloodlife.system.calculations();
 //BA.debugLineNum = 164;BA.debugLine="text_temp.Initialize";
_text_temp._initialize(processBA);
 //BA.debugLineNum = 165;BA.debugLine="Log(\"name: \"&login_form.name_query)";
anywheresoftware.b4a.keywords.Common.Log("name: "+mostCurrent._login_form._name_query);
 //BA.debugLineNum = 167;BA.debugLine="users_out_lbl.text = login_form.name_query";
mostCurrent._users_out_lbl.setText((Object)(mostCurrent._login_form._name_query));
 //BA.debugLineNum = 168;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 169;BA.debugLine="ban_logo.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._ban_logo.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logo1.jpg").getObject()));
 //BA.debugLineNum = 170;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 171;BA.debugLine="users_panel.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._users_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 172;BA.debugLine="src_blood_pnl.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._src_blood_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 173;BA.debugLine="profile_pnl.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._profile_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 174;BA.debugLine="about_pnl.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._about_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 175;BA.debugLine="help_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._help_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 176;BA.debugLine="exit_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._exit_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 178;BA.debugLine="srch_blood_img.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._srch_blood_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu_search.png").getObject()));
 //BA.debugLineNum = 179;BA.debugLine="profile_img.SetBackgroundImage(LoadBitmap(File";
mostCurrent._profile_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"emyprofile.png").getObject()));
 //BA.debugLineNum = 180;BA.debugLine="about_img.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._about_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eaboutus.png").getObject()));
 //BA.debugLineNum = 181;BA.debugLine="help_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._help_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ehelp.png").getObject()));
 //BA.debugLineNum = 182;BA.debugLine="exit_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._exit_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eexit.png").getObject()));
 //BA.debugLineNum = 184;BA.debugLine="users_heading.Color = Colors.Transparent";
mostCurrent._users_heading.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 186;BA.debugLine="ban_picture.Width = 80%x";
mostCurrent._ban_picture.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 187;BA.debugLine="ban_logo.Width = 20%x";
mostCurrent._ban_logo.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 188;BA.debugLine="users_panel.Width = Activity.Width";
mostCurrent._users_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 189;BA.debugLine="src_blood_pnl.Width = Activity.Width";
mostCurrent._src_blood_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 190;BA.debugLine="profile_pnl.Width = Activity.Width";
mostCurrent._profile_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 191;BA.debugLine="about_pnl.Width = Activity.Width";
mostCurrent._about_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 192;BA.debugLine="help_pnl.Width = Activity.Width";
mostCurrent._help_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 193;BA.debugLine="exit_pnl.Width = Activity.Width";
mostCurrent._exit_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 194;BA.debugLine="users_heading.Width = Activity.Width";
mostCurrent._users_heading.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 196;BA.debugLine="users_heading.Height = 9%y";
mostCurrent._users_heading.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 197;BA.debugLine="users_panel.Height = 18%y";
mostCurrent._users_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 198;BA.debugLine="ban_picture.Height = users_panel.Height";
mostCurrent._ban_picture.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 199;BA.debugLine="ban_logo.Height = users_panel.Height";
mostCurrent._ban_logo.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 200;BA.debugLine="src_blood_pnl.Height = 12%y";
mostCurrent._src_blood_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 201;BA.debugLine="profile_pnl.Height = 12%y";
mostCurrent._profile_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 202;BA.debugLine="about_pnl.Height = 12%y";
mostCurrent._about_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 203;BA.debugLine="help_pnl.Height = 12%y";
mostCurrent._help_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 204;BA.debugLine="exit_pnl.Height = 12%y";
mostCurrent._exit_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 206;BA.debugLine="ban_logo.Left = 0";
mostCurrent._ban_logo.setLeft((int) (0));
 //BA.debugLineNum = 207;BA.debugLine="ban_picture.Left = ban_logo.Left + ban_logo.Width";
mostCurrent._ban_picture.setLeft((int) (mostCurrent._ban_logo.getLeft()+mostCurrent._ban_logo.getWidth()));
 //BA.debugLineNum = 208;BA.debugLine="users_panel.Left = 0";
mostCurrent._users_panel.setLeft((int) (0));
 //BA.debugLineNum = 209;BA.debugLine="src_blood_pnl.Left = 0";
mostCurrent._src_blood_pnl.setLeft((int) (0));
 //BA.debugLineNum = 210;BA.debugLine="profile_pnl.Left = 0";
mostCurrent._profile_pnl.setLeft((int) (0));
 //BA.debugLineNum = 211;BA.debugLine="about_pnl.Left = 0";
mostCurrent._about_pnl.setLeft((int) (0));
 //BA.debugLineNum = 212;BA.debugLine="help_pnl.Left = 0";
mostCurrent._help_pnl.setLeft((int) (0));
 //BA.debugLineNum = 213;BA.debugLine="exit_pnl.Left = 0";
mostCurrent._exit_pnl.setLeft((int) (0));
 //BA.debugLineNum = 214;BA.debugLine="users_heading.Left = 0";
mostCurrent._users_heading.setLeft((int) (0));
 //BA.debugLineNum = 216;BA.debugLine="users_panel.Top = 0";
mostCurrent._users_panel.setTop((int) (0));
 //BA.debugLineNum = 217;BA.debugLine="ban_picture.Top = 0";
mostCurrent._ban_picture.setTop((int) (0));
 //BA.debugLineNum = 218;BA.debugLine="ban_logo.Top = 0";
mostCurrent._ban_logo.setTop((int) (0));
 //BA.debugLineNum = 219;BA.debugLine="users_heading.Top = users_panel.Top + users_panel";
mostCurrent._users_heading.setTop((int) (mostCurrent._users_panel.getTop()+mostCurrent._users_panel.getHeight()));
 //BA.debugLineNum = 220;BA.debugLine="src_blood_pnl.Top = users_heading.Top + users_hea";
mostCurrent._src_blood_pnl.setTop((int) (mostCurrent._users_heading.getTop()+mostCurrent._users_heading.getHeight()));
 //BA.debugLineNum = 221;BA.debugLine="profile_pnl.Top = src_blood_pnl.Top + src_blood_p";
mostCurrent._profile_pnl.setTop((int) (mostCurrent._src_blood_pnl.getTop()+mostCurrent._src_blood_pnl.getHeight()));
 //BA.debugLineNum = 222;BA.debugLine="about_pnl.Top = profile_pnl.Top + profile_pnl.Hei";
mostCurrent._about_pnl.setTop((int) (mostCurrent._profile_pnl.getTop()+mostCurrent._profile_pnl.getHeight()));
 //BA.debugLineNum = 223;BA.debugLine="help_pnl.Top = about_pnl.Top + about_pnl.Height";
mostCurrent._help_pnl.setTop((int) (mostCurrent._about_pnl.getTop()+mostCurrent._about_pnl.getHeight()));
 //BA.debugLineNum = 224;BA.debugLine="exit_pnl.Top = help_pnl.Top + help_pnl.Height";
mostCurrent._exit_pnl.setTop((int) (mostCurrent._help_pnl.getTop()+mostCurrent._help_pnl.getHeight()));
 //BA.debugLineNum = 233;BA.debugLine="search_blood.Width = Activity.Width - 60%x";
mostCurrent._search_blood.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 234;BA.debugLine="about.Width = Activity.Width - 60%x";
mostCurrent._about.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 235;BA.debugLine="help.Width = Activity.Width - 60%x";
mostCurrent._help.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 236;BA.debugLine="profile.Width = Activity.Width - 60%x";
mostCurrent._profile.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 237;BA.debugLine="exit_btn.Width = Activity.Width - 60%x";
mostCurrent._exit_btn.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 238;BA.debugLine="srch_blood_img.Width = Activity.Width - 85%x";
mostCurrent._srch_blood_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 239;BA.debugLine="profile_img.Width = Activity.Width - 85%x";
mostCurrent._profile_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 240;BA.debugLine="about_img.Width = Activity.Width - 85%x";
mostCurrent._about_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 241;BA.debugLine="help_img.Width = Activity.Width - 85%x";
mostCurrent._help_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 242;BA.debugLine="exit_img.Width = Activity.Width - 85%x";
mostCurrent._exit_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 244;BA.debugLine="search_blood.Height = 9%y";
mostCurrent._search_blood.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 245;BA.debugLine="about.Height = 9%y";
mostCurrent._about.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 246;BA.debugLine="help.Height = 9%y";
mostCurrent._help.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 247;BA.debugLine="profile.Height = 9%y";
mostCurrent._profile.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 248;BA.debugLine="exit_btn.Height = 9%y";
mostCurrent._exit_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 249;BA.debugLine="srch_blood_img.Height = 9%y";
mostCurrent._srch_blood_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 250;BA.debugLine="profile_img.Height = 9%y";
mostCurrent._profile_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 251;BA.debugLine="about_img.Height = 9%y";
mostCurrent._about_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 252;BA.debugLine="help_img.Height = 9%y";
mostCurrent._help_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 253;BA.debugLine="exit_img.Height = 9%y";
mostCurrent._exit_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 255;BA.debugLine="users_lbl.Left = 2%x";
mostCurrent._users_lbl.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA));
 //BA.debugLineNum = 256;BA.debugLine="users_out_lbl.Left = users_lbl.Left + users_lbl.W";
mostCurrent._users_out_lbl.setLeft((int) (mostCurrent._users_lbl.getLeft()+mostCurrent._users_lbl.getWidth()));
 //BA.debugLineNum = 257;BA.debugLine="search_blood.Left = ((src_blood_pnl.Width/2)/2)/";
mostCurrent._search_blood.setLeft((int) (((mostCurrent._src_blood_pnl.getWidth()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 258;BA.debugLine="profile.Left = ((profile_pnl.Width/2)/2)";
mostCurrent._profile.setLeft((int) (((mostCurrent._profile_pnl.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 259;BA.debugLine="about.Left = (help_pnl.Width/2)";
mostCurrent._about.setLeft((int) ((mostCurrent._help_pnl.getWidth()/(double)2)));
 //BA.debugLineNum = 260;BA.debugLine="help.Left = ((about_pnl.Width/2)/2)";
mostCurrent._help.setLeft((int) (((mostCurrent._about_pnl.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 261;BA.debugLine="exit_btn.Left = ((exit_pnl.Width/2)/2)/2";
mostCurrent._exit_btn.setLeft((int) (((mostCurrent._exit_pnl.getWidth()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 263;BA.debugLine="srch_blood_img.Left = search_blood.Left + search";
mostCurrent._srch_blood_img.setLeft((int) (mostCurrent._search_blood.getLeft()+mostCurrent._search_blood.getWidth()));
 //BA.debugLineNum = 264;BA.debugLine="profile_img.Left = profile.Left + profile.Widt";
mostCurrent._profile_img.setLeft((int) (mostCurrent._profile.getLeft()+mostCurrent._profile.getWidth()));
 //BA.debugLineNum = 265;BA.debugLine="about_img.Left = about.Left - about_img.Width";
mostCurrent._about_img.setLeft((int) (mostCurrent._about.getLeft()-mostCurrent._about_img.getWidth()));
 //BA.debugLineNum = 266;BA.debugLine="help_img.Left = help.Left + help.Width";
mostCurrent._help_img.setLeft((int) (mostCurrent._help.getLeft()+mostCurrent._help.getWidth()));
 //BA.debugLineNum = 267;BA.debugLine="exit_img.Left = exit_btn.Left + exit_btn.Width";
mostCurrent._exit_img.setLeft((int) (mostCurrent._exit_btn.getLeft()+mostCurrent._exit_btn.getWidth()));
 //BA.debugLineNum = 269;BA.debugLine="users_out_lbl.Top = ((users_heading.Height/2)/2)/";
mostCurrent._users_out_lbl.setTop((int) (((mostCurrent._users_heading.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 270;BA.debugLine="users_lbl.Top = users_out_lbl.Top";
mostCurrent._users_lbl.setTop(mostCurrent._users_out_lbl.getTop());
 //BA.debugLineNum = 272;BA.debugLine="search_blood.Top = ((src_blood_pnl.Height/2)/2)/";
mostCurrent._search_blood.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 273;BA.debugLine="about.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._about.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 274;BA.debugLine="help.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._help.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 275;BA.debugLine="profile.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._profile.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 276;BA.debugLine="exit_btn.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_btn.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 278;BA.debugLine="srch_blood_img.Top = ((src_blood_pnl.Height/2)/2";
mostCurrent._srch_blood_img.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 279;BA.debugLine="profile_img.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._profile_img.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 280;BA.debugLine="about_img.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._about_img.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 281;BA.debugLine="help_img.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._help_img.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 282;BA.debugLine="exit_img.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_img.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 284;BA.debugLine="search_blood.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._search_blood.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"SEARCH.png").getObject()));
 //BA.debugLineNum = 285;BA.debugLine="about.SetBackgroundImage(LoadBitmap(File.DirAsse";
mostCurrent._about.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ABOUT_US.png").getObject()));
 //BA.debugLineNum = 286;BA.debugLine="help.SetBackgroundImage(LoadBitmap(File.DirAsset";
mostCurrent._help.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"HELP.png").getObject()));
 //BA.debugLineNum = 287;BA.debugLine="profile.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._profile.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"my_profile.png").getObject()));
 //BA.debugLineNum = 288;BA.debugLine="exit_btn.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._exit_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"EXIT.png").getObject()));
 //BA.debugLineNum = 290;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1171;BA.debugLine="Sub locate_edit_Click";
 //BA.debugLineNum = 1172;BA.debugLine="edit_panel_click_ = 3";
_edit_panel_click_ = (int) (3);
 //BA.debugLineNum = 1173;BA.debugLine="list_location_b.Initialize";
_list_location_b.Initialize();
 //BA.debugLineNum = 1174;BA.debugLine="location_spin_street.Initialize(\"location_spin_s";
mostCurrent._location_spin_street.Initialize(mostCurrent.activityBA,"location_spin_street");
 //BA.debugLineNum = 1175;BA.debugLine="location_spin_brgy.Initialize(\"location_spin_brg";
mostCurrent._location_spin_brgy.Initialize(mostCurrent.activityBA,"location_spin_brgy");
 //BA.debugLineNum = 1176;BA.debugLine="list_location_s.Initialize";
_list_location_s.Initialize();
 //BA.debugLineNum = 1177;BA.debugLine="list_location_b.Add(\"Barangay  1\") 'index 0";
_list_location_b.Add((Object)("Barangay  1"));
 //BA.debugLineNum = 1178;BA.debugLine="list_location_b.Add(\"Barangay 2\") 'index 1";
_list_location_b.Add((Object)("Barangay 2"));
 //BA.debugLineNum = 1179;BA.debugLine="list_location_b.Add(\"Barangay 3\") 'index 2";
_list_location_b.Add((Object)("Barangay 3"));
 //BA.debugLineNum = 1180;BA.debugLine="list_location_b.Add(\"Barangay 4\") 'index 3";
_list_location_b.Add((Object)("Barangay 4"));
 //BA.debugLineNum = 1181;BA.debugLine="list_location_b.Add(\"Aguisan\") 'index 4";
_list_location_b.Add((Object)("Aguisan"));
 //BA.debugLineNum = 1182;BA.debugLine="list_location_b.Add(\"caradio-an\") 'index 5";
_list_location_b.Add((Object)("caradio-an"));
 //BA.debugLineNum = 1183;BA.debugLine="list_location_b.Add(\"Buenavista\") 'index 6";
_list_location_b.Add((Object)("Buenavista"));
 //BA.debugLineNum = 1184;BA.debugLine="list_location_b.Add(\"Cabadiangan\") 'index 7";
_list_location_b.Add((Object)("Cabadiangan"));
 //BA.debugLineNum = 1185;BA.debugLine="list_location_b.Add(\"Cabanbanan\") 'index 8";
_list_location_b.Add((Object)("Cabanbanan"));
 //BA.debugLineNum = 1186;BA.debugLine="list_location_b.Add(\"Carabalan\") 'index 9";
_list_location_b.Add((Object)("Carabalan"));
 //BA.debugLineNum = 1187;BA.debugLine="list_location_b.Add(\"Libacao\") 'index 10";
_list_location_b.Add((Object)("Libacao"));
 //BA.debugLineNum = 1188;BA.debugLine="list_location_b.Add(\"Mahalang\") 'index 11";
_list_location_b.Add((Object)("Mahalang"));
 //BA.debugLineNum = 1189;BA.debugLine="list_location_b.Add(\"Mambagaton\") 'index 12";
_list_location_b.Add((Object)("Mambagaton"));
 //BA.debugLineNum = 1190;BA.debugLine="list_location_b.Add(\"Nabalian\") 'index 13";
_list_location_b.Add((Object)("Nabalian"));
 //BA.debugLineNum = 1191;BA.debugLine="list_location_b.Add(\"San Antonio\") 'index 14";
_list_location_b.Add((Object)("San Antonio"));
 //BA.debugLineNum = 1192;BA.debugLine="list_location_b.Add(\"Saraet\") 'index 15";
_list_location_b.Add((Object)("Saraet"));
 //BA.debugLineNum = 1193;BA.debugLine="list_location_b.Add(\"Suay\") 'index 16";
_list_location_b.Add((Object)("Suay"));
 //BA.debugLineNum = 1194;BA.debugLine="list_location_b.Add(\"Talaban\") 'index 17";
_list_location_b.Add((Object)("Talaban"));
 //BA.debugLineNum = 1195;BA.debugLine="list_location_b.Add(\"Tooy\") 'index 18";
_list_location_b.Add((Object)("Tooy"));
 //BA.debugLineNum = 1196;BA.debugLine="location_spin_brgy.AddAll(list_location_b)";
mostCurrent._location_spin_brgy.AddAll(_list_location_b);
 //BA.debugLineNum = 1198;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1199;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 1200;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 1201;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 1202;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 1203;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 1204;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 1205;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 //BA.debugLineNum = 1206;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 1208;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1209;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1210;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1211;BA.debugLine="edit_ok_btn.Initialize(\"edit_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_ok_btn");
 //BA.debugLineNum = 1212;BA.debugLine="edit_can_btn.Initialize(\"edit_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_can_btn");
 //BA.debugLineNum = 1213;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1214;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1215;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1216;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1217;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1218;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1219;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1220;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1221;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1222;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1223;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1224;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1225;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1226;BA.debugLine="title_lbl.Text = \"SELECT LOCATION\"";
_title_lbl.setText((Object)("SELECT LOCATION"));
 //BA.debugLineNum = 1227;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(\"";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1228;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1229;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1230;BA.debugLine="title_lbl.Gravity =  Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1231;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1232;BA.debugLine="pnl_body.Initialize(\"pnl_body\")";
mostCurrent._pnl_body.Initialize(mostCurrent.activityBA,"pnl_body");
 //BA.debugLineNum = 1233;BA.debugLine="pnl_body.Color = Colors.Transparent";
mostCurrent._pnl_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1234;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1235;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1236;BA.debugLine="pnl.AddView(location_spin_brgy,2%x,title_lbl.Top";
_pnl.AddView((android.view.View)(mostCurrent._location_spin_brgy.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1237;BA.debugLine="pnl.AddView(location_spin_street,location_spin_br";
_pnl.AddView((android.view.View)(mostCurrent._location_spin_street.getObject()),(int) (mostCurrent._location_spin_brgy.getLeft()+mostCurrent._location_spin_brgy.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),mostCurrent._location_spin_brgy.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1238;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,location_spin_street";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._location_spin_street.getTop()+mostCurrent._location_spin_street.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1239;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._location_spin_street.getTop()+mostCurrent._location_spin_street.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1241;BA.debugLine="pnl_body.AddView(pnl,13%x,((Activity.Height/2)/2)";
mostCurrent._pnl_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1242;BA.debugLine="pnl_body.BringToFront";
mostCurrent._pnl_body.BringToFront();
 //BA.debugLineNum = 1245;BA.debugLine="Activity.AddView(pnl_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1246;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_brgy_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1816;BA.debugLine="Sub location_spin_brgy_ItemClick (Position As Int,";
 //BA.debugLineNum = 1817;BA.debugLine="list_location_s.Clear";
_list_location_s.Clear();
 //BA.debugLineNum = 1819;BA.debugLine="location_spin_street.Clear";
mostCurrent._location_spin_street.Clear();
 //BA.debugLineNum = 1822;BA.debugLine="If Position == 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 1823;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1824;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 1825;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 1826;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 1827;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 1828;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 1829;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 1830;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 }else if(_position==1) { 
 //BA.debugLineNum = 1833;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1834;BA.debugLine="list_location_s.Add(\"Monton st.\")";
_list_location_s.Add((Object)("Monton st."));
 //BA.debugLineNum = 1835;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 //BA.debugLineNum = 1836;BA.debugLine="list_location_s.Add(\"Purok star apple\")";
_list_location_s.Add((Object)("Purok star apple"));
 //BA.debugLineNum = 1837;BA.debugLine="list_location_s.Add(\"Gatuslao st.\")";
_list_location_s.Add((Object)("Gatuslao st."));
 //BA.debugLineNum = 1838;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 1839;BA.debugLine="list_location_s.Add(\"Tabino st.\")";
_list_location_s.Add((Object)("Tabino st."));
 //BA.debugLineNum = 1840;BA.debugLine="list_location_s.Add(\"River side\")";
_list_location_s.Add((Object)("River side"));
 //BA.debugLineNum = 1841;BA.debugLine="list_location_s.Add(\"Arroyan st\")";
_list_location_s.Add((Object)("Arroyan st"));
 }else if(_position==2) { 
 //BA.debugLineNum = 1843;BA.debugLine="list_location_s.Add(\"Segovia st.\")";
_list_location_s.Add((Object)("Segovia st."));
 //BA.debugLineNum = 1844;BA.debugLine="list_location_s.Add(\"Vasquez st.\")";
_list_location_s.Add((Object)("Vasquez st."));
 //BA.debugLineNum = 1845;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 1846;BA.debugLine="list_location_s.Add(\"Old relis st.\")";
_list_location_s.Add((Object)("Old relis st."));
 //BA.debugLineNum = 1847;BA.debugLine="list_location_s.Add(\"Wayang\")";
_list_location_s.Add((Object)("Wayang"));
 //BA.debugLineNum = 1848;BA.debugLine="list_location_s.Add(\"Valencia\")";
_list_location_s.Add((Object)("Valencia"));
 //BA.debugLineNum = 1849;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 1850;BA.debugLine="list_location_s.Add(\"Bingig\")";
_list_location_s.Add((Object)("Bingig"));
 //BA.debugLineNum = 1851;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 1852;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 }else if(_position==3) { 
 //BA.debugLineNum = 1854;BA.debugLine="list_location_s.Add(\"Crusher\")";
_list_location_s.Add((Object)("Crusher"));
 //BA.debugLineNum = 1855;BA.debugLine="list_location_s.Add(\"Bangga mayok\")";
_list_location_s.Add((Object)("Bangga mayok"));
 //BA.debugLineNum = 1856;BA.debugLine="list_location_s.Add(\"Villa julita\")";
_list_location_s.Add((Object)("Villa julita"));
 //BA.debugLineNum = 1857;BA.debugLine="list_location_s.Add(\"Greenland subdivision\")";
_list_location_s.Add((Object)("Greenland subdivision"));
 //BA.debugLineNum = 1858;BA.debugLine="list_location_s.Add(\"Bangga 3c\")";
_list_location_s.Add((Object)("Bangga 3c"));
 //BA.debugLineNum = 1859;BA.debugLine="list_location_s.Add(\"Cambugnon\")";
_list_location_s.Add((Object)("Cambugnon"));
 //BA.debugLineNum = 1860;BA.debugLine="list_location_s.Add(\"Menez\")";
_list_location_s.Add((Object)("Menez"));
 //BA.debugLineNum = 1861;BA.debugLine="list_location_s.Add(\"Relis\")";
_list_location_s.Add((Object)("Relis"));
 //BA.debugLineNum = 1862;BA.debugLine="list_location_s.Add(\"Bangga patyo\")";
_list_location_s.Add((Object)("Bangga patyo"));
 }else if(_position==4) { 
 //BA.debugLineNum = 1864;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1865;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1866;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1867;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1868;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 1869;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 //BA.debugLineNum = 1870;BA.debugLine="list_location_s.Add(\"Purok 7\")";
_list_location_s.Add((Object)("Purok 7"));
 //BA.debugLineNum = 1871;BA.debugLine="list_location_s.Add(\"Purok 8\")";
_list_location_s.Add((Object)("Purok 8"));
 //BA.debugLineNum = 1872;BA.debugLine="list_location_s.Add(\"Purok 9\")";
_list_location_s.Add((Object)("Purok 9"));
 //BA.debugLineNum = 1873;BA.debugLine="list_location_s.Add(\"Purok 10\")";
_list_location_s.Add((Object)("Purok 10"));
 //BA.debugLineNum = 1874;BA.debugLine="list_location_s.Add(\"Purok 11\")";
_list_location_s.Add((Object)("Purok 11"));
 //BA.debugLineNum = 1875;BA.debugLine="list_location_s.Add(\"Purok 12\")";
_list_location_s.Add((Object)("Purok 12"));
 }else if(_position==5) { 
 //BA.debugLineNum = 1877;BA.debugLine="list_location_s.Add(\"Malusay\")";
_list_location_s.Add((Object)("Malusay"));
 //BA.debugLineNum = 1878;BA.debugLine="list_location_s.Add(\"Nasug ong\")";
_list_location_s.Add((Object)("Nasug ong"));
 //BA.debugLineNum = 1879;BA.debugLine="list_location_s.Add(\"Lugway\")";
_list_location_s.Add((Object)("Lugway"));
 //BA.debugLineNum = 1880;BA.debugLine="list_location_s.Add(\"Ubay\")";
_list_location_s.Add((Object)("Ubay"));
 //BA.debugLineNum = 1881;BA.debugLine="list_location_s.Add(\"Fisheries\")";
_list_location_s.Add((Object)("Fisheries"));
 //BA.debugLineNum = 1882;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1883;BA.debugLine="list_location_s.Add(\"Calasa\")";
_list_location_s.Add((Object)("Calasa"));
 //BA.debugLineNum = 1884;BA.debugLine="list_location_s.Add(\"Hda. Serafin\")";
_list_location_s.Add((Object)("Hda. Serafin"));
 //BA.debugLineNum = 1885;BA.debugLine="list_location_s.Add(\"Patay na suba\")";
_list_location_s.Add((Object)("Patay na suba"));
 //BA.debugLineNum = 1886;BA.debugLine="list_location_s.Add(\"Lumanog\")";
_list_location_s.Add((Object)("Lumanog"));
 //BA.debugLineNum = 1887;BA.debugLine="list_location_s.Add(\"San agustin\")";
_list_location_s.Add((Object)("San agustin"));
 //BA.debugLineNum = 1888;BA.debugLine="list_location_s.Add(\"San jose\")";
_list_location_s.Add((Object)("San jose"));
 //BA.debugLineNum = 1889;BA.debugLine="list_location_s.Add(\"Maglantay\")";
_list_location_s.Add((Object)("Maglantay"));
 //BA.debugLineNum = 1890;BA.debugLine="list_location_s.Add(\"San juan\")";
_list_location_s.Add((Object)("San juan"));
 //BA.debugLineNum = 1891;BA.debugLine="list_location_s.Add(\"Magsaha\")";
_list_location_s.Add((Object)("Magsaha"));
 //BA.debugLineNum = 1892;BA.debugLine="list_location_s.Add(\"Tagmanok\")";
_list_location_s.Add((Object)("Tagmanok"));
 //BA.debugLineNum = 1893;BA.debugLine="list_location_s.Add(\"Butong\")";
_list_location_s.Add((Object)("Butong"));
 }else if(_position==6) { 
 //BA.debugLineNum = 1895;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1896;BA.debugLine="list_location_s.Add(\"Saisi\")";
_list_location_s.Add((Object)("Saisi"));
 //BA.debugLineNum = 1897;BA.debugLine="list_location_s.Add(\"Paloypoy\")";
_list_location_s.Add((Object)("Paloypoy"));
 //BA.debugLineNum = 1898;BA.debugLine="list_location_s.Add(\"Tigue\")";
_list_location_s.Add((Object)("Tigue"));
 }else if(_position==7) { 
 //BA.debugLineNum = 1900;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1901;BA.debugLine="list_location_s.Add(\"Tonggo\")";
_list_location_s.Add((Object)("Tonggo"));
 //BA.debugLineNum = 1902;BA.debugLine="list_location_s.Add(\"Iling iling\")";
_list_location_s.Add((Object)("Iling iling"));
 //BA.debugLineNum = 1903;BA.debugLine="list_location_s.Add(\"Campayas\")";
_list_location_s.Add((Object)("Campayas"));
 //BA.debugLineNum = 1904;BA.debugLine="list_location_s.Add(\"Palayan\")";
_list_location_s.Add((Object)("Palayan"));
 //BA.debugLineNum = 1905;BA.debugLine="list_location_s.Add(\"Guia\")";
_list_location_s.Add((Object)("Guia"));
 //BA.debugLineNum = 1906;BA.debugLine="list_location_s.Add(\"An-an\")";
_list_location_s.Add((Object)("An-an"));
 //BA.debugLineNum = 1907;BA.debugLine="list_location_s.Add(\"An-an 2\")";
_list_location_s.Add((Object)("An-an 2"));
 //BA.debugLineNum = 1908;BA.debugLine="list_location_s.Add(\"Sta. rita\")";
_list_location_s.Add((Object)("Sta. rita"));
 //BA.debugLineNum = 1909;BA.debugLine="list_location_s.Add(\"Benedicto\")";
_list_location_s.Add((Object)("Benedicto"));
 //BA.debugLineNum = 1910;BA.debugLine="list_location_s.Add(\"Sta. cruz/ bunggol\")";
_list_location_s.Add((Object)("Sta. cruz/ bunggol"));
 //BA.debugLineNum = 1911;BA.debugLine="list_location_s.Add(\"Olalia\")";
_list_location_s.Add((Object)("Olalia"));
 //BA.debugLineNum = 1912;BA.debugLine="list_location_s.Add(\"Banuyo\")";
_list_location_s.Add((Object)("Banuyo"));
 //BA.debugLineNum = 1913;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1914;BA.debugLine="list_location_s.Add(\"Riverside\")";
_list_location_s.Add((Object)("Riverside"));
 }else if(_position==8) { 
 //BA.debugLineNum = 1916;BA.debugLine="list_location_s.Add(\"Balangga-an\")";
_list_location_s.Add((Object)("Balangga-an"));
 //BA.debugLineNum = 1917;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1918;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1919;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 1920;BA.debugLine="list_location_s.Add(\"Bakyas\")";
_list_location_s.Add((Object)("Bakyas"));
 }else if(_position==9) { 
 //BA.debugLineNum = 1922;BA.debugLine="list_location_s.Add(\"Cunalom\")";
_list_location_s.Add((Object)("Cunalom"));
 //BA.debugLineNum = 1923;BA.debugLine="list_location_s.Add(\"Tara\")";
_list_location_s.Add((Object)("Tara"));
 //BA.debugLineNum = 1924;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1925;BA.debugLine="list_location_s.Add(\"Casipungan\")";
_list_location_s.Add((Object)("Casipungan"));
 //BA.debugLineNum = 1926;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1927;BA.debugLine="list_location_s.Add(\"Lanipga\")";
_list_location_s.Add((Object)("Lanipga"));
 //BA.debugLineNum = 1928;BA.debugLine="list_location_s.Add(\"Bulod\")";
_list_location_s.Add((Object)("Bulod"));
 //BA.debugLineNum = 1929;BA.debugLine="list_location_s.Add(\"Bonton\")";
_list_location_s.Add((Object)("Bonton"));
 //BA.debugLineNum = 1930;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 }else if(_position==10) { 
 //BA.debugLineNum = 1932;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 1933;BA.debugLine="list_location_s.Add(\"Balisong\")";
_list_location_s.Add((Object)("Balisong"));
 //BA.debugLineNum = 1934;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1935;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1936;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1937;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1938;BA.debugLine="list_location_s.Add(\"Dubdub\")";
_list_location_s.Add((Object)("Dubdub"));
 //BA.debugLineNum = 1939;BA.debugLine="list_location_s.Add(\"Hda. San jose valing\")";
_list_location_s.Add((Object)("Hda. San jose valing"));
 }else if(_position==11) { 
 //BA.debugLineNum = 1941;BA.debugLine="list_location_s.Add(\"Acapulco\")";
_list_location_s.Add((Object)("Acapulco"));
 //BA.debugLineNum = 1942;BA.debugLine="list_location_s.Add(\"Liki\")";
_list_location_s.Add((Object)("Liki"));
 //BA.debugLineNum = 1943;BA.debugLine="list_location_s.Add(\"500\")";
_list_location_s.Add((Object)("500"));
 //BA.debugLineNum = 1944;BA.debugLine="list_location_s.Add(\"Aglatong\")";
_list_location_s.Add((Object)("Aglatong"));
 //BA.debugLineNum = 1945;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1946;BA.debugLine="list_location_s.Add(\"Baptist\")";
_list_location_s.Add((Object)("Baptist"));
 }else if(_position==12) { 
 //BA.debugLineNum = 1948;BA.debugLine="list_location_s.Add(\"Lizares\")";
_list_location_s.Add((Object)("Lizares"));
 //BA.debugLineNum = 1949;BA.debugLine="list_location_s.Add(\"Pakol\")";
_list_location_s.Add((Object)("Pakol"));
 //BA.debugLineNum = 1950;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1951;BA.debugLine="list_location_s.Add(\"Lanete\")";
_list_location_s.Add((Object)("Lanete"));
 //BA.debugLineNum = 1952;BA.debugLine="list_location_s.Add(\"Kasoy\")";
_list_location_s.Add((Object)("Kasoy"));
 //BA.debugLineNum = 1953;BA.debugLine="list_location_s.Add(\"Bato\")";
_list_location_s.Add((Object)("Bato"));
 //BA.debugLineNum = 1954;BA.debugLine="list_location_s.Add(\"Frande\")";
_list_location_s.Add((Object)("Frande"));
 //BA.debugLineNum = 1955;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 1956;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 //BA.debugLineNum = 1957;BA.debugLine="list_location_s.Add(\"Culban\")";
_list_location_s.Add((Object)("Culban"));
 //BA.debugLineNum = 1958;BA.debugLine="list_location_s.Add(\"Calansi\")";
_list_location_s.Add((Object)("Calansi"));
 //BA.debugLineNum = 1959;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1960;BA.debugLine="list_location_s.Add(\"Dama\")";
_list_location_s.Add((Object)("Dama"));
 }else if(_position==13) { 
 //BA.debugLineNum = 1962;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1963;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1964;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1965;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1966;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 1967;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 }else if(_position==14) { 
 //BA.debugLineNum = 1969;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1970;BA.debugLine="list_location_s.Add(\"Calubihan\")";
_list_location_s.Add((Object)("Calubihan"));
 //BA.debugLineNum = 1971;BA.debugLine="list_location_s.Add(\"Mapulang duta\")";
_list_location_s.Add((Object)("Mapulang duta"));
 //BA.debugLineNum = 1972;BA.debugLine="list_location_s.Add(\"Abud\")";
_list_location_s.Add((Object)("Abud"));
 //BA.debugLineNum = 1973;BA.debugLine="list_location_s.Add(\"Molo\")";
_list_location_s.Add((Object)("Molo"));
 //BA.debugLineNum = 1974;BA.debugLine="list_location_s.Add(\"Balabag\")";
_list_location_s.Add((Object)("Balabag"));
 //BA.debugLineNum = 1975;BA.debugLine="list_location_s.Add(\"Pandan\")";
_list_location_s.Add((Object)("Pandan"));
 //BA.debugLineNum = 1976;BA.debugLine="list_location_s.Add(\"Nahulop\")";
_list_location_s.Add((Object)("Nahulop"));
 //BA.debugLineNum = 1977;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 1978;BA.debugLine="list_location_s.Add(\"Aglaoa\")";
_list_location_s.Add((Object)("Aglaoa"));
 }else if(_position==15) { 
 //BA.debugLineNum = 1980;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1981;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1982;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1983;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 }else if(_position==16) { 
 //BA.debugLineNum = 1985;BA.debugLine="list_location_s.Add(\"ORS\")";
_list_location_s.Add((Object)("ORS"));
 //BA.debugLineNum = 1986;BA.debugLine="list_location_s.Add(\"Aloe vera\")";
_list_location_s.Add((Object)("Aloe vera"));
 //BA.debugLineNum = 1987;BA.debugLine="list_location_s.Add(\"SCAD\")";
_list_location_s.Add((Object)("SCAD"));
 //BA.debugLineNum = 1988;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1989;BA.debugLine="list_location_s.Add(\"Sampaguita\")";
_list_location_s.Add((Object)("Sampaguita"));
 //BA.debugLineNum = 1990;BA.debugLine="list_location_s.Add(\"Bonguinvilla\")";
_list_location_s.Add((Object)("Bonguinvilla"));
 //BA.debugLineNum = 1991;BA.debugLine="list_location_s.Add(\"Cagay\")";
_list_location_s.Add((Object)("Cagay"));
 //BA.debugLineNum = 1992;BA.debugLine="list_location_s.Add(\"Naga\")";
_list_location_s.Add((Object)("Naga"));
 }else if(_position==17) { 
 //BA.debugLineNum = 1994;BA.debugLine="list_location_s.Add(\"Hda. Naval\")";
_list_location_s.Add((Object)("Hda. Naval"));
 //BA.debugLineNum = 1995;BA.debugLine="list_location_s.Add(\"Antipolo\")";
_list_location_s.Add((Object)("Antipolo"));
 //BA.debugLineNum = 1996;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1997;BA.debugLine="list_location_s.Add(\"Punta talaban\")";
_list_location_s.Add((Object)("Punta talaban"));
 //BA.debugLineNum = 1998;BA.debugLine="list_location_s.Add(\"Batang guwaan\")";
_list_location_s.Add((Object)("Batang guwaan"));
 //BA.debugLineNum = 1999;BA.debugLine="list_location_s.Add(\"Batang sulod\")";
_list_location_s.Add((Object)("Batang sulod"));
 //BA.debugLineNum = 2000;BA.debugLine="list_location_s.Add(\"Mabini st.\")";
_list_location_s.Add((Object)("Mabini st."));
 //BA.debugLineNum = 2001;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 2002;BA.debugLine="list_location_s.Add(\"Hacienda silos\")";
_list_location_s.Add((Object)("Hacienda silos"));
 //BA.debugLineNum = 2003;BA.debugLine="list_location_s.Add(\"Lopez jeana 1\")";
_list_location_s.Add((Object)("Lopez jeana 1"));
 //BA.debugLineNum = 2004;BA.debugLine="list_location_s.Add(\"Lopez jeana 2\")";
_list_location_s.Add((Object)("Lopez jeana 2"));
 }else if(_position==18) { 
 //BA.debugLineNum = 2006;BA.debugLine="list_location_s.Add(\"Ilawod\")";
_list_location_s.Add((Object)("Ilawod"));
 //BA.debugLineNum = 2007;BA.debugLine="list_location_s.Add(\"Buhian\")";
_list_location_s.Add((Object)("Buhian"));
 //BA.debugLineNum = 2008;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2009;BA.debugLine="list_location_s.Add(\"Mambato\")";
_list_location_s.Add((Object)("Mambato"));
 };
 //BA.debugLineNum = 2012;BA.debugLine="brgy_index = Position";
_brgy_index = _position;
 //BA.debugLineNum = 2013;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 2016;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_street_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 2017;BA.debugLine="Sub location_spin_street_ItemClick (Position As In";
 //BA.debugLineNum = 2018;BA.debugLine="street_index = Position";
_street_index = _position;
 //BA.debugLineNum = 2019;BA.debugLine="street_lat_lng";
_street_lat_lng();
 //BA.debugLineNum = 2020;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_bday_body_click() throws Exception{
 //BA.debugLineNum = 1098;BA.debugLine="Sub pnl_bday_body_click";
 //BA.debugLineNum = 1100;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_blood_body_click() throws Exception{
 //BA.debugLineNum = 1164;BA.debugLine="Sub pnl_blood_body_click";
 //BA.debugLineNum = 1166;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_body_click() throws Exception{
 //BA.debugLineNum = 1258;BA.debugLine="Sub pnl_body_click";
 //BA.debugLineNum = 1260;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_donated_body_click() throws Exception{
 //BA.debugLineNum = 1012;BA.debugLine="Sub pnl_donated_body_click";
 //BA.debugLineNum = 1014;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_gender_body_click() throws Exception{
 //BA.debugLineNum = 845;BA.debugLine="Sub pnl_gender_body_click";
 //BA.debugLineNum = 847;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 12;BA.debugLine="Dim list_is_gender As List";
_list_is_gender = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 13;BA.debugLine="Dim list_day,list_month,list_year As List";
_list_day = new anywheresoftware.b4a.objects.collections.List();
_list_month = new anywheresoftware.b4a.objects.collections.List();
_list_year = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Dim users_string_login As String";
_users_string_login = "";
 //BA.debugLineNum = 16;BA.debugLine="Dim blood_selected As String : blood_selected = \"";
_blood_selected = "";
 //BA.debugLineNum = 16;BA.debugLine="Dim blood_selected As String : blood_selected = \"";
_blood_selected = "A";
 //BA.debugLineNum = 17;BA.debugLine="Dim bday_day_selected As String : bday_day_select";
_bday_day_selected = "";
 //BA.debugLineNum = 17;BA.debugLine="Dim bday_day_selected As String : bday_day_select";
_bday_day_selected = "1";
 //BA.debugLineNum = 18;BA.debugLine="Dim bday_month_selected As String : bday_month_se";
_bday_month_selected = "";
 //BA.debugLineNum = 18;BA.debugLine="Dim bday_month_selected As String : bday_month_se";
_bday_month_selected = "1";
 //BA.debugLineNum = 19;BA.debugLine="Dim bday_year_selected As String : bday_year_sele";
_bday_year_selected = "";
 //BA.debugLineNum = 19;BA.debugLine="Dim bday_year_selected As String : bday_year_sele";
_bday_year_selected = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 20;BA.debugLine="Dim location_brgy_selected As String : location_b";
_location_brgy_selected = "";
 //BA.debugLineNum = 20;BA.debugLine="Dim location_brgy_selected As String : location_b";
_location_brgy_selected = "Brgy 1";
 //BA.debugLineNum = 21;BA.debugLine="Dim location_street_selected As String : location";
_location_street_selected = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim location_street_selected As String : location";
_location_street_selected = "Rizal St.";
 //BA.debugLineNum = 22;BA.debugLine="Dim gender_string_data As String : gender_string_";
_gender_string_data = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim gender_string_data As String : gender_string_";
_gender_string_data = "Male";
 //BA.debugLineNum = 23;BA.debugLine="Dim is_donated As String : is_donated = \"Yes\"";
_is_donated = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim is_donated As String : is_donated = \"Yes\"";
_is_donated = "Yes";
 //BA.debugLineNum = 24;BA.debugLine="Dim donated_index As Int : donated_index = 0";
_donated_index = 0;
 //BA.debugLineNum = 24;BA.debugLine="Dim donated_index As Int : donated_index = 0";
_donated_index = (int) (0);
 //BA.debugLineNum = 25;BA.debugLine="Dim is_gender_index As Int : is_gender_index = 0";
_is_gender_index = 0;
 //BA.debugLineNum = 25;BA.debugLine="Dim is_gender_index As Int : is_gender_index = 0";
_is_gender_index = (int) (0);
 //BA.debugLineNum = 26;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "";
 //BA.debugLineNum = 26;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 27;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "";
 //BA.debugLineNum = 27;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "122.869168";
 //BA.debugLineNum = 28;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = 0;
 //BA.debugLineNum = 28;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = (int) (0);
 //BA.debugLineNum = 29;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = 0;
 //BA.debugLineNum = 29;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = (int) (0);
 //BA.debugLineNum = 30;BA.debugLine="Dim list_location_b,list_location_s,list_location";
_list_location_b = new anywheresoftware.b4a.objects.collections.List();
_list_location_s = new anywheresoftware.b4a.objects.collections.List();
_list_location_p = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 31;BA.debugLine="Dim optionSelected As String";
_optionselected = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim isDonateDate As String";
_isdonatedate = "";
 //BA.debugLineNum = 34;BA.debugLine="Dim donate_m_pos,donate_d_pos,donate_y_pos As Int";
_donate_m_pos = 0;
_donate_d_pos = 0;
_donate_y_pos = 0;
 //BA.debugLineNum = 35;BA.debugLine="Private image_container As String";
_image_container = "";
 //BA.debugLineNum = 36;BA.debugLine="Private panel_click_ As Int : panel_click_ = 0";
_panel_click_ = 0;
 //BA.debugLineNum = 36;BA.debugLine="Private panel_click_ As Int : panel_click_ = 0";
_panel_click_ = (int) (0);
 //BA.debugLineNum = 37;BA.debugLine="Private edit_panel_click_ As Int : edit_panel_cli";
_edit_panel_click_ = 0;
 //BA.debugLineNum = 37;BA.debugLine="Private edit_panel_click_ As Int : edit_panel_cli";
_edit_panel_click_ = (int) (0);
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _profile_all_body_click() throws Exception{
 //BA.debugLineNum = 427;BA.debugLine="Sub profile_all_body_click";
 //BA.debugLineNum = 429;BA.debugLine="End Sub";
return "";
}
public static String  _profile_click() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper _aa1 = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriters = null;
bloodlife.system.calculations _url_back = null;
anywheresoftware.b4a.objects.PanelWrapper _update_top_pnl = null;
String _all_users_info = "";
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 309;BA.debugLine="Sub profile_Click";
 //BA.debugLineNum = 310;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 311;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 312;BA.debugLine="profile_img.Tag = aa1";
mostCurrent._profile_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 313;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 314;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 315;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 316;BA.debugLine="a2.Start(profile)";
mostCurrent._a2.Start((android.view.View)(mostCurrent._profile.getObject()));
 //BA.debugLineNum = 317;BA.debugLine="aa1.Start(profile_img)";
_aa1.Start((android.view.View)(mostCurrent._profile_img.getObject()));
 //BA.debugLineNum = 319;BA.debugLine="ProgressDialogShow2(\"Please Wait..\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Please Wait..",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 320;BA.debugLine="panel_click_ = 1";
_panel_click_ = (int) (1);
 //BA.debugLineNum = 321;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 //BA.debugLineNum = 322;BA.debugLine="optionSelected = \"pofileView\"";
_optionselected = "pofileView";
 //BA.debugLineNum = 323;BA.debugLine="Dim TextWriters As TextWriter";
_textwriters = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 324;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"users_all_info.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 326;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 327;BA.debugLine="Dim update_top_pnl As Panel";
_update_top_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 328;BA.debugLine="update_top_pnl.Initialize(\"update_top_pnl\")";
_update_top_pnl.Initialize(mostCurrent.activityBA,"update_top_pnl");
 //BA.debugLineNum = 329;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 330;BA.debugLine="Dim all_users_info As String";
_all_users_info = "";
 //BA.debugLineNum = 331;BA.debugLine="all_inputs_down.Initialize(\"all_inputs_down\")";
mostCurrent._all_inputs_down.Initialize(mostCurrent.activityBA,"all_inputs_down");
 //BA.debugLineNum = 332;BA.debugLine="profile_all_body.Initialize(\"profile_all_body\")";
mostCurrent._profile_all_body.Initialize(mostCurrent.activityBA,"profile_all_body");
 //BA.debugLineNum = 334;BA.debugLine="all_info_query.Initialize(\"all_info_query\",Me)";
mostCurrent._all_info_query._initialize(processBA,"all_info_query",menu_form.getObject());
 //BA.debugLineNum = 335;BA.debugLine="all_users_info = url_back.php_email_url(\"search_a";
_all_users_info = _url_back._php_email_url("search_all_users_data.php");
 //BA.debugLineNum = 336;BA.debugLine="all_info_query.Download2(all_users_info,Array As";
mostCurrent._all_info_query._download2(_all_users_info,new String[]{"all_info","SELECT * FROM `person_info` where `id`='"+mostCurrent._login_form._id_query+"';"});
 //BA.debugLineNum = 343;BA.debugLine="scroll_myprof.Initialize(90%x,73%y,\"scroll_mypro";
mostCurrent._scroll_myprof.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (73),mostCurrent.activityBA),"scroll_myprof");
 //BA.debugLineNum = 344;BA.debugLine="scroll_myprof.Panel.LoadLayout(\"update_all_input";
mostCurrent._scroll_myprof.getPanel().LoadLayout("update_all_inputs",mostCurrent.activityBA);
 //BA.debugLineNum = 345;BA.debugLine="scroll_myprof.Color = Colors.Transparent";
mostCurrent._scroll_myprof.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 346;BA.debugLine="scroll_myprof.ScrollbarsVisibility(False,False)";
mostCurrent._scroll_myprof.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 347;BA.debugLine="update_top_pnl.LoadLayout(\"update_all_top\")";
_update_top_pnl.LoadLayout("update_all_top",mostCurrent.activityBA);
 //BA.debugLineNum = 349;BA.debugLine="scroll_myprof.SetBackgroundImage(LoadBitmap(File";
mostCurrent._scroll_myprof.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 350;BA.debugLine="all_inputs.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._all_inputs.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 351;BA.debugLine="all_inputs_top.SetBackgroundImage(LoadBitmap(Fil";
mostCurrent._all_inputs_top.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 352;BA.debugLine="all_inputs_down.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._all_inputs_down.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 353;BA.debugLine="lab_fullname.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._lab_fullname.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-4-user.png").getObject()));
 //BA.debugLineNum = 354;BA.debugLine="lab_bloodgroup.SetBackgroundImage(LoadBitmap(Fil";
mostCurrent._lab_bloodgroup.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-93-tint.png").getObject()));
 //BA.debugLineNum = 355;BA.debugLine="lab_email.SetBackgroundImage(LoadBitmap(File.Dir";
mostCurrent._lab_email.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-social-40-e-mail.png").getObject()));
 //BA.debugLineNum = 356;BA.debugLine="lab_phonenumber.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._lab_phonenumber.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt1.png").getObject()));
 //BA.debugLineNum = 357;BA.debugLine="lab_phonenumber2.SetBackgroundImage(LoadBitmap(F";
mostCurrent._lab_phonenumber2.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt2.png").getObject()));
 //BA.debugLineNum = 358;BA.debugLine="lab_location.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._lab_location.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-21-home.png").getObject()));
 //BA.debugLineNum = 359;BA.debugLine="lab_question.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._lab_question.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-353-nameplate.png").getObject()));
 //BA.debugLineNum = 360;BA.debugLine="lab_donate_confirm.SetBackgroundImage(LoadBitmap";
mostCurrent._lab_donate_confirm.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-152-new-window.png").getObject()));
 //BA.debugLineNum = 361;BA.debugLine="lab_bday.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._lab_bday.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-46-calendar.png").getObject()));
 //BA.debugLineNum = 362;BA.debugLine="lab_gender.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._lab_gender.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-25-parents.png").getObject()));
 //BA.debugLineNum = 364;BA.debugLine="all_inputs_top.Width = 90%x";
mostCurrent._all_inputs_top.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 365;BA.debugLine="all_inputs_top.Height = 32%y";
mostCurrent._all_inputs_top.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (32),mostCurrent.activityBA));
 //BA.debugLineNum = 366;BA.debugLine="tittle.SetLayout(1%x,1%y,88%x,9%y)";
mostCurrent._tittle.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (88),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 367;BA.debugLine="usr_img.SetLayout(((88%x/2)/2)+3%x,tittle.Top+t";
mostCurrent._usr_img.SetLayout((int) (((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (88),mostCurrent.activityBA)/(double)2)/(double)2)+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (3),mostCurrent.activityBA)),(int) (mostCurrent._tittle.getTop()+mostCurrent._tittle.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (19),mostCurrent.activityBA));
 //BA.debugLineNum = 369;BA.debugLine="all_inputs.Width = 90%x";
mostCurrent._all_inputs.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 370;BA.debugLine="all_inputs.Height = 73%y";
mostCurrent._all_inputs.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (73),mostCurrent.activityBA));
 //BA.debugLineNum = 371;BA.debugLine="lab_fullname.SetLayout(2%x,2%y,7%x,6%y)";
mostCurrent._lab_fullname.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 372;BA.debugLine="lab_bloodgroup.SetLayout(2%x,lab_fullname.Top+";
mostCurrent._lab_bloodgroup.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_fullname.getTop()+mostCurrent._lab_fullname.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 373;BA.debugLine="lab_email.SetLayout(2%x,lab_bloodgroup.Top+lab";
mostCurrent._lab_email.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_bloodgroup.getTop()+mostCurrent._lab_bloodgroup.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 374;BA.debugLine="lab_phonenumber.SetLayout(2%x,lab_email.Top+la";
mostCurrent._lab_phonenumber.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_email.getTop()+mostCurrent._lab_email.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 375;BA.debugLine="lab_phonenumber2.SetLayout(2%x,lab_phonenumber";
mostCurrent._lab_phonenumber2.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_phonenumber.getTop()+mostCurrent._lab_phonenumber.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 376;BA.debugLine="lab_bday.SetLayout(2%x,lab_phonenumber2.Top+la";
mostCurrent._lab_bday.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_phonenumber2.getTop()+mostCurrent._lab_phonenumber2.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 377;BA.debugLine="lab_location.SetLayout(2%x,lab_bday.Top+lab_bd";
mostCurrent._lab_location.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_bday.getTop()+mostCurrent._lab_bday.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 378;BA.debugLine="lab_question.SetLayout(2%x,lab_location.Top+la";
mostCurrent._lab_question.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_location.getTop()+mostCurrent._lab_location.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 379;BA.debugLine="lab_donate_confirm.SetLayout(2%x,lab_question.";
mostCurrent._lab_donate_confirm.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_question.getTop()+mostCurrent._lab_question.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 380;BA.debugLine="lab_gender.SetLayout(2%x,lab_donate_confirm.To";
mostCurrent._lab_gender.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_donate_confirm.getTop()+mostCurrent._lab_donate_confirm.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 382;BA.debugLine="text_fn.SetLayout(lab_fullname.Left+lab_fullnam";
mostCurrent._text_fn.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 383;BA.debugLine="text_blood.SetLayout(lab_fullname.Left+lab_full";
mostCurrent._text_blood.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_bloodgroup.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 384;BA.debugLine="text_email.SetLayout(lab_fullname.Left+lab_full";
mostCurrent._text_email.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_email.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 385;BA.debugLine="text_phonenumber.SetLayout(lab_fullname.Left+la";
mostCurrent._text_phonenumber.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_phonenumber.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 386;BA.debugLine="text_phonenumber2.SetLayout(lab_fullname.Left+l";
mostCurrent._text_phonenumber2.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_phonenumber2.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 387;BA.debugLine="text_bday.SetLayout(lab_fullname.Left+lab_fulln";
mostCurrent._text_bday.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_bday.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 388;BA.debugLine="text_location.SetLayout(lab_fullname.Left+lab_f";
mostCurrent._text_location.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_location.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 389;BA.debugLine="text_answer.SetLayout(lab_fullname.Left+lab_ful";
mostCurrent._text_answer.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_question.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 390;BA.debugLine="text_donated.SetLayout(lab_fullname.Left+lab_fu";
mostCurrent._text_donated.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_donate_confirm.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 391;BA.debugLine="text_gender.SetLayout(lab_fullname.Left+lab_ful";
mostCurrent._text_gender.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_gender.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 393;BA.debugLine="blood_edit.SetLayout(text_blood.Left+text_bloo";
mostCurrent._blood_edit.SetLayout((int) (mostCurrent._text_blood.getLeft()+mostCurrent._text_blood.getWidth()),mostCurrent._text_blood.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 394;BA.debugLine="bday_edit.SetLayout(text_bday.Left+text_bday.W";
mostCurrent._bday_edit.SetLayout((int) (mostCurrent._text_bday.getLeft()+mostCurrent._text_bday.getWidth()),mostCurrent._text_bday.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 395;BA.debugLine="locate_edit.SetLayout(text_location.Left+text_";
mostCurrent._locate_edit.SetLayout((int) (mostCurrent._text_location.getLeft()+mostCurrent._text_location.getWidth()),mostCurrent._text_location.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 396;BA.debugLine="donated_edit.SetLayout(text_donated.Left+text_";
mostCurrent._donated_edit.SetLayout((int) (mostCurrent._text_donated.getLeft()+mostCurrent._text_donated.getWidth()),mostCurrent._text_donated.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 397;BA.debugLine="edit_gender.SetLayout(text_gender.Left+text_ge";
mostCurrent._edit_gender.SetLayout((int) (mostCurrent._text_gender.getLeft()+mostCurrent._text_gender.getWidth()),mostCurrent._text_gender.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 399;BA.debugLine="update_btn.Initialize(\"update_btn\")";
mostCurrent._update_btn.Initialize(mostCurrent.activityBA,"update_btn");
 //BA.debugLineNum = 400;BA.debugLine="cancel_btn.Initialize(\"cancel_btn\")";
mostCurrent._cancel_btn.Initialize(mostCurrent.activityBA,"cancel_btn");
 //BA.debugLineNum = 401;BA.debugLine="update_btn.Text = \"UPDATE\"";
mostCurrent._update_btn.setText((Object)("UPDATE"));
 //BA.debugLineNum = 402;BA.debugLine="cancel_btn.Text = \"CANCEL\"";
mostCurrent._cancel_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 403;BA.debugLine="all_inputs_down.AddView(update_btn,6%x,1%y,35%";
mostCurrent._all_inputs_down.AddView((android.view.View)(mostCurrent._update_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (35),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 404;BA.debugLine="all_inputs_down.AddView(cancel_btn,update_btn.";
mostCurrent._all_inputs_down.AddView((android.view.View)(mostCurrent._cancel_btn.getObject()),(int) (mostCurrent._update_btn.getLeft()+mostCurrent._update_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (35),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 406;BA.debugLine="profile_all_body.Color = Colors.ARGB(128,128,128";
mostCurrent._profile_all_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (128),(int) (128),(int) (.50)));
 //BA.debugLineNum = 408;BA.debugLine="profile_all_body.AddView(update_top_pnl,5%x,2%y,";
mostCurrent._profile_all_body.AddView((android.view.View)(_update_top_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 409;BA.debugLine="profile_all_body.AddView(scroll_myprof,5%x,all_i";
mostCurrent._profile_all_body.AddView((android.view.View)(mostCurrent._scroll_myprof.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (mostCurrent._all_inputs_top.getTop()+mostCurrent._all_inputs_top.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (47),mostCurrent.activityBA));
 //BA.debugLineNum = 410;BA.debugLine="profile_all_body.AddView(all_inputs_down,5%x,scr";
mostCurrent._profile_all_body.AddView((android.view.View)(mostCurrent._all_inputs_down.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (mostCurrent._scroll_myprof.getTop()+mostCurrent._scroll_myprof.getHeight()-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (13),mostCurrent.activityBA));
 //BA.debugLineNum = 411;BA.debugLine="Activity.AddView(profile_all_body,0,0,100%x,100%";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._profile_all_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 413;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 414;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 415;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 416;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 417;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 418;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 419;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 420;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 421;BA.debugLine="update_btn.Background = V_btn";
mostCurrent._update_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 422;BA.debugLine="cancel_btn.Background = C_btn";
mostCurrent._cancel_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 423;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 612;BA.debugLine="Sub profiled_Click";
 //BA.debugLineNum = 614;BA.debugLine="profile_panel.Initialize(\"profile_panel\")";
mostCurrent._profile_panel.Initialize(mostCurrent.activityBA,"profile_panel");
 //BA.debugLineNum = 616;BA.debugLine="profile_panel.SetBackgroundImage(LoadBitmap(File";
mostCurrent._profile_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 617;BA.debugLine="Dim title As Label : title.Initialize(\"\")";
_title = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 617;BA.debugLine="Dim title As Label : title.Initialize(\"\")";
_title.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 618;BA.debugLine="Dim fullN As EditText : fullN.Initialize(\"\")";
_fulln = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 618;BA.debugLine="Dim fullN As EditText : fullN.Initialize(\"\")";
_fulln.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 619;BA.debugLine="Dim blood_sel As Label : blood_sel.Initialize(\"\"";
_blood_sel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 619;BA.debugLine="Dim blood_sel As Label : blood_sel.Initialize(\"\"";
_blood_sel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 620;BA.debugLine="Dim email As EditText : email.Initialize(\"\")";
_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 620;BA.debugLine="Dim email As EditText : email.Initialize(\"\")";
_email.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 621;BA.debugLine="Dim phone1 As EditText : phone1.Initialize(\"\")";
_phone1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 621;BA.debugLine="Dim phone1 As EditText : phone1.Initialize(\"\")";
_phone1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 622;BA.debugLine="Dim phone2 As EditText : phone2.Initialize(\"\")";
_phone2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 622;BA.debugLine="Dim phone2 As EditText : phone2.Initialize(\"\")";
_phone2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 623;BA.debugLine="Dim location As Label : location.Initialize(\"\")";
_location = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 623;BA.debugLine="Dim location As Label : location.Initialize(\"\")";
_location.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 624;BA.debugLine="Dim bday As Label : bday.Initialize(\"\")";
_bday = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 624;BA.debugLine="Dim bday As Label : bday.Initialize(\"\")";
_bday.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 625;BA.debugLine="Dim nickN As EditText : nickN.Initialize(\"\")";
_nickn = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 625;BA.debugLine="Dim nickN As EditText : nickN.Initialize(\"\")";
_nickn.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 626;BA.debugLine="Dim isDonated As Label : isDonated.Initialize(\"\"";
_isdonated = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 626;BA.debugLine="Dim isDonated As Label : isDonated.Initialize(\"\"";
_isdonated.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 627;BA.debugLine="Dim img_fullN As ImageView : img_fullN.Initiali";
_img_fulln = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 627;BA.debugLine="Dim img_fullN As ImageView : img_fullN.Initiali";
_img_fulln.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 628;BA.debugLine="Dim img_blood_sel As ImageView : img_blood_sel.";
_img_blood_sel = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 628;BA.debugLine="Dim img_blood_sel As ImageView : img_blood_sel.";
_img_blood_sel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 629;BA.debugLine="Dim img_email As ImageView : img_email.Initiali";
_img_email = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 629;BA.debugLine="Dim img_email As ImageView : img_email.Initiali";
_img_email.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 630;BA.debugLine="Dim img_phone1 As ImageView : img_phone1.Initia";
_img_phone1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 630;BA.debugLine="Dim img_phone1 As ImageView : img_phone1.Initia";
_img_phone1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 631;BA.debugLine="Dim img_phone2 As ImageView : img_phone2.Initia";
_img_phone2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 631;BA.debugLine="Dim img_phone2 As ImageView : img_phone2.Initia";
_img_phone2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 632;BA.debugLine="Dim img_location As ImageView : img_location.In";
_img_location = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 632;BA.debugLine="Dim img_location As ImageView : img_location.In";
_img_location.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 633;BA.debugLine="Dim img_bday As ImageView : img_bday.Initialize";
_img_bday = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 633;BA.debugLine="Dim img_bday As ImageView : img_bday.Initialize";
_img_bday.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 634;BA.debugLine="Dim img_nickN As ImageView : img_nickN.Initiali";
_img_nickn = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 634;BA.debugLine="Dim img_nickN As ImageView : img_nickN.Initiali";
_img_nickn.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 635;BA.debugLine="Dim img_isDonated As ImageView : img_isDonated.";
_img_isdonated = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 635;BA.debugLine="Dim img_isDonated As ImageView : img_isDonated.";
_img_isdonated.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 637;BA.debugLine="title.Text = \"My Information\" '' titte";
_title.setText((Object)("My Information"));
 //BA.debugLineNum = 638;BA.debugLine="title.Typeface = Typeface.LoadFromAssets(\"HipH";
_title.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 639;BA.debugLine="title.Gravity = Gravity.CENTER";
_title.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 640;BA.debugLine="title.TextSize = 20 '''-------";
_title.setTextSize((float) (20));
 //BA.debugLineNum = 642;BA.debugLine="blood_sel.Text = \"A\"";
_blood_sel.setText((Object)("A"));
 //BA.debugLineNum = 643;BA.debugLine="bday.Text = \"may/13/1993\"";
_bday.setText((Object)("may/13/1993"));
 //BA.debugLineNum = 644;BA.debugLine="location.Text = \"hinigaran neg occ\"";
_location.setText((Object)("hinigaran neg occ"));
 //BA.debugLineNum = 645;BA.debugLine="img_fullN.SetBackgroundImage(LoadBitmap(File.Di";
_img_fulln.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-4-user.png").getObject()));
 //BA.debugLineNum = 646;BA.debugLine="img_blood_sel.SetBackgroundImage(LoadBitmap(Fil";
_img_blood_sel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-93-tint.png").getObject()));
 //BA.debugLineNum = 647;BA.debugLine="img_email.SetBackgroundImage(LoadBitmap(File.Di";
_img_email.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-social-40-e-mail.png").getObject()));
 //BA.debugLineNum = 648;BA.debugLine="img_phone1.SetBackgroundImage(LoadBitmap(File.D";
_img_phone1.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt1.png").getObject()));
 //BA.debugLineNum = 649;BA.debugLine="img_phone2.SetBackgroundImage(LoadBitmap(File.D";
_img_phone2.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt2.png").getObject()));
 //BA.debugLineNum = 650;BA.debugLine="img_location.SetBackgroundImage(LoadBitmap(File";
_img_location.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-21-home.png").getObject()));
 //BA.debugLineNum = 651;BA.debugLine="img_bday.SetBackgroundImage(LoadBitmap(File.Dir";
_img_bday.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-40-notes.png").getObject()));
 //BA.debugLineNum = 652;BA.debugLine="img_nickN.SetBackgroundImage(LoadBitmap(File.Di";
_img_nickn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-265-vcard.png").getObject()));
 //BA.debugLineNum = 653;BA.debugLine="img_isDonated.SetBackgroundImage(LoadBitmap(Fil";
_img_isdonated.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-91-eyedropper.png").getObject()));
 //BA.debugLineNum = 656;BA.debugLine="profile_panel.AddView(title,0,1%y,90%x,8%y) '";
mostCurrent._profile_panel.AddView((android.view.View)(_title.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 657;BA.debugLine="profile_panel.AddView(img_fullN,5%x, title.Top+t";
mostCurrent._profile_panel.AddView((android.view.View)(_img_fulln.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_title.getTop()+_title.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 658;BA.debugLine="profile_panel.AddView(fullN, img_fullN.Left+img_";
mostCurrent._profile_panel.AddView((android.view.View)(_fulln.getObject()),(int) (_img_fulln.getLeft()+_img_fulln.getWidth()),(int) (_title.getTop()+_title.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 660;BA.debugLine="profile_panel.AddView(img_blood_sel,5%x,fullN.To";
mostCurrent._profile_panel.AddView((android.view.View)(_img_blood_sel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_fulln.getTop()+_fulln.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 661;BA.debugLine="profile_panel.AddView(blood_sel,img_blood_sel.Le";
mostCurrent._profile_panel.AddView((android.view.View)(_blood_sel.getObject()),(int) (_img_blood_sel.getLeft()+_img_blood_sel.getWidth()),_img_blood_sel.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 663;BA.debugLine="profile_panel.AddView(img_email,5%x,img_blood_se";
mostCurrent._profile_panel.AddView((android.view.View)(_img_email.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_blood_sel.getTop()+_img_blood_sel.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 664;BA.debugLine="profile_panel.AddView(email,img_email.Left+img_e";
mostCurrent._profile_panel.AddView((android.view.View)(_email.getObject()),(int) (_img_email.getLeft()+_img_email.getWidth()),_img_email.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 666;BA.debugLine="profile_panel.AddView(img_phone1,5%x,img_email.T";
mostCurrent._profile_panel.AddView((android.view.View)(_img_phone1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_email.getTop()+_img_email.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 667;BA.debugLine="profile_panel.AddView(phone1,img_phone1.Left+img";
mostCurrent._profile_panel.AddView((android.view.View)(_phone1.getObject()),(int) (_img_phone1.getLeft()+_img_phone1.getWidth()),_img_phone1.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 669;BA.debugLine="profile_panel.AddView(img_phone2,5%x,img_phone1.";
mostCurrent._profile_panel.AddView((android.view.View)(_img_phone2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_phone1.getTop()+_img_phone1.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 670;BA.debugLine="profile_panel.AddView(phone2,img_phone2.Left+img";
mostCurrent._profile_panel.AddView((android.view.View)(_phone2.getObject()),(int) (_img_phone2.getLeft()+_img_phone2.getWidth()),_img_phone2.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 672;BA.debugLine="profile_panel.AddView(img_location,5%x,img_phone";
mostCurrent._profile_panel.AddView((android.view.View)(_img_location.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_phone2.getTop()+_img_phone2.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 673;BA.debugLine="profile_panel.AddView(location,img_location.Left";
mostCurrent._profile_panel.AddView((android.view.View)(_location.getObject()),(int) (_img_location.getLeft()+_img_location.getWidth()),_img_location.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 675;BA.debugLine="profile_panel.AddView(img_bday,5%x,img_location.";
mostCurrent._profile_panel.AddView((android.view.View)(_img_bday.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_location.getTop()+_img_location.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 676;BA.debugLine="profile_panel.AddView(bday,img_bday.Left+img_bda";
mostCurrent._profile_panel.AddView((android.view.View)(_bday.getObject()),(int) (_img_bday.getLeft()+_img_bday.getWidth()),_img_bday.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 678;BA.debugLine="profile_panel.AddView(img_nickN,5%x,img_bday.Top";
mostCurrent._profile_panel.AddView((android.view.View)(_img_nickn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_bday.getTop()+_img_bday.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 679;BA.debugLine="profile_panel.AddView(nickN,img_nickN.Left+img_n";
mostCurrent._profile_panel.AddView((android.view.View)(_nickn.getObject()),(int) (_img_nickn.getLeft()+_img_nickn.getWidth()),_img_nickn.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 681;BA.debugLine="profile_panel.AddView(img_isDonated,5%x,img_nick";
mostCurrent._profile_panel.AddView((android.view.View)(_img_isdonated.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_nickn.getTop()+_img_nickn.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 682;BA.debugLine="profile_panel.AddView(isDonated,img_isDonated.Le";
mostCurrent._profile_panel.AddView((android.view.View)(_isdonated.getObject()),(int) (_img_isdonated.getLeft()+_img_isdonated.getWidth()),_img_isdonated.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 685;BA.debugLine="scroll_profile_pnl.Panel.AddView(profile_panel,0";
mostCurrent._scroll_profile_pnl.getPanel().AddView((android.view.View)(mostCurrent._profile_panel.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 686;BA.debugLine="Activity.AddView(scroll_profile_pnl,5%x,3%y,90%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._scroll_profile_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 688;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _resizebitmap(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _original,int _width,int _height) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _new = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper _c = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _destrect = null;
 //BA.debugLineNum = 571;BA.debugLine="Sub ResizeBitmap(original As Bitmap, width As Int,";
 //BA.debugLineNum = 572;BA.debugLine="Dim new As Bitmap";
_new = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 573;BA.debugLine="new.InitializeMutable(width, height)";
_new.InitializeMutable(_width,_height);
 //BA.debugLineNum = 574;BA.debugLine="Dim c As Canvas";
_c = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 575;BA.debugLine="c.Initialize2(new)";
_c.Initialize2((android.graphics.Bitmap)(_new.getObject()));
 //BA.debugLineNum = 576;BA.debugLine="Dim destRect As Rect";
_destrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 577;BA.debugLine="destRect.Initialize(0, 0, width, height)";
_destrect.Initialize((int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 578;BA.debugLine="c.DrawBitmap(original, Null, destRect)";
_c.DrawBitmap((android.graphics.Bitmap)(_original.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_destrect.getObject()));
 //BA.debugLineNum = 579;BA.debugLine="Return new";
if (true) return _new;
 //BA.debugLineNum = 580;BA.debugLine="End Sub";
return null;
}
public static String  _search_blood_click() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper _aa1 = null;
 //BA.debugLineNum = 298;BA.debugLine="Sub search_blood_Click";
 //BA.debugLineNum = 299;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 300;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 301;BA.debugLine="srch_blood_img.Tag = aa1";
mostCurrent._srch_blood_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 302;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 303;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 304;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 305;BA.debugLine="a1.Start(search_blood)";
mostCurrent._a1.Start((android.view.View)(mostCurrent._search_blood.getObject()));
 //BA.debugLineNum = 306;BA.debugLine="aa1.Start(srch_blood_img)";
_aa1.Start((android.view.View)(mostCurrent._srch_blood_img.getObject()));
 //BA.debugLineNum = 307;BA.debugLine="StartActivity(\"search_frame\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("search_frame"));
 //BA.debugLineNum = 308;BA.debugLine="End Sub";
return "";
}
public static String  _spin_bloodgroup_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1167;BA.debugLine="Sub spin_bloodgroup_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 1168;BA.debugLine="blood_selected = Value";
_blood_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 1169;BA.debugLine="End Sub";
return "";
}
public static String  _spin_day_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1078;BA.debugLine="Sub spin_day_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 1079;BA.debugLine="bday_day_selected = Value";
_bday_day_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 1080;BA.debugLine="Log(\"day: \"&Value)";
anywheresoftware.b4a.keywords.Common.Log("day: "+BA.ObjectToString(_value));
 //BA.debugLineNum = 1081;BA.debugLine="End Sub";
return "";
}
public static String  _spin_donated_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 893;BA.debugLine="Sub spin_donated_ItemClick (Position As Int, Value";
 //BA.debugLineNum = 894;BA.debugLine="Log(Position)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_position));
 //BA.debugLineNum = 895;BA.debugLine="is_donated = Value";
_is_donated = BA.ObjectToString(_value);
 //BA.debugLineNum = 896;BA.debugLine="donated_index = Position";
_donated_index = _position;
 //BA.debugLineNum = 897;BA.debugLine="End Sub";
return "";
}
public static String  _spin_gender_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 833;BA.debugLine="Sub spin_gender_ItemClick (Position As Int, Value";
 //BA.debugLineNum = 834;BA.debugLine="is_gender_index = Position";
_is_gender_index = _position;
 //BA.debugLineNum = 835;BA.debugLine="End Sub";
return "";
}
public static String  _spin_month_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1082;BA.debugLine="Sub spin_month_ItemClick (Position As Int, Value A";
 //BA.debugLineNum = 1083;BA.debugLine="bday_month_selected = Value";
_bday_month_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 1084;BA.debugLine="Log(\"month: \"&Value)";
anywheresoftware.b4a.keywords.Common.Log("month: "+BA.ObjectToString(_value));
 //BA.debugLineNum = 1085;BA.debugLine="End Sub";
return "";
}
public static String  _spin_year_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1086;BA.debugLine="Sub spin_year_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 1087;BA.debugLine="bday_year_selected =  Value";
_bday_year_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 1088;BA.debugLine="Log(\"year: \"&Value)";
anywheresoftware.b4a.keywords.Common.Log("year: "+BA.ObjectToString(_value));
 //BA.debugLineNum = 1089;BA.debugLine="End Sub";
return "";
}
public static String  _street_lat_lng() throws Exception{
 //BA.debugLineNum = 1265;BA.debugLine="Sub street_lat_lng";
 //BA.debugLineNum = 1266;BA.debugLine="If brgy_index == 0 And street_index == 0 Then";
if (_brgy_index==0 && _street_index==0) { 
 //BA.debugLineNum = 1267;BA.debugLine="lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 1268;BA.debugLine="lng = \"122.869168\"";
_lng = "122.869168";
 }else if(_brgy_index==0 && _street_index==1) { 
 //BA.debugLineNum = 1270;BA.debugLine="lat = \"10.097226\"";
_lat = "10.097226";
 //BA.debugLineNum = 1271;BA.debugLine="lng = \"122.870659\"";
_lng = "122.870659";
 }else if(_brgy_index==0 && _street_index==2) { 
 //BA.debugLineNum = 1273;BA.debugLine="lat = \"10.097711\"";
_lat = "10.097711";
 //BA.debugLineNum = 1274;BA.debugLine="lng = \"122.868378\"";
_lng = "122.868378";
 }else if(_brgy_index==0 && _street_index==3) { 
 //BA.debugLineNum = 1276;BA.debugLine="lat = \"10.098293\"";
_lat = "10.098293";
 //BA.debugLineNum = 1277;BA.debugLine="lng = \"122.868977\"";
_lng = "122.868977";
 }else if(_brgy_index==0 && _street_index==4) { 
 //BA.debugLineNum = 1279;BA.debugLine="lat = \"10.097031\"";
_lat = "10.097031";
 //BA.debugLineNum = 1280;BA.debugLine="lng = \"122.868764\"";
_lng = "122.868764";
 }else if(_brgy_index==0 && _street_index==5) { 
 //BA.debugLineNum = 1282;BA.debugLine="lat = \"10.096021\"";
_lat = "10.096021";
 //BA.debugLineNum = 1283;BA.debugLine="lng = \"122.869737\"";
_lng = "122.869737";
 }else if(_brgy_index==0 && _street_index==6) { 
 //BA.debugLineNum = 1285;BA.debugLine="lat = \"10.095142\"";
_lat = "10.095142";
 //BA.debugLineNum = 1286;BA.debugLine="lng = \"122.868317\"";
_lng = "122.868317";
 }else if(_brgy_index==0 && _street_index==7) { 
 //BA.debugLineNum = 1288;BA.debugLine="lat = \"10.095303\"";
_lat = "10.095303";
 //BA.debugLineNum = 1289;BA.debugLine="lng = \"122.869509\"";
_lng = "122.869509";
 };
 //BA.debugLineNum = 1292;BA.debugLine="If brgy_index == 1 And street_index == 0 Then 'br";
if (_brgy_index==1 && _street_index==0) { 
 //BA.debugLineNum = 1293;BA.debugLine="lat = \"10.101356\"";
_lat = "10.101356";
 //BA.debugLineNum = 1294;BA.debugLine="lng = \"122.870075\"";
_lng = "122.870075";
 }else if(_brgy_index==1 && _street_index==1) { 
 //BA.debugLineNum = 1296;BA.debugLine="lat = \"10.100583\"";
_lat = "10.100583";
 //BA.debugLineNum = 1297;BA.debugLine="lng = \"122.870176\"";
_lng = "122.870176";
 }else if(_brgy_index==1 && _street_index==2) { 
 //BA.debugLineNum = 1299;BA.debugLine="lat = \"10.100031\"";
_lat = "10.100031";
 //BA.debugLineNum = 1300;BA.debugLine="lng = \"122.870623\"";
_lng = "122.870623";
 }else if(_brgy_index==1 && _street_index==3) { 
 //BA.debugLineNum = 1302;BA.debugLine="lat = \"10.101327\"";
_lat = "10.101327";
 //BA.debugLineNum = 1303;BA.debugLine="lng = \"122.871177\"";
_lng = "122.871177";
 }else if(_brgy_index==1 && _street_index==4) { 
 //BA.debugLineNum = 1305;BA.debugLine="lat = \"10.103330\"";
_lat = "10.103330";
 //BA.debugLineNum = 1306;BA.debugLine="lng = \"122.871391\"";
_lng = "122.871391";
 }else if(_brgy_index==1 && _street_index==5) { 
 //BA.debugLineNum = 1308;BA.debugLine="lat = \"10.102317\"";
_lat = "10.102317";
 //BA.debugLineNum = 1309;BA.debugLine="lng = \"122.870755\"";
_lng = "122.870755";
 }else if(_brgy_index==1 && _street_index==6) { 
 //BA.debugLineNum = 1311;BA.debugLine="lat = \"10.104250\"";
_lat = "10.104250";
 //BA.debugLineNum = 1312;BA.debugLine="lng = \"122.882834\"";
_lng = "122.882834";
 }else if(_brgy_index==1 && _street_index==7) { 
 //BA.debugLineNum = 1314;BA.debugLine="lat = \"10.104943\"";
_lat = "10.104943";
 //BA.debugLineNum = 1315;BA.debugLine="lng = \"122.885207\"";
_lng = "122.885207";
 }else if(_brgy_index==1 && _street_index==8) { 
 //BA.debugLineNum = 1317;BA.debugLine="lat = \"10.101843\"";
_lat = "10.101843";
 //BA.debugLineNum = 1318;BA.debugLine="lng = \"122.871020\"";
_lng = "122.871020";
 }else if(_brgy_index==1 && _street_index==9) { 
 //BA.debugLineNum = 1320;BA.debugLine="lat = \"10.103477\"";
_lat = "10.103477";
 //BA.debugLineNum = 1321;BA.debugLine="lng = \"122.870042\"";
_lng = "122.870042";
 }else if(_brgy_index==1 && _street_index==10) { 
 //BA.debugLineNum = 1323;BA.debugLine="lat = \"10.100710\"";
_lat = "10.100710";
 //BA.debugLineNum = 1324;BA.debugLine="lng = \"122.870889\"";
_lng = "122.870889";
 };
 //BA.debugLineNum = 1327;BA.debugLine="If brgy_index == 2 And street_index == 0 Then 'br";
if (_brgy_index==2 && _street_index==0) { 
 //BA.debugLineNum = 1328;BA.debugLine="lat = \"10.095478\"";
_lat = "10.095478";
 //BA.debugLineNum = 1329;BA.debugLine="lng = \"122.871176\"";
_lng = "122.871176";
 }else if(_brgy_index==2 && _street_index==1) { 
 //BA.debugLineNum = 1331;BA.debugLine="lat = \"10.098599\"";
_lat = "10.098599";
 //BA.debugLineNum = 1332;BA.debugLine="lng = \"122.871761\"";
_lng = "122.871761";
 }else if(_brgy_index==2 && _street_index==2) { 
 //BA.debugLineNum = 1334;BA.debugLine="lat = \"10.094573\"";
_lat = "10.094573";
 //BA.debugLineNum = 1335;BA.debugLine="lng = \"122.870340\"";
_lng = "122.870340";
 }else if(_brgy_index==2 && _street_index==3) { 
 //BA.debugLineNum = 1337;BA.debugLine="lat = \"10.098313\"";
_lat = "10.098313";
 //BA.debugLineNum = 1338;BA.debugLine="lng = \"122.875223\"";
_lng = "122.875223";
 }else if(_brgy_index==2 && _street_index==4) { 
 //BA.debugLineNum = 1340;BA.debugLine="lat = \"10.092235\"";
_lat = "10.092235";
 //BA.debugLineNum = 1341;BA.debugLine="lng = \"122.874356\"";
_lng = "122.874356";
 }else if(_brgy_index==2 && _street_index==5) { 
 //BA.debugLineNum = 1343;BA.debugLine="lat = \"10.103982\"";
_lat = "10.103982";
 //BA.debugLineNum = 1344;BA.debugLine="lng = \"122.885996\"";
_lng = "122.885996";
 }else if(_brgy_index==2 && _street_index==6) { 
 //BA.debugLineNum = 1346;BA.debugLine="lat = \"10.102170\"";
_lat = "10.102170";
 //BA.debugLineNum = 1347;BA.debugLine="lng = \"122.882390\"";
_lng = "122.882390";
 }else if(_brgy_index==2 && _street_index==7) { 
 //BA.debugLineNum = 1349;BA.debugLine="lat = \"10.103272\"";
_lat = "10.103272";
 //BA.debugLineNum = 1350;BA.debugLine="lng = \"122.883948\"";
_lng = "122.883948";
 }else if(_brgy_index==2 && _street_index==8) { 
 //BA.debugLineNum = 1352;BA.debugLine="lat = \"10.103849\"";
_lat = "10.103849";
 //BA.debugLineNum = 1353;BA.debugLine="lng = \"122.884602\"";
_lng = "122.884602";
 }else if(_brgy_index==2 && _street_index==9) { 
 //BA.debugLineNum = 1355;BA.debugLine="lat = \"10.101033\"";
_lat = "10.101033";
 //BA.debugLineNum = 1356;BA.debugLine="lng = \"122.874480\"";
_lng = "122.874480";
 };
 //BA.debugLineNum = 1359;BA.debugLine="If brgy_index == 3 And street_index == 0 Then 'b";
if (_brgy_index==3 && _street_index==0) { 
 //BA.debugLineNum = 1360;BA.debugLine="lat = \"10.121855\"";
_lat = "10.121855";
 //BA.debugLineNum = 1361;BA.debugLine="lng = \"122.872266\"";
_lng = "122.872266";
 }else if(_brgy_index==3 && _street_index==1) { 
 //BA.debugLineNum = 1363;BA.debugLine="lat = \"10.116699\"";
_lat = "10.116699";
 //BA.debugLineNum = 1364;BA.debugLine="lng = \"122.871783\"";
_lng = "122.871783";
 }else if(_brgy_index==3 && _street_index==2) { 
 //BA.debugLineNum = 1366;BA.debugLine="lat = \"10.116024\"";
_lat = "10.116024";
 //BA.debugLineNum = 1367;BA.debugLine="lng = \"122.872477\"";
_lng = "122.872477";
 }else if(_brgy_index==3 && _street_index==3) { 
 //BA.debugLineNum = 1369;BA.debugLine="lat = \"10.114588\"";
_lat = "10.114588";
 //BA.debugLineNum = 1370;BA.debugLine="lng = \"122.872515\"";
_lng = "122.872515";
 }else if(_brgy_index==3 && _street_index==4) { 
 //BA.debugLineNum = 1372;BA.debugLine="lat = \"10.112140\"";
_lat = "10.112140";
 //BA.debugLineNum = 1373;BA.debugLine="lng = \"122.872161\"";
_lng = "122.872161";
 }else if(_brgy_index==3 && _street_index==5) { 
 //BA.debugLineNum = 1375;BA.debugLine="lat = \"10.111531\"";
_lat = "10.111531";
 //BA.debugLineNum = 1376;BA.debugLine="lng = \"122.871542\"";
_lng = "122.871542";
 }else if(_brgy_index==3 && _street_index==6) { 
 //BA.debugLineNum = 1378;BA.debugLine="lat = \"10.107168\"";
_lat = "10.107168";
 //BA.debugLineNum = 1379;BA.debugLine="lng = \"122.871766\"";
_lng = "122.871766";
 }else if(_brgy_index==3 && _street_index==7) { 
 //BA.debugLineNum = 1381;BA.debugLine="lat = \"10.106570\"";
_lat = "10.106570";
 //BA.debugLineNum = 1382;BA.debugLine="lng = \"122.875197\"";
_lng = "122.875197";
 }else if(_brgy_index==3 && _street_index==8) { 
 //BA.debugLineNum = 1384;BA.debugLine="lat = \"10.105759\"";
_lat = "10.105759";
 //BA.debugLineNum = 1385;BA.debugLine="lng = \"122.871537\"";
_lng = "122.871537";
 };
 //BA.debugLineNum = 1388;BA.debugLine="If brgy_index == 4 And street_index == 0 Then 'A";
if (_brgy_index==4 && _street_index==0) { 
 //BA.debugLineNum = 1389;BA.debugLine="lat = \"10.165214\"";
_lat = "10.165214";
 //BA.debugLineNum = 1390;BA.debugLine="lng = \"122.865433\"";
_lng = "122.865433";
 }else if(_brgy_index==4 && _street_index==1) { 
 //BA.debugLineNum = 1392;BA.debugLine="lat = \"10.154170\"";
_lat = "10.154170";
 //BA.debugLineNum = 1393;BA.debugLine="lng = \"122.867255\"";
_lng = "122.867255";
 }else if(_brgy_index==4 && _street_index==2) { 
 //BA.debugLineNum = 1395;BA.debugLine="lat = \"10.161405\"";
_lat = "10.161405";
 //BA.debugLineNum = 1396;BA.debugLine="lng = \"122.862692\"";
_lng = "122.862692";
 }else if(_brgy_index==4 && _street_index==3) { 
 //BA.debugLineNum = 1398;BA.debugLine="lat = \"10.168471\"";
_lat = "10.168471";
 //BA.debugLineNum = 1399;BA.debugLine="lng = \"122.860955\"";
_lng = "122.860955";
 }else if(_brgy_index==4 && _street_index==4) { 
 //BA.debugLineNum = 1401;BA.debugLine="lat = \"10.172481\"";
_lat = "10.172481";
 //BA.debugLineNum = 1402;BA.debugLine="lng = \"122.858629\"";
_lng = "122.858629";
 }else if(_brgy_index==4 && _street_index==5) { 
 //BA.debugLineNum = 1404;BA.debugLine="lat = \"10.166561\"";
_lat = "10.166561";
 //BA.debugLineNum = 1405;BA.debugLine="lng = \"122.859428\"";
_lng = "122.859428";
 }else if(_brgy_index==4 && _street_index==6) { 
 //BA.debugLineNum = 1407;BA.debugLine="lat = \"10.163510\"";
_lat = "10.163510";
 //BA.debugLineNum = 1408;BA.debugLine="lng = \"122.860074\"";
_lng = "122.860074";
 }else if(_brgy_index==4 && _street_index==7) { 
 //BA.debugLineNum = 1410;BA.debugLine="lat = \"10.161033\"";
_lat = "10.161033";
 //BA.debugLineNum = 1411;BA.debugLine="lng = \"122.859773\"";
_lng = "122.859773";
 }else if(_brgy_index==4 && _street_index==8) { 
 //BA.debugLineNum = 1413;BA.debugLine="lat = \"10.159280\"";
_lat = "10.159280";
 //BA.debugLineNum = 1414;BA.debugLine="lng = \"122.861621\"";
_lng = "122.861621";
 }else if(_brgy_index==4 && _street_index==9) { 
 //BA.debugLineNum = 1416;BA.debugLine="lat = \"10.159062\"";
_lat = "10.159062";
 //BA.debugLineNum = 1417;BA.debugLine="lng = \"122.860209\"";
_lng = "122.860209";
 }else if(_brgy_index==4 && _street_index==10) { 
 //BA.debugLineNum = 1419;BA.debugLine="lat = \"10.181112\"";
_lat = "10.181112";
 //BA.debugLineNum = 1420;BA.debugLine="lng = \"122.864670\"";
_lng = "122.864670";
 }else if(_brgy_index==4 && _street_index==11) { 
 //BA.debugLineNum = 1422;BA.debugLine="lat = \"10.167295\"";
_lat = "10.167295";
 //BA.debugLineNum = 1423;BA.debugLine="lng = \"122.857858\"";
_lng = "122.857858";
 };
 //BA.debugLineNum = 1426;BA.debugLine="If brgy_index == 5 And street_index == 0 Then 'ca";
if (_brgy_index==5 && _street_index==0) { 
 //BA.debugLineNum = 1427;BA.debugLine="lat = \"10.092993\"";
_lat = "10.092993";
 //BA.debugLineNum = 1428;BA.debugLine="lng = \"122.861694\"";
_lng = "122.861694";
 }else if(_brgy_index==5 && _street_index==1) { 
 //BA.debugLineNum = 1430;BA.debugLine="lat = \"10.090587\"";
_lat = "10.090587";
 //BA.debugLineNum = 1431;BA.debugLine="lng = \"122.868414\"";
_lng = "122.868414";
 }else if(_brgy_index==5 && _street_index==2) { 
 //BA.debugLineNum = 1433;BA.debugLine="lat = \"10.091551\"";
_lat = "10.091551";
 //BA.debugLineNum = 1434;BA.debugLine="lng = \"122.869249\"";
_lng = "122.869249";
 }else if(_brgy_index==5 && _street_index==3) { 
 //BA.debugLineNum = 1436;BA.debugLine="lat = \"10.086452\"";
_lat = "10.086452";
 //BA.debugLineNum = 1437;BA.debugLine="lng = \"122.865742\"";
_lng = "122.865742";
 }else if(_brgy_index==5 && _street_index==4) { 
 //BA.debugLineNum = 1439;BA.debugLine="lat = \"10.083507\"";
_lat = "10.083507";
 //BA.debugLineNum = 1440;BA.debugLine="lng = \"122.858928\"";
_lng = "122.858928";
 }else if(_brgy_index==5 && _street_index==5) { 
 //BA.debugLineNum = 1442;BA.debugLine="lat = \"10.077131\"";
_lat = "10.077131";
 //BA.debugLineNum = 1443;BA.debugLine="lng = \"122.864236\"";
_lng = "122.864236";
 }else if(_brgy_index==5 && _street_index==6) { 
 //BA.debugLineNum = 1445;BA.debugLine="lat = \"10.081722\"";
_lat = "10.081722";
 //BA.debugLineNum = 1446;BA.debugLine="lng = \"122.882661\"";
_lng = "122.882661";
 }else if(_brgy_index==5 && _street_index==7) { 
 //BA.debugLineNum = 1448;BA.debugLine="lat = \"10.081822\"";
_lat = "10.081822";
 //BA.debugLineNum = 1449;BA.debugLine="lng = \"122.868295\"";
_lng = "122.868295";
 }else if(_brgy_index==5 && _street_index==8) { 
 //BA.debugLineNum = 1451;BA.debugLine="lat = \"10.079513\"";
_lat = "10.079513";
 //BA.debugLineNum = 1452;BA.debugLine="lng = \"122.876610\"";
_lng = "122.876610";
 }else if(_brgy_index==5 && _street_index==9) { 
 //BA.debugLineNum = 1454;BA.debugLine="lat = \"10.068560\"";
_lat = "10.068560";
 //BA.debugLineNum = 1455;BA.debugLine="lng = \"122.887366\"";
_lng = "122.887366";
 }else if(_brgy_index==5 && _street_index==10) { 
 //BA.debugLineNum = 1457;BA.debugLine="lat = \"10.066934\"";
_lat = "10.066934";
 //BA.debugLineNum = 1458;BA.debugLine="lng = \"122.871963\"";
_lng = "122.871963";
 }else if(_brgy_index==5 && _street_index==11) { 
 //BA.debugLineNum = 1460;BA.debugLine="lat = \"10.064251\"";
_lat = "10.064251";
 //BA.debugLineNum = 1461;BA.debugLine="lng = \"122.883023\"";
_lng = "122.883023";
 }else if(_brgy_index==5 && _street_index==12) { 
 //BA.debugLineNum = 1463;BA.debugLine="lat = \"10.058546\"";
_lat = "10.058546";
 //BA.debugLineNum = 1464;BA.debugLine="lng = \"122.882968\"";
_lng = "122.882968";
 }else if(_brgy_index==5 && _street_index==13) { 
 //BA.debugLineNum = 1466;BA.debugLine="lat = \"10.054104\"";
_lat = "10.054104";
 //BA.debugLineNum = 1467;BA.debugLine="lng = \"122.885506\"";
_lng = "122.885506";
 }else if(_brgy_index==5 && _street_index==14) { 
 //BA.debugLineNum = 1469;BA.debugLine="lat = \"10.049464\"";
_lat = "10.049464";
 //BA.debugLineNum = 1470;BA.debugLine="lng = \"122.885667\"";
_lng = "122.885667";
 }else if(_brgy_index==5 && _street_index==15) { 
 //BA.debugLineNum = 1472;BA.debugLine="lat = \"10.041580\"";
_lat = "10.041580";
 //BA.debugLineNum = 1473;BA.debugLine="lng = \"122.900269\"";
_lng = "122.900269";
 }else if(_brgy_index==5 && _street_index==16) { 
 //BA.debugLineNum = 1475;BA.debugLine="lat = \"10.041395\"";
_lat = "10.041395";
 //BA.debugLineNum = 1476;BA.debugLine="lng = \"122.906248\"";
_lng = "122.906248";
 };
 //BA.debugLineNum = 1479;BA.debugLine="If brgy_index == 6 And street_index == 0 Then 'Bu";
if (_brgy_index==6 && _street_index==0) { 
 //BA.debugLineNum = 1480;BA.debugLine="lat = \"10.035728\"";
_lat = "10.035728";
 //BA.debugLineNum = 1481;BA.debugLine="lng = \"122.847547\"";
_lng = "122.847547";
 }else if(_brgy_index==6 && _street_index==1) { 
 //BA.debugLineNum = 1483;BA.debugLine="lat = \"10.000603\"";
_lat = "10.000603";
 //BA.debugLineNum = 1484;BA.debugLine="lng = \"122.885243\"";
_lng = "122.885243";
 }else if(_brgy_index==6 && _street_index==2) { 
 //BA.debugLineNum = 1486;BA.debugLine="lat = \"10.000521\"";
_lat = "10.000521";
 //BA.debugLineNum = 1487;BA.debugLine="lng = \"122.895867\"";
_lng = "122.895867";
 }else if(_brgy_index==6 && _street_index==3) { 
 //BA.debugLineNum = 1489;BA.debugLine="lat = \"9.943276\"";
_lat = "9.943276";
 //BA.debugLineNum = 1490;BA.debugLine="lng = \"122.975801\"";
_lng = "122.975801";
 };
 //BA.debugLineNum = 1493;BA.debugLine="If brgy_index == 7 And street_index == 0 Then '";
if (_brgy_index==7 && _street_index==0) { 
 //BA.debugLineNum = 1494;BA.debugLine="lat = \"10.156301\"";
_lat = "10.156301";
 //BA.debugLineNum = 1495;BA.debugLine="lng = \"122.941207\"";
_lng = "122.941207";
 }else if(_brgy_index==7 && _street_index==1) { 
 //BA.debugLineNum = 1497;BA.debugLine="lat = \"10.142692\"";
_lat = "10.142692";
 //BA.debugLineNum = 1498;BA.debugLine="lng = \"122.947560\"";
_lng = "122.947560";
 }else if(_brgy_index==7 && _street_index==2) { 
 //BA.debugLineNum = 1500;BA.debugLine="lat = \"10.139494\"";
_lat = "10.139494";
 //BA.debugLineNum = 1501;BA.debugLine="lng = \"122.942788\"";
_lng = "122.942788";
 }else if(_brgy_index==7 && _street_index==3) { 
 //BA.debugLineNum = 1503;BA.debugLine="lat = \"10.110265\"";
_lat = "10.110265";
 //BA.debugLineNum = 1504;BA.debugLine="lng = \"122.947908\"";
_lng = "122.947908";
 }else if(_brgy_index==7 && _street_index==4) { 
 //BA.debugLineNum = 1506;BA.debugLine="lat = \"10.127828\"";
_lat = "10.127828";
 //BA.debugLineNum = 1507;BA.debugLine="lng = \"122.950197\"";
_lng = "122.950197";
 }else if(_brgy_index==7 && _street_index==5) { 
 //BA.debugLineNum = 1509;BA.debugLine="lat = \"10.125287\"";
_lat = "10.125287";
 //BA.debugLineNum = 1510;BA.debugLine="lng = \"122.945735\"";
_lng = "122.945735";
 }else if(_brgy_index==7 && _street_index==6) { 
 //BA.debugLineNum = 1512;BA.debugLine="lat = \"10.143975\"";
_lat = "10.143975";
 //BA.debugLineNum = 1513;BA.debugLine="lng = \"122.930610\"";
_lng = "122.930610";
 }else if(_brgy_index==7 && _street_index==7) { 
 //BA.debugLineNum = 1515;BA.debugLine="lat = \"10.137563\"";
_lat = "10.137563";
 //BA.debugLineNum = 1516;BA.debugLine="lng = \"122.939870\"";
_lng = "122.939870";
 }else if(_brgy_index==7 && _street_index==8) { 
 //BA.debugLineNum = 1518;BA.debugLine="lat = \"10.150449\"";
_lat = "10.150449";
 //BA.debugLineNum = 1519;BA.debugLine="lng = \"122.933761\"";
_lng = "122.933761";
 }else if(_brgy_index==7 && _street_index==9) { 
 //BA.debugLineNum = 1521;BA.debugLine="lat = \"10.150286\"";
_lat = "10.150286";
 //BA.debugLineNum = 1522;BA.debugLine="lng = \"122.948956\"";
_lng = "122.948956";
 }else if(_brgy_index==7 && _street_index==10) { 
 //BA.debugLineNum = 1524;BA.debugLine="lat = \"10.148481\"";
_lat = "10.148481";
 //BA.debugLineNum = 1525;BA.debugLine="lng = \"122.943230\"";
_lng = "122.943230";
 }else if(_brgy_index==7 && _street_index==11) { 
 //BA.debugLineNum = 1527;BA.debugLine="lat = \"10.106200\"";
_lat = "10.106200";
 //BA.debugLineNum = 1528;BA.debugLine="lng = \"122.948051\"";
_lng = "122.948051";
 }else if(_brgy_index==7 && _street_index==12) { 
 //BA.debugLineNum = 1530;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 1531;BA.debugLine="lng = \"122.926593\"";
_lng = "122.926593";
 }else if(_brgy_index==7 && _street_index==13) { 
 //BA.debugLineNum = 1533;BA.debugLine="lat = \"10.120798\"";
_lat = "10.120798";
 //BA.debugLineNum = 1534;BA.debugLine="lng = \"122.938371\"";
_lng = "122.938371";
 }else if(_brgy_index==7 && _street_index==14) { 
 //BA.debugLineNum = 1536;BA.debugLine="lat = \"10.153217\"";
_lat = "10.153217";
 //BA.debugLineNum = 1537;BA.debugLine="lng = \"122.951714\"";
_lng = "122.951714";
 };
 //BA.debugLineNum = 1540;BA.debugLine="If brgy_index == 8 And street_index == 0 Then";
if (_brgy_index==8 && _street_index==0) { 
 //BA.debugLineNum = 1541;BA.debugLine="lat = \"10.157177\"";
_lat = "10.157177";
 //BA.debugLineNum = 1542;BA.debugLine="lng = \"122.895986\"";
_lng = "122.895986";
 }else if(_brgy_index==8 && _street_index==1) { 
 //BA.debugLineNum = 1544;BA.debugLine="lat = \"10.180004\"";
_lat = "10.180004";
 //BA.debugLineNum = 1545;BA.debugLine="lng = \"122.897999\"";
_lng = "122.897999";
 }else if(_brgy_index==8 && _street_index==2) { 
 //BA.debugLineNum = 1547;BA.debugLine="lat = \"10.192848\"";
_lat = "10.192848";
 //BA.debugLineNum = 1548;BA.debugLine="lng = \"122.900234\"";
_lng = "122.900234";
 }else if(_brgy_index==8 && _street_index==3) { 
 //BA.debugLineNum = 1550;BA.debugLine="lat = \"10.179993\"";
_lat = "10.179993";
 //BA.debugLineNum = 1551;BA.debugLine="lng = \"122.904299\"";
_lng = "122.904299";
 }else if(_brgy_index==8 && _street_index==4) { 
 //BA.debugLineNum = 1553;BA.debugLine="lat = \"10.183439\"";
_lat = "10.183439";
 //BA.debugLineNum = 1554;BA.debugLine="lng = \"122.889622\"";
_lng = "122.889622";
 };
 //BA.debugLineNum = 1557;BA.debugLine="If brgy_index == 9 And street_index == 0 Then 'Ca";
if (_brgy_index==9 && _street_index==0) { 
 //BA.debugLineNum = 1558;BA.debugLine="lat = \"10.074128\"";
_lat = "10.074128";
 //BA.debugLineNum = 1559;BA.debugLine="lng = \"122.981978\"";
_lng = "122.981978";
 }else if(_brgy_index==9 && _street_index==1) { 
 //BA.debugLineNum = 1561;BA.debugLine="lat = \"10.109208\"";
_lat = "10.109208";
 //BA.debugLineNum = 1562;BA.debugLine="lng = \"122.896717\"";
_lng = "122.896717";
 }else if(_brgy_index==9 && _street_index==2) { 
 //BA.debugLineNum = 1564;BA.debugLine="lat = \"10.097119\"";
_lat = "10.097119";
 //BA.debugLineNum = 1565;BA.debugLine="lng = \"122.947066\"";
_lng = "122.947066";
 }else if(_brgy_index==9 && _street_index==3) { 
 //BA.debugLineNum = 1567;BA.debugLine="lat = \"10.099023\"";
_lat = "10.099023";
 //BA.debugLineNum = 1568;BA.debugLine="lng = \"122.971723\"";
_lng = "122.971723";
 }else if(_brgy_index==9 && _street_index==4) { 
 //BA.debugLineNum = 1570;BA.debugLine="lat = \"10.119761\"";
_lat = "10.119761";
 //BA.debugLineNum = 1571;BA.debugLine="lng = \"122.901613\"";
_lng = "122.901613";
 }else if(_brgy_index==9 && _street_index==5) { 
 //BA.debugLineNum = 1573;BA.debugLine="lat = \"10.099402\"";
_lat = "10.099402";
 //BA.debugLineNum = 1574;BA.debugLine="lng = \"122.896454\"";
_lng = "122.896454";
 }else if(_brgy_index==9 && _street_index==6) { 
 //BA.debugLineNum = 1576;BA.debugLine="lat = \"10.097102\"";
_lat = "10.097102";
 //BA.debugLineNum = 1577;BA.debugLine="lng = \"122.922368\"";
_lng = "122.922368";
 }else if(_brgy_index==9 && _street_index==7) { 
 //BA.debugLineNum = 1579;BA.debugLine="lat = \"10.095304\"";
_lat = "10.095304";
 //BA.debugLineNum = 1580;BA.debugLine="lng = \"122.929242\"";
_lng = "122.929242";
 }else if(_brgy_index==9 && _street_index==8) { 
 //BA.debugLineNum = 1582;BA.debugLine="lat = \"10.114128\"";
_lat = "10.114128";
 //BA.debugLineNum = 1583;BA.debugLine="lng = \"122.893868\"";
_lng = "122.893868";
 };
 //BA.debugLineNum = 1586;BA.debugLine="If brgy_index == 10 And street_index == 0 Then 'L";
if (_brgy_index==10 && _street_index==0) { 
 //BA.debugLineNum = 1587;BA.debugLine="lat = \"10.1799469\"";
_lat = "10.1799469";
 //BA.debugLineNum = 1588;BA.debugLine="lng = \"122.9068577\"";
_lng = "122.9068577";
 }else if(_brgy_index==10 && _street_index==1) { 
 //BA.debugLineNum = 1590;BA.debugLine="lat = \"10.180524\"";
_lat = "10.180524";
 //BA.debugLineNum = 1591;BA.debugLine="lng = \"122.906798\"";
_lng = "122.906798";
 }else if(_brgy_index==10 && _street_index==2) { 
 //BA.debugLineNum = 1593;BA.debugLine="lat = \"10.173336\"";
_lat = "10.173336";
 //BA.debugLineNum = 1594;BA.debugLine="lng = \"122.9118842\"";
_lng = "122.9118842";
 }else if(_brgy_index==10 && _street_index==3) { 
 //BA.debugLineNum = 1596;BA.debugLine="lat = \"10.177359\"";
_lat = "10.177359";
 //BA.debugLineNum = 1597;BA.debugLine="lng = \"122.913033\"";
_lng = "122.913033";
 }else if(_brgy_index==10 && _street_index==4) { 
 //BA.debugLineNum = 1599;BA.debugLine="lat = \"10.179847\"";
_lat = "10.179847";
 //BA.debugLineNum = 1600;BA.debugLine="lng = \"122.914160\"";
_lng = "122.914160";
 }else if(_brgy_index==10 && _street_index==5) { 
 //BA.debugLineNum = 1602;BA.debugLine="lat = \"10.182718\"";
_lat = "10.182718";
 //BA.debugLineNum = 1603;BA.debugLine="lng = \"122.915228\"";
_lng = "122.915228";
 }else if(_brgy_index==10 && _street_index==6) { 
 //BA.debugLineNum = 1605;BA.debugLine="lat = \"10.186454\"";
_lat = "10.186454";
 //BA.debugLineNum = 1606;BA.debugLine="lng = \"122.916278\"";
_lng = "122.916278";
 }else if(_brgy_index==10 && _street_index==7) { 
 //BA.debugLineNum = 1608;BA.debugLine="lat = \"10.168057\"";
_lat = "10.168057";
 //BA.debugLineNum = 1609;BA.debugLine="lng = \"122.924501\"";
_lng = "122.924501";
 };
 //BA.debugLineNum = 1612;BA.debugLine="If brgy_index == 11 And street_index == 0 Then 'M";
if (_brgy_index==11 && _street_index==0) { 
 //BA.debugLineNum = 1613;BA.debugLine="lat = \"10.050418\"";
_lat = "10.050418";
 //BA.debugLineNum = 1614;BA.debugLine="lng = \"122.867097\"";
_lng = "122.867097";
 }else if(_brgy_index==11 && _street_index==1) { 
 //BA.debugLineNum = 1616;BA.debugLine="lat = \"10.027855\"";
_lat = "10.027855";
 //BA.debugLineNum = 1617;BA.debugLine="lng = \"122.906833\"";
_lng = "122.906833";
 }else if(_brgy_index==11 && _street_index==2) { 
 //BA.debugLineNum = 1619;BA.debugLine="lat = \"10.027522\"";
_lat = "10.027522";
 //BA.debugLineNum = 1620;BA.debugLine="lng = \"122.876637\"";
_lng = "122.876637";
 }else if(_brgy_index==11 && _street_index==3) { 
 //BA.debugLineNum = 1622;BA.debugLine="lat = \"10.017254\"";
_lat = "10.017254";
 //BA.debugLineNum = 1623;BA.debugLine="lng = \"122.900969\"";
_lng = "122.900969";
 }else if(_brgy_index==11 && _street_index==4) { 
 //BA.debugLineNum = 1625;BA.debugLine="lat = \"10.028535\"";
_lat = "10.028535";
 //BA.debugLineNum = 1626;BA.debugLine="lng = \"122.900364\"";
_lng = "122.900364";
 }else if(_brgy_index==11 && _street_index==5) { 
 //BA.debugLineNum = 1628;BA.debugLine="lat = \"10.025485\"";
_lat = "10.025485";
 //BA.debugLineNum = 1629;BA.debugLine="lng = \"122.890023\"";
_lng = "122.890023";
 };
 //BA.debugLineNum = 1632;BA.debugLine="If brgy_index == 12 And street_index == 0 Then 'M";
if (_brgy_index==12 && _street_index==0) { 
 //BA.debugLineNum = 1633;BA.debugLine="lat = \"10.137572\"";
_lat = "10.137572";
 //BA.debugLineNum = 1634;BA.debugLine="lng = \"122.939888\"";
_lng = "122.939888";
 }else if(_brgy_index==12 && _street_index==1) { 
 //BA.debugLineNum = 1636;BA.debugLine="lat = \"10.132195\"";
_lat = "10.132195";
 //BA.debugLineNum = 1637;BA.debugLine="lng = \"122.899837\"";
_lng = "122.899837";
 }else if(_brgy_index==12 && _street_index==2) { 
 //BA.debugLineNum = 1639;BA.debugLine="lat = \"10.123430\"";
_lat = "10.123430";
 //BA.debugLineNum = 1640;BA.debugLine="lng = \"122.892250\"";
_lng = "122.892250";
 }else if(_brgy_index==12 && _street_index==3) { 
 //BA.debugLineNum = 1642;BA.debugLine="lat = \"10.130383\"";
_lat = "10.130383";
 //BA.debugLineNum = 1643;BA.debugLine="lng = \"122.893010\"";
_lng = "122.893010";
 }else if(_brgy_index==12 && _street_index==4) { 
 //BA.debugLineNum = 1645;BA.debugLine="lat = \"10.123127\"";
_lat = "10.123127";
 //BA.debugLineNum = 1646;BA.debugLine="lng = \"122.887952\"";
_lng = "122.887952";
 }else if(_brgy_index==12 && _street_index==5) { 
 //BA.debugLineNum = 1648;BA.debugLine="lat = \"10.131098\"";
_lat = "10.131098";
 //BA.debugLineNum = 1649;BA.debugLine="lng = \"122.879801\"";
_lng = "122.879801";
 }else if(_brgy_index==12 && _street_index==6) { 
 //BA.debugLineNum = 1651;BA.debugLine="lat = \"10.137485\"";
_lat = "10.137485";
 //BA.debugLineNum = 1652;BA.debugLine="lng = \"122.911434\"";
_lng = "122.911434";
 }else if(_brgy_index==12 && _street_index==7) { 
 //BA.debugLineNum = 1654;BA.debugLine="lat = \"10.106803\"";
_lat = "10.106803";
 //BA.debugLineNum = 1655;BA.debugLine="lng = \"122.885727\"";
_lng = "122.885727";
 }else if(_brgy_index==12 && _street_index==8) { 
 //BA.debugLineNum = 1657;BA.debugLine="lat = \"10.115220\"";
_lat = "10.115220";
 //BA.debugLineNum = 1658;BA.debugLine="lng = \"122.890515\"";
_lng = "122.890515";
 }else if(_brgy_index==12 && _street_index==9) { 
 //BA.debugLineNum = 1660;BA.debugLine="lat = \"10.108754\"";
_lat = "10.108754";
 //BA.debugLineNum = 1661;BA.debugLine="lng = \"122.894130\"";
_lng = "122.894130";
 }else if(_brgy_index==12 && _street_index==10) { 
 //BA.debugLineNum = 1663;BA.debugLine="lat = \"10.149506\"";
_lat = "10.149506";
 //BA.debugLineNum = 1664;BA.debugLine="lng = \"122.897389\"";
_lng = "122.897389";
 }else if(_brgy_index==12 && _street_index==11) { 
 //BA.debugLineNum = 1666;BA.debugLine="lat = \"10.122215\"";
_lat = "10.122215";
 //BA.debugLineNum = 1667;BA.debugLine="lng = \"122.892160\"";
_lng = "122.892160";
 }else if(_brgy_index==12 && _street_index==12) { 
 //BA.debugLineNum = 1669;BA.debugLine="lat = \"10.142698\"";
_lat = "10.142698";
 //BA.debugLineNum = 1670;BA.debugLine="lng = \"122.898168\"";
_lng = "122.898168";
 };
 //BA.debugLineNum = 1673;BA.debugLine="If brgy_index == 13 And street_index == 0 Then 'N";
if (_brgy_index==13 && _street_index==0) { 
 //BA.debugLineNum = 1674;BA.debugLine="lat = \"10.161629\"";
_lat = "10.161629";
 //BA.debugLineNum = 1675;BA.debugLine="lng = \"122.872772\"";
_lng = "122.872772";
 }else if(_brgy_index==13 && _street_index==1) { 
 //BA.debugLineNum = 1677;BA.debugLine="lat = \"10.161863\"";
_lat = "10.161863";
 //BA.debugLineNum = 1678;BA.debugLine="lng = \"122.876192\"";
_lng = "122.876192";
 }else if(_brgy_index==13 && _street_index==2) { 
 //BA.debugLineNum = 1680;BA.debugLine="lat = \"10.157407\"";
_lat = "10.157407";
 //BA.debugLineNum = 1681;BA.debugLine="lng = \"122.885663\"";
_lng = "122.885663";
 }else if(_brgy_index==13 && _street_index==3) { 
 //BA.debugLineNum = 1683;BA.debugLine="lat = \"10.167497\"";
_lat = "10.167497";
 //BA.debugLineNum = 1684;BA.debugLine="lng = \"122.879777\"";
_lng = "122.879777";
 }else if(_brgy_index==13 && _street_index==4) { 
 //BA.debugLineNum = 1686;BA.debugLine="lat = \"10.176260\"";
_lat = "10.176260";
 //BA.debugLineNum = 1687;BA.debugLine="lng = \"122.880815\"";
_lng = "122.880815";
 }else if(_brgy_index==13 && _street_index==5) { 
 //BA.debugLineNum = 1689;BA.debugLine="lat = \"10.170524\"";
_lat = "10.170524";
 //BA.debugLineNum = 1690;BA.debugLine="lng = \"122.883603\"";
_lng = "122.883603";
 };
 //BA.debugLineNum = 1693;BA.debugLine="If brgy_index == 14 And street_index == 0 Then 'S";
if (_brgy_index==14 && _street_index==0) { 
 //BA.debugLineNum = 1694;BA.debugLine="lat = \"10.071514\"";
_lat = "10.071514";
 //BA.debugLineNum = 1695;BA.debugLine="lng = \"122.916010\"";
_lng = "122.916010";
 }else if(_brgy_index==14 && _street_index==1) { 
 //BA.debugLineNum = 1697;BA.debugLine="lat = \"10.069622\"";
_lat = "10.069622";
 //BA.debugLineNum = 1698;BA.debugLine="lng = \"122.909890\"";
_lng = "122.909890";
 }else if(_brgy_index==14 && _street_index==2) { 
 //BA.debugLineNum = 1700;BA.debugLine="lat = \"10.076890\"";
_lat = "10.076890";
 //BA.debugLineNum = 1701;BA.debugLine="lng = \"122.894231\"";
_lng = "122.894231";
 }else if(_brgy_index==14 && _street_index==3) { 
 //BA.debugLineNum = 1703;BA.debugLine="lat = \"10.086207\"";
_lat = "10.086207";
 //BA.debugLineNum = 1704;BA.debugLine="lng = \"122.914044\"";
_lng = "122.914044";
 }else if(_brgy_index==14 && _street_index==4) { 
 //BA.debugLineNum = 1706;BA.debugLine="lat = \"10.067393\"";
_lat = "10.067393";
 //BA.debugLineNum = 1707;BA.debugLine="lng = \"122.900935\"";
_lng = "122.900935";
 }else if(_brgy_index==14 && _street_index==5) { 
 //BA.debugLineNum = 1709;BA.debugLine="lat = \"10.071900\"";
_lat = "10.071900";
 //BA.debugLineNum = 1710;BA.debugLine="lng = \"122.906250\"";
_lng = "122.906250";
 }else if(_brgy_index==14 && _street_index==6) { 
 //BA.debugLineNum = 1712;BA.debugLine="lat = \"10.061702\"";
_lat = "10.061702";
 //BA.debugLineNum = 1713;BA.debugLine="lng = \"122.896226\"";
_lng = "122.896226";
 }else if(_brgy_index==14 && _street_index==7) { 
 //BA.debugLineNum = 1715;BA.debugLine="lat = \"10.054802\"";
_lat = "10.054802";
 //BA.debugLineNum = 1716;BA.debugLine="lng = \"122.938688\"";
_lng = "122.938688";
 }else if(_brgy_index==14 && _street_index==8) { 
 //BA.debugLineNum = 1718;BA.debugLine="lat = \"10.071827\"";
_lat = "10.071827";
 //BA.debugLineNum = 1719;BA.debugLine="lng = \"122.921092\"";
_lng = "122.921092";
 }else if(_brgy_index==14 && _street_index==9) { 
 //BA.debugLineNum = 1721;BA.debugLine="lat = \"10.050849\"";
_lat = "10.050849";
 //BA.debugLineNum = 1722;BA.debugLine="lng = \"122.907632\"";
_lng = "122.907632";
 };
 //BA.debugLineNum = 1725;BA.debugLine="If brgy_index == 15 And street_index == 0 Then 'S";
if (_brgy_index==15 && _street_index==0) { 
 //BA.debugLineNum = 1726;BA.debugLine="lat = \"10.155844\"";
_lat = "10.155844";
 //BA.debugLineNum = 1727;BA.debugLine="lng = \"122.861129\"";
_lng = "122.861129";
 }else if(_brgy_index==15 && _street_index==1) { 
 //BA.debugLineNum = 1729;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 1730;BA.debugLine="lng = \"122.861669\"";
_lng = "122.861669";
 }else if(_brgy_index==15 && _street_index==2) { 
 //BA.debugLineNum = 1732;BA.debugLine="lat = \"10.147663\"";
_lat = "10.147663";
 //BA.debugLineNum = 1733;BA.debugLine="lng = \"122.862471\"";
_lng = "122.862471";
 }else if(_brgy_index==15 && _street_index==3) { 
 //BA.debugLineNum = 1735;BA.debugLine="lat = \"10.144440\"";
_lat = "10.144440";
 //BA.debugLineNum = 1736;BA.debugLine="lng = \"122.862524\"";
_lng = "122.862524";
 };
 //BA.debugLineNum = 1739;BA.debugLine="If brgy_index == 16 And street_index == 0 Then 'S";
if (_brgy_index==16 && _street_index==0) { 
 //BA.debugLineNum = 1740;BA.debugLine="lat = \"10.053680\"";
_lat = "10.053680";
 //BA.debugLineNum = 1741;BA.debugLine="lng = \"122.843876\"";
_lng = "122.843876";
 }else if(_brgy_index==16 && _street_index==1) { 
 //BA.debugLineNum = 1743;BA.debugLine="lat = \"10.055961\"";
_lat = "10.055961";
 //BA.debugLineNum = 1744;BA.debugLine="lng = \"122.841980\"";
_lng = "122.841980";
 }else if(_brgy_index==16 && _street_index==2) { 
 //BA.debugLineNum = 1746;BA.debugLine="lat = \"10.053363\"";
_lat = "10.053363";
 //BA.debugLineNum = 1747;BA.debugLine="lng = \"122.843295\"";
_lng = "122.843295";
 }else if(_brgy_index==16 && _street_index==3) { 
 //BA.debugLineNum = 1749;BA.debugLine="lat = \"10.053032\"";
_lat = "10.053032";
 //BA.debugLineNum = 1750;BA.debugLine="lng = \"122.842594\"";
_lng = "122.842594";
 }else if(_brgy_index==16 && _street_index==4) { 
 //BA.debugLineNum = 1752;BA.debugLine="lat = \"10.052328\"";
_lat = "10.052328";
 //BA.debugLineNum = 1753;BA.debugLine="lng = \"122.842835\"";
_lng = "122.842835";
 }else if(_brgy_index==16 && _street_index==5) { 
 //BA.debugLineNum = 1755;BA.debugLine="lat = \"10.052573\"";
_lat = "10.052573";
 //BA.debugLineNum = 1756;BA.debugLine="lng = \"122.844229\"";
_lng = "122.844229";
 }else if(_brgy_index==16 && _street_index==6) { 
 //BA.debugLineNum = 1758;BA.debugLine="lat = \"10.046957\"";
_lat = "10.046957";
 //BA.debugLineNum = 1759;BA.debugLine="lng = \"122.839610\"";
_lng = "122.839610";
 }else if(_brgy_index==16 && _street_index==7) { 
 //BA.debugLineNum = 1761;BA.debugLine="lat = \"10.035813\"";
_lat = "10.035813";
 //BA.debugLineNum = 1762;BA.debugLine="lng = \"122.835364\"";
_lng = "122.835364";
 };
 //BA.debugLineNum = 1765;BA.debugLine="If brgy_index == 17 And street_index == 0 Then 'T";
if (_brgy_index==17 && _street_index==0) { 
 //BA.debugLineNum = 1766;BA.debugLine="lat = \"10.148233\"";
_lat = "10.148233";
 //BA.debugLineNum = 1767;BA.debugLine="lng = \"122.869741\"";
_lng = "122.869741";
 }else if(_brgy_index==17 && _street_index==1) { 
 //BA.debugLineNum = 1769;BA.debugLine="lat = \"10.139867\"";
_lat = "10.139867";
 //BA.debugLineNum = 1770;BA.debugLine="lng = \"122.869882\"";
_lng = "122.869882";
 }else if(_brgy_index==17 && _street_index==2) { 
 //BA.debugLineNum = 1772;BA.debugLine="lat = \"10.126453\"";
_lat = "10.126453";
 //BA.debugLineNum = 1773;BA.debugLine="lng = \"122.868927\"";
_lng = "122.868927";
 }else if(_brgy_index==17 && _street_index==3) { 
 //BA.debugLineNum = 1775;BA.debugLine="lat = \"10.127470\"";
_lat = "10.127470";
 //BA.debugLineNum = 1776;BA.debugLine="lng = \"122.862942\"";
_lng = "122.862942";
 }else if(_brgy_index==17 && _street_index==4) { 
 //BA.debugLineNum = 1778;BA.debugLine="lat = \"10.117998\"";
_lat = "10.117998";
 //BA.debugLineNum = 1779;BA.debugLine="lng = \"122.866817\"";
_lng = "122.866817";
 }else if(_brgy_index==17 && _street_index==5) { 
 //BA.debugLineNum = 1781;BA.debugLine="lat = \"10.108173\"";
_lat = "10.108173";
 //BA.debugLineNum = 1782;BA.debugLine="lng = \"122.864592\"";
_lng = "122.864592";
 }else if(_brgy_index==17 && _street_index==6) { 
 //BA.debugLineNum = 1784;BA.debugLine="lat = \"10.126115\"";
_lat = "10.126115";
 //BA.debugLineNum = 1785;BA.debugLine="lng = \"122.871073\"";
_lng = "122.871073";
 }else if(_brgy_index==17 && _street_index==7) { 
 //BA.debugLineNum = 1787;BA.debugLine="lat = \"10.129412\"";
_lat = "10.129412";
 //BA.debugLineNum = 1788;BA.debugLine="lng = \"122.869408\"";
_lng = "122.869408";
 }else if(_brgy_index==17 && _street_index==8) { 
 //BA.debugLineNum = 1790;BA.debugLine="lat = \"10.134647\"";
_lat = "10.134647";
 //BA.debugLineNum = 1791;BA.debugLine="lng = \"122.871841\"";
_lng = "122.871841";
 }else if(_brgy_index==17 && _street_index==9) { 
 //BA.debugLineNum = 1793;BA.debugLine="lat = \"10.124801\"";
_lat = "10.124801";
 //BA.debugLineNum = 1794;BA.debugLine="lng = \"122.868277\"";
_lng = "122.868277";
 }else if(_brgy_index==17 && _street_index==10) { 
 //BA.debugLineNum = 1796;BA.debugLine="lat = \"10.124422\"";
_lat = "10.124422";
 //BA.debugLineNum = 1797;BA.debugLine="lng = \"122.866917\"";
_lng = "122.866917";
 };
 //BA.debugLineNum = 1800;BA.debugLine="If brgy_index == 18 And street_index == 0 Then 'T";
if (_brgy_index==18 && _street_index==0) { 
 //BA.debugLineNum = 1801;BA.debugLine="lat = \"10.065086\"";
_lat = "10.065086";
 //BA.debugLineNum = 1802;BA.debugLine="lng = \"122.843793\"";
_lng = "122.843793";
 }else if(_brgy_index==18 && _street_index==1) { 
 //BA.debugLineNum = 1804;BA.debugLine="lat = \"10.071356\"";
_lat = "10.071356";
 //BA.debugLineNum = 1805;BA.debugLine="lng = \"122.853102\"";
_lng = "122.853102";
 }else if(_brgy_index==18 && _street_index==2) { 
 //BA.debugLineNum = 1807;BA.debugLine="lat = \"10.060206\"";
_lat = "10.060206";
 //BA.debugLineNum = 1808;BA.debugLine="lng = \"122.850172\"";
_lng = "122.850172";
 }else if(_brgy_index==18 && _street_index==3) { 
 //BA.debugLineNum = 1810;BA.debugLine="lat = \"10.057640\"";
_lat = "10.057640";
 //BA.debugLineNum = 1811;BA.debugLine="lng = \"122.859242\"";
_lng = "122.859242";
 };
 //BA.debugLineNum = 1814;BA.debugLine="Log(\"lat: \"&lat&CRLF&\"lng: \"&lng)";
anywheresoftware.b4a.keywords.Common.Log("lat: "+_lat+anywheresoftware.b4a.keywords.Common.CRLF+"lng: "+_lng);
 //BA.debugLineNum = 1815;BA.debugLine="End Sub";
return "";
}
public static String  _update_all_inputs_click() throws Exception{
 //BA.debugLineNum = 424;BA.debugLine="Sub update_all_inputs_click";
 //BA.debugLineNum = 426;BA.debugLine="End Sub";
return "";
}
public static String  _update_btn_click() throws Exception{
bloodlife.system.calculations _url_back = null;
String _male_c = "";
String _female_c = "";
String _img_string = "";
anywheresoftware.b4a.objects.StringUtils _su = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out1 = null;
int _nmonth = 0;
int _nday = 0;
int _nyear = 0;
int _ageget = 0;
int _age = 0;
int _pyear = 0;
int _pmonth = 0;
int _pday = 0;
String _ins = "";
String _m_1 = "";
String _m_2 = "";
String _m_3 = "";
String _merge = "";
 //BA.debugLineNum = 718;BA.debugLine="Sub update_btn_Click";
 //BA.debugLineNum = 719;BA.debugLine="ProgressDialogShow2(\"Updating Please wait...\",Fal";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Updating Please wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 720;BA.debugLine="optionSelected = \"updated_click\"";
_optionselected = "updated_click";
 //BA.debugLineNum = 721;BA.debugLine="update_job.Initialize(\"update_job\",Me)";
mostCurrent._update_job._initialize(processBA,"update_job",menu_form.getObject());
 //BA.debugLineNum = 722;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 723;BA.debugLine="Dim male_c,female_c As String";
_male_c = "";
_female_c = "";
 //BA.debugLineNum = 724;BA.debugLine="male_c = File.GetText(File.DirAssets, \"male_strin";
_male_c = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"male_string.txt");
 //BA.debugLineNum = 725;BA.debugLine="female_c = File.GetText(File.DirAssets, \"female_s";
_female_c = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"female_string.txt");
 //BA.debugLineNum = 727;BA.debugLine="If image_container == male_c Or image_container =";
if ((_image_container).equals(_male_c) || (_image_container).equals(_female_c)) { 
 //BA.debugLineNum = 728;BA.debugLine="Dim img_string As String";
_img_string = "";
 //BA.debugLineNum = 729;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 730;BA.debugLine="Dim out1 As OutputStream";
_out1 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 731;BA.debugLine="If is_gender_index == 0 Then";
if (_is_gender_index==0) { 
 //BA.debugLineNum = 732;BA.debugLine="out1.InitializeToBytesArray(0) 'size not real";
_out1.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 733;BA.debugLine="File.Copy2(File.OpenInput(File.DirAssets, \"ma";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"male_clip.png").getObject()),(java.io.OutputStream)(_out1.getObject()));
 //BA.debugLineNum = 734;BA.debugLine="img_string=su.EncodeBase64(out1.ToBytesArray)";
_img_string = _su.EncodeBase64(_out1.ToBytesArray());
 //BA.debugLineNum = 736;BA.debugLine="image_container = img_string";
_image_container = _img_string;
 }else {
 //BA.debugLineNum = 738;BA.debugLine="out1.InitializeToBytesArray(0) 'size not reall";
_out1.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 739;BA.debugLine="File.Copy2(File.OpenInput(File.DirAssets, \"fe";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"female_clip.png").getObject()),(java.io.OutputStream)(_out1.getObject()));
 //BA.debugLineNum = 740;BA.debugLine="img_string=su.EncodeBase64(out1.ToBytesArray)";
_img_string = _su.EncodeBase64(_out1.ToBytesArray());
 //BA.debugLineNum = 742;BA.debugLine="image_container = img_string";
_image_container = _img_string;
 };
 }else {
 };
 //BA.debugLineNum = 749;BA.debugLine="Dim Nmonth,Nday,Nyear,ageGet As Int";
_nmonth = 0;
_nday = 0;
_nyear = 0;
_ageget = 0;
 //BA.debugLineNum = 750;BA.debugLine="Nday = DateTime.GetDayOfMonth(DateTime.Now)";
_nday = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 751;BA.debugLine="Nmonth = DateTime.GetMonth(DateTime.Now)";
_nmonth = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 752;BA.debugLine="Nyear = DateTime.GetYear(DateTime.Now)";
_nyear = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 754;BA.debugLine="Dim age,Pyear,Pmonth,Pday As Int";
_age = 0;
_pyear = 0;
_pmonth = 0;
_pday = 0;
 //BA.debugLineNum = 755;BA.debugLine="Pyear = bday_year_selected";
_pyear = (int)(Double.parseDouble(_bday_year_selected));
 //BA.debugLineNum = 756;BA.debugLine="Pmonth = bday_month_selected";
_pmonth = (int)(Double.parseDouble(_bday_month_selected));
 //BA.debugLineNum = 757;BA.debugLine="Pday = bday_day_selected";
_pday = (int)(Double.parseDouble(_bday_day_selected));
 //BA.debugLineNum = 758;BA.debugLine="age = Nyear - Pyear";
_age = (int) (_nyear-_pyear);
 //BA.debugLineNum = 759;BA.debugLine="If Pmonth <= Nmonth And Pday <= Nday Then";
if (_pmonth<=_nmonth && _pday<=_nday) { 
 //BA.debugLineNum = 761;BA.debugLine="ageGet = age";
_ageget = _age;
 }else {
 //BA.debugLineNum = 763;BA.debugLine="ageGet = age-1";
_ageget = (int) (_age-1);
 };
 //BA.debugLineNum = 767;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 769;BA.debugLine="Dim ins,m_1,m_2,m_3,merge As String";
_ins = "";
_m_1 = "";
_m_2 = "";
_m_3 = "";
_merge = "";
 //BA.debugLineNum = 771;BA.debugLine="If text_fn.Text == \"\"  Or text_email.Text == \"";
if ((mostCurrent._text_fn.getText()).equals("") || (mostCurrent._text_email.getText()).equals("") || (mostCurrent._text_phonenumber2.getText()).equals("") || (mostCurrent._text_phonenumber.getText()).equals("") || (mostCurrent._text_answer.getText()).equals("")) { 
 //BA.debugLineNum = 772;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 773;BA.debugLine="Msgbox(\"Error: Fill up those empty fields before";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Fill up those empty fields before you update!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 775;BA.debugLine="m_1 = \"UPDATE `person_info` SET `full_name`='\"&t";
_m_1 = "UPDATE `person_info` SET `full_name`='"+mostCurrent._text_fn.getText()+"',`blood_type`='"+_blood_selected+"', `phone_number1`='"+mostCurrent._text_phonenumber.getText()+"', `phone_number2`='"+mostCurrent._text_phonenumber2.getText()+"', `location_brgy`='"+_location_brgy_selected+"', `location_street`='"+_location_street_selected+"', ";
 //BA.debugLineNum = 776;BA.debugLine="m_2 = \"`location_purok`='', `bday_month`='\"&bday";
_m_2 = "`location_purok`='', `bday_month`='"+_bday_month_selected+"',`bday_day`='"+_bday_day_selected+"', `bday_year`='"+_bday_year_selected+"', `nick_name`='"+mostCurrent._text_answer.getText()+"', `donate_boolean`='"+_is_donated+"', `lat`='"+_lat+"', `long`='"+_lng+"', `image`='"+_image_container+"', ";
 //BA.debugLineNum = 777;BA.debugLine="m_3 = \"`age`='\"&ageGet&\"',`date_donated`='\"&isDo";
_m_3 = "`age`='"+BA.NumberToString(_ageget)+"',`date_donated`='"+_isdonatedate+"',`gender`='"+_gender_string_data+"' WHERE `id`="+mostCurrent._login_form._id_query+";";
 //BA.debugLineNum = 778;BA.debugLine="merge = m_1&m_2&m_3";
_merge = _m_1+_m_2+_m_3;
 //BA.debugLineNum = 779;BA.debugLine="ins = url_back.php_email_url(\"updating.php\")";
_ins = _url_back._php_email_url("updating.php");
 //BA.debugLineNum = 781;BA.debugLine="update_job.PostString(ins,\"update=\"&merge)";
mostCurrent._update_job._poststring(_ins,"update="+_merge);
 };
 //BA.debugLineNum = 786;BA.debugLine="End Sub";
return "";
}
public static String  _usr_img_click() throws Exception{
String _img_string = "";
anywheresoftware.b4a.objects.StringUtils _su = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out1 = null;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _inp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
byte[] _bytes = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 528;BA.debugLine="Sub usr_img_click";
 //BA.debugLineNum = 529;BA.debugLine="Try";
try { //BA.debugLineNum = 531;BA.debugLine="userImage.InitializeAlpha(\"\", 1, 0)";
mostCurrent._userimage.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 532;BA.debugLine="usr_img.Tag = userImage";
mostCurrent._usr_img.setTag((Object)(mostCurrent._userimage.getObject()));
 //BA.debugLineNum = 533;BA.debugLine="userImage.Duration = 300";
mostCurrent._userimage.setDuration((long) (300));
 //BA.debugLineNum = 534;BA.debugLine="userImage.RepeatCount = 1";
mostCurrent._userimage.setRepeatCount((int) (1));
 //BA.debugLineNum = 535;BA.debugLine="userImage.RepeatMode = userImage.REPEAT_REVERSE";
mostCurrent._userimage.setRepeatMode(mostCurrent._userimage.REPEAT_REVERSE);
 //BA.debugLineNum = 536;BA.debugLine="userImage.Start(usr_img)";
mostCurrent._userimage.Start((android.view.View)(mostCurrent._usr_img.getObject()));
 //BA.debugLineNum = 537;BA.debugLine="dlgFileExpl.Initialize(Activity, \"/mnt/sdcard\", \"";
mostCurrent._dlgfileexpl._initialize(mostCurrent.activityBA,mostCurrent._activity,"/mnt/sdcard",".bmp,.gif,.jpg,.png",anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.False,"OK");
 //BA.debugLineNum = 538;BA.debugLine="dlgFileExpl.FastScrollEnabled = True";
mostCurrent._dlgfileexpl._fastscrollenabled = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 539;BA.debugLine="dlgFileExpl.Explorer2(True)";
mostCurrent._dlgfileexpl._explorer2(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 540;BA.debugLine="If Not(dlgFileExpl.Selection.Canceled Or dlgFileE";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._dlgfileexpl._selection.Canceled || (mostCurrent._dlgfileexpl._selection.ChosenFile).equals(""))) { 
 //BA.debugLineNum = 542;BA.debugLine="Dim img_string As String";
_img_string = "";
 //BA.debugLineNum = 543;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 544;BA.debugLine="Dim out1 As OutputStream";
_out1 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 546;BA.debugLine="out1.InitializeToBytesArray(0) 'size not real";
_out1.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 547;BA.debugLine="File.Copy2(File.OpenInput(dlgFileExpl.Selecti";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(mostCurrent._dlgfileexpl._selection.ChosenPath,mostCurrent._dlgfileexpl._selection.ChosenFile).getObject()),(java.io.OutputStream)(_out1.getObject()));
 //BA.debugLineNum = 548;BA.debugLine="img_string=su.EncodeBase64(out1.ToBytesArray)";
_img_string = _su.EncodeBase64(_out1.ToBytesArray());
 //BA.debugLineNum = 549;BA.debugLine="Log(img_string)";
anywheresoftware.b4a.keywords.Common.Log(_img_string);
 //BA.debugLineNum = 552;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 553;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 554;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 555;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 556;BA.debugLine="bytes = su.DecodeBase64(img_string)";
_bytes = _su.DecodeBase64(_img_string);
 //BA.debugLineNum = 557;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 558;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 560;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 561;BA.debugLine="bd.Initialize(bmp)";
_bd.Initialize((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 563;BA.debugLine="usr_img.Background = bd";
mostCurrent._usr_img.setBackground((android.graphics.drawable.Drawable)(_bd.getObject()));
 };
 } 
       catch (Exception e31) {
			processBA.setLastException(e31); //BA.debugLineNum = 568;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage());
 };
 //BA.debugLineNum = 570;BA.debugLine="End Sub";
return "";
}
}
