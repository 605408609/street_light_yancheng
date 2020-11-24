/**
 * @filename:EdManholeCoverDevice 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.ed;
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
import java.math.BigDecimal;
import java.util.Date;

/**   
 * @Description:TODO(井盖设备表实体类)
 * 
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EdManholeCoverDevice extends Model<EdManholeCoverDevice> {

	private static final long serialVersionUID = 1601280752121L;
	
	@TableId(value = "id", type = IdType.AUTO)
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
    
	@ApiModelProperty(name = "limitUpper" , value = "开盖报警倾角阈值（单位：°）")
	private Double limitUpper;
    
	@ApiModelProperty(name = "realData" , value = "实时倾角（单位：°）")
	private Double realData;
    
	@ApiModelProperty(name = "locationSiteId" , value = "站点ID")
	private Integer locationSiteId;
    
	@ApiModelProperty(name = "status" , value = "井盖状态 0-关 1-开")
	private Integer status;
    
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
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
