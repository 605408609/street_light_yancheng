package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SlRespDeviceUpgradeLogStatusVO {

    @ApiModelProperty(name = "deviceId" , value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(name = "editionOld" , value = "当前版本号")
    private String editionOld;

    @ApiModelProperty(name = "isSuccess" , value = "是否成功（1：成功，2：失败）")
    private Integer isSuccess;

    @ApiModelProperty(name = "logId" , value = "升级记录id")
    private Integer logId;

}
