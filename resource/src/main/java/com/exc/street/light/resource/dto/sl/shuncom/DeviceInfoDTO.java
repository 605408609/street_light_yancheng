package com.exc.street.light.resource.dto.sl.shuncom;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 顺舟云盒 接入的设备信息
 * @Author: Xiaok
 * @Date: 2020/8/20 16:39
 */
@Getter
@Setter
@ToString
public class DeviceInfoDTO {

    /**
     * 在一个网关中，各设备
     * id 都是唯一；
     * 网关设备 id：00ff +网 关 mac 地址第一个字节
     * +网关 mac 地址第一个
     * 字节+ 网关 mac 地址
     * 家 居 设 备 id ： 01 +
     * port(1 byte) + 设备
     * ieee
     * 路 灯 设 备 id ： 02 +
     * port(1 byte) + 网关
     * mac（6 byte）+ 路灯
     * 4byte 地址
     * 工 业 设 备 id ： 03 +
     * port(1 byte) + 00+ 网 关 mac（6 byte）+ 工业
     * 设 备 1byte 地 址
     * （modbus）
     * 03 + port(1 byte) +
     * 01+ 00 + 电表 6byte 地 址（电表）
     * 03 + port(1 byte) +
     * 02+ 000000 + 4byte 地 址（sz06 采集设备）
     * 透 传 设 备 id ： 04 +
     * port(1 byte) + 00+ 网 关 mac（6 byte） + 01
     * （1 byte）
     * 回 路 设 备 id ： 05 +
     * port(1 byte) + 网关
     * mac（6 byte） + 回路设
     * 备地址（2 byte）
     * 声 纹 设 备 id ： 06 +
     * port(1 byte) + 00+ 网 关 mac（6 byte） + 声 纹 ID（1 byte）
     */
    private String id;

    /**
     * 在线状态：
     * true：在线,
     * false：掉线
     */
    @JSONField(name="ol")
    private Boolean onlineStatus;

    /**
     * 端口号
     */
    @JSONField(name="ep")
    private Integer port;

    /**
     * 协议
     */
    private Integer pid;

    /**
     * 设备类型id
     */
    @JSONField(name="did")
    private Integer deviceTypeId;

}
