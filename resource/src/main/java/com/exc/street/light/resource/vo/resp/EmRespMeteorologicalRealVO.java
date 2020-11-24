package com.exc.street.light.resource.vo.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:气象设备实时数据返回对象
 *
 * @author LeiJing
 * @Date 2020/03/28
 */
@Getter
@Setter
@ToString
public class EmRespMeteorologicalRealVO {
    /**
     * 实时数据表id，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id" , value = "实时数据表id，自增")
    private Integer id;

    /**
     * 温度
     */
    @ApiModelProperty(name = "temperature" , value = "温度")
    private Float temperature;

    /**
     * 湿度
     */
    @ApiModelProperty(name = "humidity" , value = "湿度")
    private Float humidity;

    /**
     * 气压
     */
    @ApiModelProperty(name = "airPressure" , value = "气压")
    private Float airPressure;

    /**
     * PM2.5
     */
    @ApiModelProperty(name = "pm25" , value = "PM2.5")
    private Float pm25;

    /**
     * PM10
     */
    @ApiModelProperty(name = "pm10" , value = "PM10")
    private Float pm10;

    /**
     * 噪音最大值
     */
    @ApiModelProperty(name = "noiseMax" , value = "噪音最大值")
    private Float noiseMax;

    /**
     * 噪音平均值
     */
    @ApiModelProperty(name = "noiseAverage" , value = "噪音平均值")
    private Float noiseAverage;

    /**
     * 噪音最小值
     */
    @ApiModelProperty(name = "noiseMin" , value = "噪音最小值")
    private Float noiseMin;

    /**
     * 风速最大值
     */
    @ApiModelProperty(name = "windSpeedMax" , value = "风速最大值")
    private Float windSpeedMax;

    /**
     * 风速平均值
     */
    @ApiModelProperty(name = "windSpeedAverage" , value = "风速平均值")
    private Float windSpeedAverage;

    /**
     * 风速最小值
     */
    @ApiModelProperty(name = "windSpeedMin" , value = "风速最小值")
    private Float windSpeedMin;

    /**
     * 风向最大值
     */
    @ApiModelProperty(name = "windDirectionMax" , value = "风向最大值")
    private Integer windDirectionMax;

    /**
     * 风向平均值
     */
    @ApiModelProperty(name = "windDirectionAverage" , value = "风向平均值")
    private Integer windDirectionAverage;

    /**
     * 风向最小值
     */
    @ApiModelProperty(name = "windDirectionMin" , value = "风向最小值")
    private Integer windDirectionMin;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    /**
     * 设备id
     */
    @ApiModelProperty(name = "deviceId" , value = "设备id")
    private Integer deviceId;

    /**
     * 风向最大值中文
     */
    @ApiModelProperty(name = "windDirectionMaxString" , value = "风向最大值中文")
    private String windDirectionMaxString;

    /**
     * 风向平均值中文
     */
    @ApiModelProperty(name = "windDirectionAverageString" , value = "风向平均值中文")
    private String windDirectionAverageString;

    /**
     * 风向最小值中文
     */
    @ApiModelProperty(name = "windDirectionMinString" , value = "风向最小值中文")
    private String windDirectionMinString;

    /**
     * 空气质量
     */
    @ApiModelProperty(name = "airQuality" , value = "空气质量")
    private String airQuality;
}
