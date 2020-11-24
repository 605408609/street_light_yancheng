/**
 * @filename:LampEnergyDayController 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.service.LampEnergyDayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>灯具每日能耗控制器</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-28
 *
 */
@Api(tags = "灯具每日能耗控制器", value = "灯具每日能耗控制器")
@RestController
@RequestMapping("/api/sl/lamp/energy/day")
public class LampEnergyDayController{

    @Autowired
    LampEnergyDayService lampEnergyDayService;
}