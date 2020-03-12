package com.perenc.xh.commonUtils.utils.http;

import com.google.gson.Gson;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 公司名称：贵阳赢销网科技有限公司
 * 
 * 功能描述： httpClient 工具类
 * 
 * 创建人：DengXin
 * 
 * 创建时间：2015-9-14 下午4:55:49
 */
public class HttpCilentUtils {

	/**
	 * JSON 类型
	 */
	public static final String CONTENTTYPE_JSON = "application/json";

	/**
	 * XML 类型
	 */
	public static final String CONTENTTYPE_XML = "application/xml";

	/**
	 * 日志控件
	 */
	private static final Logger logger = Logger.getLogger(HttpCilentUtils.class);

	/**
	 * 连接池
	 */
	private static PoolsHttpClientManageInit pool;

	public static void setPool(PoolsHttpClientManageInit pool) {
		HttpCilentUtils.pool = pool;
	}

	/**
	 * http get request
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String httpGet(String url) throws IOException {

		// 创建连接
		CloseableHttpClient httpClient = pool.getConnection();
		HttpGet hget = new HttpGet(url);
		CloseableHttpResponse response = null;
		String result = "";
		try {
			// 执行请求
			response = httpClient.execute(hget);
			// 判断请求是否成功
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage());
			logger.info("http连接错误"+e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.info("http连接错误"+e.getMessage());
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return result;
	}

	/**
	 * http post request
	 * 
	 * @param url
	 * @param map
	 * @return
	 * @throws IOException
	 */
	public static String httpPost(String url, Map<String, Object> map) throws IOException {

		// 创建连接
		CloseableHttpClient httpClient = pool.getConnection();
//		CloseableHttpClient httpClient = HttpClients.createDefault();//本地测试用
		HttpPost httPost = new HttpPost(url);

		// 遍历post参数
		if (!MapUtils.isEmpty(map)) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			for (String key : map.keySet()) {
				StringBody comment = new StringBody((String) map.get(key), ContentType.APPLICATION_JSON);
				builder.addPart(key, comment);
			}
			HttpEntity entity = builder.build();
			httPost.setEntity(entity);
		}
		CloseableHttpResponse response = null;
		String result = "";
		try {
			response = httpClient.execute(httPost);
			// 判断请求是否成功
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return result;
	}

	/**
	 * post 请求方法（JSON or XML）
	 * 
	 * @param url
	 * @param data
	 * @param contentType
	 * @return
	 * @throws IOException
	 */
	public static String httpPost(String url, String data, String contentType) throws IOException {
		// 创建连接
		CloseableHttpClient httpClient = pool.getConnection();
		HttpPost httPost = new HttpPost(url);
		String result = "";
		if (data != null && !"".equals(data)) {
			StringEntity entity = new StringEntity(data, "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType(contentType);
			httPost.setEntity(entity);
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httPost);
				// 判断请求是否成功
				if (response.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(response.getEntity());
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			} finally {
				if (response != null) {
					response.close();
				}
			}
		}
		return result;
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
	 * post 请求方法（JSON or XML） 证书验证
	 * 
	 * @param url
	 * @param data
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String url, String data, String contentType, SSLConnectionSocketFactory sslsf)
			throws Exception {
		// 创建连接
		CloseableHttpClient httpClient = pool.getConnection();
		HttpPost httPost = new HttpPost(url);
		String result = "";
		if (data != null && !"".equals(data)) {
			StringEntity entity = new StringEntity(data, "UTF-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType(contentType);
			httPost.setEntity(entity);
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httPost);
				// 判断请求是否成功
				if (response.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(response.getEntity());
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} finally {
				if (response != null) {
					response.close();
				}
			}
		}
		return result;
	}
	
	/**
	 * httpGET方式 不使用连接池
	 */
	public static Map<String, Object> httpGetNoPool(String url) throws Exception{
    	CloseableHttpClient closeableHttpClient = createHttpsClient();
    	  // 建立HttpGet对象 
    	  HttpGet httpGet = new HttpGet(url);
    	  // 配置要 HttpGet 的消息头
    	  httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");  
          httpGet.addHeader("Connection", "Keep-Alive");  
          httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		  httpGet.addHeader("Origin", "http://www.gyyxjqd.com");
		  httpGet.addHeader("Cookie", "");
    	  //发送get,并返回一个HttpResponse对象
    	  HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
    	  HttpEntity httpEntity2 = httpResponse.getEntity();
    	  // 如果状态码为200,就是正常返回
    	  if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
    	   String result = EntityUtils.toString(httpEntity2);
    	   // 得到返回的字符串
		  Gson gson = new Gson();
    	   Map<String, Object> map=gson.fromJson(result, HashMap.class);
    	   //关闭连接
     	  closeableHttpClient.close();
    	   return map;
    	  }else {
    	   String result = EntityUtils.toString(httpEntity2);
    	   //关闭连接
     	  closeableHttpClient.close();
    	   return null;
    	  }
    	
	}
	
	 public static CloseableHttpClient createHttpsClient() throws Exception   {
   	  X509TrustManager x509mgr = new X509TrustManager() {
   	   @Override
   	   public void checkClientTrusted(X509Certificate[] xcs, String string) {
   	   }
   	   @Override
   	   public void checkServerTrusted(X509Certificate[] xcs, String string) {
   	   }
   	   @Override
   	   public X509Certificate[] getAcceptedIssuers() {
   	    return null;
   	   }
   	  };
   	  SSLContext sslContext = SSLContext.getInstance("TLS");
   	  sslContext.init(null, new TrustManager[] { x509mgr }, new SecureRandom());
   	  SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
   	    sslContext,
   	    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
   	  return HttpClients.custom().setSSLSocketFactory(sslsf).build();
   	 }
	
	 
	 /**
		 * post 请求方法（JSON or XML）
		 * 
		 * @param url
		 * @param data
		 * @param contentType
		 * @return
		 * @throws Exception
		 */
		public static String httpPostNoPool(String url, String data, String contentType, SSLConnectionSocketFactory sslsf) throws Exception {
			// 创建连接
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpPost httPost = new HttpPost(url);
			String result = "";
			if (data != null && !"".equals(data)) {
				StringEntity entity = new StringEntity(data, "UTF-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType(contentType);
				httPost.setEntity(entity);
				CloseableHttpResponse response = null;
				try {
					response = httpClient.execute(httPost);
					// 判断请求是否成功
					if (response.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(response.getEntity());
					}
				} catch (IOException e) {
					logger.error(e.getMessage());
				} finally {
					if (response != null) {
						response.close();
					}
					if (httpClient != null) {
						httpClient.close();
					}
				}
			}
			return result;
		}

	/**
	 * 发送请求
	 * */
	public static String ssl(String url,String data,String mchId){
		StringBuffer message = new StringBuffer();
		try {
			KeyStore keyStore  = KeyStore.getInstance("PKCS12");
			String certFilePath = "F:\\apiclient_cert.p12";
			// linux下
			if ("/".equals(File.separator)) {
//				certFilePath = "/usr/local/nginx/html/"+appId+"/apiclient_cert.p12";
				certFilePath = "/usr/local/nginx/html/apiclient_cert.p12";
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
}
