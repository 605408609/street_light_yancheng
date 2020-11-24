package com.exc.street.light.resource.dto.electricity;

import lombok.Data;

/**
 * @Author: XuJiaHao
 * @Description: 电流模块绑定信息
 * @Date: Created in 16:30 2020/6/23
 * @Modified:
 */
@Data
public class ElectricityModule {
    private int deviceAddress;
    private int channelNum;
}
