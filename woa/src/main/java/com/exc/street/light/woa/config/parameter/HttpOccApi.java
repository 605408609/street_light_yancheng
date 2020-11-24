package com.exc.street.light.woa.config.parameter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "http.occ")
public class HttpOccApi {
    private String url;
    private String newsStatus;
    private String newsAll;
}