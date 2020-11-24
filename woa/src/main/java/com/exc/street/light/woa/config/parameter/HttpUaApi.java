package com.exc.street.light.woa.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "http.ua")
public class HttpUaApi {

    private String url;
    private String selectName;
    private String selectById;
    private String getApproval;

}