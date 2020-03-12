package com.perenc.xh.commonUtils.utils.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

/**
 * 公司名称：贵阳赢销截拳道网络科技有限公司
 * 
 * 功能描述：httpclient 连接池初始化
 * 
 * 创建人：DengXin
 * 
 * 创建时间：2015-12-29 下午8:06:24
 */
public class PoolsHttpClientManageInit {

	/**
	 * 日志控件
	 */
	private static final Logger logger = Logger.getLogger(PoolsHttpClientManageInit.class);

	private int maxTotal;

	private int defaultMaxPerRoute;

	private PoolingHttpClientConnectionManager pools;

	private SSLConnectionSocketFactoryBuild build;

	private int socketTimeout = 0;

	private int connectTimeout = 0;

	private int connectionRequestTimeout = 0;

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public void setBuild(SSLConnectionSocketFactoryBuild build) {
		this.build = build;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public PoolsHttpClientManageInit() {
	}

	/**
	 * 连接数初始化
	 */
	public void init() {
		if (build == null) {
			try {
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
						.<ConnectionSocketFactory> create()
						.register("http", PlainConnectionSocketFactory.getSocketFactory())
						.register("https", build.build()).build();
				pools = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			} catch (Exception e) {
				logger.debug("连接池构建错误：" + e.getMessage());
				pools = new PoolingHttpClientConnectionManager();
			}
		} else {
			pools = new PoolingHttpClientConnectionManager();
		}

		// 最大连接数
		pools.setMaxTotal(maxTotal);

		// 基础的连接数
		pools.setDefaultMaxPerRoute(defaultMaxPerRoute);

		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketTimeout).build();

		pools.setDefaultSocketConfig(socketConfig);

		logger.debug("HttpClient pools init");
	}

	/**
	 * 获取连接数
	 * 
	 * @return
	 */
	public CloseableHttpClient getConnection() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
				.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pools)
				.setDefaultRequestConfig(requestConfig).build();

		if (pools != null && pools.getTotalStats() != null) {
			logger.info("now client pool " + pools.getTotalStats().toString());
		}
		return httpClient;
	}

	/**
	 * 返回连接池
	 * 
	 * @return
	 */
	public PoolingHttpClientConnectionManager getInstancePools() {
		return pools;
	}
}