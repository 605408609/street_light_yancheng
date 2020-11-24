/**
 * @filename:Role 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.ua;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
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
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Role extends Model<Role> {

	private static final long serialVersionUID = 1584013158613L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键id")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "角色名称")
	private String name;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	@ApiParam(hidden = true)
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "更新时间")
	@ApiParam(hidden = true)
	private Date updateTime;

	@ApiModelProperty(name = "founderId" , value = "创建人id")
	@ApiParam(hidden = true)
	private Integer founderId;

	@ApiModelProperty(name = "type" , value = "类型: 1:系统固有角色  2:自定义角色")
	@ApiParam(hidden = true)
	private Integer type;
    
	@ApiModelProperty(name = "areaId" , value = "区域id")
	@ApiParam(hidden = true)
	private Integer areaId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
