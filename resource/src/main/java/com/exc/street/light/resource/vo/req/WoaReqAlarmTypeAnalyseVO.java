package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 新工单接受对象
 *
 * @author Longshuangyang
 * @date 2019/09/08
 */
@Getter
@Setter
@ToString
public class WoaReqAlarmTypeAnalyseVO {

    /**
     * 选择阶段（1：今日，2：近一周，3：近一月）
     */
    @ApiModelProperty(name = "stage", value = "选择阶段（1：今日，2：近一周，3：近一月）")
    private Integer stage;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime", value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间")
    private String endTime;
}
