/**
 * @filename:AlarmOrderSet 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.woa;
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
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AlarmOrderSet extends Model<AlarmOrderSet> {

	private static final long serialVersionUID = 1585363939673L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "告警生成工单设置表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "status" , value = "自动生成工单状态（0：关闭，1：开启）默认1")
	private Integer status;
    
	@ApiModelProperty(name = "handleDuration" , value = "处理时长（单位：小时），默认：24")
	private Integer handleDuration;

	@ApiModelProperty(name = "autoDuration" , value = "自动生成工单间隔（单位：分），默认5")
	private Integer autoDuration;

	@ApiModelProperty(name = "processor" , value = "处理人")
	private Integer processor;

	@ApiModelProperty(name = "creator" , value = "创建人id")
	private Integer creator;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
