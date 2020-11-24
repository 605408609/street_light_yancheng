package com.exc.street.light.electricity.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数组工具类
 *
 * @author LinShiWen
 * @date 2018/4/23
 */
public class ArrayUtil {
    /**
     * 获取内容帧数据内容区长度
     * 合广 12 - 13
     * 自研 9 - 10
     *
     * @param bytes 字节数组
     * @return 返回内容帧数据内容区长度
     */
    public static int getContentLen(byte[] bytes) {
        // high 8 bits
        int high = bytes[9];
        // low 8 bits
        int low = bytes[10];
        high = high & 0xff;
        low = low & 0xff;
        high = high << 8;
        int num = high | low;
        num = num & 0x0fff;
        return num;
    }

    /**
     * 获取内容区变化数据条数
     *
     * @param bytes
     * @return
     */
    public static int getContentNum(byte[] bytes) {
        // high 8 bits
        int high = bytes[11];
        // low 8 bits
        int low = bytes[12];
        high = high & 0xff;
        low = low & 0xff;
        high = high << 8;
        int num = high | low;
        num = num & 0x0fff;
        return num;
    }

    /**
     * 获取电表数据条数
     * @param bytes
     * @return
     */
    public static int getElectricMeterContentLen(byte[] bytes) {
        // high 8 bits
        int high = bytes[10];
        // low 8 bits
        int low = bytes[11];
        high = high & 0xff;
        low = low & 0xff;
        high = high << 8;
        int num = high | low;
        num = num & 0x0fff;
        return num;
    }

    /**
     * 2个byte转为int类型
     *
     * @param bytes 字节数组
     * @return 返回int数值
     */
    public static int getValue(byte... bytes) {
        // high 8 bits
        int high = bytes[0];
        // low 8 bits
        int low = bytes[1];
        high = high & 0xff;
        low = low & 0xff;
        high = high << 8;
        int num = high | low;
        return num;
    }

    /**
     * 获取测点时间
     *
     * @param bytes 字节数组
     * @return 返回int数值
     */
    public static Date getTime(byte... bytes) {
        // high 8 bits
        int high = bytes[0];
        // low 8 bits
        int low = bytes[1];
        int low1 = bytes[2];
        int low2 = bytes[3];
        high = high & 0xff;
        low = low & 0xff;
        low1 = low1 & 0xff;
        low2 = low2 & 0xff;
        high = high << 24;
        low = low << 16;
        low1 = low1 << 8;
        int num = high | low | low1 | low2;
        Date date1 = new Date();
        System.out.println(date1.getTime());
        //设备获取的是东八区的秒数,转换成格林威治毫秒数
        long time = (long) num * 1000 - 28800000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        System.out.println(sdf.format(date));
        return date;
    }

    /**
     * 获取设备物理地址
     * 合广:下面
     * 自研: 2-8
     */
    public static String getAddress(byte[] protocol) {
        StringBuilder sb = new StringBuilder(12);
        //物理地址开始索引
        int addressBeginIndex = 2;
        //物理地址结束索引
        int addressEndIndex = 8;
        for (int i = addressBeginIndex; i < addressEndIndex; i++) {
            int b = protocol[i];
            b = b & 0xff;
            String hexString = Integer.toHexString(b);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            sb.append(hexString.toUpperCase());
            if (i < 7) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}
