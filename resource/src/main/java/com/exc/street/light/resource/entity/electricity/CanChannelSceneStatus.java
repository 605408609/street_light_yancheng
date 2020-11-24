/**
 * @filename:CanChannelSceneStatus 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.electricity;
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
 * @Description:TODO(路灯网关回路场景添加状态表实体类)
 * 
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CanChannelSceneStatus extends Model<CanChannelSceneStatus> {

	private static final long serialVersionUID = 1605661932216L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "")
	private Integer id;
    
	@ApiModelProperty(name = "nid" , value = "路灯网关ID")
	private Integer nid;
    
	@ApiModelProperty(name = "channelId" , value = "路灯网关回路ID")
	private Integer channelId;
    
	@ApiModelProperty(name = "addOpenStatus" , value = "是否添加开场景 0-否 1-是")
	private Integer addOpenStatus;
    
	@ApiModelProperty(name = "addCloseStatus" , value = "是否添加关场景 0-否 1-是")
	private Integer addCloseStatus;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
