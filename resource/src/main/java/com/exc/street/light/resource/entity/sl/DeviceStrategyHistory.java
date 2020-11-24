/**
 * @filename:DeviceStrategyHistory 2020-09-04
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
 * @Description: 设备策略历史中间表(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceStrategyHistory extends Model<DeviceStrategyHistory> {

	private static final long serialVersionUID = 1599208589603L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "设备策略历史中间表")
	private Integer id;
    
	@ApiModelProperty(name = "deviceId" , value = "设备d")
	private Integer deviceId;
    
	@ApiModelProperty(name = "strategyId" , value = "策略id")
	private Integer strategyId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@ApiModelProperty(name = "isSuccess" , value = "策略是否下发成功,默认为0（失败）")
	private Integer isSuccess;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
