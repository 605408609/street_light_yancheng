package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 路灯控制接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class SlReqLightControlVO {
    /**
     * 路灯设备信息集合
     */
    @ApiModelProperty(name = "lightInfoVOS", value = "路灯设备信息集合")
    private List<SlReqLightInfoVO> lightInfoVOS;

    /**
     * 控制类型 0:关 1:开
     */
    @ApiModelProperty(name = "type", value = "控制类型 0:关 1:开")
    private Integer type;

    /**
     * 亮度值 0-100
     */
    @ApiModelProperty(name = "lightness", value = "亮度值 0-100")
    private Integer lightness;

    /**
     * 亮度值 0-100
     */
    @ApiModelProperty(name = "colorValue", value = "色值 0-255")
    private Double colorValue;

    /**
     * 站点id集合
     */
    @ApiModelProperty(name = "siteIdList", value = "站点id集合")
    private List<Integer> siteIdList;

    @ApiModelProperty(name = "areaIdList", value = "区域id集合")
    private List<Integer> areaIdList;

    @ApiModelProperty(name = "streetIdList", value = "街道id集合")
    private List<Integer> streetIdList;

    /**
     * 灯杆id集合
     */
    @ApiModelProperty(name = "lampPostIdList", value = "灯杆id集合")
    private List<Integer> lampPostIdList;

    /**
     * 分组id集合
     */
    @ApiModelProperty(name = "groupIdList", value = "分组id集合")
    private List<Integer> groupIdList;

    /**
     * 下发数据模式
     */
    @ApiModelProperty(name = "sendMode", value = "下发数据模式")
    private String sendMode;

    /**
     * 设备id集合
     */
    @ApiModelProperty(name = "lampDeviceIdList", value = "设备id集合")
    private List<Integer> lampDeviceIdList;

    /**
     * 灯具分组id集合
     */
    @ApiModelProperty(name = "controlLoopIdList" , value = "集中控制器分组id集合")
    private List<Integer> controlLoopIdList;

}