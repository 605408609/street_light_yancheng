package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 显示屏更多控制设置接收参数
 *
 * @author Longshuangyang
 * @date 2020/04/01
 */
@Setter
@Getter
@ToString
public class IrReqScreenOperateVO {

    /**
     * 显示屏开关状态（0：关，1：开）
     */
    @ApiModelProperty(name = "state" , value = "显示屏开关状态（0：关，1：开）")
    private Integer state;

    /**
     * 显示屏音量
     */
    @ApiModelProperty(name = "volume" , value = "显示屏音量")
    private Integer volume;

    /**
     * 显示屏亮度
     */
    @ApiModelProperty(name = "brightness" , value = "显示屏亮度")
    private Integer brightness;

    /**
     * 是否控制所有(0：否，1：是)
     */
    @ApiModelProperty(name = "isAll" , value = "是否控制所有(0：否，1：是)")
    private Integer isAll = 0;

    /**
     * 设备编号列表
     */
    @ApiModelProperty(name = "numList" , value = "设备编号列表")
    private List<String> numList;
}
