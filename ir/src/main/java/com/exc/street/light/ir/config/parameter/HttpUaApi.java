package com.exc.street.light.ir.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "http.ua")
public class HttpUaApi {

    private String url;

    private String selectName;

}