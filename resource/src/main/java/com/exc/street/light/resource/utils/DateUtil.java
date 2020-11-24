package com.exc.street.light.resource.utils;

import com.exc.street.light.resource.vo.TimeVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 *
 * @author Longshuangyang
 * @date 2020/03/28
 */
public class DateUtil {

    private static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
    private static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static TimeVO nearlyWeek(Integer stage) {
        TimeVO timeVO = new TimeVO();
        Calendar curStartCal = Calendar.getInstance();
        String timeZone = "GMT+8:00";
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String firstDay = null;
        String lastDay = null;
        if (stage == 1) {
            // 获取上一个星期的周一周末时间
            int weekDay = curStartCal.get(Calendar.DAY_OF_WEEK);
            curStartCal.add(Calendar.DATE, -weekDay);
            curStartCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            firstDay = formatDate.format(curStartCal.getTime()) + " 00:00:00";
            curStartCal.add(Calendar.DATE, 6);
            lastDay = formatDate.format(curStartCal.getTime()) + " 23:59:59";
        } else if (stage == 2) {
            // 获取上一个月的第一天和最后一天时间
            int monthDay = curStartCal.get(Calendar.DAY_OF_MONTH);
            curStartCal.add(Calendar.DATE, -monthDay);
            curStartCal.set(Calendar.DAY_OF_MONTH, curStartCal.getActualMinimum(Calendar.DAY_OF_MONTH));
            firstDay = formatDate.format(curStartCal.getTime()) + " 00:00:00";
            curStartCal.set(Calendar.DAY_OF_MONTH, curStartCal.getActualMaximum(Calendar.DAY_OF_MONTH));
            lastDay = formatDate.format(curStartCal.getTime()) + " 23:59:59";
        } else if (stage == 3) {
            // 获取上一年的第一天和最后一天时间
            int yearDay = curStartCal.get(Calendar.DAY_OF_YEAR);
            curStartCal.add(Calendar.DATE, -yearDay);
            curStartCal.set(Calendar.DAY_OF_YEAR, curStartCal.getActualMinimum(Calendar.DAY_OF_YEAR));
            firstDay = formatDate.format(curStartCal.getTime()) + " 00:00:00";
            curStartCal.set(Calendar.DAY_OF_YEAR, curStartCal.getActualMaximum(Calendar.DAY_OF_YEAR));
            lastDay = formatDate.format(curStartCal.getTime()) + " 23:59:59";
        } else if (stage == 4) {
            // 获取获取今天的开始时间和结束时间
            String newDate = formatDate.format(curStartCal.getTime());
            firstDay = newDate + " 00:00:00";
            lastDay = newDate + " 23:59:59";
        } else if (stage == 5) {
            // 获取近7天（包含今天）的开始时间和结束时间
            lastDay = formatDate.format(curStartCal.getTime()) + " 23:59:59";
            curStartCal.add(Calendar.DATE, -6);
            firstDay = formatDate.format(curStartCal.getTime()) + " 00:00:00";
        } else if (stage == 6) {
            // 获取近30天（包含今天）的开始时间和结束时间
            lastDay = formatDate.format(curStartCal.getTime()) + " 23:59:59";
            curStartCal.add(Calendar.DATE, -29);
            firstDay = formatDate.format(curStartCal.getTime()) + " 00:00:00";
        }
        // 插入对象中
        if (firstDay != null && lastDay != null) {
            timeVO.setStartTimeString(firstDay);
            timeVO.setEndTimeString(lastDay);
            try {
                timeVO.setStartTime(formatTime.parse(firstDay));
                timeVO.setEndTime(formatTime.parse(lastDay));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return timeVO;
    }

    public static void main(String[] args) {
        System.out.println(nearlyWeek(6));
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
        //获取星期几 此处的周日为一个星期的初始，当为星期天时减去一天
        int dayOfWeek = curStartCal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            curStartCal.add(Calendar.DATE, -1);
        }
        curStartCal.add(Calendar.DATE, num * 7);
        if (isBegin) {
            curStartCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            curStartCal.set(Calendar.HOUR_OF_DAY, 0);
            curStartCal.set(Calendar.MINUTE, 0);
            curStartCal.set(Calendar.SECOND, 0);
        } else {
            curStartCal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            //结束日期为本周星期六加一天
            curStartCal.add(Calendar.DATE, 1);
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
    public static Date getDayBeginOrEnd(int num, boolean isBegin) {
        Calendar curStartCal = Calendar.getInstance();
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

    public static Date getDayBeginOrEndBystr(String dateStr, boolean isBegin) throws ParseException {
        Date date = shortSdf.parse(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, isBegin ? 0 : 23);
        c.set(Calendar.MINUTE, isBegin ? 0 : 59);
        c.set(Calendar.SECOND, isBegin ? 0 : 59);
        c.set(Calendar.MILLISECOND, isBegin ? 0 : 999);
        return c.getTime();
    }
}
