package com.exc.street.light.resource.dto.sl.shuncom;

import lombok.Data;

/**
 * 上报设备状态改变
 *
 * @Author: Xiaok
 * @Date: 2020/8/21 15:42
 */
@Data
public class DeviceStatusChangeDTO {
    /**
     * 命令类型
     */
    private Integer code;

    /**
     * 设备ID
     */
    private String id;

    /**
     * 设备端口
     */
    private Integer ep;

    /**
     * 协议
     */
    private Integer pid;

    /**
     * 设备类型 id
     */
    private Integer did;

    /**
     * 在线状态  true:设备在线 false:设备离线
     */
    private Boolean ol;

    /**
     * 序列码，每个包唯一
     */
    private Long serial;

    /**
     * 控制类型
     */
    private Integer control;
}
