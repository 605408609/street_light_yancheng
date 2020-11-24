package com.exc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan(value = {"com.exc.street.light.*.mapper"})
@ServletComponentScan(basePackages = "com.exc.license")
public class UaApplication {

    public static void main(String[] args) {
        SpringApplication.run(UaApplication.class, args);
    }

}
