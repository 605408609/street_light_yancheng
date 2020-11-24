/**
 * @filename:LocationControl 2020-08-22
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
 * @Description: 集中控制器(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LocationControl extends Model<LocationControl> {

	private static final long serialVersionUID = 1598081425396L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "集中控制器表")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "名称")
	private String name;
    
	@ApiModelProperty(name = "num" , value = "编号")
	private String num;
    
	@ApiModelProperty(name = "location" , value = "地址")
	private String location;
    
	@ApiModelProperty(name = "typeId" , value = "集中控制器类型id")
	private Integer typeId;
    
	@ApiModelProperty(name = "isOnline" , value = "网络状态(0:离线，1:在线)默认0")
	private Integer isOnline;
    
	@ApiModelProperty(name = "creator" , value = "创建人")
	private Integer creator;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
	private Date lastOnlineTime;
    
	@ApiModelProperty(name = "description" , value = "集中控制器描述")
	private String description;
    
	@ApiModelProperty(name = "cabinetId" , value = "配电柜id")
	private Integer cabinetId;
    
	@ApiModelProperty(name = "ip" , value = "ip")
	private String ip;
    
	@ApiModelProperty(name = "port" , value = "端口")
	private String port;
    
	@ApiModelProperty(name = "mac" , value = "mac地址")
	private String mac;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
