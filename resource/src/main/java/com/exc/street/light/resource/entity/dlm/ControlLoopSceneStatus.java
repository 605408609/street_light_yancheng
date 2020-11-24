/**
 * @filename:ControlLoopSceneStatus 2020-11-05
 * @project dlm  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd. 
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

/**   
 * @Description:TODO(集控回路场景状态表实体类)
 * 
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ControlLoopSceneStatus extends Model<ControlLoopSceneStatus> {

	private static final long serialVersionUID = 1604545876091L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "主键")
	private Integer id;
    
	@ApiModelProperty(name = "locationControlId" , value = "集控器ID")
	private Integer locationControlId;
    
	@ApiModelProperty(name = "loopId" , value = "回路ID")
	private Integer loopId;
    
	@ApiModelProperty(name = "addOpenStatus" , value = "是否添加 开 场景,0-否 1-是")
	private Integer addOpenStatus;
    
	@ApiModelProperty(name = "addCloseStatus" , value = "是否添加 关 场景,0-否 1-是")
	private Integer addCloseStatus;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
