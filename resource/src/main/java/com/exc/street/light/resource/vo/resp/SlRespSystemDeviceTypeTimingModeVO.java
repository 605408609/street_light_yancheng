package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 设备类型支持的定时方式的返回对象
 * @Date 2020/8/26
 */
@Data
public class SlRespSystemDeviceTypeTimingModeVO {

    @ApiModelProperty(name = "deviceTypeId", value = "设备类型id")
    private Integer deviceTypeId;

    @ApiModelProperty(name = "timingModeIdList", value = "定时方式id集合")
    private List<Integer> timingModeIdList;

}
