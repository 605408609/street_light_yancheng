package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 灯控查询对象
 *
 * @author Longshuangyang
 * @date 2020/03/26
 */
@Setter
@Getter
@ToString
public class SlLampDeviceQuery extends QueryObject{

    /**
     * 设备id集合
     */
    @ApiModelProperty(name = "deviceIdList" , value = "设备id集合")
    private List<Integer> deviceIdList;

    /**
     * 站点id集合
     */
    @ApiModelProperty(name = "siteIdList" , value = "站点id集合")
    private List<Integer> siteIdList;

    /**
     * 灯杆id集合
     */
    @ApiModelProperty(name = "lampPostIdList" , value = "灯杆id集合")
    private List<Integer> lampPostIdList;

    /**
     * 分组id集合
     */
    @ApiModelProperty(name = "groupIdList" , value = "分组id集合")
    private List<Integer> groupIdList;

    /**
     * 灯具分组id集合
     */
    @ApiModelProperty(name = "lampGroupIdList" , value = "灯具分组id集合")
    private List<Integer> lampGroupIdList;

    /**
     * 灯具分组id集合
     */
    @ApiModelProperty(name = "locationControlIdList" , value = "集中控制器id集合")
    private List<Integer> locationControlIdList;

    /**
     * 灯具分组id集合
     */
    @ApiModelProperty(name = "controlLoopIdList" , value = "集中控制器分组id集合")
    private List<Integer> controlLoopIdList;

    /**
     * 灯具id集合
     */
    @ApiModelProperty(name = "singleIdList" , value = "灯具id集合")
    private List<Integer> singleIdList;

    @ApiModelProperty(name = "areaId" , value = "分区id")
    private Integer areaId;

    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    @ApiModelProperty(name = "networkState" , value = "网络状态0：离线，1：在线")
    private Integer networkState;

}
