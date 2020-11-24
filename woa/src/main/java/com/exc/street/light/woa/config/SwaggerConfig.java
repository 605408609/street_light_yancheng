/**
 * @filename:AlarmController 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * Swagger核心配置文件
 * ========================
 * @author Longshuangyang
 * @Date   2020-03-28
 * ========================
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

	@Value("true ")
	private Boolean swagger_enable;

	/**
	 * Swagger扫描的接口路径
	 */
	public static String CONTROLLER_URL = "com.exc.street.light.wifi.web";
	/**
	 * Swagger接口文档标题
	 */
	public static String SWAGGER_TITLE = "API文档-xiezhipeng";
	/**
	 * Swagger接口文档描述
	 */
	public static String SWAGGER_DESCRIPTION = "API文档";
	/**
	 * Swagger接口文档版本
	 */
	public static String SWAGGER_VERSION = "1.0.0";
	/**
	 * Swagger项目服务的URL
	 */
	public final static String SWAGGER_URL = "http://192.168.112.252:60029";

	@Bean(value = "defaultApi")
	public Docket defaultApi() {
		//分组/版本,名称
		String groupName = "1.0.0版本";

		ApiInfo apiInfo = new ApiInfoBuilder()
				.title(SWAGGER_TITLE)
				.description(SWAGGER_DESCRIPTION)
				.termsOfServiceUrl(SWAGGER_URL)
				.version(SWAGGER_VERSION)
				.build();

		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				// 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
				.apiInfo(apiInfo)
//          .enable(swagger_enable)
				.groupName(groupName)
				// 设置哪些接口暴露给Swagger展示
				.select()
				// 扫描所有有注解的api，用这种方式更灵活/ 扫描指定路径 apis(RequestHandlerSelectors.basePackage(CONTROLLER_URL))
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				// 扫描所有 .apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
		return docket;
	}
}
