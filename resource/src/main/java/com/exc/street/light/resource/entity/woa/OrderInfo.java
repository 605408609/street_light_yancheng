/**
 * @filename:Order 2020-03-28
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
public class OrderInfo extends Model<OrderInfo> {

	private static final long serialVersionUID = 1585378245009L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "工单表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "工单名称")
	private String name;
    
	@ApiModelProperty(name = "addr" , value = "工单发生地址")
	private String addr;
    
	@ApiModelProperty(name = "description" , value = "工单描述")
	private String description;
    
	@ApiModelProperty(name = "statusId" , value = "工单状态id")
	private Integer statusId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "finishTime" , value = "指定完成时间")
	private Date finishTime;
    
	@ApiModelProperty(name = "processor" , value = "处理人")
	private Integer processor;

	@ApiModelProperty(name = "processorRole" , value = "处理角色")
	private Integer processorRole;

	@ApiModelProperty(name = "alarmTypeId" , value = "告警类型id")
	private Integer alarmTypeId;
    
	@ApiModelProperty(name = "creator" , value = "创建人")
	private Integer creator;

	@ApiModelProperty(name = "approval" , value = "审核人")
	private Integer approval;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "修改时间")
	private Date updateTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@ApiModelProperty(name = "overtime" , value = "是否已超时（0：否，1：是）")
	private Integer overtime;

	@ApiModelProperty(name = "newStatus" , value = "工单处于新状态，未浏览（0：否，1：是）")
	private Integer newStatus;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "startHandleTime" , value = "开始处理时间")
	private Date startHandleTime;

	@ApiModelProperty(name = "lampPostId" , value = "灯杆id")
	private Integer lampPostId;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
