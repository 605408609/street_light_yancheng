package com.exc.street.light.resource.dto.dlm;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * exc集控定时设置类
 *
 * @Author: Xiaok
 * @Date: 2020/11/10 17:26
 */
@Data
@Accessors(chain = true)
public class ControlLoopTimerDTO {

    /**
     * 1为时间定时，2，3，4，5为经纬度定时
     */
    @ApiModelProperty(name = "type", required = true, value = "定时类型 1:定时执行 2:日出之前 3:日出之后 4:日落之前 5:日落之后")
    private Integer type;

    /**
     * type==1必填 格式 HH:mm:ss
     */
    @ApiModelProperty(name = "time", value = "定时时间", required = false)
    private String time;

    /**
     * 当type!=1必填
     */
    @ApiModelProperty(name = "minuteValue", value = "日出日落时间偏移量", required = false)
    private Integer minuteValue;

    @ApiModelProperty(name = "loopNum", value = "回路编号,1-8", required = true)
    private Integer loopNum;

    @ApiModelProperty(name = "isOpen", value = "回路的开关状态", required = true)
    private Boolean isOpen;

    /**
     * 不限时间传null,type!=1时无效
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(name = "startDate", value = "定时开始日期", required = false)
    private Date startDate;

    /**
     * 不限时间传null,type!=1时无效
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(name = "endDate", value = "定时结束日期", required = false)
    private Date endDate;

    @ApiModelProperty(name = "cycleTypes", value = "周期定时数组，传星期几,1-7", required = false)
    private int[] cycleTypes;
}
