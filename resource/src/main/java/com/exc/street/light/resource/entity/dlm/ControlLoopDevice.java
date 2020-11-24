/**
 * @filename:ControlLoopDevice 2020-08-24
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
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
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
public class ControlLoopDevice extends Model<ControlLoopDevice> {

	private static final long serialVersionUID = 1598252284869L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "灯具、回路（分组）、集中控制器关联表")
	private Integer id;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;
    
	@ApiModelProperty(name = "controlId" , value = "集中控制器id")
	private Integer controlId;
    
	@ApiModelProperty(name = "loopId" , value = "集中控制回路（分组）id")
	private Integer loopId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
