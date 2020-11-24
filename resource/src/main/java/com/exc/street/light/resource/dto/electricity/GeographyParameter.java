package com.exc.street.light.resource.dto.electricity;

import lombok.Data;

/**
 * @Author: XuJiaHao
 * @Description: 地理参数
 * @Date: Created in 11:27 2020/9/8
 * @Modified:
 */
@Data
public class GeographyParameter {
    /**
     * 经度  东经为正，西经为负
     */
    private String longitude;
    /**
     * 纬度   南纬为负， 北纬为正
     */
    private String latitude;
    /**
     * 日出时间
     */
    private String sunriseTime;
    /**
     * 日落时间
     */
    private String sunsetTime;
}
