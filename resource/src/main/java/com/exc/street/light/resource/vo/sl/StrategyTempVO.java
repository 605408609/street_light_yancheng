package com.exc.street.light.resource.vo.sl;

import lombok.Data;

@Data
public class StrategyTempVO {

    //报文
    private String message;

    //发送方式
    private Integer deviceTypeId;

    //发送标识
    private String sendId;
}
