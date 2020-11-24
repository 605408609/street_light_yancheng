package com.exc.street.light.woa.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "http.dlm")
public class HttpDlmApi {

    private String url;
    private String getLampPost;
    private String getStreetByAreaIdList;
    private String getSiteByStreetIdList;
    private String getLampPostBySiteIdList;
    private String getLampPostByGroupIdList;
    private String getLampPostByIdList;

}