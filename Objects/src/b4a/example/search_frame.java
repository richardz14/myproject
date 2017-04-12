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
public static int _is_complete = 0;
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
public b4a.example.httpjob _query_marker = null;
public b4a.example.main _main = null;
public b4a.example.login_form _login_form = null;
public b4a.example.create_account _create_account = null;
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
 //BA.debugLineNum = 56;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 58;BA.debugLine="Activity.LoadLayout(\"search_frame\")";
mostCurrent._activity.LoadLayout("search_frame",mostCurrent.activityBA);
 //BA.debugLineNum = 59;BA.debugLine="data_query_id.Initialize(\"data_query_id_get\",Me)";
mostCurrent._data_query_id._initialize(processBA,"data_query_id_get",search_frame.getObject());
 //BA.debugLineNum = 60;BA.debugLine="data_query_fullN.Initialize(\"data_query_fullN_get";
mostCurrent._data_query_fulln._initialize(processBA,"data_query_fullN_get",search_frame.getObject());
 //BA.debugLineNum = 61;BA.debugLine="data_query_location.Initialize(\"data_query_locati";
mostCurrent._data_query_location._initialize(processBA,"data_query_location_get",search_frame.getObject());
 //BA.debugLineNum = 62;BA.debugLine="query_lat.Initialize(\"data_query_lat_get\",Me)";
mostCurrent._query_lat._initialize(processBA,"data_query_lat_get",search_frame.getObject());
 //BA.debugLineNum = 63;BA.debugLine="query_lng.Initialize(\"data_query_lng_get\",Me)";
mostCurrent._query_lng._initialize(processBA,"data_query_lng_get",search_frame.getObject());
 //BA.debugLineNum = 64;BA.debugLine="data_query_donated.Initialize(\"data_query_donated";
mostCurrent._data_query_donated._initialize(processBA,"data_query_donated_get",search_frame.getObject());
 //BA.debugLineNum = 65;BA.debugLine="data_query_email.Initialize(\"data_query_email_get";
mostCurrent._data_query_email._initialize(processBA,"data_query_email_get",search_frame.getObject());
 //BA.debugLineNum = 66;BA.debugLine="data_query_nickname.Initialize(\"data_query_nickna";
mostCurrent._data_query_nickname._initialize(processBA,"data_query_nickname_get",search_frame.getObject());
 //BA.debugLineNum = 67;BA.debugLine="data_query_phone1.Initialize(\"data_query_phone1_g";
mostCurrent._data_query_phone1._initialize(processBA,"data_query_phone1_get",search_frame.getObject());
 //BA.debugLineNum = 68;BA.debugLine="data_query_phone2.Initialize(\"data_query_phone2_g";
mostCurrent._data_query_phone2._initialize(processBA,"data_query_phone2_get",search_frame.getObject());
 //BA.debugLineNum = 70;BA.debugLine="query_marker.Initialize(\"query_marker_get\",Me)";
mostCurrent._query_marker._initialize(processBA,"query_marker_get",search_frame.getObject());
 //BA.debugLineNum = 71;BA.debugLine="map_extras.addJavascriptInterface(map_webview,\"B4";
mostCurrent._map_extras.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._map_webview.getObject()),"B4A");
 //BA.debugLineNum = 72;BA.debugLine="is_initialize";
_is_initialize();
 //BA.debugLineNum = 73;BA.debugLine="all_layout_load";
_all_layout_load();
 //BA.debugLineNum = 74;BA.debugLine="load_list";
_load_list();
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _all_layout_load() throws Exception{
 //BA.debugLineNum = 77;BA.debugLine="Sub all_layout_load";
 //BA.debugLineNum = 78;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 79;BA.debugLine="toolkit_pnl.Color = Colors.Transparent";
mostCurrent._toolkit_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 80;BA.debugLine="list_panel.Color = Colors.Transparent";
mostCurrent._list_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 81;BA.debugLine="search_btn.SetBackgroundImage(LoadBitmap(File.Dir";
mostCurrent._search_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"esearch.png").getObject()));
 //BA.debugLineNum = 82;BA.debugLine="list_btn.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._list_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"view all.png").getObject()));
 //BA.debugLineNum = 84;BA.debugLine="toolkit_pnl.Width = Activity.Width";
mostCurrent._toolkit_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 85;BA.debugLine="list_panel.Width = Activity.Width";
mostCurrent._list_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 86;BA.debugLine="map_webview.Width = Activity.Width";
mostCurrent._map_webview.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 88;BA.debugLine="list_btn.Width = 50%x";
mostCurrent._list_btn.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 89;BA.debugLine="search_lbl.Width = 14%x";
mostCurrent._search_lbl.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 90;BA.debugLine="search_btn.Width = 14%x";
mostCurrent._search_btn.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 91;BA.debugLine="search_spiner.Width = ((toolkit_pnl.Width - sea";
mostCurrent._search_spiner.setWidth((int) (((mostCurrent._toolkit_pnl.getWidth()-mostCurrent._search_btn.getWidth())-mostCurrent._search_lbl.getWidth())-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA)));
 //BA.debugLineNum = 93;BA.debugLine="toolkit_pnl.Height = 14%y";
mostCurrent._toolkit_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 94;BA.debugLine="list_panel.Height = 11%y";
mostCurrent._list_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (11),mostCurrent.activityBA));
 //BA.debugLineNum = 95;BA.debugLine="map_webview.Height =((Activity.Height - toolkit";
mostCurrent._map_webview.setHeight((int) (((mostCurrent._activity.getHeight()-mostCurrent._toolkit_pnl.getHeight())-mostCurrent._list_panel.getHeight())));
 //BA.debugLineNum = 97;BA.debugLine="list_btn.Height = 9%y";
mostCurrent._list_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 98;BA.debugLine="search_lbl.Height = 10%y";
mostCurrent._search_lbl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 99;BA.debugLine="search_btn.Height = 10%y";
mostCurrent._search_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 100;BA.debugLine="search_spiner.Height = 10%y";
mostCurrent._search_spiner.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 102;BA.debugLine="toolkit_pnl.Left = 0";
mostCurrent._toolkit_pnl.setLeft((int) (0));
 //BA.debugLineNum = 103;BA.debugLine="list_panel.Left = 0";
mostCurrent._list_panel.setLeft((int) (0));
 //BA.debugLineNum = 104;BA.debugLine="map_webview.Left = 0";
mostCurrent._map_webview.setLeft((int) (0));
 //BA.debugLineNum = 106;BA.debugLine="list_btn.Left = ((list_panel.Width/2)/2)";
mostCurrent._list_btn.setLeft((int) (((mostCurrent._list_panel.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 107;BA.debugLine="search_lbl.Left = ((toolkit_pnl.Left + 3%x)+2%x";
mostCurrent._search_lbl.setLeft((int) (((mostCurrent._toolkit_pnl.getLeft()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (3),mostCurrent.activityBA))+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA))));
 //BA.debugLineNum = 108;BA.debugLine="search_spiner.Left = (search_lbl.Left + search_";
mostCurrent._search_spiner.setLeft((int) ((mostCurrent._search_lbl.getLeft()+mostCurrent._search_lbl.getWidth())));
 //BA.debugLineNum = 109;BA.debugLine="search_btn.Left = (search_spiner.Left + searc";
mostCurrent._search_btn.setLeft((int) ((mostCurrent._search_spiner.getLeft()+mostCurrent._search_spiner.getWidth())));
 //BA.debugLineNum = 111;BA.debugLine="toolkit_pnl.Top = 0";
mostCurrent._toolkit_pnl.setTop((int) (0));
 //BA.debugLineNum = 112;BA.debugLine="map_webview.Top = (toolkit_pnl.Top + toolkit_pnl.";
mostCurrent._map_webview.setTop((int) ((mostCurrent._toolkit_pnl.getTop()+mostCurrent._toolkit_pnl.getHeight())));
 //BA.debugLineNum = 113;BA.debugLine="list_panel.Top = (map_webview.Top + map_webview.";
mostCurrent._list_panel.setTop((int) ((mostCurrent._map_webview.getTop()+mostCurrent._map_webview.getHeight())));
 //BA.debugLineNum = 115;BA.debugLine="list_btn.Top = 1%y'(list_panel.Top + 1%Y)";
mostCurrent._list_btn.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA));
 //BA.debugLineNum = 116;BA.debugLine="search_lbl.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_lbl.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 117;BA.debugLine="search_btn.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_btn.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 118;BA.debugLine="search_spiner.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_spiner.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return "";
}
public static String  _create_map() throws Exception{
String _htmlstring1 = "";
String _htmlstring2 = "";
String _htmlstring3 = "";
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _location = null;
int _i = 0;
 //BA.debugLineNum = 173;BA.debugLine="Sub create_map";
 //BA.debugLineNum = 174;BA.debugLine="ProgressDialogShow2(\"Creating the map, please wai";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Creating the map, please wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 175;BA.debugLine="Dim htmlString1,htmlString2,htmlString3 As Strin";
_htmlstring1 = "";
_htmlstring2 = "";
_htmlstring3 = "";
 //BA.debugLineNum = 176;BA.debugLine="htmlString1 = File.GetText(File.DirAssets, \"locat";
_htmlstring1 = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"location_top.txt");
 //BA.debugLineNum = 177;BA.debugLine="htmlString1 = htmlString1 & \" var markers=[]; var";
_htmlstring1 = _htmlstring1+" var markers=[]; var contents = []; var infowindows = []; ";
 //BA.debugLineNum = 178;BA.debugLine="Dim location As TextWriter";
_location = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 179;BA.debugLine="location.Initialize(File.OpenOutput(File.";
_location.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"all_marker_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 180;BA.debugLine="location.WriteLine(htmlString1)";
_location.WriteLine(_htmlstring1);
 //BA.debugLineNum = 182;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step8 = 1;
final int limit8 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step8 > 0 && _i <= limit8) || (step8 < 0 && _i >= limit8); _i = ((int)(0 + _i + step8)) ) {
 //BA.debugLineNum = 183;BA.debugLine="htmlString2 = \"markers[\"&i&\"] = new google.maps.M";
_htmlstring2 = "markers["+BA.NumberToString(_i)+"] = new google.maps.Marker({position: new google.maps.LatLng("+BA.ObjectToString(_lat_list.Get(_i))+" , "+BA.ObjectToString(_lng_list.Get(_i))+"), map: map, title: '"+BA.ObjectToString(_fulln_llist.Get(_i))+"', icon: 'http://www.google.com/mapfiles/dd-end.png', clickable: true }); markers["+BA.NumberToString(_i)+"].index = "+BA.NumberToString(_i)+"; contents["+BA.NumberToString(_i)+"] = '<div class=\"well\"><b><h3><center>"+BA.ObjectToString(_fulln_llist.Get(_i))+"</center></h3></b><h4>Blood Type: <b>"+mostCurrent._spin_item_click+"</b></h4><h4>Email Address: <b>"+BA.ObjectToString(_email_list.Get(_i))+"</b></h4><h4>Location: <b>"+BA.ObjectToString(_location_list.Get(_i))+"</b></h4><h4>Nickname: <b>"+BA.ObjectToString(_nickname_list.Get(_i))+"</b></h4><h4>Phone Number 1: <b>"+BA.ObjectToString(_phone1_list.Get(_i))+"</b></h4><h4>Phone Number 2: <b>"+BA.ObjectToString(_phone2_list.Get(_i))+"</b></h4><h4>Donated: <b>"+BA.ObjectToString(_donated_list.Get(_i))+"</b></h4></div>'; infowindows["+BA.NumberToString(_i)+"] = new google.maps.InfoWindow({ content: contents["+BA.NumberToString(_i)+"], maxWidth: 500 }); google.maps.event.addListener(markers["+BA.NumberToString(_i)+"], 'click', function() { infowindows[this.index].open(map,markers[this.index]); map.panTo(markers[this.index].getPosition()); }); ";
 //BA.debugLineNum = 184;BA.debugLine="location.WriteLine(htmlString2)";
_location.WriteLine(_htmlstring2);
 }
};
 //BA.debugLineNum = 220;BA.debugLine="htmlString3 = File.GetText(File.DirAssets, \"locat";
_htmlstring3 = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"location_buttom.txt");
 //BA.debugLineNum = 221;BA.debugLine="location.WriteLine(htmlString3)";
_location.WriteLine(_htmlstring3);
 //BA.debugLineNum = 222;BA.debugLine="location.Close";
_location.Close();
 //BA.debugLineNum = 228;BA.debugLine="map_webview.LoadHtml(File.ReadString(File.DirInte";
mostCurrent._map_webview.LoadHtml(anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"all_marker_location.txt"));
 //BA.debugLineNum = 230;BA.debugLine="Log(File.ReadString(File.DirInternalCache,\"all_ma";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"all_marker_location.txt"));
 //BA.debugLineNum = 232;BA.debugLine="End Sub";
return "";
}
public static String  _data_list_click() throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _send = null;
int _row = 0;
 //BA.debugLineNum = 427;BA.debugLine="Sub data_list_Click";
 //BA.debugLineNum = 428;BA.debugLine="Dim Send As View";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 429;BA.debugLine="Dim row As Int";
_row = 0;
 //BA.debugLineNum = 430;BA.debugLine="Send=Sender";
_send.setObject((android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 431;BA.debugLine="row=Floor(Send.Tag/10) '20";
_row = (int) (anywheresoftware.b4a.keywords.Common.Floor((double)(BA.ObjectToNumber(_send.getTag()))/(double)10));
 //BA.debugLineNum = 432;BA.debugLine="item=row";
_item = _row;
 //BA.debugLineNum = 433;BA.debugLine="Log(row)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_row));
 //BA.debugLineNum = 434;BA.debugLine="Log(CRLF&\"Item \"&item)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.CRLF+"Item "+BA.NumberToString(_item));
 //BA.debugLineNum = 436;BA.debugLine="End Sub";
return "";
}
public static String  _dialog_all_panel_click() throws Exception{
 //BA.debugLineNum = 663;BA.debugLine="Sub dialog_all_panel_click";
 //BA.debugLineNum = 665;BA.debugLine="End Sub";
return "";
}
public static String  _dialog_panel_can_btn_click() throws Exception{
 //BA.debugLineNum = 660;BA.debugLine="Sub dialog_panel_can_btn_click";
 //BA.debugLineNum = 661;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 //BA.debugLineNum = 662;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 28;BA.debugLine="Public spin_item_click As String :";
mostCurrent._spin_item_click = "";
 //BA.debugLineNum = 29;BA.debugLine="Private toolkit_pnl As Panel";
mostCurrent._toolkit_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private search_lbl As Label";
mostCurrent._search_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private search_spiner As Spinner";
mostCurrent._search_spiner = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private search_btn As Button";
mostCurrent._search_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private map_webview As WebView";
mostCurrent._map_webview = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private list_btn As Button";
mostCurrent._list_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private list_panel As Panel";
mostCurrent._list_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private map_extras As WebViewExtras";
mostCurrent._map_extras = new uk.co.martinpearman.b4a.webviewextras.WebViewExtras();
 //BA.debugLineNum = 38;BA.debugLine="Dim scrolllista As ScrollView";
mostCurrent._scrolllista = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim item As Int";
_item = 0;
 //BA.debugLineNum = 40;BA.debugLine="Dim dialog_panel As Panel";
mostCurrent._dialog_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim dialog_all_panel As Panel";
mostCurrent._dialog_all_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private data_query_id As HttpJob";
mostCurrent._data_query_id = new b4a.example.httpjob();
 //BA.debugLineNum = 44;BA.debugLine="Private data_query_fullN As HttpJob";
mostCurrent._data_query_fulln = new b4a.example.httpjob();
 //BA.debugLineNum = 45;BA.debugLine="Private data_query_location As HttpJob";
mostCurrent._data_query_location = new b4a.example.httpjob();
 //BA.debugLineNum = 46;BA.debugLine="Private query_lat As HttpJob";
mostCurrent._query_lat = new b4a.example.httpjob();
 //BA.debugLineNum = 47;BA.debugLine="Private query_lng As HttpJob";
mostCurrent._query_lng = new b4a.example.httpjob();
 //BA.debugLineNum = 48;BA.debugLine="Private data_query_donated As HttpJob";
mostCurrent._data_query_donated = new b4a.example.httpjob();
 //BA.debugLineNum = 49;BA.debugLine="Private data_query_email As HttpJob";
mostCurrent._data_query_email = new b4a.example.httpjob();
 //BA.debugLineNum = 50;BA.debugLine="Private data_query_nickname As HttpJob";
mostCurrent._data_query_nickname = new b4a.example.httpjob();
 //BA.debugLineNum = 51;BA.debugLine="Private data_query_phone1 As HttpJob";
mostCurrent._data_query_phone1 = new b4a.example.httpjob();
 //BA.debugLineNum = 52;BA.debugLine="Private data_query_phone2 As HttpJob";
mostCurrent._data_query_phone2 = new b4a.example.httpjob();
 //BA.debugLineNum = 53;BA.debugLine="Private query_marker As HttpJob";
mostCurrent._query_marker = new b4a.example.httpjob();
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _is_initialize() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriters = null;
 //BA.debugLineNum = 236;BA.debugLine="Sub is_initialize";
 //BA.debugLineNum = 237;BA.debugLine="Dim TextWriters As TextWriter";
_textwriters = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 238;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 239;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 240;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 241;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 242;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 243;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_donated.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 244;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_email.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 245;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_nickname.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 246;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone1.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 247;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone2.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 248;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 249;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 250;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 251;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"data_query_id_get","data_query_fullN_get","data_query_location_get","data_query_lat_get","data_query_lng_get","data_query_donated_get","data_query_email_get","data_query_nickname_get","data_query_phone1_get","data_query_phone2_get")) {
case 0: {
 //BA.debugLineNum = 253;BA.debugLine="Dim TextWriter_id As TextWriter";
_textwriter_id = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 254;BA.debugLine="TextWriter_id.Initialize(File.OpenOutput(";
_textwriter_id.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 255;BA.debugLine="TextWriter_id.WriteLine(job.GetString.Tr";
_textwriter_id.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 256;BA.debugLine="TextWriter_id.Close";
_textwriter_id.Close();
 break; }
case 1: {
 //BA.debugLineNum = 259;BA.debugLine="Dim TextWriter_full As TextWriter";
_textwriter_full = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 260;BA.debugLine="TextWriter_full.Initialize(File.OpenOutpu";
_textwriter_full.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 261;BA.debugLine="TextWriter_full.WriteLine(job.GetString.";
_textwriter_full.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 262;BA.debugLine="TextWriter_full.Close";
_textwriter_full.Close();
 break; }
case 2: {
 //BA.debugLineNum = 265;BA.debugLine="Dim TextWriter_location As TextWriter";
_textwriter_location = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 266;BA.debugLine="TextWriter_location.Initialize(File.OpenO";
_textwriter_location.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 267;BA.debugLine="TextWriter_location.WriteLine(job.GetStr";
_textwriter_location.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 268;BA.debugLine="TextWriter_location.Close";
_textwriter_location.Close();
 break; }
case 3: {
 //BA.debugLineNum = 271;BA.debugLine="Dim TextWriter_lat As TextWriter";
_textwriter_lat = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 272;BA.debugLine="TextWriter_lat.Initialize(File.OpenOutput";
_textwriter_lat.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 273;BA.debugLine="TextWriter_lat.WriteLine(job.GetString.T";
_textwriter_lat.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 275;BA.debugLine="TextWriter_lat.Close";
_textwriter_lat.Close();
 break; }
case 4: {
 //BA.debugLineNum = 277;BA.debugLine="Dim TextWriter_lng As TextWriter";
_textwriter_lng = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 278;BA.debugLine="TextWriter_lng.Initialize(File.OpenOutput";
_textwriter_lng.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 279;BA.debugLine="TextWriter_lng.WriteLine(job.GetString.T";
_textwriter_lng.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 281;BA.debugLine="TextWriter_lng.Close";
_textwriter_lng.Close();
 break; }
case 5: {
 //BA.debugLineNum = 283;BA.debugLine="Dim TextWriter_donate As TextWriter";
_textwriter_donate = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 284;BA.debugLine="TextWriter_donate.Initialize(File.OpenOut";
_textwriter_donate.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_donated.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 285;BA.debugLine="TextWriter_donate.WriteLine(job.GetStrin";
_textwriter_donate.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 286;BA.debugLine="TextWriter_donate.Close";
_textwriter_donate.Close();
 break; }
case 6: {
 //BA.debugLineNum = 289;BA.debugLine="Dim TextWriter_email As TextWriter";
_textwriter_email = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 290;BA.debugLine="TextWriter_email.Initialize(File.OpenOutp";
_textwriter_email.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_email.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 291;BA.debugLine="TextWriter_email.WriteLine(job.GetString";
_textwriter_email.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 292;BA.debugLine="TextWriter_email.Close";
_textwriter_email.Close();
 break; }
case 7: {
 //BA.debugLineNum = 295;BA.debugLine="Dim TextWriter_nickname As TextWriter";
_textwriter_nickname = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 296;BA.debugLine="TextWriter_nickname.Initialize(File.OpenO";
_textwriter_nickname.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_nickname.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 297;BA.debugLine="TextWriter_nickname.WriteLine(job.GetStr";
_textwriter_nickname.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 298;BA.debugLine="TextWriter_nickname.Close";
_textwriter_nickname.Close();
 break; }
case 8: {
 //BA.debugLineNum = 301;BA.debugLine="Dim TextWriter_phone1 As TextWriter";
_textwriter_phone1 = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 302;BA.debugLine="TextWriter_phone1.Initialize(File.OpenOut";
_textwriter_phone1.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone1.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 303;BA.debugLine="TextWriter_phone1.WriteLine(job.GetStrin";
_textwriter_phone1.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 304;BA.debugLine="TextWriter_phone1.Close";
_textwriter_phone1.Close();
 break; }
case 9: {
 //BA.debugLineNum = 307;BA.debugLine="Dim TextWriter_phone2 As TextWriter";
_textwriter_phone2 = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 308;BA.debugLine="TextWriter_phone2.Initialize(File.OpenOut";
_textwriter_phone2.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone2.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 309;BA.debugLine="TextWriter_phone2.WriteLine(job.GetStrin";
_textwriter_phone2.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 310;BA.debugLine="TextWriter_phone2.Close";
_textwriter_phone2.Close();
 break; }
}
;
 //BA.debugLineNum = 313;BA.debugLine="If is_complete == 9 Then";
if (_is_complete==9) { 
 //BA.debugLineNum = 314;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 315;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 316;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 317;BA.debugLine="create_map";
_create_map();
 };
 //BA.debugLineNum = 319;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 321;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 322;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 323;BA.debugLine="Msgbox(\"Error: Error connecting to server,please";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Error connecting to server,please try again.!","C O N F I R M A T I O N",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 328;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 340;BA.debugLine="Sub list_btn_BACKUP_Click";
 //BA.debugLineNum = 341;BA.debugLine="ProgressDialogShow2(\"Loading data, Please Wait...";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Loading data, Please Wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 342;BA.debugLine="If scrolllista.IsInitialized == True Then";
if (mostCurrent._scrolllista.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 343;BA.debugLine="scrolllista.RemoveView";
mostCurrent._scrolllista.RemoveView();
 //BA.debugLineNum = 344;BA.debugLine="dialog_panel.RemoveView";
mostCurrent._dialog_panel.RemoveView();
 };
 //BA.debugLineNum = 346;BA.debugLine="scrolllista.Initialize(500)";
mostCurrent._scrolllista.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 347;BA.debugLine="dialog_panel.Initialize(\"dialog_panel\")";
mostCurrent._dialog_panel.Initialize(mostCurrent.activityBA,"dialog_panel");
 //BA.debugLineNum = 348;BA.debugLine="Dim cd As CustomDialog2";
_cd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2();
 //BA.debugLineNum = 349;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 350;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 351;BA.debugLine="Dim bgnd As ColorDrawable";
_bgnd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 352;BA.debugLine="bgnd.Initialize(Colors.Cyan, 5dip)";
_bgnd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Cyan,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 353;BA.debugLine="pnl.Background = bgnd";
_pnl.setBackground((android.graphics.drawable.Drawable)(_bgnd.getObject()));
 //BA.debugLineNum = 357;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 368;BA.debugLine="Dim Bitmap1 As Bitmap";
_bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 369;BA.debugLine="Dim Panel0 As Panel";
_panel0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 370;BA.debugLine="Dim PanelTop, PanelHeight  As Int";
_paneltop = 0;
_panelheight = 0;
 //BA.debugLineNum = 375;BA.debugLine="Bitmap1.Initialize(File.DirAssets,\"banner1.png\")";
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner1.png");
 //BA.debugLineNum = 376;BA.debugLine="PanelTop=1%y";
_paneltop = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA);
 //BA.debugLineNum = 377;BA.debugLine="Panel0=scrolllista.Panel";
_panel0 = mostCurrent._scrolllista.getPanel();
 //BA.debugLineNum = 378;BA.debugLine="Panel0.Color=Colors.argb(0,0,0,0)  'sets the invi";
_panel0.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 380;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step22 = 1;
final int limit22 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step22 > 0 && _i <= limit22) || (step22 < 0 && _i >= limit22); _i = ((int)(0 + _i + step22)) ) {
 //BA.debugLineNum = 382;BA.debugLine="If i>0 And i<3 Then Bitmap1.Initialize(File.DirA";
if (_i>0 && _i<3) { 
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner.png");};
 //BA.debugLineNum = 383;BA.debugLine="Dim ImageView1 As ImageView";
_imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 384;BA.debugLine="ImageView1.Initialize(\"data_list\")";
_imageview1.Initialize(mostCurrent.activityBA,"data_list");
 //BA.debugLineNum = 385;BA.debugLine="PanelHeight=12%y";
_panelheight = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA);
 //BA.debugLineNum = 387;BA.debugLine="Panel0.AddView(ImageView1,1%x,PanelTop,71%x,Pane";
_panel0.AddView((android.view.View)(_imageview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),_paneltop,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 388;BA.debugLine="ImageView1.Tag=i&\"1\"";
_imageview1.setTag((Object)(BA.NumberToString(_i)+"1"));
 //BA.debugLineNum = 389;BA.debugLine="ImageView1.Bitmap=Bitmap1";
_imageview1.setBitmap((android.graphics.Bitmap)(_bitmap1.getObject()));
 //BA.debugLineNum = 390;BA.debugLine="ImageView1.Gravity=Gravity.fill";
_imageview1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 392;BA.debugLine="Dim Label1, Label2 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 393;BA.debugLine="Label1.Initialize(\"\")";
_label1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 394;BA.debugLine="Label2.Initialize(\"\")";
_label2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 395;BA.debugLine="Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 396;BA.debugLine="Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 398;BA.debugLine="Label1.TextColor= Colors.black";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 399;BA.debugLine="Label1.TextSize= 17";
_label1.setTextSize((float) (17));
 //BA.debugLineNum = 400;BA.debugLine="Label1.Gravity=Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 401;BA.debugLine="Label1.Color=Colors.argb(0,0,0,0)";
_label1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 402;BA.debugLine="Label1.Text=fullN_llist.Get(i) 'set data from li";
_label1.setText(_fulln_llist.Get(_i));
 //BA.debugLineNum = 404;BA.debugLine="Label2.TextColor= Colors.black";
_label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 405;BA.debugLine="Label2.TextSize= 15";
_label2.setTextSize((float) (15));
 //BA.debugLineNum = 406;BA.debugLine="Label2.Gravity=Gravity.CENTER";
_label2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 407;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 408;BA.debugLine="Label2.Text=location_list.Get(i) 'set data from";
_label2.setText(_location_list.Get(_i));
 //BA.debugLineNum = 411;BA.debugLine="If i > id_list.size-1 Then i = id_list.size-1";
if (_i>_id_list.getSize()-1) { 
_i = (int) (_id_list.getSize()-1);};
 //BA.debugLineNum = 414;BA.debugLine="PanelTop=PanelTop+PanelHeight";
_paneltop = (int) (_paneltop+_panelheight);
 }
};
 //BA.debugLineNum = 416;BA.debugLine="Panel0.Height=PanelTop";
_panel0.setHeight(_paneltop);
 //BA.debugLineNum = 418;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 420;BA.debugLine="dialog_panel.AddView(scrolllista,1%x,1%y,75%x,78%";
mostCurrent._dialog_panel.AddView((android.view.View)(mostCurrent._scrolllista.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (78),mostCurrent.activityBA));
 //BA.debugLineNum = 421;BA.debugLine="cd.AddView(dialog_panel,75%x,78%y)";
_cd.AddView((android.view.View)(mostCurrent._dialog_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (78),mostCurrent.activityBA));
 //BA.debugLineNum = 423;BA.debugLine="cd.Show(\"List of people\", \"CANCEL\", \"VIEW\", \"\", N";
_cd.Show("List of people","CANCEL","VIEW","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 425;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 553;BA.debugLine="Sub list_btn_Click";
 //BA.debugLineNum = 554;BA.debugLine="ProgressDialogShow2(\"Loading data, Please Wait...";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Loading data, Please Wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 555;BA.debugLine="If scrolllista.IsInitialized == True Then";
if (mostCurrent._scrolllista.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 556;BA.debugLine="scrolllista.RemoveView";
mostCurrent._scrolllista.RemoveView();
 //BA.debugLineNum = 557;BA.debugLine="dialog_all_panel.RemoveView";
mostCurrent._dialog_all_panel.RemoveView();
 };
 //BA.debugLineNum = 559;BA.debugLine="scrolllista.Initialize(500)";
mostCurrent._scrolllista.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 560;BA.debugLine="dialog_panel.Initialize(\"dialog_panel\")";
mostCurrent._dialog_panel.Initialize(mostCurrent.activityBA,"dialog_panel");
 //BA.debugLineNum = 561;BA.debugLine="dialog_all_panel.Initialize(\"dialog_all_panel\")";
mostCurrent._dialog_all_panel.Initialize(mostCurrent.activityBA,"dialog_all_panel");
 //BA.debugLineNum = 563;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 564;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 565;BA.debugLine="Dim bgnd As ColorDrawable";
_bgnd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 566;BA.debugLine="bgnd.Initialize(Colors.Cyan, 5dip)";
_bgnd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Cyan,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 567;BA.debugLine="pnl.Background = bgnd";
_pnl.setBackground((android.graphics.drawable.Drawable)(_bgnd.getObject()));
 //BA.debugLineNum = 568;BA.debugLine="dialog_all_panel.Color = Colors.Transparent";
mostCurrent._dialog_all_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 572;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 583;BA.debugLine="Dim Bitmap1 As Bitmap";
_bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 584;BA.debugLine="Dim Panel0 As Panel";
_panel0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 585;BA.debugLine="Dim PanelTop, PanelHeight  As Int";
_paneltop = 0;
_panelheight = 0;
 //BA.debugLineNum = 590;BA.debugLine="Bitmap1.Initialize(File.DirAssets,\"banner1.png\")";
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner1.png");
 //BA.debugLineNum = 591;BA.debugLine="PanelTop=1%y";
_paneltop = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA);
 //BA.debugLineNum = 592;BA.debugLine="Panel0=scrolllista.Panel";
_panel0 = mostCurrent._scrolllista.getPanel();
 //BA.debugLineNum = 593;BA.debugLine="Panel0.Color=Colors.argb(0,0,0,0)  'sets the invi";
_panel0.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 595;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step23 = 1;
final int limit23 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step23 > 0 && _i <= limit23) || (step23 < 0 && _i >= limit23); _i = ((int)(0 + _i + step23)) ) {
 //BA.debugLineNum = 597;BA.debugLine="If i>0 And i<3 Then Bitmap1.Initialize(File.DirA";
if (_i>0 && _i<3) { 
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner.png");};
 //BA.debugLineNum = 598;BA.debugLine="Dim ImageView1 As ImageView";
_imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 599;BA.debugLine="ImageView1.Initialize(\"data_list\")";
_imageview1.Initialize(mostCurrent.activityBA,"data_list");
 //BA.debugLineNum = 600;BA.debugLine="PanelHeight=12%y";
_panelheight = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA);
 //BA.debugLineNum = 602;BA.debugLine="Panel0.AddView(ImageView1,1%x,PanelTop,71%x,Pane";
_panel0.AddView((android.view.View)(_imageview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),_paneltop,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 603;BA.debugLine="ImageView1.Tag=i&\"1\"";
_imageview1.setTag((Object)(BA.NumberToString(_i)+"1"));
 //BA.debugLineNum = 604;BA.debugLine="ImageView1.Bitmap=Bitmap1";
_imageview1.setBitmap((android.graphics.Bitmap)(_bitmap1.getObject()));
 //BA.debugLineNum = 605;BA.debugLine="ImageView1.Gravity=Gravity.fill";
_imageview1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 607;BA.debugLine="Dim Label1, Label2 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 608;BA.debugLine="Label1.Initialize(\"\")";
_label1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 609;BA.debugLine="Label2.Initialize(\"\")";
_label2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 610;BA.debugLine="Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 611;BA.debugLine="Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 613;BA.debugLine="Label1.TextColor= Colors.White";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 614;BA.debugLine="Label1.TextSize= 18";
_label1.setTextSize((float) (18));
 //BA.debugLineNum = 615;BA.debugLine="Label1.Typeface = Typeface.DEFAULT_BOLD";
_label1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 616;BA.debugLine="Label1.Gravity=Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 617;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 618;BA.debugLine="Label1.Text=fullN_llist.Get(i) 'set data from li";
_label1.setText(_fulln_llist.Get(_i));
 //BA.debugLineNum = 620;BA.debugLine="Label2.TextColor= Colors.White";
_label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 621;BA.debugLine="Label2.TextSize= 15";
_label2.setTextSize((float) (15));
 //BA.debugLineNum = 622;BA.debugLine="Label2.Gravity=Gravity.CENTER";
_label2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 623;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 624;BA.debugLine="Label2.Text=location_list.Get(i) 'set data from";
_label2.setText(_location_list.Get(_i));
 //BA.debugLineNum = 627;BA.debugLine="If i > id_list.size-1 Then i = id_list.size-1";
if (_i>_id_list.getSize()-1) { 
_i = (int) (_id_list.getSize()-1);};
 //BA.debugLineNum = 630;BA.debugLine="PanelTop=PanelTop+PanelHeight";
_paneltop = (int) (_paneltop+_panelheight);
 }
};
 //BA.debugLineNum = 632;BA.debugLine="Panel0.Height=PanelTop";
_panel0.setHeight(_paneltop);
 //BA.debugLineNum = 634;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 635;BA.debugLine="dialog_panel.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._dialog_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"modal_bg.png").getObject()));
 //BA.debugLineNum = 637;BA.debugLine="Dim dialog_panel_can_btn As Button";
_dialog_panel_can_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 638;BA.debugLine="Dim dialog_panel_tittle As Label";
_dialog_panel_tittle = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 639;BA.debugLine="Dim btn_panel As Panel";
_btn_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 640;BA.debugLine="btn_panel.Initialize(\"\")";
_btn_panel.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 641;BA.debugLine="btn_panel.SetBackgroundImage(LoadBitmap(File.DirA";
_btn_panel.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 642;BA.debugLine="dialog_panel_can_btn.Initialize(\"dialog_panel_can";
_dialog_panel_can_btn.Initialize(mostCurrent.activityBA,"dialog_panel_can_btn");
 //BA.debugLineNum = 643;BA.debugLine="dialog_panel_tittle.Initialize(\"dialog_panel_titt";
_dialog_panel_tittle.Initialize(mostCurrent.activityBA,"dialog_panel_tittle");
 //BA.debugLineNum = 644;BA.debugLine="dialog_panel_tittle.Text = \"LIST OF PEOPLE\"";
_dialog_panel_tittle.setText((Object)("LIST OF PEOPLE"));
 //BA.debugLineNum = 645;BA.debugLine="dialog_panel_can_btn.Text = \"SEARCH\"";
_dialog_panel_can_btn.setText((Object)("SEARCH"));
 //BA.debugLineNum = 646;BA.debugLine="dialog_panel_tittle.TextSize = 30";
_dialog_panel_tittle.setTextSize((float) (30));
 //BA.debugLineNum = 647;BA.debugLine="dialog_panel_tittle.Gravity = Gravity.CENTER";
_dialog_panel_tittle.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 648;BA.debugLine="dialog_panel_can_btn.Gravity = Gravity.CENTER";
_dialog_panel_can_btn.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 649;BA.debugLine="dialog_panel.AddView(dialog_panel_tittle,1%x,2%y,";
mostCurrent._dialog_panel.AddView((android.view.View)(_dialog_panel_tittle.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (83),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 650;BA.debugLine="dialog_panel.AddView(scrolllista,5%x,dialog_panel";
mostCurrent._dialog_panel.AddView((android.view.View)(mostCurrent._scrolllista.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) (_dialog_panel_tittle.getTop()+_dialog_panel_tittle.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (69),mostCurrent.activityBA));
 //BA.debugLineNum = 651;BA.debugLine="dialog_panel.AddView(btn_panel,1%x,79%y,83%x,10%y";
mostCurrent._dialog_panel.AddView((android.view.View)(_btn_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (79),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (83),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 652;BA.debugLine="btn_panel.AddView(dialog_panel_can_btn,((btn_pane";
_btn_panel.AddView((android.view.View)(_dialog_panel_can_btn.getObject()),(int) (((_btn_panel.getWidth()/(double)2)/(double)2)),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (42),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 653;BA.debugLine="dialog_all_panel.AddView(dialog_panel,5%x,5%y,85%";
mostCurrent._dialog_all_panel.AddView((android.view.View)(mostCurrent._dialog_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (85),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 654;BA.debugLine="Activity.AddView(dialog_all_panel,0,0,100%x,100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._dialog_all_panel.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 659;BA.debugLine="End Sub";
return "";
}
public static String  _load_list() throws Exception{
 //BA.debugLineNum = 120;BA.debugLine="Sub load_list";
 //BA.debugLineNum = 121;BA.debugLine="list_bloodgroup.Initialize";
_list_bloodgroup.Initialize();
 //BA.debugLineNum = 122;BA.debugLine="list_bloodgroup.Add(\"A\")";
_list_bloodgroup.Add((Object)("A"));
 //BA.debugLineNum = 123;BA.debugLine="list_bloodgroup.Add(\"B\")";
_list_bloodgroup.Add((Object)("B"));
 //BA.debugLineNum = 124;BA.debugLine="list_bloodgroup.Add(\"O\")";
_list_bloodgroup.Add((Object)("O"));
 //BA.debugLineNum = 125;BA.debugLine="list_bloodgroup.Add(\"AB\")";
_list_bloodgroup.Add((Object)("AB"));
 //BA.debugLineNum = 126;BA.debugLine="list_bloodgroup.Add(\"A+\")";
_list_bloodgroup.Add((Object)("A+"));
 //BA.debugLineNum = 127;BA.debugLine="search_spiner.AddAll(list_bloodgroup)";
mostCurrent._search_spiner.AddAll(_list_bloodgroup);
 //BA.debugLineNum = 128;BA.debugLine="spin_item_click = \"A\";";
mostCurrent._spin_item_click = "A";
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public static String  _lv_itemclick(int _position,Object _value) throws Exception{
b4a.example.calculations _calc = null;
 //BA.debugLineNum = 333;BA.debugLine="Sub lv_ItemClick (Position As Int, Value As Object";
 //BA.debugLineNum = 334;BA.debugLine="Dim calc As calculations";
_calc = new b4a.example.calculations();
 //BA.debugLineNum = 335;BA.debugLine="calc.users_id = id_list.Get(Position)";
_calc._users_id = BA.ObjectToString(_id_list.Get(_position));
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return "";
}
public static String  _map_shows_pagefinished(String _url) throws Exception{
 //BA.debugLineNum = 233;BA.debugLine="Sub map_shows_PageFinished (Url As String)";
 //BA.debugLineNum = 234;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 235;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 22;BA.debugLine="Dim is_complete As Int : is_complete = 0";
_is_complete = 0;
 //BA.debugLineNum = 22;BA.debugLine="Dim is_complete As Int : is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 438;BA.debugLine="Sub reading_txt";
 //BA.debugLineNum = 439;BA.debugLine="id_list.Initialize";
_id_list.Initialize();
 //BA.debugLineNum = 440;BA.debugLine="fullN_llist.Initialize";
_fulln_llist.Initialize();
 //BA.debugLineNum = 441;BA.debugLine="location_list.Initialize";
_location_list.Initialize();
 //BA.debugLineNum = 442;BA.debugLine="lat_list.Initialize";
_lat_list.Initialize();
 //BA.debugLineNum = 443;BA.debugLine="lng_list.Initialize";
_lng_list.Initialize();
 //BA.debugLineNum = 444;BA.debugLine="donated_list.Initialize";
_donated_list.Initialize();
 //BA.debugLineNum = 445;BA.debugLine="email_list.Initialize";
_email_list.Initialize();
 //BA.debugLineNum = 446;BA.debugLine="nickname_list.Initialize";
_nickname_list.Initialize();
 //BA.debugLineNum = 447;BA.debugLine="phone1_list.Initialize";
_phone1_list.Initialize();
 //BA.debugLineNum = 448;BA.debugLine="phone2_list.Initialize";
_phone2_list.Initialize();
 //BA.debugLineNum = 450;BA.debugLine="Dim TextReader_id As TextReader";
_textreader_id = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 451;BA.debugLine="TextReader_id.Initialize(File.OpenInput(File.D";
_textreader_id.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt").getObject()));
 //BA.debugLineNum = 452;BA.debugLine="Dim line_id As String";
_line_id = "";
 //BA.debugLineNum = 453;BA.debugLine="line_id = TextReader_id.ReadLine";
_line_id = _textreader_id.ReadLine();
 //BA.debugLineNum = 454;BA.debugLine="Do While line_id <> Null";
while (_line_id!= null) {
 //BA.debugLineNum = 455;BA.debugLine="id_list.Add(line_id)";
_id_list.Add((Object)(_line_id));
 //BA.debugLineNum = 456;BA.debugLine="line_id = TextReader_id.ReadLine";
_line_id = _textreader_id.ReadLine();
 }
;
 //BA.debugLineNum = 458;BA.debugLine="TextReader_id.Close";
_textreader_id.Close();
 //BA.debugLineNum = 460;BA.debugLine="Dim TextReader_fullN As TextReader";
_textreader_fulln = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 461;BA.debugLine="TextReader_fullN.Initialize(File.OpenInput(Fil";
_textreader_fulln.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt").getObject()));
 //BA.debugLineNum = 462;BA.debugLine="Dim line_fullN As String";
_line_fulln = "";
 //BA.debugLineNum = 463;BA.debugLine="line_fullN = TextReader_fullN.ReadLine";
_line_fulln = _textreader_fulln.ReadLine();
 //BA.debugLineNum = 464;BA.debugLine="Do While line_fullN <> Null";
while (_line_fulln!= null) {
 //BA.debugLineNum = 466;BA.debugLine="fullN_llist.Add(line_fullN)";
_fulln_llist.Add((Object)(_line_fulln));
 //BA.debugLineNum = 467;BA.debugLine="line_fullN = TextReader_fullN.ReadLine";
_line_fulln = _textreader_fulln.ReadLine();
 }
;
 //BA.debugLineNum = 469;BA.debugLine="TextReader_fullN.Close";
_textreader_fulln.Close();
 //BA.debugLineNum = 471;BA.debugLine="Dim TextReader_location As TextReader";
_textreader_location = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 472;BA.debugLine="TextReader_location.Initialize(File.OpenInput(";
_textreader_location.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt").getObject()));
 //BA.debugLineNum = 473;BA.debugLine="Dim line_location As String";
_line_location = "";
 //BA.debugLineNum = 474;BA.debugLine="line_location = TextReader_location.ReadLine";
_line_location = _textreader_location.ReadLine();
 //BA.debugLineNum = 475;BA.debugLine="Do While line_location <> Null";
while (_line_location!= null) {
 //BA.debugLineNum = 477;BA.debugLine="location_list.Add(line_location)";
_location_list.Add((Object)(_line_location));
 //BA.debugLineNum = 478;BA.debugLine="line_location = TextReader_location.ReadLi";
_line_location = _textreader_location.ReadLine();
 }
;
 //BA.debugLineNum = 480;BA.debugLine="TextReader_location.Close";
_textreader_location.Close();
 //BA.debugLineNum = 482;BA.debugLine="Dim TextReader_lat As TextReader";
_textreader_lat = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 483;BA.debugLine="TextReader_lat.Initialize(File.OpenInput(File.";
_textreader_lat.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt").getObject()));
 //BA.debugLineNum = 484;BA.debugLine="Dim line_lat As String";
_line_lat = "";
 //BA.debugLineNum = 485;BA.debugLine="line_lat = TextReader_lat.ReadLine";
_line_lat = _textreader_lat.ReadLine();
 //BA.debugLineNum = 486;BA.debugLine="Do While line_lat <> Null";
while (_line_lat!= null) {
 //BA.debugLineNum = 487;BA.debugLine="lat_list.Add(line_lat)";
_lat_list.Add((Object)(_line_lat));
 //BA.debugLineNum = 488;BA.debugLine="line_lat = TextReader_lat.ReadLine";
_line_lat = _textreader_lat.ReadLine();
 }
;
 //BA.debugLineNum = 490;BA.debugLine="TextReader_lat.Close";
_textreader_lat.Close();
 //BA.debugLineNum = 492;BA.debugLine="Dim TextReader_lng As TextReader";
_textreader_lng = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 493;BA.debugLine="TextReader_lng.Initialize(File.OpenInput(File.";
_textreader_lng.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt").getObject()));
 //BA.debugLineNum = 494;BA.debugLine="Dim line_lng As String";
_line_lng = "";
 //BA.debugLineNum = 495;BA.debugLine="line_lng = TextReader_lng.ReadLine";
_line_lng = _textreader_lng.ReadLine();
 //BA.debugLineNum = 496;BA.debugLine="Do While line_lng <> Null";
while (_line_lng!= null) {
 //BA.debugLineNum = 497;BA.debugLine="lng_list.Add(line_lng)";
_lng_list.Add((Object)(_line_lng));
 //BA.debugLineNum = 498;BA.debugLine="line_lng = TextReader_lng.ReadLine";
_line_lng = _textreader_lng.ReadLine();
 }
;
 //BA.debugLineNum = 500;BA.debugLine="TextReader_lng.Close";
_textreader_lng.Close();
 //BA.debugLineNum = 502;BA.debugLine="Dim TextReader_donate As TextReader";
_textreader_donate = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 503;BA.debugLine="TextReader_donate.Initialize(File.OpenInput(Fi";
_textreader_donate.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_donated.txt").getObject()));
 //BA.debugLineNum = 504;BA.debugLine="Dim line_donate As String";
_line_donate = "";
 //BA.debugLineNum = 505;BA.debugLine="line_donate = TextReader_donate.ReadLine";
_line_donate = _textreader_donate.ReadLine();
 //BA.debugLineNum = 506;BA.debugLine="Do While line_donate <> Null";
while (_line_donate!= null) {
 //BA.debugLineNum = 507;BA.debugLine="donated_list.Add(line_donate)";
_donated_list.Add((Object)(_line_donate));
 //BA.debugLineNum = 508;BA.debugLine="line_donate = TextReader_donate.ReadLine";
_line_donate = _textreader_donate.ReadLine();
 }
;
 //BA.debugLineNum = 510;BA.debugLine="TextReader_donate.Close";
_textreader_donate.Close();
 //BA.debugLineNum = 512;BA.debugLine="Dim TextReader_email As TextReader";
_textreader_email = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 513;BA.debugLine="TextReader_email.Initialize(File.OpenInput(Fil";
_textreader_email.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_email.txt").getObject()));
 //BA.debugLineNum = 514;BA.debugLine="Dim line_email As String";
_line_email = "";
 //BA.debugLineNum = 515;BA.debugLine="line_email = TextReader_email.ReadLine";
_line_email = _textreader_email.ReadLine();
 //BA.debugLineNum = 516;BA.debugLine="Do While line_email <> Null";
while (_line_email!= null) {
 //BA.debugLineNum = 517;BA.debugLine="email_list.Add(line_email)";
_email_list.Add((Object)(_line_email));
 //BA.debugLineNum = 518;BA.debugLine="line_email = TextReader_email.ReadLine";
_line_email = _textreader_email.ReadLine();
 }
;
 //BA.debugLineNum = 520;BA.debugLine="TextReader_email.Close";
_textreader_email.Close();
 //BA.debugLineNum = 522;BA.debugLine="Dim TextReader_nickname As TextReader";
_textreader_nickname = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 523;BA.debugLine="TextReader_nickname.Initialize(File.OpenInput(";
_textreader_nickname.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_nickname.txt").getObject()));
 //BA.debugLineNum = 524;BA.debugLine="Dim line_nickname As String";
_line_nickname = "";
 //BA.debugLineNum = 525;BA.debugLine="line_nickname = TextReader_nickname.ReadLine";
_line_nickname = _textreader_nickname.ReadLine();
 //BA.debugLineNum = 526;BA.debugLine="Do While line_nickname <> Null";
while (_line_nickname!= null) {
 //BA.debugLineNum = 527;BA.debugLine="nickname_list.Add(line_nickname)";
_nickname_list.Add((Object)(_line_nickname));
 //BA.debugLineNum = 528;BA.debugLine="line_nickname = TextReader_nickname.ReadLi";
_line_nickname = _textreader_nickname.ReadLine();
 }
;
 //BA.debugLineNum = 530;BA.debugLine="TextReader_nickname.Close";
_textreader_nickname.Close();
 //BA.debugLineNum = 532;BA.debugLine="Dim TextReader_phone1 As TextReader";
_textreader_phone1 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 533;BA.debugLine="TextReader_phone1.Initialize(File.OpenInput(Fi";
_textreader_phone1.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone1.txt").getObject()));
 //BA.debugLineNum = 534;BA.debugLine="Dim line_phone1 As String";
_line_phone1 = "";
 //BA.debugLineNum = 535;BA.debugLine="line_phone1 = TextReader_phone1.ReadLine";
_line_phone1 = _textreader_phone1.ReadLine();
 //BA.debugLineNum = 536;BA.debugLine="Do While line_phone1 <> Null";
while (_line_phone1!= null) {
 //BA.debugLineNum = 537;BA.debugLine="phone1_list.Add(line_phone1)";
_phone1_list.Add((Object)(_line_phone1));
 //BA.debugLineNum = 538;BA.debugLine="line_phone1 = TextReader_phone1.ReadLine";
_line_phone1 = _textreader_phone1.ReadLine();
 }
;
 //BA.debugLineNum = 540;BA.debugLine="TextReader_phone1.Close";
_textreader_phone1.Close();
 //BA.debugLineNum = 542;BA.debugLine="Dim TextReader_phone2 As TextReader";
_textreader_phone2 = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 543;BA.debugLine="TextReader_phone2.Initialize(File.OpenInput(Fi";
_textreader_phone2.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_phone2.txt").getObject()));
 //BA.debugLineNum = 544;BA.debugLine="Dim line_phone2 As String";
_line_phone2 = "";
 //BA.debugLineNum = 545;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 //BA.debugLineNum = 546;BA.debugLine="Do While line_phone2 <> Null";
while (_line_phone2!= null) {
 //BA.debugLineNum = 547;BA.debugLine="phone2_list.Add(line_phone2)";
_phone2_list.Add((Object)(_line_phone2));
 //BA.debugLineNum = 548;BA.debugLine="line_phone2 = TextReader_phone2.ReadLine";
_line_phone2 = _textreader_phone2.ReadLine();
 }
;
 //BA.debugLineNum = 550;BA.debugLine="TextReader_phone2.Close";
_textreader_phone2.Close();
 //BA.debugLineNum = 552;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 139;BA.debugLine="Sub search_btn_Click";
 //BA.debugLineNum = 140;BA.debugLine="ProgressDialogShow2(\"please wait.!!\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"please wait.!!",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 142;BA.debugLine="Dim url_back As calculations";
_url_back = new b4a.example.calculations();
 //BA.debugLineNum = 143;BA.debugLine="Dim url_id,full_name,location,lat,lng,donated,ema";
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
 //BA.debugLineNum = 144;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 145;BA.debugLine="url_id = url_back.php_email_url(\"/bloodlifePHP/se";
_url_id = _url_back._php_email_url("/bloodlifePHP/search_blood_id.php");
 //BA.debugLineNum = 146;BA.debugLine="full_name = url_back.php_email_url(\"/bloodlifePHP";
_full_name = _url_back._php_email_url("/bloodlifePHP/search_blood_fullN.php");
 //BA.debugLineNum = 147;BA.debugLine="location = url_back.php_email_url(\"/bloodlifePHP/";
_location = _url_back._php_email_url("/bloodlifePHP/search_blood_location.php");
 //BA.debugLineNum = 148;BA.debugLine="lat = url_back.php_email_url(\"/bloodlifePHP/searc";
_lat = _url_back._php_email_url("/bloodlifePHP/search_blood_lat.php");
 //BA.debugLineNum = 149;BA.debugLine="lng = url_back.php_email_url(\"/bloodlifePHP/searc";
_lng = _url_back._php_email_url("/bloodlifePHP/search_blood_long.php");
 //BA.debugLineNum = 150;BA.debugLine="donated = url_back.php_email_url(\"/bloodlifePHP/s";
_donated = _url_back._php_email_url("/bloodlifePHP/search_blood_donateB.php");
 //BA.debugLineNum = 151;BA.debugLine="email = url_back.php_email_url(\"/bloodlifePHP/sea";
_email = _url_back._php_email_url("/bloodlifePHP/search_blood_email.php");
 //BA.debugLineNum = 152;BA.debugLine="nickname = url_back.php_email_url(\"/bloodlifePHP/";
_nickname = _url_back._php_email_url("/bloodlifePHP/search_blood_nickN.php");
 //BA.debugLineNum = 153;BA.debugLine="phone1 = url_back.php_email_url(\"/bloodlifePHP/se";
_phone1 = _url_back._php_email_url("/bloodlifePHP/search_blood_phone1.php");
 //BA.debugLineNum = 154;BA.debugLine="phone2 = url_back.php_email_url(\"/bloodlifePHP/se";
_phone2 = _url_back._php_email_url("/bloodlifePHP/search_blood_phone2.php");
 //BA.debugLineNum = 157;BA.debugLine="data_query_id.Download2(url_id,Array As String(\"i";
mostCurrent._data_query_id._download2(_url_id,new String[]{"id","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 158;BA.debugLine="data_query_fullN.Download2(full_name,Array As Str";
mostCurrent._data_query_fulln._download2(_full_name,new String[]{"full_name","SELECT full_name FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 159;BA.debugLine="data_query_location.Download2(location,Array As S";
mostCurrent._data_query_location._download2(_location,new String[]{"location","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 160;BA.debugLine="query_lat.Download2(lat,Array As String(\"lat\",\"SE";
mostCurrent._query_lat._download2(_lat,new String[]{"lat","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 161;BA.debugLine="query_lng.Download2(lng,Array As String(\"long\",\"S";
mostCurrent._query_lng._download2(_lng,new String[]{"long","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 163;BA.debugLine="data_query_donated.Download2(donated,Array As Str";
mostCurrent._data_query_donated._download2(_donated,new String[]{"donate_b","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 164;BA.debugLine="data_query_email.Download2(email,Array As String(";
mostCurrent._data_query_email._download2(_email,new String[]{"email","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 165;BA.debugLine="data_query_nickname.Download2(nickname,Array As S";
mostCurrent._data_query_nickname._download2(_nickname,new String[]{"nick","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 166;BA.debugLine="data_query_phone1.Download2(phone1,Array As Strin";
mostCurrent._data_query_phone1._download2(_phone1,new String[]{"phone1","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 167;BA.debugLine="data_query_phone2.Download2(phone2,Array As Strin";
mostCurrent._data_query_phone2._download2(_phone2,new String[]{"phone2","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 172;BA.debugLine="End Sub";
return "";
}
public static String  _search_spiner_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 330;BA.debugLine="Sub search_spiner_ItemClick (Position As Int, Valu";
 //BA.debugLineNum = 331;BA.debugLine="spin_item_click = search_spiner.GetItem(Position)";
mostCurrent._spin_item_click = mostCurrent._search_spiner.GetItem(_position).trim();
 //BA.debugLineNum = 332;BA.debugLine="End Sub";
return "";
}
}
