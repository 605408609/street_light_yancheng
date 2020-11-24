package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工单图片返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespOrderAlarmVO {

    @ApiModelProperty(name = "alarmId" , value = "工单的告警id")
    private Integer alarmId;

    @ApiModelProperty(name = "lampPostId" , value = "告警发生的灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "lampPostName" , value = "告警发生的灯杆名称")
    private String lampPostName;

    @ApiModelProperty(name = "alarmTypeId" , value = "告警类型id")
    private Integer alarmTypeId;

    @ApiModelProperty(name = "alarmTypeName" , value = "告警类型名称")
    private String alarmTypeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "createTime" , value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "alarmAddr" , value = "告警发生地址")
    private String alarmAddr;
}
