package com.exc.street.light.resource.entity.electricity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备类型表
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Setter
@Getter
@TableName("can_device_type")
public class CanDeviceType extends Model<CanDeviceType> {

    private static final long serialVersionUID = 8282283845093018338L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "id，自增")
    private Integer id;

    /**
     * 设备类型编号
     */
    @ApiModelProperty(name = "sn", value = "设备类型编号")
    private Integer sn;

    /**
     * 名称
     */
    @ApiModelProperty(name = "name", value = "名称")
    private String name;
}