package com.exc.street.light.sl.VO;

import lombok.Data;

import java.util.List;

/**
 * 单灯信息
 */
@Data
public class SingleLampParamVO {

    //设备 ID
    private String deviceId;
    //网关 ID
    private String gatewayId;
    //节点类型，取值：ENDPOINT/GATEWAY/UNKNOW
    private String nodeType;
    //创建设备的时间，时间格式：yyyyMMdd'T'HHmmss'Z'
    private String createTime;
    //最后修改设备的时间
    private String lastModifiedTime;
    //设备信息
    private DeviceInfoQueryDTO deviceInfo;
    //设备服务列表
    private List<DeviceService> services;
    //设备告警信息
    private AlarmInfoDTO alarmInfo;
}
