package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 第三方接口单灯控制命令参数
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class SlReqCommandParamVO {
    /**
     * 灯1亮度
     */
    @ApiModelProperty(name = "ctrl0", value = "灯1亮度")
    private Integer ctrl0;

    /**
     * 灯2亮度 默认0
     */
    @ApiModelProperty(name = "ctrl1", value = "灯2亮度 默认0，不用填")
    private Integer ctrl1;

    /**
     * 延时时间:分钟(延时时间到恢复默认状态:常亮) 65535:一直保持控制动作
     */
    @ApiModelProperty(name = "manctrldelay", value = "延时时间:分钟(延时时间到恢复默认状态:常亮) 65535:一直保持控制动作")
    private Integer manctrldelay;
}