/**
 * @filename:LampStrategyActionDeviceType 2020-08-27
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.sl;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**   
 * @Description: 策略动作设备类型中间表(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LampStrategyActionDeviceType extends Model<LampStrategyActionDeviceType> {

	private static final long serialVersionUID = 1598516796251L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键id")
	private Integer id;
    
	@ApiModelProperty(name = "lampStrategyActionId" , value = "策略动作id")
	private Integer lampStrategyActionId;
    
	@ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
	private Integer deviceTypeId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
