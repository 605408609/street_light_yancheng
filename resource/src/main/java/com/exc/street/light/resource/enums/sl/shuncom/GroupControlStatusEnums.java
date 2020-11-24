package com.exc.street.light.resource.enums.sl.shuncom;

import org.apache.commons.lang3.StringUtils;

/**
 * 顺舟云盒 设备扩展属性
 *
 * @Author:Xiaok
 * @Date:2020/8/2016:52
 */
public enum GroupControlStatusEnums {

    on("on", "开关状态", "Boolean", false),
    bri("bri", "亮度", "Integer", false),
    hue("hue", "色调", "Integer", false),
    sat("sat", "sat", "Integer", false),
    /**
     * 取值范围: 153 - 499
     */
    ctp("ctp", "色温状态", "Integer", false),
    sta("sta", "烟感状态", "Integer", false),
    zid("zid", "烟感区域", "Integer", false),
    nlux("nlux", "当前光照度", "String", false),
    currlux("currlux", "当前光照度", "Integer", false),
    llux("llux", "光照度等级状态", "Integer", false),
    tlux("tlux", "目标光照度", "String", false),
    onlux("onlux", "光照度开灯阈值", "Integer", false),
    offlux("offlux", "光照度关灯阈值", "Integer", false),
    pt("pt", "百分比", "Integer", false),
    /**
     * 0：关，向下，反转等；
     * 1：开，向上，正转等；
     * 2：停止
     */
    ctrl("offlux", "控制类型", "Integer", false),
    ;


    /**
     * 数据code
     */
    private String code;

    /**
     * 说明
     */
    private String name;

    /**
     * 数据类型StringLongInteger
     */
    private String dataType;

    /**
     * 是否必需
     */
    private boolean isRequired;

    GroupControlStatusEnums(String code, String name, String dataType, boolean isRequired) {
        this.code = code;
        this.name = name;
        this.dataType = dataType;
        this.isRequired = isRequired;
    }

    public String code() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public static GroupControlStatusEnums getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (GroupControlStatusEnums c : GroupControlStatusEnums.values()) {
            if (c.code().equals(code)) {
                return c;
            }
        }
        return null;
    }
}
