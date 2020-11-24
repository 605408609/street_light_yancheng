/**
 * @filename:WifiApController 2020-03-12
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.WifiChartStatisticQueryObject;
import com.exc.street.light.wifi.qo.ApQueryObject;
import com.exc.street.light.wifi.service.WifiApService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time    2020-03-12
 *
 */
@Api(tags = "ap接口")
@RestController
@RequestMapping("/api/wifi/wifi/ap")
public class WifiApController {

    @Autowired
    private WifiApService wifiApService;

    /**
     * AP统计列表
     * @param request
     * @param queryObject
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "AP统计列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "wifi:module:ap:statistical:list")
    public Result list(HttpServletRequest request, ApQueryObject queryObject) {
        return wifiApService.getList(request, queryObject);
    }

    /**
     * wifi报表统计
     * @param request
     * @param queryObject
     * @return
     */
    @GetMapping("/chart/statistic")
    @ApiOperation(value = "wifi报表统计", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "wifi:module:statistical:list")
    public Result chartStatistic(HttpServletRequest request, WifiChartStatisticQueryObject queryObject) {
        return wifiApService.chartStatistic(request, queryObject);
    }

}