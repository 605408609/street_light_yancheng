package com.exc.street.light.resource.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 告警返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class TimeVO {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "startTime" , value = "开始时间")
    private Date startTime;

    @ApiModelProperty(name = "startTimeString" , value = "开始时间,字符串格式")
    private String startTimeString;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "endTime" , value = "结束时间")
    private Date endTime;

    @ApiModelProperty(name = "endTimeString" , value = "结束时间,字符串格式")
    private String endTimeString;
}
