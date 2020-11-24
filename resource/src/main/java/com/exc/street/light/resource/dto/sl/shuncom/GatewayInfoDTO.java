package com.exc.street.light.resource.dto.sl.shuncom;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 顺舟云盒 网关信息
 *
 * @Author: Xiaok
 * @Date: 2020/8/20 16:33
 */
@Getter
@Setter
@ToString
public class GatewayInfoDTO {

    /**
     * 网关id
     */
    private String id;

    /**
     * 网关mac地址
     */
    private String mac;

    /**
     * 网关版本号
     */
    @JSONField(name="gwver")
    private String gatewayVersion;

    /**
     * 网关模式
     */
    @JSONField(name="gwmode")
    private Integer gatewayMode;

    /**
     * 网络类型
     * 4G（4g 卡）、
     * wireless（作为 sta 连接别的路由器）、
     * wired（有线）
     */
    @JSONField(name = "nettype")
    private String netType;

}
