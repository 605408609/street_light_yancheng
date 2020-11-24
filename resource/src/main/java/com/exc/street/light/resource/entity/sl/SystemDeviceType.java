/**
 * @filename:SystemDeviceType 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
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
 * @Description:TODO(设备类型表实体类)
 * 
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SystemDeviceType extends Model<SystemDeviceType> {

	private static final long serialVersionUID = 1598080861798L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "设备类型表")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "类型名称")
	private String name;
    
	@ApiModelProperty(name = "edition" , value = "版本号")
	private String edition;
    
	@ApiModelProperty(name = "protocol" , value = "协议")
	private String protocol;
    
	@ApiModelProperty(name = "socket" , value = "通信方式")
	private String socket;
    
	@ApiModelProperty(name = "factoryId" , value = "厂家")
	private Integer factoryId;

	@ApiModelProperty(name = "loopType" , value = "回路类型")
	private String loopType;

	@ApiModelProperty(name = "description" , value = "设备类型描述")
	private String description;

	@ApiModelProperty(name = "numLength" , value = "编号长度")
	private Integer numLength;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
