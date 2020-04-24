/*
 * Created on 2005-7-13
 *
 * 时间工具类.
 */
package com.kingyee.common.util;


import org.springframework.util.StringUtils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 时间工具
 *
 * @author 马劼
 */
public class TimeUtil {

    /**
     * 日期格式 yyyy-MM-dd 2013-06-16
     */
    public static String FORMAT_DATE = "yyyy-MM-dd";
    /**
     * 日期格式 yyyy-MM-dd HH:mm:ss 2013-06-16 09:54:02
     */
    public static String FORMAT_DATETIME_FULL = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式 yyyy-MM-dd HH:mm:ss 2013-06-16 09:54:02
     */
    public static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm";
    /**
     * 日期格式 MM-dd 06-16
     */
    public static String FORMAT_DATE_ONLY_DAY = "MM-dd";
    /**
     * 日期格式 HH:mm 09:54
     */
    public static String FORMAT_DATE_ONLY_HOUR = "HH:mm";
    /**
     * 日期格式 yyyy-MM-dd HH:mm:ss 2013-06-16 09:54:02
     */
    public static String FORMAT_DATETIME_HH = "yyyy-MM-dd HH";
    /**
     * ueditor图片目录文件夹名称 - 日期格式 yyyy-MM-dd 2013-06-16
     */
    public static String UEDITOR_FORMAT_DATE = "yyyyMMdd";
    /**
     * 日期格式 零点
     */
    public static String FORMAT_DATETIME_FULL_ZERO = "yyyy-MM-dd 00:00:00";

    /**
     * 日期格式 yyyy-MM
     */
    public static String FORMAT_DATE_MONTH = "yyyy-MM";



    /**
     * 得到当前时间
     *
     * @return nowstr
     */
    public static Date getNowTime() {
        Date now = new Date();
        return now;
    }

    /**
     * SimpleDateFormat到格式的日期
     *
     * @return SimpleDateFormat
     */
    private static SimpleDateFormat getDateFormatter(String f) {
        SimpleDateFormat formatter = new SimpleDateFormat(f);
        // workaround for bug
        formatter.setTimeZone(java.util.TimeZone.getDefault());
        // don't alllow things like 1/35/99
        formatter.setLenient(false);
        return formatter;
    }

    /**
     * 获得当前时间Calendar格式
     *
     * @param d
     * @return now date
     */
    public static Calendar getCalendar(Date d) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        return now;
    }

    /**
     * 得到指定格式的日期字符串
     *
     * @param f
     * @return nowDate String.
     */
    public static String getNowTime(String f) {
        return getDateFormatter(f).format(getNowTime());
    }

    /**
     * 得到当前时间的毫秒部分
     *
     * @return str
     */
    public static String getSecond() {
        return getNowTime("sss");
    }

    /**
     * String到Date格式的转换
     *
     * @param d
     * @param f
     * @return date Date
     */
    public static Date stringToDate(String d, String f) {
        // will throw exception if can't parse
        try {
            return getDateFormatter(f).parse(d);
        } catch (ParseException e) {
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * string 到 date 的转换
     *
     * @param d
     * @param f
     * @return
     */
    public static Long stringToLong(String d, String f) {
        if (d == null) {
            return null;
        }
        Date date = stringToDate(d, f);
        if (date == null) {
            return null;
        }
        return date.getTime();
    }

    /**
     * date到String格式的转换
     *
     * @param d
     * @param f
     * @return date String
     */
    public static String dateToString(Date d, String f) {
        return getDateFormatter(f).format(d);
    }

    /**
     * Long到String格式的转换
     *
     * @param d
     * @param f
     * @return date String
     */
    public static String longToString(Long d, String f) {
        if (d == null)
            return "";
        return dateToString(longToDate(d), f);
    }

    /**
     * 当前时间的long格式,除以1000以Integer表示. 主要用于与c++交换日期,缺点是没有毫秒值.
     *
     * @return longDate Integer
     */
    public static Integer nowDateToInteger() {
        return Integer.valueOf(String.valueOf(getNowTime().getTime() / 1000));
    }

    /**
     * 转换指定日期到Integer格式
     *
     * @param d
     * @return date Integer
     */
    public static Integer dateToInteger(Date d) {
        if (d == null) {
            return new Integer(0);
        } else {
            return Integer.valueOf(String.valueOf(d.getTime() / 1000));
        }
    }

    /**
     * integer to Date 主要用于从c++中传入的日期类型转换为Date类型.
     *
     * @param d
     * @return date Date
     */
    public static Date integerToDate(Integer d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(((long) d.intValue()) * 1000);
        return cal.getTime();
    }

    /**
     * 转换现在的时间为Long格式 以毫秒为单位计算,从1970年1月1日0时0分0秒0000毫秒算起.
     *
     * @return longDate long
     */
    public static long dateTolong() {
        return getNowTime().getTime();
    }

    /**
     * @return longDate Long
     * @see this.dateTolong
     */
    public static Long dateToLong() {
        return new Long(getNowTime().getTime());
    }

    public static Long dateToLong(String format) {
        String date = getNowTime(format);
        return stringToLong(date, format);
    }

    /**
     * this.dateTolong
     *
     * @return longDate string
     */
    public static String dateToLongString() {
        return String.valueOf(getNowTime().getTime());
    }

    /**
     * long转换为日期
     *
     * @param d
     * @return date Date
     */
    public static Date longToDate(long d) {
        return new Date(d);
    }

    /**
     * long转换为日期
     *
     * @param d
     * @return date Date
     */
    public static Date longToDate(Long d) {
        return new Date(d.longValue());
    }

    /**
     * lang or integer型转换成date string
     *
     * @param str
     * @param format
     * @return string date
     */
    public static String getDate(String str, String format) {
        if (str.length() > 11) {
            return dateToString(longToDate(Long.valueOf(str)), format);
        } else {
            return dateToString(integerToDate(Integer.valueOf(str)), format);
        }
    }

    /**
     * 取得某月最后一天的日期整数值
     *
     * @param year  年(yyyy)
     * @param month 月(mm或m)
     * @return int 日期整数值
     */
    public static int getMaxDayOfMonth(String year, String month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(year));
        c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 计算两个long型的时间之间的差（**天 ** 小时 ** 分钟）
     *
     * @param starttime 开始时间
     * @param endtime   结束时间
     * @return
     */
    public static String getTimeDiff(long starttime, long endtime) {
        StringBuffer time = new StringBuffer();
        if (endtime > starttime) {
            long left = endtime - starttime;
            long daynum = left / (24 * 60 * 60 * 1000);
            left = left - daynum * (24 * 60 * 60 * 1000);
            long hournum = left / (3600 * 1000);
            left = left - hournum * (3600 * 1000);
            long minnum = left / (60 * 1000);
            if (daynum > 0) {
                time.append(daynum + " 天 ");
            }
            if (hournum > 0) {
                time.append(hournum + " 小时 ");
            }
            if (minnum > 0) {
                time.append(minnum + " 分钟 ");
            }
        }
        return time.toString();
    }

    /**
     * 小时差
     *
     * @param starttime
     * @param endtime
     * @return
     */
    public static Long getTimeDiffHour(long starttime, long endtime) {
        Long h = 0L;
        if (endtime > starttime) {
            long left = endtime - starttime;
            h = left / (3600 * 1000);
        }
        return h;
    }

    /**
     * 分钟差
     *
     * @param starttime
     * @param endtime
     * @return
     */
    public static Long getTimeDiffMin(long starttime, long endtime) {
        Long h = 0L;
        if (endtime > starttime) {
            long left = endtime - starttime;
            h = left / (60 * 1000);
            left = left - h * (60 * 1000);
            if (left > 0) {
                h += 1;
            }
        }
        return h;
    }

    /**
     * 天
     *
     * @param starttime
     * @param endtime
     * @return
     */
    public static Long getTimeDiffDay(long starttime, long endtime) {
        Long d = getTimeDiffHour(starttime, endtime) / 24;
        return d;
    }

    /**
     * 计算两个long型的时间之间的差（**年 ** 月）
     *
     * @param starttime 开始时间
     * @param endtime   结束时间
     * @return
     */
    public static String getTimeYearAndMonth(long starttime, long endtime) {
        StringBuffer time = new StringBuffer();
        if (endtime > starttime) {
            long left = endtime - starttime;
            long monthnum = left / (30 * 24 * 60 * 60 * 1000L);
            long year = monthnum / 12;
            long month = monthnum % 12;
            time.append(year);
            time.append("年");
            time.append(month);
            time.append("月");
        }
        return time.toString();
    }

    /**
     * zhl 常用转化精确到天
     *
     * @param time
     * @return
     */
    public static Long stringToLong(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        return stringToLong(time, "yyyy-MM-dd");
    }

    /**
     * by zhl 常用转化精确到天
     *
     * @param time
     * @return
     */
    public static String longToString(Long time) {
        if (time == null)
            return "";
        return TimeUtil.longToString(time, "yyyy-MM-dd");
    }

    /**
     * zhl 出生日期得到年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Long birthDay) {
        if (birthDay == null)
            return 0;
        return getAge(longToDate(birthDay));
    }

    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            return 0;
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    // do nothing
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        } else {
            // monthNow<monthBirth
            // donothing
        }

        return age;
    }

    /**
     * zhl 出生日期得到年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Long birthDay, Long date) {
        if (birthDay == null || date == null)
            return 0;
        return getAge(longToDate(birthDay), longToDate(date));
    }

    public static int getAge(Date birthDay, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.before(birthDay)) {
            return 0;
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    // do nothing
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        } else {
            // monthNow<monthBirth
            // donothing
        }

        return age;
    }

    /**
     * 某时间后几天
     *
     * @return
     */
    public static Long getDateAfter(Long date, Integer daysAfter) {
        if (date == null) {
            return null;
        }
        if (daysAfter == null) {
            daysAfter = 0;
        }
        return date + daysAfter * 24 * 60 * 60 * 1000L;
    }

    /**
     * 某时间差几个月
     *
     * @param d
     * @param n
     * @return
     */
    public static Long getPreOrNextMonth(long date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.add(Calendar.MONTH, n);
        return c.getTimeInMillis();
    }

    /**
     * 某时间相差N年
     *
     * @param d
     * @param n
     * @return
     */
    public static Long getPreOrNextYear(long date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.add(Calendar.YEAR, n);
        return c.getTimeInMillis();
    }

    /**
     * 0点
     *
     * @param date
     * @return
     */
    public static Long getDateZero(Long date) {
        return TimeUtil.stringToLong(TimeUtil.longToString(date));
    }

    /**
     * 本月
     *
     * @return
     */
    public static Long getCurMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTimeInMillis();
    }

    /**
     * 本周
     *
     * @return
     */
    public static Long getCurWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c.getTimeInMillis();
    }

    /**
     * 计算两个long型的时间之间的差（**天 ** 小时 ** 分钟 **秒）
     *
     * @param starttime 开始时间
     * @param endtime   结束时间
     * @return
     */
    public static String getTimeDiff2(long starttime, long endtime) {
        StringBuffer time = new StringBuffer();
        if (endtime > starttime) {
            long left = endtime - starttime;
            long daynum = left / (24 * 60 * 60 * 1000);
            left = left - daynum * (24 * 60 * 60 * 1000);
            long hournum = left / (3600 * 1000);
            left = left - hournum * (3600 * 1000);
            long minnum = left / (60 * 1000);
            left = left - minnum * (60 * 1000);
            long secnum = left / 1000;
            if (daynum > 0) {
                time.append(daynum + " 天 ");
            }
            if (hournum > 0) {
                time.append(hournum + " 小时 ");
            }
            if (minnum > 0) {
                time.append(minnum + " 分钟 ");
            }
            if (secnum > 0) {
                time.append(secnum + " 秒");
            }
        }
        return time.toString();
    }

    /**
     * 计算时间之间的差（**天 ** 小时 ** 分钟 **秒）
     *
     * @param duration 毫秒
     * @return
     */
    public static String intToStringTimeDiff(int duration) {
        StringBuffer time = new StringBuffer();
        long left = duration;
        long daynum = left / (24 * 60 * 60 * 1000);
        left = left - daynum * (24 * 60 * 60 * 1000);
        long hournum = left / (3600 * 1000);
        left = left - hournum * (3600 * 1000);
        long minnum = left / (60 * 1000);
        left = left - minnum * (60 * 1000);
        long secnum = left / 1000;
        if (daynum > 0) {
            time.append(daynum + " 天 ");
        }
        if (hournum > 0) {
            time.append(hournum + " 小时 ");
        }
        if (minnum > 0) {
            time.append(minnum + " 分钟 ");
        }
        if (secnum > 0) {
            time.append(secnum + " 秒");
        }
        return time.toString();
    }

    /**
     * 得到两个日期之间的所有年份
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Integer> getYears(Long startDate, Long endDate) {
        if (startDate == null) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startDate);
        if (endDate == null) {
            endDate = new Date().getTime();
        }
        List<Integer> list = new ArrayList<>();
        for (Long date = startDate; date < endDate; date = getPreOrNextYear(date, 1)) {
            list.add(Integer.valueOf(TimeUtil.longToString(date, "yyyy")));
        }
        return list;
    }

    /**
     * 得到两个日期之间的所有月份
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getMonths(Long startDate, Long endDate) {
        if (startDate == null) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startDate);
        if (endDate == null) {
            endDate = new Date().getTime();
        }
        List<String> list = new ArrayList<String>();
        for (Long date = startDate; date < endDate; date = getPreOrNextMonth(date, 1)) {
            list.add(TimeUtil.longToString(date, "yyyy-MM"));
        }
        return list;
    }

    /**
     * 当前月份(1-12)
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /***
     * 当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Calendar getYearFirstDay(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        setMinTime(calendar);

        return calendar;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Calendar getYearLastDay(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        setMaxTime(calendar);

        return calendar;
    }

    /**
     * 当前月份最后一天中的最后一秒
     *
     * @param year
     * @param month
     * @return
     */
    public static Calendar getEndDayofMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(cal);

        return cal;
    }

    /**
     * 取得季度第一天
     *
     * @param date    时间
     * @param quarter 季度[1 2 3 4]
     * @return
     */
    public static Calendar getFirstDateByQuarter(Date date, int quarter) {
        if (quarter < 1 || quarter > 4) {
            quarter = 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, quarter * 3 - 3);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setMinTime(calendar);

        return calendar;
    }

    /**
     * 取得季度最后一天
     *
     * @param date    时间
     * @param quarter 季度[1 2 3 4]
     * @return
     */
    public static Calendar getLastDateByQuarter(Date date, int quarter) {
        if (quarter < 1 || quarter > 4) {
            quarter = 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, quarter * 3 - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(calendar);

        return calendar;
    }

    private static void setMinTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setMaxTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
    }

    //当前日期的前n天到零点
    public static Long thisDateLastDate(Integer num){
        SimpleDateFormat format = null;
        Date date = null;
        Calendar myDate = Calendar.getInstance();
        myDate.add(Calendar.DAY_OF_MONTH, -3);
        date = myDate.getTime();
        format = new SimpleDateFormat(FORMAT_DATE);
        String rtnYes = format.format(date);
        System.out.println(rtnYes);
        return stringToLong(rtnYes,FORMAT_DATE);
    }

    //当前日期加1天
    public static Long todayOneMore(){
        Format f = new SimpleDateFormat(FORMAT_DATETIME);
        Date today = new Date();
//        System.out.println("今天是:" + f.format(today));

        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天

        Date tomorrow = c.getTime();
//        System.out.println("明天是:" + f.format(tomorrow));
        return tomorrow.getTime();
    }

    /**
     * 取得某年某月的第一天
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
    }

    /**
     * 取得某年某月的最后一天
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));
        return new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
    }


    /**
     * 获取指定日期所在月份开始的时间
     * lkeji
     * @return
     */
    public static Long getMonthBegin(String specifiedDay) {
        Date data = null;
        try {
            data = new SimpleDateFormat("yyyy-MM").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND,0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 本月第一天的时间戳转换为字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(sdf.format(new Date(new Long(c.getTimeInMillis()))));
            //Date date = sdf.parse(sdf.format(new Long(s)));// 等价于
            System.out.println("第一天："+sdf.format(date));
            return stringToLong(sdf.format(date),FORMAT_DATETIME_FULL);
        } catch (NumberFormatException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取指定日期所在月份结束的时间
     * @return
     */
    public static Long getMonthEnd(String specifiedDay) {
        Date data = null;
        try {
            data = new SimpleDateFormat("yyyy-MM").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(data);

        //设置为当月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        c.set(Calendar.MINUTE, 59);
        //将秒至59
        c.set(Calendar.SECOND, 59);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 999);
        // 本月第一天的时间戳转换为字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(sdf.format(new Date(new Long(c.getTimeInMillis()))));
            //Date date = sdf.parse(sdf.format(new Long(s)));// 等价于
            System.out.println("最后一天："+sdf.format(date));
            return stringToLong(sdf.format(date),FORMAT_DATETIME_FULL);
        } catch (NumberFormatException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取指定日期所在月份结束的时间
     * @return
     */
    public static Long getTodayTime(Integer type) {
        long current=System.currentTimeMillis();    //当前时间毫秒数
        long zeroT=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();  //今天零点零分零秒的毫秒数
        String zero = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(zeroT);
        long endT=zeroT+24*60*60*1000-1;  //今天23点59分59秒的毫秒数
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endT);
        if(type == null || type == 0){
            return zeroT;
        }else{
            return endT;
        }
    }


    public static void main(String[] args) {
/*        System.out.println(TimeUtil.stringToLong("2018-12-23", "yyyy-MM-dd"));
        System.out.println(TimeUtil.intToStringTimeDiff(45000));*/

        /*Calendar calendar = getYearFirstDay(2019);
        System.out.println(longToString(calendar.getTimeInMillis(), FORMAT_DATETIME_FULL));
        calendar = getYearLastDay(2019);
        System.out.println(longToString(calendar.getTimeInMillis(), FORMAT_DATETIME_FULL));
        int year = 2018;
        Calendar firstDayYear = TimeUtil.getYearFirstDay(year);
        Calendar lastDayYear = TimeUtil.getYearLastDay(year);
        if (year == (TimeUtil.getCurrentYear())) {
            int currentMonth = TimeUtil.getCurrentMonth();
            lastDayYear = TimeUtil.getEndDayofMonth(year, currentMonth);
        }
        System.out.println(firstDayYear.getTimeInMillis());
        System.out.println(longToString(firstDayYear.getTimeInMillis(), FORMAT_DATETIME_FULL));
        System.out.println(lastDayYear.getTimeInMillis());
        System.out.println(longToString(lastDayYear.getTimeInMillis(), FORMAT_DATETIME_FULL));


        Calendar startCalendar = Calendar.getInstance();

        startCalendar = getFirstDateByQuarter(firstDayYear.getTime(), 4);
        System.out.println(longToString(startCalendar.getTimeInMillis(), FORMAT_DATETIME_FULL));
        startCalendar = getLastDateByQuarter(firstDayYear.getTime(), 4);
        System.out.println(longToString(startCalendar.getTimeInMillis(), FORMAT_DATETIME_FULL));

        System.out.println(getYears(stringToLong("2010-01-01"), dateToLong()));*/

//        System.out.println(thisDateLastDate(3));
//        System.out.println(todayOneMore());
//        String firstTime = getFirstDayOfMonth(2020,4);
//        String lastTime = getLastDayOfMonth(2020,4);
//        System.out.println(firstTime);
//        System.out.println(lastTime);
//        System.out.println(stringToLong(firstTime,FORMAT_DATE));
//        System.out.println(stringToLong(lastTime,FORMAT_DATE)+(24*60*60*1000-1));
//        System.out.println(longToString(1588262399999l,FORMAT_DATETIME_FULL));

        System.out.println(getMonthBegin("2020-04"));
        System.out.println(getMonthEnd("2020-04"));

        System.out.println(getTodayTime(0));
        System.out.println(getTodayTime(1));


        System.out.println(longToString(1586966400000l,FORMAT_DATETIME_FULL));
        System.out.println(longToString(1587052799999l,FORMAT_DATETIME_FULL));
    }
}