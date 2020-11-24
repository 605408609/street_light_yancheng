package com.exc.street.light.dlm.utils;

/**
 * @author LinShiWen
 * @date 2018/4/9
 */
public class FloatUtil {
    /**
     * 浮点转换为字节
     *
     * @param f
     * @return
     */
    public static byte[] float2byte(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);
        byte[] byteRet = new byte[4];
        for (int i = 0; i < 4; i++) {
            byteRet[i] = (byte) (fbit >> (24 - i * 8) & 0xff);
        }
        return byteRet;
    }

    /**
     * 字节转换为浮点
     *
     * @param arr 字节（至少4个字节）
     * @return
     */
    public static float byte2float(byte... arr) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value |= ((arr[i] & 0xff)) << (24 - i * 8);
        }
        return Float.intBitsToFloat(value);
    }

    /**
     * 字节转换为浮点
     *
     * @param arr 字节（至少4个字节）
     * @return
     */
    public static float byte2float1(byte... arr) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value |= ((arr[i] & 0xff)) << (i * 8);
        }
        return Float.intBitsToFloat(value);
    }

}
