package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 回路变化数据实体类
 *
 * @author Linshiwen
 * @date 2019/1/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("can_change_data")
public class CanChangeData extends Model<CanTiming> implements Serializable {

    private static final long serialVersionUID = 8141306032390261797L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    @TableField(value = "tag_id")
    @ApiModelProperty(name = "tagId", value = "通道编号id")
    private Integer tagId;

    @TableField(value = "control_id")
    @ApiModelProperty(name = "controlId", value = "控制id")
    private Integer controlId;

    @ApiModelProperty(name = "value", value = "测点数值")
    private Float value;

    @ApiModelProperty(name = "type", value = "测点类型")
    private Integer type;

    @ApiModelProperty(name = "time", value = "采集时间")
    private Date time;

    @TableField(value = "receive_time")
    @ApiModelProperty(name = "receiveTime", value = "接收时间")
    private Date receiveTime;

    @ApiModelProperty(name = "nid", value = "强电节点id")
    private Integer nid;

    @ApiModelProperty(name = "address", value = "地址")
    private transient Integer address;

    @ApiModelProperty(name = "alarmData", value = "告警数据")
    private transient Integer alarmData;

}