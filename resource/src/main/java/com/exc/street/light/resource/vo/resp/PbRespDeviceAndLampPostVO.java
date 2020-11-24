package com.exc.street.light.resource.vo.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:气象设备和灯杆返回对象
 *
 * @author LeiJing
 * @Date 2019/5/20
 */
@Getter
@Setter
@ToString
public class PbRespDeviceAndLampPostVO {
    /**
     * 广播表id，自增长
     */
    @ApiModelProperty(name = "id" , value = "广播表id，自增长")
    private Integer id;

    /**
     * 设备型号
     */
    @ApiModelProperty(name = "model" , value = "设备型号")
    private String model;

    /**
     * ip
     */
    @ApiModelProperty(name = "ip" , value = "ip")
    private String ip;

    /**
     * mac地址
     */
    @ApiModelProperty(name = "mac" , value = "mac地址")
    private String mac;

    /**
     * 广播音量
     */
    @ApiModelProperty(name = "volume" , value = "广播音量")
    private Integer volume;

    /**
     * 设备厂家
     */
    @ApiModelProperty(name = "factory" , value = "设备厂家")
    private String factory;

    /**
     * 设备名称
     */
    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    /**
     * 设备编号
     */
    @ApiModelProperty(name = "num" , value = "设备编号")
    private String num;

    /**
     * 网络状态(0:离线，1:在线)默认0
     */
    @ApiModelProperty(name = "networkState" , value = "网络状态(0:离线，1:在线)默认0")
    private Integer networkState;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    /**
     * 最后在线时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
    private Date lastOnlineTime;

    /**
     * 灯杆id
     */
    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    /**
     * 灯杆名称
     */
    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;

    /**
     * 灯杆编号
     */
    @ApiModelProperty(name = "lampPostNum" , value = "灯杆编号")
    private String lampPostNum;

    /**
     * 经度
     */
    @ApiModelProperty(name = "longitude" , value = "经度")
    private Double longitude;

    /**
     * 纬度
     */
    @ApiModelProperty(name = "latitude" , value = "纬度")
    private Double latitude;

    /**
     * 纬度
     */
    @ApiModelProperty(name = "termId" , value = "雷拓IP广播平台终端编号")
    private Integer termId;
}
