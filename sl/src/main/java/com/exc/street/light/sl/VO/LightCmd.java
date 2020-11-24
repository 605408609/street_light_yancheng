package com.exc.street.light.sl.VO;

public class LightCmd<T> {
    //指令码
    private Integer code	;
    //序列号
    private Integer headSerial	;
    //设备ID
    private String uid	;
    //设备实际地址
    private String addr	;
    //网关地址
    private String gatewayAddr	;
    //指令体
    private T control;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getHeadSerial() {
        return headSerial;
    }

    public void setHeadSerial(Integer headSerial) {
        this.headSerial = headSerial;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getGatewayAddr() {
        return gatewayAddr;
    }

    public void setGatewayAddr(String gatewayAddr) {
        this.gatewayAddr = gatewayAddr;
    }

    public T getControl() {
        return control;
    }

    public void setControl(T control) {
        this.control = control;
    }
}
