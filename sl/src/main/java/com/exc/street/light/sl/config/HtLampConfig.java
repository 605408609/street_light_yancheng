package com.exc.street.light.sl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 华体灯控配置
 *
 * @Author: Xiaok
 * @Date: 2020/8/13 11:26
 */

@Getter
@Setter
@Component
@Configuration
public class HtLampConfig {

    @Value("${htLamp.loginUrl}")
    private String loginUrl;

    @Value("${htLamp.username}")
    private String username;

    @Value("${htLamp.password}")
    private String password;

    @Value("${htLamp.sendUrl}")
    private String sendUrl;
}
