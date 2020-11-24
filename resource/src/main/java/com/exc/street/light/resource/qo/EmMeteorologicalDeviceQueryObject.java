package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 气象设备查询对象
 *
 * @author LeiJing
 * @Date 2020/03/26
 */
@Data
public class EmMeteorologicalDeviceQueryObject extends PageParam {

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

    /**
     * 区域id
     */
    @ApiModelProperty(name = "areaId", value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "lampPostIdList", value = "灯杆设备ID集合")
    private List<Integer> lampPostIdList;
}
