package com.exc.street.light.em.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "http")
public class HttpApi {

    private Map<String,String> sl;

    private Map<String,String> wifi;

    private Map<String,String> pd;

    private Map<String,String> ss;

    private Map<String,String> ir;

    private Map<String,String> occ;

    private Map<String,String> em;

    private Map<String,String> dlm;
}