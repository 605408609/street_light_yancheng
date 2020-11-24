package com.exc.street.light.ss.common.enums;

/**
 * ProtocolEnum 协议类型
 *
 * @author liufei
 * @date 2020/06/05
 */
public enum ProtocolEnum {

    /**
     * rtsp协议
     */
    RTSP("rtsp"),

    /**
     * rtmp协议
     */
    RTMP("rtmp"),

    /**
     * hls协议
     */
    HLS("hls")
    ;

    private String code;

    ProtocolEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
