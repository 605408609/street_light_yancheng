package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Xiezhipeng
 * @Description 历史灯具设备返回视图类
 * @Date 2020/10/23
 */
@Data
public class SlRespLampDeviceHistoryVO {

    @ApiModelProperty(name = "id" , value = "主键id")
    private Integer id;

    @ApiModelProperty(name = "deviceId" , value = "设备ID")
    private Integer deviceId;

    @ApiModelProperty(name = "deviceName" , value = "设备名称")
    private String deviceName;

    @ApiModelProperty(name = "deviceNum" , value = "设备编号")
    private String deviceNum;

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "lampPostName" , value = "灯杆名称")
    private String lampPostName;

    @ApiModelProperty(name = "isSuccess" , value = "2:失败 3：成功")
    private Integer isSuccess;

}
