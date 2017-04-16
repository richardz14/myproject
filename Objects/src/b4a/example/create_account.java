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

public class create_account extends Activity implements B4AActivity{
	public static create_account mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.create_account");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (create_account).");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.create_account");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.create_account", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (create_account) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (create_account) Resume **");
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
		return create_account.class;
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
        BA.LogInfo("** Activity (create_account) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (create_account) Resume **");
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
public static anywheresoftware.b4a.objects.collections.List _list_bloodgroup = null;
public static anywheresoftware.b4a.objects.collections.List _list_donate_confirm = null;
public static anywheresoftware.b4a.objects.collections.List _list_bday_m = null;
public static anywheresoftware.b4a.objects.collections.List _list_bday_d = null;
public static anywheresoftware.b4a.objects.collections.List _list_bday_y = null;
public static anywheresoftware.b4a.objects.collections.List _list_location_b = null;
public static anywheresoftware.b4a.objects.collections.List _list_location_s = null;
public static anywheresoftware.b4a.objects.collections.List _list_location_p = null;
public static String _lat = "";
public static String _lng = "";
public static int _brgy_index = 0;
public static int _street_index = 0;
public static double _h = 0;
public static double _w = 0;
public static double _t = 0;
public static double _l = 0;
public flm.b4a.scrollview2d.ScrollView2DWrapper _scrool_2d = null;
public anywheresoftware.b4a.objects.PanelWrapper _ban_panel = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ban_logo = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ban_picture = null;
public anywheresoftware.b4a.objects.PanelWrapper _uptext_panel = null;
public anywheresoftware.b4a.objects.LabelWrapper _indicator = null;
public anywheresoftware.b4a.objects.PanelWrapper _all_inputs = null;
public anywheresoftware.b4a.objects.PanelWrapper _create_panel = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_fullname = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_fn = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_bloodgroup = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_bloodgroup = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_email = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_email = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_password = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_password = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_phonenumber = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_phonenumber = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_needreset = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_donate_confirm = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_donate_confirm = null;
public anywheresoftware.b4a.objects.ButtonWrapper _reg_button = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_question = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_answer = null;
public anywheresoftware.b4a.objects.LabelWrapper _lab_location = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _location_spin_brgy = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _location_spin_street = null;
public anywheresoftware.b4a.objects.LabelWrapper _bday = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _bday_spin_month = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _bday_spin_day = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _bday_spin_year = null;
public anywheresoftware.b4a.objects.PanelWrapper _bday_panel = null;
public anywheresoftware.b4a.objects.PanelWrapper _location_panel = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_password2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_phonenumber2 = null;
public b4a.example.httpjob _insert_job = null;
public b4a.example.httpjob _existing_email = null;
public static String _email_exists = "";
public b4a.example.main _main = null;
public b4a.example.login_form _login_form = null;
public b4a.example.search_frame _search_frame = null;
public b4a.example.menu_form _menu_form = null;
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
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 74;BA.debugLine="Activity.LoadLayout(\"create_account\")";
mostCurrent._activity.LoadLayout("create_account",mostCurrent.activityBA);
 //BA.debugLineNum = 75;BA.debugLine="all_settings_layout";
_all_settings_layout();
 //BA.debugLineNum = 76;BA.debugLine="scrolling";
_scrolling();
 //BA.debugLineNum = 77;BA.debugLine="location_panel.Color = Colors.Transparent";
mostCurrent._location_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 78;BA.debugLine="bday_panel.Color = Colors.Transparent";
mostCurrent._bday_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 79;BA.debugLine="insert_job.Initialize(\"inserting\",Me)";
mostCurrent._insert_job._initialize(processBA,"inserting",create_account.getObject());
 //BA.debugLineNum = 80;BA.debugLine="existing_email.Initialize(\"email_exist\",Me)";
mostCurrent._existing_email._initialize(processBA,"email_exist",create_account.getObject());
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 217;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 219;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 213;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 215;BA.debugLine="End Sub";
return "";
}
public static String  _all_settings_layout() throws Exception{
b4a.example.calculations _calc = null;
 //BA.debugLineNum = 221;BA.debugLine="Public Sub all_settings_layout";
 //BA.debugLineNum = 222;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 223;BA.debugLine="uptext_panel.SetBackgroundImage(LoadBitmap(Fil";
mostCurrent._uptext_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 224;BA.debugLine="create_panel.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._create_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 225;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 226;BA.debugLine="ban_logo.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._ban_logo.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logo1.jpg").getObject()));
 //BA.debugLineNum = 229;BA.debugLine="ban_panel.Width = Activity.Width";
mostCurrent._ban_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 231;BA.debugLine="create_panel.Width = Activity.Width";
mostCurrent._create_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 232;BA.debugLine="uptext_panel.Width = Activity.Width";
mostCurrent._uptext_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 233;BA.debugLine="ban_picture.Width = 80%x";
mostCurrent._ban_picture.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 234;BA.debugLine="ban_logo.Width = ban_panel.Width - ban_picture.Wi";
mostCurrent._ban_logo.setWidth((int) (mostCurrent._ban_panel.getWidth()-mostCurrent._ban_picture.getWidth()));
 //BA.debugLineNum = 235;BA.debugLine="indicator.Width = 100%x";
mostCurrent._indicator.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 240;BA.debugLine="ban_panel.Height = 17%y";
mostCurrent._ban_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (17),mostCurrent.activityBA));
 //BA.debugLineNum = 241;BA.debugLine="create_panel.Height = 12%y";
mostCurrent._create_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 242;BA.debugLine="uptext_panel.Height = 8%y";
mostCurrent._uptext_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 244;BA.debugLine="ban_picture.Height =ban_panel.Height";
mostCurrent._ban_picture.setHeight(mostCurrent._ban_panel.getHeight());
 //BA.debugLineNum = 245;BA.debugLine="ban_logo.Height = ban_panel.Height";
mostCurrent._ban_logo.setHeight(mostCurrent._ban_panel.getHeight());
 //BA.debugLineNum = 247;BA.debugLine="ban_picture.Top = Activity.Top";
mostCurrent._ban_picture.setTop(mostCurrent._activity.getTop());
 //BA.debugLineNum = 248;BA.debugLine="ban_logo.Top = Activity.Top";
mostCurrent._ban_logo.setTop(mostCurrent._activity.getTop());
 //BA.debugLineNum = 249;BA.debugLine="ban_panel.Top = Activity.Top";
mostCurrent._ban_panel.setTop(mostCurrent._activity.getTop());
 //BA.debugLineNum = 250;BA.debugLine="create_panel.Top = Activity.Height - create_panel";
mostCurrent._create_panel.setTop((int) (mostCurrent._activity.getHeight()-mostCurrent._create_panel.getHeight()));
 //BA.debugLineNum = 251;BA.debugLine="uptext_panel.Top = ban_panel.Top + ban_panel.Heig";
mostCurrent._uptext_panel.setTop((int) (mostCurrent._ban_panel.getTop()+mostCurrent._ban_panel.getHeight()));
 //BA.debugLineNum = 252;BA.debugLine="indicator.Top = (uptext_panel.Height/2) - 8dip";
mostCurrent._indicator.setTop((int) ((mostCurrent._uptext_panel.getHeight()/(double)2)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))));
 //BA.debugLineNum = 253;BA.debugLine="reg_button.Top = 4%y";
mostCurrent._reg_button.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA));
 //BA.debugLineNum = 256;BA.debugLine="ban_panel.Left = Activity.Left";
mostCurrent._ban_panel.setLeft(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 258;BA.debugLine="create_panel.Left = Activity.Left";
mostCurrent._create_panel.setLeft(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 259;BA.debugLine="uptext_panel.Left = Activity.Left";
mostCurrent._uptext_panel.setLeft(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 260;BA.debugLine="ban_logo.Left = Activity.Left";
mostCurrent._ban_logo.setLeft(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 261;BA.debugLine="ban_picture.Left = ban_logo.Left + ban_logo.Width";
mostCurrent._ban_picture.setLeft((int) (mostCurrent._ban_logo.getLeft()+mostCurrent._ban_logo.getWidth()));
 //BA.debugLineNum = 263;BA.debugLine="indicator.Left = uptext_panel.Left";
mostCurrent._indicator.setLeft(mostCurrent._uptext_panel.getLeft());
 //BA.debugLineNum = 264;BA.debugLine="indicator.Gravity = Gravity.CENTER_HORIZONTAL";
mostCurrent._indicator.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 265;BA.debugLine="reg_button.Left = create_panel.Left + ((create_pa";
mostCurrent._reg_button.setLeft((int) (mostCurrent._create_panel.getLeft()+((mostCurrent._create_panel.getWidth()/(double)2)-(mostCurrent._create_panel.getWidth()/(double)5))));
 //BA.debugLineNum = 270;BA.debugLine="Dim calc As calculations";
_calc = new b4a.example.calculations();
 //BA.debugLineNum = 271;BA.debugLine="calc.Initialize";
_calc._initialize(processBA);
 //BA.debugLineNum = 272;BA.debugLine="h = calc.sums_height(Activity.Height - ban_panel.";
_h = _calc._sums_height(mostCurrent._activity.getHeight()-mostCurrent._ban_panel.getHeight()-mostCurrent._uptext_panel.getHeight()-mostCurrent._create_panel.getHeight());
 //BA.debugLineNum = 273;BA.debugLine="w = calc.sums_width(Activity.Width)";
_w = _calc._sums_width(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 274;BA.debugLine="l = calc.sums_left(Activity.Left)";
_l = _calc._sums_left(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 275;BA.debugLine="t = calc.sums_top(uptext_panel.Top - uptext_panel";
_t = _calc._sums_top(mostCurrent._uptext_panel.getTop()-mostCurrent._uptext_panel.getHeight());
 //BA.debugLineNum = 276;BA.debugLine="End Sub";
return "";
}
public static String  _email_existance() throws Exception{
String _email_url = "";
b4a.example.calculations _url_back = null;
 //BA.debugLineNum = 82;BA.debugLine="Private Sub email_existance";
 //BA.debugLineNum = 83;BA.debugLine="ProgressDialogShow2(\"Please wait...\",True)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Please wait...",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 84;BA.debugLine="Dim email_url As String";
_email_url = "";
 //BA.debugLineNum = 85;BA.debugLine="Dim url_back As calculations";
_url_back = new b4a.example.calculations();
 //BA.debugLineNum = 86;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 87;BA.debugLine="email_url = url_back.php_email_url(\"/bloodlifePHP";
_email_url = _url_back._php_email_url("/bloodlifePHP/index.php");
 //BA.debugLineNum = 89;BA.debugLine="existing_email.Download2(email_url,Array As Strin";
mostCurrent._existing_email._download2(_email_url,new String[]{"email","SELECT email FROM `bloodlife_db`.`person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public static String  _existing_result() throws Exception{
String _full_name = "";
String _blood_type = "";
String _email = "";
String _password1 = "";
String _password2 = "";
String _phone_number1 = "";
String _phone_number2 = "";
String _brgy = "";
String _street = "";
String _purok = "";
String _month = "";
String _day = "";
String _year = "";
String _answer = "";
String _donate_boolean = "";
b4a.example.calculations _url_back = null;
String _ins = "";
String _m_1 = "";
String _m_2 = "";
String _merge = "";
int _choose = 0;
 //BA.debugLineNum = 172;BA.debugLine="Private Sub existing_result";
 //BA.debugLineNum = 174;BA.debugLine="Dim full_name As String : full_name = text_fn.Tex";
_full_name = "";
 //BA.debugLineNum = 174;BA.debugLine="Dim full_name As String : full_name = text_fn.Tex";
_full_name = mostCurrent._text_fn.getText();
 //BA.debugLineNum = 175;BA.debugLine="Dim blood_type As String : blood_type = spin_bloo";
_blood_type = "";
 //BA.debugLineNum = 175;BA.debugLine="Dim blood_type As String : blood_type = spin_bloo";
_blood_type = mostCurrent._spin_bloodgroup.getSelectedItem();
 //BA.debugLineNum = 176;BA.debugLine="Dim email As String : email = text_email.Text";
_email = "";
 //BA.debugLineNum = 176;BA.debugLine="Dim email As String : email = text_email.Text";
_email = mostCurrent._text_email.getText();
 //BA.debugLineNum = 177;BA.debugLine="Dim password1 As String : password1 = text_passwo";
_password1 = "";
 //BA.debugLineNum = 177;BA.debugLine="Dim password1 As String : password1 = text_passwo";
_password1 = mostCurrent._text_password.getText();
 //BA.debugLineNum = 178;BA.debugLine="Dim password2 As String : password2 = text_passwo";
_password2 = "";
 //BA.debugLineNum = 178;BA.debugLine="Dim password2 As String : password2 = text_passwo";
_password2 = mostCurrent._text_password2.getText();
 //BA.debugLineNum = 179;BA.debugLine="Dim phone_number1 As String :  phone_number1 = te";
_phone_number1 = "";
 //BA.debugLineNum = 179;BA.debugLine="Dim phone_number1 As String :  phone_number1 = te";
_phone_number1 = mostCurrent._text_phonenumber.getText();
 //BA.debugLineNum = 180;BA.debugLine="Dim phone_number2 As String : phone_number2 = tex";
_phone_number2 = "";
 //BA.debugLineNum = 180;BA.debugLine="Dim phone_number2 As String : phone_number2 = tex";
_phone_number2 = mostCurrent._text_phonenumber2.getText();
 //BA.debugLineNum = 181;BA.debugLine="Dim brgy,street,purok As String";
_brgy = "";
_street = "";
_purok = "";
 //BA.debugLineNum = 182;BA.debugLine="brgy = location_spin_brgy.SelectedItem";
_brgy = mostCurrent._location_spin_brgy.getSelectedItem();
 //BA.debugLineNum = 183;BA.debugLine="street = location_spin_street.SelectedItem";
_street = mostCurrent._location_spin_street.getSelectedItem();
 //BA.debugLineNum = 185;BA.debugLine="Dim month,day,year As String";
_month = "";
_day = "";
_year = "";
 //BA.debugLineNum = 186;BA.debugLine="month = bday_spin_month.SelectedItem";
_month = mostCurrent._bday_spin_month.getSelectedItem();
 //BA.debugLineNum = 187;BA.debugLine="day = bday_spin_day.SelectedItem";
_day = mostCurrent._bday_spin_day.getSelectedItem();
 //BA.debugLineNum = 188;BA.debugLine="year = bday_spin_year.SelectedItem";
_year = mostCurrent._bday_spin_year.getSelectedItem();
 //BA.debugLineNum = 189;BA.debugLine="Dim answer As String : answer = text_answer.Text";
_answer = "";
 //BA.debugLineNum = 189;BA.debugLine="Dim answer As String : answer = text_answer.Text";
_answer = mostCurrent._text_answer.getText();
 //BA.debugLineNum = 190;BA.debugLine="Dim donate_boolean As String : donate_boolean = s";
_donate_boolean = "";
 //BA.debugLineNum = 190;BA.debugLine="Dim donate_boolean As String : donate_boolean = s";
_donate_boolean = mostCurrent._spin_donate_confirm.getSelectedItem();
 //BA.debugLineNum = 194;BA.debugLine="Dim url_back As calculations";
_url_back = new b4a.example.calculations();
 //BA.debugLineNum = 195;BA.debugLine="Dim ins,m_1,m_2,merge As String";
_ins = "";
_m_1 = "";
_m_2 = "";
_merge = "";
 //BA.debugLineNum = 196;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 198;BA.debugLine="m_1 = \"INSERT INTO `bloodlife_db`.`person_info";
_m_1 = "INSERT INTO `bloodlife_db`.`person_info` (`full_name`,`blood_type`,`email`,`password`,`phone_number1`,`phone_number2`,`location_brgy`,`location_street`,`bday_month`,`bday_day`,`bday_year`,`nick_name`,`donate_boolean`,`lat`,`long`) ";
 //BA.debugLineNum = 199;BA.debugLine="m_2 = \"VALUES ('\"&full_name&\"', '\"&blood_type&";
_m_2 = "VALUES ('"+_full_name+"', '"+_blood_type+"','"+_email+"',ENCODE('"+_password2+"','goroy'),'"+_phone_number1+"','"+_phone_number2+"','"+_brgy+"','"+_street+"','"+_month+"','"+_day+"','"+_year+"','"+_answer+"','"+_donate_boolean+"','"+_lat+"','"+_lng+"');";
 //BA.debugLineNum = 200;BA.debugLine="merge = m_1&m_2";
_merge = _m_1+_m_2;
 //BA.debugLineNum = 201;BA.debugLine="ins = url_back.php_email_url(\"/bloodlifePHP/in";
_ins = _url_back._php_email_url("/bloodlifePHP/inserting.php");
 //BA.debugLineNum = 202;BA.debugLine="insert_job.Download2(ins,Array As String(\"inse";
mostCurrent._insert_job._download2(_ins,new String[]{"insert",""+_merge});
 //BA.debugLineNum = 203;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 205;BA.debugLine="Dim choose As Int : choose = Msgbox2(\"Sucessful";
_choose = 0;
 //BA.debugLineNum = 205;BA.debugLine="Dim choose As Int : choose = Msgbox2(\"Sucessful";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2("Sucessfuly Registered","C O N F I R M A T I O N","OK","","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 206;BA.debugLine="Select choose";
switch (BA.switchObjectToInt(_choose,anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE)) {
case 0: {
 //BA.debugLineNum = 208;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 209;BA.debugLine="StartActivity(\"login_form\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("login_form"));
 break; }
}
;
 //BA.debugLineNum = 211;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Dim h,w,t,l As Double";
_h = 0;
_w = 0;
_t = 0;
_l = 0;
 //BA.debugLineNum = 25;BA.debugLine="Private scrool_2d As ScrollView2D";
mostCurrent._scrool_2d = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private ban_panel As Panel";
mostCurrent._ban_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private ban_logo As ImageView";
mostCurrent._ban_logo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private ban_picture As ImageView";
mostCurrent._ban_picture = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private uptext_panel As Panel";
mostCurrent._uptext_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private indicator As Label";
mostCurrent._indicator = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private all_inputs As Panel";
mostCurrent._all_inputs = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private create_panel As Panel";
mostCurrent._create_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private lab_fullname As Label";
mostCurrent._lab_fullname = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private text_fn As EditText";
mostCurrent._text_fn = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private lab_bloodgroup As Label";
mostCurrent._lab_bloodgroup = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private spin_bloodgroup As Spinner";
mostCurrent._spin_bloodgroup = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private lab_email As Label";
mostCurrent._lab_email = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private text_email As EditText";
mostCurrent._text_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private lab_password As Label";
mostCurrent._lab_password = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private text_password As EditText";
mostCurrent._text_password = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private lab_phonenumber As Label";
mostCurrent._lab_phonenumber = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private text_phonenumber As EditText";
mostCurrent._text_phonenumber = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private lab_needReset As Label";
mostCurrent._lab_needreset = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private lab_donate_confirm As Label";
mostCurrent._lab_donate_confirm = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private spin_donate_confirm As Spinner";
mostCurrent._spin_donate_confirm = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private reg_button As Button";
mostCurrent._reg_button = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private lab_question As Label";
mostCurrent._lab_question = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private text_answer As EditText";
mostCurrent._text_answer = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private lab_location As Label";
mostCurrent._lab_location = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private location_spin_brgy As Spinner";
mostCurrent._location_spin_brgy = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private location_spin_street As Spinner";
mostCurrent._location_spin_street = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private bday As Label";
mostCurrent._bday = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private bday_spin_month As Spinner";
mostCurrent._bday_spin_month = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private bday_spin_day As Spinner";
mostCurrent._bday_spin_day = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private bday_spin_year As Spinner";
mostCurrent._bday_spin_year = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private bday_panel As Panel";
mostCurrent._bday_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private location_panel As Panel";
mostCurrent._location_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private text_password2 As EditText";
mostCurrent._text_password2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private text_phonenumber2 As EditText";
mostCurrent._text_phonenumber2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private insert_job As HttpJob";
mostCurrent._insert_job = new b4a.example.httpjob();
 //BA.debugLineNum = 67;BA.debugLine="Private existing_email As HttpJob";
mostCurrent._existing_email = new b4a.example.httpjob();
 //BA.debugLineNum = 68;BA.debugLine="Private email_exists As String";
mostCurrent._email_exists = "";
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(b4a.example.httpjob _job) throws Exception{
 //BA.debugLineNum = 91;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 93;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 94;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"email_exist")) {
case 0: {
 //BA.debugLineNum = 96;BA.debugLine="email_exists = job.GetString.Trim";
mostCurrent._email_exists = _job._getstring().trim();
 //BA.debugLineNum = 97;BA.debugLine="Log(email_exists)";
anywheresoftware.b4a.keywords.Common.Log(mostCurrent._email_exists);
 //BA.debugLineNum = 98;BA.debugLine="If email_exists.Contains(text_email.Text) == Tr";
if (mostCurrent._email_exists.contains(mostCurrent._text_email.getText())==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 99;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 100;BA.debugLine="Msgbox(\"Error: Email address are already exis";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Email address are already existed.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 102;BA.debugLine="existing_result";
_existing_result();
 };
 break; }
}
;
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 109;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 110;BA.debugLine="Msgbox(\"Error: Error connecting to server, try a";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Error connecting to server, try again laiter.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 112;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 113;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_brgy_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 925;BA.debugLine="Sub location_spin_brgy_ItemClick (Position As Int,";
 //BA.debugLineNum = 926;BA.debugLine="list_location_s.Clear";
_list_location_s.Clear();
 //BA.debugLineNum = 927;BA.debugLine="location_spin_street.Clear";
mostCurrent._location_spin_street.Clear();
 //BA.debugLineNum = 930;BA.debugLine="If Position == 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 931;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 932;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 933;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 934;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 935;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 936;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 937;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 938;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 }else if(_position==1) { 
 //BA.debugLineNum = 941;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 942;BA.debugLine="list_location_s.Add(\"Monton st.\")";
_list_location_s.Add((Object)("Monton st."));
 //BA.debugLineNum = 943;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 //BA.debugLineNum = 944;BA.debugLine="list_location_s.Add(\"Purok star apple\")";
_list_location_s.Add((Object)("Purok star apple"));
 //BA.debugLineNum = 945;BA.debugLine="list_location_s.Add(\"Gatuslao st.\")";
_list_location_s.Add((Object)("Gatuslao st."));
 //BA.debugLineNum = 946;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 947;BA.debugLine="list_location_s.Add(\"Tabino st.\")";
_list_location_s.Add((Object)("Tabino st."));
 //BA.debugLineNum = 948;BA.debugLine="list_location_s.Add(\"River side\")";
_list_location_s.Add((Object)("River side"));
 //BA.debugLineNum = 949;BA.debugLine="list_location_s.Add(\"Arroyan st\")";
_list_location_s.Add((Object)("Arroyan st"));
 }else if(_position==2) { 
 //BA.debugLineNum = 951;BA.debugLine="list_location_s.Add(\"Segovia st.\")";
_list_location_s.Add((Object)("Segovia st."));
 //BA.debugLineNum = 952;BA.debugLine="list_location_s.Add(\"Vasquez st.\")";
_list_location_s.Add((Object)("Vasquez st."));
 //BA.debugLineNum = 953;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 954;BA.debugLine="list_location_s.Add(\"Old relis st.\")";
_list_location_s.Add((Object)("Old relis st."));
 //BA.debugLineNum = 955;BA.debugLine="list_location_s.Add(\"Wayang\")";
_list_location_s.Add((Object)("Wayang"));
 //BA.debugLineNum = 956;BA.debugLine="list_location_s.Add(\"Valencia\")";
_list_location_s.Add((Object)("Valencia"));
 //BA.debugLineNum = 957;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 958;BA.debugLine="list_location_s.Add(\"Bingig\")";
_list_location_s.Add((Object)("Bingig"));
 //BA.debugLineNum = 959;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 960;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 }else if(_position==3) { 
 //BA.debugLineNum = 962;BA.debugLine="list_location_s.Add(\"Crusher\")";
_list_location_s.Add((Object)("Crusher"));
 //BA.debugLineNum = 963;BA.debugLine="list_location_s.Add(\"Bangga mayok\")";
_list_location_s.Add((Object)("Bangga mayok"));
 //BA.debugLineNum = 964;BA.debugLine="list_location_s.Add(\"Villa julita\")";
_list_location_s.Add((Object)("Villa julita"));
 //BA.debugLineNum = 965;BA.debugLine="list_location_s.Add(\"Greenland subdivision\")";
_list_location_s.Add((Object)("Greenland subdivision"));
 //BA.debugLineNum = 966;BA.debugLine="list_location_s.Add(\"Bangga 3c\")";
_list_location_s.Add((Object)("Bangga 3c"));
 //BA.debugLineNum = 967;BA.debugLine="list_location_s.Add(\"Cambugnon\")";
_list_location_s.Add((Object)("Cambugnon"));
 //BA.debugLineNum = 968;BA.debugLine="list_location_s.Add(\"Menez\")";
_list_location_s.Add((Object)("Menez"));
 //BA.debugLineNum = 969;BA.debugLine="list_location_s.Add(\"Relis\")";
_list_location_s.Add((Object)("Relis"));
 //BA.debugLineNum = 970;BA.debugLine="list_location_s.Add(\"Bangga patyo\")";
_list_location_s.Add((Object)("Bangga patyo"));
 }else if(_position==4) { 
 //BA.debugLineNum = 972;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 973;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 974;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 975;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 976;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 977;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 //BA.debugLineNum = 978;BA.debugLine="list_location_s.Add(\"Purok 7\")";
_list_location_s.Add((Object)("Purok 7"));
 //BA.debugLineNum = 979;BA.debugLine="list_location_s.Add(\"Purok 8\")";
_list_location_s.Add((Object)("Purok 8"));
 //BA.debugLineNum = 980;BA.debugLine="list_location_s.Add(\"Purok 9\")";
_list_location_s.Add((Object)("Purok 9"));
 //BA.debugLineNum = 981;BA.debugLine="list_location_s.Add(\"Purok 10\")";
_list_location_s.Add((Object)("Purok 10"));
 //BA.debugLineNum = 982;BA.debugLine="list_location_s.Add(\"Purok 11\")";
_list_location_s.Add((Object)("Purok 11"));
 //BA.debugLineNum = 983;BA.debugLine="list_location_s.Add(\"Purok 12\")";
_list_location_s.Add((Object)("Purok 12"));
 }else if(_position==5) { 
 //BA.debugLineNum = 985;BA.debugLine="list_location_s.Add(\"Malusay\")";
_list_location_s.Add((Object)("Malusay"));
 //BA.debugLineNum = 986;BA.debugLine="list_location_s.Add(\"Nasug ong\")";
_list_location_s.Add((Object)("Nasug ong"));
 //BA.debugLineNum = 987;BA.debugLine="list_location_s.Add(\"Lugway\")";
_list_location_s.Add((Object)("Lugway"));
 //BA.debugLineNum = 988;BA.debugLine="list_location_s.Add(\"Ubay\")";
_list_location_s.Add((Object)("Ubay"));
 //BA.debugLineNum = 989;BA.debugLine="list_location_s.Add(\"Fisheries\")";
_list_location_s.Add((Object)("Fisheries"));
 //BA.debugLineNum = 990;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 991;BA.debugLine="list_location_s.Add(\"Calasa\")";
_list_location_s.Add((Object)("Calasa"));
 //BA.debugLineNum = 992;BA.debugLine="list_location_s.Add(\"Hda. Serafin\")";
_list_location_s.Add((Object)("Hda. Serafin"));
 //BA.debugLineNum = 993;BA.debugLine="list_location_s.Add(\"Patay na suba\")";
_list_location_s.Add((Object)("Patay na suba"));
 //BA.debugLineNum = 994;BA.debugLine="list_location_s.Add(\"Lumanog\")";
_list_location_s.Add((Object)("Lumanog"));
 //BA.debugLineNum = 995;BA.debugLine="list_location_s.Add(\"San agustin\")";
_list_location_s.Add((Object)("San agustin"));
 //BA.debugLineNum = 996;BA.debugLine="list_location_s.Add(\"San jose\")";
_list_location_s.Add((Object)("San jose"));
 //BA.debugLineNum = 997;BA.debugLine="list_location_s.Add(\"Maglantay\")";
_list_location_s.Add((Object)("Maglantay"));
 //BA.debugLineNum = 998;BA.debugLine="list_location_s.Add(\"San juan\")";
_list_location_s.Add((Object)("San juan"));
 //BA.debugLineNum = 999;BA.debugLine="list_location_s.Add(\"Magsaha\")";
_list_location_s.Add((Object)("Magsaha"));
 //BA.debugLineNum = 1000;BA.debugLine="list_location_s.Add(\"Tagmanok\")";
_list_location_s.Add((Object)("Tagmanok"));
 //BA.debugLineNum = 1001;BA.debugLine="list_location_s.Add(\"Butong\")";
_list_location_s.Add((Object)("Butong"));
 }else if(_position==6) { 
 //BA.debugLineNum = 1003;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1004;BA.debugLine="list_location_s.Add(\"Saisi\")";
_list_location_s.Add((Object)("Saisi"));
 //BA.debugLineNum = 1005;BA.debugLine="list_location_s.Add(\"Paloypoy\")";
_list_location_s.Add((Object)("Paloypoy"));
 //BA.debugLineNum = 1006;BA.debugLine="list_location_s.Add(\"Tigue\")";
_list_location_s.Add((Object)("Tigue"));
 }else if(_position==7) { 
 //BA.debugLineNum = 1008;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1009;BA.debugLine="list_location_s.Add(\"Tonggo\")";
_list_location_s.Add((Object)("Tonggo"));
 //BA.debugLineNum = 1010;BA.debugLine="list_location_s.Add(\"Iling iling\")";
_list_location_s.Add((Object)("Iling iling"));
 //BA.debugLineNum = 1011;BA.debugLine="list_location_s.Add(\"Campayas\")";
_list_location_s.Add((Object)("Campayas"));
 //BA.debugLineNum = 1012;BA.debugLine="list_location_s.Add(\"Palayan\")";
_list_location_s.Add((Object)("Palayan"));
 //BA.debugLineNum = 1013;BA.debugLine="list_location_s.Add(\"Guia\")";
_list_location_s.Add((Object)("Guia"));
 //BA.debugLineNum = 1014;BA.debugLine="list_location_s.Add(\"An-an\")";
_list_location_s.Add((Object)("An-an"));
 //BA.debugLineNum = 1015;BA.debugLine="list_location_s.Add(\"An-an 2\")";
_list_location_s.Add((Object)("An-an 2"));
 //BA.debugLineNum = 1016;BA.debugLine="list_location_s.Add(\"Sta. rita\")";
_list_location_s.Add((Object)("Sta. rita"));
 //BA.debugLineNum = 1017;BA.debugLine="list_location_s.Add(\"Benedicto\")";
_list_location_s.Add((Object)("Benedicto"));
 //BA.debugLineNum = 1018;BA.debugLine="list_location_s.Add(\"Sta. cruz/ bunggol\")";
_list_location_s.Add((Object)("Sta. cruz/ bunggol"));
 //BA.debugLineNum = 1019;BA.debugLine="list_location_s.Add(\"Olalia\")";
_list_location_s.Add((Object)("Olalia"));
 //BA.debugLineNum = 1020;BA.debugLine="list_location_s.Add(\"Banuyo\")";
_list_location_s.Add((Object)("Banuyo"));
 //BA.debugLineNum = 1021;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1022;BA.debugLine="list_location_s.Add(\"Riverside\")";
_list_location_s.Add((Object)("Riverside"));
 }else if(_position==8) { 
 //BA.debugLineNum = 1024;BA.debugLine="list_location_s.Add(\"Balangga-an\")";
_list_location_s.Add((Object)("Balangga-an"));
 //BA.debugLineNum = 1025;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1026;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1027;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 1028;BA.debugLine="list_location_s.Add(\"Bakyas\")";
_list_location_s.Add((Object)("Bakyas"));
 }else if(_position==9) { 
 //BA.debugLineNum = 1030;BA.debugLine="list_location_s.Add(\"Cunalom\")";
_list_location_s.Add((Object)("Cunalom"));
 //BA.debugLineNum = 1031;BA.debugLine="list_location_s.Add(\"Tara\")";
_list_location_s.Add((Object)("Tara"));
 //BA.debugLineNum = 1032;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1033;BA.debugLine="list_location_s.Add(\"Casipungan\")";
_list_location_s.Add((Object)("Casipungan"));
 //BA.debugLineNum = 1034;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1035;BA.debugLine="list_location_s.Add(\"Lanipga\")";
_list_location_s.Add((Object)("Lanipga"));
 //BA.debugLineNum = 1036;BA.debugLine="list_location_s.Add(\"Bulod\")";
_list_location_s.Add((Object)("Bulod"));
 //BA.debugLineNum = 1037;BA.debugLine="list_location_s.Add(\"Bonton\")";
_list_location_s.Add((Object)("Bonton"));
 //BA.debugLineNum = 1038;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 }else if(_position==10) { 
 //BA.debugLineNum = 1040;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 1041;BA.debugLine="list_location_s.Add(\"Balisong\")";
_list_location_s.Add((Object)("Balisong"));
 //BA.debugLineNum = 1042;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1043;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1044;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1045;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1046;BA.debugLine="list_location_s.Add(\"Dubdub\")";
_list_location_s.Add((Object)("Dubdub"));
 //BA.debugLineNum = 1047;BA.debugLine="list_location_s.Add(\"Hda. San jose valing\")";
_list_location_s.Add((Object)("Hda. San jose valing"));
 }else if(_position==11) { 
 //BA.debugLineNum = 1049;BA.debugLine="list_location_s.Add(\"Acapulco\")";
_list_location_s.Add((Object)("Acapulco"));
 //BA.debugLineNum = 1050;BA.debugLine="list_location_s.Add(\"Liki\")";
_list_location_s.Add((Object)("Liki"));
 //BA.debugLineNum = 1051;BA.debugLine="list_location_s.Add(\"500\")";
_list_location_s.Add((Object)("500"));
 //BA.debugLineNum = 1052;BA.debugLine="list_location_s.Add(\"Aglatong\")";
_list_location_s.Add((Object)("Aglatong"));
 //BA.debugLineNum = 1053;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1054;BA.debugLine="list_location_s.Add(\"Baptist\")";
_list_location_s.Add((Object)("Baptist"));
 }else if(_position==12) { 
 //BA.debugLineNum = 1056;BA.debugLine="list_location_s.Add(\"Lizares\")";
_list_location_s.Add((Object)("Lizares"));
 //BA.debugLineNum = 1057;BA.debugLine="list_location_s.Add(\"Pakol\")";
_list_location_s.Add((Object)("Pakol"));
 //BA.debugLineNum = 1058;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1059;BA.debugLine="list_location_s.Add(\"Lanete\")";
_list_location_s.Add((Object)("Lanete"));
 //BA.debugLineNum = 1060;BA.debugLine="list_location_s.Add(\"Kasoy\")";
_list_location_s.Add((Object)("Kasoy"));
 //BA.debugLineNum = 1061;BA.debugLine="list_location_s.Add(\"Bato\")";
_list_location_s.Add((Object)("Bato"));
 //BA.debugLineNum = 1062;BA.debugLine="list_location_s.Add(\"Frande\")";
_list_location_s.Add((Object)("Frande"));
 //BA.debugLineNum = 1063;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 1064;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 //BA.debugLineNum = 1065;BA.debugLine="list_location_s.Add(\"Culban\")";
_list_location_s.Add((Object)("Culban"));
 //BA.debugLineNum = 1066;BA.debugLine="list_location_s.Add(\"Calansi\")";
_list_location_s.Add((Object)("Calansi"));
 //BA.debugLineNum = 1067;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1068;BA.debugLine="list_location_s.Add(\"Dama\")";
_list_location_s.Add((Object)("Dama"));
 }else if(_position==13) { 
 //BA.debugLineNum = 1070;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1071;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1072;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1073;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1074;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 1075;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 }else if(_position==14) { 
 //BA.debugLineNum = 1077;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1078;BA.debugLine="list_location_s.Add(\"Calubihan\")";
_list_location_s.Add((Object)("Calubihan"));
 //BA.debugLineNum = 1079;BA.debugLine="list_location_s.Add(\"Mapulang duta\")";
_list_location_s.Add((Object)("Mapulang duta"));
 //BA.debugLineNum = 1080;BA.debugLine="list_location_s.Add(\"Abud\")";
_list_location_s.Add((Object)("Abud"));
 //BA.debugLineNum = 1081;BA.debugLine="list_location_s.Add(\"Molo\")";
_list_location_s.Add((Object)("Molo"));
 //BA.debugLineNum = 1082;BA.debugLine="list_location_s.Add(\"Balabag\")";
_list_location_s.Add((Object)("Balabag"));
 //BA.debugLineNum = 1083;BA.debugLine="list_location_s.Add(\"Pandan\")";
_list_location_s.Add((Object)("Pandan"));
 //BA.debugLineNum = 1084;BA.debugLine="list_location_s.Add(\"Nahulop\")";
_list_location_s.Add((Object)("Nahulop"));
 //BA.debugLineNum = 1085;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 1086;BA.debugLine="list_location_s.Add(\"Aglaoa\")";
_list_location_s.Add((Object)("Aglaoa"));
 }else if(_position==15) { 
 //BA.debugLineNum = 1088;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1089;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1090;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1091;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 }else if(_position==16) { 
 //BA.debugLineNum = 1093;BA.debugLine="list_location_s.Add(\"ORS\")";
_list_location_s.Add((Object)("ORS"));
 //BA.debugLineNum = 1094;BA.debugLine="list_location_s.Add(\"Aloe vera\")";
_list_location_s.Add((Object)("Aloe vera"));
 //BA.debugLineNum = 1095;BA.debugLine="list_location_s.Add(\"SCAD\")";
_list_location_s.Add((Object)("SCAD"));
 //BA.debugLineNum = 1096;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1097;BA.debugLine="list_location_s.Add(\"Sampaguita\")";
_list_location_s.Add((Object)("Sampaguita"));
 //BA.debugLineNum = 1098;BA.debugLine="list_location_s.Add(\"Bonguinvilla\")";
_list_location_s.Add((Object)("Bonguinvilla"));
 //BA.debugLineNum = 1099;BA.debugLine="list_location_s.Add(\"Cagay\")";
_list_location_s.Add((Object)("Cagay"));
 //BA.debugLineNum = 1100;BA.debugLine="list_location_s.Add(\"Naga\")";
_list_location_s.Add((Object)("Naga"));
 }else if(_position==17) { 
 //BA.debugLineNum = 1102;BA.debugLine="list_location_s.Add(\"Hda. Naval\")";
_list_location_s.Add((Object)("Hda. Naval"));
 //BA.debugLineNum = 1103;BA.debugLine="list_location_s.Add(\"Antipolo\")";
_list_location_s.Add((Object)("Antipolo"));
 //BA.debugLineNum = 1104;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1105;BA.debugLine="list_location_s.Add(\"Punta talaban\")";
_list_location_s.Add((Object)("Punta talaban"));
 //BA.debugLineNum = 1106;BA.debugLine="list_location_s.Add(\"Batang guwaan\")";
_list_location_s.Add((Object)("Batang guwaan"));
 //BA.debugLineNum = 1107;BA.debugLine="list_location_s.Add(\"Batang sulod\")";
_list_location_s.Add((Object)("Batang sulod"));
 //BA.debugLineNum = 1108;BA.debugLine="list_location_s.Add(\"Mabini st.\")";
_list_location_s.Add((Object)("Mabini st."));
 //BA.debugLineNum = 1109;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 1110;BA.debugLine="list_location_s.Add(\"Hacienda silos\")";
_list_location_s.Add((Object)("Hacienda silos"));
 //BA.debugLineNum = 1111;BA.debugLine="list_location_s.Add(\"Lopez jeana 1\")";
_list_location_s.Add((Object)("Lopez jeana 1"));
 //BA.debugLineNum = 1112;BA.debugLine="list_location_s.Add(\"Lopez jeana 2\")";
_list_location_s.Add((Object)("Lopez jeana 2"));
 }else if(_position==18) { 
 //BA.debugLineNum = 1114;BA.debugLine="list_location_s.Add(\"Ilawod\")";
_list_location_s.Add((Object)("Ilawod"));
 //BA.debugLineNum = 1115;BA.debugLine="list_location_s.Add(\"Buhian\")";
_list_location_s.Add((Object)("Buhian"));
 //BA.debugLineNum = 1116;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1117;BA.debugLine="list_location_s.Add(\"Mambato\")";
_list_location_s.Add((Object)("Mambato"));
 };
 //BA.debugLineNum = 1120;BA.debugLine="brgy_index = Position";
_brgy_index = _position;
 //BA.debugLineNum = 1121;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 1124;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_street_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1125;BA.debugLine="Sub location_spin_street_ItemClick (Position As In";
 //BA.debugLineNum = 1126;BA.debugLine="street_index = Position";
_street_index = _position;
 //BA.debugLineNum = 1127;BA.debugLine="street_lat_lng";
_street_lat_lng();
 //BA.debugLineNum = 1128;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim list_bloodgroup As List";
_list_bloodgroup = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 10;BA.debugLine="Dim list_donate_confirm As List";
_list_donate_confirm = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 11;BA.debugLine="Dim list_bday_m,list_bday_d,list_bday_y As List";
_list_bday_m = new anywheresoftware.b4a.objects.collections.List();
_list_bday_d = new anywheresoftware.b4a.objects.collections.List();
_list_bday_y = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 12;BA.debugLine="Dim list_location_b,list_location_s,list_location";
_list_location_b = new anywheresoftware.b4a.objects.collections.List();
_list_location_s = new anywheresoftware.b4a.objects.collections.List();
_list_location_p = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "";
 //BA.debugLineNum = 14;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 15;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "";
 //BA.debugLineNum = 15;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "122.869168";
 //BA.debugLineNum = 16;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = 0;
 //BA.debugLineNum = 16;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = (int) (0);
 //BA.debugLineNum = 17;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = 0;
 //BA.debugLineNum = 17;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = (int) (0);
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _reg_button_click() throws Exception{
String _full_name = "";
String _blood_type = "";
String _email = "";
String _password1 = "";
String _password2 = "";
String _phone_number1 = "";
String _phone_number2 = "";
String _brgy = "";
String _street = "";
String _purok = "";
String _month = "";
String _day = "";
String _year = "";
String _answer = "";
String _donate_boolean = "";
 //BA.debugLineNum = 114;BA.debugLine="Sub reg_button_Click";
 //BA.debugLineNum = 115;BA.debugLine="Dim full_name As String : full_name = text_fn.Tex";
_full_name = "";
 //BA.debugLineNum = 115;BA.debugLine="Dim full_name As String : full_name = text_fn.Tex";
_full_name = mostCurrent._text_fn.getText();
 //BA.debugLineNum = 116;BA.debugLine="Dim blood_type As String : blood_type = spin_bloo";
_blood_type = "";
 //BA.debugLineNum = 116;BA.debugLine="Dim blood_type As String : blood_type = spin_bloo";
_blood_type = mostCurrent._spin_bloodgroup.getSelectedItem();
 //BA.debugLineNum = 117;BA.debugLine="Dim email As String : email = text_email.Text";
_email = "";
 //BA.debugLineNum = 117;BA.debugLine="Dim email As String : email = text_email.Text";
_email = mostCurrent._text_email.getText();
 //BA.debugLineNum = 118;BA.debugLine="Dim password1 As String : password1 = text_passwo";
_password1 = "";
 //BA.debugLineNum = 118;BA.debugLine="Dim password1 As String : password1 = text_passwo";
_password1 = mostCurrent._text_password.getText();
 //BA.debugLineNum = 119;BA.debugLine="Dim password2 As String : password2 = text_passwo";
_password2 = "";
 //BA.debugLineNum = 119;BA.debugLine="Dim password2 As String : password2 = text_passwo";
_password2 = mostCurrent._text_password2.getText();
 //BA.debugLineNum = 120;BA.debugLine="Dim phone_number1 As String :  phone_number1 = te";
_phone_number1 = "";
 //BA.debugLineNum = 120;BA.debugLine="Dim phone_number1 As String :  phone_number1 = te";
_phone_number1 = mostCurrent._text_phonenumber.getText();
 //BA.debugLineNum = 121;BA.debugLine="Dim phone_number2 As String : phone_number2 = tex";
_phone_number2 = "";
 //BA.debugLineNum = 121;BA.debugLine="Dim phone_number2 As String : phone_number2 = tex";
_phone_number2 = mostCurrent._text_phonenumber2.getText();
 //BA.debugLineNum = 122;BA.debugLine="Dim brgy,street,purok As String";
_brgy = "";
_street = "";
_purok = "";
 //BA.debugLineNum = 123;BA.debugLine="brgy = location_spin_brgy.SelectedItem";
_brgy = mostCurrent._location_spin_brgy.getSelectedItem();
 //BA.debugLineNum = 124;BA.debugLine="street = location_spin_street.SelectedItem";
_street = mostCurrent._location_spin_street.getSelectedItem();
 //BA.debugLineNum = 126;BA.debugLine="Dim month,day,year As String";
_month = "";
_day = "";
_year = "";
 //BA.debugLineNum = 127;BA.debugLine="month = bday_spin_month.SelectedItem";
_month = mostCurrent._bday_spin_month.getSelectedItem();
 //BA.debugLineNum = 128;BA.debugLine="day = bday_spin_day.SelectedItem";
_day = mostCurrent._bday_spin_day.getSelectedItem();
 //BA.debugLineNum = 129;BA.debugLine="year = bday_spin_year.SelectedItem";
_year = mostCurrent._bday_spin_year.getSelectedItem();
 //BA.debugLineNum = 130;BA.debugLine="Dim answer As String : answer = text_answer.Text";
_answer = "";
 //BA.debugLineNum = 130;BA.debugLine="Dim answer As String : answer = text_answer.Text";
_answer = mostCurrent._text_answer.getText();
 //BA.debugLineNum = 131;BA.debugLine="Dim donate_boolean As String : donate_boolean = s";
_donate_boolean = "";
 //BA.debugLineNum = 131;BA.debugLine="Dim donate_boolean As String : donate_boolean = s";
_donate_boolean = mostCurrent._spin_donate_confirm.getSelectedItem();
 //BA.debugLineNum = 135;BA.debugLine="If text_fn.Text == \"\"  Or text_email.Text == \"\" O";
if ((mostCurrent._text_fn.getText()).equals("") || (mostCurrent._text_email.getText()).equals("") || (mostCurrent._text_password.getText()).equals("") || (mostCurrent._text_password2.getText()).equals("") || (mostCurrent._text_phonenumber.getText()).equals("") || (mostCurrent._text_phonenumber2.getText()).equals("") || (mostCurrent._text_password2.getText()).equals("") || (mostCurrent._text_answer.getText()).equals("")) { 
 //BA.debugLineNum = 136;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 137;BA.debugLine="Msgbox(\"Error: Fill up those empty fields before";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Fill up those empty fields before you registered!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 140;BA.debugLine="If password1.Contains(password2) == False Then";
if (_password1.contains(_password2)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 141;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 142;BA.debugLine="text_password.Text = \"\"";
mostCurrent._text_password.setText((Object)(""));
 //BA.debugLineNum = 143;BA.debugLine="text_password2.Text = \"\"";
mostCurrent._text_password2.setText((Object)(""));
 //BA.debugLineNum = 144;BA.debugLine="Msgbox(\"Error: Password did not match!\",\"C O N";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Password did not match!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 146;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 147;BA.debugLine="email_existance";
_email_existance();
 };
 };
 //BA.debugLineNum = 170;BA.debugLine="End Sub";
return "";
}
public static String  _scrolling() throws Exception{
 //BA.debugLineNum = 356;BA.debugLine="Sub scrolling";
 //BA.debugLineNum = 358;BA.debugLine="scrool_2d.Initialize(100%x,Activity.Height - ban_";
mostCurrent._scrool_2d.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),(int) (mostCurrent._activity.getHeight()-mostCurrent._ban_panel.getHeight()-mostCurrent._uptext_panel.getHeight()-mostCurrent._create_panel.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),"scroll2d");
 //BA.debugLineNum = 360;BA.debugLine="scrool_2d.Panel.LoadLayout(\"create_all_inputs\")";
mostCurrent._scrool_2d.getPanel().LoadLayout("create_all_inputs",mostCurrent.activityBA);
 //BA.debugLineNum = 361;BA.debugLine="scrool_2d.ScrollbarsVisibility(False,False)";
mostCurrent._scrool_2d.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 362;BA.debugLine="all_inputs.Width = 100%x";
mostCurrent._all_inputs.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 363;BA.debugLine="all_inputs.Height = 100%y";
mostCurrent._all_inputs.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 364;BA.debugLine="scrool_2d.Panel.SendToBack";
mostCurrent._scrool_2d.getPanel().SendToBack();
 //BA.debugLineNum = 366;BA.debugLine="all_inputs.SendToBack";
mostCurrent._all_inputs.SendToBack();
 //BA.debugLineNum = 367;BA.debugLine="all_inputs.Color = Colors.Transparent";
mostCurrent._all_inputs.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 368;BA.debugLine="Activity.AddView(scrool_2d,0,uptext_panel.Top + u";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._scrool_2d.getObject()),(int) (0),(int) (mostCurrent._uptext_panel.getTop()+mostCurrent._uptext_panel.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),(int) (mostCurrent._activity.getHeight()-mostCurrent._ban_panel.getHeight()-mostCurrent._uptext_panel.getHeight()-mostCurrent._create_panel.getHeight()));
 //BA.debugLineNum = 369;BA.debugLine="spinners_list_data";
_spinners_list_data();
 //BA.debugLineNum = 370;BA.debugLine="End Sub";
return "";
}
public static String  _spinners_list_data() throws Exception{
int _i = 0;
int _ii = 0;
int _iii = 0;
 //BA.debugLineNum = 277;BA.debugLine="Sub spinners_list_data";
 //BA.debugLineNum = 278;BA.debugLine="list_bloodgroup.Initialize";
_list_bloodgroup.Initialize();
 //BA.debugLineNum = 279;BA.debugLine="list_donate_confirm.Initialize";
_list_donate_confirm.Initialize();
 //BA.debugLineNum = 280;BA.debugLine="list_bday_m.Initialize";
_list_bday_m.Initialize();
 //BA.debugLineNum = 281;BA.debugLine="list_bday_d.Initialize";
_list_bday_d.Initialize();
 //BA.debugLineNum = 282;BA.debugLine="list_bday_y.Initialize";
_list_bday_y.Initialize();
 //BA.debugLineNum = 283;BA.debugLine="list_location_b.Initialize";
_list_location_b.Initialize();
 //BA.debugLineNum = 284;BA.debugLine="list_location_s.Initialize";
_list_location_s.Initialize();
 //BA.debugLineNum = 287;BA.debugLine="list_bloodgroup.Add(\"A\")";
_list_bloodgroup.Add((Object)("A"));
 //BA.debugLineNum = 288;BA.debugLine="list_bloodgroup.Add(\"B\")";
_list_bloodgroup.Add((Object)("B"));
 //BA.debugLineNum = 289;BA.debugLine="list_bloodgroup.Add(\"O\")";
_list_bloodgroup.Add((Object)("O"));
 //BA.debugLineNum = 290;BA.debugLine="list_bloodgroup.Add(\"AB\")";
_list_bloodgroup.Add((Object)("AB"));
 //BA.debugLineNum = 291;BA.debugLine="list_bloodgroup.Add(\"A+\")";
_list_bloodgroup.Add((Object)("A+"));
 //BA.debugLineNum = 292;BA.debugLine="list_bloodgroup.Add(\"B+\")";
_list_bloodgroup.Add((Object)("B+"));
 //BA.debugLineNum = 293;BA.debugLine="list_bloodgroup.Add(\"O+\")";
_list_bloodgroup.Add((Object)("O+"));
 //BA.debugLineNum = 294;BA.debugLine="list_bloodgroup.Add(\"AB+\")";
_list_bloodgroup.Add((Object)("AB+"));
 //BA.debugLineNum = 295;BA.debugLine="list_bloodgroup.Add(\"A-\")";
_list_bloodgroup.Add((Object)("A-"));
 //BA.debugLineNum = 296;BA.debugLine="list_bloodgroup.Add(\"B-\")";
_list_bloodgroup.Add((Object)("B-"));
 //BA.debugLineNum = 297;BA.debugLine="list_bloodgroup.Add(\"O-\")";
_list_bloodgroup.Add((Object)("O-"));
 //BA.debugLineNum = 298;BA.debugLine="list_bloodgroup.Add(\"AB-\")";
_list_bloodgroup.Add((Object)("AB-"));
 //BA.debugLineNum = 299;BA.debugLine="spin_bloodgroup.AddAll(list_bloodgroup)";
mostCurrent._spin_bloodgroup.AddAll(_list_bloodgroup);
 //BA.debugLineNum = 301;BA.debugLine="list_donate_confirm.Add(\"YES\")";
_list_donate_confirm.Add((Object)("YES"));
 //BA.debugLineNum = 302;BA.debugLine="list_donate_confirm.Add(\"NO\")";
_list_donate_confirm.Add((Object)("NO"));
 //BA.debugLineNum = 303;BA.debugLine="spin_donate_confirm.AddAll(list_donate_confirm)";
mostCurrent._spin_donate_confirm.AddAll(_list_donate_confirm);
 //BA.debugLineNum = 305;BA.debugLine="For i = 1 To 31";
{
final int step24 = 1;
final int limit24 = (int) (31);
for (_i = (int) (1) ; (step24 > 0 && _i <= limit24) || (step24 < 0 && _i >= limit24); _i = ((int)(0 + _i + step24)) ) {
 //BA.debugLineNum = 306;BA.debugLine="list_bday_d.Add(i)";
_list_bday_d.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 308;BA.debugLine="For ii = 1940 To 2017";
{
final int step27 = 1;
final int limit27 = (int) (2017);
for (_ii = (int) (1940) ; (step27 > 0 && _ii <= limit27) || (step27 < 0 && _ii >= limit27); _ii = ((int)(0 + _ii + step27)) ) {
 //BA.debugLineNum = 309;BA.debugLine="list_bday_y.Add(ii)";
_list_bday_y.Add((Object)(_ii));
 }
};
 //BA.debugLineNum = 311;BA.debugLine="For iii = 1 To 12";
{
final int step30 = 1;
final int limit30 = (int) (12);
for (_iii = (int) (1) ; (step30 > 0 && _iii <= limit30) || (step30 < 0 && _iii >= limit30); _iii = ((int)(0 + _iii + step30)) ) {
 //BA.debugLineNum = 312;BA.debugLine="list_bday_m.Add(iii)";
_list_bday_m.Add((Object)(_iii));
 }
};
 //BA.debugLineNum = 314;BA.debugLine="bday_spin_month.AddAll(list_bday_m)";
mostCurrent._bday_spin_month.AddAll(_list_bday_m);
 //BA.debugLineNum = 315;BA.debugLine="bday_spin_day.AddAll(list_bday_d)";
mostCurrent._bday_spin_day.AddAll(_list_bday_d);
 //BA.debugLineNum = 316;BA.debugLine="bday_spin_year.AddAll(list_bday_y)";
mostCurrent._bday_spin_year.AddAll(_list_bday_y);
 //BA.debugLineNum = 318;BA.debugLine="list_location_b.Add(\"Barangay  1\") 'index 0";
_list_location_b.Add((Object)("Barangay  1"));
 //BA.debugLineNum = 319;BA.debugLine="list_location_b.Add(\"Barangay 2\") 'index 1";
_list_location_b.Add((Object)("Barangay 2"));
 //BA.debugLineNum = 320;BA.debugLine="list_location_b.Add(\"Barangay 3\") 'index 2";
_list_location_b.Add((Object)("Barangay 3"));
 //BA.debugLineNum = 321;BA.debugLine="list_location_b.Add(\"Barangay 4\") 'index 3";
_list_location_b.Add((Object)("Barangay 4"));
 //BA.debugLineNum = 322;BA.debugLine="list_location_b.Add(\"Aguisan\") 'index 4";
_list_location_b.Add((Object)("Aguisan"));
 //BA.debugLineNum = 323;BA.debugLine="list_location_b.Add(\"caradio-an\") 'index 5";
_list_location_b.Add((Object)("caradio-an"));
 //BA.debugLineNum = 324;BA.debugLine="list_location_b.Add(\"Buenavista\") 'index 6";
_list_location_b.Add((Object)("Buenavista"));
 //BA.debugLineNum = 325;BA.debugLine="list_location_b.Add(\"Cabadiangan\") 'index 7";
_list_location_b.Add((Object)("Cabadiangan"));
 //BA.debugLineNum = 326;BA.debugLine="list_location_b.Add(\"Cabanbanan\") 'index 8";
_list_location_b.Add((Object)("Cabanbanan"));
 //BA.debugLineNum = 327;BA.debugLine="list_location_b.Add(\"Carabalan\") 'index 9";
_list_location_b.Add((Object)("Carabalan"));
 //BA.debugLineNum = 328;BA.debugLine="list_location_b.Add(\"Libacao\") 'index 10";
_list_location_b.Add((Object)("Libacao"));
 //BA.debugLineNum = 329;BA.debugLine="list_location_b.Add(\"Mahalang\") 'index 11";
_list_location_b.Add((Object)("Mahalang"));
 //BA.debugLineNum = 330;BA.debugLine="list_location_b.Add(\"Mambagaton\") 'index 12";
_list_location_b.Add((Object)("Mambagaton"));
 //BA.debugLineNum = 331;BA.debugLine="list_location_b.Add(\"Nabalian\") 'index 13";
_list_location_b.Add((Object)("Nabalian"));
 //BA.debugLineNum = 332;BA.debugLine="list_location_b.Add(\"San Antonio\") 'index 14";
_list_location_b.Add((Object)("San Antonio"));
 //BA.debugLineNum = 333;BA.debugLine="list_location_b.Add(\"Saraet\") 'index 15";
_list_location_b.Add((Object)("Saraet"));
 //BA.debugLineNum = 334;BA.debugLine="list_location_b.Add(\"Suay\") 'index 16";
_list_location_b.Add((Object)("Suay"));
 //BA.debugLineNum = 335;BA.debugLine="list_location_b.Add(\"Talaban\") 'index 17";
_list_location_b.Add((Object)("Talaban"));
 //BA.debugLineNum = 336;BA.debugLine="list_location_b.Add(\"Tooy\") 'index 18";
_list_location_b.Add((Object)("Tooy"));
 //BA.debugLineNum = 343;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 344;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 345;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 346;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 347;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 348;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 349;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 350;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 //BA.debugLineNum = 352;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 353;BA.debugLine="location_spin_brgy.AddAll(list_location_b)";
mostCurrent._location_spin_brgy.AddAll(_list_location_b);
 //BA.debugLineNum = 355;BA.debugLine="End Sub";
return "";
}
public static String  _street_lat_lng() throws Exception{
 //BA.debugLineNum = 371;BA.debugLine="Sub street_lat_lng";
 //BA.debugLineNum = 372;BA.debugLine="If brgy_index == 0 And street_index == 0 Then";
if (_brgy_index==0 && _street_index==0) { 
 //BA.debugLineNum = 373;BA.debugLine="lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 374;BA.debugLine="lng = \"122.869168\"";
_lng = "122.869168";
 }else if(_brgy_index==0 && _street_index==1) { 
 //BA.debugLineNum = 376;BA.debugLine="lat = \"10.097226\"";
_lat = "10.097226";
 //BA.debugLineNum = 377;BA.debugLine="lng = \"122.870659\"";
_lng = "122.870659";
 }else if(_brgy_index==0 && _street_index==2) { 
 //BA.debugLineNum = 379;BA.debugLine="lat = \"10.097711\"";
_lat = "10.097711";
 //BA.debugLineNum = 380;BA.debugLine="lng = \"122.868378\"";
_lng = "122.868378";
 }else if(_brgy_index==0 && _street_index==3) { 
 //BA.debugLineNum = 382;BA.debugLine="lat = \"10.098293\"";
_lat = "10.098293";
 //BA.debugLineNum = 383;BA.debugLine="lng = \"122.868977\"";
_lng = "122.868977";
 }else if(_brgy_index==0 && _street_index==4) { 
 //BA.debugLineNum = 385;BA.debugLine="lat = \"10.097031\"";
_lat = "10.097031";
 //BA.debugLineNum = 386;BA.debugLine="lng = \"122.868764\"";
_lng = "122.868764";
 }else if(_brgy_index==0 && _street_index==5) { 
 //BA.debugLineNum = 388;BA.debugLine="lat = \"10.096021\"";
_lat = "10.096021";
 //BA.debugLineNum = 389;BA.debugLine="lng = \"122.869737\"";
_lng = "122.869737";
 }else if(_brgy_index==0 && _street_index==6) { 
 //BA.debugLineNum = 391;BA.debugLine="lat = \"10.095142\"";
_lat = "10.095142";
 //BA.debugLineNum = 392;BA.debugLine="lng = \"122.868317\"";
_lng = "122.868317";
 }else if(_brgy_index==0 && _street_index==7) { 
 //BA.debugLineNum = 394;BA.debugLine="lat = \"10.095303\"";
_lat = "10.095303";
 //BA.debugLineNum = 395;BA.debugLine="lng = \"122.869509\"";
_lng = "122.869509";
 };
 //BA.debugLineNum = 398;BA.debugLine="If brgy_index == 1 And street_index == 0 Then 'br";
if (_brgy_index==1 && _street_index==0) { 
 //BA.debugLineNum = 399;BA.debugLine="lat = \"10.101356\"";
_lat = "10.101356";
 //BA.debugLineNum = 400;BA.debugLine="lng = \"122.870075\"";
_lng = "122.870075";
 }else if(_brgy_index==1 && _street_index==1) { 
 //BA.debugLineNum = 402;BA.debugLine="lat = \"10.100583\"";
_lat = "10.100583";
 //BA.debugLineNum = 403;BA.debugLine="lng = \"122.870176\"";
_lng = "122.870176";
 }else if(_brgy_index==1 && _street_index==2) { 
 //BA.debugLineNum = 405;BA.debugLine="lat = \"10.100031\"";
_lat = "10.100031";
 //BA.debugLineNum = 406;BA.debugLine="lng = \"122.870623\"";
_lng = "122.870623";
 }else if(_brgy_index==1 && _street_index==3) { 
 //BA.debugLineNum = 408;BA.debugLine="lat = \"10.101327\"";
_lat = "10.101327";
 //BA.debugLineNum = 409;BA.debugLine="lng = \"122.871177\"";
_lng = "122.871177";
 }else if(_brgy_index==1 && _street_index==4) { 
 //BA.debugLineNum = 411;BA.debugLine="lat = \"10.103330\"";
_lat = "10.103330";
 //BA.debugLineNum = 412;BA.debugLine="lng = \"122.871391\"";
_lng = "122.871391";
 }else if(_brgy_index==1 && _street_index==5) { 
 //BA.debugLineNum = 414;BA.debugLine="lat = \"10.102317\"";
_lat = "10.102317";
 //BA.debugLineNum = 415;BA.debugLine="lng = \"122.870755\"";
_lng = "122.870755";
 }else if(_brgy_index==1 && _street_index==6) { 
 //BA.debugLineNum = 417;BA.debugLine="lat = \"10.104250\"";
_lat = "10.104250";
 //BA.debugLineNum = 418;BA.debugLine="lng = \"122.882834\"";
_lng = "122.882834";
 }else if(_brgy_index==1 && _street_index==7) { 
 //BA.debugLineNum = 420;BA.debugLine="lat = \"10.104943\"";
_lat = "10.104943";
 //BA.debugLineNum = 421;BA.debugLine="lng = \"122.885207\"";
_lng = "122.885207";
 }else if(_brgy_index==1 && _street_index==8) { 
 //BA.debugLineNum = 423;BA.debugLine="lat = \"10.101843\"";
_lat = "10.101843";
 //BA.debugLineNum = 424;BA.debugLine="lng = \"122.871020\"";
_lng = "122.871020";
 }else if(_brgy_index==1 && _street_index==9) { 
 //BA.debugLineNum = 426;BA.debugLine="lat = \"10.103477\"";
_lat = "10.103477";
 //BA.debugLineNum = 427;BA.debugLine="lng = \"122.870042\"";
_lng = "122.870042";
 }else if(_brgy_index==1 && _street_index==10) { 
 //BA.debugLineNum = 429;BA.debugLine="lat = \"10.100710\"";
_lat = "10.100710";
 //BA.debugLineNum = 430;BA.debugLine="lng = \"122.870889\"";
_lng = "122.870889";
 };
 //BA.debugLineNum = 433;BA.debugLine="If brgy_index == 2 And street_index == 0 Then 'br";
if (_brgy_index==2 && _street_index==0) { 
 //BA.debugLineNum = 434;BA.debugLine="lat = \"10.095478\"";
_lat = "10.095478";
 //BA.debugLineNum = 435;BA.debugLine="lng = \"122.871176\"";
_lng = "122.871176";
 }else if(_brgy_index==2 && _street_index==1) { 
 //BA.debugLineNum = 437;BA.debugLine="lat = \"10.098599\"";
_lat = "10.098599";
 //BA.debugLineNum = 438;BA.debugLine="lng = \"122.871761\"";
_lng = "122.871761";
 }else if(_brgy_index==2 && _street_index==2) { 
 //BA.debugLineNum = 440;BA.debugLine="lat = \"10.094573\"";
_lat = "10.094573";
 //BA.debugLineNum = 441;BA.debugLine="lng = \"122.870340\"";
_lng = "122.870340";
 }else if(_brgy_index==2 && _street_index==3) { 
 //BA.debugLineNum = 443;BA.debugLine="lat = \"10.098313\"";
_lat = "10.098313";
 //BA.debugLineNum = 444;BA.debugLine="lng = \"122.875223\"";
_lng = "122.875223";
 }else if(_brgy_index==2 && _street_index==4) { 
 //BA.debugLineNum = 446;BA.debugLine="lat = \"10.092235\"";
_lat = "10.092235";
 //BA.debugLineNum = 447;BA.debugLine="lng = \"122.874356\"";
_lng = "122.874356";
 }else if(_brgy_index==2 && _street_index==5) { 
 //BA.debugLineNum = 449;BA.debugLine="lat = \"10.103982\"";
_lat = "10.103982";
 //BA.debugLineNum = 450;BA.debugLine="lng = \"122.885996\"";
_lng = "122.885996";
 }else if(_brgy_index==2 && _street_index==6) { 
 //BA.debugLineNum = 452;BA.debugLine="lat = \"10.102170\"";
_lat = "10.102170";
 //BA.debugLineNum = 453;BA.debugLine="lng = \"122.882390\"";
_lng = "122.882390";
 }else if(_brgy_index==2 && _street_index==7) { 
 //BA.debugLineNum = 455;BA.debugLine="lat = \"10.103272\"";
_lat = "10.103272";
 //BA.debugLineNum = 456;BA.debugLine="lng = \"122.883948\"";
_lng = "122.883948";
 }else if(_brgy_index==2 && _street_index==8) { 
 //BA.debugLineNum = 458;BA.debugLine="lat = \"10.103849\"";
_lat = "10.103849";
 //BA.debugLineNum = 459;BA.debugLine="lng = \"122.884602\"";
_lng = "122.884602";
 }else if(_brgy_index==2 && _street_index==9) { 
 //BA.debugLineNum = 461;BA.debugLine="lat = \"10.101033\"";
_lat = "10.101033";
 //BA.debugLineNum = 462;BA.debugLine="lng = \"122.874480\"";
_lng = "122.874480";
 };
 //BA.debugLineNum = 465;BA.debugLine="If brgy_index == 3 And street_index == 0 Then 'b";
if (_brgy_index==3 && _street_index==0) { 
 //BA.debugLineNum = 466;BA.debugLine="lat = \"10.121855\"";
_lat = "10.121855";
 //BA.debugLineNum = 467;BA.debugLine="lng = \"122.872266\"";
_lng = "122.872266";
 }else if(_brgy_index==3 && _street_index==1) { 
 //BA.debugLineNum = 469;BA.debugLine="lat = \"10.116699\"";
_lat = "10.116699";
 //BA.debugLineNum = 470;BA.debugLine="lng = \"122.871783\"";
_lng = "122.871783";
 }else if(_brgy_index==3 && _street_index==2) { 
 //BA.debugLineNum = 472;BA.debugLine="lat = \"10.116024\"";
_lat = "10.116024";
 //BA.debugLineNum = 473;BA.debugLine="lng = \"122.872477\"";
_lng = "122.872477";
 }else if(_brgy_index==3 && _street_index==3) { 
 //BA.debugLineNum = 475;BA.debugLine="lat = \"10.114588\"";
_lat = "10.114588";
 //BA.debugLineNum = 476;BA.debugLine="lng = \"122.872515\"";
_lng = "122.872515";
 }else if(_brgy_index==3 && _street_index==4) { 
 //BA.debugLineNum = 478;BA.debugLine="lat = \"10.112140\"";
_lat = "10.112140";
 //BA.debugLineNum = 479;BA.debugLine="lng = \"122.872161\"";
_lng = "122.872161";
 }else if(_brgy_index==3 && _street_index==5) { 
 //BA.debugLineNum = 481;BA.debugLine="lat = \"10.111531\"";
_lat = "10.111531";
 //BA.debugLineNum = 482;BA.debugLine="lng = \"122.871542\"";
_lng = "122.871542";
 }else if(_brgy_index==3 && _street_index==6) { 
 //BA.debugLineNum = 484;BA.debugLine="lat = \"10.107168\"";
_lat = "10.107168";
 //BA.debugLineNum = 485;BA.debugLine="lng = \"122.871766\"";
_lng = "122.871766";
 }else if(_brgy_index==3 && _street_index==7) { 
 //BA.debugLineNum = 487;BA.debugLine="lat = \"10.106570\"";
_lat = "10.106570";
 //BA.debugLineNum = 488;BA.debugLine="lng = \"122.875197\"";
_lng = "122.875197";
 }else if(_brgy_index==3 && _street_index==8) { 
 //BA.debugLineNum = 490;BA.debugLine="lat = \"10.105759\"";
_lat = "10.105759";
 //BA.debugLineNum = 491;BA.debugLine="lng = \"122.871537\"";
_lng = "122.871537";
 };
 //BA.debugLineNum = 494;BA.debugLine="If brgy_index == 4 And street_index == 0 Then 'A";
if (_brgy_index==4 && _street_index==0) { 
 //BA.debugLineNum = 495;BA.debugLine="lat = \"10.165214\"";
_lat = "10.165214";
 //BA.debugLineNum = 496;BA.debugLine="lng = \"122.865433\"";
_lng = "122.865433";
 }else if(_brgy_index==4 && _street_index==1) { 
 //BA.debugLineNum = 498;BA.debugLine="lat = \"10.154170\"";
_lat = "10.154170";
 //BA.debugLineNum = 499;BA.debugLine="lng = \"122.867255\"";
_lng = "122.867255";
 }else if(_brgy_index==4 && _street_index==2) { 
 //BA.debugLineNum = 501;BA.debugLine="lat = \"10.161405\"";
_lat = "10.161405";
 //BA.debugLineNum = 502;BA.debugLine="lng = \"122.862692\"";
_lng = "122.862692";
 }else if(_brgy_index==4 && _street_index==3) { 
 //BA.debugLineNum = 504;BA.debugLine="lat = \"10.168471\"";
_lat = "10.168471";
 //BA.debugLineNum = 505;BA.debugLine="lng = \"122.860955\"";
_lng = "122.860955";
 }else if(_brgy_index==4 && _street_index==4) { 
 //BA.debugLineNum = 507;BA.debugLine="lat = \"10.172481\"";
_lat = "10.172481";
 //BA.debugLineNum = 508;BA.debugLine="lng = \"122.858629\"";
_lng = "122.858629";
 }else if(_brgy_index==4 && _street_index==5) { 
 //BA.debugLineNum = 510;BA.debugLine="lat = \"10.166561\"";
_lat = "10.166561";
 //BA.debugLineNum = 511;BA.debugLine="lng = \"122.859428\"";
_lng = "122.859428";
 }else if(_brgy_index==4 && _street_index==6) { 
 //BA.debugLineNum = 513;BA.debugLine="lat = \"10.163510\"";
_lat = "10.163510";
 //BA.debugLineNum = 514;BA.debugLine="lng = \"122.860074\"";
_lng = "122.860074";
 }else if(_brgy_index==4 && _street_index==7) { 
 //BA.debugLineNum = 516;BA.debugLine="lat = \"10.161033\"";
_lat = "10.161033";
 //BA.debugLineNum = 517;BA.debugLine="lng = \"122.859773\"";
_lng = "122.859773";
 }else if(_brgy_index==4 && _street_index==8) { 
 //BA.debugLineNum = 519;BA.debugLine="lat = \"10.159280\"";
_lat = "10.159280";
 //BA.debugLineNum = 520;BA.debugLine="lng = \"122.861621\"";
_lng = "122.861621";
 }else if(_brgy_index==4 && _street_index==9) { 
 //BA.debugLineNum = 522;BA.debugLine="lat = \"10.159062\"";
_lat = "10.159062";
 //BA.debugLineNum = 523;BA.debugLine="lng = \"122.860209\"";
_lng = "122.860209";
 }else if(_brgy_index==4 && _street_index==10) { 
 //BA.debugLineNum = 525;BA.debugLine="lat = \"10.181112\"";
_lat = "10.181112";
 //BA.debugLineNum = 526;BA.debugLine="lng = \"122.864670\"";
_lng = "122.864670";
 }else if(_brgy_index==4 && _street_index==11) { 
 //BA.debugLineNum = 528;BA.debugLine="lat = \"10.167295\"";
_lat = "10.167295";
 //BA.debugLineNum = 529;BA.debugLine="lng = \"122.857858\"";
_lng = "122.857858";
 };
 //BA.debugLineNum = 532;BA.debugLine="If brgy_index == 5 And street_index == 0 Then 'ca";
if (_brgy_index==5 && _street_index==0) { 
 //BA.debugLineNum = 533;BA.debugLine="lat = \"10.092993\"";
_lat = "10.092993";
 //BA.debugLineNum = 534;BA.debugLine="lng = \"122.861694\"";
_lng = "122.861694";
 }else if(_brgy_index==5 && _street_index==1) { 
 //BA.debugLineNum = 536;BA.debugLine="lat = \"10.090587\"";
_lat = "10.090587";
 //BA.debugLineNum = 537;BA.debugLine="lng = \"122.868414\"";
_lng = "122.868414";
 }else if(_brgy_index==5 && _street_index==2) { 
 //BA.debugLineNum = 539;BA.debugLine="lat = \"10.091551\"";
_lat = "10.091551";
 //BA.debugLineNum = 540;BA.debugLine="lng = \"122.869249\"";
_lng = "122.869249";
 }else if(_brgy_index==5 && _street_index==3) { 
 //BA.debugLineNum = 542;BA.debugLine="lat = \"10.086452\"";
_lat = "10.086452";
 //BA.debugLineNum = 543;BA.debugLine="lng = \"122.865742\"";
_lng = "122.865742";
 }else if(_brgy_index==5 && _street_index==4) { 
 //BA.debugLineNum = 545;BA.debugLine="lat = \"10.083507\"";
_lat = "10.083507";
 //BA.debugLineNum = 546;BA.debugLine="lng = \"122.858928\"";
_lng = "122.858928";
 }else if(_brgy_index==5 && _street_index==5) { 
 //BA.debugLineNum = 548;BA.debugLine="lat = \"10.077131\"";
_lat = "10.077131";
 //BA.debugLineNum = 549;BA.debugLine="lng = \"122.864236\"";
_lng = "122.864236";
 }else if(_brgy_index==5 && _street_index==6) { 
 //BA.debugLineNum = 551;BA.debugLine="lat = \"10.081722\"";
_lat = "10.081722";
 //BA.debugLineNum = 552;BA.debugLine="lng = \"122.882661\"";
_lng = "122.882661";
 }else if(_brgy_index==5 && _street_index==7) { 
 //BA.debugLineNum = 554;BA.debugLine="lat = \"10.081822\"";
_lat = "10.081822";
 //BA.debugLineNum = 555;BA.debugLine="lng = \"122.868295\"";
_lng = "122.868295";
 }else if(_brgy_index==5 && _street_index==8) { 
 //BA.debugLineNum = 557;BA.debugLine="lat = \"10.079513\"";
_lat = "10.079513";
 //BA.debugLineNum = 558;BA.debugLine="lng = \"122.876610\"";
_lng = "122.876610";
 }else if(_brgy_index==5 && _street_index==9) { 
 //BA.debugLineNum = 560;BA.debugLine="lat = \"10.068560\"";
_lat = "10.068560";
 //BA.debugLineNum = 561;BA.debugLine="lng = \"122.887366\"";
_lng = "122.887366";
 }else if(_brgy_index==5 && _street_index==10) { 
 //BA.debugLineNum = 563;BA.debugLine="lat = \"10.066934\"";
_lat = "10.066934";
 //BA.debugLineNum = 564;BA.debugLine="lng = \"122.871963\"";
_lng = "122.871963";
 }else if(_brgy_index==5 && _street_index==11) { 
 //BA.debugLineNum = 566;BA.debugLine="lat = \"10.064251\"";
_lat = "10.064251";
 //BA.debugLineNum = 567;BA.debugLine="lng = \"122.883023\"";
_lng = "122.883023";
 }else if(_brgy_index==5 && _street_index==12) { 
 //BA.debugLineNum = 569;BA.debugLine="lat = \"10.058546\"";
_lat = "10.058546";
 //BA.debugLineNum = 570;BA.debugLine="lng = \"122.882968\"";
_lng = "122.882968";
 }else if(_brgy_index==5 && _street_index==13) { 
 //BA.debugLineNum = 572;BA.debugLine="lat = \"10.054104\"";
_lat = "10.054104";
 //BA.debugLineNum = 573;BA.debugLine="lng = \"122.885506\"";
_lng = "122.885506";
 }else if(_brgy_index==5 && _street_index==14) { 
 //BA.debugLineNum = 575;BA.debugLine="lat = \"10.049464\"";
_lat = "10.049464";
 //BA.debugLineNum = 576;BA.debugLine="lng = \"122.885667\"";
_lng = "122.885667";
 }else if(_brgy_index==5 && _street_index==15) { 
 //BA.debugLineNum = 578;BA.debugLine="lat = \"10.041580\"";
_lat = "10.041580";
 //BA.debugLineNum = 579;BA.debugLine="lng = \"122.900269\"";
_lng = "122.900269";
 }else if(_brgy_index==5 && _street_index==16) { 
 //BA.debugLineNum = 581;BA.debugLine="lat = \"10.041395\"";
_lat = "10.041395";
 //BA.debugLineNum = 582;BA.debugLine="lng = \"122.906248\"";
_lng = "122.906248";
 };
 //BA.debugLineNum = 585;BA.debugLine="If brgy_index == 6 And street_index == 0 Then 'Bu";
if (_brgy_index==6 && _street_index==0) { 
 //BA.debugLineNum = 586;BA.debugLine="lat = \"10.035728\"";
_lat = "10.035728";
 //BA.debugLineNum = 587;BA.debugLine="lng = \"122.847547\"";
_lng = "122.847547";
 }else if(_brgy_index==6 && _street_index==1) { 
 //BA.debugLineNum = 589;BA.debugLine="lat = \"10.000603\"";
_lat = "10.000603";
 //BA.debugLineNum = 590;BA.debugLine="lng = \"122.885243\"";
_lng = "122.885243";
 }else if(_brgy_index==6 && _street_index==2) { 
 //BA.debugLineNum = 592;BA.debugLine="lat = \"10.000521\"";
_lat = "10.000521";
 //BA.debugLineNum = 593;BA.debugLine="lng = \"122.895867\"";
_lng = "122.895867";
 }else if(_brgy_index==6 && _street_index==3) { 
 //BA.debugLineNum = 595;BA.debugLine="lat = \"9.943276\"";
_lat = "9.943276";
 //BA.debugLineNum = 596;BA.debugLine="lng = \"122.975801\"";
_lng = "122.975801";
 };
 //BA.debugLineNum = 599;BA.debugLine="If brgy_index == 7 And street_index == 0 Then '";
if (_brgy_index==7 && _street_index==0) { 
 //BA.debugLineNum = 600;BA.debugLine="lat = \"10.156301\"";
_lat = "10.156301";
 //BA.debugLineNum = 601;BA.debugLine="lng = \"122.941207\"";
_lng = "122.941207";
 }else if(_brgy_index==7 && _street_index==1) { 
 //BA.debugLineNum = 603;BA.debugLine="lat = \"10.142692\"";
_lat = "10.142692";
 //BA.debugLineNum = 604;BA.debugLine="lng = \"122.947560\"";
_lng = "122.947560";
 }else if(_brgy_index==7 && _street_index==2) { 
 //BA.debugLineNum = 606;BA.debugLine="lat = \"10.139494\"";
_lat = "10.139494";
 //BA.debugLineNum = 607;BA.debugLine="lng = \"122.942788\"";
_lng = "122.942788";
 }else if(_brgy_index==7 && _street_index==3) { 
 //BA.debugLineNum = 609;BA.debugLine="lat = \"10.110265\"";
_lat = "10.110265";
 //BA.debugLineNum = 610;BA.debugLine="lng = \"122.947908\"";
_lng = "122.947908";
 }else if(_brgy_index==7 && _street_index==4) { 
 //BA.debugLineNum = 612;BA.debugLine="lat = \"10.127828\"";
_lat = "10.127828";
 //BA.debugLineNum = 613;BA.debugLine="lng = \"122.950197\"";
_lng = "122.950197";
 }else if(_brgy_index==7 && _street_index==5) { 
 //BA.debugLineNum = 615;BA.debugLine="lat = \"10.125287\"";
_lat = "10.125287";
 //BA.debugLineNum = 616;BA.debugLine="lng = \"122.945735\"";
_lng = "122.945735";
 }else if(_brgy_index==7 && _street_index==6) { 
 //BA.debugLineNum = 618;BA.debugLine="lat = \"10.143975\"";
_lat = "10.143975";
 //BA.debugLineNum = 619;BA.debugLine="lng = \"122.930610\"";
_lng = "122.930610";
 }else if(_brgy_index==7 && _street_index==7) { 
 //BA.debugLineNum = 621;BA.debugLine="lat = \"10.137563\"";
_lat = "10.137563";
 //BA.debugLineNum = 622;BA.debugLine="lng = \"122.939870\"";
_lng = "122.939870";
 }else if(_brgy_index==7 && _street_index==8) { 
 //BA.debugLineNum = 624;BA.debugLine="lat = \"10.150449\"";
_lat = "10.150449";
 //BA.debugLineNum = 625;BA.debugLine="lng = \"122.933761\"";
_lng = "122.933761";
 }else if(_brgy_index==7 && _street_index==9) { 
 //BA.debugLineNum = 627;BA.debugLine="lat = \"10.150286\"";
_lat = "10.150286";
 //BA.debugLineNum = 628;BA.debugLine="lng = \"122.948956\"";
_lng = "122.948956";
 }else if(_brgy_index==7 && _street_index==10) { 
 //BA.debugLineNum = 630;BA.debugLine="lat = \"10.148481\"";
_lat = "10.148481";
 //BA.debugLineNum = 631;BA.debugLine="lng = \"122.943230\"";
_lng = "122.943230";
 }else if(_brgy_index==7 && _street_index==11) { 
 //BA.debugLineNum = 633;BA.debugLine="lat = \"10.106200\"";
_lat = "10.106200";
 //BA.debugLineNum = 634;BA.debugLine="lng = \"122.948051\"";
_lng = "122.948051";
 }else if(_brgy_index==7 && _street_index==12) { 
 //BA.debugLineNum = 636;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 637;BA.debugLine="lng = \"122.926593\"";
_lng = "122.926593";
 }else if(_brgy_index==7 && _street_index==13) { 
 //BA.debugLineNum = 639;BA.debugLine="lat = \"10.120798\"";
_lat = "10.120798";
 //BA.debugLineNum = 640;BA.debugLine="lng = \"122.938371\"";
_lng = "122.938371";
 }else if(_brgy_index==7 && _street_index==14) { 
 //BA.debugLineNum = 642;BA.debugLine="lat = \"10.153217\"";
_lat = "10.153217";
 //BA.debugLineNum = 643;BA.debugLine="lng = \"122.951714\"";
_lng = "122.951714";
 };
 //BA.debugLineNum = 646;BA.debugLine="If brgy_index == 8 And street_index == 0 Then";
if (_brgy_index==8 && _street_index==0) { 
 //BA.debugLineNum = 647;BA.debugLine="lat = \"10.157177\"";
_lat = "10.157177";
 //BA.debugLineNum = 648;BA.debugLine="lng = \"122.895986\"";
_lng = "122.895986";
 }else if(_brgy_index==8 && _street_index==1) { 
 //BA.debugLineNum = 650;BA.debugLine="lat = \"10.180004\"";
_lat = "10.180004";
 //BA.debugLineNum = 651;BA.debugLine="lng = \"122.897999\"";
_lng = "122.897999";
 }else if(_brgy_index==8 && _street_index==2) { 
 //BA.debugLineNum = 653;BA.debugLine="lat = \"10.192848\"";
_lat = "10.192848";
 //BA.debugLineNum = 654;BA.debugLine="lng = \"122.900234\"";
_lng = "122.900234";
 }else if(_brgy_index==8 && _street_index==3) { 
 //BA.debugLineNum = 656;BA.debugLine="lat = \"10.179993\"";
_lat = "10.179993";
 //BA.debugLineNum = 657;BA.debugLine="lng = \"122.904299\"";
_lng = "122.904299";
 }else if(_brgy_index==8 && _street_index==4) { 
 //BA.debugLineNum = 659;BA.debugLine="lat = \"10.183439\"";
_lat = "10.183439";
 //BA.debugLineNum = 660;BA.debugLine="lng = \"122.889622\"";
_lng = "122.889622";
 };
 //BA.debugLineNum = 663;BA.debugLine="If brgy_index == 9 And street_index == 0 Then 'Ca";
if (_brgy_index==9 && _street_index==0) { 
 //BA.debugLineNum = 664;BA.debugLine="lat = \"10.074128\"";
_lat = "10.074128";
 //BA.debugLineNum = 665;BA.debugLine="lng = \"122.981978\"";
_lng = "122.981978";
 }else if(_brgy_index==9 && _street_index==1) { 
 //BA.debugLineNum = 667;BA.debugLine="lat = \"10.109208\"";
_lat = "10.109208";
 //BA.debugLineNum = 668;BA.debugLine="lng = \"122.896717\"";
_lng = "122.896717";
 }else if(_brgy_index==9 && _street_index==2) { 
 //BA.debugLineNum = 670;BA.debugLine="lat = \"10.097119\"";
_lat = "10.097119";
 //BA.debugLineNum = 671;BA.debugLine="lng = \"122.947066\"";
_lng = "122.947066";
 }else if(_brgy_index==9 && _street_index==3) { 
 //BA.debugLineNum = 673;BA.debugLine="lat = \"10.099023\"";
_lat = "10.099023";
 //BA.debugLineNum = 674;BA.debugLine="lng = \"122.971723\"";
_lng = "122.971723";
 }else if(_brgy_index==9 && _street_index==4) { 
 //BA.debugLineNum = 676;BA.debugLine="lat = \"10.119761\"";
_lat = "10.119761";
 //BA.debugLineNum = 677;BA.debugLine="lng = \"122.901613\"";
_lng = "122.901613";
 }else if(_brgy_index==9 && _street_index==5) { 
 //BA.debugLineNum = 679;BA.debugLine="lat = \"10.099402\"";
_lat = "10.099402";
 //BA.debugLineNum = 680;BA.debugLine="lng = \"122.896454\"";
_lng = "122.896454";
 }else if(_brgy_index==9 && _street_index==6) { 
 //BA.debugLineNum = 682;BA.debugLine="lat = \"10.097102\"";
_lat = "10.097102";
 //BA.debugLineNum = 683;BA.debugLine="lng = \"122.922368\"";
_lng = "122.922368";
 }else if(_brgy_index==9 && _street_index==7) { 
 //BA.debugLineNum = 685;BA.debugLine="lat = \"10.095304\"";
_lat = "10.095304";
 //BA.debugLineNum = 686;BA.debugLine="lng = \"122.929242\"";
_lng = "122.929242";
 }else if(_brgy_index==9 && _street_index==8) { 
 //BA.debugLineNum = 688;BA.debugLine="lat = \"10.114128\"";
_lat = "10.114128";
 //BA.debugLineNum = 689;BA.debugLine="lng = \"122.893868\"";
_lng = "122.893868";
 };
 //BA.debugLineNum = 692;BA.debugLine="If brgy_index == 10 And street_index == 0 Then 'L";
if (_brgy_index==10 && _street_index==0) { 
 //BA.debugLineNum = 693;BA.debugLine="lat = \"10.1799469\"";
_lat = "10.1799469";
 //BA.debugLineNum = 694;BA.debugLine="lng = \"122.9068577\"";
_lng = "122.9068577";
 }else if(_brgy_index==10 && _street_index==1) { 
 //BA.debugLineNum = 696;BA.debugLine="lat = \"10.180524\"";
_lat = "10.180524";
 //BA.debugLineNum = 697;BA.debugLine="lng = \"122.906798\"";
_lng = "122.906798";
 }else if(_brgy_index==10 && _street_index==2) { 
 //BA.debugLineNum = 699;BA.debugLine="lat = \"10.173336\"";
_lat = "10.173336";
 //BA.debugLineNum = 700;BA.debugLine="lng = \"122.9118842\"";
_lng = "122.9118842";
 }else if(_brgy_index==10 && _street_index==3) { 
 //BA.debugLineNum = 702;BA.debugLine="lat = \"10.177359\"";
_lat = "10.177359";
 //BA.debugLineNum = 703;BA.debugLine="lng = \"122.913033\"";
_lng = "122.913033";
 }else if(_brgy_index==10 && _street_index==4) { 
 //BA.debugLineNum = 705;BA.debugLine="lat = \"10.179847\"";
_lat = "10.179847";
 //BA.debugLineNum = 706;BA.debugLine="lng = \"122.914160\"";
_lng = "122.914160";
 }else if(_brgy_index==10 && _street_index==5) { 
 //BA.debugLineNum = 708;BA.debugLine="lat = \"10.182718\"";
_lat = "10.182718";
 //BA.debugLineNum = 709;BA.debugLine="lng = \"122.915228\"";
_lng = "122.915228";
 }else if(_brgy_index==10 && _street_index==6) { 
 //BA.debugLineNum = 711;BA.debugLine="lat = \"10.186454\"";
_lat = "10.186454";
 //BA.debugLineNum = 712;BA.debugLine="lng = \"122.916278\"";
_lng = "122.916278";
 }else if(_brgy_index==10 && _street_index==7) { 
 //BA.debugLineNum = 714;BA.debugLine="lat = \"10.168057\"";
_lat = "10.168057";
 //BA.debugLineNum = 715;BA.debugLine="lng = \"122.924501\"";
_lng = "122.924501";
 };
 //BA.debugLineNum = 718;BA.debugLine="If brgy_index == 11 And street_index == 0 Then 'M";
if (_brgy_index==11 && _street_index==0) { 
 //BA.debugLineNum = 719;BA.debugLine="lat = \"10.050418\"";
_lat = "10.050418";
 //BA.debugLineNum = 720;BA.debugLine="lng = \"122.867097\"";
_lng = "122.867097";
 }else if(_brgy_index==11 && _street_index==1) { 
 //BA.debugLineNum = 722;BA.debugLine="lat = \"10.027855\"";
_lat = "10.027855";
 //BA.debugLineNum = 723;BA.debugLine="lng = \"122.906833\"";
_lng = "122.906833";
 }else if(_brgy_index==11 && _street_index==2) { 
 //BA.debugLineNum = 725;BA.debugLine="lat = \"10.027522\"";
_lat = "10.027522";
 //BA.debugLineNum = 726;BA.debugLine="lng = \"122.876637\"";
_lng = "122.876637";
 }else if(_brgy_index==11 && _street_index==3) { 
 //BA.debugLineNum = 728;BA.debugLine="lat = \"10.017254\"";
_lat = "10.017254";
 //BA.debugLineNum = 729;BA.debugLine="lng = \"122.900969\"";
_lng = "122.900969";
 }else if(_brgy_index==11 && _street_index==4) { 
 //BA.debugLineNum = 731;BA.debugLine="lat = \"10.028535\"";
_lat = "10.028535";
 //BA.debugLineNum = 732;BA.debugLine="lng = \"122.900364\"";
_lng = "122.900364";
 }else if(_brgy_index==11 && _street_index==5) { 
 //BA.debugLineNum = 734;BA.debugLine="lat = \"10.025485\"";
_lat = "10.025485";
 //BA.debugLineNum = 735;BA.debugLine="lng = \"122.890023\"";
_lng = "122.890023";
 };
 //BA.debugLineNum = 738;BA.debugLine="If brgy_index == 12 And street_index == 0 Then 'M";
if (_brgy_index==12 && _street_index==0) { 
 //BA.debugLineNum = 739;BA.debugLine="lat = \"10.137572\"";
_lat = "10.137572";
 //BA.debugLineNum = 740;BA.debugLine="lng = \"122.939888\"";
_lng = "122.939888";
 }else if(_brgy_index==12 && _street_index==1) { 
 //BA.debugLineNum = 742;BA.debugLine="lat = \"10.132195\"";
_lat = "10.132195";
 //BA.debugLineNum = 743;BA.debugLine="lng = \"122.899837\"";
_lng = "122.899837";
 }else if(_brgy_index==12 && _street_index==2) { 
 //BA.debugLineNum = 745;BA.debugLine="lat = \"10.123430\"";
_lat = "10.123430";
 //BA.debugLineNum = 746;BA.debugLine="lng = \"122.892250\"";
_lng = "122.892250";
 }else if(_brgy_index==12 && _street_index==3) { 
 //BA.debugLineNum = 748;BA.debugLine="lat = \"10.130383\"";
_lat = "10.130383";
 //BA.debugLineNum = 749;BA.debugLine="lng = \"122.893010\"";
_lng = "122.893010";
 }else if(_brgy_index==12 && _street_index==4) { 
 //BA.debugLineNum = 751;BA.debugLine="lat = \"10.123127\"";
_lat = "10.123127";
 //BA.debugLineNum = 752;BA.debugLine="lng = \"122.887952\"";
_lng = "122.887952";
 }else if(_brgy_index==12 && _street_index==5) { 
 //BA.debugLineNum = 754;BA.debugLine="lat = \"10.131098\"";
_lat = "10.131098";
 //BA.debugLineNum = 755;BA.debugLine="lng = \"122.879801\"";
_lng = "122.879801";
 }else if(_brgy_index==12 && _street_index==6) { 
 //BA.debugLineNum = 757;BA.debugLine="lat = \"10.137485\"";
_lat = "10.137485";
 //BA.debugLineNum = 758;BA.debugLine="lng = \"122.911434\"";
_lng = "122.911434";
 }else if(_brgy_index==12 && _street_index==7) { 
 //BA.debugLineNum = 760;BA.debugLine="lat = \"10.106803\"";
_lat = "10.106803";
 //BA.debugLineNum = 761;BA.debugLine="lng = \"122.885727\"";
_lng = "122.885727";
 }else if(_brgy_index==12 && _street_index==8) { 
 //BA.debugLineNum = 763;BA.debugLine="lat = \"10.115220\"";
_lat = "10.115220";
 //BA.debugLineNum = 764;BA.debugLine="lng = \"122.890515\"";
_lng = "122.890515";
 }else if(_brgy_index==12 && _street_index==9) { 
 //BA.debugLineNum = 766;BA.debugLine="lat = \"10.108754\"";
_lat = "10.108754";
 //BA.debugLineNum = 767;BA.debugLine="lng = \"122.894130\"";
_lng = "122.894130";
 }else if(_brgy_index==12 && _street_index==10) { 
 //BA.debugLineNum = 769;BA.debugLine="lat = \"10.149506\"";
_lat = "10.149506";
 //BA.debugLineNum = 770;BA.debugLine="lng = \"122.897389\"";
_lng = "122.897389";
 }else if(_brgy_index==12 && _street_index==11) { 
 //BA.debugLineNum = 772;BA.debugLine="lat = \"10.122215\"";
_lat = "10.122215";
 //BA.debugLineNum = 773;BA.debugLine="lng = \"122.892160\"";
_lng = "122.892160";
 }else if(_brgy_index==12 && _street_index==12) { 
 //BA.debugLineNum = 775;BA.debugLine="lat = \"10.142698\"";
_lat = "10.142698";
 //BA.debugLineNum = 776;BA.debugLine="lng = \"122.898168\"";
_lng = "122.898168";
 };
 //BA.debugLineNum = 779;BA.debugLine="If brgy_index == 13 And street_index == 0 Then 'N";
if (_brgy_index==13 && _street_index==0) { 
 //BA.debugLineNum = 780;BA.debugLine="lat = \"10.161629\"";
_lat = "10.161629";
 //BA.debugLineNum = 781;BA.debugLine="lng = \"122.872772\"";
_lng = "122.872772";
 }else if(_brgy_index==13 && _street_index==1) { 
 //BA.debugLineNum = 783;BA.debugLine="lat = \"10.161863\"";
_lat = "10.161863";
 //BA.debugLineNum = 784;BA.debugLine="lng = \"122.876192\"";
_lng = "122.876192";
 }else if(_brgy_index==13 && _street_index==2) { 
 //BA.debugLineNum = 786;BA.debugLine="lat = \"10.157407\"";
_lat = "10.157407";
 //BA.debugLineNum = 787;BA.debugLine="lng = \"122.885663\"";
_lng = "122.885663";
 }else if(_brgy_index==13 && _street_index==3) { 
 //BA.debugLineNum = 789;BA.debugLine="lat = \"10.167497\"";
_lat = "10.167497";
 //BA.debugLineNum = 790;BA.debugLine="lng = \"122.879777\"";
_lng = "122.879777";
 }else if(_brgy_index==13 && _street_index==4) { 
 //BA.debugLineNum = 792;BA.debugLine="lat = \"10.176260\"";
_lat = "10.176260";
 //BA.debugLineNum = 793;BA.debugLine="lng = \"122.880815\"";
_lng = "122.880815";
 }else if(_brgy_index==13 && _street_index==5) { 
 //BA.debugLineNum = 795;BA.debugLine="lat = \"10.170524\"";
_lat = "10.170524";
 //BA.debugLineNum = 796;BA.debugLine="lng = \"122.883603\"";
_lng = "122.883603";
 };
 //BA.debugLineNum = 799;BA.debugLine="If brgy_index == 14 And street_index == 0 Then 'S";
if (_brgy_index==14 && _street_index==0) { 
 //BA.debugLineNum = 800;BA.debugLine="lat = \"10.071514\"";
_lat = "10.071514";
 //BA.debugLineNum = 801;BA.debugLine="lng = \"122.916010\"";
_lng = "122.916010";
 }else if(_brgy_index==14 && _street_index==1) { 
 //BA.debugLineNum = 803;BA.debugLine="lat = \"10.069622\"";
_lat = "10.069622";
 //BA.debugLineNum = 804;BA.debugLine="lng = \"122.909890\"";
_lng = "122.909890";
 }else if(_brgy_index==14 && _street_index==2) { 
 //BA.debugLineNum = 806;BA.debugLine="lat = \"10.076890\"";
_lat = "10.076890";
 //BA.debugLineNum = 807;BA.debugLine="lng = \"122.894231\"";
_lng = "122.894231";
 }else if(_brgy_index==14 && _street_index==3) { 
 //BA.debugLineNum = 809;BA.debugLine="lat = \"10.086207\"";
_lat = "10.086207";
 //BA.debugLineNum = 810;BA.debugLine="lng = \"122.914044\"";
_lng = "122.914044";
 }else if(_brgy_index==14 && _street_index==4) { 
 //BA.debugLineNum = 812;BA.debugLine="lat = \"10.067393\"";
_lat = "10.067393";
 //BA.debugLineNum = 813;BA.debugLine="lng = \"122.900935\"";
_lng = "122.900935";
 }else if(_brgy_index==14 && _street_index==5) { 
 //BA.debugLineNum = 815;BA.debugLine="lat = \"10.071900\"";
_lat = "10.071900";
 //BA.debugLineNum = 816;BA.debugLine="lng = \"122.906250\"";
_lng = "122.906250";
 }else if(_brgy_index==14 && _street_index==6) { 
 //BA.debugLineNum = 818;BA.debugLine="lat = \"10.061702\"";
_lat = "10.061702";
 //BA.debugLineNum = 819;BA.debugLine="lng = \"122.896226\"";
_lng = "122.896226";
 }else if(_brgy_index==14 && _street_index==7) { 
 //BA.debugLineNum = 821;BA.debugLine="lat = \"10.054802\"";
_lat = "10.054802";
 //BA.debugLineNum = 822;BA.debugLine="lng = \"122.938688\"";
_lng = "122.938688";
 }else if(_brgy_index==14 && _street_index==8) { 
 //BA.debugLineNum = 824;BA.debugLine="lat = \"10.071827\"";
_lat = "10.071827";
 //BA.debugLineNum = 825;BA.debugLine="lng = \"122.921092\"";
_lng = "122.921092";
 }else if(_brgy_index==14 && _street_index==9) { 
 //BA.debugLineNum = 827;BA.debugLine="lat = \"10.050849\"";
_lat = "10.050849";
 //BA.debugLineNum = 828;BA.debugLine="lng = \"122.907632\"";
_lng = "122.907632";
 };
 //BA.debugLineNum = 831;BA.debugLine="If brgy_index == 15 And street_index == 0 Then 'S";
if (_brgy_index==15 && _street_index==0) { 
 //BA.debugLineNum = 832;BA.debugLine="lat = \"10.155844\"";
_lat = "10.155844";
 //BA.debugLineNum = 833;BA.debugLine="lng = \"122.861129\"";
_lng = "122.861129";
 }else if(_brgy_index==15 && _street_index==1) { 
 //BA.debugLineNum = 835;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 836;BA.debugLine="lng = \"122.861669\"";
_lng = "122.861669";
 }else if(_brgy_index==15 && _street_index==2) { 
 //BA.debugLineNum = 838;BA.debugLine="lat = \"10.147663\"";
_lat = "10.147663";
 //BA.debugLineNum = 839;BA.debugLine="lng = \"122.862471\"";
_lng = "122.862471";
 }else if(_brgy_index==15 && _street_index==3) { 
 //BA.debugLineNum = 841;BA.debugLine="lat = \"10.144440\"";
_lat = "10.144440";
 //BA.debugLineNum = 842;BA.debugLine="lng = \"122.862524\"";
_lng = "122.862524";
 };
 //BA.debugLineNum = 845;BA.debugLine="If brgy_index == 16 And street_index == 0 Then 'S";
if (_brgy_index==16 && _street_index==0) { 
 //BA.debugLineNum = 846;BA.debugLine="lat = \"10.053680\"";
_lat = "10.053680";
 //BA.debugLineNum = 847;BA.debugLine="lng = \"122.843876\"";
_lng = "122.843876";
 }else if(_brgy_index==16 && _street_index==1) { 
 //BA.debugLineNum = 849;BA.debugLine="lat = \"10.055961\"";
_lat = "10.055961";
 //BA.debugLineNum = 850;BA.debugLine="lng = \"122.841980\"";
_lng = "122.841980";
 }else if(_brgy_index==16 && _street_index==2) { 
 //BA.debugLineNum = 852;BA.debugLine="lat = \"10.053363\"";
_lat = "10.053363";
 //BA.debugLineNum = 853;BA.debugLine="lng = \"122.843295\"";
_lng = "122.843295";
 }else if(_brgy_index==16 && _street_index==3) { 
 //BA.debugLineNum = 855;BA.debugLine="lat = \"10.053032\"";
_lat = "10.053032";
 //BA.debugLineNum = 856;BA.debugLine="lng = \"122.842594\"";
_lng = "122.842594";
 }else if(_brgy_index==16 && _street_index==4) { 
 //BA.debugLineNum = 858;BA.debugLine="lat = \"10.052328\"";
_lat = "10.052328";
 //BA.debugLineNum = 859;BA.debugLine="lng = \"122.842835\"";
_lng = "122.842835";
 }else if(_brgy_index==16 && _street_index==5) { 
 //BA.debugLineNum = 861;BA.debugLine="lat = \"10.052573\"";
_lat = "10.052573";
 //BA.debugLineNum = 862;BA.debugLine="lng = \"122.844229\"";
_lng = "122.844229";
 }else if(_brgy_index==16 && _street_index==6) { 
 //BA.debugLineNum = 864;BA.debugLine="lat = \"10.046957\"";
_lat = "10.046957";
 //BA.debugLineNum = 865;BA.debugLine="lng = \"122.839610\"";
_lng = "122.839610";
 }else if(_brgy_index==16 && _street_index==7) { 
 //BA.debugLineNum = 867;BA.debugLine="lat = \"10.035813\"";
_lat = "10.035813";
 //BA.debugLineNum = 868;BA.debugLine="lng = \"122.835364\"";
_lng = "122.835364";
 };
 //BA.debugLineNum = 871;BA.debugLine="If brgy_index == 17 And street_index == 0 Then 'T";
if (_brgy_index==17 && _street_index==0) { 
 //BA.debugLineNum = 872;BA.debugLine="lat = \"10.148233\"";
_lat = "10.148233";
 //BA.debugLineNum = 873;BA.debugLine="lng = \"122.869741\"";
_lng = "122.869741";
 }else if(_brgy_index==17 && _street_index==1) { 
 //BA.debugLineNum = 875;BA.debugLine="lat = \"10.139867\"";
_lat = "10.139867";
 //BA.debugLineNum = 876;BA.debugLine="lng = \"122.869882\"";
_lng = "122.869882";
 }else if(_brgy_index==17 && _street_index==2) { 
 //BA.debugLineNum = 878;BA.debugLine="lat = \"10.126453\"";
_lat = "10.126453";
 //BA.debugLineNum = 879;BA.debugLine="lng = \"122.868927\"";
_lng = "122.868927";
 }else if(_brgy_index==17 && _street_index==3) { 
 //BA.debugLineNum = 881;BA.debugLine="lat = \"10.127470\"";
_lat = "10.127470";
 //BA.debugLineNum = 882;BA.debugLine="lng = \"122.862942\"";
_lng = "122.862942";
 }else if(_brgy_index==17 && _street_index==4) { 
 //BA.debugLineNum = 884;BA.debugLine="lat = \"10.117998\"";
_lat = "10.117998";
 //BA.debugLineNum = 885;BA.debugLine="lng = \"122.866817\"";
_lng = "122.866817";
 }else if(_brgy_index==17 && _street_index==5) { 
 //BA.debugLineNum = 887;BA.debugLine="lat = \"10.108173\"";
_lat = "10.108173";
 //BA.debugLineNum = 888;BA.debugLine="lng = \"122.864592\"";
_lng = "122.864592";
 }else if(_brgy_index==17 && _street_index==6) { 
 //BA.debugLineNum = 890;BA.debugLine="lat = \"10.126115\"";
_lat = "10.126115";
 //BA.debugLineNum = 891;BA.debugLine="lng = \"122.871073\"";
_lng = "122.871073";
 }else if(_brgy_index==17 && _street_index==7) { 
 //BA.debugLineNum = 893;BA.debugLine="lat = \"10.129412\"";
_lat = "10.129412";
 //BA.debugLineNum = 894;BA.debugLine="lng = \"122.869408\"";
_lng = "122.869408";
 }else if(_brgy_index==17 && _street_index==8) { 
 //BA.debugLineNum = 896;BA.debugLine="lat = \"10.134647\"";
_lat = "10.134647";
 //BA.debugLineNum = 897;BA.debugLine="lng = \"122.871841\"";
_lng = "122.871841";
 }else if(_brgy_index==17 && _street_index==9) { 
 //BA.debugLineNum = 899;BA.debugLine="lat = \"10.124801\"";
_lat = "10.124801";
 //BA.debugLineNum = 900;BA.debugLine="lng = \"122.868277\"";
_lng = "122.868277";
 }else if(_brgy_index==17 && _street_index==10) { 
 //BA.debugLineNum = 902;BA.debugLine="lat = \"10.124422\"";
_lat = "10.124422";
 //BA.debugLineNum = 903;BA.debugLine="lng = \"122.866917\"";
_lng = "122.866917";
 };
 //BA.debugLineNum = 906;BA.debugLine="If brgy_index == 18 And street_index == 0 Then 'T";
if (_brgy_index==18 && _street_index==0) { 
 //BA.debugLineNum = 907;BA.debugLine="lat = \"10.065086\"";
_lat = "10.065086";
 //BA.debugLineNum = 908;BA.debugLine="lng = \"122.843793\"";
_lng = "122.843793";
 }else if(_brgy_index==18 && _street_index==1) { 
 //BA.debugLineNum = 910;BA.debugLine="lat = \"10.071356\"";
_lat = "10.071356";
 //BA.debugLineNum = 911;BA.debugLine="lng = \"122.853102\"";
_lng = "122.853102";
 }else if(_brgy_index==18 && _street_index==2) { 
 //BA.debugLineNum = 913;BA.debugLine="lat = \"10.060206\"";
_lat = "10.060206";
 //BA.debugLineNum = 914;BA.debugLine="lng = \"122.850172\"";
_lng = "122.850172";
 }else if(_brgy_index==18 && _street_index==3) { 
 //BA.debugLineNum = 916;BA.debugLine="lat = \"10.057640\"";
_lat = "10.057640";
 //BA.debugLineNum = 917;BA.debugLine="lng = \"122.859242\"";
_lng = "122.859242";
 };
 //BA.debugLineNum = 920;BA.debugLine="Log(\"lat: \"&lat&CRLF&\"lng: \"&lng)";
anywheresoftware.b4a.keywords.Common.Log("lat: "+_lat+anywheresoftware.b4a.keywords.Common.CRLF+"lng: "+_lng);
 //BA.debugLineNum = 923;BA.debugLine="End Sub";
return "";
}
}
