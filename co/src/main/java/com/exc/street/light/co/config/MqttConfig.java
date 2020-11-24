package com.exc.street.light.co.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author tanhonghang
 * @create 2020/10/10 18:29
 */
@Data
public class MqttConfig {
    private String host;
    private String clientId;
    private String username;
    private String password;
    private int timeout;
    private int keepAlive;
    private String[] topic;
    private int[] qos;
    private String apikey;


}
