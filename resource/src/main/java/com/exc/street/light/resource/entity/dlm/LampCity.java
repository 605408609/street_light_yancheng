package com.exc.street.light.resource.entity.dlm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 城市灯具信息
 *
 * @version: V1.0
 * @author: Huangjinhao
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LampCity  extends Model<LampCity> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id" , value = "区域表id，自增")
    private Integer id;

    @ApiModelProperty(name = "lampState" , value = "区域灯具状态")
    private Integer lampState;

    @ApiModelProperty(name = "lampBrightness" , value = "区域灯具亮度")
    private Integer lampBrightness;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
