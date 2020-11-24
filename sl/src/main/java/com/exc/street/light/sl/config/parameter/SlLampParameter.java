package com.exc.street.light.sl.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sl.lamp")
public class SlLampParameter {

    private String address;

    private String username;

    private String password;

    private String loginUrl;

    private String controlUrl;

    private String dataUrl;

}