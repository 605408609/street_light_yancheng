package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DlmDeviceQuery {
    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;
    @ApiModelProperty(name = "type" , value = "设备类型 0灯杆 1智慧照明 2公共WIFI 3公共广播 4智能安防 5信息发布 6一键呼叫 7环境监测")
    private int type=0;
}
