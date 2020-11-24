/**
 * @filename:SystemDeviceThreshold 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.sl;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description:TODO(设备阈值表实体类)
 * 
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SystemDeviceThreshold extends Model<SystemDeviceThreshold> {

	private static final long serialVersionUID = 1598087427085L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "设备阈值表")
	private Integer id;
    
	@ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
	private Integer deviceTypeId;
    
	@ApiModelProperty(name = "filed" , value = "阈值字段")
	private String filed;
    
	@ApiModelProperty(name = "unit" , value = "阈值单位")
	private String unit;
    
	@ApiModelProperty(name = "name" , value = "阈值含义")
	private String name;
    
	@ApiModelProperty(name = "isMust" , value = "是否必填（0：否，1：是）")
	private Integer isMust;
    
	@ApiModelProperty(name = "regexp" , value = "校验正则表达式")
	@TableField("`regexp`")
	private String regexp;
    
	@ApiModelProperty(name = "icon" , value = "小图标文件名")
	private String icon;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
