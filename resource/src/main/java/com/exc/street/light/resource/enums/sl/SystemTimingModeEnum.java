package com.exc.street.light.resource.enums.sl;

/**
 * @author Xiezhipeng
 * @Description 策略定时方式枚举类
 * @Date 2020/8/27
 */
public enum SystemTimingModeEnum {

    DS(1, "定时执行"),
    JWD(2, "经纬度执行"),
    SJD(3, "时间段执行"),
    ZQ(4, "周期执行");

    private int code;

    private String type;

    SystemTimingModeEnum(int code, String type) {
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
        for (SystemTimingModeEnum value : SystemTimingModeEnum.values()) {
            if (value.code == code) {
                return value.type;
            }
        }
        return null;
    }

}
