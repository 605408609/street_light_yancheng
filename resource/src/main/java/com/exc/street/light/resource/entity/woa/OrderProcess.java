/**
 * @filename:OrderProcess 2020-03-28
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
public class OrderProcess extends Model<OrderProcess> {

	private static final long serialVersionUID = 1585378245009L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "工单进程表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "operator" , value = "操作人")
	private Integer operator;
    
	@ApiModelProperty(name = "description" , value = "工单进程描述")
	private String description;
    
	@ApiModelProperty(name = "statusId" , value = "工单状态id")
	private Integer statusId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@ApiModelProperty(name = "orderId" , value = "工单id")
	private Integer orderId;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
