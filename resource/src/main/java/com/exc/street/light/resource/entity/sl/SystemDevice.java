/**
 * @filename:SystemDevice 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
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
 * @Description:TODO(设备表实体类)
 * 
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SystemDevice extends Model<SystemDevice> {

	private static final long serialVersionUID = 1599039507868L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "设备表")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "设备名称")
	private String name;
    
	@ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
	private Integer deviceTypeId;
    
	@ApiModelProperty(name = "num" , value = "编号")
	private String num;
    
	@ApiModelProperty(name = "location" , value = "地址")
	private String location;
    
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
    
	@ApiModelProperty(name = "description" , value = "设备描述")
	private String description;
    
	@ApiModelProperty(name = "lampPostId" , value = "灯杆id")
	private Integer lampPostId;
    
	@ApiModelProperty(name = "ip" , value = "ip")
	private String ip;
    
	@ApiModelProperty(name = "port" , value = "端口")
	private String port;
    
	@ApiModelProperty(name = "mac" , value = "mac地址")
	private String mac;
    
	@ApiModelProperty(name = "strategyId" , value = "当前策略id")
	private Integer strategyId;
    
	@ApiModelProperty(name = "isDelete" , value = "是否删除（0：未删除，1：已删除）")
	private Integer isDelete;

	@ApiModelProperty(name = "deviceState" , value = "开关状态（0-关，1-开）")
	private Integer deviceState;

	@ApiModelProperty(name = "setLonLat" , value = "是否设置经纬度（0-未设置，1-已设置）")
	private Integer setLonLat;

	@ApiModelProperty(name = "setStrategy" , value = "是否设置策略（0-未设置，1-已设置）")
	private Integer setStrategy;

	@ApiModelProperty(name = "reserveOne" , value = "预留字段一")
	private String reserveOne;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
