/**
 * @filename:LampEnergyController 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.req.SlReqEnergyStatisticslVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamReqVO;
import com.exc.street.light.sl.service.LampEnergyDayService;
import com.exc.street.light.sl.service.LampEnergyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>灯具能耗控制器</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-03-20
 *
 */
@Api(tags = "灯具能耗控制器",value="灯具能耗控制器" )
@RestController
@RequestMapping("/api/sl/lamp/energy")
public class LampEnergyController {

    @Autowired
    private LampEnergyService lampEnergyService;
    @Autowired
    private LampEnergyDayService lampEnergyDayService;

    /**
     * @explain 指定时间区间获取能耗和亮灯率（用于统计报表）
     * @author huangjinhao
     * @time 2020-03-25
     */
    @PostMapping("/energy")
    @ApiOperation(value = "获取能耗数据和亮灯率", notes = "作者：huangjinhao")
    @RequiresPermissions(value = {"sl:module:statistics:light:rate", "sl:module:statistics:energy:consumption"}, logical = Logical.OR)
    public Result energy(@RequestBody SlReqEnergyStatisticslVO slReqEnergyStatisticslVO, HttpServletRequest request) {
        return lampEnergyDayService.energy(slReqEnergyStatisticslVO, request);
    }

    /**
     * @explain 统计能耗相关数据（用于app首页）
     * @author  huangjinhao
     * @time    2020-03-25
     */
    @GetMapping("/energyInformation")
    @ApiOperation(value = "统计能耗相关数据", notes = "作者：huangjinhao")
    @RequiresPermissions(value = "view:data")
    public Result energyInformation(HttpServletRequest request){
        return lampEnergyService.energyInformation(request);
    }

    /**
     * @explain 获取近七天的能耗（用于app首页）
     * @author huangjinhao
     * @time 2020-03-25
     */
    @GetMapping("/weekEnergy")
    @ApiOperation(value = "获取近七天的能耗", notes = "作者：huangjinhao")
    @RequiresPermissions(value = "view:data")
    public Result weekEnergy(HttpServletRequest request) {
        return lampEnergyService.weekEnergy(request);
    }

    /**
     * @explain 获取近七天的亮灯率（用于pc首页）
     * @author huangjinhao
     * @time 2020-03-25
     */
    @GetMapping("/weekLightingRate")
    @ApiOperation(value = "获取近七天的亮灯率", notes = "作者：huangjinhao")
    @RequiresPermissions(value = "view:data")
    public Result weekLightingRate(HttpServletRequest request) {
        return lampEnergyService.weekLightingRate(request);
    }
}