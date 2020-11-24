/**
 * @filename:LampEnergy 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.sl;
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
public class LampEnergy extends Model<LampEnergy> {

	private static final long serialVersionUID = 1584699575481L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "灯具能耗表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "energy" , value = "能耗数据")
	private Float energy;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;

	@ApiModelProperty(name = "cumulativeEnergy" , value = "累计能耗数据")
	private Double cumulativeEnergy;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;


	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
