/**
 * @filename:LampStrategyController 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.SlLampStrategyQuery;
import com.exc.street.light.resource.vo.req.SlReqLampStrategyExecuteVO;
import com.exc.street.light.resource.vo.req.SlReqStrategyVO;
import com.exc.street.light.sl.service.LampStrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time 2020-08-26
 *
 */
@Api(tags = "策略控制器")
@RestController
@RequestMapping("/api/sl/lamp/strategy")
public class LampStrategyController {

    @Autowired
    private LampStrategyService lampStrategyService;

    @PostMapping
    @ApiOperation(value = "新增策略", notes = "新增策略,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:strategy:manage:add")
    @SystemLog(logModul = "智慧照明", logType = "新增", logDesc = "新增策略")
    public Result insertLampStrategy(@ApiParam @RequestBody SlReqStrategyVO slReqStrategyVO, HttpServletRequest request) {
        return lampStrategyService.insertLampStrategy(slReqStrategyVO, request);
    }

    @PutMapping
    @ApiOperation(value = "编辑策略", notes = "编辑策略,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:strategy:manage:update")
    @SystemLog(logModul = "智慧照明", logType = "编辑", logDesc = "编辑策略")
    public Result updateLampStrategy(@ApiParam @RequestBody SlReqStrategyVO slReqStrategyVO, HttpServletRequest request) {
        return lampStrategyService.updateLampStrategy(slReqStrategyVO, request);
    }

    @GetMapping("/{strategyId}")
    @ApiOperation(value = "策略详情", notes = "策略详情,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:strategy:manage:detail")
    public Result getLampStrategy(@PathVariable("strategyId") Integer strategyId, HttpServletRequest request) {
        return lampStrategyService.getLampStrategy(strategyId, request);
    }

    @DeleteMapping("/{strategyId}")
    @ApiOperation(value = "删除策略", notes = "删除策略,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:strategy:manage:delete")
    @SystemLog(logModul = "智慧照明", logType = "删除", logDesc = "删除策略")
    public Result deleteLampStrategy(@PathVariable("strategyId") Integer strategyId, HttpServletRequest request) {
        return lampStrategyService.deleteLampStrategy(strategyId, request);
    }

    @GetMapping("/page")
    @ApiOperation(value = "灯控策略分页条件查询", notes = "灯控策略分页条件查询,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:strategy:manage")
    public Result getPage(SlLampStrategyQuery slLampStrategyQuery, HttpServletRequest request) {
        return lampStrategyService.getPage(slLampStrategyQuery, request);
    }

    @PostMapping("/execute")
    @ApiOperation(value = "下发策略", notes = "下发策略,作者：xiezhipeng")
    @RequiresPermissions(value = "sl:module:light:strategy:control")
    @SystemLog(logModul = "智慧照明", logType = "控制", logDesc = "下发策略")
    public Result execute(HttpServletRequest request, @ApiParam @RequestBody SlReqLampStrategyExecuteVO slReqLampStrategyExecuteVO) {
        return lampStrategyService.execute(request, slReqLampStrategyExecuteVO);
    }

}