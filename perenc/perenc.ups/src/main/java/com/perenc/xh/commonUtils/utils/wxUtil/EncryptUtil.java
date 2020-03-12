package com.perenc.xh.commonUtils.utils.wxUtil;

import com.perenc.xh.commonUtils.utils.StringOrDate.XhrtStringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 加密工具类
 * @author Edward 15-10-11
 *
 */
public class EncryptUtil {
	/**
	 * SHA-1加密
	 * @param str
	 */
	public static String sha1(String str) throws Exception{
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(str.getBytes("UTF-8"));
		byte[] hash = crypt.digest();
		
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	/**
	 * 签名
	 * @param map 参与签名的名值对
	 * @param key 密钥
	 */
	public static String sign(Map<String, Object> map, String keyName, String key) throws Exception {
		String sign = null;
		
		List<String> signValueList = new ArrayList<String>();
		if(map != null){
			Set<String> keySet = map.keySet();
			List<String> list = new ArrayList<String>(keySet);
			Collections.sort(list);
			for(String k : list){
				if("class".equals(k)){
					continue;
				}
				Object value = map.get(k);
				if(value != null && XhrtStringUtils.isNotEmpty(value.toString())){
					signValueList.add(k + "=" + value.toString());
				}
			}
		}
		if(signValueList.size() > 0){
			StringBuffer str = new StringBuffer();
			for(String signStr : signValueList){
				str.append("&").append(signStr);
			}
			
			String stringA = str.toString().replaceFirst("&", "");
			String stringB = stringA + "&"+keyName+"=" + key;
			//System.out.println("==============strB:"+stringB);
			sign = encode(stringB, "MD5").toUpperCase();
		}
		return sign;
	}
	
	/**
	 * 签名(重载)
	 * @param map
	 * @param key
	 */
	public static String sign(Map<String, Object> map, String key) throws Exception {
		return EncryptUtil.sign(map, "key", key);
	}

	public static String encode(String msg, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] b = md.digest(msg.getBytes("UTF-8"));// 产生数据的指纹
			// Base64编码
			StringBuilder sbDes = new StringBuilder();
			String tmp = null;
			for (int i = 0; i < b.length; i++) {
				tmp = (Integer.toHexString(b[i] & 0xFF));
				if (tmp.length() == 1) {
					sbDes.append("0");
				}
				sbDes.append(tmp);
			}
			return sbDes.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
//	public static void main(String[] args) {
//
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("appid", "wx6284b75a737e288d");
//		params.put("mch_id", "1505647441");
//		String nonce_str = "40002E91F7144B638D0F982DD569BFF6";
//		int length = nonce_str.length();
//		params.put("nonce_str", nonce_str);
//		params.put("body", "zhifu");
//		params.put("out_trade_no", "zmsc201906251542037741014222506");
//		params.put("total_fee", 1);
//		//id=17000000&name=新大类&apikey=8BFE3DA41DAB5592FD92B0A3D69180C5
//		params.put("spbill_create_ip", "127.0.0.1");
//		params.put("notify_url", "https://www.wlc-huish.com/phone/api/notify_wechat");
//		params.put("trade_type", "JSAPI");
//
//		String sign = null;
//		String resXml = "";
//		try {
//			sign = EncryptUtil.sign(params, "key", "zmsc6666zmsc6666zmsc6666zmsc6666");
//			params.put("sign", EncryptUtil.sign(params, "key", "zmsc6666zmsc6666zmsc6666zmsc6666"));
//			resXml = HttpCilentUtils.post("https://api.mch.weixin.qq.com/pay/unifiedorder", XmlUtil.mapToXml(params, "xml"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("===========sign:"+resXml);
//	}
}
