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
public b4a.example.httpjob _data_query_id = null;
public b4a.example.httpjob _data_query_fulln = null;
public b4a.example.httpjob _data_query_location = null;
public b4a.example.httpjob _query_lat = null;
public b4a.example.httpjob _query_lng = null;
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
 //BA.debugLineNum = 45;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 47;BA.debugLine="Activity.LoadLayout(\"search_frame\")";
mostCurrent._activity.LoadLayout("search_frame",mostCurrent.activityBA);
 //BA.debugLineNum = 48;BA.debugLine="data_query_id.Initialize(\"data_query_id_get\",Me)";
mostCurrent._data_query_id._initialize(processBA,"data_query_id_get",search_frame.getObject());
 //BA.debugLineNum = 49;BA.debugLine="data_query_fullN.Initialize(\"data_query_fullN_get";
mostCurrent._data_query_fulln._initialize(processBA,"data_query_fullN_get",search_frame.getObject());
 //BA.debugLineNum = 50;BA.debugLine="data_query_location.Initialize(\"data_query_locati";
mostCurrent._data_query_location._initialize(processBA,"data_query_location_get",search_frame.getObject());
 //BA.debugLineNum = 51;BA.debugLine="query_lat.Initialize(\"data_query_lat_get\",Me)";
mostCurrent._query_lat._initialize(processBA,"data_query_lat_get",search_frame.getObject());
 //BA.debugLineNum = 52;BA.debugLine="query_lng.Initialize(\"data_query_lng_get\",Me)";
mostCurrent._query_lng._initialize(processBA,"data_query_lng_get",search_frame.getObject());
 //BA.debugLineNum = 53;BA.debugLine="query_marker.Initialize(\"query_marker_get\",Me)";
mostCurrent._query_marker._initialize(processBA,"query_marker_get",search_frame.getObject());
 //BA.debugLineNum = 54;BA.debugLine="map_extras.addJavascriptInterface(map_webview,\"B4";
mostCurrent._map_extras.addJavascriptInterface(mostCurrent.activityBA,(android.webkit.WebView)(mostCurrent._map_webview.getObject()),"B4A");
 //BA.debugLineNum = 55;BA.debugLine="is_initialize";
_is_initialize();
 //BA.debugLineNum = 56;BA.debugLine="all_layout_load";
_all_layout_load();
 //BA.debugLineNum = 57;BA.debugLine="load_list";
_load_list();
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 119;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 121;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 115;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return "";
}
public static String  _all_layout_load() throws Exception{
 //BA.debugLineNum = 60;BA.debugLine="Sub all_layout_load";
 //BA.debugLineNum = 61;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._activity.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 62;BA.debugLine="toolkit_pnl.Color = Colors.Transparent";
mostCurrent._toolkit_pnl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 63;BA.debugLine="list_panel.Color = Colors.Transparent";
mostCurrent._list_panel.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 64;BA.debugLine="search_btn.SetBackgroundImage(LoadBitmap(File.Dir";
mostCurrent._search_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"esearch.png").getObject()));
 //BA.debugLineNum = 65;BA.debugLine="list_btn.SetBackgroundImage(LoadBitmap(File.DirAs";
mostCurrent._list_btn.SetBackgroundImage((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"view all.png").getObject()));
 //BA.debugLineNum = 67;BA.debugLine="toolkit_pnl.Width = Activity.Width";
mostCurrent._toolkit_pnl.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 68;BA.debugLine="list_panel.Width = Activity.Width";
mostCurrent._list_panel.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 69;BA.debugLine="map_webview.Width = Activity.Width";
mostCurrent._map_webview.setWidth(mostCurrent._activity.getWidth());
 //BA.debugLineNum = 71;BA.debugLine="list_btn.Width = 50%x";
mostCurrent._list_btn.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 72;BA.debugLine="search_lbl.Width = 14%x";
mostCurrent._search_lbl.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 73;BA.debugLine="search_btn.Width = 14%x";
mostCurrent._search_btn.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 74;BA.debugLine="search_spiner.Width = ((toolkit_pnl.Width - sea";
mostCurrent._search_spiner.setWidth((int) (((mostCurrent._toolkit_pnl.getWidth()-mostCurrent._search_btn.getWidth())-mostCurrent._search_lbl.getWidth())-anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA)));
 //BA.debugLineNum = 76;BA.debugLine="toolkit_pnl.Height = 14%y";
mostCurrent._toolkit_pnl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (14),mostCurrent.activityBA));
 //BA.debugLineNum = 77;BA.debugLine="list_panel.Height = 11%y";
mostCurrent._list_panel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (11),mostCurrent.activityBA));
 //BA.debugLineNum = 78;BA.debugLine="map_webview.Height =((Activity.Height - toolkit";
mostCurrent._map_webview.setHeight((int) (((mostCurrent._activity.getHeight()-mostCurrent._toolkit_pnl.getHeight())-mostCurrent._list_panel.getHeight())));
 //BA.debugLineNum = 80;BA.debugLine="list_btn.Height = 9%y";
mostCurrent._list_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (9),mostCurrent.activityBA));
 //BA.debugLineNum = 81;BA.debugLine="search_lbl.Height = 10%y";
mostCurrent._search_lbl.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 82;BA.debugLine="search_btn.Height = 10%y";
mostCurrent._search_btn.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 83;BA.debugLine="search_spiner.Height = 10%y";
mostCurrent._search_spiner.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 85;BA.debugLine="toolkit_pnl.Left = 0";
mostCurrent._toolkit_pnl.setLeft((int) (0));
 //BA.debugLineNum = 86;BA.debugLine="list_panel.Left = 0";
mostCurrent._list_panel.setLeft((int) (0));
 //BA.debugLineNum = 87;BA.debugLine="map_webview.Left = 0";
mostCurrent._map_webview.setLeft((int) (0));
 //BA.debugLineNum = 89;BA.debugLine="list_btn.Left = ((list_panel.Width/2)/2)";
mostCurrent._list_btn.setLeft((int) (((mostCurrent._list_panel.getWidth()/(double)2)/(double)2)));
 //BA.debugLineNum = 90;BA.debugLine="search_lbl.Left = ((toolkit_pnl.Left + 3%x)+2%x";
mostCurrent._search_lbl.setLeft((int) (((mostCurrent._toolkit_pnl.getLeft()+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (3),mostCurrent.activityBA))+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA))));
 //BA.debugLineNum = 91;BA.debugLine="search_spiner.Left = (search_lbl.Left + search_";
mostCurrent._search_spiner.setLeft((int) ((mostCurrent._search_lbl.getLeft()+mostCurrent._search_lbl.getWidth())));
 //BA.debugLineNum = 92;BA.debugLine="search_btn.Left = (search_spiner.Left + searc";
mostCurrent._search_btn.setLeft((int) ((mostCurrent._search_spiner.getLeft()+mostCurrent._search_spiner.getWidth())));
 //BA.debugLineNum = 94;BA.debugLine="toolkit_pnl.Top = 0";
mostCurrent._toolkit_pnl.setTop((int) (0));
 //BA.debugLineNum = 95;BA.debugLine="map_webview.Top = (toolkit_pnl.Top + toolkit_pnl.";
mostCurrent._map_webview.setTop((int) ((mostCurrent._toolkit_pnl.getTop()+mostCurrent._toolkit_pnl.getHeight())));
 //BA.debugLineNum = 96;BA.debugLine="list_panel.Top = (map_webview.Top + map_webview.";
mostCurrent._list_panel.setTop((int) ((mostCurrent._map_webview.getTop()+mostCurrent._map_webview.getHeight())));
 //BA.debugLineNum = 98;BA.debugLine="list_btn.Top = 1%y'(list_panel.Top + 1%Y)";
mostCurrent._list_btn.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA));
 //BA.debugLineNum = 99;BA.debugLine="search_lbl.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_lbl.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 100;BA.debugLine="search_btn.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_btn.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 101;BA.debugLine="search_spiner.top = ((toolkit_pnl.Height/2)/3)";
mostCurrent._search_spiner.setTop((int) (((mostCurrent._toolkit_pnl.getHeight()/(double)2)/(double)3)));
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}
public static String  _create_map() throws Exception{
String _htmlstring = "";
int _i = 0;
 //BA.debugLineNum = 145;BA.debugLine="Sub create_map";
 //BA.debugLineNum = 146;BA.debugLine="ProgressDialogShow2(\"Creating the map, please wai";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Creating the map, please wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 147;BA.debugLine="Dim htmlString As String";
_htmlstring = "";
 //BA.debugLineNum = 148;BA.debugLine="htmlString = File.GetText(File.DirAssets, \"locati";
_htmlstring = anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"location_top.txt");
 //BA.debugLineNum = 150;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step4 = 1;
final int limit4 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4); _i = ((int)(0 + _i + step4)) ) {
 //BA.debugLineNum = 151;BA.debugLine="Log(id_list.Get(i))";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(_id_list.Get(_i)));
 //BA.debugLineNum = 153;BA.debugLine="htmlString = htmlString & \" var markerc\"&i&\" = ne";
_htmlstring = _htmlstring+" var markerc"+BA.NumberToString(_i)+" = new google.maps.Marker({	position: new google.maps.LatLng("+BA.ObjectToString(_lat_list.Get(_i))+","+BA.ObjectToString(_lng_list.Get(_i))+"),map: map, title: '"+BA.ObjectToString(_fulln_llist.Get(_i))+"',clickable: true,icon: 'http://www.google.com/mapfiles/dd-end.png' });";
 }
};
 //BA.debugLineNum = 157;BA.debugLine="htmlString = htmlString&File.GetText(File.DirAsse";
_htmlstring = _htmlstring+anywheresoftware.b4a.keywords.Common.File.GetText(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"location_buttom.txt");
 //BA.debugLineNum = 158;BA.debugLine="map_webview.LoadHtml(htmlString)";
mostCurrent._map_webview.LoadHtml(_htmlstring);
 //BA.debugLineNum = 159;BA.debugLine="Log(htmlString)";
anywheresoftware.b4a.keywords.Common.Log(_htmlstring);
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
return "";
}
public static String  _data_list_click() throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _send = null;
int _row = 0;
 //BA.debugLineNum = 322;BA.debugLine="Sub data_list_Click";
 //BA.debugLineNum = 323;BA.debugLine="Dim Send As View";
_send = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
 //BA.debugLineNum = 324;BA.debugLine="Dim row As Int";
_row = 0;
 //BA.debugLineNum = 325;BA.debugLine="Send=Sender";
_send.setObject((android.view.View)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 326;BA.debugLine="row=Floor(Send.Tag/10) '20";
_row = (int) (anywheresoftware.b4a.keywords.Common.Floor((double)(BA.ObjectToNumber(_send.getTag()))/(double)10));
 //BA.debugLineNum = 327;BA.debugLine="item=row";
_item = _row;
 //BA.debugLineNum = 328;BA.debugLine="Log(row)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_row));
 //BA.debugLineNum = 329;BA.debugLine="Log(CRLF&\"Item \"&item)";
anywheresoftware.b4a.keywords.Common.Log(anywheresoftware.b4a.keywords.Common.CRLF+"Item "+BA.NumberToString(_item));
 //BA.debugLineNum = 331;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 23;BA.debugLine="Public spin_item_click As String";
mostCurrent._spin_item_click = "";
 //BA.debugLineNum = 24;BA.debugLine="Private toolkit_pnl As Panel";
mostCurrent._toolkit_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private search_lbl As Label";
mostCurrent._search_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private search_spiner As Spinner";
mostCurrent._search_spiner = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private search_btn As Button";
mostCurrent._search_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private map_webview As WebView";
mostCurrent._map_webview = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private list_btn As Button";
mostCurrent._list_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private list_panel As Panel";
mostCurrent._list_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private map_extras As WebViewExtras";
mostCurrent._map_extras = new uk.co.martinpearman.b4a.webviewextras.WebViewExtras();
 //BA.debugLineNum = 33;BA.debugLine="Dim scrolllista As ScrollView";
mostCurrent._scrolllista = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim item As Int";
_item = 0;
 //BA.debugLineNum = 35;BA.debugLine="Dim dialog_panel As Panel";
mostCurrent._dialog_panel = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private data_query_id As HttpJob";
mostCurrent._data_query_id = new b4a.example.httpjob();
 //BA.debugLineNum = 38;BA.debugLine="Private data_query_fullN As HttpJob";
mostCurrent._data_query_fulln = new b4a.example.httpjob();
 //BA.debugLineNum = 39;BA.debugLine="Private data_query_location As HttpJob";
mostCurrent._data_query_location = new b4a.example.httpjob();
 //BA.debugLineNum = 40;BA.debugLine="Private query_lat As HttpJob";
mostCurrent._query_lat = new b4a.example.httpjob();
 //BA.debugLineNum = 41;BA.debugLine="Private query_lng As HttpJob";
mostCurrent._query_lng = new b4a.example.httpjob();
 //BA.debugLineNum = 42;BA.debugLine="Private query_marker As HttpJob";
mostCurrent._query_marker = new b4a.example.httpjob();
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _is_initialize() throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriters = null;
 //BA.debugLineNum = 165;BA.debugLine="Sub is_initialize";
 //BA.debugLineNum = 166;BA.debugLine="Dim TextWriters As TextWriter";
_textwriters = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 167;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 168;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirIn";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 169;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.DirI";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 170;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 171;BA.debugLine="TextWriters.Initialize(File.OpenOutput(File.Dir";
_textwriters.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 172;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(b4a.example.httpjob _job) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_id = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_full = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_location = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_lat = null;
anywheresoftware.b4a.objects.streams.File.TextWriterWrapper _textwriter_lng = null;
 //BA.debugLineNum = 173;BA.debugLine="Public Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 174;BA.debugLine="If job.Success Then";
if (_job._success) { 
 //BA.debugLineNum = 175;BA.debugLine="Select job.JobName";
switch (BA.switchObjectToInt(_job._jobname,"data_query_id_get","data_query_fullN_get","data_query_location_get","data_query_lat_get","data_query_lng_get")) {
case 0: {
 //BA.debugLineNum = 177;BA.debugLine="Dim TextWriter_id As TextWriter";
_textwriter_id = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 178;BA.debugLine="TextWriter_id.Initialize(File.OpenOutput(";
_textwriter_id.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 179;BA.debugLine="TextWriter_id.WriteLine(job.GetString.Tr";
_textwriter_id.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 180;BA.debugLine="TextWriter_id.Close";
_textwriter_id.Close();
 break; }
case 1: {
 //BA.debugLineNum = 183;BA.debugLine="Dim TextWriter_full As TextWriter";
_textwriter_full = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 184;BA.debugLine="TextWriter_full.Initialize(File.OpenOutpu";
_textwriter_full.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 185;BA.debugLine="TextWriter_full.WriteLine(job.GetString.";
_textwriter_full.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 186;BA.debugLine="TextWriter_full.Close";
_textwriter_full.Close();
 break; }
case 2: {
 //BA.debugLineNum = 189;BA.debugLine="Dim TextWriter_location As TextWriter";
_textwriter_location = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 190;BA.debugLine="TextWriter_location.Initialize(File.OpenO";
_textwriter_location.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 191;BA.debugLine="TextWriter_location.WriteLine(job.GetStr";
_textwriter_location.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 192;BA.debugLine="TextWriter_location.Close";
_textwriter_location.Close();
 break; }
case 3: {
 //BA.debugLineNum = 194;BA.debugLine="Dim TextWriter_lat As TextWriter";
_textwriter_lat = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 195;BA.debugLine="TextWriter_lat.Initialize(File.OpenOutput";
_textwriter_lat.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 196;BA.debugLine="TextWriter_lat.WriteLine(job.GetString.T";
_textwriter_lat.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 198;BA.debugLine="TextWriter_lat.Close";
_textwriter_lat.Close();
 break; }
case 4: {
 //BA.debugLineNum = 200;BA.debugLine="Dim TextWriter_lng As TextWriter";
_textwriter_lng = new anywheresoftware.b4a.objects.streams.File.TextWriterWrapper();
 //BA.debugLineNum = 201;BA.debugLine="TextWriter_lng.Initialize(File.OpenOutput";
_textwriter_lng.Initialize((java.io.OutputStream)(anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt",anywheresoftware.b4a.keywords.Common.False).getObject()));
 //BA.debugLineNum = 202;BA.debugLine="TextWriter_lng.WriteLine(job.GetString.T";
_textwriter_lng.WriteLine(_job._getstring().trim());
 //BA.debugLineNum = 204;BA.debugLine="TextWriter_lng.Close";
_textwriter_lng.Close();
 break; }
}
;
 //BA.debugLineNum = 207;BA.debugLine="If is_complete == 4 Then";
if (_is_complete==4) { 
 //BA.debugLineNum = 208;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 209;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 210;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 211;BA.debugLine="create_map";
_create_map();
 };
 //BA.debugLineNum = 213;BA.debugLine="is_complete = is_complete + 1";
_is_complete = (int) (_is_complete+1);
 }else if(_job._success==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 215;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 216;BA.debugLine="is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 217;BA.debugLine="Msgbox(\"Error: Error connecting to server,please";
anywheresoftware.b4a.keywords.Common.Msgbox("Error: Error connecting to server,please try again.!","Confirmation",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 222;BA.debugLine="End Sub";
return "";
}
public static String  _list_btn_click() throws Exception{
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
 //BA.debugLineNum = 234;BA.debugLine="Sub list_btn_Click";
 //BA.debugLineNum = 235;BA.debugLine="ProgressDialogShow2(\"Loading data, Please Wait...";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"Loading data, Please Wait...",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 236;BA.debugLine="If scrolllista.IsInitialized == True Then";
if (mostCurrent._scrolllista.IsInitialized()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 237;BA.debugLine="scrolllista.RemoveView";
mostCurrent._scrolllista.RemoveView();
 //BA.debugLineNum = 238;BA.debugLine="dialog_panel.RemoveView";
mostCurrent._dialog_panel.RemoveView();
 };
 //BA.debugLineNum = 240;BA.debugLine="scrolllista.Initialize(500)";
mostCurrent._scrolllista.Initialize(mostCurrent.activityBA,(int) (500));
 //BA.debugLineNum = 241;BA.debugLine="dialog_panel.Initialize(\"dialog_panel\")";
mostCurrent._dialog_panel.Initialize(mostCurrent.activityBA,"dialog_panel");
 //BA.debugLineNum = 242;BA.debugLine="Dim cd As CustomDialog2";
_cd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2();
 //BA.debugLineNum = 243;BA.debugLine="Dim pnl As Panel";
_pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 244;BA.debugLine="pnl.Initialize(\"pnl\")";
_pnl.Initialize(mostCurrent.activityBA,"pnl");
 //BA.debugLineNum = 245;BA.debugLine="Dim bgnd As ColorDrawable";
_bgnd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 246;BA.debugLine="bgnd.Initialize(Colors.Cyan, 5dip)";
_bgnd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Cyan,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 247;BA.debugLine="pnl.Background = bgnd";
_pnl.setBackground((android.graphics.drawable.Drawable)(_bgnd.getObject()));
 //BA.debugLineNum = 251;BA.debugLine="reading_txt";
_reading_txt();
 //BA.debugLineNum = 263;BA.debugLine="Dim Bitmap1 As Bitmap";
_bitmap1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 264;BA.debugLine="Dim Panel0 As Panel";
_panel0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 265;BA.debugLine="Dim PanelTop, PanelHeight  As Int";
_paneltop = 0;
_panelheight = 0;
 //BA.debugLineNum = 270;BA.debugLine="Bitmap1.Initialize(File.DirAssets,\"banner1.png\")";
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner1.png");
 //BA.debugLineNum = 271;BA.debugLine="PanelTop=1%y";
_paneltop = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA);
 //BA.debugLineNum = 272;BA.debugLine="Panel0=scrolllista.Panel";
_panel0 = mostCurrent._scrolllista.getPanel();
 //BA.debugLineNum = 273;BA.debugLine="Panel0.Color=Colors.argb(0,0,0,0)  'sets the invi";
_panel0.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 275;BA.debugLine="For i=0 To id_list.Size-1";
{
final int step22 = 1;
final int limit22 = (int) (_id_list.getSize()-1);
for (_i = (int) (0) ; (step22 > 0 && _i <= limit22) || (step22 < 0 && _i >= limit22); _i = ((int)(0 + _i + step22)) ) {
 //BA.debugLineNum = 277;BA.debugLine="If i>0 And i<3 Then Bitmap1.Initialize(File.DirA";
if (_i>0 && _i<3) { 
_bitmap1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"banner.png");};
 //BA.debugLineNum = 278;BA.debugLine="Dim ImageView1 As ImageView";
_imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 279;BA.debugLine="ImageView1.Initialize(\"data_list\")";
_imageview1.Initialize(mostCurrent.activityBA,"data_list");
 //BA.debugLineNum = 280;BA.debugLine="PanelHeight=12%y";
_panelheight = anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (12),mostCurrent.activityBA);
 //BA.debugLineNum = 282;BA.debugLine="Panel0.AddView(ImageView1,1%x,PanelTop,71%x,Pane";
_panel0.AddView((android.view.View)(_imageview1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),_paneltop,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 283;BA.debugLine="ImageView1.Tag=i&\"1\"";
_imageview1.setTag((Object)(BA.NumberToString(_i)+"1"));
 //BA.debugLineNum = 284;BA.debugLine="ImageView1.Bitmap=Bitmap1";
_imageview1.setBitmap((android.graphics.Bitmap)(_bitmap1.getObject()));
 //BA.debugLineNum = 285;BA.debugLine="ImageView1.Gravity=Gravity.fill";
_imageview1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 287;BA.debugLine="Dim Label1, Label2 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 288;BA.debugLine="Label1.Initialize(\"\")";
_label1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 289;BA.debugLine="Label2.Initialize(\"\")";
_label2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 290;BA.debugLine="Panel0.AddView(Label1,1%x,PanelTop-2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 291;BA.debugLine="Panel0.AddView(Label2,1%x,PanelTop+2%y,71%x,Pane";
_panel0.AddView((android.view.View)(_label2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) (_paneltop+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (71),mostCurrent.activityBA),_panelheight);
 //BA.debugLineNum = 293;BA.debugLine="Label1.TextColor= Colors.black";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 294;BA.debugLine="Label1.TextSize= 17";
_label1.setTextSize((float) (17));
 //BA.debugLineNum = 295;BA.debugLine="Label1.Gravity=Gravity.CENTER";
_label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 296;BA.debugLine="Label1.Color=Colors.argb(0,0,0,0)";
_label1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 297;BA.debugLine="Label1.Text=fullN_llist.Get(i) 'set data from li";
_label1.setText(_fulln_llist.Get(_i));
 //BA.debugLineNum = 299;BA.debugLine="Label2.TextColor= Colors.black";
_label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 300;BA.debugLine="Label2.TextSize= 15";
_label2.setTextSize((float) (15));
 //BA.debugLineNum = 301;BA.debugLine="Label2.Gravity=Gravity.CENTER";
_label2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 302;BA.debugLine="Label2.Color=Colors.argb(0,0,0,0)";
_label2.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 303;BA.debugLine="Label2.Text=location_list.Get(i) 'set data from";
_label2.setText(_location_list.Get(_i));
 //BA.debugLineNum = 306;BA.debugLine="If i > id_list.size-1 Then i = id_list.size-1";
if (_i>_id_list.getSize()-1) { 
_i = (int) (_id_list.getSize()-1);};
 //BA.debugLineNum = 309;BA.debugLine="PanelTop=PanelTop+PanelHeight";
_paneltop = (int) (_paneltop+_panelheight);
 }
};
 //BA.debugLineNum = 311;BA.debugLine="Panel0.Height=PanelTop";
_panel0.setHeight(_paneltop);
 //BA.debugLineNum = 313;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 315;BA.debugLine="dialog_panel.AddView(scrolllista,1%x,1%y,75%x,78%";
mostCurrent._dialog_panel.AddView((android.view.View)(mostCurrent._scrolllista.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (78),mostCurrent.activityBA));
 //BA.debugLineNum = 316;BA.debugLine="cd.AddView(dialog_panel,75%x,78%y)";
_cd.AddView((android.view.View)(mostCurrent._dialog_panel.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (78),mostCurrent.activityBA));
 //BA.debugLineNum = 318;BA.debugLine="cd.Show(\"List of people\", \"CANCEL\", \"VIEW\", \"\", N";
_cd.Show("List of people","CANCEL","VIEW","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 320;BA.debugLine="End Sub";
return "";
}
public static String  _load_list() throws Exception{
 //BA.debugLineNum = 103;BA.debugLine="Sub load_list";
 //BA.debugLineNum = 104;BA.debugLine="list_bloodgroup.Initialize";
_list_bloodgroup.Initialize();
 //BA.debugLineNum = 105;BA.debugLine="list_bloodgroup.Add(\"A\")";
_list_bloodgroup.Add((Object)("A"));
 //BA.debugLineNum = 106;BA.debugLine="list_bloodgroup.Add(\"B\")";
_list_bloodgroup.Add((Object)("B"));
 //BA.debugLineNum = 107;BA.debugLine="list_bloodgroup.Add(\"O\")";
_list_bloodgroup.Add((Object)("O"));
 //BA.debugLineNum = 108;BA.debugLine="list_bloodgroup.Add(\"AB\")";
_list_bloodgroup.Add((Object)("AB"));
 //BA.debugLineNum = 109;BA.debugLine="list_bloodgroup.Add(\"A+\")";
_list_bloodgroup.Add((Object)("A+"));
 //BA.debugLineNum = 110;BA.debugLine="search_spiner.AddAll(list_bloodgroup)";
mostCurrent._search_spiner.AddAll(_list_bloodgroup);
 //BA.debugLineNum = 111;BA.debugLine="spin_item_click = \"A\";";
mostCurrent._spin_item_click = "A";
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return "";
}
public static String  _lv_itemclick(int _position,Object _value) throws Exception{
b4a.example.calculations _calc = null;
 //BA.debugLineNum = 227;BA.debugLine="Sub lv_ItemClick (Position As Int, Value As Object";
 //BA.debugLineNum = 228;BA.debugLine="Dim calc As calculations";
_calc = new b4a.example.calculations();
 //BA.debugLineNum = 229;BA.debugLine="calc.users_id = id_list.Get(Position)";
_calc._users_id = (int)(BA.ObjectToNumber(_id_list.Get(_position)));
 //BA.debugLineNum = 232;BA.debugLine="End Sub";
return "";
}
public static String  _map_shows_pagefinished(String _url) throws Exception{
 //BA.debugLineNum = 162;BA.debugLine="Sub map_shows_PageFinished (Url As String)";
 //BA.debugLineNum = 163;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 164;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 17;BA.debugLine="Dim is_complete As Int : is_complete = 0";
_is_complete = 0;
 //BA.debugLineNum = 17;BA.debugLine="Dim is_complete As Int : is_complete = 0";
_is_complete = (int) (0);
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
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
 //BA.debugLineNum = 333;BA.debugLine="Sub reading_txt";
 //BA.debugLineNum = 334;BA.debugLine="id_list.Initialize";
_id_list.Initialize();
 //BA.debugLineNum = 335;BA.debugLine="fullN_llist.Initialize";
_fulln_llist.Initialize();
 //BA.debugLineNum = 336;BA.debugLine="location_list.Initialize";
_location_list.Initialize();
 //BA.debugLineNum = 337;BA.debugLine="lat_list.Initialize";
_lat_list.Initialize();
 //BA.debugLineNum = 338;BA.debugLine="lng_list.Initialize";
_lng_list.Initialize();
 //BA.debugLineNum = 340;BA.debugLine="Dim TextReader_id As TextReader";
_textreader_id = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 341;BA.debugLine="TextReader_id.Initialize(File.OpenInput(File.D";
_textreader_id.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_id.txt").getObject()));
 //BA.debugLineNum = 342;BA.debugLine="Dim line_id As String";
_line_id = "";
 //BA.debugLineNum = 343;BA.debugLine="line_id = TextReader_id.ReadLine";
_line_id = _textreader_id.ReadLine();
 //BA.debugLineNum = 344;BA.debugLine="Do While line_id <> Null";
while (_line_id!= null) {
 //BA.debugLineNum = 346;BA.debugLine="id_list.Add(line_id)";
_id_list.Add((Object)(_line_id));
 //BA.debugLineNum = 347;BA.debugLine="line_id = TextReader_id.ReadLine";
_line_id = _textreader_id.ReadLine();
 }
;
 //BA.debugLineNum = 349;BA.debugLine="TextReader_id.Close";
_textreader_id.Close();
 //BA.debugLineNum = 351;BA.debugLine="Dim TextReader_fullN As TextReader";
_textreader_fulln = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 352;BA.debugLine="TextReader_fullN.Initialize(File.OpenInput(Fil";
_textreader_fulln.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_fullN.txt").getObject()));
 //BA.debugLineNum = 353;BA.debugLine="Dim line_fullN As String";
_line_fulln = "";
 //BA.debugLineNum = 354;BA.debugLine="line_fullN = TextReader_fullN.ReadLine";
_line_fulln = _textreader_fulln.ReadLine();
 //BA.debugLineNum = 355;BA.debugLine="Do While line_fullN <> Null";
while (_line_fulln!= null) {
 //BA.debugLineNum = 357;BA.debugLine="fullN_llist.Add(line_fullN)";
_fulln_llist.Add((Object)(_line_fulln));
 //BA.debugLineNum = 358;BA.debugLine="line_fullN = TextReader_fullN.ReadLine";
_line_fulln = _textreader_fulln.ReadLine();
 }
;
 //BA.debugLineNum = 360;BA.debugLine="TextReader_fullN.Close";
_textreader_fulln.Close();
 //BA.debugLineNum = 362;BA.debugLine="Dim TextReader_location As TextReader";
_textreader_location = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 363;BA.debugLine="TextReader_location.Initialize(File.OpenInput(";
_textreader_location.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_location.txt").getObject()));
 //BA.debugLineNum = 364;BA.debugLine="Dim line_location As String";
_line_location = "";
 //BA.debugLineNum = 365;BA.debugLine="line_location = TextReader_location.ReadLine";
_line_location = _textreader_location.ReadLine();
 //BA.debugLineNum = 366;BA.debugLine="Do While line_location <> Null";
while (_line_location!= null) {
 //BA.debugLineNum = 368;BA.debugLine="location_list.Add(line_location)";
_location_list.Add((Object)(_line_location));
 //BA.debugLineNum = 369;BA.debugLine="line_location = TextReader_location.ReadLi";
_line_location = _textreader_location.ReadLine();
 }
;
 //BA.debugLineNum = 371;BA.debugLine="TextReader_location.Close";
_textreader_location.Close();
 //BA.debugLineNum = 373;BA.debugLine="Dim TextReader_lat As TextReader";
_textreader_lat = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 374;BA.debugLine="TextReader_lat.Initialize(File.OpenInput(File.";
_textreader_lat.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lat.txt").getObject()));
 //BA.debugLineNum = 375;BA.debugLine="Dim line_lat As String";
_line_lat = "";
 //BA.debugLineNum = 376;BA.debugLine="line_lat = TextReader_lat.ReadLine";
_line_lat = _textreader_lat.ReadLine();
 //BA.debugLineNum = 377;BA.debugLine="Do While line_lat <> Null";
while (_line_lat!= null) {
 //BA.debugLineNum = 378;BA.debugLine="lat_list.Add(line_lat)";
_lat_list.Add((Object)(_line_lat));
 //BA.debugLineNum = 379;BA.debugLine="line_lat = TextReader_lat.ReadLine";
_line_lat = _textreader_lat.ReadLine();
 }
;
 //BA.debugLineNum = 381;BA.debugLine="TextReader_lat.Close";
_textreader_lat.Close();
 //BA.debugLineNum = 383;BA.debugLine="Dim TextReader_lng As TextReader";
_textreader_lng = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 384;BA.debugLine="TextReader_lng.Initialize(File.OpenInput(File.";
_textreader_lng.Initialize((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"data_query_lng.txt").getObject()));
 //BA.debugLineNum = 385;BA.debugLine="Dim line_lng As String";
_line_lng = "";
 //BA.debugLineNum = 386;BA.debugLine="line_lng = TextReader_lng.ReadLine";
_line_lng = _textreader_lng.ReadLine();
 //BA.debugLineNum = 387;BA.debugLine="Do While line_lng <> Null";
while (_line_lng!= null) {
 //BA.debugLineNum = 388;BA.debugLine="lng_list.Add(line_lng)";
_lng_list.Add((Object)(_line_lng));
 //BA.debugLineNum = 389;BA.debugLine="line_lng = TextReader_lng.ReadLine";
_line_lng = _textreader_lng.ReadLine();
 }
;
 //BA.debugLineNum = 391;BA.debugLine="TextReader_lng.Close";
_textreader_lng.Close();
 //BA.debugLineNum = 395;BA.debugLine="End Sub";
return "";
}
public static String  _search_btn_click() throws Exception{
b4a.example.calculations _url_back = null;
String _url_id = "";
String _full_name = "";
String _location = "";
String _lat = "";
String _lng = "";
 //BA.debugLineNum = 122;BA.debugLine="Sub search_btn_Click";
 //BA.debugLineNum = 123;BA.debugLine="ProgressDialogShow2(\"please wait.!!\",False)";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,"please wait.!!",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 125;BA.debugLine="Dim url_back As calculations";
_url_back = new b4a.example.calculations();
 //BA.debugLineNum = 126;BA.debugLine="Dim url_id,full_name,location,lat,lng As String";
_url_id = "";
_full_name = "";
_location = "";
_lat = "";
_lng = "";
 //BA.debugLineNum = 127;BA.debugLine="url_back.Initialize";
_url_back._initialize(processBA);
 //BA.debugLineNum = 128;BA.debugLine="url_id = url_back.php_email_url(\"/bloodlifePHP/se";
_url_id = _url_back._php_email_url("/bloodlifePHP/search_blood_id.php");
 //BA.debugLineNum = 129;BA.debugLine="full_name = url_back.php_email_url(\"/bloodlifePHP";
_full_name = _url_back._php_email_url("/bloodlifePHP/search_blood_fullN.php");
 //BA.debugLineNum = 130;BA.debugLine="location = url_back.php_email_url(\"/bloodlifePHP/";
_location = _url_back._php_email_url("/bloodlifePHP/search_blood_location.php");
 //BA.debugLineNum = 131;BA.debugLine="lat = url_back.php_email_url(\"/bloodlifePHP/searc";
_lat = _url_back._php_email_url("/bloodlifePHP/search_blood_lat.php");
 //BA.debugLineNum = 132;BA.debugLine="lng = url_back.php_email_url(\"/bloodlifePHP/searc";
_lng = _url_back._php_email_url("/bloodlifePHP/search_blood_long.php");
 //BA.debugLineNum = 135;BA.debugLine="data_query_id.Download2(url_id,Array As String(\"i";
mostCurrent._data_query_id._download2(_url_id,new String[]{"id","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 136;BA.debugLine="data_query_fullN.Download2(full_name,Array As Str";
mostCurrent._data_query_fulln._download2(_full_name,new String[]{"full_name","SELECT full_name FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 137;BA.debugLine="data_query_location.Download2(location,Array As S";
mostCurrent._data_query_location._download2(_location,new String[]{"location","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 138;BA.debugLine="query_lat.Download2(lat,Array As String(\"lat\",\"SE";
mostCurrent._query_lat._download2(_lat,new String[]{"lat","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 139;BA.debugLine="query_lng.Download2(lng,Array As String(\"long\",\"S";
mostCurrent._query_lng._download2(_lng,new String[]{"long","SELECT * FROM `bloodlife_db`.`person_info` where `blood_type`='"+mostCurrent._spin_item_click+"';"});
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public static String  _search_spiner_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 224;BA.debugLine="Sub search_spiner_ItemClick (Position As Int, Valu";
 //BA.debugLineNum = 225;BA.debugLine="spin_item_click = search_spiner.GetItem(Position)";
mostCurrent._spin_item_click = mostCurrent._search_spiner.GetItem(_position).trim();
 //BA.debugLineNum = 226;BA.debugLine="End Sub";
return "";
}
}
