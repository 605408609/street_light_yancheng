package com.exc.street.light.sl.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "lora")
public class LoraApi {

    private String token;
    private String sendUrl;
    private String sendMcUrl;
    private String createNode;
    private String deleteNode;
    private String loraMcId;
    private String appId;
    private String appSKey;
    private String nwkSKey;
    private String createNodeMc;
    private String deleteNodeMc;

    private String num;
    private String sendId;


}