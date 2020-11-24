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

/**
 * 强电定时表
 *
 * @author Linshiwen
 * @date 2018/6/1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("can_timing")
public class CanTiming extends Model<CanTiming> {

    private static final long serialVersionUID = -261667887227283088L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    @ApiModelProperty(name = "nid", value = "强电节点编号")
    private Integer nid;

    @ApiModelProperty(name = "pid", value = "模式下定时id")
    private Integer pid;

    @ApiModelProperty(name = "isExecute", value = "是否启用 1：执行 2：不执行", required = true)
    private Integer isExecute;

    @ApiModelProperty(name = "name", value = "定时器名称")
    private String name;

    @ApiModelProperty(name = "inputTime", value = "录入时间")
    private Date inputTime;

    /**
     * 场景id 开-回路编号*2+2-1  关-回路编号*2+2
     */
    @ApiModelProperty(name = "tagId", value = "控制对象编码",required = true)
    private Integer tagId;

    @ApiModelProperty(name = "tagType", value = "控制对象类型")
    private Integer tagType = 11;

    @ApiModelProperty(name = "tagValueType", value = "控制值类型")
    private transient Integer tagValueType = 1;

    @ApiModelProperty(name = "tagValue", value = "控制值")
    private Double tagValue = 1D;

    /**
     * 当type==1时必填
     */
    @ApiModelProperty(name = "time", value = "定时时间",required = false)
    private String time;

    /**
     * 当type!=1必填
     */
    @ApiModelProperty(name = "minuteValue", value = "日出日落时间偏移量",required = false)
    private Integer minuteValue;

    /**
     * 不限时间传null
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(name = "startDate", value = "定时开始",required = false)
    private Date startDate;

    /**
     * 不限时间传null
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(name = "endDate", value = "定时结束日期",required = false)
    private Date endDate;

    @ApiModelProperty(name = "type",required = true,value = "定时类型 1:定时执行 2:日出之前 3:日出之后 4:日落之前 5:日落之后")
    private Integer type;

    /**
     * 当执行类型为1（定时）时，该位有效：
     * Bit0：为0以星期定时，为1以年月日定时；Bit1-Bit7对应周一到周日，为0不执行，为1执行；
     * 当执行类型为2~5时，该位无效，保留0即可；
     */
    @TableField(value = "cycle_type")
    @ApiModelProperty(name = "cycleType", value = "周期类型",required = true)
    private Integer cycleType = 1;

    @TableField(value = "execute_value")
    @ApiModelProperty(name = "executeType", value = "是否执行 0-不执行，1-执行")
    private Integer executeType = 1;

    @ApiModelProperty(name = "isExecute", value = "是否执行 1：执行 2：不执行")
    private transient CanScene canScene;
}