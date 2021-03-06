/**
 * @filename:LampStrategyAction 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.sl;

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
import java.util.Date;

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
public class LampStrategyActionVO extends Model<LampStrategyActionVO> {

	private static final long serialVersionUID = 1584699575481L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "灯具策略动作表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "type" , value = "策略动作类型（1：开灯，0：关灯）")
	private Integer type;
    
	@ApiModelProperty(name = "name" , value = "动作名称")
	private String name;
    
	@ApiModelProperty(name = "brightness" , value = "亮度，默认0")
	private Integer brightness;
    
	@ApiModelProperty(name = "executionMode" , value = "执行模式(1:每天执行,2:每周执行)")
	private Integer executionMode;
    
	@ApiModelProperty(name = "executionTime" , value = "执行时间（时分秒）")
	private String executionTime;
    
	@ApiModelProperty(name = "startDate" , value = "开始时间（年月日）")
	private String startDate;
    
	@ApiModelProperty(name = "endDate" , value = "结束时间（年月日）")
	private String endDate;
    
	@ApiModelProperty(name = "weekValue" , value = "周控制值：1-7:周日到周六循环执行")
	private Integer weekValue;
    
	@ApiModelProperty(name = "strategyId" , value = "策略id")
	private Integer strategyId;
    
	@ApiModelProperty(name = "cron" , value = "cron表达式")
	private String cron;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "修改时间")
	private Date updateTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@ApiModelProperty(name = "scene" , value = "场景")
	private Integer scene;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
