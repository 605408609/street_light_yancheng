package com.exc.street.light.wifi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "http.dlm")
public class HttpApi {

    /**
     * 设备位置管理模块
     */
    private String url;

    private String areaById;

    private String lampPostById;

    private String getStreetByAreaIdList;

    private String getSiteByStreetIdList;

}