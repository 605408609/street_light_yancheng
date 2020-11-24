package com.exc.street.light.resource.enums.sl.ht;

/**
 * 操作结果
 *
 * @Author: Xiaok
 * @Date: 2020/7/24 18:02
 */
public enum HtResultEnum {
    SUCCESS(0, "成功"),
    COMMAND_PARAM_ERROR(1, "命令参数错误"),
    SETTING_PARAM_ERROR(2, "设置参数错误"),
    DEVICE_OFFLINE(3, "设备不在线"),
    EXECUTE_FAIL(4, "执行失败"),
    TOKEN_ERROR(5, "token失效或不正确");


    private int code;
    private String msg;

    HtResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public static String getMsgByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (HtResultEnum v : HtResultEnum.values()) {
            if (v.code() == code) {
                return v.msg;
            }
        }
        return null;
    }
}
