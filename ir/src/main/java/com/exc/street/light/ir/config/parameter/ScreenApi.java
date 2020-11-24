package com.exc.street.light.ir.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "screen")
public class ScreenApi {

    private String ip;

    private Integer port;

    /**
     * 请求发送超时时间
     */
    private Integer sendTime;

    /**
     * 多次字幕间的间隔
     */
    private Integer subtitleSpacing;

}