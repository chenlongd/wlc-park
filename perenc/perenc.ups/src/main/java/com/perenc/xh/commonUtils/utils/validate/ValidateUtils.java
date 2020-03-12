package com.perenc.xh.commonUtils.utils.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {

    public static void main(String[] args)  {
        //System.out.println(isChineseName("天下太知"));
        //System.out.println(isAge("100"));
         double a=100D;
        //double value = new BigDecimal( 100000213 / 100D).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        String hphm2 = "渝DF34567";
        //String hphmEx2 = "^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})$";
        /*boolean isMatch0 = Pattern.matches(hphmEx2, hphm2);
        System.out.println(isMatch0);*/
        System.out.println(isCarnumberNO("渝D234567"));

        //System.out.println(getDoubleToInteger(a));
        //System.out.println(result);
        //System.out.println(isEmail("914476463@qq.com"));
    }


    /**
     * 用户名验证
     * 验证是否仅含有数字和字母
     * 用户名只能输入2-16位字母或数字
     * @param username
     * @return
     */
    public  static boolean isValidateUsername(String username) {
        try {
            String regex = "^[a-z0-9A-Z]{2,16}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(username);
            if (m.matches()) {
                return true;
            } else {
                System.out.println("只能输入包含有数字和字母");
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
    }

    /**
     * 密码验证
     * 密码只能输入6-18位字母或数字
     * @param password
     * @return
     */
    public  static boolean isValidatePassword(String password) {
        try {

            //要求字母开头，必须字母数字组合，允许标点符号，位数6-18位
            //String regex ="^[a-zA-Z](?![0-9]+$)(?![a-zA-Z]+$)([a-zA-Z0-9]|[._#@]){6,18}$";
            String regex ="^[a-z0-9A-Z]{6,18}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(password);
            if (m.matches()) {
                return true;
            } else {
                System.out.println("只能输入包含有数字和字母");
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
    }



    /**
     * 验证是否是大写（A~Z）
     * @param pinyin
     * @return
     */
    public  static boolean isValidateAtoZ(String pinyin) {
        try {
            String regex = "[A-Z]";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(pinyin);
            if (m.matches()) {
                return true;
            } else {
                System.out.println("只能输入A到Z的大写");
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
    }


    /**
     * 验证是否是两位的数字（01~99）
     * @param number
     * @return
     */
    public  static boolean isValidateTwoNumber(String number) {
        try {
            if (number.equals("00")) {
                return false;
            }
            String regex = "[0-9][0-9]";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(number);
            if (m.matches()) {
                return true;
            } else {
                System.out.println("只能输入01到99的数字");
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
    }

    /**
     * 验证姓名
     * @param name
     * @return
     */
    public  static boolean isChineseName(String name) {
        try {
            String regex = "[\u4e00-\u9fa5]{2,4}";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(name);
            if (m.matches()) {
                return true;
            } else {
                System.out.println("只能输入2到4个汉字");
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
    }


    /**
     * 验证姓名
     * 2到8个字的汉字,或者2到16个字的英文
     * @param name
     * @return
     */
    public  static boolean isValidateName(String name) {
        try {
            String regex = "^(([\u4e00-\u9fa5]{2,8})|([a-zA-Z]{2,16}))$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(name);
            if (m.matches()) {
                return true;
            } else {
                System.out.println("只能输入2到8个汉字");
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
    }


    private static final String REGEX_MOBILE = "(134[0-8]\\d{7})" +
            "|(" +
            "((13([0-3]|[5-9]))" +
            "|149" +
            "|15([0-3]|[5-9])" +
            "|166" +
            "|17(3|[5-8])" +
            "|18[0-9]" +
            "|19[8-9]" +
            ")" +
            "\\d{8}" +
            ")";

    /**
     * 验证手机号码
     * @param phone
     * @return
     */
    public static boolean isPhoneCheck(String phone) {
        try {
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9])|(16[6]))\\d{8}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            if (m.matches()) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        try {
            String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p = Pattern.compile(regEx1);
            Matcher m = p.matcher(email);
            if (m.matches()) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
    }

    /**
     * 验证年龄
     * @param age
     * @return
     */
    public static boolean isAge(String age) {
        try {
            String regAge1 = "[0-9]*";
            Pattern p = Pattern.compile(regAge1);
            Matcher m = p.matcher(age);
            if (m.matches()) {
                int ageInt = Integer.parseInt(age);
                if(ageInt < 120 && ageInt > 0){
                    return true;
                }
            } else {
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
        return  false;
    }



    /**
     * 是否是6位数字
     * @param number
     * @return
     */
    public static boolean isSixNumber(String number) {
        try{
            int inumber = Integer.valueOf(number);
            return  true;
        }catch(Exception ex){
            return  false;
        }
    }


    /**
     * 验证是不是纯数字
     * @param number
     * @return
     */
    public static boolean isNumber(String number) {
        try {
            String regNumber = "[0-9]*";
            Pattern p = Pattern.compile(regNumber);
            Matcher m = p.matcher(number);
            if (m.matches()) {
                int numberInt = Integer.parseInt(number);
                    return true;
            } else {
                return false;
            }
        }catch (Exception e) {
            return  false;
        }
    }
    /**
     * 验证小数点后2位的数字
     * @param amount
     * @return
     */
    public static boolean isAmount(String amount) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(amount);
        if(match.matches()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 车牌号验证
     * 1.常规车牌号：仅允许以汉字开头，后面可录入六个字符，由大写英文字母和阿拉伯数字组成。如：粤B12345；
     * 2.武警车牌：允许前两位为大写英文字母，后面可录入五个或六个字符，由大写英文字母和阿拉伯数字组成，其中第三位可录汉字也可录大写英文字母及阿拉伯数字，第三位也可空，如：WJ警00081、WJ京1234J、WJ1234X。
     * 3.最后一个为汉字的车牌：允许以汉字开头，后面可录入六个字符，前五位字符，由大写英文字母和阿拉伯数字组成，而最后一个字符为汉字，汉字包括“挂”、“学”、“警”、“军”、“港”、“澳”。如：粤Z1234港。
     * 4.新军车牌：以两位为大写英文字母开头，后面以5位阿拉伯数字组成。如：BA12345。
     * @param carnumber
     * @return
     */
    public static boolean isCarnumberNO(String carnumber) {
        //Pattern pattern = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[警京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{0,1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$"); // 判断小数点后2位的数字的正则表达式
        //Pattern pattern = Pattern.compile("^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})$");
        Pattern pattern = Pattern.compile("^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$");
        Matcher match = pattern.matcher(carnumber);
        if(match.matches()){
            return true;
        }else{
            return false;
        }
    }


}
