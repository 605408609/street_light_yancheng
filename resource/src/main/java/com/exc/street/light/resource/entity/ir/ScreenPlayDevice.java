/**
 * @filename:ScreenPlayDevice 2020-04-26
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.ir;
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
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ScreenPlayDevice extends Model<ScreenPlayDevice> {

	private static final long serialVersionUID = 1587889987954L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "显示屏和播放中间表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;
    
	@ApiModelProperty(name = "screenPlayId" , value = "播放id")
	private Integer screenPlayId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
