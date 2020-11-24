/**
 * @filename:Group 2020-03-18
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
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
public class LocationGroup extends Model<LocationGroup> {

	private static final long serialVersionUID = 1584525385219L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "分组表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "分组名称")
	private String name;
    
	@ApiModelProperty(name = "description" , value = "分组描述")
	private String description;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@ApiModelProperty(name = "creator" , value = "创建人（运营原型出来后为不可空，关联用户）")
	private Integer creator;

	@ApiModelProperty(name = "typeId" , value = "分组类型（1：灯杆分组，2：灯具分组）")
	private Integer typeId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
