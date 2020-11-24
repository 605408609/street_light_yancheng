 package com.exc.street.light.resource.vo;

import com.exc.street.light.resource.entity.ss.SsDevice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huangmin
 * @date 2020/03/12
 */
 @Data
public class SsDeviceVO extends SsDevice {
    /**
     * 灯杆名称
     */
    @ApiModelProperty(name = "lampPostName", value = "灯杆名称")
    private String lampPostName;
    /**
     * 站点ID
     */
    @ApiModelProperty(name = "siteId", value = "站点ID")
    private Integer siteId;
    /**
     * 站点名称
     */
    @ApiModelProperty(name = "siteName", value = "站点名称")
    private String siteName;

    /**
     * 街道ID
     */
    @ApiModelProperty(name = "streetId", value = "街道名称")
    private Integer streetId;
    /**
     * 街道名称
     */
    @ApiModelProperty(name = "streetName", value = "街道名称")
    private String streetName;
    /**
     * 区域ID
     */
    @ApiModelProperty(name = "areaId", value = "区域ID")
    private Integer areaId;
    /**
     * 区域名称
     */
    @ApiModelProperty(name = "areaName", value = "区域名称")
    private String areaName;
    
    /**
     * 安装位置
     */
    @ApiModelProperty(name = "location", value = "安装位置")
    private String location;

}
