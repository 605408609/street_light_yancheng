package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DlmBatchCreateDeviceRespVO {

    @ApiModelProperty(name = "deviceId" , value = "返回id")
    private String deviceId;

    @ApiModelProperty(name = "resultCode" , value = "结果编号")
    private Integer resultCode;

    @ApiModelProperty(name = "description" , value = "结果信息")
    private String description;

    @ApiModelProperty(name = "imei" , value = "imei号")
    private String imei;
}
