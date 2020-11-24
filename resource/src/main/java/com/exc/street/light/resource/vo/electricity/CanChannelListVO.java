package com.exc.street.light.resource.vo.electricity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 通道视图
 *
 * @author Linshiwen
 * @date 2018/5/23
 */
@Setter
@Getter
@ToString
@ApiModel(value = "通道视图")
public class CanChannelListVO {

    @ApiModelProperty("ID")
    private Integer id;

    /**
     * 回路名称
     */
    @ApiModelProperty("回路名称")
    private String name;

    /**
     * 模块地址
     */
    @ApiModelProperty("模块地址")
    private Integer deviceAddress;

    /**
     * 模块控制值
     */
    @ApiModelProperty("模块控制值")
    private Integer controlId;

    /**
     * 回路电流更新时间
     */
    @ApiModelProperty("回路电流更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date electricityUpdateTime;

    /**
     * 通道值
     */
    @ApiModelProperty("通道值")
    private Double value;

    /**
     * 通道电流值
     */
    @ApiModelProperty("通道电流值")
    private Double currentValue = 0D;

    /**
     * 通道id
     */
    @ApiModelProperty("通道id")
    private Integer tagId;

    /**
     * 回路类型名称
     */
    @ApiModelProperty("回路类型名称")
    private String channelTypeName;

    /**
     * 回路类型id
     */
    @ApiModelProperty("回路类型id")
    private Integer canChannelTypeId;

    /**
     * 外键:监控类型
     */
    @ApiModelProperty("外键:监控类型")
    private Integer sid;

    /**
     * can_device的索引
     */
    @ApiModelProperty("can_device的索引")
    private Integer canIndex;

    /**
     * 外键:设备类型编号
     */
    @ApiModelProperty("外键:设备类型编号")
    private Integer dsn;

    /**
     * 外键:can设备id
     */
    @ApiModelProperty("外键:can设备id")
    private Integer did;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String canDeviceName;

    /**
     * 网关名称
     */
    @ApiModelProperty("网关名称")
    private String nodeName;

    /**
     * 网关id
     */
    @ApiModelProperty("网关id")
    private Integer nid;

    /**
     * 网关编号
     */
    @ApiModelProperty("网关编号")
    private String nodeNum;


    @ApiModelProperty(name = "nodeStatus", value = "网关网络状态 0:在线 1:离线")
    private Integer nodeStatus;


    /**
     * 是否被场景编辑过
     */
    @ApiModelProperty("是否被场景编辑过")
    private Integer isEdit = 0;

    /**
     * 区域id
     */
    @ApiModelProperty("区域id")
    private Integer areaId;

    /**
     * 区域名称
     */
    @ApiModelProperty("区域名称")
    private String areaName;

    @ApiModelProperty("当前绑定的策略ID")
    private String strategyId;
}