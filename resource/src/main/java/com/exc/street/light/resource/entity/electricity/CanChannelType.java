package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 回路类型表
 *
 * @author Linshiwen
 * @date 2018/5/28
 */
@Setter
@Getter
@TableName(value = "can_channel_type")
public class CanChannelType extends Model<CanChannelType> {

    private static final long serialVersionUID = -8350664509835613952L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(name = "name", value = "名称")
    private String name;

    /**
     * 父类型id
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(name = "parentId", value = "父类型id")
    private Integer parentId;
}