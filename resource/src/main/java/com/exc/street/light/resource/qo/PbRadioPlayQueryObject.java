package com.exc.street.light.resource.qo;

import com.exc.street.light.resource.core.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公共广播节目播放查询对象
 *
 * @author LeiJing
 * @Date 2020/04/01
 */
@Data
public class PbRadioPlayQueryObject extends PageParam {

    private static final long serialVersionUID = 2220339883262437920L;
    /**
     * 设备名称
     */
    @ApiModelProperty(name = "deviceName" , value = "设备名称")
    private String deviceName;

    /**
     * 节目名称
     */
    @ApiModelProperty(name = "name" , value = "节目名称")
    private String name;

    /**
     * 播放状态（0：待播放，1：正在播放）
     */
    @ApiModelProperty(name = "status", value = "播放状态（0：待播放，1：正在播放）")
    private Integer status;

    /**
     * 区域id
     */
    @ApiModelProperty(name = "areaId", value = "区域id")
    private Integer areaId;
}
