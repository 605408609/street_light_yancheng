/**
 * @filename:SystemAreaParameter 2020-08-31
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.dlm;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.exc.street.light.resource.entity.sl.SystemDeviceType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**   
 * @Description: 区域参数(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SystemAreaParameter extends Model<SystemAreaParameter> {

	private static final long serialVersionUID = 1598852374760L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "区域参数表")
	private Integer id;
    
	@ApiModelProperty(name = "areaId" , value = "区域id")
	private Integer areaId;
    
	@ApiModelProperty(name = "filed" , value = "参数字段")
	private String filed;
    
	@ApiModelProperty(name = "unit" , value = "参数单位")
	private String unit;
    
	@ApiModelProperty(name = "name" , value = "参数含义")
	private String name;
    
	@ApiModelProperty(name = "value" , value = "参数值")
	private String value;

	@ApiModelProperty(name = "remarks" , value = "备注")
	private String remarks;

	@ApiModelProperty(name = "systemDeviceType" , value = "设备类型对象")
	private SystemDeviceType systemDeviceType;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
