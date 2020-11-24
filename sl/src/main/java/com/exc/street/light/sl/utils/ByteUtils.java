package com.exc.street.light.sl.utils;

import java.util.Arrays;

public final class ByteUtils {

    private ByteUtils() {
    }

    /**
     * 将两个字节数组按先后顺序拼接为一个字节数组
     *
     * @param array1
     * @param array2
     * @return
     */
    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    /**
     * 将long转化为byte数组(大端序)
     *
     * @param value
     * @return
     */
    public static byte[] longToBytesBE(long value) {
        byte[] bytes = new byte[Long.BYTES];
        bytes[7] = (byte) (value);
        bytes[6] = (byte) (value >>> 8);
        bytes[5] = (byte) (value >>> 16);
        bytes[4] = (byte) (value >>> 24);
        bytes[3] = (byte) (value >>> 32);
        bytes[2] = (byte) (value >>> 40);
        bytes[1] = (byte) (value >>> 48);
        bytes[0] = (byte) (value >>> 56);
        return bytes;
    }
}
