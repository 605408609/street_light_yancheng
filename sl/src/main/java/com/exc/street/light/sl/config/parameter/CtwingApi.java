package com.exc.street.light.sl.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ctwing")
public class CtwingApi {

    private String secret;
    private String application;
    private String masterKey;
    private String productId;
    private String sendMessage;
    private String createNode;
    private String deleteNode;
    private String getTime;
    private String outTime;
    private String intervalTime;
    private String retransmissionTime;
    private String batchCreateDevice;

}