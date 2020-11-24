package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 月度能耗返回参数
 *
 * @author Longshuangyang
 * @date 2020/03/23
 */
@Setter
@Getter
@ToString
public class SlRespEnergyMonthlyVO {

    /**
     * 年月格式字符串
     */
    @ApiModelProperty(name = "yearMonth" , value = "年月格式字符串")
    private String yearMonth;

    /**
     * 能耗
     */
    @ApiModelProperty(name = "energy" , value = "能耗")
    private Float energy;
}
