package com.exc.street.light.resource.enums.sl.ht;

/**
 * 单灯控制器支路输出
 *
 * @Author: Xiaok
 * @Date: 2020/7/25 9:22
 */
public enum HtSingleLampBranchOutputEnum {
    LIGHT_0(0, "关灯"),
    LIGHT_5(5, "调光5%"),
    LIGHT_10(10, "调光10%"),
    LIGHT_15(15, "调光15%"),
    LIGHT_20(20, "调光20%"),
    LIGHT_25(25, "调光25%"),
    LIGHT_30(30, "调光20%"),
    LIGHT_35(35, "调光35%"),
    LIGHT_40(40, "调光40%"),
    LIGHT_45(45, "调光45%"),
    LIGHT_50(50, "调光50%"),
    LIGHT_55(55, "调光55%"),
    LIGHT_60(60, "调光60%"),
    LIGHT_65(65, "调光65%"),
    LIGHT_70(70, "调光70%"),
    LIGHT_75(75, "调光75%"),
    LIGHT_80(80, "调光80%"),
    LIGHT_85(85, "调光85%"),
    LIGHT_90(90, "调光90%"),
    LIGHT_95(95, "调光95%"),
    LIGHT_100(100, "全开"),
    NONE(255, "无输出动作");


    private int code;
    private String type;

    HtSingleLampBranchOutputEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int code() {
        return code;
    }

    public String type() {
        return type;
    }

    public static String getTypeByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (HtSingleLampBranchOutputEnum c : HtSingleLampBranchOutputEnum.values()) {
            if (c.code() == code) {
                return c.type;
            }
        }
        return null;
    }

    public static HtSingleLampBranchOutputEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (HtSingleLampBranchOutputEnum c : HtSingleLampBranchOutputEnum.values()) {
            if (c.code() == code) {
                return c;
            }
        }
        return null;
    }
}
