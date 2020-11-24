/**
 * @filename:RadioDevice 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.pb;
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
 * @author: LeiJing
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RadioDevice extends Model<RadioDevice> {

	private static final long serialVersionUID = 1584773703240L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "广播表id，自增长")
	private Integer id;
    
	@ApiModelProperty(name = "model" , value = "设备型号")
	private String model;
    
	@ApiModelProperty(name = "ip" , value = "ip")
	private String ip;
    
	@ApiModelProperty(name = "mac" , value = "mac地址")
	private String mac;
    
	@ApiModelProperty(name = "volume" , value = "广播音量")
	private Integer volume;
    
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

	/**
	 * 雷拓IP广播平台终端编号, 设备id
	 */
	@ApiModelProperty(name = "termId" , value = "雷拓IP广播平台终端编号")
	private Integer termId;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
