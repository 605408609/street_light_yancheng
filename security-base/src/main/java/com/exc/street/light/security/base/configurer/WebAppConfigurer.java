package com.exc.street.light.security.base.configurer;

import com.exc.street.light.security.base.interceptor.AuthcInterceptor;
import com.exc.street.light.security.base.interceptor.AutheInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 注册拦截器
 *
 * @author Linshiwen
 * @date 2018/12/11
 */
@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    /**
     * 不需要鉴权的url
     */
    @Value("${exclude.authc.url}")
    private String excludeAuthcUrl;

    /**
     * 不需要认证的url
     */
    @Value("${exclude.authe.url}")
    private String excludeAutheUrl;

    @Bean
    public AutheInterceptor autheInterceptor() {
        return new AutheInterceptor();
    }

    @Bean
    public AuthcInterceptor authcInterceptor() {
        return new AuthcInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autheInterceptor()).excludePathPatterns(excludeAutheUrl.split(","));
        registry.addInterceptor(authcInterceptor()).excludePathPatterns(excludeAuthcUrl.split(","));
    }
}
