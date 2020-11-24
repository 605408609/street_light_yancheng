package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 策略动作接收参数
 *
 * @author xiezhipeng
 * @date 2020/08/26
 */
@Data
public class SlReqStrategyActionVO {

    @ApiModelProperty(name = "id" , value = "策略动作表ID")
    private Integer id;

    @ApiModelProperty(name = "isOpen" , value = "策略动作类型（1：开灯，0：关灯）")
    private Integer isOpen;

    @ApiModelProperty(name = "brightness" , value = "亮度，默认0")
    private Integer brightness;

    @ApiModelProperty(name = "executionTime" , value = "执行时间（时分秒）")
    private String executionTime;

    @ApiModelProperty(name = "startDate" , value = "开始时间（年月日）")
    private String startDate;

    @ApiModelProperty(name = "endDate" , value = "结束时间（年月日）")
    private String endDate;

    @ApiModelProperty(name = "cycleTypes", value = "周期类型(1:星期天 2:星期一 3:星期二 4:星期三 5:星期四 6:星期五 7:星期六)")
    private Integer[] cycleTypes;

    @ApiModelProperty(name = "strategyId" , value = "策略id")
    private Integer strategyId;

    @ApiModelProperty(name = "lightModeId" , value = "亮灯方式id")
    private Integer lightModeId;

    @ApiModelProperty(name = "deviation" , value = "偏移值（分钟）")
    private Integer deviation;

    @ApiModelProperty(name = "deviceTypeIdOfActionList" , value = "绑定动作的设备类型id集合")
    private List<Integer> deviceTypeIdOfActionList;

}
