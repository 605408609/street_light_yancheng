package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 节目播放列表查询对象
 *
 * @author Longshuangyang
 * @date 2019/8/23
 */
@Setter
@Getter
@ToString
public class IrPlayProgramQuery extends QueryObject {

    /**
     * 显示屏设备名称
     */
    @ApiModelProperty(name = "deviceName" , value = "显示屏设备名称")
    private String deviceName;

    /**
     * 节目名称
     */
    @ApiModelProperty(name = "programName" , value = "节目名称")
    private String programName;

    /**
     * 播放状态
     */
    @ApiModelProperty(name = "status" , value = "播放状态")
    private Integer status;

    /**
     * 显示屏设备id列表
     */
    @ApiModelProperty(name = "deviceIdList" , value = "显示屏设备id列表")
    private List<Integer> deviceIdList;

    /**
     * 是否控制所有(0：否，1：是，默认0)
     */
    @ApiModelProperty(name = "isAll" , value = "是否控制所有(0：否，1：是，默认0)")
    private Integer isAll = 0;

}
