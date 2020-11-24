package com.exc.street.light.resource.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 告警工单设置对象
 *
 * @author Longshuangyang
 * @date 2020/03/26
 */
@Setter
@Getter
@ToString
public class WoaAlarmOrderSetQuery extends QueryObject{

    /**
     * 自动生成工单状态（0：关闭，1：开启）
     */
    @ApiModelProperty(name = "status" , value = "自动生成工单状态（0：关闭，1：开启）")
    private String status;

    @ApiModelProperty(name = "areaId" , value = "区域id")
    private Integer areaId;

}
