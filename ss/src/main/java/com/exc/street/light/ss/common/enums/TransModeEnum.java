package com.exc.street.light.ss.common.enums;

/**
 * TransModeEnum 协议类型
 *
 * @author liufei
 * @date 2020/06/05
 */
public enum TransModeEnum {

    /**
     * udp
     */
    UDP(0),

    /**
     * tcp
     */
    TCP(1)
    ;

    private int code;

    TransModeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
