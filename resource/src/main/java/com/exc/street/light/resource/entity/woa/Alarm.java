/**
 * @filename:Alarm 2020-03-28
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
public class Alarm extends Model<Alarm> {

	private static final long serialVersionUID = 1585363939673L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "告警表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "content" , value = "告警内容")
	private String content;

	@ApiModelProperty(name = "addr" , value = "告警发生地址")
	private String addr;
    
	@ApiModelProperty(name = "status" , value = "告警状态（1：未处理，2：处理中，3：已处理）默认1")
	private Integer status;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "disposeTime" , value = "开始处理时间")
	private Date disposeTime;
    
	@ApiModelProperty(name = "typeId" , value = "告警类型id")
	private Integer typeId;

	@ApiModelProperty(name = "lampPostId" , value = "灯杆id")
	private Integer lampPostId;

	@ApiModelProperty(name = "orderId" , value = "工单id")
	private Integer orderId;
	
	@ApiModelProperty(name = "haveRead" , value = "是否已读（0：未读，1：已读）")
	private Integer haveRead;

	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;

	@ApiModelProperty(name = "deviceName" , value = "设备名称")
	private String deviceName;

	@ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
	private Integer deviceTypeId;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
