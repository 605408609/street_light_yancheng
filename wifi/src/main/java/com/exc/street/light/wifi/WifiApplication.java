package com.exc.street.light.wifi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@MapperScan(value = {"com.exc.street.light.*.mapper"})
@ComponentScan(basePackages = {"com.exc.street.light.wifi", "com.exc.street.light.security.base", "com.exc.street.light.log_api"})
public class WifiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WifiApplication.class, args);
    }

}
