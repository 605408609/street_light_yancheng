package com.exc.street.light.resource.utils;

import com.exc.street.light.resource.quartz.TimerDTO;
import org.quartz.impl.calendar.DailyCalendar;
import org.quartz.impl.calendar.WeeklyCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author Longshuangyang
 * @date 2020/03/28
 */
public class TimeUtil {
    public static byte[] setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        calendar.setTimeZone(timeZone);
        byte[] bytes = new byte[16];
        //获取年份
        int year = calendar.get(Calendar.YEAR);
        //获取年份的高8位
        bytes[0] = (byte) (year & 0xFF);
        //获取年份的低8位
        bytes[1] = (byte) (year >> 8 & 0xFF);
        //获取月份
        int month = calendar.get(Calendar.MONTH) + 1;
        bytes[2] = (byte) month;
        bytes[3] = 0x00;
        //周几
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        bytes[4] = (byte) week;
        bytes[5] = 0x00;
        //获取所在日
        int day = calendar.get(Calendar.DATE);
        bytes[6] = (byte) day;
        bytes[7] = 0x00;
        //获取小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        bytes[8] = (byte) hour;
        bytes[9] = 0x00;
        //获取分钟数
        int minute = calendar.get(Calendar.MINUTE);
        bytes[10] = (byte) minute;
        bytes[11] = 0x00;
        //获取秒数
        int second = calendar.get(Calendar.SECOND);
        bytes[12] = (byte) second;
        bytes[13] = 0x00;
        //获取毫秒
        int millisecond = calendar.get(Calendar.MILLISECOND);
        bytes[14] = (byte) (millisecond & 0xFF);
        bytes[15] = (byte) (millisecond >> 8 & 0xFF);
        return bytes;
    }

    /**
     * 获取当前时刻到当天0点的毫秒数
     *
     * @return
     */
    public static long getCurrentDayMillis() {
        long currentTimeMillis = System.currentTimeMillis();
        long zero = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        return currentTimeMillis - zero;
    }

    /**
     * 获取 HH:mm:ss 的毫秒数
     *
     * @param date
     * @return
     */
    public static long getMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //获取秒数
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);
        long millis = ((hour * 60 + minute) * 60 + second) * 1000 + millisecond;
        return millis;
    }

    public static WeeklyCalendar getWeeklyCalendar(TimerDTO dto) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        DailyCalendar dailyCalendar = new DailyCalendar(sdf.format(dto.getBeginDate()), sdf.format(dto.getEndDate()));
        dailyCalendar.setInvertTimeRange(true);
        WeeklyCalendar weeklyCalendar = new WeeklyCalendar(dailyCalendar);
        //设置不触发
        boolean[] weekDays = {true, true, true, true, true, true, true, true};
        weeklyCalendar.setDaysExcluded(weekDays);
        Integer[] cycleTypes = dto.getCycleTypes();
        for (int i = 0; i < cycleTypes.length; i++) {
            Integer cycleType = cycleTypes[i];
            if (cycleType == 1) {
                weeklyCalendar.setDayExcluded(Calendar.SUNDAY, false);
            }
            if (cycleType == 2) {
                weeklyCalendar.setDayExcluded(Calendar.MONDAY, false);
            }
            if (cycleType == 3) {
                weeklyCalendar.setDayExcluded(Calendar.TUESDAY, false);
            }
            if (cycleType == 4) {
                weeklyCalendar.setDayExcluded(Calendar.WEDNESDAY, false);
            }
            if (cycleType == 5) {
                weeklyCalendar.setDayExcluded(Calendar.THURSDAY, false);
            }
            if (cycleType == 6) {
                weeklyCalendar.setDayExcluded(Calendar.FRIDAY, false);
            }
            if (cycleType == 7) {
                weeklyCalendar.setDayExcluded(Calendar.SATURDAY, false);
            }
        }
        return weeklyCalendar;
    }

    public static Date getDate(String date, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = date + " " + time;
        Date parse = null;
        try {
            parse = sdf.parse(date);
        } catch (ParseException e) {

        }
        return parse;
    }

}
