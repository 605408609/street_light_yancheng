package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Xiaok
 * @Date: 2020/10/26 16:33
 */
@Data
public class PbReqPlayRemoveBindVO {

    @ApiModelProperty(name = "playId",value = "广播定时任务ID")
    private Integer playId;

    @ApiModelProperty(name = "deviceId",value = "广播设备ID")
    private Integer deviceId;
}
