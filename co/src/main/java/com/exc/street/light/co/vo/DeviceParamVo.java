package com.exc.street.light.co.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author tanhonghang
 * @create 2020/9/28 19:09
 */
@Data
public class DeviceParamVo {
    /**
     * 设备号
     */
    private String snCode;

    /**
     *  版本
     */
    private String version;

    /**
     * 电量
     */
    private int electricity;

    /**
     * 信号强度,误码率
     */
    private String signalIntensity;

    /**
     * 上传周期
     */
    private String uploadInterval;

    /**
     * 井盖倾角阈值
     */
    private int dipAngleLimit;

    /**
     * 井盖倾角
     */
    private int dipAngle;

    /**
     * 状态 开：0  关：1
     */
    private int status;

    /**
     * 设备时间
     */
    private Date deviceTime;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * SIM卡id
     */
    private String ICCID;

    /**
     * 设备参数更新时间
     */
    private Date updateTime;

}
