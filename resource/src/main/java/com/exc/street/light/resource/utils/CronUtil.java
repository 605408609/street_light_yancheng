package com.exc.street.light.resource.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 定时工具类
 *
 * @author Longshuangyang
 * @date 2020/03/28
 */
public class CronUtil {
    private static final Logger logger = LoggerFactory.getLogger(CronUtil.class);

    /**
     * 获取指定时间的cron
     *
     * @param startTime
     * @return
     */
    public static String getCronCycle(String startTime) {
        StringBuilder sb = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //获取小时
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            //获取分钟数
            int minute = calendar.get(Calendar.MINUTE);
            //获取秒数
            int second = calendar.get(Calendar.SECOND);
            sb = new StringBuilder(20);
            sb.append(second).append(" ").append(minute).append(" ").append(hour)
                    .append(" ").append("*").append(" ").append("*").append(" ").append("?");
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * 获取日期的cron
     *
     * @param date
     * @return
     */
    public static String getDateCron(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //获取年份
        int year = calendar.get(Calendar.YEAR);
        //获取月份
        int month = calendar.get(Calendar.MONTH) + 1;
        //获取所在日
        int day = calendar.get(Calendar.DATE);
        //获取小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //获取分钟数
        int minute = calendar.get(Calendar.MINUTE);
        //获取秒数
        int second = calendar.get(Calendar.SECOND);
        StringBuilder sb = new StringBuilder(20);
        sb.append(second).append(" ").append(minute).append(" ").append(hour).append(" ").
                append(day).append(" ").append(month).append(" ").append("?").append(" ").append(year);
        return sb.toString();
    }

    /**
     * 获取星期的cron
     *
     * @param startTime
     * @param cycleTypes
     * @return
     */
    public static String getWeekCron(String startTime, Integer[] cycleTypes) {
        StringBuilder sb = new StringBuilder(20);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //获取开始小时
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            //获取开始分钟数
            int minute = calendar.get(Calendar.MINUTE);
            //获取开始秒数
            int second = calendar.get(Calendar.SECOND);
            sb.append(second).append(" ").append(minute).append(" ").append(hour).append(" ").append("? * ");
            for (int i = 0; i < cycleTypes.length; i++) {
                Integer cycleType = cycleTypes[i];
                sb.append(cycleType);
                if ((i + 1) < cycleTypes.length) {
                    sb.append(",");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * 指定时间和时间段结合的表达式
     * @param startTime
     * @param dataBegin
     * @param dataEnd
     * @return
     */
    public static String getTimeAndPhaseCron(String startTime, String dataBegin, String dataEnd) {
        StringBuilder sb = new StringBuilder(20);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //获取开始小时
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            //获取开始分钟数
            int minute = calendar.get(Calendar.MINUTE);
            //获取开始秒数
            int second = calendar.get(Calendar.SECOND);
            sb.append(second).append(" ").append(minute).append(" ").append(hour).append(" ");


//            for (int i = 0; i < cycleTypes.length; i++) {
//                Integer cycleType = cycleTypes[i];
//                sb.append(cycleType);
//                if ((i + 1) < cycleTypes.length) {
//                    sb.append(",");
//                }
//            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * 指定时间和时间段周期结合的表达式
     * @param startTime
     * @param dateTime
     * @return
     */
    public static String getWeekTimeAndPhaseCron(String startTime, List<String> dateTime) {
        StringBuilder sb = new StringBuilder(20);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(startTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //获取开始小时
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            //获取开始分钟数
            int minute = calendar.get(Calendar.MINUTE);
            //获取开始秒数
            int second = calendar.get(Calendar.SECOND);
            sb.append(second).append(" ").append(minute).append(" ").append(hour).append(" ").append("? * ");
//            for (int i = 0; i < cycleTypes.length; i++) {
//                Integer cycleType = cycleTypes[i];
//                sb.append(cycleType);
//                if ((i + 1) < cycleTypes.length) {
//                    sb.append(",");
//                }
//            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * 获取某段时间内的周一（二等等）的日期
     * @param dataBegin 开始日期
     * @param dataEnd 结束日期
     * @param weekDays 获取周几，1－7代表周日到周六
     * @return 返回日期List
     */
    public static List<String> getDayOfWeekWithinDateInterval(String dataBegin, String dataEnd, int[] weekDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateResult = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        String[] dateInterval = {dataBegin, dataEnd};
        Date[] dates = new Date[dateInterval.length];
        for (int i = 0; i < dateInterval.length; i++) {
            String[] ymd = dateInterval[i].split("[^\\d]+");
            cal.set(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]) - 1, Integer.parseInt(ymd[2]));
            dates[i] = cal.getTime();
        }
        for (Date date = dates[0]; date.compareTo(dates[1]) <= 0; ) {
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
            for (int i = 0; i < weekDays.length; i++) {
                if (cal.get(Calendar.DAY_OF_WEEK) == weekDays[i]) {
                    String format = sdf.format(date);
                    dateResult.add(format);
                }
            }
        }
        return dateResult;
    }

    public static void main(String[] args) throws ParseException {
//        String cronCycle = getCronCycle("12:34:33");
//        Scanner in = new Scanner(System.in);
//        try {
//            System.out.println(in.nextInt());
//        } catch (Exception e) {
//            System.out.println("出错");
//        }
        getTimeAndPhaseCron("17:20:32", "2020-09-02", "2020-09-03");
    }
}
