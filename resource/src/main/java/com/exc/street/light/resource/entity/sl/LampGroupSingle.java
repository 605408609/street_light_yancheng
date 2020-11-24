/**
 * @filename:LampGroupSingle 2020-07-16
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.sl;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**   
 * @Description: 灯具分组中间表(灯具分组中间表实体类)
 * 
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LampGroupSingle extends Model<LampGroupSingle> {

	private static final long serialVersionUID = 1594883988373L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "灯具分组中间表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "singleId" , value = "灯具id")
	private Integer singleId;
    
	@ApiModelProperty(name = "lampGroupId" , value = "灯具分组id")
	private Integer lampGroupId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
