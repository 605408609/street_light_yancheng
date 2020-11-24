package com.exc.street.light.dlm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间工具类
 *
 * @author LinShiWen
 * @date 2017/10/10
 */
public class TimeUtil {
    /**
     * 获取格式为yyyy-MM-dd HH:mm:ss.SSS的时间,并转化为8个字节的16进制
     */
    public static byte[] getTime() {
        byte[] bytes = new byte[8];
        Calendar calendar = new GregorianCalendar();
        setTime(calendar, bytes);
        return bytes;
    }

    /**
     * 获取小时和分钟的字符串格式(HH:mm)
     *
     * @param date
     * @return
     */
    public static String getHourAndMinute(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String format = sdf.format(date);
        return format;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        return format;
    }

    public static String format2Date(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        return format;
    }

    /**
     * 获取格式为yyyy-MM-dd HH:mm:ss的时间,并转化为8个字节的16进制
     */
    public static byte[] getSysTime(String sysTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = sdf.parse(sysTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        byte[] bytes = new byte[8];
        setTime(calendar, bytes);
        return bytes;
    }

    /**
     * 设置时间
     *
     * @param calendar 日历
     * @param bytes    数组
     */
    private static void setTime(Calendar calendar, byte[] bytes) {
        //获取年份
        int year = calendar.get(Calendar.YEAR);
        //获取年份的高8位
        bytes[0] = HexUtil.getByte(year >> 8 & 0xff);
        //获取年份的低8位
        bytes[1] = HexUtil.getByte(year & 0xff);
        //获取月份
        int month = calendar.get(Calendar.MONTH) + 1;
        bytes[2] = HexUtil.getByte(month);
        //获取所在日
        int day = calendar.get(Calendar.DATE);
        bytes[3] = HexUtil.getByte(day);
        //获取小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        bytes[4] = HexUtil.getByte(hour);
        //获取分钟数
        int minute = calendar.get(Calendar.MINUTE);
        bytes[5] = HexUtil.getByte(minute);
        //获取秒数
        int second = calendar.get(Calendar.SECOND);
        bytes[6] = HexUtil.getByte(second);
        //获取毫秒
        int millisecond = calendar.get(Calendar.MILLISECOND);
        int unit = millisecond / 4;
        int remainder = millisecond % 4;
        if (remainder > 1) {
            unit += 1;
        }
        //获取毫秒的低8位
        bytes[7] = HexUtil.getByte(unit & 0xff);
    }

    /**
     * 获取日期
     *
     * @param date 日期
     * @param time 时间
     * @return
     */
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

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        // 把开始时间加入集合
        lDate.add(beginDate);
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        // 把结束时间加入集合
        lDate.add(endDate);
        return lDate;
    }

    public static String getHexTime(String[] times){
        if(times == null){
            throw new NullPointerException();
        }
        if(times.length != 3){
            throw new NumberFormatException();
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i =0;i<times.length;++i){
            if(i != times.length-1){
                stringBuffer.append(Integer2String(Integer.parseInt(times[i],16))).append(":");
            }else{
                stringBuffer.append(Integer2String(Integer.parseInt(times[i],16)));
            }
        }
      return String.valueOf(stringBuffer);
    }

    public static String Integer2String(Integer integer){
        if(integer < 10){
            return "0"+integer;
        }
        return ""+integer;
    }
}
