package com.exc.street.light.sl.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * ctwing平台mqtt类型配置
 * @Author: Xiaok
 * @Date: 2020/9/15 10:36
 */
@Data
@Component
@ConfigurationProperties(prefix = "ctwing.mqtt-type")
public class CtWingMqttApi {
    /**
     * App Key    开发者中心->应用管理->应用开发信息App Key
     */
    private String application;
    /**
     * App Secret 开发者中心->应用管理->应用开发信息App Secret
     */
    private String secret;
    /**
     * 产品ID      开发者中心->产品中心->mqtt产品概况->产品ID
     */
    private String productId;
    /**
     * Master-APIkey      开发者中心->产品中心->mqtt产品概况->Master-APIkey
     */
    private String masterKey;
    /**
     * 创建设备map
     */
    private HashMap<String,String> createDevice;
    /**
     * 下发指令map
     */
    private HashMap<String,String> sendMessage;
    /**
     * 删除设备map
     */
    private HashMap<String,String> deleteDevice;
    /**
     * 获取时间map
     */
    private HashMap<String,String> getTime;
    /**
     * 指令超时时间
     */
    private String outTime;
}
