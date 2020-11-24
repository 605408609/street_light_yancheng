package com.exc.street.light.resource.dto.sl.shuncom;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * 云平台通过此数据包对网关和设备进行控制、查询、设备信息的修改等等操作。
 * @Author: Xiaok
 * @Date: 2020/8/21 11:21
 */
@Getter
@Setter
@ToString
public class ControlGatewayDTO {

    /**
     * 设备 id
     */
    @NonNull
    private String id;

    /**
     * 端口号
     */
    @NonNull
    private Integer port;

    /**
     * 控制参数 GatewayControlEnums
     * key:GatewayControlEnums.code,value:对应值
     * 示例关灯  key:"on"  value:false
     */
    private Map<String,Object> control;
}
