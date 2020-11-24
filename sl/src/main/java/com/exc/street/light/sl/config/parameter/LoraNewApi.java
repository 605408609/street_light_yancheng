package com.exc.street.light.sl.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "loranew")
public class LoraNewApi {

    private String sendMessage;
    private String createNode;
    private String deleteNode;
    private String appsKey;
    private String nwksKey;
    private String projectId;
    private String username;
    private String password;
    private String login;
    private String sendSignToken;
    private String sendSecretKey;
    private String pushSignToken;
    private String pushSecretKey;

}