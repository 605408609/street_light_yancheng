package com.exc.street.light.co.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author tanhonghang
 * @create 2020/10/14 21:35
 */
@Configuration
public class Constants {

    /**
     * 设备参数类型
     */
    public static String DEVICE_PARAM_TYPE = "1";

    /**
     * 告警类型
     */
    public static String DEVICE_ALARM_TYPE = "3";

    public static String APIKEY;

    @Value("${mqtt.apikey}")
    public void setAPIKEY(String apikey ){
        Constants.APIKEY = apikey;
    }
}
