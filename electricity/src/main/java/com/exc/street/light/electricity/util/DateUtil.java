package com.exc.street.light.electricity.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Author:xujiahao
 * @Description long and time switch
 * @Data:Created in 15:52 2018/2/1
 * @Modified By:
 */
public class DateUtil {
    /**
     * @param mss 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        return days + " days " + hours + " hours " + minutes + " minutes "
                + seconds + " seconds ";
    }

    /**
     * 毫秒值转Data
     *
     * @param ms
     * @return
     */
    public static Date long2Date(long ms) {
        //获得当前时间和当前时间前30秒时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = new GregorianCalendar();
        Date date = new Date();
        c.setTime(date);
        //设置参数时间
        c.add(Calendar.SECOND, -(int) (ms / 1000));
        // 把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date = c.getTime();
        // 这个时间就是日期往后推一天的结果
        return date;
    }

    /**
     * 日期转String
     *
     * @param date
     * @return
     */
    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
