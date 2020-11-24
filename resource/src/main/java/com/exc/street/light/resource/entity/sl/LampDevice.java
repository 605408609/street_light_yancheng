/**
 * @filename:LampDevice 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
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
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

/**   
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LampDevice extends Model<LampDevice> {

	private static final long serialVersionUID = 1584431915591L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "灯控器表id，自增长")
	private Integer id;
    
	@ApiModelProperty(name = "model" , value = "设备型号")
	private String model;
    
	@ApiModelProperty(name = "factory" , value = "设备厂家")
	private String factory;

	@ApiModelProperty(name = "loopTypeId" , value = "回路类型id")
	private Integer loopTypeId;
    
	@ApiModelProperty(name = "num" , value = "设备编号")
	private String num;
    
	@ApiModelProperty(name = "networkState" , value = "网络状态(0:离线，1:在线)默认0")
	private Integer networkState;

	/*@ApiModelProperty(name = "brightState" , value = "亮灯状态（0：关，1：开）默认0")
	private Integer brightState;

	@ApiModelProperty(name = "presetBrightState" , value = "预设亮灯状态（0：关，1：开）默认0")
	private Integer presetBrightState;

	@ApiModelProperty(name = "brightness" , value = "亮度（0-100）默认0")
	private Integer brightness;

	@ApiModelProperty(name = "presetBrightness" , value = "预设亮度（0-100）默认0")
	private Integer presetBrightness;*/
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
	private Date lastOnlineTime;
    
	@ApiModelProperty(name = "lampPostId" , value = "灯杆id")
	private Integer lampPostId;

	@ApiModelProperty(name = "sendId" , value = "信息发送标识")
	private String sendId;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
