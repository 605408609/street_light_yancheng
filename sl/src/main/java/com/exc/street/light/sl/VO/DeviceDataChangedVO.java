package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class DeviceDataChangedVO {

    //通知类型，取值：deviceDataChanged。
    private String notifyType;
    //消息的序列号
    private String requestId;
    //设备 ID
    private String deviceId;
    //网关 ID
    private String gatewayId;
    //设备的服务数据
    private DeviceServiceData service;



}
