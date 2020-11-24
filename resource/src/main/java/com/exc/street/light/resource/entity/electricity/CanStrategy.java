/**
 * @filename:CanStrategy 2020-11-18
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
 * @Description:TODO(路灯网关策略表实体类)
 * 
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CanStrategy extends Model<CanStrategy> {

	private static final long serialVersionUID = 1605662015580L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "策略名称")
	private String name;
    
	@ApiModelProperty(name = "description" , value = "说明")
	private String description;
    
	@ApiModelProperty(name = "creator" , value = "创建人")
	private Integer creator;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@ApiModelProperty(name = "delFlag" , value = "删除标记 0-未删除 1-已删除")
	private Integer delFlag;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
