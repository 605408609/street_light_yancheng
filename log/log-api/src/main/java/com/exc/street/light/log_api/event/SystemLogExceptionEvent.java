package com.exc.street.light.log_api.event;

import com.exc.street.light.resource.entity.log.LogException;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: XuJiaHao
 * @Description:
 * @Date: Created in 15:51 2020/5/8
 * @Modified:
 */
public class SystemLogExceptionEvent extends ApplicationEvent {
    public SystemLogExceptionEvent(LogException source) {
        super(source);
    }
}
