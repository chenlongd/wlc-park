package com.perenc.xh.commonUtils.utils.http;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

/**
 * 公司名称：贵阳赢销截拳道网络科技有限公司
 * 
 * 功能描述：SSL 连接工厂构建接口
 * 
 * 创建人：DengXin
 * 
 * 创建时间：2016-1-26 上午10:16:52
 */
public interface SSLConnectionSocketFactoryBuild {

	/**
	 * 构建SSL连接工厂
	 * 
	 * @return
	 */
	public SSLConnectionSocketFactory build() throws Exception;
}
