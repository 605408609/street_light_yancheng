package com.exc.street.light.resource.enums.sl.shuncom;

/**
 * 顺舟云盒 设备状态
 * @Author: Xiaok
 * @Date: 2020/8/21 15:57
 */
public enum DeviceStatusEnums {
    fac("fac", "设备厂商", "String", false),
    dsp("dsp", "自定义描述（model id）", "String", false),
    swid("swid", "版本号", "String", false),
    batpt("batpt", "电池电量百分比 取值范围: 0– 200", "Integer", false),
    Supervision("Supervision", "设备到网关的心跳周期，单位是秒", "Integer", false),
    lqi("lqi", "接收信号强度 取值范围：-127 – 127", "Integer", false),
    /**
     *  0x00:正常锁门
     *  0x01:门外反锁
     *  0x02:门没有上锁
     *  0x03:门内反锁
     */
    dsta("dsta", "门锁状态", "Integer", false),
    /**
     * bit0:门栓阻塞报警
     * bit1:恢复出厂设置报警
     * bit2: Reserved
     * bit3:电源重启/换电池报警
     * bit4:尝试开锁次数过多，门锁冻结报警
     * bit5:门锁被撬报警
     * bit6:强行开门锁提醒
     * bit7: 暴力撞击门锁报警
     * bit8: 门锁内部温度过高报警
     * bit9: 用户输入挟持密码报警
     * bit10: 钥匙忘记拔提醒
     * bit11: 门口有人敲门提醒
     * bit12: 门常开 1 分钟提醒
     * bit13: 门内开门提醒
     * bit14 - 15: Reserved
     */
    alm("alm", "门锁报警", "Integer", false),
    processmode("processmode", "0:网关不处理；", "Integer", false),
    ;


    /**
     * 数据code
     */
    private String code;

    /**
     * 说明
     */
    private String name;

    /**
     * 数据类型String Long Integer Boolean
     */
    private String dataType;

    /**
     * 是否必需
     */
    private boolean isRequired;

    DeviceStatusEnums(String code, String name, String dataType, boolean isRequired) {
        this.code = code;
        this.name = name;
        this.dataType = dataType;
        this.isRequired = isRequired;
    }

    public String code() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public static DeviceStatusEnums getByCode(String code) {
        for (DeviceStatusEnums c : DeviceStatusEnums.values()) {
            if (c.code().equals(code)) {
                return c;
            }
        }
        return null;
    }
}
