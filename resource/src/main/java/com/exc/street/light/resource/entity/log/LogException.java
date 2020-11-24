/**
 * @filename:LogException 2020-05-08
 * @project log  V1.0
 * Copyright(c) 2020 xuJiaHao Co. Ltd. 
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
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: xuJiaHao
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogException extends Model<LogException> {

	private static final long serialVersionUID = 1588917042706L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键ID")
	private Long id;
    
	@ApiModelProperty(name = "modul" , value = "功能模块")
	private String modul;
    
	@ApiModelProperty(name = "logType" , value = "操作类型")
	private String logType;
    
	@ApiModelProperty(name = "description" , value = "操作描述")
	private String description;
    
	@ApiModelProperty(name = "requParam" , value = "请求参数")
	private String requParam;
    
	@ApiModelProperty(name = "userId" , value = "用户id")
	private Integer userId;
    
	@ApiModelProperty(name = "userName" , value = "用户姓名")
	private String userName;
    
	@ApiModelProperty(name = "method" , value = "操作方法")
	private String method;
    
	@ApiModelProperty(name = "uri" , value = "请求uri")
	private String uri;
    
	@ApiModelProperty(name = "requIp" , value = "操作请求ip")
	private String requIp;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "操作创建时间")
	private Date createTime;

	@ApiModelProperty(name = "reason" , value = "错误原因")
	private String reason;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
