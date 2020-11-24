package com.exc.street.light.ss.common.enums;

/**
 * StreamTypeEnum 码流类型
 *
 * @author liufei
 * @date 2020/06/05
 */
public enum StreamTypeEnum {

    /**
     * 主码流
     */
    MAIN_CODE_STREAM(0),

    /**
     * 子码流
     */
    SUB_CODE_STREAM(1)
    ;

    private int code;

    StreamTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
