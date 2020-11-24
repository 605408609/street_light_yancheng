package com.exc.street.light.sl.VO;

import lombok.Data;

/**
 * 自定义属性值
 */
@Data
public class ObjectNode {

    //电量
    private String valtage;
    //信号强度
    private String signal;
    //信号参数
    private String snr;
    //信号参数
    private String ecl;
    //设备上行协议数据
    private String data;

}
