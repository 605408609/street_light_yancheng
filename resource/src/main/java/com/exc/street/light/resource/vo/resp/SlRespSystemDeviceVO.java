/**
 * @filename:LampDevice 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.resp;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 灯控设备详情返回对象
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SlRespSystemDeviceVO extends Model<SlRespSystemDeviceVO> {

	@ApiModelProperty(name = "id" , value = "设备id，自增长")
	private Integer id;

	@ApiModelProperty(name = "model" , value = "设备型号")
	private String model;

	@ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
	private Integer deviceTypeId;

	@ApiModelProperty(name = "factory" , value = "设备厂家")
	private String factory;

	@ApiModelProperty(name = "name" , value = "设备名称")
	private String name;

	@ApiModelProperty(name = "num" , value = "设备编号")
	private String num;

	@ApiModelProperty(name = "isOnline" , value = "网络状态(0:离线，1:在线)默认0")
	private Integer isOnline;

	@ApiModelProperty(name = "deviceState" , value = "亮灯状态（0：关，1：开）默认0")
	private Integer deviceState;

	@ApiModelProperty(name = "brightness" , value = "亮度（0-100）默认0")
	private Integer brightness;

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

	@ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
	private String lampPostName;

	@ApiModelProperty(name = "slLampPost" , value = "灯杆")
	private SlLampPost slLampPost;

	@ApiModelProperty(name = "lampPosition" , value = "灯具位置")
	private String lampPosition;

	@ApiModelProperty(name = "lampPositionId" , value = "灯具位置id")
	private Integer lampPositionId;

	@ApiModelProperty(name = "strategyId" , value = "当前策略id")
	private Integer strategyId;

	@ApiModelProperty(name = "reserveOne" , value = "信息发送标识")
	private String reserveOne;


	/*@ApiModelProperty(name = "slRespStrategyVO" , value = "灯具策略信息")
	private SlRespStrategyVO slRespStrategyVO;


	@ApiModelProperty(name = "voltage" , value = "当前电压有效值")
	private Integer voltage;

	@ApiModelProperty(name = "electricCurrent" , value = "通道当前电流有效值")
	private Integer electricCurrent;

	@ApiModelProperty(name = "power" , value = "通道当前有功功率")
	private Integer power;

	@ApiModelProperty(name = "electricEnergy" , value = "通道有功电能（累计）")
	private Integer electricEnergy;

	@ApiModelProperty(name = "moduleTemperature" , value = "模块温度")
	private Integer moduleTemperature;

	@ApiModelProperty(name = "lampTime" , value = "通道灯具工作时长")
	private Integer lampTime;

	@ApiModelProperty(name = "lampATime" , value = "通道灯具累计工作时长")
	private Integer lampATime;*/

	@ApiModelProperty(name = "slRespSystemDeviceParameterVOS" , value = "特殊字段集合")
	private List<SlRespSystemDeviceParameterVO> slRespSystemDeviceParameterVOS;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
