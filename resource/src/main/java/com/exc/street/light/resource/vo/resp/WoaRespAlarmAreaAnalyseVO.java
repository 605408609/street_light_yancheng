package com.exc.street.light.resource.vo.resp;

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
public class WoaRespAlarmAreaAnalyseVO {

    @ApiModelProperty(name = "areaName" , value = "区域名称")
    private String areaName;

    @ApiModelProperty(name = "streetNames" , value = "街道名称（，逗号拼接）")
    private String streetNames;

    @ApiModelProperty(name = "siteNames" , value = "站点名称（，逗号拼接）")
    private String siteNames;

    @ApiModelProperty(name = "lampPostNumber" , value = "灯杆数量")
    private Integer lampPostNumber;

    @ApiModelProperty(name = "alarmTotal" , value = "告警总数")
    private Integer alarmTotal;

    @ApiModelProperty(name = "untreatedAlarmNumber" , value = "未处理告警数量")
    private Integer untreatedAlarmNumber;

    @ApiModelProperty(name = "maintainers" , value = "维护人员（，逗号拼接）")
    private String maintainers;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name = "latelyAlarmTime" , value = "最近告警上传时间")
    private Date latelyAlarmTime;
}
