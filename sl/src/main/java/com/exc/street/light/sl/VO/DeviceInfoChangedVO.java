package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class DeviceInfoChangedVO {

    //通知类型(deviceInfoChanged)
    private String notifyType;
    //设备 ID
    private String deviceId;
    //网关 ID
    private String gatewayId;
    //设备信息
    private DeviceInfoQueryDTO deviceInfo;
}
