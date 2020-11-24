package com.exc.street.light.resource.dto.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 控制命令对象
 *
 * @author LinShiWen
 * @date 2018/4/23
 */
@Setter
@Getter
public class ControlCommand {
    /**
     * 通道id
     */
    private int tagId;
    /**
     * 控制值 1开 2关
     */
    private int value;
    /**
     * 设备类型编号
     */
    //private int sn;
    /**
     * 模块地址
     */
    private int deviceAddress;
    /**
     * 开关回路通道
     */
    private int controlId;
}
