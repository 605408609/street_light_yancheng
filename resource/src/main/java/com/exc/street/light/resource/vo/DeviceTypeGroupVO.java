package com.exc.street.light.resource.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceTypeGroupVO {
    @ApiModelProperty(name = "type" , value = "设备类型")
    private Integer type;
    @ApiModelProperty(name = "count" , value = "本周/本月在线时间总数")
    private Integer count;
}
