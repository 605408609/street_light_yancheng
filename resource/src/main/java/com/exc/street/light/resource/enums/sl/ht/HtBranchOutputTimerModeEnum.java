package com.exc.street.light.resource.enums.sl.ht;

/**
 * 输出支路定时模式
 * @Author: Xiaok
 * @Date: 2020/7/25 9:29
 */
public enum HtBranchOutputTimerModeEnum {
    NONE(0, "无输出"),
    CYCLE_24(1, "24小时循环定时"),
    ALL_TIME(2, "全时间点定时");


    private int code;
    private String type;

    HtBranchOutputTimerModeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int code() {
        return code;
    }

    public String type() {
        return type;
    }

    public static String getTypeByCode(int code) {
        for (HtBranchOutputTimerModeEnum c : HtBranchOutputTimerModeEnum.values()) {
            if (c.code() == code) {
                return c.type;
            }
        }
        return null;
    }
}
