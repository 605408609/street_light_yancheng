package com.exc.street.light.resource.vo.electricity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 强电节点列表视图
 *
 * @author Linshiwen
 * @date 2018/5/22
 */
@Setter
@Getter
@ToString
public class ElectricityNodeVO {

    private Integer id;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    @ApiModelProperty(name = "model", value = "设备型号")
    private String model;

    /**
     * mac
     */
    private String mac;

    /**
     * 编号
     */
    private String num;

    /**
     * 节点名称
     */
    private String name;
    private String addr;

    /**
     * 安装位置
     */
    private String installAddr;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "lastOnlineTime",value = "最后在线时间")
    private Date lastOnlineTime;

    /**
     * 施工单位
     */
    private String constructionUnits;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;


    /**
     * 所属区域ID
     */
    private Integer areaId;

    /**
     * 所属区域名称
     */
    private String areaName;
    /**
     * 节点设备
     */
    private List<CanDeviceVO> devices = new ArrayList<>();

    /**
     * 电箱门开关量(0:开门 1:关门)
     */
    private Integer isOpen;

    /**
     * 门开方向(0:正 1:反)
     */
    private Integer openDirection;

    //private Building building;

    /**
     * 网络类型 1:有线 2:无线 3:双备份
     */
    private Integer networkType;

    /**
     * 版本号
     */
    private String version;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;


    /**
     * 所属灯杆id
     */
    private Integer lampPostId;

    /**
     * 所属灯杆名称
     */
    private String lampPostName;
}