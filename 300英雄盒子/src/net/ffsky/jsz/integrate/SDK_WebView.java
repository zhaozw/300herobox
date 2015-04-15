package net.ffsky.jsz.integrate;

import io.dcloud.EntryProxy;
import io.dcloud.DHInterface.IApp;
import io.dcloud.DHInterface.IApp.ConfigProperty;
import io.dcloud.DHInterface.ICore;
import io.dcloud.DHInterface.ICore.ICoreStatusListener;
import io.dcloud.DHInterface.IFeature;
import io.dcloud.DHInterface.IMgr;
import io.dcloud.DHInterface.IOnCreateSplashView;
import io.dcloud.DHInterface.ISysEventListener.SysEventType;
import io.dcloud.DHInterface.IWebview;
import io.dcloud.DHInterface.SplashView;
import io.dcloud.adapter.util.PlatformUtil;
import io.dcloud.sdk.SDK;
import io.dcloud.util.BaseInfo;
import io.dcloud.util.IOUtil;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;

/**
 * 本demo为以webview控件方式集成pandora sdk，包含如何扩展Feature,如
 * {@link WebViewMode_FeatureImpl}
 * 
 * @author yanglei
 *
 */
public class SDK_WebView extends Activity {

	boolean doHardAcc = true;
	EntryProxy mEntryProxy = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (mEntryProxy == null) {
			FrameLayout fl = new FrameLayout(this);
			WebviewMode wm = new WebviewMode(fl);
			mEntryProxy = EntryProxy.init(this, wm);
			mEntryProxy.onCreate(savedInstanceState,
					SDK.IntegratedMode.WEBVIEW, wm);
			setContentView(fl);
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
		if (intent.getFlags() != 0x10600000) {// 非点击icon调用activity时才调用newintent事件
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
		boolean _ret = mEntryProxy.onExecute(SysEventType.OnKeyDown,
				new Object[] { keyCode, event });
		return _ret ? _ret : super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean _ret = mEntryProxy.onExecute(SysEventType.OnKeyUp,
				new Object[] { keyCode, event });
		return _ret ? _ret : super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		boolean _ret = mEntryProxy.onExecute(SysEventType.onKeyLongPress,
				new Object[] { keyCode, event });
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
		mEntryProxy.onExecute(SysEventType.OnActivityResult, new Object[] {
				requestCode, resultCode, data });
	}

}

class WebviewMode implements ICoreStatusListener, IOnCreateSplashView {

	FrameLayout mRootView = null;

	public WebviewMode(FrameLayout rootView) {
		mRootView = rootView;
	}

	@Override
	public void onCoreInitEnd(ICore coreHandler) {
		// SDK.initSDK(coreHandler);
		// 注册扩展的Feature
		// 1，featureName 为特征名称
		// 2, className 为处理扩展Feature的接收类全名称
		// 3, content 为扩展Feature而创建的js代码，代码中必须使用
		// plus.bridge.execSync(featureName,actionName,[arguments])或plus.bridge.exec(featureName,actionName,[arguments])与native层进行数据交互
		SDK.registerJsApi(featureName, className, content);
		// 创建默认webapp，赋值appid
		String launchPage = "file:///android_asset/apps/300herobox/www/index.html";

		IWebview webview = SDK.createWebview(launchPage);// 获取webapp的首页面
		final WebView web = webview.obtainWebview();// 获取Webview
		web.setOnKeyListener(new OnKeyListener() {// 监听webview的页面的返回键
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (web.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
					web.loadUrl("javascript:"
							+ "var ret = window.plus.T.test();alert('结果为：'+ret);");
					web.goBack();
					return true;
				}
				return false;
			}
		});
		SDK.attach(mRootView, webview);// 附着webview到父容器mRootView
	}

	@Override
	public void onCoreReady(ICore coreHandler) {
		try {
			SDK.initSDK(coreHandler);
			SDK.requestFeature(IFeature.F_UI, null, false);
			SDK.loadCustomPath(new String(IOUtil.getBytes(PlatformUtil
					.getResInputStream("path.properties"))));// 加载路径配置文件
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public SplashView onCreateSplashView(Context pContextWrapper, IMgr pMgr,
			IApp pAppHandler) {
		return null;
	}

	String featureName = "T";
	String className = "net.ffsky.jsz.integrate.WebViewMode_FeatureImpl";
	String content = "(function(plus){function test(){return plus.bridge.execSync('T','test',[arguments]);}plus.T = {test:test};})(window.plus);";

	@Override
	public boolean onCoreStop() {
		// TODO Auto-generated method stub
		return false;
	}
}
