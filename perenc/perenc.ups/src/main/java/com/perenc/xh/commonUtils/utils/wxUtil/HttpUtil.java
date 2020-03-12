package com.perenc.xh.commonUtils.utils.wxUtil;


import com.perenc.xh.lsp.service.wxService.impl.TrustManagerImpl;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 后台发起请求的工具
 */
public class HttpUtil {

	private static Logger logger = Logger.getLogger(HttpUtil.class);

    public static BufferedImage getRequestImage(String url){

		HttpClient client = new HttpClient();
		HttpConnectionManager hcm = client.getHttpConnectionManager();

		HttpConnectionManagerParams params = hcm.getParams();
		params.setConnectionTimeout(50000);
		params.setSoTimeout(45000);
		hcm.setParams(params);

		client.setHttpConnectionManager(hcm);

		GetMethod method = new GetMethod(url);
		method.addRequestHeader("Accept-Language", "zh-cn");
		method.addRequestHeader("Connection", "Keep-Alive");
		method.addRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; InfoPath.2; .NET4.0C; .NET4.0E)");

		//结果
		BufferedImage buffImg = null;
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode == 200) {

				//保存图片
				InputStream is = method.getResponseBodyAsStream();
				ImageInputStream iis = ImageIO.createImageInputStream(is);

				buffImg = ImageIO.read(iis);
			}
		} catch (Exception e) {
			logger.error("图像地址"+url+"不存在："+e.getMessage());
		} finally {
			method.releaseConnection();
		}

		return buffImg;
	}
	/**
	 * 发送get请求
	 * @param url
	 */
	public static String sendGet(String url) throws Exception{
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();// 打开连接
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw e;
		} finally {//关闭输入流
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 发送post请求
	 * @param url
	 * @param param
	 */
	public static String sendPost(String url, String param) throws Exception {
		String result = "";
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 读取响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw e;
		} finally {// 关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 安全协议请求
	 * SSL(Secure Sockets Layer 安全套接层),是为网络通信提供安全及数据完整性的一种安全协议
	 * @param reqUrl
	 * @param method
	 * @param outputStr
	 */
	public static String SSLRequest(String reqUrl, String method, String outputStr) throws Exception {
		StringBuffer buffer = new StringBuffer();
		TrustManager[] tm = { new TrustManagerImpl() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new SecureRandom());
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		URL url = new URL(reqUrl);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setSSLSocketFactory(ssf);
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		httpUrlConn.setRequestMethod(method);
		if ("GET".equalsIgnoreCase(method)) {
			httpUrlConn.connect();
		}
		if (null != outputStr) {
			OutputStream outputStream = httpUrlConn.getOutputStream();

			outputStream.write(outputStr.getBytes("UTF-8"));
			outputStream.close();
		}
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		return buffer.toString();
	}
	public static String sendGet1 (String url) throws Exception {
		return get(url);
	}
	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		} finally {
			if (httpclient != null) {
				httpclient.close();
			}
		}
	}

	/**
	 * 将参数转换成string
	 * 
	 * @param paramMap
	 * @param requestEncoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String getMapParamsToStr(Map paramMap, String requestEncoding) throws IOException {
		StringBuffer params = new StringBuffer();
		// 设置边界
		for (Iterator iter = paramMap.entrySet().iterator(); iter.hasNext();) {
			Entry element = (Entry) iter.next();
			params.append(element.getKey().toString());
			params.append("=");
			params.append(URLEncoder.encode(element.getValue().toString(), requestEncoding));
			params.append("&");
		}

		if (params.length() > 0) {
			params = params.deleteCharAt(params.length() - 1);
		}

		return params.toString();
	}

//	public static String doJsonPost(String reqUrl, Map parameters, String jsonData) {
//		HttpURLConnection url_con = null;
//		String responseContent = null;
//		try {
//			String params = getMapParamsToStr(parameters, HttpRequestProxy.requestEncoding);
//
//			URL url = new URL(reqUrl + "&" + params);
//			url_con = (HttpURLConnection) url.openConnection();
//			url_con.setRequestMethod("POST");
//			System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
//			System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
//			url_con.setDoOutput(true);
//			url_con.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
//			url_con.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
//
//			byte[] b = jsonData.toString().getBytes();
//			url_con.getOutputStream().write(b, 0, b.length);
//			url_con.getOutputStream().flush();
//			url_con.getOutputStream().close();
//
//			InputStream in = url_con.getInputStream();
//			BufferedReader rd = new BufferedReader(new InputStreamReader(in, HttpRequestProxy.requestEncoding));
//			String tempLine = rd.readLine();
//			StringBuffer tempStr = new StringBuffer();
//			String crlf = System.getProperty("line.separator");
//			while (tempLine != null) {
//				tempStr.append(tempLine);
//				tempStr.append(crlf);
//				tempLine = rd.readLine();
//			}
//			responseContent = tempStr.toString();
//			rd.close();
//			in.close();
//		} catch (IOException e) {
//			logger.error("网络故障", e);
//		} finally {
//			if (url_con != null) {
//				url_con.disconnect();
//			}
//		}
//		return responseContent;
//	}

	public static String post(String url, Map<String, String> dataMap) throws ClientProtocolException, IOException {
		// 和GET方式一样，先将参数放入List
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		if (dataMap != null) {
			Iterator<String> keys = dataMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = dataMap.get(key);
				params.add(new BasicNameValuePair(key, value));
			}
		}

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response = null;
		try {
			HttpPost postMethod = new HttpPost(url);
			postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 将参数填入POST
																				// Entity中

			response = httpclient.execute(postMethod); // 执行POST方法
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		} finally {
			if (httpclient != null) {
				httpclient.close();
			}
		}
	}

	public static String post(String url, String jsonStr) throws Exception {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		} finally {
			if (httpclient != null) {
				httpclient.close();
			}
		}

	}

	/**
	 * 发送请求
	 * */
	public static String ssl(String url,String data,String mchId,String appId){
		logger.info("---------商户----------"+mchId);
		StringBuffer message = new StringBuffer();
		try {
			KeyStore keyStore  = KeyStore.getInstance("PKCS12");
			String certFilePath = "F:\\apiclient_cert.p12";
			// linux下
			if ("/".equals(File.separator)) {
				certFilePath = "/usr/local/nginx/html/"+appId+"/apiclient_cert.p12";
//				certFilePath = "/usr/local/nginx/html/apiclient_cert.p12";
			}
			FileInputStream instream = new FileInputStream(new File(certFilePath));
			keyStore.load(instream, mchId.toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpPost httpost = new HttpPost(url);
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept", "*/*");
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpost.addHeader("Host", "api.mch.weixin.qq.com");
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpost.addHeader("Cache-Control", "max-age=0");
			httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			System.out.println("executing request" + httpost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: " + entity.getContentLength());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
					String text;
					while ((text = bufferedReader.readLine()) != null) {
						message.append(text);
					}
				}
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				response.close();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return message.toString();
	}

}
