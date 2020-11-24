/**
 * @filename:ControlLoop 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.dlm;
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
public class ControlLoop extends Model<ControlLoop> {

	private static final long serialVersionUID = 1598237405346L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "集中控制器回路（分组）表")
	private Integer id;
    
	@ApiModelProperty(name = "typeId" , value = "集中控制器回路类型（1：回路，2：分组, 3：支路 4：自研回路）")
	private Integer typeId;
    
	@ApiModelProperty(name = "name" , value = "名称")
	private String name;
    
	@ApiModelProperty(name = "num" , value = "编号")
	private String num;
    
	@ApiModelProperty(name = "type" , value = "回路类型（1：灯杆回路）")
	private Integer type;
    
	@ApiModelProperty(name = "isOpen" , value = "开关状态（0：关，1：开）")
	private Integer isOpen;
    
	@ApiModelProperty(name = "sceneId" , value = "回路场景Id")
	private Integer sceneId;

	@ApiModelProperty(name = "sceneStrategyId" , value = "回路场景策略Id")
	private Integer sceneStrategyId;

	@ApiModelProperty(name = "creator" , value = "创建人")
	private Integer creator;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "修改时间")
	private Date updateTime;
    
	@ApiModelProperty(name = "description" , value = "集中控制器回路描述")
	private String description;
    
	@ApiModelProperty(name = "controlId" , value = "集中控制器id")
	private Integer controlId;

	@ApiModelProperty(name = "sn" , value = "序列号")
	private String sn;

	@ApiModelProperty(name = "orders" , value = "序号")
	private Integer orders;

	@ApiModelProperty(name = "isUse" , value = "是否启用场景（0：未启用 1：启用）")
	private Integer isUse;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
