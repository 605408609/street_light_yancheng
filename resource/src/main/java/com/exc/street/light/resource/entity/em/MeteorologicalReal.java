/**
 * @filename:MeteorologicalReal 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.resource.entity.em;
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
 * @author: LeiJing
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MeteorologicalReal extends Model<MeteorologicalReal> {

	private static final long serialVersionUID = 1584772749293L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "实时数据表id，自增")
	private Integer id;
    
	@ApiModelProperty(name = "temperature" , value = "温度")
	private Float temperature;
    
	@ApiModelProperty(name = "humidity" , value = "湿度")
	private Float humidity;
    
	@ApiModelProperty(name = "airPressure" , value = "气压")
	private Float airPressure;
    
	@ApiModelProperty(name = "pm25" , value = "PM2.5")
	private Float pm25;
    
	@ApiModelProperty(name = "pm10" , value = "PM10")
	private Float pm10;
    
	@ApiModelProperty(name = "noiseMax" , value = "噪音最大值")
	private Float noiseMax;
    
	@ApiModelProperty(name = "noiseAverage" , value = "噪音平均值")
	private Float noiseAverage;
    
	@ApiModelProperty(name = "noiseMin" , value = "噪音最小值")
	private Float noiseMin;
    
	@ApiModelProperty(name = "windSpeedMax" , value = "风速最大值")
	private Float windSpeedMax;
    
	@ApiModelProperty(name = "windSpeedAverage" , value = "风速平均值")
	private Float windSpeedAverage;
    
	@ApiModelProperty(name = "windSpeedMin" , value = "风速最小值")
	private Float windSpeedMin;
    
	@ApiModelProperty(name = "windDirectionMax" , value = "风向最大值")
	private Integer windDirectionMax;
    
	@ApiModelProperty(name = "windDirectionAverage" , value = "风向平均值")
	private Integer windDirectionAverage;
    
	@ApiModelProperty(name = "windDirectionMin" , value = "风向最小值")
	private Integer windDirectionMin;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@ApiModelProperty(name = "deviceId" , value = "设备id")
	private Integer deviceId;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
