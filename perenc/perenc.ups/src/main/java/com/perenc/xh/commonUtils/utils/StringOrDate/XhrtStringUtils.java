package com.perenc.xh.commonUtils.utils.StringOrDate;


import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.UUID;

public class XhrtStringUtils extends StringUtils {

	/**
	 * 判断该字符串 是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		if ("".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * String 转换 Integer类型
	 * 
	 * @param num
	 * @return
	 */
	public static Integer strToInteger(String num) {
		if (isEmpty(num)) {
			return 0;
		}
		Integer count = 0;
		try {
			count = Integer.parseInt(num);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * String 转换 Long 类型
	 * 
	 * @param num
	 * @return
	 */
	public static Long strToLong(String num) {
		if (isEmpty(num)) {
			return 0L;
		}
		Long count = 0L;
		try {
			count = Long.parseLong(num);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * String 转换 Double类型
	 * 
	 * @param num
	 * @return
	 */
	public static Double strToDouble(String num) {
		if (isEmpty(num)) {
			return 0.0;
		}
		Double count = 0.0;
		try {
			count = Double.parseDouble(num);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * String 转换 BigDecimal类型
	 * 
	 * @param num
	 * @return
	 */
	public static BigDecimal strToBigDecimal(String num) {
		if (isEmpty(num)) {
			return new BigDecimal(0);
		}
		BigDecimal count = new BigDecimal(0);

		try {
			count = new BigDecimal(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 生成32位ID
	 * 
	 * @return
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.replaceAll("-", "").toUpperCase();
	}

	/**
	 * 数字补0 字符串
	 * 
	 * @param num
	 * @param count
	 * @return
	 */
	public static String numAddZero(String num, Integer count) {
		Integer numLenght = num.length();
		Integer forCount = count - numLenght;
		String str = "";
		for (int i = 1; i <= forCount; i++) {
			str += "0";
		}
		return str + num;
	}
	
	/**
	 * 生成随机数密码
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }

	/**
	 * 使用StringBuilder 拼凑字符串
	 *
	 * @param str
	 * @return
	 */
	public static String builderStr(String... str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length; i++) {
			sb.append(str[i]);
		}
		return sb.toString();
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
}
