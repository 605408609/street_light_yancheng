package com.exc.street.light.resource.dto.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: XuJiaHao
 * @Description: 模块控制公共类
 * @Date: Created in 11:42 2020/3/14
 * @Modified:
 */

@Getter
@Setter
@ToString
public class ChannelControlDeviceParameter {

    /**
     * 模块地址
     */
    private int address;
    /**
     * 模块通道 1~ 16 开 默认为0
     */
    private int channel1 = 0;
    /**
     * 模块通道 1~ 16 关 默认为0
     */
    private int channel2 = 0;

    /**
     * 模块通道 1~16 反转 默认为0
     */
    private int channel3 = 0;
}
