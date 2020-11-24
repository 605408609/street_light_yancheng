/**
 * @filename:AhDevice 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.occ;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**   
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AhDevice extends Model<AhDevice> {

	private static final long serialVersionUID = 1584332811936L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "呼叫设备表id，自增长")
	private Integer id;
    
	@ApiModelProperty(name = "model" , value = "设备型号")
	private String model;
    
	@ApiModelProperty(name = "ip" , value = "ip")
	private String ip;
    
	@ApiModelProperty(name = "port" , value = "端口")
	private Integer port;
    
	@ApiModelProperty(name = "factory" , value = "设备厂家")
	private String factory;
    
	@ApiModelProperty(name = "name" , value = "设备名称")
	private String name;
    
	@ApiModelProperty(name = "num" , value = "设备编号")
	private String num;
    
	@ApiModelProperty(name = "networkState" , value = "网络状态(0:离线，1:在线)默认0")
	private Integer networkState;
    
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
	
	@ApiModelProperty(name = "registration_success" , value = "监听服务是否注册设备成功,0 未注册或注册失败 , 1 注册成功")
	private Integer registrationSuccess;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
