package com.exc.street.light.electricity.util;

/**
 * 字符串工具类
 *
 * @author LinShiWen
 * @date 2018/4/20
 */
public class StringUtil {

    /**
     * 去掉字符串的空字符
     *
     * @param data
     * @return
     */
    public static String trimBlank(String data) {
        return data.replaceAll(" ", "");
    }

    /**
     * 获取设备地址的字节数组
     *
     * @param address 设备地址
     * @return 设备地址的字节数组
     */
    public static byte[] getAddress(String address) {
        address = address.replaceAll("-", "");
        address = address.replaceAll(":","");
        return HexUtil.hexStringToBytes(address);
    }

    /**
     *  String左对齐
     */
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }
    /**
     *  String右对齐
     */
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

}
