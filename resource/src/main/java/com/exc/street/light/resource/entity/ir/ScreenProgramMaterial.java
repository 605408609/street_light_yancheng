/**
 * @filename:ScreenProgramMaterial 2020-04-02
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
public class ScreenProgramMaterial extends Model<ScreenProgramMaterial> {

	private static final long serialVersionUID = 1585821576248L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "节目素材中间表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "programId" , value = "节目id")
	private Integer programId;
    
	@ApiModelProperty(name = "materialId" , value = "素材id")
	private Integer materialId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@ApiModelProperty(name = "playTime" , value = "在时间轴上的相对起始播放时间 单位秒")
	private Integer playTime;
    
	@ApiModelProperty(name = "timeSpan" , value = "持续时长 单位秒")
	private Integer timeSpan;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
