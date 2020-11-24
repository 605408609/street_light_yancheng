package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 气象设备查询对象
 *
 * @author LeiJing
 * @Date 2020/03/26
 */
@Data
public class MeteorologicalDeviceQueryObject extends PageParam {

    /**
     * 设备名称
     */
    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    /**
     * 设备编号
     */
    @ApiModelProperty(name = "num" , value = "设备编号")
    private String num;

    /**
     * 网络状态
     */
    @ApiModelProperty(name = "networkState", value = "网络状态")
    private Integer networkState;
}
