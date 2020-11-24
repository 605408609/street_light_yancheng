/**
 * @filename:ScreenSubtitleEmScreen 2020-11-16
 * @project em  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.em;
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
 * @Description:TODO(传感器关联显示屏中间表实体类)
 * 
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ScreenSubtitleEmScreen extends Model<ScreenSubtitleEmScreen> {

	private static final long serialVersionUID = 1605511952388L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = " 传感器关联显示屏中间表")
	private Integer id;
    
	@ApiModelProperty(name = "screenSubtitleEmId" , value = " 传感器关联显示屏显示数据设置id")
	private Integer screenSubtitleEmId;
    
	@ApiModelProperty(name = "screenDeviceId" , value = "显示屏id")
	private Integer screenDeviceId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
