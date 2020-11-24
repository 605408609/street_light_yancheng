package com.exc.street.light.resource.vo.req.htLamp;

import com.exc.street.light.resource.enums.sl.ht.HtLoopOutputTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 华体集中控制器回路策略
 * @Author: Xiaok
 * @Date: 2020/8/13 17:26
 */
@Getter
@Setter
@ToString
public class HtSetControlPlanRequestVO {

    @ApiModelProperty("开始时间 如果开始时间和结束时间为空，则按time中的时间点来")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty("结束时间 如果开始时间和结束时间为空，则按time中的时间点来")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "动作时间点",required = true)
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    private Date time;

    @ApiModelProperty(value = "回路输出动作类型 见 HtLoopOutputTypeEnum.code",required = true)
    private Integer actType;

    @ApiModelProperty("回路1   false：不控制  true：控制")
    private boolean loop1;

    @ApiModelProperty("回路2   false：不控制  true：控制")
    private boolean loop2;

    @ApiModelProperty("回路3   false：不控制  true：控制")
    private boolean loop3;

    @ApiModelProperty("回路4   false：不控制  true：控制")
    private boolean loop4;

}
