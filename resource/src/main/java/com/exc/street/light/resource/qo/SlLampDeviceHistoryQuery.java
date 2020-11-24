package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 历史策略下的设备查询类
 * @Date 2020/10/23
 */
@Data
public class SlLampDeviceHistoryQuery {

    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private String createTime;

    @ApiModelProperty(name = "strategyId" , value = "策略ID")
    private Integer strategyId;

    @ApiModelProperty(name = "isSuccess" , value = "2:失败 3：成功")
    private Integer isSuccess;

}
