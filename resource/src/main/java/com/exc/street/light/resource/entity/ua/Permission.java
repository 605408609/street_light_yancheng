/**
 * @filename:Permission 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.ua;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class Permission extends Model<Permission> {

	private static final long serialVersionUID = 1584013158613L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键id")
	private Integer id;
    
	@ApiModelProperty(name = "code" , value = "权限名")
	private String code;
    
	@ApiModelProperty(name = "name" , value = "权限中文名")
	private String name;
    
	@ApiModelProperty(name = "uri" , value = "uri")
	private String uri;
    
	@ApiModelProperty(name = "sort" , value = "种类(1:模块菜单 2:一级菜单 3:二级菜单 4:权限按钮)")
	private Integer sort;
    
	@ApiModelProperty(name = "parentId" , value = "父类id")
	private Integer parentId;

	@ApiModelProperty(name = "type" , value = "类型")
	private String type;

	@ApiModelProperty(name = "orders" , value = "排序")
	private Integer orders;

	@ApiModelProperty(name = "isShow" , value = "是否显示（0：隐藏 1：显示，默认显示）")
	private Integer isShow;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
