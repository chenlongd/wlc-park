package com.perenc.xh.lsp.entity.wxConfig;

/**
 * 配置项
 * 
 * @author Edward
 */
public class Cfg {

	//姊妹商城
	private static String wxAppId = "wxa7b8b5973c288571";

	//万绿城地产
//	private static String wxAppId = "wxe95ff446872e9f8a";

	//姊妹商城
	private static String wxAppSecret = "342ea82e873014594f5f9a63c46473b1";

	//万绿城地产
//	private static String wxAppSecret = "d15bc553168a4f56d8e787911a1e3560";

	//姊妹商城
	private static String appRoot = "http://www.wlczmsc.com/xhwechat/";

	//公众号名称
	private static String appName = "姊妹商城";

	private static App app = null;

	private static String storeId = "5d3fa734e4b08372ad0e5c2d";

	public static App getApp() {
		return app;
	}


//	public static void setApp(App app) {
//		Cfg.app = app;
//		if(app!=null){
//			Cfg.appIndexId = app.getId();
//			Cfg.wxAppId = app.getAppId();
//			Cfg.wxAppSecret = app.getAppSecret();
//
//			Cfg.mchId = app.getAppMchId();
//			Cfg.mchKey = app.getAppMchKey();
//			Cfg.appRoot = app.getAppRoot();
//
//			Cfg.appName = app.getName();
//		}
//	}


	public static String getWxappid() {
		return wxAppId;
	}


	public static String getWxappsecret() {
		return wxAppSecret;
	}


	public static String getApproot() {
		return appRoot;
	}


	public static String getAppname() {
		return appName;
	}

	public static String getStoreId() {
		return storeId;
	}
}
