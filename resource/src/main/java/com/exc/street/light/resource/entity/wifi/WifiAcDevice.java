/**
 * @filename:WifiAcDevice 2020-03-27
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.wifi;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

/**   
 * @Description: ac设备实体类
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WifiAcDevice extends Model<WifiAcDevice> {

	private static final long serialVersionUID = 1585302898681L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键id")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "设备名称")
	private String name;
    
	@ApiModelProperty(name = "num" , value = "设备编号")
	private String num;
    
	@ApiModelProperty(name = "model" , value = "设备型号")
	private String model;
    
	@ApiModelProperty(name = "ip" , value = "ip地址")
	private String ip;
    
	@ApiModelProperty(name = "mac" , value = "Mac地址")
	private String mac;

	@ApiModelProperty(name = "connApCount" , value = "连接ap数量")
	private Integer connApCount;
    
	@ApiModelProperty(name = "factory" , value = "设备厂家")
	private String factory;
    
	@ApiModelProperty(name = "networkState" , value = "网络状态(0：离线 1：在线)")
	@ApiParam(hidden = true)
	private Integer networkState;
    
	@ApiModelProperty(name = "areaId" , value = "区域id")
	private Integer areaId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	@ApiParam(hidden = true)
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
	@ApiParam(hidden = true)
	private Date lastOnlineTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
