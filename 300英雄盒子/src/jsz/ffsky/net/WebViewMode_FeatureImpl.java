package jsz.ffsky.net;

import io.dcloud.DHInterface.AbsMgr;
import io.dcloud.DHInterface.IFeature;
import io.dcloud.DHInterface.IWebview;

public class WebViewMode_FeatureImpl implements IFeature{

	@Override
	public String execute(IWebview pWebViewImpl, String pActionName,
			String[] pJsArgs) {
		if("test".equals(pActionName)){//监听js层调用plus.T.test 函数
			return "'xxxxxxxxxx'";
		}
		return null;
	}

	@Override
	public void init(AbsMgr pFeatureMgr, String pFeatureName) {
		//初始化Feature
	}

	@Override
	public void dispose(String pAppid) {
		
	}

}
