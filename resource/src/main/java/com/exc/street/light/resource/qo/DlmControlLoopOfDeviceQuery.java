package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xiezhipeng
 * @Description 分组下的设备查询独享
 * @Date 2020/8/25
 */
@Data
public class DlmControlLoopOfDeviceQuery {

    @ApiModelProperty(name = "controlId" , value = "集控器ID")
    private Integer controlId;

    @ApiModelProperty(name = "loopId" , value = "分组ID")
    private Integer loopId;

    @ApiModelProperty(name = "areaId" , value = "区域ID")
    private Integer areaId;

    @ApiModelProperty(name = "deviceIdList" , value = "灯具id集合")
    private List<Integer> deviceIdList;
}
