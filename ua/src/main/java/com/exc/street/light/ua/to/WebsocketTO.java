package com.exc.street.light.ua.to;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Linshiwen
 * @date 2018/7/12
 */
@Setter
@Getter
@ToString
public class WebsocketTO {
    private String eventType;
    private Object message;
}
