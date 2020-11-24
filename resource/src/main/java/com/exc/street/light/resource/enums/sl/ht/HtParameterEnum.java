package com.exc.street.light.resource.enums.sl.ht;

/**
 * 参数类
 *
 * @Author: Xiaok
 * @Date: 2020/9/3 11:05
 */
public enum HtParameterEnum {
    INDEX("ht_index", "单灯控制器索引"),
    LOOP_NUM("ht_loopNum", "支路数"),
    ADDRESS("ht_address", "通讯地址"),
    ACT("ht_act", "支路输出"),
    POWER("ht_power", "功率"),
    POWER_FACTOR("ht_powerFactor", "功率因数"),
    VOLTAGE("ht_voltage", "电压"),
    ELECTRIC_CURRENT("ht_electricCurrent", "电流");



    private String code;
    private String name;

    HtParameterEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String code() {
        return code;
    }

    public static String getNameByCode(String code) {
        if (code == null) {
            return null;
        }
        for (HtParameterEnum v : HtParameterEnum.values()) {
            if (v.code().equals(code)) {
                return v.name;
            }
        }
        return null;
    }

    public static HtParameterEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (HtParameterEnum v : HtParameterEnum.values()) {
            if (v.code().equals(code)) {
                return v;
            }
        }
        return null;
    }
}
