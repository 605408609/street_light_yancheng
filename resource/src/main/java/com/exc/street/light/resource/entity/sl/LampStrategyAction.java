/**
 * @filename:LampStrategyAction 2020-08-26
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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LampStrategyAction extends Model<LampStrategyAction> {

	private static final long serialVersionUID = 1598412286275L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "策略动作表")
	private Integer id;
    
	@ApiModelProperty(name = "isOpen" , value = "策略动作类型（1：开灯，0：关灯）")
	private Integer isOpen;
    
	@ApiModelProperty(name = "brightness" , value = "亮度，默认0")
	private Integer brightness;
    
	@ApiModelProperty(name = "executionTime" , value = "执行时间（时分秒）")
	private String executionTime;
    
	@ApiModelProperty(name = "startDate" , value = "开始时间（年月日）")
	private String startDate;
    
	@ApiModelProperty(name = "endDate" , value = "结束时间（年月日）")
	private String endDate;
    
	@ApiModelProperty(name = "weekValue" , value = "周控制值1-7:周日到周六循环执行")
	private Integer weekValue;
    
	@ApiModelProperty(name = "strategyId" , value = "策略id")
	private Integer strategyId;
    
	@ApiModelProperty(name = "cron" , value = "cron表达式")
	private String cron;
    
	@ApiModelProperty(name = "lightModeId" , value = "亮灯方式id")
	private Integer lightModeId;
    
	@ApiModelProperty(name = "deviation" , value = "偏移值（分钟）")
	private Integer deviation;
    
	@ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
	private Integer deviceTypeId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "修改时间")
	private Date updateTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
