package com.exc.street.light.resource.entity.electricity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * can监控类型表
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("can_supervision_type")
public class CanSupervisionType extends Model<CanSupervisionType> {

    private static final long serialVersionUID = -7846306995471785840L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    @ApiModelProperty(name = "type", value = "")
    private Integer type;

    /**
     * 编号
     */
    @ApiModelProperty(name = "type", value = "编号")
    private String sn;

    /**
     * 监控点名称
     */
    @ApiModelProperty(name = "type", value = "监控点名称")
    private String name;

    /**
     * 控制值(1/4/8)
     */
    @ApiModelProperty(name = "type", value = "控制值(1/4/8)")
    private Integer value;

}