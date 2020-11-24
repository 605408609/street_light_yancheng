package com.exc.street.light.wifi.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Xiezhipeng
 * @Description 字符串处理工具类
 * @Date 2020/5/6
 */
public class StringDealUtils {

    /**
     * 截取oid最后六个整数，转换为十六进制，并拼接成Mac地址
     *
     * @param s
     * @return
     */
    public static String stringDeal(String s) {
        StringBuffer buffer = new StringBuffer();
        if (StringUtils.isNotBlank(s)) {
            String[] split = s.split("\\.");
            for (int i = 6; i > 0; i--) {
                if (Integer.parseInt(split[split.length - i]) < 16) {
                    buffer.append("0" + Integer.toHexString(Integer.parseInt(split[split.length - i])));
                } else {
                    buffer.append(Integer.toHexString(Integer.parseInt(split[split.length - i])));
                }
                if (i == 1) {
                    break;
                }
                buffer.append("-");
            }
        }
        return buffer.toString();
    }


    /**
     * 用于获取ap流量的oid，获取第一个口的流量，最后一位为1，去掉oid最后的一位，方便获取ap的Mac
     *
     * @param oid
     * @return
     */
    public static String deleteLastChar(String oid) {
        String mac = null;
        if (StringUtils.isNotBlank(oid)) {
            if (oid.substring(oid.length() - 1).equals("1")) {
                String newOid = oid.substring(0, oid.length() - 2);
                mac = stringDeal(newOid);
            }
        }
        return mac;
    }

    /**
     * ap与ac关联的在线时长格式：0:02:46:43，将其转换成分钟，后面的秒数忽略
     *
     * @param date
     * @return
     */
    public static Integer StringDateToMinute(String date) {
        int sumMinute = 0;
        if (StringUtils.isNotBlank(date)) {
            String[] split = date.split(":");
            int day = Integer.parseInt(split[0].trim());
            int hour = Integer.parseInt(split[1]);
            int minute = Integer.parseInt(split[2]);
            sumMinute = day * 24 * 60 + hour * 60 + minute;
        }
        return sumMinute;
    }

}
