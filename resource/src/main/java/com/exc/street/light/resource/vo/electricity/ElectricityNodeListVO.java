package com.exc.street.light.resource.vo.electricity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 强电节点列表视图
 *
 * @author Linshiwen
 * @date 2018/5/22
 */
@Setter
@Getter
@ToString
public class ElectricityNodeListVO {

    private Integer id;

    /**
     * ip
     */
    @ApiModelProperty(name = "ip",value = "ip")
    private String ip;

    /**
     * 编号
     */
    @ApiModelProperty(name = "num",value = "编号")
    private String num;

    @ApiModelProperty(name = "name",value = "网关名称")
    private String name;

    @ApiModelProperty(name = "addr",value = "地址")
    private String addr;

    @ApiModelProperty(name = "mac",value = "MAC地址")
    private String mac;

    @ApiModelProperty(name = "port",value = "端口")
    private Integer port;

    @ApiModelProperty(name = "model",value = "设备型号")
    private String model;

    @ApiModelProperty(name = "isOpen",value = "电箱门开关量(0:开门 1:关门)")
    private Integer isOpen;

    @ApiModelProperty(name = "isOffline",value = "网络状态 0:在线 1:离线")
    private Integer isOffline;

    @ApiModelProperty(name = "isOnline",value = "网络状态 1:在线 0:离线")
    private Integer isOnline;

    /**
     * 离线时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "offlineTime",value = "离线时间")
    private Date offlineTime;

    /**
     * 开始上电时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "startTime",value = "开始上电时间")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "lastOnlineTime",value = "最后在线时间")
    private Date lastOnlineTime;

    /**
     * 节点当前总电能
     */
    private Double totalEnergy = 0D;


    /**
     * 网络类型 1:有线 2:无线 3:双备份
     */
    @ApiModelProperty(name = "networkType",value = "网络类型 1:有线 2:无线 3:双备份")
    private Integer networkType;

    /**
     * 路由器网络类型： 1、正常  2、telnet网关端口不同 3、ping不通  4、异常
     */
    @ApiModelProperty(name = "routerIsOffline",value = "路由器网络类型： 1、正常  2、telnet网关端口不同 3、ping不通  4、异常")
    private Integer routerIsOffline;

    /**
     * 路由器网络类型1-3都会更新该时间   4、异常不会更新改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date routerOfflineTime;

    @ApiModelProperty(name = "creator", value = "创建人ID")
    private Integer creator;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;


    @ApiModelProperty(name = "creatorName", value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(name = "areaId", value = "区域Id")
    private Integer areaId;

    @ApiModelProperty(name = "areaName", value = "区域名称")
    private String areaName;

    /**
     * 版本号
     */
    private String version;


    @ApiModelProperty(name = "lampPostId", value = "灯杆ID")
    private Integer lampPostId;

    @ApiModelProperty(name = "lampPostName", value = "灯杆名称")
    private String lampPostName;

    @ApiModelProperty(name = "longitude",value = "灯杆经度坐标")
    private Double longitude;

    @ApiModelProperty(name = "latitude",value = "灯杆纬度坐标")
    private Double latitude;
}