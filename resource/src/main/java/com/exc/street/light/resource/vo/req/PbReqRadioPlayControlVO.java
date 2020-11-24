/**
 * @filename:RadioProgram 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**   
 * 公共广播节目接收参数
 * 
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Getter
@Setter
@ToString
public class PbReqRadioPlayControlVO {
	/**
	 * 节目播放ID
	 */
	@ApiModelProperty(name = "playId" , value = "节目播放ID")
	private Integer playId;

	/**
	 * 节目播放状态， 0：停止播放，1：开始播放
	 */
	@ApiModelProperty(name = "PlayStatus" , value = "节目播放状态")
	private Integer playStatus;
}
