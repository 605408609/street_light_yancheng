package com.exc.street.light.em.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alarm")
public class AlarmConfig {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 温度告警值
     */
    private Float temperature;

    /**
     * pm2.5告警值
     */
    private Float pm25;

    /**
     * 噪声告警值
     */
    private Float noise;

    /**
     * 风速告警值
     */
    private Float windSpeed;
}
