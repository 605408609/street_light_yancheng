package com.exc.street.light.wifi.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description wifiAp统计列表参数查询类
 * @Date 2020/3/27
 */
@Data
public class ApQueryObject extends PageParam {

    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "区域id")
    private Integer areaId;
}
