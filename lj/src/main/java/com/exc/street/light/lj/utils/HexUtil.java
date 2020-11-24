package com.exc.street.light.lj.utils;

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
    public static String getByte(int num) {
        String s = Integer.toHexString(num);
        if (s.length() == 2) {
            s = "00" + s;
        }else if(s.length()==1){
            s = "000" + s;
        }else if(s.length() == 3){
            s = "0" + s;
        }
        String s1 = s.substring(0, 2);
        String s2 = s.substring(2, 4);
        s= s2 +s1;

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
        StringBuilder temp = new StringBuilder();
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
     * int到byte[]
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        // 由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte[]到int
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    /**
     * int到16进制字符串（限2字节）
     * @param i
     * @return
     */
    public static String intToHexString(int i){
        byte[] bytes = HexUtil.intToByteArray(i);
        byte[] newBytes = {bytes[2],bytes[3]};
        return HexUtil.bytesTohex(newBytes).replace(" ", "");
    }

    /**
     * int到16进制字符串（限1字节）
     * @param i
     * @return
     */
    public static String intToHexStringOne(int i){
        byte[] bytes = HexUtil.intToByteArray(i);
        byte[] newBytes = {bytes[3]};
        return HexUtil.bytesTohex(newBytes).replace(" ", "");
    }

}
