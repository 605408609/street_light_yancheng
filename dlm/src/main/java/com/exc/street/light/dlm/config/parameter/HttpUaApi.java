package com.exc.street.light.dlm.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "http.ua")
public class HttpUaApi {

    private String url;

    private String modifyByArea;

    private String user;
}