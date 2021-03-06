package com.exc.street.light.dlm.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "http.occ")
public class HttpOccApi {

    private String url;

    private String deviceAll;

    private String devicePage;

    private String deviceById;

    private String pulldownByLampPostIdList;

}