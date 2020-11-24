/**
 * @filename:LampStrategyTimingMode 2020-08-27
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
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LampStrategyTimingMode extends Model<LampStrategyTimingMode> {

	private static final long serialVersionUID = 1598496874036L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "策略定时方式中间表")
	private Integer id;
    
	@ApiModelProperty(name = "timingModeId" , value = "定时方式id")
	private Integer timingModeId;
    
	@ApiModelProperty(name = "strategyId" , value = "策略id")
	private Integer strategyId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
