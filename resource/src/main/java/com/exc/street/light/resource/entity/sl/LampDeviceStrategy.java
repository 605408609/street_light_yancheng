/**
 * @filename:LampDeviceStrategy 2020-03-24
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
public class LampDeviceStrategy extends Model<LampDeviceStrategy> {

	private static final long serialVersionUID = 1585036051452L;

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "路灯设备与策略中间表")
	private Integer id;

	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;

	@ApiModelProperty(name = "strategyId" , value = "策略id")
	private Integer strategyId;

	@ApiModelProperty(name = "used" , value = "使用状况（1：使用中，0：未使用）")
	private Integer used;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
