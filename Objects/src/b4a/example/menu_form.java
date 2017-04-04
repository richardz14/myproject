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
 //BA.debugLineNum = 187;BA.debugLine="Sub about_Click";
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 42;BA.debugLine="Activity.LoadLayout (\"menu_frame\")";
mostCurrent._activity.LoadLayout("menu_frame",mostCurrent.activityBA);
 //BA.debugLineNum = 43;BA.debugLine="load_activity_layout";
_load_activity_layout();
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 178;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 180;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 174;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 176;BA.debugLine="End Sub";
return "";
}
public static String  _exit_btn_click() throws Exception{
 //BA.debugLineNum = 193;BA.debugLine="Sub exit_btn_Click";
 //BA.debugLineNum = 195;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 16;BA.debugLine="Private search_blood As Button";
mostCurrent._search_blood = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private about As Button";
mostCurrent._about = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private help As Button";
mostCurrent._help = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private exit_btn As Button";
mostCurrent._exit_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private profile As Button";
mostCurrent._profile = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private src_blood_pnl As Panel";
mostCurrent._src_blood_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private users_panel As Panel";
mostCurrent._users_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private profile_pnl As Panel";
mostCurrent._profile_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private about_pnl As Panel";
mostCurrent._about_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private help_pnl As Panel";
mostCurrent._help_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private exit_pnl As Panel";
mostCurrent._exit_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private users_out_lbl As Label";
mostCurrent._users_out_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private users_lbl As Label";
mostCurrent._users_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private ban_logo As ImageView";
mostCurrent._ban_logo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private ban_picture As ImageView";
mostCurrent._ban_picture = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private users_heading As Panel";
mostCurrent._users_heading = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private srch_blood_img As ImageView";
mostCurrent._srch_blood_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private profile_img As ImageView";
mostCurrent._profile_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private about_img As ImageView";
mostCurrent._about_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private help_img As ImageView";
mostCurrent._help_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private exit_img As ImageView";
mostCurrent._exit_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _help_click() throws Exception{
 //BA.debugLineNum = 190;BA.debugLine="Sub help_Click";
 //BA.debugLineNum = 192;BA.debugLine="End Sub";
return "";
}
public static String  _load_activity_layout() throws Exception{
b4a.example.calculations _text_temp = null;
 //BA.debugLineNum = 45;BA.debugLine="Sub load_activity_layout";
 //BA.debugLineNum = 46;BA.debugLine="Dim text_temp As calculations";
_text_temp = new b4a.example.calculations();
 //BA.debugLineNum = 48;BA.debugLine="text_temp.Initialize";
_text_temp._initialize(processBA);
 //BA.debugLineNum = 51;BA.debugLine="users_out_lbl.text = login_form.name_query";
mostCurrent._users_out_lbl.setText((Object)(mostCurrent._login_form._name_query));
 //BA.debugLineNum = 52;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 53;BA.debugLine="ban_logo.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._ban_logo.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logo1.jpg").getObject()));
 //BA.debugLineNum = 54;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 55;BA.debugLine="users_panel.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._users_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 56;BA.debugLine="src_blood_pnl.SetBackgroundImage(LoadBitmap(File.";
mostCurrent._src_blood_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 57;BA.debugLine="profile_pnl.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._profile_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 58;BA.debugLine="about_pnl.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._about_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 59;BA.debugLine="help_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._help_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 60;BA.debugLine="exit_pnl.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._exit_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 62;BA.debugLine="srch_blood_img.SetBackgroundImage(LoadBitmap(Fi";
mostCurrent._srch_blood_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"menu_search.png").getObject()));
 //BA.debugLineNum = 63;BA.debugLine="profile_img.SetBackgroundImage(LoadBitmap(File";
mostCurrent._profile_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"emyprofile.png").getObject()));
 //BA.debugLineNum = 64;BA.debugLine="about_img.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._about_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eaboutus.png").getObject()));
 //BA.debugLineNum = 65;BA.debugLine="help_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._help_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ehelp.png").getObject()));
 //BA.debugLineNum = 66;BA.debugLine="exit_img.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._exit_img.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"eexit.png").getObject()));
 //BA.debugLineNum = 68;BA.debugLine="users_heading.Color = Colors.Transparent";
mostCurrent._users_heading.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 70;BA.debugLine="ban_picture.Width = 80%x";
mostCurrent._ban_picture.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 71;BA.debugLine="ban_logo.Width = 20%x";
mostCurrent._ban_logo.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 72;BA.debugLine="users_panel.Width = Activity.Width";
mostCurrent._users_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 73;BA.debugLine="src_blood_pnl.Width = Activity.Width";
mostCurrent._src_blood_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 74;BA.debugLine="profile_pnl.Width = Activity.Width";
mostCurrent._profile_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 75;BA.debugLine="about_pnl.Width = Activity.Width";
mostCurrent._about_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 76;BA.debugLine="help_pnl.Width = Activity.Width";
mostCurrent._help_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 77;BA.debugLine="exit_pnl.Width = Activity.Width";
mostCurrent._exit_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 78;BA.debugLine="users_heading.Width = Activity.Width";
mostCurrent._users_heading.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 80;BA.debugLine="users_heading.Height = 9%y";
mostCurrent._users_heading.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 81;BA.debugLine="users_panel.Height = 18%y";
mostCurrent._users_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (18),mostCurrent.activityBA));
 //BA.debugLineNum = 82;BA.debugLine="ban_picture.Height = users_panel.Height";
mostCurrent._ban_picture.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 83;BA.debugLine="ban_logo.Height = users_panel.Height";
mostCurrent._ban_logo.setHeight(mostCurrent._users_panel.getHeight());
 //BA.debugLineNum = 84;BA.debugLine="src_blood_pnl.Height = 12%y";
mostCurrent._src_blood_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 85;BA.debugLine="profile_pnl.Height = 12%y";
mostCurrent._profile_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 86;BA.debugLine="about_pnl.Height = 12%y";
mostCurrent._about_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 87;BA.debugLine="help_pnl.Height = 12%y";
mostCurrent._help_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 88;BA.debugLine="exit_pnl.Height = 12%y";
mostCurrent._exit_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 90;BA.debugLine="ban_logo.Left = 0";
mostCurrent._ban_logo.setLeft((int) (0));
 //BA.debugLineNum = 91;BA.debugLine="ban_picture.Left = ban_logo.Left + ban_logo.Width";
mostCurrent._ban_picture.setLeft((int) (mostCurrent._ban_logo.getLeft()+mostCurrent._ban_logo.getWidth()));
 //BA.debugLineNum = 92;BA.debugLine="users_panel.Left = 0";
mostCurrent._users_panel.setLeft((int) (0));
 //BA.debugLineNum = 93;BA.debugLine="src_blood_pnl.Left = 0";
mostCurrent._src_blood_pnl.setLeft((int) (0));
 //BA.debugLineNum = 94;BA.debugLine="profile_pnl.Left = 0";
mostCurrent._profile_pnl.setLeft((int) (0));
 //BA.debugLineNum = 95;BA.debugLine="about_pnl.Left = 0";
mostCurrent._about_pnl.setLeft((int) (0));
 //BA.debugLineNum = 96;BA.debugLine="help_pnl.Left = 0";
mostCurrent._help_pnl.setLeft((int) (0));
 //BA.debugLineNum = 97;BA.debugLine="exit_pnl.Left = 0";
mostCurrent._exit_pnl.setLeft((int) (0));
 //BA.debugLineNum = 98;BA.debugLine="users_heading.Left = 0";
mostCurrent._users_heading.setLeft((int) (0));
 //BA.debugLineNum = 100;BA.debugLine="users_panel.Top = 0";
mostCurrent._users_panel.setTop((int) (0));
 //BA.debugLineNum = 101;BA.debugLine="ban_picture.Top = 0";
mostCurrent._ban_picture.setTop((int) (0));
 //BA.debugLineNum = 102;BA.debugLine="ban_logo.Top = 0";
mostCurrent._ban_logo.setTop((int) (0));
 //BA.debugLineNum = 103;BA.debugLine="users_heading.Top = users_panel.Top + users_panel";
mostCurrent._users_heading.setTop((int) (mostCurrent._users_panel.getTop()+mostCurrent._users_panel.getHeight()));
 //BA.debugLineNum = 104;BA.debugLine="src_blood_pnl.Top = users_heading.Top + users_hea";
mostCurrent._src_blood_pnl.setTop((int) (mostCurrent._users_heading.getTop()+mostCurrent._users_heading.getHeight()));
 //BA.debugLineNum = 105;BA.debugLine="profile_pnl.Top = src_blood_pnl.Top + src_blood_p";
mostCurrent._profile_pnl.setTop((int) (mostCurrent._src_blood_pnl.getTop()+mostCurrent._src_blood_pnl.getHeight()));
 //BA.debugLineNum = 106;BA.debugLine="about_pnl.Top = profile_pnl.Top + profile_pnl.Hei";
mostCurrent._about_pnl.setTop((int) (mostCurrent._profile_pnl.getTop()+mostCurrent._profile_pnl.getHeight()));
 //BA.debugLineNum = 107;BA.debugLine="help_pnl.Top = about_pnl.Top + about_pnl.Height";
mostCurrent._help_pnl.setTop((int) (mostCurrent._about_pnl.getTop()+mostCurrent._about_pnl.getHeight()));
 //BA.debugLineNum = 108;BA.debugLine="exit_pnl.Top = help_pnl.Top + help_pnl.Height";
mostCurrent._exit_pnl.setTop((int) (mostCurrent._help_pnl.getTop()+mostCurrent._help_pnl.getHeight()));
 //BA.debugLineNum = 117;BA.debugLine="search_blood.Width = Activity.Width - 60%x";
mostCurrent._search_blood.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 118;BA.debugLine="about.Width = Activity.Width - 60%x";
mostCurrent._about.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 119;BA.debugLine="help.Width = Activity.Width - 60%x";
mostCurrent._help.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 120;BA.debugLine="profile.Width = Activity.Width - 60%x";
mostCurrent._profile.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 121;BA.debugLine="exit_btn.Width = Activity.Width - 60%x";
mostCurrent._exit_btn.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA)));
 //BA.debugLineNum = 122;BA.debugLine="srch_blood_img.Width = Activity.Width - 85%x";
mostCurrent._srch_blood_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 123;BA.debugLine="profile_img.Width = Activity.Width - 85%x";
mostCurrent._profile_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 124;BA.debugLine="about_img.Width = Activity.Width - 85%x";
mostCurrent._about_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 125;BA.debugLine="help_img.Width = Activity.Width - 85%x";
mostCurrent._help_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 126;BA.debugLine="exit_img.Width = Activity.Width - 85%x";
mostCurrent._exit_img.setWidth((int) (mostCurrent._activity.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA)));
 //BA.debugLineNum = 128;BA.debugLine="search_blood.Height = 9%y";
mostCurrent._search_blood.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 129;BA.debugLine="about.Height = 9%y";
mostCurrent._about.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 130;BA.debugLine="help.Height = 9%y";
mostCurrent._help.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 131;BA.debugLine="profile.Height = 9%y";
mostCurrent._profile.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 132;BA.debugLine="exit_btn.Height = 9%y";
mostCurrent._exit_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 133;BA.debugLine="srch_blood_img.Height = 9%y";
mostCurrent._srch_blood_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 134;BA.debugLine="profile_img.Height = 9%y";
mostCurrent._profile_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 135;BA.debugLine="about_img.Height = 9%y";
mostCurrent._about_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 136;BA.debugLine="help_img.Height = 9%y";
mostCurrent._help_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 137;BA.debugLine="exit_img.Height = 9%y";
mostCurrent._exit_img.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 139;BA.debugLine="users_lbl.Left = 2%x";
mostCurrent._users_lbl.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA));
 //BA.debugLineNum = 140;BA.debugLine="users_out_lbl.Left = users_lbl.Left + users_lbl.W";
mostCurrent._users_out_lbl.setLeft((int) (mostCurrent._users_lbl.getLeft()+mostCurrent._users_lbl.getWidth()));
 //BA.debugLineNum = 141;BA.debugLine="search_blood.Left = ((src_blood_pnl.Width/2)/2)/";
mostCurrent._search_blood.setLeft((int) (((mostCurrent._src_blood_pnl.getWidth()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 142;BA.debugLine="profile.Left = ((profile_pnl.Width/2)/2)";
mostCurrent._profile.setLeft((int) (((mostCurrent._profile_pnl.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 143;BA.debugLine="about.Left = (help_pnl.Width/2)";
mostCurrent._about.setLeft((int) ((mostCurrent._help_pnl.getWidth()/(double)2)));
 //BA.debugLineNum = 144;BA.debugLine="help.Left = ((about_pnl.Width/2)/2)";
mostCurrent._help.setLeft((int) (((mostCurrent._about_pnl.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 145;BA.debugLine="exit_btn.Left = ((exit_pnl.Width/2)/2)/2";
mostCurrent._exit_btn.setLeft((int) (((mostCurrent._exit_pnl.getWidth()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 147;BA.debugLine="srch_blood_img.Left = search_blood.Left + search";
mostCurrent._srch_blood_img.setLeft((int) (mostCurrent._search_blood.getLeft()+mostCurrent._search_blood.getWidth()));
 //BA.debugLineNum = 148;BA.debugLine="profile_img.Left = profile.Left + profile.Widt";
mostCurrent._profile_img.setLeft((int) (mostCurrent._profile.getLeft()+mostCurrent._profile.getWidth()));
 //BA.debugLineNum = 149;BA.debugLine="about_img.Left = about.Left - about_img.Width";
mostCurrent._about_img.setLeft((int) (mostCurrent._about.getLeft()-mostCurrent._about_img.getWidth()));
 //BA.debugLineNum = 150;BA.debugLine="help_img.Left = help.Left + help.Width";
mostCurrent._help_img.setLeft((int) (mostCurrent._help.getLeft()+mostCurrent._help.getWidth()));
 //BA.debugLineNum = 151;BA.debugLine="exit_img.Left = exit_btn.Left + exit_btn.Width";
mostCurrent._exit_img.setLeft((int) (mostCurrent._exit_btn.getLeft()+mostCurrent._exit_btn.getWidth()));
 //BA.debugLineNum = 153;BA.debugLine="users_out_lbl.Top = ((users_heading.Height/2)/2)/";
mostCurrent._users_out_lbl.setTop((int) (((mostCurrent._users_heading.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 154;BA.debugLine="users_lbl.Left = users_out_lbl.Top";
mostCurrent._users_lbl.setLeft(mostCurrent._users_out_lbl.getTop());
 //BA.debugLineNum = 156;BA.debugLine="search_blood.Top = ((src_blood_pnl.Height/2)/2)/";
mostCurrent._search_blood.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 157;BA.debugLine="about.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._about.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 158;BA.debugLine="help.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._help.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 159;BA.debugLine="profile.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._profile.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 160;BA.debugLine="exit_btn.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_btn.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 162;BA.debugLine="srch_blood_img.Top = ((src_blood_pnl.Height/2)/2";
mostCurrent._srch_blood_img.setTop((int) (((mostCurrent._src_blood_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 163;BA.debugLine="profile_img.Top = ((about_pnl.Height/2)/2)/2";
mostCurrent._profile_img.setTop((int) (((mostCurrent._about_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 164;BA.debugLine="about_img.Top = ((help_pnl.Height/2)/2)/2";
mostCurrent._about_img.setTop((int) (((mostCurrent._help_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 165;BA.debugLine="help_img.Top = ((profile_pnl.Height/2)/2)/2";
mostCurrent._help_img.setTop((int) (((mostCurrent._profile_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 166;BA.debugLine="exit_img.Top = ((exit_pnl.Height/2)/2)/2";
mostCurrent._exit_img.setTop((int) (((mostCurrent._exit_pnl.getHeight()/(double)2)/(double)2)/(double)2));
 //BA.debugLineNum = 168;BA.debugLine="search_blood.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._search_blood.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"SEARCH.png").getObject()));
 //BA.debugLineNum = 169;BA.debugLine="about.SetBackgroundImage(LoadBitmap(File.DirAsse";
mostCurrent._about.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ABOUT_US.png").getObject()));
 //BA.debugLineNum = 170;BA.debugLine="help.SetBackgroundImage(LoadBitmap(File.DirAsset";
mostCurrent._help.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"HELP.png").getObject()));
 //BA.debugLineNum = 171;BA.debugLine="profile.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._profile.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"my_profile.png").getObject()));
 //BA.debugLineNum = 172;BA.debugLine="exit_btn.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._exit_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"EXIT.png").getObject()));
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _profile_click() throws Exception{
 //BA.debugLineNum = 184;BA.debugLine="Sub profile_Click";
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _search_blood_click() throws Exception{
 //BA.debugLineNum = 181;BA.debugLine="Sub search_blood_Click";
 //BA.debugLineNum = 182;BA.debugLine="StartActivity(\"search_frame\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("search_frame"));
 //BA.debugLineNum = 183;BA.debugLine="End Sub";
return "";
}
}
