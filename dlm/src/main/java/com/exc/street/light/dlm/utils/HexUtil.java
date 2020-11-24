package com.exc.street.light.dlm.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * 16进制工具类
 *
 * @author LinShiWen
 * @date 2017/10/10
 */
public class HexUtil {


    /**
     * 将int转为16进制的字节
     */
    public static byte getByte(int num) {
        String s = Integer.toHexString(num);
        if (s.length() == 1) {
            s = "0" + s;
        }
        byte[] bytes = hexStringToBytes(s.toUpperCase());
        return bytes[0];
    }

    /**
     * 将int转为16进制的字节
     */
    public static byte getHex(int num) {
        String s1 = String.valueOf(num);
        int i = Integer.parseInt(s1, 16);
        String s = Integer.toHexString(i);
        if (s.length() == 1) {
            s = "0" + s;
        }
        byte[] bytes = hexStringToBytes(s.toUpperCase());
        return bytes[0];
    }


    /**
     * 将int转为16进制的字符串
     */
    public static String toHex(int num) {
        String s = Integer.toHexString(num);
        if (s.length() % 2 == 1) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
     *
     * @param src0
     * @param src1
     * @return
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        byte ret = (byte) (b0 ^ b1);
        return ret;
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
     * 0xD9}
     */
    public static byte[] hexStringToBytes(String src) {
        int len = src.length() / 2;
        byte[] ret = new byte[len];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < len; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 将数组转为16进制的字符串
     *
     * @param bytes
     * @return
     */
    public static String bytesTohex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1) {
                // 每个字节8位，转为16进制标志，2个16进制位
                tmp = "0" + tmp;
            }
            sb.append(tmp.toUpperCase()).append(" ");
        }
        return sb.toString();
    }

    /**
     * 将数组转为字符串
     *
     * @param bytes
     * @return
     */
    public static String bytesToString(byte[] bytes, String codingType) {
        String s = null;
        try {
            s = new String(bytes, codingType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 字节转十六进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 字节数组转为普通字符串（ASCII对应的字符）
     *
     * @param array byte[]
     * @return String
     */
    public static String byte2String(byte[] array) {
        String result = "";
        char temp;

        int length = array.length;
        for (int i = 0; i < length; i++) {
            temp = (char) array[i];
            result += temp;
        }
        return result;
    }

    /**
     * byte数组转int
     *
     * @param bytes
     * @return
     */
    public static Integer bytesToInt(byte... bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value = value | ((bytes[i] & 0xff)) << (24 - i * 8);
        }
        return value;
    }

    public static Integer bytesToInteger(byte... bytes) {
        return Integer.parseInt(cn.hutool.core.util.HexUtil.encodeHexStr(bytes), 16);
    }

    /**
     * 截取byte数组返回
     *
     * @param srcBytes
     * @param beginIndex
     * @param length
     * @return
     */
    public static byte[] getByteArr(byte[] srcBytes, int beginIndex, int length) {
        if (length < 1) {
            return new byte[0];
        }
        byte[] destBytes = new byte[length];
        System.arraycopy(srcBytes, beginIndex, destBytes, 0, length);
        return destBytes;
    }


    /**
     * byte数组转float
     *
     * @param b
     * @param x 保留小数位
     * @return
     */
    public static Float bytesToFloat(byte[] b, int x) {
        Long l = Long.parseLong(bytesTohex(b).replaceAll(" ", ""), 16);
        return new BigDecimal(Float.intBitsToFloat(l.intValue())).setScale(x, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
