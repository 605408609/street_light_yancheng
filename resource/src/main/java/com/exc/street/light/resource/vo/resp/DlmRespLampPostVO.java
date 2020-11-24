package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DlmRespLampPostVO {

    /**
     * 灯杆id
     */
    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    /**
     * 灯杆编号
     */
    @ApiModelProperty(name = "lampPostNum" , value = "灯杆编号")
    private String lampPostNum;

    /**
     * 灯杆名称
     */
    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;

    /**
     * 区域名称
     */
    @ApiModelProperty(name = "areaName" , value = "区域名称")
    private String areaName;

    /**
     * 街道名称
     */
    @ApiModelProperty(name = "streetName" , value = "街道名称")
    private String streetName;

    /**
     * 站点名称
     */
    @ApiModelProperty(name = "siteName" , value = "站点名称")
    private String siteName;

    /**
     * 挂载设备数量
     */
    @ApiModelProperty(name = "deviceNumber" , value = "挂载设备数量")
    private Integer deviceNumber;


}
