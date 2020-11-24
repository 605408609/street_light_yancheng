package com.exc.street.light.resource.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 数据工具类
 *
 * @author Longshuangyang
 * @date 2020/03/28
 */
public class DataUtil {

    /**
     * 根据周期值算出周期名称 1111 1110
     *
     * @param num
     * @return
     */
    public static Integer[] getCycleName(int num) {
        Set<Integer> set = new HashSet<>();
        int flag = 0x01;
        if ((num >> 1 & flag) == 1) {
            set.add(1);
        }
        if ((num >> 2 & flag) == 1) {
            set.add(2);
        }
        if ((num >> 3 & flag) == 1) {
            set.add(3);
        }
        if ((num >> 4 & flag) == 1) {
            set.add(4);
        }
        if ((num >> 5 & flag) == 1) {
            set.add(5);
        }
        if ((num >> 6 & flag) == 1) {
            set.add(6);
        }
        if ((num >> 7 & flag) == 1) {
            set.add(7);
        }
        Integer[] integers = set.toArray(new Integer[set.size()]);
        return integers;
    }

    /**
     * 获取周期位运算值
     *
     * @param cycleTypes  周期集合
     * @param executeType 执行类型 1:执行
     * @return
     */
    public static int getCycleType(Integer[] cycleTypes, int executeType) {
        int bits = 0;
        for (int j = 0; j < cycleTypes.length; j++) {
            int cycleType = cycleTypes[j];
            if (cycleType == 1) {
                bits = bits | (executeType << 1);
            }
            if (cycleType == 2) {
                bits = bits | (executeType << 2);
            }
            if (cycleType == 3) {
                bits = bits | (executeType << 3);
            }
            if (cycleType == 4) {
                bits = bits | (executeType << 4);
            }
            if (cycleType == 5) {
                bits = bits | (executeType << 5);
            }
            if (cycleType == 6) {
                bits = bits | (executeType << 6);
            }
            if (cycleType == 7) {
                bits = bits | (executeType << 7);
            }
        }
        return bits;
    }


}
