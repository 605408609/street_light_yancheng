/**
 * @filename:LampDeviceThreghold 2020-08-22
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
 * @Description:TODO(设备阈值数据表实体类)
 * 
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LampDeviceThreghold extends Model<LampDeviceThreghold> {

	private static final long serialVersionUID = 1598087484818L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "设备阈值数据表")
	private Integer id;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;
    
	@ApiModelProperty(name = "thresholdValue" , value = "阈值")
	private String thresholdValue;
    
	@ApiModelProperty(name = "thresholdId" , value = "阈值参数id")
	private Integer thresholdId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
