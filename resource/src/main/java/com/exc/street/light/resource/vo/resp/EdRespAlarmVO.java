package com.exc.street.light.resource.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class EdRespAlarmVO {

    @ApiModelProperty(name = "id" , value = "告警id")
    private Integer id;

    @ApiModelProperty(name = "deviceId" , value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    @ApiModelProperty(name = "num" , value = "编号")
    private String num;

    @ApiModelProperty(name = "locationSiteId" , value = "站点ID")
    private Integer locationSiteId;

    @ApiModelProperty(name = "locationSiteName" , value = "站点名称")
    private String locationSiteName;

    @ApiModelProperty(name = "locationStreetId" , value = "街道ID")
    private Integer locationStreetId;

    @ApiModelProperty(name = "locationStreetName" , value = "街道名称")
    private String locationStreetName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "alarmTime" , value = "告警时间")
    private Date alarmTime;

    @ApiModelProperty(name = "realData" , value = "实时倾角（单位：°）")
    private Double realData;

    @ApiModelProperty(name = "status" , value = "井盖状态 0-关 1-开")
    private Integer status;

    @ApiModelProperty(name = "alarmStatus" , value = "告警状态 0-正常 1-告警")
    private Integer alarmStatus;

}
