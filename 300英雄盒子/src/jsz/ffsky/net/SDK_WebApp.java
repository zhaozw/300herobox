package jsz.ffsky.net;

import io.dcloud.EntryProxy;
import io.dcloud.DHInterface.IApp;
import io.dcloud.DHInterface.IApp.ConfigProperty;
import io.dcloud.DHInterface.ICore;
import io.dcloud.DHInterface.ICore.ICoreStatusListener;
import io.dcloud.DHInterface.IFeature;
import io.dcloud.DHInterface.IMgr;
import io.dcloud.DHInterface.IOnCreateSplashView;
import io.dcloud.DHInterface.ISysEventListener.SysEventType;
import io.dcloud.DHInterface.SplashView;
import io.dcloud.sdk.SDK;
import io.dcloud.splash.SplashActivity;
import io.dcloud.util.BaseInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.widget.FrameLayout;


public class SDK_WebApp extends Activity {

	boolean doHardAcc = true;
	EntryProxy mEntryProxy = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(mEntryProxy == null){
        	Intent i = new Intent();
			i.setClassName(this, SplashActivity.class.getName());
			startActivity(i);
			
        	WebappMode wm = new WebappMode();
        	 mEntryProxy = EntryProxy.init(this,wm);
        	 FrameLayout f = new FrameLayout(this);        	
    	     mEntryProxy.onCreate(savedInstanceState, f, SDK.IntegratedMode.WEBAPP,wm);
    	     setContentView(f);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mEntryProxy.onExecute(SysEventType.OnCreateOptionMenu, menu);
    }
	
	@Override
	public void onPause() {
		super.onPause();
		mEntryProxy.onPause();
	}
	@Override
	public void onResume() {
		super.onResume();
		mEntryProxy.onResume();
	}
	
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent.getFlags() != 0x10600000){//非点击icon调用activity时才调用newintent事件
			mEntryProxy.onNewIntent(intent);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mEntryProxy.onStop();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean _ret = mEntryProxy.onExecute(SysEventType.OnKeyDown, new Object[]{keyCode,event});
		return _ret ? _ret : super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean _ret = mEntryProxy.onExecute(SysEventType.OnKeyUp, new Object[]{keyCode,event});
		return _ret ? _ret : super.onKeyUp(keyCode, event);
	}
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		boolean _ret = mEntryProxy.onExecute(SysEventType.onKeyLongPress, new Object[]{keyCode,event});
		return _ret ? _ret : super.onKeyLongPress(keyCode, event);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			int temp = this.getResources().getConfiguration().orientation;
			if (mEntryProxy != null) {
				mEntryProxy.onConfigurationChanged(temp);
			}
			super.onConfigurationChanged(newConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mEntryProxy.onExecute(SysEventType.OnActivityResult, new Object[]{requestCode,resultCode,data});
	}
	
}

 class WebappMode implements ICoreStatusListener,IOnCreateSplashView {
	@Override
	public void onCoreInitEnd(ICore coreHandler) {
		
		//创建默认webapp，赋值appid
		String appid = "300herobox";
		String appBasePath = BaseInfo.sBaseResAppsPath + "300herobox/";//获取runtime运行应用的文件系统根路径
		String launchPage = "index.html";//保证存在此文件
		launchPage = "file:///android_asset/apps/300herobox/www/index.html";//保证存在此文件
		IApp app = SDK.createUnstrictWebApp(appid,appBasePath, launchPage,IApp.APP_RUNNING_MODE);//创建webapp句柄，指定首页面地址、运行模式为文件系统
		app.addFeaturePermission(IFeature.F_RUNTIME);//添加runtime权限
		app.addFeaturePermission(IFeature.F_UI); //添加ui权限
		app.addFeaturePermission(IFeature.F_BARCODE); //添加ui权限
		app.setConfigProperty(ConfigProperty.CONFIG_NAME, "测试以webapp方式集成sdk");
		SDK.startWebApp(app,null);//启动webapp
	}
	
	@Override
	public void onCoreReady(ICore coreHandler) {
		//加载自定runtime使用的路径
		SDK.initSDK(coreHandler);
		SDK.requestAllFeature();
//		try {
//			InputStream is = PlatformUtil.getResInputStream("path.properties");//加载assets/path.properties文件内容
//			String paths = new String(IOUtil.getBytes(is));//转化为字符串
//			SDK.loadCustomPath(paths);//解析加载路径配置文件
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public SplashView onCreateSplashView(Context pContextWrapper, IMgr pMgr,IApp pAppHandler) {
		//自定义splash时可以实现此方法逻辑
		return null;
	}

	@Override
	public boolean onCoreStop() {
		return false;
	}
	
}
