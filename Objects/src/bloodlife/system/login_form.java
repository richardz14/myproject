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

public class login_form extends Activity implements B4AActivity{
	public static login_form mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "bloodlife.system", "bloodlife.system.login_form");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (login_form).");
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
		activityBA = new BA(this, layout, processBA, "bloodlife.system", "bloodlife.system.login_form");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "bloodlife.system.login_form", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (login_form) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (login_form) Resume **");
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
		return login_form.class;
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
        BA.LogInfo("** Activity (login_form) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (login_form) Resume **");
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
public static String _id_query = "";
public static String _name_query = "";
public static boolean _log_click = false;
public static boolean _is_log_in = false;
public static anywheresoftware.b4a.sql.SQL _sqllite = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ban_picture = null;
public anywheresoftware.b4a.objects.PanelWrapper _ban_panel = null;
public anywheresoftware.b4a.objects.PanelWrapper _ban_tools = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_email = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_password = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_email = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_password = null;
public anywheresoftware.b4a.objects.LabelWrapper _label_forgot = null;
public anywheresoftware.b4a.objects.ButtonWrapper _log_in_button = null;
public anywheresoftware.b4a.objects.PanelWrapper _ban_create = null;
public anywheresoftware.b4a.objects.ButtonWrapper _new_acc_button = null;
public bloodlife.system.httpjob _h_email = null;
public bloodlife.system.httpjob _h_pass = null;
public bloodlife.system.httpjob _h_fullname = null;
public bloodlife.system.httpjob _user_id = null;
public bloodlife.system.httpjob _nick_name = null;
public static String _email = "";
public static String _pass = "";
public static String _name = "";
public static String _true_false = "";
public static int _booleancount = 0;
public static int _booleanforgotcount = 0;
public bloodlife.system.calculations _calcs = null;
public anywheresoftware.b4a.objects.PanelWrapper _forgot_pass_pnl = null;
public anywheresoftware.b4a.objects.EditTextWrapper _email_forgot = null;
public anywheresoftware.b4a.objects.EditTextWrapper _nickn_forgot = null;
public anywheresoftware.b4a.objects.EditTextWrapper _newpass = null;
public anywheresoftware.b4a.objects.EditTextWrapper _renewpass = null;
public bloodlife.system.httpjob _forgot_email_job = null;
public bloodlife.system.httpjob _forgot_nick_job = null;
public bloodlife.system.httpjob _forgot_recover_job = null;
public static String _email_for = "";
public static String _nickname_for = "";
public anywheresoftware.b4a.objects.AnimationWrapper _a1 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a2 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a3 = null;
public bloodlife.system.main _main = null;
public bloodlife.system.create_account _create_account = null;
public bloodlife.system.menu_form _menu_form = null;
public bloodlife.system.help_frame _help_frame = null;
public bloodlife.system.search_frame _search_frame = null;
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
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 51;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 53;BA.debugLine="Activity.LoadLayout(\"login_form\")";
mostCurrent._activity.LoadLayout("login_form",mostCurrent.activityBA);
 //BA.debugLineNum = 55;BA.debugLine="Activity.Title = \"LOG IN\"";
mostCurrent._activity.setTitle((Object)("LOG IN"));
 //BA.debugLineNum = 56;BA.debugLine="calcs.Initialize";
mostCurrent._calcs._initialize(processBA);
 //BA.debugLineNum = 57;BA.debugLine="h_email.Initialize(\"email_get\",Me)";
mostCurrent._h_email._initialize(processBA,"email_get",login_form.getObject());
 //BA.debugLineNum = 58;BA.debugLine="h_pass.Initialize(\"pass_get\",Me)";
mostCurrent._h_pass._initialize(processBA,"pass_get",login_form.getObject());
 //BA.debugLineNum = 59;BA.debugLine="h_fullname.Initialize(\"full_name_get\",Me)";
mostCurrent._h_fullname._initialize(processBA,"full_name_get",login_form.getObject());
 //BA.debugLineNum = 60;BA.debugLine="user_id.Initialize(\"user_id_get\",Me)";
mostCurrent._user_id._initialize(processBA,"user_id_get",login_form.getObject());
 //BA.debugLineNum = 61;BA.debugLine="nick_name.Initialize(\"nick_name_get\",Me)";
mostCurrent._nick_name._initialize(processBA,"nick_name_get",login_form.getObject());
 //BA.debugLineNum = 62;BA.debugLine="forgot_email_job.Initialize(\"forgot_email_get\",M";
mostCurrent._forgot_email_job._initialize(processBA,"forgot_email_get",login_form.getObject());
 //BA.debugLineNum = 63;BA.debugLine="forgot_nick_job.Initialize(\"forgot_nick_get\",Me)";
mostCurrent._forgot_nick_job._initialize(processBA,"forgot_nick_get",login_form.getObject());
 //BA.debugLineNum = 64;BA.debugLine="forgot_recover_job.Initialize(\"forgot_recover_jo";
mostCurrent._forgot_recover_job._initialize(processBA,"forgot_recover_job",login_form.getObject());
 //BA.debugLineNum = 66;BA.debugLine="all_settings_layout";
_all_settings_layout();
 //BA.debugLineNum = 67;BA.debugLine="email_forgot.Initialize(\"\")";
mostCurrent._email_forgot.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 68;BA.debugLine="nickN_forgot.Initialize(\"\")";
mostCurrent._nickn_forgot.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 69;BA.debugLine="newPass.Initialize(\"\")";
mostCurrent._newpass.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 70;BA.debugLine="renewPass.Initialize(\"\")";
mostCurrent._renewpass.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 71;BA.debugLine="for_btn_animation";
_for_btn_animation();
 //BA.debugLineNum = 72;BA.debugLine="database_init";
_database_init();
 //BA.debugLineNum = 73;BA.debugLine="sqlLite.Initialize(File.DirInternal,\"mydb.db\",Tru";
_sqllite.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _set_cursor = null;
int _i = 0;
int _choose = 0;
 //BA.debugLineNum = 97;BA.debugLine="Sub Activity_KeyPress(KeyCode As Int) As Boolean";
 //BA.debugLineNum = 99;BA.debugLine="If KeyCode == KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 100;BA.debugLine="Dim set_cursor As Cursor";
_set_cursor = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 101;BA.debugLine="set_cursor = sqlLite.ExecQuery(\"select * from dat";
_set_cursor.setObject((android.database.Cursor)(_sqllite.ExecQuery("select * from data where `id`=1;")));
 //BA.debugLineNum = 102;BA.debugLine="For i = 0 To set_cursor.RowCount - 1";
{
final int step4 = 1;
final int limit4 = (int) (_set_cursor.getRowCount()-1);
for (_i = (int) (0) ; (step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4); _i = ((int)(0 + _i + step4)) ) {
 //BA.debugLineNum = 103;BA.debugLine="set_cursor.Position = i";
_set_cursor.setPosition(_i);
 //BA.debugLineNum = 104;BA.debugLine="If set_cursor.GetString(\"isStart\") == 1 Th";
if ((_set_cursor.GetString("isStart")).equals(BA.NumberToString(1))) { 
 //BA.debugLineNum = 105;BA.debugLine="Dim choose As Int";
_choose = 0;
 //BA.debugLineNum = 106;BA.debugLine="choose = Msgbox2(\"would you like to exit";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2("would you like to exit this application?","C O N F I R M A T I O N","YES","","NO",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 107;BA.debugLine="If choose == DialogResponse.POSITIVE Then";
if (_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 108;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 };
 }else {
 };
 }
};
 }else {
 };
 //BA.debugLineNum = 118;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 247;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 243;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 245;BA.debugLine="End Sub";
return "";
}
public static String  _all_settings_layout() throws Exception{
double _sums = 0;
double _left_sums = 0;
double _left = 0;
anywheresoftware.b4a.objects.drawable.GradientDrawable _gradiant = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _log_grad = null;
int[] _col = null;
 //BA.debugLineNum = 251;BA.debugLine="Public Sub all_settings_layout";
 //BA.debugLineNum = 252;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 253;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 256;BA.debugLine="new_acc_button.Typeface = Typeface.LoadFromAssets";
mostCurrent._new_acc_button.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 257;BA.debugLine="log_in_button.Typeface = Typeface.LoadFromAssets(";
mostCurrent._log_in_button.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 258;BA.debugLine="label_forgot.Typeface = Typeface.LoadFromAssets(\"";
mostCurrent._label_forgot.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 259;BA.debugLine="label_email.Typeface = Typeface.LoadFromAssets(\"Z";
mostCurrent._label_email.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 260;BA.debugLine="label_password.Typeface = Typeface.LoadFromAssets";
mostCurrent._label_password.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 262;BA.debugLine="ban_tools.Color = Colors.Transparent";
mostCurrent._ban_tools.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 263;BA.debugLine="ban_create.SetBackgroundImage(LoadBitmap(File.Dir";
mostCurrent._ban_create.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 265;BA.debugLine="label_email.Gravity = Gravity.CENTER";
mostCurrent._label_email.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 266;BA.debugLine="label_password.Gravity = Gravity.CENTER";
mostCurrent._label_password.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 267;BA.debugLine="label_forgot.Gravity = Gravity.CENTER";
mostCurrent._label_forgot.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 269;BA.debugLine="ban_panel.Width = 100%x";
mostCurrent._ban_panel.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 270;BA.debugLine="ban_picture.Width = ban_panel.Width";
mostCurrent._ban_picture.setWidth(mostCurrent._ban_panel.getWidth());
 //BA.debugLineNum = 271;BA.debugLine="ban_tools.Width = 100%x";
mostCurrent._ban_tools.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 272;BA.debugLine="ban_create.Width = 100%x";
mostCurrent._ban_create.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 273;BA.debugLine="label_email.Width = 30%x";
mostCurrent._label_email.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 274;BA.debugLine="label_password.Width = 30%x";
mostCurrent._label_password.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 275;BA.debugLine="text_email.Width = 50%x";
mostCurrent._text_email.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 276;BA.debugLine="text_password.Width = 50%x";
mostCurrent._text_password.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 277;BA.debugLine="label_forgot.Width = 60%x";
mostCurrent._label_forgot.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA));
 //BA.debugLineNum = 278;BA.debugLine="log_in_button.Width = 40%x";
mostCurrent._log_in_button.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 279;BA.debugLine="new_acc_button.Width = 64%x";
mostCurrent._new_acc_button.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (64),mostCurrent.activityBA));
 //BA.debugLineNum = 281;BA.debugLine="ban_panel.Height = 25%y";
mostCurrent._ban_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 282;BA.debugLine="ban_picture.Height = ban_panel.Height";
mostCurrent._ban_picture.setHeight(mostCurrent._ban_panel.getHeight());
 //BA.debugLineNum = 283;BA.debugLine="ban_create.Height = 16%y";
mostCurrent._ban_create.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (16),mostCurrent.activityBA));
 //BA.debugLineNum = 284;BA.debugLine="ban_tools.Height = Activity.Height - ban_panel.H";
mostCurrent._ban_tools.setHeight((int) (mostCurrent._activity.getHeight()-mostCurrent._ban_panel.getHeight()-mostCurrent._ban_create.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 285;BA.debugLine="label_email.Height = 8%y";
mostCurrent._label_email.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 286;BA.debugLine="label_password.Height = 8%y";
mostCurrent._label_password.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 287;BA.debugLine="text_email.Height = 8%y";
mostCurrent._text_email.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 288;BA.debugLine="text_password.Height = 8%y";
mostCurrent._text_password.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 289;BA.debugLine="label_forgot.Height = 6%y";
mostCurrent._label_forgot.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 290;BA.debugLine="log_in_button.Height = 10%y";
mostCurrent._log_in_button.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 291;BA.debugLine="new_acc_button.Height = 10%y";
mostCurrent._new_acc_button.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 293;BA.debugLine="ban_panel.Top = Activity.Top";
mostCurrent._ban_panel.setTop(mostCurrent._activity.getTop());
 //BA.debugLineNum = 294;BA.debugLine="ban_picture.Top = ban_panel.Top";
mostCurrent._ban_picture.setTop(mostCurrent._ban_panel.getTop());
 //BA.debugLineNum = 295;BA.debugLine="ban_tools.Top = ban_panel.Height + 2dip";
mostCurrent._ban_tools.setTop((int) (mostCurrent._ban_panel.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 296;BA.debugLine="ban_create.Top = ban_tools.Top + ban_tools.Heigh";
mostCurrent._ban_create.setTop((int) (mostCurrent._ban_tools.getTop()+mostCurrent._ban_tools.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)));
 //BA.debugLineNum = 297;BA.debugLine="label_email.Top = 10%y";
mostCurrent._label_email.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 298;BA.debugLine="label_password.Top = 18%y";
mostCurrent._label_password.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 299;BA.debugLine="text_email.Top = 10%y";
mostCurrent._text_email.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 300;BA.debugLine="text_password.Top = 18%y";
mostCurrent._text_password.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 301;BA.debugLine="label_forgot.Top = text_password.Top + text_pass";
mostCurrent._label_forgot.setTop((int) (mostCurrent._text_password.getTop()+mostCurrent._text_password.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 302;BA.debugLine="log_in_button.Top = label_forgot.Top + label_for";
mostCurrent._log_in_button.setTop((int) (mostCurrent._label_forgot.getTop()+mostCurrent._label_forgot.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (17))));
 //BA.debugLineNum = 303;BA.debugLine="Dim sums As Double";
_sums = 0;
 //BA.debugLineNum = 304;BA.debugLine="sums= ban_create.Height/2";
_sums = mostCurrent._ban_create.getHeight()/(double)2;
 //BA.debugLineNum = 305;BA.debugLine="new_acc_button.Top = sums - (sums/2)";
mostCurrent._new_acc_button.setTop((int) (_sums-(_sums/(double)2)));
 //BA.debugLineNum = 307;BA.debugLine="Dim left_sums,left As Double";
_left_sums = 0;
_left = 0;
 //BA.debugLineNum = 308;BA.debugLine="left_sums = (Activity.Width/2)";
_left_sums = (mostCurrent._activity.getWidth()/(double)2);
 //BA.debugLineNum = 309;BA.debugLine="left =  left_sums - (left_sums/2)";
_left = _left_sums-(_left_sums/(double)2);
 //BA.debugLineNum = 310;BA.debugLine="ban_panel.left = 0";
mostCurrent._ban_panel.setLeft((int) (0));
 //BA.debugLineNum = 311;BA.debugLine="ban_picture.left = ban_panel.left";
mostCurrent._ban_picture.setLeft(mostCurrent._ban_panel.getLeft());
 //BA.debugLineNum = 312;BA.debugLine="ban_create.left = 0";
mostCurrent._ban_create.setLeft((int) (0));
 //BA.debugLineNum = 313;BA.debugLine="ban_tools.left = 0";
mostCurrent._ban_tools.setLeft((int) (0));
 //BA.debugLineNum = 314;BA.debugLine="label_email.left = 5%x'left/2";
mostCurrent._label_email.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 315;BA.debugLine="label_password.left = 5%x'left/2";
mostCurrent._label_password.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 316;BA.debugLine="text_email.left =  label_email.left + label_emai";
mostCurrent._text_email.setLeft((int) (mostCurrent._label_email.getLeft()+mostCurrent._label_email.getWidth()));
 //BA.debugLineNum = 317;BA.debugLine="text_password.left = label_password.left + label";
mostCurrent._text_password.setLeft((int) (mostCurrent._label_password.getLeft()+mostCurrent._label_email.getWidth()));
 //BA.debugLineNum = 318;BA.debugLine="label_forgot.left = text_password.left '+ (text_";
mostCurrent._label_forgot.setLeft(mostCurrent._text_password.getLeft());
 //BA.debugLineNum = 319;BA.debugLine="log_in_button.left = 30%x 'text_password.left +";
mostCurrent._log_in_button.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 320;BA.debugLine="new_acc_button.left = 18%x";
mostCurrent._new_acc_button.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 322;BA.debugLine="Dim gradiant,log_grad As GradientDrawable";
_gradiant = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_log_grad = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 323;BA.debugLine="Dim col(2) As Int";
_col = new int[(int) (2)];
;
 //BA.debugLineNum = 324;BA.debugLine="col(0) = Colors.Red";
_col[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 325;BA.debugLine="col(1) = Colors.LightGray";
_col[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.LightGray;
 //BA.debugLineNum = 326;BA.debugLine="gradiant.Initialize(\"TOP_BOTTOM\",col)";
_gradiant.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 327;BA.debugLine="log_grad.Initialize(\"TOP_BOTTOM\",col)";
_log_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 328;BA.debugLine="gradiant.CornerRadius = 5dip";
_gradiant.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 329;BA.debugLine="log_grad.CornerRadius = 5dip";
_log_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 331;BA.debugLine="new_acc_button.Background = gradiant";
mostCurrent._new_acc_button.setBackground((android.graphics.drawable.Drawable)(_gradiant.getObject()));
 //BA.debugLineNum = 332;BA.debugLine="log_in_button.Background = log_grad";
mostCurrent._log_in_button.setBackground((android.graphics.drawable.Drawable)(_log_grad.getObject()));
 //BA.debugLineNum = 333;BA.debugLine="log_in_button.Text = \"LOG IN\"";
mostCurrent._log_in_button.setText((Object)("LOG IN"));
 //BA.debugLineNum = 334;BA.debugLine="new_acc_button.Text = \"CREATE ACCOUNT\"";
mostCurrent._new_acc_button.setText((Object)("CREATE ACCOUNT"));
 //BA.debugLineNum = 335;BA.debugLine="End Sub";
return "";
}
public static String  _database_init() throws Exception{
 //BA.debugLineNum = 91;BA.debugLine="Sub database_init";
 //BA.debugLineNum = 92;BA.debugLine="If File.Exists(File.DirInternal,\"mydb.db\") = Fal";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 93;BA.debugLine="File.Copy(File.DirAssets,\"mydb.db\",File.DirIntern";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mydb.db",anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db");
 }else {
 };
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _for_btn_animation() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper[] _animations = null;
int _i = 0;
 //BA.debugLineNum = 75;BA.debugLine="Sub for_btn_animation";
 //BA.debugLineNum = 77;BA.debugLine="a1.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 78;BA.debugLine="a2.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a2.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 79;BA.debugLine="a3.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a3.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 80;BA.debugLine="new_acc_button.Tag = a2";
mostCurrent._new_acc_button.setTag((Object)(mostCurrent._a2.getObject()));
 //BA.debugLineNum = 81;BA.debugLine="log_in_button.Tag = a1";
mostCurrent._log_in_button.setTag((Object)(mostCurrent._a1.getObject()));
 //BA.debugLineNum = 82;BA.debugLine="label_forgot.Tag = a3";
mostCurrent._label_forgot.setTag((Object)(mostCurrent._a3.getObject()));
 //BA.debugLineNum = 83;BA.debugLine="Dim animations() As Animation";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[(int) (0)];
{
int d0 = _animations.length;
for (int i0 = 0;i0 < d0;i0++) {
_animations[i0] = new anywheresoftware.b4a.objects.AnimationWrapper();
}
}
;
 //BA.debugLineNum = 84;BA.debugLine="animations = Array As Animation(a1, a2, a3)";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[]{mostCurrent._a1,mostCurrent._a2,mostCurrent._a3};
 //BA.debugLineNum = 85;BA.debugLine="For i = 0 To animations.Length - 1";
{
final int step9 = 1;
final int limit9 = (int) (_animations.length-1);
for (_i = (int) (0) ; (step9 > 0 && _i <= limit9) || (step9 < 0 && _i >= limit9); _i = ((int)(0 + _i + step9)) ) {
 //BA.debugLineNum = 86;BA.debugLine="animations(i).Duration = 400";
_animations[_i].setDuration((long) (400));
 //BA.debugLineNum = 87;BA.debugLine="animations(i).RepeatCount = 1";
_animations[_i].setRepeatCount((int) (1));
 //BA.debugLineNum = 88;BA.debugLine="animations(i).RepeatMode = animations(i).REPEAT_";
_animations[_i].setRepeatMode(_animations[_i].REPEAT_REVERSE);
 }
};
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public static String  _forgot_can_btn_click() throws Exception{
 //BA.debugLineNum = 422;BA.debugLine="Sub forgot_can_btn_click";
 //BA.debugLineNum = 423;BA.debugLine="forgot_pass_pnl.RemoveView";
mostCurrent._forgot_pass_pnl.RemoveView();
 //BA.debugLineNum = 424;BA.debugLine="End Sub";
return "";
}
public static String  _forgot_pass_pnl_click() throws Exception{
 //BA.debugLineNum = 425;BA.debugLine="Sub forgot_pass_pnl_click";
 //BA.debugLineNum = 427;BA.debugLine="End Sub";
return "";
}
public static String  _forgot_recover_btn_click() throws Exception{
bloodlife.system.calculations _url_back = null;
String _url_email = "";
String _nickname = "";
 //BA.debugLineNum = 341;BA.debugLine="Sub forgot_recover_btn_click";
 //BA.debugLineNum = 342;BA.debugLine="booleanforgotcount = 0";
_booleanforgotcount = (int) (0);
 //BA.debugLineNum = 343;BA.debugLine="nickName_for = \"\"";
mostCurrent._nickname_for = "";
 //BA.debugLineNum = 344;BA.debugLine="email_for = \"\"";
mostCurrent._email_for = "";
 //BA.debugLineNum = 345;BA.debugLine="If email_forgot.Text == \"\" Or nickN_forgot.Text =";
if ((mostCurrent._email_forgot.getText()).equals("") || (mostCurrent._nickn_forgot.getText()).equals("") || (mostCurrent._newpass.getText()).equals("") || (mostCurrent._renewpass.getText()).equals("")) { 
 //BA.debugLineNum = 346;BA.debugLine="Msgbox(\"Error: Empty fields. Needed to fill all";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Empty fields. Needed to fill all required fields to recover your account!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 348;BA.debugLine="If newPass.Text.Length <= 5 Then";
if (mostCurrent._newpass.getText().length()<=5) { 
 //BA.debugLineNum = 349;BA.debugLine="Msgbox(\"Error: Password lenght must be 6 letter";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Password lenght must be 6 letter and above!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else if(mostCurrent._newpass.getText().contains(mostCurrent._renewpass.getText())==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 351;BA.debugLine="Msgbox(\"Error: Password did not match!\",\"C O N F";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Password did not match!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 353;BA.debugLine="ProgressDialogShow2(\"please wait.!!\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"please wait.!!",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 354;BA.debugLine="log_click = False";
_log_click = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 355;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 356;BA.debugLine="Dim url_email,nickname As String";
_url_email = "";
_nickname = "";
 //BA.debugLineNum = 357;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 358;BA.debugLine="url_email = url_back.php_email_url(\"index.php\")";
_url_email = _url_back._php_email_url("index.php");
 //BA.debugLineNum = 359;BA.debugLine="nickname = url_back.php_email_url(\"search_blood";
_nickname = _url_back._php_email_url("search_blood_nickN.php");
 //BA.debugLineNum = 360;BA.debugLine="forgot_email_job.Download2(url_email,Array As S";
mostCurrent._forgot_email_job._download2(_url_email,new String[]{"email","SELECT email FROM `person_info` where `email`='"+mostCurrent._email_forgot.getText()+"';"});
 //BA.debugLineNum = 361;BA.debugLine="forgot_nick_job.Download2(nickname,Array As Str";
mostCurrent._forgot_nick_job._download2(_nickname,new String[]{"nick","SELECT nick_name FROM `person_info` where `nick_name`='"+mostCurrent._nickn_forgot.getText()+"';"});
 };
 };
 //BA.debugLineNum = 365;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 19;BA.debugLine="Private ban_picture As ImageView";
mostCurrent._ban_picture = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private ban_panel As Panel";
mostCurrent._ban_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private ban_tools As Panel";
mostCurrent._ban_tools = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private label_email As Label";
mostCurrent._label_email = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private label_password As Label";
mostCurrent._label_password = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private text_email As EditText";
mostCurrent._text_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private text_password As EditText";
mostCurrent._text_password = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private label_forgot As Label";
mostCurrent._label_forgot = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private log_in_button As Button";
mostCurrent._log_in_button = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private ban_create As Panel";
mostCurrent._ban_create = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private new_acc_button As Button";
mostCurrent._new_acc_button = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private h_email As HttpJob";
mostCurrent._h_email = new bloodlife.system.httpjob();
 //BA.debugLineNum = 32;BA.debugLine="Private h_pass As HttpJob";
mostCurrent._h_pass = new bloodlife.system.httpjob();
 //BA.debugLineNum = 33;BA.debugLine="Private h_fullname As HttpJob";
mostCurrent._h_fullname = new bloodlife.system.httpjob();
 //BA.debugLineNum = 34;BA.debugLine="Private user_id As HttpJob";
mostCurrent._user_id = new bloodlife.system.httpjob();
 //BA.debugLineNum = 35;BA.debugLine="Private nick_name As HttpJob";
mostCurrent._nick_name = new bloodlife.system.httpjob();
 //BA.debugLineNum = 36;BA.debugLine="Dim Email,pass,name,true_false=\"false\" As String";
mostCurrent._email = "";
mostCurrent._pass = "";
mostCurrent._name = "";
mostCurrent._true_false = "false";
 //BA.debugLineNum = 37;BA.debugLine="Private booleanCount = 0 As Int";
_booleancount = (int) (0);
 //BA.debugLineNum = 38;BA.debugLine="Private booleanforgotcount = 0 As Int";
_booleanforgotcount = (int) (0);
 //BA.debugLineNum = 40;BA.debugLine="Dim calcs As calculations";
mostCurrent._calcs = new bloodlife.system.calculations();
 //BA.debugLineNum = 41;BA.debugLine="Private forgot_pass_pnl As Panel";
mostCurrent._forgot_pass_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Dim email_forgot,nickN_forgot,newPass,renewPass A";
mostCurrent._email_forgot = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._nickn_forgot = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._newpass = new anywheresoftware.b4a.objects.EditTextWrapper();
mostCurrent._renewpass = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Dim forgot_email_job As HttpJob";
mostCurrent._forgot_email_job = new bloodlife.system.httpjob();
 //BA.debugLineNum = 44;BA.debugLine="Dim forgot_nick_job As HttpJob";
mostCurrent._forgot_nick_job = new bloodlife.system.httpjob();
 //BA.debugLineNum = 45;BA.debugLine="Dim forgot_recover_job As HttpJob";
mostCurrent._forgot_recover_job = new bloodlife.system.httpjob();
 //BA.debugLineNum = 46;BA.debugLine="Dim email_for,nickName_for As String";
mostCurrent._email_for = "";
mostCurrent._nickname_for = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim a1, a2, a3 As Animation";
mostCurrent._a1 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a2 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a3 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 49;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(bloodlife.system.httpjob _job) throws Exception{
int _dialogr = 0;
bloodlife.system.calculations _url_back = null;
String _forgot_string = "";
 //BA.debugLineNum = 120;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 121;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 122;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"email_get","pass_get","full_name_get","user_id_get","nick_name_get","forgot_email_get","forgot_nick_get")) {
case 0: {
 //BA.debugLineNum = 124;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 125;BA.debugLine="Email = job.GetString.Trim";
mostCurrent._email = _job._getstring().trim();
 break; }
case 1: {
 //BA.debugLineNum = 127;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 128;BA.debugLine="pass = job.GetString.Trim";
mostCurrent._pass = _job._getstring().trim();
 break; }
case 2: {
 //BA.debugLineNum = 130;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 131;BA.debugLine="name_query = job.GetString.Trim";
_name_query = _job._getstring().trim();
 //BA.debugLineNum = 132;BA.debugLine="name = job.GetString.Trim";
mostCurrent._name = _job._getstring().trim();
 break; }
case 3: {
 //BA.debugLineNum = 134;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 135;BA.debugLine="id_query = job.GetString.Trim";
_id_query = _job._getstring().trim();
 break; }
case 4: {
 //BA.debugLineNum = 138;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 140;BA.debugLine="calcs.name = job.GetString.Trim";
mostCurrent._calcs._name = _job._getstring().trim();
 break; }
case 5: {
 //BA.debugLineNum = 142;BA.debugLine="email_for = job.GetString.Trim";
mostCurrent._email_for = _job._getstring().trim();
 break; }
case 6: {
 //BA.debugLineNum = 144;BA.debugLine="nickName_for = job.GetString.Trim";
mostCurrent._nickname_for = _job._getstring().trim();
 break; }
}
;
 //BA.debugLineNum = 147;BA.debugLine="booleanforgotcount = booleanforgotcount + 1";
_booleanforgotcount = (int) (_booleanforgotcount+1);
 //BA.debugLineNum = 148;BA.debugLine="booleanCount = booleanCount+1";
_booleancount = (int) (_booleancount+1);
 //BA.debugLineNum = 150;BA.debugLine="If log_click == True Then";
if (_log_click==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 151;BA.debugLine="If booleanCount = 4 Then '''''''' 1st statement";
if (_booleancount==4) { 
 //BA.debugLineNum = 152;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 153;BA.debugLine="If text_email.Text == \"\" Or text_password.Text";
if ((mostCurrent._text_email.getText()).equals("") || (mostCurrent._text_password.getText()).equals("")) { 
 //BA.debugLineNum = 154;BA.debugLine="Msgbox(\"Error empty field.!\",\"C O N F I R M";
anywheresoftware.b4a.keywords.Common.Msgbox("Error empty field.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 156;BA.debugLine="If text_email.Text.CompareTo(Email) == 0 And t";
if (mostCurrent._text_email.getText().compareTo(mostCurrent._email)==0 && mostCurrent._text_password.getText().compareTo(mostCurrent._pass)==0) { 
 //BA.debugLineNum = 160;BA.debugLine="Dim dialogR As Int";
_dialogr = 0;
 //BA.debugLineNum = 161;BA.debugLine="dialogR = Msgbox2(\"Welcome \"&name,\"C O N F I";
_dialogr = anywheresoftware.b4a.keywords.Common.Msgbox2("Welcome "+mostCurrent._name,"C O N F I R M A T I O N","OK","","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 162;BA.debugLine="booleanCount = 0";
_booleancount = (int) (0);
 //BA.debugLineNum = 163;BA.debugLine="If  dialogR == DialogResponse.POSITIVE Then";
if (_dialogr==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 164;BA.debugLine="text_password.Text = \"\"";
mostCurrent._text_password.setText((Object)(""));
 //BA.debugLineNum = 165;BA.debugLine="text_email.text = \"\"";
mostCurrent._text_email.setText((Object)(""));
 //BA.debugLineNum = 166;BA.debugLine="is_log_in = True";
_is_log_in = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 167;BA.debugLine="StartActivity(\"menu_form\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("menu_form"));
 }else {
 };
 }else {
 //BA.debugLineNum = 171;BA.debugLine="Msgbox(\"Error email address or password.!\",\"";
anywheresoftware.b4a.keywords.Common.Msgbox("Error email address or password.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 //BA.debugLineNum = 172;BA.debugLine="text_password.Text = \"\"";
mostCurrent._text_password.setText((Object)(""));
 //BA.debugLineNum = 173;BA.debugLine="booleanCount = 0";
_booleancount = (int) (0);
 };
 };
 }else {
 };
 };
 //BA.debugLineNum = 182;BA.debugLine="If log_click == False Then";
if (_log_click==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 183;BA.debugLine="If booleanforgotcount == 1 Then";
if (_booleanforgotcount==1) { 
 //BA.debugLineNum = 184;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 185;BA.debugLine="If email_for.CompareTo(email_forgot.Text) == 0";
if (mostCurrent._email_for.compareTo(mostCurrent._email_forgot.getText())==0 && mostCurrent._nickname_for.compareTo(mostCurrent._nickn_forgot.getText())==0) { 
 //BA.debugLineNum = 187;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 188;BA.debugLine="Dim forgot_string As String";
_forgot_string = "";
 //BA.debugLineNum = 189;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 190;BA.debugLine="forgot_string = url_back.php_email_url(\"forgot";
_forgot_string = _url_back._php_email_url("forgot_pass.php");
 //BA.debugLineNum = 191;BA.debugLine="forgot_recover_job.Download2(forgot_string,Arr";
mostCurrent._forgot_recover_job._download2(_forgot_string,new String[]{"recover","UPDATE `person_info` SET `password`=ENCODE('"+mostCurrent._newpass.getText()+"','goroy') WHERE  `email`='"+mostCurrent._email_for+"' and `nick_name`='"+mostCurrent._nickname_for+"';"});
 //BA.debugLineNum = 192;BA.debugLine="ToastMessageShow(\"Successfully Recovered Accou";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Successfully Recovered Account!",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 193;BA.debugLine="forgot_pass_pnl.RemoveView";
mostCurrent._forgot_pass_pnl.RemoveView();
 }else {
 //BA.debugLineNum = 195;BA.debugLine="Msgbox(\"Error: Information not existed!\",\"C O N";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Information not existed!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 };
 };
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 201;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 202;BA.debugLine="If booleanCount = 4 And booleanforgotcount = 1 T";
if (_booleancount==4 && _booleanforgotcount==1) { 
 //BA.debugLineNum = 203;BA.debugLine="Msgbox(\"Error: Error connecting to server, try a";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Error connecting to server, try again laiter.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 //BA.debugLineNum = 204;BA.debugLine="booleanCount = 0";
_booleancount = (int) (0);
 //BA.debugLineNum = 205;BA.debugLine="booleanforgotcount = 0";
_booleanforgotcount = (int) (0);
 }else {
 //BA.debugLineNum = 207;BA.debugLine="booleanCount = 4";
_booleancount = (int) (4);
 //BA.debugLineNum = 208;BA.debugLine="booleanforgotcount = 1";
_booleanforgotcount = (int) (1);
 };
 };
 //BA.debugLineNum = 218;BA.debugLine="End Sub";
return "";
}
public static String  _label_forgot_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_ok_btn = null;
anywheresoftware.b4a.objects.ButtonWrapper _edit_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _title_lbl = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _v_btn = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _c_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 366;BA.debugLine="Sub label_forgot_Click";
 //BA.debugLineNum = 368;BA.debugLine="a3.Start(label_forgot)";
mostCurrent._a3.Start((android.view.View)(mostCurrent._label_forgot.getObject()));
 //BA.debugLineNum = 369;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 370;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 371;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 372;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 373;BA.debugLine="edit_ok_btn.Initialize(\"forgot_recover_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"forgot_recover_btn");
 //BA.debugLineNum = 374;BA.debugLine="edit_can_btn.Initialize(\"forgot_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"forgot_can_btn");
 //BA.debugLineNum = 375;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 376;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 377;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 378;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 379;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 380;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 381;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 382;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 383;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 384;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 385;BA.debugLine="edit_ok_btn.Text = \"RECOVER\"";
_edit_ok_btn.setText((Object)("RECOVER"));
 //BA.debugLineNum = 386;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 387;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(\"H";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 388;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(\"";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 389;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 390;BA.debugLine="forgot_pass_pnl.Initialize(\"forgot_pass_pnl\")";
mostCurrent._forgot_pass_pnl.Initialize(mostCurrent.activityBA,"forgot_pass_pnl");
 //BA.debugLineNum = 391;BA.debugLine="forgot_pass_pnl.Color = Colors.Transparent";
mostCurrent._forgot_pass_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 392;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 393;BA.debugLine="title_lbl.Text = \"RECOVERING ACCOUNT\"";
_title_lbl.setText((Object)("RECOVERING ACCOUNT"));
 //BA.debugLineNum = 394;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hip";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 395;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 397;BA.debugLine="email_forgot.Initialize(\"\")";
mostCurrent._email_forgot.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 398;BA.debugLine="nickN_forgot.Initialize(\"\")";
mostCurrent._nickn_forgot.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 399;BA.debugLine="newPass.Initialize(\"\")";
mostCurrent._newpass.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 400;BA.debugLine="renewPass.Initialize(\"\")";
mostCurrent._renewpass.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 401;BA.debugLine="email_forgot.Hint = \"Enter your Email Address\"";
mostCurrent._email_forgot.setHint("Enter your Email Address");
 //BA.debugLineNum = 402;BA.debugLine="nickN_forgot.Hint = \"Enter your Nick Name\"";
mostCurrent._nickn_forgot.setHint("Enter your Nick Name");
 //BA.debugLineNum = 403;BA.debugLine="newPass.Hint = \"Enter your New Password\"";
mostCurrent._newpass.setHint("Enter your New Password");
 //BA.debugLineNum = 404;BA.debugLine="renewPass.Hint = \"Retype New Password\"";
mostCurrent._renewpass.setHint("Retype New Password");
 //BA.debugLineNum = 405;BA.debugLine="newPass.PasswordMode = True";
mostCurrent._newpass.setPasswordMode(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 406;BA.debugLine="renewPass.PasswordMode = True";
mostCurrent._renewpass.setPasswordMode(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 407;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 408;BA.debugLine="pnl.AddView(email_forgot,2%x,title_lbl.Top + titl";
_pnl.AddView((android.view.View)(mostCurrent._email_forgot.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 409;BA.debugLine="pnl.AddView(nickN_forgot,2%x,email_forgot.Top + e";
_pnl.AddView((android.view.View)(mostCurrent._nickn_forgot.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._email_forgot.getTop()+mostCurrent._email_forgot.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 410;BA.debugLine="pnl.AddView(newPass,2%x,nickN_forgot.Top + nickN_";
_pnl.AddView((android.view.View)(mostCurrent._newpass.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._nickn_forgot.getTop()+mostCurrent._nickn_forgot.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 411;BA.debugLine="pnl.AddView(renewPass,2%x,newPass.Top + newPass.H";
_pnl.AddView((android.view.View)(mostCurrent._renewpass.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (mostCurrent._newpass.getTop()+mostCurrent._newpass.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 413;BA.debugLine="pnl.AddView(edit_ok_btn,8%x,renewPass.Top+renewPa";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (8),mostCurrent.activityBA),(int) (mostCurrent._renewpass.getTop()+mostCurrent._renewpass.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (28),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 414;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._renewpass.getTop()+mostCurrent._renewpass.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (28),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 416;BA.debugLine="forgot_pass_pnl.AddView(pnl,13%x,(((Activity.Heig";
mostCurrent._forgot_pass_pnl.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) ((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (58),mostCurrent.activityBA));
 //BA.debugLineNum = 417;BA.debugLine="forgot_pass_pnl.BringToFront";
mostCurrent._forgot_pass_pnl.BringToFront();
 //BA.debugLineNum = 420;BA.debugLine="Activity.AddView(forgot_pass_pnl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._forgot_pass_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 421;BA.debugLine="End Sub";
return "";
}
public static String  _log_in_button_click() throws Exception{
bloodlife.system.calculations _url_back = null;
String _url_email = "";
String _url_pass = "";
String _full_name = "";
String _id = "";
String _nickname = "";
 //BA.debugLineNum = 219;BA.debugLine="Sub log_in_button_click";
 //BA.debugLineNum = 220;BA.debugLine="a1.Start(log_in_button)";
mostCurrent._a1.Start((android.view.View)(mostCurrent._log_in_button.getObject()));
 //BA.debugLineNum = 221;BA.debugLine="ProgressDialogShow2(\"please wait.!!\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"please wait.!!",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 222;BA.debugLine="Email = \"\"";
mostCurrent._email = "";
 //BA.debugLineNum = 223;BA.debugLine="pass = \"\"";
mostCurrent._pass = "";
 //BA.debugLineNum = 224;BA.debugLine="log_click = True";
_log_click = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 225;BA.debugLine="booleanCount = 0";
_booleancount = (int) (0);
 //BA.debugLineNum = 226;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 227;BA.debugLine="Dim url_email,url_pass,full_name,id,nickname As S";
_url_email = "";
_url_pass = "";
_full_name = "";
_id = "";
_nickname = "";
 //BA.debugLineNum = 228;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 229;BA.debugLine="url_email = url_back.php_email_url(\"index.php\")";
_url_email = _url_back._php_email_url("index.php");
 //BA.debugLineNum = 230;BA.debugLine="url_pass = url_back.php_email_url(\"index1.php\")";
_url_pass = _url_back._php_email_url("index1.php");
 //BA.debugLineNum = 231;BA.debugLine="full_name = url_back.php_email_url(\"search_blood_";
_full_name = _url_back._php_email_url("search_blood_fullN.php");
 //BA.debugLineNum = 232;BA.debugLine="id = url_back.php_email_url(\"search_blood_id.php\"";
_id = _url_back._php_email_url("search_blood_id.php");
 //BA.debugLineNum = 233;BA.debugLine="nickname = url_back.php_email_url(\"search_blood_n";
_nickname = _url_back._php_email_url("search_blood_nickN.php");
 //BA.debugLineNum = 235;BA.debugLine="h_fullname.Download2(full_name,Array As String(\"f";
mostCurrent._h_fullname._download2(_full_name,new String[]{"full_name","SELECT full_name FROM `person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 236;BA.debugLine="user_id.Download2(id,Array As String(\"id\",\"SELECT";
mostCurrent._user_id._download2(_id,new String[]{"id","SELECT id FROM `person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 237;BA.debugLine="nick_name.Download2(nickname,Array As String(\"nic";
mostCurrent._nick_name._download2(_nickname,new String[]{"nick","SELECT nick_name FROM `person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 239;BA.debugLine="h_email.Download2(url_email,Array As String(\"emai";
mostCurrent._h_email._download2(_url_email,new String[]{"email","SELECT email FROM `person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 240;BA.debugLine="h_pass.Download2(url_pass,Array As String(\"pass\",";
mostCurrent._h_pass._download2(_url_pass,new String[]{"pass","SELECT decode(password,'goroy') FROM `person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 242;BA.debugLine="End Sub";
return "";
}
public static String  _new_acc_button_click() throws Exception{
 //BA.debugLineNum = 337;BA.debugLine="Sub new_acc_button_Click";
 //BA.debugLineNum = 338;BA.debugLine="a2.Start(new_acc_button)";
mostCurrent._a2.Start((android.view.View)(mostCurrent._new_acc_button.getObject()));
 //BA.debugLineNum = 339;BA.debugLine="StartActivity (\"create_account\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("create_account"));
 //BA.debugLineNum = 340;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Public id_query As String : id_query = \"1\"";
_id_query = "";
 //BA.debugLineNum = 9;BA.debugLine="Public id_query As String : id_query = \"1\"";
_id_query = "1";
 //BA.debugLineNum = 10;BA.debugLine="Public name_query As String : name_query = \"Me\"";
_name_query = "";
 //BA.debugLineNum = 10;BA.debugLine="Public name_query As String : name_query = \"Me\"";
_name_query = "Me";
 //BA.debugLineNum = 11;BA.debugLine="Private log_click As Boolean";
_log_click = false;
 //BA.debugLineNum = 12;BA.debugLine="Public is_log_in As Boolean : is_log_in = True";
_is_log_in = false;
 //BA.debugLineNum = 12;BA.debugLine="Public is_log_in As Boolean : is_log_in = True";
_is_log_in = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 13;BA.debugLine="Dim sqlLite As SQL";
_sqllite = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
}
