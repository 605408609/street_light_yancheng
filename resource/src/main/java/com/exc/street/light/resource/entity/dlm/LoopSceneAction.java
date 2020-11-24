/**
 * @filename:LoopSceneAction 2020-11-07
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.dlm;
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
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoopSceneAction extends Model<LoopSceneAction> {

	private static final long serialVersionUID = 1604730225126L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "回路场景动作表，主键id自增")
	private Integer id;
    
	@ApiModelProperty(name = "isOpen" , value = "场景动作（0：关 1：开）")
	private Integer isOpen;
    
	@ApiModelProperty(name = "executionTime" , value = "执行时间（时分秒）")
	private String executionTime;
    
	@ApiModelProperty(name = "startDate" , value = "开始时间（年月日）")
	private String startDate;
    
	@ApiModelProperty(name = "endDate" , value = "结束时间（年月日）")
	private String endDate;
    
	@ApiModelProperty(name = "weekValue" , value = "周控制值1-7（周日到周六循环执行）")
	private Integer weekValue;
    
	@ApiModelProperty(name = "strategyId" , value = "策略id")
	private Integer strategyId;
    
	@ApiModelProperty(name = "cron" , value = "cron表达式")
	private String cron;
    
	@ApiModelProperty(name = "openModeId" , value = "开关方式id(经纬度)")
	private Integer openModeId;
    
	@ApiModelProperty(name = "deviation" , value = "偏移值（分钟）")
	private Integer deviation;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "更新时间")
	private Date updateTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
