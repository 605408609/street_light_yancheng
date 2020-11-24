package com.exc.street.light.resource.enums.dlm;

/**
 * @author Xiezhipeng
 * @Description 集中控制器类型枚举类
 * @Date 2020/8/24
 */
public enum LocationControlTypeEnum {

    SZ(1, "SZ"),
    HT(2, "HT"),
    ZKZL(3, "ZKZL"),
    EXC(4, "EXC");

    private int code;

    private String type;

    LocationControlTypeEnum(int code, String type) {
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
        for (LocationControlTypeEnum value : LocationControlTypeEnum.values()) {
            if (value.code == code) {
                return value.type;
            }
        }
        return null;
    }

}
