package com.exc.street.light.resource.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 获取上下文得到spring容器中的静态bean
 *
 * @author Longshuangyang
 * @date 2020/04/25
 */
@Configuration
public class ApplicationContextUtil implements ApplicationContextAware {

    /**
     * 定义静态的ApplicationContext的成员对象
     */
    private static ApplicationContext applicationContext;

    /**
     * 重新setApplicationContext方法，得到参数中的ApplicationContext对象赋给成员对象
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    /**
     * 根据Class，调用上下文的getBean方法获取容器中指定的对象
     *
     * @param Clazz
     * @param <T>
     * @return
     */
    public static <T> T get(Class<T> Clazz) {
        return applicationContext.getBean(Clazz);
    }

    /**
     * 根据对象名，调用上下文的getBean方法获取容器中指定的对象
     *
     * @param beanName
     * @return
     */
    public Object get(String beanName) {
        return applicationContext.getBean(beanName);
    }

}
