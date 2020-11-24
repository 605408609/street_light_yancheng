package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 第三方路灯平台设备控制接口接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class SlReqDeviceControlVO {
    /**
     * token 默认12小时
     */
    @ApiModelProperty(name = "token", value = "token 默认12小时")
    private String token;

    /**
     * 设备编号
     */
    @ApiModelProperty(name = "deveui", value = "设备编号")
    private String deveui;

    /**
     * 设备类型
     */
    @ApiModelProperty(name = "devtype", value = "设备类型")
    private String devtype;

    /**
     * 是否为需确认指令 默认false
     */
    @ApiModelProperty(name = "confirm", value = "是否为需确认指令 默认false")
    private Boolean confirm;

    /**
     * 设备侧端口 默认0
     */
    @ApiModelProperty(name = "devport", value = "设备侧端口 默认0")
    private Integer devport;

    /**
     * 控制对象
     */
    @ApiModelProperty(name = "command", value = "控制对象")
    private SlReqCommandVO command;
}