package com.exc.street.light.dlm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(value = {"com.exc.street.light.*.mapper"})
//@ComponentScan(basePackages = {"com.exc.street.light.dlm", "com.exc.street.light.security.base"})
@ComponentScan(basePackages = {"com.exc.street.light.dlm","com.exc.street.light.log_api", "com.exc.street.light.security.base"})
public class DlmApplication {

    public static void main(String[] args) {
        SpringApplication.run(DlmApplication.class, args);
    }

}
