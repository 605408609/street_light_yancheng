/**
 * @filename:RadioPlay 2020-03-21
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
public class RadioPlay extends Model<RadioPlay> {

	private static final long serialVersionUID = 1584773703240L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "广播播放表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "executionMode" , value = "执行模式(1:每天执行,2:每周执行,3:每月执行,4:一次性执行)")
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
    
	@ApiModelProperty(name = "weekValue" , value = "周控制值")
	private Integer weekValue;

	@ApiModelProperty(name = "weekValueStr" , value = "周控制值(集合，用逗号分隔)")
	private String weekValueStr;
    
	@ApiModelProperty(name = "playStatus" , value = "播放状态（0：待播放，1：正在播放 2:结束播放）")
	private Integer playStatus;

	@ApiModelProperty(name = "playVol" , value = "播放音量")
	private Integer playVol;

	@ApiModelProperty(name = "deviceType" , value = "设备类型（1：区域 2：分组）")
	private Integer deviceType;
    
	@ApiModelProperty(name = "number" , value = "节目循环播放次数")
	private Integer number;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;
    
	@ApiModelProperty(name = "programId" , value = "节目id")
	private Integer programId;
    
	@ApiModelProperty(name = "sessionId" , value = "雷拓节目播放会话ID")
	private Integer sessionId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
