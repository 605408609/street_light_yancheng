package com.exc.street.light.sl.netty.shuncom;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xujiahao
 * @description
 * @date in 16:45 2017/12/28
 */
@Getter
@Setter
public class RequestBean {
    private byte[] req;
    private int length;
}
