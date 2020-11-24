package com.exc.street.light.resource.vo.req.htLamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: Xiaok
 * @Date: 2020/9/2 19:59
 */
@Data
@ApiModel("")
public class HtSetLocationControlLoopPlanRequestVO {

    @ApiModelProperty("华体集中控制器ID")
    private Integer locationControlId;

    @ApiModelProperty("华体集中控制器通讯地址（编号）")
    private String locationControlAddr;

    @ApiModelProperty("计划列表")
    private List<HtSetControlPlanRequestVO> planList;


}
