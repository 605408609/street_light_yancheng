package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SlRespSystemDeviceParameterVO {

    @ApiModelProperty(name = "deviceId" , value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(name = "deviceTypeId" , value = "设备类型id")
    private Integer deviceTypeId;

    @ApiModelProperty(name = "filed" , value = "参数字段")
    private String filed;

    @ApiModelProperty(name = "unit" , value = "参数单位")
    private String unit;

    @ApiModelProperty(name = "name" , value = "参数含义")
    private String name;

    @ApiModelProperty(name = "isMust" , value = "是否必填（0：否，1：是）")
    private Integer isMust;

    @ApiModelProperty(name = "regexp" , value = "校验正则表达式")
    private String regexp;

    @ApiModelProperty(name = "icon" , value = "小图标文件名")
    private String icon;

    @ApiModelProperty(name = "showFlag" , value = "展示标识（0：仅在列表中展示，1：仅在详情中展示）")
    private Integer showFlag;

    @ApiModelProperty(name = "parameterValue" , value = "参数值")
    private String parameterValue;

    @ApiModelProperty(name = "parameterId" , value = "参数id")
    private Integer parameterId;

}
