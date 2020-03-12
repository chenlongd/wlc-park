package com.perenc.xh.common.loginFilter.cache;


import com.perenc.xh.commonUtils.utils.wxUtil.HttpUtil;
import com.perenc.xh.commonUtils.utils.wxUtil.JsonUtil;
import com.perenc.xh.lsp.entity.wxConfig.App;

import org.apache.log4j.Logger;

public class Cache extends BaseCache {
	
	static Logger logger = Logger.getLogger(Cache.class);
	
	//-----单例模式
	private static Cache instance = null;
	private Cache() {
	}
	public static Cache getInstance() {
		if(instance == null) {
			instance = new Cache();
		}
		return instance;
	}//-----end
	
 
	/**
	 * 取微信令牌
	 * @return
	 */
	public String getWxToken(App wx) throws Exception {

		String appId = wx.getAppId();
		String appSecret = wx.getAppSecret();
		
		String key = "wxToken-"+appId;
		if(super.isExp(key)) {
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"+"&appid="+appId+"&secret="+appSecret;
			logger.info("------------请求token的URL-----------"+url);
			String res = HttpUtil.sendGet(url);
			logger.info("------------取微信token返回值-----------"+res);
			String wxToken = (String) JsonUtil.parseMap(res).get("access_token");
			logger.info("取微信token:"+wxToken);

//			try {
//				//保存token到数据库，供后台程序使用
//				DebugService debugService = SpringContextUtils.getBean(DebugService.class);
//				debugService.saveWxToken_tx(wxToken, Cfg.getAppIndexid());
//			} catch (Exception e) {
//				logger.error("保存微信token到数据库时发生异常", e);
//			}

			if(wxToken == null) {
				logger.error("获取token失败:"+url);
			}
			super.set(key, wxToken, 3600L);
		}
		logger.info("------------super.get(key)-----------"+super.get(key)+"");
		return super.get(key)+"";
	}

	/**
	 * 取微信令牌信息
	 * @return
	 */
	public String getWxTokenMsg(String appId,String appSecret) throws Exception {

		String key = "wxToken-"+appId;
		if(super.isExp(key)) {
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"+"&appid="+appId+"&secret="+appSecret;
			logger.info("------------请求token的URL-----------"+url);
			String res = HttpUtil.sendGet(url);
			logger.info("------------取微信token返回值-----------"+res);
			String wxToken = (String) JsonUtil.parseMap(res).get("access_token");
			logger.info("取微信token:"+wxToken);
			if(wxToken == null) {
				logger.error("获取token失败:"+url);
			}
			super.set(key, wxToken, 3600L);
		}
		logger.info("------------super.get(key)-----------"+super.get(key)+"");
		return super.get(key)+"";
	}

//	public static void main(String[] args) {
//		String appId = Cfg.appId;
//		String appSecret = Cfg.appSecret;
//		
//		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"+"&appid="+appId+"&secret="+appSecret;
//		String res;
//		try {
//			res = HttpUtil.sendGet(url);
//			String wxToken = (String)JsonUtil.parseMap(res).get("access_token");
//			logger.info("取微信token:"+wxToken);
//		} catch (Exception e) { 
//			e.printStackTrace();
//		}
//		
//	}
	
	/**
	 * 使微信令牌与票据过期
	 */
	public void refreshWxTokenAndTicket(App wx) {
		String key = "wxToken-"+wx.getAppId();
		String key2 = "wxTicket-"+wx.getAppId();
		super.makeExp(key);
		super.makeExp(key2);
	}
	
	/**
	 * 取微信票据
	 * @return
	 */
	public String getWxTicket(App wx) throws Exception {
		String appId = wx.getAppId();
		String wxToken = getWxToken(wx);
		
		String key = "wxTicket-"+appId;
		if(super.isExp(key)) {
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+wxToken+"&type=jsapi";
			String res = HttpUtil.sendGet(url);
			String wxTicket = (String)JsonUtil.parseMap(res).get("ticket");
			logger.info("取微信ticket:"+wxTicket);
			if(wxTicket == null) {
				logger.error("获取ticket失败:"+url);
			}
			super.set(key, wxTicket, 3600L);
		}
		return super.get(key)+"";
	}
}
