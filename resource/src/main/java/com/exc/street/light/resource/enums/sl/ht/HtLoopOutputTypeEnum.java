package com.exc.street.light.resource.enums.sl.ht;

/**
 * 回路输出动作类型
 *
 * @Author: Xiaok
 * @Date: 2020/7/25 9:18
 */
public enum HtLoopOutputTypeEnum {
    NONE(0, "无输出动作"),
    OPEN(1, "开"),
    CLOSE(2, "关");

    private int code;
    private String type;

    public int code() {
        return code;
    }

    public String type() {
        return type;
    }

    HtLoopOutputTypeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static String getTypeByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (HtLoopOutputTypeEnum c : HtLoopOutputTypeEnum.values()) {
            if (c.code() == code) {
                return c.type;
            }
        }
        return null;
    }

    public static HtLoopOutputTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (HtLoopOutputTypeEnum c : HtLoopOutputTypeEnum.values()) {
            if (c.code() == code) {
                return c;
            }
        }
        return null;
    }
}
