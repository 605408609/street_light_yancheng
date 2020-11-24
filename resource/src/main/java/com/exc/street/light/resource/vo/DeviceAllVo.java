package com.exc.street.light.resource.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class DeviceAllVo {
    @ApiModelProperty(name = "id" , value = "设备ID")
    private int id;
    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;
    @ApiModelProperty(name = "datetime" , value = "设备今天上线时间")
    private Date datetime;
    @ApiModelProperty(name = "type" , value = "设备类型")
    private int type;
    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private int lampPostId;
}
