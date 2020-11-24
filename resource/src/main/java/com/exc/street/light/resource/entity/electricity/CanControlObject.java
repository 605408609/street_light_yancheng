package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 控制对象表
 *
 * @author Linshiwen
 * @date 2018/5/28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("can_control_object")
public class CanControlObject extends Model<CanScene> {

    private static final long serialVersionUID = 3621507195776660484L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    @ApiModelProperty(name = "tagId", value = "控制对象编码")
    private Integer tagId;

    @ApiModelProperty(name = "tagType", value = "控制对象类型")
    private Integer tagType;

    @ApiModelProperty(name = "tagValue", value = "控制值")
    private Double tagValue;

    @ApiModelProperty(name = "time", value = "延时时长或者定时时长")
    private Integer time = 0;

    @ApiModelProperty(name = "sid", value = "sid")
    private Integer sid;
}