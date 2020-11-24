package com.exc.street.light.resource.dto.electricity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: XuJiaHao
 * @Description: 网关点表参数
 * @Date: Created in 15:07 2020/3/12
 * @Modified:
 */
@Getter
@Setter
@ToString
public class GatewayParameter {

    /**
     * 软件版本
     */
    private String softwareVersion;

    /**
     * 硬件版本
     */
    private String hardWareVersion;

    /**
     * 模块设备类别   1:4通道   2:8通道   3:12通道 4:12路电流采集模块 5:4路交流接触器模块，
     * 6:8路交流接触器模块 7:12路交流接触器模块
     */
    private int moduleDeviceType;

    /**
     * 设备地址 0x01~0x63
     */
    private int deviceAddress;

    /**
     * 生产批号
     */
    private String batchNumber;

    /**
     * 设备型号
     */
    private String unitType;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 电流模块对应绑定数据
     */
    private List<ElectricityModule> electricityModuleList;
}
