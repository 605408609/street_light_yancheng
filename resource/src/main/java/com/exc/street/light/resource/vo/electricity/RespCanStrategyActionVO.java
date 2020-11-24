package com.exc.street.light.resource.vo.electricity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: Xiaok
 * @Date: 2020/11/18 11:05
 */
@Data
@ApiModel("策略动作返回类")
public class RespCanStrategyActionVO {

    @ApiModelProperty(name = "id", value = "")
    private Integer id;

    @ApiModelProperty(name = "isOpen", value = "动作类型,0-关,1-开")
    private Integer isOpen;

    @ApiModelProperty(name = "executionTime", value = "执行时间,格式：HH:mm:ss")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private String executionTime;

    @ApiModelProperty(name = "startDate", value = "开始日期,格式：yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;

    @ApiModelProperty(name = "endDate", value = "结束日期,格式：yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    @ApiModelProperty(name = "cycleTypes", value = "周期控制集合,周一:1,周二:2,周三:3,周四:4,周五:5,周六:6,周日:7")
    private Integer[] cycleTypes;
}
