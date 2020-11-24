package com.exc.street.light.resource.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 单灯控制节点接收参数
 *
 * @author Longshuangyang
 * @date 2020/03/21
 */
@Setter
@Getter
@ToString
public class SlReqLightInfoVO {

    /**
     * 单灯设备id
     */
    @ApiModelProperty(name = "id", value = "单灯设备id")
    private Integer id;

    /**
     * 单灯设备编号
     */
    @ApiModelProperty(name = "num", value = "单灯设备编号")
    private String num;

    /**
     * 单灯设备名称
     */
    @ApiModelProperty(name = "name", value = "单灯设备名称")
    private String name;
}