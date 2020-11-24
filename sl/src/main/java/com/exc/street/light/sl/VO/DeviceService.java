package com.exc.street.light.sl.VO;

import lombok.Data;

/**
 * 设备服务列表
 */
@Data
public class DeviceService {

    //设备的服务标识。
    private String serviceId;
    //设备的服务类型。
    private String serviceType;
    //设备的服务信息。
    private ServiceInfo serviceInfo;
    //属性值对。
    private ObjectNode data;
    //时间格式：yyyyMMdd'T'HHmmss'Z'
    private String eventTime;
}
