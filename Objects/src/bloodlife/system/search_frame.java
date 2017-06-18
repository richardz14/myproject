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

public class search_frame extends Activity implements B4AActivity{
	public static search_frame mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "bloodlife.system", "bloodlife.system.search_frame");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (search_frame).");
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
		activityBA = new BA(this, layout, processBA, "bloodlife.system", "bloodlife.system.search_frame");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "bloodlife.system.search_frame", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (search_frame) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (search_frame) Resume **");
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
		return search_frame.class;
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
        BA.LogInfo("** Activity (search_frame) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (search_frame) Resume **");
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
public static anywheresoftware.b4a.sql.SQL _sqllite = null;
public static anywheresoftware.b4a.objects.collections.List _id_list = null;
public static anywheresoftware.b4a.objects.collections.List _bday_list = null;
public static anywheresoftware.b4a.objects.collections.List _bloodtype_list = null;
public static anywheresoftware.b4a.objects.collections.List _fulln_llist = null;
public static anywheresoftware.b4a.objects.collections.List _location_list = null;
public static anywheresoftware.b4a.objects.collections.List _lat_list = null;
public static anywheresoftware.b4a.objects.collections.List _lng_list = null;
public static anywheresoftware.b4a.objects.collections.List _donated_list = null;
public static anywheresoftware.b4a.objects.collections.List _email_list = null;
public static anywheresoftware.b4a.objects.collections.List _nickname_list = null;
public static anywheresoftware.b4a.objects.collections.List _phone1_list = null;
public static anywheresoftware.b4a.objects.collections.List _phone2_list = null;
public static anywheresoftware.b4a.objects.collections.List _image_list = null;
public static anywheresoftware.b4a.objects.collections.List _age_list = null;
public static anywheresoftware.b4a.objects.collections.List _gender_list = null;
public static int _is_complete = 0;
public static anywheresoftware.b4a.objects.collections.List _all_users_data_search_list = null;
public static anywheresoftware.b4a.gps.GPS _gpsclient = null;
public static anywheresoftware.b4a.gps.LocationWrapper _userlocation = null;
public static boolean _is_check_true = false;
public static String _row_click = "";
public static float _earth_radius = 0f;
public static float _pi = 0f;
public static int _clicked_list_all = 0;
public static int _list_all_select = 0;
public static String _spin_item_click = "";
public anywheresoftware.b4a.objects.PanelWrapper _toolkit_pnl = null;
public anywheresoftware.b4a.objects.LabelWrapper _search_lbl = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _search_spiner = null;
public anywheresoftware.b4a.objects.ButtonWrapper _search_btn = null;
public anywheresoftware.b4a.objects.WebViewWrapper _map_webview = null;
public anywheresoftware.b4a.objects.ButtonWrapper _list_btn = null;
public anywheresoftware.b4a.objects.PanelWrapper _list_panel = null;
public uk.co.martinpearman.b4a.webviewextras.WebViewExtras _map_extras = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrolllista = null;
public static int _item = 0;
public anywheresoftware.b4a.objects.PanelWrapper _dialog_panel = null;
public anywheresoftware.b4a.objects.PanelWrapper _dialog_all_panel = null;
public bloodlife.system.httpjob _data_query_id = null;
public bloodlife.system.httpjob _data_query_fulln = null;
public bloodlife.system.httpjob _data_query_location = null;
public bloodlife.system.httpjob _query_lat = null;
public bloodlife.system.httpjob _query_lng = null;
public bloodlife.system.httpjob _data_query_donated = null;
public bloodlife.system.httpjob _data_query_email = null;
public bloodlife.system.httpjob _data_query_nickname = null;
public bloodlife.system.httpjob _data_query_phone1 = null;
public bloodlife.system.httpjob _data_query_phone2 = null;
public bloodlife.system.httpjob _data_query_image = null;
public bloodlife.system.httpjob _data_query_age = null;
public bloodlife.system.httpjob _data_query_gender = null;
public bloodlife.system.httpjob _query_marker = null;
public bloodlife.system.httpjob _data_query_all_users_datas = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _isgpson = null;
public anywheresoftware.b4a.objects.PanelWrapper _view_info_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _view_data_info_person = null;
public flm.b4a.scrollview2d.ScrollView2DWrapper _scroll_view_info = null;
public anywheresoftware.b4a.objects.PanelWrapper _user_img_panl = null;
public anywheresoftware.b4a.objects.PanelWrapper _ph1_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _ph2_pnl = null;
public anywheresoftware.b4a.objects.LabelWrapper _phone1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _phone2 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a1 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _a2 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _ph1_a1 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _ph2_a2 = null;
public anywheresoftware.b4a.objects.AnimationWrapper _useri_a3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _user_image = null;
public bloodlife.system.main _main = null;
public bloodlife.system.login_form _login_form = null;
public bloodlife.system.create_account _create_account = null;
public bloodlife.system.menu_form _menu_form = null;
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
 //BA.debugLineNum = 94;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 96;BA.debugLine="Activity.LoadLayout(\"search_frame\")";
mostCurrent._activity.LoadLayout("search_frame",mostCurrent.activityBA);
 //BA.debugLineNum = 97;BA.debugLine="Activity.Title = \"SEARCH\"";
mostCurrent._activity.setTitle((Object)("SEARCH"));
 //BA.debugLineNum = 98;BA.debugLine="data_query_all_users_datas.Initialize(\"data_query";
mostCurrent._data_query_all_users_datas._initialize(processBA,"data_query_all_users_datas",search_frame.getObject());
 //BA.debugLineNum = 115;BA.debugLine="isGPSon.Enabled = False ''' for disable the GPS c";
mostCurrent._isgpson.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 116;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 117;BA.debugLine="gpsClient.Initialize(\"gpsClient\")";
_gpsclient.Initialize("gpsClient");
 //BA.debugLineNum = 118;BA.debugLine="userLocation.Initialize";
_userlocation.Initialize();
 };
 //BA.debugLineNum = 120;BA.debugLine="is_initialize";
_is_initialize();
 //BA.debugLineNum = 121;BA.debugLine="all_layout_load";
_all_layout_load();
 //BA.debugLineNum = 122;BA.debugLine="load_list";
_load_list();
 //BA.debugLineNum = 123;BA.debugLine="for_btn_animation";
_for_btn_animation();
 //BA.debugLineNum = 125;BA.debugLine="If sqlLite.IsInitialized = False Then";
if (_sqllite.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 126;BA.debugLine="sqlLite.Initialize(File.DirInternal, \"mydb.db\",";
_sqllite.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db",anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _choose = 0;
 //BA.debugLineNum = 216;BA.debugLine="Sub Activity_KeyPress(KeyCode As Int) As Boolean";
 //BA.debugLineNum = 218;BA.debugLine="If KeyCode == KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 219;BA.debugLine="If clicked_list_all == 0 Then";
if (_clicked_list_all==0) { 
 //BA.debugLineNum = 220;BA.debugLine="Dim choose As Int : choose = Msgbox2(\"Would";
_choose = 0;
 //BA.debugLineNum = 220;BA.debugLine="Dim choose As Int : choose = Msgbox2(\"Would";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2("Would you like to exit searching?","C O N F I R M A T I O N","YES","","NO",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 221;BA.debugLine="If choose == DialogResponse.POSITIVE Then";
if (_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 223;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 };
 }else if(_clicked_list_all==1) { 
 //BA.debugLineNum = 227;BA.debugLine="If list_all_select == 0 Then";
if (_list_all_select==0) { 
 //BA.debugLineNum = 228;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 //BA.debugLineNum = 229;BA.debugLine="clicked_list_all = 0";
_clicked_list_all = (int) (0);
 }else if(_list_all_select==1) { 
 //BA.debugLineNum = 231;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 232;BA.debugLine="list_all_select = 0";
_list_all_select = (int) (0);
 }else {
 //BA.debugLineNum = 234;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 //BA.debugLineNum = 235;BA.debugLine="list_all_select = 0";
_list_all_select = (int) (0);
 };
 };
 };
 //BA.debugLineNum = 241;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 242;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 248;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 250;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 244;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return "";
}
public static String  _all_layout_load() throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub all_layout_load";
 //BA.debugLineNum = 150;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 151;BA.debugLine="toolkit_pnl.Color = Colors.Transparent";
mostCurrent._toolkit_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 152;BA.debugLine="list_panel.Color = Colors.Transparent";
mostCurrent._list_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 153;BA.debugLine="search_btn.SetBackgroundImage(LoadBitmap(File.Dir";
mostCurrent._search_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"esearch.png").getObject()));
 //BA.debugLineNum = 154;BA.debugLine="list_btn.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._list_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"view all.png").getObject()));
 //BA.debugLineNum = 156;BA.debugLine="toolkit_pnl.Width = Activity.Width";
mostCurrent._toolkit_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 157;BA.debugLine="list_panel.Width = Activity.Width";
mostCurrent._list_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 158;BA.debugLine="map_webview.Width = Activity.Width";
mostCurrent._map_webview.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 159;BA.debugLine="isGPSon.Width = 50%x";
mostCurrent._isgpson.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 161;BA.debugLine="list_btn.Width = 50%x";
mostCurrent._list_btn.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 162;BA.debugLine="search_lbl.Width = 14%x";
mostCurrent._search_lbl.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 163;BA.debugLine="search_btn.Width = 14%x";
mostCurrent._search_btn.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 164;BA.debugLine="search_spiner.Width = ((toolkit_pnl.Width - sea";
mostCurrent._search_spiner.setWidth((int) (((mostCurrent._toolkit_pnl.getWidth()-mostCurrent._search_btn.getWidth())-mostCurrent._search_lbl.getWidth())-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA)));
 //BA.debugLineNum = 166;BA.debugLine="toolkit_pnl.Height = 14%y";
mostCurrent._toolkit_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 167;BA.debugLine="list_panel.Height = 11%y";
mostCurrent._list_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (11),mostCurrent.activityBA));
 //BA.debugLineNum = 168;BA.debugLine="map_webview.Height =((Activity.Height - toolkit";
mostCurrent._map_webview.setHeight((int) (((mostCurrent._activity.getHeight()-mostCurrent._toolkit_pnl.getHeight())-mostCurrent._list_panel.getHeight())));
 //BA.debugLineNum = 170;BA.debugLine="list_btn.Height = 9%y";
mostCurrent._list_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 171;BA.debugLine="search_lbl.Height = 10%y";
mostCurrent._search_lbl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 172;BA.debugLine="search_btn.Height = 10%y";
mostCurrent._search_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 173;BA.debugLine="search_spiner.Height = 10%y";
mostCurrent._search_spiner.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 175;BA.debugLine="toolkit_pnl.Left = 0";
mostCurrent._toolkit_pnl.setLeft((int) (0));
 //BA.debugLineNum = 176;BA.debugLine="list_panel.Left = 0";
mostCurrent._list_panel.setLeft((int) (0));
 //BA.debugLineNum = 177;BA.debugLine="map_webview.Left = 0";
mostCurrent._map_webview.setLeft((int) (0));
 //BA.debugLineNum = 179;BA.debugLine="isGPSon.Left = Activity.Width - isGPSon.Width";
mostCurrent._isgpson.setLeft((int) (mostCurrent._activity.getWidth()-mostCurrent._isgpson.getWidth()));
 //BA.debugLineNum = 181;BA.debugLine="list_btn.Left = ((list_panel.Width/2)/2)";
mostCurrent._list_btn.setLeft((int) (((mostCurrent._list_panel.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 182;BA.debugLine="search_lbl.Left = ((toolkit_pnl.Left + 3%x)+2%x";
mostCurrent._search_lbl.setLeft((int) (((mostCurrent._toolkit_pnl.getLeft()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (3),mostCurrent.activityBA))+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA))));
 //BA.debugLineNum = 183;BA.debugLine="search_spiner.Left = (search_lbl.Left + search_";
mostCurrent._search_spiner.setLeft((int) ((mostCurrent._search_lbl.getLeft()+mostCurrent._search_lbl.getWidth())));
 //BA.debugLineNum = 184;BA.debugLine="search_btn.Left = (search_spiner.Left + searc";
mostCurrent._search_btn.setLeft((int) ((mostCurrent._search_spiner.getLeft()+mostCurrent._search_spiner.getWidth())));
 //BA.debugLineNum = 186;BA.debugLine="toolkit_pnl.Top = 0";
mostCurrent._toolkit_pnl.setTop((int) (0));
 //BA.debugLineNum = 187;BA.debugLine="isGPSon.Top = toolkit_pnl.Top + toolkit_pnl.Heig";
mostCurrent._isgpson.setTop((int) (mostCurrent._toolkit_pnl.getTop()+mostCurrent._toolkit_pnl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)));
 //BA.debugLineNum = 188;BA.debugLine="map_webview.Top = (toolkit_pnl.Top + toolkit_pnl.";
mostCurrent._map_webview.setTop((int) ((mostCurrent._toolkit_pnl.getTop()+mostCurrent._toolkit_pnl.getHeight())));
 //BA.debugLineNum = 189;BA.debugLine="list_panel.Top = (map_webview.Top + map_webview.";
mostCurrent._list_panel.setTop((int) ((mostCurrent._map_webview.getTop()+mostCurrent._map_webview.getHeight())));
 //BA.debugLineNum = 191;BA.debugLine="list_btn.Top = 1%y'(list_panel.Top + 1%Y)";
mostCurrent._list_btn.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA));
 //BA.debugLineNum = 192;BA.debugLine="search_lbl.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_lbl.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 193;BA.debugLine="search_btn.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_btn.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 194;BA.debugLine="search_spiner.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_spiner.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 196;BA.debugLine="isGPSon.Gravity = Gravity.RIGHT";
mostCurrent._isgpson.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.RIGHT);
 //BA.debugLineNum = 197;BA.debugLine="End Sub";
return "";
}
public static String  _back_up_is_initialize() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriters = null;
 //BA.debugLineNum = 431;BA.debugLine="Sub back_up_is_initialize";
 //BA.debugLineNum = 432;BA.debugLine="Dim TextWriters As TextWriter";
_textwriters = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 433;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 434;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 435;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 436;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 437;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 438;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_donated.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 439;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_email.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 440;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_nickname.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 441;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone1.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 442;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone2.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 443;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.D";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_image.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 444;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_age.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 445;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_gender.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 446;BA.debugLine="End Sub";
return "";
}
public static String  _back_up_reading_txt() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_id = null;
String _line_id = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_fulln = null;
String _line_fulln = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_location = null;
String _line_location = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_lat = null;
String _line_lat = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_lng = null;
String _line_lng = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_donate = null;
String _line_donate = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_email = null;
String _line_email = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_nickname = null;
String _line_nickname = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_phone1 = null;
String _line_phone1 = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_phone2 = null;
String _line_phone2 = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_image = null;
String _line_image = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_age = null;
String _line_age = "";
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader_gender = null;
String _line_gender = "";
 //BA.debugLineNum = 1244;BA.debugLine="Sub back_up_reading_txt";
 //BA.debugLineNum = 1245;BA.debugLine="id_list.Initialize";
_id_list.Initialize();
 //BA.debugLineNum = 1246;BA.debugLine="fullN_llist.Initialize";
_fulln_llist.Initialize();
 //BA.debugLineNum = 1247;BA.debugLine="location_list.Initialize";
_location_list.Initialize();
 //BA.debugLineNum = 1248;BA.debugLine="lat_list.Initialize";
_lat_list.Initialize();
 //BA.debugLineNum = 1249;BA.debugLine="lng_list.Initialize";
_lng_list.Initialize();
 //BA.debugLineNum = 1250;BA.debugLine="donated_list.Initialize";
_donated_list.Initialize();
 //BA.debugLineNum = 1251;BA.debugLine="email_list.Initialize";
_email_list.Initialize();
 //BA.debugLineNum = 1252;BA.debugLine="nickname_list.Initialize";
_nickname_list.Initialize();
 //BA.debugLineNum = 1253;BA.debugLine="phone1_list.Initialize";
_phone1_list.Initialize();
 //BA.debugLineNum = 1254;BA.debugLine="phone2_list.Initialize";
_phone2_list.Initialize();
 //BA.debugLineNum = 1255;BA.debugLine="image_list.Initialize";
_image_list.Initialize();
 //BA.debugLineNum = 1256;BA.debugLine="age_list.Initialize";
_age_list.Initialize();
 //BA.debugLineNum = 1257;BA.debugLine="gender_list.Initialize";
_gender_list.Initialize();
 //BA.debugLineNum = 1259;BA.debugLine="Dim TextReader_id As TextReader";
_textreader_id = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1260;BA.debugLine="TextReader_id.Initialize(File.OpenInput(File.D";
_textreader_id.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt").getObject()));
 //BA.debugLineNum = 1261;BA.debugLine="Dim line_id As String";
_line_id = "";
 //BA.debugLineNum = 1262;BA.debugLine="line_id = TextReader_id.ReadLine";
_line_id = _textreader_id.ReadLine();
 //BA.debugLineNum = 1263;BA.debugLine="Do While line_id <> Null";
while (_line_id!= null) {
 //BA.debugLineNum = 1264;BA.debugLine="id_list.Add(line_id)";
_id_list.Add((Object)(_line_id));
 //BA.debugLineNum = 1265;BA.debugLine="line_id = TextReader_id.ReadLine";
_line_id = _textreader_id.ReadLine();
 }
;
 //BA.debugLineNum = 1267;BA.debugLine="TextReader_id.Close";
_textreader_id.Close();
 //BA.debugLineNum = 1269;BA.debugLine="Dim TextReader_fullN As TextReader";
_textreader_fulln = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1270;BA.debugLine="TextReader_fullN.Initialize(File.OpenInput(Fil";
_textreader_fulln.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt").getObject()));
 //BA.debugLineNum = 1271;BA.debugLine="Dim line_fullN As String";
_line_fulln = "";
 //BA.debugLineNum = 1272;BA.debugLine="line_fullN = TextReader_fullN.ReadLine";
_line_fulln = _textreader_fulln.ReadLine();
 //BA.debugLineNum = 1273;BA.debugLine="Do While line_fullN <> Null";
while (_line_fulln!= null) {
 //BA.debugLineNum = 1275;BA.debugLine="fullN_llist.Add(line_fullN)";
_fulln_llist.Add((Object)(_line_fulln));
 //BA.debugLineNum = 1276;BA.debugLine="line_fullN = TextReader_fullN.ReadLine";
_line_fulln = _textreader_fulln.ReadLine();
 }
;
 //BA.debugLineNum = 1278;BA.debugLine="TextReader_fullN.Close";
_textreader_fulln.Close();
 //BA.debugLineNum = 1280;BA.debugLine="Dim TextReader_location As TextReader";
_textreader_location = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1281;BA.debugLine="TextReader_location.Initialize(File.OpenInput(";
_textreader_location.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt").getObject()));
 //BA.debugLineNum = 1282;BA.debugLine="Dim line_location As String";
_line_location = "";
 //BA.debugLineNum = 1283;BA.debugLine="line_location = TextReader_location.ReadLine";
_line_location = _textreader_location.ReadLine();
 //BA.debugLineNum = 1284;BA.debugLine="Do While line_location <> Null";
while (_line_location!= null) {
 //BA.debugLineNum = 1286;BA.debugLine="location_list.Add(line_location)";
_location_list.Add((Object)(_line_location));
 //BA.debugLineNum = 1287;BA.debugLine="line_location = TextReader_location.ReadLi";
_line_location = _textreader_location.ReadLine();
 }
;
 //BA.debugLineNum = 1289;BA.debugLine="TextReader_location.Close";
_textreader_location.Close();
 //BA.debugLineNum = 1291;BA.debugLine="Dim TextReader_lat As TextReader";
_textreader_lat = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1292;BA.debugLine="TextReader_lat.Initialize(File.OpenInput(File.";
_textreader_lat.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt").getObject()));
 //BA.debugLineNum = 1293;BA.debugLine="Dim line_lat As String";
_line_lat = "";
 //BA.debugLineNum = 1294;BA.debugLine="line_lat = TextReader_lat.ReadLine";
_line_lat = _textreader_lat.ReadLine();
 //BA.debugLineNum = 1295;BA.debugLine="Do While line_lat <> Null";
while (_line_lat!= null) {
 //BA.debugLineNum = 1296;BA.debugLine="lat_list.Add(line_lat)";
_lat_list.Add((Object)(_line_lat));
 //BA.debugLineNum = 1297;BA.debugLine="line_lat = TextReader_lat.ReadLine";
_line_lat = _textreader_lat.ReadLine();
 }
;
 //BA.debugLineNum = 1299;BA.debugLine="TextReader_lat.Close";
_textreader_lat.Close();
 //BA.debugLineNum = 1301;BA.debugLine="Dim TextReader_lng As TextReader";
_textreader_lng = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1302;BA.debugLine="TextReader_lng.Initialize(File.OpenInput(File.";
_textreader_lng.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt").getObject()));
 //BA.debugLineNum = 1303;BA.debugLine="Dim line_lng As String";
_line_lng = "";
 //BA.debugLineNum = 1304;BA.debugLine="line_lng = TextReader_lng.ReadLine";
_line_lng = _textreader_lng.ReadLine();
 //BA.debugLineNum = 1305;BA.debugLine="Do While line_lng <> Null";
while (_line_lng!= null) {
 //BA.debugLineNum = 1306;BA.debugLine="lng_list.Add(line_lng)";
_lng_list.Add((Object)(_line_lng));
 //BA.debugLineNum = 1307;BA.debugLine="line_lng = TextReader_lng.ReadLine";
_line_lng = _textreader_lng.ReadLine();
 }
;
 //BA.debugLineNum = 1309;BA.debugLine="TextReader_lng.Close";
_textreader_lng.Close();
 //BA.debugLineNum = 1311;BA.debugLine="Dim TextReader_donate As TextReader";
_textreader_donate = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1312;BA.debugLine="TextReader_donate.Initialize(File.OpenInput(Fi";
_textreader_donate.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_donated.txt").getObject()));
 //BA.debugLineNum = 1313;BA.debugLine="Dim line_donate As String";
_line_donate = "";
 //BA.debugLineNum = 1314;BA.debugLine="line_donate = TextReader_donate.ReadLine";
_line_donate = _textreader_donate.ReadLine();
 //BA.debugLineNum = 1315;BA.debugLine="Do While line_donate <> Null";
while (_line_donate!= null) {
 //BA.debugLineNum = 1316;BA.debugLine="donated_list.Add(line_donate)";
_donated_list.Add((Object)(_line_donate));
 //BA.debugLineNum = 1317;BA.debugLine="line_donate = TextReader_donate.ReadLine";
_line_donate = _textreader_donate.ReadLine();
 }
;
 //BA.debugLineNum = 1319;BA.debugLine="TextReader_donate.Close";
_textreader_donate.Close();
 //BA.debugLineNum = 1321;BA.debugLine="Dim TextReader_email As TextReader";
_textreader_email = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1322;BA.debugLine="TextReader_email.Initialize(File.OpenInput(Fil";
_textreader_email.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_email.txt").getObject()));
 //BA.debugLineNum = 1323;BA.debugLine="Dim line_email As String";
_line_email = "";
 //BA.debugLineNum = 1324;BA.debugLine="line_email = TextReader_email.ReadLine";
_line_email = _textreader_email.ReadLine();
 //BA.debugLineNum = 1325;BA.debugLine="Do While line_email <> Null";
while (_line_email!= null) {
 //BA.debugLineNum = 1326;BA.debugLine="email_list.Add(line_email)";
_email_list.Add((Object)(_line_email));
 //BA.debugLineNum = 1327;BA.debugLine="line_email = TextReader_email.ReadLine";
_line_email = _textreader_email.ReadLine();
 }
;
 //BA.debugLineNum = 1329;BA.debugLine="TextReader_email.Close";
_textreader_email.Close();
 //BA.debugLineNum = 1331;BA.debugLine="Dim TextReader_nickname As TextReader";
_textreader_nickname = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1332;BA.debugLine="TextReader_nickname.Initialize(File.OpenInput(";
_textreader_nickname.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_nickname.txt").getObject()));
 //BA.debugLineNum = 1333;BA.debugLine="Dim line_nickname As String";
_line_nickname = "";
 //BA.debugLineNum = 1334;BA.debugLine="line_nickname = TextReader_nickname.ReadLine";
_line_nickname = _textreader_nickname.ReadLine();
 //BA.debugLineNum = 1335;BA.debugLine="Do While line_nickname <> Null";
while (_line_nickname!= null) {
 //BA.debugLineNum = 1336;BA.debugLine="nickname_list.Add(line_nickname)";
_nickname_list.Add((Object)(_line_nickname));
 //BA.debugLineNum = 1337;BA.debugLine="line_nickname = TextReader_nickname.ReadLi";
_line_nickname = _textreader_nickname.ReadLine();
 }
;
 //BA.debugLineNum = 1339;BA.debugLine="TextReader_nickname.Close";
_textreader_nickname.Close();
 //BA.debugLineNum = 1341;BA.debugLine="Dim TextReader_phone1 As TextReader";
_textreader_phone1 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1342;BA.debugLine="TextReader_phone1.Initialize(File.OpenInput(Fi";
_textreader_phone1.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone1.txt").getObject()));
 //BA.debugLineNum = 1343;BA.debugLine="Dim line_phone1 As String";
_line_phone1 = "";
 //BA.debugLineNum = 1344;BA.debugLine="line_phone1 = TextReader_phone1.ReadLine";
_line_phone1 = _textreader_phone1.ReadLine();
 //BA.debugLineNum = 1345;BA.debugLine="Do While line_phone1 <> Null";
while (_line_phone1!= null) {
 //BA.debugLineNum = 1346;BA.debugLine="phone1_list.Add(line_phone1)";
_phone1_list.Add((Object)(_line_phone1));
 //BA.debugLineNum = 1347;BA.debugLine="line_phone1 = TextReader_phone1.ReadLine";
_line_phone1 = _textreader_phone1.ReadLine();
 }
;
 //BA.debugLineNum = 1349;BA.debugLine="TextReader_phone1.Close";
_textreader_phone1.Close();
 //BA.debugLineNum = 1351;BA.debugLine="Dim TextReader_phone2 As TextReader";
_textreader_phone2 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1352;BA.debugLine="TextReader_phone2.Initialize(File.OpenInput(Fi";
_textreader_phone2.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone2.txt").getObject()));
 //BA.debugLineNum = 1353;BA.debugLine="Dim line_phone2 As String";
_line_phone2 = "";
 //BA.debugLineNum = 1354;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 //BA.debugLineNum = 1355;BA.debugLine="Do While line_phone2 <> Null";
while (_line_phone2!= null) {
 //BA.debugLineNum = 1356;BA.debugLine="phone2_list.Add(line_phone2)";
_phone2_list.Add((Object)(_line_phone2));
 //BA.debugLineNum = 1357;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 }
;
 //BA.debugLineNum = 1359;BA.debugLine="TextReader_phone2.Close";
_textreader_phone2.Close();
 //BA.debugLineNum = 1361;BA.debugLine="Dim TextReader_image As TextReader";
_textreader_image = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1362;BA.debugLine="TextReader_image.Initialize(File.OpenInput(Fil";
_textreader_image.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_image.txt").getObject()));
 //BA.debugLineNum = 1363;BA.debugLine="Dim line_image As String";
_line_image = "";
 //BA.debugLineNum = 1364;BA.debugLine="line_image = TextReader_image.ReadLine";
_line_image = _textreader_image.ReadLine();
 //BA.debugLineNum = 1365;BA.debugLine="Do While line_image <> Null";
while (_line_image!= null) {
 //BA.debugLineNum = 1366;BA.debugLine="image_list.Add(line_image)";
_image_list.Add((Object)(_line_image));
 //BA.debugLineNum = 1367;BA.debugLine="line_image = TextReader_image.ReadLine";
_line_image = _textreader_image.ReadLine();
 }
;
 //BA.debugLineNum = 1369;BA.debugLine="TextReader_image.Close";
_textreader_image.Close();
 //BA.debugLineNum = 1370;BA.debugLine="Dim TextReader_age As TextReader";
_textreader_age = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1371;BA.debugLine="TextReader_age.Initialize(File.OpenInput(File.";
_textreader_age.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_age.txt").getObject()));
 //BA.debugLineNum = 1372;BA.debugLine="Dim line_age As String";
_line_age = "";
 //BA.debugLineNum = 1373;BA.debugLine="line_age = TextReader_age.ReadLine";
_line_age = _textreader_age.ReadLine();
 //BA.debugLineNum = 1374;BA.debugLine="Do While line_age <> Null";
while (_line_age!= null) {
 //BA.debugLineNum = 1375;BA.debugLine="age_list.Add(line_age)";
_age_list.Add((Object)(_line_age));
 //BA.debugLineNum = 1376;BA.debugLine="line_age = TextReader_age.ReadLine";
_line_age = _textreader_age.ReadLine();
 }
;
 //BA.debugLineNum = 1378;BA.debugLine="TextReader_age.Close";
_textreader_age.Close();
 //BA.debugLineNum = 1380;BA.debugLine="Dim TextReader_gender As TextReader";
_textreader_gender = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1381;BA.debugLine="TextReader_gender.Initialize(File.OpenInput(Fi";
_textreader_gender.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_gender.txt").getObject()));
 //BA.debugLineNum = 1382;BA.debugLine="Dim line_gender As String";
_line_gender = "";
 //BA.debugLineNum = 1383;BA.debugLine="line_gender = TextReader_gender.ReadLine";
_line_gender = _textreader_gender.ReadLine();
 //BA.debugLineNum = 1384;BA.debugLine="Do While line_gender <> Null";
while (_line_gender!= null) {
 //BA.debugLineNum = 1385;BA.debugLine="gender_list.Add(line_gender)";
_gender_list.Add((Object)(_line_gender));
 //BA.debugLineNum = 1386;BA.debugLine="line_gender = TextReader_gender.ReadLine";
_line_gender = _textreader_gender.ReadLine();
 }
;
 //BA.debugLineNum = 1388;BA.debugLine="TextReader_gender.Close";
_textreader_gender.Close();
 //BA.debugLineNum = 1389;BA.debugLine="End Sub";
return "";
}
public static String  _back_up_search_btn_click() throws Exception{
bloodlife.system.calculations _url_back = null;
String _url_id = "";
String _full_name = "";
String _location = "";
String _lat = "";
String _lng = "";
String _donated = "";
String _email = "";
String _nickname = "";
String _phoneq1 = "";
String _phoneq2 = "";
String _image = "";
String _age = "";
String _gender = "";
 //BA.debugLineNum = 265;BA.debugLine="Sub back_up_search_btn_Click";
 //BA.debugLineNum = 266;BA.debugLine="a2.Start(search_btn)";
mostCurrent._a2.Start((android.view.View)(mostCurrent._search_btn.getObject()));
 //BA.debugLineNum = 267;BA.debugLine="ProgressDialogShow2(\"please wait...\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"please wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 268;BA.debugLine="isGPSon.Enabled = True";
mostCurrent._isgpson.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 269;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 270;BA.debugLine="Dim url_id,full_name,location,lat,lng,donated,ema";
_url_id = "";
_full_name = "";
_location = "";
_lat = "";
_lng = "";
_donated = "";
_email = "";
_nickname = "";
_phoneq1 = "";
_phoneq2 = "";
_image = "";
_age = "";
_gender = "";
 //BA.debugLineNum = 271;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 272;BA.debugLine="url_id = url_back.php_email_url(\"search_blood_id.";
_url_id = _url_back._php_email_url("search_blood_id.php");
 //BA.debugLineNum = 273;BA.debugLine="full_name = url_back.php_email_url(\"search_blood_";
_full_name = _url_back._php_email_url("search_blood_fullN.php");
 //BA.debugLineNum = 274;BA.debugLine="location = url_back.php_email_url(\"search_blood_l";
_location = _url_back._php_email_url("search_blood_location.php");
 //BA.debugLineNum = 275;BA.debugLine="lat = url_back.php_email_url(\"search_blood_lat.ph";
_lat = _url_back._php_email_url("search_blood_lat.php");
 //BA.debugLineNum = 276;BA.debugLine="lng = url_back.php_email_url(\"search_blood_long.p";
_lng = _url_back._php_email_url("search_blood_long.php");
 //BA.debugLineNum = 277;BA.debugLine="donated = url_back.php_email_url(\"search_blood_do";
_donated = _url_back._php_email_url("search_blood_donateB.php");
 //BA.debugLineNum = 278;BA.debugLine="email = url_back.php_email_url(\"search_blood_emai";
_email = _url_back._php_email_url("search_blood_email.php");
 //BA.debugLineNum = 279;BA.debugLine="nickname = url_back.php_email_url(\"search_blood_n";
_nickname = _url_back._php_email_url("search_blood_nickN.php");
 //BA.debugLineNum = 280;BA.debugLine="phoneq1 = url_back.php_email_url(\"search_blood_ph";
_phoneq1 = _url_back._php_email_url("search_blood_phone1.php");
 //BA.debugLineNum = 281;BA.debugLine="phoneq2 = url_back.php_email_url(\"search_blood_ph";
_phoneq2 = _url_back._php_email_url("search_blood_phone2.php");
 //BA.debugLineNum = 282;BA.debugLine="image = url_back.php_email_url(\"search_blood_imag";
_image = _url_back._php_email_url("search_blood_image.php");
 //BA.debugLineNum = 283;BA.debugLine="age = url_back.php_email_url(\"search_blood_age.ph";
_age = _url_back._php_email_url("search_blood_age.php");
 //BA.debugLineNum = 284;BA.debugLine="gender = url_back.php_email_url(\"search_blood_gen";
_gender = _url_back._php_email_url("search_blood_gender.php");
 //BA.debugLineNum = 288;BA.debugLine="data_query_id.Download2(url_id,Array As String(\"i";
mostCurrent._data_query_id._download2(_url_id,new String[]{"id","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 289;BA.debugLine="data_query_fullN.Download2(full_name,Array As Str";
mostCurrent._data_query_fulln._download2(_full_name,new String[]{"full_name","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 290;BA.debugLine="data_query_location.Download2(location,Array As S";
mostCurrent._data_query_location._download2(_location,new String[]{"location","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 291;BA.debugLine="query_lat.Download2(lat,Array As String(\"lat\",\"SE";
mostCurrent._query_lat._download2(_lat,new String[]{"lat","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 292;BA.debugLine="query_lng.Download2(lng,Array As String(\"long\",\"S";
mostCurrent._query_lng._download2(_lng,new String[]{"long","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 294;BA.debugLine="data_query_donated.Download2(donated,Array As Str";
mostCurrent._data_query_donated._download2(_donated,new String[]{"donate_b","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 295;BA.debugLine="data_query_email.Download2(email,Array As String(";
mostCurrent._data_query_email._download2(_email,new String[]{"email","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 296;BA.debugLine="data_query_nickname.Download2(nickname,Array As S";
mostCurrent._data_query_nickname._download2(_nickname,new String[]{"nick","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 297;BA.debugLine="data_query_phone1.Download2(phoneq1,Array As Stri";
mostCurrent._data_query_phone1._download2(_phoneq1,new String[]{"phone1","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 298;BA.debugLine="data_query_phone2.Download2(phoneq2,Array As Stri";
mostCurrent._data_query_phone2._download2(_phoneq2,new String[]{"phone2","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 300;BA.debugLine="data_query_image.Download2(image,Array As String(";
mostCurrent._data_query_image._download2(_image,new String[]{"image","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 302;BA.debugLine="data_query_age.Download2(age,Array As String(\"age";
mostCurrent._data_query_age._download2(_age,new String[]{"age","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 303;BA.debugLine="data_query_gender.Download2(gender,Array As Strin";
mostCurrent._data_query_gender._download2(_gender,new String[]{"gender","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 308;BA.debugLine="End Sub";
return "";
}
public static String  _bookmark_img_click() throws Exception{
int _choose = 0;
anywheresoftware.b4a.sql.SQL.CursorWrapper _set_cursor = null;
 //BA.debugLineNum = 1010;BA.debugLine="Sub bookmark_img_click";
 //BA.debugLineNum = 1011;BA.debugLine="Dim choose As Int";
_choose = 0;
 //BA.debugLineNum = 1012;BA.debugLine="choose = Msgbox2(\"Would you like to add this as b";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2("Would you like to add this as bookmark","C O N F I R M A T I O N","YES","CANCEL","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 1013;BA.debugLine="If choose == DialogResponse.POSITIVE Then";
if (_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 1014;BA.debugLine="If sqlLite.IsInitialized = True Then";
if (_sqllite.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1015;BA.debugLine="Dim set_cursor As Cursor";
_set_cursor = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 1016;BA.debugLine="set_cursor = sqlLite.ExecQuery(\"SELECT * from";
_set_cursor.setObject((android.database.Cursor)(_sqllite.ExecQuery("SELECT * from `bookmarks` where `users_id`="+BA.ObjectToString(_id_list.Get((int)(Double.parseDouble(_row_click))))+";")));
 //BA.debugLineNum = 1018;BA.debugLine="If set_cursor.RowCount == 0 Then";
if (_set_cursor.getRowCount()==0) { 
 //BA.debugLineNum = 1019;BA.debugLine="sqlLite.ExecNonQuery(\"INSERT INTO `bookmarks`";
_sqllite.ExecNonQuery("INSERT INTO `bookmarks` (`users_id`,`age`,`gender`,`is_donated`,`email`,`ph_number1`,`ph_number2`,`location`,`full_name`,`image`) VALUES('"+BA.ObjectToString(_id_list.Get((int)(Double.parseDouble(_row_click))))+"','"+BA.ObjectToString(_age_list.Get((int)(Double.parseDouble(_row_click))))+"','"+BA.ObjectToString(_gender_list.Get((int)(Double.parseDouble(_row_click))))+"','"+BA.ObjectToString(_donated_list.Get((int)(Double.parseDouble(_row_click))))+"','"+BA.ObjectToString(_email_list.Get((int)(Double.parseDouble(_row_click))))+"','"+BA.ObjectToString(_phone1_list.Get((int)(Double.parseDouble(_row_click))))+"','"+BA.ObjectToString(_phone2_list.Get((int)(Double.parseDouble(_row_click))))+"','"+BA.ObjectToString(_location_list.Get((int)(Double.parseDouble(_row_click))))+"','"+BA.ObjectToString(_fulln_llist.Get((int)(Double.parseDouble(_row_click))))+"','"+BA.ObjectToString(_image_list.Get((int)(Double.parseDouble(_row_click))))+"')");
 //BA.debugLineNum = 1020;BA.debugLine="Msgbox(\"Successfuly added as bookmark...\",\"C";
anywheresoftware.b4a.keywords.Common.Msgbox("Successfuly added as bookmark...","C O N F I R M A T I O N",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 1022;BA.debugLine="Msgbox(\"You have already added this person as";
anywheresoftware.b4a.keywords.Common.Msgbox("You have already added this person as bookmark...","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 };
 }else if(_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 };
 //BA.debugLineNum = 1031;BA.debugLine="End Sub";
return "";
}
public static String  _can_btn_click() throws Exception{
 //BA.debugLineNum = 1056;BA.debugLine="Sub can_btn_click";
 //BA.debugLineNum = 1057;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 //BA.debugLineNum = 1058;BA.debugLine="End Sub";
return "";
}
public static String  _create_map() throws Exception{
String _htmlstring1 = "";
String _htmlstring1_1 = "";
String _htmlstring2 = "";
String _htmlstring3 = "";
double _gpslat = 0;
double _gpslng = 0;
double _tolat = 0;
double _tolng = 0;
double _distance = 0;
int _distancemeter = 0;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _location = null;
int _i = 0;
 //BA.debugLineNum = 339;BA.debugLine="Sub create_map";
 //BA.debugLineNum = 340;BA.debugLine="ProgressDialogShow2(\"Creating the map, please wai";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Creating the map, please wait...",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 341;BA.debugLine="Dim htmlString1,htmlString1_1,htmlString2,htmlStr";
_htmlstring1 = "";
_htmlstring1_1 = "";
_htmlstring2 = "";
_htmlstring3 = "";
 //BA.debugLineNum = 342;BA.debugLine="Dim GPSlat,GPSlng,TOlat,TOlng,distance As Double";
_gpslat = 0;
_gpslng = 0;
_tolat = 0;
_tolng = 0;
_distance = 0;
 //BA.debugLineNum = 343;BA.debugLine="Dim distanceMeter As Int";
_distancemeter = 0;
 //BA.debugLineNum = 344;BA.debugLine="htmlString1 = File.GetText(File.DirAssets, \"locat";
_htmlstring1 = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"location_top.txt");
 //BA.debugLineNum = 345;BA.debugLine="htmlString1 = htmlString1 & \" var markers=[]; var";
_htmlstring1 = _htmlstring1+" var markers=[]; var contents = []; var infowindows = []; ";
 //BA.debugLineNum = 346;BA.debugLine="Dim location As TextWriter";
_location = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 347;BA.debugLine="location.Initialize(File.OpenOutput(File.";
_location.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"all_marker_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 348;BA.debugLine="location.WriteLine(htmlString1)";
_location.WriteLine(_htmlstring1);
 //BA.debugLineNum = 349;BA.debugLine="If is_check_true == True Then";
if (_is_check_true==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 350;BA.debugLine="GPSlat = userLocation.Latitude";
_gpslat = _userlocation.getLatitude();
 //BA.debugLineNum = 351;BA.debugLine="GPSlng = userLocation.Longitude";
_gpslng = _userlocation.getLongitude();
 //BA.debugLineNum = 353;BA.debugLine="Log(\"long: \"&GPSlng)";
anywheresoftware.b4a.keywords.Common.Log("long: "+BA.NumberToString(_gpslng));
 //BA.debugLineNum = 355;BA.debugLine="htmlString1_1 = \" new google.maps.Marker({ posit";
_htmlstring1_1 = " new google.maps.Marker({ position: new google.maps.LatLng("+BA.NumberToString(_gpslat)+", "+BA.NumberToString(_gpslng)+"), map: map, title: 'My Location', clickable: true, icon: 'http://www.google.com/mapfiles/dd-end.png' }); ";
 //BA.debugLineNum = 356;BA.debugLine="location.WriteLine(htmlString1_1)";
_location.WriteLine(_htmlstring1_1);
 }else {
 };
 //BA.debugLineNum = 361;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step18 = 1;
final int limit18 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step18 > 0 && _i <= limit18) || (step18 < 0 && _i >= limit18); _i = ((int)(0 + _i + step18)) ) {
 //BA.debugLineNum = 362;BA.debugLine="If is_check_true == True Then";
if (_is_check_true==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 363;BA.debugLine="TOlat = lat_list.Get(i)";
_tolat = (double)(BA.ObjectToNumber(_lat_list.Get(_i)));
 //BA.debugLineNum = 364;BA.debugLine="TOlng = lng_list.Get(i)";
_tolng = (double)(BA.ObjectToNumber(_lng_list.Get(_i)));
 //BA.debugLineNum = 366;BA.debugLine="distance = earth_radius * ACos( Cos( ( 90 - GPS";
_distance = _earth_radius*anywheresoftware.b4a.keywords.Common.ACos(anywheresoftware.b4a.keywords.Common.Cos((90-_gpslat)*(_pi/(double)180))*anywheresoftware.b4a.keywords.Common.Cos((90-_tolat)*(_pi/(double)180))+anywheresoftware.b4a.keywords.Common.Sin((90-_gpslat)*(_pi/(double)180))*anywheresoftware.b4a.keywords.Common.Sin((90-_tolat)*(_pi/(double)180))*anywheresoftware.b4a.keywords.Common.Cos((_gpslng-_tolng)*(_pi/(double)180)));
 //BA.debugLineNum = 367;BA.debugLine="distanceMeter = distance*1000";
_distancemeter = (int) (_distance*1000);
 //BA.debugLineNum = 369;BA.debugLine="htmlString2 = \"markers[\"&i&\"] = new google.maps";
_htmlstring2 = "markers["+BA.NumberToString(_i)+"] = new google.maps.Marker({position: new google.maps.LatLng("+BA.ObjectToString(_lat_list.Get(_i))+" , "+BA.ObjectToString(_lng_list.Get(_i))+"), map: map, title: '"+BA.ObjectToString(_fulln_llist.Get(_i))+"', icon: 'https://raw.githubusercontent.com/richardz14/myproject/master/Files/heart_arrow_img.png', clickable: true }); markers["+BA.NumberToString(_i)+"].index = "+BA.NumberToString(_i)+"; contents["+BA.NumberToString(_i)+"] = '<div class=\"iw-container\"><div class=\"iw-title\">Personal Information</div><div class=\"iw-content\"><b><h2><center>"+BA.ObjectToString(_fulln_llist.Get(_i))+"</center></h2></b><h4>Blood Type: <b>"+mostCurrent._spin_item_click+"</b></h4><h4>Age: <b>"+BA.ObjectToString(_age_list.Get(_i))+"</b></h4><h4>Age: <b>"+BA.ObjectToString(_gender_list.Get(_i))+"</b></h4><h4>Email Address: <b>"+BA.ObjectToString(_email_list.Get(_i))+"</b></h4><h4>Location: <b>"+BA.ObjectToString(_location_list.Get(_i))+"</b></h4><h4>Phone Number 1: <b>"+BA.ObjectToString(_phone1_list.Get(_i))+"</b></h4><h4>Phone Number 2: <b>"+BA.ObjectToString(_phone2_list.Get(_i))+"</b></h4><h4>Donated: <b>"+BA.ObjectToString(_donated_list.Get(_i))+"</b></h4><h4><b>You are "+BA.NumberToString(_distancemeter)+"m away from the donor!</b></h4></div></div>'; infowindows["+BA.NumberToString(_i)+"] = new google.maps.InfoWindow({ content: contents["+BA.NumberToString(_i)+"], maxWidth: 350 }); google.maps.event.addListener(markers["+BA.NumberToString(_i)+"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); ";
 //BA.debugLineNum = 370;BA.debugLine="location.WriteLine(htmlString2)";
_location.WriteLine(_htmlstring2);
 }else {
 //BA.debugLineNum = 372;BA.debugLine="htmlString2 = \"markers[\"&i&\"] = new google.maps";
_htmlstring2 = "markers["+BA.NumberToString(_i)+"] = new google.maps.Marker({position: new google.maps.LatLng("+BA.ObjectToString(_lat_list.Get(_i))+" , "+BA.ObjectToString(_lng_list.Get(_i))+"), map: map, title: '"+BA.ObjectToString(_fulln_llist.Get(_i))+"', icon: 'https://raw.githubusercontent.com/richardz14/myproject/master/Files/heart_arrow_img.png', clickable: true }); markers["+BA.NumberToString(_i)+"].index = "+BA.NumberToString(_i)+"; contents["+BA.NumberToString(_i)+"] = '<div class=\"iw-container\"><div class=\"iw-title\">Personal Information</div><div class=\"iw-content\"><b><h2><center>"+BA.ObjectToString(_fulln_llist.Get(_i))+"</center></h2></b><h4>Blood Type: <b>"+mostCurrent._spin_item_click+"</b></h4><h4>Age: <b>"+BA.ObjectToString(_age_list.Get(_i))+"</b></h4><h4>Age: <b>"+BA.ObjectToString(_gender_list.Get(_i))+"</b></h4><h4>Email Address: <b>"+BA.ObjectToString(_email_list.Get(_i))+"</b></h4><h4>Location: <b>"+BA.ObjectToString(_location_list.Get(_i))+"</b></h4><h4>Phone Number 1: <b>"+BA.ObjectToString(_phone1_list.Get(_i))+"</b></h4><h4>Phone Number 2: <b>"+BA.ObjectToString(_phone2_list.Get(_i))+"</b></h4><h4>Donated: <b>"+BA.ObjectToString(_donated_list.Get(_i))+"</b></h4></div></div>'; infowindows["+BA.NumberToString(_i)+"] = new google.maps.InfoWindow({ content: contents["+BA.NumberToString(_i)+"], maxWidth: 350}); google.maps.event.addListener(markers["+BA.NumberToString(_i)+"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); ";
 //BA.debugLineNum = 373;BA.debugLine="location.WriteLine(htmlString2)";
_location.WriteLine(_htmlstring2);
 };
 }
};
 //BA.debugLineNum = 415;BA.debugLine="htmlString3 = File.GetText(File.DirAssets, \"locat";
_htmlstring3 = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"location_buttom.txt");
 //BA.debugLineNum = 416;BA.debugLine="location.WriteLine(htmlString3)";
_location.WriteLine(_htmlstring3);
 //BA.debugLineNum = 417;BA.debugLine="location.Close";
_location.Close();
 //BA.debugLineNum = 423;BA.debugLine="map_webview.LoadHtml(File.ReadString(File.DirInte";
mostCurrent._map_webview.LoadHtml(anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"all_marker_location.txt"));
 //BA.debugLineNum = 427;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 720;BA.debugLine="Sub data_list_Click";
 //BA.debugLineNum = 721;BA.debugLine="list_all_select = 2";
_list_all_select = (int) (2);
 //BA.debugLineNum = 722;BA.debugLine="Dim Send As View";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 723;BA.debugLine="Dim row As Int";
_row = 0;
 //BA.debugLineNum = 724;BA.debugLine="Send=Sender";
_send.setObject((android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 725;BA.debugLine="row=Floor(Send.Tag/10) '20";
_row = (int) (anywheresoftware.b4a.keywords.Common.Floor((double)(BA.ObjectToNumber(_send.getTag()))/(double)10));
 //BA.debugLineNum = 726;BA.debugLine="item=row";
_item = _row;
 //BA.debugLineNum = 727;BA.debugLine="row_click = row";
_row_click = BA.NumberToString(_row);
 //BA.debugLineNum = 728;BA.debugLine="Log(row)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_row));
 //BA.debugLineNum = 732;BA.debugLine="If view_info_pnl.IsInitialized == True Then";
if (mostCurrent._view_info_pnl.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 733;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 };
 //BA.debugLineNum = 735;BA.debugLine="view_info_pnl.Initialize(\"view_info_pnl\")";
mostCurrent._view_info_pnl.Initialize(mostCurrent.activityBA,"view_info_pnl");
 //BA.debugLineNum = 736;BA.debugLine="Dim view_panl As Panel";
_view_panl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 737;BA.debugLine="Dim vie_btn,can_btn As Button";
_vie_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 738;BA.debugLine="Dim lbl_tittle As Label";
_lbl_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 739;BA.debugLine="lbl_tittle.Initialize(\"\")";
_lbl_tittle.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 740;BA.debugLine="view_panl.Initialize(\"view_panl\")";
_view_panl.Initialize(mostCurrent.activityBA,"view_panl");
 //BA.debugLineNum = 741;BA.debugLine="vie_btn.Initialize(\"vie_btn\")";
_vie_btn.Initialize(mostCurrent.activityBA,"vie_btn");
 //BA.debugLineNum = 742;BA.debugLine="can_btn.Initialize(\"can_btn\")";
_can_btn.Initialize(mostCurrent.activityBA,"can_btn");
 //BA.debugLineNum = 743;BA.debugLine="vie_btn.Text = \"VIEW\"";
_vie_btn.setText((Object)("VIEW"));
 //BA.debugLineNum = 744;BA.debugLine="can_btn.Text = \"CANCEL\"";
_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 745;BA.debugLine="vie_btn.Typeface = Typeface.LoadFromAssets(\"HipHo";
_vie_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 746;BA.debugLine="can_btn.Typeface = Typeface.LoadFromAssets(\"HipHo";
_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 747;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 748;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 749;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 750;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 751;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 752;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 753;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 754;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 755;BA.debugLine="vie_btn.Background = V_btn";
_vie_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 756;BA.debugLine="can_btn.Background = C_btn";
_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 757;BA.debugLine="lbl_tittle.Text = \"SELECT ACTION\"";
_lbl_tittle.setText((Object)("SELECT ACTION"));
 //BA.debugLineNum = 758;BA.debugLine="lbl_tittle.Typeface = Typeface.LoadFromAssets(\"Hi";
_lbl_tittle.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 759;BA.debugLine="lbl_tittle.Gravity = Gravity.CENTER";
_lbl_tittle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 760;BA.debugLine="lbl_tittle.TextColor = Colors.White";
_lbl_tittle.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 761;BA.debugLine="view_panl.SetBackgroundImage(LoadBitmap(File.DirA";
_view_panl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 762;BA.debugLine="view_panl.AddView(lbl_tittle,1%x,2%y,72%x,8%y)";
_view_panl.AddView((android.view.View)(_lbl_tittle.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 763;BA.debugLine="view_panl.AddView(vie_btn,5%x,lbl_tittle.Top + lb";
_view_panl.AddView((android.view.View)(_vie_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_lbl_tittle.getTop()+_lbl_tittle.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (31),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 764;BA.debugLine="view_panl.AddView(can_btn,vie_btn.Left+vie_btn.Wi";
_view_panl.AddView((android.view.View)(_can_btn.getObject()),(int) (_vie_btn.getLeft()+_vie_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),_vie_btn.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (31),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 765;BA.debugLine="view_info_pnl.AddView(view_panl,13%x,((Activity.H";
mostCurrent._view_info_pnl.AddView((android.view.View)(_view_panl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 766;BA.debugLine="Activity.AddView(view_info_pnl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._view_info_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 767;BA.debugLine="End Sub";
return "";
}
public static String  _database_init() throws Exception{
 //BA.debugLineNum = 129;BA.debugLine="Sub database_init";
 //BA.debugLineNum = 130;BA.debugLine="If File.Exists(File.DirInternal,\"mydb.db\") = True";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db")==anywheresoftware.b4a.keywords.Common.True) { 
 }else {
 //BA.debugLineNum = 133;BA.debugLine="File.Copy(File.DirAssets,\"mydb.db\",File.DirInter";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mydb.db",anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"mydb.db");
 };
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _dialog_all_panel_click() throws Exception{
 //BA.debugLineNum = 1513;BA.debugLine="Sub dialog_all_panel_click";
 //BA.debugLineNum = 1515;BA.debugLine="End Sub";
return "";
}
public static String  _dialog_panel_can_btn_click() throws Exception{
 //BA.debugLineNum = 1509;BA.debugLine="Sub dialog_panel_can_btn_click";
 //BA.debugLineNum = 1510;BA.debugLine="clicked_list_all = 0";
_clicked_list_all = (int) (0);
 //BA.debugLineNum = 1511;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 //BA.debugLineNum = 1512;BA.debugLine="End Sub";
return "";
}
public static String  _for_btn_animation() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper[] _animations = null;
int _i = 0;
 //BA.debugLineNum = 136;BA.debugLine="Sub for_btn_animation";
 //BA.debugLineNum = 137;BA.debugLine="a1.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 138;BA.debugLine="a2.InitializeAlpha(\"\", 1, 0)";
mostCurrent._a2.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 139;BA.debugLine="search_btn.Tag = a2";
mostCurrent._search_btn.setTag((Object)(mostCurrent._a2.getObject()));
 //BA.debugLineNum = 140;BA.debugLine="list_btn.Tag = a1";
mostCurrent._list_btn.setTag((Object)(mostCurrent._a1.getObject()));
 //BA.debugLineNum = 141;BA.debugLine="Dim animations() As Animation";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[(int) (0)];
{
int d0 = _animations.length;
for (int i0 = 0;i0 < d0;i0++) {
_animations[i0] = new anywheresoftware.b4a.objects.AnimationWrapper();
}
}
;
 //BA.debugLineNum = 142;BA.debugLine="animations = Array As Animation(a1, a2)";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[]{mostCurrent._a1,mostCurrent._a2};
 //BA.debugLineNum = 143;BA.debugLine="For i = 0 To animations.Length - 1";
{
final int step7 = 1;
final int limit7 = (int) (_animations.length-1);
for (_i = (int) (0) ; (step7 > 0 && _i <= limit7) || (step7 < 0 && _i >= limit7); _i = ((int)(0 + _i + step7)) ) {
 //BA.debugLineNum = 144;BA.debugLine="animations(i).Duration = 300";
_animations[_i].setDuration((long) (300));
 //BA.debugLineNum = 145;BA.debugLine="animations(i).RepeatCount = 1";
_animations[_i].setRepeatCount((int) (1));
 //BA.debugLineNum = 146;BA.debugLine="animations(i).RepeatMode = animations(i).REPEAT_";
_animations[_i].setRepeatMode(_animations[_i].REPEAT_REVERSE);
 }
};
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return "";
}
public static String  _for_phone_clik_animation() throws Exception{
anywheresoftware.b4a.objects.AnimationWrapper[] _animations = null;
int _i = 0;
 //BA.debugLineNum = 1032;BA.debugLine="Sub for_phone_clik_animation";
 //BA.debugLineNum = 1033;BA.debugLine="ph1_a1.InitializeAlpha(\"\", 1, 0)";
mostCurrent._ph1_a1.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 1034;BA.debugLine="ph2_a2.InitializeAlpha(\"\", 1, 0)";
mostCurrent._ph2_a2.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 1035;BA.debugLine="userI_a3.InitializeAlpha(\"\", 1, 0)";
mostCurrent._useri_a3.InitializeAlpha(mostCurrent.activityBA,"",(float) (1),(float) (0));
 //BA.debugLineNum = 1037;BA.debugLine="ph2_pnl.Tag = ph2_a2";
mostCurrent._ph2_pnl.setTag((Object)(mostCurrent._ph2_a2.getObject()));
 //BA.debugLineNum = 1038;BA.debugLine="ph1_pnl.Tag = ph1_a1";
mostCurrent._ph1_pnl.setTag((Object)(mostCurrent._ph1_a1.getObject()));
 //BA.debugLineNum = 1039;BA.debugLine="user_image.Tag = userI_a3";
mostCurrent._user_image.setTag((Object)(mostCurrent._useri_a3.getObject()));
 //BA.debugLineNum = 1041;BA.debugLine="Dim animations() As Animation";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[(int) (0)];
{
int d0 = _animations.length;
for (int i0 = 0;i0 < d0;i0++) {
_animations[i0] = new anywheresoftware.b4a.objects.AnimationWrapper();
}
}
;
 //BA.debugLineNum = 1042;BA.debugLine="animations = Array As Animation(ph2_a2, ph1_a1, u";
_animations = new anywheresoftware.b4a.objects.AnimationWrapper[]{mostCurrent._ph2_a2,mostCurrent._ph1_a1,mostCurrent._useri_a3};
 //BA.debugLineNum = 1043;BA.debugLine="For i = 0 To animations.Length - 1";
{
final int step9 = 1;
final int limit9 = (int) (_animations.length-1);
for (_i = (int) (0) ; (step9 > 0 && _i <= limit9) || (step9 < 0 && _i >= limit9); _i = ((int)(0 + _i + step9)) ) {
 //BA.debugLineNum = 1044;BA.debugLine="animations(i).Duration = 300";
_animations[_i].setDuration((long) (300));
 //BA.debugLineNum = 1045;BA.debugLine="animations(i).RepeatCount = 1";
_animations[_i].setRepeatCount((int) (1));
 //BA.debugLineNum = 1046;BA.debugLine="animations(i).RepeatMode = animations(i).REPEAT_";
_animations[_i].setRepeatMode(_animations[_i].REPEAT_REVERSE);
 }
};
 //BA.debugLineNum = 1048;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 45;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 48;BA.debugLine="Public spin_item_click As String :";
mostCurrent._spin_item_click = "";
 //BA.debugLineNum = 49;BA.debugLine="Private toolkit_pnl As Panel";
mostCurrent._toolkit_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private search_lbl As Label";
mostCurrent._search_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private search_spiner As Spinner";
mostCurrent._search_spiner = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private search_btn As Button";
mostCurrent._search_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private map_webview As WebView";
mostCurrent._map_webview = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private list_btn As Button";
mostCurrent._list_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private list_panel As Panel";
mostCurrent._list_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private map_extras As WebViewExtras";
mostCurrent._map_extras = new uk.co.martinpearman.b4a.webviewextras.WebViewExtras();
 //BA.debugLineNum = 58;BA.debugLine="Dim scrolllista As ScrollView";
mostCurrent._scrolllista = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Dim item As Int";
_item = 0;
 //BA.debugLineNum = 60;BA.debugLine="Dim dialog_panel As Panel";
mostCurrent._dialog_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Dim dialog_all_panel As Panel";
mostCurrent._dialog_all_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private data_query_id As HttpJob";
mostCurrent._data_query_id = new bloodlife.system.httpjob();
 //BA.debugLineNum = 64;BA.debugLine="Private data_query_fullN As HttpJob";
mostCurrent._data_query_fulln = new bloodlife.system.httpjob();
 //BA.debugLineNum = 65;BA.debugLine="Private data_query_location As HttpJob";
mostCurrent._data_query_location = new bloodlife.system.httpjob();
 //BA.debugLineNum = 66;BA.debugLine="Private query_lat As HttpJob";
mostCurrent._query_lat = new bloodlife.system.httpjob();
 //BA.debugLineNum = 67;BA.debugLine="Private query_lng As HttpJob";
mostCurrent._query_lng = new bloodlife.system.httpjob();
 //BA.debugLineNum = 68;BA.debugLine="Private data_query_donated As HttpJob";
mostCurrent._data_query_donated = new bloodlife.system.httpjob();
 //BA.debugLineNum = 69;BA.debugLine="Private data_query_email As HttpJob";
mostCurrent._data_query_email = new bloodlife.system.httpjob();
 //BA.debugLineNum = 70;BA.debugLine="Private data_query_nickname As HttpJob";
mostCurrent._data_query_nickname = new bloodlife.system.httpjob();
 //BA.debugLineNum = 71;BA.debugLine="Private data_query_phone1 As HttpJob";
mostCurrent._data_query_phone1 = new bloodlife.system.httpjob();
 //BA.debugLineNum = 72;BA.debugLine="Private data_query_phone2 As HttpJob";
mostCurrent._data_query_phone2 = new bloodlife.system.httpjob();
 //BA.debugLineNum = 73;BA.debugLine="Private data_query_image As HttpJob";
mostCurrent._data_query_image = new bloodlife.system.httpjob();
 //BA.debugLineNum = 74;BA.debugLine="Private data_query_age As HttpJob";
mostCurrent._data_query_age = new bloodlife.system.httpjob();
 //BA.debugLineNum = 75;BA.debugLine="Private data_query_gender As HttpJob";
mostCurrent._data_query_gender = new bloodlife.system.httpjob();
 //BA.debugLineNum = 76;BA.debugLine="Private query_marker As HttpJob";
mostCurrent._query_marker = new bloodlife.system.httpjob();
 //BA.debugLineNum = 78;BA.debugLine="Private data_query_all_users_datas As HttpJob";
mostCurrent._data_query_all_users_datas = new bloodlife.system.httpjob();
 //BA.debugLineNum = 79;BA.debugLine="Private isGPSon As CheckBox";
mostCurrent._isgpson = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Dim view_info_pnl As Panel";
mostCurrent._view_info_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Dim view_data_info_person As Panel";
mostCurrent._view_data_info_person = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Dim scroll_view_info As ScrollView2D";
mostCurrent._scroll_view_info = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Dim user_img_panl As Panel";
mostCurrent._user_img_panl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private ph1_pnl,ph2_pnl As Panel";
mostCurrent._ph1_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._ph2_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 87;BA.debugLine="Private phone1,phone2 As Label";
mostCurrent._phone1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._phone2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 89;BA.debugLine="Dim a1, a2 As Animation";
mostCurrent._a1 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._a2 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 90;BA.debugLine="Dim ph1_a1,ph2_a2,userI_a3 As Animation";
mostCurrent._ph1_a1 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._ph2_a2 = new anywheresoftware.b4a.objects.AnimationWrapper();
mostCurrent._useri_a3 = new anywheresoftware.b4a.objects.AnimationWrapper();
 //BA.debugLineNum = 91;BA.debugLine="Dim user_image As ImageView";
mostCurrent._user_image = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _gpsclient_locationchanged(anywheresoftware.b4a.gps.LocationWrapper _gpslocation) throws Exception{
 //BA.debugLineNum = 331;BA.debugLine="Sub gpsClient_LocationChanged (gpsLocation As Loca";
 //BA.debugLineNum = 332;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 333;BA.debugLine="userLocation=gpsLocation";
_userlocation = _gpslocation;
 //BA.debugLineNum = 335;BA.debugLine="gpsClient.Stop";
_gpsclient.Stop();
 //BA.debugLineNum = 336;BA.debugLine="create_map";
_create_map();
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return "";
}
public static String  _is_initialize() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriters = null;
 //BA.debugLineNum = 447;BA.debugLine="Sub is_initialize";
 //BA.debugLineNum = 448;BA.debugLine="Dim TextWriters As TextWriter";
_textwriters = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 449;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_all_users_data.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 450;BA.debugLine="End Sub";
return "";
}
public static String  _isgpson_checkedchange(boolean _checked) throws Exception{
int _choose = 0;
 //BA.debugLineNum = 309;BA.debugLine="Sub isGPSon_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 310;BA.debugLine="is_check_true = Checked";
_is_check_true = _checked;
 //BA.debugLineNum = 311;BA.debugLine="If Checked == True Then";
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 312;BA.debugLine="If gpsClient.GPSEnabled=False Then";
if (_gpsclient.getGPSEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 314;BA.debugLine="Dim choose As Int";
_choose = 0;
 //BA.debugLineNum = 315;BA.debugLine="choose = Msgbox2(\"Please enable your device's G";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2("Please enable your device's GPS capabilities","C O N F I R M A T I O N","SETTINGS","CANCEL","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 316;BA.debugLine="If choose == DialogResponse.POSITIVE Then";
if (_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 317;BA.debugLine="isGPSon.Checked = False";
mostCurrent._isgpson.setChecked(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 318;BA.debugLine="StartActivity(gpsClient.LocationSetting";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_gpsclient.getLocationSettingsIntent()));
 }else if(_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 320;BA.debugLine="isGPSon.Checked = False";
mostCurrent._isgpson.setChecked(anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 324;BA.debugLine="gpsClient.Start(10.1799469, 122.9068577)";
_gpsclient.Start(processBA,(long) (10.1799469),(float) (122.9068577));
 //BA.debugLineNum = 325;BA.debugLine="ProgressDialogShow(\"Waiting for GPS location\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Waiting for GPS location");
 };
 }else {
 //BA.debugLineNum = 328;BA.debugLine="create_map";
_create_map();
 };
 //BA.debugLineNum = 330;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(bloodlife.system.httpjob _job) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_id = null;
 //BA.debugLineNum = 451;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 452;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 453;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"data_query_all_users_datas")) {
case 0: {
 //BA.debugLineNum = 455;BA.debugLine="Dim TextWriter_id As TextWriter";
_textwriter_id = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 456;BA.debugLine="TextWriter_id.Initialize(File.OpenOutput(";
_textwriter_id.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_all_users_data.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 457;BA.debugLine="TextWriter_id.WriteLine(job.GetString.Tr";
_textwriter_id.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 458;BA.debugLine="TextWriter_id.Close";
_textwriter_id.Close();
 //BA.debugLineNum = 459;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 460;BA.debugLine="Log(\"ok\")";
anywheresoftware.b4a.keywords.Common.Log("ok");
 //BA.debugLineNum = 461;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
}
;
 //BA.debugLineNum = 466;BA.debugLine="If is_complete == 1 Then";
if (_is_complete==1) { 
 //BA.debugLineNum = 467;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 468;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 469;BA.debugLine="Try";
try { //BA.debugLineNum = 470;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 471;BA.debugLine="create_map";
_create_map();
 } 
       catch (Exception e19) {
			processBA.setLastException(e19); //BA.debugLineNum = 473;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage());
 };
 };
 //BA.debugLineNum = 476;BA.debugLine="Log(is_complete)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_is_complete));
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 479;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 480;BA.debugLine="job.Release";
_job._release();
 //BA.debugLineNum = 481;BA.debugLine="isGPSon.Enabled = False";
mostCurrent._isgpson.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 482;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 483;BA.debugLine="Msgbox(\"Error: Error connecting to server,please";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Error connecting to server,please try again.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 488;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone_backup(bloodlife.system.httpjob _job) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_id = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_full = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_location = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_lat = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_lng = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_donate = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_email = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_nickname = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_phone1 = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_phone2 = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_image = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_age = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_gender = null;
 //BA.debugLineNum = 489;BA.debugLine="Public Sub JobDone_backup(job As HttpJob)";
 //BA.debugLineNum = 490;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 491;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"data_query_id_get","data_query_fullN_get","data_query_location_get","data_query_lat_get","data_query_lng_get","data_query_donated_get","data_query_email_get","data_query_nickname_get","data_query_phone1_get","data_query_phone2_get","data_query_image","data_query_age_get","data_query_gender_get")) {
case 0: {
 //BA.debugLineNum = 493;BA.debugLine="Dim TextWriter_id As TextWriter";
_textwriter_id = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 494;BA.debugLine="TextWriter_id.Initialize(File.OpenOutput(";
_textwriter_id.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 495;BA.debugLine="TextWriter_id.WriteLine(job.GetString.Tr";
_textwriter_id.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 496;BA.debugLine="TextWriter_id.Close";
_textwriter_id.Close();
 //BA.debugLineNum = 497;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 498;BA.debugLine="Log(\"id ok\")";
anywheresoftware.b4a.keywords.Common.Log("id ok");
 //BA.debugLineNum = 499;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 1: {
 //BA.debugLineNum = 501;BA.debugLine="Dim TextWriter_full As TextWriter";
_textwriter_full = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 502;BA.debugLine="TextWriter_full.Initialize(File.OpenOutpu";
_textwriter_full.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 503;BA.debugLine="TextWriter_full.WriteLine(job.GetString.";
_textwriter_full.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 504;BA.debugLine="TextWriter_full.Close";
_textwriter_full.Close();
 //BA.debugLineNum = 505;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 506;BA.debugLine="Log(\"fn ok\")";
anywheresoftware.b4a.keywords.Common.Log("fn ok");
 //BA.debugLineNum = 507;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 2: {
 //BA.debugLineNum = 509;BA.debugLine="Dim TextWriter_location As TextWriter";
_textwriter_location = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 510;BA.debugLine="TextWriter_location.Initialize(File.OpenO";
_textwriter_location.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 511;BA.debugLine="TextWriter_location.WriteLine(job.GetStr";
_textwriter_location.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 512;BA.debugLine="TextWriter_location.Close";
_textwriter_location.Close();
 //BA.debugLineNum = 513;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 514;BA.debugLine="Log(\"location ok\")";
anywheresoftware.b4a.keywords.Common.Log("location ok");
 //BA.debugLineNum = 515;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 3: {
 //BA.debugLineNum = 517;BA.debugLine="Dim TextWriter_lat As TextWriter";
_textwriter_lat = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 518;BA.debugLine="TextWriter_lat.Initialize(File.OpenOutput";
_textwriter_lat.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 519;BA.debugLine="TextWriter_lat.WriteLine(job.GetString.t";
_textwriter_lat.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 520;BA.debugLine="TextWriter_lat.Close";
_textwriter_lat.Close();
 //BA.debugLineNum = 521;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 522;BA.debugLine="Log(\"lat ok\")";
anywheresoftware.b4a.keywords.Common.Log("lat ok");
 //BA.debugLineNum = 523;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 4: {
 //BA.debugLineNum = 525;BA.debugLine="Dim TextWriter_lng As TextWriter";
_textwriter_lng = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 526;BA.debugLine="TextWriter_lng.Initialize(File.OpenOutput";
_textwriter_lng.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 527;BA.debugLine="TextWriter_lng.WriteLine(job.GetString.t";
_textwriter_lng.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 528;BA.debugLine="TextWriter_lng.Close";
_textwriter_lng.Close();
 //BA.debugLineNum = 529;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 530;BA.debugLine="Log(\"lng ok\")";
anywheresoftware.b4a.keywords.Common.Log("lng ok");
 //BA.debugLineNum = 531;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 5: {
 //BA.debugLineNum = 533;BA.debugLine="Dim TextWriter_donate As TextWriter";
_textwriter_donate = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 534;BA.debugLine="TextWriter_donate.Initialize(File.OpenOut";
_textwriter_donate.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_donated.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 535;BA.debugLine="TextWriter_donate.WriteLine(job.GetStrin";
_textwriter_donate.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 536;BA.debugLine="TextWriter_donate.Close";
_textwriter_donate.Close();
 //BA.debugLineNum = 537;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 538;BA.debugLine="Log(\"donated ok\")";
anywheresoftware.b4a.keywords.Common.Log("donated ok");
 //BA.debugLineNum = 539;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 6: {
 //BA.debugLineNum = 541;BA.debugLine="Dim TextWriter_email As TextWriter";
_textwriter_email = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 542;BA.debugLine="TextWriter_email.Initialize(File.OpenOutp";
_textwriter_email.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_email.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 543;BA.debugLine="TextWriter_email.WriteLine(job.GetString";
_textwriter_email.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 544;BA.debugLine="TextWriter_email.Close";
_textwriter_email.Close();
 //BA.debugLineNum = 545;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 546;BA.debugLine="Log(\"email ok\")";
anywheresoftware.b4a.keywords.Common.Log("email ok");
 //BA.debugLineNum = 547;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 7: {
 //BA.debugLineNum = 549;BA.debugLine="Dim TextWriter_nickname As TextWriter";
_textwriter_nickname = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 550;BA.debugLine="TextWriter_nickname.Initialize(File.OpenO";
_textwriter_nickname.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_nickname.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 551;BA.debugLine="TextWriter_nickname.WriteLine(job.GetStr";
_textwriter_nickname.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 552;BA.debugLine="TextWriter_nickname.Close";
_textwriter_nickname.Close();
 //BA.debugLineNum = 553;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 554;BA.debugLine="Log(\"nick ok\")";
anywheresoftware.b4a.keywords.Common.Log("nick ok");
 //BA.debugLineNum = 555;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 8: {
 //BA.debugLineNum = 557;BA.debugLine="Dim TextWriter_phone1 As TextWriter";
_textwriter_phone1 = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 558;BA.debugLine="TextWriter_phone1.Initialize(File.OpenOut";
_textwriter_phone1.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone1.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 559;BA.debugLine="TextWriter_phone1.WriteLine(job.GetStrin";
_textwriter_phone1.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 560;BA.debugLine="TextWriter_phone1.Close";
_textwriter_phone1.Close();
 //BA.debugLineNum = 561;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 562;BA.debugLine="Log(\"phone1 ok\")";
anywheresoftware.b4a.keywords.Common.Log("phone1 ok");
 //BA.debugLineNum = 563;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 9: {
 //BA.debugLineNum = 565;BA.debugLine="Dim TextWriter_phone2 As TextWriter";
_textwriter_phone2 = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 566;BA.debugLine="TextWriter_phone2.Initialize(File.OpenOut";
_textwriter_phone2.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone2.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 567;BA.debugLine="TextWriter_phone2.WriteLine(job.GetStrin";
_textwriter_phone2.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 568;BA.debugLine="TextWriter_phone2.Close";
_textwriter_phone2.Close();
 //BA.debugLineNum = 569;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 570;BA.debugLine="Log(\"phone2 ok\")";
anywheresoftware.b4a.keywords.Common.Log("phone2 ok");
 //BA.debugLineNum = 571;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 10: {
 //BA.debugLineNum = 573;BA.debugLine="Dim TextWriter_image As TextWriter";
_textwriter_image = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 574;BA.debugLine="TextWriter_image.Initialize(File.OpenOutp";
_textwriter_image.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_image.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 575;BA.debugLine="TextWriter_image.WriteLine(job.GetString";
_textwriter_image.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 576;BA.debugLine="TextWriter_image.Close";
_textwriter_image.Close();
 //BA.debugLineNum = 577;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 578;BA.debugLine="Log(\"image ok\")";
anywheresoftware.b4a.keywords.Common.Log("image ok");
 break; }
case 11: {
 //BA.debugLineNum = 581;BA.debugLine="Dim TextWriter_age As TextWriter";
_textwriter_age = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 582;BA.debugLine="TextWriter_age.Initialize(File.OpenOutput";
_textwriter_age.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_age.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 583;BA.debugLine="TextWriter_age.WriteLine(job.GetString.t";
_textwriter_age.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 584;BA.debugLine="TextWriter_age.Close";
_textwriter_age.Close();
 //BA.debugLineNum = 585;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 586;BA.debugLine="Log(\"age ok\")";
anywheresoftware.b4a.keywords.Common.Log("age ok");
 //BA.debugLineNum = 587;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
case 12: {
 //BA.debugLineNum = 589;BA.debugLine="Dim TextWriter_gender As TextWriter";
_textwriter_gender = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 590;BA.debugLine="TextWriter_gender.Initialize(File.OpenOut";
_textwriter_gender.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_gender.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 591;BA.debugLine="TextWriter_gender.WriteLine(job.GetStrin";
_textwriter_gender.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 592;BA.debugLine="TextWriter_gender.Close";
_textwriter_gender.Close();
 //BA.debugLineNum = 593;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 //BA.debugLineNum = 594;BA.debugLine="Log(\"gender ok\")";
anywheresoftware.b4a.keywords.Common.Log("gender ok");
 //BA.debugLineNum = 595;BA.debugLine="Log(job.GetString.trim)";
anywheresoftware.b4a.keywords.Common.Log(_job._getstring().trim());
 break; }
}
;
 //BA.debugLineNum = 599;BA.debugLine="If is_complete == 12 Then";
if (_is_complete==12) { 
 //BA.debugLineNum = 600;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 601;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 602;BA.debugLine="Try";
try { //BA.debugLineNum = 603;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 604;BA.debugLine="create_map";
_create_map();
 } 
       catch (Exception e114) {
			processBA.setLastException(e114); //BA.debugLineNum = 606;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage());
 };
 };
 //BA.debugLineNum = 609;BA.debugLine="Log(is_complete)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_is_complete));
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 612;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 613;BA.debugLine="job.Release";
_job._release();
 //BA.debugLineNum = 614;BA.debugLine="isGPSon.Enabled = False";
mostCurrent._isgpson.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 615;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 616;BA.debugLine="Msgbox(\"Error: Error connecting to server,please";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Error connecting to server,please try again.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 621;BA.debugLine="End Sub";
return "";
}
public static String  _list_btn_backup_click() throws Exception{
anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2 _cd = null;
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bgnd = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap1 = null;
anywheresoftware.b4a.objects.PanelWrapper _panel0 = null;
int _paneltop = 0;
int _panelheight = 0;
int _i = 0;
anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
 //BA.debugLineNum = 633;BA.debugLine="Sub list_btn_BACKUP_Click";
 //BA.debugLineNum = 634;BA.debugLine="ProgressDialogShow2(\"Loading data, Please Wait...";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Loading data, Please Wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 635;BA.debugLine="If scrolllista.IsInitialized == True Then";
if (mostCurrent._scrolllista.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 636;BA.debugLine="scrolllista.RemoveView";
mostCurrent._scrolllista.RemoveView();
 //BA.debugLineNum = 637;BA.debugLine="dialog_panel.RemoveView";
mostCurrent._dialog_panel.RemoveView();
 };
 //BA.debugLineNum = 639;BA.debugLine="scrolllista.Initialize(500)";
mostCurrent._scrolllista.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 640;BA.debugLine="dialog_panel.Initialize(\"dialog_panel\")";
mostCurrent._dialog_panel.Initialize(mostCurrent.activityBA,"dialog_panel");
 //BA.debugLineNum = 641;BA.debugLine="Dim cd As CustomDialog2";
_cd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2();
 //BA.debugLineNum = 642;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 643;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 644;BA.debugLine="Dim bgnd As ColorDrawable";
_bgnd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 645;BA.debugLine="bgnd.Initialize(Colors.Cyan, 5dip)";
_bgnd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Cyan,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 646;BA.debugLine="pnl.Background = bgnd";
_pnl.setBackground((android.graphics.drawable.Drawable)(_bgnd.getObject()));
 //BA.debugLineNum = 650;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 661;BA.debugLine="Dim Bitmap1 As Bitmap";
_bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 662;BA.debugLine="Dim Panel0 As Panel";
_panel0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 663;BA.debugLine="Dim PanelTop, PanelHeight  As Int";
_paneltop = 0;
_panelheight = 0;
 //BA.debugLineNum = 668;BA.debugLine="Bitmap1.Initialize(File.DirAssets,\"banner1.png\")";
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner1.png");
 //BA.debugLineNum = 669;BA.debugLine="PanelTop=1%y";
_paneltop = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA);
 //BA.debugLineNum = 670;BA.debugLine="Panel0=scrolllista.Panel";
_panel0 = mostCurrent._scrolllista.getPanel();
 //BA.debugLineNum = 671;BA.debugLine="Panel0.Color=Colors.argb(0,0,0,0)  'sets the invi";
_panel0.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 673;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step22 = 1;
final int limit22 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step22 > 0 && _i <= limit22) || (step22 < 0 && _i >= limit22); _i = ((int)(0 + _i + step22)) ) {
 //BA.debugLineNum = 675;BA.debugLine="If i>0 And i<3 Then Bitmap1.Initialize(File.DirA";
if (_i>0 && _i<3) { 
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner.png");};
 //BA.debugLineNum = 676;BA.debugLine="Dim ImageView1 As ImageView";
_imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 677;BA.debugLine="ImageView1.Initialize(\"data_list\")";
_imageview1.Initialize(mostCurrent.activityBA,"data_list");
 //BA.debugLineNum = 678;BA.debugLine="PanelHeight=12%y";
_panelheight = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA);
 //BA.debugLineNum = 680;BA.debugLine="Panel0.AddView(ImageView1,1%x,PanelTop,71%x,Pane";
_panel0.AddView((android.view.View)(_imageview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),_paneltop,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 681;BA.debugLine="ImageView1.Tag=i&\"1\"";
_imageview1.setTag((Object)(BA.NumberToString(_i)+"1"));
 //BA.debugLineNum = 682;BA.debugLine="ImageView1.Bitmap=Bitmap1";
_imageview1.setBitmap((android.graphics.Bitmap)(_bitmap1.getObject()));
 //BA.debugLineNum = 683;BA.debugLine="ImageView1.Gravity=Gravity.fill";
_imageview1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 685;BA.debugLine="Dim Label1, Label2 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 686;BA.debugLine="Label1.Initialize(\"\")";
_label1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 687;BA.debugLine="Label2.Initialize(\"\")";
_label2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 688;BA.debugLine="Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 689;BA.debugLine="Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 691;BA.debugLine="Label1.TextColor= Colors.black";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 692;BA.debugLine="Label1.TextSize= 17";
_label1.setTextSize((float) (17));
 //BA.debugLineNum = 693;BA.debugLine="Label1.Gravity=Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 694;BA.debugLine="Label1.Color=Colors.argb(0,0,0,0)";
_label1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 695;BA.debugLine="Label1.Text=fullN_llist.Get(i) 'set data from li";
_label1.setText(_fulln_llist.Get(_i));
 //BA.debugLineNum = 697;BA.debugLine="Label2.TextColor= Colors.black";
_label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 698;BA.debugLine="Label2.TextSize= 15";
_label2.setTextSize((float) (15));
 //BA.debugLineNum = 699;BA.debugLine="Label2.Gravity=Gravity.CENTER";
_label2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 700;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 701;BA.debugLine="Label2.Text=location_list.Get(i) 'set data from";
_label2.setText(_location_list.Get(_i));
 //BA.debugLineNum = 704;BA.debugLine="If i > id_list.size-1 Then i = id_list.size-1";
if (_i>_id_list.getSize()-1) { 
_i = (int) (_id_list.getSize()-1);};
 //BA.debugLineNum = 707;BA.debugLine="PanelTop=PanelTop+PanelHeight";
_paneltop = (int) (_paneltop+_panelheight);
 }
};
 //BA.debugLineNum = 709;BA.debugLine="Panel0.Height=PanelTop";
_panel0.setHeight(_paneltop);
 //BA.debugLineNum = 711;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 713;BA.debugLine="dialog_panel.AddView(scrolllista,1%x,1%y,75%x,78%";
mostCurrent._dialog_panel.AddView((android.view.View)(mostCurrent._scrolllista.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (78),mostCurrent.activityBA));
 //BA.debugLineNum = 714;BA.debugLine="cd.AddView(dialog_panel,75%x,78%y)";
_cd.AddView((android.view.View)(mostCurrent._dialog_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (78),mostCurrent.activityBA));
 //BA.debugLineNum = 716;BA.debugLine="cd.Show(\"List of people\", \"CANCEL\", \"VIEW\", \"\", N";
_cd.Show("List of people","CANCEL","VIEW","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 718;BA.debugLine="End Sub";
return "";
}
public static String  _list_btn_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bgnd = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bitmap1 = null;
anywheresoftware.b4a.objects.PanelWrapper _panel0 = null;
int _paneltop = 0;
int _panelheight = 0;
int _i = 0;
anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
anywheresoftware.b4a.objects.ButtonWrapper _dialog_panel_can_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _dialog_panel_tittle = null;
anywheresoftware.b4a.objects.PanelWrapper _btn_panel = null;
anywheresoftware.b4a.objects.drawable.GradientDrawable _se_btn = null;
int[] _colorg = null;
 //BA.debugLineNum = 1390;BA.debugLine="Sub list_btn_Click";
 //BA.debugLineNum = 1391;BA.debugLine="a1.Start(list_btn)";
mostCurrent._a1.Start((android.view.View)(mostCurrent._list_btn.getObject()));
 //BA.debugLineNum = 1392;BA.debugLine="clicked_list_all = 1";
_clicked_list_all = (int) (1);
 //BA.debugLineNum = 1393;BA.debugLine="ProgressDialogShow2(\"Loading data, Please Wait...";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Loading data, Please Wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1394;BA.debugLine="If scrolllista.IsInitialized == True Then";
if (mostCurrent._scrolllista.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1395;BA.debugLine="scrolllista.RemoveView";
mostCurrent._scrolllista.RemoveView();
 //BA.debugLineNum = 1396;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 };
 //BA.debugLineNum = 1398;BA.debugLine="scrolllista.Initialize(500)";
mostCurrent._scrolllista.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 1399;BA.debugLine="dialog_panel.Initialize(\"dialog_panel\")";
mostCurrent._dialog_panel.Initialize(mostCurrent.activityBA,"dialog_panel");
 //BA.debugLineNum = 1400;BA.debugLine="dialog_all_panel.Initialize(\"dialog_all_panel\")";
mostCurrent._dialog_all_panel.Initialize(mostCurrent.activityBA,"dialog_all_panel");
 //BA.debugLineNum = 1402;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1403;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1404;BA.debugLine="Dim bgnd As ColorDrawable";
_bgnd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1405;BA.debugLine="bgnd.Initialize(Colors.Cyan, 5dip)";
_bgnd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Cyan,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 1406;BA.debugLine="pnl.Background = bgnd";
_pnl.setBackground((android.graphics.drawable.Drawable)(_bgnd.getObject()));
 //BA.debugLineNum = 1407;BA.debugLine="dialog_all_panel.Color = Colors.Transparent";
mostCurrent._dialog_all_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1411;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 1422;BA.debugLine="Dim Bitmap1 As Bitmap";
_bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 1423;BA.debugLine="Dim Panel0 As Panel";
_panel0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1424;BA.debugLine="Dim PanelTop, PanelHeight  As Int";
_paneltop = 0;
_panelheight = 0;
 //BA.debugLineNum = 1429;BA.debugLine="Bitmap1.Initialize(File.DirAssets,\"banner1.png\")";
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner1.png");
 //BA.debugLineNum = 1430;BA.debugLine="PanelTop=1%y";
_paneltop = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA);
 //BA.debugLineNum = 1431;BA.debugLine="Panel0=scrolllista.Panel";
_panel0 = mostCurrent._scrolllista.getPanel();
 //BA.debugLineNum = 1432;BA.debugLine="Panel0.Color=Colors.argb(0,0,0,0)  'sets the invi";
_panel0.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 1434;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step25 = 1;
final int limit25 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step25 > 0 && _i <= limit25) || (step25 < 0 && _i >= limit25); _i = ((int)(0 + _i + step25)) ) {
 //BA.debugLineNum = 1436;BA.debugLine="If i>0 And i<3 Then Bitmap1.Initialize(File.DirA";
if (_i>0 && _i<3) { 
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner.png");};
 //BA.debugLineNum = 1437;BA.debugLine="Dim ImageView1 As ImageView";
_imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 1438;BA.debugLine="ImageView1.Initialize(\"data_list\")";
_imageview1.Initialize(mostCurrent.activityBA,"data_list");
 //BA.debugLineNum = 1439;BA.debugLine="PanelHeight=12%y";
_panelheight = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA);
 //BA.debugLineNum = 1441;BA.debugLine="Panel0.AddView(ImageView1,1%x,PanelTop,71%x,Pane";
_panel0.AddView((android.view.View)(_imageview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),_paneltop,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 1442;BA.debugLine="ImageView1.Tag=i&\"1\"";
_imageview1.setTag((Object)(BA.NumberToString(_i)+"1"));
 //BA.debugLineNum = 1443;BA.debugLine="ImageView1.Bitmap=Bitmap1";
_imageview1.setBitmap((android.graphics.Bitmap)(_bitmap1.getObject()));
 //BA.debugLineNum = 1444;BA.debugLine="ImageView1.Gravity=Gravity.fill";
_imageview1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 1446;BA.debugLine="Dim Label1, Label2 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1447;BA.debugLine="Label1.Initialize(\"\")";
_label1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1448;BA.debugLine="Label2.Initialize(\"\")";
_label2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1449;BA.debugLine="Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 1450;BA.debugLine="Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 1452;BA.debugLine="Label1.TextColor= Colors.White";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1453;BA.debugLine="Label1.TextSize= 18";
_label1.setTextSize((float) (18));
 //BA.debugLineNum = 1454;BA.debugLine="Label1.Typeface = Typeface.DEFAULT_BOLD";
_label1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 1455;BA.debugLine="Label1.Gravity=Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1456;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 1457;BA.debugLine="Label1.Text=fullN_llist.Get(i) 'set data from li";
_label1.setText(_fulln_llist.Get(_i));
 //BA.debugLineNum = 1459;BA.debugLine="Label2.TextColor= Colors.White";
_label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1460;BA.debugLine="Label2.TextSize= 15";
_label2.setTextSize((float) (15));
 //BA.debugLineNum = 1461;BA.debugLine="Label2.Gravity=Gravity.CENTER";
_label2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1462;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 1463;BA.debugLine="Label2.Text=location_list.Get(i) 'set data from";
_label2.setText(_location_list.Get(_i));
 //BA.debugLineNum = 1466;BA.debugLine="If i > id_list.size-1 Then i = id_list.size-1";
if (_i>_id_list.getSize()-1) { 
_i = (int) (_id_list.getSize()-1);};
 //BA.debugLineNum = 1469;BA.debugLine="PanelTop=PanelTop+PanelHeight";
_paneltop = (int) (_paneltop+_panelheight);
 }
};
 //BA.debugLineNum = 1471;BA.debugLine="Panel0.Height=PanelTop";
_panel0.setHeight(_paneltop);
 //BA.debugLineNum = 1473;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1474;BA.debugLine="dialog_panel.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._dialog_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1476;BA.debugLine="Dim dialog_panel_can_btn As Button";
_dialog_panel_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1477;BA.debugLine="Dim dialog_panel_tittle As Label";
_dialog_panel_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1478;BA.debugLine="Dim btn_panel As Panel";
_btn_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1479;BA.debugLine="btn_panel.Initialize(\"\")";
_btn_panel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1480;BA.debugLine="btn_panel.SetBackgroundImage(LoadBitmap(File.DirA";
_btn_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 1481;BA.debugLine="dialog_panel_can_btn.Initialize(\"dialog_panel_can";
_dialog_panel_can_btn.Initialize(mostCurrent.activityBA,"dialog_panel_can_btn");
 //BA.debugLineNum = 1482;BA.debugLine="dialog_panel_tittle.Initialize(\"dialog_panel_titt";
_dialog_panel_tittle.Initialize(mostCurrent.activityBA,"dialog_panel_tittle");
 //BA.debugLineNum = 1483;BA.debugLine="dialog_panel_tittle.Text = \"LIST OF PEOPLE\"";
_dialog_panel_tittle.setText((Object)("LIST OF PEOPLE"));
 //BA.debugLineNum = 1484;BA.debugLine="dialog_panel_can_btn.Text = \"SEARCH\"";
_dialog_panel_can_btn.setText((Object)("SEARCH"));
 //BA.debugLineNum = 1485;BA.debugLine="dialog_panel_can_btn.Typeface = Typeface.LoadFro";
_dialog_panel_can_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1486;BA.debugLine="dialog_panel_tittle.Typeface = Typeface.LoadFrom";
_dialog_panel_tittle.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 1487;BA.debugLine="Dim se_btn As GradientDrawable";
_se_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1488;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1489;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1490;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1491;BA.debugLine="se_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_se_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1492;BA.debugLine="se_btn.CornerRadius = 50dip";
_se_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1493;BA.debugLine="dialog_panel_can_btn.Background = se_btn";
_dialog_panel_can_btn.setBackground((android.graphics.drawable.Drawable)(_se_btn.getObject()));
 //BA.debugLineNum = 1494;BA.debugLine="dialog_panel_tittle.TextSize = 30";
_dialog_panel_tittle.setTextSize((float) (30));
 //BA.debugLineNum = 1495;BA.debugLine="dialog_panel_tittle.Gravity = Gravity.CENTER";
_dialog_panel_tittle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1496;BA.debugLine="dialog_panel_can_btn.Gravity = Gravity.CENTER";
_dialog_panel_can_btn.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1497;BA.debugLine="dialog_panel.AddView(dialog_panel_tittle,1%x,2%y,";
mostCurrent._dialog_panel.AddView((android.view.View)(_dialog_panel_tittle.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (83),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1498;BA.debugLine="dialog_panel.AddView(scrolllista,5%x,dialog_panel";
mostCurrent._dialog_panel.AddView((android.view.View)(mostCurrent._scrolllista.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_dialog_panel_tittle.getTop()+_dialog_panel_tittle.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (69),mostCurrent.activityBA));
 //BA.debugLineNum = 1499;BA.debugLine="dialog_panel.AddView(btn_panel,1%x,79%y,83%x,10%y";
mostCurrent._dialog_panel.AddView((android.view.View)(_btn_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (83),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 1500;BA.debugLine="btn_panel.AddView(dialog_panel_can_btn,((btn_pane";
_btn_panel.AddView((android.view.View)(_dialog_panel_can_btn.getObject()),(int) (((_btn_panel.getWidth()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (42),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1501;BA.debugLine="dialog_all_panel.AddView(dialog_panel,7.5%x,5%y,8";
mostCurrent._dialog_all_panel.AddView((android.view.View)(mostCurrent._dialog_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7.5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 1502;BA.debugLine="dialog_all_panel.Color = Colors.ARGB(128,128,128,";
mostCurrent._dialog_all_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (128),(int) (128),(int) (.50)));
 //BA.debugLineNum = 1503;BA.debugLine="Activity.AddView(dialog_all_panel,0,0,100%x,100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._dialog_all_panel.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1508;BA.debugLine="End Sub";
return "";
}
public static String  _load_list() throws Exception{
 //BA.debugLineNum = 199;BA.debugLine="Sub load_list";
 //BA.debugLineNum = 200;BA.debugLine="list_bloodgroup.Initialize";
_list_bloodgroup.Initialize();
 //BA.debugLineNum = 201;BA.debugLine="list_bloodgroup.Add(\"A+\")";
_list_bloodgroup.Add((Object)("A+"));
 //BA.debugLineNum = 202;BA.debugLine="list_bloodgroup.Add(\"B+\")";
_list_bloodgroup.Add((Object)("B+"));
 //BA.debugLineNum = 203;BA.debugLine="list_bloodgroup.Add(\"O+\")";
_list_bloodgroup.Add((Object)("O+"));
 //BA.debugLineNum = 204;BA.debugLine="list_bloodgroup.Add(\"AB+\")";
_list_bloodgroup.Add((Object)("AB+"));
 //BA.debugLineNum = 209;BA.debugLine="list_bloodgroup.Add(\"A-\")";
_list_bloodgroup.Add((Object)("A-"));
 //BA.debugLineNum = 210;BA.debugLine="list_bloodgroup.Add(\"B-\")";
_list_bloodgroup.Add((Object)("B-"));
 //BA.debugLineNum = 211;BA.debugLine="list_bloodgroup.Add(\"O-\")";
_list_bloodgroup.Add((Object)("O-"));
 //BA.debugLineNum = 212;BA.debugLine="list_bloodgroup.Add(\"AB-\")";
_list_bloodgroup.Add((Object)("AB-"));
 //BA.debugLineNum = 213;BA.debugLine="search_spiner.AddAll(list_bloodgroup)";
mostCurrent._search_spiner.AddAll(_list_bloodgroup);
 //BA.debugLineNum = 214;BA.debugLine="spin_item_click = \"A+\"";
mostCurrent._spin_item_click = "A+";
 //BA.debugLineNum = 215;BA.debugLine="End Sub";
return "";
}
public static String  _lv_itemclick(int _position,Object _value) throws Exception{
bloodlife.system.calculations _calc = null;
 //BA.debugLineNum = 626;BA.debugLine="Sub lv_ItemClick (Position As Int, Value As Object";
 //BA.debugLineNum = 627;BA.debugLine="Dim calc As calculations";
_calc = new bloodlife.system.calculations();
 //BA.debugLineNum = 628;BA.debugLine="calc.users_id = id_list.Get(Position)";
_calc._users_id = BA.ObjectToString(_id_list.Get(_position));
 //BA.debugLineNum = 631;BA.debugLine="End Sub";
return "";
}
public static String  _map_shows_pagefinished(String _url) throws Exception{
 //BA.debugLineNum = 428;BA.debugLine="Sub map_shows_PageFinished (Url As String)";
 //BA.debugLineNum = 429;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 430;BA.debugLine="End Sub";
return "";
}
public static String  _ok_vie_btn_click() throws Exception{
 //BA.debugLineNum = 1049;BA.debugLine="Sub ok_vie_btn_click";
 //BA.debugLineNum = 1050;BA.debugLine="list_all_select = 0";
_list_all_select = (int) (0);
 //BA.debugLineNum = 1051;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 1052;BA.debugLine="End Sub";
return "";
}
public static String  _ok_vie_btn_longclick() throws Exception{
 //BA.debugLineNum = 1053;BA.debugLine="Sub ok_vie_btn_LongClick";
 //BA.debugLineNum = 1054;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 1055;BA.debugLine="End Sub";
return "";
}
public static String  _phone1_view_call_click() throws Exception{
int _choose = 0;
anywheresoftware.b4a.phone.Phone.PhoneCalls _ph = null;
 //BA.debugLineNum = 771;BA.debugLine="Sub phone1_view_call_click";
 //BA.debugLineNum = 772;BA.debugLine="ph1_a1.Start(ph1_pnl)";
mostCurrent._ph1_a1.Start((android.view.View)(mostCurrent._ph1_pnl.getObject()));
 //BA.debugLineNum = 773;BA.debugLine="Dim choose As Int";
_choose = 0;
 //BA.debugLineNum = 774;BA.debugLine="choose = Msgbox2(\"\"&phone1.Text,\"Phone Number: \",";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2(""+mostCurrent._phone1.getText(),"Phone Number: ","CALL","","CANCEL",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 775;BA.debugLine="If choose == DialogResponse .POSITIVE Then";
if (_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 776;BA.debugLine="Dim ph As PhoneCalls";
_ph = new anywheresoftware.b4a.phone.Phone.PhoneCalls();
 //BA.debugLineNum = 777;BA.debugLine="StartActivity(ph.Call(phone1.Text))";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_ph.Call(mostCurrent._phone1.getText())));
 }else {
 };
 //BA.debugLineNum = 780;BA.debugLine="End Sub";
return "";
}
public static String  _phone2_view_call_click() throws Exception{
int _choose = 0;
anywheresoftware.b4a.phone.Phone.PhoneCalls _ph = null;
 //BA.debugLineNum = 781;BA.debugLine="Sub phone2_view_call_click";
 //BA.debugLineNum = 782;BA.debugLine="ph2_a2.Start(ph2_pnl)";
mostCurrent._ph2_a2.Start((android.view.View)(mostCurrent._ph2_pnl.getObject()));
 //BA.debugLineNum = 783;BA.debugLine="Dim choose As Int";
_choose = 0;
 //BA.debugLineNum = 784;BA.debugLine="choose = Msgbox2(\"\"&phone2.Text,\"Phone Number: \",";
_choose = anywheresoftware.b4a.keywords.Common.Msgbox2(""+mostCurrent._phone2.getText(),"Phone Number: ","CALL","","CANCEL",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 785;BA.debugLine="If choose == DialogResponse .POSITIVE Then";
if (_choose==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 786;BA.debugLine="Dim ph As PhoneCalls";
_ph = new anywheresoftware.b4a.phone.Phone.PhoneCalls();
 //BA.debugLineNum = 787;BA.debugLine="StartActivity(ph.Call(phone2.Text))";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_ph.Call(mostCurrent._phone2.getText())));
 }else {
 };
 //BA.debugLineNum = 790;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Dim list_bloodgroup As List";
_list_bloodgroup = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 11;BA.debugLine="Dim sqlLite As SQL";
_sqllite = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 12;BA.debugLine="database_init";
_database_init();
 //BA.debugLineNum = 13;BA.debugLine="Dim id_list As List";
_id_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Dim bday_list As List";
_bday_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 15;BA.debugLine="Dim bloodtype_list As List";
_bloodtype_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 16;BA.debugLine="Dim fullN_llist As List";
_fulln_llist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 17;BA.debugLine="Dim location_list As List";
_location_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 18;BA.debugLine="Dim lat_list As List";
_lat_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 19;BA.debugLine="Dim lng_list As List";
_lng_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 20;BA.debugLine="Dim donated_list As List";
_donated_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 21;BA.debugLine="Dim email_list As List";
_email_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 22;BA.debugLine="Dim nickname_list As List";
_nickname_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 23;BA.debugLine="Dim phone1_list As List";
_phone1_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 24;BA.debugLine="Dim phone2_list As List";
_phone2_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 25;BA.debugLine="Dim image_list As List";
_image_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 26;BA.debugLine="Dim age_list As List";
_age_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 27;BA.debugLine="Dim gender_list As List";
_gender_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 28;BA.debugLine="Dim is_complete As Int : is_complete = 0";
_is_complete = 0;
 //BA.debugLineNum = 28;BA.debugLine="Dim is_complete As Int : is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 30;BA.debugLine="Dim all_users_data_search_list As List";
_all_users_data_search_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 32;BA.debugLine="Dim gpsClient As GPS";
_gpsclient = new anywheresoftware.b4a.gps.GPS();
 //BA.debugLineNum = 33;BA.debugLine="Dim userLocation As Location";
_userlocation = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim is_check_true As Boolean : is_check_true = Fal";
_is_check_true = false;
 //BA.debugLineNum = 34;BA.debugLine="Dim is_check_true As Boolean : is_check_true = Fal";
_is_check_true = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 36;BA.debugLine="Dim row_click As String";
_row_click = "";
 //BA.debugLineNum = 38;BA.debugLine="Dim earth_radius As Float	: earth_radius = 6373 '";
_earth_radius = 0f;
 //BA.debugLineNum = 38;BA.debugLine="Dim earth_radius As Float	: earth_radius = 6373 '";
_earth_radius = (float) (6373);
 //BA.debugLineNum = 39;BA.debugLine="Dim pi As Float : pi = 3.1416 'the default value";
_pi = 0f;
 //BA.debugLineNum = 39;BA.debugLine="Dim pi As Float : pi = 3.1416 'the default value";
_pi = (float) (3.1416);
 //BA.debugLineNum = 41;BA.debugLine="Private clicked_list_all As Int : clicked_list_al";
_clicked_list_all = 0;
 //BA.debugLineNum = 41;BA.debugLine="Private clicked_list_all As Int : clicked_list_al";
_clicked_list_all = (int) (0);
 //BA.debugLineNum = 42;BA.debugLine="Private list_all_select As Int : list_all_select";
_list_all_select = 0;
 //BA.debugLineNum = 42;BA.debugLine="Private list_all_select As Int : list_all_select";
_list_all_select = (int) (0);
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _reading_txt() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _textreader = null;
String _line = "";
int _id_c = 0;
int _fn_c = 0;
int _loc_c = 0;
int _lat_c = 0;
int _lng_c = 0;
int _don_c = 0;
int _email_c = 0;
int _nick_c = 0;
int _ph1_c = 0;
int _ph2_c = 0;
int _img_c = 0;
int _age_c = 0;
int _gend_c = 0;
int _blood_c = 0;
int _bday_c = 0;
 //BA.debugLineNum = 1119;BA.debugLine="Sub reading_txt";
 //BA.debugLineNum = 1120;BA.debugLine="id_list.Initialize";
_id_list.Initialize();
 //BA.debugLineNum = 1121;BA.debugLine="fullN_llist.Initialize";
_fulln_llist.Initialize();
 //BA.debugLineNum = 1122;BA.debugLine="location_list.Initialize";
_location_list.Initialize();
 //BA.debugLineNum = 1123;BA.debugLine="lat_list.Initialize";
_lat_list.Initialize();
 //BA.debugLineNum = 1124;BA.debugLine="lng_list.Initialize";
_lng_list.Initialize();
 //BA.debugLineNum = 1125;BA.debugLine="donated_list.Initialize";
_donated_list.Initialize();
 //BA.debugLineNum = 1126;BA.debugLine="email_list.Initialize";
_email_list.Initialize();
 //BA.debugLineNum = 1127;BA.debugLine="nickname_list.Initialize";
_nickname_list.Initialize();
 //BA.debugLineNum = 1128;BA.debugLine="phone1_list.Initialize";
_phone1_list.Initialize();
 //BA.debugLineNum = 1129;BA.debugLine="phone2_list.Initialize";
_phone2_list.Initialize();
 //BA.debugLineNum = 1130;BA.debugLine="image_list.Initialize";
_image_list.Initialize();
 //BA.debugLineNum = 1131;BA.debugLine="age_list.Initialize";
_age_list.Initialize();
 //BA.debugLineNum = 1132;BA.debugLine="gender_list.Initialize";
_gender_list.Initialize();
 //BA.debugLineNum = 1133;BA.debugLine="bloodtype_list.Initialize";
_bloodtype_list.Initialize();
 //BA.debugLineNum = 1134;BA.debugLine="bday_list.Initialize";
_bday_list.Initialize();
 //BA.debugLineNum = 1136;BA.debugLine="all_users_data_search_list.Initialize";
_all_users_data_search_list.Initialize();
 //BA.debugLineNum = 1138;BA.debugLine="Dim TextReader As TextReader";
_textreader = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1139;BA.debugLine="TextReader.Initialize(File.OpenInput(File.DirI";
_textreader.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_all_users_data.txt").getObject()));
 //BA.debugLineNum = 1140;BA.debugLine="Dim line As String";
_line = "";
 //BA.debugLineNum = 1141;BA.debugLine="line = TextReader.ReadLine";
_line = _textreader.ReadLine();
 //BA.debugLineNum = 1142;BA.debugLine="Do While line <> Null";
while (_line!= null) {
 //BA.debugLineNum = 1143;BA.debugLine="all_users_data_search_list.Add(line)";
_all_users_data_search_list.Add((Object)(_line));
 //BA.debugLineNum = 1144;BA.debugLine="line = TextReader.ReadLine";
_line = _textreader.ReadLine();
 }
;
 //BA.debugLineNum = 1146;BA.debugLine="TextReader.Close";
_textreader.Close();
 //BA.debugLineNum = 1148;BA.debugLine="Dim id_c, fn_c, loc_c, lat_c, lng_c, don_c, email";
_id_c = 0;
_fn_c = 0;
_loc_c = 0;
_lat_c = 0;
_lng_c = 0;
_don_c = 0;
_email_c = 0;
_nick_c = 0;
_ph1_c = 0;
_ph2_c = 0;
_img_c = 0;
_age_c = 0;
_gend_c = 0;
_blood_c = 0;
_bday_c = 0;
 //BA.debugLineNum = 1151;BA.debugLine="For fn_c = 0 To all_users_data_search_list.Size -";
{
final int step27 = 1;
final int limit27 = (int) (_all_users_data_search_list.getSize()-1);
for (_fn_c = (int) (0) ; (step27 > 0 && _fn_c <= limit27) || (step27 < 0 && _fn_c >= limit27); _fn_c = ((int)(0 + _fn_c + step27)) ) {
 //BA.debugLineNum = 1152;BA.debugLine="If fn_c mod 15 = 0 Then";
if (_fn_c%15==0) { 
 //BA.debugLineNum = 1153;BA.debugLine="fullN_llist.Add(all_users_data_search_list.Get(";
_fulln_llist.Add(_all_users_data_search_list.Get(_fn_c));
 };
 }
};
 //BA.debugLineNum = 1158;BA.debugLine="For blood_c = 0 To all_users_data_search_list.Siz";
{
final int step32 = 1;
final int limit32 = (int) (_all_users_data_search_list.getSize()-1);
for (_blood_c = (int) (0) ; (step32 > 0 && _blood_c <= limit32) || (step32 < 0 && _blood_c >= limit32); _blood_c = ((int)(0 + _blood_c + step32)) ) {
 //BA.debugLineNum = 1159;BA.debugLine="If blood_c mod 15 = 1 Then";
if (_blood_c%15==1) { 
 //BA.debugLineNum = 1160;BA.debugLine="bloodtype_list.Add(all_users_data_search_list.G";
_bloodtype_list.Add(_all_users_data_search_list.Get(_blood_c));
 };
 }
};
 //BA.debugLineNum = 1165;BA.debugLine="For email_c = 0 To all_users_data_search_list.Siz";
{
final int step37 = 1;
final int limit37 = (int) (_all_users_data_search_list.getSize()-1);
for (_email_c = (int) (0) ; (step37 > 0 && _email_c <= limit37) || (step37 < 0 && _email_c >= limit37); _email_c = ((int)(0 + _email_c + step37)) ) {
 //BA.debugLineNum = 1166;BA.debugLine="If email_c mod 15 = 2 Then";
if (_email_c%15==2) { 
 //BA.debugLineNum = 1167;BA.debugLine="email_list.Add(all_users_data_search_list.Get(e";
_email_list.Add(_all_users_data_search_list.Get(_email_c));
 };
 }
};
 //BA.debugLineNum = 1171;BA.debugLine="For ph1_c = 0 To all_users_data_search_list.Size";
{
final int step42 = 1;
final int limit42 = (int) (_all_users_data_search_list.getSize()-1);
for (_ph1_c = (int) (0) ; (step42 > 0 && _ph1_c <= limit42) || (step42 < 0 && _ph1_c >= limit42); _ph1_c = ((int)(0 + _ph1_c + step42)) ) {
 //BA.debugLineNum = 1172;BA.debugLine="If ph1_c mod 15 = 3 Then";
if (_ph1_c%15==3) { 
 //BA.debugLineNum = 1173;BA.debugLine="phone1_list.Add(all_users_data_search_list.Get(";
_phone1_list.Add(_all_users_data_search_list.Get(_ph1_c));
 };
 }
};
 //BA.debugLineNum = 1177;BA.debugLine="For ph2_c = 0 To all_users_data_search_list.Size";
{
final int step47 = 1;
final int limit47 = (int) (_all_users_data_search_list.getSize()-1);
for (_ph2_c = (int) (0) ; (step47 > 0 && _ph2_c <= limit47) || (step47 < 0 && _ph2_c >= limit47); _ph2_c = ((int)(0 + _ph2_c + step47)) ) {
 //BA.debugLineNum = 1178;BA.debugLine="If ph2_c mod 15 = 4 Then";
if (_ph2_c%15==4) { 
 //BA.debugLineNum = 1179;BA.debugLine="phone2_list.Add(all_users_data_search_list.Get(";
_phone2_list.Add(_all_users_data_search_list.Get(_ph2_c));
 };
 }
};
 //BA.debugLineNum = 1183;BA.debugLine="For bday_c = 0 To all_users_data_search_list.Size";
{
final int step52 = 1;
final int limit52 = (int) (_all_users_data_search_list.getSize()-1);
for (_bday_c = (int) (0) ; (step52 > 0 && _bday_c <= limit52) || (step52 < 0 && _bday_c >= limit52); _bday_c = ((int)(0 + _bday_c + step52)) ) {
 //BA.debugLineNum = 1184;BA.debugLine="If bday_c mod 15 = 5 Then";
if (_bday_c%15==5) { 
 //BA.debugLineNum = 1185;BA.debugLine="bday_list.Add(all_users_data_search_list.Get(bd";
_bday_list.Add(_all_users_data_search_list.Get(_bday_c));
 };
 }
};
 //BA.debugLineNum = 1189;BA.debugLine="For loc_c = 0 To all_users_data_search_list.Size";
{
final int step57 = 1;
final int limit57 = (int) (_all_users_data_search_list.getSize()-1);
for (_loc_c = (int) (0) ; (step57 > 0 && _loc_c <= limit57) || (step57 < 0 && _loc_c >= limit57); _loc_c = ((int)(0 + _loc_c + step57)) ) {
 //BA.debugLineNum = 1190;BA.debugLine="If loc_c mod 15 = 6 Then";
if (_loc_c%15==6) { 
 //BA.debugLineNum = 1191;BA.debugLine="location_list.Add(all_users_data_search_list.Ge";
_location_list.Add(_all_users_data_search_list.Get(_loc_c));
 };
 }
};
 //BA.debugLineNum = 1195;BA.debugLine="For nick_c = 0 To all_users_data_search_list.Size";
{
final int step62 = 1;
final int limit62 = (int) (_all_users_data_search_list.getSize()-1);
for (_nick_c = (int) (0) ; (step62 > 0 && _nick_c <= limit62) || (step62 < 0 && _nick_c >= limit62); _nick_c = ((int)(0 + _nick_c + step62)) ) {
 //BA.debugLineNum = 1196;BA.debugLine="If nick_c mod 15 = 7 Then";
if (_nick_c%15==7) { 
 //BA.debugLineNum = 1197;BA.debugLine="nickname_list.Add(all_users_data_search_list.Ge";
_nickname_list.Add(_all_users_data_search_list.Get(_nick_c));
 };
 }
};
 //BA.debugLineNum = 1201;BA.debugLine="For don_c = 0 To all_users_data_search_list.Size";
{
final int step67 = 1;
final int limit67 = (int) (_all_users_data_search_list.getSize()-1);
for (_don_c = (int) (0) ; (step67 > 0 && _don_c <= limit67) || (step67 < 0 && _don_c >= limit67); _don_c = ((int)(0 + _don_c + step67)) ) {
 //BA.debugLineNum = 1202;BA.debugLine="If don_c mod 15 = 8 Then";
if (_don_c%15==8) { 
 //BA.debugLineNum = 1203;BA.debugLine="donated_list.Add(all_users_data_search_list.Get";
_donated_list.Add(_all_users_data_search_list.Get(_don_c));
 };
 }
};
 //BA.debugLineNum = 1207;BA.debugLine="For gend_c = 0 To all_users_data_search_list.Size";
{
final int step72 = 1;
final int limit72 = (int) (_all_users_data_search_list.getSize()-1);
for (_gend_c = (int) (0) ; (step72 > 0 && _gend_c <= limit72) || (step72 < 0 && _gend_c >= limit72); _gend_c = ((int)(0 + _gend_c + step72)) ) {
 //BA.debugLineNum = 1208;BA.debugLine="If gend_c mod 15 = 9 Then";
if (_gend_c%15==9) { 
 //BA.debugLineNum = 1209;BA.debugLine="gender_list.Add(all_users_data_search_list.Get(";
_gender_list.Add(_all_users_data_search_list.Get(_gend_c));
 };
 }
};
 //BA.debugLineNum = 1213;BA.debugLine="For id_c = 0 To all_users_data_search_list.Size -";
{
final int step77 = 1;
final int limit77 = (int) (_all_users_data_search_list.getSize()-1);
for (_id_c = (int) (0) ; (step77 > 0 && _id_c <= limit77) || (step77 < 0 && _id_c >= limit77); _id_c = ((int)(0 + _id_c + step77)) ) {
 //BA.debugLineNum = 1214;BA.debugLine="If id_c mod 15 = 10 Then";
if (_id_c%15==10) { 
 //BA.debugLineNum = 1215;BA.debugLine="id_list.Add(all_users_data_search_list.Get(id_c";
_id_list.Add(_all_users_data_search_list.Get(_id_c));
 };
 }
};
 //BA.debugLineNum = 1219;BA.debugLine="For age_c = 0 To all_users_data_search_list.Size";
{
final int step82 = 1;
final int limit82 = (int) (_all_users_data_search_list.getSize()-1);
for (_age_c = (int) (0) ; (step82 > 0 && _age_c <= limit82) || (step82 < 0 && _age_c >= limit82); _age_c = ((int)(0 + _age_c + step82)) ) {
 //BA.debugLineNum = 1220;BA.debugLine="If age_c mod 15 = 11 Then";
if (_age_c%15==11) { 
 //BA.debugLineNum = 1221;BA.debugLine="age_list.Add(all_users_data_search_list.Get(age";
_age_list.Add(_all_users_data_search_list.Get(_age_c));
 };
 }
};
 //BA.debugLineNum = 1225;BA.debugLine="For lat_c = 0 To all_users_data_search_list.Size";
{
final int step87 = 1;
final int limit87 = (int) (_all_users_data_search_list.getSize()-1);
for (_lat_c = (int) (0) ; (step87 > 0 && _lat_c <= limit87) || (step87 < 0 && _lat_c >= limit87); _lat_c = ((int)(0 + _lat_c + step87)) ) {
 //BA.debugLineNum = 1226;BA.debugLine="If lat_c mod 15 = 12 Then";
if (_lat_c%15==12) { 
 //BA.debugLineNum = 1227;BA.debugLine="lat_list.Add(all_users_data_search_list.Get(lat";
_lat_list.Add(_all_users_data_search_list.Get(_lat_c));
 };
 }
};
 //BA.debugLineNum = 1231;BA.debugLine="For lng_c = 0 To all_users_data_search_list.Size";
{
final int step92 = 1;
final int limit92 = (int) (_all_users_data_search_list.getSize()-1);
for (_lng_c = (int) (0) ; (step92 > 0 && _lng_c <= limit92) || (step92 < 0 && _lng_c >= limit92); _lng_c = ((int)(0 + _lng_c + step92)) ) {
 //BA.debugLineNum = 1232;BA.debugLine="If lng_c mod 15 = 13 Then";
if (_lng_c%15==13) { 
 //BA.debugLineNum = 1233;BA.debugLine="lng_list.Add(all_users_data_search_list.Get(lng";
_lng_list.Add(_all_users_data_search_list.Get(_lng_c));
 };
 }
};
 //BA.debugLineNum = 1237;BA.debugLine="For img_c = 0 To all_users_data_search_list.Size";
{
final int step97 = 1;
final int limit97 = (int) (_all_users_data_search_list.getSize()-1);
for (_img_c = (int) (0) ; (step97 > 0 && _img_c <= limit97) || (step97 < 0 && _img_c >= limit97); _img_c = ((int)(0 + _img_c + step97)) ) {
 //BA.debugLineNum = 1238;BA.debugLine="If img_c mod 15 = 14 Then";
if (_img_c%15==14) { 
 //BA.debugLineNum = 1239;BA.debugLine="image_list.Add(all_users_data_search_list.Get(i";
_image_list.Add(_all_users_data_search_list.Get(_img_c));
 };
 }
};
 //BA.debugLineNum = 1243;BA.debugLine="End Sub";
return "";
}
public static String  _search_btn_click() throws Exception{
bloodlife.system.calculations _url_back = null;
String _search_all_users_data = "";
 //BA.debugLineNum = 251;BA.debugLine="Sub search_btn_Click";
 //BA.debugLineNum = 252;BA.debugLine="a2.Start(search_btn)";
mostCurrent._a2.Start((android.view.View)(mostCurrent._search_btn.getObject()));
 //BA.debugLineNum = 253;BA.debugLine="ProgressDialogShow2(\"please wait...\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"please wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 254;BA.debugLine="isGPSon.Enabled = True";
mostCurrent._isgpson.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 255;BA.debugLine="Dim url_back As calculations";
_url_back = new bloodlife.system.calculations();
 //BA.debugLineNum = 256;BA.debugLine="Dim search_all_users_data As String";
_search_all_users_data = "";
 //BA.debugLineNum = 257;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 258;BA.debugLine="search_all_users_data = url_back.php_email_url(\"s";
_search_all_users_data = _url_back._php_email_url("search_all_users_data_list.php");
 //BA.debugLineNum = 262;BA.debugLine="data_query_all_users_datas.Download2(search_all_u";
mostCurrent._data_query_all_users_datas._download2(_search_all_users_data,new String[]{"all_info","SELECT * FROM person_info where blood_type='"+mostCurrent._spin_item_click+"' and not `id`="+mostCurrent._login_form._id_query+";"});
 //BA.debugLineNum = 264;BA.debugLine="End Sub";
return "";
}
public static String  _search_spiner_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 623;BA.debugLine="Sub search_spiner_ItemClick (Position As Int, Valu";
 //BA.debugLineNum = 624;BA.debugLine="spin_item_click = search_spiner.GetItem(Position)";
mostCurrent._spin_item_click = mostCurrent._search_spiner.GetItem(_position).trim();
 //BA.debugLineNum = 625;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1059;BA.debugLine="Sub user_image_click";
 //BA.debugLineNum = 1060;BA.debugLine="userI_a3.Start(user_image)";
mostCurrent._useri_a3.Start((android.view.View)(mostCurrent._user_image.getObject()));
 //BA.debugLineNum = 1061;BA.debugLine="If user_img_panl.IsInitialized == True Then";
if (mostCurrent._user_img_panl.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1062;BA.debugLine="user_img_panl.RemoveView";
mostCurrent._user_img_panl.RemoveView();
 };
 //BA.debugLineNum = 1064;BA.debugLine="user_img_panl.Initialize(\"user_img_panl\")";
mostCurrent._user_img_panl.Initialize(mostCurrent.activityBA,"user_img_panl");
 //BA.debugLineNum = 1065;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1066;BA.debugLine="Dim user_imgClose_btn As Button";
_user_imgclose_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1067;BA.debugLine="Dim img_user_webview As WebView";
_img_user_webview = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 1068;BA.debugLine="Dim user_img_view As ImageView";
_user_img_view = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 1069;BA.debugLine="user_imgClose_btn.Initialize(\"user_imgClose_btn\")";
_user_imgclose_btn.Initialize(mostCurrent.activityBA,"user_imgClose_btn");
 //BA.debugLineNum = 1070;BA.debugLine="user_img_view.Initialize(\"user_img_view\")";
_user_img_view.Initialize(mostCurrent.activityBA,"user_img_view");
 //BA.debugLineNum = 1071;BA.debugLine="user_imgClose_btn.Text = \"CLOSE\"";
_user_imgclose_btn.setText((Object)("CLOSE"));
 //BA.debugLineNum = 1072;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1073;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1074;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1075;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1076;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1077;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1078;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1079;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1080;BA.debugLine="user_imgClose_btn.Background = V_btn";
_user_imgclose_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 1081;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1082;BA.debugLine="img_user_webview.Initialize(\"img_user_webview\")";
_img_user_webview.Initialize(mostCurrent.activityBA,"img_user_webview");
 //BA.debugLineNum = 1083;BA.debugLine="user_img_panl.Color = Colors.Transparent";
mostCurrent._user_img_panl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1084;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1086;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 1087;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 1088;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 1089;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 1090;BA.debugLine="bytes = su.DecodeBase64(image_list.Get(row_click)";
_bytes = _su.DecodeBase64(BA.ObjectToString(_image_list.Get((int)(Double.parseDouble(_row_click)))));
 //BA.debugLineNum = 1091;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 1092;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 1094;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 1095;BA.debugLine="bd.Initialize(bmp)";
_bd.Initialize((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 1097;BA.debugLine="user_img_view.Background = bd";
_user_img_view.setBackground((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 1102;BA.debugLine="pnl.AddView(user_img_view,1%x,1%y,86%x,75%y)";
_pnl.AddView((android.view.View)(_user_img_view.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (86),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (75),mostCurrent.activityBA));
 //BA.debugLineNum = 1103;BA.debugLine="pnl.AddView(user_imgClose_btn,1%x,user_img_view.T";
_pnl.AddView((android.view.View)(_user_imgclose_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_user_img_view.getTop()+_user_img_view.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (86),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1104;BA.debugLine="user_img_panl.AddView(pnl,6%x,(((((Activity.Heigh";
mostCurrent._user_img_panl.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),(int) ((((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (88),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (89),mostCurrent.activityBA));
 //BA.debugLineNum = 1105;BA.debugLine="user_img_panl.BringToFront";
mostCurrent._user_img_panl.BringToFront();
 //BA.debugLineNum = 1108;BA.debugLine="Activity.AddView(user_img_panl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._user_img_panl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1109;BA.debugLine="End Sub";
return "";
}
public static String  _user_img_panl_click() throws Exception{
 //BA.debugLineNum = 1113;BA.debugLine="Sub user_img_panl_click";
 //BA.debugLineNum = 1115;BA.debugLine="End Sub";
return "";
}
public static String  _user_imgclose_btn_click() throws Exception{
 //BA.debugLineNum = 1110;BA.debugLine="Sub user_imgClose_btn_click";
 //BA.debugLineNum = 1111;BA.debugLine="user_img_panl.RemoveView";
mostCurrent._user_img_panl.RemoveView();
 //BA.debugLineNum = 1112;BA.debugLine="End Sub";
return "";
}
public static String  _vie_btn_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _view_panl = null;
anywheresoftware.b4a.objects.PanelWrapper _view_for_image = null;
anywheresoftware.b4a.objects.PanelWrapper _view_for_btn = null;
anywheresoftware.b4a.objects.LabelWrapper _tittle = null;
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
anywheresoftware.b4a.objects.ImageViewWrapper _bookmark_img = null;
anywheresoftware.b4a.objects.ButtonWrapper _ok_vie_btn = null;
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
 //BA.debugLineNum = 791;BA.debugLine="Sub vie_btn_click";
 //BA.debugLineNum = 792;BA.debugLine="list_all_select = 1";
_list_all_select = (int) (1);
 //BA.debugLineNum = 793;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 //BA.debugLineNum = 794;BA.debugLine="If view_data_info_person.IsInitialized == True T";
if (mostCurrent._view_data_info_person.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 795;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 796;BA.debugLine="scroll_view_info.RemoveView";
mostCurrent._scroll_view_info.RemoveView();
 }else {
 };
 //BA.debugLineNum = 799;BA.debugLine="view_data_info_person.Initialize(\"view_data_info_";
mostCurrent._view_data_info_person.Initialize(mostCurrent.activityBA,"view_data_info_person");
 //BA.debugLineNum = 800;BA.debugLine="scroll_view_info.Initialize(74%x,57%y,\"scroll_vie";
mostCurrent._scroll_view_info.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (57),mostCurrent.activityBA),"scroll_view_info");
 //BA.debugLineNum = 802;BA.debugLine="Dim view_panl,view_for_image,view_for_btn As Pane";
_view_panl = new anywheresoftware.b4a.objects.PanelWrapper();
_view_for_image = new anywheresoftware.b4a.objects.PanelWrapper();
_view_for_btn = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 803;BA.debugLine="Dim tittle,fullname,location,donated,email,age,ge";
_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
_fullname = new anywheresoftware.b4a.objects.LabelWrapper();
_location = new anywheresoftware.b4a.objects.LabelWrapper();
_donated = new anywheresoftware.b4a.objects.LabelWrapper();
_email = new anywheresoftware.b4a.objects.LabelWrapper();
_age = new anywheresoftware.b4a.objects.LabelWrapper();
_gender = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 804;BA.debugLine="Dim fn_pnl,loc_pnl,don_pnl,ema_pnl,btn_pnl,age_pn";
_fn_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_loc_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_don_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_ema_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_btn_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_age_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_gender_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 805;BA.debugLine="Dim fn_img,loc_img,don_img,ema_img,ph1_img,ph2_im";
_fn_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_loc_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_don_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ema_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ph1_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ph2_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_age_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_gender_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_bookmark_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 806;BA.debugLine="fn_img.Initialize(\"\")";
_fn_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 807;BA.debugLine="loc_img.Initialize(\"\")";
_loc_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 808;BA.debugLine="don_img.Initialize(\"\")";
_don_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 809;BA.debugLine="ema_img.Initialize(\"\")";
_ema_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 810;BA.debugLine="ph1_img.Initialize(\"\")";
_ph1_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 811;BA.debugLine="ph2_img.Initialize(\"\")";
_ph2_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 812;BA.debugLine="user_image.Initialize(\"user_image\")";
mostCurrent._user_image.Initialize(mostCurrent.activityBA,"user_image");
 //BA.debugLineNum = 813;BA.debugLine="age_img.Initialize(\"\")";
_age_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 814;BA.debugLine="gender_img.Initialize(\"\")";
_gender_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 815;BA.debugLine="bookmark_img.Initialize(\"bookmark_img\")";
_bookmark_img.Initialize(mostCurrent.activityBA,"bookmark_img");
 //BA.debugLineNum = 816;BA.debugLine="fn_pnl.Initialize(\"\")";
_fn_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 817;BA.debugLine="loc_pnl.Initialize(\"\")";
_loc_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 818;BA.debugLine="don_pnl.Initialize(\"\")";
_don_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 819;BA.debugLine="ema_pnl.Initialize(\"\")";
_ema_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 820;BA.debugLine="ph1_pnl.Initialize(\"phone1_view_call\")";
mostCurrent._ph1_pnl.Initialize(mostCurrent.activityBA,"phone1_view_call");
 //BA.debugLineNum = 821;BA.debugLine="ph2_pnl.Initialize(\"phone2_view_call\")";
mostCurrent._ph2_pnl.Initialize(mostCurrent.activityBA,"phone2_view_call");
 //BA.debugLineNum = 822;BA.debugLine="btn_pnl.Initialize(\"\")";
_btn_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 823;BA.debugLine="age_pnl.Initialize(\"\")";
_age_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 824;BA.debugLine="gender_pnl.Initialize(\"\")";
_gender_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 825;BA.debugLine="Dim ok_vie_btn As Button";
_ok_vie_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 826;BA.debugLine="ok_vie_btn.Initialize(\"ok_vie_btn\")";
_ok_vie_btn.Initialize(mostCurrent.activityBA,"ok_vie_btn");
 //BA.debugLineNum = 827;BA.debugLine="tittle.Initialize(\"\")";
_tittle.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 828;BA.debugLine="fullname.Initialize(\"\")";
_fullname.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 829;BA.debugLine="location.Initialize(\"\")";
_location.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 830;BA.debugLine="donated.Initialize(\"\")";
_donated.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 831;BA.debugLine="email.Initialize(\"\")";
_email.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 832;BA.debugLine="phone1.Initialize(\"\")";
mostCurrent._phone1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 833;BA.debugLine="phone2.Initialize(\"\")";
mostCurrent._phone2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 834;BA.debugLine="age.Initialize(\"\")";
_age.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 835;BA.debugLine="gender.Initialize(\"\")";
_gender.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 836;BA.debugLine="view_panl.Initialize(\"\")";
_view_panl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 837;BA.debugLine="view_for_image.Initialize(\"\")";
_view_for_image.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 838;BA.debugLine="view_for_btn.Initialize(\"\")";
_view_for_btn.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 839;BA.debugLine="fullname.Text = fullN_llist.Get(row_click)			'str";
_fullname.setText(_fulln_llist.Get((int)(Double.parseDouble(_row_click))));
 //BA.debugLineNum = 840;BA.debugLine="location.Text = \": \"&location_list.Get(row_click)";
_location.setText((Object)(": "+BA.ObjectToString(_location_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 841;BA.debugLine="donated.Text = \": \"&donated_list.Get(row_click)";
_donated.setText((Object)(": "+BA.ObjectToString(_donated_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 842;BA.debugLine="email.Text = \": \"&email_list.Get(row_click)";
_email.setText((Object)(": "+BA.ObjectToString(_email_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 843;BA.debugLine="phone1.Text = \": \"&phone1_list.Get(row_click)";
mostCurrent._phone1.setText((Object)(": "+BA.ObjectToString(_phone1_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 844;BA.debugLine="phone2.Text = \": \"&phone2_list.Get(row_click)";
mostCurrent._phone2.setText((Object)(": "+BA.ObjectToString(_phone2_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 845;BA.debugLine="age.Text = \": \"&age_list.Get(row_click)";
_age.setText((Object)(": "+BA.ObjectToString(_age_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 846;BA.debugLine="gender.Text = \": \"&gender_list.Get(row_click)";
_gender.setText((Object)(": "+BA.ObjectToString(_gender_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 847;BA.debugLine="location.Gravity = Gravity.CENTER_VERTICAL";
_location.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 848;BA.debugLine="donated.Gravity = Gravity.CENTER_VERTICAL";
_donated.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 849;BA.debugLine="email.Gravity = Gravity.CENTER_VERTICAL";
_email.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 850;BA.debugLine="phone1.Gravity = Gravity.CENTER_VERTICAL";
mostCurrent._phone1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 851;BA.debugLine="phone2.Gravity = Gravity.CENTER_VERTICAL";
mostCurrent._phone2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 852;BA.debugLine="age.Gravity = Gravity.CENTER_VERTICAL";
_age.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 853;BA.debugLine="gender.Gravity = Gravity.CENTER_VERTICAL";
_gender.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 854;BA.debugLine="fullname.TextColor = Colors.Black";
_fullname.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 855;BA.debugLine="location.TextColor = Colors.Black";
_location.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 856;BA.debugLine="donated.TextColor = Colors.Black";
_donated.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 857;BA.debugLine="email.TextColor = Colors.Black";
_email.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 858;BA.debugLine="phone1.TextColor = Colors.Black";
mostCurrent._phone1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 859;BA.debugLine="phone2.TextColor = Colors.Black";
mostCurrent._phone2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 860;BA.debugLine="ok_vie_btn.TextColor = Colors.Black";
_ok_vie_btn.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 861;BA.debugLine="age.TextColor = Colors.Black";
_age.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 862;BA.debugLine="gender.TextColor = Colors.Black";
_gender.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 863;BA.debugLine="location.Typeface = Typeface.LoadFromAssets(\"";
_location.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 864;BA.debugLine="donated.Typeface = Typeface.LoadFromAssets(\"Z";
_donated.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 865;BA.debugLine="email.Typeface = Typeface.LoadFromAssets(\"ZIN";
_email.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 866;BA.debugLine="phone1.Typeface = Typeface.LoadFromAssets(\"ZI";
mostCurrent._phone1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 867;BA.debugLine="phone2.Typeface = Typeface.LoadFromAssets(\"ZI";
mostCurrent._phone2.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 868;BA.debugLine="age.Typeface = Typeface.LoadFromAssets(\"ZINGH";
_age.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 869;BA.debugLine="gender.Typeface = Typeface.LoadFromAssets(\"ZI";
_gender.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 872;BA.debugLine="fullname.Gravity = Gravity.CENTER";
_fullname.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 873;BA.debugLine="ok_vie_btn.Text = \"OK\"";
_ok_vie_btn.setText((Object)("OK"));
 //BA.debugLineNum = 874;BA.debugLine="ok_vie_btn.Typeface = Typeface.LoadFromAssets(\"Hi";
_ok_vie_btn.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("HipHopDemi.ttf"));
 //BA.debugLineNum = 875;BA.debugLine="view_panl.SetBackgroundImage(LoadBitmap(File.DirA";
_view_panl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 882;BA.debugLine="Dim loc_bitd,don_bitd,ema_bitd,ph1_bitd,ph2_bi";
_loc_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_don_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_ema_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_ph1_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_ph2_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_age_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_gender_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 883;BA.debugLine="Dim loc_bit,don_bit,ema_bit,ph1_bit,ph2_bit,ag";
_loc_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_don_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ema_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ph1_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ph2_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_age_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_gender_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 884;BA.debugLine="loc_bit.Initialize(File.DirAssets,\"glyphicons";
_loc_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-21-home.png");
 //BA.debugLineNum = 885;BA.debugLine="don_bit.Initialize(File.DirAssets,\"glyphicons";
_don_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-152-new-window.png");
 //BA.debugLineNum = 886;BA.debugLine="ema_bit.Initialize(File.DirAssets,\"glyphicons";
_ema_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-social-40-e-mail.png");
 //BA.debugLineNum = 887;BA.debugLine="ph1_bit.Initialize(File.DirAssets,\"glyphicons";
_ph1_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt1.png");
 //BA.debugLineNum = 888;BA.debugLine="ph2_bit.Initialize(File.DirAssets,\"glyphicons";
_ph2_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt2.png");
 //BA.debugLineNum = 889;BA.debugLine="age_bit.Initialize(File.DirAssets,\"glyphicons";
_age_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-577-uk-rat-r18.png");
 //BA.debugLineNum = 890;BA.debugLine="gender_bit.Initialize(File.DirAssets,\"glyphic";
_gender_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-25-parents.png");
 //BA.debugLineNum = 891;BA.debugLine="loc_bitd.Initialize(loc_bit)";
_loc_bitd.Initialize((android.graphics.Bitmap)(_loc_bit.getObject()));
 //BA.debugLineNum = 892;BA.debugLine="don_bitd.Initialize(don_bit)";
_don_bitd.Initialize((android.graphics.Bitmap)(_don_bit.getObject()));
 //BA.debugLineNum = 893;BA.debugLine="ema_bitd.Initialize(ema_bit)";
_ema_bitd.Initialize((android.graphics.Bitmap)(_ema_bit.getObject()));
 //BA.debugLineNum = 894;BA.debugLine="ph1_bitd.Initialize(ph1_bit)";
_ph1_bitd.Initialize((android.graphics.Bitmap)(_ph1_bit.getObject()));
 //BA.debugLineNum = 895;BA.debugLine="ph2_bitd.Initialize(ph2_bit)";
_ph2_bitd.Initialize((android.graphics.Bitmap)(_ph2_bit.getObject()));
 //BA.debugLineNum = 896;BA.debugLine="age_bitd.Initialize(age_bit)";
_age_bitd.Initialize((android.graphics.Bitmap)(_age_bit.getObject()));
 //BA.debugLineNum = 897;BA.debugLine="gender_bitd.Initialize(gender_bit)";
_gender_bitd.Initialize((android.graphics.Bitmap)(_gender_bit.getObject()));
 //BA.debugLineNum = 898;BA.debugLine="loc_img.Background = loc_bitd";
_loc_img.setBackground((android.graphics.drawable.Drawable)(_loc_bitd.getObject()));
 //BA.debugLineNum = 899;BA.debugLine="don_img.Background = don_bitd";
_don_img.setBackground((android.graphics.drawable.Drawable)(_don_bitd.getObject()));
 //BA.debugLineNum = 900;BA.debugLine="ema_img.Background = ema_bitd";
_ema_img.setBackground((android.graphics.drawable.Drawable)(_ema_bitd.getObject()));
 //BA.debugLineNum = 901;BA.debugLine="ph1_img.Background = ph1_bitd";
_ph1_img.setBackground((android.graphics.drawable.Drawable)(_ph1_bitd.getObject()));
 //BA.debugLineNum = 902;BA.debugLine="ph2_img.Background = ph2_bitd";
_ph2_img.setBackground((android.graphics.drawable.Drawable)(_ph2_bitd.getObject()));
 //BA.debugLineNum = 903;BA.debugLine="age_img.Background = age_bitd";
_age_img.setBackground((android.graphics.drawable.Drawable)(_age_bitd.getObject()));
 //BA.debugLineNum = 904;BA.debugLine="gender_img.Background = gender_bitd";
_gender_img.setBackground((android.graphics.drawable.Drawable)(_gender_bitd.getObject()));
 //BA.debugLineNum = 905;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 906;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 907;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 908;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 909;BA.debugLine="bytes = su.DecodeBase64(image_list.Get(row_click)";
_bytes = _su.DecodeBase64(BA.ObjectToString(_image_list.Get((int)(Double.parseDouble(_row_click)))));
 //BA.debugLineNum = 910;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 911;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 913;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 914;BA.debugLine="bd.Initialize(bmp)";
_bd.Initialize((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 915;BA.debugLine="user_image.Background = bd";
mostCurrent._user_image.setBackground((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 918;BA.debugLine="Dim fn_grad,don_grad,ema_grad,ph1_grad,ph2_grad";
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
 //BA.debugLineNum = 919;BA.debugLine="Dim colorG(2),btn_color(2),panl_btn(2) As Int";
_colorg = new int[(int) (2)];
;
_btn_color = new int[(int) (2)];
;
_panl_btn = new int[(int) (2)];
;
 //BA.debugLineNum = 920;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 921;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 922;BA.debugLine="btn_color(0) = Colors.Red";
_btn_color[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 923;BA.debugLine="btn_color(1) = Colors.White";
_btn_color[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 924;BA.debugLine="panl_btn(0) = Colors.Gray";
_panl_btn[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Gray;
 //BA.debugLineNum = 925;BA.debugLine="panl_btn(1) = Colors.Red";
_panl_btn[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 926;BA.debugLine="fn_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_fn_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 927;BA.debugLine="don_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_don_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 928;BA.debugLine="ema_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_ema_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 929;BA.debugLine="ph1_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_ph1_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 930;BA.debugLine="ph2_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_ph2_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 931;BA.debugLine="loc_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_loc_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 932;BA.debugLine="age_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_age_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 933;BA.debugLine="gender_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_gender_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 934;BA.debugLine="btn_grad.Initialize(\"TOP_BOTTOM\",panl_btn)";
_btn_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_panl_btn);
 //BA.debugLineNum = 935;BA.debugLine="ok_btn_grad.Initialize(\"TOP_BOTTOM\",btn_color)";
_ok_btn_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_btn_color);
 //BA.debugLineNum = 936;BA.debugLine="fn_grad.CornerRadius = 10dip";
_fn_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 937;BA.debugLine="ok_btn_grad.CornerRadius = 50dip";
_ok_btn_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 938;BA.debugLine="fn_pnl.Background = fn_grad		'fn_pnl.Color = Col";
_fn_pnl.setBackground((android.graphics.drawable.Drawable)(_fn_grad.getObject()));
 //BA.debugLineNum = 939;BA.debugLine="don_pnl.Background = don_grad		'don_pnl.Color =";
_don_pnl.setBackground((android.graphics.drawable.Drawable)(_don_grad.getObject()));
 //BA.debugLineNum = 940;BA.debugLine="ema_pnl.Background = ema_grad		'ema_pnl.Color =";
_ema_pnl.setBackground((android.graphics.drawable.Drawable)(_ema_grad.getObject()));
 //BA.debugLineNum = 941;BA.debugLine="ph1_pnl.Background = ph1_grad		'ph1_pnl.Color =";
mostCurrent._ph1_pnl.setBackground((android.graphics.drawable.Drawable)(_ph1_grad.getObject()));
 //BA.debugLineNum = 942;BA.debugLine="ph2_pnl.Background = ph2_grad		'ph2_pnl.Color =";
mostCurrent._ph2_pnl.setBackground((android.graphics.drawable.Drawable)(_ph2_grad.getObject()));
 //BA.debugLineNum = 943;BA.debugLine="loc_pnl.Background = loc_grad		'loc_pnl.Color =";
_loc_pnl.setBackground((android.graphics.drawable.Drawable)(_loc_grad.getObject()));
 //BA.debugLineNum = 944;BA.debugLine="age_pnl.Background = loc_grad";
_age_pnl.setBackground((android.graphics.drawable.Drawable)(_loc_grad.getObject()));
 //BA.debugLineNum = 945;BA.debugLine="gender_pnl.Background = loc_grad";
_gender_pnl.setBackground((android.graphics.drawable.Drawable)(_loc_grad.getObject()));
 //BA.debugLineNum = 946;BA.debugLine="btn_pnl.Background = btn_grad";
_btn_pnl.setBackground((android.graphics.drawable.Drawable)(_btn_grad.getObject()));
 //BA.debugLineNum = 947;BA.debugLine="ok_vie_btn.Background = ok_btn_grad";
_ok_vie_btn.setBackground((android.graphics.drawable.Drawable)(_ok_btn_grad.getObject()));
 //BA.debugLineNum = 950;BA.debugLine="Dim bitmd As BitmapDrawable";
_bitmd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 951;BA.debugLine="Dim bitm As Bitmap";
_bitm = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 952;BA.debugLine="bitm.Initialize(File.DirAssets,\"bh1.png\")";
_bitm.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bh1.png");
 //BA.debugLineNum = 953;BA.debugLine="bitmd.Initialize(bitm)";
_bitmd.Initialize((android.graphics.Bitmap)(_bitm.getObject()));
 //BA.debugLineNum = 954;BA.debugLine="bookmark_img.Background = bitmd";
_bookmark_img.setBackground((android.graphics.drawable.Drawable)(_bitmd.getObject()));
 //BA.debugLineNum = 956;BA.debugLine="view_for_image.AddView(fn_pnl,0,0,74%x,30%y) ' fu";
_view_for_image.AddView((android.view.View)(_fn_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 958;BA.debugLine="fn_pnl.AddView(user_image,((fn_pnl.Width/2)/2)-2";
_fn_pnl.AddView((android.view.View)(mostCurrent._user_image.getObject()),(int) (((_fn_pnl.getWidth()/(double)2)/(double)2)-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1.2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (39),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (17),mostCurrent.activityBA));
 //BA.debugLineNum = 959;BA.debugLine="fn_pnl.AddView(bookmark_img,fn_pnl.Width-13.5%x";
_fn_pnl.AddView((android.view.View)(_bookmark_img.getObject()),(int) (_fn_pnl.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13.5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1.3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (12),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 960;BA.debugLine="fn_pnl.AddView(fullname,0,user_image.Top + user_";
_fn_pnl.AddView((android.view.View)(_fullname.getObject()),(int) (0),(int) (mostCurrent._user_image.getTop()+mostCurrent._user_image.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA));
 //BA.debugLineNum = 962;BA.debugLine="fullname.TextSize = 18";
_fullname.setTextSize((float) (18));
 //BA.debugLineNum = 963;BA.debugLine="fullname.Typeface = Typeface.LoadFromAssets(\"ZIN";
_fullname.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.LoadFromAssets("ZINGHABI.otf"));
 //BA.debugLineNum = 965;BA.debugLine="view_panl.AddView(age_pnl,1%x,0,72%x,8%y)";
_view_panl.AddView((android.view.View)(_age_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 966;BA.debugLine="age_pnl.AddView(age_img,4%x,1%y,6%x,6%y) ''  ima";
_age_pnl.AddView((android.view.View)(_age_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 967;BA.debugLine="age_pnl.AddView(age,age_img.Left + age_img.Width";
_age_pnl.AddView((android.view.View)(_age.getObject()),(int) (_age_img.getLeft()+_age_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 969;BA.debugLine="view_panl.AddView(gender_pnl,1%x,age_pnl.Top + ag";
_view_panl.AddView((android.view.View)(_gender_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_age_pnl.getTop()+_age_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 970;BA.debugLine="gender_pnl.AddView(gender_img,4%x,1%y,6%x,6%y) '";
_gender_pnl.AddView((android.view.View)(_gender_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 971;BA.debugLine="gender_pnl.AddView(gender,gender_img.Left + gend";
_gender_pnl.AddView((android.view.View)(_gender.getObject()),(int) (_gender_img.getLeft()+_gender_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 973;BA.debugLine="view_panl.AddView(don_pnl,1%x,gender_pnl.Top + ge";
_view_panl.AddView((android.view.View)(_don_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_gender_pnl.getTop()+_gender_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 974;BA.debugLine="don_pnl.AddView(don_img,4%x,1%y,6%x,6%y) '' imag";
_don_pnl.AddView((android.view.View)(_don_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 975;BA.debugLine="don_pnl.AddView(donated,don_img.Left + don_img.W";
_don_pnl.AddView((android.view.View)(_donated.getObject()),(int) (_don_img.getLeft()+_don_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 977;BA.debugLine="view_panl.AddView(ema_pnl,1%x,don_pnl.Top + don_p";
_view_panl.AddView((android.view.View)(_ema_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_don_pnl.getTop()+_don_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 978;BA.debugLine="ema_pnl.AddView(ema_img,4%x,1%y,6%x,6%y) '' imag";
_ema_pnl.AddView((android.view.View)(_ema_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 979;BA.debugLine="ema_pnl.AddView(email,ema_img.Left + ema_img.Wid";
_ema_pnl.AddView((android.view.View)(_email.getObject()),(int) (_ema_img.getLeft()+_ema_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 981;BA.debugLine="view_panl.AddView(ph1_pnl,1%x,ema_pnl.Top + ema_p";
_view_panl.AddView((android.view.View)(mostCurrent._ph1_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_ema_pnl.getTop()+_ema_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 982;BA.debugLine="ph1_pnl.AddView(ph1_img,4%x,1%y,6%x,6%y) '' imag";
mostCurrent._ph1_pnl.AddView((android.view.View)(_ph1_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 983;BA.debugLine="ph1_pnl.AddView(phone1,ph1_img.Left + ph1_img.Wi";
mostCurrent._ph1_pnl.AddView((android.view.View)(mostCurrent._phone1.getObject()),(int) (_ph1_img.getLeft()+_ph1_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 985;BA.debugLine="view_panl.AddView(ph2_pnl,1%x,ph1_pnl.Top + ph1_p";
_view_panl.AddView((android.view.View)(mostCurrent._ph2_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (mostCurrent._ph1_pnl.getTop()+mostCurrent._ph1_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 986;BA.debugLine="ph2_pnl.AddView(ph2_img,4%x,1%y,6%x,6%y) '' imag";
mostCurrent._ph2_pnl.AddView((android.view.View)(_ph2_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 987;BA.debugLine="ph2_pnl.AddView(phone2,ph2_img.Left + ph2_img.Wi";
mostCurrent._ph2_pnl.AddView((android.view.View)(mostCurrent._phone2.getObject()),(int) (_ph2_img.getLeft()+_ph2_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 989;BA.debugLine="view_panl.AddView(loc_pnl,1%x,ph2_pnl.Top + ph2_p";
_view_panl.AddView((android.view.View)(_loc_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (mostCurrent._ph2_pnl.getTop()+mostCurrent._ph2_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 990;BA.debugLine="loc_pnl.AddView(loc_img,4%x,1%y,6%x,6%y) '' imag";
_loc_pnl.AddView((android.view.View)(_loc_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 991;BA.debugLine="loc_pnl.AddView(location,loc_img.Left + loc_img.";
_loc_pnl.AddView((android.view.View)(_location.getObject()),(int) (_loc_img.getLeft()+_loc_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 994;BA.debugLine="btn_pnl.AddView(ok_vie_btn,((74%x/2)/2),1%y,37%x,";
_btn_pnl.AddView((android.view.View)(_ok_vie_btn.getObject()),(int) (((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (37),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 996;BA.debugLine="view_data_info_person.Color = Colors.ARGB(128,12";
mostCurrent._view_data_info_person.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (128),(int) (128),(int) (.70)));
 //BA.debugLineNum = 999;BA.debugLine="scroll_view_info.ScrollbarsVisibility(False,Fals";
mostCurrent._scroll_view_info.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1000;BA.debugLine="scroll_view_info.SetBackgroundImage(LoadBitmap(F";
mostCurrent._scroll_view_info.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 1001;BA.debugLine="scroll_view_info.Panel.AddView(view_panl,0,0,74%x";
mostCurrent._scroll_view_info.getPanel().AddView((android.view.View)(_view_panl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (57),mostCurrent.activityBA));
 //BA.debugLineNum = 1002;BA.debugLine="view_data_info_person.AddView(view_for_image,13%x";
mostCurrent._view_data_info_person.AddView((android.view.View)(_view_for_image.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (31),mostCurrent.activityBA));
 //BA.debugLineNum = 1003;BA.debugLine="view_data_info_person.AddView(scroll_view_info,13";
mostCurrent._view_data_info_person.AddView((android.view.View)(mostCurrent._scroll_view_info.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (_view_for_image.getTop()+_view_for_image.getHeight()-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (.7),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 1004;BA.debugLine="view_data_info_person.AddView(btn_pnl,13%x,scroll";
mostCurrent._view_data_info_person.AddView((android.view.View)(_btn_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (mostCurrent._scroll_view_info.getTop()+mostCurrent._scroll_view_info.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 1006;BA.debugLine="Activity.AddView(view_data_info_person,0,0,100%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._view_data_info_person.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1008;BA.debugLine="for_phone_clik_animation";
_for_phone_clik_animation();
 //BA.debugLineNum = 1009;BA.debugLine="End Sub";
return "";
}
public static String  _view_data_info_person_click() throws Exception{
 //BA.debugLineNum = 1116;BA.debugLine="Sub view_data_info_person_click";
 //BA.debugLineNum = 1118;BA.debugLine="End Sub";
return "";
}
public static String  _view_info_pnl_click() throws Exception{
 //BA.debugLineNum = 768;BA.debugLine="Sub view_info_pnl_click";
 //BA.debugLineNum = 770;BA.debugLine="End Sub";
return "";
}
}
