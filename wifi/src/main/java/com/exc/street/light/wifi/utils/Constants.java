package com.exc.street.light.wifi.utils;

/**
 * @author Xiezhipeng
 * @Description oid相关常数
 * @Date 2020/4/29
 */
public class Constants {

    // 团体字
    public static final String community = "ruijie";

    // 5秒内的cpu使用率
    public static final String cpu = "1.3.6.1.4.1.4881.1.1.10.2.36.1.1.1.0";
    // 当前连接到本AC的AP数,在线的ap数量
    public static final String apOnlineCount = "1.3.6.1.4.1.4881.1.1.10.2.56.1.1.11.0";
    // 当前连接到本AC的AP数,离线的ap数量
    public static final String apOfflineCount = "1.3.6.1.4.1.4881.1.1.10.2.56.1.1.80.0";

    // ap的ip地址:在线ip
    public static final String apIp = "1.3.6.1.4.1.4881.1.1.10.2.56.2.1.1.1.33";
    // ap状态：锐捷规定（1:在线 2：离线）
    public static final String apStatus = "1.3.6.1.4.1.4881.1.1.10.2.56.2.1.8.1.3";
    // ap设备的mac：在线数据
    public static final String apMac = "1.3.6.1.4.1.4881.1.1.10.2.56.2.1.13.1.1";
    // 当前连接ap且认证成功的人数
    public static final String apConnPeopleCount = "1.3.6.1.4.1.4881.1.1.10.2.81.10.2.1.1.2";
    // ap与ac的关联时间
    public static final String apOnlineTime = "1.3.6.1.4.1.4881.1.1.10.2.56.2.1.1.1.47";
    // ap上行流量发送总字节数
    public static final String apUpFlow = "1.3.6.1.4.1.4881.1.1.10.2.10.1.13.1.44";
    // ap下行流量接收总字节数
    public static final String apDownFlow = "1.3.6.1.4.1.4881.1.1.10.2.10.1.13.1.43";
    // 认证成功的次数
    public static final String certSuccessTimes = "1.3.6.1.4.1.4881.1.1.10.2.81.10.2.1.1.11";
    // 认证请求的次数
    public static final String certTimes = "1.3.6.1.4.1.4881.1.1.10.2.81.10.2.1.1.10";
    // 用户关联的ap设备的Mac
    public static final String userAssociateApMac = "1.3.6.1.4.1.4881.1.1.10.2.56.5.1.1.1.2";
    // STA接收报文的总字节数(下行流量)
    public static final String STARecvTotalByte = "1.3.6.1.4.1.4881.1.1.10.2.81.10.7.1.1.6";
    // STA发送报文的总字节数(上行流量)
    public static final String STASendTotalByte = "1.3.6.1.4.1.4881.1.1.10.2.81.10.7.1.1.8";

}
