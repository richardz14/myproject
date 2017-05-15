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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.search_frame");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.search_frame");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.search_frame", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public static anywheresoftware.b4a.objects.collections.List _id_list = null;
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
public static anywheresoftware.b4a.gps.GPS _gpsclient = null;
public static anywheresoftware.b4a.gps.LocationWrapper _userlocation = null;
public static boolean _is_check_true = false;
public static String _row_click = "";
public static float _earth_radius = 0f;
public static float _pi = 0f;
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
public b4a.example.httpjob _data_query_id = null;
public b4a.example.httpjob _data_query_fulln = null;
public b4a.example.httpjob _data_query_location = null;
public b4a.example.httpjob _query_lat = null;
public b4a.example.httpjob _query_lng = null;
public b4a.example.httpjob _data_query_donated = null;
public b4a.example.httpjob _data_query_email = null;
public b4a.example.httpjob _data_query_nickname = null;
public b4a.example.httpjob _data_query_phone1 = null;
public b4a.example.httpjob _data_query_phone2 = null;
public b4a.example.httpjob _data_query_image = null;
public b4a.example.httpjob _data_query_age = null;
public b4a.example.httpjob _data_query_gender = null;
public b4a.example.httpjob _query_marker = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _isgpson = null;
public anywheresoftware.b4a.objects.PanelWrapper _view_info_pnl = null;
public anywheresoftware.b4a.objects.PanelWrapper _view_data_info_person = null;
public flm.b4a.scrollview2d.ScrollView2DWrapper _scroll_view_info = null;
public anywheresoftware.b4a.objects.PanelWrapper _user_img_panl = null;
public b4a.example.main _main = null;
public b4a.example.login_form _login_form = null;
public b4a.example.create_account _create_account = null;
public b4a.example.menu_form _menu_form = null;
public b4a.example.help_frame _help_frame = null;
public b4a.example.httputils2service _httputils2service = null;
public b4a.example.my_profile _my_profile = null;
public b4a.example.about_frame _about_frame = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 77;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 79;BA.debugLine="Activity.LoadLayout(\"search_frame\")";
mostCurrent._activity.LoadLayout("search_frame",mostCurrent.activityBA);
 //BA.debugLineNum = 80;BA.debugLine="data_query_id.Initialize(\"data_query_id_get\",Me)";
mostCurrent._data_query_id._initialize(processBA,"data_query_id_get",search_frame.getObject());
 //BA.debugLineNum = 81;BA.debugLine="data_query_fullN.Initialize(\"data_query_fullN_get";
mostCurrent._data_query_fulln._initialize(processBA,"data_query_fullN_get",search_frame.getObject());
 //BA.debugLineNum = 82;BA.debugLine="data_query_location.Initialize(\"data_query_locati";
mostCurrent._data_query_location._initialize(processBA,"data_query_location_get",search_frame.getObject());
 //BA.debugLineNum = 83;BA.debugLine="query_lat.Initialize(\"data_query_lat_get\",Me)";
mostCurrent._query_lat._initialize(processBA,"data_query_lat_get",search_frame.getObject());
 //BA.debugLineNum = 84;BA.debugLine="query_lng.Initialize(\"data_query_lng_get\",Me)";
mostCurrent._query_lng._initialize(processBA,"data_query_lng_get",search_frame.getObject());
 //BA.debugLineNum = 85;BA.debugLine="data_query_donated.Initialize(\"data_query_donated";
mostCurrent._data_query_donated._initialize(processBA,"data_query_donated_get",search_frame.getObject());
 //BA.debugLineNum = 86;BA.debugLine="data_query_email.Initialize(\"data_query_email_get";
mostCurrent._data_query_email._initialize(processBA,"data_query_email_get",search_frame.getObject());
 //BA.debugLineNum = 87;BA.debugLine="data_query_nickname.Initialize(\"data_query_nickna";
mostCurrent._data_query_nickname._initialize(processBA,"data_query_nickname_get",search_frame.getObject());
 //BA.debugLineNum = 88;BA.debugLine="data_query_phone1.Initialize(\"data_query_phone1_g";
mostCurrent._data_query_phone1._initialize(processBA,"data_query_phone1_get",search_frame.getObject());
 //BA.debugLineNum = 89;BA.debugLine="data_query_phone2.Initialize(\"data_query_phone2_g";
mostCurrent._data_query_phone2._initialize(processBA,"data_query_phone2_get",search_frame.getObject());
 //BA.debugLineNum = 90;BA.debugLine="data_query_image.Initialize(\"data_query_image\",Me";
mostCurrent._data_query_image._initialize(processBA,"data_query_image",search_frame.getObject());
 //BA.debugLineNum = 91;BA.debugLine="data_query_age.Initialize(\"data_query_age_get\",Me";
mostCurrent._data_query_age._initialize(processBA,"data_query_age_get",search_frame.getObject());
 //BA.debugLineNum = 92;BA.debugLine="data_query_gender.Initialize(\"data_query_gender_g";
mostCurrent._data_query_gender._initialize(processBA,"data_query_gender_get",search_frame.getObject());
 //BA.debugLineNum = 93;BA.debugLine="query_marker.Initialize(\"query_marker_get\",Me)";
mostCurrent._query_marker._initialize(processBA,"query_marker_get",search_frame.getObject());
 //BA.debugLineNum = 94;BA.debugLine="map_extras.addJavascriptInterface(map_webview,\"B4";
mostCurrent._map_extras.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._map_webview.getObject()),"B4A");
 //BA.debugLineNum = 96;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 97;BA.debugLine="gpsClient.Initialize(\"gpsClient\")";
_gpsclient.Initialize("gpsClient");
 //BA.debugLineNum = 98;BA.debugLine="userLocation.Initialize";
_userlocation.Initialize();
 };
 //BA.debugLineNum = 100;BA.debugLine="is_initialize";
_is_initialize();
 //BA.debugLineNum = 101;BA.debugLine="all_layout_load";
_all_layout_load();
 //BA.debugLineNum = 102;BA.debugLine="load_list";
_load_list();
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 173;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 169;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 171;BA.debugLine="End Sub";
return "";
}
public static String  _all_layout_load() throws Exception{
 //BA.debugLineNum = 105;BA.debugLine="Sub all_layout_load";
 //BA.debugLineNum = 106;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 107;BA.debugLine="toolkit_pnl.Color = Colors.Transparent";
mostCurrent._toolkit_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 108;BA.debugLine="list_panel.Color = Colors.Transparent";
mostCurrent._list_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 109;BA.debugLine="search_btn.SetBackgroundImage(LoadBitmap(File.Dir";
mostCurrent._search_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"esearch.png").getObject()));
 //BA.debugLineNum = 110;BA.debugLine="list_btn.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._list_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"view all.png").getObject()));
 //BA.debugLineNum = 112;BA.debugLine="toolkit_pnl.Width = Activity.Width";
mostCurrent._toolkit_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 113;BA.debugLine="list_panel.Width = Activity.Width";
mostCurrent._list_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 114;BA.debugLine="map_webview.Width = Activity.Width";
mostCurrent._map_webview.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 116;BA.debugLine="list_btn.Width = 50%x";
mostCurrent._list_btn.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 117;BA.debugLine="search_lbl.Width = 14%x";
mostCurrent._search_lbl.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 118;BA.debugLine="search_btn.Width = 14%x";
mostCurrent._search_btn.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 119;BA.debugLine="search_spiner.Width = ((toolkit_pnl.Width - sea";
mostCurrent._search_spiner.setWidth((int) (((mostCurrent._toolkit_pnl.getWidth()-mostCurrent._search_btn.getWidth())-mostCurrent._search_lbl.getWidth())-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA)));
 //BA.debugLineNum = 121;BA.debugLine="toolkit_pnl.Height = 14%y";
mostCurrent._toolkit_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 122;BA.debugLine="list_panel.Height = 11%y";
mostCurrent._list_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (11),mostCurrent.activityBA));
 //BA.debugLineNum = 123;BA.debugLine="map_webview.Height =((Activity.Height - toolkit";
mostCurrent._map_webview.setHeight((int) (((mostCurrent._activity.getHeight()-mostCurrent._toolkit_pnl.getHeight())-mostCurrent._list_panel.getHeight())));
 //BA.debugLineNum = 125;BA.debugLine="list_btn.Height = 9%y";
mostCurrent._list_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 126;BA.debugLine="search_lbl.Height = 10%y";
mostCurrent._search_lbl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 127;BA.debugLine="search_btn.Height = 10%y";
mostCurrent._search_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 128;BA.debugLine="search_spiner.Height = 10%y";
mostCurrent._search_spiner.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 130;BA.debugLine="toolkit_pnl.Left = 0";
mostCurrent._toolkit_pnl.setLeft((int) (0));
 //BA.debugLineNum = 131;BA.debugLine="list_panel.Left = 0";
mostCurrent._list_panel.setLeft((int) (0));
 //BA.debugLineNum = 132;BA.debugLine="map_webview.Left = 0";
mostCurrent._map_webview.setLeft((int) (0));
 //BA.debugLineNum = 133;BA.debugLine="isGPSon.Left = map_webview.Width - isGPSon.Width";
mostCurrent._isgpson.setLeft((int) (mostCurrent._map_webview.getWidth()-mostCurrent._isgpson.getWidth()-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA)));
 //BA.debugLineNum = 135;BA.debugLine="list_btn.Left = ((list_panel.Width/2)/2)";
mostCurrent._list_btn.setLeft((int) (((mostCurrent._list_panel.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 136;BA.debugLine="search_lbl.Left = ((toolkit_pnl.Left + 3%x)+2%x";
mostCurrent._search_lbl.setLeft((int) (((mostCurrent._toolkit_pnl.getLeft()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (3),mostCurrent.activityBA))+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA))));
 //BA.debugLineNum = 137;BA.debugLine="search_spiner.Left = (search_lbl.Left + search_";
mostCurrent._search_spiner.setLeft((int) ((mostCurrent._search_lbl.getLeft()+mostCurrent._search_lbl.getWidth())));
 //BA.debugLineNum = 138;BA.debugLine="search_btn.Left = (search_spiner.Left + searc";
mostCurrent._search_btn.setLeft((int) ((mostCurrent._search_spiner.getLeft()+mostCurrent._search_spiner.getWidth())));
 //BA.debugLineNum = 140;BA.debugLine="toolkit_pnl.Top = 0";
mostCurrent._toolkit_pnl.setTop((int) (0));
 //BA.debugLineNum = 141;BA.debugLine="isGPSon.Top = toolkit_pnl.Top + toolkit_pnl.Heig";
mostCurrent._isgpson.setTop((int) (mostCurrent._toolkit_pnl.getTop()+mostCurrent._toolkit_pnl.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)));
 //BA.debugLineNum = 142;BA.debugLine="map_webview.Top = (toolkit_pnl.Top + toolkit_pnl.";
mostCurrent._map_webview.setTop((int) ((mostCurrent._toolkit_pnl.getTop()+mostCurrent._toolkit_pnl.getHeight())));
 //BA.debugLineNum = 143;BA.debugLine="list_panel.Top = (map_webview.Top + map_webview.";
mostCurrent._list_panel.setTop((int) ((mostCurrent._map_webview.getTop()+mostCurrent._map_webview.getHeight())));
 //BA.debugLineNum = 145;BA.debugLine="list_btn.Top = 1%y'(list_panel.Top + 1%Y)";
mostCurrent._list_btn.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA));
 //BA.debugLineNum = 146;BA.debugLine="search_lbl.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_lbl.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 147;BA.debugLine="search_btn.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_btn.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 148;BA.debugLine="search_spiner.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_spiner.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _can_btn_click() throws Exception{
 //BA.debugLineNum = 802;BA.debugLine="Sub can_btn_click";
 //BA.debugLineNum = 803;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 //BA.debugLineNum = 804;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 242;BA.debugLine="Sub create_map";
 //BA.debugLineNum = 243;BA.debugLine="ProgressDialogShow2(\"Creating the map, please wai";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Creating the map, please wait...",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 244;BA.debugLine="Dim htmlString1,htmlString1_1,htmlString2,htmlStr";
_htmlstring1 = "";
_htmlstring1_1 = "";
_htmlstring2 = "";
_htmlstring3 = "";
 //BA.debugLineNum = 245;BA.debugLine="Dim GPSlat,GPSlng,TOlat,TOlng,distance As Double";
_gpslat = 0;
_gpslng = 0;
_tolat = 0;
_tolng = 0;
_distance = 0;
 //BA.debugLineNum = 246;BA.debugLine="Dim distanceMeter As Int";
_distancemeter = 0;
 //BA.debugLineNum = 247;BA.debugLine="htmlString1 = File.GetText(File.DirAssets, \"locat";
_htmlstring1 = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"location_top.txt");
 //BA.debugLineNum = 248;BA.debugLine="htmlString1 = htmlString1 & \" var markers=[]; var";
_htmlstring1 = _htmlstring1+" var markers=[]; var contents = []; var infowindows = []; ";
 //BA.debugLineNum = 249;BA.debugLine="Dim location As TextWriter";
_location = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 250;BA.debugLine="location.Initialize(File.OpenOutput(File.";
_location.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"all_marker_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 251;BA.debugLine="location.WriteLine(htmlString1)";
_location.WriteLine(_htmlstring1);
 //BA.debugLineNum = 252;BA.debugLine="If is_check_true == True Then";
if (_is_check_true==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 253;BA.debugLine="GPSlat = userLocation.Latitude";
_gpslat = _userlocation.getLatitude();
 //BA.debugLineNum = 254;BA.debugLine="GPSlng = userLocation.Longitude";
_gpslng = _userlocation.getLongitude();
 //BA.debugLineNum = 255;BA.debugLine="Log(\"lat: \"&GPSlat)";
anywheresoftware.b4a.keywords.Common.Log("lat: "+BA.NumberToString(_gpslat));
 //BA.debugLineNum = 256;BA.debugLine="Log(\"long: \"&GPSlng)";
anywheresoftware.b4a.keywords.Common.Log("long: "+BA.NumberToString(_gpslng));
 //BA.debugLineNum = 258;BA.debugLine="htmlString1_1 = \" new google.maps.Marker({ posit";
_htmlstring1_1 = " new google.maps.Marker({ position: new google.maps.LatLng("+BA.NumberToString(_gpslat)+", "+BA.NumberToString(_gpslng)+"), map: map, title: 'My Location', clickable: true, icon: 'http://www.google.com/mapfiles/dd-end.png' }); ";
 //BA.debugLineNum = 259;BA.debugLine="location.WriteLine(htmlString1_1)";
_location.WriteLine(_htmlstring1_1);
 }else {
 };
 //BA.debugLineNum = 264;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step19 = 1;
final int limit19 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step19 > 0 && _i <= limit19) || (step19 < 0 && _i >= limit19); _i = ((int)(0 + _i + step19)) ) {
 //BA.debugLineNum = 265;BA.debugLine="If is_check_true == True Then";
if (_is_check_true==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 266;BA.debugLine="TOlat = lat_list.Get(i)";
_tolat = (double)(BA.ObjectToNumber(_lat_list.Get(_i)));
 //BA.debugLineNum = 267;BA.debugLine="TOlng = lng_list.Get(i)";
_tolng = (double)(BA.ObjectToNumber(_lng_list.Get(_i)));
 //BA.debugLineNum = 269;BA.debugLine="distance = earth_radius * ACos( Cos( ( 90 - GPS";
_distance = _earth_radius*anywheresoftware.b4a.keywords.Common.ACos(anywheresoftware.b4a.keywords.Common.Cos((90-_gpslat)*(_pi/(double)180))*anywheresoftware.b4a.keywords.Common.Cos((90-_tolat)*(_pi/(double)180))+anywheresoftware.b4a.keywords.Common.Sin((90-_gpslat)*(_pi/(double)180))*anywheresoftware.b4a.keywords.Common.Sin((90-_tolat)*(_pi/(double)180))*anywheresoftware.b4a.keywords.Common.Cos((_gpslng-_tolng)*(_pi/(double)180)));
 //BA.debugLineNum = 270;BA.debugLine="distanceMeter = distance*1000";
_distancemeter = (int) (_distance*1000);
 //BA.debugLineNum = 272;BA.debugLine="htmlString2 = \"markers[\"&i&\"] = new google.maps";
_htmlstring2 = "markers["+BA.NumberToString(_i)+"] = new google.maps.Marker({position: new google.maps.LatLng("+BA.ObjectToString(_lat_list.Get(_i))+" , "+BA.ObjectToString(_lng_list.Get(_i))+"), map: map, title: '"+BA.ObjectToString(_fulln_llist.Get(_i))+"', icon: 'https://raw.githubusercontent.com/richardz14/myproject/master/Files/heart_arrow_img.png', clickable: true }); markers["+BA.NumberToString(_i)+"].index = "+BA.NumberToString(_i)+"; contents["+BA.NumberToString(_i)+"] = '<div class=\"iw-container\"><div class=\"iw-title\">Personal Information</div><div class=\"iw-content\"><b><h2><center>"+BA.ObjectToString(_fulln_llist.Get(_i))+"</center></h2></b><h4>Blood Type: <b>"+mostCurrent._spin_item_click+"</b></h4><h4>Age: <b>"+BA.ObjectToString(_age_list.Get(_i))+"</b></h4><h4>Age: <b>"+BA.ObjectToString(_gender_list.Get(_i))+"</b></h4><h4>Email Address: <b>"+BA.ObjectToString(_email_list.Get(_i))+"</b></h4><h4>Location: <b>"+BA.ObjectToString(_location_list.Get(_i))+"</b></h4><h4>Phone Number 1: <b>"+BA.ObjectToString(_phone1_list.Get(_i))+"</b></h4><h4>Phone Number 2: <b>"+BA.ObjectToString(_phone2_list.Get(_i))+"</b></h4><h4>Donated: <b>"+BA.ObjectToString(_donated_list.Get(_i))+"</b></h4><h4><b>You are "+BA.NumberToString(_distancemeter)+"m away from the donor!</b></h4></div></div>'; infowindows["+BA.NumberToString(_i)+"] = new google.maps.InfoWindow({ content: contents["+BA.NumberToString(_i)+"], maxWidth: 350 }); google.maps.event.addListener(markers["+BA.NumberToString(_i)+"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); ";
 //BA.debugLineNum = 273;BA.debugLine="location.WriteLine(htmlString2)";
_location.WriteLine(_htmlstring2);
 }else {
 //BA.debugLineNum = 275;BA.debugLine="htmlString2 = \"markers[\"&i&\"] = new google.maps";
_htmlstring2 = "markers["+BA.NumberToString(_i)+"] = new google.maps.Marker({position: new google.maps.LatLng("+BA.ObjectToString(_lat_list.Get(_i))+" , "+BA.ObjectToString(_lng_list.Get(_i))+"), map: map, title: '"+BA.ObjectToString(_fulln_llist.Get(_i))+"', icon: 'https://raw.githubusercontent.com/richardz14/myproject/master/Files/heart_arrow_img.png', clickable: true }); markers["+BA.NumberToString(_i)+"].index = "+BA.NumberToString(_i)+"; contents["+BA.NumberToString(_i)+"] = '<div class=\"iw-container\"><div class=\"iw-title\">Personal Information</div><div class=\"iw-content\"><b><h2><center>"+BA.ObjectToString(_fulln_llist.Get(_i))+"</center></h2></b><h4>Blood Type: <b>"+mostCurrent._spin_item_click+"</b></h4><h4>Age: <b>"+BA.ObjectToString(_age_list.Get(_i))+"</b></h4><h4>Age: <b>"+BA.ObjectToString(_gender_list.Get(_i))+"</b></h4><h4>Email Address: <b>"+BA.ObjectToString(_email_list.Get(_i))+"</b></h4><h4>Location: <b>"+BA.ObjectToString(_location_list.Get(_i))+"</b></h4><h4>Phone Number 1: <b>"+BA.ObjectToString(_phone1_list.Get(_i))+"</b></h4><h4>Phone Number 2: <b>"+BA.ObjectToString(_phone2_list.Get(_i))+"</b></h4><h4>Donated: <b>"+BA.ObjectToString(_donated_list.Get(_i))+"</b></h4></div></div>'; infowindows["+BA.NumberToString(_i)+"] = new google.maps.InfoWindow({ content: contents["+BA.NumberToString(_i)+"], maxWidth: 350}); google.maps.event.addListener(markers["+BA.NumberToString(_i)+"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); ";
 //BA.debugLineNum = 276;BA.debugLine="location.WriteLine(htmlString2)";
_location.WriteLine(_htmlstring2);
 };
 }
};
 //BA.debugLineNum = 318;BA.debugLine="htmlString3 = File.GetText(File.DirAssets, \"locat";
_htmlstring3 = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"location_buttom.txt");
 //BA.debugLineNum = 319;BA.debugLine="location.WriteLine(htmlString3)";
_location.WriteLine(_htmlstring3);
 //BA.debugLineNum = 320;BA.debugLine="location.Close";
_location.Close();
 //BA.debugLineNum = 326;BA.debugLine="map_webview.LoadHtml(File.ReadString(File.DirInte";
mostCurrent._map_webview.LoadHtml(anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"all_marker_location.txt"));
 //BA.debugLineNum = 330;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 549;BA.debugLine="Sub data_list_Click";
 //BA.debugLineNum = 550;BA.debugLine="Dim Send As View";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 551;BA.debugLine="Dim row As Int";
_row = 0;
 //BA.debugLineNum = 552;BA.debugLine="Send=Sender";
_send.setObject((android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 553;BA.debugLine="row=Floor(Send.Tag/10) '20";
_row = (int) (anywheresoftware.b4a.keywords.Common.Floor((double)(BA.ObjectToNumber(_send.getTag()))/(double)10));
 //BA.debugLineNum = 554;BA.debugLine="item=row";
_item = _row;
 //BA.debugLineNum = 555;BA.debugLine="row_click = row";
_row_click = BA.NumberToString(_row);
 //BA.debugLineNum = 556;BA.debugLine="Log(row)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_row));
 //BA.debugLineNum = 560;BA.debugLine="If view_info_pnl.IsInitialized == True Then";
if (mostCurrent._view_info_pnl.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 561;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 };
 //BA.debugLineNum = 563;BA.debugLine="view_info_pnl.Initialize(\"view_info_pnl\")";
mostCurrent._view_info_pnl.Initialize(mostCurrent.activityBA,"view_info_pnl");
 //BA.debugLineNum = 564;BA.debugLine="Dim view_panl As Panel";
_view_panl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 565;BA.debugLine="Dim vie_btn,can_btn As Button";
_vie_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 566;BA.debugLine="Dim lbl_tittle As Label";
_lbl_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 567;BA.debugLine="lbl_tittle.Initialize(\"\")";
_lbl_tittle.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 568;BA.debugLine="view_panl.Initialize(\"view_panl\")";
_view_panl.Initialize(mostCurrent.activityBA,"view_panl");
 //BA.debugLineNum = 569;BA.debugLine="vie_btn.Initialize(\"vie_btn\")";
_vie_btn.Initialize(mostCurrent.activityBA,"vie_btn");
 //BA.debugLineNum = 570;BA.debugLine="can_btn.Initialize(\"can_btn\")";
_can_btn.Initialize(mostCurrent.activityBA,"can_btn");
 //BA.debugLineNum = 571;BA.debugLine="vie_btn.Text = \"VIEW\"";
_vie_btn.setText((Object)("VIEW"));
 //BA.debugLineNum = 572;BA.debugLine="can_btn.Text = \"CANCEL\"";
_can_btn.setText((Object)("CANCEL"));
 //BA.debugLineNum = 573;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 574;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 575;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 576;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 577;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 578;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 579;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 580;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 581;BA.debugLine="vie_btn.Background = V_btn";
_vie_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 582;BA.debugLine="can_btn.Background = C_btn";
_can_btn.setBackground((android.graphics.drawable.Drawable)(_c_btn.getObject()));
 //BA.debugLineNum = 583;BA.debugLine="lbl_tittle.Text = \"SELECT ACTION\"";
_lbl_tittle.setText((Object)("SELECT ACTION"));
 //BA.debugLineNum = 584;BA.debugLine="lbl_tittle.Gravity = Gravity.CENTER";
_lbl_tittle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 585;BA.debugLine="lbl_tittle.TextColor = Colors.White";
_lbl_tittle.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 586;BA.debugLine="view_panl.SetBackgroundImage(LoadBitmap(File.DirA";
_view_panl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 587;BA.debugLine="view_panl.AddView(lbl_tittle,1%x,2%y,72%x,8%y)";
_view_panl.AddView((android.view.View)(_lbl_tittle.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 588;BA.debugLine="view_panl.AddView(vie_btn,5%x,lbl_tittle.Top + lb";
_view_panl.AddView((android.view.View)(_vie_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_lbl_tittle.getTop()+_lbl_tittle.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (31),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 589;BA.debugLine="view_panl.AddView(can_btn,vie_btn.Left+vie_btn.Wi";
_view_panl.AddView((android.view.View)(_can_btn.getObject()),(int) (_vie_btn.getLeft()+_vie_btn.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),_vie_btn.getTop(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (31),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 590;BA.debugLine="view_info_pnl.AddView(view_panl,13%x,((Activity.H";
mostCurrent._view_info_pnl.AddView((android.view.View)(_view_panl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((mostCurrent._activity.getHeight()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 591;BA.debugLine="Activity.AddView(view_info_pnl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._view_info_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 592;BA.debugLine="End Sub";
return "";
}
public static String  _dialog_all_panel_click() throws Exception{
 //BA.debugLineNum = 1129;BA.debugLine="Sub dialog_all_panel_click";
 //BA.debugLineNum = 1131;BA.debugLine="End Sub";
return "";
}
public static String  _dialog_panel_can_btn_click() throws Exception{
 //BA.debugLineNum = 1126;BA.debugLine="Sub dialog_panel_can_btn_click";
 //BA.debugLineNum = 1127;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 //BA.debugLineNum = 1128;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 40;BA.debugLine="Public spin_item_click As String :";
mostCurrent._spin_item_click = "";
 //BA.debugLineNum = 41;BA.debugLine="Private toolkit_pnl As Panel";
mostCurrent._toolkit_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private search_lbl As Label";
mostCurrent._search_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private search_spiner As Spinner";
mostCurrent._search_spiner = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private search_btn As Button";
mostCurrent._search_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private map_webview As WebView";
mostCurrent._map_webview = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private list_btn As Button";
mostCurrent._list_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private list_panel As Panel";
mostCurrent._list_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private map_extras As WebViewExtras";
mostCurrent._map_extras = new uk.co.martinpearman.b4a.webviewextras.WebViewExtras();
 //BA.debugLineNum = 50;BA.debugLine="Dim scrolllista As ScrollView";
mostCurrent._scrolllista = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Dim item As Int";
_item = 0;
 //BA.debugLineNum = 52;BA.debugLine="Dim dialog_panel As Panel";
mostCurrent._dialog_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Dim dialog_all_panel As Panel";
mostCurrent._dialog_all_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private data_query_id As HttpJob";
mostCurrent._data_query_id = new b4a.example.httpjob();
 //BA.debugLineNum = 56;BA.debugLine="Private data_query_fullN As HttpJob";
mostCurrent._data_query_fulln = new b4a.example.httpjob();
 //BA.debugLineNum = 57;BA.debugLine="Private data_query_location As HttpJob";
mostCurrent._data_query_location = new b4a.example.httpjob();
 //BA.debugLineNum = 58;BA.debugLine="Private query_lat As HttpJob";
mostCurrent._query_lat = new b4a.example.httpjob();
 //BA.debugLineNum = 59;BA.debugLine="Private query_lng As HttpJob";
mostCurrent._query_lng = new b4a.example.httpjob();
 //BA.debugLineNum = 60;BA.debugLine="Private data_query_donated As HttpJob";
mostCurrent._data_query_donated = new b4a.example.httpjob();
 //BA.debugLineNum = 61;BA.debugLine="Private data_query_email As HttpJob";
mostCurrent._data_query_email = new b4a.example.httpjob();
 //BA.debugLineNum = 62;BA.debugLine="Private data_query_nickname As HttpJob";
mostCurrent._data_query_nickname = new b4a.example.httpjob();
 //BA.debugLineNum = 63;BA.debugLine="Private data_query_phone1 As HttpJob";
mostCurrent._data_query_phone1 = new b4a.example.httpjob();
 //BA.debugLineNum = 64;BA.debugLine="Private data_query_phone2 As HttpJob";
mostCurrent._data_query_phone2 = new b4a.example.httpjob();
 //BA.debugLineNum = 65;BA.debugLine="Private data_query_image As HttpJob";
mostCurrent._data_query_image = new b4a.example.httpjob();
 //BA.debugLineNum = 66;BA.debugLine="Private data_query_age As HttpJob";
mostCurrent._data_query_age = new b4a.example.httpjob();
 //BA.debugLineNum = 67;BA.debugLine="Private data_query_gender As HttpJob";
mostCurrent._data_query_gender = new b4a.example.httpjob();
 //BA.debugLineNum = 68;BA.debugLine="Private query_marker As HttpJob";
mostCurrent._query_marker = new b4a.example.httpjob();
 //BA.debugLineNum = 69;BA.debugLine="Private isGPSon As CheckBox";
mostCurrent._isgpson = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Dim view_info_pnl As Panel";
mostCurrent._view_info_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Dim view_data_info_person As Panel";
mostCurrent._view_data_info_person = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Dim scroll_view_info As ScrollView2D";
mostCurrent._scroll_view_info = new flm.b4a.scrollview2d.ScrollView2DWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Dim user_img_panl As Panel";
mostCurrent._user_img_panl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return "";
}
public static String  _gpsclient_locationchanged(anywheresoftware.b4a.gps.LocationWrapper _gpslocation) throws Exception{
 //BA.debugLineNum = 234;BA.debugLine="Sub gpsClient_LocationChanged (gpsLocation As Loca";
 //BA.debugLineNum = 235;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 236;BA.debugLine="userLocation=gpsLocation";
_userlocation = _gpslocation;
 //BA.debugLineNum = 238;BA.debugLine="gpsClient.Stop";
_gpsclient.Stop();
 //BA.debugLineNum = 239;BA.debugLine="create_map";
_create_map();
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static String  _is_initialize() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriters = null;
 //BA.debugLineNum = 334;BA.debugLine="Sub is_initialize";
 //BA.debugLineNum = 335;BA.debugLine="Dim TextWriters As TextWriter";
_textwriters = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 336;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 337;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 338;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 339;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 340;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 341;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_donated.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 342;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_email.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 343;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_nickname.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 344;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone1.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 345;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone2.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 346;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.D";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_image.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 347;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_age.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 348;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_gender.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 349;BA.debugLine="End Sub";
return "";
}
public static String  _isgpson_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 219;BA.debugLine="Sub isGPSon_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 220;BA.debugLine="is_check_true = Checked";
_is_check_true = _checked;
 //BA.debugLineNum = 221;BA.debugLine="If Checked == True Then";
if (_checked==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 222;BA.debugLine="If gpsClient.GPSEnabled=False Then";
if (_gpsclient.getGPSEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 223;BA.debugLine="ToastMessageShow(\"Please enable your device's GP";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Please enable your device's GPS capabilities",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 224;BA.debugLine="isGPSon.Checked = False";
mostCurrent._isgpson.setChecked(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 225;BA.debugLine="StartActivity(gpsClient.LocationSettingsIn";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_gpsclient.getLocationSettingsIntent()));
 }else {
 //BA.debugLineNum = 227;BA.debugLine="gpsClient.Start(10.1799469, 122.9068577)";
_gpsclient.Start(processBA,(long) (10.1799469),(float) (122.9068577));
 //BA.debugLineNum = 228;BA.debugLine="ProgressDialogShow(\"Waiting for GPS location\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Waiting for GPS location");
 };
 }else {
 //BA.debugLineNum = 231;BA.debugLine="create_map";
_create_map();
 };
 //BA.debugLineNum = 233;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(b4a.example.httpjob _job) throws Exception{
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
 //BA.debugLineNum = 350;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 351;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 352;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"data_query_id_get","data_query_fullN_get","data_query_location_get","data_query_lat_get","data_query_lng_get","data_query_donated_get","data_query_email_get","data_query_nickname_get","data_query_phone1_get","data_query_phone2_get","data_query_image","data_query_age_get","data_query_gender_get")) {
case 0: {
 //BA.debugLineNum = 354;BA.debugLine="Dim TextWriter_id As TextWriter";
_textwriter_id = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 355;BA.debugLine="TextWriter_id.Initialize(File.OpenOutput(";
_textwriter_id.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 356;BA.debugLine="TextWriter_id.WriteLine(job.GetString.Tr";
_textwriter_id.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 357;BA.debugLine="TextWriter_id.Close";
_textwriter_id.Close();
 break; }
case 1: {
 //BA.debugLineNum = 360;BA.debugLine="Dim TextWriter_full As TextWriter";
_textwriter_full = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 361;BA.debugLine="TextWriter_full.Initialize(File.OpenOutpu";
_textwriter_full.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 362;BA.debugLine="TextWriter_full.WriteLine(job.GetString.";
_textwriter_full.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 363;BA.debugLine="TextWriter_full.Close";
_textwriter_full.Close();
 break; }
case 2: {
 //BA.debugLineNum = 366;BA.debugLine="Dim TextWriter_location As TextWriter";
_textwriter_location = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 367;BA.debugLine="TextWriter_location.Initialize(File.OpenO";
_textwriter_location.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 368;BA.debugLine="TextWriter_location.WriteLine(job.GetStr";
_textwriter_location.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 369;BA.debugLine="TextWriter_location.Close";
_textwriter_location.Close();
 break; }
case 3: {
 //BA.debugLineNum = 372;BA.debugLine="Dim TextWriter_lat As TextWriter";
_textwriter_lat = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 373;BA.debugLine="TextWriter_lat.Initialize(File.OpenOutput";
_textwriter_lat.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 374;BA.debugLine="TextWriter_lat.WriteLine(job.GetString.T";
_textwriter_lat.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 376;BA.debugLine="TextWriter_lat.Close";
_textwriter_lat.Close();
 break; }
case 4: {
 //BA.debugLineNum = 378;BA.debugLine="Dim TextWriter_lng As TextWriter";
_textwriter_lng = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 379;BA.debugLine="TextWriter_lng.Initialize(File.OpenOutput";
_textwriter_lng.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 380;BA.debugLine="TextWriter_lng.WriteLine(job.GetString.T";
_textwriter_lng.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 382;BA.debugLine="TextWriter_lng.Close";
_textwriter_lng.Close();
 break; }
case 5: {
 //BA.debugLineNum = 384;BA.debugLine="Dim TextWriter_donate As TextWriter";
_textwriter_donate = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 385;BA.debugLine="TextWriter_donate.Initialize(File.OpenOut";
_textwriter_donate.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_donated.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 386;BA.debugLine="TextWriter_donate.WriteLine(job.GetStrin";
_textwriter_donate.WriteLine(_job._getstring());
 //BA.debugLineNum = 387;BA.debugLine="TextWriter_donate.Close";
_textwriter_donate.Close();
 break; }
case 6: {
 //BA.debugLineNum = 390;BA.debugLine="Dim TextWriter_email As TextWriter";
_textwriter_email = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 391;BA.debugLine="TextWriter_email.Initialize(File.OpenOutp";
_textwriter_email.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_email.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 392;BA.debugLine="TextWriter_email.WriteLine(job.GetString";
_textwriter_email.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 393;BA.debugLine="TextWriter_email.Close";
_textwriter_email.Close();
 break; }
case 7: {
 //BA.debugLineNum = 396;BA.debugLine="Dim TextWriter_nickname As TextWriter";
_textwriter_nickname = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 397;BA.debugLine="TextWriter_nickname.Initialize(File.OpenO";
_textwriter_nickname.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_nickname.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 398;BA.debugLine="TextWriter_nickname.WriteLine(job.GetStr";
_textwriter_nickname.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 399;BA.debugLine="TextWriter_nickname.Close";
_textwriter_nickname.Close();
 break; }
case 8: {
 //BA.debugLineNum = 402;BA.debugLine="Dim TextWriter_phone1 As TextWriter";
_textwriter_phone1 = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 403;BA.debugLine="TextWriter_phone1.Initialize(File.OpenOut";
_textwriter_phone1.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone1.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 404;BA.debugLine="TextWriter_phone1.WriteLine(job.GetStrin";
_textwriter_phone1.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 405;BA.debugLine="TextWriter_phone1.Close";
_textwriter_phone1.Close();
 break; }
case 9: {
 //BA.debugLineNum = 408;BA.debugLine="Dim TextWriter_phone2 As TextWriter";
_textwriter_phone2 = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 409;BA.debugLine="TextWriter_phone2.Initialize(File.OpenOut";
_textwriter_phone2.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone2.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 410;BA.debugLine="TextWriter_phone2.WriteLine(job.GetStrin";
_textwriter_phone2.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 411;BA.debugLine="TextWriter_phone2.Close";
_textwriter_phone2.Close();
 break; }
case 10: {
 //BA.debugLineNum = 414;BA.debugLine="Dim TextWriter_image As TextWriter";
_textwriter_image = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 415;BA.debugLine="TextWriter_image.Initialize(File.OpenOutp";
_textwriter_image.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_image.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 416;BA.debugLine="TextWriter_image.WriteLine(job.GetString";
_textwriter_image.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 417;BA.debugLine="TextWriter_image.Close";
_textwriter_image.Close();
 break; }
case 11: {
 //BA.debugLineNum = 419;BA.debugLine="Dim TextWriter_age As TextWriter";
_textwriter_age = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 420;BA.debugLine="TextWriter_age.Initialize(File.OpenOutput";
_textwriter_age.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_age.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 421;BA.debugLine="TextWriter_age.WriteLine(job.GetString.T";
_textwriter_age.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 422;BA.debugLine="TextWriter_age.Close";
_textwriter_age.Close();
 break; }
case 12: {
 //BA.debugLineNum = 424;BA.debugLine="Dim TextWriter_gender As TextWriter";
_textwriter_gender = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 425;BA.debugLine="TextWriter_gender.Initialize(File.OpenOut";
_textwriter_gender.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_gender.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 426;BA.debugLine="TextWriter_gender.WriteLine(job.GetStrin";
_textwriter_gender.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 427;BA.debugLine="TextWriter_gender.Close";
_textwriter_gender.Close();
 break; }
}
;
 //BA.debugLineNum = 430;BA.debugLine="Log(\"datas: \"&is_complete)";
anywheresoftware.b4a.keywords.Common.Log("datas: "+BA.NumberToString(_is_complete));
 //BA.debugLineNum = 431;BA.debugLine="If is_complete == 12 Then";
if (_is_complete==12) { 
 //BA.debugLineNum = 432;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 433;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 434;BA.debugLine="Try";
try { //BA.debugLineNum = 435;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 436;BA.debugLine="create_map";
_create_map();
 } 
       catch (Exception e77) {
			processBA.setLastException(e77); //BA.debugLineNum = 438;BA.debugLine="Log(LastException.Message)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA).getMessage());
 };
 };
 //BA.debugLineNum = 441;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 443;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 444;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 445;BA.debugLine="Msgbox(\"Error: Error connecting to server,please";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Error connecting to server,please try again.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 450;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 462;BA.debugLine="Sub list_btn_BACKUP_Click";
 //BA.debugLineNum = 463;BA.debugLine="ProgressDialogShow2(\"Loading data, Please Wait...";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Loading data, Please Wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 464;BA.debugLine="If scrolllista.IsInitialized == True Then";
if (mostCurrent._scrolllista.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 465;BA.debugLine="scrolllista.RemoveView";
mostCurrent._scrolllista.RemoveView();
 //BA.debugLineNum = 466;BA.debugLine="dialog_panel.RemoveView";
mostCurrent._dialog_panel.RemoveView();
 };
 //BA.debugLineNum = 468;BA.debugLine="scrolllista.Initialize(500)";
mostCurrent._scrolllista.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 469;BA.debugLine="dialog_panel.Initialize(\"dialog_panel\")";
mostCurrent._dialog_panel.Initialize(mostCurrent.activityBA,"dialog_panel");
 //BA.debugLineNum = 470;BA.debugLine="Dim cd As CustomDialog2";
_cd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2();
 //BA.debugLineNum = 471;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 472;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 473;BA.debugLine="Dim bgnd As ColorDrawable";
_bgnd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 474;BA.debugLine="bgnd.Initialize(Colors.Cyan, 5dip)";
_bgnd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Cyan,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 475;BA.debugLine="pnl.Background = bgnd";
_pnl.setBackground((android.graphics.drawable.Drawable)(_bgnd.getObject()));
 //BA.debugLineNum = 479;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 490;BA.debugLine="Dim Bitmap1 As Bitmap";
_bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 491;BA.debugLine="Dim Panel0 As Panel";
_panel0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 492;BA.debugLine="Dim PanelTop, PanelHeight  As Int";
_paneltop = 0;
_panelheight = 0;
 //BA.debugLineNum = 497;BA.debugLine="Bitmap1.Initialize(File.DirAssets,\"banner1.png\")";
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner1.png");
 //BA.debugLineNum = 498;BA.debugLine="PanelTop=1%y";
_paneltop = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA);
 //BA.debugLineNum = 499;BA.debugLine="Panel0=scrolllista.Panel";
_panel0 = mostCurrent._scrolllista.getPanel();
 //BA.debugLineNum = 500;BA.debugLine="Panel0.Color=Colors.argb(0,0,0,0)  'sets the invi";
_panel0.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 502;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step22 = 1;
final int limit22 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step22 > 0 && _i <= limit22) || (step22 < 0 && _i >= limit22); _i = ((int)(0 + _i + step22)) ) {
 //BA.debugLineNum = 504;BA.debugLine="If i>0 And i<3 Then Bitmap1.Initialize(File.DirA";
if (_i>0 && _i<3) { 
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner.png");};
 //BA.debugLineNum = 505;BA.debugLine="Dim ImageView1 As ImageView";
_imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 506;BA.debugLine="ImageView1.Initialize(\"data_list\")";
_imageview1.Initialize(mostCurrent.activityBA,"data_list");
 //BA.debugLineNum = 507;BA.debugLine="PanelHeight=12%y";
_panelheight = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA);
 //BA.debugLineNum = 509;BA.debugLine="Panel0.AddView(ImageView1,1%x,PanelTop,71%x,Pane";
_panel0.AddView((android.view.View)(_imageview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),_paneltop,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 510;BA.debugLine="ImageView1.Tag=i&\"1\"";
_imageview1.setTag((Object)(BA.NumberToString(_i)+"1"));
 //BA.debugLineNum = 511;BA.debugLine="ImageView1.Bitmap=Bitmap1";
_imageview1.setBitmap((android.graphics.Bitmap)(_bitmap1.getObject()));
 //BA.debugLineNum = 512;BA.debugLine="ImageView1.Gravity=Gravity.fill";
_imageview1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 514;BA.debugLine="Dim Label1, Label2 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 515;BA.debugLine="Label1.Initialize(\"\")";
_label1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 516;BA.debugLine="Label2.Initialize(\"\")";
_label2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 517;BA.debugLine="Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 518;BA.debugLine="Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 520;BA.debugLine="Label1.TextColor= Colors.black";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 521;BA.debugLine="Label1.TextSize= 17";
_label1.setTextSize((float) (17));
 //BA.debugLineNum = 522;BA.debugLine="Label1.Gravity=Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 523;BA.debugLine="Label1.Color=Colors.argb(0,0,0,0)";
_label1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 524;BA.debugLine="Label1.Text=fullN_llist.Get(i) 'set data from li";
_label1.setText(_fulln_llist.Get(_i));
 //BA.debugLineNum = 526;BA.debugLine="Label2.TextColor= Colors.black";
_label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 527;BA.debugLine="Label2.TextSize= 15";
_label2.setTextSize((float) (15));
 //BA.debugLineNum = 528;BA.debugLine="Label2.Gravity=Gravity.CENTER";
_label2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 529;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 530;BA.debugLine="Label2.Text=location_list.Get(i) 'set data from";
_label2.setText(_location_list.Get(_i));
 //BA.debugLineNum = 533;BA.debugLine="If i > id_list.size-1 Then i = id_list.size-1";
if (_i>_id_list.getSize()-1) { 
_i = (int) (_id_list.getSize()-1);};
 //BA.debugLineNum = 536;BA.debugLine="PanelTop=PanelTop+PanelHeight";
_paneltop = (int) (_paneltop+_panelheight);
 }
};
 //BA.debugLineNum = 538;BA.debugLine="Panel0.Height=PanelTop";
_panel0.setHeight(_paneltop);
 //BA.debugLineNum = 540;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 542;BA.debugLine="dialog_panel.AddView(scrolllista,1%x,1%y,75%x,78%";
mostCurrent._dialog_panel.AddView((android.view.View)(mostCurrent._scrolllista.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (78),mostCurrent.activityBA));
 //BA.debugLineNum = 543;BA.debugLine="cd.AddView(dialog_panel,75%x,78%y)";
_cd.AddView((android.view.View)(mostCurrent._dialog_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (78),mostCurrent.activityBA));
 //BA.debugLineNum = 545;BA.debugLine="cd.Show(\"List of people\", \"CANCEL\", \"VIEW\", \"\", N";
_cd.Show("List of people","CANCEL","VIEW","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 547;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 1011;BA.debugLine="Sub list_btn_Click";
 //BA.debugLineNum = 1012;BA.debugLine="ProgressDialogShow2(\"Loading data, Please Wait...";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Loading data, Please Wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 1013;BA.debugLine="If scrolllista.IsInitialized == True Then";
if (mostCurrent._scrolllista.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 1014;BA.debugLine="scrolllista.RemoveView";
mostCurrent._scrolllista.RemoveView();
 //BA.debugLineNum = 1015;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 };
 //BA.debugLineNum = 1017;BA.debugLine="scrolllista.Initialize(500)";
mostCurrent._scrolllista.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 1018;BA.debugLine="dialog_panel.Initialize(\"dialog_panel\")";
mostCurrent._dialog_panel.Initialize(mostCurrent.activityBA,"dialog_panel");
 //BA.debugLineNum = 1019;BA.debugLine="dialog_all_panel.Initialize(\"dialog_all_panel\")";
mostCurrent._dialog_all_panel.Initialize(mostCurrent.activityBA,"dialog_all_panel");
 //BA.debugLineNum = 1021;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1022;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 1023;BA.debugLine="Dim bgnd As ColorDrawable";
_bgnd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 1024;BA.debugLine="bgnd.Initialize(Colors.Cyan, 5dip)";
_bgnd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Cyan,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 1025;BA.debugLine="pnl.Background = bgnd";
_pnl.setBackground((android.graphics.drawable.Drawable)(_bgnd.getObject()));
 //BA.debugLineNum = 1026;BA.debugLine="dialog_all_panel.Color = Colors.Transparent";
mostCurrent._dialog_all_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 1030;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 1041;BA.debugLine="Dim Bitmap1 As Bitmap";
_bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 1042;BA.debugLine="Dim Panel0 As Panel";
_panel0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1043;BA.debugLine="Dim PanelTop, PanelHeight  As Int";
_paneltop = 0;
_panelheight = 0;
 //BA.debugLineNum = 1048;BA.debugLine="Bitmap1.Initialize(File.DirAssets,\"banner1.png\")";
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner1.png");
 //BA.debugLineNum = 1049;BA.debugLine="PanelTop=1%y";
_paneltop = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA);
 //BA.debugLineNum = 1050;BA.debugLine="Panel0=scrolllista.Panel";
_panel0 = mostCurrent._scrolllista.getPanel();
 //BA.debugLineNum = 1051;BA.debugLine="Panel0.Color=Colors.argb(0,0,0,0)  'sets the invi";
_panel0.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 1053;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step23 = 1;
final int limit23 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step23 > 0 && _i <= limit23) || (step23 < 0 && _i >= limit23); _i = ((int)(0 + _i + step23)) ) {
 //BA.debugLineNum = 1055;BA.debugLine="If i>0 And i<3 Then Bitmap1.Initialize(File.DirA";
if (_i>0 && _i<3) { 
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner.png");};
 //BA.debugLineNum = 1056;BA.debugLine="Dim ImageView1 As ImageView";
_imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 1057;BA.debugLine="ImageView1.Initialize(\"data_list\")";
_imageview1.Initialize(mostCurrent.activityBA,"data_list");
 //BA.debugLineNum = 1058;BA.debugLine="PanelHeight=12%y";
_panelheight = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA);
 //BA.debugLineNum = 1060;BA.debugLine="Panel0.AddView(ImageView1,1%x,PanelTop,71%x,Pane";
_panel0.AddView((android.view.View)(_imageview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),_paneltop,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 1061;BA.debugLine="ImageView1.Tag=i&\"1\"";
_imageview1.setTag((Object)(BA.NumberToString(_i)+"1"));
 //BA.debugLineNum = 1062;BA.debugLine="ImageView1.Bitmap=Bitmap1";
_imageview1.setBitmap((android.graphics.Bitmap)(_bitmap1.getObject()));
 //BA.debugLineNum = 1063;BA.debugLine="ImageView1.Gravity=Gravity.fill";
_imageview1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 1065;BA.debugLine="Dim Label1, Label2 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1066;BA.debugLine="Label1.Initialize(\"\")";
_label1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1067;BA.debugLine="Label2.Initialize(\"\")";
_label2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1068;BA.debugLine="Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 1069;BA.debugLine="Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 1071;BA.debugLine="Label1.TextColor= Colors.White";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1072;BA.debugLine="Label1.TextSize= 18";
_label1.setTextSize((float) (18));
 //BA.debugLineNum = 1073;BA.debugLine="Label1.Typeface = Typeface.DEFAULT_BOLD";
_label1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 1074;BA.debugLine="Label1.Gravity=Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1075;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 1076;BA.debugLine="Label1.Text=fullN_llist.Get(i) 'set data from li";
_label1.setText(_fulln_llist.Get(_i));
 //BA.debugLineNum = 1078;BA.debugLine="Label2.TextColor= Colors.White";
_label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 1079;BA.debugLine="Label2.TextSize= 15";
_label2.setTextSize((float) (15));
 //BA.debugLineNum = 1080;BA.debugLine="Label2.Gravity=Gravity.CENTER";
_label2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1081;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 1082;BA.debugLine="Label2.Text=location_list.Get(i) 'set data from";
_label2.setText(_location_list.Get(_i));
 //BA.debugLineNum = 1085;BA.debugLine="If i > id_list.size-1 Then i = id_list.size-1";
if (_i>_id_list.getSize()-1) { 
_i = (int) (_id_list.getSize()-1);};
 //BA.debugLineNum = 1088;BA.debugLine="PanelTop=PanelTop+PanelHeight";
_paneltop = (int) (_paneltop+_panelheight);
 }
};
 //BA.debugLineNum = 1090;BA.debugLine="Panel0.Height=PanelTop";
_panel0.setHeight(_paneltop);
 //BA.debugLineNum = 1092;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 1093;BA.debugLine="dialog_panel.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._dialog_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 1095;BA.debugLine="Dim dialog_panel_can_btn As Button";
_dialog_panel_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 1096;BA.debugLine="Dim dialog_panel_tittle As Label";
_dialog_panel_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 1097;BA.debugLine="Dim btn_panel As Panel";
_btn_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 1098;BA.debugLine="btn_panel.Initialize(\"\")";
_btn_panel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 1099;BA.debugLine="btn_panel.SetBackgroundImage(LoadBitmap(File.DirA";
_btn_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 1100;BA.debugLine="dialog_panel_can_btn.Initialize(\"dialog_panel_can";
_dialog_panel_can_btn.Initialize(mostCurrent.activityBA,"dialog_panel_can_btn");
 //BA.debugLineNum = 1101;BA.debugLine="dialog_panel_tittle.Initialize(\"dialog_panel_titt";
_dialog_panel_tittle.Initialize(mostCurrent.activityBA,"dialog_panel_tittle");
 //BA.debugLineNum = 1102;BA.debugLine="dialog_panel_tittle.Text = \"LIST OF PEOPLE\"";
_dialog_panel_tittle.setText((Object)("LIST OF PEOPLE"));
 //BA.debugLineNum = 1103;BA.debugLine="dialog_panel_can_btn.Text = \"SEARCH\"";
_dialog_panel_can_btn.setText((Object)("SEARCH"));
 //BA.debugLineNum = 1104;BA.debugLine="Dim se_btn As GradientDrawable";
_se_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 1105;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 1106;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 1107;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 1108;BA.debugLine="se_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_se_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 1109;BA.debugLine="se_btn.CornerRadius = 50dip";
_se_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 1110;BA.debugLine="dialog_panel_can_btn.Background = se_btn";
_dialog_panel_can_btn.setBackground((android.graphics.drawable.Drawable)(_se_btn.getObject()));
 //BA.debugLineNum = 1111;BA.debugLine="dialog_panel_tittle.TextSize = 30";
_dialog_panel_tittle.setTextSize((float) (30));
 //BA.debugLineNum = 1112;BA.debugLine="dialog_panel_tittle.Gravity = Gravity.CENTER";
_dialog_panel_tittle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1113;BA.debugLine="dialog_panel_can_btn.Gravity = Gravity.CENTER";
_dialog_panel_can_btn.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 1114;BA.debugLine="dialog_panel.AddView(dialog_panel_tittle,1%x,2%y,";
mostCurrent._dialog_panel.AddView((android.view.View)(_dialog_panel_tittle.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (83),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1115;BA.debugLine="dialog_panel.AddView(scrolllista,5%x,dialog_panel";
mostCurrent._dialog_panel.AddView((android.view.View)(mostCurrent._scrolllista.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_dialog_panel_tittle.getTop()+_dialog_panel_tittle.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (69),mostCurrent.activityBA));
 //BA.debugLineNum = 1116;BA.debugLine="dialog_panel.AddView(btn_panel,1%x,79%y,83%x,10%y";
mostCurrent._dialog_panel.AddView((android.view.View)(_btn_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (83),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 1117;BA.debugLine="btn_panel.AddView(dialog_panel_can_btn,((btn_pane";
_btn_panel.AddView((android.view.View)(_dialog_panel_can_btn.getObject()),(int) (((_btn_panel.getWidth()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (42),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 1118;BA.debugLine="dialog_all_panel.AddView(dialog_panel,7.5%x,5%y,8";
mostCurrent._dialog_all_panel.AddView((android.view.View)(mostCurrent._dialog_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (7.5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 1119;BA.debugLine="dialog_all_panel.Color = Colors.ARGB(128,128,128,";
mostCurrent._dialog_all_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (128),(int) (128),(int) (.50)));
 //BA.debugLineNum = 1120;BA.debugLine="Activity.AddView(dialog_all_panel,0,0,100%x,100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._dialog_all_panel.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 1125;BA.debugLine="End Sub";
return "";
}
public static String  _load_list() throws Exception{
 //BA.debugLineNum = 150;BA.debugLine="Sub load_list";
 //BA.debugLineNum = 151;BA.debugLine="list_bloodgroup.Initialize";
_list_bloodgroup.Initialize();
 //BA.debugLineNum = 152;BA.debugLine="list_bloodgroup.Add(\"A\")";
_list_bloodgroup.Add((Object)("A"));
 //BA.debugLineNum = 153;BA.debugLine="list_bloodgroup.Add(\"B\")";
_list_bloodgroup.Add((Object)("B"));
 //BA.debugLineNum = 154;BA.debugLine="list_bloodgroup.Add(\"O\")";
_list_bloodgroup.Add((Object)("O"));
 //BA.debugLineNum = 155;BA.debugLine="list_bloodgroup.Add(\"AB\")";
_list_bloodgroup.Add((Object)("AB"));
 //BA.debugLineNum = 156;BA.debugLine="list_bloodgroup.Add(\"A+\")";
_list_bloodgroup.Add((Object)("A+"));
 //BA.debugLineNum = 157;BA.debugLine="list_bloodgroup.Add(\"B+\")";
_list_bloodgroup.Add((Object)("B+"));
 //BA.debugLineNum = 158;BA.debugLine="list_bloodgroup.Add(\"O+\")";
_list_bloodgroup.Add((Object)("O+"));
 //BA.debugLineNum = 159;BA.debugLine="list_bloodgroup.Add(\"AB+\")";
_list_bloodgroup.Add((Object)("AB+"));
 //BA.debugLineNum = 160;BA.debugLine="list_bloodgroup.Add(\"A-\")";
_list_bloodgroup.Add((Object)("A-"));
 //BA.debugLineNum = 161;BA.debugLine="list_bloodgroup.Add(\"B-\")";
_list_bloodgroup.Add((Object)("B-"));
 //BA.debugLineNum = 162;BA.debugLine="list_bloodgroup.Add(\"O-\")";
_list_bloodgroup.Add((Object)("O-"));
 //BA.debugLineNum = 163;BA.debugLine="list_bloodgroup.Add(\"AB-\")";
_list_bloodgroup.Add((Object)("AB-"));
 //BA.debugLineNum = 164;BA.debugLine="search_spiner.AddAll(list_bloodgroup)";
mostCurrent._search_spiner.AddAll(_list_bloodgroup);
 //BA.debugLineNum = 165;BA.debugLine="spin_item_click = \"A\";";
mostCurrent._spin_item_click = "A";
 //BA.debugLineNum = 166;BA.debugLine="End Sub";
return "";
}
public static String  _lv_itemclick(int _position,Object _value) throws Exception{
b4a.example.calculations _calc = null;
 //BA.debugLineNum = 455;BA.debugLine="Sub lv_ItemClick (Position As Int, Value As Object";
 //BA.debugLineNum = 456;BA.debugLine="Dim calc As calculations";
_calc = new b4a.example.calculations();
 //BA.debugLineNum = 457;BA.debugLine="calc.users_id = id_list.Get(Position)";
_calc._users_id = BA.ObjectToString(_id_list.Get(_position));
 //BA.debugLineNum = 460;BA.debugLine="End Sub";
return "";
}
public static String  _map_shows_pagefinished(String _url) throws Exception{
 //BA.debugLineNum = 331;BA.debugLine="Sub map_shows_PageFinished (Url As String)";
 //BA.debugLineNum = 332;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 333;BA.debugLine="End Sub";
return "";
}
public static String  _ok_vie_btn_click() throws Exception{
 //BA.debugLineNum = 796;BA.debugLine="Sub ok_vie_btn_click";
 //BA.debugLineNum = 797;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 798;BA.debugLine="End Sub";
return "";
}
public static String  _ok_vie_btn_longclick() throws Exception{
 //BA.debugLineNum = 799;BA.debugLine="Sub ok_vie_btn_LongClick";
 //BA.debugLineNum = 800;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 801;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Dim list_bloodgroup As List";
_list_bloodgroup = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 12;BA.debugLine="Dim id_list As List";
_id_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 13;BA.debugLine="Dim fullN_llist As List";
_fulln_llist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 14;BA.debugLine="Dim location_list As List";
_location_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 15;BA.debugLine="Dim lat_list As List";
_lat_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 16;BA.debugLine="Dim lng_list As List";
_lng_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 17;BA.debugLine="Dim donated_list As List";
_donated_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 18;BA.debugLine="Dim email_list As List";
_email_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 19;BA.debugLine="Dim nickname_list As List";
_nickname_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 20;BA.debugLine="Dim phone1_list As List";
_phone1_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 21;BA.debugLine="Dim phone2_list As List";
_phone2_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 22;BA.debugLine="Dim image_list As List";
_image_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 23;BA.debugLine="Dim age_list As List";
_age_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 24;BA.debugLine="Dim gender_list As List";
_gender_list = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 25;BA.debugLine="Dim is_complete As Int : is_complete = 0";
_is_complete = 0;
 //BA.debugLineNum = 25;BA.debugLine="Dim is_complete As Int : is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 27;BA.debugLine="Dim gpsClient As GPS";
_gpsclient = new anywheresoftware.b4a.gps.GPS();
 //BA.debugLineNum = 28;BA.debugLine="Dim userLocation As Location";
_userlocation = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim is_check_true As Boolean : is_check_true = Fal";
_is_check_true = false;
 //BA.debugLineNum = 29;BA.debugLine="Dim is_check_true As Boolean : is_check_true = Fal";
_is_check_true = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 31;BA.debugLine="Dim row_click As String";
_row_click = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim earth_radius As Float	: earth_radius = 6373 '";
_earth_radius = 0f;
 //BA.debugLineNum = 33;BA.debugLine="Dim earth_radius As Float	: earth_radius = 6373 '";
_earth_radius = (float) (6373);
 //BA.debugLineNum = 34;BA.debugLine="Dim pi As Float : pi = 3.1416 'the default value";
_pi = 0f;
 //BA.debugLineNum = 34;BA.debugLine="Dim pi As Float : pi = 3.1416 'the default value";
_pi = (float) (3.1416);
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return "";
}
public static String  _reading_txt() throws Exception{
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
 //BA.debugLineNum = 864;BA.debugLine="Sub reading_txt";
 //BA.debugLineNum = 865;BA.debugLine="id_list.Initialize";
_id_list.Initialize();
 //BA.debugLineNum = 866;BA.debugLine="fullN_llist.Initialize";
_fulln_llist.Initialize();
 //BA.debugLineNum = 867;BA.debugLine="location_list.Initialize";
_location_list.Initialize();
 //BA.debugLineNum = 868;BA.debugLine="lat_list.Initialize";
_lat_list.Initialize();
 //BA.debugLineNum = 869;BA.debugLine="lng_list.Initialize";
_lng_list.Initialize();
 //BA.debugLineNum = 870;BA.debugLine="donated_list.Initialize";
_donated_list.Initialize();
 //BA.debugLineNum = 871;BA.debugLine="email_list.Initialize";
_email_list.Initialize();
 //BA.debugLineNum = 872;BA.debugLine="nickname_list.Initialize";
_nickname_list.Initialize();
 //BA.debugLineNum = 873;BA.debugLine="phone1_list.Initialize";
_phone1_list.Initialize();
 //BA.debugLineNum = 874;BA.debugLine="phone2_list.Initialize";
_phone2_list.Initialize();
 //BA.debugLineNum = 875;BA.debugLine="image_list.Initialize";
_image_list.Initialize();
 //BA.debugLineNum = 876;BA.debugLine="age_list.Initialize";
_age_list.Initialize();
 //BA.debugLineNum = 877;BA.debugLine="gender_list.Initialize";
_gender_list.Initialize();
 //BA.debugLineNum = 879;BA.debugLine="Dim TextReader_id As TextReader";
_textreader_id = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 880;BA.debugLine="TextReader_id.Initialize(File.OpenInput(File.D";
_textreader_id.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt").getObject()));
 //BA.debugLineNum = 881;BA.debugLine="Dim line_id As String";
_line_id = "";
 //BA.debugLineNum = 882;BA.debugLine="line_id = TextReader_id.ReadLine";
_line_id = _textreader_id.ReadLine();
 //BA.debugLineNum = 883;BA.debugLine="Do While line_id <> Null";
while (_line_id!= null) {
 //BA.debugLineNum = 884;BA.debugLine="id_list.Add(line_id)";
_id_list.Add((Object)(_line_id));
 //BA.debugLineNum = 885;BA.debugLine="line_id = TextReader_id.ReadLine";
_line_id = _textreader_id.ReadLine();
 }
;
 //BA.debugLineNum = 887;BA.debugLine="TextReader_id.Close";
_textreader_id.Close();
 //BA.debugLineNum = 889;BA.debugLine="Dim TextReader_fullN As TextReader";
_textreader_fulln = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 890;BA.debugLine="TextReader_fullN.Initialize(File.OpenInput(Fil";
_textreader_fulln.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt").getObject()));
 //BA.debugLineNum = 891;BA.debugLine="Dim line_fullN As String";
_line_fulln = "";
 //BA.debugLineNum = 892;BA.debugLine="line_fullN = TextReader_fullN.ReadLine";
_line_fulln = _textreader_fulln.ReadLine();
 //BA.debugLineNum = 893;BA.debugLine="Do While line_fullN <> Null";
while (_line_fulln!= null) {
 //BA.debugLineNum = 895;BA.debugLine="fullN_llist.Add(line_fullN)";
_fulln_llist.Add((Object)(_line_fulln));
 //BA.debugLineNum = 896;BA.debugLine="line_fullN = TextReader_fullN.ReadLine";
_line_fulln = _textreader_fulln.ReadLine();
 }
;
 //BA.debugLineNum = 898;BA.debugLine="TextReader_fullN.Close";
_textreader_fulln.Close();
 //BA.debugLineNum = 900;BA.debugLine="Dim TextReader_location As TextReader";
_textreader_location = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 901;BA.debugLine="TextReader_location.Initialize(File.OpenInput(";
_textreader_location.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt").getObject()));
 //BA.debugLineNum = 902;BA.debugLine="Dim line_location As String";
_line_location = "";
 //BA.debugLineNum = 903;BA.debugLine="line_location = TextReader_location.ReadLine";
_line_location = _textreader_location.ReadLine();
 //BA.debugLineNum = 904;BA.debugLine="Do While line_location <> Null";
while (_line_location!= null) {
 //BA.debugLineNum = 906;BA.debugLine="location_list.Add(line_location)";
_location_list.Add((Object)(_line_location));
 //BA.debugLineNum = 907;BA.debugLine="line_location = TextReader_location.ReadLi";
_line_location = _textreader_location.ReadLine();
 }
;
 //BA.debugLineNum = 909;BA.debugLine="TextReader_location.Close";
_textreader_location.Close();
 //BA.debugLineNum = 911;BA.debugLine="Dim TextReader_lat As TextReader";
_textreader_lat = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 912;BA.debugLine="TextReader_lat.Initialize(File.OpenInput(File.";
_textreader_lat.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt").getObject()));
 //BA.debugLineNum = 913;BA.debugLine="Dim line_lat As String";
_line_lat = "";
 //BA.debugLineNum = 914;BA.debugLine="line_lat = TextReader_lat.ReadLine";
_line_lat = _textreader_lat.ReadLine();
 //BA.debugLineNum = 915;BA.debugLine="Do While line_lat <> Null";
while (_line_lat!= null) {
 //BA.debugLineNum = 916;BA.debugLine="lat_list.Add(line_lat)";
_lat_list.Add((Object)(_line_lat));
 //BA.debugLineNum = 917;BA.debugLine="line_lat = TextReader_lat.ReadLine";
_line_lat = _textreader_lat.ReadLine();
 }
;
 //BA.debugLineNum = 919;BA.debugLine="TextReader_lat.Close";
_textreader_lat.Close();
 //BA.debugLineNum = 921;BA.debugLine="Dim TextReader_lng As TextReader";
_textreader_lng = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 922;BA.debugLine="TextReader_lng.Initialize(File.OpenInput(File.";
_textreader_lng.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt").getObject()));
 //BA.debugLineNum = 923;BA.debugLine="Dim line_lng As String";
_line_lng = "";
 //BA.debugLineNum = 924;BA.debugLine="line_lng = TextReader_lng.ReadLine";
_line_lng = _textreader_lng.ReadLine();
 //BA.debugLineNum = 925;BA.debugLine="Do While line_lng <> Null";
while (_line_lng!= null) {
 //BA.debugLineNum = 926;BA.debugLine="lng_list.Add(line_lng)";
_lng_list.Add((Object)(_line_lng));
 //BA.debugLineNum = 927;BA.debugLine="line_lng = TextReader_lng.ReadLine";
_line_lng = _textreader_lng.ReadLine();
 }
;
 //BA.debugLineNum = 929;BA.debugLine="TextReader_lng.Close";
_textreader_lng.Close();
 //BA.debugLineNum = 931;BA.debugLine="Dim TextReader_donate As TextReader";
_textreader_donate = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 932;BA.debugLine="TextReader_donate.Initialize(File.OpenInput(Fi";
_textreader_donate.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_donated.txt").getObject()));
 //BA.debugLineNum = 933;BA.debugLine="Dim line_donate As String";
_line_donate = "";
 //BA.debugLineNum = 934;BA.debugLine="line_donate = TextReader_donate.ReadLine";
_line_donate = _textreader_donate.ReadLine();
 //BA.debugLineNum = 935;BA.debugLine="Do While line_donate <> Null";
while (_line_donate!= null) {
 //BA.debugLineNum = 936;BA.debugLine="donated_list.Add(line_donate)";
_donated_list.Add((Object)(_line_donate));
 //BA.debugLineNum = 937;BA.debugLine="line_donate = TextReader_donate.ReadLine";
_line_donate = _textreader_donate.ReadLine();
 }
;
 //BA.debugLineNum = 939;BA.debugLine="TextReader_donate.Close";
_textreader_donate.Close();
 //BA.debugLineNum = 941;BA.debugLine="Dim TextReader_email As TextReader";
_textreader_email = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 942;BA.debugLine="TextReader_email.Initialize(File.OpenInput(Fil";
_textreader_email.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_email.txt").getObject()));
 //BA.debugLineNum = 943;BA.debugLine="Dim line_email As String";
_line_email = "";
 //BA.debugLineNum = 944;BA.debugLine="line_email = TextReader_email.ReadLine";
_line_email = _textreader_email.ReadLine();
 //BA.debugLineNum = 945;BA.debugLine="Do While line_email <> Null";
while (_line_email!= null) {
 //BA.debugLineNum = 946;BA.debugLine="email_list.Add(line_email)";
_email_list.Add((Object)(_line_email));
 //BA.debugLineNum = 947;BA.debugLine="line_email = TextReader_email.ReadLine";
_line_email = _textreader_email.ReadLine();
 }
;
 //BA.debugLineNum = 949;BA.debugLine="TextReader_email.Close";
_textreader_email.Close();
 //BA.debugLineNum = 951;BA.debugLine="Dim TextReader_nickname As TextReader";
_textreader_nickname = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 952;BA.debugLine="TextReader_nickname.Initialize(File.OpenInput(";
_textreader_nickname.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_nickname.txt").getObject()));
 //BA.debugLineNum = 953;BA.debugLine="Dim line_nickname As String";
_line_nickname = "";
 //BA.debugLineNum = 954;BA.debugLine="line_nickname = TextReader_nickname.ReadLine";
_line_nickname = _textreader_nickname.ReadLine();
 //BA.debugLineNum = 955;BA.debugLine="Do While line_nickname <> Null";
while (_line_nickname!= null) {
 //BA.debugLineNum = 956;BA.debugLine="nickname_list.Add(line_nickname)";
_nickname_list.Add((Object)(_line_nickname));
 //BA.debugLineNum = 957;BA.debugLine="line_nickname = TextReader_nickname.ReadLi";
_line_nickname = _textreader_nickname.ReadLine();
 }
;
 //BA.debugLineNum = 959;BA.debugLine="TextReader_nickname.Close";
_textreader_nickname.Close();
 //BA.debugLineNum = 961;BA.debugLine="Dim TextReader_phone1 As TextReader";
_textreader_phone1 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 962;BA.debugLine="TextReader_phone1.Initialize(File.OpenInput(Fi";
_textreader_phone1.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone1.txt").getObject()));
 //BA.debugLineNum = 963;BA.debugLine="Dim line_phone1 As String";
_line_phone1 = "";
 //BA.debugLineNum = 964;BA.debugLine="line_phone1 = TextReader_phone1.ReadLine";
_line_phone1 = _textreader_phone1.ReadLine();
 //BA.debugLineNum = 965;BA.debugLine="Do While line_phone1 <> Null";
while (_line_phone1!= null) {
 //BA.debugLineNum = 966;BA.debugLine="phone1_list.Add(line_phone1)";
_phone1_list.Add((Object)(_line_phone1));
 //BA.debugLineNum = 967;BA.debugLine="line_phone1 = TextReader_phone1.ReadLine";
_line_phone1 = _textreader_phone1.ReadLine();
 }
;
 //BA.debugLineNum = 969;BA.debugLine="TextReader_phone1.Close";
_textreader_phone1.Close();
 //BA.debugLineNum = 971;BA.debugLine="Dim TextReader_phone2 As TextReader";
_textreader_phone2 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 972;BA.debugLine="TextReader_phone2.Initialize(File.OpenInput(Fi";
_textreader_phone2.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone2.txt").getObject()));
 //BA.debugLineNum = 973;BA.debugLine="Dim line_phone2 As String";
_line_phone2 = "";
 //BA.debugLineNum = 974;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 //BA.debugLineNum = 975;BA.debugLine="Do While line_phone2 <> Null";
while (_line_phone2!= null) {
 //BA.debugLineNum = 976;BA.debugLine="phone2_list.Add(line_phone2)";
_phone2_list.Add((Object)(_line_phone2));
 //BA.debugLineNum = 977;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 }
;
 //BA.debugLineNum = 979;BA.debugLine="TextReader_phone2.Close";
_textreader_phone2.Close();
 //BA.debugLineNum = 981;BA.debugLine="Dim TextReader_image As TextReader";
_textreader_image = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 982;BA.debugLine="TextReader_image.Initialize(File.OpenInput(Fil";
_textreader_image.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_image.txt").getObject()));
 //BA.debugLineNum = 983;BA.debugLine="Dim line_image As String";
_line_image = "";
 //BA.debugLineNum = 984;BA.debugLine="line_image = TextReader_image.ReadLine";
_line_image = _textreader_image.ReadLine();
 //BA.debugLineNum = 985;BA.debugLine="Do While line_image <> Null";
while (_line_image!= null) {
 //BA.debugLineNum = 986;BA.debugLine="image_list.Add(line_image)";
_image_list.Add((Object)(_line_image));
 //BA.debugLineNum = 987;BA.debugLine="line_image = TextReader_image.ReadLine";
_line_image = _textreader_image.ReadLine();
 }
;
 //BA.debugLineNum = 989;BA.debugLine="TextReader_image.Close";
_textreader_image.Close();
 //BA.debugLineNum = 991;BA.debugLine="Dim TextReader_age As TextReader";
_textreader_age = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 992;BA.debugLine="TextReader_age.Initialize(File.OpenInput(File.";
_textreader_age.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_age.txt").getObject()));
 //BA.debugLineNum = 993;BA.debugLine="Dim line_age As String";
_line_age = "";
 //BA.debugLineNum = 994;BA.debugLine="line_age = TextReader_age.ReadLine";
_line_age = _textreader_age.ReadLine();
 //BA.debugLineNum = 995;BA.debugLine="Do While line_age <> Null";
while (_line_age!= null) {
 //BA.debugLineNum = 996;BA.debugLine="age_list.Add(line_age)";
_age_list.Add((Object)(_line_age));
 //BA.debugLineNum = 997;BA.debugLine="line_age = TextReader_age.ReadLine";
_line_age = _textreader_age.ReadLine();
 }
;
 //BA.debugLineNum = 999;BA.debugLine="TextReader_age.Close";
_textreader_age.Close();
 //BA.debugLineNum = 1001;BA.debugLine="Dim TextReader_gender As TextReader";
_textreader_gender = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 1002;BA.debugLine="TextReader_gender.Initialize(File.OpenInput(Fi";
_textreader_gender.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_gender.txt").getObject()));
 //BA.debugLineNum = 1003;BA.debugLine="Dim line_gender As String";
_line_gender = "";
 //BA.debugLineNum = 1004;BA.debugLine="line_gender = TextReader_gender.ReadLine";
_line_gender = _textreader_gender.ReadLine();
 //BA.debugLineNum = 1005;BA.debugLine="Do While line_gender <> Null";
while (_line_gender!= null) {
 //BA.debugLineNum = 1006;BA.debugLine="gender_list.Add(line_gender)";
_gender_list.Add((Object)(_line_gender));
 //BA.debugLineNum = 1007;BA.debugLine="line_gender = TextReader_gender.ReadLine";
_line_gender = _textreader_gender.ReadLine();
 }
;
 //BA.debugLineNum = 1009;BA.debugLine="TextReader_gender.Close";
_textreader_gender.Close();
 //BA.debugLineNum = 1010;BA.debugLine="End Sub";
return "";
}
public static String  _search_btn_click() throws Exception{
b4a.example.calculations _url_back = null;
String _url_id = "";
String _full_name = "";
String _location = "";
String _lat = "";
String _lng = "";
String _donated = "";
String _email = "";
String _nickname = "";
String _phone1 = "";
String _phone2 = "";
String _image = "";
String _age = "";
String _gender = "";
 //BA.debugLineNum = 176;BA.debugLine="Sub search_btn_Click";
 //BA.debugLineNum = 177;BA.debugLine="ProgressDialogShow2(\"please wait.!!\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"please wait.!!",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 179;BA.debugLine="Dim url_back As calculations";
_url_back = new b4a.example.calculations();
 //BA.debugLineNum = 180;BA.debugLine="Dim url_id,full_name,location,lat,lng,donated,ema";
_url_id = "";
_full_name = "";
_location = "";
_lat = "";
_lng = "";
_donated = "";
_email = "";
_nickname = "";
_phone1 = "";
_phone2 = "";
_image = "";
_age = "";
_gender = "";
 //BA.debugLineNum = 181;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 182;BA.debugLine="url_id = url_back.php_email_url(\"/bloodlifePHP/se";
_url_id = _url_back._php_email_url("/bloodlifePHP/search_blood_id.php");
 //BA.debugLineNum = 183;BA.debugLine="full_name = url_back.php_email_url(\"/bloodlifePHP";
_full_name = _url_back._php_email_url("/bloodlifePHP/search_blood_fullN.php");
 //BA.debugLineNum = 184;BA.debugLine="location = url_back.php_email_url(\"/bloodlifePHP/";
_location = _url_back._php_email_url("/bloodlifePHP/search_blood_location.php");
 //BA.debugLineNum = 185;BA.debugLine="lat = url_back.php_email_url(\"/bloodlifePHP/searc";
_lat = _url_back._php_email_url("/bloodlifePHP/search_blood_lat.php");
 //BA.debugLineNum = 186;BA.debugLine="lng = url_back.php_email_url(\"/bloodlifePHP/searc";
_lng = _url_back._php_email_url("/bloodlifePHP/search_blood_long.php");
 //BA.debugLineNum = 187;BA.debugLine="donated = url_back.php_email_url(\"/bloodlifePHP/s";
_donated = _url_back._php_email_url("/bloodlifePHP/search_blood_donateB.php");
 //BA.debugLineNum = 188;BA.debugLine="email = url_back.php_email_url(\"/bloodlifePHP/sea";
_email = _url_back._php_email_url("/bloodlifePHP/search_blood_email.php");
 //BA.debugLineNum = 189;BA.debugLine="nickname = url_back.php_email_url(\"/bloodlifePHP/";
_nickname = _url_back._php_email_url("/bloodlifePHP/search_blood_nickN.php");
 //BA.debugLineNum = 190;BA.debugLine="phone1 = url_back.php_email_url(\"/bloodlifePHP/se";
_phone1 = _url_back._php_email_url("/bloodlifePHP/search_blood_phone1.php");
 //BA.debugLineNum = 191;BA.debugLine="phone2 = url_back.php_email_url(\"/bloodlifePHP/se";
_phone2 = _url_back._php_email_url("/bloodlifePHP/search_blood_phone2.php");
 //BA.debugLineNum = 192;BA.debugLine="image = url_back.php_email_url(\"/bloodlifePHP/sea";
_image = _url_back._php_email_url("/bloodlifePHP/search_blood_image.php");
 //BA.debugLineNum = 193;BA.debugLine="age = url_back.php_email_url(\"/bloodlifePHP/searc";
_age = _url_back._php_email_url("/bloodlifePHP/search_blood_age.php");
 //BA.debugLineNum = 194;BA.debugLine="gender = url_back.php_email_url(\"/bloodlifePHP/se";
_gender = _url_back._php_email_url("/bloodlifePHP/search_blood_gender.php");
 //BA.debugLineNum = 198;BA.debugLine="data_query_id.Download2(url_id,Array As String(\"i";
mostCurrent._data_query_id._download2(_url_id,new String[]{"id","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 199;BA.debugLine="data_query_fullN.Download2(full_name,Array As Str";
mostCurrent._data_query_fulln._download2(_full_name,new String[]{"full_name","SELECT full_name FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 200;BA.debugLine="data_query_location.Download2(location,Array As S";
mostCurrent._data_query_location._download2(_location,new String[]{"location","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 201;BA.debugLine="query_lat.Download2(lat,Array As String(\"lat\",\"SE";
mostCurrent._query_lat._download2(_lat,new String[]{"lat","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 202;BA.debugLine="query_lng.Download2(lng,Array As String(\"long\",\"S";
mostCurrent._query_lng._download2(_lng,new String[]{"long","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 204;BA.debugLine="data_query_donated.Download2(donated,Array As Str";
mostCurrent._data_query_donated._download2(_donated,new String[]{"donate_b","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 205;BA.debugLine="data_query_email.Download2(email,Array As String(";
mostCurrent._data_query_email._download2(_email,new String[]{"email","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 206;BA.debugLine="data_query_nickname.Download2(nickname,Array As S";
mostCurrent._data_query_nickname._download2(_nickname,new String[]{"nick","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 207;BA.debugLine="data_query_phone1.Download2(phone1,Array As Strin";
mostCurrent._data_query_phone1._download2(_phone1,new String[]{"phone1","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 208;BA.debugLine="data_query_phone2.Download2(phone2,Array As Strin";
mostCurrent._data_query_phone2._download2(_phone2,new String[]{"phone2","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 210;BA.debugLine="data_query_image.Download2(image,Array As String(";
mostCurrent._data_query_image._download2(_image,new String[]{"image","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 212;BA.debugLine="data_query_age.Download2(age,Array As String(\"age";
mostCurrent._data_query_age._download2(_age,new String[]{"age","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 213;BA.debugLine="data_query_gender.Download2(gender,Array As Strin";
mostCurrent._data_query_gender._download2(_gender,new String[]{"gender","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 218;BA.debugLine="End Sub";
return "";
}
public static String  _search_spiner_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 452;BA.debugLine="Sub search_spiner_ItemClick (Position As Int, Valu";
 //BA.debugLineNum = 453;BA.debugLine="spin_item_click = search_spiner.GetItem(Position)";
mostCurrent._spin_item_click = mostCurrent._search_spiner.GetItem(_position).trim();
 //BA.debugLineNum = 454;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 805;BA.debugLine="Sub user_image_click";
 //BA.debugLineNum = 806;BA.debugLine="If user_img_panl.IsInitialized == True Then";
if (mostCurrent._user_img_panl.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 807;BA.debugLine="user_img_panl.RemoveView";
mostCurrent._user_img_panl.RemoveView();
 };
 //BA.debugLineNum = 809;BA.debugLine="user_img_panl.Initialize(\"user_img_panl\")";
mostCurrent._user_img_panl.Initialize(mostCurrent.activityBA,"user_img_panl");
 //BA.debugLineNum = 810;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 811;BA.debugLine="Dim user_imgClose_btn As Button";
_user_imgclose_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 812;BA.debugLine="Dim img_user_webview As WebView";
_img_user_webview = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 813;BA.debugLine="Dim user_img_view As ImageView";
_user_img_view = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 814;BA.debugLine="user_imgClose_btn.Initialize(\"user_imgClose_btn\")";
_user_imgclose_btn.Initialize(mostCurrent.activityBA,"user_imgClose_btn");
 //BA.debugLineNum = 815;BA.debugLine="user_img_view.Initialize(\"user_img_view\")";
_user_img_view.Initialize(mostCurrent.activityBA,"user_img_view");
 //BA.debugLineNum = 816;BA.debugLine="user_imgClose_btn.Text = \"CLOSE\"";
_user_imgclose_btn.setText((Object)("CLOSE"));
 //BA.debugLineNum = 817;BA.debugLine="Dim V_btn,C_btn As GradientDrawable";
_v_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
_c_btn = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 818;BA.debugLine="Dim colorG(2) As Int";
_colorg = new int[(int) (2)];
;
 //BA.debugLineNum = 819;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 820;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 821;BA.debugLine="C_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_c_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 822;BA.debugLine="V_btn.Initialize(\"TOP_BOTTOM\",colorG)";
_v_btn.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 823;BA.debugLine="V_btn.CornerRadius = 50dip";
_v_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 824;BA.debugLine="C_btn.CornerRadius = 50dip";
_c_btn.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 825;BA.debugLine="user_imgClose_btn.Background = V_btn";
_user_imgclose_btn.setBackground((android.graphics.drawable.Drawable)(_v_btn.getObject()));
 //BA.debugLineNum = 826;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 827;BA.debugLine="img_user_webview.Initialize(\"img_user_webview\")";
_img_user_webview.Initialize(mostCurrent.activityBA,"img_user_webview");
 //BA.debugLineNum = 828;BA.debugLine="user_img_panl.Color = Colors.Transparent";
mostCurrent._user_img_panl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 829;BA.debugLine="pnl.SetBackgroundImage(LoadBitmap(File.DirAssets,";
_pnl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 831;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 832;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 833;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 834;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 835;BA.debugLine="bytes = su.DecodeBase64(image_list.Get(row_click)";
_bytes = _su.DecodeBase64(BA.ObjectToString(_image_list.Get((int)(Double.parseDouble(_row_click)))));
 //BA.debugLineNum = 836;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 837;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 839;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 840;BA.debugLine="bd.Initialize(bmp)";
_bd.Initialize((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 842;BA.debugLine="user_img_view.Background = bd";
_user_img_view.setBackground((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 847;BA.debugLine="pnl.AddView(user_img_view,1%x,1%y,86%x,75%y)";
_pnl.AddView((android.view.View)(_user_img_view.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (86),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (75),mostCurrent.activityBA));
 //BA.debugLineNum = 848;BA.debugLine="pnl.AddView(user_imgClose_btn,1%x,user_img_view.T";
_pnl.AddView((android.view.View)(_user_imgclose_btn.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_user_img_view.getTop()+_user_img_view.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (86),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 849;BA.debugLine="user_img_panl.AddView(pnl,6%x,(((((Activity.Heigh";
mostCurrent._user_img_panl.AddView((android.view.View)(_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),(int) ((((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (88),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (89),mostCurrent.activityBA));
 //BA.debugLineNum = 850;BA.debugLine="user_img_panl.BringToFront";
mostCurrent._user_img_panl.BringToFront();
 //BA.debugLineNum = 853;BA.debugLine="Activity.AddView(user_img_panl,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._user_img_panl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 854;BA.debugLine="End Sub";
return "";
}
public static String  _user_img_panl_click() throws Exception{
 //BA.debugLineNum = 858;BA.debugLine="Sub user_img_panl_click";
 //BA.debugLineNum = 860;BA.debugLine="End Sub";
return "";
}
public static String  _user_imgclose_btn_click() throws Exception{
 //BA.debugLineNum = 855;BA.debugLine="Sub user_imgClose_btn_click";
 //BA.debugLineNum = 856;BA.debugLine="user_img_panl.RemoveView";
mostCurrent._user_img_panl.RemoveView();
 //BA.debugLineNum = 857;BA.debugLine="End Sub";
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
anywheresoftware.b4a.objects.LabelWrapper _phone1 = null;
anywheresoftware.b4a.objects.LabelWrapper _phone2 = null;
anywheresoftware.b4a.objects.LabelWrapper _age = null;
anywheresoftware.b4a.objects.LabelWrapper _gender = null;
anywheresoftware.b4a.objects.PanelWrapper _fn_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _loc_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _don_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _ema_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _ph1_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _ph2_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _btn_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _age_pnl = null;
anywheresoftware.b4a.objects.PanelWrapper _gender_pnl = null;
anywheresoftware.b4a.objects.ImageViewWrapper _user_image = null;
anywheresoftware.b4a.objects.ImageViewWrapper _fn_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _loc_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _don_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _ema_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _ph1_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _ph2_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _age_img = null;
anywheresoftware.b4a.objects.ImageViewWrapper _gender_img = null;
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
 //BA.debugLineNum = 596;BA.debugLine="Sub vie_btn_click";
 //BA.debugLineNum = 598;BA.debugLine="view_info_pnl.RemoveView";
mostCurrent._view_info_pnl.RemoveView();
 //BA.debugLineNum = 599;BA.debugLine="If view_data_info_person.IsInitialized == True T";
if (mostCurrent._view_data_info_person.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 600;BA.debugLine="view_data_info_person.RemoveView";
mostCurrent._view_data_info_person.RemoveView();
 //BA.debugLineNum = 601;BA.debugLine="scroll_view_info.RemoveView";
mostCurrent._scroll_view_info.RemoveView();
 }else {
 };
 //BA.debugLineNum = 604;BA.debugLine="view_data_info_person.Initialize(\"view_data_info_";
mostCurrent._view_data_info_person.Initialize(mostCurrent.activityBA,"view_data_info_person");
 //BA.debugLineNum = 605;BA.debugLine="scroll_view_info.Initialize(74%x,57%y,\"scroll_vie";
mostCurrent._scroll_view_info.Initialize(mostCurrent.activityBA,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (57),mostCurrent.activityBA),"scroll_view_info");
 //BA.debugLineNum = 607;BA.debugLine="Dim view_panl,view_for_image,view_for_btn As Pane";
_view_panl = new anywheresoftware.b4a.objects.PanelWrapper();
_view_for_image = new anywheresoftware.b4a.objects.PanelWrapper();
_view_for_btn = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 608;BA.debugLine="Dim tittle,fullname,location,donated,email,phone1";
_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
_fullname = new anywheresoftware.b4a.objects.LabelWrapper();
_location = new anywheresoftware.b4a.objects.LabelWrapper();
_donated = new anywheresoftware.b4a.objects.LabelWrapper();
_email = new anywheresoftware.b4a.objects.LabelWrapper();
_phone1 = new anywheresoftware.b4a.objects.LabelWrapper();
_phone2 = new anywheresoftware.b4a.objects.LabelWrapper();
_age = new anywheresoftware.b4a.objects.LabelWrapper();
_gender = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 609;BA.debugLine="Dim fn_pnl,loc_pnl,don_pnl,ema_pnl,ph1_pnl,ph2_pn";
_fn_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_loc_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_don_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_ema_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_ph1_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_ph2_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_btn_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_age_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
_gender_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 610;BA.debugLine="Dim user_image,fn_img,loc_img,don_img,ema_img,ph1";
_user_image = new anywheresoftware.b4a.objects.ImageViewWrapper();
_fn_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_loc_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_don_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ema_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ph1_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_ph2_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_age_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
_gender_img = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 611;BA.debugLine="fn_img.Initialize(\"\")";
_fn_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 612;BA.debugLine="loc_img.Initialize(\"\")";
_loc_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 613;BA.debugLine="don_img.Initialize(\"\")";
_don_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 614;BA.debugLine="ema_img.Initialize(\"\")";
_ema_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 615;BA.debugLine="ph1_img.Initialize(\"\")";
_ph1_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 616;BA.debugLine="ph2_img.Initialize(\"\")";
_ph2_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 617;BA.debugLine="user_image.Initialize(\"user_image\")";
_user_image.Initialize(mostCurrent.activityBA,"user_image");
 //BA.debugLineNum = 618;BA.debugLine="age_img.Initialize(\"\")";
_age_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 619;BA.debugLine="gender_img.Initialize(\"\")";
_gender_img.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 620;BA.debugLine="fn_pnl.Initialize(\"\")";
_fn_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 621;BA.debugLine="loc_pnl.Initialize(\"\")";
_loc_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 622;BA.debugLine="don_pnl.Initialize(\"\")";
_don_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 623;BA.debugLine="ema_pnl.Initialize(\"\")";
_ema_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 624;BA.debugLine="ph1_pnl.Initialize(\"\")";
_ph1_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 625;BA.debugLine="ph2_pnl.Initialize(\"\")";
_ph2_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 626;BA.debugLine="btn_pnl.Initialize(\"\")";
_btn_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 627;BA.debugLine="age_pnl.Initialize(\"\")";
_age_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 628;BA.debugLine="gender_pnl.Initialize(\"\")";
_gender_pnl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 629;BA.debugLine="Dim ok_vie_btn As Button";
_ok_vie_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 630;BA.debugLine="ok_vie_btn.Initialize(\"ok_vie_btn\")";
_ok_vie_btn.Initialize(mostCurrent.activityBA,"ok_vie_btn");
 //BA.debugLineNum = 631;BA.debugLine="tittle.Initialize(\"\")";
_tittle.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 632;BA.debugLine="fullname.Initialize(\"\")";
_fullname.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 633;BA.debugLine="location.Initialize(\"\")";
_location.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 634;BA.debugLine="donated.Initialize(\"\")";
_donated.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 635;BA.debugLine="email.Initialize(\"\")";
_email.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 636;BA.debugLine="phone1.Initialize(\"\")";
_phone1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 637;BA.debugLine="phone2.Initialize(\"\")";
_phone2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 638;BA.debugLine="age.Initialize(\"\")";
_age.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 639;BA.debugLine="gender.Initialize(\"\")";
_gender.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 640;BA.debugLine="view_panl.Initialize(\"\")";
_view_panl.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 641;BA.debugLine="view_for_image.Initialize(\"\")";
_view_for_image.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 642;BA.debugLine="view_for_btn.Initialize(\"\")";
_view_for_btn.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 643;BA.debugLine="fullname.Text = fullN_llist.Get(row_click)			'str";
_fullname.setText(_fulln_llist.Get((int)(Double.parseDouble(_row_click))));
 //BA.debugLineNum = 644;BA.debugLine="location.Text = \": \"&location_list.Get(row_click)";
_location.setText((Object)(": "+BA.ObjectToString(_location_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 645;BA.debugLine="donated.Text = \": \"&donated_list.Get(row_click)";
_donated.setText((Object)(": "+BA.ObjectToString(_donated_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 646;BA.debugLine="email.Text = \": \"&email_list.Get(row_click)";
_email.setText((Object)(": "+BA.ObjectToString(_email_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 647;BA.debugLine="phone1.Text = \": \"&phone1_list.Get(row_click)";
_phone1.setText((Object)(": "+BA.ObjectToString(_phone1_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 648;BA.debugLine="phone2.Text = \": \"&phone2_list.Get(row_click)";
_phone2.setText((Object)(": "+BA.ObjectToString(_phone2_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 649;BA.debugLine="age.Text = \": \"&age_list.Get(row_click)";
_age.setText((Object)(": "+BA.ObjectToString(_age_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 650;BA.debugLine="gender.Text = \": \"&gender_list.Get(row_click)";
_gender.setText((Object)(": "+BA.ObjectToString(_gender_list.Get((int)(Double.parseDouble(_row_click))))));
 //BA.debugLineNum = 651;BA.debugLine="location.Gravity = Gravity.CENTER_VERTICAL";
_location.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 652;BA.debugLine="donated.Gravity = Gravity.CENTER_VERTICAL";
_donated.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 653;BA.debugLine="email.Gravity = Gravity.CENTER_VERTICAL";
_email.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 654;BA.debugLine="phone1.Gravity = Gravity.CENTER_VERTICAL";
_phone1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 655;BA.debugLine="phone2.Gravity = Gravity.CENTER_VERTICAL";
_phone2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 656;BA.debugLine="age.Gravity = Gravity.CENTER_VERTICAL";
_age.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 657;BA.debugLine="gender.Gravity = Gravity.CENTER_VERTICAL";
_gender.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_VERTICAL);
 //BA.debugLineNum = 658;BA.debugLine="fullname.TextColor = Colors.Black";
_fullname.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 659;BA.debugLine="location.TextColor = Colors.Black";
_location.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 660;BA.debugLine="donated.TextColor = Colors.Black";
_donated.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 661;BA.debugLine="email.TextColor = Colors.Black";
_email.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 662;BA.debugLine="phone1.TextColor = Colors.Black";
_phone1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 663;BA.debugLine="phone2.TextColor = Colors.Black";
_phone2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 664;BA.debugLine="ok_vie_btn.TextColor = Colors.Black";
_ok_vie_btn.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 665;BA.debugLine="age.TextColor = Colors.Black";
_age.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 666;BA.debugLine="gender.TextColor = Colors.Black";
_gender.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 669;BA.debugLine="fullname.Gravity = Gravity.CENTER";
_fullname.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 670;BA.debugLine="ok_vie_btn.Text = \"OK\"";
_ok_vie_btn.setText((Object)("OK"));
 //BA.debugLineNum = 671;BA.debugLine="view_panl.SetBackgroundImage(LoadBitmap(File.DirA";
_view_panl.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 678;BA.debugLine="Dim loc_bitd,don_bitd,ema_bitd,ph1_bitd,ph2_bi";
_loc_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_don_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_ema_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_ph1_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_ph2_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_age_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
_gender_bitd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 679;BA.debugLine="Dim loc_bit,don_bit,ema_bit,ph1_bit,ph2_bit,ag";
_loc_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_don_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ema_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ph1_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_ph2_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_age_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
_gender_bit = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 680;BA.debugLine="loc_bit.Initialize(File.DirAssets,\"glyphicons";
_loc_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-21-home.png");
 //BA.debugLineNum = 681;BA.debugLine="don_bit.Initialize(File.DirAssets,\"glyphicons";
_don_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-152-new-window.png");
 //BA.debugLineNum = 682;BA.debugLine="ema_bit.Initialize(File.DirAssets,\"glyphicons";
_ema_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-social-40-e-mail.png");
 //BA.debugLineNum = 683;BA.debugLine="ph1_bit.Initialize(File.DirAssets,\"glyphicons";
_ph1_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt1.png");
 //BA.debugLineNum = 684;BA.debugLine="ph2_bit.Initialize(File.DirAssets,\"glyphicons";
_ph2_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-354-nameplate-alt2.png");
 //BA.debugLineNum = 685;BA.debugLine="age_bit.Initialize(File.DirAssets,\"glyphicons";
_age_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-577-uk-rat-r18.png");
 //BA.debugLineNum = 686;BA.debugLine="gender_bit.Initialize(File.DirAssets,\"glyphic";
_gender_bit.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"glyphicons-25-parents.png");
 //BA.debugLineNum = 687;BA.debugLine="loc_bitd.Initialize(loc_bit)";
_loc_bitd.Initialize((android.graphics.Bitmap)(_loc_bit.getObject()));
 //BA.debugLineNum = 688;BA.debugLine="don_bitd.Initialize(don_bit)";
_don_bitd.Initialize((android.graphics.Bitmap)(_don_bit.getObject()));
 //BA.debugLineNum = 689;BA.debugLine="ema_bitd.Initialize(ema_bit)";
_ema_bitd.Initialize((android.graphics.Bitmap)(_ema_bit.getObject()));
 //BA.debugLineNum = 690;BA.debugLine="ph1_bitd.Initialize(ph1_bit)";
_ph1_bitd.Initialize((android.graphics.Bitmap)(_ph1_bit.getObject()));
 //BA.debugLineNum = 691;BA.debugLine="ph2_bitd.Initialize(ph2_bit)";
_ph2_bitd.Initialize((android.graphics.Bitmap)(_ph2_bit.getObject()));
 //BA.debugLineNum = 692;BA.debugLine="age_bitd.Initialize(age_bit)";
_age_bitd.Initialize((android.graphics.Bitmap)(_age_bit.getObject()));
 //BA.debugLineNum = 693;BA.debugLine="gender_bitd.Initialize(gender_bit)";
_gender_bitd.Initialize((android.graphics.Bitmap)(_gender_bit.getObject()));
 //BA.debugLineNum = 694;BA.debugLine="loc_img.Background = loc_bitd";
_loc_img.setBackground((android.graphics.drawable.Drawable)(_loc_bitd.getObject()));
 //BA.debugLineNum = 695;BA.debugLine="don_img.Background = don_bitd";
_don_img.setBackground((android.graphics.drawable.Drawable)(_don_bitd.getObject()));
 //BA.debugLineNum = 696;BA.debugLine="ema_img.Background = ema_bitd";
_ema_img.setBackground((android.graphics.drawable.Drawable)(_ema_bitd.getObject()));
 //BA.debugLineNum = 697;BA.debugLine="ph1_img.Background = ph1_bitd";
_ph1_img.setBackground((android.graphics.drawable.Drawable)(_ph1_bitd.getObject()));
 //BA.debugLineNum = 698;BA.debugLine="ph2_img.Background = ph2_bitd";
_ph2_img.setBackground((android.graphics.drawable.Drawable)(_ph2_bitd.getObject()));
 //BA.debugLineNum = 699;BA.debugLine="age_img.Background = age_bitd";
_age_img.setBackground((android.graphics.drawable.Drawable)(_age_bitd.getObject()));
 //BA.debugLineNum = 700;BA.debugLine="gender_img.Background = gender_bitd";
_gender_img.setBackground((android.graphics.drawable.Drawable)(_gender_bitd.getObject()));
 //BA.debugLineNum = 701;BA.debugLine="Dim inp As InputStream";
_inp = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 702;BA.debugLine="Dim bmp As Bitmap";
_bmp = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 703;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 704;BA.debugLine="Dim bytes() As Byte";
_bytes = new byte[(int) (0)];
;
 //BA.debugLineNum = 705;BA.debugLine="bytes = su.DecodeBase64(image_list.Get(row_click)";
_bytes = _su.DecodeBase64(BA.ObjectToString(_image_list.Get((int)(Double.parseDouble(_row_click)))));
 //BA.debugLineNum = 706;BA.debugLine="inp.InitializeFromBytesArray(bytes,0,bytes.Length";
_inp.InitializeFromBytesArray(_bytes,(int) (0),_bytes.length);
 //BA.debugLineNum = 707;BA.debugLine="bmp.Initialize2(inp)";
_bmp.Initialize2((java.io.InputStream)(_inp.getObject()));
 //BA.debugLineNum = 709;BA.debugLine="Dim bd As BitmapDrawable";
_bd = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 710;BA.debugLine="bd.Initialize(bmp)";
_bd.Initialize((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 711;BA.debugLine="user_image.Background = bd";
_user_image.setBackground((android.graphics.drawable.Drawable)(_bd.getObject()));
 //BA.debugLineNum = 714;BA.debugLine="Dim fn_grad,don_grad,ema_grad,ph1_grad,ph2_grad";
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
 //BA.debugLineNum = 715;BA.debugLine="Dim colorG(2),btn_color(2),panl_btn(2) As Int";
_colorg = new int[(int) (2)];
;
_btn_color = new int[(int) (2)];
;
_panl_btn = new int[(int) (2)];
;
 //BA.debugLineNum = 716;BA.debugLine="colorG(0) = Colors.White";
_colorg[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 717;BA.debugLine="colorG(1) = Colors.Red";
_colorg[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 718;BA.debugLine="btn_color(0) = Colors.Red";
_btn_color[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 719;BA.debugLine="btn_color(1) = Colors.White";
_btn_color[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.White;
 //BA.debugLineNum = 720;BA.debugLine="panl_btn(0) = Colors.Gray";
_panl_btn[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.Gray;
 //BA.debugLineNum = 721;BA.debugLine="panl_btn(1) = Colors.Red";
_panl_btn[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.Red;
 //BA.debugLineNum = 722;BA.debugLine="fn_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_fn_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 723;BA.debugLine="don_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_don_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 724;BA.debugLine="ema_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_ema_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 725;BA.debugLine="ph1_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_ph1_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 726;BA.debugLine="ph2_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_ph2_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 727;BA.debugLine="loc_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_loc_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 728;BA.debugLine="age_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_age_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 729;BA.debugLine="gender_grad.Initialize(\"TOP_BOTTOM\",colorG)";
_gender_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_colorg);
 //BA.debugLineNum = 730;BA.debugLine="btn_grad.Initialize(\"TOP_BOTTOM\",panl_btn)";
_btn_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_panl_btn);
 //BA.debugLineNum = 731;BA.debugLine="ok_btn_grad.Initialize(\"TOP_BOTTOM\",btn_color)";
_ok_btn_grad.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"TOP_BOTTOM"),_btn_color);
 //BA.debugLineNum = 732;BA.debugLine="fn_grad.CornerRadius = 10dip";
_fn_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))));
 //BA.debugLineNum = 733;BA.debugLine="ok_btn_grad.CornerRadius = 50dip";
_ok_btn_grad.setCornerRadius((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 734;BA.debugLine="fn_pnl.Background = fn_grad		'fn_pnl.Color = Col";
_fn_pnl.setBackground((android.graphics.drawable.Drawable)(_fn_grad.getObject()));
 //BA.debugLineNum = 735;BA.debugLine="don_pnl.Background = don_grad		'don_pnl.Color =";
_don_pnl.setBackground((android.graphics.drawable.Drawable)(_don_grad.getObject()));
 //BA.debugLineNum = 736;BA.debugLine="ema_pnl.Background = ema_grad		'ema_pnl.Color =";
_ema_pnl.setBackground((android.graphics.drawable.Drawable)(_ema_grad.getObject()));
 //BA.debugLineNum = 737;BA.debugLine="ph1_pnl.Background = ph1_grad		'ph1_pnl.Color =";
_ph1_pnl.setBackground((android.graphics.drawable.Drawable)(_ph1_grad.getObject()));
 //BA.debugLineNum = 738;BA.debugLine="ph2_pnl.Background = ph2_grad		'ph2_pnl.Color =";
_ph2_pnl.setBackground((android.graphics.drawable.Drawable)(_ph2_grad.getObject()));
 //BA.debugLineNum = 739;BA.debugLine="loc_pnl.Background = loc_grad		'loc_pnl.Color =";
_loc_pnl.setBackground((android.graphics.drawable.Drawable)(_loc_grad.getObject()));
 //BA.debugLineNum = 740;BA.debugLine="age_pnl.Background = loc_grad";
_age_pnl.setBackground((android.graphics.drawable.Drawable)(_loc_grad.getObject()));
 //BA.debugLineNum = 741;BA.debugLine="gender_pnl.Background = loc_grad";
_gender_pnl.setBackground((android.graphics.drawable.Drawable)(_loc_grad.getObject()));
 //BA.debugLineNum = 742;BA.debugLine="btn_pnl.Background = btn_grad";
_btn_pnl.setBackground((android.graphics.drawable.Drawable)(_btn_grad.getObject()));
 //BA.debugLineNum = 743;BA.debugLine="ok_vie_btn.Background = ok_btn_grad";
_ok_vie_btn.setBackground((android.graphics.drawable.Drawable)(_ok_btn_grad.getObject()));
 //BA.debugLineNum = 746;BA.debugLine="view_for_image.AddView(fn_pnl,0,0,74%x,30%y) ' fu";
_view_for_image.AddView((android.view.View)(_fn_pnl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 748;BA.debugLine="fn_pnl.AddView(user_image,((fn_pnl.Width/2)/2)-2";
_fn_pnl.AddView((android.view.View)(_user_image.getObject()),(int) (((_fn_pnl.getWidth()/(double)2)/(double)2)-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1.2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (39),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (17),mostCurrent.activityBA));
 //BA.debugLineNum = 749;BA.debugLine="fn_pnl.AddView(fullname,0,user_image.Top + user_";
_fn_pnl.AddView((android.view.View)(_fullname.getObject()),(int) (0),(int) (_user_image.getTop()+_user_image.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 751;BA.debugLine="fullname.TextSize = 25";
_fullname.setTextSize((float) (25));
 //BA.debugLineNum = 753;BA.debugLine="view_panl.AddView(age_pnl,1%x,0,72%x,8%y)";
_view_panl.AddView((android.view.View)(_age_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 754;BA.debugLine="age_pnl.AddView(age_img,4%x,1%y,6%x,6%y) ''  ima";
_age_pnl.AddView((android.view.View)(_age_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 755;BA.debugLine="age_pnl.AddView(age,age_img.Left + age_img.Width";
_age_pnl.AddView((android.view.View)(_age.getObject()),(int) (_age_img.getLeft()+_age_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 757;BA.debugLine="view_panl.AddView(gender_pnl,1%x,age_pnl.Top + ag";
_view_panl.AddView((android.view.View)(_gender_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_age_pnl.getTop()+_age_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 758;BA.debugLine="gender_pnl.AddView(gender_img,4%x,1%y,6%x,6%y) '";
_gender_pnl.AddView((android.view.View)(_gender_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 759;BA.debugLine="gender_pnl.AddView(gender,gender_img.Left + gend";
_gender_pnl.AddView((android.view.View)(_gender.getObject()),(int) (_gender_img.getLeft()+_gender_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 761;BA.debugLine="view_panl.AddView(don_pnl,1%x,gender_pnl.Top + ge";
_view_panl.AddView((android.view.View)(_don_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_gender_pnl.getTop()+_gender_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 762;BA.debugLine="don_pnl.AddView(don_img,4%x,1%y,6%x,6%y) '' imag";
_don_pnl.AddView((android.view.View)(_don_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 763;BA.debugLine="don_pnl.AddView(donated,don_img.Left + don_img.W";
_don_pnl.AddView((android.view.View)(_donated.getObject()),(int) (_don_img.getLeft()+_don_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 765;BA.debugLine="view_panl.AddView(ema_pnl,1%x,don_pnl.Top + don_p";
_view_panl.AddView((android.view.View)(_ema_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_don_pnl.getTop()+_don_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 766;BA.debugLine="ema_pnl.AddView(ema_img,4%x,1%y,6%x,6%y) '' imag";
_ema_pnl.AddView((android.view.View)(_ema_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 767;BA.debugLine="ema_pnl.AddView(email,ema_img.Left + ema_img.Wid";
_ema_pnl.AddView((android.view.View)(_email.getObject()),(int) (_ema_img.getLeft()+_ema_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 769;BA.debugLine="view_panl.AddView(ph1_pnl,1%x,ema_pnl.Top + ema_p";
_view_panl.AddView((android.view.View)(_ph1_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_ema_pnl.getTop()+_ema_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 770;BA.debugLine="ph1_pnl.AddView(ph1_img,4%x,1%y,6%x,6%y) '' imag";
_ph1_pnl.AddView((android.view.View)(_ph1_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 771;BA.debugLine="ph1_pnl.AddView(phone1,ph1_img.Left + ph1_img.Wi";
_ph1_pnl.AddView((android.view.View)(_phone1.getObject()),(int) (_ph1_img.getLeft()+_ph1_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 773;BA.debugLine="view_panl.AddView(ph2_pnl,1%x,ph1_pnl.Top + ph1_p";
_view_panl.AddView((android.view.View)(_ph2_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_ph1_pnl.getTop()+_ph1_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 774;BA.debugLine="ph2_pnl.AddView(ph2_img,4%x,1%y,6%x,6%y) '' imag";
_ph2_pnl.AddView((android.view.View)(_ph2_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 775;BA.debugLine="ph2_pnl.AddView(phone2,ph2_img.Left + ph2_img.Wi";
_ph2_pnl.AddView((android.view.View)(_phone2.getObject()),(int) (_ph2_img.getLeft()+_ph2_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 777;BA.debugLine="view_panl.AddView(loc_pnl,1%x,ph2_pnl.Top + ph2_p";
_view_panl.AddView((android.view.View)(_loc_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_ph2_pnl.getTop()+_ph2_pnl.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (72),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 778;BA.debugLine="loc_pnl.AddView(loc_img,4%x,1%y,6%x,6%y) '' imag";
_loc_pnl.AddView((android.view.View)(_loc_img.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 779;BA.debugLine="loc_pnl.AddView(location,loc_img.Left + loc_img.";
_loc_pnl.AddView((android.view.View)(_location.getObject()),(int) (_loc_img.getLeft()+_loc_img.getWidth()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (6),mostCurrent.activityBA));
 //BA.debugLineNum = 782;BA.debugLine="btn_pnl.AddView(ok_vie_btn,((74%x/2)/2),1%y,37%x,";
_btn_pnl.AddView((android.view.View)(_ok_vie_btn.getObject()),(int) (((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (37),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 784;BA.debugLine="view_data_info_person.Color = Colors.ARGB(128,12";
mostCurrent._view_data_info_person.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (128),(int) (128),(int) (128),(int) (.70)));
 //BA.debugLineNum = 787;BA.debugLine="scroll_view_info.ScrollbarsVisibility(False,Fals";
mostCurrent._scroll_view_info.ScrollbarsVisibility(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 788;BA.debugLine="scroll_view_info.SetBackgroundImage(LoadBitmap(F";
mostCurrent._scroll_view_info.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 789;BA.debugLine="scroll_view_info.Panel.AddView(view_panl,0,0,74%x";
mostCurrent._scroll_view_info.getPanel().AddView((android.view.View)(_view_panl.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (57),mostCurrent.activityBA));
 //BA.debugLineNum = 790;BA.debugLine="view_data_info_person.AddView(view_for_image,13%x";
mostCurrent._view_data_info_person.AddView((android.view.View)(_view_for_image.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (((((mostCurrent._activity.getHeight()/(double)2)/(double)2)/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (31),mostCurrent.activityBA));
 //BA.debugLineNum = 791;BA.debugLine="view_data_info_person.AddView(scroll_view_info,13";
mostCurrent._view_data_info_person.AddView((android.view.View)(mostCurrent._scroll_view_info.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (_view_for_image.getTop()+_view_for_image.getHeight()-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (.7),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 792;BA.debugLine="view_data_info_person.AddView(btn_pnl,13%x,scroll";
mostCurrent._view_data_info_person.AddView((android.view.View)(_btn_pnl.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (13),mostCurrent.activityBA),(int) (mostCurrent._scroll_view_info.getTop()+mostCurrent._scroll_view_info.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (74),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 794;BA.debugLine="Activity.AddView(view_data_info_person,0,0,100%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._view_data_info_person.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 795;BA.debugLine="End Sub";
return "";
}
public static String  _view_data_info_person_click() throws Exception{
 //BA.debugLineNum = 861;BA.debugLine="Sub view_data_info_person_click";
 //BA.debugLineNum = 863;BA.debugLine="End Sub";
return "";
}
public static String  _view_info_pnl_click() throws Exception{
 //BA.debugLineNum = 593;BA.debugLine="Sub view_info_pnl_click";
 //BA.debugLineNum = 595;BA.debugLine="End Sub";
return "";
}
}
