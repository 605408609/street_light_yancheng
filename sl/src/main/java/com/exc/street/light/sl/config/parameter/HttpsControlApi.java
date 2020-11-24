package com.exc.street.light.sl.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "https.control")
public class HttpsControlApi {

    private String selfcertpwd;
    private String trustcapwd;
    private String testPath;
    private String selfcertpath;
    private String trustcapath;
    private String url;
    private String token;
    private String select;
    private String issue;
    private String batchIssue;
    private String appKey;
    private String secret;
    private String callbackUrl;

}