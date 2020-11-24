package com.exc.street.light.resource.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 告警区域分析返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaAlarmAreaAnalyseVO {

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "areaName" , value = "区域名称")
    private String areaName;

    @ApiModelProperty(name = "streetId" , value = "街道id")
    private Integer streetId;

    @ApiModelProperty(name = "streetName" , value = "街道名称")
    private String streetName;

    @ApiModelProperty(name = "siteId" , value = "站点id")
    private Integer siteId;

    @ApiModelProperty(name = "siteName" , value = "站点名称")
    private String siteName;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "userId" , value = "用户id")
    private Integer userId;

    @ApiModelProperty(name = "userName" , value = "用户名称")
    private String userName;

    @ApiModelProperty(name = "alarmId" , value = "告警id")
    private Integer alarmId;

    @ApiModelProperty(name = "alarmStatus" , value = "告警状态")
    private Integer alarmStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "alarmCreateTime" , value = "告警创建时间")
    private Date alarmCreateTime;
}
