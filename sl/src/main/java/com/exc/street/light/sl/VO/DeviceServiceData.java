package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class DeviceServiceData {

    //服务 ID
    private String serviceId;
    //服务的类型
    private String serviceType;
    //服务数据信息
    private ObjectNode data;
    //事件发生时间，时间格式yyyymmddThhmmssZ
    private String eventTime;
}
