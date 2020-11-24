package com.exc.street.light.wifi.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description Ap设备查询参数类
 * @Date 2020/3/23
 */
@Data
public class ApDeviceQueryObject extends PageParam {

    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "网络状态")
    private Integer networkState;

    @ApiModelProperty(value = "区域id")
    private Integer areaId;
}
