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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "bloodlife.system", "bloodlife.system.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "bloodlife.system", "bloodlife.system.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "bloodlife.system.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (main) Resume **");
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
public static anywheresoftware.b4a.sql.SQL _sqllite = null;
public anywheresoftware.b4a.objects.PanelWrapper _ban_panel = null;
public anywheresoftware.b4a.objects.PanelWrapper _but_panel = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ban_picture = null;
public anywheresoftware.b4a.objects.ButtonWrapper _start_button = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _main_tittle = null;
public bloodlife.system.login_form _login_form = null;
public bloodlife.system.create_account _create_account = null;
public bloodlife.system.menu_form _menu_form = null;
public bloodlife.system.search_frame _search_frame = null;
public bloodlife.system.httputils2service _httputils2service = null;
public bloodlife.system.help_frame _help_frame = null;
public bloodlife.system.my_profile _my_profile = null;
public bloodlife.system.about_frame _about_frame = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (login_form.mostCurrent != null);
vis = vis | (create_account.mostCurrent != null);
vis = vis | (menu_form.mostCurrent != null);
vis = vis | (search_frame.mostCurrent != null);
vis = vis | (help_frame.mostCurrent != null);
vis = vis | (my_profile.mostCurrent != null);
vis = vis | (about_frame.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 36;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 38;BA.debugLine="Activity.LoadLayout(\"load_form\")";
mostCurrent._activity.LoadLayout("load_form",mostCurrent.activityBA);
 //BA.debugLineNum = 39;BA.debugLine="Activity.Title = \"MAIN\"";
mostCurrent._activity.setTitle((Object)("MAIN"));
 //BA.debugLineNum = 41;BA.debugLine="all_settings_layout";
_all_settings_layout();
 //BA.debugLineNum = 43;BA.debugLine="for_btn_animation";
_for_btn_animation();
 //BA.debugLineNum = 44;BA.debugLine="If sqlLite.IsInitialized = False Then";
if (_sqllite.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 45;BA.debugLine="sqlLite.Initialize(File.DirInternal, \"mydb.db\",";
_sqllite.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db",anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _choose = 0;
 //BA.debugLineNum = 82;BA.debugLine="Sub Activity_KeyPress(KeyCode As Int) As Boolean";
 //BA.debugLineNum = 84;BA.debugLine="If KeyCode == KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 85;BA.debugLine="Dim choose As Int : choose = Msgbox2(\"would you";
_choose = 0;
 //BA.debugLineNum = 85;BA.debugLine="Dim choose As Int : choose = Msgbox2(\"would you";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2("would you like to exit this application?","C O N F I R M A T I O N","YES","","NO",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 86;BA.debugLine="If choose == DialogResponse.POSITIVE Then";
if (_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 87;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 };
 };
 //BA.debugLineNum = 92;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 94;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _set_cursor = null;
int _i = 0;
 //BA.debugLineNum = 64;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 65;BA.debugLine="If sqlLite.IsInitialized = True Then";
if (_sqllite.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 66;BA.debugLine="Dim set_cursor As Cursor";
_set_cursor = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 67;BA.debugLine="set_cursor = sqlLite.ExecQuery(\"select * from da";
_set_cursor.setObject((android.database.Cursor)(_sqllite.ExecQuery("select * from data where `id`=1;")));
 //BA.debugLineNum = 68;BA.debugLine="For i = 0 To set_cursor.RowCount - 1";
{
final int step4 = 1;
final int limit4 = (int) (_set_cursor.getRowCount()-1);
for (_i = (int) (0) ; (step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4); _i = ((int)(0 + _i + step4)) ) {
 //BA.debugLineNum = 69;BA.debugLine="set_cursor.Position = i";
_set_cursor.setPosition(_i);
 //BA.debugLineNum = 70;BA.debugLine="If set_cursor.GetString(\"isStart\") == 1 The";
if ((_set_cursor.GetString("isStart")).equals(BA.NumberToString(1))) { 
 //BA.debugLineNum = 71;BA.debugLine="StartActivity(\"login_form\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("login_form"));
 //BA.debugLineNum = 72;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 }else {
 };
 }
};
 };
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _all_settings_layout() throws Exception{
anywheresoftware.b4a.objects.drawable.GradientDrawable _gradiant = null;
int[] _col = null;
 //BA.debugLineNum = 107;BA.debugLine="Public Sub all_settings_layout";
 //BA.debugLineNum = 108;BA.debugLine="ban_picture.SetBackgroundImage(LoadBitmap(File.Di";
mostCurrent._ban_picture.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner01.jpg").getObject()));
 //BA.debugLineNum = 109;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 110;BA.debugLine="but_panel.Color = Colors.Transparent";
mostCurrent._but_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 112;BA.debugLine="ban_panel.Width = 100%x";
mostCurrent._ban_panel.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 113;BA.debugLine="ban_picture.Width = ban_panel.Width";
mostCurrent._ban_picture.setWidth(mostCurrent._ban_panel.getWidth());
 //BA.debugLineNum = 114;BA.debugLine="but_panel.Width = 100%x";
mostCurrent._but_panel.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 115;BA.debugLine="start_button.Width = 40%x";
mostCurrent._start_button.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 117;BA.debugLine="main_tittle.Width = 100%x";
mostCurrent._main_tittle.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 119;BA.debugLine="ban_panel.Height = 30%y";
mostCurrent._ban_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 120;BA.debugLine="ban_picture.Height = ban_panel.Height - 3dip";
mostCurrent._ban_picture.setHeight((int) (mostCurrent._ban_panel.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 121;BA.debugLine="but_panel.Height = Activity.Height - ban_panel.H";
mostCurrent._but_panel.setHeight((int) (mostCurrent._activity.getHeight()-mostCurrent._ban_panel.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 122;BA.debugLine="start_button.Height = 11%y";
mostCurrent._start_button.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (11),mostCurrent.activityBA));
 //BA.debugLineNum = 124;BA.debugLine="main_tittle.Height = 38%y";
mostCurrent._main_tittle.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (38),mostCurrent.activityBA));
 //BA.debugLineNum = 126;BA.debugLine="ban_panel.Top = Activity.Top";
mostCurrent._ban_panel.setTop(mostCurrent._activity.getTop());
 //BA.debugLineNum = 127;BA.debugLine="ban_picture.Top = ban_panel.Top + 2dip";
mostCurrent._ban_picture.setTop((int) (mostCurrent._ban_panel.getTop()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2))));
 //BA.debugLineNum = 128;BA.debugLine="but_panel.Top = ban_panel.Top + ban_panel.Height";
mostCurrent._but_panel.setTop((int) (mostCurrent._ban_panel.getTop()+mostCurrent._ban_panel.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3))));
 //BA.debugLineNum = 129;BA.debugLine="start_button.Top =43%y";
mostCurrent._start_button.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (43),mostCurrent.activityBA));
 //BA.debugLineNum = 131;BA.debugLine="main_tittle.Top = 0";
mostCurrent._main_tittle.setTop((int) (0));
 //BA.debugLineNum = 133;BA.debugLine="ban_panel.Left = 0";
mostCurrent._ban_panel.setLeft((int) (0));
 //BA.debugLineNum = 134;BA.debugLine="ban_picture.Left = 0";
mostCurrent._ban_picture.setLeft((int) (0));
 //BA.debugLineNum = 135;BA.debugLine="but_panel.Left = 0";
mostCurrent._but_panel.setLeft((int) (0));
 //BA.debugLineNum = 136;BA.debugLine="start_button.Left = 30%x";
mostCurrent._start_button.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 138;BA.debugLine="main_tittle.Left = 0";
mostCurrent._main_tittle.setLeft((int) (0));
 //BA.debugLineNum = 140;BA.debugLine="main_tittle.Text = \"WELCOME\"&CRLF&\"→TO←\"&CRLF&\"L";
mostCurrent._main_tittle.setText((Object)("WELCOME"+anywheresoftware.b4a.keywords.Common.CRLF+"→TO←"+anywheresoftware.b4a.keywords.Common.CRLF+"LIFEBLOOD"));
 //BA.debugLineNum = 141;BA.debugLine="main_tittle.TextSize = 43";
mostCurrent._main_tittle.setTextSize((float) (43));
 //BA.debugLineNum = 142;BA.debugLine="main_tittle.Gravity = Gravity.CENTER";
mostCurrent._main_tittle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 143;BA.debugLine="main_tittle.Typeface = Typeface.LoadFromAssets(\"";
mostCurrent._main_tittle.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("harrington.ttf"));
 //BA.debugLineNum = 144;BA.debugLine="start_button.Typeface = Typeface.LoadFromAssets(";
mostCurrent._start_button.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 146;BA.debugLine="Dim gradiant As GradientDrawable";
_gradiant = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 147;BA.debugLine="Dim col(2) As Int";
_col = new int[(int) (2)];
;
 //BA.debugLineNum = 148;BA.debugLine="col(0) = Colors.Red";
_col[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 149;BA.debugLine="col(1) = Colors.LightGray";
_col[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.LightGray;
 //BA.debugLineNum = 150;BA.debugLine="gradiant.Initialize(\"TOP_BOTTOM\",col)";
_gradiant.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_col);
 //BA.debugLineNum = 151;BA.debugLine="gradiant.CornerRadius = 5dip";
_gradiant.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 152;BA.debugLine="start_button.Background = gradiant";
mostCurrent._start_button.setBackground((android.graphics.drawable.Drawable)(_gradiant.getObject()));
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public static String  _database_init() throws Exception{
 //BA.debugLineNum = 57;BA.debugLine="Sub database_init";
 //BA.debugLineNum = 58;BA.debugLine="If File.Exists(File.DirInternal,\"mydb.db\") = True";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db")==anywheresoftware.b4a.keywords.Common.True) { 
 }else {
 //BA.debugLineNum = 61;BA.debugLine="File.Copy(File.DirAssets,\"mydb.db\",File.DirInter";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mydb.db",anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db");
 };
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static String  _for_btn_animation() throws Exception{
 //BA.debugLineNum = 49;BA.debugLine="Sub for_btn_animation";
 //BA.debugLineNum = 51;BA.debugLine="a1.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 52;BA.debugLine="start_button.Tag = a1";
mostCurrent._start_button.setTag((Object)(mostCurrent._a1.getObject()));
 //BA.debugLineNum = 53;BA.debugLine="a1.Duration = 400";
mostCurrent._a1.setDuration((long) (400));
 //BA.debugLineNum = 54;BA.debugLine="a1.RepeatCount = 1";
mostCurrent._a1.setRepeatCount((int) (1));
 //BA.debugLineNum = 55;BA.debugLine="a1.RepeatMode = a1.REPEAT_REVERSE";
mostCurrent._a1.setRepeatMode(mostCurrent._a1.REPEAT_REVERSE);
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 27;BA.debugLine="Private ban_panel As Panel";
mostCurrent._ban_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private but_panel As Panel";
mostCurrent._but_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private ban_picture As ImageView";
mostCurrent._ban_picture = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private start_button As Button";
mostCurrent._start_button = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim a1 As Animation";
mostCurrent._a1 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private main_tittle As Label";
mostCurrent._main_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
login_form._process_globals();
create_account._process_globals();
menu_form._process_globals();
search_frame._process_globals();
httputils2service._process_globals();
help_frame._process_globals();
my_profile._process_globals();
about_frame._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Dim sqlLite As SQL";
_sqllite = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 20;BA.debugLine="database_init";
_database_init();
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _start_button_click() throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub start_button_Click";
 //BA.debugLineNum = 101;BA.debugLine="a1.Start(start_button)";
mostCurrent._a1.Start((android.view.View)(mostCurrent._start_button.getObject()));
 //BA.debugLineNum = 102;BA.debugLine="sqlLite.ExecNonQuery(\"UPDATE data SET isStart='1";
_sqllite.ExecNonQuery("UPDATE data SET isStart='1' WHERE id='1';");
 //BA.debugLineNum = 103;BA.debugLine="StartActivity (\"login_form\")";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)("login_form"));
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}
}
