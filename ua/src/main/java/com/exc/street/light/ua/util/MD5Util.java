package com.exc.street.light.ua.util;

public class MD5Util {

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }

    public static void main(String[] args) {
        String str = "123456";
        System.out.println(convertMD5(str));
        System.out.println(convertMD5(convertMD5(str)));
    }

}
