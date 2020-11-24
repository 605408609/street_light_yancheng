package com.exc.street.light.resource.dto.pb;

import org.hibernate.validator.constraints.Length;

/**
 * 广播设备数据excel导入类 顺序与excel模板对应
 *
 * @Author: Xiaok
 * @Date: 2020/11/13 10:45
 */
public class RadioDeviceImportDataDTO {

    @Length(max = 10)
    private String name;

    @Length(max = 36)
    private String num;

    @Length(max = 10)
    private Integer termId;

    @Length(max = 100)
    private String lampPostName;

    @Length(max = 25)
    private String model;

    @Length(max = 25)
    private String ip;

    @Length(max = 25)
    private String mac;

    @Length(max = 25)
    private String factory;

    public RadioDeviceImportDataDTO() {
    }


    public RadioDeviceImportDataDTO(@Length(max = 10) String name, @Length(max = 36) String num, @Length(max = 10) Integer termId, @Length(max = 100) String lampPostName, @Length(max = 25) String model, @Length(max = 25) String ip, @Length(max = 25) String mac, @Length(max = 25) String factory) {
        this.name = name;
        this.num = num;
        this.termId = termId;
        this.lampPostName = lampPostName;
        this.model = model;
        this.ip = ip;
        this.mac = mac;
        this.factory = factory;
    }

    @Override
    public String toString() {
        return "RadioDeviceImportDTO{" +
                "name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", termId=" + termId +
                ", lampPostName='" + lampPostName + '\'' +
                ", model='" + model + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", factory='" + factory + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getLampPostName() {
        return lampPostName;
    }

    public void setLampPostName(String lampPostName) {
        this.lampPostName = lampPostName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }
}
