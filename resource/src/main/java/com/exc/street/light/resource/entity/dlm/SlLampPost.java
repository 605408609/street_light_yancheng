/**
 * @filename:SlLampPost 2020-03-17
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
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
 * @author: Longshuangyang
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SlLampPost extends Model<SlLampPost> {

	private static final long serialVersionUID = 1584409549109L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "灯杆表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "灯杆名称（不可空）")
	private String name;
    
	@ApiModelProperty(name = "num" , value = "灯杆编号（不可空）")
	private String num;
    
	@ApiModelProperty(name = "model" , value = "灯杆型号")
	private String model;
    
	@ApiModelProperty(name = "longitude" , value = "经度")
	private Double longitude;
    
	@ApiModelProperty(name = "latitude" , value = "纬度")
	private Double latitude;
    
	@ApiModelProperty(name = "manufacturer" , value = "厂家")
	private String manufacturer;
    
	@ApiModelProperty(name = "location" , value = "安装位置")
	private String location;
    
	@ApiModelProperty(name = "groupId" , value = "分组id")
	private Integer groupId;
    
	@ApiModelProperty(name = "siteId" , value = "站点id（不可空）")
	private Integer siteId;
	
	@ApiModelProperty(name = "default_camera_id" , value = "默认的弹窗显示摄像头")
	private Integer defaultCameraId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
