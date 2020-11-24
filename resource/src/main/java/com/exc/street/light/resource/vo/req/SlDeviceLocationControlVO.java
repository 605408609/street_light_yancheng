/**
 * @filename:LocationControl 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved.
		*/
package com.exc.street.light.resource.vo.req;
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

@Data
public class SlDeviceLocationControlVO extends Model<SlDeviceLocationControlVO> {

	private static final long serialVersionUID = 1598081425396L;

	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;

	@ApiModelProperty(name = "num" , value = "集中控制器编号")
	private String num;
}
