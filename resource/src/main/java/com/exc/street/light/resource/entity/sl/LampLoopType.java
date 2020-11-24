/**
 * @filename:LampDevice 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
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
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: Huangjinhao
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LampLoopType extends Model<LampLoopType> {

	private static final long serialVersionUID = -5508418732123133067L;

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "回路类型id，自增长")
	private Integer id;

	@ApiModelProperty(name = "type" , value = "回路类型")
	private String type;
    
	@ApiModelProperty(name = "loopTotal" , value = "回路数量")
	private Integer loopTotal;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
