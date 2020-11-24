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

import java.util.Date;

/**
 * 告警数据实体类
 *
 * @author Linshiwen
 * @date 2019/1/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("can_alarm_data")
public class CanAlarmData extends Model<CanTiming> {

    private static final long serialVersionUID = 6361192857683248914L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    @TableField(value = "tag_id")
    @ApiModelProperty(name = "tagId", value = "通道编号id")
    private Integer tagId;

    @TableField(value = "tag_value")
    @ApiModelProperty(name = "tagValue", value = "测点数值")
    private Float tagValue;

    @TableField(value = "alarm_value")
    @ApiModelProperty(name = "alarmValue", value = "告警限制值")
    private Float alarmValue;

    @TableField(value = "alarm_type")
    @ApiModelProperty(name = "alarmType", value = "告警类型(1表示高报警，2表示低报警，3表示状态告警)")
    private Integer alarmType;

    @TableField(value = "alarm_action")
    @ApiModelProperty(name = "alarmAction", value = "告警动作(1表示告警发生，2表示告警恢复)")
    private Integer alarmAction;

    @ApiModelProperty(name = "time", value = "采集时间")
    private Date time;

    @ApiModelProperty(name = "nid", value = "强电节点id")
    private Integer nid;

    @ApiModelProperty(name = "address", value = "地址")
    private transient String address;
}