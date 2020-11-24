package com.exc.street.light.sl.VO;

import lombok.Data;

/**
 * 设备信息
 */
@Data
public class DeviceInfoQueryDTO {

    //设备唯一标识码。
    private String nodeId;
    //设备名称。
    private String name;
    //设备的描述信息。
    private String description;
    //厂商 ID
    private String manufacturerId;
    //厂商名称。
    private String manufacturerName;
    //设备的 MAC 地址。
    private String mac;
    //设备的位置信息。
    private String location;
    //设备类型
    private String deviceType;
    //设备的型号。
    private String model;
    //设备的软件版本。
    private String swVersion;
    //设备的固件版本。
    private String fwVersion;
    //设备的硬件版本。
    private String hwVersion;
    //NB-IoT 终端的 IMSI。
    private String imsi;
    //设备使用的协议类型，
    private String protocolType;
    //Radius 地址。
    private String radiusIp;
    //Bridge 标识
    private String bridgeId;
    //设备的状态，表示设备是否在线，取值范围：ONLINE、OFFLINE、ABNORMAL。
    private String status;
    //设备的状态详情
    private String statusDetail;
    //表示设备是否处于冻结状态，
    private String mute;
    //表示设备是否支持安全模式。
    private String supportedSecurity;
    //表示设备当前是否启用安全模式。
    private String isSecurity;
    //设备的信号强度。
    private String signalStrength;
    //设备的 sig 版本。
    private String sigVersion;
    //设备的序列号。
    private String serialNumber;
    //设备的电池电量。
    private String batteryLevel;
}
