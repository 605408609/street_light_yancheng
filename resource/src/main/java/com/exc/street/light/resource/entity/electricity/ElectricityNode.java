package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 强电节点表
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("electricity_node")
public class ElectricityNode extends Model<ElectricityNode> {

    private static final long serialVersionUID = -4358881149634623396L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    //@Excel(name = "节点名称", orderNum = "0")
    @ApiModelProperty(name = "name", value = "节点名称")
    private String name;

    @ApiModelProperty(name = "mac", value = "mac")
    private String mac;

    //@Excel(name = "节点编号", orderNum = "1")
    @ApiModelProperty(name = "num", value = "节点编号")
    private String num;

    @ApiModelProperty(name = "model", value = "设备型号")
    private String model;

    //@Excel(name = "省", orderNum = "2")
    @ApiModelProperty(name = "province", value = "省")
    private String province;

    //@Excel(name = "市", orderNum = "3")
    @ApiModelProperty(name = "city", value = "市")
    private String city;

    //@Excel(name = "区", orderNum = "4")
    @ApiModelProperty(name = "district", value = "区")
    private String district;

    //@Excel(name = "地址", orderNum = "5")
    @ApiModelProperty(name = "addr", value = "地址")
    private String addr;

    //@Excel(name = "IP", orderNum = "6")
    @ApiModelProperty(name = "ip", value = "ip")
    private String ip;

    //@Excel(name = "端口", orderNum = "7")
    @ApiModelProperty(name = "port", value = "端口")
    private Integer port;

    @TableField(value = "install_addr")
    //@Excel(name = "安装位置", orderNum = "8")
    @ApiModelProperty(name = "installAddr", value = "安装位置")
    private String installAddr;

    @TableField(value = "construction_units")
    //@Excel(name = "施工单位", orderNum = "9")
    @ApiModelProperty(name = "constructionUnits", value = "施工单位")
    private String constructionUnits;

    @ApiModelProperty(name = "version", value = "版本号")
    private String version;

    @TableField(value = "building_id")
    @ApiModelProperty(name = "buildingId", value = "建筑物id")
    private Integer buildingId;

    @TableField(value = "is_open")
    @ApiModelProperty(name = "isOpen", value = "电箱门开关量(0:开门 1:关门)")
    private Integer isOpen;

    @TableField(value = "open_direction")
    @ApiModelProperty(name = "openDirection", value = "门开方向(0:正 1:反)")
    private Integer openDirection;

    @TableField(value = "is_offline")
    @ApiModelProperty(name = "isOffline", value = "网络状态 0:在线 1:离线")
    private Integer isOffline;

    @TableField(value = "network_type")
    @ApiModelProperty(name = "networkType", value = "网络类型 1:有线 2:无线 3:双备份")
    private Integer networkType;

    @TableField(value = "router_is_offline")
    @ApiModelProperty(name = "routerIsOffline", value = "路由器网络状态(0:离线 1:在线)")
    private Integer routerIsOffline;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "router_offline_time")
    @ApiModelProperty(name = "routerOfflineTime", value = "路由器最后在线时间")
    private Date routerOfflineTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "offlineTime", value = "离线时间")
    private Date offlineTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "lastOnlineTime", value = "最后在线时间")
    private Date lastOnlineTime;

    @ApiModelProperty(name = "lampPostId", value = "灯杆ID")
    private Integer lampPostId;

    @ApiModelProperty(name = "longitude", value = "经度")
    private Double longitude;

    @ApiModelProperty(name = "latitude", value = "纬度")
    private Double latitude;

    @ApiModelProperty(name = "creator", value = "创建人ID")
    private Integer creator;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "partition", value = "所属分区")
    private transient Partition partition;

    @ApiModelProperty(name = "canTimings", value = "场景定时")
    private transient List<CanTiming> canTimings;
}