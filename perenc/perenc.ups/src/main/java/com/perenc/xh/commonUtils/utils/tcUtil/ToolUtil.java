package com.perenc.xh.commonUtils.utils.tcUtil;

import org.apache.commons.lang.math.JVMRandom;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ToolUtil {



    /**
     * 金额两位小数点转换为整数
     * @param number
     * @return
     */
    public static Integer getDoubleToInt(double number) {
        Integer numberInt=0;
        try {
            numberInt=(int)(number * 100);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return numberInt;
    }

    /**
     * 整数转换为金额保留两位小数点
     * @param number
     * @return
     */
    public static double getIntToDouble(Integer number) {
        double numberDoub =0;
        try {
            numberDoub = new BigDecimal(number / 100D).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return numberDoub;
    }

    /**
     *获取两个时间段相差的天数
     * 单位天数
     * @param startTime
     * @param endTime
     * @return
     */
    public static Integer getDateDayNum(String startTime, String endTime) {
        Integer number=0;
        //日期转换
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(startTime!=null && startTime!="" && endTime!=null && endTime!="") {
                Date stime = formatter.parse(startTime);
                Date etime = formatter.parse(endTime);//截止时间
                long between = (etime.getTime() - stime.getTime()) / 1000;//除以1000是为了转换成秒
                long day1 = between / (24 * 3600);
                //转换成小时
                number =(int) day1;
                if(day1<0) {
                    number=0;
                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return number;
    }


    /**
     *获取两个时间段相差的小时数
     * 单位小时
     * @param startTime
     * @param endTime
     * @return
     */
    public static double getDateHourNum(String startTime, String endTime) {
        double number=0;
        //日期转换
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(startTime!=null && startTime!="" && endTime!=null && endTime!="") {
                Date stime = formatter.parse(startTime);
                Date etime = formatter.parse(endTime);//截止时间
                long between = (etime.getTime() - stime.getTime()) / 1000;//除以1000是为了转换成秒
                long day1 = between / (24 * 3600);
                long hour1 = between % (24 * 3600) / 3600;
                long hour2 = between / 3600;
                long minute1 = between % 3600 / 60;
                long second1 = between % 60;
                //转换成小时
                number = day1 * 24 + hour1;
                //分钟计算小数点
                double minute = (double) minute1 / 60;
                BigDecimal bd = new BigDecimal(minute);
                Double temminute = bd.setScale(1, BigDecimal.ROUND_FLOOR).doubleValue();
                number = number + temminute;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return number;
    }

    /**
     *获取两个时间段相差的分钟数
     * 单位分钟
     * @param startTime
     * @param endTime
     * @return
     */
    public static double getDateMinuteNum(String startTime, String endTime) {
        double number=0;
        //日期转换
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date stime = formatter.parse(startTime);
            Date etime = formatter.parse(endTime);//截止时间
            long between=(etime.getTime() - stime.getTime())/1000;//除以1000是为了转换成秒
            long day1=between/(24 *3600);
            //long hour1=between%(24*3600)/3600;
            long hour2=between/3600;
            long minute1=between%3600/60;
            long second1=between%60;
            //转换成分钟数
            number=hour2* 60 + minute1;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return number;
    }

    /**
     *获取两个时间段相差的小时数
     * 单位:x天xx小时xx分
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getDateDayHourMinute(String startTime, String endTime) {
        String number="";
        //日期转换
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(startTime!=null && startTime!="" && endTime!=null && endTime!="") {
                Date stime = formatter.parse(startTime);
                Date etime = formatter.parse(endTime);//截止时间
                long between = (etime.getTime() - stime.getTime()) / 1000;//除以1000是为了转换成秒
                long day1 = between / (24 * 3600);
                long hour1 = between % (24 * 3600) / 3600;
                long hour2 = between / 3600;
                long minute1 = between % 3600 / 60;
                long second1 = between % 60;
                if(day1>0) {
                    number=day1+"天";
                }
                if(hour1>0) {
                    number=number+hour1+"小时";
                }
                number=number+minute1+"分";
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return number;
    }

    /**
     * 获取流水号格式:前缀+时间戳得到8位不重复的随机数+一位随机数(0-9,a-z,A-Z范围)
     * 如：发票流水号格式:WLC-I-000000000
     * 如：档案流水号格式:WLC-F-000000000
     * 如：资产流水号格式:WLC-A-000000000
     * @param prefix    //流水号前缀 （发票类：WLC-I-,档案类：WLC-F-,资产类，WLC-A-）
     * @return
     */
    public static String getNumber(String prefix) {
        String number="";
        try {
            Thread.sleep(1);
            //获取当前时间戳为13位数
            long nowDate = new Date().getTime();
            //把10进制的int转成16进制的
            String sid = Integer.toHexString((int)nowDate);
            //生成一位随机数
            char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            Random random = new Random();
            //XD-P-+结果转换后为8位不重复的随机数+一位随机数
            number=prefix+sid+chr[random.nextInt(chr.length)];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }

    public static String getToken() {
        return String.valueOf(new JVMRandom().nextLong());
    }


}
