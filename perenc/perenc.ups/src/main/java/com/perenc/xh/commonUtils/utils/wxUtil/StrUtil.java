package com.perenc.xh.commonUtils.utils.wxUtil;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

/**
 * 字符串工具类
 * @author Edward
 */
public class StrUtil {

	/**
	 * 判断字符串为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
	
	public static boolean isNoneBlank(String str){
		return str!= null&&str.trim().length()!=0;
	}
	/**
	 * 判断字符串不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return str != null && !"".equals(str);
	}
	
	/**
	 * 转换属性名为get方法名
	 * @param fieldName
	 * @return
	 */
	public static String toGetFunctionName(String fieldName) {
		String result = null;
		if(fieldName != null && fieldName.length() >= 1) {
			result = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
		}
		return result;
	}
	
	/**
	 * 转换属性名为set方法名
	 * @param fieldName
	 * @return
	 */
	public static String toSetFunctionName(String fieldName) {
		String result = null;
		if(fieldName != null && fieldName.length() >= 1) {
			result = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
		}
		return result;
	}
	
	/**
	 * 获取32位的UUID
	 */
	public static String getUUID32() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}
	
	/**
	 * 剔除特殊字符
	 */
	public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("utf-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256; // 去掉符号位
			if (((b >> 5) ^ 0x6) == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			} else if (((b >> 4) ^ 0xE) == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			} else if (((b >> 3) ^ 0x1E) == 0) {
				i += 4;
			} else if (((b >> 2) ^ 0x3E) == 0) {
				i += 5;
			} else if (((b >> 1) ^ 0x7E) == 0) {
				i += 6;
			} else {
				buffer.put(bytes[i++]);// 添加处理如b的指为-48等情况出现的死循环
			}
		}
		buffer.flip();
		return new String(buffer.array(), "utf-8");
	}

	public static boolean contains(List<String> list, String str) {
		boolean b = false;
		for(String i : list) {
			if(i.equals(str)) {
				b = true;
				break;
			}
		}
		return b;
	}
	
	public static String[] listToArray(List<String> list) {
		String[] arr = new String[list.size()];
		for(int i=0; i<list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}
	
	public static String rightTrim(String str, String trimBy) {
		while(str.endsWith(trimBy)) {
			str = str.substring(0, str.length()-trimBy.length());
		}
		return str;
	}
	
	public static String leftTrim(String str, String trimBy) {
		while(str.startsWith(trimBy)) {
			str = str.substring(trimBy.length());
		}
		return str;
	}

}
