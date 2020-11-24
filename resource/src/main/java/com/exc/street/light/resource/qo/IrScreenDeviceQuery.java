package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 显示屏设备查询对象
 *
 * @author Longshuangyang
 * @date 2020/04/01
 */
@Setter
@Getter
@ToString
public class IrScreenDeviceQuery extends QueryObject{

    @ApiModelProperty(name = "name" , value = "设备名称")
    private String name;

    @ApiModelProperty(name = "networkState" , value = "网络状态(0:离线，1:在线)默认0")
    private Integer networkState;

	@ApiModelProperty(value = "区域id")
	private Integer areaId;

}
