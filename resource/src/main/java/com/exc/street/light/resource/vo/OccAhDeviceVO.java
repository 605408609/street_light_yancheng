 package com.exc.street.light.resource.vo;

import java.util.List;

import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.occ.AhPlay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huangmin
 * @date 2020/03/12
 */
 @Data
public class OccAhDeviceVO extends AhDevice{
    /**
     * 灯杆名称
     */
     @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;
     /**
      * 安装位置
      */
     @ApiModelProperty(name = "location", value = "安装位置")
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
      * 告警信息集合
      */
     @ApiModelProperty(name = "listAhPlay", value = "告警信息集合")
     private List<AhPlay> listAhPlay;
     
     @ApiModelProperty(value = "区域id")
     private Integer areaId;
}
