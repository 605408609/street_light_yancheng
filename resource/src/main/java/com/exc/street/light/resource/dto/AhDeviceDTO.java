 package com.exc.street.light.resource.dto;

 /**
 * @author huangmin
 * @date 2020/03/23
 */
public class AhDeviceDTO {
    private String deviceIP;
    private int devicePort;
    private String deviceUserName;
    private String devicePassword;
    private String listenIP;
    private int listenPort;
    public String getDeviceIP() {
        return deviceIP;
    }
    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }
    public int getDevicePort() {
        return devicePort;
    }
    public void setDevicePort(int devicePort) {
        this.devicePort = devicePort;
    }
    public String getDeviceUserName() {
        return deviceUserName;
    }
    public void setDeviceUserName(String deviceUserName) {
        this.deviceUserName = deviceUserName;
    }
    public String getDevicePassword() {
        return devicePassword;
    }
    public void setDevicePassword(String devicePassword) {
        this.devicePassword = devicePassword;
    }
    public String getListenIP() {
        return listenIP;
    }
    public void setListenIP(String listenIP) {
        this.listenIP = listenIP;
    }
    public int getListenPort() {
        return listenPort;
    }
    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }
   
    
    
}
