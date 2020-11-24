/**
 * @filename:RadioProgram 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.exc.street.light.resource.entity.pb.RadioProgramMaterial;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**   
 * 公共广播节目接收参数
 * 
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Getter
@Setter
@ToString
public class PbReqRadioProgramVO {

	@ApiModelProperty(name = "id" , value = "节目表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "节目名称")
	private String name;
    
	@ApiModelProperty(name = "description" , value = "节目描述")
	private String description;
    
	@ApiModelProperty(name = "duration" , value = "节目时长")
	private Integer duration;

	@ApiModelProperty(name = "durationStr" , value = "节目时长显示(格式：x:x:x)")
	private transient String durationStr;
    
	@ApiModelProperty(name = "capacity" , value = "节目容量")
	private Float capacity;
    
	@ApiModelProperty(name = "creator" , value = "创建人（运营原型出来后为不可空，关联用户）")
	private Integer creator;

	@ApiModelProperty(name = "creatorName" , value = "创建人名称")
	private String creatorName;

    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "updateTime" , value = "修改时间")
	private Date updateTime;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	/**
	 * 节目素材对应关系集合
	 */
	@ApiModelProperty(name = "radioProgramMaterialList" , value = "节目素材对应关系集合")
	private List<PbReqRadioProgramMaterialVO> radioProgramMaterialList;

	@ApiModelProperty(name = "radioMaterialIdList" , value = "节目素材id集合")
	private List<Integer> radioMaterialIdList;
}
