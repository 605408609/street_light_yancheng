package com.exc.street.light.dlm.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "http.sl")
public class HttpSlApi {

    private String url;

    private String deviceAll;

    private String devicePage;

    private String deviceById;

    private String pulldownByLampPostIdList;

    private String control;

    private String loopControl;

    private String loopScene;

    private String getDeviceListByIdList;

    private String register;

    private String relieveRegister;

}