package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 传感器关联显示屏显示数据设置查询对象
 *
 * @author Longshuangyang
 * @date 2020/11/11
 */
@Setter
@Getter
@ToString
public class IrScreenSubtitleEmQuery extends QueryObject{

    @ApiModelProperty(name = "lampPostId" , value = "灯杆id")
    private Integer lampPostId;

    @ApiModelProperty(name = "lampPostName", value = "灯杆名称")
    private String lampPostName;

    @ApiModelProperty(name = "areaId", value = "分区id")
    private Integer areaId;

}
