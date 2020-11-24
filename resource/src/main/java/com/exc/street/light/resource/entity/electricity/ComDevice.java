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

/**
 * com设备表
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Setter
@Getter
@ToString
@TableName("com_device")
public class ComDevice extends Model<ComDevice> {

    private static final long serialVersionUID = -7323924552381427855L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    /**
     * 串口号
     */
    @TableField(value = "can_port")
    @ApiModelProperty(name = "canPort", value = "串口号")
    private Integer canPort;

    /**
     * 设备类型
     */
    @ApiModelProperty(name = "type", value = "设备类型")
    private Integer type;

    /**
     * 设备物理地址
     */
    @TableField(value = "device_address")
    @ApiModelProperty(name = "deviceAddress", value = "设备物理地址")
    private Integer deviceAddress;

    /**
     * 模块地址
     */
    @TableField(value = "module_address")
    @ApiModelProperty(name = "moduleAddress", value = "模块地址")
    private String moduleAddress;

    /**
     * 采集周期
     */
    @TableField(value = "cycle_time")
    @ApiModelProperty(name = "cycleTime", value = "采集周期")
    private Integer cycleTime;
    /**
     * 等待时间
     */
    @TableField(value = "wait_time")
    @ApiModelProperty(name = "waitTime", value = "等待时间")
    private Integer waitTime;

    /**
     * 中断失败基数
     */
    @TableField(value = "failure_value")
    @ApiModelProperty(name = "failureValue", value = "中断失败基数")
    private Integer failureValue;

    /**
     * 设备名称
     */
    @ApiModelProperty(name = "name", value = "设备名称")
    private String name;

    /**
     * 强电节点id
     */
    @ApiModelProperty(name = "nid", value = "强电节点id")
    private Integer nid;

}