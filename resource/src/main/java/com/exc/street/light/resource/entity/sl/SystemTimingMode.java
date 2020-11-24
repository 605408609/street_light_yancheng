/**
 * @filename:SystemTimingMode 2020-08-27
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.sl;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class SystemTimingMode extends Model<SystemTimingMode> {

	private static final long serialVersionUID = 1598496874036L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "定时方式表")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "名称")
	private String name;
    
	@ApiModelProperty(name = "mutexGroup" , value = "互斥组别")
	private String mutexGroup;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
