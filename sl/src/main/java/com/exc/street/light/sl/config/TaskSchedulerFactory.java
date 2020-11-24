package com.exc.street.light.sl.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @author Longshuangyang
 * @date 2020/03/25
 */
@Component
public class TaskSchedulerFactory extends AdaptableJobFactory {
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // 首先，调用父类的方法创建好Quartz所需的Job实例
        Object jobInstance = super.createJobInstance(bundle);
        // 然后，使用BeanFactory为创建好的Job实例进行属性自动装配并将其纳入到Spring容器的管理之中，属于Spring的技术范畴.
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
