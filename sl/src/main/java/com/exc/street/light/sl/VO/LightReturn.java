package com.exc.street.light.sl.VO;

import lombok.Data;

@Data
public class LightReturn {

    //同请求
    private Integer code;
    //服务器端指令序列号
    private Integer serial;
    //控制结果
    private Integer result;
    //客户端指令序列号
    private Integer headSerial;
    //响应提示信息
    private String response;


}
