package com.exc.street.light.ir.config.parameter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "path")
public class PathApi {

    private String upload;

    private String file;

}
