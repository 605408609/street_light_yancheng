/**
 * @filename:ScreenPlay 2020-04-26
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
public class ScreenPlay extends Model<ScreenPlay> {

	private static final long serialVersionUID = 1587890241135L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "显示屏播放表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "playStatus" , value = "播放状态（0：待播放，1：正在播放，2：结束播放）")
	private Integer playStatus;
    
	@ApiModelProperty(name = "programId" , value = "节目id")
	private Integer programId;
    
	@ApiModelProperty(name = "deviceSubordinate" , value = "设备隶属于（1：区域，2：分组）")
	private Integer deviceSubordinate;
    
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
