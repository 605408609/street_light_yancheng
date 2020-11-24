package com.exc.street.light.dlm.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "lora")
public class LoraApi {

    private String token;
    private String createNode;
    private String loraMcId;
    private String appId;
    private String appSKey;
    private String nwkSKey;
    private String createNodeMc;

}