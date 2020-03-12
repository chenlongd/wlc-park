package com.perenc.xh.commonUtils.utils.lang;

import com.perenc.xh.commonUtils.utils.StringOrDate.DateHelperUtils;
import com.perenc.xh.commonUtils.utils.StringOrDate.XhrtStringUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 贵阳营销截拳道网络科技有限公司 User: CAI Date: 16-1-14 Time: 下午4:44 表的主键生成
 */
public class RandomUtils {

	/**
	 * 订单生成
	 * 
	 * @return
	 */
	public static String orderRandom() {
		return generateOrderNo("", 9);
	}

	/**
	 * 生成订单号 前缀为空，不加前缀
	 * 
	 * @param pre
	 * @param count
	 *            随机数的位数
	 * @return
	 */
	public static String generateOrderNo(String pre, int count) {
		String nonceStr = RandomStringUtils.random(count, "0123456789");
		String now = DateHelperUtils.getSystem(DateHelperUtils.DATE_FORMATE_YYYYMMDDHHMMSSSS);
		if (now.length() != 17) {
			now = XhrtStringUtils.builderStr(now, RandomStringUtils.random(1, "0123456789"));
		}
		String ordersNo;
		if (XhrtStringUtils.isEmpty(pre)) {
			ordersNo = XhrtStringUtils.builderStr(now, nonceStr);
		} else {
			ordersNo = XhrtStringUtils.builderStr(pre, now, nonceStr);
		}
		return ordersNo;
	}

	public static String generateVerificationCode(){
		return RandomStringUtils.randomNumeric(6);
	}

	public static String generateVerificationCode(int bit) {
		return RandomStringUtils.randomNumeric(bit);
	}

//	public static void main(String[] args) {
//		System.out.println(generateOrderNo("zmsc",10));
//		System.out.println(generateOrderNo("zmsc",11).length());
//
//	}

}
