package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: Xiezhipeng
 * @Description: 场景动作请求对象
 * @Date: 2020/11/7 14:49
 */
@Data
public class DlmReqSceneActionVO {

    @ApiModelProperty(name = "id" , value = "回路场景动作表，主键id自增")
    private Integer id;

    @ApiModelProperty(name = "isOpen" , value = "场景动作（0：关 1：开）")
    private Integer isOpen;

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

    @ApiModelProperty(name = "cron" , value = "cron表达式")
    private String cron;

    @ApiModelProperty(name = "openModeId" , value = "开关方式id(经纬度)")
    private Integer openModeId;

    @ApiModelProperty(name = "deviation" , value = "偏移值（分钟）")
    private Integer deviation;

}
