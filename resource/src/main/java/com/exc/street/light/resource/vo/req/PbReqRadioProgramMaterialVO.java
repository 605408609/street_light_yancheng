/**
 * @filename:RadioProgramMaterial 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.exc.street.light.resource.entity.pb.RadioMaterial;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**   
 * @Description:TODO(实体类)
 * 
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Getter
@Setter
@ToString
public class PbReqRadioProgramMaterialVO {
	@ApiModelProperty(name = "id" , value = "节目素材中间表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "programId" , value = "节目id")
	private Integer programId;
    
	@ApiModelProperty(name = "materialId" , value = "素材id")
	private Integer materialId;
    
	@ApiModelProperty(name = "materialOrder" , value = "播放序号")
	private Integer materialOrder;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	/**
	 * 公共广播节目素材对象
	 */
	@ApiModelProperty(name = "radioMaterialList" , value = "公共广播节目素材对象")
	private RadioMaterial radioMaterial;
}
