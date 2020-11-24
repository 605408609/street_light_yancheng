/**
 * @filename:CanStrategyAction 2020-11-18
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
 * @Description:TODO(路灯网关策略对应动作表实体类)
 * 
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CanStrategyAction extends Model<CanStrategyAction> {

	private static final long serialVersionUID = 1605662038265L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "")
	private Integer id;
    
	@ApiModelProperty(name = "strategyId" , value = "路灯网关策略表ID")
	private Integer strategyId;
    
	@ApiModelProperty(name = "isOpen" , value = "回路动作(0-关 1-开)")
	private Integer isOpen;

	@ApiModelProperty(name = "weekValue" , value = "周控制值1-7（周一到周日循环执行）")
	private Integer weekValue;
    
	@ApiModelProperty(name = "startDate" , value = "开始日期（年月日）")
	private String startDate;
    
	@ApiModelProperty(name = "endDate" , value = "结束日期（年月日）")
	private String endDate;
    
	@ApiModelProperty(name = "executionTime" , value = "动作执行时间（时分秒）")
	private String executionTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
