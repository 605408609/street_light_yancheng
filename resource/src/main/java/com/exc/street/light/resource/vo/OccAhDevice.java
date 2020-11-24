 package com.exc.street.light.resource.vo;

import com.exc.street.light.resource.entity.occ.AhPlay;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author huangmin
 * @date 2020/03/12
 */
 @Data
public class OccAhDevice extends AhPlay {
    // AhDeviceVO
    /**
     * 灯杆名称
     */
    @ApiModelProperty(name = "lampPostName", value = "灯杆名称")
    private String lampPostName;
    /**
     * 报警盒安装位置
     */
    @ApiModelProperty(name = "location", value = "报警盒安装位置")
    private String location;

    /**
     * 紧急报警设备名称
     */
    @ApiModelProperty(name = "deviceName", value = "紧急报警设备名称")
    private String deviceName;

    /**
     * 站点名称
     */
    @ApiModelProperty(name = "siteName", value = "站点名称")
    private String siteName;

    /**
     * 街道名称
     */
    @ApiModelProperty(name = "streetName", value = "街道名称")
    private String streetName;

    /**
     * 区域名称
     */
    @ApiModelProperty(name = "areaName", value = "区域名称")
    private String areaName;

    /**
     * 开始时刻
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "beginTime", value = "开始时刻")
    private Date beginTime;

    /**
     * 结束时刻
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(name = "endTime", value = "结束时刻")
    private Date endTime;

}
