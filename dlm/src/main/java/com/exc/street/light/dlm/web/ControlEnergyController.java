/**
 * @filename:ControlEnergyController 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.ControlEnergyDayService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.req.DlmReqControlEnergyStatisticVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time    2020-10-27
 *
 */
@Api(tags = "集中控制器实时能耗控制器")
@RestController
@RequestMapping("/api/dlm/control/energy")
public class ControlEnergyController {

    @Autowired
    private ControlEnergyDayService controlEnergyDayService;

    @PostMapping("/statistic")
    @ApiOperation(value = "获取集控能耗统计报表数据", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:statistics:energy:consumption")
    public Result energyStatistic(@RequestBody DlmReqControlEnergyStatisticVO energyStatisticVO, HttpServletRequest request) {
        return controlEnergyDayService.energyStatistic(energyStatisticVO, request);
    }

}