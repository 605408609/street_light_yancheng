/**
 * @filename:RadioPlay 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**   
 * 公共广播节目播放参数
 * 
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Getter
@Setter
@ToString
public class PbReqRadioPlayVO {
	@ApiModelProperty(name = "id" , value = "广播播放表id，自增")
	private Integer id;

	//执行模式(1:每天执行,2:每周执行,3:每月执行,4:一次性执行)
	@ApiModelProperty(name = "executionMode" , value = "执行模式(1:定时执行,2:周期执行,4:单次执行)")
	private Integer executionMode;

    @DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "executionTime" , value = "执行时间（时分秒）")
	private Date executionTime;

    @DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "executionEndTime" , value = "执行结束时间（时分秒）")
	private Date executionEndTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	@ApiModelProperty(name = "startDate" , value = "开始时间（年月日）")
	private Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	@ApiModelProperty(name = "endDate" , value = "结束时间（年月日）")
	private Date endDate;

	@ApiModelProperty(name = "weekValue" , value = "周控制值集合(周一:1,以此类推: 周日:7)")
	private List<Integer> weekValue;

	@ApiModelProperty(name = "playStatus" , value = "播放状态（0：待播放，1：正在播放 2:结束播放")
	private Integer playStatus;

	@ApiModelProperty(name = "playVol" , value = "播放音量")
	private Integer playVol;

	@ApiModelProperty(name = "deviceType" , value = "设备归属（1：区域 2：分组）")
	private Integer deviceType;

	@ApiModelProperty(name = "number" , value = "节目循环播放次数")
	private Integer number;

	@ApiModelProperty(name = "programId" , value = "节目id")
	private Integer programId;

	@ApiModelProperty(name = "sessionId" , value = "雷拓节目播放会话ID")
	private Integer sessionId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@ApiModelProperty(name = "deviceIds" , value = "公共广播设备id集合")
	private List<Integer> deviceIds;

	@ApiModelProperty(name = "groupIds" , value = "分组id集合")
	private List<Integer> groupIds;

	@ApiModelProperty(name = "reqRadioProgramVO" , value = "节目对象")
	private PbReqRadioProgramVO reqRadioProgramVO;
}
