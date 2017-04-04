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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.login_form");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.login_form");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.login_form", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public b4a.example.httpjob _h_email = null;
public b4a.example.httpjob _h_pass = null;
public b4a.example.httpjob _h_fullname = null;
public b4a.example.httpjob _user_id = null;
public b4a.example.httpjob _nick_name = null;
public static String _email = "";
public static String _pass = "";
public static String _name = "";
public static String _true_false = "";
public static int _booleancount = 0;
public b4a.example.calculations _calcs = null;
public b4a.example.main _main = null;
public b4a.example.create_account _create_account = null;
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
 //BA.debugLineNum = 39;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 41;BA.debugLine="Activity.LoadLayout(\"login_form\")";
mostCurrent._activity.LoadLayout("login_form",mostCurrent.activityBA);
 //BA.debugLineNum = 43;BA.debugLine="calcs.Initialize";
mostCurrent._calcs._initialize(processBA);
 //BA.debugLineNum = 44;BA.debugLine="h_email.Initialize(\"email_get\",Me)";
mostCurrent._h_email._initialize(processBA,"email_get",login_form.getObject());
 //BA.debugLineNum = 45;BA.debugLine="h_pass.Initialize(\"pass_get\",Me)";
mostCurrent._h_pass._initialize(processBA,"pass_get",login_form.getObject());
 //BA.debugLineNum = 46;BA.debugLine="h_fullname.Initialize(\"full_name_get\",Me)";
mostCurrent._h_fullname._initialize(processBA,"full_name_get",login_form.getObject());
 //BA.debugLineNum = 47;BA.debugLine="user_id.Initialize(\"user_id_get\",Me)";
mostCurrent._user_id._initialize(processBA,"user_id_get",login_form.getObject());
 //BA.debugLineNum = 48;BA.debugLine="nick_name.Initialize(\"nick_name_get\",Me)";
mostCurrent._nick_name._initialize(processBA,"nick_name_get",login_form.getObject());
 //BA.debugLineNum = 50;BA.debugLine="all_settings_layout";
_all_settings_layout();
 //BA.debugLineNum = 51;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 137;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 133;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _all_settings_layout() throws Exception{
double _sums = 0;
double _left_sums = 0;
double _left = 0;
 //BA.debugLineNum = 141;BA.debugLine="Public Sub all_settings_layout";
 //BA.debugLineNum = 142;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 143;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 144;BA.debugLine="log_in_button.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._log_in_button.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"LOG_IN.png").getObject()));
 //BA.debugLineNum = 145;BA.debugLine="new_acc_button.SetBackgroundImage(LoadBitmap(File";
mostCurrent._new_acc_button.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"CREATE_ACOUNT.png").getObject()));
 //BA.debugLineNum = 147;BA.debugLine="ban_tools.Color = Colors.Transparent";
mostCurrent._ban_tools.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 148;BA.debugLine="ban_create.Color = Colors.Transparent";
mostCurrent._ban_create.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 150;BA.debugLine="ban_panel.Width = 100%x";
mostCurrent._ban_panel.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 151;BA.debugLine="ban_picture.Width = ban_panel.Width";
mostCurrent._ban_picture.setWidth(mostCurrent._ban_panel.getWidth());
 //BA.debugLineNum = 152;BA.debugLine="ban_tools.Width = 100%x";
mostCurrent._ban_tools.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 153;BA.debugLine="ban_create.Width = 100%x";
mostCurrent._ban_create.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 154;BA.debugLine="label_email.Width = 20%x";
mostCurrent._label_email.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 155;BA.debugLine="label_password.Width = 20%x";
mostCurrent._label_password.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 156;BA.debugLine="text_email.Width = 45%x";
mostCurrent._text_email.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (45),mostCurrent.activityBA));
 //BA.debugLineNum = 157;BA.debugLine="text_password.Width = 45%x";
mostCurrent._text_password.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (45),mostCurrent.activityBA));
 //BA.debugLineNum = 158;BA.debugLine="label_forgot.Width = 30%x";
mostCurrent._label_forgot.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 159;BA.debugLine="log_in_button.Width = 30%x";
mostCurrent._log_in_button.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 160;BA.debugLine="new_acc_button.Width = 35%x";
mostCurrent._new_acc_button.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (35),mostCurrent.activityBA));
 //BA.debugLineNum = 162;BA.debugLine="ban_panel.Height = 25%y";
mostCurrent._ban_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 163;BA.debugLine="ban_picture.Height = ban_panel.Height - 3dip";
mostCurrent._ban_picture.setHeight((int) (mostCurrent._ban_panel.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 164;BA.debugLine="ban_create.Height = 16%y";
mostCurrent._ban_create.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (16),mostCurrent.activityBA));
 //BA.debugLineNum = 165;BA.debugLine="ban_tools.Height = Activity.Height - ban_panel.H";
mostCurrent._ban_tools.setHeight((int) (mostCurrent._activity.getHeight()-mostCurrent._ban_panel.getHeight()-mostCurrent._ban_create.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 166;BA.debugLine="label_email.Height = 6%y";
mostCurrent._label_email.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 167;BA.debugLine="label_password.Height = 6%y";
mostCurrent._label_password.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 168;BA.debugLine="text_email.Height = 6%y";
mostCurrent._text_email.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 169;BA.debugLine="text_password.Height = 6%y";
mostCurrent._text_password.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 170;BA.debugLine="label_forgot.Height = 6%y";
mostCurrent._label_forgot.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 171;BA.debugLine="log_in_button.Height = 10%y";
mostCurrent._log_in_button.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 172;BA.debugLine="new_acc_button.Height = 10%y";
mostCurrent._new_acc_button.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 174;BA.debugLine="ban_panel.Top = Activity.Top";
mostCurrent._ban_panel.setTop(mostCurrent._activity.getTop());
 //BA.debugLineNum = 175;BA.debugLine="ban_picture.Top = ban_panel.Top + 3dip";
mostCurrent._ban_picture.setTop((int) (mostCurrent._ban_panel.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 176;BA.debugLine="ban_tools.Top = ban_panel.Height + 2dip";
mostCurrent._ban_tools.setTop((int) (mostCurrent._ban_panel.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 177;BA.debugLine="ban_create.Top = ban_panel.Height + ban_tools.He";
mostCurrent._ban_create.setTop((int) (mostCurrent._ban_panel.getHeight()+mostCurrent._ban_tools.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 178;BA.debugLine="label_email.Top = 10%y";
mostCurrent._label_email.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 179;BA.debugLine="label_password.Top = 18%y";
mostCurrent._label_password.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 180;BA.debugLine="text_email.Top = 10%y";
mostCurrent._text_email.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 181;BA.debugLine="text_password.Top = 18%y";
mostCurrent._text_password.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 182;BA.debugLine="label_forgot.Top = text_password.Top + text_pass";
mostCurrent._label_forgot.setTop((int) (mostCurrent._text_password.getTop()+mostCurrent._text_password.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 183;BA.debugLine="log_in_button.Top = label_forgot.Top + label_for";
mostCurrent._log_in_button.setTop((int) (mostCurrent._label_forgot.getTop()+mostCurrent._label_forgot.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (17))));
 //BA.debugLineNum = 184;BA.debugLine="Dim sums As Double";
_sums = 0;
 //BA.debugLineNum = 185;BA.debugLine="sums= ban_create.Height/2";
_sums = mostCurrent._ban_create.getHeight()/(double)2;
 //BA.debugLineNum = 186;BA.debugLine="new_acc_button.Top = sums - (sums/2)";
mostCurrent._new_acc_button.setTop((int) (_sums-(_sums/(double)2)));
 //BA.debugLineNum = 188;BA.debugLine="Dim left_sums,left As Double";
_left_sums = 0;
_left = 0;
 //BA.debugLineNum = 189;BA.debugLine="left_sums = (Activity.Width/2)";
_left_sums = (mostCurrent._activity.getWidth()/(double)2);
 //BA.debugLineNum = 190;BA.debugLine="left =  left_sums - (left_sums/2)";
_left = _left_sums-(_left_sums/(double)2);
 //BA.debugLineNum = 191;BA.debugLine="ban_panel.left = 0";
mostCurrent._ban_panel.setLeft((int) (0));
 //BA.debugLineNum = 192;BA.debugLine="ban_picture.left = ban_panel.left";
mostCurrent._ban_picture.setLeft(mostCurrent._ban_panel.getLeft());
 //BA.debugLineNum = 193;BA.debugLine="ban_create.left = 0";
mostCurrent._ban_create.setLeft((int) (0));
 //BA.debugLineNum = 194;BA.debugLine="ban_tools.left = 0";
mostCurrent._ban_tools.setLeft((int) (0));
 //BA.debugLineNum = 195;BA.debugLine="label_email.left = left/2";
mostCurrent._label_email.setLeft((int) (_left/(double)2));
 //BA.debugLineNum = 196;BA.debugLine="label_password.left = left/2";
mostCurrent._label_password.setLeft((int) (_left/(double)2));
 //BA.debugLineNum = 197;BA.debugLine="text_email.left =  label_email.left + label_emai";
mostCurrent._text_email.setLeft((int) (mostCurrent._label_email.getLeft()+mostCurrent._label_email.getWidth()));
 //BA.debugLineNum = 198;BA.debugLine="text_password.left = label_password.left + label";
mostCurrent._text_password.setLeft((int) (mostCurrent._label_password.getLeft()+mostCurrent._label_email.getWidth()));
 //BA.debugLineNum = 199;BA.debugLine="label_forgot.left = text_password.left + (text_p";
mostCurrent._label_forgot.setLeft((int) (mostCurrent._text_password.getLeft()+(mostCurrent._text_password.getWidth()/(double)2)));
 //BA.debugLineNum = 200;BA.debugLine="log_in_button.left = text_password.left + 10dip";
mostCurrent._log_in_button.setLeft((int) (mostCurrent._text_password.getLeft()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 201;BA.debugLine="new_acc_button.left = 5%x";
mostCurrent._new_acc_button.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 202;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 16;BA.debugLine="Private ban_picture As ImageView";
mostCurrent._ban_picture = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private ban_panel As Panel";
mostCurrent._ban_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private ban_tools As Panel";
mostCurrent._ban_tools = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private label_email As Label";
mostCurrent._label_email = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private label_password As Label";
mostCurrent._label_password = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private text_email As EditText";
mostCurrent._text_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private text_password As EditText";
mostCurrent._text_password = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private label_forgot As Label";
mostCurrent._label_forgot = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private log_in_button As Button";
mostCurrent._log_in_button = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private ban_create As Panel";
mostCurrent._ban_create = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private new_acc_button As Button";
mostCurrent._new_acc_button = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private h_email As HttpJob";
mostCurrent._h_email = new b4a.example.httpjob();
 //BA.debugLineNum = 29;BA.debugLine="Private h_pass As HttpJob";
mostCurrent._h_pass = new b4a.example.httpjob();
 //BA.debugLineNum = 30;BA.debugLine="Private h_fullname As HttpJob";
mostCurrent._h_fullname = new b4a.example.httpjob();
 //BA.debugLineNum = 31;BA.debugLine="Private user_id As HttpJob";
mostCurrent._user_id = new b4a.example.httpjob();
 //BA.debugLineNum = 32;BA.debugLine="Private nick_name As HttpJob";
mostCurrent._nick_name = new b4a.example.httpjob();
 //BA.debugLineNum = 33;BA.debugLine="Dim Email,pass,name,true_false=\"false\" As String";
mostCurrent._email = "";
mostCurrent._pass = "";
mostCurrent._name = "";
mostCurrent._true_false = "false";
 //BA.debugLineNum = 34;BA.debugLine="Private booleanCount = 0 As Int";
_booleancount = (int) (0);
 //BA.debugLineNum = 36;BA.debugLine="Dim calcs As calculations";
mostCurrent._calcs = new b4a.example.calculations();
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(b4a.example.httpjob _job) throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 53;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 54;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"email_get","pass_get","full_name_get","user_id_get","nick_name_get")) {
case 0: {
 //BA.debugLineNum = 56;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 57;BA.debugLine="Email = job.GetString.Trim";
mostCurrent._email = _job._getstring().trim();
 break; }
case 1: {
 //BA.debugLineNum = 59;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 60;BA.debugLine="pass = job.GetString.Trim";
mostCurrent._pass = _job._getstring().trim();
 break; }
case 2: {
 //BA.debugLineNum = 62;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 63;BA.debugLine="name = job.GetString.Trim";
mostCurrent._name = _job._getstring().trim();
 break; }
case 3: {
 //BA.debugLineNum = 65;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 66;BA.debugLine="id_query = job.GetString.Trim";
_id_query = _job._getstring().trim();
 break; }
case 4: {
 //BA.debugLineNum = 69;BA.debugLine="Log(job.GetString)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring());
 //BA.debugLineNum = 70;BA.debugLine="name_query = job.GetString.Trim";
_name_query = _job._getstring().trim();
 break; }
}
;
 //BA.debugLineNum = 74;BA.debugLine="booleanCount = booleanCount+1";
_booleancount = (int) (_booleancount+1);
 //BA.debugLineNum = 75;BA.debugLine="If booleanCount = 4 Then '''''''' 1st statement";
if (_booleancount==4) { 
 //BA.debugLineNum = 76;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 77;BA.debugLine="If text_email.Text == Null Or text_password.Tex";
if (mostCurrent._text_email.getText()== null || mostCurrent._text_password.getText()== null) { 
 //BA.debugLineNum = 78;BA.debugLine="Msgbox(\"Error email address or password.!\",";
anywheresoftware.b4a.keywords.Common.Msgbox("Error email address or password.!","Confirmation",mostCurrent.activityBA);
 }else if((mostCurrent._text_email.getText()).equals("") && (mostCurrent._text_password.getText()).equals("")) { 
 //BA.debugLineNum = 80;BA.debugLine="Msgbox(\"Error empty field.!\",\"Confirmation\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Error empty field.!","Confirmation",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 82;BA.debugLine="If text_email.Text == Email And text_password.";
if ((mostCurrent._text_email.getText()).equals(mostCurrent._email) && (mostCurrent._text_password.getText()).equals(mostCurrent._pass)) { 
 //BA.debugLineNum = 86;BA.debugLine="Msgbox(\"Welcome \"&name,\"Confirmation\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Welcome "+mostCurrent._name,"Confirmation",mostCurrent.activityBA);
 //BA.debugLineNum = 87;BA.debugLine="booleanCount = 0";
_booleancount = (int) (0);
 //BA.debugLineNum = 88;BA.debugLine="StartActivity(\"menu_form\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("menu_form"));
 }else {
 //BA.debugLineNum = 90;BA.debugLine="Msgbox(\"Error email address or password.!\",\"";
anywheresoftware.b4a.keywords.Common.Msgbox("Error email address or password.!","Confirmation",mostCurrent.activityBA);
 //BA.debugLineNum = 91;BA.debugLine="booleanCount = 0";
_booleancount = (int) (0);
 };
 };
 }else {
 };
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 99;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 100;BA.debugLine="If booleanCount = 4 Then";
if (_booleancount==4) { 
 //BA.debugLineNum = 101;BA.debugLine="Msgbox(\"Error: Error connecting to server, try a";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Error connecting to server, try again laiter.!","Confirmation",mostCurrent.activityBA);
 //BA.debugLineNum = 102;BA.debugLine="booleanCount = 0";
_booleancount = (int) (0);
 }else {
 //BA.debugLineNum = 104;BA.debugLine="booleanCount = 4";
_booleancount = (int) (4);
 };
 };
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return "";
}
public static String  _log_in_button_click() throws Exception{
b4a.example.calculations _url_back = null;
String _url_email = "";
String _url_pass = "";
String _full_name = "";
String _id = "";
String _nickname = "";
 //BA.debugLineNum = 113;BA.debugLine="Sub log_in_button_click";
 //BA.debugLineNum = 114;BA.debugLine="ProgressDialogShow2(\"please wait.!!\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"please wait.!!",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 115;BA.debugLine="booleanCount = 0";
_booleancount = (int) (0);
 //BA.debugLineNum = 116;BA.debugLine="Dim url_back As calculations";
_url_back = new b4a.example.calculations();
 //BA.debugLineNum = 117;BA.debugLine="Dim url_email,url_pass,full_name,id,nickname As S";
_url_email = "";
_url_pass = "";
_full_name = "";
_id = "";
_nickname = "";
 //BA.debugLineNum = 118;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 119;BA.debugLine="url_email = url_back.php_email_url(\"/bloodlifePHP";
_url_email = _url_back._php_email_url("/bloodlifePHP/index.php");
 //BA.debugLineNum = 120;BA.debugLine="url_pass = url_back.php_email_url(\"/bloodlifePHP/";
_url_pass = _url_back._php_email_url("/bloodlifePHP/index1.php");
 //BA.debugLineNum = 121;BA.debugLine="full_name = url_back.php_email_url(\"/bloodlifePHP";
_full_name = _url_back._php_email_url("/bloodlifePHP/search_blood_fullN.php");
 //BA.debugLineNum = 122;BA.debugLine="id = url_back.php_email_url(\"/bloodlifePHP/search";
_id = _url_back._php_email_url("/bloodlifePHP/search_blood_id.php");
 //BA.debugLineNum = 123;BA.debugLine="nickname = url_back.php_email_url(\"/bloodlifePHP/";
_nickname = _url_back._php_email_url("/bloodlifePHP/search_blood_nickN.php");
 //BA.debugLineNum = 125;BA.debugLine="h_fullname.Download2(full_name,Array As String(\"f";
mostCurrent._h_fullname._download2(_full_name,new String[]{"full_name","SELECT full_name FROM `bloodlife_db`.`person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 126;BA.debugLine="user_id.Download2(id,Array As String(\"id\",\"SELECT";
mostCurrent._user_id._download2(_id,new String[]{"id","SELECT id FROM `bloodlife_db`.`person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 127;BA.debugLine="nick_name.Download2(nickname,Array As String(\"nic";
mostCurrent._nick_name._download2(_nickname,new String[]{"nick","SELECT nick_name FROM `bloodlife_db`.`person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 129;BA.debugLine="h_email.Download2(url_email,Array As String(\"emai";
mostCurrent._h_email._download2(_url_email,new String[]{"email","SELECT email FROM `bloodlife_db`.`person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 130;BA.debugLine="h_pass.Download2(url_pass,Array As String(\"pass\",";
mostCurrent._h_pass._download2(_url_pass,new String[]{"pass","SELECT decode(password,'goroy') FROM `bloodlife_db`.`person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
public static String  _new_acc_button_click() throws Exception{
 //BA.debugLineNum = 204;BA.debugLine="Sub new_acc_button_Click";
 //BA.debugLineNum = 205;BA.debugLine="StartActivity (\"create_account\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("create_account"));
 //BA.debugLineNum = 206;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Public id_query As String : id_query = \"\"";
_id_query = "";
 //BA.debugLineNum = 9;BA.debugLine="Public id_query As String : id_query = \"\"";
_id_query = "";
 //BA.debugLineNum = 10;BA.debugLine="Public name_query As String : name_query = \"Me\"";
_name_query = "";
 //BA.debugLineNum = 10;BA.debugLine="Public name_query As String : name_query = \"Me\"";
_name_query = "Me";
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
}
