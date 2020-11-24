/**
 * @filename:LampStrategy 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LampStrategy extends Model<LampStrategy> {

	private static final long serialVersionUID = 1598412286275L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "灯具策略表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "策略名称")
	private String name;
    
	@ApiModelProperty(name = "strategyTypeId" , value = "策略类型id")
	private Integer strategyTypeId;
    
	@ApiModelProperty(name = "description" , value = "策略描述")
	private String description;
    
	@ApiModelProperty(name = "creator" , value = "创建人")
	private Integer creator;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "修改时间")
	private Date updateTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@ApiModelProperty(name = "idSynchro" , value = "是否同步（0：否，1：是）默认0")
	private Integer idSynchro;
    
	@ApiModelProperty(name = "isDelete" , value = "是否删除（0：未删除，1：已删除）")
	private Integer isDelete;

	@ApiModelProperty(name = "scene" , value = "场景号（0~65535）")
	private Integer scene;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
