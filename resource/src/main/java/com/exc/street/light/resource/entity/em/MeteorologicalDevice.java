/**
 * @filename:MeteorologicalDevice 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.em;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;
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
public class MeteorologicalDevice extends Model<MeteorologicalDevice> {

	private static final long serialVersionUID = 1584772749293L;

	/**
	 * 气象传感器参数表id，自增长
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "气象传感器参数表id，自增长")
	@Ignore
	private Integer id;

	/**
	 * 设备型号
	 */
	@ApiModelProperty(name = "model" , value = "设备型号")
	private String model;

	/**
	 * ip
	 */
	@ApiModelProperty(name = "ip" , value = "ip")
	private String ip;

	/**
	 * 端口
	 */
	@ApiModelProperty(name = "port" , value = "端口")
	private Integer port;

	/**
	 * 设备厂家
	 */
	@ApiModelProperty(name = "factory" , value = "设备厂家")
	private String factory;

	/**
	 * 设备名称
	 */
	@ApiModelProperty(name = "name" , value = "设备名称")
	private String name;

	/**
	 * 设备编号
	 */
	@ApiModelProperty(name = "num" , value = "设备编号")
	private String num;

	/**
	 * 网络状态(0:离线，1:在线)默认0
	 */
	@ApiModelProperty(name = "networkState" , value = "网络状态(0:离线，1:在线)默认0")
	@Ignore
	private Integer networkState;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	@Ignore
	private Date createTime;

	/**
	 * 最后在线时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "lastOnlineTime" , value = "最后在线时间")
	@Ignore
	private Date lastOnlineTime;

	/**
	 * 灯杆id
	 */
	@ApiModelProperty(name = "lampPostId" , value = "灯杆id")
	private Integer lampPostId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
