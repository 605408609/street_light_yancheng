/**
 * @filename:SystemDeviceTypeTimingMode 2020-08-26
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
 * @Description: 设备类型支持的定时模式(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SystemDeviceTypeTimingMode extends Model<SystemDeviceTypeTimingMode> {

	private static final long serialVersionUID = 1598444680982L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "设备类型支持的定时方式")
	private Integer id;
    
	@ApiModelProperty(name = "timingModeId" , value = "定时方式id")
	private Integer timingModeId;
    
	@ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
	private Integer deviceTypeId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
