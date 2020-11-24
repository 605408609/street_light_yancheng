/**
 * @filename:LampStrategyActionTimingMode 2020-08-27
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.sl;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
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
public class LampStrategyActionTimingMode extends Model<LampStrategyActionTimingMode> {

	private static final long serialVersionUID = 1598496874036L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "策略动作、定时方式中间表")
	private Integer id;
    
	@ApiModelProperty(name = "timingModeId" , value = "定时方式id")
	private Integer timingModeId;
    
	@ApiModelProperty(name = "strategyActionId" , value = "策略动作id")
	private Integer strategyActionId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
