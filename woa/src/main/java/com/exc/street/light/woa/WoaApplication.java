package com.exc.street.light.woa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan(value = {"com.exc.street.light.*.mapper"})
@ComponentScan(basePackages = {"com.exc.street.light.woa", "com.exc.street.light.security.base", "com.exc.street.light.log_api"})
public class WoaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WoaApplication.class, args);
    }

}
