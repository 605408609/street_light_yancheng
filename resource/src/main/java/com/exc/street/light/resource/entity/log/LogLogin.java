/**
 * @filename:LogLogin 2020-06-09
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.log;

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
 * @Description:登录日志(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogLogin extends Model<LogLogin> {

	private static final long serialVersionUID = 1591694440708L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键id")
	private Integer id;
    
	@ApiModelProperty(name = "operatorId" , value = "操作人员id")
	private Integer operatorId;
    
	@ApiModelProperty(name = "requIp" , value = "操作请求ip")
	private String requIp;
    
	@ApiModelProperty(name = "description" , value = "操作描述")
	private String description;
    
	@ApiModelProperty(name = "status" , value = "登录状态（1：成功 0：失败）")
	private Integer status;
    
	@ApiModelProperty(name = "method" , value = "操作方法")
	private String method;
    
	@ApiModelProperty(name = "uri" , value = "请求uri")
	private String uri;
    
	@ApiModelProperty(name = "requParam" , value = "请求参数")
	private String requParam;
    
	@ApiModelProperty(name = "respParam" , value = "返回参数")
	private String respParam;
    
	@ApiModelProperty(name = "reason" , value = "异常信息")
	private String reason;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "操作创建时间")
	private Date createTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
