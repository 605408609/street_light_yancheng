/**
 * @filename:AhPlay 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.occ;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**   
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AhPlay extends Model<AhPlay> {

	private static final long serialVersionUID = 1584363060540L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "报警信息表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "status" , value = "状态（0：紧急求助报警，1：紧急求助报警恢复）默认0")
	private Integer status;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@ApiModelProperty(name = "deviceNum" , value = "设备编号")
	private String deviceNum;
	
	@ApiModelProperty(name = "lampId" , value = "灯杆ID")
	private Integer lampId;
    
	@ApiModelProperty(name = "content" , value = "报警内容")
	private String content;
	
	@ApiModelProperty(name = "haveRead" , value = "是否已读（0：未读，1：已读）")
	private Integer haveRead;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
