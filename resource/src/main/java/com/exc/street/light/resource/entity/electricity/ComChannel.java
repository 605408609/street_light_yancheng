package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * com回路表
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName( "com_channel")
public class ComChannel extends Model<CanTiming> {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

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
     * 通道类型
     */
    @ApiModelProperty(name = "type", value = "通道类型")
    private Integer type;

    /**
     * 设备类型
     */
    @TableField(value = "device_address")
    @ApiModelProperty(name = "deviceAddress", value = "设备类型")
    private Integer deviceAddress;

    /**
     * 串口号
     */
    @TableField(value = "com_port")
    @ApiModelProperty(name = "comPort", value = "串口号")
    private Integer comPort;

    /**
     * 死区值
     */
    @TableField(value = "bad_value")
    @ApiModelProperty(name = "badValue", value = "死区值")
    private Double badValue;

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
     * 外键:电表设备id
     */
    @ApiModelProperty(name = "cid", value = "外键:电表设备id")
    private Integer cid;

    /**
     * 强电节点id
     */
    @ApiModelProperty(name = "nid", value = "强电节点id")
    private Integer nid;

}