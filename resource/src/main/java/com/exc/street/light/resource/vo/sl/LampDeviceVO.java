/**
 * @filename:LampDevice 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.sl;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class LampDeviceVO extends Model<LampDeviceVO> {

	@ApiModelProperty(name = "model" , value = "设备型号")
	private String model;
    
	/*@ApiModelProperty(name = "ip" , value = "ip")
	private String ip;
    
	@ApiModelProperty(name = "mac" , value = "mac地址")
	private String mac;*/
    
	@ApiModelProperty(name = "factory" , value = "设备厂家")
	private String factory;
    
	@ApiModelProperty(name = "name" , value = "设备名称")
	private String name;
    
	@ApiModelProperty(name = "num" , value = "设备编号")
	private String num;

	@ApiModelProperty(name = "lampPostId" , value = "灯杆id")
	private Integer lampPostId;

	@ApiModelProperty(name = "devEui" , value = "灯具类型")
	private String devEui;

}
