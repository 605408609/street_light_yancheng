/**
 * @filename:ScreenPlayStrategy 2020-04-26
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
public class ScreenPlayStrategy extends Model<ScreenPlayStrategy> {

	private static final long serialVersionUID = 1587889987954L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "显示屏下发策略表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "executionMode" , value = "执行模式(1:每天执行,2:每周执行)")
	private Integer executionMode;
    
	@ApiModelProperty(name = "executionEndTime" , value = "执行结束时间（时分秒）")
	private String executionEndTime;
    
	@ApiModelProperty(name = "executionStartTime" , value = "执行开始时间（时分秒）")
	private String executionStartTime;
    
	@ApiModelProperty(name = "startDate" , value = "开始时间（年月日）")
	private String startDate;
    
	@ApiModelProperty(name = "endDate" , value = "结束时间（年月日）")
	private String endDate;
    
	@ApiModelProperty(name = "weekValue" , value = "周控制值1-7:周日到周六循环执行")
	private Integer weekValue;
    
	@ApiModelProperty(name = "screenPlayId" , value = "播放id")
	private Integer screenPlayId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "修改时间")
	private Date updateTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
