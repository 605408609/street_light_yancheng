package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 播放中节目查询对象
 *
 * @author Longshuangyang
 * @date 2020/04/01
 */
@Setter
@Getter
@ToString
public class IrScreenPlayQuery extends QueryObject{

    @ApiModelProperty(name = "programName" , value = "节目名称")
    private String programName;

    @ApiModelProperty(name = "playStatus" , value = "播放状态（0：待播放，1：正在播放，2：结束播放）")
    private Integer playStatus;
    @ApiModelProperty(value = "区域id")
	private Integer areaId;

}
