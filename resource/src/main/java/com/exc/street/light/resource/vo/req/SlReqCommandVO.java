package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 第三方接口单灯控制命令
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class SlReqCommandVO {
    /**
     * 控制命令
     */
    @ApiModelProperty(name = "cmd", value = "控制命令")
    private String cmd;

    /**
     * 控制参数
     */
    @ApiModelProperty(name = "paras", value = "控制参数")
    private SlReqCommandParamVO paras;
}