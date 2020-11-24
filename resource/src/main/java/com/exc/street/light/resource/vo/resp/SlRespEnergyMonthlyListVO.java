package com.exc.street.light.resource.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 月度能耗返回参数
 *
 * @author Longshuangyang
 * @date 2020/03/23
 */
@Setter
@Getter
@ToString
public class SlRespEnergyMonthlyListVO {

    /**
     * 本周能耗
     */
    @ApiModelProperty(name = "weekEnergy" , value = "本周能耗")
    private float weekEnergy;
    /**
     * 上周能耗
     */
    @ApiModelProperty(name = "lastWeekEnergy" , value = "上周能耗")
    private float lastWeekEnergy;
    /**
     * 本月能耗
     */
    @ApiModelProperty(name = "monthEnergy" , value = "本月能耗")
    private float monthEnergy;
    /**
     * 上月能耗
     */
    @ApiModelProperty(name = "lastMonthEnergy" , value = "上月能耗")
    private float lastMonthEnergy;


    /**
     * 年月格式字符串集合
     */
    @ApiModelProperty(name = "yearMonthList" , value = "年月格式字符串集合")
    private List<String> yearMonthList;

    /**
     * 能耗集合
     */
    @ApiModelProperty(name = "energyList" , value = "能耗集合")
    private List<Float> energyList;

    /**
     * 年度累计能耗
     */
    @ApiModelProperty(name = "yearEnery" , value = "年度累计能耗")
    private float yearEnery;

}
