package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 集控分组或回来分页查询类
 * @Date 2020/8/24
 */
@Data
public class DlmControlLoopQuery extends PageParam {

    @ApiModelProperty(name = "deviceName" , value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "loopName" , value = "分组名称")
    private String loopName;

    @ApiModelProperty(name = "sceneIsOpen" , value = "场景是否启用（0：未启用，1：启用）")
    private Integer sceneIsOpen;

    @ApiModelProperty(name = "loopIsOpen" , value = "回路开关状态（0：关，1：开）")
    private Integer loopIsOpen;

    @ApiModelProperty(name = "areaId" , value = "区域ID")
    private Integer areaId;

}
