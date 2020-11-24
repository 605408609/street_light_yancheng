package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 策略查询对象
 *
 * @author Longshuangyang
 * @date 2020/03/26
 */
@Setter
@Getter
@ToString
public class SlLampStrategyQuery extends QueryObject{

    @ApiModelProperty(name = "name" , value = "策略名称")
    private String name;

    @ApiModelProperty(name = "strategyTypeId" , value = "策略类型id")
    private Integer strategyTypeId;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

    @ApiModelProperty(name = "lampGroupIdList" , value = "灯具分组id集合")
    private List<Integer> lampsGroupIdList;

    @ApiModelProperty(name = "controlGroupIdList" , value = "集控分组id集合")
    private List<Integer> controlGroupIdList;

    @ApiModelProperty(name = "lampPostIdList", value = "灯杆分组id集合")
    private List<Integer> lampPostIdList;

    @ApiModelProperty(name = "lampDeviceIdList", value = "灯具设备id集合")
    private List<Integer> lampDeviceIdList;

    @ApiModelProperty(name = "strategyIdList", value = "策略id集合")
    private List<Integer> strategyIdList;

}
