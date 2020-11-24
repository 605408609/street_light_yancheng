package com.exc.street.light.co.vo;

/**
 * @Author tanhonghang
 * @create 2020/9/28 16:43
 */
public abstract class DeviceParameterCode {

    /**
     * 软件版本
     */
    public static final String SOFTWARE_VERSION = "0x0002";

    /**
     * ICCID
     */
    public static final String ICCID = "0x013A";

    /**
     * 信号强度 0-31
     */
    public static final String SIGNAL_INTENSITY = "0x013C";

    /**
     *  上传周期 s
     */
    public static final String UPLOAD_INTERVAL = "0x0402";

    /**
     * 开盖报警倾角阈值
     */
    public static final String DIP_ANGLE_LIMIT = "0x06B2";

    /**
     * 井盖倾角
     */
    public static final String DIP_ANGLE = "0x06B0";

    /**
     * 井盖状态
     */
    public static final String STATUS = "0x06B1";

    /**
     * 设备时间
     */
    public static final String DEVICE_TIME = "0x0404";

    /**
     * 产品类型
     */
    public static final String PRODUCT_TYPE = "0x0019";
    
    /**
     * 电池电量
     */
    public static final String ELECTRICITY_QUANTITY = "0x0165";
}
