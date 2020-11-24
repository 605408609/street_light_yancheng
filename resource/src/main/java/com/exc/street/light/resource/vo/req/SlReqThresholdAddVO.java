/**
 * @filename:SystemDeviceThreshold 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.req;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SlReqThresholdAddVO {

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "设备id")
	private Integer id;
    
	@ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
	private Integer deviceTypeId;
    
	@ApiModelProperty(name = "filed" , value = "阈值字段")
	private String filed;
    
	@ApiModelProperty(name = "unit" , value = "阈值单位")
	private String unit;
    
	@ApiModelProperty(name = "name" , value = "阈值含义")
	private String name;
    
	@ApiModelProperty(name = "isMust" , value = "是否必填（0：否，1：是）")
	private Integer isMust;
    
	@ApiModelProperty(name = "regexp" , value = "校验正则表达式")
	private String regexp;
    
	@ApiModelProperty(name = "icon" , value = "小图标文件名")
	private String icon;

	@ApiModelProperty(name = "thresholdValue" , value = "阈值")
	private String thresholdValue;
    
}
