package com.exc.street.light.log_api;

import com.exc.street.light.log_api.service.LogExceptionService;
import com.exc.street.light.log_api.service.LogNormalService;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.log_api.aspect.SystemLogAspect;
import com.exc.street.light.log_api.event.SystemLogListener;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: XuJiaHao
 * @Description: 自动配置aop
 * @Date: Created in 23:28 2020/5/7
 * @Modified:
 */
@EnableAsync
@AllArgsConstructor
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
public class SystemLogConfiguration {
    @Autowired
    LogNormalService logNormalService;
    @Autowired
    LogExceptionService logExceptionService;
    @Autowired
    LogUserService logUserService;

    @Bean
    public SystemLogListener sysLogListener() {
        return new SystemLogListener(logNormalService,logExceptionService, logUserService);
    }

    @Bean
    public SystemLogAspect sysLogAspect() {
        return new SystemLogAspect();
    }

}
