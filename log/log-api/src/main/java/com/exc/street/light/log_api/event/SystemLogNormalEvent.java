package com.exc.street.light.log_api.event;

import com.exc.street.light.resource.entity.log.LogNormal;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: XuJiaHao
 * @Description:
 * @Date: Created in 15:50 2020/5/8
 * @Modified:
 */
public class SystemLogNormalEvent extends ApplicationEvent {
    public SystemLogNormalEvent(LogNormal source) {
        super(source);
    }
}
