package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 告警类型分析返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespAlarmTypeAnalyseVO {

    @ApiModelProperty(name = "alarmTypeId" , value = "告警类型id")
    private Integer alarmTypeId;

    @ApiModelProperty(name = "alarmTypeName" , value = "告警类型名称")
    private String alarmTypeName;

    @ApiModelProperty(name = "alarmId" , value = "告警id")
    private Integer alarmId;

    @ApiModelProperty(name = "alarmNumber" , value = "告警数量")
    private Integer alarmNumber;

    @ApiModelProperty(name = "proportion" , value = "占比")
    private Double proportion;

    @ApiModelProperty(name = "alarmTotal" , value = "告警总数")
    private Integer alarmTotal;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "latelyAlarmTime" , value = "最近告警上传时间")
    private Date latelyAlarmTime;
}
