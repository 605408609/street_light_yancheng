package com.exc.street.light.ss.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * HikArtemisConfig
 *
 * @author liufei
 * @date 2020/06/04
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix="hikvision-artemis-config")
public class HikArtemisConfig {

    /**
     * api网关的的http协议
     */
    private String httpType;

    /**
     * 代理API网关nginx服务器ip端口
     */
    private String host;

    /**
     * 秘钥appkey
     */
    private String appKey;

    /**
     * 秘钥appSecret
     */
    private String appSecret;
    
}
