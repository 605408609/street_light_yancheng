package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 地图工单概要返回参数
 *
 * @author Longshuangyang
 * @date 2020/04/16
 */
@Getter
@Setter
@ToString
public class WoaRespOrderSummaryVO {

    /**
     * 正在处理工单数量
     */
    @ApiModelProperty(name = "beingCount" , value = "正在处理工单数量")
    private Integer beingCount;

    /**
     * 未处理工单数量
     */
    @ApiModelProperty(name = "noCount" , value = "未处理工单数量")
    private Integer noCount;

    /**
     * 处理超时工单数量
     */
    @ApiModelProperty(name = "overtimeCount" , value = "处理超时工单数量")
    private Integer overtimeCount;

    /**
     * 所有工单数量
     */
    @ApiModelProperty(name = "allCount" , value = "所有工单数量")
    private Integer allCount;

}
