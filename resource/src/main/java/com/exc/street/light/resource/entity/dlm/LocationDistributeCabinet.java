/**
 * @filename:LocationDistributeCabinet 2020-08-22
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
public class LocationDistributeCabinet extends Model<LocationDistributeCabinet> {

	private static final long serialVersionUID = 1598064024658L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "配电柜表")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "配电柜名称")
	private String name;
    
	@ApiModelProperty(name = "num" , value = "配电柜编号")
	private String num;
    
	@ApiModelProperty(name = "state" , value = "配电柜状态（1：正常，0：异常）默认1")
	private Integer state;
    
	@ApiModelProperty(name = "areaId" , value = "区域id")
	private Integer areaId;

	@ApiModelProperty(name = "streetId" , value = "街道id")
	private Integer streetId;
    
	@ApiModelProperty(name = "longitude" , value = "经度")
	private Double longitude;
    
	@ApiModelProperty(name = "latitude" , value = "维度")
	private Double latitude;
    
	@ApiModelProperty(name = "location" , value = "安装位置")
	private String location;
    
	@ApiModelProperty(name = "creator" , value = "创建人")
	private Integer creator;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@ApiModelProperty(name = "description" , value = "配电柜描述")
	private String description;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
