/**
 * @filename:EdAshcan 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.vo.resp;

import com.exc.street.light.resource.qo.QueryObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**   
 * @Description:TODO(垃圾桶信息表实体类)
 * 
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Data
public class EdRespAshcanVO{


	@ApiModelProperty(name = "id" , value = "唯一标识")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "设备名称")
	private String name;
    
	@ApiModelProperty(name = "num" , value = "设备编号")
	private String num;
    
	@ApiModelProperty(name = "longitude" , value = "经度")
	private String longitude;
    
	@ApiModelProperty(name = "latitude" , value = "纬度")
	private String latitude;
    
	@ApiModelProperty(name = "limitUpper" , value = "报警深度阈值(cm)")
	private Double limitUpper;
    
	@ApiModelProperty(name = "realData" , value = "实时深度(cm)")
	private Double realData;
    
	@ApiModelProperty(name = "armedStatus" , value = "是否布防 0撤防 1布防")
	private Integer armedStatus;

	@ApiModelProperty(name = "locationSiteId" , value = "站点ID")
	private Integer locationSiteId;

	@ApiModelProperty(name = "locationSiteName" , value = "站点名称")
	private String locationSiteName;

	@ApiModelProperty(name = "locationStreetId" , value = "街道ID")
	private Integer locationStreetId;

	@ApiModelProperty(name = "locationStreetName" , value = "街道名称")
	private String locationStreetName;

	@ApiModelProperty(name = "locationAreaId" , value = "区域ID")
	private Integer locationAreaId;

	@ApiModelProperty(name = "locationAreaName" , value = "区域名称")
	private String locationAreaName;
    
	@ApiModelProperty(name = "deviceVersion" , value = "版本号")
	private String deviceVersion;
    
	@ApiModelProperty(name = "uploadCycle" , value = "上传周期(s)")
	private Integer uploadCycle;
    
	@ApiModelProperty(name = "creator" , value = "创建人")
	private Integer creator;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@ApiModelProperty(name = "status" , value = "垃圾桶在线状态 0-离线 1-在线")
	private Integer status;

}
