package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 路灯策略下发接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/24
 */
@Setter
@Getter
@ToString
public class SlReqLampStrategyExecuteVO {

    @ApiModelProperty(name = "strategyId", value = "策略id")
    private Integer strategyId;

    @ApiModelProperty(name = "controlGroupIdList", value = "集控分组id集合")
    private List<Integer> controlGroupIdList;

    @ApiModelProperty(name = "lampPostGroupIdList", value = "灯杆分组id集合")
    private List<Integer> lampPostGroupIdList;

    @ApiModelProperty(name = "lampsGroupIdList", value = "灯具分组id集合")
    private List<Integer> lampsGroupIdList;

    @ApiModelProperty(name = "lampDeviceIdList", value = "灯具设备id集合")
    private List<Integer> lampDeviceIdList;
}
