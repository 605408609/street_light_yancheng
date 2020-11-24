/**
 * @filename:LocationControlType 2020-08-22
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
public class LocationControlType extends Model<LocationControlType> {

	private static final long serialVersionUID = 1598081425396L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "集中控制器类型表")
	private Integer id;
    
	@ApiModelProperty(name = "type" , value = "类型")
	private String type;
    
	@ApiModelProperty(name = "factoryId" , value = "厂家id")
	private Integer factoryId;
    
	@ApiModelProperty(name = "edition" , value = "版本号")
	private String edition;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
