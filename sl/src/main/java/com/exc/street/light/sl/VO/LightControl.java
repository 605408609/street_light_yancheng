package com.exc.street.light.sl.VO;


public class LightControl {
    //灯控器类型
    private Integer devtype;
    //on为1是开灯为0是关灯
    private Integer on;
    //bri取值为调光亮度的百分比值
    private Integer bri;
    //read值固定为1是读取灯具数据
    private Integer read;

    public Integer getDevtype() {
        return devtype;
    }

    public void setDevtype(Integer devtype) {
        this.devtype = devtype;
    }

    public Integer getOn() {
        return on;
    }

    public void setOn(Integer on) {
        this.on = on;
    }

    public Integer getBri() {
        return bri;
    }

    public void setBri(Integer bri) {
        this.bri = bri;
    }

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }
}
