/**
 * @filename:LampDeviceParameter 2020-09-03
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
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

/**   
 * @Description:TODO(设备参数数据表实体类)
 * 
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LampDeviceParameter extends Model<LampDeviceParameter> {

	private static final long serialVersionUID = 1599104184513L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "设备参数数据表")
	private Integer id;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;
    
	@ApiModelProperty(name = "parameterValue" , value = "参数值")
	private String parameterValue;
    
	@ApiModelProperty(name = "parameterId" , value = "参数id")
	private Integer parameterId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
