/**
 * @filename:LampEnergyMonthController 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.service.LampEnergyMonthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>灯具每月能耗控制器</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-03-28
 *
 */
@Api(tags = "灯具每月能耗控制器",value="灯具每月能耗控制器" )
@RestController
@RequestMapping("/api/sl/lamp/energy/month")
public class LampEnergyMonthController{

    @Autowired
    private LampEnergyMonthService lampEnergyMonthService;

    /**
     * @explain 首页月度能耗数据（pc和app首页）
     * @author  Longshuangyang
     * @time    2020-03-25
     */
    @GetMapping("/analysis")
    @ApiOperation(value = "首页月度能耗数据", notes = "作者：Huangjinhao")
    @RequiresPermissions(value = "view:data")
    public Result monthly(HttpServletRequest request){
        return lampEnergyMonthService.monthly(request);
    }
}