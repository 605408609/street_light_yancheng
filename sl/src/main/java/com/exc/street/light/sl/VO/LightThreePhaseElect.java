package com.exc.street.light.sl.VO;

public class LightThreePhaseElect {


    //四代网关屏幕密码
    private String screenPasswd	;
    //网关uid
    private String gateway_id	;
    //告警使能
    private String alaenab	;
    //数据上报时间
    private String refresh_time	;
    //不知道
    private String Cdiv	;
    //三相电设备uid
    private String uid	;
    //有功功率
    private String actp	;
    //无功功率
    private String reactp	;
    //电压
    private String volt	;
    //报警类型
    private Integer alarm_status	;
    //温度
    private String mtemp	;
    //同alarm_status
    private Integer alarm	;
    //端口
    private Integer port_id	;
    //在线状态
    private Integer online	;
    //电流
    private String curr	;


    public String getScreenPasswd() {
        return screenPasswd;
    }

    public void setScreenPasswd(String screenPasswd) {
        this.screenPasswd = screenPasswd;
    }

    public String getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(String gateway_id) {
        this.gateway_id = gateway_id;
    }

    public String getAlaenab() {
        return alaenab;
    }

    public void setAlaenab(String alaenab) {
        this.alaenab = alaenab;
    }

    public String getRefresh_time() {
        return refresh_time;
    }

    public void setRefresh_time(String refresh_time) {
        this.refresh_time = refresh_time;
    }

    public String getCdiv() {
        return Cdiv;
    }

    public void setCdiv(String cdiv) {
        Cdiv = cdiv;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getActp() {
        return actp;
    }

    public void setActp(String actp) {
        this.actp = actp;
    }

    public String getReactp() {
        return reactp;
    }

    public void setReactp(String reactp) {
        this.reactp = reactp;
    }

    public String getVolt() {
        return volt;
    }

    public void setVolt(String volt) {
        this.volt = volt;
    }

    public Integer getAlarm_status() {
        return alarm_status;
    }

    public void setAlarm_status(Integer alarm_status) {
        this.alarm_status = alarm_status;
    }

    public String getMtemp() {
        return mtemp;
    }

    public void setMtemp(String mtemp) {
        this.mtemp = mtemp;
    }

    public Integer getAlarm() {
        return alarm;
    }

    public void setAlarm(Integer alarm) {
        this.alarm = alarm;
    }

    public Integer getPort_id() {
        return port_id;
    }

    public void setPort_id(Integer port_id) {
        this.port_id = port_id;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getCurr() {
        return curr;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }
}
