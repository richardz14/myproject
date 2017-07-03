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
			processBA = new BA(this.getApplicationContext(), null, null, "bloodlife.system", "bloodlife.system.create_account");
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
		activityBA = new BA(this, layout, processBA, "bloodlife.system", "bloodlife.system.create_account");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "bloodlife.system.create_account", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public static anywheresoftware.b4a.objects.collections.List _list_day = null;
public static anywheresoftware.b4a.objects.collections.List _list_month = null;
public static anywheresoftware.b4a.objects.collections.List _list_year = null;
public static anywheresoftware.b4a.objects.collections.List _list_gender = null;
public static String _lat = "";
public static String _lng = "";
public static int _brgy_index = 0;
public static int _street_index = 0;
public static int _gender_index = 0;
public static int _ageget = 0;
public static String _isdonatedate = "";
public static int _spin_donate_pos = 0;
public static int _donate_m_pos = 0;
public static int _donate_d_pos = 0;
public static int _donate_y_pos = 0;
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
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_day = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_month = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_year = null;
public anywheresoftware.b4a.objects.PanelWrapper _bday_panel = null;
public anywheresoftware.b4a.objects.PanelWrapper _location_panel = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_password2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _text_phonenumber2 = null;
public bloodlife.system.httpjob _insert_job = null;
public bloodlife.system.httpjob _existing_email = null;
public static String _email_exists = "";
public anywheresoftware.b4a.objects.PanelWrapper _pnl_bday_body = null;
public anywheresoftware.b4a.objects.LabelWrapper _is_donate_date = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spin_gender = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a1 = null;
public bloodlife.system.main _main = null;
public bloodlife.system.login_form _login_form = null;
public bloodlife.system.menu_form _menu_form = null;
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
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 88;BA.debugLine="Activity.LoadLayout(\"create_account\")";
mostCurrent._activity.LoadLayout("create_account",mostCurrent.activityBA);
 //BA.debugLineNum = 89;BA.debugLine="Activity.Title = \"CREATE ACCOUNT\"";
mostCurrent._activity.setTitle((Object)("CREATE ACCOUNT"));
 //BA.debugLineNum = 90;BA.debugLine="all_settings_layout";
_all_settings_layout();
 //BA.debugLineNum = 91;BA.debugLine="scrolling";
_scrolling();
 //BA.debugLineNum = 92;BA.debugLine="for_btn_animation";
_for_btn_animation();
 //BA.debugLineNum = 93;BA.debugLine="lab_needReset.Visible = False 'for the need rese";
mostCurrent._lab_needreset.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 94;BA.debugLine="location_panel.Color = Colors.Transparent";
mostCurrent._location_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 95;BA.debugLine="bday_panel.Color = Colors.Transparent";
mostCurrent._bday_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 96;BA.debugLine="insert_job.Initialize(\"inserting\",Me)";
mostCurrent._insert_job._initialize(processBA,"inserting",create_account.getObject());
 //BA.debugLineNum = 97;BA.debugLine="existing_email.Initialize(\"email_exist\",Me)";
mostCurrent._existing_email._initialize(processBA,"email_exist",create_account.getObject());
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 342;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 344;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 338;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 340;BA.debugLine="End Sub";
return "";
}
public static String  _all_settings_layout() throws Exception{
anywheresoftware.b4a.objects.drawable.GradientDrawable _gradiant = null;
int[] _col = null;
bloodlife.system.calculations _calc = null;
 //BA.debugLineNum = 346;BA.debugLine="Public Sub all_settings_layout";
 //BA.debugLineNum = 347;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 348;BA.debugLine="uptext_panel.SetBackgroundImage(LoadBitmap(Fil";
mostCurrent._uptext_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 349;BA.debugLine="create_panel.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._create_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 350;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 351;BA.debugLine="ban_logo.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._ban_logo.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"logo1.jpg").getObject()));
 //BA.debugLineNum = 354;BA.debugLine="ban_panel.Width = Activity.Width";
mostCurrent._ban_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 356;BA.debugLine="create_panel.Width = Activity.Width";
mostCurrent._create_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 357;BA.debugLine="uptext_panel.Width = Activity.Width";
mostCurrent._uptext_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 358;BA.debugLine="ban_picture.Width = 80%x";
mostCurrent._ban_picture.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 359;BA.debugLine="ban_logo.Width = ban_panel.Width - ban_picture.Wi";
mostCurrent._ban_logo.setWidth((int) (mostCurrent._ban_panel.getWidth()-mostCurrent._ban_picture.getWidth()));
 //BA.debugLineNum = 360;BA.debugLine="indicator.Width = 100%x";
mostCurrent._indicator.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 362;BA.debugLine="reg_button.Width = 50%x";
mostCurrent._reg_button.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 367;BA.debugLine="ban_panel.Height = 17%y";
mostCurrent._ban_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (17),mostCurrent.activityBA));
 //BA.debugLineNum = 368;BA.debugLine="create_panel.Height = 12%y";
mostCurrent._create_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 369;BA.debugLine="uptext_panel.Height = 8%y";
mostCurrent._uptext_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 371;BA.debugLine="ban_picture.Height =ban_panel.Height";
mostCurrent._ban_picture.setHeight(mostCurrent._ban_panel.getHeight());
 //BA.debugLineNum = 372;BA.debugLine="ban_logo.Height = ban_panel.Height";
mostCurrent._ban_logo.setHeight(mostCurrent._ban_panel.getHeight());
 //BA.debugLineNum = 374;BA.debugLine="ban_picture.Top = Activity.Top";
mostCurrent._ban_picture.setTop(mostCurrent._activity.getTop());
 //BA.debugLineNum = 375;BA.debugLine="ban_logo.Top = Activity.Top";
mostCurrent._ban_logo.setTop(mostCurrent._activity.getTop());
 //BA.debugLineNum = 376;BA.debugLine="ban_panel.Top = Activity.Top";
mostCurrent._ban_panel.setTop(mostCurrent._activity.getTop());
 //BA.debugLineNum = 377;BA.debugLine="create_panel.Top = Activity.Height - create_panel";
mostCurrent._create_panel.setTop((int) (mostCurrent._activity.getHeight()-mostCurrent._create_panel.getHeight()));
 //BA.debugLineNum = 378;BA.debugLine="uptext_panel.Top = ban_panel.Top + ban_panel.Heig";
mostCurrent._uptext_panel.setTop((int) (mostCurrent._ban_panel.getTop()+mostCurrent._ban_panel.getHeight()));
 //BA.debugLineNum = 379;BA.debugLine="indicator.Top = (uptext_panel.Height/2) - 8dip";
mostCurrent._indicator.setTop((int) ((mostCurrent._uptext_panel.getHeight()/(double)2)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (8))));
 //BA.debugLineNum = 380;BA.debugLine="reg_button.Top = ((create_panel.Height/2)/2) '4%y";
mostCurrent._reg_button.setTop((int) (((mostCurrent._create_panel.getHeight()/(double)2)/(double)2)));
 //BA.debugLineNum = 383;BA.debugLine="ban_panel.Left = Activity.Left";
mostCurrent._ban_panel.setLeft(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 385;BA.debugLine="create_panel.Left = Activity.Left";
mostCurrent._create_panel.setLeft(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 386;BA.debugLine="uptext_panel.Left = Activity.Left";
mostCurrent._uptext_panel.setLeft(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 387;BA.debugLine="ban_logo.Left = Activity.Left";
mostCurrent._ban_logo.setLeft(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 388;BA.debugLine="ban_picture.Left = ban_logo.Left + ban_logo.Width";
mostCurrent._ban_picture.setLeft((int) (mostCurrent._ban_logo.getLeft()+mostCurrent._ban_logo.getWidth()));
 //BA.debugLineNum = 390;BA.debugLine="indicator.Left = uptext_panel.Left";
mostCurrent._indicator.setLeft(mostCurrent._uptext_panel.getLeft());
 //BA.debugLineNum = 391;BA.debugLine="indicator.Gravity = Gravity.CENTER_HORIZONTAL";
mostCurrent._indicator.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 392;BA.debugLine="reg_button.Left = 25%x'create_panel.Left + ((crea";
mostCurrent._reg_button.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 397;BA.debugLine="Dim gradiant As GradientDrawable";
_gradiant = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 398;BA.debugLine="Dim col(2) As Int";
_col = new int[(int) (2)];
;
 //BA.debugLineNum = 399;BA.debugLine="col(0) = Colors.Red";
_col[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 400;BA.debugLine="col(1) = Colors.LightGray";
_col[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.LightGray;
 //BA.debugLineNum = 401;BA.debugLine="gradiant.Initialize(\"TOP_BOTTOM\",col)";
_gradiant.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 402;BA.debugLine="gradiant.CornerRadius = 5dip";
_gradiant.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 404;BA.debugLine="reg_button.Background = gradiant";
mostCurrent._reg_button.setBackground((android.graphics.drawable.Drawable)(_gradiant.getObject()));
 //BA.debugLineNum = 406;BA.debugLine="reg_button.Typeface = Typeface.LoadFromAssets(\"Hi";
mostCurrent._reg_button.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 407;BA.debugLine="indicator.Typeface = Typeface.LoadFromAssets(\"Hip";
mostCurrent._indicator.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 408;BA.debugLine="indicator.TextSize = 18";
mostCurrent._indicator.setTextSize((float) (18));
 //BA.debugLineNum = 409;BA.debugLine="Dim calc As calculations";
_calc = new bloodlife.system.calculations();
 //BA.debugLineNum = 410;BA.debugLine="calc.Initialize";
_calc._initialize(processBA);
 //BA.debugLineNum = 411;BA.debugLine="h = calc.sums_height(Activity.Height - ban_panel.";
_h = _calc._sums_height(mostCurrent._activity.getHeight()-mostCurrent._ban_panel.getHeight()-mostCurrent._uptext_panel.getHeight()-mostCurrent._create_panel.getHeight());
 //BA.debugLineNum = 412;BA.debugLine="w = calc.sums_width(Activity.Width)";
_w = _calc._sums_width(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 413;BA.debugLine="l = calc.sums_left(Activity.Left)";
_l = _calc._sums_left(mostCurrent._activity.getLeft());
 //BA.debugLineNum = 414;BA.debugLine="t = calc.sums_top(uptext_panel.Top - uptext_panel";
_t = _calc._sums_top(mostCurrent._uptext_panel.getTop()-mostCurrent._uptext_panel.getHeight());
 //BA.debugLineNum = 415;BA.debugLine="End Sub";
return "";
}
public static String  _donate_spin_day_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1350;BA.debugLine="Sub donate_spin_day_ItemClick (Position As Int, Va";
 //BA.debugLineNum = 1351;BA.debugLine="donate_d_pos = Position";
_donate_d_pos = _position;
 //BA.debugLineNum = 1353;BA.debugLine="End Sub";
return "";
}
public static String  _donate_spin_month_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1354;BA.debugLine="Sub donate_spin_month_ItemClick (Position As Int,";
 //BA.debugLineNum = 1355;BA.debugLine="donate_m_pos = Position";
_donate_m_pos = _position;
 //BA.debugLineNum = 1357;BA.debugLine="End Sub";
return "";
}
public static String  _donate_spin_year_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1358;BA.debugLine="Sub donate_spin_year_ItemClick (Position As Int, V";
 //BA.debugLineNum = 1359;BA.debugLine="donate_y_pos = Position";
_donate_y_pos = _position;
 //BA.debugLineNum = 1361;BA.debugLine="End Sub";
return "";
}
public static String  _email_existance() throws Exception{
String _email_url = "";
bloodlife.system.calculations _url_back = null;
 //BA.debugLineNum = 107;BA.debugLine="Private Sub email_existance";
 //BA.debugLineNum = 108;BA.debugLine="ProgressDialogShow2(\"Please wait...\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Please wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 109;BA.debugLine="Dim email_url As String";
_email_url = "";
 //BA.debugLineNum = 110;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 111;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 112;BA.debugLine="email_url = url_back.php_email_url(\"index.php\")";
_email_url = _url_back._php_email_url("index.php");
 //BA.debugLineNum = 114;BA.debugLine="existing_email.Download2(email_url,Array As Strin";
mostCurrent._existing_email._download2(_email_url,new String[]{"email","SELECT email FROM `person_info` where `email`='"+mostCurrent._text_email.getText()+"';"});
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
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
bloodlife.system.calculations _url_back = null;
String _ins = "";
String _m_1 = "";
String _m_2 = "";
String _merge = "";
String _gender_string = "";
String _img_string = "";
anywheresoftware.b4a.objects.StringUtils _su = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out1 = null;
anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator _htp = null;
anywheresoftware.b4a.objects.collections.JSONParser _hps = null;
anywheresoftware.b4a.objects.collections.Map _maps = null;
anywheresoftware.b4a.objects.collections.List _jsonlist = null;
String _jsonstring = "";
int _choose = 0;
 //BA.debugLineNum = 233;BA.debugLine="Private Sub existing_result";
 //BA.debugLineNum = 235;BA.debugLine="Dim full_name As String : full_name = text_fn.Tex";
_full_name = "";
 //BA.debugLineNum = 235;BA.debugLine="Dim full_name As String : full_name = text_fn.Tex";
_full_name = mostCurrent._text_fn.getText();
 //BA.debugLineNum = 236;BA.debugLine="Dim blood_type As String : blood_type = spin_bloo";
_blood_type = "";
 //BA.debugLineNum = 236;BA.debugLine="Dim blood_type As String : blood_type = spin_bloo";
_blood_type = mostCurrent._spin_bloodgroup.getSelectedItem();
 //BA.debugLineNum = 237;BA.debugLine="Dim email As String : email = text_email.Text";
_email = "";
 //BA.debugLineNum = 237;BA.debugLine="Dim email As String : email = text_email.Text";
_email = mostCurrent._text_email.getText();
 //BA.debugLineNum = 238;BA.debugLine="Dim password1 As String : password1 = text_passwo";
_password1 = "";
 //BA.debugLineNum = 238;BA.debugLine="Dim password1 As String : password1 = text_passwo";
_password1 = mostCurrent._text_password.getText();
 //BA.debugLineNum = 239;BA.debugLine="Dim password2 As String : password2 = text_passwo";
_password2 = "";
 //BA.debugLineNum = 239;BA.debugLine="Dim password2 As String : password2 = text_passwo";
_password2 = mostCurrent._text_password2.getText();
 //BA.debugLineNum = 240;BA.debugLine="Dim phone_number1 As String :  phone_number1 = te";
_phone_number1 = "";
 //BA.debugLineNum = 240;BA.debugLine="Dim phone_number1 As String :  phone_number1 = te";
_phone_number1 = mostCurrent._text_phonenumber.getText();
 //BA.debugLineNum = 241;BA.debugLine="Dim phone_number2 As String : phone_number2 = tex";
_phone_number2 = "";
 //BA.debugLineNum = 241;BA.debugLine="Dim phone_number2 As String : phone_number2 = tex";
_phone_number2 = mostCurrent._text_phonenumber2.getText();
 //BA.debugLineNum = 242;BA.debugLine="Dim brgy,street,purok As String";
_brgy = "";
_street = "";
_purok = "";
 //BA.debugLineNum = 243;BA.debugLine="brgy = location_spin_brgy.SelectedItem";
_brgy = mostCurrent._location_spin_brgy.getSelectedItem();
 //BA.debugLineNum = 244;BA.debugLine="street = location_spin_street.SelectedItem";
_street = mostCurrent._location_spin_street.getSelectedItem();
 //BA.debugLineNum = 246;BA.debugLine="Dim month,day,year As String";
_month = "";
_day = "";
_year = "";
 //BA.debugLineNum = 247;BA.debugLine="month = bday_spin_month.SelectedItem";
_month = mostCurrent._bday_spin_month.getSelectedItem();
 //BA.debugLineNum = 248;BA.debugLine="day = bday_spin_day.SelectedItem";
_day = mostCurrent._bday_spin_day.getSelectedItem();
 //BA.debugLineNum = 249;BA.debugLine="year = bday_spin_year.SelectedItem";
_year = mostCurrent._bday_spin_year.getSelectedItem();
 //BA.debugLineNum = 250;BA.debugLine="Dim answer As String : answer = text_answer.Text";
_answer = "";
 //BA.debugLineNum = 250;BA.debugLine="Dim answer As String : answer = text_answer.Text";
_answer = mostCurrent._text_answer.getText();
 //BA.debugLineNum = 251;BA.debugLine="Dim donate_boolean As String : donate_boolean = s";
_donate_boolean = "";
 //BA.debugLineNum = 251;BA.debugLine="Dim donate_boolean As String : donate_boolean = s";
_donate_boolean = mostCurrent._spin_donate_confirm.getSelectedItem();
 //BA.debugLineNum = 253;BA.debugLine="If spin_donate_pos == 0 Then";
if (_spin_donate_pos==0) { 
 //BA.debugLineNum = 254;BA.debugLine="isDonateDate = \"NONE\"";
_isdonatedate = "NONE";
 }else {
 };
 //BA.debugLineNum = 260;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 261;BA.debugLine="Dim ins,m_1,m_2,merge As String";
_ins = "";
_m_1 = "";
_m_2 = "";
_merge = "";
 //BA.debugLineNum = 262;BA.debugLine="Dim gender_string As String";
_gender_string = "";
 //BA.debugLineNum = 263;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 265;BA.debugLine="gender_string = spin_gender.GetItem(gender_ind";
_gender_string = mostCurrent._spin_gender.GetItem(_gender_index);
 //BA.debugLineNum = 266;BA.debugLine="Dim img_string As String";
_img_string = "";
 //BA.debugLineNum = 267;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 268;BA.debugLine="Dim out1 As OutputStream";
_out1 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 270;BA.debugLine="out1.InitializeToBytesArray(0) 'size not real";
_out1.InitializeToBytesArray((int) (0));
 //BA.debugLineNum = 271;BA.debugLine="If gender_index == 0 Then";
if (_gender_index==0) { 
 //BA.debugLineNum = 272;BA.debugLine="File.Copy2(File.OpenInput(File.DirAssets, \"ma";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"male_clip.png").getObject()),(java.io.OutputStream)(_out1.getObject()));
 }else {
 //BA.debugLineNum = 274;BA.debugLine="File.Copy2(File.OpenInput(File.DirAssets, \"fe";
anywheresoftware.b4a.keywords.Common.File.Copy2((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"female_clip.png").getObject()),(java.io.OutputStream)(_out1.getObject()));
 };
 //BA.debugLineNum = 276;BA.debugLine="img_string=su.EncodeBase64(out1.ToBytesArray)";
_img_string = _su.EncodeBase64(_out1.ToBytesArray());
 //BA.debugLineNum = 280;BA.debugLine="m_1 = \"INSERT INTO `person_info` (`full_name`,";
_m_1 = "INSERT INTO `person_info` (`full_name`,`blood_type`,`email`,`password`,`phone_number1`,`phone_number2`,`location_brgy`,`location_street`,`bday_month`,`bday_day`,`bday_year`,`nick_name`,`donate_boolean`,`lat`,`long`,`image`,`age`,`date_donated`,`gender`) ";
 //BA.debugLineNum = 281;BA.debugLine="m_2 = \"VALUES ('\"&full_name&\"', '\"&blood_type&";
_m_2 = "VALUES ('"+_full_name+"', '"+_blood_type+"','"+_email+"',ENCODE('"+_password2+"','goroy'),'"+_phone_number1+"','"+_phone_number2+"','"+_brgy+"','"+_street+"','"+_month+"','"+_day+"','"+_year+"','"+_answer+"','"+_donate_boolean+"','"+_lat+"','"+_lng+"','"+_img_string+"','"+BA.NumberToString(_ageget)+"','"+_isdonatedate+"','"+_gender_string+"');";
 //BA.debugLineNum = 282;BA.debugLine="merge = m_1&m_2";
_merge = _m_1+_m_2;
 //BA.debugLineNum = 283;BA.debugLine="ins = url_back.php_email_url(\"inserting.php\")";
_ins = _url_back._php_email_url("inserting.php");
 //BA.debugLineNum = 289;BA.debugLine="Dim htp As JSONGenerator";
_htp = new anywheresoftware.b4a.objects.collections.JSONParser.JSONGenerator();
 //BA.debugLineNum = 290;BA.debugLine="Dim hps As JSONParser ''not used!!";
_hps = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 291;BA.debugLine="Dim maps As Map";
_maps = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 292;BA.debugLine="Dim JSONList As List";
_jsonlist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 293;BA.debugLine="JSONList.Initialize";
_jsonlist.Initialize();
 //BA.debugLineNum = 294;BA.debugLine="maps.Initialize";
_maps.Initialize();
 //BA.debugLineNum = 295;BA.debugLine="maps.Put(\"text_fn\",full_name)";
_maps.Put((Object)("text_fn"),(Object)(_full_name));
 //BA.debugLineNum = 296;BA.debugLine="maps.Put(\"blood_type\",blood_type)";
_maps.Put((Object)("blood_type"),(Object)(_blood_type));
 //BA.debugLineNum = 297;BA.debugLine="maps.Put(\"email\",email)";
_maps.Put((Object)("email"),(Object)(_email));
 //BA.debugLineNum = 298;BA.debugLine="maps.Put(\"password\",password2)";
_maps.Put((Object)("password"),(Object)(_password2));
 //BA.debugLineNum = 299;BA.debugLine="maps.Put(\"phone_number1\",phone_number1)";
_maps.Put((Object)("phone_number1"),(Object)(_phone_number1));
 //BA.debugLineNum = 300;BA.debugLine="maps.Put(\"phone_number2\",phone_number2)";
_maps.Put((Object)("phone_number2"),(Object)(_phone_number2));
 //BA.debugLineNum = 301;BA.debugLine="maps.Put(\"location_brgy\",brgy)";
_maps.Put((Object)("location_brgy"),(Object)(_brgy));
 //BA.debugLineNum = 302;BA.debugLine="maps.Put(\"location_street\",street)";
_maps.Put((Object)("location_street"),(Object)(_street));
 //BA.debugLineNum = 303;BA.debugLine="maps.Put(\"location_purok\",\"Him. City\")";
_maps.Put((Object)("location_purok"),(Object)("Him. City"));
 //BA.debugLineNum = 304;BA.debugLine="maps.Put(\"bday_month\",month)";
_maps.Put((Object)("bday_month"),(Object)(_month));
 //BA.debugLineNum = 305;BA.debugLine="maps.Put(\"bday_day\",day)";
_maps.Put((Object)("bday_day"),(Object)(_day));
 //BA.debugLineNum = 306;BA.debugLine="maps.Put(\"bday_year\",year)";
_maps.Put((Object)("bday_year"),(Object)(_year));
 //BA.debugLineNum = 307;BA.debugLine="maps.Put(\"nick_name\",answer)";
_maps.Put((Object)("nick_name"),(Object)(_answer));
 //BA.debugLineNum = 308;BA.debugLine="maps.Put(\"donate_boolean\",donate_boolean)";
_maps.Put((Object)("donate_boolean"),(Object)(_donate_boolean));
 //BA.debugLineNum = 309;BA.debugLine="maps.Put(\"lat\",lat)";
_maps.Put((Object)("lat"),(Object)(_lat));
 //BA.debugLineNum = 310;BA.debugLine="maps.Put(\"long\",lng)";
_maps.Put((Object)("long"),(Object)(_lng));
 //BA.debugLineNum = 311;BA.debugLine="maps.Put(\"age\",ageGet)";
_maps.Put((Object)("age"),(Object)(_ageget));
 //BA.debugLineNum = 312;BA.debugLine="maps.Put(\"date_donated\",isDonateDate)";
_maps.Put((Object)("date_donated"),(Object)(_isdonatedate));
 //BA.debugLineNum = 313;BA.debugLine="maps.Put(\"gender\",gender_string)";
_maps.Put((Object)("gender"),(Object)(_gender_string));
 //BA.debugLineNum = 314;BA.debugLine="maps.Put(\"id\",login_form.id_query)";
_maps.Put((Object)("id"),(Object)(mostCurrent._login_form._id_query));
 //BA.debugLineNum = 315;BA.debugLine="maps.Put(\"image\",img_string)";
_maps.Put((Object)("image"),(Object)(_img_string));
 //BA.debugLineNum = 316;BA.debugLine="JSONList.Add(maps)";
_jsonlist.Add((Object)(_maps.getObject()));
 //BA.debugLineNum = 318;BA.debugLine="htp.Initialize2(JSONList)";
_htp.Initialize2(_jsonlist);
 //BA.debugLineNum = 319;BA.debugLine="Dim JSONstring As String";
_jsonstring = "";
 //BA.debugLineNum = 320;BA.debugLine="JSONstring = htp.ToString";
_jsonstring = _htp.ToString();
 //BA.debugLineNum = 322;BA.debugLine="insert_job.PostString(ins,\"insert=\"&htp.ToString";
mostCurrent._insert_job._poststring(_ins,"insert="+_htp.ToString());
 //BA.debugLineNum = 328;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 330;BA.debugLine="Dim choose As Int : choose = Msgbox2(\"Sucessful";
_choose = 0;
 //BA.debugLineNum = 330;BA.debugLine="Dim choose As Int : choose = Msgbox2(\"Sucessful";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2("Sucessfuly Registered, this will be redirect to login!","C O N F I R M A T I O N","OK","","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 331;BA.debugLine="Select choose";
switch (BA.switchObjectToInt(_choose,anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE)) {
case 0: {
 //BA.debugLineNum = 333;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 334;BA.debugLine="StartActivity(\"login_form\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("login_form"));
 break; }
}
;
 //BA.debugLineNum = 336;BA.debugLine="End Sub";
return "";
}
public static String  _for_btn_animation() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub for_btn_animation";
 //BA.debugLineNum = 101;BA.debugLine="a1.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 102;BA.debugLine="reg_button.Tag = a1";
mostCurrent._reg_button.setTag((Object)(mostCurrent._a1.getObject()));
 //BA.debugLineNum = 103;BA.debugLine="a1.Duration = 400";
mostCurrent._a1.setDuration((long) (400));
 //BA.debugLineNum = 104;BA.debugLine="a1.RepeatCount = 1";
mostCurrent._a1.setRepeatCount((int) (1));
 //BA.debugLineNum = 105;BA.debugLine="a1.RepeatMode = a1.REPEAT_REVERSE";
mostCurrent._a1.setRepeatMode(mostCurrent._a1.REPEAT_REVERSE);
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 31;BA.debugLine="Dim h,w,t,l As Double";
_h = 0;
_w = 0;
_t = 0;
_l = 0;
 //BA.debugLineNum = 32;BA.debugLine="Private scrool_2d As ScrollView2D";
mostCurrent._scrool_2d = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private ban_panel As Panel";
mostCurrent._ban_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private ban_logo As ImageView";
mostCurrent._ban_logo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private ban_picture As ImageView";
mostCurrent._ban_picture = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private uptext_panel As Panel";
mostCurrent._uptext_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private indicator As Label";
mostCurrent._indicator = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private all_inputs As Panel";
mostCurrent._all_inputs = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private create_panel As Panel";
mostCurrent._create_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private lab_fullname As Label";
mostCurrent._lab_fullname = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private text_fn As EditText";
mostCurrent._text_fn = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private lab_bloodgroup As Label";
mostCurrent._lab_bloodgroup = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private spin_bloodgroup As Spinner";
mostCurrent._spin_bloodgroup = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private lab_email As Label";
mostCurrent._lab_email = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private text_email As EditText";
mostCurrent._text_email = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private lab_password As Label";
mostCurrent._lab_password = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private text_password As EditText";
mostCurrent._text_password = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private lab_phonenumber As Label";
mostCurrent._lab_phonenumber = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private text_phonenumber As EditText";
mostCurrent._text_phonenumber = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private lab_needReset As Label";
mostCurrent._lab_needreset = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private lab_donate_confirm As Label";
mostCurrent._lab_donate_confirm = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private spin_donate_confirm As Spinner";
mostCurrent._spin_donate_confirm = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private reg_button As Button";
mostCurrent._reg_button = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private lab_question As Label";
mostCurrent._lab_question = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private text_answer As EditText";
mostCurrent._text_answer = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private lab_location As Label";
mostCurrent._lab_location = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private location_spin_brgy As Spinner";
mostCurrent._location_spin_brgy = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private location_spin_street As Spinner";
mostCurrent._location_spin_street = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private bday As Label";
mostCurrent._bday = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private bday_spin_month As Spinner";
mostCurrent._bday_spin_month = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private bday_spin_day As Spinner";
mostCurrent._bday_spin_day = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private bday_spin_year As Spinner";
mostCurrent._bday_spin_year = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Dim spin_day,spin_month,spin_year As Spinner";
mostCurrent._spin_day = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spin_month = new anywheresoftware.b4a.objects.SpinnerWrapper();
mostCurrent._spin_year = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private bday_panel As Panel";
mostCurrent._bday_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Private location_panel As Panel";
mostCurrent._location_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private text_password2 As EditText";
mostCurrent._text_password2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Private text_phonenumber2 As EditText";
mostCurrent._text_phonenumber2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private insert_job As HttpJob";
mostCurrent._insert_job = new bloodlife.system.httpjob();
 //BA.debugLineNum = 75;BA.debugLine="Private existing_email As HttpJob";
mostCurrent._existing_email = new bloodlife.system.httpjob();
 //BA.debugLineNum = 76;BA.debugLine="Private email_exists As String";
mostCurrent._email_exists = "";
 //BA.debugLineNum = 79;BA.debugLine="Dim pnl_bday_body As Panel";
mostCurrent._pnl_bday_body = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private is_donate_date As Label";
mostCurrent._is_donate_date = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private spin_gender As Spinner";
mostCurrent._spin_gender = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Dim a1 As Animation";
mostCurrent._a1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1285;BA.debugLine="Sub isDonate_edit_";
 //BA.debugLineNum = 1286;BA.debugLine="list_day.Initialize";
_list_day.Initialize();
 //BA.debugLineNum = 1287;BA.debugLine="list_month.Initialize";
_list_month.Initialize();
 //BA.debugLineNum = 1288;BA.debugLine="list_year.Initialize";
_list_year.Initialize();
 //BA.debugLineNum = 1289;BA.debugLine="spin_day.Initialize(\"donate_spin_day\")";
mostCurrent._spin_day.Initialize(mostCurrent.activityBA,"donate_spin_day");
 //BA.debugLineNum = 1290;BA.debugLine="spin_month.Initialize(\"donate_spin_month\")";
mostCurrent._spin_month.Initialize(mostCurrent.activityBA,"donate_spin_month");
 //BA.debugLineNum = 1291;BA.debugLine="spin_year.Initialize(\"donate_spin_year\")";
mostCurrent._spin_year.Initialize(mostCurrent.activityBA,"donate_spin_year");
 //BA.debugLineNum = 1292;BA.debugLine="For i = 1 To 31";
{
final int step7 = 1;
final int limit7 = (int) (31);
for (_i = (int) (1) ; (step7 > 0 && _i <= limit7) || (step7 < 0 && _i >= limit7); _i = ((int)(0 + _i + step7)) ) {
 //BA.debugLineNum = 1293;BA.debugLine="list_day.Add(i)";
_list_day.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 1295;BA.debugLine="Dim iNowYear As Int";
_inowyear = 0;
 //BA.debugLineNum = 1296;BA.debugLine="iNowYear = DateTime.GetYear(DateTime.Now)";
_inowyear = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 1297;BA.debugLine="For ii = 1950 To DateTime.GetYear(DateTime.Now)";
{
final int step12 = 1;
final int limit12 = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
for (_ii = (int) (1950) ; (step12 > 0 && _ii <= limit12) || (step12 < 0 && _ii >= limit12); _ii = ((int)(0 + _ii + step12)) ) {
 //BA.debugLineNum = 1298;BA.debugLine="list_year.Add(iNowYear)";
_list_year.Add((Object)(_inowyear));
 //BA.debugLineNum = 1299;BA.debugLine="iNowYear = iNowYear-1";
_inowyear = (int) (_inowyear-1);
 }
};
 //BA.debugLineNum = 1301;BA.debugLine="For iii = 1 To 12";
{
final int step16 = 1;
final int limit16 = (int) (12);
for (_iii = (int) (1) ; (step16 > 0 && _iii <= limit16) || (step16 < 0 && _iii >= limit16); _iii = ((int)(0 + _iii + step16)) ) {
 //BA.debugLineNum = 1302;BA.debugLine="list_month.Add(iii)";
_list_month.Add((Object)(_iii));
 }
};
 //BA.debugLineNum = 1304;BA.debugLine="spin_day.AddAll(list_day)";
mostCurrent._spin_day.AddAll(_list_day);
 //BA.debugLineNum = 1305;BA.debugLine="spin_month.AddAll(list_month)";
mostCurrent._spin_month.AddAll(_list_month);
 //BA.debugLineNum = 1306;BA.debugLine="spin_year.AddAll(list_year)";
mostCurrent._spin_year.AddAll(_list_year);
 //BA.debugLineNum = 1307;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1308;BA.debugLine="Dim edit_ok_btn,edit_can_btn As Button";
_edit_ok_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_edit_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1309;BA.debugLine="Dim title_lbl As Label";
_title_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1310;BA.debugLine="edit_ok_btn.Initialize(\"isdonated_ok_btn\")";
_edit_ok_btn.Initialize(mostCurrent.activityBA,"isdonated_ok_btn");
 //BA.debugLineNum = 1311;BA.debugLine="edit_can_btn.Initialize(\"isdonated_can_btn\")";
_edit_can_btn.Initialize(mostCurrent.activityBA,"isdonated_can_btn");
 //BA.debugLineNum = 1312;BA.debugLine="title_lbl.Initialize(\"\")";
_title_lbl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1313;BA.debugLine="edit_ok_btn.Text = \"OK\"";
_edit_ok_btn.setText((Object)("OK"));
 //BA.debugLineNum = 1314;BA.debugLine="edit_can_btn.Text = \"CANCEL\"";
_edit_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 1315;BA.debugLine="edit_can_btn.Typeface = Typeface.LoadFromAssets(\"";
_edit_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1316;BA.debugLine="edit_ok_btn.Typeface = Typeface.LoadFromAssets(\"H";
_edit_ok_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1317;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1318;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1319;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1320;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1321;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1322;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1323;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1324;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1325;BA.debugLine="edit_ok_btn.Background = V_btn";
_edit_ok_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1326;BA.debugLine="edit_can_btn.Background = C_btn";
_edit_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 1327;BA.debugLine="title_lbl.Text = \"SELECT DONATED DATE\"";
_title_lbl.setText((Object)("SELECT DONATED DATE"));
 //BA.debugLineNum = 1328;BA.debugLine="title_lbl.Typeface = Typeface.LoadFromAssets(\"Hip";
_title_lbl.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1329;BA.debugLine="title_lbl.Gravity = Gravity.CENTER";
_title_lbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1330;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1331;BA.debugLine="pnl_bday_body.Initialize(\"pnl_bday_body\")";
mostCurrent._pnl_bday_body.Initialize(mostCurrent.activityBA,"pnl_bday_body");
 //BA.debugLineNum = 1332;BA.debugLine="pnl_bday_body.Color = Colors.Transparent";
mostCurrent._pnl_bday_body.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1333;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1334;BA.debugLine="pnl.AddView(title_lbl,2%x,2%y,68%x,8%y)";
_pnl.AddView((android.view.View)(_title_lbl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (68),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1335;BA.debugLine="pnl.AddView(spin_day,2%x,title_lbl.Top + title_lb";
_pnl.AddView((android.view.View)(mostCurrent._spin_day.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),(int) (_title_lbl.getTop()+_title_lbl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1336;BA.debugLine="pnl.AddView(spin_month,spin_day.Left+spin_day.Wid";
_pnl.AddView((android.view.View)(mostCurrent._spin_month.getObject()),(int) (mostCurrent._spin_day.getLeft()+mostCurrent._spin_day.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_day.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1337;BA.debugLine="pnl.AddView(spin_year,spin_month.Left+spin_month.";
_pnl.AddView((android.view.View)(mostCurrent._spin_year.getObject()),(int) (mostCurrent._spin_month.getLeft()+mostCurrent._spin_month.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),mostCurrent._spin_month.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1338;BA.debugLine="pnl.AddView(edit_ok_btn,15%x,spin_year.Top+spin_y";
_pnl.AddView((android.view.View)(_edit_ok_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1339;BA.debugLine="pnl.AddView(edit_can_btn,edit_ok_btn.Left+edit_ok";
_pnl.AddView((android.view.View)(_edit_can_btn.getObject()),(int) (_edit_ok_btn.getLeft()+_edit_ok_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),(int) (mostCurrent._spin_year.getTop()+mostCurrent._spin_year.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1341;BA.debugLine="pnl_bday_body.AddView(pnl,13%x,((Activity.Height/";
mostCurrent._pnl_bday_body.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA));
 //BA.debugLineNum = 1342;BA.debugLine="pnl_bday_body.BringToFront";
mostCurrent._pnl_bday_body.BringToFront();
 //BA.debugLineNum = 1345;BA.debugLine="Activity.AddView(pnl_bday_body,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnl_bday_body.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1346;BA.debugLine="End Sub";
return "";
}
public static String  _isdonated_can_btn_click() throws Exception{
 //BA.debugLineNum = 1373;BA.debugLine="Sub isdonated_can_btn_click";
 //BA.debugLineNum = 1374;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 1375;BA.debugLine="End Sub";
return "";
}
public static String  _isdonated_ok_btn_click() throws Exception{
String _day = "";
String _month = "";
String _year = "";
 //BA.debugLineNum = 1362;BA.debugLine="Sub isdonated_ok_btn_click";
 //BA.debugLineNum = 1363;BA.debugLine="Dim day,month,year As String";
_day = "";
_month = "";
_year = "";
 //BA.debugLineNum = 1364;BA.debugLine="day = spin_day.GetItem(donate_d_pos)";
_day = mostCurrent._spin_day.GetItem(_donate_d_pos);
 //BA.debugLineNum = 1365;BA.debugLine="month = spin_month.GetItem(donate_m_pos)";
_month = mostCurrent._spin_month.GetItem(_donate_m_pos);
 //BA.debugLineNum = 1366;BA.debugLine="year = spin_year.GetItem(donate_y_pos)";
_year = mostCurrent._spin_year.GetItem(_donate_y_pos);
 //BA.debugLineNum = 1367;BA.debugLine="isDonateDate = month&\"/\"&day&\"/\"&year";
_isdonatedate = _month+"/"+_day+"/"+_year;
 //BA.debugLineNum = 1368;BA.debugLine="is_donate_date.Text = \"(\"&isDonateDate&\")\"";
mostCurrent._is_donate_date.setText((Object)("("+_isdonatedate+")"));
 //BA.debugLineNum = 1369;BA.debugLine="Msgbox(\"\"&month&\"/\"&day&\"/\"&year,\"Date Selected\")";
anywheresoftware.b4a.keywords.Common.Msgbox(""+_month+"/"+_day+"/"+_year,"Date Selected",mostCurrent.activityBA);
 //BA.debugLineNum = 1371;BA.debugLine="pnl_bday_body.RemoveView";
mostCurrent._pnl_bday_body.RemoveView();
 //BA.debugLineNum = 1372;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(bloodlife.system.httpjob _job) throws Exception{
 //BA.debugLineNum = 116;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 118;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 119;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"email_exist")) {
case 0: {
 //BA.debugLineNum = 121;BA.debugLine="email_exists = job.GetString.Trim";
mostCurrent._email_exists = _job._getstring().trim();
 //BA.debugLineNum = 123;BA.debugLine="If email_exists.Contains(text_email.Text) == Tr";
if (mostCurrent._email_exists.contains(mostCurrent._text_email.getText())==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 124;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 125;BA.debugLine="Msgbox(\"Error: Email address are already exis";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Email address are already existed.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 127;BA.debugLine="existing_result";
_existing_result();
 };
 break; }
}
;
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 134;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 135;BA.debugLine="Msgbox(\"Error: Error connecting to server, try a";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Error connecting to server, try again later.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 137;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_brgy_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1071;BA.debugLine="Sub location_spin_brgy_ItemClick (Position As Int,";
 //BA.debugLineNum = 1072;BA.debugLine="list_location_s.Clear";
_list_location_s.Clear();
 //BA.debugLineNum = 1073;BA.debugLine="location_spin_street.Clear";
mostCurrent._location_spin_street.Clear();
 //BA.debugLineNum = 1076;BA.debugLine="If Position == 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 1077;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1078;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 1079;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 1080;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 1081;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 1082;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 1083;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 1084;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 }else if(_position==1) { 
 //BA.debugLineNum = 1087;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1088;BA.debugLine="list_location_s.Add(\"Monton st.\")";
_list_location_s.Add((Object)("Monton st."));
 //BA.debugLineNum = 1089;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 //BA.debugLineNum = 1090;BA.debugLine="list_location_s.Add(\"Purok star apple\")";
_list_location_s.Add((Object)("Purok star apple"));
 //BA.debugLineNum = 1091;BA.debugLine="list_location_s.Add(\"Gatuslao st.\")";
_list_location_s.Add((Object)("Gatuslao st."));
 //BA.debugLineNum = 1092;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 1093;BA.debugLine="list_location_s.Add(\"Tabino st.\")";
_list_location_s.Add((Object)("Tabino st."));
 //BA.debugLineNum = 1094;BA.debugLine="list_location_s.Add(\"River side\")";
_list_location_s.Add((Object)("River side"));
 //BA.debugLineNum = 1095;BA.debugLine="list_location_s.Add(\"Arroyan st\")";
_list_location_s.Add((Object)("Arroyan st"));
 }else if(_position==2) { 
 //BA.debugLineNum = 1097;BA.debugLine="list_location_s.Add(\"Segovia st.\")";
_list_location_s.Add((Object)("Segovia st."));
 //BA.debugLineNum = 1098;BA.debugLine="list_location_s.Add(\"Vasquez st.\")";
_list_location_s.Add((Object)("Vasquez st."));
 //BA.debugLineNum = 1099;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 1100;BA.debugLine="list_location_s.Add(\"Old relis st.\")";
_list_location_s.Add((Object)("Old relis st."));
 //BA.debugLineNum = 1101;BA.debugLine="list_location_s.Add(\"Wayang\")";
_list_location_s.Add((Object)("Wayang"));
 //BA.debugLineNum = 1102;BA.debugLine="list_location_s.Add(\"Valencia\")";
_list_location_s.Add((Object)("Valencia"));
 //BA.debugLineNum = 1103;BA.debugLine="list_location_s.Add(\"Bungyod\")";
_list_location_s.Add((Object)("Bungyod"));
 //BA.debugLineNum = 1104;BA.debugLine="list_location_s.Add(\"Bingig\")";
_list_location_s.Add((Object)("Bingig"));
 //BA.debugLineNum = 1105;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 1106;BA.debugLine="list_location_s.Add(\"Carabalan road\")";
_list_location_s.Add((Object)("Carabalan road"));
 }else if(_position==3) { 
 //BA.debugLineNum = 1108;BA.debugLine="list_location_s.Add(\"Crusher\")";
_list_location_s.Add((Object)("Crusher"));
 //BA.debugLineNum = 1109;BA.debugLine="list_location_s.Add(\"Bangga mayok\")";
_list_location_s.Add((Object)("Bangga mayok"));
 //BA.debugLineNum = 1110;BA.debugLine="list_location_s.Add(\"Villa julita\")";
_list_location_s.Add((Object)("Villa julita"));
 //BA.debugLineNum = 1111;BA.debugLine="list_location_s.Add(\"Greenland subdivision\")";
_list_location_s.Add((Object)("Greenland subdivision"));
 //BA.debugLineNum = 1112;BA.debugLine="list_location_s.Add(\"Bangga 3c\")";
_list_location_s.Add((Object)("Bangga 3c"));
 //BA.debugLineNum = 1113;BA.debugLine="list_location_s.Add(\"Cambugnon\")";
_list_location_s.Add((Object)("Cambugnon"));
 //BA.debugLineNum = 1114;BA.debugLine="list_location_s.Add(\"Menez\")";
_list_location_s.Add((Object)("Menez"));
 //BA.debugLineNum = 1115;BA.debugLine="list_location_s.Add(\"Relis\")";
_list_location_s.Add((Object)("Relis"));
 //BA.debugLineNum = 1116;BA.debugLine="list_location_s.Add(\"Bangga patyo\")";
_list_location_s.Add((Object)("Bangga patyo"));
 }else if(_position==4) { 
 //BA.debugLineNum = 1118;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1119;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1120;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1121;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1122;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 1123;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 //BA.debugLineNum = 1124;BA.debugLine="list_location_s.Add(\"Purok 7\")";
_list_location_s.Add((Object)("Purok 7"));
 //BA.debugLineNum = 1125;BA.debugLine="list_location_s.Add(\"Purok 8\")";
_list_location_s.Add((Object)("Purok 8"));
 //BA.debugLineNum = 1126;BA.debugLine="list_location_s.Add(\"Purok 9\")";
_list_location_s.Add((Object)("Purok 9"));
 //BA.debugLineNum = 1127;BA.debugLine="list_location_s.Add(\"Purok 10\")";
_list_location_s.Add((Object)("Purok 10"));
 //BA.debugLineNum = 1128;BA.debugLine="list_location_s.Add(\"Purok 11\")";
_list_location_s.Add((Object)("Purok 11"));
 //BA.debugLineNum = 1129;BA.debugLine="list_location_s.Add(\"Purok 12\")";
_list_location_s.Add((Object)("Purok 12"));
 }else if(_position==5) { 
 //BA.debugLineNum = 1131;BA.debugLine="list_location_s.Add(\"Malusay\")";
_list_location_s.Add((Object)("Malusay"));
 //BA.debugLineNum = 1132;BA.debugLine="list_location_s.Add(\"Nasug ong\")";
_list_location_s.Add((Object)("Nasug ong"));
 //BA.debugLineNum = 1133;BA.debugLine="list_location_s.Add(\"Lugway\")";
_list_location_s.Add((Object)("Lugway"));
 //BA.debugLineNum = 1134;BA.debugLine="list_location_s.Add(\"Ubay\")";
_list_location_s.Add((Object)("Ubay"));
 //BA.debugLineNum = 1135;BA.debugLine="list_location_s.Add(\"Fisheries\")";
_list_location_s.Add((Object)("Fisheries"));
 //BA.debugLineNum = 1136;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1137;BA.debugLine="list_location_s.Add(\"Calasa\")";
_list_location_s.Add((Object)("Calasa"));
 //BA.debugLineNum = 1138;BA.debugLine="list_location_s.Add(\"Hda. Serafin\")";
_list_location_s.Add((Object)("Hda. Serafin"));
 //BA.debugLineNum = 1139;BA.debugLine="list_location_s.Add(\"Patay na suba\")";
_list_location_s.Add((Object)("Patay na suba"));
 //BA.debugLineNum = 1140;BA.debugLine="list_location_s.Add(\"Lumanog\")";
_list_location_s.Add((Object)("Lumanog"));
 //BA.debugLineNum = 1141;BA.debugLine="list_location_s.Add(\"San agustin\")";
_list_location_s.Add((Object)("San agustin"));
 //BA.debugLineNum = 1142;BA.debugLine="list_location_s.Add(\"San jose\")";
_list_location_s.Add((Object)("San jose"));
 //BA.debugLineNum = 1143;BA.debugLine="list_location_s.Add(\"Maglantay\")";
_list_location_s.Add((Object)("Maglantay"));
 //BA.debugLineNum = 1144;BA.debugLine="list_location_s.Add(\"San juan\")";
_list_location_s.Add((Object)("San juan"));
 //BA.debugLineNum = 1145;BA.debugLine="list_location_s.Add(\"Magsaha\")";
_list_location_s.Add((Object)("Magsaha"));
 //BA.debugLineNum = 1146;BA.debugLine="list_location_s.Add(\"Tagmanok\")";
_list_location_s.Add((Object)("Tagmanok"));
 //BA.debugLineNum = 1147;BA.debugLine="list_location_s.Add(\"Butong\")";
_list_location_s.Add((Object)("Butong"));
 }else if(_position==6) { 
 //BA.debugLineNum = 1149;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1150;BA.debugLine="list_location_s.Add(\"Saisi\")";
_list_location_s.Add((Object)("Saisi"));
 //BA.debugLineNum = 1151;BA.debugLine="list_location_s.Add(\"Paloypoy\")";
_list_location_s.Add((Object)("Paloypoy"));
 //BA.debugLineNum = 1152;BA.debugLine="list_location_s.Add(\"Tigue\")";
_list_location_s.Add((Object)("Tigue"));
 }else if(_position==7) { 
 //BA.debugLineNum = 1154;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1155;BA.debugLine="list_location_s.Add(\"Tonggo\")";
_list_location_s.Add((Object)("Tonggo"));
 //BA.debugLineNum = 1156;BA.debugLine="list_location_s.Add(\"Iling iling\")";
_list_location_s.Add((Object)("Iling iling"));
 //BA.debugLineNum = 1157;BA.debugLine="list_location_s.Add(\"Campayas\")";
_list_location_s.Add((Object)("Campayas"));
 //BA.debugLineNum = 1158;BA.debugLine="list_location_s.Add(\"Palayan\")";
_list_location_s.Add((Object)("Palayan"));
 //BA.debugLineNum = 1159;BA.debugLine="list_location_s.Add(\"Guia\")";
_list_location_s.Add((Object)("Guia"));
 //BA.debugLineNum = 1160;BA.debugLine="list_location_s.Add(\"An-an\")";
_list_location_s.Add((Object)("An-an"));
 //BA.debugLineNum = 1161;BA.debugLine="list_location_s.Add(\"An-an 2\")";
_list_location_s.Add((Object)("An-an 2"));
 //BA.debugLineNum = 1162;BA.debugLine="list_location_s.Add(\"Sta. rita\")";
_list_location_s.Add((Object)("Sta. rita"));
 //BA.debugLineNum = 1163;BA.debugLine="list_location_s.Add(\"Benedicto\")";
_list_location_s.Add((Object)("Benedicto"));
 //BA.debugLineNum = 1164;BA.debugLine="list_location_s.Add(\"Sta. cruz/ bunggol\")";
_list_location_s.Add((Object)("Sta. cruz/ bunggol"));
 //BA.debugLineNum = 1165;BA.debugLine="list_location_s.Add(\"Olalia\")";
_list_location_s.Add((Object)("Olalia"));
 //BA.debugLineNum = 1166;BA.debugLine="list_location_s.Add(\"Banuyo\")";
_list_location_s.Add((Object)("Banuyo"));
 //BA.debugLineNum = 1167;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1168;BA.debugLine="list_location_s.Add(\"Riverside\")";
_list_location_s.Add((Object)("Riverside"));
 }else if(_position==8) { 
 //BA.debugLineNum = 1170;BA.debugLine="list_location_s.Add(\"Balangga-an\")";
_list_location_s.Add((Object)("Balangga-an"));
 //BA.debugLineNum = 1171;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1172;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1173;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 1174;BA.debugLine="list_location_s.Add(\"Bakyas\")";
_list_location_s.Add((Object)("Bakyas"));
 }else if(_position==9) { 
 //BA.debugLineNum = 1176;BA.debugLine="list_location_s.Add(\"Cunalom\")";
_list_location_s.Add((Object)("Cunalom"));
 //BA.debugLineNum = 1177;BA.debugLine="list_location_s.Add(\"Tara\")";
_list_location_s.Add((Object)("Tara"));
 //BA.debugLineNum = 1178;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1179;BA.debugLine="list_location_s.Add(\"Casipungan\")";
_list_location_s.Add((Object)("Casipungan"));
 //BA.debugLineNum = 1180;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1181;BA.debugLine="list_location_s.Add(\"Lanipga\")";
_list_location_s.Add((Object)("Lanipga"));
 //BA.debugLineNum = 1182;BA.debugLine="list_location_s.Add(\"Bulod\")";
_list_location_s.Add((Object)("Bulod"));
 //BA.debugLineNum = 1183;BA.debugLine="list_location_s.Add(\"Bonton\")";
_list_location_s.Add((Object)("Bonton"));
 //BA.debugLineNum = 1184;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 }else if(_position==10) { 
 //BA.debugLineNum = 1186;BA.debugLine="list_location_s.Add(\"Ruiz\")";
_list_location_s.Add((Object)("Ruiz"));
 //BA.debugLineNum = 1187;BA.debugLine="list_location_s.Add(\"Balisong\")";
_list_location_s.Add((Object)("Balisong"));
 //BA.debugLineNum = 1188;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1189;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1190;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1191;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1192;BA.debugLine="list_location_s.Add(\"Dubdub\")";
_list_location_s.Add((Object)("Dubdub"));
 //BA.debugLineNum = 1193;BA.debugLine="list_location_s.Add(\"Hda. San jose valing\")";
_list_location_s.Add((Object)("Hda. San jose valing"));
 }else if(_position==11) { 
 //BA.debugLineNum = 1195;BA.debugLine="list_location_s.Add(\"Acapulco\")";
_list_location_s.Add((Object)("Acapulco"));
 //BA.debugLineNum = 1196;BA.debugLine="list_location_s.Add(\"Liki\")";
_list_location_s.Add((Object)("Liki"));
 //BA.debugLineNum = 1197;BA.debugLine="list_location_s.Add(\"500\")";
_list_location_s.Add((Object)("500"));
 //BA.debugLineNum = 1198;BA.debugLine="list_location_s.Add(\"Aglatong\")";
_list_location_s.Add((Object)("Aglatong"));
 //BA.debugLineNum = 1199;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1200;BA.debugLine="list_location_s.Add(\"Baptist\")";
_list_location_s.Add((Object)("Baptist"));
 }else if(_position==12) { 
 //BA.debugLineNum = 1202;BA.debugLine="list_location_s.Add(\"Lizares\")";
_list_location_s.Add((Object)("Lizares"));
 //BA.debugLineNum = 1203;BA.debugLine="list_location_s.Add(\"Pakol\")";
_list_location_s.Add((Object)("Pakol"));
 //BA.debugLineNum = 1204;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1205;BA.debugLine="list_location_s.Add(\"Lanete\")";
_list_location_s.Add((Object)("Lanete"));
 //BA.debugLineNum = 1206;BA.debugLine="list_location_s.Add(\"Kasoy\")";
_list_location_s.Add((Object)("Kasoy"));
 //BA.debugLineNum = 1207;BA.debugLine="list_location_s.Add(\"Bato\")";
_list_location_s.Add((Object)("Bato"));
 //BA.debugLineNum = 1208;BA.debugLine="list_location_s.Add(\"Frande\")";
_list_location_s.Add((Object)("Frande"));
 //BA.debugLineNum = 1209;BA.debugLine="list_location_s.Add(\"Bajay\")";
_list_location_s.Add((Object)("Bajay"));
 //BA.debugLineNum = 1210;BA.debugLine="list_location_s.Add(\"Poblador\")";
_list_location_s.Add((Object)("Poblador"));
 //BA.debugLineNum = 1211;BA.debugLine="list_location_s.Add(\"Culban\")";
_list_location_s.Add((Object)("Culban"));
 //BA.debugLineNum = 1212;BA.debugLine="list_location_s.Add(\"Calansi\")";
_list_location_s.Add((Object)("Calansi"));
 //BA.debugLineNum = 1213;BA.debugLine="list_location_s.Add(\"Carmen\")";
_list_location_s.Add((Object)("Carmen"));
 //BA.debugLineNum = 1214;BA.debugLine="list_location_s.Add(\"Dama\")";
_list_location_s.Add((Object)("Dama"));
 }else if(_position==13) { 
 //BA.debugLineNum = 1216;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1217;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1218;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1219;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 //BA.debugLineNum = 1220;BA.debugLine="list_location_s.Add(\"Purok 5\")";
_list_location_s.Add((Object)("Purok 5"));
 //BA.debugLineNum = 1221;BA.debugLine="list_location_s.Add(\"Purok 6\")";
_list_location_s.Add((Object)("Purok 6"));
 }else if(_position==14) { 
 //BA.debugLineNum = 1223;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1224;BA.debugLine="list_location_s.Add(\"Calubihan\")";
_list_location_s.Add((Object)("Calubihan"));
 //BA.debugLineNum = 1225;BA.debugLine="list_location_s.Add(\"Mapulang duta\")";
_list_location_s.Add((Object)("Mapulang duta"));
 //BA.debugLineNum = 1226;BA.debugLine="list_location_s.Add(\"Abud\")";
_list_location_s.Add((Object)("Abud"));
 //BA.debugLineNum = 1227;BA.debugLine="list_location_s.Add(\"Molo\")";
_list_location_s.Add((Object)("Molo"));
 //BA.debugLineNum = 1228;BA.debugLine="list_location_s.Add(\"Balabag\")";
_list_location_s.Add((Object)("Balabag"));
 //BA.debugLineNum = 1229;BA.debugLine="list_location_s.Add(\"Pandan\")";
_list_location_s.Add((Object)("Pandan"));
 //BA.debugLineNum = 1230;BA.debugLine="list_location_s.Add(\"Nahulop\")";
_list_location_s.Add((Object)("Nahulop"));
 //BA.debugLineNum = 1231;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 1232;BA.debugLine="list_location_s.Add(\"Aglaoa\")";
_list_location_s.Add((Object)("Aglaoa"));
 }else if(_position==15) { 
 //BA.debugLineNum = 1234;BA.debugLine="list_location_s.Add(\"Purok 1\")";
_list_location_s.Add((Object)("Purok 1"));
 //BA.debugLineNum = 1235;BA.debugLine="list_location_s.Add(\"Purok 2\")";
_list_location_s.Add((Object)("Purok 2"));
 //BA.debugLineNum = 1236;BA.debugLine="list_location_s.Add(\"Purok 3\")";
_list_location_s.Add((Object)("Purok 3"));
 //BA.debugLineNum = 1237;BA.debugLine="list_location_s.Add(\"Purok 4\")";
_list_location_s.Add((Object)("Purok 4"));
 }else if(_position==16) { 
 //BA.debugLineNum = 1239;BA.debugLine="list_location_s.Add(\"ORS\")";
_list_location_s.Add((Object)("ORS"));
 //BA.debugLineNum = 1240;BA.debugLine="list_location_s.Add(\"Aloe vera\")";
_list_location_s.Add((Object)("Aloe vera"));
 //BA.debugLineNum = 1241;BA.debugLine="list_location_s.Add(\"SCAD\")";
_list_location_s.Add((Object)("SCAD"));
 //BA.debugLineNum = 1242;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1243;BA.debugLine="list_location_s.Add(\"Sampaguita\")";
_list_location_s.Add((Object)("Sampaguita"));
 //BA.debugLineNum = 1244;BA.debugLine="list_location_s.Add(\"Bonguinvilla\")";
_list_location_s.Add((Object)("Bonguinvilla"));
 //BA.debugLineNum = 1245;BA.debugLine="list_location_s.Add(\"Cagay\")";
_list_location_s.Add((Object)("Cagay"));
 //BA.debugLineNum = 1246;BA.debugLine="list_location_s.Add(\"Naga\")";
_list_location_s.Add((Object)("Naga"));
 }else if(_position==17) { 
 //BA.debugLineNum = 1248;BA.debugLine="list_location_s.Add(\"Hda. Naval\")";
_list_location_s.Add((Object)("Hda. Naval"));
 //BA.debugLineNum = 1249;BA.debugLine="list_location_s.Add(\"Antipolo\")";
_list_location_s.Add((Object)("Antipolo"));
 //BA.debugLineNum = 1250;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 1251;BA.debugLine="list_location_s.Add(\"Punta talaban\")";
_list_location_s.Add((Object)("Punta talaban"));
 //BA.debugLineNum = 1252;BA.debugLine="list_location_s.Add(\"Batang guwaan\")";
_list_location_s.Add((Object)("Batang guwaan"));
 //BA.debugLineNum = 1253;BA.debugLine="list_location_s.Add(\"Batang sulod\")";
_list_location_s.Add((Object)("Batang sulod"));
 //BA.debugLineNum = 1254;BA.debugLine="list_location_s.Add(\"Mabini st.\")";
_list_location_s.Add((Object)("Mabini st."));
 //BA.debugLineNum = 1255;BA.debugLine="list_location_s.Add(\"Cubay\")";
_list_location_s.Add((Object)("Cubay"));
 //BA.debugLineNum = 1256;BA.debugLine="list_location_s.Add(\"Hacienda silos\")";
_list_location_s.Add((Object)("Hacienda silos"));
 //BA.debugLineNum = 1257;BA.debugLine="list_location_s.Add(\"Lopez jeana 1\")";
_list_location_s.Add((Object)("Lopez jeana 1"));
 //BA.debugLineNum = 1258;BA.debugLine="list_location_s.Add(\"Lopez jeana 2\")";
_list_location_s.Add((Object)("Lopez jeana 2"));
 }else if(_position==18) { 
 //BA.debugLineNum = 1260;BA.debugLine="list_location_s.Add(\"Ilawod\")";
_list_location_s.Add((Object)("Ilawod"));
 //BA.debugLineNum = 1261;BA.debugLine="list_location_s.Add(\"Buhian\")";
_list_location_s.Add((Object)("Buhian"));
 //BA.debugLineNum = 1262;BA.debugLine="list_location_s.Add(\"Proper\")";
_list_location_s.Add((Object)("Proper"));
 //BA.debugLineNum = 1263;BA.debugLine="list_location_s.Add(\"Mambato\")";
_list_location_s.Add((Object)("Mambato"));
 };
 //BA.debugLineNum = 1266;BA.debugLine="brgy_index = Position";
_brgy_index = _position;
 //BA.debugLineNum = 1267;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 1270;BA.debugLine="End Sub";
return "";
}
public static String  _location_spin_street_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1271;BA.debugLine="Sub location_spin_street_ItemClick (Position As In";
 //BA.debugLineNum = 1272;BA.debugLine="street_index = Position";
_street_index = _position;
 //BA.debugLineNum = 1273;BA.debugLine="street_lat_lng";
_street_lat_lng();
 //BA.debugLineNum = 1274;BA.debugLine="End Sub";
return "";
}
public static String  _pnl_bday_body_click() throws Exception{
 //BA.debugLineNum = 1347;BA.debugLine="Sub pnl_bday_body_click";
 //BA.debugLineNum = 1349;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 13;BA.debugLine="Dim list_day,list_month,list_year As List";
_list_day = new anywheresoftware.b4a.objects.collections.List();
_list_month = new anywheresoftware.b4a.objects.collections.List();
_list_year = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Dim list_gender As List";
_list_gender = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 16;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "";
 //BA.debugLineNum = 16;BA.debugLine="Dim lat As String : lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 17;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "";
 //BA.debugLineNum = 17;BA.debugLine="Dim lng As String : lng = \"122.869168\"";
_lng = "122.869168";
 //BA.debugLineNum = 18;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = 0;
 //BA.debugLineNum = 18;BA.debugLine="Dim brgy_index As Int : brgy_index = 0";
_brgy_index = (int) (0);
 //BA.debugLineNum = 19;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = 0;
 //BA.debugLineNum = 19;BA.debugLine="Dim street_index As Int : street_index = 0";
_street_index = (int) (0);
 //BA.debugLineNum = 20;BA.debugLine="Dim gender_index As Int : gender_index = 0";
_gender_index = 0;
 //BA.debugLineNum = 20;BA.debugLine="Dim gender_index As Int : gender_index = 0";
_gender_index = (int) (0);
 //BA.debugLineNum = 21;BA.debugLine="Dim ageGet As Int";
_ageget = 0;
 //BA.debugLineNum = 23;BA.debugLine="Dim isDonateDate As String";
_isdonatedate = "";
 //BA.debugLineNum = 24;BA.debugLine="Dim spin_donate_pos As Int : spin_donate_pos =0";
_spin_donate_pos = 0;
 //BA.debugLineNum = 24;BA.debugLine="Dim spin_donate_pos As Int : spin_donate_pos =0";
_spin_donate_pos = (int) (0);
 //BA.debugLineNum = 25;BA.debugLine="Dim donate_m_pos,donate_d_pos,donate_y_pos As In";
_donate_m_pos = 0;
_donate_d_pos = 0;
_donate_y_pos = 0;
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
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
int _nmonth = 0;
int _nday = 0;
int _nyear = 0;
int _age = 0;
int _pyear = 0;
int _pmonth = 0;
int _pday = 0;
 //BA.debugLineNum = 139;BA.debugLine="Sub reg_button_Click";
 //BA.debugLineNum = 140;BA.debugLine="a1.Start(reg_button)";
mostCurrent._a1.Start((android.view.View)(mostCurrent._reg_button.getObject()));
 //BA.debugLineNum = 142;BA.debugLine="Dim full_name As String : full_name = text_fn.Tex";
_full_name = "";
 //BA.debugLineNum = 142;BA.debugLine="Dim full_name As String : full_name = text_fn.Tex";
_full_name = mostCurrent._text_fn.getText();
 //BA.debugLineNum = 143;BA.debugLine="Dim blood_type As String : blood_type = spin_bloo";
_blood_type = "";
 //BA.debugLineNum = 143;BA.debugLine="Dim blood_type As String : blood_type = spin_bloo";
_blood_type = mostCurrent._spin_bloodgroup.getSelectedItem();
 //BA.debugLineNum = 144;BA.debugLine="Dim email As String : email = text_email.Text";
_email = "";
 //BA.debugLineNum = 144;BA.debugLine="Dim email As String : email = text_email.Text";
_email = mostCurrent._text_email.getText();
 //BA.debugLineNum = 145;BA.debugLine="Dim password1 As String : password1 = text_passwo";
_password1 = "";
 //BA.debugLineNum = 145;BA.debugLine="Dim password1 As String : password1 = text_passwo";
_password1 = mostCurrent._text_password.getText();
 //BA.debugLineNum = 146;BA.debugLine="Dim password2 As String : password2 = text_passwo";
_password2 = "";
 //BA.debugLineNum = 146;BA.debugLine="Dim password2 As String : password2 = text_passwo";
_password2 = mostCurrent._text_password2.getText();
 //BA.debugLineNum = 147;BA.debugLine="Dim phone_number1 As String :  phone_number1 = te";
_phone_number1 = "";
 //BA.debugLineNum = 147;BA.debugLine="Dim phone_number1 As String :  phone_number1 = te";
_phone_number1 = mostCurrent._text_phonenumber.getText();
 //BA.debugLineNum = 148;BA.debugLine="Dim phone_number2 As String : phone_number2 = tex";
_phone_number2 = "";
 //BA.debugLineNum = 148;BA.debugLine="Dim phone_number2 As String : phone_number2 = tex";
_phone_number2 = mostCurrent._text_phonenumber2.getText();
 //BA.debugLineNum = 149;BA.debugLine="Dim brgy,street,purok As String";
_brgy = "";
_street = "";
_purok = "";
 //BA.debugLineNum = 150;BA.debugLine="brgy = location_spin_brgy.SelectedItem";
_brgy = mostCurrent._location_spin_brgy.getSelectedItem();
 //BA.debugLineNum = 151;BA.debugLine="street = location_spin_street.SelectedItem";
_street = mostCurrent._location_spin_street.getSelectedItem();
 //BA.debugLineNum = 153;BA.debugLine="Dim month,day,year As String";
_month = "";
_day = "";
_year = "";
 //BA.debugLineNum = 154;BA.debugLine="month = bday_spin_month.SelectedItem";
_month = mostCurrent._bday_spin_month.getSelectedItem();
 //BA.debugLineNum = 155;BA.debugLine="day = bday_spin_day.SelectedItem";
_day = mostCurrent._bday_spin_day.getSelectedItem();
 //BA.debugLineNum = 156;BA.debugLine="year = bday_spin_year.SelectedItem";
_year = mostCurrent._bday_spin_year.getSelectedItem();
 //BA.debugLineNum = 157;BA.debugLine="Dim answer As String : answer = text_answer.Text";
_answer = "";
 //BA.debugLineNum = 157;BA.debugLine="Dim answer As String : answer = text_answer.Text";
_answer = mostCurrent._text_answer.getText();
 //BA.debugLineNum = 158;BA.debugLine="Dim donate_boolean As String : donate_boolean = s";
_donate_boolean = "";
 //BA.debugLineNum = 158;BA.debugLine="Dim donate_boolean As String : donate_boolean = s";
_donate_boolean = mostCurrent._spin_donate_confirm.getSelectedItem();
 //BA.debugLineNum = 160;BA.debugLine="Dim Nmonth,Nday,Nyear As Int";
_nmonth = 0;
_nday = 0;
_nyear = 0;
 //BA.debugLineNum = 161;BA.debugLine="Nday = DateTime.GetDayOfMonth(DateTime.Now)";
_nday = anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 162;BA.debugLine="Nmonth = DateTime.GetMonth(DateTime.Now)";
_nmonth = anywheresoftware.b4a.keywords.Common.DateTime.GetMonth(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 163;BA.debugLine="Nyear = DateTime.GetYear(DateTime.Now)";
_nyear = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 165;BA.debugLine="Dim age,Pyear,Pmonth,Pday As Int";
_age = 0;
_pyear = 0;
_pmonth = 0;
_pday = 0;
 //BA.debugLineNum = 166;BA.debugLine="Pyear = year";
_pyear = (int)(Double.parseDouble(_year));
 //BA.debugLineNum = 167;BA.debugLine="Pmonth = month";
_pmonth = (int)(Double.parseDouble(_month));
 //BA.debugLineNum = 168;BA.debugLine="Pday = day";
_pday = (int)(Double.parseDouble(_day));
 //BA.debugLineNum = 169;BA.debugLine="age = Nyear - Pyear";
_age = (int) (_nyear-_pyear);
 //BA.debugLineNum = 170;BA.debugLine="If Pmonth <= Nmonth And Pday <= Nday Then";
if (_pmonth<=_nmonth && _pday<=_nday) { 
 //BA.debugLineNum = 172;BA.debugLine="ageGet = age";
_ageget = _age;
 }else {
 //BA.debugLineNum = 174;BA.debugLine="ageGet = age-1";
_ageget = (int) (_age-1);
 };
 //BA.debugLineNum = 180;BA.debugLine="If text_fn.Text == \"\"  Or text_email.Text == \"\" O";
if ((mostCurrent._text_fn.getText()).equals("") || (mostCurrent._text_email.getText()).equals("") || (mostCurrent._text_password.getText()).equals("") || (mostCurrent._text_password2.getText()).equals("") || (mostCurrent._text_phonenumber.getText()).equals("") || (mostCurrent._text_phonenumber2.getText()).equals("") || (mostCurrent._text_password2.getText()).equals("") || (mostCurrent._text_answer.getText()).equals("")) { 
 //BA.debugLineNum = 181;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 182;BA.debugLine="Msgbox(\"Error: Fill up those empty fields before";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Fill up those empty fields before you registered!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 185;BA.debugLine="If password1.Contains(password2) == False Then";
if (_password1.contains(_password2)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 186;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 187;BA.debugLine="text_password.Text = \"\"";
mostCurrent._text_password.setText((Object)(""));
 //BA.debugLineNum = 188;BA.debugLine="text_password2.Text = \"\"";
mostCurrent._text_password2.setText((Object)(""));
 //BA.debugLineNum = 189;BA.debugLine="Msgbox(\"Error: Password did not match!\",\"C O N";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Password did not match!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else if(mostCurrent._text_email.getText().indexOf("@")==-1 || mostCurrent._text_email.getText().indexOf(".")==-1) { 
 //BA.debugLineNum = 191;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 192;BA.debugLine="text_password.Text = \"\"";
mostCurrent._text_password.setText((Object)(""));
 //BA.debugLineNum = 193;BA.debugLine="text_password2.Text = \"\"";
mostCurrent._text_password2.setText((Object)(""));
 //BA.debugLineNum = 194;BA.debugLine="Msgbox(\"Error: Must be a valid email address!\",";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Must be a valid email address!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else if(mostCurrent._text_phonenumber.getText().length()<=10 || mostCurrent._text_phonenumber2.getText().length()<=10) { 
 //BA.debugLineNum = 196;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 197;BA.debugLine="text_password.Text = \"\"";
mostCurrent._text_password.setText((Object)(""));
 //BA.debugLineNum = 198;BA.debugLine="text_password2.Text = \"\"";
mostCurrent._text_password2.setText((Object)(""));
 //BA.debugLineNum = 199;BA.debugLine="Msgbox(\"Error: Phone number must be 11 digits!\"";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Phone number must be 11 digits!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else if(mostCurrent._text_password.getText().length()<=5 || mostCurrent._text_password2.getText().length()<=5) { 
 //BA.debugLineNum = 201;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 202;BA.debugLine="Msgbox(\"Error: Password must contain a minimum";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Password must contain a minimum of 6 letters and above!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else if(_ageget<18) { 
 //BA.debugLineNum = 204;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 205;BA.debugLine="Msgbox(\"Error: Age must be 18 above to register";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Age must be 18 above to register!","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 207;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 208;BA.debugLine="email_existance";
_email_existance();
 };
 };
 //BA.debugLineNum = 231;BA.debugLine="End Sub";
return "";
}
public static String  _scrolling() throws Exception{
 //BA.debugLineNum = 502;BA.debugLine="Sub scrolling";
 //BA.debugLineNum = 504;BA.debugLine="scrool_2d.Initialize(100%x,112%y,\"scroll2d\")";
mostCurrent._scrool_2d.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (112),mostCurrent.activityBA),"scroll2d");
 //BA.debugLineNum = 506;BA.debugLine="scrool_2d.Panel.LoadLayout(\"create_all_inputs\")";
mostCurrent._scrool_2d.getPanel().LoadLayout("create_all_inputs",mostCurrent.activityBA);
 //BA.debugLineNum = 507;BA.debugLine="scrool_2d.ScrollbarsVisibility(False,False)";
mostCurrent._scrool_2d.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 508;BA.debugLine="all_inputs.Width = 100%x";
mostCurrent._all_inputs.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 509;BA.debugLine="all_inputs.Height = 100%y";
mostCurrent._all_inputs.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 510;BA.debugLine="scrool_2d.Panel.SendToBack";
mostCurrent._scrool_2d.getPanel().SendToBack();
 //BA.debugLineNum = 512;BA.debugLine="all_inputs.SendToBack";
mostCurrent._all_inputs.SendToBack();
 //BA.debugLineNum = 513;BA.debugLine="all_inputs.Color = Colors.Transparent";
mostCurrent._all_inputs.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 514;BA.debugLine="Activity.AddView(scrool_2d,0,uptext_panel.Top + u";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._scrool_2d.getObject()),(int) (0),(int) (mostCurrent._uptext_panel.getTop()+mostCurrent._uptext_panel.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),(int) (mostCurrent._activity.getHeight()-mostCurrent._ban_panel.getHeight()-mostCurrent._uptext_panel.getHeight()-mostCurrent._create_panel.getHeight()));
 //BA.debugLineNum = 515;BA.debugLine="spinners_list_data";
_spinners_list_data();
 //BA.debugLineNum = 516;BA.debugLine="End Sub";
return "";
}
public static String  _spin_donate_confirm_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1275;BA.debugLine="Sub spin_donate_confirm_ItemClick (Position As Int";
 //BA.debugLineNum = 1276;BA.debugLine="spin_donate_pos = Position";
_spin_donate_pos = _position;
 //BA.debugLineNum = 1277;BA.debugLine="Log(\"pos: \"&spin_donate_pos)";
anywheresoftware.b4a.keywords.Common.Log("pos: "+BA.NumberToString(_spin_donate_pos));
 //BA.debugLineNum = 1278;BA.debugLine="If Position == 0 Then";
if (_position==0) { 
 //BA.debugLineNum = 1279;BA.debugLine="is_donate_date.Text = \" \"";
mostCurrent._is_donate_date.setText((Object)(" "));
 //BA.debugLineNum = 1280;BA.debugLine="isDonateDate = \"NONE\"";
_isdonatedate = "NONE";
 }else {
 //BA.debugLineNum = 1282;BA.debugLine="isDonate_edit_";
_isdonate_edit_();
 };
 //BA.debugLineNum = 1284;BA.debugLine="End Sub";
return "";
}
public static String  _spin_gender_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 1377;BA.debugLine="Sub spin_gender_ItemClick (Position As Int, Value";
 //BA.debugLineNum = 1378;BA.debugLine="gender_index = Position";
_gender_index = _position;
 //BA.debugLineNum = 1379;BA.debugLine="End Sub";
return "";
}
public static String  _spinners_list_data() throws Exception{
int _i = 0;
int _inowyear = 0;
int _ii = 0;
int _iii = 0;
 //BA.debugLineNum = 416;BA.debugLine="Sub spinners_list_data";
 //BA.debugLineNum = 417;BA.debugLine="list_bloodgroup.Initialize";
_list_bloodgroup.Initialize();
 //BA.debugLineNum = 418;BA.debugLine="list_donate_confirm.Initialize";
_list_donate_confirm.Initialize();
 //BA.debugLineNum = 419;BA.debugLine="list_bday_m.Initialize";
_list_bday_m.Initialize();
 //BA.debugLineNum = 420;BA.debugLine="list_bday_d.Initialize";
_list_bday_d.Initialize();
 //BA.debugLineNum = 421;BA.debugLine="list_bday_y.Initialize";
_list_bday_y.Initialize();
 //BA.debugLineNum = 422;BA.debugLine="list_location_b.Initialize";
_list_location_b.Initialize();
 //BA.debugLineNum = 423;BA.debugLine="list_location_s.Initialize";
_list_location_s.Initialize();
 //BA.debugLineNum = 424;BA.debugLine="list_gender.Initialize";
_list_gender.Initialize();
 //BA.debugLineNum = 427;BA.debugLine="list_bloodgroup.Add(\"A+\")";
_list_bloodgroup.Add((Object)("A+"));
 //BA.debugLineNum = 428;BA.debugLine="list_bloodgroup.Add(\"B+\")";
_list_bloodgroup.Add((Object)("B+"));
 //BA.debugLineNum = 429;BA.debugLine="list_bloodgroup.Add(\"O+\")";
_list_bloodgroup.Add((Object)("O+"));
 //BA.debugLineNum = 430;BA.debugLine="list_bloodgroup.Add(\"AB+\")";
_list_bloodgroup.Add((Object)("AB+"));
 //BA.debugLineNum = 435;BA.debugLine="list_bloodgroup.Add(\"A-\")";
_list_bloodgroup.Add((Object)("A-"));
 //BA.debugLineNum = 436;BA.debugLine="list_bloodgroup.Add(\"B-\")";
_list_bloodgroup.Add((Object)("B-"));
 //BA.debugLineNum = 437;BA.debugLine="list_bloodgroup.Add(\"O-\")";
_list_bloodgroup.Add((Object)("O-"));
 //BA.debugLineNum = 438;BA.debugLine="list_bloodgroup.Add(\"AB-\")";
_list_bloodgroup.Add((Object)("AB-"));
 //BA.debugLineNum = 439;BA.debugLine="spin_bloodgroup.AddAll(list_bloodgroup)";
mostCurrent._spin_bloodgroup.AddAll(_list_bloodgroup);
 //BA.debugLineNum = 441;BA.debugLine="list_donate_confirm.Add(\"NO\")";
_list_donate_confirm.Add((Object)("NO"));
 //BA.debugLineNum = 442;BA.debugLine="list_donate_confirm.Add(\"YES\")";
_list_donate_confirm.Add((Object)("YES"));
 //BA.debugLineNum = 443;BA.debugLine="spin_donate_confirm.AddAll(list_donate_confirm)";
mostCurrent._spin_donate_confirm.AddAll(_list_donate_confirm);
 //BA.debugLineNum = 445;BA.debugLine="list_gender.Add(\"Male\")";
_list_gender.Add((Object)("Male"));
 //BA.debugLineNum = 446;BA.debugLine="list_gender.Add(\"Female\")";
_list_gender.Add((Object)("Female"));
 //BA.debugLineNum = 447;BA.debugLine="spin_gender.AddAll(list_gender)";
mostCurrent._spin_gender.AddAll(_list_gender);
 //BA.debugLineNum = 449;BA.debugLine="For i = 1 To 31";
{
final int step24 = 1;
final int limit24 = (int) (31);
for (_i = (int) (1) ; (step24 > 0 && _i <= limit24) || (step24 < 0 && _i >= limit24); _i = ((int)(0 + _i + step24)) ) {
 //BA.debugLineNum = 450;BA.debugLine="list_bday_d.Add(i)";
_list_bday_d.Add((Object)(_i));
 }
};
 //BA.debugLineNum = 452;BA.debugLine="Dim iNowYear As Int";
_inowyear = 0;
 //BA.debugLineNum = 453;BA.debugLine="iNowYear = DateTime.GetYear(DateTime.Now)";
_inowyear = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 454;BA.debugLine="For ii = 1950 To DateTime.GetYear(DateTime.Now)";
{
final int step29 = 1;
final int limit29 = anywheresoftware.b4a.keywords.Common.DateTime.GetYear(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
for (_ii = (int) (1950) ; (step29 > 0 && _ii <= limit29) || (step29 < 0 && _ii >= limit29); _ii = ((int)(0 + _ii + step29)) ) {
 //BA.debugLineNum = 455;BA.debugLine="list_bday_y.Add(iNowYear)";
_list_bday_y.Add((Object)(_inowyear));
 //BA.debugLineNum = 456;BA.debugLine="iNowYear = iNowYear-1";
_inowyear = (int) (_inowyear-1);
 }
};
 //BA.debugLineNum = 458;BA.debugLine="For iii = 1 To 12";
{
final int step33 = 1;
final int limit33 = (int) (12);
for (_iii = (int) (1) ; (step33 > 0 && _iii <= limit33) || (step33 < 0 && _iii >= limit33); _iii = ((int)(0 + _iii + step33)) ) {
 //BA.debugLineNum = 459;BA.debugLine="list_bday_m.Add(iii)";
_list_bday_m.Add((Object)(_iii));
 }
};
 //BA.debugLineNum = 461;BA.debugLine="bday_spin_month.AddAll(list_bday_m)";
mostCurrent._bday_spin_month.AddAll(_list_bday_m);
 //BA.debugLineNum = 462;BA.debugLine="bday_spin_day.AddAll(list_bday_d)";
mostCurrent._bday_spin_day.AddAll(_list_bday_d);
 //BA.debugLineNum = 463;BA.debugLine="bday_spin_year.AddAll(list_bday_y)";
mostCurrent._bday_spin_year.AddAll(_list_bday_y);
 //BA.debugLineNum = 465;BA.debugLine="list_location_b.Add(\"Barangay  1\") 'index 0";
_list_location_b.Add((Object)("Barangay  1"));
 //BA.debugLineNum = 466;BA.debugLine="list_location_b.Add(\"Barangay 2\") 'index 1";
_list_location_b.Add((Object)("Barangay 2"));
 //BA.debugLineNum = 467;BA.debugLine="list_location_b.Add(\"Barangay 3\") 'index 2";
_list_location_b.Add((Object)("Barangay 3"));
 //BA.debugLineNum = 468;BA.debugLine="list_location_b.Add(\"Barangay 4\") 'index 3";
_list_location_b.Add((Object)("Barangay 4"));
 //BA.debugLineNum = 469;BA.debugLine="list_location_b.Add(\"Aguisan\") 'index 4";
_list_location_b.Add((Object)("Aguisan"));
 //BA.debugLineNum = 470;BA.debugLine="list_location_b.Add(\"caradio-an\") 'index 5";
_list_location_b.Add((Object)("caradio-an"));
 //BA.debugLineNum = 471;BA.debugLine="list_location_b.Add(\"Buenavista\") 'index 6";
_list_location_b.Add((Object)("Buenavista"));
 //BA.debugLineNum = 472;BA.debugLine="list_location_b.Add(\"Cabadiangan\") 'index 7";
_list_location_b.Add((Object)("Cabadiangan"));
 //BA.debugLineNum = 473;BA.debugLine="list_location_b.Add(\"Cabanbanan\") 'index 8";
_list_location_b.Add((Object)("Cabanbanan"));
 //BA.debugLineNum = 474;BA.debugLine="list_location_b.Add(\"Carabalan\") 'index 9";
_list_location_b.Add((Object)("Carabalan"));
 //BA.debugLineNum = 475;BA.debugLine="list_location_b.Add(\"Libacao\") 'index 10";
_list_location_b.Add((Object)("Libacao"));
 //BA.debugLineNum = 476;BA.debugLine="list_location_b.Add(\"Mahalang\") 'index 11";
_list_location_b.Add((Object)("Mahalang"));
 //BA.debugLineNum = 477;BA.debugLine="list_location_b.Add(\"Mambagaton\") 'index 12";
_list_location_b.Add((Object)("Mambagaton"));
 //BA.debugLineNum = 478;BA.debugLine="list_location_b.Add(\"Nabalian\") 'index 13";
_list_location_b.Add((Object)("Nabalian"));
 //BA.debugLineNum = 479;BA.debugLine="list_location_b.Add(\"San Antonio\") 'index 14";
_list_location_b.Add((Object)("San Antonio"));
 //BA.debugLineNum = 480;BA.debugLine="list_location_b.Add(\"Saraet\") 'index 15";
_list_location_b.Add((Object)("Saraet"));
 //BA.debugLineNum = 481;BA.debugLine="list_location_b.Add(\"Suay\") 'index 16";
_list_location_b.Add((Object)("Suay"));
 //BA.debugLineNum = 482;BA.debugLine="list_location_b.Add(\"Talaban\") 'index 17";
_list_location_b.Add((Object)("Talaban"));
 //BA.debugLineNum = 483;BA.debugLine="list_location_b.Add(\"Tooy\") 'index 18";
_list_location_b.Add((Object)("Tooy"));
 //BA.debugLineNum = 489;BA.debugLine="list_location_s.Add(\"Rizal st.\")";
_list_location_s.Add((Object)("Rizal st."));
 //BA.debugLineNum = 490;BA.debugLine="list_location_s.Add(\"Valega st.\")";
_list_location_s.Add((Object)("Valega st."));
 //BA.debugLineNum = 491;BA.debugLine="list_location_s.Add(\"Sarmiento st.\")";
_list_location_s.Add((Object)("Sarmiento st."));
 //BA.debugLineNum = 492;BA.debugLine="list_location_s.Add(\"Bantolinao st.\")";
_list_location_s.Add((Object)("Bantolinao st."));
 //BA.debugLineNum = 493;BA.debugLine="list_location_s.Add(\"Versoza st.\")";
_list_location_s.Add((Object)("Versoza st."));
 //BA.debugLineNum = 494;BA.debugLine="list_location_s.Add(\"Jimenez st.\")";
_list_location_s.Add((Object)("Jimenez st."));
 //BA.debugLineNum = 495;BA.debugLine="list_location_s.Add(\"Olmedo st.\")";
_list_location_s.Add((Object)("Olmedo st."));
 //BA.debugLineNum = 496;BA.debugLine="list_location_s.Add(\"Burgos st.\")";
_list_location_s.Add((Object)("Burgos st."));
 //BA.debugLineNum = 498;BA.debugLine="location_spin_street.AddAll(list_location_s)";
mostCurrent._location_spin_street.AddAll(_list_location_s);
 //BA.debugLineNum = 499;BA.debugLine="location_spin_brgy.AddAll(list_location_b)";
mostCurrent._location_spin_brgy.AddAll(_list_location_b);
 //BA.debugLineNum = 501;BA.debugLine="End Sub";
return "";
}
public static String  _street_lat_lng() throws Exception{
 //BA.debugLineNum = 517;BA.debugLine="Sub street_lat_lng";
 //BA.debugLineNum = 518;BA.debugLine="If brgy_index == 0 And street_index == 0 Then";
if (_brgy_index==0 && _street_index==0) { 
 //BA.debugLineNum = 519;BA.debugLine="lat = \"10.098014\"";
_lat = "10.098014";
 //BA.debugLineNum = 520;BA.debugLine="lng = \"122.869168\"";
_lng = "122.869168";
 }else if(_brgy_index==0 && _street_index==1) { 
 //BA.debugLineNum = 522;BA.debugLine="lat = \"10.097226\"";
_lat = "10.097226";
 //BA.debugLineNum = 523;BA.debugLine="lng = \"122.870659\"";
_lng = "122.870659";
 }else if(_brgy_index==0 && _street_index==2) { 
 //BA.debugLineNum = 525;BA.debugLine="lat = \"10.097711\"";
_lat = "10.097711";
 //BA.debugLineNum = 526;BA.debugLine="lng = \"122.868378\"";
_lng = "122.868378";
 }else if(_brgy_index==0 && _street_index==3) { 
 //BA.debugLineNum = 528;BA.debugLine="lat = \"10.098293\"";
_lat = "10.098293";
 //BA.debugLineNum = 529;BA.debugLine="lng = \"122.868977\"";
_lng = "122.868977";
 }else if(_brgy_index==0 && _street_index==4) { 
 //BA.debugLineNum = 531;BA.debugLine="lat = \"10.097031\"";
_lat = "10.097031";
 //BA.debugLineNum = 532;BA.debugLine="lng = \"122.868764\"";
_lng = "122.868764";
 }else if(_brgy_index==0 && _street_index==5) { 
 //BA.debugLineNum = 534;BA.debugLine="lat = \"10.096021\"";
_lat = "10.096021";
 //BA.debugLineNum = 535;BA.debugLine="lng = \"122.869737\"";
_lng = "122.869737";
 }else if(_brgy_index==0 && _street_index==6) { 
 //BA.debugLineNum = 537;BA.debugLine="lat = \"10.095142\"";
_lat = "10.095142";
 //BA.debugLineNum = 538;BA.debugLine="lng = \"122.868317\"";
_lng = "122.868317";
 }else if(_brgy_index==0 && _street_index==7) { 
 //BA.debugLineNum = 540;BA.debugLine="lat = \"10.095303\"";
_lat = "10.095303";
 //BA.debugLineNum = 541;BA.debugLine="lng = \"122.869509\"";
_lng = "122.869509";
 };
 //BA.debugLineNum = 544;BA.debugLine="If brgy_index == 1 And street_index == 0 Then 'br";
if (_brgy_index==1 && _street_index==0) { 
 //BA.debugLineNum = 545;BA.debugLine="lat = \"10.101356\"";
_lat = "10.101356";
 //BA.debugLineNum = 546;BA.debugLine="lng = \"122.870075\"";
_lng = "122.870075";
 }else if(_brgy_index==1 && _street_index==1) { 
 //BA.debugLineNum = 548;BA.debugLine="lat = \"10.100583\"";
_lat = "10.100583";
 //BA.debugLineNum = 549;BA.debugLine="lng = \"122.870176\"";
_lng = "122.870176";
 }else if(_brgy_index==1 && _street_index==2) { 
 //BA.debugLineNum = 551;BA.debugLine="lat = \"10.100031\"";
_lat = "10.100031";
 //BA.debugLineNum = 552;BA.debugLine="lng = \"122.870623\"";
_lng = "122.870623";
 }else if(_brgy_index==1 && _street_index==3) { 
 //BA.debugLineNum = 554;BA.debugLine="lat = \"10.101327\"";
_lat = "10.101327";
 //BA.debugLineNum = 555;BA.debugLine="lng = \"122.871177\"";
_lng = "122.871177";
 }else if(_brgy_index==1 && _street_index==4) { 
 //BA.debugLineNum = 557;BA.debugLine="lat = \"10.103330\"";
_lat = "10.103330";
 //BA.debugLineNum = 558;BA.debugLine="lng = \"122.871391\"";
_lng = "122.871391";
 }else if(_brgy_index==1 && _street_index==5) { 
 //BA.debugLineNum = 560;BA.debugLine="lat = \"10.102317\"";
_lat = "10.102317";
 //BA.debugLineNum = 561;BA.debugLine="lng = \"122.870755\"";
_lng = "122.870755";
 }else if(_brgy_index==1 && _street_index==6) { 
 //BA.debugLineNum = 563;BA.debugLine="lat = \"10.104250\"";
_lat = "10.104250";
 //BA.debugLineNum = 564;BA.debugLine="lng = \"122.882834\"";
_lng = "122.882834";
 }else if(_brgy_index==1 && _street_index==7) { 
 //BA.debugLineNum = 566;BA.debugLine="lat = \"10.104943\"";
_lat = "10.104943";
 //BA.debugLineNum = 567;BA.debugLine="lng = \"122.885207\"";
_lng = "122.885207";
 }else if(_brgy_index==1 && _street_index==8) { 
 //BA.debugLineNum = 569;BA.debugLine="lat = \"10.101843\"";
_lat = "10.101843";
 //BA.debugLineNum = 570;BA.debugLine="lng = \"122.871020\"";
_lng = "122.871020";
 }else if(_brgy_index==1 && _street_index==9) { 
 //BA.debugLineNum = 572;BA.debugLine="lat = \"10.103477\"";
_lat = "10.103477";
 //BA.debugLineNum = 573;BA.debugLine="lng = \"122.870042\"";
_lng = "122.870042";
 }else if(_brgy_index==1 && _street_index==10) { 
 //BA.debugLineNum = 575;BA.debugLine="lat = \"10.100710\"";
_lat = "10.100710";
 //BA.debugLineNum = 576;BA.debugLine="lng = \"122.870889\"";
_lng = "122.870889";
 };
 //BA.debugLineNum = 579;BA.debugLine="If brgy_index == 2 And street_index == 0 Then 'br";
if (_brgy_index==2 && _street_index==0) { 
 //BA.debugLineNum = 580;BA.debugLine="lat = \"10.095478\"";
_lat = "10.095478";
 //BA.debugLineNum = 581;BA.debugLine="lng = \"122.871176\"";
_lng = "122.871176";
 }else if(_brgy_index==2 && _street_index==1) { 
 //BA.debugLineNum = 583;BA.debugLine="lat = \"10.098599\"";
_lat = "10.098599";
 //BA.debugLineNum = 584;BA.debugLine="lng = \"122.871761\"";
_lng = "122.871761";
 }else if(_brgy_index==2 && _street_index==2) { 
 //BA.debugLineNum = 586;BA.debugLine="lat = \"10.094573\"";
_lat = "10.094573";
 //BA.debugLineNum = 587;BA.debugLine="lng = \"122.870340\"";
_lng = "122.870340";
 }else if(_brgy_index==2 && _street_index==3) { 
 //BA.debugLineNum = 589;BA.debugLine="lat = \"10.098313\"";
_lat = "10.098313";
 //BA.debugLineNum = 590;BA.debugLine="lng = \"122.875223\"";
_lng = "122.875223";
 }else if(_brgy_index==2 && _street_index==4) { 
 //BA.debugLineNum = 592;BA.debugLine="lat = \"10.092235\"";
_lat = "10.092235";
 //BA.debugLineNum = 593;BA.debugLine="lng = \"122.874356\"";
_lng = "122.874356";
 }else if(_brgy_index==2 && _street_index==5) { 
 //BA.debugLineNum = 595;BA.debugLine="lat = \"10.103982\"";
_lat = "10.103982";
 //BA.debugLineNum = 596;BA.debugLine="lng = \"122.885996\"";
_lng = "122.885996";
 }else if(_brgy_index==2 && _street_index==6) { 
 //BA.debugLineNum = 598;BA.debugLine="lat = \"10.102170\"";
_lat = "10.102170";
 //BA.debugLineNum = 599;BA.debugLine="lng = \"122.882390\"";
_lng = "122.882390";
 }else if(_brgy_index==2 && _street_index==7) { 
 //BA.debugLineNum = 601;BA.debugLine="lat = \"10.103272\"";
_lat = "10.103272";
 //BA.debugLineNum = 602;BA.debugLine="lng = \"122.883948\"";
_lng = "122.883948";
 }else if(_brgy_index==2 && _street_index==8) { 
 //BA.debugLineNum = 604;BA.debugLine="lat = \"10.103849\"";
_lat = "10.103849";
 //BA.debugLineNum = 605;BA.debugLine="lng = \"122.884602\"";
_lng = "122.884602";
 }else if(_brgy_index==2 && _street_index==9) { 
 //BA.debugLineNum = 607;BA.debugLine="lat = \"10.101033\"";
_lat = "10.101033";
 //BA.debugLineNum = 608;BA.debugLine="lng = \"122.874480\"";
_lng = "122.874480";
 };
 //BA.debugLineNum = 611;BA.debugLine="If brgy_index == 3 And street_index == 0 Then 'b";
if (_brgy_index==3 && _street_index==0) { 
 //BA.debugLineNum = 612;BA.debugLine="lat = \"10.121855\"";
_lat = "10.121855";
 //BA.debugLineNum = 613;BA.debugLine="lng = \"122.872266\"";
_lng = "122.872266";
 }else if(_brgy_index==3 && _street_index==1) { 
 //BA.debugLineNum = 615;BA.debugLine="lat = \"10.116699\"";
_lat = "10.116699";
 //BA.debugLineNum = 616;BA.debugLine="lng = \"122.871783\"";
_lng = "122.871783";
 }else if(_brgy_index==3 && _street_index==2) { 
 //BA.debugLineNum = 618;BA.debugLine="lat = \"10.116024\"";
_lat = "10.116024";
 //BA.debugLineNum = 619;BA.debugLine="lng = \"122.872477\"";
_lng = "122.872477";
 }else if(_brgy_index==3 && _street_index==3) { 
 //BA.debugLineNum = 621;BA.debugLine="lat = \"10.114588\"";
_lat = "10.114588";
 //BA.debugLineNum = 622;BA.debugLine="lng = \"122.872515\"";
_lng = "122.872515";
 }else if(_brgy_index==3 && _street_index==4) { 
 //BA.debugLineNum = 624;BA.debugLine="lat = \"10.112140\"";
_lat = "10.112140";
 //BA.debugLineNum = 625;BA.debugLine="lng = \"122.872161\"";
_lng = "122.872161";
 }else if(_brgy_index==3 && _street_index==5) { 
 //BA.debugLineNum = 627;BA.debugLine="lat = \"10.111531\"";
_lat = "10.111531";
 //BA.debugLineNum = 628;BA.debugLine="lng = \"122.871542\"";
_lng = "122.871542";
 }else if(_brgy_index==3 && _street_index==6) { 
 //BA.debugLineNum = 630;BA.debugLine="lat = \"10.107168\"";
_lat = "10.107168";
 //BA.debugLineNum = 631;BA.debugLine="lng = \"122.871766\"";
_lng = "122.871766";
 }else if(_brgy_index==3 && _street_index==7) { 
 //BA.debugLineNum = 633;BA.debugLine="lat = \"10.106570\"";
_lat = "10.106570";
 //BA.debugLineNum = 634;BA.debugLine="lng = \"122.875197\"";
_lng = "122.875197";
 }else if(_brgy_index==3 && _street_index==8) { 
 //BA.debugLineNum = 636;BA.debugLine="lat = \"10.105759\"";
_lat = "10.105759";
 //BA.debugLineNum = 637;BA.debugLine="lng = \"122.871537\"";
_lng = "122.871537";
 };
 //BA.debugLineNum = 640;BA.debugLine="If brgy_index == 4 And street_index == 0 Then 'A";
if (_brgy_index==4 && _street_index==0) { 
 //BA.debugLineNum = 641;BA.debugLine="lat = \"10.165214\"";
_lat = "10.165214";
 //BA.debugLineNum = 642;BA.debugLine="lng = \"122.865433\"";
_lng = "122.865433";
 }else if(_brgy_index==4 && _street_index==1) { 
 //BA.debugLineNum = 644;BA.debugLine="lat = \"10.154170\"";
_lat = "10.154170";
 //BA.debugLineNum = 645;BA.debugLine="lng = \"122.867255\"";
_lng = "122.867255";
 }else if(_brgy_index==4 && _street_index==2) { 
 //BA.debugLineNum = 647;BA.debugLine="lat = \"10.161405\"";
_lat = "10.161405";
 //BA.debugLineNum = 648;BA.debugLine="lng = \"122.862692\"";
_lng = "122.862692";
 }else if(_brgy_index==4 && _street_index==3) { 
 //BA.debugLineNum = 650;BA.debugLine="lat = \"10.168471\"";
_lat = "10.168471";
 //BA.debugLineNum = 651;BA.debugLine="lng = \"122.860955\"";
_lng = "122.860955";
 }else if(_brgy_index==4 && _street_index==4) { 
 //BA.debugLineNum = 653;BA.debugLine="lat = \"10.172481\"";
_lat = "10.172481";
 //BA.debugLineNum = 654;BA.debugLine="lng = \"122.858629\"";
_lng = "122.858629";
 }else if(_brgy_index==4 && _street_index==5) { 
 //BA.debugLineNum = 656;BA.debugLine="lat = \"10.166561\"";
_lat = "10.166561";
 //BA.debugLineNum = 657;BA.debugLine="lng = \"122.859428\"";
_lng = "122.859428";
 }else if(_brgy_index==4 && _street_index==6) { 
 //BA.debugLineNum = 659;BA.debugLine="lat = \"10.163510\"";
_lat = "10.163510";
 //BA.debugLineNum = 660;BA.debugLine="lng = \"122.860074\"";
_lng = "122.860074";
 }else if(_brgy_index==4 && _street_index==7) { 
 //BA.debugLineNum = 662;BA.debugLine="lat = \"10.161033\"";
_lat = "10.161033";
 //BA.debugLineNum = 663;BA.debugLine="lng = \"122.859773\"";
_lng = "122.859773";
 }else if(_brgy_index==4 && _street_index==8) { 
 //BA.debugLineNum = 665;BA.debugLine="lat = \"10.159280\"";
_lat = "10.159280";
 //BA.debugLineNum = 666;BA.debugLine="lng = \"122.861621\"";
_lng = "122.861621";
 }else if(_brgy_index==4 && _street_index==9) { 
 //BA.debugLineNum = 668;BA.debugLine="lat = \"10.159062\"";
_lat = "10.159062";
 //BA.debugLineNum = 669;BA.debugLine="lng = \"122.860209\"";
_lng = "122.860209";
 }else if(_brgy_index==4 && _street_index==10) { 
 //BA.debugLineNum = 671;BA.debugLine="lat = \"10.181112\"";
_lat = "10.181112";
 //BA.debugLineNum = 672;BA.debugLine="lng = \"122.864670\"";
_lng = "122.864670";
 }else if(_brgy_index==4 && _street_index==11) { 
 //BA.debugLineNum = 674;BA.debugLine="lat = \"10.167295\"";
_lat = "10.167295";
 //BA.debugLineNum = 675;BA.debugLine="lng = \"122.857858\"";
_lng = "122.857858";
 };
 //BA.debugLineNum = 678;BA.debugLine="If brgy_index == 5 And street_index == 0 Then 'ca";
if (_brgy_index==5 && _street_index==0) { 
 //BA.debugLineNum = 679;BA.debugLine="lat = \"10.092993\"";
_lat = "10.092993";
 //BA.debugLineNum = 680;BA.debugLine="lng = \"122.861694\"";
_lng = "122.861694";
 }else if(_brgy_index==5 && _street_index==1) { 
 //BA.debugLineNum = 682;BA.debugLine="lat = \"10.090587\"";
_lat = "10.090587";
 //BA.debugLineNum = 683;BA.debugLine="lng = \"122.868414\"";
_lng = "122.868414";
 }else if(_brgy_index==5 && _street_index==2) { 
 //BA.debugLineNum = 685;BA.debugLine="lat = \"10.091551\"";
_lat = "10.091551";
 //BA.debugLineNum = 686;BA.debugLine="lng = \"122.869249\"";
_lng = "122.869249";
 }else if(_brgy_index==5 && _street_index==3) { 
 //BA.debugLineNum = 688;BA.debugLine="lat = \"10.086452\"";
_lat = "10.086452";
 //BA.debugLineNum = 689;BA.debugLine="lng = \"122.865742\"";
_lng = "122.865742";
 }else if(_brgy_index==5 && _street_index==4) { 
 //BA.debugLineNum = 691;BA.debugLine="lat = \"10.083507\"";
_lat = "10.083507";
 //BA.debugLineNum = 692;BA.debugLine="lng = \"122.858928\"";
_lng = "122.858928";
 }else if(_brgy_index==5 && _street_index==5) { 
 //BA.debugLineNum = 694;BA.debugLine="lat = \"10.077131\"";
_lat = "10.077131";
 //BA.debugLineNum = 695;BA.debugLine="lng = \"122.864236\"";
_lng = "122.864236";
 }else if(_brgy_index==5 && _street_index==6) { 
 //BA.debugLineNum = 697;BA.debugLine="lat = \"10.081722\"";
_lat = "10.081722";
 //BA.debugLineNum = 698;BA.debugLine="lng = \"122.882661\"";
_lng = "122.882661";
 }else if(_brgy_index==5 && _street_index==7) { 
 //BA.debugLineNum = 700;BA.debugLine="lat = \"10.081822\"";
_lat = "10.081822";
 //BA.debugLineNum = 701;BA.debugLine="lng = \"122.868295\"";
_lng = "122.868295";
 }else if(_brgy_index==5 && _street_index==8) { 
 //BA.debugLineNum = 703;BA.debugLine="lat = \"10.079513\"";
_lat = "10.079513";
 //BA.debugLineNum = 704;BA.debugLine="lng = \"122.876610\"";
_lng = "122.876610";
 }else if(_brgy_index==5 && _street_index==9) { 
 //BA.debugLineNum = 706;BA.debugLine="lat = \"10.068560\"";
_lat = "10.068560";
 //BA.debugLineNum = 707;BA.debugLine="lng = \"122.887366\"";
_lng = "122.887366";
 }else if(_brgy_index==5 && _street_index==10) { 
 //BA.debugLineNum = 709;BA.debugLine="lat = \"10.066934\"";
_lat = "10.066934";
 //BA.debugLineNum = 710;BA.debugLine="lng = \"122.871963\"";
_lng = "122.871963";
 }else if(_brgy_index==5 && _street_index==11) { 
 //BA.debugLineNum = 712;BA.debugLine="lat = \"10.064251\"";
_lat = "10.064251";
 //BA.debugLineNum = 713;BA.debugLine="lng = \"122.883023\"";
_lng = "122.883023";
 }else if(_brgy_index==5 && _street_index==12) { 
 //BA.debugLineNum = 715;BA.debugLine="lat = \"10.058546\"";
_lat = "10.058546";
 //BA.debugLineNum = 716;BA.debugLine="lng = \"122.882968\"";
_lng = "122.882968";
 }else if(_brgy_index==5 && _street_index==13) { 
 //BA.debugLineNum = 718;BA.debugLine="lat = \"10.054104\"";
_lat = "10.054104";
 //BA.debugLineNum = 719;BA.debugLine="lng = \"122.885506\"";
_lng = "122.885506";
 }else if(_brgy_index==5 && _street_index==14) { 
 //BA.debugLineNum = 721;BA.debugLine="lat = \"10.049464\"";
_lat = "10.049464";
 //BA.debugLineNum = 722;BA.debugLine="lng = \"122.885667\"";
_lng = "122.885667";
 }else if(_brgy_index==5 && _street_index==15) { 
 //BA.debugLineNum = 724;BA.debugLine="lat = \"10.041580\"";
_lat = "10.041580";
 //BA.debugLineNum = 725;BA.debugLine="lng = \"122.900269\"";
_lng = "122.900269";
 }else if(_brgy_index==5 && _street_index==16) { 
 //BA.debugLineNum = 727;BA.debugLine="lat = \"10.041395\"";
_lat = "10.041395";
 //BA.debugLineNum = 728;BA.debugLine="lng = \"122.906248\"";
_lng = "122.906248";
 };
 //BA.debugLineNum = 731;BA.debugLine="If brgy_index == 6 And street_index == 0 Then 'Bu";
if (_brgy_index==6 && _street_index==0) { 
 //BA.debugLineNum = 732;BA.debugLine="lat = \"10.035728\"";
_lat = "10.035728";
 //BA.debugLineNum = 733;BA.debugLine="lng = \"122.847547\"";
_lng = "122.847547";
 }else if(_brgy_index==6 && _street_index==1) { 
 //BA.debugLineNum = 735;BA.debugLine="lat = \"10.000603\"";
_lat = "10.000603";
 //BA.debugLineNum = 736;BA.debugLine="lng = \"122.885243\"";
_lng = "122.885243";
 }else if(_brgy_index==6 && _street_index==2) { 
 //BA.debugLineNum = 738;BA.debugLine="lat = \"10.000521\"";
_lat = "10.000521";
 //BA.debugLineNum = 739;BA.debugLine="lng = \"122.895867\"";
_lng = "122.895867";
 }else if(_brgy_index==6 && _street_index==3) { 
 //BA.debugLineNum = 741;BA.debugLine="lat = \"9.943276\"";
_lat = "9.943276";
 //BA.debugLineNum = 742;BA.debugLine="lng = \"122.975801\"";
_lng = "122.975801";
 };
 //BA.debugLineNum = 745;BA.debugLine="If brgy_index == 7 And street_index == 0 Then '";
if (_brgy_index==7 && _street_index==0) { 
 //BA.debugLineNum = 746;BA.debugLine="lat = \"10.156301\"";
_lat = "10.156301";
 //BA.debugLineNum = 747;BA.debugLine="lng = \"122.941207\"";
_lng = "122.941207";
 }else if(_brgy_index==7 && _street_index==1) { 
 //BA.debugLineNum = 749;BA.debugLine="lat = \"10.142692\"";
_lat = "10.142692";
 //BA.debugLineNum = 750;BA.debugLine="lng = \"122.947560\"";
_lng = "122.947560";
 }else if(_brgy_index==7 && _street_index==2) { 
 //BA.debugLineNum = 752;BA.debugLine="lat = \"10.139494\"";
_lat = "10.139494";
 //BA.debugLineNum = 753;BA.debugLine="lng = \"122.942788\"";
_lng = "122.942788";
 }else if(_brgy_index==7 && _street_index==3) { 
 //BA.debugLineNum = 755;BA.debugLine="lat = \"10.110265\"";
_lat = "10.110265";
 //BA.debugLineNum = 756;BA.debugLine="lng = \"122.947908\"";
_lng = "122.947908";
 }else if(_brgy_index==7 && _street_index==4) { 
 //BA.debugLineNum = 758;BA.debugLine="lat = \"10.127828\"";
_lat = "10.127828";
 //BA.debugLineNum = 759;BA.debugLine="lng = \"122.950197\"";
_lng = "122.950197";
 }else if(_brgy_index==7 && _street_index==5) { 
 //BA.debugLineNum = 761;BA.debugLine="lat = \"10.125287\"";
_lat = "10.125287";
 //BA.debugLineNum = 762;BA.debugLine="lng = \"122.945735\"";
_lng = "122.945735";
 }else if(_brgy_index==7 && _street_index==6) { 
 //BA.debugLineNum = 764;BA.debugLine="lat = \"10.143975\"";
_lat = "10.143975";
 //BA.debugLineNum = 765;BA.debugLine="lng = \"122.930610\"";
_lng = "122.930610";
 }else if(_brgy_index==7 && _street_index==7) { 
 //BA.debugLineNum = 767;BA.debugLine="lat = \"10.137563\"";
_lat = "10.137563";
 //BA.debugLineNum = 768;BA.debugLine="lng = \"122.939870\"";
_lng = "122.939870";
 }else if(_brgy_index==7 && _street_index==8) { 
 //BA.debugLineNum = 770;BA.debugLine="lat = \"10.150449\"";
_lat = "10.150449";
 //BA.debugLineNum = 771;BA.debugLine="lng = \"122.933761\"";
_lng = "122.933761";
 }else if(_brgy_index==7 && _street_index==9) { 
 //BA.debugLineNum = 773;BA.debugLine="lat = \"10.150286\"";
_lat = "10.150286";
 //BA.debugLineNum = 774;BA.debugLine="lng = \"122.948956\"";
_lng = "122.948956";
 }else if(_brgy_index==7 && _street_index==10) { 
 //BA.debugLineNum = 776;BA.debugLine="lat = \"10.148481\"";
_lat = "10.148481";
 //BA.debugLineNum = 777;BA.debugLine="lng = \"122.943230\"";
_lng = "122.943230";
 }else if(_brgy_index==7 && _street_index==11) { 
 //BA.debugLineNum = 779;BA.debugLine="lat = \"10.106200\"";
_lat = "10.106200";
 //BA.debugLineNum = 780;BA.debugLine="lng = \"122.948051\"";
_lng = "122.948051";
 }else if(_brgy_index==7 && _street_index==12) { 
 //BA.debugLineNum = 782;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 783;BA.debugLine="lng = \"122.926593\"";
_lng = "122.926593";
 }else if(_brgy_index==7 && _street_index==13) { 
 //BA.debugLineNum = 785;BA.debugLine="lat = \"10.120798\"";
_lat = "10.120798";
 //BA.debugLineNum = 786;BA.debugLine="lng = \"122.938371\"";
_lng = "122.938371";
 }else if(_brgy_index==7 && _street_index==14) { 
 //BA.debugLineNum = 788;BA.debugLine="lat = \"10.153217\"";
_lat = "10.153217";
 //BA.debugLineNum = 789;BA.debugLine="lng = \"122.951714\"";
_lng = "122.951714";
 };
 //BA.debugLineNum = 792;BA.debugLine="If brgy_index == 8 And street_index == 0 Then";
if (_brgy_index==8 && _street_index==0) { 
 //BA.debugLineNum = 793;BA.debugLine="lat = \"10.157177\"";
_lat = "10.157177";
 //BA.debugLineNum = 794;BA.debugLine="lng = \"122.895986\"";
_lng = "122.895986";
 }else if(_brgy_index==8 && _street_index==1) { 
 //BA.debugLineNum = 796;BA.debugLine="lat = \"10.180004\"";
_lat = "10.180004";
 //BA.debugLineNum = 797;BA.debugLine="lng = \"122.897999\"";
_lng = "122.897999";
 }else if(_brgy_index==8 && _street_index==2) { 
 //BA.debugLineNum = 799;BA.debugLine="lat = \"10.192848\"";
_lat = "10.192848";
 //BA.debugLineNum = 800;BA.debugLine="lng = \"122.900234\"";
_lng = "122.900234";
 }else if(_brgy_index==8 && _street_index==3) { 
 //BA.debugLineNum = 802;BA.debugLine="lat = \"10.179993\"";
_lat = "10.179993";
 //BA.debugLineNum = 803;BA.debugLine="lng = \"122.904299\"";
_lng = "122.904299";
 }else if(_brgy_index==8 && _street_index==4) { 
 //BA.debugLineNum = 805;BA.debugLine="lat = \"10.183439\"";
_lat = "10.183439";
 //BA.debugLineNum = 806;BA.debugLine="lng = \"122.889622\"";
_lng = "122.889622";
 };
 //BA.debugLineNum = 809;BA.debugLine="If brgy_index == 9 And street_index == 0 Then 'Ca";
if (_brgy_index==9 && _street_index==0) { 
 //BA.debugLineNum = 810;BA.debugLine="lat = \"10.074128\"";
_lat = "10.074128";
 //BA.debugLineNum = 811;BA.debugLine="lng = \"122.981978\"";
_lng = "122.981978";
 }else if(_brgy_index==9 && _street_index==1) { 
 //BA.debugLineNum = 813;BA.debugLine="lat = \"10.109208\"";
_lat = "10.109208";
 //BA.debugLineNum = 814;BA.debugLine="lng = \"122.896717\"";
_lng = "122.896717";
 }else if(_brgy_index==9 && _street_index==2) { 
 //BA.debugLineNum = 816;BA.debugLine="lat = \"10.097119\"";
_lat = "10.097119";
 //BA.debugLineNum = 817;BA.debugLine="lng = \"122.947066\"";
_lng = "122.947066";
 }else if(_brgy_index==9 && _street_index==3) { 
 //BA.debugLineNum = 819;BA.debugLine="lat = \"10.099023\"";
_lat = "10.099023";
 //BA.debugLineNum = 820;BA.debugLine="lng = \"122.971723\"";
_lng = "122.971723";
 }else if(_brgy_index==9 && _street_index==4) { 
 //BA.debugLineNum = 822;BA.debugLine="lat = \"10.119761\"";
_lat = "10.119761";
 //BA.debugLineNum = 823;BA.debugLine="lng = \"122.901613\"";
_lng = "122.901613";
 }else if(_brgy_index==9 && _street_index==5) { 
 //BA.debugLineNum = 825;BA.debugLine="lat = \"10.099402\"";
_lat = "10.099402";
 //BA.debugLineNum = 826;BA.debugLine="lng = \"122.896454\"";
_lng = "122.896454";
 }else if(_brgy_index==9 && _street_index==6) { 
 //BA.debugLineNum = 828;BA.debugLine="lat = \"10.097102\"";
_lat = "10.097102";
 //BA.debugLineNum = 829;BA.debugLine="lng = \"122.922368\"";
_lng = "122.922368";
 }else if(_brgy_index==9 && _street_index==7) { 
 //BA.debugLineNum = 831;BA.debugLine="lat = \"10.095304\"";
_lat = "10.095304";
 //BA.debugLineNum = 832;BA.debugLine="lng = \"122.929242\"";
_lng = "122.929242";
 }else if(_brgy_index==9 && _street_index==8) { 
 //BA.debugLineNum = 834;BA.debugLine="lat = \"10.114128\"";
_lat = "10.114128";
 //BA.debugLineNum = 835;BA.debugLine="lng = \"122.893868\"";
_lng = "122.893868";
 };
 //BA.debugLineNum = 838;BA.debugLine="If brgy_index == 10 And street_index == 0 Then 'L";
if (_brgy_index==10 && _street_index==0) { 
 //BA.debugLineNum = 839;BA.debugLine="lat = \"10.1799469\"";
_lat = "10.1799469";
 //BA.debugLineNum = 840;BA.debugLine="lng = \"122.9068577\"";
_lng = "122.9068577";
 }else if(_brgy_index==10 && _street_index==1) { 
 //BA.debugLineNum = 842;BA.debugLine="lat = \"10.180524\"";
_lat = "10.180524";
 //BA.debugLineNum = 843;BA.debugLine="lng = \"122.906798\"";
_lng = "122.906798";
 }else if(_brgy_index==10 && _street_index==2) { 
 //BA.debugLineNum = 845;BA.debugLine="lat = \"10.173336\"";
_lat = "10.173336";
 //BA.debugLineNum = 846;BA.debugLine="lng = \"122.9118842\"";
_lng = "122.9118842";
 }else if(_brgy_index==10 && _street_index==3) { 
 //BA.debugLineNum = 848;BA.debugLine="lat = \"10.177359\"";
_lat = "10.177359";
 //BA.debugLineNum = 849;BA.debugLine="lng = \"122.913033\"";
_lng = "122.913033";
 }else if(_brgy_index==10 && _street_index==4) { 
 //BA.debugLineNum = 851;BA.debugLine="lat = \"10.179847\"";
_lat = "10.179847";
 //BA.debugLineNum = 852;BA.debugLine="lng = \"122.914160\"";
_lng = "122.914160";
 }else if(_brgy_index==10 && _street_index==5) { 
 //BA.debugLineNum = 854;BA.debugLine="lat = \"10.182718\"";
_lat = "10.182718";
 //BA.debugLineNum = 855;BA.debugLine="lng = \"122.915228\"";
_lng = "122.915228";
 }else if(_brgy_index==10 && _street_index==6) { 
 //BA.debugLineNum = 857;BA.debugLine="lat = \"10.186454\"";
_lat = "10.186454";
 //BA.debugLineNum = 858;BA.debugLine="lng = \"122.916278\"";
_lng = "122.916278";
 }else if(_brgy_index==10 && _street_index==7) { 
 //BA.debugLineNum = 860;BA.debugLine="lat = \"10.168057\"";
_lat = "10.168057";
 //BA.debugLineNum = 861;BA.debugLine="lng = \"122.924501\"";
_lng = "122.924501";
 };
 //BA.debugLineNum = 864;BA.debugLine="If brgy_index == 11 And street_index == 0 Then 'M";
if (_brgy_index==11 && _street_index==0) { 
 //BA.debugLineNum = 865;BA.debugLine="lat = \"10.050418\"";
_lat = "10.050418";
 //BA.debugLineNum = 866;BA.debugLine="lng = \"122.867097\"";
_lng = "122.867097";
 }else if(_brgy_index==11 && _street_index==1) { 
 //BA.debugLineNum = 868;BA.debugLine="lat = \"10.027855\"";
_lat = "10.027855";
 //BA.debugLineNum = 869;BA.debugLine="lng = \"122.906833\"";
_lng = "122.906833";
 }else if(_brgy_index==11 && _street_index==2) { 
 //BA.debugLineNum = 871;BA.debugLine="lat = \"10.027522\"";
_lat = "10.027522";
 //BA.debugLineNum = 872;BA.debugLine="lng = \"122.876637\"";
_lng = "122.876637";
 }else if(_brgy_index==11 && _street_index==3) { 
 //BA.debugLineNum = 874;BA.debugLine="lat = \"10.017254\"";
_lat = "10.017254";
 //BA.debugLineNum = 875;BA.debugLine="lng = \"122.900969\"";
_lng = "122.900969";
 }else if(_brgy_index==11 && _street_index==4) { 
 //BA.debugLineNum = 877;BA.debugLine="lat = \"10.028535\"";
_lat = "10.028535";
 //BA.debugLineNum = 878;BA.debugLine="lng = \"122.900364\"";
_lng = "122.900364";
 }else if(_brgy_index==11 && _street_index==5) { 
 //BA.debugLineNum = 880;BA.debugLine="lat = \"10.025485\"";
_lat = "10.025485";
 //BA.debugLineNum = 881;BA.debugLine="lng = \"122.890023\"";
_lng = "122.890023";
 };
 //BA.debugLineNum = 884;BA.debugLine="If brgy_index == 12 And street_index == 0 Then 'M";
if (_brgy_index==12 && _street_index==0) { 
 //BA.debugLineNum = 885;BA.debugLine="lat = \"10.137572\"";
_lat = "10.137572";
 //BA.debugLineNum = 886;BA.debugLine="lng = \"122.939888\"";
_lng = "122.939888";
 }else if(_brgy_index==12 && _street_index==1) { 
 //BA.debugLineNum = 888;BA.debugLine="lat = \"10.132195\"";
_lat = "10.132195";
 //BA.debugLineNum = 889;BA.debugLine="lng = \"122.899837\"";
_lng = "122.899837";
 }else if(_brgy_index==12 && _street_index==2) { 
 //BA.debugLineNum = 891;BA.debugLine="lat = \"10.123430\"";
_lat = "10.123430";
 //BA.debugLineNum = 892;BA.debugLine="lng = \"122.892250\"";
_lng = "122.892250";
 }else if(_brgy_index==12 && _street_index==3) { 
 //BA.debugLineNum = 894;BA.debugLine="lat = \"10.130383\"";
_lat = "10.130383";
 //BA.debugLineNum = 895;BA.debugLine="lng = \"122.893010\"";
_lng = "122.893010";
 }else if(_brgy_index==12 && _street_index==4) { 
 //BA.debugLineNum = 897;BA.debugLine="lat = \"10.123127\"";
_lat = "10.123127";
 //BA.debugLineNum = 898;BA.debugLine="lng = \"122.887952\"";
_lng = "122.887952";
 }else if(_brgy_index==12 && _street_index==5) { 
 //BA.debugLineNum = 900;BA.debugLine="lat = \"10.131098\"";
_lat = "10.131098";
 //BA.debugLineNum = 901;BA.debugLine="lng = \"122.879801\"";
_lng = "122.879801";
 }else if(_brgy_index==12 && _street_index==6) { 
 //BA.debugLineNum = 903;BA.debugLine="lat = \"10.137485\"";
_lat = "10.137485";
 //BA.debugLineNum = 904;BA.debugLine="lng = \"122.911434\"";
_lng = "122.911434";
 }else if(_brgy_index==12 && _street_index==7) { 
 //BA.debugLineNum = 906;BA.debugLine="lat = \"10.106803\"";
_lat = "10.106803";
 //BA.debugLineNum = 907;BA.debugLine="lng = \"122.885727\"";
_lng = "122.885727";
 }else if(_brgy_index==12 && _street_index==8) { 
 //BA.debugLineNum = 909;BA.debugLine="lat = \"10.115220\"";
_lat = "10.115220";
 //BA.debugLineNum = 910;BA.debugLine="lng = \"122.890515\"";
_lng = "122.890515";
 }else if(_brgy_index==12 && _street_index==9) { 
 //BA.debugLineNum = 912;BA.debugLine="lat = \"10.108754\"";
_lat = "10.108754";
 //BA.debugLineNum = 913;BA.debugLine="lng = \"122.894130\"";
_lng = "122.894130";
 }else if(_brgy_index==12 && _street_index==10) { 
 //BA.debugLineNum = 915;BA.debugLine="lat = \"10.149506\"";
_lat = "10.149506";
 //BA.debugLineNum = 916;BA.debugLine="lng = \"122.897389\"";
_lng = "122.897389";
 }else if(_brgy_index==12 && _street_index==11) { 
 //BA.debugLineNum = 918;BA.debugLine="lat = \"10.122215\"";
_lat = "10.122215";
 //BA.debugLineNum = 919;BA.debugLine="lng = \"122.892160\"";
_lng = "122.892160";
 }else if(_brgy_index==12 && _street_index==12) { 
 //BA.debugLineNum = 921;BA.debugLine="lat = \"10.142698\"";
_lat = "10.142698";
 //BA.debugLineNum = 922;BA.debugLine="lng = \"122.898168\"";
_lng = "122.898168";
 };
 //BA.debugLineNum = 925;BA.debugLine="If brgy_index == 13 And street_index == 0 Then 'N";
if (_brgy_index==13 && _street_index==0) { 
 //BA.debugLineNum = 926;BA.debugLine="lat = \"10.161629\"";
_lat = "10.161629";
 //BA.debugLineNum = 927;BA.debugLine="lng = \"122.872772\"";
_lng = "122.872772";
 }else if(_brgy_index==13 && _street_index==1) { 
 //BA.debugLineNum = 929;BA.debugLine="lat = \"10.161863\"";
_lat = "10.161863";
 //BA.debugLineNum = 930;BA.debugLine="lng = \"122.876192\"";
_lng = "122.876192";
 }else if(_brgy_index==13 && _street_index==2) { 
 //BA.debugLineNum = 932;BA.debugLine="lat = \"10.157407\"";
_lat = "10.157407";
 //BA.debugLineNum = 933;BA.debugLine="lng = \"122.885663\"";
_lng = "122.885663";
 }else if(_brgy_index==13 && _street_index==3) { 
 //BA.debugLineNum = 935;BA.debugLine="lat = \"10.167497\"";
_lat = "10.167497";
 //BA.debugLineNum = 936;BA.debugLine="lng = \"122.879777\"";
_lng = "122.879777";
 }else if(_brgy_index==13 && _street_index==4) { 
 //BA.debugLineNum = 938;BA.debugLine="lat = \"10.176260\"";
_lat = "10.176260";
 //BA.debugLineNum = 939;BA.debugLine="lng = \"122.880815\"";
_lng = "122.880815";
 }else if(_brgy_index==13 && _street_index==5) { 
 //BA.debugLineNum = 941;BA.debugLine="lat = \"10.170524\"";
_lat = "10.170524";
 //BA.debugLineNum = 942;BA.debugLine="lng = \"122.883603\"";
_lng = "122.883603";
 };
 //BA.debugLineNum = 945;BA.debugLine="If brgy_index == 14 And street_index == 0 Then 'S";
if (_brgy_index==14 && _street_index==0) { 
 //BA.debugLineNum = 946;BA.debugLine="lat = \"10.071514\"";
_lat = "10.071514";
 //BA.debugLineNum = 947;BA.debugLine="lng = \"122.916010\"";
_lng = "122.916010";
 }else if(_brgy_index==14 && _street_index==1) { 
 //BA.debugLineNum = 949;BA.debugLine="lat = \"10.069622\"";
_lat = "10.069622";
 //BA.debugLineNum = 950;BA.debugLine="lng = \"122.909890\"";
_lng = "122.909890";
 }else if(_brgy_index==14 && _street_index==2) { 
 //BA.debugLineNum = 952;BA.debugLine="lat = \"10.076890\"";
_lat = "10.076890";
 //BA.debugLineNum = 953;BA.debugLine="lng = \"122.894231\"";
_lng = "122.894231";
 }else if(_brgy_index==14 && _street_index==3) { 
 //BA.debugLineNum = 955;BA.debugLine="lat = \"10.086207\"";
_lat = "10.086207";
 //BA.debugLineNum = 956;BA.debugLine="lng = \"122.914044\"";
_lng = "122.914044";
 }else if(_brgy_index==14 && _street_index==4) { 
 //BA.debugLineNum = 958;BA.debugLine="lat = \"10.067393\"";
_lat = "10.067393";
 //BA.debugLineNum = 959;BA.debugLine="lng = \"122.900935\"";
_lng = "122.900935";
 }else if(_brgy_index==14 && _street_index==5) { 
 //BA.debugLineNum = 961;BA.debugLine="lat = \"10.071900\"";
_lat = "10.071900";
 //BA.debugLineNum = 962;BA.debugLine="lng = \"122.906250\"";
_lng = "122.906250";
 }else if(_brgy_index==14 && _street_index==6) { 
 //BA.debugLineNum = 964;BA.debugLine="lat = \"10.061702\"";
_lat = "10.061702";
 //BA.debugLineNum = 965;BA.debugLine="lng = \"122.896226\"";
_lng = "122.896226";
 }else if(_brgy_index==14 && _street_index==7) { 
 //BA.debugLineNum = 967;BA.debugLine="lat = \"10.054802\"";
_lat = "10.054802";
 //BA.debugLineNum = 968;BA.debugLine="lng = \"122.938688\"";
_lng = "122.938688";
 }else if(_brgy_index==14 && _street_index==8) { 
 //BA.debugLineNum = 970;BA.debugLine="lat = \"10.071827\"";
_lat = "10.071827";
 //BA.debugLineNum = 971;BA.debugLine="lng = \"122.921092\"";
_lng = "122.921092";
 }else if(_brgy_index==14 && _street_index==9) { 
 //BA.debugLineNum = 973;BA.debugLine="lat = \"10.050849\"";
_lat = "10.050849";
 //BA.debugLineNum = 974;BA.debugLine="lng = \"122.907632\"";
_lng = "122.907632";
 };
 //BA.debugLineNum = 977;BA.debugLine="If brgy_index == 15 And street_index == 0 Then 'S";
if (_brgy_index==15 && _street_index==0) { 
 //BA.debugLineNum = 978;BA.debugLine="lat = \"10.155844\"";
_lat = "10.155844";
 //BA.debugLineNum = 979;BA.debugLine="lng = \"122.861129\"";
_lng = "122.861129";
 }else if(_brgy_index==15 && _street_index==1) { 
 //BA.debugLineNum = 981;BA.debugLine="lat = \"10.152073\"";
_lat = "10.152073";
 //BA.debugLineNum = 982;BA.debugLine="lng = \"122.861669\"";
_lng = "122.861669";
 }else if(_brgy_index==15 && _street_index==2) { 
 //BA.debugLineNum = 984;BA.debugLine="lat = \"10.147663\"";
_lat = "10.147663";
 //BA.debugLineNum = 985;BA.debugLine="lng = \"122.862471\"";
_lng = "122.862471";
 }else if(_brgy_index==15 && _street_index==3) { 
 //BA.debugLineNum = 987;BA.debugLine="lat = \"10.144440\"";
_lat = "10.144440";
 //BA.debugLineNum = 988;BA.debugLine="lng = \"122.862524\"";
_lng = "122.862524";
 };
 //BA.debugLineNum = 991;BA.debugLine="If brgy_index == 16 And street_index == 0 Then 'S";
if (_brgy_index==16 && _street_index==0) { 
 //BA.debugLineNum = 992;BA.debugLine="lat = \"10.053680\"";
_lat = "10.053680";
 //BA.debugLineNum = 993;BA.debugLine="lng = \"122.843876\"";
_lng = "122.843876";
 }else if(_brgy_index==16 && _street_index==1) { 
 //BA.debugLineNum = 995;BA.debugLine="lat = \"10.055961\"";
_lat = "10.055961";
 //BA.debugLineNum = 996;BA.debugLine="lng = \"122.841980\"";
_lng = "122.841980";
 }else if(_brgy_index==16 && _street_index==2) { 
 //BA.debugLineNum = 998;BA.debugLine="lat = \"10.053363\"";
_lat = "10.053363";
 //BA.debugLineNum = 999;BA.debugLine="lng = \"122.843295\"";
_lng = "122.843295";
 }else if(_brgy_index==16 && _street_index==3) { 
 //BA.debugLineNum = 1001;BA.debugLine="lat = \"10.053032\"";
_lat = "10.053032";
 //BA.debugLineNum = 1002;BA.debugLine="lng = \"122.842594\"";
_lng = "122.842594";
 }else if(_brgy_index==16 && _street_index==4) { 
 //BA.debugLineNum = 1004;BA.debugLine="lat = \"10.052328\"";
_lat = "10.052328";
 //BA.debugLineNum = 1005;BA.debugLine="lng = \"122.842835\"";
_lng = "122.842835";
 }else if(_brgy_index==16 && _street_index==5) { 
 //BA.debugLineNum = 1007;BA.debugLine="lat = \"10.052573\"";
_lat = "10.052573";
 //BA.debugLineNum = 1008;BA.debugLine="lng = \"122.844229\"";
_lng = "122.844229";
 }else if(_brgy_index==16 && _street_index==6) { 
 //BA.debugLineNum = 1010;BA.debugLine="lat = \"10.046957\"";
_lat = "10.046957";
 //BA.debugLineNum = 1011;BA.debugLine="lng = \"122.839610\"";
_lng = "122.839610";
 }else if(_brgy_index==16 && _street_index==7) { 
 //BA.debugLineNum = 1013;BA.debugLine="lat = \"10.035813\"";
_lat = "10.035813";
 //BA.debugLineNum = 1014;BA.debugLine="lng = \"122.835364\"";
_lng = "122.835364";
 };
 //BA.debugLineNum = 1017;BA.debugLine="If brgy_index == 17 And street_index == 0 Then 'T";
if (_brgy_index==17 && _street_index==0) { 
 //BA.debugLineNum = 1018;BA.debugLine="lat = \"10.148233\"";
_lat = "10.148233";
 //BA.debugLineNum = 1019;BA.debugLine="lng = \"122.869741\"";
_lng = "122.869741";
 }else if(_brgy_index==17 && _street_index==1) { 
 //BA.debugLineNum = 1021;BA.debugLine="lat = \"10.139867\"";
_lat = "10.139867";
 //BA.debugLineNum = 1022;BA.debugLine="lng = \"122.869882\"";
_lng = "122.869882";
 }else if(_brgy_index==17 && _street_index==2) { 
 //BA.debugLineNum = 1024;BA.debugLine="lat = \"10.126453\"";
_lat = "10.126453";
 //BA.debugLineNum = 1025;BA.debugLine="lng = \"122.868927\"";
_lng = "122.868927";
 }else if(_brgy_index==17 && _street_index==3) { 
 //BA.debugLineNum = 1027;BA.debugLine="lat = \"10.127470\"";
_lat = "10.127470";
 //BA.debugLineNum = 1028;BA.debugLine="lng = \"122.862942\"";
_lng = "122.862942";
 }else if(_brgy_index==17 && _street_index==4) { 
 //BA.debugLineNum = 1030;BA.debugLine="lat = \"10.117998\"";
_lat = "10.117998";
 //BA.debugLineNum = 1031;BA.debugLine="lng = \"122.866817\"";
_lng = "122.866817";
 }else if(_brgy_index==17 && _street_index==5) { 
 //BA.debugLineNum = 1033;BA.debugLine="lat = \"10.108173\"";
_lat = "10.108173";
 //BA.debugLineNum = 1034;BA.debugLine="lng = \"122.864592\"";
_lng = "122.864592";
 }else if(_brgy_index==17 && _street_index==6) { 
 //BA.debugLineNum = 1036;BA.debugLine="lat = \"10.126115\"";
_lat = "10.126115";
 //BA.debugLineNum = 1037;BA.debugLine="lng = \"122.871073\"";
_lng = "122.871073";
 }else if(_brgy_index==17 && _street_index==7) { 
 //BA.debugLineNum = 1039;BA.debugLine="lat = \"10.129412\"";
_lat = "10.129412";
 //BA.debugLineNum = 1040;BA.debugLine="lng = \"122.869408\"";
_lng = "122.869408";
 }else if(_brgy_index==17 && _street_index==8) { 
 //BA.debugLineNum = 1042;BA.debugLine="lat = \"10.134647\"";
_lat = "10.134647";
 //BA.debugLineNum = 1043;BA.debugLine="lng = \"122.871841\"";
_lng = "122.871841";
 }else if(_brgy_index==17 && _street_index==9) { 
 //BA.debugLineNum = 1045;BA.debugLine="lat = \"10.124801\"";
_lat = "10.124801";
 //BA.debugLineNum = 1046;BA.debugLine="lng = \"122.868277\"";
_lng = "122.868277";
 }else if(_brgy_index==17 && _street_index==10) { 
 //BA.debugLineNum = 1048;BA.debugLine="lat = \"10.124422\"";
_lat = "10.124422";
 //BA.debugLineNum = 1049;BA.debugLine="lng = \"122.866917\"";
_lng = "122.866917";
 };
 //BA.debugLineNum = 1052;BA.debugLine="If brgy_index == 18 And street_index == 0 Then 'T";
if (_brgy_index==18 && _street_index==0) { 
 //BA.debugLineNum = 1053;BA.debugLine="lat = \"10.065086\"";
_lat = "10.065086";
 //BA.debugLineNum = 1054;BA.debugLine="lng = \"122.843793\"";
_lng = "122.843793";
 }else if(_brgy_index==18 && _street_index==1) { 
 //BA.debugLineNum = 1056;BA.debugLine="lat = \"10.071356\"";
_lat = "10.071356";
 //BA.debugLineNum = 1057;BA.debugLine="lng = \"122.853102\"";
_lng = "122.853102";
 }else if(_brgy_index==18 && _street_index==2) { 
 //BA.debugLineNum = 1059;BA.debugLine="lat = \"10.060206\"";
_lat = "10.060206";
 //BA.debugLineNum = 1060;BA.debugLine="lng = \"122.850172\"";
_lng = "122.850172";
 }else if(_brgy_index==18 && _street_index==3) { 
 //BA.debugLineNum = 1062;BA.debugLine="lat = \"10.057640\"";
_lat = "10.057640";
 //BA.debugLineNum = 1063;BA.debugLine="lng = \"122.859242\"";
_lng = "122.859242";
 };
 //BA.debugLineNum = 1069;BA.debugLine="End Sub";
return "";
}
}
