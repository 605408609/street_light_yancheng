/**
 * @filename:LampStrategyActionHistoryController 2020-03-23
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.req.SlReqStrategyActionVO;
import com.exc.street.light.sl.service.LampStrategyActionHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>灯具策略动作历史控制器</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-23
 *
 */
@Api(tags = "灯具策略动作历史控制器", value = "灯具策略动作历史控制器")
@RestController
@RequestMapping("/api/sl/lamp/strategy/action/history")
public class LampStrategyActionHistoryController {

    @Autowired
    private LampStrategyActionHistoryService lampStrategyActionHistoryService;

    /**
     * @explain 查询策略历史动作列表
     * @author Longshuangyang
     * @time 2020-03-23
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询策略历史动作列表", notes = "查询策略历史动作列表,作者：Longshuangyang")
    @RequiresPermissions(value = "sl:module:strategy:manage")
    public Result getList(@RequestParam(required = false) Integer lampStrategyTypeId, HttpServletRequest request) {
        return lampStrategyActionHistoryService.getList(lampStrategyTypeId, request);
    }

    /**
     * @explain 添加策略历史动作
     * @author Longshuangyang
     * @time 2020-03-23
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加策略历史动作", notes = "添加策略历史动作,作者：Longshuangyang")
    @RequiresPermissions(value = "sl:module:strategy:manage")
    @SystemLog(logModul = "智慧照明",logType = "新增",logDesc = "添加策略历史动作")
    public Result add(@RequestBody List<SlReqStrategyActionVO> slReqStrategyActionVOList, HttpServletRequest request) {
        return lampStrategyActionHistoryService.add(slReqStrategyActionVOList, request);
    }

    /**
     * @explain 获取策略历史动作详情
     * @author Longshuangyang
     * @time 2020-03-23
     */
    @GetMapping("/detail/{strategyTypeId}")
    @ApiOperation(value = "获取策略历史动作详情", notes = "获取策略历史动作详情,作者：Longshuangyang")
    @RequiresPermissions(value = "sl:module:strategy:manage")
    public Result detail(@PathVariable("strategyTypeId") Integer strategyTypeId, HttpServletRequest request) {
        return lampStrategyActionHistoryService.detail(strategyTypeId, request);
    }
}