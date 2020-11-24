package com.exc.street.light.dlm.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Spring MVC 配置
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Value("${path.file}")
    private String uploadPath;

    /**
     * 使用阿里 FastJson 作为JSON MessageConverter
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        //FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();
        FastJsonConfig config = new FastJsonConfig();
        //保留空的字段
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,
                //SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
                //SerializerFeature.WriteNullNumberAsZero,
                //禁止循环引用
                SerializerFeature.DisableCircularReferenceDetect);
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(converter);
    }


    /**
     * 解决跨域问题
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("PUT", "POST", "GET", "DELETE", "OPTIONS").allowCredentials(true)
                .allowedHeaders("Origin", "X-Requested-With", "Content-Type",
                        "Accept", "client_id", "uuid", "Authorization", "token", "Cookie").allowedOrigins("*");
    }

    /**
     * 静态资源配置
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("file:" + uploadPath);
        // 开放swagger静态资源访问
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

//
//    //静态资源配置
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        URL resource = this.getClass().getResource("/");
//        //registry.addResourceHandler("/**").addResourceLocations("file:" + "C:/Users/Gigabyte/Desktop/Intelligent street lamp/ExcelTemplate/");
//        registry.addResourceHandler("/**").addResourceLocations(resource+"static/");
//        super.addResourceHandlers(registry);
//    }

}
