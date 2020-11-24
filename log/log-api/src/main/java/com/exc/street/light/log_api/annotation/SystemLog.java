package com.exc.street.light.log_api.annotation;

import java.lang.annotation.*;

/**
 * @Author: XuJiaHao
 * @Description: 系统操作日志注解
 * @Date: Created in 23:16 2020/5/7
 * @Modified:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
    String logModul() default ""; // 操作模块
    String logType() default "";  // 操作类型
    String logDesc() default "";  // 操作说明
}
