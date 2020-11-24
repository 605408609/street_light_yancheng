/**
 * @filename:CanChannelStrategyHistory 2020-11-18
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
import java.util.Date;

/**   
 * @Description:TODO(路灯网关回路下发策略历史表实体类)
 * 
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CanChannelStrategyHistory extends Model<CanChannelStrategyHistory> {

	private static final long serialVersionUID = 1605661986414L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "")
	private Integer id;
    
	@ApiModelProperty(name = "channelId" , value = "路灯网关_回路ID")
	private Integer channelId;
    
	@ApiModelProperty(name = "strategyId" , value = "路灯网关_策略ID")
	private Integer strategyId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@ApiModelProperty(name = "isSuccess" , value = "下发是否成功 0-失败 1-成功")
	private Integer isSuccess;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
