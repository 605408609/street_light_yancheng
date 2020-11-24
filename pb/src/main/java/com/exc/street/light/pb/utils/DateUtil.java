package com.exc.street.light.pb.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    private static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
    private static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
    }

    /**
     * 获取季度的开始时间
     *
     * @param num -1：上一季度 0当前季度 1下一季度
     * @return
     */
    public static Date getQuarterStartTime(Integer num) {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            if (num != null) {
                c.add(Calendar.MONTH, 3 * num);
            }
            //获取某月最小天数
            int firstDay = c.getActualMinimum(Calendar.DAY_OF_MONTH);
            c.set(Calendar.DATE, firstDay);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取季度的最后时间
     *
     * @param num -1：上一季度 0当前季度 1下一季度
     * @return
     */
    public static Date getQuarterEndTime(Integer num) {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 2);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 5);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 8);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 11);
            if (num != null) {
                c.add(Calendar.MONTH, 3 * num);
            }
            //获取某月最小天数
            int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            c.set(Calendar.DATE, lastDay);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取月份的开始或结束时间
     *
     * @param num     -1：上一季度 0当前季度 1下一季度 以此类推
     * @param isStart true 季度的开始时间 false季度的结束时间
     * @return
     */
    public static Date getMonthTime(Integer num, boolean isStart) {
        Calendar c = Calendar.getInstance();
        Date now = null;
        c.add(Calendar.MONTH, num);
        try {
            if (isStart) {
                //获取某月最小天数
                c.set(Calendar.DATE, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
            } else {
                //获取最大天数
                c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取一周中的周一或者周日
     *
     * @param num     -1 上一个星期 0本星期 1下星期 以此类推
     * @param isBegin true 周一 false 周日
     * @return
     */
    public static Date getWeekBeginOrEnd(int num, boolean isBegin) {
        Calendar curStartCal = Calendar.getInstance();
        curStartCal.add(Calendar.DATE, num * 7);
        if (isBegin) {
            curStartCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            curStartCal.set(Calendar.HOUR_OF_DAY, 0);
            curStartCal.set(Calendar.MINUTE, 0);
            curStartCal.set(Calendar.SECOND, 0);
        } else {
            curStartCal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            curStartCal.add(Calendar.DAY_OF_MONTH, 1);
            curStartCal.set(Calendar.HOUR_OF_DAY, 23);
            curStartCal.set(Calendar.MINUTE, 59);
            curStartCal.set(Calendar.SECOND, 59);
        }
        return curStartCal.getTime();
    }

    /**
     * 获取一天内的开始时间或结束时间
     *
     * @param num     -1 前一天 0今天 1后一天 以此类推
     * @param isBegin true 00:00:00 false 23:59:59
     * @return
     */
    public static Date getDayBeginOrEnd(int num, boolean isBegin, Date date) {
        Calendar curStartCal = Calendar.getInstance();
        if (date != null) {
            curStartCal.setTime(date);
        }
        curStartCal.add(Calendar.DAY_OF_MONTH, num);
        if (isBegin) {
            curStartCal.set(Calendar.HOUR_OF_DAY, 0);
            curStartCal.set(Calendar.MINUTE, 0);
            curStartCal.set(Calendar.SECOND, 0);
        } else {
            curStartCal.set(Calendar.HOUR_OF_DAY, 23);
            curStartCal.set(Calendar.MINUTE, 59);
            curStartCal.set(Calendar.SECOND, 59);
        }
        return curStartCal.getTime();
    }

    /**
     * 获取一年内的开始时间或结束时间
     *
     * @param num     -1 上一个年 0本年 1下一年 以此类推
     * @param isBegin true 1月1日 false 一年内的最后一天
     * @return
     */
    public static Date getYearBeginOrEnd(int num, boolean isBegin) {
        Calendar curStartCal = Calendar.getInstance();
        curStartCal.add(Calendar.YEAR, num);
        if (isBegin) {
            curStartCal.set(Calendar.MONTH, 0);
            curStartCal.set(Calendar.DATE, curStartCal.getActualMinimum(Calendar.DAY_OF_MONTH));
            curStartCal.set(Calendar.HOUR_OF_DAY, 0);
            curStartCal.set(Calendar.MINUTE, 0);
            curStartCal.set(Calendar.SECOND, 0);
        } else {
            curStartCal.set(Calendar.MONTH, 11);
            curStartCal.set(Calendar.DATE, curStartCal.getActualMaximum(Calendar.DAY_OF_MONTH));
            curStartCal.set(Calendar.HOUR_OF_DAY, 23);
            curStartCal.set(Calendar.MINUTE, 59);
            curStartCal.set(Calendar.SECOND, 59);
        }
        return curStartCal.getTime();
    }

    /**
     * 秒数转时长  3601 -> 01:00:01
     *
     * @param second
     * @return
     */
    public static String secondToTime(Integer second) {
        if (second == null) {
            return null;
        }
        //long days = second / (60 * 60 * 24);
        Integer hours = (second / (60 * 60));
        Integer minutes = (second % (60 * 60)) / (60);
        Integer seconds = (second % (60));

        String hoursStr = hours < 10 ? "0" + hours : hours + "";
        String minutesStr = minutes < 10 ? "0" + minutes : minutes + "";
        String secondsStr = seconds < 10 ? "0" + seconds : seconds + "";
        return hoursStr + ":" + minutesStr + ":" + secondsStr;
    }


}
