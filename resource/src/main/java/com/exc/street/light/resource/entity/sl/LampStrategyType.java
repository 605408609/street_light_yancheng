/**
 * @filename:LampStrategyType 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.sl;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**   
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LampStrategyType extends Model<LampStrategyType> {

	private static final long serialVersionUID = 1584699575481L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "灯具策略类型表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "策略类型名称")
	private String name;

	@ApiModelProperty(name = "presetBrightState" , value = "预设亮灯状态（0：关，1：开）默认0")
	private Integer presetBrightState;
	
	@ApiModelProperty(name = "presetBrightness" , value = "预设亮度（0-100）默认0")
	private Integer presetBrightness;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
