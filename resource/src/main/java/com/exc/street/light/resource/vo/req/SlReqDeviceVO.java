package com.exc.street.light.resource.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 新增灯杆设备接口接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class SlReqDeviceVO {
    @ApiModelProperty(name = "id" , value = "灯控设备表id，自增长")
    private Integer id;

    @ApiModelProperty(name = "model" , value = "设备型号")
    private String model;

    @ApiModelProperty(name = "ip" , value = "ip")
    private String ip;

    @ApiModelProperty(name = "mac" , value = "mac地址")
    private String mac;

    @ApiModelProperty(name = "factory" , value = "设备厂家")
    private String factory;

    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "设备编号")
    private String num;

    @ApiModelProperty(name = "networkState" , value = "网络状态(0:离线，1:在线)默认0")
    private Integer networkState;

    @ApiModelProperty(name = "brightState" , value = "亮灯状态（0：关，1：开）默认0")
    private Integer brightState;

    @ApiModelProperty(name = "presetBrightState" , value = "预设亮灯状态（0：关，1：开）默认0")
    private Integer presetBrightState;

    @ApiModelProperty(name = "brightness" , value = "亮度（0-100）默认0")
    private Integer brightness;

    @ApiModelProperty(name = "presetBrightness" , value = "预设亮度（0-100）默认0")
    private Integer presetBrightness;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
    private Date lastOnlineTime;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "unique" , value = "验证唯一（0：否，1：是）")
    private Integer unique;
}