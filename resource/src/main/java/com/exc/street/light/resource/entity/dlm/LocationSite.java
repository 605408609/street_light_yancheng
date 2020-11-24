/**
 * @filename:LocationSite 2020-03-16
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
public class LocationSite extends Model<LocationSite> {

	private static final long serialVersionUID = 1584361326978L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "站点表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "name" , value = "站点名称（不可空）")
	private String name;
    
	@ApiModelProperty(name = "description" , value = "站点描述")
	private String description;
    
	@ApiModelProperty(name = "streetId" , value = "街道id（不可空）")
	private Integer streetId;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;

	@ApiModelProperty(name = "lampState" , value = "站点灯具状态")
	private Integer lampState;

	@ApiModelProperty(name = "lampBrightness" , value = "站点灯具亮度")
	private Integer lampBrightness;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
