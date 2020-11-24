/**
 * @filename:LocationControlTypeLoop 2020-09-01
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.dlm;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**   
 * @Description: 集中控制器类型回路中间表(实体类)
 * 
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LocationControlTypeLoop extends Model<LocationControlTypeLoop> {

	private static final long serialVersionUID = 1598939995636L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键id")
	private Integer id;
    
	@ApiModelProperty(name = "loopId" , value = "分组或回路（1回路 2分组 3支路）")
	private Integer loopId;
    
	@ApiModelProperty(name = "locationControlTypeId" , value = "集中控制器类型id")
	private Integer locationControlTypeId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
