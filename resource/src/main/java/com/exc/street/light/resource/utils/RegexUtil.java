package com.exc.street.light.resource.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author Linshiwen
 * @date 2018/5/25
 */
public class RegexUtil {
    /**
     * 判断IP是否合法
     *
     * @param ipAddress IP地址
     * @return 判断结果
     */
    public static boolean isboolIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }


    /**
     * 判断手机号是否合法
     * @param telephone
     * @return 判断结果
     */
    public static boolean isPhone(String telephone) {
        String phone = "[1](([3][0-9])|([4][5,7,9])|([5][0-9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}";
        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(telephone);
        return matcher.matches();
    }

    /**
     * 判断经度是否合法
     *
     * @param longitude 经度
     * @return 判断结果
     */
    public static boolean isLongitude(String longitude) {
        String longitudePatten = "(\\-?)(?:[0-9]|[1-9][0-9]|1[0-7][0-9])\\.([0-9]{1,17})|(180\\.0{1,17})";
        Pattern pattern = Pattern.compile(longitudePatten);
        Matcher matcher = pattern.matcher(longitude);
        return matcher.matches();
    }

    /**
     * 判断纬度是否合法
     *
     * @param latitude 纬度
     * @return 判断结果
     */
    public static boolean isLatitude(String latitude) {
        String latitudePatten = "(\\-?)(?:[0-9]|[1-8][0-9])\\.([0-9]{1,17})|(90\\.0{1,17})";
        Pattern pattern = Pattern.compile(latitudePatten);
        Matcher matcher = pattern.matcher(latitude);
        return matcher.matches();
    }

}
