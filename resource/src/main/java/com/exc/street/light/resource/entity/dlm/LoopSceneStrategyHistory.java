/**
 * @filename:LoopSceneStrategyHistory 2020-11-07
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.dlm;
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
 * @author: xiezhipeng
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoopSceneStrategyHistory extends Model<LoopSceneStrategyHistory> {

	private static final long serialVersionUID = 1604730225126L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "回路场景策略历史中间表，主键id自增")
	private Integer id;
    
	@ApiModelProperty(name = "loopId" , value = "回路id")
	private Integer loopId;
    
	@ApiModelProperty(name = "strategyId" , value = "场景策略id")
	private Integer strategyId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@ApiModelProperty(name = "isSuccess" , value = "预留字段")
	private Integer isSuccess;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
