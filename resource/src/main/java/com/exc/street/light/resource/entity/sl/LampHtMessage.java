/**
 * @filename:LampHtMessage 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd. 
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
import java.util.Date;

/**   
 * @Description:TODO(华体集中控制器消息记录实体类)
 * 
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LampHtMessage extends Model<LampHtMessage> {

	private static final long serialVersionUID = 1599013690228L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键ID")
	private Integer id;
    
	@ApiModelProperty(name = "locationControlId" , value = "集中控制器ID")
	private Integer locationControlId;
    
	@ApiModelProperty(name = "orderCmdType" , value = "下发命令类型")
	private Integer orderCmdType;
    
	@ApiModelProperty(name = "orderCmd" , value = "下发命令json")
	private String orderCmd;
    
	@ApiModelProperty(name = "orderResult" , value = "下发结果")
	private Integer orderResult;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "orderTime" , value = "下发时间")
	private Date orderTime;
    
	@ApiModelProperty(name = "returnCmdType" , value = "回送命令类型")
	private Integer returnCmdType;
    
	@ApiModelProperty(name = "returnCmd" , value = "回送命令json")
	private String returnCmd;
    
	@ApiModelProperty(name = "returnResult" , value = "回送结果")
	private Integer returnResult;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "returnTime" , value = "回送消息时间")
	private Date returnTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
