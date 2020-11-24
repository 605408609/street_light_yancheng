package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * can模块类型表
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("can_module_type")
public class CanModuleType extends Model<CanModuleType> {

    private static final long serialVersionUID = -5200209367164417005L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(name = "id", value = "名称")
    private String name;
}