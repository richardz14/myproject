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
public static anywheresoftware.b4a.objects.collections.List _bookmark_id_list = null;
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
public static anywheresoftware.b4a.sql.SQL _sqllite = null;
public static int _row_click = 0;
public static int _item = 0;
public static int _list_all_select = 0;
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
public bloodlife.system.httpjob _update_img_job = null;
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
public anywheresoftware.b4a.objects.ImageViewWrapper _bookmark_image = null;
public anywheresoftware.b4a.objects.PanelWrapper _dialog_panel = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrolllista = null;
public anywheresoftware.b4a.objects.PanelWrapper _dialog_all_panel = null;
public anywheresoftware.b4a.objects.PanelWrapper _view_info_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _view_data_info_person = null;
public flm.b4a.scrollview2d.ScrollView2DWrapper _scroll_view_info = null;
public anywheresoftware.b4a.objects.AnimationWrapper _ph1_a1 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _ph2_a2 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _useri_a3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _user_image = null;
public anywheresoftware.b4a.objects.PanelWrapper _ph1_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _ph2_pnl = null;
public anywheresoftware.b4a.objects.LabelWrapper _phone1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _phone2 = null;
public anywheresoftware.b4a.objects.collections.List _image_list = null;
public anywheresoftware.b4a.objects.PanelWrapper _user_img_panl = null;
public bloodlife.system.main _main = null;
public bloodlife.system.login_form _login_form = null;
public bloodlife.system.create_account _create_account = null;
public bloodlife.system.search_frame _search_frame = null;
public bloodlife.system.httputils2service _httputils2service = null;
public bloodlife.system.help_frame _help_frame = null;
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
String _ss_string = "";
String _info_1 = "";
String _info_2 = "";
String _info_3 = "";
int _string_h = 0;
 //BA.debugLineNum = 2344;BA.debugLine="Sub about_Click";
 //BA.debugLineNum = 2345;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 2346;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 2347;BA.debugLine="about_img.Tag = aa1";
mostCurrent._about_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 2348;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 2349;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 2350;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 2351;BA.debugLine="a3.Start(about)";
mostCurrent._a3.Start((android.view.View)(mostCurrent._about.getObject()));
 //BA.debugLineNum = 2352;BA.debugLine="aa1.Start(about_img)";
_aa1.Start((android.view.View)(mostCurrent._about_img.getObject()));
 //BA.debugLineNum = 2353;BA.debugLine="panel_click_ = 2";
_panel_click_ = (int) (2);
 //BA.debugLineNum = 2354;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2355;BA.debugLine="Dim about_ok_btn As Button";
_about_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 2356;BA.debugLine="Dim title_lbl,about_data,for_h As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_about_data = new anywheresoftware.b4a.objects.LabelWrapper();
_for_h = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 2358;BA.debugLine="for_h.Initialize(\"\")";
_for_h.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2359;BA.debugLine="Dim sus As StringUtils";
_sus = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 2364;BA.debugLine="about_us_pnl.Initialize(\"about_us_pnl\")";
mostCurrent._about_us_pnl.Initialize(mostCurrent.activityBA,"about_us_pnl");
 //BA.debugLineNum = 2365;BA.debugLine="about_ok_btn.Initialize(\"about_ok_btn\")";
_about_ok_btn.Initialize(mostCurrent.activityBA,"about_ok_btn");
 //BA.debugLineNum = 2366;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2367;BA.debugLine="about_data.Initialize(\"\")";
_about_data.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2368;BA.debugLine="about_ok_btn.Text = \"OK\"";
_about_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 2369;BA.debugLine="about_ok_btn.Typeface = Typeface.LoadFromAssets(\"";
_about_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2370;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 2371;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 2372;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 2373;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 2374;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2375;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2376;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 2377;BA.debugLine="about_ok_btn.Background = V_btn";
_about_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 2378;BA.debugLine="title_lbl.Text = \"ABOUT\"";
_title_lbl.setText((Object)("ABOUT"));
 //BA.debugLineNum = 2379;BA.debugLine="title_lbl.TextSize = 25";
_title_lbl.setTextSize((float) (25));
 //BA.debugLineNum = 2380;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hip";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2382;BA.debugLine="title_lbl.SetBackgroundImage(LoadBitmap(File.DirA";
_title_lbl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bgs.jpg").getObject()));
 //BA.debugLineNum = 2383;BA.debugLine="title_lbl.TextColor = Colors.White";
_title_lbl.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 2385;BA.debugLine="Dim rs As RichString";
_rs = new anywheresoftware.b4a.agraham.richstring.RichStringBuilder.RichString();
 //BA.debugLineNum = 2386;BA.debugLine="Dim f_string,s_string,ss_string,info_1,info_2,inf";
_f_string = "";
_s_string = "";
_ss_string = "";
_info_1 = "";
_info_2 = "";
_info_3 = "";
 //BA.debugLineNum = 2387;BA.debugLine="f_string = CRLF&\"•	{ib}{bg1}Becgrajhon2013{bg1}{i";
_f_string = anywheresoftware.b4a.keywords.Common.CRLF+"•	{ib}{bg1}Becgrajhon2013{bg1}{ib} is the developer and designer of the LIFEBLOOD WITH GIS mobile app. This application was conceived when the developer was prompted by a post in one of the NONESCOST’s bulletin boards, about a boy who was asking for blood donation.";
 //BA.debugLineNum = 2388;BA.debugLine="ss_string = CRLF&CRLF&\"•  Motivation for the deve";
_ss_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Motivation for the development of this mobile apps is indebted to the NONESCOST FAMILY.";
 //BA.debugLineNum = 2389;BA.debugLine="s_string = CRLF&CRLF&\"{f_tittle}INFORMATION{f_tit";
_s_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"{f_tittle}INFORMATION{f_tittle}";
 //BA.debugLineNum = 2390;BA.debugLine="info_1 = CRLF&\"Blood shortage is happening in the";
_info_1 = anywheresoftware.b4a.keywords.Common.CRLF+"Blood shortage is happening in the Philippines in general, and even in Himamaylan City in particular. People here would go to nearby municipalities and cities to look for blood donors. If meet, they would go to Bacolod City just to process blood donation procedure of the government.";
 //BA.debugLineNum = 2391;BA.debugLine="info_2 = CRLF&\"With this LIFEBLOOD with GIS, look";
_info_2 = anywheresoftware.b4a.keywords.Common.CRLF+"With this LIFEBLOOD with GIS, looking for possible blood donors would just be easy. This benefit not only the “Himamaylanons” but people in the neighboring municipalities and cities as well. ";
 //BA.debugLineNum = 2392;BA.debugLine="info_3 = CRLF&CRLF&CRLF&\"{c_right}Copyright © 201";
_info_3 = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"{c_right}Copyright © 2017 Mary Ann P. Goroy{c_right}";
 //BA.debugLineNum = 2394;BA.debugLine="rs.Initialize(f_string&ss_string&s_string&info_1&";
_rs.Initialize((java.lang.CharSequence)(_f_string+_ss_string+_s_string+_info_1+_info_2+_info_3));
 //BA.debugLineNum = 2395;BA.debugLine="rs.Style2(rs.STYLE_BOLD_ITALIC, \"{ib}\")";
_rs.Style2(_rs.STYLE_BOLD_ITALIC,"{ib}");
 //BA.debugLineNum = 2396;BA.debugLine="rs.Style2(rs.STYLE_BOLD_ITALIC, \"{iib}\")";
_rs.Style2(_rs.STYLE_BOLD_ITALIC,"{iib}");
 //BA.debugLineNum = 2397;BA.debugLine="rs.RelativeSize2(1.2,\"{c_right}\")";
_rs.RelativeSize2((float) (1.2),"{c_right}");
 //BA.debugLineNum = 2398;BA.debugLine="rs.RelativeSize2(1.5,\"{f_tittle}\")";
_rs.RelativeSize2((float) (1.5),"{f_tittle}");
 //BA.debugLineNum = 2402;BA.debugLine="rs.Underscore2(\"{bg1}\")";
_rs.Underscore2("{bg1}");
 //BA.debugLineNum = 2403;BA.debugLine="rs.Underscore2(\"{bg2}\")";
_rs.Underscore2("{bg2}");
 //BA.debugLineNum = 2404;BA.debugLine="about_data.Text = rs '' to set the string output";
_about_data.setText((Object)(_rs.getObject()));
 //BA.debugLineNum = 2405;BA.debugLine="about_data.Typeface = Typeface.LoadFromAssets(\"ZI";
_about_data.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGBISD.otf"));
 //BA.debugLineNum = 2406;BA.debugLine="about_data.TextSize = 17";
_about_data.setTextSize((float) (17));
 //BA.debugLineNum = 2407;BA.debugLine="for_h.Text = rs";
_for_h.setText((Object)(_rs.getObject()));
 //BA.debugLineNum = 2408;BA.debugLine="about_us_pnl.AddView(for_h,0,0,50%x,50%y)";
mostCurrent._about_us_pnl.AddView((android.view.View)(_for_h.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 2409;BA.debugLine="for_h.Visible = False";
_for_h.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2410;BA.debugLine="Dim string_h As Int : string_h= sus.MeasureMulti";
_string_h = 0;
 //BA.debugLineNum = 2410;BA.debugLine="Dim string_h As Int : string_h= sus.MeasureMulti";
_string_h = _sus.MeasureMultilineTextHeight((android.widget.TextView)(_for_h.getObject()),_for_h.getText());
 //BA.debugLineNum = 2412;BA.debugLine="about_sc2d.Initialize(68%x,string_h+10%Y,\"about";
mostCurrent._about_sc2d.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),(int) (_string_h+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA)),"about_sc2d");
 //BA.debugLineNum = 2413;BA.debugLine="about_sc2d.ScrollbarsVisibility(False,False)";
mostCurrent._about_sc2d.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2414;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2415;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 2416;BA.debugLine="about_us_pnl.Color = Colors.Transparent";
mostCurrent._about_us_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 2417;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 2418;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,70.5%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70.5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2420;BA.debugLine="about_sc2d.Panel.AddView(about_data,2%x,0,66%x,st";
mostCurrent._about_sc2d.getPanel().AddView((android.view.View)(_about_data.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (66),mostCurrent.activityBA),(int) (_string_h+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA)));
 //BA.debugLineNum = 2421;BA.debugLine="pnl.AddView(about_sc2d,2%x,title_lbl.Top + title_";
_pnl.AddView((android.view.View)(mostCurrent._about_sc2d.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (60),mostCurrent.activityBA));
 //BA.debugLineNum = 2422;BA.debugLine="pnl.AddView(about_ok_btn,1%x,about_sc2d.Top + abo";
_pnl.AddView((android.view.View)(_about_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (mostCurrent._about_sc2d.getTop()+mostCurrent._about_sc2d.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2424;BA.debugLine="about_us_pnl.AddView(pnl,13%x,((((Activity.Height";
mostCurrent._about_us_pnl.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 2425;BA.debugLine="about_us_pnl.BringToFront";
mostCurrent._about_us_pnl.BringToFront();
 //BA.debugLineNum = 2429;BA.debugLine="Activity.AddView(about_us_pnl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._about_us_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 2430;BA.debugLine="End Sub";
return "";
}
public static String  _about_ok_btn_click() throws Exception{
 //BA.debugLineNum = 2431;BA.debugLine="Sub about_ok_btn_click";
 //BA.debugLineNum = 2432;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 //BA.debugLineNum = 2433;BA.debugLine="about_us_pnl.RemoveView";
mostCurrent._about_us_pnl.RemoveView();
 //BA.debugLineNum = 2434;BA.debugLine="End Sub";
return "";
}
public static String  _about_us_pnl_click() throws Exception{
 //BA.debugLineNum = 2435;BA.debugLine="Sub about_us_pnl_click";
 //BA.debugLineNum = 2437;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 157;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 160;BA.debugLine="Activity.LoadLayout (\"menu_frame\")";
mostCurrent._activity.LoadLayout("menu_frame",mostCurrent.activityBA);
 //BA.debugLineNum = 161;BA.debugLine="Activity.Title = \"MENU\"";
mostCurrent._activity.setTitle((Object)("MENU"));
 //BA.debugLineNum = 162;BA.debugLine="load_activity_layout";
_load_activity_layout();
 //BA.debugLineNum = 163;BA.debugLine="for_btn_animation";
_for_btn_animation();
 //BA.debugLineNum = 164;BA.debugLine="If sqlLite.IsInitialized = False Then";
if (_sqllite.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 165;BA.debugLine="sqlLite.Initialize(File.DirInternal, \"mydb.db\",";
_sqllite.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db",anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _confirm = 0;
 //BA.debugLineNum = 733;BA.debugLine="Sub Activity_KeyPress(KeyCode As Int) As Boolean";
 //BA.debugLineNum = 735;BA.debugLine="If dlgFileExpl.IsInitialized Then";
if (mostCurrent._dlgfileexpl.IsInitialized()) { 
 //BA.debugLineNum = 736;BA.debugLine="If dlgFileExpl.IsActive Then Return True";
if (mostCurrent._dlgfileexpl._isactive()) { 
if (true) return anywheresoftware.b4a.keywords.Common.True;};
 };
 //BA.debugLineNum = 738;BA.debugLine="If KeyCode == KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 739;BA.debugLine="If panel_click_ == 0 Then";
if (_panel_click_==0) { 
 //BA.debugLineNum = 741;BA.debugLine="Dim confirm As Int";
_confirm = 0;
 //BA.debugLineNum = 742;BA.debugLine="confirm = Msgbox2(\"Would you to like log out yo";
_confirm = anywheresoftware.b4a.keywords.Common.Msgbox2("Would you to like log out your account?","C O N F I R M A T I O N","YES","","NO",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 743;BA.debugLine="If confirm == DialogResponse.POSITIVE Then";
if (_confirm==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 744;BA.debugLine="login_form.is_log_in = False";
mostCurrent._login_form._is_log_in = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 745;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 //BA.debugLineNum = 746;BA.debugLine="StartActivity(\"login_form\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("login_form"));
 }else {
 };
 }else if(_panel_click_==1) { 
 //BA.debugLineNum = 754;BA.debugLine="If edit_panel_click_ == 1 Then";
if (_edit_panel_click_==1) { 
 //BA.debugLineNum = 755;BA.debugLine="pnl_blood_body.RemoveView";
mostCurrent._pnl_blood_body.RemoveView();
 //BA.debugLineNum = 756;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 }else if(_edit_panel_click_==2) { 
 //BA.debugLineNum = 758;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 759;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 }else if(_edit_panel_click_==3) { 
 //BA.debugLineNum = 761;BA.debugLine="pnl_body.RemoveView";
mostCurrent._pnl_body.RemoveView();
 //BA.debugLineNum = 762;BA.debugLine="edit_panel_click_  = 0";
_edit_panel_click_ = (int) (0);
 }else if(_edit_panel_click_==4) { 
 //BA.debugLineNum = 764;BA.debugLine="pnl_donated_body.RemoveView";
mostCurrent._pnl_donated_body.RemoveView();
 //BA.debugLineNum = 765;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 }else if(_edit_panel_click_==5) { 
 //BA.debugLineNum = 767;BA.debugLine="pnl_gender_body.RemoveView";
mostCurrent._pnl_gender_body.RemoveView();
 //BA.debugLineNum = 768;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 }else {
 //BA.debugLineNum = 770;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 771;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 //BA.debugLineNum = 772;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 };
 }else if(_panel_click_==2) { 
 //BA.debugLineNum = 776;BA.debugLine="about_us_pnl.RemoveView";
mostCurrent._about_us_pnl.RemoveView();
 //BA.debugLineNum = 777;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 }else if(_panel_click_==3) { 
 //BA.debugLineNum = 779;BA.debugLine="help_us_pnl.RemoveView";
mostCurrent._help_us_pnl.RemoveView();
 //BA.debugLineNum = 780;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 }else if(_panel_click_==4) { 
 //BA.debugLineNum = 782;BA.debugLine="If list_all_select == 1 Then";
if (_list_all_select==1) { 
 //BA.debugLineNum = 783;BA.debugLine="list_all_select = 0";
_list_all_select = (int) (0);
 //BA.debugLineNum = 784;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 }else if(_list_all_select==2) { 
 //BA.debugLineNum = 786;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 787;BA.debugLine="list_all_select = 0";
_list_all_select = (int) (0);
 }else if(_list_all_select==3) { 
 //BA.debugLineNum = 789;BA.debugLine="user_img_panl.RemoveView";
mostCurrent._user_img_panl.RemoveView();
 //BA.debugLineNum = 790;BA.debugLine="list_all_select = 2";
_list_all_select = (int) (2);
 }else {
 //BA.debugLineNum = 792;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 //BA.debugLineNum = 793;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 };
 };
 };
 //BA.debugLineNum = 799;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 800;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 552;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 554;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 548;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 550;BA.debugLine="End Sub";
return "";
}
public static String  _all_input_on_list() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_phone2 = null;
String _line_phone2 = "";
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _inp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
anywheresoftware.b4a.objects.StringUtils _su = null;
byte[] _bytes = null;
 //BA.debugLineNum = 687;BA.debugLine="Sub all_input_on_list";
 //BA.debugLineNum = 688;BA.debugLine="list_all_info.Initialize";
_list_all_info.Initialize();
 //BA.debugLineNum = 689;BA.debugLine="Dim TextReader_phone2 As TextReader";
_textreader_phone2 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 690;BA.debugLine="TextReader_phone2.Initialize(File.OpenInput(Fi";
_textreader_phone2.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"users_all_info.txt").getObject()));
 //BA.debugLineNum = 691;BA.debugLine="Dim line_phone2 As String";
_line_phone2 = "";
 //BA.debugLineNum = 692;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 //BA.debugLineNum = 693;BA.debugLine="Do While line_phone2 <> Null";
while (_line_phone2!= null) {
 //BA.debugLineNum = 694;BA.debugLine="list_all_info.Add(line_phone2)";
_list_all_info.Add((Object)(_line_phone2));
 //BA.debugLineNum = 695;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 }
;
 //BA.debugLineNum = 697;BA.debugLine="TextReader_phone2.Close";
_textreader_phone2.Close();
 //BA.debugLineNum = 699;BA.debugLine="text_fn.Text = list_all_info.Get(0)";
mostCurrent._text_fn.setText(_list_all_info.Get((int) (0)));
 //BA.debugLineNum = 700;BA.debugLine="text_blood.Text = list_all_info.Get(1)";
mostCurrent._text_blood.setText(_list_all_info.Get((int) (1)));
 //BA.debugLineNum = 701;BA.debugLine="blood_selected = list_all_info.Get(1)";
_blood_selected = BA.ObjectToString(_list_all_info.Get((int) (1)));
 //BA.debugLineNum = 702;BA.debugLine="text_email.Text = list_all_info.Get(2)";
mostCurrent._text_email.setText(_list_all_info.Get((int) (2)));
 //BA.debugLineNum = 703;BA.debugLine="text_phonenumber.Text = list_all_info.Get(3)";
mostCurrent._text_phonenumber.setText(_list_all_info.Get((int) (3)));
 //BA.debugLineNum = 704;BA.debugLine="text_phonenumber2.Text = list_all_info.Get(4)";
mostCurrent._text_phonenumber2.setText(_list_all_info.Get((int) (4)));
 //BA.debugLineNum = 705;BA.debugLine="text_bday.Text = list_all_info.Get(5)&\"/\"&list_al";
mostCurrent._text_bday.setText((Object)(BA.ObjectToString(_list_all_info.Get((int) (5)))+"/"+BA.ObjectToString(_list_all_info.Get((int) (6)))+"/"+BA.ObjectToString(_list_all_info.Get((int) (7)))));
 //BA.debugLineNum = 706;BA.debugLine="bday_month_selected = list_all_info.Get(5)";
_bday_month_selected = BA.ObjectToString(_list_all_info.Get((int) (5)));
 //BA.debugLineNum = 707;BA.debugLine="bday_day_selected = list_all_info.Get(6)";
_bday_day_selected = BA.ObjectToString(_list_all_info.Get((int) (6)));
 //BA.debugLineNum = 708;BA.debugLine="bday_year_selected = list_all_info.Get(7)";
_bday_year_selected = BA.ObjectToString(_list_all_info.Get((int) (7)));
 //BA.debugLineNum = 709;BA.debugLine="text_location.Text = list_all_info.Get(8)&\", \"&li";
mostCurrent._text_location.setText((Object)(BA.ObjectToString(_list_all_info.Get((int) (8)))+", "+BA.ObjectToString(_list_all_info.Get((int) (9)))));
 //BA.debugLineNum = 710;BA.debugLine="location_brgy_selected = list_all_info.Get(8)";
_location_brgy_selected = BA.ObjectToString(_list_all_info.Get((int) (8)));
 //BA.debugLineNum = 711;BA.debugLine="location_street_selected = list_all_info.Get(";
_location_street_selected = BA.ObjectToString(_list_all_info.Get((int) (9)));
 //BA.debugLineNum = 712;BA.debugLine="text_answer.Text = list_all_info.Get(10)";
mostCurrent._text_answer.setText(_list_all_info.Get((int) (10)));
 //BA.debugLineNum = 713;BA.debugLine="text_donated.Text = list_all_info.Get(11)";
mostCurrent._text_donated.setText(_list_all_info.Get((int) (11)));
 //BA.debugLineNum = 714;BA.debugLine="is_donated = list_all_info.Get(11)";
_is_donated = BA.ObjectToString(_list_all_info.Get((int) (11)));
 //BA.debugLineNum = 715;BA.debugLine="text_gender.Text = list_all_info.Get(13)";
mostCurrent._text_gender.setText(_list_all_info.Get((int) (13)));
 //BA.debugLineNum = 716;BA.debugLine="gender_string_data = list_all_info.Get(13)";
_gender_string_data = BA.ObjectToString(_list_all_info.Get((int) (13)));
 //BA.debugLineNum = 717;BA.debugLine="isDonateDate = list_all_info.Get(14)";
_isdonatedate = BA.ObjectToString(_list_all_info.Get((int) (14)));
 //BA.debugLineNum = 720;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 721;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 722;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 723;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 724;BA.debugLine="bytes = su.DecodeBase64(list_all_info.Get(12))";
_bytes = _su.DecodeBase64(BA.ObjectToString(_list_all_info.Get((int) (12))));
 //BA.debugLineNum = 725;BA.debugLine="image_container = list_all_info.Get(12)";
_image_container = BA.ObjectToString(_list_all_info.Get((int) (12)));
 //BA.debugLineNum = 726;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 727;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 728;BA.debugLine="usr_img.SetBackgroundImage(bmp)";
mostCurrent._usr_img.SetBackgroundImage((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 731;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 732;BA.debugLine="End Sub";
return "";
}
public static String  _all_inputs_click() throws Exception{
 //BA.debugLineNum = 1584;BA.debugLine="Sub all_inputs_click";
 //BA.debugLineNum = 1586;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1338;BA.debugLine="Sub bday_edit_Click";
 //BA.debugLineNum = 1339;BA.debugLine="edit_panel_click_ = 2";
_edit_panel_click_ = (int) (2);
 //BA.debugLineNum = 1340;BA.debugLine="list_day.Initialize";
_list_day.Initialize();
 //BA.debugLineNum = 1341;BA.debugLine="list_month.Initialize";
_list_month.Initialize();
 //BA.debugLineNum = 1342;BA.debugLine="list_year.Initialize";
_list_year.Initialize();
 //BA.debugLineNum = 1343;BA.debugLine="spin_day.Initialize(\"spin_day\")";
mostCurrent._spin_day.Initialize(mostCurrent.activityBA,"spin_day");
 //BA.debugLineNum = 1344;BA.debugLine="spin_month.Initialize(\"spin_month\")";
mostCurrent._spin_month.Initialize(mostCurrent.activityBA,"spin_month");
 //BA.debugLineNum = 1345;BA.debugLine="spin_year.Initialize(\"spin_year\")";
mostCurrent._spin_year.Initialize(mostCurrent.activityBA,"spin_year");
 //BA.debugLineNum = 1346;BA.debugLine="For i = 1 To 31";
{
final int step8 = 1;
final int limit8 = (int) (31);
for (_i = (int) (1) ; (step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8); _i = ((int)(0 + _i + step8)) ) {
 //BA.debugLineNum = 1347;BA.debugLine="list_day.Add(i)";
_list_day.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 1349;BA.debugLine="Dim iNowYear As Int";
_inowyear = 0;
 //BA.debugLineNum = 1350;BA.debugLine="iNowYear = DateTime.GetYear(DateTime.Now)";
_inowyear = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1351;BA.debugLine="For ii = 1950 To DateTime.GetYear(DateTime.Now)";
{
final int step13 = 1;
final int limit13 = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
for (_ii = (int) (1950) ; (step13 > 0 && _ii <= limit13) || (step13 < 0 && _ii >= limit13); _ii = ((int)(0 + _ii + step13)) ) {
 //BA.debugLineNum = 1352;BA.debugLine="list_year.Add(iNowYear)";
_list_year.Add((Object)(_inowyear));
 //BA.debugLineNum = 1353;BA.debugLine="iNowYear = iNowYear-1";
_inowyear = (int) (_inowyear-1);
 }
};
 //BA.debugLineNum = 1355;BA.debugLine="For iii = 1 To 12";
{
final int step17 = 1;
final int limit17 = (int) (12);
for (_iii = (int) (1) ; (step17 > 0 && _iii <= limit17) || (step17 < 0 && _iii >= limit17); _iii = ((int)(0 + _iii + step17)) ) {
 //BA.debugLineNum = 1356;BA.debugLine="list_month.Add(iii)";
_list_month.Add((Object)(_iii));
 }
};
 //BA.debugLineNum = 1358;BA.debugLine="spin_day.AddAll(list_day)";
mostCurrent._spin_day.AddAll(_list_day);
 //BA.debugLineNum = 1359;BA.debugLine="spin_month.AddAll(list_month)";
mostCurrent._spin_month.AddAll(_list_month);
 //BA.debugLineNum = 1360;BA.debugLine="spin_year.AddAll(list_year)";
mostCurrent._spin_year.AddAll(_list_year);
 //BA.debugLineNum = 1361;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1362;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1363;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1364;BA.debugLine="edit_ok_btn.Initialize(\"edit_bday_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_bday_ok_btn");
 //BA.debugLineNum = 1365;BA.debugLine="edit_can_btn.Initialize(\"edit_bday_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_bday_can_btn");
 //BA.debugLineNum = 1366;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1367;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1368;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1369;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1370;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1371;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1372;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1373;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1374;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1375;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1376;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1377;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1378;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1379;BA.debugLine="title_lbl.Text = \"SELECT BIRTH DATE\"";
_title_lbl.setText((Object)("SELECT BIRTH DATE"));
 //BA.debugLineNum = 1380;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1381;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1382;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1383;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1384;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1385;BA.debugLine="pnl_bday_body.Initialize(\"pnl_bday_body\")";
mostCurrent._pnl_bday_body.Initialize(mostCurrent.activityBA,"pnl_bday_body");
 //BA.debugLineNum = 1386;BA.debugLine="pnl_bday_body.Color = Colors.Transparent";
mostCurrent._pnl_bday_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1387;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1388;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1389;BA.debugLine="pnl.AddView(spin_day,2%x,title_lbl.Top + title_lb";
_pnl.AddView((android.view.View)(mostCurrent._spin_day.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1390;BA.debugLine="pnl.AddView(spin_month,spin_day.Left+spin_day.Wid";
_pnl.AddView((android.view.View)(mostCurrent._spin_month.getObject()),(int) (mostCurrent._spin_day.getLeft()+mostCurrent._spin_day.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_day.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1391;BA.debugLine="pnl.AddView(spin_year,spin_month.Left+spin_month.";
_pnl.AddView((android.view.View)(mostCurrent._spin_year.getObject()),(int) (mostCurrent._spin_month.getLeft()+mostCurrent._spin_month.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_month.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1392;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_year.Top+spin_y";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1393;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1395;BA.debugLine="pnl_bday_body.AddView(pnl,13%x,((Activity.Height/";
mostCurrent._pnl_bday_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1396;BA.debugLine="pnl_bday_body.BringToFront";
mostCurrent._pnl_bday_body.BringToFront();
 //BA.debugLineNum = 1399;BA.debugLine="Activity.AddView(pnl_bday_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_bday_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1400;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1424;BA.debugLine="Sub blood_edit_Click";
 //BA.debugLineNum = 1426;BA.debugLine="edit_panel_click_ = 1";
_edit_panel_click_ = (int) (1);
 //BA.debugLineNum = 1427;BA.debugLine="list_bloodgroup.Initialize";
_list_bloodgroup.Initialize();
 //BA.debugLineNum = 1428;BA.debugLine="spin_bloodgroup.Initialize(\"spin_bloodgroup\")";
mostCurrent._spin_bloodgroup.Initialize(mostCurrent.activityBA,"spin_bloodgroup");
 //BA.debugLineNum = 1429;BA.debugLine="list_bloodgroup.Add(\"A+\")";
_list_bloodgroup.Add((Object)("A+"));
 //BA.debugLineNum = 1430;BA.debugLine="list_bloodgroup.Add(\"B+\")";
_list_bloodgroup.Add((Object)("B+"));
 //BA.debugLineNum = 1431;BA.debugLine="list_bloodgroup.Add(\"O+\")";
_list_bloodgroup.Add((Object)("O+"));
 //BA.debugLineNum = 1432;BA.debugLine="list_bloodgroup.Add(\"AB+\")";
_list_bloodgroup.Add((Object)("AB+"));
 //BA.debugLineNum = 1437;BA.debugLine="list_bloodgroup.Add(\"A-\")";
_list_bloodgroup.Add((Object)("A-"));
 //BA.debugLineNum = 1438;BA.debugLine="list_bloodgroup.Add(\"B-\")";
_list_bloodgroup.Add((Object)("B-"));
 //BA.debugLineNum = 1439;BA.debugLine="list_bloodgroup.Add(\"O-\")";
_list_bloodgroup.Add((Object)("O-"));
 //BA.debugLineNum = 1440;BA.debugLine="list_bloodgroup.Add(\"AB-\")";
_list_bloodgroup.Add((Object)("AB-"));
 //BA.debugLineNum = 1441;BA.debugLine="spin_bloodgroup.AddAll(list_bloodgroup)";
mostCurrent._spin_bloodgroup.AddAll(_list_bloodgroup);
 //BA.debugLineNum = 1442;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1443;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1444;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1445;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1446;BA.debugLine="edit_ok_btn.Initialize(\"edit_blood_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_blood_ok_btn");
 //BA.debugLineNum = 1447;BA.debugLine="edit_can_btn.Initialize(\"edit_blood_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_blood_can_btn");
 //BA.debugLineNum = 1448;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1449;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1450;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1451;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1452;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1453;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1454;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1455;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1456;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1457;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1458;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1459;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1460;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1461;BA.debugLine="pnl_blood_body.Initialize(\"pnl_blood_body\")";
mostCurrent._pnl_blood_body.Initialize(mostCurrent.activityBA,"pnl_blood_body");
 //BA.debugLineNum = 1462;BA.debugLine="pnl_blood_body.Color = Colors.Transparent";
mostCurrent._pnl_blood_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1463;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1464;BA.debugLine="title_lbl.Text = \"SELECT BLOOD TYPE\"";
_title_lbl.setText((Object)("SELECT BLOOD TYPE"));
 //BA.debugLineNum = 1465;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1466;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1467;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1468;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1469;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1470;BA.debugLine="pnl.AddView(spin_bloodgroup,2%x,title_lbl.Top + t";
_pnl.AddView((android.view.View)(mostCurrent._spin_bloodgroup.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1471;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_bloodgroup.Top+";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_bloodgroup.getTop()+mostCurrent._spin_bloodgroup.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1472;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_bloodgroup.getTop()+mostCurrent._spin_bloodgroup.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1474;BA.debugLine="pnl_blood_body.AddView(pnl,13%x,((Activity.Height";
mostCurrent._pnl_blood_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1475;BA.debugLine="pnl_blood_body.BringToFront";
mostCurrent._pnl_blood_body.BringToFront();
 //BA.debugLineNum = 1478;BA.debugLine="Activity.AddView(pnl_blood_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_blood_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1479;BA.debugLine="End Sub";
return "";
}
public static String  _bookmark_image_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bgnd = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap1 = null;
anywheresoftware.b4a.objects.PanelWrapper _panel0 = null;
int _paneltop = 0;
int _panelheight = 0;
anywheresoftware.b4a.objects.collections.List _fulln_llist = null;
anywheresoftware.b4a.objects.collections.List _location_list = null;
anywheresoftware.b4a.sql.SQL.CursorWrapper _set_cursor = null;
int _i = 0;
anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
anywheresoftware.b4a.objects.ButtonWrapper _dialog_panel_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _dialog_panel_tittle = null;
anywheresoftware.b4a.objects.PanelWrapper _btn_panel = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _se_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 2538;BA.debugLine="Sub bookmark_image_Click";
 //BA.debugLineNum = 2540;BA.debugLine="panel_click_ = 4";
_panel_click_ = (int) (4);
 //BA.debugLineNum = 2541;BA.debugLine="ProgressDialogShow2(\"Loading data, Please Wait...";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Loading data, Please Wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2542;BA.debugLine="If scrolllista.IsInitialized == True Then";
if (mostCurrent._scrolllista.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 2543;BA.debugLine="scrolllista.RemoveView";
mostCurrent._scrolllista.RemoveView();
 //BA.debugLineNum = 2544;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 };
 //BA.debugLineNum = 2546;BA.debugLine="bookmark_id_list.Initialize";
_bookmark_id_list.Initialize();
 //BA.debugLineNum = 2547;BA.debugLine="scrolllista.Initialize(500)";
mostCurrent._scrolllista.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 2548;BA.debugLine="dialog_panel.Initialize(\"dialog_panel\")";
mostCurrent._dialog_panel.Initialize(mostCurrent.activityBA,"dialog_panel");
 //BA.debugLineNum = 2549;BA.debugLine="dialog_all_panel.Initialize(\"dialog_all_panel\")";
mostCurrent._dialog_all_panel.Initialize(mostCurrent.activityBA,"dialog_all_panel");
 //BA.debugLineNum = 2551;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2552;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 2553;BA.debugLine="Dim bgnd As ColorDrawable";
_bgnd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 2554;BA.debugLine="bgnd.Initialize(Colors.Cyan, 5dip)";
_bgnd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Cyan,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 2555;BA.debugLine="pnl.Background = bgnd";
_pnl.setBackground((android.graphics.drawable.Drawable)(_bgnd.getObject()));
 //BA.debugLineNum = 2556;BA.debugLine="dialog_all_panel.Color = Colors.Transparent";
mostCurrent._dialog_all_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 2571;BA.debugLine="Dim Bitmap1 As Bitmap";
_bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 2572;BA.debugLine="Dim Panel0 As Panel";
_panel0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2573;BA.debugLine="Dim PanelTop, PanelHeight  As Int";
_paneltop = 0;
_panelheight = 0;
 //BA.debugLineNum = 2578;BA.debugLine="Bitmap1.Initialize(File.DirAssets,\"banner1.png\")";
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner1.png");
 //BA.debugLineNum = 2579;BA.debugLine="PanelTop=1%y";
_paneltop = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA);
 //BA.debugLineNum = 2580;BA.debugLine="Panel0=scrolllista.Panel";
_panel0 = mostCurrent._scrolllista.getPanel();
 //BA.debugLineNum = 2581;BA.debugLine="Panel0.Color=Colors.argb(0,0,0,0)  'sets the invi";
_panel0.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 2585;BA.debugLine="Dim fullN_llist,location_list As List";
_fulln_llist = new anywheresoftware.b4a.objects.collections.List();
_location_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 2586;BA.debugLine="fullN_llist.Initialize";
_fulln_llist.Initialize();
 //BA.debugLineNum = 2587;BA.debugLine="location_list.Initialize";
_location_list.Initialize();
 //BA.debugLineNum = 2588;BA.debugLine="If sqlLite.IsInitialized = True Then";
if (_sqllite.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 2589;BA.debugLine="Dim set_cursor As Cursor";
_set_cursor = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 2590;BA.debugLine="set_cursor = sqlLite.ExecQuery(\"select * from `";
_set_cursor.setObject((android.database.Cursor)(_sqllite.ExecQuery("select * from `bookmarks`;")));
 //BA.debugLineNum = 2591;BA.debugLine="For i = 0 To set_cursor.RowCount - 1";
{
final int step30 = 1;
final int limit30 = (int) (_set_cursor.getRowCount()-1);
for (_i = (int) (0) ; (step30 > 0 && _i <= limit30) || (step30 < 0 && _i >= limit30); _i = ((int)(0 + _i + step30)) ) {
 //BA.debugLineNum = 2592;BA.debugLine="set_cursor.Position = i";
_set_cursor.setPosition(_i);
 //BA.debugLineNum = 2593;BA.debugLine="bookmark_id_list.Add(set_cursor.GetInt(\"use";
_bookmark_id_list.Add((Object)(_set_cursor.GetInt("users_id")));
 //BA.debugLineNum = 2594;BA.debugLine="fullN_llist.Add(set_cursor.GetString(\"full_";
_fulln_llist.Add((Object)(_set_cursor.GetString("full_name")));
 //BA.debugLineNum = 2595;BA.debugLine="location_list.Add(set_cursor.GetString(\"loc";
_location_list.Add((Object)(_set_cursor.GetString("location")));
 }
};
 };
 //BA.debugLineNum = 2600;BA.debugLine="For i=0 To bookmark_id_list.Size-1";
{
final int step37 = 1;
final int limit37 = (int) (_bookmark_id_list.getSize()-1);
for (_i = (int) (0) ; (step37 > 0 && _i <= limit37) || (step37 < 0 && _i >= limit37); _i = ((int)(0 + _i + step37)) ) {
 //BA.debugLineNum = 2602;BA.debugLine="If i>0 And i<3 Then Bitmap1.Initialize(File.DirA";
if (_i>0 && _i<3) { 
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner.png");};
 //BA.debugLineNum = 2603;BA.debugLine="Dim ImageView1 As ImageView";
_imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 2604;BA.debugLine="ImageView1.Initialize(\"data_list\")";
_imageview1.Initialize(mostCurrent.activityBA,"data_list");
 //BA.debugLineNum = 2605;BA.debugLine="PanelHeight=12%y";
_panelheight = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA);
 //BA.debugLineNum = 2607;BA.debugLine="Panel0.AddView(ImageView1,1%x,PanelTop,71%x,Pane";
_panel0.AddView((android.view.View)(_imageview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),_paneltop,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 2608;BA.debugLine="ImageView1.Tag=i&\"1\"";
_imageview1.setTag((Object)(BA.NumberToString(_i)+"1"));
 //BA.debugLineNum = 2609;BA.debugLine="ImageView1.Bitmap=Bitmap1";
_imageview1.setBitmap((android.graphics.Bitmap)(_bitmap1.getObject()));
 //BA.debugLineNum = 2610;BA.debugLine="ImageView1.Gravity=Gravity.fill";
_imageview1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 2612;BA.debugLine="Dim Label1, Label2 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 2613;BA.debugLine="Label1.Initialize(\"\")";
_label1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2614;BA.debugLine="Label2.Initialize(\"\")";
_label2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2615;BA.debugLine="Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 2616;BA.debugLine="Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 2618;BA.debugLine="Label1.TextColor= Colors.White";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 2619;BA.debugLine="Label1.TextSize= 18";
_label1.setTextSize((float) (18));
 //BA.debugLineNum = 2620;BA.debugLine="Label1.Typeface = Typeface.DEFAULT_BOLD";
_label1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 2621;BA.debugLine="Label1.Gravity=Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2622;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 2623;BA.debugLine="Label1.Text=fullN_llist.Get(i) 'set data from li";
_label1.setText(_fulln_llist.Get(_i));
 //BA.debugLineNum = 2625;BA.debugLine="Label2.TextColor= Colors.White";
_label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 2626;BA.debugLine="Label2.TextSize= 15";
_label2.setTextSize((float) (15));
 //BA.debugLineNum = 2627;BA.debugLine="Label2.Gravity=Gravity.CENTER";
_label2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2628;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 2629;BA.debugLine="Label2.Text=location_list.Get(i) 'set data from";
_label2.setText(_location_list.Get(_i));
 //BA.debugLineNum = 2632;BA.debugLine="If i > bookmark_id_list.size-1 Then i = bookmark";
if (_i>_bookmark_id_list.getSize()-1) { 
_i = (int) (_bookmark_id_list.getSize()-1);};
 //BA.debugLineNum = 2635;BA.debugLine="PanelTop=PanelTop+PanelHeight";
_paneltop = (int) (_paneltop+_panelheight);
 }
};
 //BA.debugLineNum = 2637;BA.debugLine="Panel0.Height=PanelTop";
_panel0.setHeight(_paneltop);
 //BA.debugLineNum = 2639;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 2640;BA.debugLine="dialog_panel.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._dialog_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 2642;BA.debugLine="Dim dialog_panel_can_btn As Button";
_dialog_panel_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 2643;BA.debugLine="Dim dialog_panel_tittle As Label";
_dialog_panel_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 2644;BA.debugLine="Dim btn_panel As Panel";
_btn_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2645;BA.debugLine="btn_panel.Initialize(\"\")";
_btn_panel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2646;BA.debugLine="btn_panel.SetBackgroundImage(LoadBitmap(File.DirA";
_btn_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 2647;BA.debugLine="dialog_panel_can_btn.Initialize(\"dialog_panel_can";
_dialog_panel_can_btn.Initialize(mostCurrent.activityBA,"dialog_panel_can_btn");
 //BA.debugLineNum = 2648;BA.debugLine="dialog_panel_tittle.Initialize(\"dialog_panel_titt";
_dialog_panel_tittle.Initialize(mostCurrent.activityBA,"dialog_panel_tittle");
 //BA.debugLineNum = 2649;BA.debugLine="dialog_panel_tittle.Text = \"BOOKMARKS LIST\"";
_dialog_panel_tittle.setText((Object)("BOOKMARKS LIST"));
 //BA.debugLineNum = 2650;BA.debugLine="dialog_panel_can_btn.Text = \"OK\"";
_dialog_panel_can_btn.setText((Object)("OK"));
 //BA.debugLineNum = 2651;BA.debugLine="dialog_panel_can_btn.Typeface = Typeface.LoadFro";
_dialog_panel_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2652;BA.debugLine="dialog_panel_tittle.Typeface = Typeface.LoadFrom";
_dialog_panel_tittle.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2653;BA.debugLine="Dim se_btn As GradientDrawable";
_se_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 2654;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 2655;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 2656;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 2657;BA.debugLine="se_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_se_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2658;BA.debugLine="se_btn.CornerRadius = 50dip";
_se_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 2659;BA.debugLine="dialog_panel_can_btn.Background = se_btn";
_dialog_panel_can_btn.setBackground((android.graphics.drawable.Drawable)(_se_btn.getObject()));
 //BA.debugLineNum = 2660;BA.debugLine="dialog_panel_tittle.TextSize = 30";
_dialog_panel_tittle.setTextSize((float) (30));
 //BA.debugLineNum = 2661;BA.debugLine="dialog_panel_tittle.Gravity = Gravity.CENTER";
_dialog_panel_tittle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2662;BA.debugLine="dialog_panel_can_btn.Gravity = Gravity.CENTER";
_dialog_panel_can_btn.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2663;BA.debugLine="dialog_panel.AddView(dialog_panel_tittle,1%x,2%y,";
mostCurrent._dialog_panel.AddView((android.view.View)(_dialog_panel_tittle.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (83),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2664;BA.debugLine="dialog_panel.AddView(scrolllista,5%x,dialog_panel";
mostCurrent._dialog_panel.AddView((android.view.View)(mostCurrent._scrolllista.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_dialog_panel_tittle.getTop()+_dialog_panel_tittle.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (69),mostCurrent.activityBA));
 //BA.debugLineNum = 2665;BA.debugLine="dialog_panel.AddView(btn_panel,1%x,79%y,83%x,10%y";
mostCurrent._dialog_panel.AddView((android.view.View)(_btn_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (83),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 2666;BA.debugLine="btn_panel.AddView(dialog_panel_can_btn,((btn_pane";
_btn_panel.AddView((android.view.View)(_dialog_panel_can_btn.getObject()),(int) (((_btn_panel.getWidth()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (42),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2667;BA.debugLine="dialog_all_panel.AddView(dialog_panel,7.5%x,5%y,8";
mostCurrent._dialog_all_panel.AddView((android.view.View)(mostCurrent._dialog_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7.5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 2668;BA.debugLine="dialog_all_panel.Color = Colors.ARGB(128,128,128,";
mostCurrent._dialog_all_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (128),(int) (128),(int) (.50)));
 //BA.debugLineNum = 2669;BA.debugLine="Activity.AddView(dialog_all_panel,0,0,100%x,100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._dialog_all_panel.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 2673;BA.debugLine="End Sub";
return "";
}
public static String  _can_btn_click() throws Exception{
 //BA.debugLineNum = 2728;BA.debugLine="Sub can_btn_click";
 //BA.debugLineNum = 2729;BA.debugLine="list_all_select = 0";
_list_all_select = (int) (0);
 //BA.debugLineNum = 2730;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 //BA.debugLineNum = 2731;BA.debugLine="End Sub";
return "";
}
public static String  _cancel_btn_click() throws Exception{
 //BA.debugLineNum = 990;BA.debugLine="Sub cancel_btn_Click";
 //BA.debugLineNum = 991;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 //BA.debugLineNum = 992;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 993;BA.debugLine="End Sub";
return "";
}
public static String  _data_list_click() throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _send = null;
int _row = 0;
anywheresoftware.b4a.objects.PanelWrapper _view_panl = null;
anywheresoftware.b4a.objects.ButtonWrapper _vie_btn = null;
anywheresoftware.b4a.objects.ButtonWrapper _can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl_tittle = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 2674;BA.debugLine="Sub data_list_Click";
 //BA.debugLineNum = 2675;BA.debugLine="list_all_select = 1";
_list_all_select = (int) (1);
 //BA.debugLineNum = 2676;BA.debugLine="Dim Send As View";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 2677;BA.debugLine="Dim row As Int";
_row = 0;
 //BA.debugLineNum = 2678;BA.debugLine="Send=Sender";
_send.setObject((android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 2679;BA.debugLine="row=Floor(Send.Tag/10) '20";
_row = (int) (anywheresoftware.b4a.keywords.Common.Floor((double)(BA.ObjectToNumber(_send.getTag()))/(double)10));
 //BA.debugLineNum = 2680;BA.debugLine="item=row";
_item = _row;
 //BA.debugLineNum = 2681;BA.debugLine="row_click = row";
_row_click = _row;
 //BA.debugLineNum = 2686;BA.debugLine="If view_info_pnl.IsInitialized == True Then";
if (mostCurrent._view_info_pnl.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 2687;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 };
 //BA.debugLineNum = 2689;BA.debugLine="view_info_pnl.Initialize(\"view_info_pnl\")";
mostCurrent._view_info_pnl.Initialize(mostCurrent.activityBA,"view_info_pnl");
 //BA.debugLineNum = 2690;BA.debugLine="Dim view_panl As Panel";
_view_panl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2691;BA.debugLine="Dim vie_btn,can_btn As Button";
_vie_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 2692;BA.debugLine="Dim lbl_tittle As Label";
_lbl_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 2693;BA.debugLine="lbl_tittle.Initialize(\"\")";
_lbl_tittle.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2694;BA.debugLine="view_panl.Initialize(\"view_panl\")";
_view_panl.Initialize(mostCurrent.activityBA,"view_panl");
 //BA.debugLineNum = 2695;BA.debugLine="vie_btn.Initialize(\"vie_btn\")";
_vie_btn.Initialize(mostCurrent.activityBA,"vie_btn");
 //BA.debugLineNum = 2696;BA.debugLine="can_btn.Initialize(\"can_btn\")";
_can_btn.Initialize(mostCurrent.activityBA,"can_btn");
 //BA.debugLineNum = 2697;BA.debugLine="vie_btn.Text = \"VIEW\"";
_vie_btn.setText((Object)("VIEW"));
 //BA.debugLineNum = 2698;BA.debugLine="can_btn.Text = \"CANCEL\"";
_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 2699;BA.debugLine="vie_btn.Typeface = Typeface.LoadFromAssets(\"HipHo";
_vie_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2700;BA.debugLine="can_btn.Typeface = Typeface.LoadFromAssets(\"HipHo";
_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2701;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 2702;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 2703;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 2704;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 2705;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2706;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2707;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 2708;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 2709;BA.debugLine="vie_btn.Background = V_btn";
_vie_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 2710;BA.debugLine="can_btn.Background = C_btn";
_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 2711;BA.debugLine="lbl_tittle.Text = \"SELECT ACTION\"";
_lbl_tittle.setText((Object)("SELECT ACTION"));
 //BA.debugLineNum = 2712;BA.debugLine="lbl_tittle.Typeface = Typeface.LoadFromAssets(\"Hi";
_lbl_tittle.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2713;BA.debugLine="lbl_tittle.Gravity = Gravity.CENTER";
_lbl_tittle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2714;BA.debugLine="lbl_tittle.TextColor = Colors.White";
_lbl_tittle.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 2715;BA.debugLine="view_panl.SetBackgroundImage(LoadBitmap(File.DirA";
_view_panl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 2716;BA.debugLine="view_panl.AddView(lbl_tittle,1%x,2%y,72%x,8%y)";
_view_panl.AddView((android.view.View)(_lbl_tittle.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2717;BA.debugLine="view_panl.AddView(vie_btn,5%x,lbl_tittle.Top + lb";
_view_panl.AddView((android.view.View)(_vie_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_lbl_tittle.getTop()+_lbl_tittle.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (31),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2718;BA.debugLine="view_panl.AddView(can_btn,vie_btn.Left+vie_btn.Wi";
_view_panl.AddView((android.view.View)(_can_btn.getObject()),(int) (_vie_btn.getLeft()+_vie_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),_vie_btn.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (31),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2719;BA.debugLine="view_info_pnl.AddView(view_panl,13%x,((Activity.H";
mostCurrent._view_info_pnl.AddView((android.view.View)(_view_panl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 2720;BA.debugLine="Activity.AddView(view_info_pnl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._view_info_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 2721;BA.debugLine="End Sub";
return "";
}
public static String  _database_init() throws Exception{
 //BA.debugLineNum = 2531;BA.debugLine="Sub database_init";
 //BA.debugLineNum = 2532;BA.debugLine="If File.Exists(File.DirInternal,\"mydb.db\") = True";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db")==anywheresoftware.b4a.keywords.Common.True) { 
 }else {
 //BA.debugLineNum = 2535;BA.debugLine="File.Copy(File.DirAssets,\"mydb.db\",File.DirInter";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mydb.db",anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db");
 };
 //BA.debugLineNum = 2537;BA.debugLine="End Sub";
return "";
}
public static String  _delete_bookmark_click() throws Exception{
int _choose_bm = 0;
 //BA.debugLineNum = 2738;BA.debugLine="Sub delete_bookmark_click";
 //BA.debugLineNum = 2739;BA.debugLine="Dim choose_bm As Int";
_choose_bm = 0;
 //BA.debugLineNum = 2740;BA.debugLine="choose_bm = Msgbox2(\"Would you like to delete thi";
_choose_bm = anywheresoftware.b4a.keywords.Common.Msgbox2("Would you like to delete this bookmark?","C O N F I R M A T I O N","YES","","NO",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 2741;BA.debugLine="If choose_bm == DialogResponse.POSITIVE Then";
if (_choose_bm==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 2742;BA.debugLine="If sqlLite.IsInitialized == True Then";
if (_sqllite.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 2743;BA.debugLine="sqlLite.ExecNonQuery(\"DELETE FROM `bookmarks`";
_sqllite.ExecNonQuery("DELETE FROM `bookmarks` WHERE  `users_id`='"+BA.ObjectToString(_bookmark_id_list.Get(_row_click))+"';");
 //BA.debugLineNum = 2745;BA.debugLine="list_all_select = 0";
_list_all_select = (int) (0);
 //BA.debugLineNum = 2746;BA.debugLine="panel_click_ = 4";
_panel_click_ = (int) (4);
 //BA.debugLineNum = 2747;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 2748;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 //BA.debugLineNum = 2749;BA.debugLine="bookmark_image_Click";
_bookmark_image_click();
 };
 }else if(_choose_bm==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 };
 //BA.debugLineNum = 2755;BA.debugLine="End Sub";
return "";
}
public static String  _dialog_all_panel_click() throws Exception{
 //BA.debugLineNum = 2732;BA.debugLine="Sub dialog_all_panel_click";
 //BA.debugLineNum = 2734;BA.debugLine="End Sub";
return "";
}
public static String  _dialog_panel_can_btn_click() throws Exception{
 //BA.debugLineNum = 2723;BA.debugLine="Sub dialog_panel_can_btn_click";
 //BA.debugLineNum = 2724;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 //BA.debugLineNum = 2726;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 //BA.debugLineNum = 2727;BA.debugLine="End Sub";
return "";
}
public static String  _donate_spin_day_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1296;BA.debugLine="Sub donate_spin_day_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 1297;BA.debugLine="donate_d_pos = Position";
_donate_d_pos = _position;
 //BA.debugLineNum = 1299;BA.debugLine="End Sub";
return "";
}
public static String  _donate_spin_month_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1300;BA.debugLine="Sub donate_spin_month_ItemClick (Position As Int,";
 //BA.debugLineNum = 1301;BA.debugLine="donate_m_pos = Position";
_donate_m_pos = _position;
 //BA.debugLineNum = 1303;BA.debugLine="End Sub";
return "";
}
public static String  _donate_spin_year_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1304;BA.debugLine="Sub donate_spin_year_ItemClick (Position As Int, V";
 //BA.debugLineNum = 1305;BA.debugLine="donate_y_pos = Position";
_donate_y_pos = _position;
 //BA.debugLineNum = 1307;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1170;BA.debugLine="Sub donated_edit_Click";
 //BA.debugLineNum = 1171;BA.debugLine="edit_panel_click_ = 4";
_edit_panel_click_ = (int) (4);
 //BA.debugLineNum = 1172;BA.debugLine="list_donated.Initialize";
_list_donated.Initialize();
 //BA.debugLineNum = 1173;BA.debugLine="spin_donated.Initialize(\"spin_donated\")";
mostCurrent._spin_donated.Initialize(mostCurrent.activityBA,"spin_donated");
 //BA.debugLineNum = 1174;BA.debugLine="list_donated.Add(\"NO\")";
_list_donated.Add((Object)("NO"));
 //BA.debugLineNum = 1175;BA.debugLine="list_donated.Add(\"YES\")";
_list_donated.Add((Object)("YES"));
 //BA.debugLineNum = 1176;BA.debugLine="spin_donated.AddAll(list_donated)";
mostCurrent._spin_donated.AddAll(_list_donated);
 //BA.debugLineNum = 1177;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1178;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1179;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1180;BA.debugLine="edit_ok_btn.Initialize(\"edit_donated_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_donated_ok_btn");
 //BA.debugLineNum = 1181;BA.debugLine="edit_can_btn.Initialize(\"edit_donated_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_donated_can_btn");
 //BA.debugLineNum = 1182;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1183;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1184;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1185;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1186;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1187;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1188;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1189;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1190;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1191;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1192;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1193;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1194;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1195;BA.debugLine="title_lbl.Text = \"SELECT DONATED STATUS\"";
_title_lbl.setText((Object)("SELECT DONATED STATUS"));
 //BA.debugLineNum = 1196;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1197;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1198;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1199;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1200;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1201;BA.debugLine="pnl_donated_body.Initialize(\"pnl_donated_body\")";
mostCurrent._pnl_donated_body.Initialize(mostCurrent.activityBA,"pnl_donated_body");
 //BA.debugLineNum = 1202;BA.debugLine="pnl_donated_body.Color = Colors.Transparent";
mostCurrent._pnl_donated_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1203;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1204;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1205;BA.debugLine="pnl.AddView(spin_donated,2%x,title_lbl.Top+title_";
_pnl.AddView((android.view.View)(mostCurrent._spin_donated.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1206;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_donated.Top+spi";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_donated.getTop()+mostCurrent._spin_donated.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1207;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_donated.getTop()+mostCurrent._spin_donated.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1209;BA.debugLine="pnl_donated_body.AddView(pnl,13%x,((Activity.Heig";
mostCurrent._pnl_donated_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1210;BA.debugLine="pnl_donated_body.BringToFront";
mostCurrent._pnl_donated_body.BringToFront();
 //BA.debugLineNum = 1213;BA.debugLine="Activity.AddView(pnl_donated_body,0,0,100%x,100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_donated_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1214;BA.debugLine="End Sub";
return "";
}
public static String  _edit_bday_can_btn_click() throws Exception{
 //BA.debugLineNum = 1418;BA.debugLine="Sub edit_bday_can_btn_click";
 //BA.debugLineNum = 1419;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 1420;BA.debugLine="End Sub";
return "";
}
public static String  _edit_bday_ok_btn_click() throws Exception{
 //BA.debugLineNum = 1413;BA.debugLine="Sub edit_bday_ok_btn_click";
 //BA.debugLineNum = 1414;BA.debugLine="text_bday.Text = bday_month_selected&\"/\"&bday_day";
mostCurrent._text_bday.setText((Object)(_bday_month_selected+"/"+_bday_day_selected+"/"+_bday_year_selected));
 //BA.debugLineNum = 1416;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 1417;BA.debugLine="End Sub";
return "";
}
public static String  _edit_blood_can_btn_click() throws Exception{
 //BA.debugLineNum = 1484;BA.debugLine="Sub edit_blood_can_btn_click";
 //BA.debugLineNum = 1485;BA.debugLine="pnl_blood_body.RemoveView";
mostCurrent._pnl_blood_body.RemoveView();
 //BA.debugLineNum = 1486;BA.debugLine="End Sub";
return "";
}
public static String  _edit_blood_ok_btn_click() throws Exception{
 //BA.debugLineNum = 1480;BA.debugLine="Sub edit_blood_ok_btn_click";
 //BA.debugLineNum = 1481;BA.debugLine="text_blood.Text = blood_selected";
mostCurrent._text_blood.setText((Object)(_blood_selected));
 //BA.debugLineNum = 1482;BA.debugLine="pnl_blood_body.RemoveView";
mostCurrent._pnl_blood_body.RemoveView();
 //BA.debugLineNum = 1483;BA.debugLine="End Sub";
return "";
}
public static String  _edit_can_btn_click() throws Exception{
 //BA.debugLineNum = 1570;BA.debugLine="Sub edit_can_btn_click";
 //BA.debugLineNum = 1571;BA.debugLine="pnl_body.RemoveView";
mostCurrent._pnl_body.RemoveView();
 //BA.debugLineNum = 1572;BA.debugLine="End Sub";
return "";
}
public static String  _edit_donated_can_btn_click() throws Exception{
 //BA.debugLineNum = 1332;BA.debugLine="Sub edit_donated_can_btn_click";
 //BA.debugLineNum = 1333;BA.debugLine="pnl_donated_body.RemoveView";
mostCurrent._pnl_donated_body.RemoveView();
 //BA.debugLineNum = 1334;BA.debugLine="End Sub";
return "";
}
public static String  _edit_donated_ok_btn_click() throws Exception{
 //BA.debugLineNum = 1220;BA.debugLine="Sub edit_donated_ok_btn_click";
 //BA.debugLineNum = 1221;BA.debugLine="If donated_index == 0 Then";
if (_donated_index==0) { 
 //BA.debugLineNum = 1222;BA.debugLine="is_donated = \"NO\"";
_is_donated = "NO";
 //BA.debugLineNum = 1223;BA.debugLine="isDonateDate = \"NONE\"";
_isdonatedate = "NONE";
 //BA.debugLineNum = 1224;BA.debugLine="text_donated.Text = \"NO\"";
mostCurrent._text_donated.setText((Object)("NO"));
 }else {
 //BA.debugLineNum = 1226;BA.debugLine="isDonate_edit_";
_isdonate_edit_();
 };
 //BA.debugLineNum = 1230;BA.debugLine="pnl_donated_body.RemoveView";
mostCurrent._pnl_donated_body.RemoveView();
 //BA.debugLineNum = 1231;BA.debugLine="End Sub";
return "";
}
public static String  _edit_gender_can_btn_click() throws Exception{
 //BA.debugLineNum = 1164;BA.debugLine="Sub edit_gender_can_btn_click";
 //BA.debugLineNum = 1165;BA.debugLine="pnl_gender_body.RemoveView";
mostCurrent._pnl_gender_body.RemoveView();
 //BA.debugLineNum = 1166;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1109;BA.debugLine="Sub edit_gender_Click";
 //BA.debugLineNum = 1110;BA.debugLine="edit_panel_click_ = 5";
_edit_panel_click_ = (int) (5);
 //BA.debugLineNum = 1111;BA.debugLine="list_is_gender.Initialize";
_list_is_gender.Initialize();
 //BA.debugLineNum = 1112;BA.debugLine="spin_gender.Initialize(\"spin_gender\")";
mostCurrent._spin_gender.Initialize(mostCurrent.activityBA,"spin_gender");
 //BA.debugLineNum = 1113;BA.debugLine="list_is_gender.Add(\"Male\")";
_list_is_gender.Add((Object)("Male"));
 //BA.debugLineNum = 1114;BA.debugLine="list_is_gender.Add(\"Female\")";
_list_is_gender.Add((Object)("Female"));
 //BA.debugLineNum = 1115;BA.debugLine="spin_gender.AddAll(list_is_gender)";
mostCurrent._spin_gender.AddAll(_list_is_gender);
 //BA.debugLineNum = 1116;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1117;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1118;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1119;BA.debugLine="edit_ok_btn.Initialize(\"edit_gender_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_gender_ok_btn");
 //BA.debugLineNum = 1120;BA.debugLine="edit_can_btn.Initialize(\"edit_gender_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_gender_can_btn");
 //BA.debugLineNum = 1121;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1122;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1123;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1124;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1125;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1126;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1127;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1128;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1129;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1130;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1131;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1132;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1133;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1134;BA.debugLine="title_lbl.Text = \"SELECT GENDER\"";
_title_lbl.setText((Object)("SELECT GENDER"));
 //BA.debugLineNum = 1135;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1136;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1137;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1138;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1139;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1140;BA.debugLine="pnl_gender_body.Initialize(\"pnl_gender_body\")";
mostCurrent._pnl_gender_body.Initialize(mostCurrent.activityBA,"pnl_gender_body");
 //BA.debugLineNum = 1141;BA.debugLine="pnl_gender_body.Color = Colors.Transparent";
mostCurrent._pnl_gender_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1142;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1143;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1144;BA.debugLine="pnl.AddView(spin_gender,2%x,title_lbl.Top+title_l";
_pnl.AddView((android.view.View)(mostCurrent._spin_gender.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1145;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_gender.Top+spin";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_gender.getTop()+mostCurrent._spin_gender.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1146;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_gender.getTop()+mostCurrent._spin_gender.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1148;BA.debugLine="pnl_gender_body.AddView(pnl,13%x,((Activity.Heigh";
mostCurrent._pnl_gender_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1149;BA.debugLine="pnl_gender_body.BringToFront";
mostCurrent._pnl_gender_body.BringToFront();
 //BA.debugLineNum = 1152;BA.debugLine="Activity.AddView(pnl_gender_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_gender_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1153;BA.debugLine="End Sub";
return "";
}
public static String  _edit_gender_ok_btn_click() throws Exception{
 //BA.debugLineNum = 1158;BA.debugLine="Sub edit_gender_ok_btn_click";
 //BA.debugLineNum = 1160;BA.debugLine="text_gender.Text = list_is_gender.Get(is_gender_i";
mostCurrent._text_gender.setText(_list_is_gender.Get(_is_gender_index));
 //BA.debugLineNum = 1161;BA.debugLine="gender_string_data = list_is_gender.Get(is_gender";
_gender_string_data = BA.ObjectToString(_list_is_gender.Get(_is_gender_index));
 //BA.debugLineNum = 1162;BA.debugLine="pnl_gender_body.RemoveView";
mostCurrent._pnl_gender_body.RemoveView();
 //BA.debugLineNum = 1163;BA.debugLine="End Sub";
return "";
}
public static String  _edit_ok_btn_click() throws Exception{
String _click_string = "";
 //BA.debugLineNum = 1573;BA.debugLine="Sub edit_ok_btn_click";
 //BA.debugLineNum = 1574;BA.debugLine="Dim click_string As String";
_click_string = "";
 //BA.debugLineNum = 1575;BA.debugLine="click_string = location_spin_brgy.GetItem(brgy_in";
_click_string = mostCurrent._location_spin_brgy.GetItem(_brgy_index)+", "+mostCurrent._location_spin_street.GetItem(_street_index);
 //BA.debugLineNum = 1576;BA.debugLine="location_brgy_selected = location_spin_brgy.GetIt";
_location_brgy_selected = mostCurrent._location_spin_brgy.GetItem(_brgy_index);
 //BA.debugLineNum = 1577;BA.debugLine="location_street_selected = location_spin_stree";
_location_street_selected = mostCurrent._location_spin_street.GetItem(_street_index);
 //BA.debugLineNum = 1578;BA.debugLine="text_location.Text = click_string";
mostCurrent._text_location.setText((Object)(_click_string));
 //BA.debugLineNum = 1579;BA.debugLine="pnl_body.RemoveView";
mostCurrent._pnl_body.RemoveView();
 //BA.debugLineNum = 1580;BA.debugLine="End Sub";
return "";
}
public static String  _exit_btn_click() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper _aa1 = null;
int _confirm = 0;
 //BA.debugLineNum = 965;BA.debugLine="Sub exit_btn_Click";
 //BA.debugLineNum = 966;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 967;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 968;BA.debugLine="exit_img.Tag = aa1";
mostCurrent._exit_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 969;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 970;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 971;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 972;BA.debugLine="a5.Start(exit_btn)";
mostCurrent._a5.Start((android.view.View)(mostCurrent._exit_btn.getObject()));
 //BA.debugLineNum = 973;BA.debugLine="aa1.Start(exit_img)";
_aa1.Start((android.view.View)(mostCurrent._exit_img.getObject()));
 //BA.debugLineNum = 975;BA.debugLine="If login_form.is_log_in == True Then";
if (mostCurrent._login_form._is_log_in==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 976;BA.debugLine="Dim confirm As Int";
_confirm = 0;
 //BA.debugLineNum = 977;BA.debugLine="confirm = Msgbox2(\"Would you to like log out you";
_confirm = anywheresoftware.b4a.keywords.Common.Msgbox2("Would you to like log out your account, and exit the application?","C O N F I R M A T I O N","YES","","NO",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 978;BA.debugLine="If confirm == DialogResponse.POSITIVE Then";
if (_confirm==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 979;BA.debugLine="login_form.is_log_in = False";
mostCurrent._login_form._is_log_in = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 981;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 };
 }else {
 };
 //BA.debugLineNum = 989;BA.debugLine="End Sub";
return "";
}
public static String  _for_btn_animation() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper[] _animations = null;
int _i = 0;
 //BA.debugLineNum = 168;BA.debugLine="Sub for_btn_animation";
 //BA.debugLineNum = 169;BA.debugLine="a1.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 170;BA.debugLine="a2.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a2.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 171;BA.debugLine="a3.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a3.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 172;BA.debugLine="a4.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a4.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 173;BA.debugLine="a5.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a5.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 174;BA.debugLine="search_blood.Tag = a1";
mostCurrent._search_blood.setTag((Object)(mostCurrent._a1.getObject()));
 //BA.debugLineNum = 175;BA.debugLine="profile.Tag = a2";
mostCurrent._profile.setTag((Object)(mostCurrent._a2.getObject()));
 //BA.debugLineNum = 176;BA.debugLine="about.Tag = a3";
mostCurrent._about.setTag((Object)(mostCurrent._a3.getObject()));
 //BA.debugLineNum = 177;BA.debugLine="help.Tag = a4";
mostCurrent._help.setTag((Object)(mostCurrent._a4.getObject()));
 //BA.debugLineNum = 178;BA.debugLine="exit_btn.Tag = a5";
mostCurrent._exit_btn.setTag((Object)(mostCurrent._a5.getObject()));
 //BA.debugLineNum = 179;BA.debugLine="Dim animations() As Animation";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[(int) (0)];
{
int d0 = _animations.length;
for (int i0 = 0;i0 < d0;i0++) {
_animations[i0] = new anywheresoftware.b4a.objects.AnimationWrapper();
}
}
;
 //BA.debugLineNum = 180;BA.debugLine="animations = Array As Animation(a1, a2, a3, a4, a";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[]{mostCurrent._a1,mostCurrent._a2,mostCurrent._a3,mostCurrent._a4,mostCurrent._a5};
 //BA.debugLineNum = 181;BA.debugLine="For i = 0 To animations.Length - 1";
{
final int step13 = 1;
final int limit13 = (int) (_animations.length-1);
for (_i = (int) (0) ; (step13 > 0 && _i <= limit13) || (step13 < 0 && _i >= limit13); _i = ((int)(0 + _i + step13)) ) {
 //BA.debugLineNum = 182;BA.debugLine="animations(i).Duration = 200";
_animations[_i].setDuration((long) (200));
 //BA.debugLineNum = 183;BA.debugLine="animations(i).RepeatCount = 1";
_animations[_i].setRepeatCount((int) (1));
 //BA.debugLineNum = 184;BA.debugLine="animations(i).RepeatMode = animations(i).REPEAT_";
_animations[_i].setRepeatMode(_animations[_i].REPEAT_REVERSE);
 }
};
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _for_phone_clik_animation() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper[] _animations = null;
int _i = 0;
 //BA.debugLineNum = 3011;BA.debugLine="Sub for_phone_clik_animation";
 //BA.debugLineNum = 3012;BA.debugLine="ph1_a1.InitializeAlpha(\"\", 1, 0)";
mostCurrent._ph1_a1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 3013;BA.debugLine="ph2_a2.InitializeAlpha(\"\", 1, 0)";
mostCurrent._ph2_a2.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 3014;BA.debugLine="userI_a3.InitializeAlpha(\"\", 1, 0)";
mostCurrent._useri_a3.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 3016;BA.debugLine="ph2_pnl.Tag = ph2_a2";
mostCurrent._ph2_pnl.setTag((Object)(mostCurrent._ph2_a2.getObject()));
 //BA.debugLineNum = 3017;BA.debugLine="ph1_pnl.Tag = ph1_a1";
mostCurrent._ph1_pnl.setTag((Object)(mostCurrent._ph1_a1.getObject()));
 //BA.debugLineNum = 3018;BA.debugLine="user_image.Tag = userI_a3";
mostCurrent._user_image.setTag((Object)(mostCurrent._useri_a3.getObject()));
 //BA.debugLineNum = 3020;BA.debugLine="Dim animations() As Animation";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[(int) (0)];
{
int d0 = _animations.length;
for (int i0 = 0;i0 < d0;i0++) {
_animations[i0] = new anywheresoftware.b4a.objects.AnimationWrapper();
}
}
;
 //BA.debugLineNum = 3021;BA.debugLine="animations = Array As Animation(ph2_a2, ph1_a1, u";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[]{mostCurrent._ph2_a2,mostCurrent._ph1_a1,mostCurrent._useri_a3};
 //BA.debugLineNum = 3022;BA.debugLine="For i = 0 To animations.Length - 1";
{
final int step9 = 1;
final int limit9 = (int) (_animations.length-1);
for (_i = (int) (0) ; (step9 > 0 && _i <= limit9) || (step9 < 0 && _i >= limit9); _i = ((int)(0 + _i + step9)) ) {
 //BA.debugLineNum = 3023;BA.debugLine="animations(i).Duration = 300";
_animations[_i].setDuration((long) (300));
 //BA.debugLineNum = 3024;BA.debugLine="animations(i).RepeatCount = 1";
_animations[_i].setRepeatCount((int) (1));
 //BA.debugLineNum = 3025;BA.debugLine="animations(i).RepeatMode = animations(i).REPEAT_";
_animations[_i].setRepeatMode(_animations[_i].REPEAT_REVERSE);
 }
};
 //BA.debugLineNum = 3027;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 47;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 50;BA.debugLine="Dim location_spin_street As Spinner";
mostCurrent._location_spin_street = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Dim location_spin_brgy As Spinner";
mostCurrent._location_spin_brgy = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Dim spin_bloodgroup As Spinner";
mostCurrent._spin_bloodgroup = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Dim spin_day,spin_month,spin_year As Spinner";
mostCurrent._spin_day = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spin_month = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spin_year = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Dim spin_donated As Spinner";
mostCurrent._spin_donated = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Dim spin_gender As Spinner";
mostCurrent._spin_gender = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Dim dlgFileExpl As ClsExplorer";
mostCurrent._dlgfileexpl = new bloodlife.system.clsexplorer();
 //BA.debugLineNum = 59;BA.debugLine="Private search_blood As Button";
mostCurrent._search_blood = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private about As Button";
mostCurrent._about = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private help As Button";
mostCurrent._help = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private exit_btn As Button";
mostCurrent._exit_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private profile As Button";
mostCurrent._profile = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private src_blood_pnl As Panel";
mostCurrent._src_blood_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private users_panel As Panel";
mostCurrent._users_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private profile_pnl As Panel";
mostCurrent._profile_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Private about_pnl As Panel";
mostCurrent._about_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Private help_pnl As Panel";
mostCurrent._help_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private exit_pnl As Panel";
mostCurrent._exit_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private users_out_lbl As Label";
mostCurrent._users_out_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Private users_lbl As Label";
mostCurrent._users_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private ban_logo As ImageView";
mostCurrent._ban_logo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private ban_picture As ImageView";
mostCurrent._ban_picture = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private users_heading As Panel";
mostCurrent._users_heading = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Private srch_blood_img As ImageView";
mostCurrent._srch_blood_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Private profile_img As ImageView";
mostCurrent._profile_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Private about_img As ImageView";
mostCurrent._about_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Private help_img As ImageView";
mostCurrent._help_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private exit_img As ImageView";
mostCurrent._exit_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Dim profile_panel As Panel";
mostCurrent._profile_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Dim scroll_profile_pnl As ScrollView";
mostCurrent._scroll_profile_pnl = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Dim profile_all_body As Panel";
mostCurrent._profile_all_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 85;BA.debugLine="Private all_inputs As Panel";
mostCurrent._all_inputs = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Dim pnl_body As Panel";
mostCurrent._pnl_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Dim pnl_blood_body As Panel";
mostCurrent._pnl_blood_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 88;BA.debugLine="Dim pnl_bday_body As Panel";
mostCurrent._pnl_bday_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Dim pnl_donated_body As Panel";
mostCurrent._pnl_donated_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Dim pnl_gender_body As Panel";
mostCurrent._pnl_gender_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Private all_info_query As HttpJob";
mostCurrent._all_info_query = new bloodlife.system.httpjob();
 //BA.debugLineNum = 93;BA.debugLine="Private update_job As HttpJob";
mostCurrent._update_job = new bloodlife.system.httpjob();
 //BA.debugLineNum = 94;BA.debugLine="Private update_img_job As HttpJob";
mostCurrent._update_img_job = new bloodlife.system.httpjob();
 //BA.debugLineNum = 96;BA.debugLine="Private lab_fullname As Label";
mostCurrent._lab_fullname = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Private lab_bloodgroup As Label";
mostCurrent._lab_bloodgroup = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 98;BA.debugLine="Private lab_email As Label";
mostCurrent._lab_email = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private lab_phonenumber As Label";
mostCurrent._lab_phonenumber = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Private lab_phonenumber2 As Label";
mostCurrent._lab_phonenumber2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 101;BA.debugLine="Private lab_location As Label";
mostCurrent._lab_location = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 102;BA.debugLine="Private lab_question As Label";
mostCurrent._lab_question = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 103;BA.debugLine="Private lab_donate_confirm As Label";
mostCurrent._lab_donate_confirm = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 104;BA.debugLine="Private lab_bday As Label";
mostCurrent._lab_bday = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 107;BA.debugLine="Private text_fn As EditText";
mostCurrent._text_fn = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 108;BA.debugLine="Private text_blood As EditText";
mostCurrent._text_blood = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 109;BA.debugLine="Private text_email As EditText";
mostCurrent._text_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 110;BA.debugLine="Private text_phonenumber As EditText";
mostCurrent._text_phonenumber = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 111;BA.debugLine="Private text_phonenumber2 As EditText";
mostCurrent._text_phonenumber2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 112;BA.debugLine="Private text_bday As EditText";
mostCurrent._text_bday = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 113;BA.debugLine="Private text_location As EditText";
mostCurrent._text_location = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 114;BA.debugLine="Private text_answer As EditText";
mostCurrent._text_answer = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 115;BA.debugLine="Private text_donated As EditText";
mostCurrent._text_donated = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 116;BA.debugLine="Private cancel_btn As Button";
mostCurrent._cancel_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 117;BA.debugLine="Private update_btn As Button";
mostCurrent._update_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 118;BA.debugLine="Private usr_img As ImageView";
mostCurrent._usr_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 119;BA.debugLine="Private all_inputs_top As Panel";
mostCurrent._all_inputs_top = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 122;BA.debugLine="Private scroll_myprof As ScrollView2D";
mostCurrent._scroll_myprof = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 123;BA.debugLine="Private all_inputs_down As Panel";
mostCurrent._all_inputs_down = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 124;BA.debugLine="Private tittle As Label";
mostCurrent._tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 125;BA.debugLine="Private donated_edit As Label";
mostCurrent._donated_edit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 126;BA.debugLine="Private bday_edit As Label";
mostCurrent._bday_edit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 127;BA.debugLine="Private locate_edit As Label";
mostCurrent._locate_edit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 128;BA.debugLine="Private blood_edit As Label";
mostCurrent._blood_edit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 129;BA.debugLine="Private lab_gender As Label";
mostCurrent._lab_gender = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 130;BA.debugLine="Private text_gender As EditText";
mostCurrent._text_gender = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 131;BA.debugLine="Private edit_gender As Label";
mostCurrent._edit_gender = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 133;BA.debugLine="Private about_us_pnl As Panel";
mostCurrent._about_us_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 134;BA.debugLine="Private help_us_pnl As Panel";
mostCurrent._help_us_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 136;BA.debugLine="Dim about_sc2d As ScrollView2D";
mostCurrent._about_sc2d = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 137;BA.debugLine="Dim help_sc2d As ScrollView2D";
mostCurrent._help_sc2d = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 139;BA.debugLine="Dim a1, a2, a3, a4, a5, userImage As Animation";
mostCurrent._a1 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a2 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a3 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a4 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a5 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._userimage = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 141;BA.debugLine="Private bookmark_image As ImageView";
mostCurrent._bookmark_image = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 144;BA.debugLine="Dim dialog_panel As Panel";
mostCurrent._dialog_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 145;BA.debugLine="Dim scrolllista As ScrollView";
mostCurrent._scrolllista = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 146;BA.debugLine="Dim dialog_all_panel As Panel";
mostCurrent._dialog_all_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 147;BA.debugLine="Dim view_info_pnl As Panel";
mostCurrent._view_info_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 148;BA.debugLine="Dim view_data_info_person As Panel";
mostCurrent._view_data_info_person = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 149;BA.debugLine="Dim scroll_view_info As ScrollView2D";
mostCurrent._scroll_view_info = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 150;BA.debugLine="Dim ph1_a1,ph2_a2,userI_a3 As Animation";
mostCurrent._ph1_a1 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._ph2_a2 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._useri_a3 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 151;BA.debugLine="Dim user_image As ImageView";
mostCurrent._user_image = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 152;BA.debugLine="Private ph1_pnl,ph2_pnl As Panel";
mostCurrent._ph1_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._ph2_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 153;BA.debugLine="Private phone1,phone2 As Label";
mostCurrent._phone1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._phone2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 154;BA.debugLine="Dim image_list As List";
mostCurrent._image_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 155;BA.debugLine="Dim user_img_panl As Panel";
mostCurrent._user_img_panl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 156;BA.debugLine="End Sub";
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
String _si_string = "";
String _sev_string = "";
String _title_first = "";
String _title_second = "";
String _very_first = "";
int _string_h = 0;
 //BA.debugLineNum = 2438;BA.debugLine="Sub help_Click";
 //BA.debugLineNum = 2439;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 2440;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 2441;BA.debugLine="help_img.Tag = aa1";
mostCurrent._help_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 2442;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 2443;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 2444;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 2445;BA.debugLine="a4.Start(help)";
mostCurrent._a4.Start((android.view.View)(mostCurrent._help.getObject()));
 //BA.debugLineNum = 2446;BA.debugLine="aa1.Start(help_img)";
_aa1.Start((android.view.View)(mostCurrent._help_img.getObject()));
 //BA.debugLineNum = 2447;BA.debugLine="panel_click_ = 3";
_panel_click_ = (int) (3);
 //BA.debugLineNum = 2448;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2449;BA.debugLine="Dim help_ok_btn As Button";
_help_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 2450;BA.debugLine="Dim title_lbl,help_data,for_h As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_help_data = new anywheresoftware.b4a.objects.LabelWrapper();
_for_h = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 2451;BA.debugLine="for_h.Initialize(\"\")";
_for_h.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2452;BA.debugLine="Dim sus As StringUtils";
_sus = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 2453;BA.debugLine="help_ok_btn.Initialize(\"help_ok_btn\")";
_help_ok_btn.Initialize(mostCurrent.activityBA,"help_ok_btn");
 //BA.debugLineNum = 2454;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2455;BA.debugLine="help_data.Initialize(\"\")";
_help_data.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2456;BA.debugLine="help_ok_btn.Text = \"OK\"";
_help_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 2457;BA.debugLine="help_ok_btn.Typeface = Typeface.LoadFromAssets(\"H";
_help_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2458;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 2459;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 2460;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 2461;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 2462;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2463;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2464;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 2466;BA.debugLine="help_ok_btn.Background = V_btn";
_help_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 2467;BA.debugLine="title_lbl.Text = \"HELP\"";
_title_lbl.setText((Object)("HELP"));
 //BA.debugLineNum = 2468;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hip";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2469;BA.debugLine="title_lbl.TextSize = 25";
_title_lbl.setTextSize((float) (25));
 //BA.debugLineNum = 2471;BA.debugLine="title_lbl.SetBackgroundImage(LoadBitmap(File.DirA";
_title_lbl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bgs.jpg").getObject()));
 //BA.debugLineNum = 2472;BA.debugLine="title_lbl.TextColor = Colors.White";
_title_lbl.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 2474;BA.debugLine="help_us_pnl.Initialize(\"help_us_pnl\")";
mostCurrent._help_us_pnl.Initialize(mostCurrent.activityBA,"help_us_pnl");
 //BA.debugLineNum = 2475;BA.debugLine="Dim rs As RichString";
_rs = new anywheresoftware.b4a.agraham.richstring.RichStringBuilder.RichString();
 //BA.debugLineNum = 2476;BA.debugLine="Dim f_string,s_string,t_string,fo_string,fi_strin";
_f_string = "";
_s_string = "";
_t_string = "";
_fo_string = "";
_fi_string = "";
_si_string = "";
_sev_string = "";
 //BA.debugLineNum = 2477;BA.debugLine="Dim title_first,title_second,very_first As String";
_title_first = "";
_title_second = "";
_very_first = "";
 //BA.debugLineNum = 2478;BA.debugLine="very_first = CRLF&\"Bookmark: use for saving multi";
_very_first = anywheresoftware.b4a.keywords.Common.CRLF+"Bookmark: use for saving multiple possible blood donors. ";
 //BA.debugLineNum = 2480;BA.debugLine="title_first = CRLF&CRLF&\"{f_tittle}Caution:{f_tit";
_title_first = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"{f_tittle}Caution:{f_tittle}";
 //BA.debugLineNum = 2481;BA.debugLine="f_string = CRLF&\"•	DO NOT do a search and contact";
_f_string = anywheresoftware.b4a.keywords.Common.CRLF+"•	DO NOT do a search and contact just to test the app. If you do TEST the SEARCH button and contact the donor, telling him/her that you are only testing the app, he/she might be disappointed. You’re not only wasting your time and effort, but also that of the patient who really needs a blood donor. ";
 //BA.debugLineNum = 2482;BA.debugLine="s_string = CRLF&CRLF&\"•  Once you gave your name";
_s_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Once you gave your name and your contact number, all your information will be shown to all this mobile app users. ";
 //BA.debugLineNum = 2483;BA.debugLine="t_string = CRLF&CRLF&\"•  Your mobile number will";
_t_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Your mobile number will be called once the recipient found you as possible donors. ";
 //BA.debugLineNum = 2485;BA.debugLine="title_second = CRLF&CRLF&\"{s_tittle}Information:{";
_title_second = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"{s_tittle}Information:{s_tittle}";
 //BA.debugLineNum = 2486;BA.debugLine="fo_string = CRLF&\"•  Using call button is more ef";
_fo_string = anywheresoftware.b4a.keywords.Common.CRLF+"•  Using call button is more effective than sending message to the donor. It saves time and effort. ";
 //BA.debugLineNum = 2487;BA.debugLine="fi_string = CRLF&CRLF&\"•  Select the correct Feed";
_fi_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Select the correct Feedback given to this app so that it helps in boosting the confidence of the developer in making more community based apps like this-LIFEBLOOD WITH GIS. ";
 //BA.debugLineNum = 2488;BA.debugLine="si_string = CRLF&CRLF&\"•  Researcher will be exci";
_si_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Researcher will be excited to hear from you. Do share your feedbacks, queries or concerns. Please email me at {bg}beckaygoroy@gmail.com{bg} ";
 //BA.debugLineNum = 2489;BA.debugLine="sev_string = CRLF&CRLF&\"•  Please share this app";
_sev_string = anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"•  Please share this app and let us build a community that cares.";
 //BA.debugLineNum = 2490;BA.debugLine="rs.Initialize(very_first&title_first&f_string&s_s";
_rs.Initialize((java.lang.CharSequence)(_very_first+_title_first+_f_string+_s_string+_t_string+_title_second+_fo_string+_fi_string+_si_string+_sev_string));
 //BA.debugLineNum = 2491;BA.debugLine="rs.Color2(Colors.Cyan,\"{bg}\")";
_rs.Color2(anywheresoftware.b4a.keywords.Common.Colors.Cyan,"{bg}");
 //BA.debugLineNum = 2492;BA.debugLine="rs.RelativeSize2(1.5,\"{f_tittle}\")";
_rs.RelativeSize2((float) (1.5),"{f_tittle}");
 //BA.debugLineNum = 2493;BA.debugLine="rs.RelativeSize2(1.5,\"{s_tittle}\")";
_rs.RelativeSize2((float) (1.5),"{s_tittle}");
 //BA.debugLineNum = 2494;BA.debugLine="help_data.Text = rs '' to set the string output";
_help_data.setText((Object)(_rs.getObject()));
 //BA.debugLineNum = 2495;BA.debugLine="help_data.Typeface = Typeface.LoadFromAssets(\"ZIN";
_help_data.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGBISD.otf"));
 //BA.debugLineNum = 2496;BA.debugLine="help_data.TextSize = 17";
_help_data.setTextSize((float) (17));
 //BA.debugLineNum = 2497;BA.debugLine="for_h.Text = rs";
_for_h.setText((Object)(_rs.getObject()));
 //BA.debugLineNum = 2498;BA.debugLine="help_us_pnl.AddView(for_h,0,0,50%x,50%y)";
mostCurrent._help_us_pnl.AddView((android.view.View)(_for_h.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 2499;BA.debugLine="for_h.Visible = False";
_for_h.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2500;BA.debugLine="Dim string_h As Int : string_h= sus.MeasureMulti";
_string_h = 0;
 //BA.debugLineNum = 2500;BA.debugLine="Dim string_h As Int : string_h= sus.MeasureMulti";
_string_h = _sus.MeasureMultilineTextHeight((android.widget.TextView)(_for_h.getObject()),_for_h.getText());
 //BA.debugLineNum = 2502;BA.debugLine="help_sc2d.Initialize(68%x,string_h+10%Y,\"help_s";
mostCurrent._help_sc2d.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),(int) (_string_h+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA)),"help_sc2d");
 //BA.debugLineNum = 2503;BA.debugLine="help_sc2d.ScrollbarsVisibility(False,False)";
mostCurrent._help_sc2d.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2504;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2505;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 2507;BA.debugLine="help_us_pnl.Color = Colors.Transparent";
mostCurrent._help_us_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 2508;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 2509;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,70%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2511;BA.debugLine="pnl.AddView(help_sc2d,2%x,title_lbl.Top + title_l";
_pnl.AddView((android.view.View)(mostCurrent._help_sc2d.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (60),mostCurrent.activityBA));
 //BA.debugLineNum = 2512;BA.debugLine="help_sc2d.Panel.AddView(help_data,2%x,0,66%x,stri";
mostCurrent._help_sc2d.getPanel().AddView((android.view.View)(_help_data.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (66),mostCurrent.activityBA),(int) (_string_h+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA)));
 //BA.debugLineNum = 2513;BA.debugLine="pnl.AddView(help_ok_btn,1%x,help_sc2d.Top + help_";
_pnl.AddView((android.view.View)(_help_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (mostCurrent._help_sc2d.getTop()+mostCurrent._help_sc2d.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2515;BA.debugLine="help_us_pnl.AddView(pnl,13%x,((((Activity.Height/";
mostCurrent._help_us_pnl.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 2516;BA.debugLine="help_us_pnl.BringToFront";
mostCurrent._help_us_pnl.BringToFront();
 //BA.debugLineNum = 2519;BA.debugLine="Activity.AddView(help_us_pnl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._help_us_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 2520;BA.debugLine="End Sub";
return "";
}
public static String  _help_ok_btn_click() throws Exception{
 //BA.debugLineNum = 2521;BA.debugLine="Sub help_ok_btn_click";
 //BA.debugLineNum = 2522;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 //BA.debugLineNum = 2523;BA.debugLine="help_us_pnl.RemoveView";
mostCurrent._help_us_pnl.RemoveView();
 //BA.debugLineNum = 2524;BA.debugLine="End Sub";
return "";
}
public static String  _help_us_pnl_click() throws Exception{
 //BA.debugLineNum = 2525;BA.debugLine="Sub help_us_pnl_click";
 //BA.debugLineNum = 2527;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1232;BA.debugLine="Sub isDonate_edit_";
 //BA.debugLineNum = 1233;BA.debugLine="donated_index = 0";
_donated_index = (int) (0);
 //BA.debugLineNum = 1235;BA.debugLine="list_day.Initialize";
_list_day.Initialize();
 //BA.debugLineNum = 1236;BA.debugLine="list_month.Initialize";
_list_month.Initialize();
 //BA.debugLineNum = 1237;BA.debugLine="list_year.Initialize";
_list_year.Initialize();
 //BA.debugLineNum = 1238;BA.debugLine="spin_day.Initialize(\"donate_spin_day\")";
mostCurrent._spin_day.Initialize(mostCurrent.activityBA,"donate_spin_day");
 //BA.debugLineNum = 1239;BA.debugLine="spin_month.Initialize(\"donate_spin_month\")";
mostCurrent._spin_month.Initialize(mostCurrent.activityBA,"donate_spin_month");
 //BA.debugLineNum = 1240;BA.debugLine="spin_year.Initialize(\"donate_spin_year\")";
mostCurrent._spin_year.Initialize(mostCurrent.activityBA,"donate_spin_year");
 //BA.debugLineNum = 1241;BA.debugLine="For i = 1 To 31";
{
final int step8 = 1;
final int limit8 = (int) (31);
for (_i = (int) (1) ; (step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8); _i = ((int)(0 + _i + step8)) ) {
 //BA.debugLineNum = 1242;BA.debugLine="list_day.Add(i)";
_list_day.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 1244;BA.debugLine="Dim iNowYear As Int";
_inowyear = 0;
 //BA.debugLineNum = 1245;BA.debugLine="iNowYear = DateTime.GetYear(DateTime.Now)";
_inowyear = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1246;BA.debugLine="For ii = 1950 To DateTime.GetYear(DateTime.Now)";
{
final int step13 = 1;
final int limit13 = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
for (_ii = (int) (1950) ; (step13 > 0 && _ii <= limit13) || (step13 < 0 && _ii >= limit13); _ii = ((int)(0 + _ii + step13)) ) {
 //BA.debugLineNum = 1247;BA.debugLine="list_year.Add(iNowYear)";
_list_year.Add((Object)(_inowyear));
 //BA.debugLineNum = 1248;BA.debugLine="iNowYear = iNowYear-1";
_inowyear = (int) (_inowyear-1);
 }
};
 //BA.debugLineNum = 1250;BA.debugLine="For iii = 1 To 12";
{
final int step17 = 1;
final int limit17 = (int) (12);
for (_iii = (int) (1) ; (step17 > 0 && _iii <= limit17) || (step17 < 0 && _iii >= limit17); _iii = ((int)(0 + _iii + step17)) ) {
 //BA.debugLineNum = 1251;BA.debugLine="list_month.Add(iii)";
_list_month.Add((Object)(_iii));
 }
};
 //BA.debugLineNum = 1253;BA.debugLine="spin_day.AddAll(list_day)";
mostCurrent._spin_day.AddAll(_list_day);
 //BA.debugLineNum = 1254;BA.debugLine="spin_month.AddAll(list_month)";
mostCurrent._spin_month.AddAll(_list_month);
 //BA.debugLineNum = 1255;BA.debugLine="spin_year.AddAll(list_year)";
mostCurrent._spin_year.AddAll(_list_year);
 //BA.debugLineNum = 1256;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1257;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1258;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1259;BA.debugLine="edit_ok_btn.Initialize(\"isdonated_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"isdonated_ok_btn");
 //BA.debugLineNum = 1260;BA.debugLine="edit_can_btn.Initialize(\"isdonated_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"isdonated_can_btn");
 //BA.debugLineNum = 1261;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1262;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1263;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1264;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1265;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1266;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1267;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1268;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1269;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1270;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1271;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1272;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1273;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1274;BA.debugLine="title_lbl.Text = \"SELECT DONATED DATE\"";
_title_lbl.setText((Object)("SELECT DONATED DATE"));
 //BA.debugLineNum = 1275;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1276;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1277;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1278;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1279;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1280;BA.debugLine="pnl_bday_body.Initialize(\"pnl_bday_body\")";
mostCurrent._pnl_bday_body.Initialize(mostCurrent.activityBA,"pnl_bday_body");
 //BA.debugLineNum = 1281;BA.debugLine="pnl_bday_body.Color = Colors.Transparent";
mostCurrent._pnl_bday_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1282;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1283;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1284;BA.debugLine="pnl.AddView(spin_day,2%x,title_lbl.Top + title_lb";
_pnl.AddView((android.view.View)(mostCurrent._spin_day.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1285;BA.debugLine="pnl.AddView(spin_month,spin_day.Left+spin_day.Wid";
_pnl.AddView((android.view.View)(mostCurrent._spin_month.getObject()),(int) (mostCurrent._spin_day.getLeft()+mostCurrent._spin_day.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_day.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1286;BA.debugLine="pnl.AddView(spin_year,spin_month.Left+spin_month.";
_pnl.AddView((android.view.View)(mostCurrent._spin_year.getObject()),(int) (mostCurrent._spin_month.getLeft()+mostCurrent._spin_month.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_month.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1287;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_year.Top+spin_y";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1288;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1290;BA.debugLine="pnl_bday_body.AddView(pnl,13%x,((Activity.Height/";
mostCurrent._pnl_bday_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1291;BA.debugLine="pnl_bday_body.BringToFront";
mostCurrent._pnl_bday_body.BringToFront();
 //BA.debugLineNum = 1294;BA.debugLine="Activity.AddView(pnl_bday_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_bday_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1295;BA.debugLine="End Sub";
return "";
}
public static String  _isdonated_can_btn_click() throws Exception{
 //BA.debugLineNum = 1320;BA.debugLine="Sub isdonated_can_btn_click";
 //BA.debugLineNum = 1322;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 1323;BA.debugLine="If text_donated.Text == \"NO\" Then";
if ((mostCurrent._text_donated.getText()).equals("NO")) { 
 //BA.debugLineNum = 1324;BA.debugLine="text_donated.Text = \"NO\"";
mostCurrent._text_donated.setText((Object)("NO"));
 }else {
 //BA.debugLineNum = 1326;BA.debugLine="text_donated.Text = \"YES\"";
mostCurrent._text_donated.setText((Object)("YES"));
 };
 //BA.debugLineNum = 1329;BA.debugLine="End Sub";
return "";
}
public static String  _isdonated_ok_btn_click() throws Exception{
String _day = "";
String _month = "";
String _year = "";
 //BA.debugLineNum = 1308;BA.debugLine="Sub isdonated_ok_btn_click";
 //BA.debugLineNum = 1309;BA.debugLine="Dim day,month,year As String";
_day = "";
_month = "";
_year = "";
 //BA.debugLineNum = 1310;BA.debugLine="day = spin_day.GetItem(donate_d_pos)";
_day = mostCurrent._spin_day.GetItem(_donate_d_pos);
 //BA.debugLineNum = 1311;BA.debugLine="month = spin_month.GetItem(donate_m_pos)";
_month = mostCurrent._spin_month.GetItem(_donate_m_pos);
 //BA.debugLineNum = 1312;BA.debugLine="year = spin_year.GetItem(donate_y_pos)";
_year = mostCurrent._spin_year.GetItem(_donate_y_pos);
 //BA.debugLineNum = 1313;BA.debugLine="isDonateDate = month&\"/\"&day&\"/\"&year";
_isdonatedate = _month+"/"+_day+"/"+_year;
 //BA.debugLineNum = 1315;BA.debugLine="Msgbox(\"\"&month&\"/\"&day&\"/\"&year,\"Date Selected\")";
anywheresoftware.b4a.keywords.Common.Msgbox(""+_month+"/"+_day+"/"+_year,"Date Selected",mostCurrent.activityBA);
 //BA.debugLineNum = 1316;BA.debugLine="text_donated.Text = is_donated";
mostCurrent._text_donated.setText((Object)(_is_donated));
 //BA.debugLineNum = 1318;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 1319;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(bloodlife.system.httpjob _job) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _user_all_info = null;
int _confirmr = 0;
 //BA.debugLineNum = 855;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 856;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 857;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"all_info_query","update_img_job")) {
case 0: {
 //BA.debugLineNum = 859;BA.debugLine="Dim user_all_info As TextWriter";
_user_all_info = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 860;BA.debugLine="user_all_info.Initialize(File.OpenOutput(";
_user_all_info.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"users_all_info.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 861;BA.debugLine="user_all_info.WriteLine(job.GetString.Tr";
_user_all_info.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 862;BA.debugLine="user_all_info.Close";
_user_all_info.Close();
 break; }
case 1: {
 break; }
}
;
 //BA.debugLineNum = 868;BA.debugLine="If optionSelected == \"pofileView\" Then";
if ((_optionselected).equals("pofileView")) { 
 //BA.debugLineNum = 869;BA.debugLine="all_input_on_list";
_all_input_on_list();
 }else {
 //BA.debugLineNum = 871;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 872;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 873;BA.debugLine="Dim confirmR As Int";
_confirmr = 0;
 //BA.debugLineNum = 874;BA.debugLine="confirmR = Msgbox2(\"successfully Update!\",\"C O";
_confirmr = anywheresoftware.b4a.keywords.Common.Msgbox2("successfully Update!","C O N F I R M A T I O N","OK","","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 875;BA.debugLine="If confirmR == DialogResponse.POSITIVE Then";
if (_confirmr==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 876;BA.debugLine="users_out_lbl.text = text_answer.Text";
mostCurrent._users_out_lbl.setText((Object)(mostCurrent._text_answer.getText()));
 }else {
 };
 };
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 882;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 883;BA.debugLine="profile_all_body.RemoveView";
mostCurrent._profile_all_body.RemoveView();
 //BA.debugLineNum = 884;BA.debugLine="Msgbox(\"Error connecting to server, try again!\"";
anywheresoftware.b4a.keywords.Common.Msgbox("Error connecting to server, try again!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 887;BA.debugLine="End Sub";
return "";
}
public static String  _load_activity_layout() throws Exception{
bloodlife.system.calculations _text_temp = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _search_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _about_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _help_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _profile_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _exit_grad = null;
int[] _col = null;
 //BA.debugLineNum = 368;BA.debugLine="Sub load_activity_layout";
 //BA.debugLineNum = 369;BA.debugLine="Dim text_temp As calculations";
_text_temp = new bloodlife.system.calculations();
 //BA.debugLineNum = 371;BA.debugLine="text_temp.Initialize";
_text_temp._initialize(processBA);
 //BA.debugLineNum = 374;BA.debugLine="users_out_lbl.text = login_form.name_query";
mostCurrent._users_out_lbl.setText((Object)(mostCurrent._login_form._name_query));
 //BA.debugLineNum = 375;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 376;BA.debugLine="ban_logo.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._ban_logo.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logo1.jpg").getObject()));
 //BA.debugLineNum = 377;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 378;BA.debugLine="users_panel.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._users_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 379;BA.debugLine="src_blood_pnl.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._src_blood_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 380;BA.debugLine="profile_pnl.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._profile_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 381;BA.debugLine="about_pnl.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._about_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 382;BA.debugLine="help_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._help_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 383;BA.debugLine="exit_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._exit_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 385;BA.debugLine="srch_blood_img.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._srch_blood_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu_search.png").getObject()));
 //BA.debugLineNum = 386;BA.debugLine="profile_img.SetBackgroundImage(LoadBitmap(File";
mostCurrent._profile_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"emyprofile.png").getObject()));
 //BA.debugLineNum = 387;BA.debugLine="about_img.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._about_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eaboutus.png").getObject()));
 //BA.debugLineNum = 388;BA.debugLine="help_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._help_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ehelp.png").getObject()));
 //BA.debugLineNum = 389;BA.debugLine="exit_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._exit_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eexit.png").getObject()));
 //BA.debugLineNum = 391;BA.debugLine="bookmark_image.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._bookmark_image.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bh3.png").getObject()));
 //BA.debugLineNum = 392;BA.debugLine="users_heading.Color = Colors.Transparent";
mostCurrent._users_heading.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 394;BA.debugLine="bookmark_image.Width = 12%x";
mostCurrent._bookmark_image.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 395;BA.debugLine="ban_picture.Width = 80%x";
mostCurrent._ban_picture.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 396;BA.debugLine="ban_logo.Width = 20%x";
mostCurrent._ban_logo.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 397;BA.debugLine="users_panel.Width = Activity.Width";
mostCurrent._users_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 398;BA.debugLine="src_blood_pnl.Width = Activity.Width";
mostCurrent._src_blood_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 399;BA.debugLine="profile_pnl.Width = Activity.Width";
mostCurrent._profile_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 400;BA.debugLine="about_pnl.Width = Activity.Width";
mostCurrent._about_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 401;BA.debugLine="help_pnl.Width = Activity.Width";
mostCurrent._help_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 402;BA.debugLine="exit_pnl.Width = Activity.Width";
mostCurrent._exit_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 403;BA.debugLine="users_heading.Width = Activity.Width";
mostCurrent._users_heading.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 405;BA.debugLine="bookmark_image.Height = 7%y";
mostCurrent._bookmark_image.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 406;BA.debugLine="users_heading.Height = 9%y";
mostCurrent._users_heading.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 407;BA.debugLine="users_panel.Height = 18%y";
mostCurrent._users_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 408;BA.debugLine="ban_picture.Height = users_panel.Height";
mostCurrent._ban_picture.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 409;BA.debugLine="ban_logo.Height = users_panel.Height";
mostCurrent._ban_logo.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 410;BA.debugLine="src_blood_pnl.Height = 12%y";
mostCurrent._src_blood_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 411;BA.debugLine="profile_pnl.Height = 12%y";
mostCurrent._profile_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 412;BA.debugLine="about_pnl.Height = 12%y";
mostCurrent._about_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 413;BA.debugLine="help_pnl.Height = 12%y";
mostCurrent._help_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 414;BA.debugLine="exit_pnl.Height = 12%y";
mostCurrent._exit_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 416;BA.debugLine="ban_logo.Left = 0";
mostCurrent._ban_logo.setLeft((int) (0));
 //BA.debugLineNum = 417;BA.debugLine="ban_picture.Left = ban_logo.Left + ban_logo.Width";
mostCurrent._ban_picture.setLeft((int) (mostCurrent._ban_logo.getLeft()+mostCurrent._ban_logo.getWidth()));
 //BA.debugLineNum = 418;BA.debugLine="users_panel.Left = 0";
mostCurrent._users_panel.setLeft((int) (0));
 //BA.debugLineNum = 419;BA.debugLine="src_blood_pnl.Left = 0";
mostCurrent._src_blood_pnl.setLeft((int) (0));
 //BA.debugLineNum = 420;BA.debugLine="profile_pnl.Left = 0";
mostCurrent._profile_pnl.setLeft((int) (0));
 //BA.debugLineNum = 421;BA.debugLine="about_pnl.Left = 0";
mostCurrent._about_pnl.setLeft((int) (0));
 //BA.debugLineNum = 422;BA.debugLine="help_pnl.Left = 0";
mostCurrent._help_pnl.setLeft((int) (0));
 //BA.debugLineNum = 423;BA.debugLine="exit_pnl.Left = 0";
mostCurrent._exit_pnl.setLeft((int) (0));
 //BA.debugLineNum = 424;BA.debugLine="users_heading.Left = 0";
mostCurrent._users_heading.setLeft((int) (0));
 //BA.debugLineNum = 426;BA.debugLine="users_panel.Top = 0";
mostCurrent._users_panel.setTop((int) (0));
 //BA.debugLineNum = 427;BA.debugLine="ban_picture.Top = 0";
mostCurrent._ban_picture.setTop((int) (0));
 //BA.debugLineNum = 428;BA.debugLine="ban_logo.Top = 0";
mostCurrent._ban_logo.setTop((int) (0));
 //BA.debugLineNum = 429;BA.debugLine="users_heading.Top = users_panel.Top + users_panel";
mostCurrent._users_heading.setTop((int) (mostCurrent._users_panel.getTop()+mostCurrent._users_panel.getHeight()));
 //BA.debugLineNum = 430;BA.debugLine="src_blood_pnl.Top = users_heading.Top + users_hea";
mostCurrent._src_blood_pnl.setTop((int) (mostCurrent._users_heading.getTop()+mostCurrent._users_heading.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)));
 //BA.debugLineNum = 431;BA.debugLine="profile_pnl.Top = src_blood_pnl.Top + src_blood_p";
mostCurrent._profile_pnl.setTop((int) (mostCurrent._src_blood_pnl.getTop()+mostCurrent._src_blood_pnl.getHeight()));
 //BA.debugLineNum = 432;BA.debugLine="about_pnl.Top = profile_pnl.Top + profile_pnl.Hei";
mostCurrent._about_pnl.setTop((int) (mostCurrent._profile_pnl.getTop()+mostCurrent._profile_pnl.getHeight()));
 //BA.debugLineNum = 433;BA.debugLine="help_pnl.Top = about_pnl.Top + about_pnl.Height";
mostCurrent._help_pnl.setTop((int) (mostCurrent._about_pnl.getTop()+mostCurrent._about_pnl.getHeight()));
 //BA.debugLineNum = 434;BA.debugLine="exit_pnl.Top = help_pnl.Top + help_pnl.Height";
mostCurrent._exit_pnl.setTop((int) (mostCurrent._help_pnl.getTop()+mostCurrent._help_pnl.getHeight()));
 //BA.debugLineNum = 443;BA.debugLine="search_blood.Width = Activity.Width - 60%x";
mostCurrent._search_blood.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 444;BA.debugLine="about.Width = Activity.Width - 60%x";
mostCurrent._about.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 445;BA.debugLine="help.Width = Activity.Width - 60%x";
mostCurrent._help.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 446;BA.debugLine="profile.Width = Activity.Width - 60%x";
mostCurrent._profile.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 447;BA.debugLine="exit_btn.Width = Activity.Width - 60%x";
mostCurrent._exit_btn.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 448;BA.debugLine="srch_blood_img.Width = Activity.Width - 85%x";
mostCurrent._srch_blood_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 449;BA.debugLine="profile_img.Width = Activity.Width - 85%x";
mostCurrent._profile_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 450;BA.debugLine="about_img.Width = Activity.Width - 85%x";
mostCurrent._about_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 451;BA.debugLine="help_img.Width = Activity.Width - 85%x";
mostCurrent._help_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 452;BA.debugLine="exit_img.Width = Activity.Width - 85%x";
mostCurrent._exit_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 454;BA.debugLine="users_out_lbl.Width = 50%x";
mostCurrent._users_out_lbl.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 456;BA.debugLine="search_blood.Height = 9%y";
mostCurrent._search_blood.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 457;BA.debugLine="about.Height = 9%y";
mostCurrent._about.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 458;BA.debugLine="help.Height = 9%y";
mostCurrent._help.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 459;BA.debugLine="profile.Height = 9%y";
mostCurrent._profile.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 460;BA.debugLine="exit_btn.Height = 9%y";
mostCurrent._exit_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 461;BA.debugLine="srch_blood_img.Height = 9%y";
mostCurrent._srch_blood_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 462;BA.debugLine="profile_img.Height = 9%y";
mostCurrent._profile_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 463;BA.debugLine="about_img.Height = 9%y";
mostCurrent._about_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 464;BA.debugLine="help_img.Height = 9%y";
mostCurrent._help_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 465;BA.debugLine="exit_img.Height = 9%y";
mostCurrent._exit_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 467;BA.debugLine="bookmark_image.Left = 97%x - bookmark_image.Width";
mostCurrent._bookmark_image.setLeft((int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (97),mostCurrent.activityBA)-mostCurrent._bookmark_image.getWidth()));
 //BA.debugLineNum = 469;BA.debugLine="users_lbl.Left = 2%x";
mostCurrent._users_lbl.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA));
 //BA.debugLineNum = 470;BA.debugLine="users_out_lbl.Left = users_lbl.Left + users_lbl.W";
mostCurrent._users_out_lbl.setLeft((int) (mostCurrent._users_lbl.getLeft()+mostCurrent._users_lbl.getWidth()));
 //BA.debugLineNum = 471;BA.debugLine="search_blood.Left = ((src_blood_pnl.Width/2)/2)";
mostCurrent._search_blood.setLeft((int) (((mostCurrent._src_blood_pnl.getWidth()/(double)2)/(double)2)-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)));
 //BA.debugLineNum = 472;BA.debugLine="profile.Left = ((profile_pnl.Width/2)/2) - 2%";
mostCurrent._profile.setLeft((int) (((mostCurrent._profile_pnl.getWidth()/(double)2)/(double)2)-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)));
 //BA.debugLineNum = 473;BA.debugLine="about.Left = ((help_pnl.Width/2)/2) - 2%x";
mostCurrent._about.setLeft((int) (((mostCurrent._help_pnl.getWidth()/(double)2)/(double)2)-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)));
 //BA.debugLineNum = 474;BA.debugLine="help.Left = ((about_pnl.Width/2)/2) - 2%x";
mostCurrent._help.setLeft((int) (((mostCurrent._about_pnl.getWidth()/(double)2)/(double)2)-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)));
 //BA.debugLineNum = 475;BA.debugLine="exit_btn.Left = ((exit_pnl.Width/2)/2) - 2%x";
mostCurrent._exit_btn.setLeft((int) (((mostCurrent._exit_pnl.getWidth()/(double)2)/(double)2)-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)));
 //BA.debugLineNum = 477;BA.debugLine="srch_blood_img.Left = search_blood.Left + search";
mostCurrent._srch_blood_img.setLeft((int) (mostCurrent._search_blood.getLeft()+mostCurrent._search_blood.getWidth()));
 //BA.debugLineNum = 478;BA.debugLine="profile_img.Left = profile.Left + profile.Widt";
mostCurrent._profile_img.setLeft((int) (mostCurrent._profile.getLeft()+mostCurrent._profile.getWidth()));
 //BA.debugLineNum = 479;BA.debugLine="about_img.Left = about.Left + about.Width";
mostCurrent._about_img.setLeft((int) (mostCurrent._about.getLeft()+mostCurrent._about.getWidth()));
 //BA.debugLineNum = 480;BA.debugLine="help_img.Left = help.Left + help.Width";
mostCurrent._help_img.setLeft((int) (mostCurrent._help.getLeft()+mostCurrent._help.getWidth()));
 //BA.debugLineNum = 481;BA.debugLine="exit_img.Left = exit_btn.Left + exit_btn.Width";
mostCurrent._exit_img.setLeft((int) (mostCurrent._exit_btn.getLeft()+mostCurrent._exit_btn.getWidth()));
 //BA.debugLineNum = 483;BA.debugLine="users_out_lbl.Top = ((users_heading.Height/2)/2)/";
mostCurrent._users_out_lbl.setTop((int) (((mostCurrent._users_heading.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 484;BA.debugLine="users_lbl.Top = users_out_lbl.Top";
mostCurrent._users_lbl.setTop(mostCurrent._users_out_lbl.getTop());
 //BA.debugLineNum = 485;BA.debugLine="bookmark_image.Top = users_out_lbl.Top";
mostCurrent._bookmark_image.setTop(mostCurrent._users_out_lbl.getTop());
 //BA.debugLineNum = 487;BA.debugLine="search_blood.Top = ((src_blood_pnl.Height/2)/2)/";
mostCurrent._search_blood.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 488;BA.debugLine="about.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._about.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 489;BA.debugLine="help.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._help.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 490;BA.debugLine="profile.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._profile.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 491;BA.debugLine="exit_btn.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_btn.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 493;BA.debugLine="srch_blood_img.Top = ((src_blood_pnl.Height/2)/2";
mostCurrent._srch_blood_img.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 494;BA.debugLine="profile_img.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._profile_img.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 495;BA.debugLine="about_img.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._about_img.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 496;BA.debugLine="help_img.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._help_img.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 497;BA.debugLine="exit_img.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_img.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 499;BA.debugLine="Dim search_grad,about_grad,help_grad,profile_grad";
_search_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_about_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_help_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_profile_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_exit_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 500;BA.debugLine="Dim col(2) As Int";
_col = new int[(int) (2)];
;
 //BA.debugLineNum = 501;BA.debugLine="col(0) = Colors.Red";
_col[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 502;BA.debugLine="col(1) = Colors.LightGray";
_col[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.LightGray;
 //BA.debugLineNum = 503;BA.debugLine="search_grad.Initialize(\"TOP_BOTTOM\",col)";
_search_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 504;BA.debugLine="about_grad.Initialize(\"TOP_BOTTOM\",col)";
_about_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 505;BA.debugLine="help_grad.Initialize(\"TOP_BOTTOM\",col)";
_help_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 506;BA.debugLine="profile_grad.Initialize(\"TOP_BOTTOM\",col)";
_profile_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 507;BA.debugLine="exit_grad.Initialize(\"TOP_BOTTOM\",col)";
_exit_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 509;BA.debugLine="search_blood.Background = search_grad";
mostCurrent._search_blood.setBackground((android.graphics.drawable.Drawable)(_search_grad.getObject()));
 //BA.debugLineNum = 510;BA.debugLine="about.Background = about_grad";
mostCurrent._about.setBackground((android.graphics.drawable.Drawable)(_about_grad.getObject()));
 //BA.debugLineNum = 511;BA.debugLine="help.Background = help_grad";
mostCurrent._help.setBackground((android.graphics.drawable.Drawable)(_help_grad.getObject()));
 //BA.debugLineNum = 512;BA.debugLine="profile.Background = profile_grad";
mostCurrent._profile.setBackground((android.graphics.drawable.Drawable)(_profile_grad.getObject()));
 //BA.debugLineNum = 513;BA.debugLine="exit_btn.Background = exit_grad";
mostCurrent._exit_btn.setBackground((android.graphics.drawable.Drawable)(_exit_grad.getObject()));
 //BA.debugLineNum = 514;BA.debugLine="search_grad.CornerRadius = 5dip";
_search_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 515;BA.debugLine="about_grad.CornerRadius = 5dip";
_about_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 516;BA.debugLine="help_grad.CornerRadius = 5dip";
_help_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 517;BA.debugLine="profile_grad.CornerRadius = 5dip";
_profile_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 518;BA.debugLine="exit_grad.CornerRadius = 5dip";
_exit_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 520;BA.debugLine="search_blood.Text = \" SEARCH \"";
mostCurrent._search_blood.setText((Object)(" SEARCH "));
 //BA.debugLineNum = 521;BA.debugLine="about.Text = \" ABOUT \"";
mostCurrent._about.setText((Object)(" ABOUT "));
 //BA.debugLineNum = 522;BA.debugLine="help.Text = \" HELP \"";
mostCurrent._help.setText((Object)(" HELP "));
 //BA.debugLineNum = 523;BA.debugLine="profile.Text = \" PROFILE \"";
mostCurrent._profile.setText((Object)(" PROFILE "));
 //BA.debugLineNum = 524;BA.debugLine="exit_btn.Text = \" EXIT \"";
mostCurrent._exit_btn.setText((Object)(" EXIT "));
 //BA.debugLineNum = 526;BA.debugLine="search_blood.Typeface = Typeface.LoadFromAssets";
mostCurrent._search_blood.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 527;BA.debugLine="about.Typeface = Typeface.LoadFromAssets(\"HipHo";
mostCurrent._about.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 528;BA.debugLine="help.Typeface = Typeface.LoadFromAssets(\"HipHop";
mostCurrent._help.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 529;BA.debugLine="profile.Typeface = Typeface.LoadFromAssets(\"Hip";
mostCurrent._profile.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 530;BA.debugLine="exit_btn.Typeface = Typeface.LoadFromAssets(\"Hi";
mostCurrent._exit_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 532;BA.debugLine="search_blood.TextSize = 25";
mostCurrent._search_blood.setTextSize((float) (25));
 //BA.debugLineNum = 533;BA.debugLine="about.TextSize = 25";
mostCurrent._about.setTextSize((float) (25));
 //BA.debugLineNum = 534;BA.debugLine="help.TextSize = 25";
mostCurrent._help.setTextSize((float) (25));
 //BA.debugLineNum = 535;BA.debugLine="profile.TextSize = 25";
mostCurrent._profile.setTextSize((float) (25));
 //BA.debugLineNum = 536;BA.debugLine="exit_btn.TextSize = 25";
mostCurrent._exit_btn.setTextSize((float) (25));
 //BA.debugLineNum = 544;BA.debugLine="users_lbl.Typeface = Typeface.LoadFromAssets(\"ZI";
mostCurrent._users_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 545;BA.debugLine="users_out_lbl.Typeface = Typeface.LoadFromAsset";
mostCurrent._users_out_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 547;BA.debugLine="End Sub";
return "";
}
public static String  _load_activity_layout_backup() throws Exception{
bloodlife.system.calculations _text_temp = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _search_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _about_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _help_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _profile_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _exit_grad = null;
int[] _col = null;
 //BA.debugLineNum = 187;BA.debugLine="Sub load_activity_layout_backup";
 //BA.debugLineNum = 188;BA.debugLine="Dim text_temp As calculations";
_text_temp = new bloodlife.system.calculations();
 //BA.debugLineNum = 190;BA.debugLine="text_temp.Initialize";
_text_temp._initialize(processBA);
 //BA.debugLineNum = 193;BA.debugLine="users_out_lbl.text = login_form.name_query";
mostCurrent._users_out_lbl.setText((Object)(mostCurrent._login_form._name_query));
 //BA.debugLineNum = 194;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 195;BA.debugLine="ban_logo.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._ban_logo.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logo1.jpg").getObject()));
 //BA.debugLineNum = 196;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 197;BA.debugLine="users_panel.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._users_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 198;BA.debugLine="src_blood_pnl.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._src_blood_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 199;BA.debugLine="profile_pnl.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._profile_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 200;BA.debugLine="about_pnl.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._about_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 201;BA.debugLine="help_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._help_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 202;BA.debugLine="exit_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._exit_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 204;BA.debugLine="srch_blood_img.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._srch_blood_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu_search.png").getObject()));
 //BA.debugLineNum = 205;BA.debugLine="profile_img.SetBackgroundImage(LoadBitmap(File";
mostCurrent._profile_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"emyprofile.png").getObject()));
 //BA.debugLineNum = 206;BA.debugLine="about_img.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._about_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eaboutus.png").getObject()));
 //BA.debugLineNum = 207;BA.debugLine="help_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._help_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ehelp.png").getObject()));
 //BA.debugLineNum = 208;BA.debugLine="exit_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._exit_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eexit.png").getObject()));
 //BA.debugLineNum = 210;BA.debugLine="bookmark_image.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._bookmark_image.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bh3.png").getObject()));
 //BA.debugLineNum = 211;BA.debugLine="users_heading.Color = Colors.Transparent";
mostCurrent._users_heading.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 213;BA.debugLine="bookmark_image.Width = 12%x";
mostCurrent._bookmark_image.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 214;BA.debugLine="ban_picture.Width = 80%x";
mostCurrent._ban_picture.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 215;BA.debugLine="ban_logo.Width = 20%x";
mostCurrent._ban_logo.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 216;BA.debugLine="users_panel.Width = Activity.Width";
mostCurrent._users_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 217;BA.debugLine="src_blood_pnl.Width = Activity.Width";
mostCurrent._src_blood_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 218;BA.debugLine="profile_pnl.Width = Activity.Width";
mostCurrent._profile_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 219;BA.debugLine="about_pnl.Width = Activity.Width";
mostCurrent._about_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 220;BA.debugLine="help_pnl.Width = Activity.Width";
mostCurrent._help_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 221;BA.debugLine="exit_pnl.Width = Activity.Width";
mostCurrent._exit_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 222;BA.debugLine="users_heading.Width = Activity.Width";
mostCurrent._users_heading.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 224;BA.debugLine="bookmark_image.Height = 7%y";
mostCurrent._bookmark_image.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 225;BA.debugLine="users_heading.Height = 9%y";
mostCurrent._users_heading.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 226;BA.debugLine="users_panel.Height = 18%y";
mostCurrent._users_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 227;BA.debugLine="ban_picture.Height = users_panel.Height";
mostCurrent._ban_picture.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 228;BA.debugLine="ban_logo.Height = users_panel.Height";
mostCurrent._ban_logo.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 229;BA.debugLine="src_blood_pnl.Height = 12%y";
mostCurrent._src_blood_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 230;BA.debugLine="profile_pnl.Height = 12%y";
mostCurrent._profile_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 231;BA.debugLine="about_pnl.Height = 12%y";
mostCurrent._about_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 232;BA.debugLine="help_pnl.Height = 12%y";
mostCurrent._help_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 233;BA.debugLine="exit_pnl.Height = 12%y";
mostCurrent._exit_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 235;BA.debugLine="ban_logo.Left = 0";
mostCurrent._ban_logo.setLeft((int) (0));
 //BA.debugLineNum = 236;BA.debugLine="ban_picture.Left = ban_logo.Left + ban_logo.Width";
mostCurrent._ban_picture.setLeft((int) (mostCurrent._ban_logo.getLeft()+mostCurrent._ban_logo.getWidth()));
 //BA.debugLineNum = 237;BA.debugLine="users_panel.Left = 0";
mostCurrent._users_panel.setLeft((int) (0));
 //BA.debugLineNum = 238;BA.debugLine="src_blood_pnl.Left = 0";
mostCurrent._src_blood_pnl.setLeft((int) (0));
 //BA.debugLineNum = 239;BA.debugLine="profile_pnl.Left = 0";
mostCurrent._profile_pnl.setLeft((int) (0));
 //BA.debugLineNum = 240;BA.debugLine="about_pnl.Left = 0";
mostCurrent._about_pnl.setLeft((int) (0));
 //BA.debugLineNum = 241;BA.debugLine="help_pnl.Left = 0";
mostCurrent._help_pnl.setLeft((int) (0));
 //BA.debugLineNum = 242;BA.debugLine="exit_pnl.Left = 0";
mostCurrent._exit_pnl.setLeft((int) (0));
 //BA.debugLineNum = 243;BA.debugLine="users_heading.Left = 0";
mostCurrent._users_heading.setLeft((int) (0));
 //BA.debugLineNum = 245;BA.debugLine="users_panel.Top = 0";
mostCurrent._users_panel.setTop((int) (0));
 //BA.debugLineNum = 246;BA.debugLine="ban_picture.Top = 0";
mostCurrent._ban_picture.setTop((int) (0));
 //BA.debugLineNum = 247;BA.debugLine="ban_logo.Top = 0";
mostCurrent._ban_logo.setTop((int) (0));
 //BA.debugLineNum = 248;BA.debugLine="users_heading.Top = users_panel.Top + users_panel";
mostCurrent._users_heading.setTop((int) (mostCurrent._users_panel.getTop()+mostCurrent._users_panel.getHeight()));
 //BA.debugLineNum = 249;BA.debugLine="src_blood_pnl.Top = users_heading.Top + users_hea";
mostCurrent._src_blood_pnl.setTop((int) (mostCurrent._users_heading.getTop()+mostCurrent._users_heading.getHeight()));
 //BA.debugLineNum = 250;BA.debugLine="profile_pnl.Top = src_blood_pnl.Top + src_blood_p";
mostCurrent._profile_pnl.setTop((int) (mostCurrent._src_blood_pnl.getTop()+mostCurrent._src_blood_pnl.getHeight()));
 //BA.debugLineNum = 251;BA.debugLine="about_pnl.Top = profile_pnl.Top + profile_pnl.Hei";
mostCurrent._about_pnl.setTop((int) (mostCurrent._profile_pnl.getTop()+mostCurrent._profile_pnl.getHeight()));
 //BA.debugLineNum = 252;BA.debugLine="help_pnl.Top = about_pnl.Top + about_pnl.Height";
mostCurrent._help_pnl.setTop((int) (mostCurrent._about_pnl.getTop()+mostCurrent._about_pnl.getHeight()));
 //BA.debugLineNum = 253;BA.debugLine="exit_pnl.Top = help_pnl.Top + help_pnl.Height";
mostCurrent._exit_pnl.setTop((int) (mostCurrent._help_pnl.getTop()+mostCurrent._help_pnl.getHeight()));
 //BA.debugLineNum = 262;BA.debugLine="search_blood.Width = Activity.Width - 60%x";
mostCurrent._search_blood.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 263;BA.debugLine="about.Width = Activity.Width - 60%x";
mostCurrent._about.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 264;BA.debugLine="help.Width = Activity.Width - 60%x";
mostCurrent._help.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 265;BA.debugLine="profile.Width = Activity.Width - 60%x";
mostCurrent._profile.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 266;BA.debugLine="exit_btn.Width = Activity.Width - 60%x";
mostCurrent._exit_btn.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 267;BA.debugLine="srch_blood_img.Width = Activity.Width - 85%x";
mostCurrent._srch_blood_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 268;BA.debugLine="profile_img.Width = Activity.Width - 85%x";
mostCurrent._profile_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 269;BA.debugLine="about_img.Width = Activity.Width - 85%x";
mostCurrent._about_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 270;BA.debugLine="help_img.Width = Activity.Width - 85%x";
mostCurrent._help_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 271;BA.debugLine="exit_img.Width = Activity.Width - 85%x";
mostCurrent._exit_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 273;BA.debugLine="users_out_lbl.Width = 50%x";
mostCurrent._users_out_lbl.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 275;BA.debugLine="search_blood.Height = 9%y";
mostCurrent._search_blood.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 276;BA.debugLine="about.Height = 9%y";
mostCurrent._about.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 277;BA.debugLine="help.Height = 9%y";
mostCurrent._help.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 278;BA.debugLine="profile.Height = 9%y";
mostCurrent._profile.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 279;BA.debugLine="exit_btn.Height = 9%y";
mostCurrent._exit_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 280;BA.debugLine="srch_blood_img.Height = 9%y";
mostCurrent._srch_blood_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 281;BA.debugLine="profile_img.Height = 9%y";
mostCurrent._profile_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 282;BA.debugLine="about_img.Height = 9%y";
mostCurrent._about_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 283;BA.debugLine="help_img.Height = 9%y";
mostCurrent._help_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 284;BA.debugLine="exit_img.Height = 9%y";
mostCurrent._exit_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 286;BA.debugLine="bookmark_image.Left = 97%x - bookmark_image.Width";
mostCurrent._bookmark_image.setLeft((int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (97),mostCurrent.activityBA)-mostCurrent._bookmark_image.getWidth()));
 //BA.debugLineNum = 288;BA.debugLine="users_lbl.Left = 2%x";
mostCurrent._users_lbl.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA));
 //BA.debugLineNum = 289;BA.debugLine="users_out_lbl.Left = users_lbl.Left + users_lbl.W";
mostCurrent._users_out_lbl.setLeft((int) (mostCurrent._users_lbl.getLeft()+mostCurrent._users_lbl.getWidth()));
 //BA.debugLineNum = 290;BA.debugLine="search_blood.Left = ((src_blood_pnl.Width/2)/2)/";
mostCurrent._search_blood.setLeft((int) (((mostCurrent._src_blood_pnl.getWidth()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 291;BA.debugLine="profile.Left = ((profile_pnl.Width/2)/2)";
mostCurrent._profile.setLeft((int) (((mostCurrent._profile_pnl.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 292;BA.debugLine="about.Left = (help_pnl.Width/2)";
mostCurrent._about.setLeft((int) ((mostCurrent._help_pnl.getWidth()/(double)2)));
 //BA.debugLineNum = 293;BA.debugLine="help.Left = ((about_pnl.Width/2)/2)";
mostCurrent._help.setLeft((int) (((mostCurrent._about_pnl.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 294;BA.debugLine="exit_btn.Left = ((exit_pnl.Width/2)/2)/2";
mostCurrent._exit_btn.setLeft((int) (((mostCurrent._exit_pnl.getWidth()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 296;BA.debugLine="srch_blood_img.Left = search_blood.Left + search";
mostCurrent._srch_blood_img.setLeft((int) (mostCurrent._search_blood.getLeft()+mostCurrent._search_blood.getWidth()));
 //BA.debugLineNum = 297;BA.debugLine="profile_img.Left = profile.Left + profile.Widt";
mostCurrent._profile_img.setLeft((int) (mostCurrent._profile.getLeft()+mostCurrent._profile.getWidth()));
 //BA.debugLineNum = 298;BA.debugLine="about_img.Left = about.Left - about_img.Width";
mostCurrent._about_img.setLeft((int) (mostCurrent._about.getLeft()-mostCurrent._about_img.getWidth()));
 //BA.debugLineNum = 299;BA.debugLine="help_img.Left = help.Left + help.Width";
mostCurrent._help_img.setLeft((int) (mostCurrent._help.getLeft()+mostCurrent._help.getWidth()));
 //BA.debugLineNum = 300;BA.debugLine="exit_img.Left = exit_btn.Left + exit_btn.Width";
mostCurrent._exit_img.setLeft((int) (mostCurrent._exit_btn.getLeft()+mostCurrent._exit_btn.getWidth()));
 //BA.debugLineNum = 302;BA.debugLine="users_out_lbl.Top = ((users_heading.Height/2)/2)/";
mostCurrent._users_out_lbl.setTop((int) (((mostCurrent._users_heading.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 303;BA.debugLine="users_lbl.Top = users_out_lbl.Top";
mostCurrent._users_lbl.setTop(mostCurrent._users_out_lbl.getTop());
 //BA.debugLineNum = 304;BA.debugLine="bookmark_image.Top = users_out_lbl.Top";
mostCurrent._bookmark_image.setTop(mostCurrent._users_out_lbl.getTop());
 //BA.debugLineNum = 306;BA.debugLine="search_blood.Top = ((src_blood_pnl.Height/2)/2)/";
mostCurrent._search_blood.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 307;BA.debugLine="about.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._about.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 308;BA.debugLine="help.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._help.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 309;BA.debugLine="profile.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._profile.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 310;BA.debugLine="exit_btn.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_btn.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 312;BA.debugLine="srch_blood_img.Top = ((src_blood_pnl.Height/2)/2";
mostCurrent._srch_blood_img.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 313;BA.debugLine="profile_img.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._profile_img.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 314;BA.debugLine="about_img.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._about_img.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 315;BA.debugLine="help_img.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._help_img.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 316;BA.debugLine="exit_img.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_img.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 318;BA.debugLine="Dim search_grad,about_grad,help_grad,profile_grad";
_search_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_about_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_help_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_profile_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_exit_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 319;BA.debugLine="Dim col(2) As Int";
_col = new int[(int) (2)];
;
 //BA.debugLineNum = 320;BA.debugLine="col(0) = Colors.Red";
_col[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 321;BA.debugLine="col(1) = Colors.LightGray";
_col[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.LightGray;
 //BA.debugLineNum = 322;BA.debugLine="search_grad.Initialize(\"TOP_BOTTOM\",col)";
_search_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 323;BA.debugLine="about_grad.Initialize(\"TOP_BOTTOM\",col)";
_about_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 324;BA.debugLine="help_grad.Initialize(\"TOP_BOTTOM\",col)";
_help_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 325;BA.debugLine="profile_grad.Initialize(\"TOP_BOTTOM\",col)";
_profile_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 326;BA.debugLine="exit_grad.Initialize(\"TOP_BOTTOM\",col)";
_exit_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 328;BA.debugLine="search_blood.Background = search_grad";
mostCurrent._search_blood.setBackground((android.graphics.drawable.Drawable)(_search_grad.getObject()));
 //BA.debugLineNum = 329;BA.debugLine="about.Background = about_grad";
mostCurrent._about.setBackground((android.graphics.drawable.Drawable)(_about_grad.getObject()));
 //BA.debugLineNum = 330;BA.debugLine="help.Background = help_grad";
mostCurrent._help.setBackground((android.graphics.drawable.Drawable)(_help_grad.getObject()));
 //BA.debugLineNum = 331;BA.debugLine="profile.Background = profile_grad";
mostCurrent._profile.setBackground((android.graphics.drawable.Drawable)(_profile_grad.getObject()));
 //BA.debugLineNum = 332;BA.debugLine="exit_btn.Background = exit_grad";
mostCurrent._exit_btn.setBackground((android.graphics.drawable.Drawable)(_exit_grad.getObject()));
 //BA.debugLineNum = 333;BA.debugLine="search_grad.CornerRadius = 5dip";
_search_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 334;BA.debugLine="about_grad.CornerRadius = 5dip";
_about_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 335;BA.debugLine="help_grad.CornerRadius = 5dip";
_help_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 336;BA.debugLine="profile_grad.CornerRadius = 5dip";
_profile_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 337;BA.debugLine="exit_grad.CornerRadius = 5dip";
_exit_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 339;BA.debugLine="search_blood.Text = \" SEARCH \"";
mostCurrent._search_blood.setText((Object)(" SEARCH "));
 //BA.debugLineNum = 340;BA.debugLine="about.Text = \" ABOUT \"";
mostCurrent._about.setText((Object)(" ABOUT "));
 //BA.debugLineNum = 341;BA.debugLine="help.Text = \" HELP \"";
mostCurrent._help.setText((Object)(" HELP "));
 //BA.debugLineNum = 342;BA.debugLine="profile.Text = \" PROFILE \"";
mostCurrent._profile.setText((Object)(" PROFILE "));
 //BA.debugLineNum = 343;BA.debugLine="exit_btn.Text = \" EXIT \"";
mostCurrent._exit_btn.setText((Object)(" EXIT "));
 //BA.debugLineNum = 345;BA.debugLine="search_blood.Typeface = Typeface.LoadFromAssets";
mostCurrent._search_blood.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 346;BA.debugLine="about.Typeface = Typeface.LoadFromAssets(\"HipHo";
mostCurrent._about.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 347;BA.debugLine="help.Typeface = Typeface.LoadFromAssets(\"HipHop";
mostCurrent._help.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 348;BA.debugLine="profile.Typeface = Typeface.LoadFromAssets(\"Hip";
mostCurrent._profile.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 349;BA.debugLine="exit_btn.Typeface = Typeface.LoadFromAssets(\"Hi";
mostCurrent._exit_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 351;BA.debugLine="search_blood.TextSize = 25";
mostCurrent._search_blood.setTextSize((float) (25));
 //BA.debugLineNum = 352;BA.debugLine="about.TextSize = 25";
mostCurrent._about.setTextSize((float) (25));
 //BA.debugLineNum = 353;BA.debugLine="help.TextSize = 25";
mostCurrent._help.setTextSize((float) (25));
 //BA.debugLineNum = 354;BA.debugLine="profile.TextSize = 25";
mostCurrent._profile.setTextSize((float) (25));
 //BA.debugLineNum = 355;BA.debugLine="exit_btn.TextSize = 25";
mostCurrent._exit_btn.setTextSize((float) (25));
 //BA.debugLineNum = 363;BA.debugLine="users_lbl.Typeface = Typeface.LoadFromAssets(\"ZI";
mostCurrent._users_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 364;BA.debugLine="users_out_lbl.Typeface = Typeface.LoadFromAsset";
mostCurrent._users_out_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 366;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1494;BA.debugLine="Sub locate_edit_Click";
 //BA.debugLineNum = 1495;BA.debugLine="edit_panel_click_ = 3";
_edit_panel_click_ = (int) (3);
 //BA.debugLineNum = 1496;BA.debugLine="list_location_b.Initialize";
_list_location_b.Initialize();
 //BA.debugLineNum = 1497;BA.debugLine="location_spin_street.Initialize(\"location_spin_s";
mostCurrent._location_spin_street.Initialize(mostCurrent.activityBA,"location_spin_street");
 //BA.debugLineNum = 1498;BA.debugLine="location_spin_brgy.Initialize(\"location_spin_brg";
mostCurrent._location_spin_brgy.Initialize(mostCurrent.activityBA,"location_spin_brgy");
 //BA.debugLineNum = 1499;BA.debugLine="list_location_s.Initialize";
_list_location_s.Initialize();
 //BA.debugLineNum = 1500;BA.debugLine="list_location_b.Add(\"Barangay  1\") 'index 0";
_list_location_b.Add((Object)("Barangay  1"));
 //BA.debugLineNum = 1501;BA.debugLine="list_location_b.Add(\"Barangay 2\") 'index 1";
_list_location_b.Add((Object)("Barangay 2"));
 //BA.debugLineNum = 1502;BA.debugLine="list_location_b.Add(\"Barangay 3\") 'index 2";
_list_location_b.Add((Object)("Barangay 3"));
 //BA.debugLineNum = 1503;BA.debugLine="list_location_b.Add(\"Barangay 4\") 'index 3";
_list_location_b.Add((Object)("Barangay 4"));
 //BA.debugLineNum = 1504;BA.debugLine="list_location_b.Add(\"Aguisan\") 'index 4";
_list_location_b.Add((Object)("Aguisan"));
 //BA.debugLineNum = 1505;BA.debugLine="list_location_b.Add(\"caradio-an\") 'index 5";
_list_location_b.Add((Object)("caradio-an"));
 //BA.debugLineNum = 1506;BA.debugLine="list_location_b.Add(\"Buenavista\") 'index 6";
_list_location_b.Add((Object)("Buenavista"));
 //BA.debugLineNum = 1507;BA.debugLine="list_location_b.Add(\"Cabadiangan\") 'index 7";
_list_location_b.Add((Object)("Cabadiangan"));
 //BA.debugLineNum = 1508;BA.debugLine="list_location_b.Add(\"Cabanbanan\") 'index 8";
_list_location_b.Add((Object)("Cabanbanan"));
 //BA.debugLineNum = 1509;BA.debugLine="list_location_b.Add(\"Carabalan\") 'index 9";
_list_location_b.Add((Object)("Carabalan"));
 //BA.debugLineNum = 1510;BA.debugLine="list_location_b.Add(\"Libacao\") 'index 10";
_list_location_b.Add((Object)("Libacao"));
 //BA.debugLineNum = 1511;BA.debugLine="list_location_b.Add(\"Mahalang\") 'index 11";
_list_location_b.Add((Object)("Mahalang"));
 //BA.debugLineNum = 1512;BA.debugLine="list_location_b.Add(\"Mambagaton\") 'index 12";
_list_location_b.Add((Object)("Mambagaton"));
 //BA.debugLineNum = 1513;BA.debugLine="list_location_b.Add(\"Nabalian\") 'index 13";
_list_location_b.Add((Object)("Nabalian"));
 //BA.debugLineNum = 1514;BA.debugLine="list_location_b.Add(\"San Antonio\") 'index 14";
_list_location_b.Add((Object)("San Antonio"));
 //BA.debugLineNum = 1515;BA.debugLine="list_location_b.Add(\"Saraet\") 'index 15";
_list_location_b.Add((Object)("Saraet"));
 //BA.debugLineNum = 1516;BA.debugLine="list_location_b.Add(\"Suay\") 'index 16";
_list_location_b.Add((Object)("Suay"));
 //BA.debugLineNum = 1517;BA.debugLine="list_location_b.Add(\"Talaban\") 'index 17";
_list_location_b.Add((Object)("Talaban"));
 //BA.debugLineNum = 1518;BA.debugLine="list_location_b.Add(\"Tooy\") 'index 18";
_list_location_b.Add((Object)("Tooy"));
 //BA.debugLineNum = 1519;BA.debugLine="location_spin_brgy.AddAll(list_location_b)";
mostCurrent._location_spin_brgy.AddAll(_list_location_b);
 //BA.debugLineNum = 1521;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1522;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 1523;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 1524;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 1525;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 1526;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 1527;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 1528;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 //BA.debugLineNum = 1529;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 1531;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1532;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1533;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1534;BA.debugLine="edit_ok_btn.Initialize(\"edit_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"edit_ok_btn");
 //BA.debugLineNum = 1535;BA.debugLine="edit_can_btn.Initialize(\"edit_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"edit_can_btn");
 //BA.debugLineNum = 1536;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1537;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1538;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1539;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1540;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1541;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1542;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1543;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1544;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1545;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1546;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1547;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1548;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1549;BA.debugLine="title_lbl.Text = \"SELECT LOCATION\"";
_title_lbl.setText((Object)("SELECT LOCATION"));
 //BA.debugLineNum = 1550;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(\"";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1551;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1552;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hi";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1553;BA.debugLine="title_lbl.Gravity =  Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1554;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1555;BA.debugLine="pnl_body.Initialize(\"pnl_body\")";
mostCurrent._pnl_body.Initialize(mostCurrent.activityBA,"pnl_body");
 //BA.debugLineNum = 1556;BA.debugLine="pnl_body.Color = Colors.Transparent";
mostCurrent._pnl_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1557;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1558;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1559;BA.debugLine="pnl.AddView(location_spin_brgy,2%x,title_lbl.Top";
_pnl.AddView((android.view.View)(mostCurrent._location_spin_brgy.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1560;BA.debugLine="pnl.AddView(location_spin_street,location_spin_br";
_pnl.AddView((android.view.View)(mostCurrent._location_spin_street.getObject()),(int) (mostCurrent._location_spin_brgy.getLeft()+mostCurrent._location_spin_brgy.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),mostCurrent._location_spin_brgy.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1561;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,location_spin_street";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._location_spin_street.getTop()+mostCurrent._location_spin_street.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1562;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._location_spin_street.getTop()+mostCurrent._location_spin_street.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1564;BA.debugLine="pnl_body.AddView(pnl,13%x,((Activity.Height/2)/2)";
mostCurrent._pnl_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1565;BA.debugLine="pnl_body.BringToFront";
mostCurrent._pnl_body.BringToFront();
 //BA.debugLineNum = 1568;BA.debugLine="Activity.AddView(pnl_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1569;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_brgy_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 2139;BA.debugLine="Sub location_spin_brgy_ItemClick (Position As Int,";
 //BA.debugLineNum = 2140;BA.debugLine="list_location_s.Clear";
_list_location_s.Clear();
 //BA.debugLineNum = 2142;BA.debugLine="location_spin_street.Clear";
mostCurrent._location_spin_street.Clear();
 //BA.debugLineNum = 2145;BA.debugLine="If Position == 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 2146;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 2147;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 2148;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 2149;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 2150;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 2151;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 2152;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 2153;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 }else if(_position==1) { 
 //BA.debugLineNum = 2156;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 2157;BA.debugLine="list_location_s.Add(\"Monton st.\")";
_list_location_s.Add((Object)("Monton st."));
 //BA.debugLineNum = 2158;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 //BA.debugLineNum = 2159;BA.debugLine="list_location_s.Add(\"Purok star apple\")";
_list_location_s.Add((Object)("Purok star apple"));
 //BA.debugLineNum = 2160;BA.debugLine="list_location_s.Add(\"Gatuslao st.\")";
_list_location_s.Add((Object)("Gatuslao st."));
 //BA.debugLineNum = 2161;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 2162;BA.debugLine="list_location_s.Add(\"Tabino st.\")";
_list_location_s.Add((Object)("Tabino st."));
 //BA.debugLineNum = 2163;BA.debugLine="list_location_s.Add(\"River side\")";
_list_location_s.Add((Object)("River side"));
 //BA.debugLineNum = 2164;BA.debugLine="list_location_s.Add(\"Arroyan st\")";
_list_location_s.Add((Object)("Arroyan st"));
 }else if(_position==2) { 
 //BA.debugLineNum = 2166;BA.debugLine="list_location_s.Add(\"Segovia st.\")";
_list_location_s.Add((Object)("Segovia st."));
 //BA.debugLineNum = 2167;BA.debugLine="list_location_s.Add(\"Vasquez st.\")";
_list_location_s.Add((Object)("Vasquez st."));
 //BA.debugLineNum = 2168;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 2169;BA.debugLine="list_location_s.Add(\"Old relis st.\")";
_list_location_s.Add((Object)("Old relis st."));
 //BA.debugLineNum = 2170;BA.debugLine="list_location_s.Add(\"Wayang\")";
_list_location_s.Add((Object)("Wayang"));
 //BA.debugLineNum = 2171;BA.debugLine="list_location_s.Add(\"Valencia\")";
_list_location_s.Add((Object)("Valencia"));
 //BA.debugLineNum = 2172;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 2173;BA.debugLine="list_location_s.Add(\"Bingig\")";
_list_location_s.Add((Object)("Bingig"));
 //BA.debugLineNum = 2174;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 2175;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 }else if(_position==3) { 
 //BA.debugLineNum = 2177;BA.debugLine="list_location_s.Add(\"Crusher\")";
_list_location_s.Add((Object)("Crusher"));
 //BA.debugLineNum = 2178;BA.debugLine="list_location_s.Add(\"Bangga mayok\")";
_list_location_s.Add((Object)("Bangga mayok"));
 //BA.debugLineNum = 2179;BA.debugLine="list_location_s.Add(\"Villa julita\")";
_list_location_s.Add((Object)("Villa julita"));
 //BA.debugLineNum = 2180;BA.debugLine="list_location_s.Add(\"Greenland subdivision\")";
_list_location_s.Add((Object)("Greenland subdivision"));
 //BA.debugLineNum = 2181;BA.debugLine="list_location_s.Add(\"Bangga 3c\")";
_list_location_s.Add((Object)("Bangga 3c"));
 //BA.debugLineNum = 2182;BA.debugLine="list_location_s.Add(\"Cambugnon\")";
_list_location_s.Add((Object)("Cambugnon"));
 //BA.debugLineNum = 2183;BA.debugLine="list_location_s.Add(\"Menez\")";
_list_location_s.Add((Object)("Menez"));
 //BA.debugLineNum = 2184;BA.debugLine="list_location_s.Add(\"Relis\")";
_list_location_s.Add((Object)("Relis"));
 //BA.debugLineNum = 2185;BA.debugLine="list_location_s.Add(\"Bangga patyo\")";
_list_location_s.Add((Object)("Bangga patyo"));
 }else if(_position==4) { 
 //BA.debugLineNum = 2187;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 2188;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 2189;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 2190;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 2191;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 2192;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 //BA.debugLineNum = 2193;BA.debugLine="list_location_s.Add(\"Purok 7\")";
_list_location_s.Add((Object)("Purok 7"));
 //BA.debugLineNum = 2194;BA.debugLine="list_location_s.Add(\"Purok 8\")";
_list_location_s.Add((Object)("Purok 8"));
 //BA.debugLineNum = 2195;BA.debugLine="list_location_s.Add(\"Purok 9\")";
_list_location_s.Add((Object)("Purok 9"));
 //BA.debugLineNum = 2196;BA.debugLine="list_location_s.Add(\"Purok 10\")";
_list_location_s.Add((Object)("Purok 10"));
 //BA.debugLineNum = 2197;BA.debugLine="list_location_s.Add(\"Purok 11\")";
_list_location_s.Add((Object)("Purok 11"));
 //BA.debugLineNum = 2198;BA.debugLine="list_location_s.Add(\"Purok 12\")";
_list_location_s.Add((Object)("Purok 12"));
 }else if(_position==5) { 
 //BA.debugLineNum = 2200;BA.debugLine="list_location_s.Add(\"Malusay\")";
_list_location_s.Add((Object)("Malusay"));
 //BA.debugLineNum = 2201;BA.debugLine="list_location_s.Add(\"Nasug ong\")";
_list_location_s.Add((Object)("Nasug ong"));
 //BA.debugLineNum = 2202;BA.debugLine="list_location_s.Add(\"Lugway\")";
_list_location_s.Add((Object)("Lugway"));
 //BA.debugLineNum = 2203;BA.debugLine="list_location_s.Add(\"Ubay\")";
_list_location_s.Add((Object)("Ubay"));
 //BA.debugLineNum = 2204;BA.debugLine="list_location_s.Add(\"Fisheries\")";
_list_location_s.Add((Object)("Fisheries"));
 //BA.debugLineNum = 2205;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2206;BA.debugLine="list_location_s.Add(\"Calasa\")";
_list_location_s.Add((Object)("Calasa"));
 //BA.debugLineNum = 2207;BA.debugLine="list_location_s.Add(\"Hda. Serafin\")";
_list_location_s.Add((Object)("Hda. Serafin"));
 //BA.debugLineNum = 2208;BA.debugLine="list_location_s.Add(\"Patay na suba\")";
_list_location_s.Add((Object)("Patay na suba"));
 //BA.debugLineNum = 2209;BA.debugLine="list_location_s.Add(\"Lumanog\")";
_list_location_s.Add((Object)("Lumanog"));
 //BA.debugLineNum = 2210;BA.debugLine="list_location_s.Add(\"San agustin\")";
_list_location_s.Add((Object)("San agustin"));
 //BA.debugLineNum = 2211;BA.debugLine="list_location_s.Add(\"San jose\")";
_list_location_s.Add((Object)("San jose"));
 //BA.debugLineNum = 2212;BA.debugLine="list_location_s.Add(\"Maglantay\")";
_list_location_s.Add((Object)("Maglantay"));
 //BA.debugLineNum = 2213;BA.debugLine="list_location_s.Add(\"San juan\")";
_list_location_s.Add((Object)("San juan"));
 //BA.debugLineNum = 2214;BA.debugLine="list_location_s.Add(\"Magsaha\")";
_list_location_s.Add((Object)("Magsaha"));
 //BA.debugLineNum = 2215;BA.debugLine="list_location_s.Add(\"Tagmanok\")";
_list_location_s.Add((Object)("Tagmanok"));
 //BA.debugLineNum = 2216;BA.debugLine="list_location_s.Add(\"Butong\")";
_list_location_s.Add((Object)("Butong"));
 }else if(_position==6) { 
 //BA.debugLineNum = 2218;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2219;BA.debugLine="list_location_s.Add(\"Saisi\")";
_list_location_s.Add((Object)("Saisi"));
 //BA.debugLineNum = 2220;BA.debugLine="list_location_s.Add(\"Paloypoy\")";
_list_location_s.Add((Object)("Paloypoy"));
 //BA.debugLineNum = 2221;BA.debugLine="list_location_s.Add(\"Tigue\")";
_list_location_s.Add((Object)("Tigue"));
 }else if(_position==7) { 
 //BA.debugLineNum = 2223;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2224;BA.debugLine="list_location_s.Add(\"Tonggo\")";
_list_location_s.Add((Object)("Tonggo"));
 //BA.debugLineNum = 2225;BA.debugLine="list_location_s.Add(\"Iling iling\")";
_list_location_s.Add((Object)("Iling iling"));
 //BA.debugLineNum = 2226;BA.debugLine="list_location_s.Add(\"Campayas\")";
_list_location_s.Add((Object)("Campayas"));
 //BA.debugLineNum = 2227;BA.debugLine="list_location_s.Add(\"Palayan\")";
_list_location_s.Add((Object)("Palayan"));
 //BA.debugLineNum = 2228;BA.debugLine="list_location_s.Add(\"Guia\")";
_list_location_s.Add((Object)("Guia"));
 //BA.debugLineNum = 2229;BA.debugLine="list_location_s.Add(\"An-an\")";
_list_location_s.Add((Object)("An-an"));
 //BA.debugLineNum = 2230;BA.debugLine="list_location_s.Add(\"An-an 2\")";
_list_location_s.Add((Object)("An-an 2"));
 //BA.debugLineNum = 2231;BA.debugLine="list_location_s.Add(\"Sta. rita\")";
_list_location_s.Add((Object)("Sta. rita"));
 //BA.debugLineNum = 2232;BA.debugLine="list_location_s.Add(\"Benedicto\")";
_list_location_s.Add((Object)("Benedicto"));
 //BA.debugLineNum = 2233;BA.debugLine="list_location_s.Add(\"Sta. cruz/ bunggol\")";
_list_location_s.Add((Object)("Sta. cruz/ bunggol"));
 //BA.debugLineNum = 2234;BA.debugLine="list_location_s.Add(\"Olalia\")";
_list_location_s.Add((Object)("Olalia"));
 //BA.debugLineNum = 2235;BA.debugLine="list_location_s.Add(\"Banuyo\")";
_list_location_s.Add((Object)("Banuyo"));
 //BA.debugLineNum = 2236;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 2237;BA.debugLine="list_location_s.Add(\"Riverside\")";
_list_location_s.Add((Object)("Riverside"));
 }else if(_position==8) { 
 //BA.debugLineNum = 2239;BA.debugLine="list_location_s.Add(\"Balangga-an\")";
_list_location_s.Add((Object)("Balangga-an"));
 //BA.debugLineNum = 2240;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2241;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2242;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 2243;BA.debugLine="list_location_s.Add(\"Bakyas\")";
_list_location_s.Add((Object)("Bakyas"));
 }else if(_position==9) { 
 //BA.debugLineNum = 2245;BA.debugLine="list_location_s.Add(\"Cunalom\")";
_list_location_s.Add((Object)("Cunalom"));
 //BA.debugLineNum = 2246;BA.debugLine="list_location_s.Add(\"Tara\")";
_list_location_s.Add((Object)("Tara"));
 //BA.debugLineNum = 2247;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2248;BA.debugLine="list_location_s.Add(\"Casipungan\")";
_list_location_s.Add((Object)("Casipungan"));
 //BA.debugLineNum = 2249;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 2250;BA.debugLine="list_location_s.Add(\"Lanipga\")";
_list_location_s.Add((Object)("Lanipga"));
 //BA.debugLineNum = 2251;BA.debugLine="list_location_s.Add(\"Bulod\")";
_list_location_s.Add((Object)("Bulod"));
 //BA.debugLineNum = 2252;BA.debugLine="list_location_s.Add(\"Bonton\")";
_list_location_s.Add((Object)("Bonton"));
 //BA.debugLineNum = 2253;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 }else if(_position==10) { 
 //BA.debugLineNum = 2255;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 2256;BA.debugLine="list_location_s.Add(\"Balisong\")";
_list_location_s.Add((Object)("Balisong"));
 //BA.debugLineNum = 2257;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 2258;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 2259;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 2260;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 2261;BA.debugLine="list_location_s.Add(\"Dubdub\")";
_list_location_s.Add((Object)("Dubdub"));
 //BA.debugLineNum = 2262;BA.debugLine="list_location_s.Add(\"Hda. San jose valing\")";
_list_location_s.Add((Object)("Hda. San jose valing"));
 }else if(_position==11) { 
 //BA.debugLineNum = 2264;BA.debugLine="list_location_s.Add(\"Acapulco\")";
_list_location_s.Add((Object)("Acapulco"));
 //BA.debugLineNum = 2265;BA.debugLine="list_location_s.Add(\"Liki\")";
_list_location_s.Add((Object)("Liki"));
 //BA.debugLineNum = 2266;BA.debugLine="list_location_s.Add(\"500\")";
_list_location_s.Add((Object)("500"));
 //BA.debugLineNum = 2267;BA.debugLine="list_location_s.Add(\"Aglatong\")";
_list_location_s.Add((Object)("Aglatong"));
 //BA.debugLineNum = 2268;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2269;BA.debugLine="list_location_s.Add(\"Baptist\")";
_list_location_s.Add((Object)("Baptist"));
 }else if(_position==12) { 
 //BA.debugLineNum = 2271;BA.debugLine="list_location_s.Add(\"Lizares\")";
_list_location_s.Add((Object)("Lizares"));
 //BA.debugLineNum = 2272;BA.debugLine="list_location_s.Add(\"Pakol\")";
_list_location_s.Add((Object)("Pakol"));
 //BA.debugLineNum = 2273;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2274;BA.debugLine="list_location_s.Add(\"Lanete\")";
_list_location_s.Add((Object)("Lanete"));
 //BA.debugLineNum = 2275;BA.debugLine="list_location_s.Add(\"Kasoy\")";
_list_location_s.Add((Object)("Kasoy"));
 //BA.debugLineNum = 2276;BA.debugLine="list_location_s.Add(\"Bato\")";
_list_location_s.Add((Object)("Bato"));
 //BA.debugLineNum = 2277;BA.debugLine="list_location_s.Add(\"Frande\")";
_list_location_s.Add((Object)("Frande"));
 //BA.debugLineNum = 2278;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 2279;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 //BA.debugLineNum = 2280;BA.debugLine="list_location_s.Add(\"Culban\")";
_list_location_s.Add((Object)("Culban"));
 //BA.debugLineNum = 2281;BA.debugLine="list_location_s.Add(\"Calansi\")";
_list_location_s.Add((Object)("Calansi"));
 //BA.debugLineNum = 2282;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 2283;BA.debugLine="list_location_s.Add(\"Dama\")";
_list_location_s.Add((Object)("Dama"));
 }else if(_position==13) { 
 //BA.debugLineNum = 2285;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 2286;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 2287;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 2288;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 2289;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 2290;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 }else if(_position==14) { 
 //BA.debugLineNum = 2292;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2293;BA.debugLine="list_location_s.Add(\"Calubihan\")";
_list_location_s.Add((Object)("Calubihan"));
 //BA.debugLineNum = 2294;BA.debugLine="list_location_s.Add(\"Mapulang duta\")";
_list_location_s.Add((Object)("Mapulang duta"));
 //BA.debugLineNum = 2295;BA.debugLine="list_location_s.Add(\"Abud\")";
_list_location_s.Add((Object)("Abud"));
 //BA.debugLineNum = 2296;BA.debugLine="list_location_s.Add(\"Molo\")";
_list_location_s.Add((Object)("Molo"));
 //BA.debugLineNum = 2297;BA.debugLine="list_location_s.Add(\"Balabag\")";
_list_location_s.Add((Object)("Balabag"));
 //BA.debugLineNum = 2298;BA.debugLine="list_location_s.Add(\"Pandan\")";
_list_location_s.Add((Object)("Pandan"));
 //BA.debugLineNum = 2299;BA.debugLine="list_location_s.Add(\"Nahulop\")";
_list_location_s.Add((Object)("Nahulop"));
 //BA.debugLineNum = 2300;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 2301;BA.debugLine="list_location_s.Add(\"Aglaoa\")";
_list_location_s.Add((Object)("Aglaoa"));
 }else if(_position==15) { 
 //BA.debugLineNum = 2303;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 2304;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 2305;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 2306;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 }else if(_position==16) { 
 //BA.debugLineNum = 2308;BA.debugLine="list_location_s.Add(\"ORS\")";
_list_location_s.Add((Object)("ORS"));
 //BA.debugLineNum = 2309;BA.debugLine="list_location_s.Add(\"Aloe vera\")";
_list_location_s.Add((Object)("Aloe vera"));
 //BA.debugLineNum = 2310;BA.debugLine="list_location_s.Add(\"SCAD\")";
_list_location_s.Add((Object)("SCAD"));
 //BA.debugLineNum = 2311;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2312;BA.debugLine="list_location_s.Add(\"Sampaguita\")";
_list_location_s.Add((Object)("Sampaguita"));
 //BA.debugLineNum = 2313;BA.debugLine="list_location_s.Add(\"Bonguinvilla\")";
_list_location_s.Add((Object)("Bonguinvilla"));
 //BA.debugLineNum = 2314;BA.debugLine="list_location_s.Add(\"Cagay\")";
_list_location_s.Add((Object)("Cagay"));
 //BA.debugLineNum = 2315;BA.debugLine="list_location_s.Add(\"Naga\")";
_list_location_s.Add((Object)("Naga"));
 }else if(_position==17) { 
 //BA.debugLineNum = 2317;BA.debugLine="list_location_s.Add(\"Hda. Naval\")";
_list_location_s.Add((Object)("Hda. Naval"));
 //BA.debugLineNum = 2318;BA.debugLine="list_location_s.Add(\"Antipolo\")";
_list_location_s.Add((Object)("Antipolo"));
 //BA.debugLineNum = 2319;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 2320;BA.debugLine="list_location_s.Add(\"Punta talaban\")";
_list_location_s.Add((Object)("Punta talaban"));
 //BA.debugLineNum = 2321;BA.debugLine="list_location_s.Add(\"Batang guwaan\")";
_list_location_s.Add((Object)("Batang guwaan"));
 //BA.debugLineNum = 2322;BA.debugLine="list_location_s.Add(\"Batang sulod\")";
_list_location_s.Add((Object)("Batang sulod"));
 //BA.debugLineNum = 2323;BA.debugLine="list_location_s.Add(\"Mabini st.\")";
_list_location_s.Add((Object)("Mabini st."));
 //BA.debugLineNum = 2324;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 2325;BA.debugLine="list_location_s.Add(\"Hacienda silos\")";
_list_location_s.Add((Object)("Hacienda silos"));
 //BA.debugLineNum = 2326;BA.debugLine="list_location_s.Add(\"Lopez jeana 1\")";
_list_location_s.Add((Object)("Lopez jeana 1"));
 //BA.debugLineNum = 2327;BA.debugLine="list_location_s.Add(\"Lopez jeana 2\")";
_list_location_s.Add((Object)("Lopez jeana 2"));
 }else if(_position==18) { 
 //BA.debugLineNum = 2329;BA.debugLine="list_location_s.Add(\"Ilawod\")";
_list_location_s.Add((Object)("Ilawod"));
 //BA.debugLineNum = 2330;BA.debugLine="list_location_s.Add(\"Buhian\")";
_list_location_s.Add((Object)("Buhian"));
 //BA.debugLineNum = 2331;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 2332;BA.debugLine="list_location_s.Add(\"Mambato\")";
_list_location_s.Add((Object)("Mambato"));
 };
 //BA.debugLineNum = 2335;BA.debugLine="brgy_index = Position";
_brgy_index = _position;
 //BA.debugLineNum = 2336;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 2339;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_street_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 2340;BA.debugLine="Sub location_spin_street_ItemClick (Position As In";
 //BA.debugLineNum = 2341;BA.debugLine="street_index = Position";
_street_index = _position;
 //BA.debugLineNum = 2342;BA.debugLine="street_lat_lng";
_street_lat_lng();
 //BA.debugLineNum = 2343;BA.debugLine="End Sub";
return "";
}
public static String  _ok_vie_btn_click() throws Exception{
 //BA.debugLineNum = 3005;BA.debugLine="Sub ok_vie_btn_click";
 //BA.debugLineNum = 3006;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 3007;BA.debugLine="End Sub";
return "";
}
public static String  _phone1_view_call_click() throws Exception{
int _choose = 0;
anywheresoftware.b4a.phone.Phone.PhoneCalls _ph = null;
 //BA.debugLineNum = 3028;BA.debugLine="Sub phone1_view_call_click";
 //BA.debugLineNum = 3029;BA.debugLine="ph1_a1.Start(ph1_pnl)";
mostCurrent._ph1_a1.Start((android.view.View)(mostCurrent._ph1_pnl.getObject()));
 //BA.debugLineNum = 3030;BA.debugLine="Dim choose As Int";
_choose = 0;
 //BA.debugLineNum = 3031;BA.debugLine="choose = Msgbox2(\"\"&phone1.Text,\"Phone Number: \",";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2(""+mostCurrent._phone1.getText(),"Phone Number: ","CALL","","CANCEL",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 3032;BA.debugLine="If choose == DialogResponse .POSITIVE Then";
if (_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 3033;BA.debugLine="Dim ph As PhoneCalls";
_ph = new anywheresoftware.b4a.phone.Phone.PhoneCalls();
 //BA.debugLineNum = 3034;BA.debugLine="StartActivity(ph.Call(phone1.Text))";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_ph.Call(mostCurrent._phone1.getText())));
 }else {
 };
 //BA.debugLineNum = 3037;BA.debugLine="End Sub";
return "";
}
public static String  _phone2_view_call_click() throws Exception{
int _choose = 0;
anywheresoftware.b4a.phone.Phone.PhoneCalls _ph = null;
 //BA.debugLineNum = 3038;BA.debugLine="Sub phone2_view_call_click";
 //BA.debugLineNum = 3039;BA.debugLine="ph2_a2.Start(ph2_pnl)";
mostCurrent._ph2_a2.Start((android.view.View)(mostCurrent._ph2_pnl.getObject()));
 //BA.debugLineNum = 3040;BA.debugLine="Dim choose As Int";
_choose = 0;
 //BA.debugLineNum = 3041;BA.debugLine="choose = Msgbox2(\"\"&phone2.Text,\"Phone Number: \",";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2(""+mostCurrent._phone2.getText(),"Phone Number: ","CALL","","CANCEL",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 3042;BA.debugLine="If choose == DialogResponse .POSITIVE Then";
if (_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 3043;BA.debugLine="Dim ph As PhoneCalls";
_ph = new anywheresoftware.b4a.phone.Phone.PhoneCalls();
 //BA.debugLineNum = 3044;BA.debugLine="StartActivity(ph.Call(phone2.Text))";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_ph.Call(mostCurrent._phone2.getText())));
 }else {
 };
 //BA.debugLineNum = 3047;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_bday_body_click() throws Exception{
 //BA.debugLineNum = 1421;BA.debugLine="Sub pnl_bday_body_click";
 //BA.debugLineNum = 1423;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_blood_body_click() throws Exception{
 //BA.debugLineNum = 1487;BA.debugLine="Sub pnl_blood_body_click";
 //BA.debugLineNum = 1489;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_body_click() throws Exception{
 //BA.debugLineNum = 1581;BA.debugLine="Sub pnl_body_click";
 //BA.debugLineNum = 1583;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_donated_body_click() throws Exception{
 //BA.debugLineNum = 1335;BA.debugLine="Sub pnl_donated_body_click";
 //BA.debugLineNum = 1337;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_gender_body_click() throws Exception{
 //BA.debugLineNum = 1167;BA.debugLine="Sub pnl_gender_body_click";
 //BA.debugLineNum = 1169;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim bookmark_id_list As List";
_bookmark_id_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 10;BA.debugLine="Dim list_all_info As List";
_list_all_info = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 11;BA.debugLine="Dim list_bloodgroup As List";
_list_bloodgroup = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 12;BA.debugLine="Dim list_donated As List";
_list_donated = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 13;BA.debugLine="Dim list_is_gender As List";
_list_is_gender = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Dim list_day,list_month,list_year As List";
_list_day = new anywheresoftware.b4a.objects.collections.List();
_list_month = new anywheresoftware.b4a.objects.collections.List();
_list_year = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 15;BA.debugLine="Dim users_string_login As String";
_users_string_login = "";
 //BA.debugLineNum = 17;BA.debugLine="Dim blood_selected As String : blood_selected = \"";
_blood_selected = "";
 //BA.debugLineNum = 17;BA.debugLine="Dim blood_selected As String : blood_selected = \"";
_blood_selected = "A+";
 //BA.debugLineNum = 18;BA.debugLine="Dim bday_day_selected As String : bday_day_select";
_bday_day_selected = "";
 //BA.debugLineNum = 18;BA.debugLine="Dim bday_day_selected As String : bday_day_select";
_bday_day_selected = "1";
 //BA.debugLineNum = 19;BA.debugLine="Dim bday_month_selected As String : bday_month_se";
_bday_month_selected = "";
 //BA.debugLineNum = 19;BA.debugLine="Dim bday_month_selected As String : bday_month_se";
_bday_month_selected = "1";
 //BA.debugLineNum = 20;BA.debugLine="Dim bday_year_selected As String : bday_year_sele";
_bday_year_selected = "";
 //BA.debugLineNum = 20;BA.debugLine="Dim bday_year_selected As String : bday_year_sele";
_bday_year_selected = BA.NumberToString(anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow()));
 //BA.debugLineNum = 21;BA.debugLine="Dim location_brgy_selected As String : location_b";
_location_brgy_selected = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim location_brgy_selected As String : location_b";
_location_brgy_selected = "Brgy 1";
 //BA.debugLineNum = 22;BA.debugLine="Dim location_street_selected As String : location";
_location_street_selected = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim location_street_selected As String : location";
_location_street_selected = "Rizal St.";
 //BA.debugLineNum = 23;BA.debugLine="Dim gender_string_data As String : gender_string_";
_gender_string_data = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim gender_string_data As String : gender_string_";
_gender_string_data = "Male";
 //BA.debugLineNum = 24;BA.debugLine="Dim is_donated As String : is_donated = \"Yes\"";
_is_donated = "";
 //BA.debugLineNum = 24;BA.debugLine="Dim is_donated As String : is_donated = \"Yes\"";
_is_donated = "Yes";
 //BA.debugLineNum = 25;BA.debugLine="Dim donated_index As Int : donated_index = 0";
_donated_index = 0;
 //BA.debugLineNum = 25;BA.debugLine="Dim donated_index As Int : donated_index = 0";
_donated_index = (int) (0);
 //BA.debugLineNum = 26;BA.debugLine="Dim is_gender_index As Int : is_gender_index = 0";
_is_gender_index = 0;
 //BA.debugLineNum = 26;BA.debugLine="Dim is_gender_index As Int : is_gender_index = 0";
_is_gender_index = (int) (0);
 //BA.debugLineNum = 27;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "";
 //BA.debugLineNum = 27;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 28;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "";
 //BA.debugLineNum = 28;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "122.869168";
 //BA.debugLineNum = 29;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = 0;
 //BA.debugLineNum = 29;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = (int) (0);
 //BA.debugLineNum = 30;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = 0;
 //BA.debugLineNum = 30;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = (int) (0);
 //BA.debugLineNum = 31;BA.debugLine="Dim list_location_b,list_location_s,list_location";
_list_location_b = new anywheresoftware.b4a.objects.collections.List();
_list_location_s = new anywheresoftware.b4a.objects.collections.List();
_list_location_p = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 32;BA.debugLine="Dim optionSelected As String";
_optionselected = "";
 //BA.debugLineNum = 34;BA.debugLine="Dim isDonateDate As String";
_isdonatedate = "";
 //BA.debugLineNum = 35;BA.debugLine="Dim donate_m_pos,donate_d_pos,donate_y_pos As Int";
_donate_m_pos = 0;
_donate_d_pos = 0;
_donate_y_pos = 0;
 //BA.debugLineNum = 36;BA.debugLine="Private image_container As String";
_image_container = "";
 //BA.debugLineNum = 37;BA.debugLine="Private panel_click_ As Int : panel_click_ = 0";
_panel_click_ = 0;
 //BA.debugLineNum = 37;BA.debugLine="Private panel_click_ As Int : panel_click_ = 0";
_panel_click_ = (int) (0);
 //BA.debugLineNum = 38;BA.debugLine="Private edit_panel_click_ As Int : edit_panel_cli";
_edit_panel_click_ = 0;
 //BA.debugLineNum = 38;BA.debugLine="Private edit_panel_click_ As Int : edit_panel_cli";
_edit_panel_click_ = (int) (0);
 //BA.debugLineNum = 40;BA.debugLine="Dim sqlLite As SQL";
_sqllite = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 41;BA.debugLine="database_init";
_database_init();
 //BA.debugLineNum = 42;BA.debugLine="Dim row_click As Int";
_row_click = 0;
 //BA.debugLineNum = 43;BA.debugLine="Dim item As Int";
_item = 0;
 //BA.debugLineNum = 44;BA.debugLine="Dim list_all_select As Int";
_list_all_select = 0;
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static String  _profile_all_body_click() throws Exception{
 //BA.debugLineNum = 684;BA.debugLine="Sub profile_all_body_click";
 //BA.debugLineNum = 686;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 566;BA.debugLine="Sub profile_Click";
 //BA.debugLineNum = 567;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 568;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 569;BA.debugLine="profile_img.Tag = aa1";
mostCurrent._profile_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 570;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 571;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 572;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 573;BA.debugLine="a2.Start(profile)";
mostCurrent._a2.Start((android.view.View)(mostCurrent._profile.getObject()));
 //BA.debugLineNum = 574;BA.debugLine="aa1.Start(profile_img)";
_aa1.Start((android.view.View)(mostCurrent._profile_img.getObject()));
 //BA.debugLineNum = 576;BA.debugLine="ProgressDialogShow2(\"Please Wait..\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Please Wait..",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 577;BA.debugLine="panel_click_ = 1";
_panel_click_ = (int) (1);
 //BA.debugLineNum = 578;BA.debugLine="edit_panel_click_ = 0";
_edit_panel_click_ = (int) (0);
 //BA.debugLineNum = 579;BA.debugLine="optionSelected = \"pofileView\"";
_optionselected = "pofileView";
 //BA.debugLineNum = 580;BA.debugLine="Dim TextWriters As TextWriter";
_textwriters = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 581;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"users_all_info.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 583;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 584;BA.debugLine="Dim update_top_pnl As Panel";
_update_top_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 585;BA.debugLine="update_top_pnl.Initialize(\"update_top_pnl\")";
_update_top_pnl.Initialize(mostCurrent.activityBA,"update_top_pnl");
 //BA.debugLineNum = 586;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 587;BA.debugLine="Dim all_users_info As String";
_all_users_info = "";
 //BA.debugLineNum = 588;BA.debugLine="all_inputs_down.Initialize(\"all_inputs_down\")";
mostCurrent._all_inputs_down.Initialize(mostCurrent.activityBA,"all_inputs_down");
 //BA.debugLineNum = 589;BA.debugLine="profile_all_body.Initialize(\"profile_all_body\")";
mostCurrent._profile_all_body.Initialize(mostCurrent.activityBA,"profile_all_body");
 //BA.debugLineNum = 591;BA.debugLine="all_info_query.Initialize(\"all_info_query\",Me)";
mostCurrent._all_info_query._initialize(processBA,"all_info_query",menu_form.getObject());
 //BA.debugLineNum = 592;BA.debugLine="all_users_info = url_back.php_email_url(\"search_a";
_all_users_info = _url_back._php_email_url("search_all_users_data.php");
 //BA.debugLineNum = 593;BA.debugLine="all_info_query.Download2(all_users_info,Array As";
mostCurrent._all_info_query._download2(_all_users_info,new String[]{"all_info","SELECT * FROM `person_info` where `id`='"+mostCurrent._login_form._id_query+"';"});
 //BA.debugLineNum = 600;BA.debugLine="scroll_myprof.Initialize(90%x,73%y,\"scroll_mypro";
mostCurrent._scroll_myprof.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (73),mostCurrent.activityBA),"scroll_myprof");
 //BA.debugLineNum = 601;BA.debugLine="scroll_myprof.Panel.LoadLayout(\"update_all_input";
mostCurrent._scroll_myprof.getPanel().LoadLayout("update_all_inputs",mostCurrent.activityBA);
 //BA.debugLineNum = 602;BA.debugLine="scroll_myprof.Color = Colors.Transparent";
mostCurrent._scroll_myprof.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 603;BA.debugLine="scroll_myprof.ScrollbarsVisibility(False,False)";
mostCurrent._scroll_myprof.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 604;BA.debugLine="update_top_pnl.LoadLayout(\"update_all_top\")";
_update_top_pnl.LoadLayout("update_all_top",mostCurrent.activityBA);
 //BA.debugLineNum = 606;BA.debugLine="scroll_myprof.SetBackgroundImage(LoadBitmap(File";
mostCurrent._scroll_myprof.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 607;BA.debugLine="all_inputs.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._all_inputs.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 608;BA.debugLine="all_inputs_top.SetBackgroundImage(LoadBitmap(Fil";
mostCurrent._all_inputs_top.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 609;BA.debugLine="all_inputs_down.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._all_inputs_down.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 610;BA.debugLine="lab_fullname.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._lab_fullname.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-4-user.png").getObject()));
 //BA.debugLineNum = 611;BA.debugLine="lab_bloodgroup.SetBackgroundImage(LoadBitmap(Fil";
mostCurrent._lab_bloodgroup.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-93-tint.png").getObject()));
 //BA.debugLineNum = 612;BA.debugLine="lab_email.SetBackgroundImage(LoadBitmap(File.Dir";
mostCurrent._lab_email.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-social-40-e-mail.png").getObject()));
 //BA.debugLineNum = 613;BA.debugLine="lab_phonenumber.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._lab_phonenumber.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt1.png").getObject()));
 //BA.debugLineNum = 614;BA.debugLine="lab_phonenumber2.SetBackgroundImage(LoadBitmap(F";
mostCurrent._lab_phonenumber2.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt2.png").getObject()));
 //BA.debugLineNum = 615;BA.debugLine="lab_location.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._lab_location.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-21-home.png").getObject()));
 //BA.debugLineNum = 616;BA.debugLine="lab_question.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._lab_question.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-353-nameplate.png").getObject()));
 //BA.debugLineNum = 617;BA.debugLine="lab_donate_confirm.SetBackgroundImage(LoadBitmap";
mostCurrent._lab_donate_confirm.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-152-new-window.png").getObject()));
 //BA.debugLineNum = 618;BA.debugLine="lab_bday.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._lab_bday.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-46-calendar.png").getObject()));
 //BA.debugLineNum = 619;BA.debugLine="lab_gender.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._lab_gender.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-25-parents.png").getObject()));
 //BA.debugLineNum = 621;BA.debugLine="all_inputs_top.Width = 90%x";
mostCurrent._all_inputs_top.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 622;BA.debugLine="all_inputs_top.Height = 32%y";
mostCurrent._all_inputs_top.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (32),mostCurrent.activityBA));
 //BA.debugLineNum = 623;BA.debugLine="tittle.SetLayout(1%x,1%y,88%x,9%y)";
mostCurrent._tittle.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (88),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 624;BA.debugLine="usr_img.SetLayout(((88%x/2)/2)+3%x,tittle.Top+t";
mostCurrent._usr_img.SetLayout((int) (((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (88),mostCurrent.activityBA)/(double)2)/(double)2)+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (3),mostCurrent.activityBA)),(int) (mostCurrent._tittle.getTop()+mostCurrent._tittle.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (19),mostCurrent.activityBA));
 //BA.debugLineNum = 626;BA.debugLine="all_inputs.Width = 90%x";
mostCurrent._all_inputs.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 627;BA.debugLine="all_inputs.Height = 73%y";
mostCurrent._all_inputs.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (73),mostCurrent.activityBA));
 //BA.debugLineNum = 628;BA.debugLine="lab_fullname.SetLayout(2%x,2%y,7%x,6%y)";
mostCurrent._lab_fullname.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 629;BA.debugLine="lab_bloodgroup.SetLayout(2%x,lab_fullname.Top+";
mostCurrent._lab_bloodgroup.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_fullname.getTop()+mostCurrent._lab_fullname.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 630;BA.debugLine="lab_email.SetLayout(2%x,lab_bloodgroup.Top+lab";
mostCurrent._lab_email.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_bloodgroup.getTop()+mostCurrent._lab_bloodgroup.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 631;BA.debugLine="lab_phonenumber.SetLayout(2%x,lab_email.Top+la";
mostCurrent._lab_phonenumber.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_email.getTop()+mostCurrent._lab_email.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 632;BA.debugLine="lab_phonenumber2.SetLayout(2%x,lab_phonenumber";
mostCurrent._lab_phonenumber2.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_phonenumber.getTop()+mostCurrent._lab_phonenumber.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 633;BA.debugLine="lab_bday.SetLayout(2%x,lab_phonenumber2.Top+la";
mostCurrent._lab_bday.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_phonenumber2.getTop()+mostCurrent._lab_phonenumber2.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 634;BA.debugLine="lab_location.SetLayout(2%x,lab_bday.Top+lab_bd";
mostCurrent._lab_location.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_bday.getTop()+mostCurrent._lab_bday.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 635;BA.debugLine="lab_question.SetLayout(2%x,lab_location.Top+la";
mostCurrent._lab_question.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_location.getTop()+mostCurrent._lab_location.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 636;BA.debugLine="lab_donate_confirm.SetLayout(2%x,lab_question.";
mostCurrent._lab_donate_confirm.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_question.getTop()+mostCurrent._lab_question.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 637;BA.debugLine="lab_gender.SetLayout(2%x,lab_donate_confirm.To";
mostCurrent._lab_gender.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._lab_donate_confirm.getTop()+mostCurrent._lab_donate_confirm.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 639;BA.debugLine="text_fn.SetLayout(lab_fullname.Left+lab_fullnam";
mostCurrent._text_fn.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 640;BA.debugLine="text_blood.SetLayout(lab_fullname.Left+lab_full";
mostCurrent._text_blood.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_bloodgroup.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 641;BA.debugLine="text_email.SetLayout(lab_fullname.Left+lab_full";
mostCurrent._text_email.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_email.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 642;BA.debugLine="text_phonenumber.SetLayout(lab_fullname.Left+la";
mostCurrent._text_phonenumber.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_phonenumber.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 643;BA.debugLine="text_phonenumber2.SetLayout(lab_fullname.Left+l";
mostCurrent._text_phonenumber2.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_phonenumber2.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 644;BA.debugLine="text_bday.SetLayout(lab_fullname.Left+lab_fulln";
mostCurrent._text_bday.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_bday.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 645;BA.debugLine="text_location.SetLayout(lab_fullname.Left+lab_f";
mostCurrent._text_location.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_location.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 646;BA.debugLine="text_answer.SetLayout(lab_fullname.Left+lab_ful";
mostCurrent._text_answer.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_question.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 647;BA.debugLine="text_donated.SetLayout(lab_fullname.Left+lab_fu";
mostCurrent._text_donated.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_donate_confirm.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 648;BA.debugLine="text_gender.SetLayout(lab_fullname.Left+lab_ful";
mostCurrent._text_gender.SetLayout((int) (mostCurrent._lab_fullname.getLeft()+mostCurrent._lab_fullname.getWidth()),mostCurrent._lab_gender.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (69),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 650;BA.debugLine="blood_edit.SetLayout(text_blood.Left+text_bloo";
mostCurrent._blood_edit.SetLayout((int) (mostCurrent._text_blood.getLeft()+mostCurrent._text_blood.getWidth()),mostCurrent._text_blood.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 651;BA.debugLine="bday_edit.SetLayout(text_bday.Left+text_bday.W";
mostCurrent._bday_edit.SetLayout((int) (mostCurrent._text_bday.getLeft()+mostCurrent._text_bday.getWidth()),mostCurrent._text_bday.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 652;BA.debugLine="locate_edit.SetLayout(text_location.Left+text_";
mostCurrent._locate_edit.SetLayout((int) (mostCurrent._text_location.getLeft()+mostCurrent._text_location.getWidth()),mostCurrent._text_location.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 653;BA.debugLine="donated_edit.SetLayout(text_donated.Left+text_";
mostCurrent._donated_edit.SetLayout((int) (mostCurrent._text_donated.getLeft()+mostCurrent._text_donated.getWidth()),mostCurrent._text_donated.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 654;BA.debugLine="edit_gender.SetLayout(text_gender.Left+text_ge";
mostCurrent._edit_gender.SetLayout((int) (mostCurrent._text_gender.getLeft()+mostCurrent._text_gender.getWidth()),mostCurrent._text_gender.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 656;BA.debugLine="update_btn.Initialize(\"update_btn\")";
mostCurrent._update_btn.Initialize(mostCurrent.activityBA,"update_btn");
 //BA.debugLineNum = 657;BA.debugLine="cancel_btn.Initialize(\"cancel_btn\")";
mostCurrent._cancel_btn.Initialize(mostCurrent.activityBA,"cancel_btn");
 //BA.debugLineNum = 658;BA.debugLine="update_btn.Text = \"UPDATE\"";
mostCurrent._update_btn.setText((Object)("UPDATE"));
 //BA.debugLineNum = 659;BA.debugLine="cancel_btn.Text = \"CANCEL\"";
mostCurrent._cancel_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 660;BA.debugLine="all_inputs_down.AddView(update_btn,6%x,1%y,35%";
mostCurrent._all_inputs_down.AddView((android.view.View)(mostCurrent._update_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (35),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 661;BA.debugLine="all_inputs_down.AddView(cancel_btn,update_btn.";
mostCurrent._all_inputs_down.AddView((android.view.View)(mostCurrent._cancel_btn.getObject()),(int) (mostCurrent._update_btn.getLeft()+mostCurrent._update_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (35),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 663;BA.debugLine="profile_all_body.Color = Colors.ARGB(128,128,128";
mostCurrent._profile_all_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (128),(int) (128),(int) (.50)));
 //BA.debugLineNum = 665;BA.debugLine="profile_all_body.AddView(update_top_pnl,5%x,2%y,";
mostCurrent._profile_all_body.AddView((android.view.View)(_update_top_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 666;BA.debugLine="profile_all_body.AddView(scroll_myprof,5%x,all_i";
mostCurrent._profile_all_body.AddView((android.view.View)(mostCurrent._scroll_myprof.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (mostCurrent._all_inputs_top.getTop()+mostCurrent._all_inputs_top.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (47),mostCurrent.activityBA));
 //BA.debugLineNum = 667;BA.debugLine="profile_all_body.AddView(all_inputs_down,5%x,scr";
mostCurrent._profile_all_body.AddView((android.view.View)(mostCurrent._all_inputs_down.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (mostCurrent._scroll_myprof.getTop()+mostCurrent._scroll_myprof.getHeight()-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (13),mostCurrent.activityBA));
 //BA.debugLineNum = 668;BA.debugLine="Activity.AddView(profile_all_body,0,0,100%x,100%";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._profile_all_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 670;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 671;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 672;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 673;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 674;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 675;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 676;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 677;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 678;BA.debugLine="update_btn.Background = V_btn";
mostCurrent._update_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 679;BA.debugLine="cancel_btn.Background = C_btn";
mostCurrent._cancel_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 680;BA.debugLine="End Sub";
return "";
}
public static String  _profiled_click() throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _title = null;
anywheresoftware.b4a.objects.EditTextWrapper _fulln = null;
anywheresoftware.b4a.objects.LabelWrapper _blood_sel = null;
anywheresoftware.b4a.objects.EditTextWrapper _email = null;
anywheresoftware.b4a.objects.EditTextWrapper _phone11 = null;
anywheresoftware.b4a.objects.EditTextWrapper _phone22 = null;
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
 //BA.debugLineNum = 888;BA.debugLine="Sub profiled_Click";
 //BA.debugLineNum = 890;BA.debugLine="profile_panel.Initialize(\"profile_panel\")";
mostCurrent._profile_panel.Initialize(mostCurrent.activityBA,"profile_panel");
 //BA.debugLineNum = 892;BA.debugLine="profile_panel.SetBackgroundImage(LoadBitmap(File";
mostCurrent._profile_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 893;BA.debugLine="Dim title As Label : title.Initialize(\"\")";
_title = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 893;BA.debugLine="Dim title As Label : title.Initialize(\"\")";
_title.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 894;BA.debugLine="Dim fullN As EditText : fullN.Initialize(\"\")";
_fulln = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 894;BA.debugLine="Dim fullN As EditText : fullN.Initialize(\"\")";
_fulln.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 895;BA.debugLine="Dim blood_sel As Label : blood_sel.Initialize(\"\"";
_blood_sel = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 895;BA.debugLine="Dim blood_sel As Label : blood_sel.Initialize(\"\"";
_blood_sel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 896;BA.debugLine="Dim email As EditText : email.Initialize(\"\")";
_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 896;BA.debugLine="Dim email As EditText : email.Initialize(\"\")";
_email.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 897;BA.debugLine="Dim phone11 As EditText : phone11.Initialize(\"\")";
_phone11 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 897;BA.debugLine="Dim phone11 As EditText : phone11.Initialize(\"\")";
_phone11.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 898;BA.debugLine="Dim phone22 As EditText : phone22.Initialize(\"\")";
_phone22 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 898;BA.debugLine="Dim phone22 As EditText : phone22.Initialize(\"\")";
_phone22.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 899;BA.debugLine="Dim location As Label : location.Initialize(\"\")";
_location = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 899;BA.debugLine="Dim location As Label : location.Initialize(\"\")";
_location.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 900;BA.debugLine="Dim bday As Label : bday.Initialize(\"\")";
_bday = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 900;BA.debugLine="Dim bday As Label : bday.Initialize(\"\")";
_bday.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 901;BA.debugLine="Dim nickN As EditText : nickN.Initialize(\"\")";
_nickn = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 901;BA.debugLine="Dim nickN As EditText : nickN.Initialize(\"\")";
_nickn.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 902;BA.debugLine="Dim isDonated As Label : isDonated.Initialize(\"\"";
_isdonated = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 902;BA.debugLine="Dim isDonated As Label : isDonated.Initialize(\"\"";
_isdonated.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 903;BA.debugLine="Dim img_fullN As ImageView : img_fullN.Initiali";
_img_fulln = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 903;BA.debugLine="Dim img_fullN As ImageView : img_fullN.Initiali";
_img_fulln.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 904;BA.debugLine="Dim img_blood_sel As ImageView : img_blood_sel.";
_img_blood_sel = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 904;BA.debugLine="Dim img_blood_sel As ImageView : img_blood_sel.";
_img_blood_sel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 905;BA.debugLine="Dim img_email As ImageView : img_email.Initiali";
_img_email = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 905;BA.debugLine="Dim img_email As ImageView : img_email.Initiali";
_img_email.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 906;BA.debugLine="Dim img_phone1 As ImageView : img_phone1.Initia";
_img_phone1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 906;BA.debugLine="Dim img_phone1 As ImageView : img_phone1.Initia";
_img_phone1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 907;BA.debugLine="Dim img_phone2 As ImageView : img_phone2.Initia";
_img_phone2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 907;BA.debugLine="Dim img_phone2 As ImageView : img_phone2.Initia";
_img_phone2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 908;BA.debugLine="Dim img_location As ImageView : img_location.In";
_img_location = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 908;BA.debugLine="Dim img_location As ImageView : img_location.In";
_img_location.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 909;BA.debugLine="Dim img_bday As ImageView : img_bday.Initialize";
_img_bday = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 909;BA.debugLine="Dim img_bday As ImageView : img_bday.Initialize";
_img_bday.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 910;BA.debugLine="Dim img_nickN As ImageView : img_nickN.Initiali";
_img_nickn = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 910;BA.debugLine="Dim img_nickN As ImageView : img_nickN.Initiali";
_img_nickn.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 911;BA.debugLine="Dim img_isDonated As ImageView : img_isDonated.";
_img_isdonated = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 911;BA.debugLine="Dim img_isDonated As ImageView : img_isDonated.";
_img_isdonated.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 913;BA.debugLine="title.Text = \"My Information\" '' titte";
_title.setText((Object)("My Information"));
 //BA.debugLineNum = 914;BA.debugLine="title.Typeface = Typeface.LoadFromAssets(\"HipH";
_title.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 915;BA.debugLine="title.Gravity = Gravity.CENTER";
_title.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 916;BA.debugLine="title.TextSize = 20 '''-------";
_title.setTextSize((float) (20));
 //BA.debugLineNum = 918;BA.debugLine="blood_sel.Text = \"A+\"";
_blood_sel.setText((Object)("A+"));
 //BA.debugLineNum = 919;BA.debugLine="bday.Text = \"may/13/1993\"";
_bday.setText((Object)("may/13/1993"));
 //BA.debugLineNum = 920;BA.debugLine="location.Text = \"hinigaran neg occ\"";
_location.setText((Object)("hinigaran neg occ"));
 //BA.debugLineNum = 921;BA.debugLine="img_fullN.SetBackgroundImage(LoadBitmap(File.Di";
_img_fulln.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-4-user.png").getObject()));
 //BA.debugLineNum = 922;BA.debugLine="img_blood_sel.SetBackgroundImage(LoadBitmap(Fil";
_img_blood_sel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-93-tint.png").getObject()));
 //BA.debugLineNum = 923;BA.debugLine="img_email.SetBackgroundImage(LoadBitmap(File.Di";
_img_email.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-social-40-e-mail.png").getObject()));
 //BA.debugLineNum = 924;BA.debugLine="img_phone1.SetBackgroundImage(LoadBitmap(File.D";
_img_phone1.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt1.png").getObject()));
 //BA.debugLineNum = 925;BA.debugLine="img_phone2.SetBackgroundImage(LoadBitmap(File.D";
_img_phone2.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt2.png").getObject()));
 //BA.debugLineNum = 926;BA.debugLine="img_location.SetBackgroundImage(LoadBitmap(File";
_img_location.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-21-home.png").getObject()));
 //BA.debugLineNum = 927;BA.debugLine="img_bday.SetBackgroundImage(LoadBitmap(File.Dir";
_img_bday.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-40-notes.png").getObject()));
 //BA.debugLineNum = 928;BA.debugLine="img_nickN.SetBackgroundImage(LoadBitmap(File.Di";
_img_nickn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-265-vcard.png").getObject()));
 //BA.debugLineNum = 929;BA.debugLine="img_isDonated.SetBackgroundImage(LoadBitmap(Fil";
_img_isdonated.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-91-eyedropper.png").getObject()));
 //BA.debugLineNum = 932;BA.debugLine="profile_panel.AddView(title,0,1%y,90%x,8%y) '";
mostCurrent._profile_panel.AddView((android.view.View)(_title.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 933;BA.debugLine="profile_panel.AddView(img_fullN,5%x, title.Top+t";
mostCurrent._profile_panel.AddView((android.view.View)(_img_fulln.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_title.getTop()+_title.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 934;BA.debugLine="profile_panel.AddView(fullN, img_fullN.Left+img_";
mostCurrent._profile_panel.AddView((android.view.View)(_fulln.getObject()),(int) (_img_fulln.getLeft()+_img_fulln.getWidth()),(int) (_title.getTop()+_title.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 936;BA.debugLine="profile_panel.AddView(img_blood_sel,5%x,fullN.To";
mostCurrent._profile_panel.AddView((android.view.View)(_img_blood_sel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_fulln.getTop()+_fulln.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 937;BA.debugLine="profile_panel.AddView(blood_sel,img_blood_sel.Le";
mostCurrent._profile_panel.AddView((android.view.View)(_blood_sel.getObject()),(int) (_img_blood_sel.getLeft()+_img_blood_sel.getWidth()),_img_blood_sel.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 939;BA.debugLine="profile_panel.AddView(img_email,5%x,img_blood_se";
mostCurrent._profile_panel.AddView((android.view.View)(_img_email.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_blood_sel.getTop()+_img_blood_sel.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 940;BA.debugLine="profile_panel.AddView(email,img_email.Left+img_e";
mostCurrent._profile_panel.AddView((android.view.View)(_email.getObject()),(int) (_img_email.getLeft()+_img_email.getWidth()),_img_email.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 942;BA.debugLine="profile_panel.AddView(img_phone1,5%x,img_email.T";
mostCurrent._profile_panel.AddView((android.view.View)(_img_phone1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_email.getTop()+_img_email.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 943;BA.debugLine="profile_panel.AddView(phone11,img_phone1.Left+im";
mostCurrent._profile_panel.AddView((android.view.View)(_phone11.getObject()),(int) (_img_phone1.getLeft()+_img_phone1.getWidth()),_img_phone1.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 945;BA.debugLine="profile_panel.AddView(img_phone2,5%x,img_phone1.";
mostCurrent._profile_panel.AddView((android.view.View)(_img_phone2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_phone1.getTop()+_img_phone1.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 946;BA.debugLine="profile_panel.AddView(phone22,img_phone2.Left+im";
mostCurrent._profile_panel.AddView((android.view.View)(_phone22.getObject()),(int) (_img_phone2.getLeft()+_img_phone2.getWidth()),_img_phone2.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 948;BA.debugLine="profile_panel.AddView(img_location,5%x,img_phone";
mostCurrent._profile_panel.AddView((android.view.View)(_img_location.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_phone2.getTop()+_img_phone2.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 949;BA.debugLine="profile_panel.AddView(location,img_location.Left";
mostCurrent._profile_panel.AddView((android.view.View)(_location.getObject()),(int) (_img_location.getLeft()+_img_location.getWidth()),_img_location.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 951;BA.debugLine="profile_panel.AddView(img_bday,5%x,img_location.";
mostCurrent._profile_panel.AddView((android.view.View)(_img_bday.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_location.getTop()+_img_location.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 952;BA.debugLine="profile_panel.AddView(bday,img_bday.Left+img_bda";
mostCurrent._profile_panel.AddView((android.view.View)(_bday.getObject()),(int) (_img_bday.getLeft()+_img_bday.getWidth()),_img_bday.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 954;BA.debugLine="profile_panel.AddView(img_nickN,5%x,img_bday.Top";
mostCurrent._profile_panel.AddView((android.view.View)(_img_nickn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_bday.getTop()+_img_bday.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 955;BA.debugLine="profile_panel.AddView(nickN,img_nickN.Left+img_n";
mostCurrent._profile_panel.AddView((android.view.View)(_nickn.getObject()),(int) (_img_nickn.getLeft()+_img_nickn.getWidth()),_img_nickn.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 957;BA.debugLine="profile_panel.AddView(img_isDonated,5%x,img_nick";
mostCurrent._profile_panel.AddView((android.view.View)(_img_isdonated.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_img_nickn.getTop()+_img_nickn.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 958;BA.debugLine="profile_panel.AddView(isDonated,img_isDonated.Le";
mostCurrent._profile_panel.AddView((android.view.View)(_isdonated.getObject()),(int) (_img_isdonated.getLeft()+_img_isdonated.getWidth()),_img_isdonated.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 961;BA.debugLine="scroll_profile_pnl.Panel.AddView(profile_panel,0";
mostCurrent._scroll_profile_pnl.getPanel().AddView((android.view.View)(mostCurrent._profile_panel.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 962;BA.debugLine="Activity.AddView(scroll_profile_pnl,5%x,3%y,90%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._scroll_profile_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 964;BA.debugLine="End Sub";
return "";
}
public static anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _resizebitmap(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _original,int _width,int _height) throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _new = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper _c = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper _destrect = null;
 //BA.debugLineNum = 845;BA.debugLine="Sub ResizeBitmap(original As Bitmap, width As Int,";
 //BA.debugLineNum = 846;BA.debugLine="Dim new As Bitmap";
_new = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 847;BA.debugLine="new.InitializeMutable(width, height)";
_new.InitializeMutable(_width,_height);
 //BA.debugLineNum = 848;BA.debugLine="Dim c As Canvas";
_c = new anywheresoftware.b4a.objects.drawable.CanvasWrapper();
 //BA.debugLineNum = 849;BA.debugLine="c.Initialize2(new)";
_c.Initialize2((android.graphics.Bitmap)(_new.getObject()));
 //BA.debugLineNum = 850;BA.debugLine="Dim destRect As Rect";
_destrect = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.RectWrapper();
 //BA.debugLineNum = 851;BA.debugLine="destRect.Initialize(0, 0, width, height)";
_destrect.Initialize((int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 852;BA.debugLine="c.DrawBitmap(original, Null, destRect)";
_c.DrawBitmap((android.graphics.Bitmap)(_original.getObject()),(android.graphics.Rect)(anywheresoftware.b4a.keywords.Common.Null),(android.graphics.Rect)(_destrect.getObject()));
 //BA.debugLineNum = 853;BA.debugLine="Return new";
if (true) return _new;
 //BA.debugLineNum = 854;BA.debugLine="End Sub";
return null;
}
public static String  _search_blood_click() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper _aa1 = null;
 //BA.debugLineNum = 555;BA.debugLine="Sub search_blood_Click";
 //BA.debugLineNum = 556;BA.debugLine="Dim aa1 As Animation";
_aa1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 557;BA.debugLine="aa1.InitializeAlpha(\"\", 1, 0)";
_aa1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 558;BA.debugLine="srch_blood_img.Tag = aa1";
mostCurrent._srch_blood_img.setTag((Object)(_aa1.getObject()));
 //BA.debugLineNum = 559;BA.debugLine="aa1.Duration = 200";
_aa1.setDuration((long) (200));
 //BA.debugLineNum = 560;BA.debugLine="aa1.RepeatCount = 1";
_aa1.setRepeatCount((int) (1));
 //BA.debugLineNum = 561;BA.debugLine="aa1.RepeatMode = aa1.REPEAT_REVERSE";
_aa1.setRepeatMode(_aa1.REPEAT_REVERSE);
 //BA.debugLineNum = 562;BA.debugLine="a1.Start(search_blood)";
mostCurrent._a1.Start((android.view.View)(mostCurrent._search_blood.getObject()));
 //BA.debugLineNum = 563;BA.debugLine="aa1.Start(srch_blood_img)";
_aa1.Start((android.view.View)(mostCurrent._srch_blood_img.getObject()));
 //BA.debugLineNum = 564;BA.debugLine="StartActivity(\"search_frame\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("search_frame"));
 //BA.debugLineNum = 565;BA.debugLine="End Sub";
return "";
}
public static String  _spin_bloodgroup_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1490;BA.debugLine="Sub spin_bloodgroup_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 1491;BA.debugLine="blood_selected = Value";
_blood_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 1492;BA.debugLine="End Sub";
return "";
}
public static String  _spin_day_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1401;BA.debugLine="Sub spin_day_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 1402;BA.debugLine="bday_day_selected = Value";
_bday_day_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 1404;BA.debugLine="End Sub";
return "";
}
public static String  _spin_donated_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1215;BA.debugLine="Sub spin_donated_ItemClick (Position As Int, Value";
 //BA.debugLineNum = 1217;BA.debugLine="is_donated = Value";
_is_donated = BA.ObjectToString(_value);
 //BA.debugLineNum = 1218;BA.debugLine="donated_index = Position";
_donated_index = _position;
 //BA.debugLineNum = 1219;BA.debugLine="End Sub";
return "";
}
public static String  _spin_gender_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1155;BA.debugLine="Sub spin_gender_ItemClick (Position As Int, Value";
 //BA.debugLineNum = 1156;BA.debugLine="is_gender_index = Position";
_is_gender_index = _position;
 //BA.debugLineNum = 1157;BA.debugLine="End Sub";
return "";
}
public static String  _spin_month_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1405;BA.debugLine="Sub spin_month_ItemClick (Position As Int, Value A";
 //BA.debugLineNum = 1406;BA.debugLine="bday_month_selected = Value";
_bday_month_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 1408;BA.debugLine="End Sub";
return "";
}
public static String  _spin_year_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1409;BA.debugLine="Sub spin_year_ItemClick (Position As Int, Value As";
 //BA.debugLineNum = 1410;BA.debugLine="bday_year_selected =  Value";
_bday_year_selected = BA.ObjectToString(_value);
 //BA.debugLineNum = 1412;BA.debugLine="End Sub";
return "";
}
public static String  _street_lat_lng() throws Exception{
 //BA.debugLineNum = 1588;BA.debugLine="Sub street_lat_lng";
 //BA.debugLineNum = 1589;BA.debugLine="If brgy_index == 0 And street_index == 0 Then";
if (_brgy_index==0 && _street_index==0) { 
 //BA.debugLineNum = 1590;BA.debugLine="lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 1591;BA.debugLine="lng = \"122.869168\"";
_lng = "122.869168";
 }else if(_brgy_index==0 && _street_index==1) { 
 //BA.debugLineNum = 1593;BA.debugLine="lat = \"10.097226\"";
_lat = "10.097226";
 //BA.debugLineNum = 1594;BA.debugLine="lng = \"122.870659\"";
_lng = "122.870659";
 }else if(_brgy_index==0 && _street_index==2) { 
 //BA.debugLineNum = 1596;BA.debugLine="lat = \"10.097711\"";
_lat = "10.097711";
 //BA.debugLineNum = 1597;BA.debugLine="lng = \"122.868378\"";
_lng = "122.868378";
 }else if(_brgy_index==0 && _street_index==3) { 
 //BA.debugLineNum = 1599;BA.debugLine="lat = \"10.098293\"";
_lat = "10.098293";
 //BA.debugLineNum = 1600;BA.debugLine="lng = \"122.868977\"";
_lng = "122.868977";
 }else if(_brgy_index==0 && _street_index==4) { 
 //BA.debugLineNum = 1602;BA.debugLine="lat = \"10.097031\"";
_lat = "10.097031";
 //BA.debugLineNum = 1603;BA.debugLine="lng = \"122.868764\"";
_lng = "122.868764";
 }else if(_brgy_index==0 && _street_index==5) { 
 //BA.debugLineNum = 1605;BA.debugLine="lat = \"10.096021\"";
_lat = "10.096021";
 //BA.debugLineNum = 1606;BA.debugLine="lng = \"122.869737\"";
_lng = "122.869737";
 }else if(_brgy_index==0 && _street_index==6) { 
 //BA.debugLineNum = 1608;BA.debugLine="lat = \"10.095142\"";
_lat = "10.095142";
 //BA.debugLineNum = 1609;BA.debugLine="lng = \"122.868317\"";
_lng = "122.868317";
 }else if(_brgy_index==0 && _street_index==7) { 
 //BA.debugLineNum = 1611;BA.debugLine="lat = \"10.095303\"";
_lat = "10.095303";
 //BA.debugLineNum = 1612;BA.debugLine="lng = \"122.869509\"";
_lng = "122.869509";
 };
 //BA.debugLineNum = 1615;BA.debugLine="If brgy_index == 1 And street_index == 0 Then 'br";
if (_brgy_index==1 && _street_index==0) { 
 //BA.debugLineNum = 1616;BA.debugLine="lat = \"10.101356\"";
_lat = "10.101356";
 //BA.debugLineNum = 1617;BA.debugLine="lng = \"122.870075\"";
_lng = "122.870075";
 }else if(_brgy_index==1 && _street_index==1) { 
 //BA.debugLineNum = 1619;BA.debugLine="lat = \"10.100583\"";
_lat = "10.100583";
 //BA.debugLineNum = 1620;BA.debugLine="lng = \"122.870176\"";
_lng = "122.870176";
 }else if(_brgy_index==1 && _street_index==2) { 
 //BA.debugLineNum = 1622;BA.debugLine="lat = \"10.100031\"";
_lat = "10.100031";
 //BA.debugLineNum = 1623;BA.debugLine="lng = \"122.870623\"";
_lng = "122.870623";
 }else if(_brgy_index==1 && _street_index==3) { 
 //BA.debugLineNum = 1625;BA.debugLine="lat = \"10.101327\"";
_lat = "10.101327";
 //BA.debugLineNum = 1626;BA.debugLine="lng = \"122.871177\"";
_lng = "122.871177";
 }else if(_brgy_index==1 && _street_index==4) { 
 //BA.debugLineNum = 1628;BA.debugLine="lat = \"10.103330\"";
_lat = "10.103330";
 //BA.debugLineNum = 1629;BA.debugLine="lng = \"122.871391\"";
_lng = "122.871391";
 }else if(_brgy_index==1 && _street_index==5) { 
 //BA.debugLineNum = 1631;BA.debugLine="lat = \"10.102317\"";
_lat = "10.102317";
 //BA.debugLineNum = 1632;BA.debugLine="lng = \"122.870755\"";
_lng = "122.870755";
 }else if(_brgy_index==1 && _street_index==6) { 
 //BA.debugLineNum = 1634;BA.debugLine="lat = \"10.104250\"";
_lat = "10.104250";
 //BA.debugLineNum = 1635;BA.debugLine="lng = \"122.882834\"";
_lng = "122.882834";
 }else if(_brgy_index==1 && _street_index==7) { 
 //BA.debugLineNum = 1637;BA.debugLine="lat = \"10.104943\"";
_lat = "10.104943";
 //BA.debugLineNum = 1638;BA.debugLine="lng = \"122.885207\"";
_lng = "122.885207";
 }else if(_brgy_index==1 && _street_index==8) { 
 //BA.debugLineNum = 1640;BA.debugLine="lat = \"10.101843\"";
_lat = "10.101843";
 //BA.debugLineNum = 1641;BA.debugLine="lng = \"122.871020\"";
_lng = "122.871020";
 }else if(_brgy_index==1 && _street_index==9) { 
 //BA.debugLineNum = 1643;BA.debugLine="lat = \"10.103477\"";
_lat = "10.103477";
 //BA.debugLineNum = 1644;BA.debugLine="lng = \"122.870042\"";
_lng = "122.870042";
 }else if(_brgy_index==1 && _street_index==10) { 
 //BA.debugLineNum = 1646;BA.debugLine="lat = \"10.100710\"";
_lat = "10.100710";
 //BA.debugLineNum = 1647;BA.debugLine="lng = \"122.870889\"";
_lng = "122.870889";
 };
 //BA.debugLineNum = 1650;BA.debugLine="If brgy_index == 2 And street_index == 0 Then 'br";
if (_brgy_index==2 && _street_index==0) { 
 //BA.debugLineNum = 1651;BA.debugLine="lat = \"10.095478\"";
_lat = "10.095478";
 //BA.debugLineNum = 1652;BA.debugLine="lng = \"122.871176\"";
_lng = "122.871176";
 }else if(_brgy_index==2 && _street_index==1) { 
 //BA.debugLineNum = 1654;BA.debugLine="lat = \"10.098599\"";
_lat = "10.098599";
 //BA.debugLineNum = 1655;BA.debugLine="lng = \"122.871761\"";
_lng = "122.871761";
 }else if(_brgy_index==2 && _street_index==2) { 
 //BA.debugLineNum = 1657;BA.debugLine="lat = \"10.094573\"";
_lat = "10.094573";
 //BA.debugLineNum = 1658;BA.debugLine="lng = \"122.870340\"";
_lng = "122.870340";
 }else if(_brgy_index==2 && _street_index==3) { 
 //BA.debugLineNum = 1660;BA.debugLine="lat = \"10.098313\"";
_lat = "10.098313";
 //BA.debugLineNum = 1661;BA.debugLine="lng = \"122.875223\"";
_lng = "122.875223";
 }else if(_brgy_index==2 && _street_index==4) { 
 //BA.debugLineNum = 1663;BA.debugLine="lat = \"10.092235\"";
_lat = "10.092235";
 //BA.debugLineNum = 1664;BA.debugLine="lng = \"122.874356\"";
_lng = "122.874356";
 }else if(_brgy_index==2 && _street_index==5) { 
 //BA.debugLineNum = 1666;BA.debugLine="lat = \"10.103982\"";
_lat = "10.103982";
 //BA.debugLineNum = 1667;BA.debugLine="lng = \"122.885996\"";
_lng = "122.885996";
 }else if(_brgy_index==2 && _street_index==6) { 
 //BA.debugLineNum = 1669;BA.debugLine="lat = \"10.102170\"";
_lat = "10.102170";
 //BA.debugLineNum = 1670;BA.debugLine="lng = \"122.882390\"";
_lng = "122.882390";
 }else if(_brgy_index==2 && _street_index==7) { 
 //BA.debugLineNum = 1672;BA.debugLine="lat = \"10.103272\"";
_lat = "10.103272";
 //BA.debugLineNum = 1673;BA.debugLine="lng = \"122.883948\"";
_lng = "122.883948";
 }else if(_brgy_index==2 && _street_index==8) { 
 //BA.debugLineNum = 1675;BA.debugLine="lat = \"10.103849\"";
_lat = "10.103849";
 //BA.debugLineNum = 1676;BA.debugLine="lng = \"122.884602\"";
_lng = "122.884602";
 }else if(_brgy_index==2 && _street_index==9) { 
 //BA.debugLineNum = 1678;BA.debugLine="lat = \"10.101033\"";
_lat = "10.101033";
 //BA.debugLineNum = 1679;BA.debugLine="lng = \"122.874480\"";
_lng = "122.874480";
 };
 //BA.debugLineNum = 1682;BA.debugLine="If brgy_index == 3 And street_index == 0 Then 'b";
if (_brgy_index==3 && _street_index==0) { 
 //BA.debugLineNum = 1683;BA.debugLine="lat = \"10.121855\"";
_lat = "10.121855";
 //BA.debugLineNum = 1684;BA.debugLine="lng = \"122.872266\"";
_lng = "122.872266";
 }else if(_brgy_index==3 && _street_index==1) { 
 //BA.debugLineNum = 1686;BA.debugLine="lat = \"10.116699\"";
_lat = "10.116699";
 //BA.debugLineNum = 1687;BA.debugLine="lng = \"122.871783\"";
_lng = "122.871783";
 }else if(_brgy_index==3 && _street_index==2) { 
 //BA.debugLineNum = 1689;BA.debugLine="lat = \"10.116024\"";
_lat = "10.116024";
 //BA.debugLineNum = 1690;BA.debugLine="lng = \"122.872477\"";
_lng = "122.872477";
 }else if(_brgy_index==3 && _street_index==3) { 
 //BA.debugLineNum = 1692;BA.debugLine="lat = \"10.114588\"";
_lat = "10.114588";
 //BA.debugLineNum = 1693;BA.debugLine="lng = \"122.872515\"";
_lng = "122.872515";
 }else if(_brgy_index==3 && _street_index==4) { 
 //BA.debugLineNum = 1695;BA.debugLine="lat = \"10.112140\"";
_lat = "10.112140";
 //BA.debugLineNum = 1696;BA.debugLine="lng = \"122.872161\"";
_lng = "122.872161";
 }else if(_brgy_index==3 && _street_index==5) { 
 //BA.debugLineNum = 1698;BA.debugLine="lat = \"10.111531\"";
_lat = "10.111531";
 //BA.debugLineNum = 1699;BA.debugLine="lng = \"122.871542\"";
_lng = "122.871542";
 }else if(_brgy_index==3 && _street_index==6) { 
 //BA.debugLineNum = 1701;BA.debugLine="lat = \"10.107168\"";
_lat = "10.107168";
 //BA.debugLineNum = 1702;BA.debugLine="lng = \"122.871766\"";
_lng = "122.871766";
 }else if(_brgy_index==3 && _street_index==7) { 
 //BA.debugLineNum = 1704;BA.debugLine="lat = \"10.106570\"";
_lat = "10.106570";
 //BA.debugLineNum = 1705;BA.debugLine="lng = \"122.875197\"";
_lng = "122.875197";
 }else if(_brgy_index==3 && _street_index==8) { 
 //BA.debugLineNum = 1707;BA.debugLine="lat = \"10.105759\"";
_lat = "10.105759";
 //BA.debugLineNum = 1708;BA.debugLine="lng = \"122.871537\"";
_lng = "122.871537";
 };
 //BA.debugLineNum = 1711;BA.debugLine="If brgy_index == 4 And street_index == 0 Then 'A";
if (_brgy_index==4 && _street_index==0) { 
 //BA.debugLineNum = 1712;BA.debugLine="lat = \"10.165214\"";
_lat = "10.165214";
 //BA.debugLineNum = 1713;BA.debugLine="lng = \"122.865433\"";
_lng = "122.865433";
 }else if(_brgy_index==4 && _street_index==1) { 
 //BA.debugLineNum = 1715;BA.debugLine="lat = \"10.154170\"";
_lat = "10.154170";
 //BA.debugLineNum = 1716;BA.debugLine="lng = \"122.867255\"";
_lng = "122.867255";
 }else if(_brgy_index==4 && _street_index==2) { 
 //BA.debugLineNum = 1718;BA.debugLine="lat = \"10.161405\"";
_lat = "10.161405";
 //BA.debugLineNum = 1719;BA.debugLine="lng = \"122.862692\"";
_lng = "122.862692";
 }else if(_brgy_index==4 && _street_index==3) { 
 //BA.debugLineNum = 1721;BA.debugLine="lat = \"10.168471\"";
_lat = "10.168471";
 //BA.debugLineNum = 1722;BA.debugLine="lng = \"122.860955\"";
_lng = "122.860955";
 }else if(_brgy_index==4 && _street_index==4) { 
 //BA.debugLineNum = 1724;BA.debugLine="lat = \"10.172481\"";
_lat = "10.172481";
 //BA.debugLineNum = 1725;BA.debugLine="lng = \"122.858629\"";
_lng = "122.858629";
 }else if(_brgy_index==4 && _street_index==5) { 
 //BA.debugLineNum = 1727;BA.debugLine="lat = \"10.166561\"";
_lat = "10.166561";
 //BA.debugLineNum = 1728;BA.debugLine="lng = \"122.859428\"";
_lng = "122.859428";
 }else if(_brgy_index==4 && _street_index==6) { 
 //BA.debugLineNum = 1730;BA.debugLine="lat = \"10.163510\"";
_lat = "10.163510";
 //BA.debugLineNum = 1731;BA.debugLine="lng = \"122.860074\"";
_lng = "122.860074";
 }else if(_brgy_index==4 && _street_index==7) { 
 //BA.debugLineNum = 1733;BA.debugLine="lat = \"10.161033\"";
_lat = "10.161033";
 //BA.debugLineNum = 1734;BA.debugLine="lng = \"122.859773\"";
_lng = "122.859773";
 }else if(_brgy_index==4 && _street_index==8) { 
 //BA.debugLineNum = 1736;BA.debugLine="lat = \"10.159280\"";
_lat = "10.159280";
 //BA.debugLineNum = 1737;BA.debugLine="lng = \"122.861621\"";
_lng = "122.861621";
 }else if(_brgy_index==4 && _street_index==9) { 
 //BA.debugLineNum = 1739;BA.debugLine="lat = \"10.159062\"";
_lat = "10.159062";
 //BA.debugLineNum = 1740;BA.debugLine="lng = \"122.860209\"";
_lng = "122.860209";
 }else if(_brgy_index==4 && _street_index==10) { 
 //BA.debugLineNum = 1742;BA.debugLine="lat = \"10.181112\"";
_lat = "10.181112";
 //BA.debugLineNum = 1743;BA.debugLine="lng = \"122.864670\"";
_lng = "122.864670";
 }else if(_brgy_index==4 && _street_index==11) { 
 //BA.debugLineNum = 1745;BA.debugLine="lat = \"10.167295\"";
_lat = "10.167295";
 //BA.debugLineNum = 1746;BA.debugLine="lng = \"122.857858\"";
_lng = "122.857858";
 };
 //BA.debugLineNum = 1749;BA.debugLine="If brgy_index == 5 And street_index == 0 Then 'ca";
if (_brgy_index==5 && _street_index==0) { 
 //BA.debugLineNum = 1750;BA.debugLine="lat = \"10.092993\"";
_lat = "10.092993";
 //BA.debugLineNum = 1751;BA.debugLine="lng = \"122.861694\"";
_lng = "122.861694";
 }else if(_brgy_index==5 && _street_index==1) { 
 //BA.debugLineNum = 1753;BA.debugLine="lat = \"10.090587\"";
_lat = "10.090587";
 //BA.debugLineNum = 1754;BA.debugLine="lng = \"122.868414\"";
_lng = "122.868414";
 }else if(_brgy_index==5 && _street_index==2) { 
 //BA.debugLineNum = 1756;BA.debugLine="lat = \"10.091551\"";
_lat = "10.091551";
 //BA.debugLineNum = 1757;BA.debugLine="lng = \"122.869249\"";
_lng = "122.869249";
 }else if(_brgy_index==5 && _street_index==3) { 
 //BA.debugLineNum = 1759;BA.debugLine="lat = \"10.086452\"";
_lat = "10.086452";
 //BA.debugLineNum = 1760;BA.debugLine="lng = \"122.865742\"";
_lng = "122.865742";
 }else if(_brgy_index==5 && _street_index==4) { 
 //BA.debugLineNum = 1762;BA.debugLine="lat = \"10.083507\"";
_lat = "10.083507";
 //BA.debugLineNum = 1763;BA.debugLine="lng = \"122.858928\"";
_lng = "122.858928";
 }else if(_brgy_index==5 && _street_index==5) { 
 //BA.debugLineNum = 1765;BA.debugLine="lat = \"10.077131\"";
_lat = "10.077131";
 //BA.debugLineNum = 1766;BA.debugLine="lng = \"122.864236\"";
_lng = "122.864236";
 }else if(_brgy_index==5 && _street_index==6) { 
 //BA.debugLineNum = 1768;BA.debugLine="lat = \"10.081722\"";
_lat = "10.081722";
 //BA.debugLineNum = 1769;BA.debugLine="lng = \"122.882661\"";
_lng = "122.882661";
 }else if(_brgy_index==5 && _street_index==7) { 
 //BA.debugLineNum = 1771;BA.debugLine="lat = \"10.081822\"";
_lat = "10.081822";
 //BA.debugLineNum = 1772;BA.debugLine="lng = \"122.868295\"";
_lng = "122.868295";
 }else if(_brgy_index==5 && _street_index==8) { 
 //BA.debugLineNum = 1774;BA.debugLine="lat = \"10.079513\"";
_lat = "10.079513";
 //BA.debugLineNum = 1775;BA.debugLine="lng = \"122.876610\"";
_lng = "122.876610";
 }else if(_brgy_index==5 && _street_index==9) { 
 //BA.debugLineNum = 1777;BA.debugLine="lat = \"10.068560\"";
_lat = "10.068560";
 //BA.debugLineNum = 1778;BA.debugLine="lng = \"122.887366\"";
_lng = "122.887366";
 }else if(_brgy_index==5 && _street_index==10) { 
 //BA.debugLineNum = 1780;BA.debugLine="lat = \"10.066934\"";
_lat = "10.066934";
 //BA.debugLineNum = 1781;BA.debugLine="lng = \"122.871963\"";
_lng = "122.871963";
 }else if(_brgy_index==5 && _street_index==11) { 
 //BA.debugLineNum = 1783;BA.debugLine="lat = \"10.064251\"";
_lat = "10.064251";
 //BA.debugLineNum = 1784;BA.debugLine="lng = \"122.883023\"";
_lng = "122.883023";
 }else if(_brgy_index==5 && _street_index==12) { 
 //BA.debugLineNum = 1786;BA.debugLine="lat = \"10.058546\"";
_lat = "10.058546";
 //BA.debugLineNum = 1787;BA.debugLine="lng = \"122.882968\"";
_lng = "122.882968";
 }else if(_brgy_index==5 && _street_index==13) { 
 //BA.debugLineNum = 1789;BA.debugLine="lat = \"10.054104\"";
_lat = "10.054104";
 //BA.debugLineNum = 1790;BA.debugLine="lng = \"122.885506\"";
_lng = "122.885506";
 }else if(_brgy_index==5 && _street_index==14) { 
 //BA.debugLineNum = 1792;BA.debugLine="lat = \"10.049464\"";
_lat = "10.049464";
 //BA.debugLineNum = 1793;BA.debugLine="lng = \"122.885667\"";
_lng = "122.885667";
 }else if(_brgy_index==5 && _street_index==15) { 
 //BA.debugLineNum = 1795;BA.debugLine="lat = \"10.041580\"";
_lat = "10.041580";
 //BA.debugLineNum = 1796;BA.debugLine="lng = \"122.900269\"";
_lng = "122.900269";
 }else if(_brgy_index==5 && _street_index==16) { 
 //BA.debugLineNum = 1798;BA.debugLine="lat = \"10.041395\"";
_lat = "10.041395";
 //BA.debugLineNum = 1799;BA.debugLine="lng = \"122.906248\"";
_lng = "122.906248";
 };
 //BA.debugLineNum = 1802;BA.debugLine="If brgy_index == 6 And street_index == 0 Then 'Bu";
if (_brgy_index==6 && _street_index==0) { 
 //BA.debugLineNum = 1803;BA.debugLine="lat = \"10.035728\"";
_lat = "10.035728";
 //BA.debugLineNum = 1804;BA.debugLine="lng = \"122.847547\"";
_lng = "122.847547";
 }else if(_brgy_index==6 && _street_index==1) { 
 //BA.debugLineNum = 1806;BA.debugLine="lat = \"10.000603\"";
_lat = "10.000603";
 //BA.debugLineNum = 1807;BA.debugLine="lng = \"122.885243\"";
_lng = "122.885243";
 }else if(_brgy_index==6 && _street_index==2) { 
 //BA.debugLineNum = 1809;BA.debugLine="lat = \"10.000521\"";
_lat = "10.000521";
 //BA.debugLineNum = 1810;BA.debugLine="lng = \"122.895867\"";
_lng = "122.895867";
 }else if(_brgy_index==6 && _street_index==3) { 
 //BA.debugLineNum = 1812;BA.debugLine="lat = \"9.943276\"";
_lat = "9.943276";
 //BA.debugLineNum = 1813;BA.debugLine="lng = \"122.975801\"";
_lng = "122.975801";
 };
 //BA.debugLineNum = 1816;BA.debugLine="If brgy_index == 7 And street_index == 0 Then '";
if (_brgy_index==7 && _street_index==0) { 
 //BA.debugLineNum = 1817;BA.debugLine="lat = \"10.156301\"";
_lat = "10.156301";
 //BA.debugLineNum = 1818;BA.debugLine="lng = \"122.941207\"";
_lng = "122.941207";
 }else if(_brgy_index==7 && _street_index==1) { 
 //BA.debugLineNum = 1820;BA.debugLine="lat = \"10.142692\"";
_lat = "10.142692";
 //BA.debugLineNum = 1821;BA.debugLine="lng = \"122.947560\"";
_lng = "122.947560";
 }else if(_brgy_index==7 && _street_index==2) { 
 //BA.debugLineNum = 1823;BA.debugLine="lat = \"10.139494\"";
_lat = "10.139494";
 //BA.debugLineNum = 1824;BA.debugLine="lng = \"122.942788\"";
_lng = "122.942788";
 }else if(_brgy_index==7 && _street_index==3) { 
 //BA.debugLineNum = 1826;BA.debugLine="lat = \"10.110265\"";
_lat = "10.110265";
 //BA.debugLineNum = 1827;BA.debugLine="lng = \"122.947908\"";
_lng = "122.947908";
 }else if(_brgy_index==7 && _street_index==4) { 
 //BA.debugLineNum = 1829;BA.debugLine="lat = \"10.127828\"";
_lat = "10.127828";
 //BA.debugLineNum = 1830;BA.debugLine="lng = \"122.950197\"";
_lng = "122.950197";
 }else if(_brgy_index==7 && _street_index==5) { 
 //BA.debugLineNum = 1832;BA.debugLine="lat = \"10.125287\"";
_lat = "10.125287";
 //BA.debugLineNum = 1833;BA.debugLine="lng = \"122.945735\"";
_lng = "122.945735";
 }else if(_brgy_index==7 && _street_index==6) { 
 //BA.debugLineNum = 1835;BA.debugLine="lat = \"10.143975\"";
_lat = "10.143975";
 //BA.debugLineNum = 1836;BA.debugLine="lng = \"122.930610\"";
_lng = "122.930610";
 }else if(_brgy_index==7 && _street_index==7) { 
 //BA.debugLineNum = 1838;BA.debugLine="lat = \"10.137563\"";
_lat = "10.137563";
 //BA.debugLineNum = 1839;BA.debugLine="lng = \"122.939870\"";
_lng = "122.939870";
 }else if(_brgy_index==7 && _street_index==8) { 
 //BA.debugLineNum = 1841;BA.debugLine="lat = \"10.150449\"";
_lat = "10.150449";
 //BA.debugLineNum = 1842;BA.debugLine="lng = \"122.933761\"";
_lng = "122.933761";
 }else if(_brgy_index==7 && _street_index==9) { 
 //BA.debugLineNum = 1844;BA.debugLine="lat = \"10.150286\"";
_lat = "10.150286";
 //BA.debugLineNum = 1845;BA.debugLine="lng = \"122.948956\"";
_lng = "122.948956";
 }else if(_brgy_index==7 && _street_index==10) { 
 //BA.debugLineNum = 1847;BA.debugLine="lat = \"10.148481\"";
_lat = "10.148481";
 //BA.debugLineNum = 1848;BA.debugLine="lng = \"122.943230\"";
_lng = "122.943230";
 }else if(_brgy_index==7 && _street_index==11) { 
 //BA.debugLineNum = 1850;BA.debugLine="lat = \"10.106200\"";
_lat = "10.106200";
 //BA.debugLineNum = 1851;BA.debugLine="lng = \"122.948051\"";
_lng = "122.948051";
 }else if(_brgy_index==7 && _street_index==12) { 
 //BA.debugLineNum = 1853;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 1854;BA.debugLine="lng = \"122.926593\"";
_lng = "122.926593";
 }else if(_brgy_index==7 && _street_index==13) { 
 //BA.debugLineNum = 1856;BA.debugLine="lat = \"10.120798\"";
_lat = "10.120798";
 //BA.debugLineNum = 1857;BA.debugLine="lng = \"122.938371\"";
_lng = "122.938371";
 }else if(_brgy_index==7 && _street_index==14) { 
 //BA.debugLineNum = 1859;BA.debugLine="lat = \"10.153217\"";
_lat = "10.153217";
 //BA.debugLineNum = 1860;BA.debugLine="lng = \"122.951714\"";
_lng = "122.951714";
 };
 //BA.debugLineNum = 1863;BA.debugLine="If brgy_index == 8 And street_index == 0 Then";
if (_brgy_index==8 && _street_index==0) { 
 //BA.debugLineNum = 1864;BA.debugLine="lat = \"10.157177\"";
_lat = "10.157177";
 //BA.debugLineNum = 1865;BA.debugLine="lng = \"122.895986\"";
_lng = "122.895986";
 }else if(_brgy_index==8 && _street_index==1) { 
 //BA.debugLineNum = 1867;BA.debugLine="lat = \"10.180004\"";
_lat = "10.180004";
 //BA.debugLineNum = 1868;BA.debugLine="lng = \"122.897999\"";
_lng = "122.897999";
 }else if(_brgy_index==8 && _street_index==2) { 
 //BA.debugLineNum = 1870;BA.debugLine="lat = \"10.192848\"";
_lat = "10.192848";
 //BA.debugLineNum = 1871;BA.debugLine="lng = \"122.900234\"";
_lng = "122.900234";
 }else if(_brgy_index==8 && _street_index==3) { 
 //BA.debugLineNum = 1873;BA.debugLine="lat = \"10.179993\"";
_lat = "10.179993";
 //BA.debugLineNum = 1874;BA.debugLine="lng = \"122.904299\"";
_lng = "122.904299";
 }else if(_brgy_index==8 && _street_index==4) { 
 //BA.debugLineNum = 1876;BA.debugLine="lat = \"10.183439\"";
_lat = "10.183439";
 //BA.debugLineNum = 1877;BA.debugLine="lng = \"122.889622\"";
_lng = "122.889622";
 };
 //BA.debugLineNum = 1880;BA.debugLine="If brgy_index == 9 And street_index == 0 Then 'Ca";
if (_brgy_index==9 && _street_index==0) { 
 //BA.debugLineNum = 1881;BA.debugLine="lat = \"10.074128\"";
_lat = "10.074128";
 //BA.debugLineNum = 1882;BA.debugLine="lng = \"122.981978\"";
_lng = "122.981978";
 }else if(_brgy_index==9 && _street_index==1) { 
 //BA.debugLineNum = 1884;BA.debugLine="lat = \"10.109208\"";
_lat = "10.109208";
 //BA.debugLineNum = 1885;BA.debugLine="lng = \"122.896717\"";
_lng = "122.896717";
 }else if(_brgy_index==9 && _street_index==2) { 
 //BA.debugLineNum = 1887;BA.debugLine="lat = \"10.097119\"";
_lat = "10.097119";
 //BA.debugLineNum = 1888;BA.debugLine="lng = \"122.947066\"";
_lng = "122.947066";
 }else if(_brgy_index==9 && _street_index==3) { 
 //BA.debugLineNum = 1890;BA.debugLine="lat = \"10.099023\"";
_lat = "10.099023";
 //BA.debugLineNum = 1891;BA.debugLine="lng = \"122.971723\"";
_lng = "122.971723";
 }else if(_brgy_index==9 && _street_index==4) { 
 //BA.debugLineNum = 1893;BA.debugLine="lat = \"10.119761\"";
_lat = "10.119761";
 //BA.debugLineNum = 1894;BA.debugLine="lng = \"122.901613\"";
_lng = "122.901613";
 }else if(_brgy_index==9 && _street_index==5) { 
 //BA.debugLineNum = 1896;BA.debugLine="lat = \"10.099402\"";
_lat = "10.099402";
 //BA.debugLineNum = 1897;BA.debugLine="lng = \"122.896454\"";
_lng = "122.896454";
 }else if(_brgy_index==9 && _street_index==6) { 
 //BA.debugLineNum = 1899;BA.debugLine="lat = \"10.097102\"";
_lat = "10.097102";
 //BA.debugLineNum = 1900;BA.debugLine="lng = \"122.922368\"";
_lng = "122.922368";
 }else if(_brgy_index==9 && _street_index==7) { 
 //BA.debugLineNum = 1902;BA.debugLine="lat = \"10.095304\"";
_lat = "10.095304";
 //BA.debugLineNum = 1903;BA.debugLine="lng = \"122.929242\"";
_lng = "122.929242";
 }else if(_brgy_index==9 && _street_index==8) { 
 //BA.debugLineNum = 1905;BA.debugLine="lat = \"10.114128\"";
_lat = "10.114128";
 //BA.debugLineNum = 1906;BA.debugLine="lng = \"122.893868\"";
_lng = "122.893868";
 };
 //BA.debugLineNum = 1909;BA.debugLine="If brgy_index == 10 And street_index == 0 Then 'L";
if (_brgy_index==10 && _street_index==0) { 
 //BA.debugLineNum = 1910;BA.debugLine="lat = \"10.1799469\"";
_lat = "10.1799469";
 //BA.debugLineNum = 1911;BA.debugLine="lng = \"122.9068577\"";
_lng = "122.9068577";
 }else if(_brgy_index==10 && _street_index==1) { 
 //BA.debugLineNum = 1913;BA.debugLine="lat = \"10.180524\"";
_lat = "10.180524";
 //BA.debugLineNum = 1914;BA.debugLine="lng = \"122.906798\"";
_lng = "122.906798";
 }else if(_brgy_index==10 && _street_index==2) { 
 //BA.debugLineNum = 1916;BA.debugLine="lat = \"10.173336\"";
_lat = "10.173336";
 //BA.debugLineNum = 1917;BA.debugLine="lng = \"122.9118842\"";
_lng = "122.9118842";
 }else if(_brgy_index==10 && _street_index==3) { 
 //BA.debugLineNum = 1919;BA.debugLine="lat = \"10.177359\"";
_lat = "10.177359";
 //BA.debugLineNum = 1920;BA.debugLine="lng = \"122.913033\"";
_lng = "122.913033";
 }else if(_brgy_index==10 && _street_index==4) { 
 //BA.debugLineNum = 1922;BA.debugLine="lat = \"10.179847\"";
_lat = "10.179847";
 //BA.debugLineNum = 1923;BA.debugLine="lng = \"122.914160\"";
_lng = "122.914160";
 }else if(_brgy_index==10 && _street_index==5) { 
 //BA.debugLineNum = 1925;BA.debugLine="lat = \"10.182718\"";
_lat = "10.182718";
 //BA.debugLineNum = 1926;BA.debugLine="lng = \"122.915228\"";
_lng = "122.915228";
 }else if(_brgy_index==10 && _street_index==6) { 
 //BA.debugLineNum = 1928;BA.debugLine="lat = \"10.186454\"";
_lat = "10.186454";
 //BA.debugLineNum = 1929;BA.debugLine="lng = \"122.916278\"";
_lng = "122.916278";
 }else if(_brgy_index==10 && _street_index==7) { 
 //BA.debugLineNum = 1931;BA.debugLine="lat = \"10.168057\"";
_lat = "10.168057";
 //BA.debugLineNum = 1932;BA.debugLine="lng = \"122.924501\"";
_lng = "122.924501";
 };
 //BA.debugLineNum = 1935;BA.debugLine="If brgy_index == 11 And street_index == 0 Then 'M";
if (_brgy_index==11 && _street_index==0) { 
 //BA.debugLineNum = 1936;BA.debugLine="lat = \"10.050418\"";
_lat = "10.050418";
 //BA.debugLineNum = 1937;BA.debugLine="lng = \"122.867097\"";
_lng = "122.867097";
 }else if(_brgy_index==11 && _street_index==1) { 
 //BA.debugLineNum = 1939;BA.debugLine="lat = \"10.027855\"";
_lat = "10.027855";
 //BA.debugLineNum = 1940;BA.debugLine="lng = \"122.906833\"";
_lng = "122.906833";
 }else if(_brgy_index==11 && _street_index==2) { 
 //BA.debugLineNum = 1942;BA.debugLine="lat = \"10.027522\"";
_lat = "10.027522";
 //BA.debugLineNum = 1943;BA.debugLine="lng = \"122.876637\"";
_lng = "122.876637";
 }else if(_brgy_index==11 && _street_index==3) { 
 //BA.debugLineNum = 1945;BA.debugLine="lat = \"10.017254\"";
_lat = "10.017254";
 //BA.debugLineNum = 1946;BA.debugLine="lng = \"122.900969\"";
_lng = "122.900969";
 }else if(_brgy_index==11 && _street_index==4) { 
 //BA.debugLineNum = 1948;BA.debugLine="lat = \"10.028535\"";
_lat = "10.028535";
 //BA.debugLineNum = 1949;BA.debugLine="lng = \"122.900364\"";
_lng = "122.900364";
 }else if(_brgy_index==11 && _street_index==5) { 
 //BA.debugLineNum = 1951;BA.debugLine="lat = \"10.025485\"";
_lat = "10.025485";
 //BA.debugLineNum = 1952;BA.debugLine="lng = \"122.890023\"";
_lng = "122.890023";
 };
 //BA.debugLineNum = 1955;BA.debugLine="If brgy_index == 12 And street_index == 0 Then 'M";
if (_brgy_index==12 && _street_index==0) { 
 //BA.debugLineNum = 1956;BA.debugLine="lat = \"10.137572\"";
_lat = "10.137572";
 //BA.debugLineNum = 1957;BA.debugLine="lng = \"122.939888\"";
_lng = "122.939888";
 }else if(_brgy_index==12 && _street_index==1) { 
 //BA.debugLineNum = 1959;BA.debugLine="lat = \"10.132195\"";
_lat = "10.132195";
 //BA.debugLineNum = 1960;BA.debugLine="lng = \"122.899837\"";
_lng = "122.899837";
 }else if(_brgy_index==12 && _street_index==2) { 
 //BA.debugLineNum = 1962;BA.debugLine="lat = \"10.123430\"";
_lat = "10.123430";
 //BA.debugLineNum = 1963;BA.debugLine="lng = \"122.892250\"";
_lng = "122.892250";
 }else if(_brgy_index==12 && _street_index==3) { 
 //BA.debugLineNum = 1965;BA.debugLine="lat = \"10.130383\"";
_lat = "10.130383";
 //BA.debugLineNum = 1966;BA.debugLine="lng = \"122.893010\"";
_lng = "122.893010";
 }else if(_brgy_index==12 && _street_index==4) { 
 //BA.debugLineNum = 1968;BA.debugLine="lat = \"10.123127\"";
_lat = "10.123127";
 //BA.debugLineNum = 1969;BA.debugLine="lng = \"122.887952\"";
_lng = "122.887952";
 }else if(_brgy_index==12 && _street_index==5) { 
 //BA.debugLineNum = 1971;BA.debugLine="lat = \"10.131098\"";
_lat = "10.131098";
 //BA.debugLineNum = 1972;BA.debugLine="lng = \"122.879801\"";
_lng = "122.879801";
 }else if(_brgy_index==12 && _street_index==6) { 
 //BA.debugLineNum = 1974;BA.debugLine="lat = \"10.137485\"";
_lat = "10.137485";
 //BA.debugLineNum = 1975;BA.debugLine="lng = \"122.911434\"";
_lng = "122.911434";
 }else if(_brgy_index==12 && _street_index==7) { 
 //BA.debugLineNum = 1977;BA.debugLine="lat = \"10.106803\"";
_lat = "10.106803";
 //BA.debugLineNum = 1978;BA.debugLine="lng = \"122.885727\"";
_lng = "122.885727";
 }else if(_brgy_index==12 && _street_index==8) { 
 //BA.debugLineNum = 1980;BA.debugLine="lat = \"10.115220\"";
_lat = "10.115220";
 //BA.debugLineNum = 1981;BA.debugLine="lng = \"122.890515\"";
_lng = "122.890515";
 }else if(_brgy_index==12 && _street_index==9) { 
 //BA.debugLineNum = 1983;BA.debugLine="lat = \"10.108754\"";
_lat = "10.108754";
 //BA.debugLineNum = 1984;BA.debugLine="lng = \"122.894130\"";
_lng = "122.894130";
 }else if(_brgy_index==12 && _street_index==10) { 
 //BA.debugLineNum = 1986;BA.debugLine="lat = \"10.149506\"";
_lat = "10.149506";
 //BA.debugLineNum = 1987;BA.debugLine="lng = \"122.897389\"";
_lng = "122.897389";
 }else if(_brgy_index==12 && _street_index==11) { 
 //BA.debugLineNum = 1989;BA.debugLine="lat = \"10.122215\"";
_lat = "10.122215";
 //BA.debugLineNum = 1990;BA.debugLine="lng = \"122.892160\"";
_lng = "122.892160";
 }else if(_brgy_index==12 && _street_index==12) { 
 //BA.debugLineNum = 1992;BA.debugLine="lat = \"10.142698\"";
_lat = "10.142698";
 //BA.debugLineNum = 1993;BA.debugLine="lng = \"122.898168\"";
_lng = "122.898168";
 };
 //BA.debugLineNum = 1996;BA.debugLine="If brgy_index == 13 And street_index == 0 Then 'N";
if (_brgy_index==13 && _street_index==0) { 
 //BA.debugLineNum = 1997;BA.debugLine="lat = \"10.161629\"";
_lat = "10.161629";
 //BA.debugLineNum = 1998;BA.debugLine="lng = \"122.872772\"";
_lng = "122.872772";
 }else if(_brgy_index==13 && _street_index==1) { 
 //BA.debugLineNum = 2000;BA.debugLine="lat = \"10.161863\"";
_lat = "10.161863";
 //BA.debugLineNum = 2001;BA.debugLine="lng = \"122.876192\"";
_lng = "122.876192";
 }else if(_brgy_index==13 && _street_index==2) { 
 //BA.debugLineNum = 2003;BA.debugLine="lat = \"10.157407\"";
_lat = "10.157407";
 //BA.debugLineNum = 2004;BA.debugLine="lng = \"122.885663\"";
_lng = "122.885663";
 }else if(_brgy_index==13 && _street_index==3) { 
 //BA.debugLineNum = 2006;BA.debugLine="lat = \"10.167497\"";
_lat = "10.167497";
 //BA.debugLineNum = 2007;BA.debugLine="lng = \"122.879777\"";
_lng = "122.879777";
 }else if(_brgy_index==13 && _street_index==4) { 
 //BA.debugLineNum = 2009;BA.debugLine="lat = \"10.176260\"";
_lat = "10.176260";
 //BA.debugLineNum = 2010;BA.debugLine="lng = \"122.880815\"";
_lng = "122.880815";
 }else if(_brgy_index==13 && _street_index==5) { 
 //BA.debugLineNum = 2012;BA.debugLine="lat = \"10.170524\"";
_lat = "10.170524";
 //BA.debugLineNum = 2013;BA.debugLine="lng = \"122.883603\"";
_lng = "122.883603";
 };
 //BA.debugLineNum = 2016;BA.debugLine="If brgy_index == 14 And street_index == 0 Then 'S";
if (_brgy_index==14 && _street_index==0) { 
 //BA.debugLineNum = 2017;BA.debugLine="lat = \"10.071514\"";
_lat = "10.071514";
 //BA.debugLineNum = 2018;BA.debugLine="lng = \"122.916010\"";
_lng = "122.916010";
 }else if(_brgy_index==14 && _street_index==1) { 
 //BA.debugLineNum = 2020;BA.debugLine="lat = \"10.069622\"";
_lat = "10.069622";
 //BA.debugLineNum = 2021;BA.debugLine="lng = \"122.909890\"";
_lng = "122.909890";
 }else if(_brgy_index==14 && _street_index==2) { 
 //BA.debugLineNum = 2023;BA.debugLine="lat = \"10.076890\"";
_lat = "10.076890";
 //BA.debugLineNum = 2024;BA.debugLine="lng = \"122.894231\"";
_lng = "122.894231";
 }else if(_brgy_index==14 && _street_index==3) { 
 //BA.debugLineNum = 2026;BA.debugLine="lat = \"10.086207\"";
_lat = "10.086207";
 //BA.debugLineNum = 2027;BA.debugLine="lng = \"122.914044\"";
_lng = "122.914044";
 }else if(_brgy_index==14 && _street_index==4) { 
 //BA.debugLineNum = 2029;BA.debugLine="lat = \"10.067393\"";
_lat = "10.067393";
 //BA.debugLineNum = 2030;BA.debugLine="lng = \"122.900935\"";
_lng = "122.900935";
 }else if(_brgy_index==14 && _street_index==5) { 
 //BA.debugLineNum = 2032;BA.debugLine="lat = \"10.071900\"";
_lat = "10.071900";
 //BA.debugLineNum = 2033;BA.debugLine="lng = \"122.906250\"";
_lng = "122.906250";
 }else if(_brgy_index==14 && _street_index==6) { 
 //BA.debugLineNum = 2035;BA.debugLine="lat = \"10.061702\"";
_lat = "10.061702";
 //BA.debugLineNum = 2036;BA.debugLine="lng = \"122.896226\"";
_lng = "122.896226";
 }else if(_brgy_index==14 && _street_index==7) { 
 //BA.debugLineNum = 2038;BA.debugLine="lat = \"10.054802\"";
_lat = "10.054802";
 //BA.debugLineNum = 2039;BA.debugLine="lng = \"122.938688\"";
_lng = "122.938688";
 }else if(_brgy_index==14 && _street_index==8) { 
 //BA.debugLineNum = 2041;BA.debugLine="lat = \"10.071827\"";
_lat = "10.071827";
 //BA.debugLineNum = 2042;BA.debugLine="lng = \"122.921092\"";
_lng = "122.921092";
 }else if(_brgy_index==14 && _street_index==9) { 
 //BA.debugLineNum = 2044;BA.debugLine="lat = \"10.050849\"";
_lat = "10.050849";
 //BA.debugLineNum = 2045;BA.debugLine="lng = \"122.907632\"";
_lng = "122.907632";
 };
 //BA.debugLineNum = 2048;BA.debugLine="If brgy_index == 15 And street_index == 0 Then 'S";
if (_brgy_index==15 && _street_index==0) { 
 //BA.debugLineNum = 2049;BA.debugLine="lat = \"10.155844\"";
_lat = "10.155844";
 //BA.debugLineNum = 2050;BA.debugLine="lng = \"122.861129\"";
_lng = "122.861129";
 }else if(_brgy_index==15 && _street_index==1) { 
 //BA.debugLineNum = 2052;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 2053;BA.debugLine="lng = \"122.861669\"";
_lng = "122.861669";
 }else if(_brgy_index==15 && _street_index==2) { 
 //BA.debugLineNum = 2055;BA.debugLine="lat = \"10.147663\"";
_lat = "10.147663";
 //BA.debugLineNum = 2056;BA.debugLine="lng = \"122.862471\"";
_lng = "122.862471";
 }else if(_brgy_index==15 && _street_index==3) { 
 //BA.debugLineNum = 2058;BA.debugLine="lat = \"10.144440\"";
_lat = "10.144440";
 //BA.debugLineNum = 2059;BA.debugLine="lng = \"122.862524\"";
_lng = "122.862524";
 };
 //BA.debugLineNum = 2062;BA.debugLine="If brgy_index == 16 And street_index == 0 Then 'S";
if (_brgy_index==16 && _street_index==0) { 
 //BA.debugLineNum = 2063;BA.debugLine="lat = \"10.053680\"";
_lat = "10.053680";
 //BA.debugLineNum = 2064;BA.debugLine="lng = \"122.843876\"";
_lng = "122.843876";
 }else if(_brgy_index==16 && _street_index==1) { 
 //BA.debugLineNum = 2066;BA.debugLine="lat = \"10.055961\"";
_lat = "10.055961";
 //BA.debugLineNum = 2067;BA.debugLine="lng = \"122.841980\"";
_lng = "122.841980";
 }else if(_brgy_index==16 && _street_index==2) { 
 //BA.debugLineNum = 2069;BA.debugLine="lat = \"10.053363\"";
_lat = "10.053363";
 //BA.debugLineNum = 2070;BA.debugLine="lng = \"122.843295\"";
_lng = "122.843295";
 }else if(_brgy_index==16 && _street_index==3) { 
 //BA.debugLineNum = 2072;BA.debugLine="lat = \"10.053032\"";
_lat = "10.053032";
 //BA.debugLineNum = 2073;BA.debugLine="lng = \"122.842594\"";
_lng = "122.842594";
 }else if(_brgy_index==16 && _street_index==4) { 
 //BA.debugLineNum = 2075;BA.debugLine="lat = \"10.052328\"";
_lat = "10.052328";
 //BA.debugLineNum = 2076;BA.debugLine="lng = \"122.842835\"";
_lng = "122.842835";
 }else if(_brgy_index==16 && _street_index==5) { 
 //BA.debugLineNum = 2078;BA.debugLine="lat = \"10.052573\"";
_lat = "10.052573";
 //BA.debugLineNum = 2079;BA.debugLine="lng = \"122.844229\"";
_lng = "122.844229";
 }else if(_brgy_index==16 && _street_index==6) { 
 //BA.debugLineNum = 2081;BA.debugLine="lat = \"10.046957\"";
_lat = "10.046957";
 //BA.debugLineNum = 2082;BA.debugLine="lng = \"122.839610\"";
_lng = "122.839610";
 }else if(_brgy_index==16 && _street_index==7) { 
 //BA.debugLineNum = 2084;BA.debugLine="lat = \"10.035813\"";
_lat = "10.035813";
 //BA.debugLineNum = 2085;BA.debugLine="lng = \"122.835364\"";
_lng = "122.835364";
 };
 //BA.debugLineNum = 2088;BA.debugLine="If brgy_index == 17 And street_index == 0 Then 'T";
if (_brgy_index==17 && _street_index==0) { 
 //BA.debugLineNum = 2089;BA.debugLine="lat = \"10.148233\"";
_lat = "10.148233";
 //BA.debugLineNum = 2090;BA.debugLine="lng = \"122.869741\"";
_lng = "122.869741";
 }else if(_brgy_index==17 && _street_index==1) { 
 //BA.debugLineNum = 2092;BA.debugLine="lat = \"10.139867\"";
_lat = "10.139867";
 //BA.debugLineNum = 2093;BA.debugLine="lng = \"122.869882\"";
_lng = "122.869882";
 }else if(_brgy_index==17 && _street_index==2) { 
 //BA.debugLineNum = 2095;BA.debugLine="lat = \"10.126453\"";
_lat = "10.126453";
 //BA.debugLineNum = 2096;BA.debugLine="lng = \"122.868927\"";
_lng = "122.868927";
 }else if(_brgy_index==17 && _street_index==3) { 
 //BA.debugLineNum = 2098;BA.debugLine="lat = \"10.127470\"";
_lat = "10.127470";
 //BA.debugLineNum = 2099;BA.debugLine="lng = \"122.862942\"";
_lng = "122.862942";
 }else if(_brgy_index==17 && _street_index==4) { 
 //BA.debugLineNum = 2101;BA.debugLine="lat = \"10.117998\"";
_lat = "10.117998";
 //BA.debugLineNum = 2102;BA.debugLine="lng = \"122.866817\"";
_lng = "122.866817";
 }else if(_brgy_index==17 && _street_index==5) { 
 //BA.debugLineNum = 2104;BA.debugLine="lat = \"10.108173\"";
_lat = "10.108173";
 //BA.debugLineNum = 2105;BA.debugLine="lng = \"122.864592\"";
_lng = "122.864592";
 }else if(_brgy_index==17 && _street_index==6) { 
 //BA.debugLineNum = 2107;BA.debugLine="lat = \"10.126115\"";
_lat = "10.126115";
 //BA.debugLineNum = 2108;BA.debugLine="lng = \"122.871073\"";
_lng = "122.871073";
 }else if(_brgy_index==17 && _street_index==7) { 
 //BA.debugLineNum = 2110;BA.debugLine="lat = \"10.129412\"";
_lat = "10.129412";
 //BA.debugLineNum = 2111;BA.debugLine="lng = \"122.869408\"";
_lng = "122.869408";
 }else if(_brgy_index==17 && _street_index==8) { 
 //BA.debugLineNum = 2113;BA.debugLine="lat = \"10.134647\"";
_lat = "10.134647";
 //BA.debugLineNum = 2114;BA.debugLine="lng = \"122.871841\"";
_lng = "122.871841";
 }else if(_brgy_index==17 && _street_index==9) { 
 //BA.debugLineNum = 2116;BA.debugLine="lat = \"10.124801\"";
_lat = "10.124801";
 //BA.debugLineNum = 2117;BA.debugLine="lng = \"122.868277\"";
_lng = "122.868277";
 }else if(_brgy_index==17 && _street_index==10) { 
 //BA.debugLineNum = 2119;BA.debugLine="lat = \"10.124422\"";
_lat = "10.124422";
 //BA.debugLineNum = 2120;BA.debugLine="lng = \"122.866917\"";
_lng = "122.866917";
 };
 //BA.debugLineNum = 2123;BA.debugLine="If brgy_index == 18 And street_index == 0 Then 'T";
if (_brgy_index==18 && _street_index==0) { 
 //BA.debugLineNum = 2124;BA.debugLine="lat = \"10.065086\"";
_lat = "10.065086";
 //BA.debugLineNum = 2125;BA.debugLine="lng = \"122.843793\"";
_lng = "122.843793";
 }else if(_brgy_index==18 && _street_index==1) { 
 //BA.debugLineNum = 2127;BA.debugLine="lat = \"10.071356\"";
_lat = "10.071356";
 //BA.debugLineNum = 2128;BA.debugLine="lng = \"122.853102\"";
_lng = "122.853102";
 }else if(_brgy_index==18 && _street_index==2) { 
 //BA.debugLineNum = 2130;BA.debugLine="lat = \"10.060206\"";
_lat = "10.060206";
 //BA.debugLineNum = 2131;BA.debugLine="lng = \"122.850172\"";
_lng = "122.850172";
 }else if(_brgy_index==18 && _street_index==3) { 
 //BA.debugLineNum = 2133;BA.debugLine="lat = \"10.057640\"";
_lat = "10.057640";
 //BA.debugLineNum = 2134;BA.debugLine="lng = \"122.859242\"";
_lng = "122.859242";
 };
 //BA.debugLineNum = 2138;BA.debugLine="End Sub";
return "";
}
public static String  _update_all_inputs_click() throws Exception{
 //BA.debugLineNum = 681;BA.debugLine="Sub update_all_inputs_click";
 //BA.debugLineNum = 683;BA.debugLine="End Sub";
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
anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator _htp = null;
anywheresoftware.b4a.objects.collections.JSONParser _hps = null;
anywheresoftware.b4a.objects.collections.Map _maps = null;
anywheresoftware.b4a.objects.collections.List _jsonlist = null;
String _jsonstring = "";
 //BA.debugLineNum = 995;BA.debugLine="Sub update_btn_Click";
 //BA.debugLineNum = 996;BA.debugLine="panel_click_ = 0";
_panel_click_ = (int) (0);
 //BA.debugLineNum = 997;BA.debugLine="ProgressDialogShow2(\"Updating Please wait...\",Fal";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Updating Please wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 998;BA.debugLine="optionSelected = \"updated_click\"";
_optionselected = "updated_click";
 //BA.debugLineNum = 999;BA.debugLine="update_job.Initialize(\"update_job\",Me)";
mostCurrent._update_job._initialize(processBA,"update_job",menu_form.getObject());
 //BA.debugLineNum = 1000;BA.debugLine="update_img_job.Initialize(\"update_img_job\",Me)";
mostCurrent._update_img_job._initialize(processBA,"update_img_job",menu_form.getObject());
 //BA.debugLineNum = 1001;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 1002;BA.debugLine="Dim male_c,female_c As String";
_male_c = "";
_female_c = "";
 //BA.debugLineNum = 1003;BA.debugLine="male_c = File.GetText(File.DirAssets, \"male_strin";
_male_c = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"male_string.txt");
 //BA.debugLineNum = 1004;BA.debugLine="female_c = File.GetText(File.DirAssets, \"female_s";
_female_c = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"female_string.txt");
 //BA.debugLineNum = 1006;BA.debugLine="If image_container == male_c Or image_container =";
if ((_image_container).equals(_male_c) || (_image_container).equals(_female_c)) { 
 //BA.debugLineNum = 1007;BA.debugLine="Dim img_string As String";
_img_string = "";
 //BA.debugLineNum = 1008;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 1009;BA.debugLine="Dim out1 As OutputStream";
_out1 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 1010;BA.debugLine="If is_gender_index == 0 Then";
if (_is_gender_index==0) { 
 //BA.debugLineNum = 1011;BA.debugLine="out1.InitializeToBytesArray(0) 'size not real";
_out1.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 1012;BA.debugLine="File.Copy2(File.OpenInput(File.DirAssets, \"ma";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"male_clip.png").getObject()),(java.io.OutputStream)(_out1.getObject()));
 //BA.debugLineNum = 1013;BA.debugLine="img_string=su.EncodeBase64(out1.ToBytesArray)";
_img_string = _su.EncodeBase64(_out1.ToBytesArray());
 //BA.debugLineNum = 1015;BA.debugLine="image_container = img_string";
_image_container = _img_string;
 }else {
 //BA.debugLineNum = 1017;BA.debugLine="out1.InitializeToBytesArray(0) 'size not reall";
_out1.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 1018;BA.debugLine="File.Copy2(File.OpenInput(File.DirAssets, \"fe";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"female_clip.png").getObject()),(java.io.OutputStream)(_out1.getObject()));
 //BA.debugLineNum = 1019;BA.debugLine="img_string=su.EncodeBase64(out1.ToBytesArray)";
_img_string = _su.EncodeBase64(_out1.ToBytesArray());
 //BA.debugLineNum = 1021;BA.debugLine="image_container = img_string";
_image_container = _img_string;
 };
 }else {
 };
 //BA.debugLineNum = 1028;BA.debugLine="Dim Nmonth,Nday,Nyear,ageGet As Int";
_nmonth = 0;
_nday = 0;
_nyear = 0;
_ageget = 0;
 //BA.debugLineNum = 1029;BA.debugLine="Nday = DateTime.GetDayOfMonth(DateTime.Now)";
_nday = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1030;BA.debugLine="Nmonth = DateTime.GetMonth(DateTime.Now)";
_nmonth = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1031;BA.debugLine="Nyear = DateTime.GetYear(DateTime.Now)";
_nyear = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1033;BA.debugLine="Dim age,Pyear,Pmonth,Pday As Int";
_age = 0;
_pyear = 0;
_pmonth = 0;
_pday = 0;
 //BA.debugLineNum = 1034;BA.debugLine="Pyear = bday_year_selected";
_pyear = (int)(Double.parseDouble(_bday_year_selected));
 //BA.debugLineNum = 1035;BA.debugLine="Pmonth = bday_month_selected";
_pmonth = (int)(Double.parseDouble(_bday_month_selected));
 //BA.debugLineNum = 1036;BA.debugLine="Pday = bday_day_selected";
_pday = (int)(Double.parseDouble(_bday_day_selected));
 //BA.debugLineNum = 1037;BA.debugLine="age = Nyear - Pyear";
_age = (int) (_nyear-_pyear);
 //BA.debugLineNum = 1038;BA.debugLine="If Pmonth <= Nmonth And Pday <= Nday Then";
if (_pmonth<=_nmonth && _pday<=_nday) { 
 //BA.debugLineNum = 1040;BA.debugLine="ageGet = age";
_ageget = _age;
 }else {
 //BA.debugLineNum = 1042;BA.debugLine="ageGet = age-1";
_ageget = (int) (_age-1);
 };
 //BA.debugLineNum = 1046;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 1048;BA.debugLine="Dim ins,m_1,m_2,m_3,merge As String";
_ins = "";
_m_1 = "";
_m_2 = "";
_m_3 = "";
_merge = "";
 //BA.debugLineNum = 1050;BA.debugLine="If text_fn.Text == \"\"  Or text_email.Text == \"";
if ((mostCurrent._text_fn.getText()).equals("") || (mostCurrent._text_email.getText()).equals("") || (mostCurrent._text_phonenumber2.getText()).equals("") || (mostCurrent._text_phonenumber.getText()).equals("") || (mostCurrent._text_answer.getText()).equals("")) { 
 //BA.debugLineNum = 1051;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1052;BA.debugLine="Msgbox(\"Error: Fill up those empty fields before";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Fill up those empty fields before you update!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else if(_ageget<18) { 
 //BA.debugLineNum = 1054;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1055;BA.debugLine="Msgbox(\"Error: Your age must be 18 and above!\",\"";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Your age must be 18 and above!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else if(mostCurrent._text_phonenumber.getText().length()!=11 || mostCurrent._text_phonenumber2.getText().length()!=11) { 
 //BA.debugLineNum = 1057;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1058;BA.debugLine="Msgbox(\"Error: Phone number must be 11 digits!\",";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Phone number must be 11 digits!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 1060;BA.debugLine="m_1 = \"UPDATE `person_info` SET `full_name`='\"&t";
_m_1 = "UPDATE `person_info` SET `full_name`='"+mostCurrent._text_fn.getText()+"',`blood_type`='"+_blood_selected+"', `phone_number1`='"+mostCurrent._text_phonenumber.getText()+"', `phone_number2`='"+mostCurrent._text_phonenumber2.getText()+"', `location_brgy`='"+_location_brgy_selected+"', `location_street`='"+_location_street_selected+"', ";
 //BA.debugLineNum = 1061;BA.debugLine="m_2 = \"`location_purok`='', `bday_month`='\"&bday";
_m_2 = "`location_purok`='', `bday_month`='"+_bday_month_selected+"',`bday_day`='"+_bday_day_selected+"', `bday_year`='"+_bday_year_selected+"', `nick_name`='"+mostCurrent._text_answer.getText()+"', `donate_boolean`='"+_is_donated+"', `lat`='"+_lat+"', `long`='"+_lng+"', `image`='"+_image_container+"', ";
 //BA.debugLineNum = 1062;BA.debugLine="m_3 = \"`age`='\"&ageGet&\"',`date_donated`='\"&isDo";
_m_3 = "`age`='"+BA.NumberToString(_ageget)+"',`date_donated`='"+_isdonatedate+"',`gender`='"+_gender_string_data+"' WHERE `id`="+mostCurrent._login_form._id_query+";";
 //BA.debugLineNum = 1063;BA.debugLine="merge = m_1&m_2&m_3";
_merge = _m_1+_m_2+_m_3;
 //BA.debugLineNum = 1064;BA.debugLine="ins = url_back.php_email_url(\"updating.php\")";
_ins = _url_back._php_email_url("updating.php");
 //BA.debugLineNum = 1071;BA.debugLine="Dim htp As JSONGenerator";
_htp = new anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator();
 //BA.debugLineNum = 1072;BA.debugLine="Dim hps As JSONParser ''not used!!";
_hps = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 1073;BA.debugLine="Dim maps As Map";
_maps = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 1074;BA.debugLine="Dim JSONList As List";
_jsonlist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 1075;BA.debugLine="JSONList.Initialize";
_jsonlist.Initialize();
 //BA.debugLineNum = 1076;BA.debugLine="maps.Initialize";
_maps.Initialize();
 //BA.debugLineNum = 1077;BA.debugLine="maps.Put(\"text_fn\",text_fn.Text)";
_maps.Put((Object)("text_fn"),(Object)(mostCurrent._text_fn.getText()));
 //BA.debugLineNum = 1078;BA.debugLine="maps.Put(\"blood_type\",blood_selected)";
_maps.Put((Object)("blood_type"),(Object)(_blood_selected));
 //BA.debugLineNum = 1079;BA.debugLine="maps.Put(\"phone_number1\",text_phonenumber.Text)";
_maps.Put((Object)("phone_number1"),(Object)(mostCurrent._text_phonenumber.getText()));
 //BA.debugLineNum = 1080;BA.debugLine="maps.Put(\"phone_number2\",text_phonenumber2.Text";
_maps.Put((Object)("phone_number2"),(Object)(mostCurrent._text_phonenumber2.getText()));
 //BA.debugLineNum = 1081;BA.debugLine="maps.Put(\"location_brgy\",location_brgy_selected";
_maps.Put((Object)("location_brgy"),(Object)(_location_brgy_selected));
 //BA.debugLineNum = 1082;BA.debugLine="maps.Put(\"location_street\",location_street_sele";
_maps.Put((Object)("location_street"),(Object)(_location_street_selected));
 //BA.debugLineNum = 1083;BA.debugLine="maps.Put(\"location_purok\",\"Him. City\")";
_maps.Put((Object)("location_purok"),(Object)("Him. City"));
 //BA.debugLineNum = 1084;BA.debugLine="maps.Put(\"bday_month\",bday_month_selected)";
_maps.Put((Object)("bday_month"),(Object)(_bday_month_selected));
 //BA.debugLineNum = 1085;BA.debugLine="maps.Put(\"bday_day\",bday_day_selected)";
_maps.Put((Object)("bday_day"),(Object)(_bday_day_selected));
 //BA.debugLineNum = 1086;BA.debugLine="maps.Put(\"bday_year\",bday_year_selected)";
_maps.Put((Object)("bday_year"),(Object)(_bday_year_selected));
 //BA.debugLineNum = 1087;BA.debugLine="maps.Put(\"nick_name\",text_answer.Text)";
_maps.Put((Object)("nick_name"),(Object)(mostCurrent._text_answer.getText()));
 //BA.debugLineNum = 1088;BA.debugLine="maps.Put(\"donate_boolean\",is_donated)";
_maps.Put((Object)("donate_boolean"),(Object)(_is_donated));
 //BA.debugLineNum = 1089;BA.debugLine="maps.Put(\"lat\",lat)";
_maps.Put((Object)("lat"),(Object)(_lat));
 //BA.debugLineNum = 1090;BA.debugLine="maps.Put(\"long\",lng)";
_maps.Put((Object)("long"),(Object)(_lng));
 //BA.debugLineNum = 1091;BA.debugLine="maps.Put(\"image\",image_container)";
_maps.Put((Object)("image"),(Object)(_image_container));
 //BA.debugLineNum = 1092;BA.debugLine="maps.Put(\"age\",ageGet)";
_maps.Put((Object)("age"),(Object)(_ageget));
 //BA.debugLineNum = 1093;BA.debugLine="maps.Put(\"date_donated\",isDonateDate)";
_maps.Put((Object)("date_donated"),(Object)(_isdonatedate));
 //BA.debugLineNum = 1094;BA.debugLine="maps.Put(\"gender\",gender_string_data)";
_maps.Put((Object)("gender"),(Object)(_gender_string_data));
 //BA.debugLineNum = 1095;BA.debugLine="maps.Put(\"id\",login_form.id_query)";
_maps.Put((Object)("id"),(Object)(mostCurrent._login_form._id_query));
 //BA.debugLineNum = 1096;BA.debugLine="JSONList.Add(maps)";
_jsonlist.Add((Object)(_maps.getObject()));
 //BA.debugLineNum = 1098;BA.debugLine="htp.Initialize2(JSONList)";
_htp.Initialize2(_jsonlist);
 //BA.debugLineNum = 1099;BA.debugLine="Dim JSONstring As String";
_jsonstring = "";
 //BA.debugLineNum = 1100;BA.debugLine="JSONstring = htp.ToString";
_jsonstring = _htp.ToString();
 //BA.debugLineNum = 1102;BA.debugLine="update_img_job.PostString(ins,\"update=\"&htp.ToSt";
mostCurrent._update_img_job._poststring(_ins,"update="+_htp.ToString());
 };
 //BA.debugLineNum = 1108;BA.debugLine="End Sub";
return "";
}
public static String  _user_image_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _user_imgclose_btn = null;
anywheresoftware.b4a.objects.WebViewWrapper _img_user_webview = null;
anywheresoftware.b4a.objects.ImageViewWrapper _user_img_view = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _inp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
anywheresoftware.b4a.objects.StringUtils _su = null;
byte[] _bytes = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
 //BA.debugLineNum = 3048;BA.debugLine="Sub user_image_click";
 //BA.debugLineNum = 3049;BA.debugLine="list_all_select = 3";
_list_all_select = (int) (3);
 //BA.debugLineNum = 3050;BA.debugLine="userI_a3.Start(user_image)";
mostCurrent._useri_a3.Start((android.view.View)(mostCurrent._user_image.getObject()));
 //BA.debugLineNum = 3051;BA.debugLine="If user_img_panl.IsInitialized == True Then";
if (mostCurrent._user_img_panl.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 3052;BA.debugLine="user_img_panl.RemoveView";
mostCurrent._user_img_panl.RemoveView();
 };
 //BA.debugLineNum = 3054;BA.debugLine="user_img_panl.Initialize(\"user_img_panl\")";
mostCurrent._user_img_panl.Initialize(mostCurrent.activityBA,"user_img_panl");
 //BA.debugLineNum = 3055;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 3056;BA.debugLine="Dim user_imgClose_btn As Button";
_user_imgclose_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 3057;BA.debugLine="Dim img_user_webview As WebView";
_img_user_webview = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 3058;BA.debugLine="Dim user_img_view As ImageView";
_user_img_view = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 3059;BA.debugLine="user_imgClose_btn.Initialize(\"user_imgClose_btn\")";
_user_imgclose_btn.Initialize(mostCurrent.activityBA,"user_imgClose_btn");
 //BA.debugLineNum = 3060;BA.debugLine="user_img_view.Initialize(\"user_img_view\")";
_user_img_view.Initialize(mostCurrent.activityBA,"user_img_view");
 //BA.debugLineNum = 3061;BA.debugLine="user_imgClose_btn.Text = \"CLOSE\"";
_user_imgclose_btn.setText((Object)("CLOSE"));
 //BA.debugLineNum = 3062;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 3063;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 3064;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 3065;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 3066;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 3067;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 3068;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 3069;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 3070;BA.debugLine="user_imgClose_btn.Background = V_btn";
_user_imgclose_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 3071;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 3072;BA.debugLine="img_user_webview.Initialize(\"img_user_webview\")";
_img_user_webview.Initialize(mostCurrent.activityBA,"img_user_webview");
 //BA.debugLineNum = 3073;BA.debugLine="user_img_panl.Color = Colors.Transparent";
mostCurrent._user_img_panl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 3074;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 3076;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 3077;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 3078;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 3079;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 3080;BA.debugLine="bytes = su.DecodeBase64(image_list.Get(0))";
_bytes = _su.DecodeBase64(BA.ObjectToString(mostCurrent._image_list.Get((int) (0))));
 //BA.debugLineNum = 3081;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 3082;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 3084;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 3085;BA.debugLine="bd.Initialize(bmp)";
_bd.Initialize((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 3087;BA.debugLine="user_img_view.Background = bd";
_user_img_view.setBackground((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 3092;BA.debugLine="pnl.AddView(user_img_view,1%x,1%y,86%x,75%y)";
_pnl.AddView((android.view.View)(_user_img_view.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (86),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (75),mostCurrent.activityBA));
 //BA.debugLineNum = 3093;BA.debugLine="pnl.AddView(user_imgClose_btn,1%x,user_img_view.T";
_pnl.AddView((android.view.View)(_user_imgclose_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_user_img_view.getTop()+_user_img_view.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (86),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 3094;BA.debugLine="user_img_panl.AddView(pnl,6%x,(((((Activity.Heigh";
mostCurrent._user_img_panl.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),(int) ((((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (88),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (89),mostCurrent.activityBA));
 //BA.debugLineNum = 3095;BA.debugLine="user_img_panl.BringToFront";
mostCurrent._user_img_panl.BringToFront();
 //BA.debugLineNum = 3098;BA.debugLine="Activity.AddView(user_img_panl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._user_img_panl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 3099;BA.debugLine="End Sub";
return "";
}
public static String  _user_imgclose_btn_click() throws Exception{
 //BA.debugLineNum = 3100;BA.debugLine="Sub user_imgClose_btn_click";
 //BA.debugLineNum = 3101;BA.debugLine="list_all_select = 2";
_list_all_select = (int) (2);
 //BA.debugLineNum = 3102;BA.debugLine="user_img_panl.RemoveView";
mostCurrent._user_img_panl.RemoveView();
 //BA.debugLineNum = 3103;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 801;BA.debugLine="Sub usr_img_click";
 //BA.debugLineNum = 802;BA.debugLine="Try";
try { //BA.debugLineNum = 804;BA.debugLine="userImage.InitializeAlpha(\"\", 1, 0)";
mostCurrent._userimage.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 805;BA.debugLine="usr_img.Tag = userImage";
mostCurrent._usr_img.setTag((Object)(mostCurrent._userimage.getObject()));
 //BA.debugLineNum = 806;BA.debugLine="userImage.Duration = 300";
mostCurrent._userimage.setDuration((long) (300));
 //BA.debugLineNum = 807;BA.debugLine="userImage.RepeatCount = 1";
mostCurrent._userimage.setRepeatCount((int) (1));
 //BA.debugLineNum = 808;BA.debugLine="userImage.RepeatMode = userImage.REPEAT_REVERSE";
mostCurrent._userimage.setRepeatMode(mostCurrent._userimage.REPEAT_REVERSE);
 //BA.debugLineNum = 809;BA.debugLine="userImage.Start(usr_img)";
mostCurrent._userimage.Start((android.view.View)(mostCurrent._usr_img.getObject()));
 //BA.debugLineNum = 810;BA.debugLine="dlgFileExpl.Initialize(Activity, \"/mnt/sdcard\", \"";
mostCurrent._dlgfileexpl._initialize(mostCurrent.activityBA,mostCurrent._activity,"/mnt/sdcard",".bmp,.gif,.jpg,.png",anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.False,"OK");
 //BA.debugLineNum = 811;BA.debugLine="dlgFileExpl.FastScrollEnabled = True";
mostCurrent._dlgfileexpl._fastscrollenabled = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 812;BA.debugLine="dlgFileExpl.Explorer2(True)";
mostCurrent._dlgfileexpl._explorer2(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 813;BA.debugLine="If Not(dlgFileExpl.Selection.Canceled Or dlgFileE";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._dlgfileexpl._selection.Canceled || (mostCurrent._dlgfileexpl._selection.ChosenFile).equals(""))) { 
 //BA.debugLineNum = 815;BA.debugLine="Dim img_string As String";
_img_string = "";
 //BA.debugLineNum = 816;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 817;BA.debugLine="Dim out1 As OutputStream";
_out1 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 819;BA.debugLine="out1.InitializeToBytesArray(0) 'size not real";
_out1.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 820;BA.debugLine="File.Copy2(File.OpenInput(dlgFileExpl.Selecti";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(mostCurrent._dlgfileexpl._selection.ChosenPath,mostCurrent._dlgfileexpl._selection.ChosenFile).getObject()),(java.io.OutputStream)(_out1.getObject()));
 //BA.debugLineNum = 821;BA.debugLine="img_string=su.EncodeBase64(out1.ToBytesArray)";
_img_string = _su.EncodeBase64(_out1.ToBytesArray());
 //BA.debugLineNum = 823;BA.debugLine="image_container = img_string";
_image_container = _img_string;
 //BA.debugLineNum = 825;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 826;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 827;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 828;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 829;BA.debugLine="bytes = su.DecodeBase64(img_string)";
_bytes = _su.DecodeBase64(_img_string);
 //BA.debugLineNum = 830;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 831;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 833;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 834;BA.debugLine="bd.Initialize(bmp)";
_bd.Initialize((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 836;BA.debugLine="usr_img.Background = bd";
mostCurrent._usr_img.setBackground((android.graphics.drawable.Drawable)(_bd.getObject()));
 };
 } 
       catch (Exception e31) {
			processBA.setLastException(e31); //BA.debugLineNum = 841;BA.debugLine="Msgbox(\"Image is too big... can't parse..!\",\"C O";
anywheresoftware.b4a.keywords.Common.Msgbox("Image is too big... can't parse..!","C O N F I R M A T I O N",mostCurrent.activityBA);
 //BA.debugLineNum = 842;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage());
 };
 //BA.debugLineNum = 844;BA.debugLine="End Sub";
return "";
}
public static String  _vie_btn_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _view_panl = null;
anywheresoftware.b4a.objects.PanelWrapper _view_for_image = null;
anywheresoftware.b4a.objects.PanelWrapper _view_for_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _fullname = null;
anywheresoftware.b4a.objects.LabelWrapper _location = null;
anywheresoftware.b4a.objects.LabelWrapper _donated = null;
anywheresoftware.b4a.objects.LabelWrapper _email = null;
anywheresoftware.b4a.objects.LabelWrapper _age = null;
anywheresoftware.b4a.objects.LabelWrapper _gender = null;
anywheresoftware.b4a.objects.PanelWrapper _fn_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _loc_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _don_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _ema_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _btn_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _age_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _gender_pnl = null;
anywheresoftware.b4a.objects.ImageViewWrapper _fn_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _loc_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _don_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _ema_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _ph1_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _ph2_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _age_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _gender_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _delete_bookmark = null;
anywheresoftware.b4a.objects.ButtonWrapper _ok_vie_btn = null;
anywheresoftware.b4a.objects.collections.List _fulln_llist = null;
anywheresoftware.b4a.objects.collections.List _location_list = null;
anywheresoftware.b4a.objects.collections.List _donated_list = null;
anywheresoftware.b4a.objects.collections.List _email_list = null;
anywheresoftware.b4a.objects.collections.List _phone1_list = null;
anywheresoftware.b4a.objects.collections.List _phone2_list = null;
anywheresoftware.b4a.objects.collections.List _age_list = null;
anywheresoftware.b4a.objects.collections.List _gender_list = null;
anywheresoftware.b4a.sql.SQL.CursorWrapper _set_cursor = null;
int _i = 0;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _loc_bitd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _don_bitd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _ema_bitd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _ph1_bitd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _ph2_bitd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _age_bitd = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _gender_bitd = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _loc_bit = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _don_bit = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _ema_bit = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _ph1_bit = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _ph2_bit = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _age_bit = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _gender_bit = null;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _inp = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmp = null;
anywheresoftware.b4a.objects.StringUtils _su = null;
byte[] _bytes = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bd = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _fn_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _don_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _ema_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _ph1_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _ph2_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _loc_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _btn_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _ok_btn_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _age_grad = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _gender_grad = null;
int[] _colorg = null;
int[] _btn_color = null;
int[] _panl_btn = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _bitmd = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitm = null;
 //BA.debugLineNum = 2756;BA.debugLine="Sub vie_btn_click";
 //BA.debugLineNum = 2757;BA.debugLine="list_all_select = 2";
_list_all_select = (int) (2);
 //BA.debugLineNum = 2758;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 //BA.debugLineNum = 2759;BA.debugLine="If view_data_info_person.IsInitialized == True T";
if (mostCurrent._view_data_info_person.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 2760;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 2761;BA.debugLine="scroll_view_info.RemoveView";
mostCurrent._scroll_view_info.RemoveView();
 }else {
 };
 //BA.debugLineNum = 2764;BA.debugLine="view_data_info_person.Initialize(\"view_data_info_";
mostCurrent._view_data_info_person.Initialize(mostCurrent.activityBA,"view_data_info_person");
 //BA.debugLineNum = 2765;BA.debugLine="scroll_view_info.Initialize(74%x,57%y,\"scroll_vie";
mostCurrent._scroll_view_info.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (57),mostCurrent.activityBA),"scroll_view_info");
 //BA.debugLineNum = 2767;BA.debugLine="Dim view_panl,view_for_image,view_for_btn As Pane";
_view_panl = new anywheresoftware.b4a.objects.PanelWrapper();
_view_for_image = new anywheresoftware.b4a.objects.PanelWrapper();
_view_for_btn = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2768;BA.debugLine="Dim tittle,fullname,location,donated,email,age,ge";
mostCurrent._tittle = new anywheresoftware.b4a.objects.LabelWrapper();
_fullname = new anywheresoftware.b4a.objects.LabelWrapper();
_location = new anywheresoftware.b4a.objects.LabelWrapper();
_donated = new anywheresoftware.b4a.objects.LabelWrapper();
_email = new anywheresoftware.b4a.objects.LabelWrapper();
_age = new anywheresoftware.b4a.objects.LabelWrapper();
_gender = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 2769;BA.debugLine="Dim fn_pnl,loc_pnl,don_pnl,ema_pnl,btn_pnl,age_pn";
_fn_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_loc_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_don_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_ema_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_btn_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_age_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_gender_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 2770;BA.debugLine="Dim fn_img,loc_img,don_img,ema_img,ph1_img,ph2_im";
_fn_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_loc_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_don_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ema_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ph1_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ph2_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_age_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_gender_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_delete_bookmark = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 2771;BA.debugLine="fn_img.Initialize(\"\")";
_fn_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2772;BA.debugLine="loc_img.Initialize(\"\")";
_loc_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2773;BA.debugLine="don_img.Initialize(\"\")";
_don_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2774;BA.debugLine="ema_img.Initialize(\"\")";
_ema_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2775;BA.debugLine="ph1_img.Initialize(\"\")";
_ph1_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2776;BA.debugLine="ph2_img.Initialize(\"\")";
_ph2_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2777;BA.debugLine="user_image.Initialize(\"user_image\")";
mostCurrent._user_image.Initialize(mostCurrent.activityBA,"user_image");
 //BA.debugLineNum = 2778;BA.debugLine="age_img.Initialize(\"\")";
_age_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2779;BA.debugLine="gender_img.Initialize(\"\")";
_gender_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2780;BA.debugLine="delete_bookmark.Initialize(\"delete_bookmark";
_delete_bookmark.Initialize(mostCurrent.activityBA,"delete_bookmark");
 //BA.debugLineNum = 2781;BA.debugLine="fn_pnl.Initialize(\"\")";
_fn_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2782;BA.debugLine="loc_pnl.Initialize(\"\")";
_loc_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2783;BA.debugLine="don_pnl.Initialize(\"\")";
_don_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2784;BA.debugLine="ema_pnl.Initialize(\"\")";
_ema_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2785;BA.debugLine="ph1_pnl.Initialize(\"phone1_view_call\")";
mostCurrent._ph1_pnl.Initialize(mostCurrent.activityBA,"phone1_view_call");
 //BA.debugLineNum = 2786;BA.debugLine="ph2_pnl.Initialize(\"phone2_view_call\")";
mostCurrent._ph2_pnl.Initialize(mostCurrent.activityBA,"phone2_view_call");
 //BA.debugLineNum = 2787;BA.debugLine="btn_pnl.Initialize(\"\")";
_btn_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2788;BA.debugLine="age_pnl.Initialize(\"\")";
_age_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2789;BA.debugLine="gender_pnl.Initialize(\"\")";
_gender_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2790;BA.debugLine="Dim ok_vie_btn As Button";
_ok_vie_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 2791;BA.debugLine="ok_vie_btn.Initialize(\"ok_vie_btn\")";
_ok_vie_btn.Initialize(mostCurrent.activityBA,"ok_vie_btn");
 //BA.debugLineNum = 2792;BA.debugLine="tittle.Initialize(\"\")";
mostCurrent._tittle.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2793;BA.debugLine="fullname.Initialize(\"\")";
_fullname.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2794;BA.debugLine="location.Initialize(\"\")";
_location.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2795;BA.debugLine="donated.Initialize(\"\")";
_donated.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2796;BA.debugLine="email.Initialize(\"\")";
_email.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2797;BA.debugLine="phone1.Initialize(\"\")";
mostCurrent._phone1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2798;BA.debugLine="phone2.Initialize(\"\")";
mostCurrent._phone2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2799;BA.debugLine="age.Initialize(\"\")";
_age.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2800;BA.debugLine="gender.Initialize(\"\")";
_gender.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2801;BA.debugLine="view_panl.Initialize(\"\")";
_view_panl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2802;BA.debugLine="view_for_image.Initialize(\"\")";
_view_for_image.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2803;BA.debugLine="view_for_btn.Initialize(\"\")";
_view_for_btn.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 2806;BA.debugLine="Dim fullN_llist,location_list,donated_list,email_";
_fulln_llist = new anywheresoftware.b4a.objects.collections.List();
_location_list = new anywheresoftware.b4a.objects.collections.List();
_donated_list = new anywheresoftware.b4a.objects.collections.List();
_email_list = new anywheresoftware.b4a.objects.collections.List();
_phone1_list = new anywheresoftware.b4a.objects.collections.List();
_phone2_list = new anywheresoftware.b4a.objects.collections.List();
_age_list = new anywheresoftware.b4a.objects.collections.List();
_gender_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 2807;BA.debugLine="fullN_llist.Initialize";
_fulln_llist.Initialize();
 //BA.debugLineNum = 2808;BA.debugLine="location_list.Initialize";
_location_list.Initialize();
 //BA.debugLineNum = 2809;BA.debugLine="donated_list.Initialize";
_donated_list.Initialize();
 //BA.debugLineNum = 2810;BA.debugLine="email_list.Initialize";
_email_list.Initialize();
 //BA.debugLineNum = 2811;BA.debugLine="phone1_list.Initialize";
_phone1_list.Initialize();
 //BA.debugLineNum = 2812;BA.debugLine="phone2_list.Initialize";
_phone2_list.Initialize();
 //BA.debugLineNum = 2813;BA.debugLine="age_list.Initialize";
_age_list.Initialize();
 //BA.debugLineNum = 2814;BA.debugLine="gender_list.Initialize";
_gender_list.Initialize();
 //BA.debugLineNum = 2815;BA.debugLine="image_list.Initialize";
mostCurrent._image_list.Initialize();
 //BA.debugLineNum = 2816;BA.debugLine="If sqlLite.IsInitialized = True Then";
if (_sqllite.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 2817;BA.debugLine="Dim set_cursor As Cursor";
_set_cursor = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 2818;BA.debugLine="set_cursor = sqlLite.ExecQuery(\"select * from b";
_set_cursor.setObject((android.database.Cursor)(_sqllite.ExecQuery("select * from bookmarks where `users_id`='"+BA.ObjectToString(_bookmark_id_list.Get(_row_click))+"';")));
 //BA.debugLineNum = 2819;BA.debugLine="For i = 0 To set_cursor.RowCount - 1";
{
final int step60 = 1;
final int limit60 = (int) (_set_cursor.getRowCount()-1);
for (_i = (int) (0) ; (step60 > 0 && _i <= limit60) || (step60 < 0 && _i >= limit60); _i = ((int)(0 + _i + step60)) ) {
 //BA.debugLineNum = 2820;BA.debugLine="set_cursor.Position = i";
_set_cursor.setPosition(_i);
 //BA.debugLineNum = 2822;BA.debugLine="fullN_llist.Add(set_cursor.GetString(\"full";
_fulln_llist.Add((Object)(_set_cursor.GetString("full_name")));
 //BA.debugLineNum = 2823;BA.debugLine="location_list.Add(set_cursor.GetString(\"lo";
_location_list.Add((Object)(_set_cursor.GetString("location")));
 //BA.debugLineNum = 2824;BA.debugLine="donated_list.Add(set_cursor.GetString(\"is_";
_donated_list.Add((Object)(_set_cursor.GetString("is_donated")));
 //BA.debugLineNum = 2825;BA.debugLine="email_list.Add(set_cursor.GetString(\"email";
_email_list.Add((Object)(_set_cursor.GetString("email")));
 //BA.debugLineNum = 2826;BA.debugLine="phone1_list.Add(set_cursor.GetString(\"ph_n";
_phone1_list.Add((Object)(_set_cursor.GetString("ph_number1")));
 //BA.debugLineNum = 2827;BA.debugLine="phone2_list.Add(set_cursor.GetString(\"ph_n";
_phone2_list.Add((Object)(_set_cursor.GetString("ph_number2")));
 //BA.debugLineNum = 2828;BA.debugLine="age_list.Add(set_cursor.GetString(\"age\"))";
_age_list.Add((Object)(_set_cursor.GetString("age")));
 //BA.debugLineNum = 2829;BA.debugLine="gender_list.Add(set_cursor.GetString(\"gend";
_gender_list.Add((Object)(_set_cursor.GetString("gender")));
 //BA.debugLineNum = 2830;BA.debugLine="image_list.Add(set_cursor.GetString(\"image";
mostCurrent._image_list.Add((Object)(_set_cursor.GetString("image")));
 }
};
 };
 //BA.debugLineNum = 2834;BA.debugLine="fullname.Text = fullN_llist.Get(0)			'string outp";
_fullname.setText(_fulln_llist.Get((int) (0)));
 //BA.debugLineNum = 2835;BA.debugLine="location.Text = \": \"&location_list.Get(0)";
_location.setText((Object)(": "+BA.ObjectToString(_location_list.Get((int) (0)))));
 //BA.debugLineNum = 2836;BA.debugLine="donated.Text = \": \"&donated_list.Get(0)";
_donated.setText((Object)(": "+BA.ObjectToString(_donated_list.Get((int) (0)))));
 //BA.debugLineNum = 2837;BA.debugLine="email.Text = \": \"&email_list.Get(0)";
_email.setText((Object)(": "+BA.ObjectToString(_email_list.Get((int) (0)))));
 //BA.debugLineNum = 2838;BA.debugLine="phone1.Text = \": \"&phone1_list.Get(0)";
mostCurrent._phone1.setText((Object)(": "+BA.ObjectToString(_phone1_list.Get((int) (0)))));
 //BA.debugLineNum = 2839;BA.debugLine="phone2.Text = \": \"&phone2_list.Get(0)   	'string";
mostCurrent._phone2.setText((Object)(": "+BA.ObjectToString(_phone2_list.Get((int) (0)))));
 //BA.debugLineNum = 2840;BA.debugLine="age.Text = \": \"&age_list.Get(0)";
_age.setText((Object)(": "+BA.ObjectToString(_age_list.Get((int) (0)))));
 //BA.debugLineNum = 2841;BA.debugLine="gender.Text = \": \"&gender_list.Get(0)";
_gender.setText((Object)(": "+BA.ObjectToString(_gender_list.Get((int) (0)))));
 //BA.debugLineNum = 2842;BA.debugLine="location.Gravity = Gravity.CENTER_VERTICAL";
_location.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 2843;BA.debugLine="donated.Gravity = Gravity.CENTER_VERTICAL";
_donated.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 2844;BA.debugLine="email.Gravity = Gravity.CENTER_VERTICAL";
_email.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 2845;BA.debugLine="phone1.Gravity = Gravity.CENTER_VERTICAL";
mostCurrent._phone1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 2846;BA.debugLine="phone2.Gravity = Gravity.CENTER_VERTICAL";
mostCurrent._phone2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 2847;BA.debugLine="age.Gravity = Gravity.CENTER_VERTICAL";
_age.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 2848;BA.debugLine="gender.Gravity = Gravity.CENTER_VERTICAL";
_gender.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 2849;BA.debugLine="fullname.TextColor = Colors.Black";
_fullname.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2850;BA.debugLine="location.TextColor = Colors.Black";
_location.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2851;BA.debugLine="donated.TextColor = Colors.Black";
_donated.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2852;BA.debugLine="email.TextColor = Colors.Black";
_email.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2853;BA.debugLine="phone1.TextColor = Colors.Black";
mostCurrent._phone1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2854;BA.debugLine="phone2.TextColor = Colors.Black";
mostCurrent._phone2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2855;BA.debugLine="ok_vie_btn.TextColor = Colors.Black";
_ok_vie_btn.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2856;BA.debugLine="age.TextColor = Colors.Black";
_age.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2857;BA.debugLine="gender.TextColor = Colors.Black";
_gender.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 2858;BA.debugLine="location.Typeface = Typeface.LoadFromAssets(\"";
_location.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 2859;BA.debugLine="donated.Typeface = Typeface.LoadFromAssets(\"Z";
_donated.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 2860;BA.debugLine="email.Typeface = Typeface.LoadFromAssets(\"ZIN";
_email.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 2861;BA.debugLine="phone1.Typeface = Typeface.LoadFromAssets(\"ZI";
mostCurrent._phone1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 2862;BA.debugLine="phone2.Typeface = Typeface.LoadFromAssets(\"ZI";
mostCurrent._phone2.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 2863;BA.debugLine="age.Typeface = Typeface.LoadFromAssets(\"ZINGH";
_age.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 2864;BA.debugLine="gender.Typeface = Typeface.LoadFromAssets(\"ZI";
_gender.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 2867;BA.debugLine="fullname.Gravity = Gravity.CENTER";
_fullname.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 2868;BA.debugLine="ok_vie_btn.Text = \"OK\"";
_ok_vie_btn.setText((Object)("OK"));
 //BA.debugLineNum = 2869;BA.debugLine="ok_vie_btn.Typeface = Typeface.LoadFromAssets(\"Hi";
_ok_vie_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 2870;BA.debugLine="view_panl.SetBackgroundImage(LoadBitmap(File.DirA";
_view_panl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 2877;BA.debugLine="Dim loc_bitd,don_bitd,ema_bitd,ph1_bitd,ph2_bi";
_loc_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_don_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_ema_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_ph1_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_ph2_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_age_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_gender_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 2878;BA.debugLine="Dim loc_bit,don_bit,ema_bit,ph1_bit,ph2_bit,ag";
_loc_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_don_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ema_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ph1_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ph2_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_age_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_gender_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 2879;BA.debugLine="loc_bit.Initialize(File.DirAssets,\"glyphicons";
_loc_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-21-home.png");
 //BA.debugLineNum = 2880;BA.debugLine="don_bit.Initialize(File.DirAssets,\"glyphicons";
_don_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-152-new-window.png");
 //BA.debugLineNum = 2881;BA.debugLine="ema_bit.Initialize(File.DirAssets,\"glyphicons";
_ema_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-social-40-e-mail.png");
 //BA.debugLineNum = 2882;BA.debugLine="ph1_bit.Initialize(File.DirAssets,\"glyphicons";
_ph1_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt1.png");
 //BA.debugLineNum = 2883;BA.debugLine="ph2_bit.Initialize(File.DirAssets,\"glyphicons";
_ph2_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt2.png");
 //BA.debugLineNum = 2884;BA.debugLine="age_bit.Initialize(File.DirAssets,\"glyphicons";
_age_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-577-uk-rat-r18.png");
 //BA.debugLineNum = 2885;BA.debugLine="gender_bit.Initialize(File.DirAssets,\"glyphic";
_gender_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-25-parents.png");
 //BA.debugLineNum = 2886;BA.debugLine="loc_bitd.Initialize(loc_bit)";
_loc_bitd.Initialize((android.graphics.Bitmap)(_loc_bit.getObject()));
 //BA.debugLineNum = 2887;BA.debugLine="don_bitd.Initialize(don_bit)";
_don_bitd.Initialize((android.graphics.Bitmap)(_don_bit.getObject()));
 //BA.debugLineNum = 2888;BA.debugLine="ema_bitd.Initialize(ema_bit)";
_ema_bitd.Initialize((android.graphics.Bitmap)(_ema_bit.getObject()));
 //BA.debugLineNum = 2889;BA.debugLine="ph1_bitd.Initialize(ph1_bit)";
_ph1_bitd.Initialize((android.graphics.Bitmap)(_ph1_bit.getObject()));
 //BA.debugLineNum = 2890;BA.debugLine="ph2_bitd.Initialize(ph2_bit)";
_ph2_bitd.Initialize((android.graphics.Bitmap)(_ph2_bit.getObject()));
 //BA.debugLineNum = 2891;BA.debugLine="age_bitd.Initialize(age_bit)";
_age_bitd.Initialize((android.graphics.Bitmap)(_age_bit.getObject()));
 //BA.debugLineNum = 2892;BA.debugLine="gender_bitd.Initialize(gender_bit)";
_gender_bitd.Initialize((android.graphics.Bitmap)(_gender_bit.getObject()));
 //BA.debugLineNum = 2893;BA.debugLine="loc_img.Background = loc_bitd";
_loc_img.setBackground((android.graphics.drawable.Drawable)(_loc_bitd.getObject()));
 //BA.debugLineNum = 2894;BA.debugLine="don_img.Background = don_bitd";
_don_img.setBackground((android.graphics.drawable.Drawable)(_don_bitd.getObject()));
 //BA.debugLineNum = 2895;BA.debugLine="ema_img.Background = ema_bitd";
_ema_img.setBackground((android.graphics.drawable.Drawable)(_ema_bitd.getObject()));
 //BA.debugLineNum = 2896;BA.debugLine="ph1_img.Background = ph1_bitd";
_ph1_img.setBackground((android.graphics.drawable.Drawable)(_ph1_bitd.getObject()));
 //BA.debugLineNum = 2897;BA.debugLine="ph2_img.Background = ph2_bitd";
_ph2_img.setBackground((android.graphics.drawable.Drawable)(_ph2_bitd.getObject()));
 //BA.debugLineNum = 2898;BA.debugLine="age_img.Background = age_bitd";
_age_img.setBackground((android.graphics.drawable.Drawable)(_age_bitd.getObject()));
 //BA.debugLineNum = 2899;BA.debugLine="gender_img.Background = gender_bitd";
_gender_img.setBackground((android.graphics.drawable.Drawable)(_gender_bitd.getObject()));
 //BA.debugLineNum = 2900;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 2901;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 2902;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 2903;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 2904;BA.debugLine="bytes = su.DecodeBase64(image_list.Get(0))";
_bytes = _su.DecodeBase64(BA.ObjectToString(mostCurrent._image_list.Get((int) (0))));
 //BA.debugLineNum = 2905;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 2906;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 2908;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 2909;BA.debugLine="bd.Initialize(bmp)";
_bd.Initialize((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 2910;BA.debugLine="user_image.Background = bd";
mostCurrent._user_image.setBackground((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 2913;BA.debugLine="Dim fn_grad,don_grad,ema_grad,ph1_grad,ph2_grad";
_fn_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_don_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_ema_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_ph1_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_ph2_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_loc_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_btn_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_ok_btn_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_age_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_gender_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 2914;BA.debugLine="Dim colorG(2),btn_color(2),panl_btn(2) As Int";
_colorg = new int[(int) (2)];
;
_btn_color = new int[(int) (2)];
;
_panl_btn = new int[(int) (2)];
;
 //BA.debugLineNum = 2915;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 2916;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 2917;BA.debugLine="btn_color(0) = Colors.Red";
_btn_color[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 2918;BA.debugLine="btn_color(1) = Colors.White";
_btn_color[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 2919;BA.debugLine="panl_btn(0) = Colors.Gray";
_panl_btn[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Gray;
 //BA.debugLineNum = 2920;BA.debugLine="panl_btn(1) = Colors.Red";
_panl_btn[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 2921;BA.debugLine="fn_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_fn_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2922;BA.debugLine="don_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_don_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2923;BA.debugLine="ema_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_ema_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2924;BA.debugLine="ph1_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_ph1_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2925;BA.debugLine="ph2_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_ph2_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2926;BA.debugLine="loc_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_loc_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2927;BA.debugLine="age_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_age_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2928;BA.debugLine="gender_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_gender_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 2929;BA.debugLine="btn_grad.Initialize(\"TOP_BOTTOM\",panl_btn)";
_btn_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_panl_btn);
 //BA.debugLineNum = 2930;BA.debugLine="ok_btn_grad.Initialize(\"TOP_BOTTOM\",btn_color)";
_ok_btn_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_btn_color);
 //BA.debugLineNum = 2931;BA.debugLine="fn_grad.CornerRadius = 10dip";
_fn_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 2932;BA.debugLine="ok_btn_grad.CornerRadius = 50dip";
_ok_btn_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 2933;BA.debugLine="fn_pnl.Background = fn_grad		'fn_pnl.Color = Col";
_fn_pnl.setBackground((android.graphics.drawable.Drawable)(_fn_grad.getObject()));
 //BA.debugLineNum = 2934;BA.debugLine="don_pnl.Background = don_grad		'don_pnl.Color =";
_don_pnl.setBackground((android.graphics.drawable.Drawable)(_don_grad.getObject()));
 //BA.debugLineNum = 2935;BA.debugLine="ema_pnl.Background = ema_grad		'ema_pnl.Color =";
_ema_pnl.setBackground((android.graphics.drawable.Drawable)(_ema_grad.getObject()));
 //BA.debugLineNum = 2936;BA.debugLine="ph1_pnl.Background = ph1_grad		'ph1_pnl.Color =";
mostCurrent._ph1_pnl.setBackground((android.graphics.drawable.Drawable)(_ph1_grad.getObject()));
 //BA.debugLineNum = 2937;BA.debugLine="ph2_pnl.Background = ph2_grad		'ph2_pnl.Color =";
mostCurrent._ph2_pnl.setBackground((android.graphics.drawable.Drawable)(_ph2_grad.getObject()));
 //BA.debugLineNum = 2938;BA.debugLine="loc_pnl.Background = loc_grad		'loc_pnl.Color =";
_loc_pnl.setBackground((android.graphics.drawable.Drawable)(_loc_grad.getObject()));
 //BA.debugLineNum = 2939;BA.debugLine="age_pnl.Background = loc_grad";
_age_pnl.setBackground((android.graphics.drawable.Drawable)(_loc_grad.getObject()));
 //BA.debugLineNum = 2940;BA.debugLine="gender_pnl.Background = loc_grad";
_gender_pnl.setBackground((android.graphics.drawable.Drawable)(_loc_grad.getObject()));
 //BA.debugLineNum = 2941;BA.debugLine="btn_pnl.Background = btn_grad";
_btn_pnl.setBackground((android.graphics.drawable.Drawable)(_btn_grad.getObject()));
 //BA.debugLineNum = 2942;BA.debugLine="ok_vie_btn.Background = ok_btn_grad";
_ok_vie_btn.setBackground((android.graphics.drawable.Drawable)(_ok_btn_grad.getObject()));
 //BA.debugLineNum = 2945;BA.debugLine="Dim bitmd As BitmapDrawable";
_bitmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 2946;BA.debugLine="Dim bitm As Bitmap";
_bitm = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 2947;BA.debugLine="bitm.Initialize(File.DirAssets,\"bh2.png\")";
_bitm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bh2.png");
 //BA.debugLineNum = 2948;BA.debugLine="bitmd.Initialize(bitm)";
_bitmd.Initialize((android.graphics.Bitmap)(_bitm.getObject()));
 //BA.debugLineNum = 2949;BA.debugLine="delete_bookmark.Background = bitmd";
_delete_bookmark.setBackground((android.graphics.drawable.Drawable)(_bitmd.getObject()));
 //BA.debugLineNum = 2951;BA.debugLine="view_for_image.AddView(fn_pnl,0,0,74%x,30%y) ' fu";
_view_for_image.AddView((android.view.View)(_fn_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 2953;BA.debugLine="fn_pnl.AddView(user_image,((fn_pnl.Width/2)/2)-2";
_fn_pnl.AddView((android.view.View)(mostCurrent._user_image.getObject()),(int) (((_fn_pnl.getWidth()/(double)2)/(double)2)-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1.2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (39),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (17),mostCurrent.activityBA));
 //BA.debugLineNum = 2954;BA.debugLine="fn_pnl.AddView(delete_bookmark,fn_pnl.Width-13.";
_fn_pnl.AddView((android.view.View)(_delete_bookmark.getObject()),(int) (_fn_pnl.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13.5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1.3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (12),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 2955;BA.debugLine="fn_pnl.AddView(fullname,0,user_image.Top + user_";
_fn_pnl.AddView((android.view.View)(_fullname.getObject()),(int) (0),(int) (mostCurrent._user_image.getTop()+mostCurrent._user_image.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 2957;BA.debugLine="fullname.TextSize = 25";
_fullname.setTextSize((float) (25));
 //BA.debugLineNum = 2958;BA.debugLine="fullname.Typeface = Typeface.LoadFromAssets(\"ZIN";
_fullname.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 2960;BA.debugLine="view_panl.AddView(age_pnl,1%x,0,72%x,8%y)";
_view_panl.AddView((android.view.View)(_age_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2961;BA.debugLine="age_pnl.AddView(age_img,4%x,1%y,6%x,6%y) ''  ima";
_age_pnl.AddView((android.view.View)(_age_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2962;BA.debugLine="age_pnl.AddView(age,age_img.Left + age_img.Width";
_age_pnl.AddView((android.view.View)(_age.getObject()),(int) (_age_img.getLeft()+_age_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2964;BA.debugLine="view_panl.AddView(gender_pnl,1%x,age_pnl.Top + ag";
_view_panl.AddView((android.view.View)(_gender_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_age_pnl.getTop()+_age_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2965;BA.debugLine="gender_pnl.AddView(gender_img,4%x,1%y,6%x,6%y) '";
_gender_pnl.AddView((android.view.View)(_gender_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2966;BA.debugLine="gender_pnl.AddView(gender,gender_img.Left + gend";
_gender_pnl.AddView((android.view.View)(_gender.getObject()),(int) (_gender_img.getLeft()+_gender_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2968;BA.debugLine="view_panl.AddView(don_pnl,1%x,gender_pnl.Top + ge";
_view_panl.AddView((android.view.View)(_don_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_gender_pnl.getTop()+_gender_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2969;BA.debugLine="don_pnl.AddView(don_img,4%x,1%y,6%x,6%y) '' imag";
_don_pnl.AddView((android.view.View)(_don_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2970;BA.debugLine="don_pnl.AddView(donated,don_img.Left + don_img.W";
_don_pnl.AddView((android.view.View)(_donated.getObject()),(int) (_don_img.getLeft()+_don_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2972;BA.debugLine="view_panl.AddView(ema_pnl,1%x,don_pnl.Top + don_p";
_view_panl.AddView((android.view.View)(_ema_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_don_pnl.getTop()+_don_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2973;BA.debugLine="ema_pnl.AddView(ema_img,4%x,1%y,6%x,6%y) '' imag";
_ema_pnl.AddView((android.view.View)(_ema_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2974;BA.debugLine="ema_pnl.AddView(email,ema_img.Left + ema_img.Wid";
_ema_pnl.AddView((android.view.View)(_email.getObject()),(int) (_ema_img.getLeft()+_ema_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2976;BA.debugLine="view_panl.AddView(ph1_pnl,1%x,ema_pnl.Top + ema_p";
_view_panl.AddView((android.view.View)(mostCurrent._ph1_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_ema_pnl.getTop()+_ema_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2977;BA.debugLine="ph1_pnl.AddView(ph1_img,4%x,1%y,6%x,6%y) '' imag";
mostCurrent._ph1_pnl.AddView((android.view.View)(_ph1_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2978;BA.debugLine="ph1_pnl.AddView(phone1,ph1_img.Left + ph1_img.Wi";
mostCurrent._ph1_pnl.AddView((android.view.View)(mostCurrent._phone1.getObject()),(int) (_ph1_img.getLeft()+_ph1_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2980;BA.debugLine="view_panl.AddView(ph2_pnl,1%x,ph1_pnl.Top + ph1_p";
_view_panl.AddView((android.view.View)(mostCurrent._ph2_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (mostCurrent._ph1_pnl.getTop()+mostCurrent._ph1_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2981;BA.debugLine="ph2_pnl.AddView(ph2_img,4%x,1%y,6%x,6%y) '' imag";
mostCurrent._ph2_pnl.AddView((android.view.View)(_ph2_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2982;BA.debugLine="ph2_pnl.AddView(phone2,ph2_img.Left + ph2_img.Wi";
mostCurrent._ph2_pnl.AddView((android.view.View)(mostCurrent._phone2.getObject()),(int) (_ph2_img.getLeft()+_ph2_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2984;BA.debugLine="view_panl.AddView(loc_pnl,1%x,ph2_pnl.Top + ph2_p";
_view_panl.AddView((android.view.View)(_loc_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (mostCurrent._ph2_pnl.getTop()+mostCurrent._ph2_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2985;BA.debugLine="loc_pnl.AddView(loc_img,4%x,1%y,6%x,6%y) '' imag";
_loc_pnl.AddView((android.view.View)(_loc_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2986;BA.debugLine="loc_pnl.AddView(location,loc_img.Left + loc_img.";
_loc_pnl.AddView((android.view.View)(_location.getObject()),(int) (_loc_img.getLeft()+_loc_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 2989;BA.debugLine="btn_pnl.AddView(ok_vie_btn,((74%x/2)/2),1%y,37%x,";
_btn_pnl.AddView((android.view.View)(_ok_vie_btn.getObject()),(int) (((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (37),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 2991;BA.debugLine="view_data_info_person.Color = Colors.ARGB(128,12";
mostCurrent._view_data_info_person.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (128),(int) (128),(int) (.70)));
 //BA.debugLineNum = 2994;BA.debugLine="scroll_view_info.ScrollbarsVisibility(False,Fals";
mostCurrent._scroll_view_info.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 2995;BA.debugLine="scroll_view_info.SetBackgroundImage(LoadBitmap(F";
mostCurrent._scroll_view_info.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 2996;BA.debugLine="scroll_view_info.Panel.AddView(view_panl,0,0,74%x";
mostCurrent._scroll_view_info.getPanel().AddView((android.view.View)(_view_panl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (57),mostCurrent.activityBA));
 //BA.debugLineNum = 2997;BA.debugLine="view_data_info_person.AddView(view_for_image,13%x";
mostCurrent._view_data_info_person.AddView((android.view.View)(_view_for_image.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (31),mostCurrent.activityBA));
 //BA.debugLineNum = 2998;BA.debugLine="view_data_info_person.AddView(scroll_view_info,13";
mostCurrent._view_data_info_person.AddView((android.view.View)(mostCurrent._scroll_view_info.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (_view_for_image.getTop()+_view_for_image.getHeight()-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (.7),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 2999;BA.debugLine="view_data_info_person.AddView(btn_pnl,13%x,scroll";
mostCurrent._view_data_info_person.AddView((android.view.View)(_btn_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (mostCurrent._scroll_view_info.getTop()+mostCurrent._scroll_view_info.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 3001;BA.debugLine="Activity.AddView(view_data_info_person,0,0,100%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._view_data_info_person.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 3003;BA.debugLine="for_phone_clik_animation";
_for_phone_clik_animation();
 //BA.debugLineNum = 3004;BA.debugLine="End Sub";
return "";
}
public static String  _view_data_info_person_click() throws Exception{
 //BA.debugLineNum = 3008;BA.debugLine="Sub view_data_info_person_click";
 //BA.debugLineNum = 3010;BA.debugLine="End Sub";
return "";
}
public static String  _view_info_pnl_click() throws Exception{
 //BA.debugLineNum = 2735;BA.debugLine="Sub view_info_pnl_click";
 //BA.debugLineNum = 2737;BA.debugLine="End Sub";
return "";
}
}
