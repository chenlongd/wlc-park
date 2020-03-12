package com.perenc.xh.commonUtils.utils.StringOrDate;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class DateHelperUtils {

    public final static String DATE_FORMATE_YYYYMMDD1 = "yyyy.MM.dd";
    public final static String DATE_FORMATE_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public final static String DATE_FORMATE_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_FORMATE_YYYYMMDD = "yyyy-MM-dd";
    public final static String DATE_FORMATE_YYYYMMDD_01 = "yyyyMMdd";
    public final static String DATE_FORMATE_YYYYMM = "yyyy-MM";
    public final static String DATE_FORMATE_YYYYMM_01 = "yyyyMM";
    public final static String DATE_FORMATE_YYYYMMDDHHMMSSSS = "yyyyMMddHHmmssSS";
    public final static String DATE_FORMATE_YYYYMMDDHHMMSSS = "yyyyMMddHHmmssS";

    private final static ReentrantLock LOCK = new ReentrantLock();

    /**
     * 区分两个日期之间指定字段的差值
     *
     * @param time1 开始时间
     * @param time2 结束时间
     * @param field 要比较的字段(年，月，日,...)
     * @return 如果time1>time2就反回一个正的差值,如果time1<time2则返回一个负的差值,如果相等，返回0
     */
    public static int getFieldDifference(long time1, long time2, int field) {
        if (time1 == time2) {
            return 0;
        } else if (time1 > time2) {
            return -getFieldDifference(time2, time1, field);
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setLenient(false);
        cal1.setTimeInMillis(time1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setLenient(false);
        cal2.setTimeInMillis(time2);
        for (int x = 0; x < Calendar.FIELD_COUNT; x++) {
            if (x > field) {
                cal1.clear(x);
                cal2.clear(x);
            }
        }
        time1 = cal1.getTimeInMillis();
        time2 = cal2.getTimeInMillis();

        long ms = 0;
        int min = 0, max = 1;

        while (true) {
            cal1.setTimeInMillis(time1);
            cal1.add(field, max);
            ms = cal1.getTimeInMillis();
            if (ms == time2) {
                min = max;
                break;
            } else if (ms > time2) {
                break;
            } else {
                max <<= 1;
            }
        }

        while (max > min) {
            cal1.setTimeInMillis(time1);
            int t = (min + max) >>> 1;
            cal1.add(field, t);
            ms = cal1.getTimeInMillis();
            if (ms == time2) {
                min = t;
                break;
            } else if (ms > time2) {
                max = t;
            } else {
                min = t;
            }
        }
        return -min;
    }

    /**
     * 两时间的月份
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static int getMonthSpace(String date1, String date2) throws ParseException {

        int result = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));
        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        if (year  == 0) {
        	result = c2.get(Calendar.MONDAY) - c1.get(Calendar.MONTH);
        }else{
        	result = year * 12 + c2.get(Calendar.MONDAY) - c1.get(Calendar.MONTH);
        }

        return result;
    }

    /**
     * 两时间相差的天数
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static int getDateSpace(String date1, String date2, String formateType) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(formateType);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date1));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(date2));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 日期相加减
     *
     * @param s     日期
     * @param n     值
     * @param feild 加减类型 年，月，日
     * @return
     */
    public static String addDate(String s, int n, int feild, String formateType) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formateType);
            Calendar cd = Calendar.getInstance();
            cd.setTime(sdf.parse(s));
            cd.add(feild, n);
            return sdf.format(cd.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串转换为时间
     *
     * @param date
     * @param formateType
     * @return
     */
    public static Date strToDate(String date, String formateType) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formateType);
        if (date == null || "".equals(date)) {
            return null;
        }
        Date formatedate = null;
        try {
            formatedate = dateformat.parse(date);
        } catch (ParseException e) {
            return formatedate;
        }
        return formatedate;
    }

    /**
     * 日期 类型转换 字符串
     *
     * @param date
     * @param formateType
     * @return
     */
    public static String dateToStr(Date date, String formateType) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formateType);
        return sdf.format(date);
    }

    /**
     * 时间加减
     *
     * @param date
     * @param count
     * @param formater
     * @return
     */
    public static String dateAddOrReduceDay(Date date, int count, String formater) {

        // 获取一个日历
        Calendar cal = Calendar.getInstance();

        // 设定日期
        cal.setTime(date);

        // 取当前日期的前一天.
        cal.add(Calendar.DAY_OF_MONTH, count);

        // 格式化对象
        SimpleDateFormat format = new SimpleDateFormat(formater);

        // 格式化
        String strDate = null;
        try {
            strDate = format.format(cal.getTime());
        } catch (Exception e) {
            return strDate;
        }
        return strDate;
    }

    /**
     * 获取一年中某一个月的天数
     */
    public static int getDays(String yearmonth) throws Exception {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        c.setTime(sdf.parse(yearmonth));
        int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    /**
     * 获取指定格式的系统时间
     *
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static String getSystem(String formatType) {
        if (StringUtils.isNotEmpty(formatType)) {

            SimpleDateFormat sdf = new SimpleDateFormat(formatType);
            String str = sdf.format(new Date(System.currentTimeMillis()));
            return str;
        } else {
            return "";
        }
    }

    /**
     * 获取两时间中有多少月份 (格式为 YYYY-MM)
     *
     * @param start
     * @param end
     * @return List (YYYY-MM)
     */
    public static String[] getAllMonths(String start, String end) {
        String splitSign = "-";
        String regex = "\\d{4}" + splitSign + "(([0][1-9])|([1][012]))"; // 判断YYYY-MM时间格式的正则表达式
        if (!start.matches(regex) || !end.matches(regex))
            return new String[0];

        List<String> list = new ArrayList<String>();
        if (start.compareTo(end) > 0) {
            // start大于end日期时，互换
            String temp = start;
            start = end;
            end = temp;
        }

        String temp = start; // 从最小月份开始
        while (temp.compareTo(start) >= 0 && temp.compareTo(end) <= 0) {
            list.add(temp); // 首先加上最小月份,接着计算下一个月份
            String[] arr = temp.split(splitSign);
            int year = Integer.valueOf(arr[0]);
            int month = Integer.valueOf(arr[1]) + 1;
            if (month > 12) {
                month = 1;
                year++;
            }
            if (month < 10) {// 补0操作
                temp = year + splitSign + "0" + month;
            } else {
                temp = year + splitSign + month;
            }
        }

        int size = list.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    /**
     * 比较日期大小
     * @param DATE1
     * @param DATE2
     * @param formatType
     * @return
     */
    public static int compareDate(String DATE1, String DATE2, String formatType) {

        DateFormat df = new SimpleDateFormat(formatType);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 日期运算
     *
     * @param date
     * @param count
     * @param formatType
     * @param field      YEAR = 1 MONTH = 2 WEEK_OF_YEAR = 3 WEEK_OF_MONTH = 4 MINUTE = 12
     * @return
     */
    public static String dateOperate(Date date, int count, String formatType, int field) {
        if (StringUtils.isEmpty(formatType)) {
            return "";
        }
        if (date == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, count);
        Date afterDate = (Date) cal.getTime();
        cal.setTime(new Date(System.currentTimeMillis()));
        return formatter(afterDate, formatType);
    }

    /**
     * 日期格式化
     *
     * @param date
     * @param formatType
     * @return
     */
    public static String formatter(Date date, String formatType) {
        if (StringUtils.isEmpty(formatType)) {
            return "";
        }
        if (date == null) {
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        String ctime = formatter.format(date);
        return ctime;
    }

    /**
     * 获取当天为星期几
     * 0-"星期日", 1-"星期一", 2-"星期二", 3-"星期三", 4-"星期四", 5-"星期五", 6-"星期六"
     *
     * @param date
     * @return
     */
    public static int getWeekOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int Week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (Week < 0)
            Week = 0;
        return Week;
    }

    /**
     * 获取当前日期属于本月的第几周的起点日期
     * 按照当月第一个星期一为计算起点
     * 多余部分算作上月的最后一周
     * 本函数的作用就是获取当月第一个星期一的日期
     *
     * @param date
     * @return
     * @throws ParseException
     */

    public static Date GetWeekDate(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        date = strToDate(getWeekStartTime(date),DATE_FORMATE_YYYYMMDD);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        String date1 = formatter(cal.getTime(), DATE_FORMATE_YYYYMMDD);
        String date2 = formatter(date, DATE_FORMATE_YYYYMMDD);
        int Week = getWeekOfDate(date);
        int Day1 = getDateSpace(date1, date2, DATE_FORMATE_YYYYMMDD) + 1;
        /**
         * 根据当月一号的日期和当前日期的星期和天数差值确定是否从上月1号开始取值.
         */
        if (Week == 0) {
            //如果是星期天,从新赋值为7
            Week = 7;
        }
        if (Week > Day1) {  //判断当月一号到当前日期的天数和当前星期做比较,是否在7天以内,如果是在7天以内,跳到上月1号
            cal.set(Calendar.DAY_OF_MONTH, -1);  //设置上月第一天
        }
        Week = getWeekOfDate(cal.getTime());
        if (Week == 0) {
            cal.set(Calendar.DAY_OF_MONTH, 2);
        } else if (Week > 1) {
            cal.set(Calendar.DAY_OF_MONTH, 9 - Week);
        }
        Date ResultDate = cal.getTime();
        cal.setTime(new Date(System.currentTimeMillis()));
        return ResultDate;
    }


    /**
     * 获取当前日期所属第几周
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static int GetWeekNumber(Date date) throws ParseException {
        if (date == null) {
            return 0;
        }
        String date1 = formatter(GetWeekDate(date), DATE_FORMATE_YYYYMMDD);
        String date2 = formatter(date, DATE_FORMATE_YYYYMMDD);
        int Day = getDateSpace(date1, date2, DATE_FORMATE_YYYYMMDD) + 1;

        int Week = Day / 7;
        if (((Week * 7) < Day) || (Week == 0)) {
            Week = Week + 1;
        }
        return Week;
    }


    /**
     * 返回当月总的周数
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static int GetTotalWeek(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Date fastDate = GetWeekDate(date);
        cal.setTime(fastDate);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date MaxDate = cal.getTime();
        MaxDate.setDate(lastDay);
        int Day = getDateSpace(formatter(fastDate, DATE_FORMATE_YYYYMMDD), formatter(MaxDate, DATE_FORMATE_YYYYMMDD), DATE_FORMATE_YYYYMMDD) + 1;
        cal.setTime(new Date(System.currentTimeMillis()));
        if (Day % 7 > 0) {
            return (Day / 7) + 1;
        } else {
            return Day / 7;
        }
    }


    /**
     * 获取当前日期所属那个月份,
     * 是按照周为基本单位计算的
     * 当月的第一个星期一为第一周的开始,多余的部分算为上个月的最后一个周
     * 所有此函数可能范围的值和实际的日期不相符
     * 例如2016-09-01属于8月的第四周,所以该函数返回的是201608
     *
     * @param date
     * @return
     * @throws ParseException
     */

    public static String GetWeekIsMonth(Date date) throws ParseException {
        if (date == null) {
            return "";
        }
        return formatter(GetWeekDate(date), DATE_FORMATE_YYYYMM_01);
    }

    // 获得当前日期与本周日相差的天数
    private static int getMondayPlus(Date gmtCreate) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(gmtCreate);
        //一周第一天是否为星期天
        boolean isFirstSunday = (cd.getFirstDayOfWeek() == Calendar.SUNDAY);
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK); // 因为按中国礼拜一作为第一天所以这里减1
        if (isFirstSunday) {
            dayOfWeek = dayOfWeek - 1;
            if (dayOfWeek == 0) {
                dayOfWeek = 7;
            }
        }
        cd.setTime(new Date(System.currentTimeMillis()));
        if (dayOfWeek == 1) {
            return 0;
        } else if (dayOfWeek == 0) {
            return -6;
        } else {
            return 1 - dayOfWeek;
        }
    }

    // 获得下周星期一的日期
    public static Date getNextMonday(Date gmtCreate) {
        int mondayPlus = getMondayPlus(gmtCreate);
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        currentDate.setTime(new Date(System.currentTimeMillis()));
        return monday;
    }

    // 获得本周星期一的日期
    public static Date getFastMonday(Date gmtCreate) {
        int mondayPlus = getMondayPlus(gmtCreate);
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(gmtCreate);
        currentDate.add(Calendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        currentDate.setTime(new Date(System.currentTimeMillis()));
        return monday;
    }

    /**
     * 获取当前日期属于当年第几周
     *
     * @param time
     * @return
     */
    public static int getWeekOfYear(String time) {
        int weekOfYear = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return weekOfYear;
    }

    /**
     * 获取两个日期相差的周数
     * 2017年1月3日修改, 解决跨年计算不准确的bug
     *
     * @param end
     * @param start
     * @return
     */
    public static int getTwoDatesDifOfWeek(Date end, Date start) {
        String s = dateToStr(start, "yyyy-MM-dd");
        String e = dateToStr(end, "yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long from = df.parse(s).getTime();
            long to = df.parse(e).getTime();
            return (int) ((to - from) / (1000 * 3600 * 24 * 7));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return 0;
    }



    /**
     * 根据传入日期获取当前周的开始时间
     * @param date
     * @return
     */
    public static String getWeekStartTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMATE_YYYYMMDDHHMMSS);
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return df.format((Date)currentDate.getTime().clone());
    }

    /**
     * 根据传入日期获取当前周的结束时间
     * @param date
     * @return
     */
    public static String getWeekEndTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMATE_YYYYMMDDHHMMSS);
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return df.format((Date) currentDate.getTime().clone());
    }

    /**
     * 根据传入日期获取当天的开始时间
     * @param date
     * @return
     */
    public static String getDayStartTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMATE_YYYYMMDDHHMMSS);
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return df.format((Date)currentDate.getTime().clone());
    }

    /**
     * 根据传入日期获取当天的结束时间
     * @param date
     * @return
     */
    public static String getDayEndTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMATE_YYYYMMDDHHMMSS);
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return df.format((Date)currentDate.getTime().clone());
    }

}
