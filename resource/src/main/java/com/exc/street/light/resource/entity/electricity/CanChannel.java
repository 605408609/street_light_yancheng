package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * can回路表
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Setter
@Getter
@ToString
@TableName("can_channel")
public class CanChannel extends Model<CanChannel> {
    private static final long serialVersionUID = -4180040463455720917L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    /**
     * 模块地址
     */
    @TableField(value = "device_address")
    @ApiModelProperty(name = "deviceAddress", value = "模块地址")
    private Integer deviceAddress;

    /**
     * 绑定的控制回路的模块地址
     */
    @TableField(value = "bind_address")
    @ApiModelProperty(name = "bindAddress", value = "绑定的控制回路的模块地址")
    private Integer bindAddress;

    /**
     * 回路电流更新时间
     */
    @TableField(value = "electricity_update_time")
    @ApiModelProperty(name = "electricityUpdateTime", value = "回路电流更新时间")
    private Date electricityUpdateTime;

    /**
     * 交流接触器回路选择的相位（1：A相 2：B相 3：C相）
     */
    @TableField(value = "phase_position")
    @ApiModelProperty(name = "phasePosition", value = "交流接触器回路选择的相位（1：A相 2：B相 3：C相）")
    private Integer phasePosition;

    /**
     * 绑定的模块回路控制id
     */
    @TableField(value = "bind_channel_control_id")
    @ApiModelProperty(name = "bindChannelControlId", value = "绑定的模块回路控制id")
    private Integer bindChannelControlId;

    /**
     * 控制id
     */
    @TableField(value = "control_id")
    @ApiModelProperty(name = "controlId", value = "控制id")
    private Integer controlId;

    /**
     * 通道id
     */
    @TableField(value = "tag_id")
    @ApiModelProperty(name = "tagId", value = "通道id")
    private Integer tagId;

    /**
     * 通道地址
     */
    @ApiModelProperty(name = "address", value = "通道地址")
    private Integer address;

    /**
     * 上限值
     */
    @TableField(value = "upper_value")
    @ApiModelProperty(name = "upperValue", value = "上限值")
    private Double upperValue;

    /**
     * 下限值
     */
    @TableField(value = "lower_value")
    @ApiModelProperty(name = "lowerValue", value = "下限值")
    private Double lowerValue;

    /**
     * 信号名称
     */
    @ApiModelProperty(name = "name", value = "信号名称")
    private String name;

    /**
     * 通道值
     */
    @ApiModelProperty(name = "value", value = "通道值")
    private Double value;

    /**
     * 通道电流值
     */
    @TableField(value = "electricity_value")
    @ApiModelProperty(name = "electricityValue", value = "通道电流值")
    private Double electricityValue;

    /**
     * 外键
     */
    @TableField(value = "can_channel_type_id")
    @ApiModelProperty(name = "canChannelTypeId", value = "外键")
    private Integer canChannelTypeId;

    /**
     * 外键:监控类型
     */
    @ApiModelProperty(name = "sid", value = "外键:监控类型")
    private Integer sid;

    /**
     * can_device的索引
     */
    @ApiModelProperty(name = "canIndex", value = "can_device的索引")
    private Integer canIndex;

    /**
     * 外键:can设备id
     */
    @ApiModelProperty(name = "did", value = "外键:can设备id")
    private Integer did;

    /**
     * 外键:设备类型编码
     */
    @ApiModelProperty(name = "dsn", value = "外键:设备类型编码")
    private Integer dsn;

    /**
     * 外键:强电节点id
     */
    @ApiModelProperty(name = "nid", value = "外键:强电节点id")
    private Integer nid;

    @ApiModelProperty(name = "strategyId", value = "当前绑定的策略ID")
    private Integer strategyId;

    /**
     * 所属设备
     */
    @ApiModelProperty(name = "canDevice", value = "所属设备")
    private transient CanDevice canDevice;

    /**
     * 场景对象
     */
    @ApiModelProperty(name = "canScene", value = "场景对象")
    private transient CanScene canScene;

    /**
     * 所属节点
     */
    @ApiModelProperty(name = "electricityNode", value = "所属节点")
    private transient ElectricityNode electricityNode;

}