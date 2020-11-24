/**
 * @filename:AlarmOrderSetLampPost 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.woa;
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
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AlarmOrderSetLampPost extends Model<AlarmOrderSetLampPost> {

	private static final long serialVersionUID = 1585363939673L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "告警设置灯杆中间表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "setId" , value = "告警生成工单设置id")
	private Integer setId;
    
	@ApiModelProperty(name = "lampPostId" , value = "灯杆id")
	private Integer lampPostId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
