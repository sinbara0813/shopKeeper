package com.d2cmall.shopkeeper.utils;

import android.text.format.Time;

import com.d2cmall.shopkeeper.common.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间格式化工具类
 * Author: Blend
 * Date: 16/6/5 01:06
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public class DateUtil {

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(Constants.DATE_FORMAT_LONG1);
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(Constants.DATE_FORMAT_SHORT);
        }
    };

    private static boolean isInEasternEightZones() {
        return TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
    }

    private static boolean isTodayYear(long time) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = new Date(time);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            return true;
        }
        return false;
    }

    private static Date transformTime(Date date, TimeZone oldZone, TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }

    public static Date formatDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(strDate, pos);
        return date;
    }

    public static Date defaultFormatDate(String strDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(strDate, pos);
        return date;
    }

    public static String formatString(Date date,int type) {
        DateFormat df=null;
        if (type==1){
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }else {
            df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        }
        return df.format(date);
    }

    public static int compareToday(long when){
        Time time = new Time();
        time.set(when);

        int thenYear = time.year;
        int thenMonth = time.month;
        int thenMonthDay = time.monthDay;

        time.set(System.currentTimeMillis());
        if (thenYear<time.year){
            return -1;
        }else if (thenYear>time.year){
            return 1;
        }else {
           if (thenMonth<time.month){
               return -1;
           }else if (thenMonth>time.month){
               return 1;
           }else {
               if (thenMonthDay<time.monthDay){
                   return -1;
               }else if (thenMonthDay>time.monthDay){
                   return 1;
               }else {
                   return 0;
               }
           }
        }
    }

    public static String getExpireTime(String time){
        String result="";
        Date date=toDate(time);
        Date date1=new Date();
        if (date.getTime()<date1.getTime()){
            Date date2=new Date(date1.getTime()+1200000);
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
            result=dateFormat.format(date2);
        }else {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
            result=dateFormat.format(DateUtil.defaultFormatDate(time));
        }
        return result;
    }

    public static boolean isSameDate(Date date1, Date date2) {

        Calendar cal1 = Calendar.getInstance();

        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();

        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

        boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

        return isSameDate;

    }

    public static String getFutureDate(String baseDate,int futureDays){
        Date d=toDate(baseDate);
        if (d==null)return "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DAY_OF_YEAR, futureDays);
        Date date = calendar.getTime();
        return dateFormater.get().format(date);
    }

    public static String getToday(){
        StringBuilder builder=new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        builder.append(addZero(currentYear)).append("-").append(addZero(currentMonth)).append("-").append(addZero(currentDay));
        builder.append(" 00:00:00");
        return builder.toString();
    }

    public static String addZero(int num){
        if (num<10){
            return "0"+num;
        }
        return String.valueOf(num);
    }

    public static Date toDate(String sdate) {
        return toDate(sdate, dateFormater.get());
    }

    public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
        try {
            return dateFormater.parse(sdate);
        } catch (ParseException e) {
            SimpleDateFormat dateFormat=new SimpleDateFormat(Constants.DATE_FORMAT_LONG);
            try {
                return dateFormat.parse(sdate);
            } catch (ParseException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    public static String getTimeFromNow(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {//同一天
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                long minute = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1);
                if (minute >= 0 && minute <= 10) {
                    ftime = "刚刚";
                } else {
                    ftime = minute + "分钟前";
                }
            } else {
                ftime = hour + "小时前";
            }
            return ftime;
        }else {
            SimpleDateFormat dateFormat=new SimpleDateFormat("MM月dd日 HH:mm");
            return dateFormat.format(time);
        }
    }

}
