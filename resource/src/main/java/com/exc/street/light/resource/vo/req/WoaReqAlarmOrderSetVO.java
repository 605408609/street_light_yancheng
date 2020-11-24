package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 策略定时接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/23
 */
@Setter
@Getter
@ToString
public class WoaReqAlarmOrderSetVO {
    /**
     * 自动生成工单设置id
     */
    @ApiModelProperty(name = "id", value = "自动生成工单设置id")
    private Integer id;

    /**
     * 路灯id集合
     */
    @ApiModelProperty(name = "lampPostIdList", value = "路灯id集合")
    private List<Integer> lampPostIdList;

    /**
     * 处理人id
     */
    @ApiModelProperty(name = "processor", value = "处理人id")
    private Integer processor;

    /**
     * 处理时长（小时）
     */
    @ApiModelProperty(name = "handleDuration", value = "处理时长（小时）")
    private Integer handleDuration;

}
