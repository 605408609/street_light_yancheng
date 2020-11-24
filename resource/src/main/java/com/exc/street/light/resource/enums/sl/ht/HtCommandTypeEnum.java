package com.exc.street.light.resource.enums.sl.ht;

/**
 * 命令类型
 *
 * @Author: Xiaok
 * @Date: 2020/7/24 18:16
 */
public enum HtCommandTypeEnum {
    LOGIN(0, "登录", "POST"),
    SEARCH_ACCESS_DEVICE_BY_LAMP_POST(1, "查询集中控制器接入单灯集中器通讯地址", "GET"),
    SET_LAMP_POST(2, "设置集中控制器的单灯集中器通讯地址", "POST"),
    SEARCH_RELEGATION(3, "查询归属", "GET"),
    SET_RELEGATION(4, "设置归属", "POST"),
    SEARCH_SINGLE_LAMP_INFO(5, "查询单灯控制器信息", "GET"),
    SET_SINGLE_LAMP_INFO(6, "设置单灯控制器信息", "POST"),
    SEARCH_LAMP_POST_REAL(7, "查询单灯控制器运行数据及报警状态", "GET"),
    SEARCH_SINGLE_LAMP_REAL(8, "查询单灯控制器运行数据", "GET"),
    SEARCH_LAMP_POST_PLAN(9, "查询集中控制器回路输出计划", "GET"),
    SET_LAMP_POST_PLAN(10, "设置集中控制器回路输出计划", "POST"),
    SEARCH_SINGLE_LAMP_PLAN(11, "查询单灯控制器定时计划", "GET"),
    SET_SINGLE_LAMP_PLAN(12, "设置单灯控制器定时计划", "POST"),
    MANUAL_OUTPUT_LAMP_POST(13, "单灯控制器回路手动输出", "POST"),
    MANUAL_OUTPUT_SINGLE_LAMP(14, "单灯控制器手动输出", "POST"),
    COMMAND_RETURN(15, "命令回送", "POST"),
    DATA_UPLOAD_LAMP_POST(16, "单灯控制器运行数据及报警状态上传", "POST"),
    DATA_UPLOAD_SINGLE_LAMP(17, "单灯控制器运行数据上传", "POST");


    private int code;
    private String type;
    private String requestMethod;

    HtCommandTypeEnum(int code, String type, String requestMethod) {
        this.code = code;
        this.type = type;
        this.requestMethod = requestMethod;
    }

    public int code() {
        return code;
    }

    public String type() {
        return type;
    }

    public String requestMethod() {
        return requestMethod;
    }

    public static String getTypeByCode(int code) {
        for (HtCommandTypeEnum c : HtCommandTypeEnum.values()) {
            if (c.code() == code) {
                return c.type;
            }
        }
        return null;
    }

    public static String getRequestMethodByCode(int code) {
        for (HtCommandTypeEnum c : HtCommandTypeEnum.values()) {
            if (c.code() == code) {
                return c.requestMethod;
            }
        }
        return null;
    }

    public static HtCommandTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (HtCommandTypeEnum c : HtCommandTypeEnum.values()) {
            if (code.equals(c.code)) {
                return c;
            }
        }
        return null;
    }
}
