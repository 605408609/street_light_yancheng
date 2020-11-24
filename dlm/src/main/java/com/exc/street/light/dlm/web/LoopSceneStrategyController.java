/**
 * @filename:LoopSceneStrategyController 2020-11-07
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.LoopSceneStrategyService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.DlmSceneStrategyQuery;
import com.exc.street.light.resource.vo.req.DlmReqSceneStrategyExecuteVO;
import com.exc.street.light.resource.vo.req.DlmReqSceneStrategyVO;
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
 * @time    2020-11-07
 *
 */
@Api(tags = "回路场景策略控制器")
@RestController
@RequestMapping("/api/dlm/loop/scene/strategy")
public class LoopSceneStrategyController {

    @Autowired
    private LoopSceneStrategyService loopSceneStrategyService;

    @PostMapping
    @ApiOperation(value = "新增场景策略", notes = "新增场景策略,作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:exc:controller:control:loop:scene:set")
    @SystemLog(logModul = "位置管理", logType = "新增", logDesc = "新增场景策略")
    public Result insertLampStrategy(@ApiParam @RequestBody DlmReqSceneStrategyVO dlmReqSceneStrategyVO, HttpServletRequest request) {
        return loopSceneStrategyService.insertSceneStrategy(dlmReqSceneStrategyVO, request);
    }

    @PutMapping
    @ApiOperation(value = "编辑场景策略", notes = "编辑场景策略,作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:exc:controller:control:loop:scene:set")
    @SystemLog(logModul = "位置管理", logType = "编辑", logDesc = "编辑场景策略")
    public Result updateLampStrategy(@ApiParam @RequestBody DlmReqSceneStrategyVO dlmReqSceneStrategyVO, HttpServletRequest request) {
        return loopSceneStrategyService.updateSceneStrategy(dlmReqSceneStrategyVO, request);
    }

    @GetMapping("/{strategyId}")
    @ApiOperation(value = "场景策略详情", notes = "场景策略详情,作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:exc:controller:control:loop:scene:set")
    public Result getLampStrategy(@PathVariable("strategyId") Integer strategyId, HttpServletRequest request) {
        return loopSceneStrategyService.getSceneStrategy(strategyId, request);
    }

    @DeleteMapping("/{strategyId}")
    @ApiOperation(value = "删除场景策略", notes = "删除场景策略,作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:exc:controller:control:loop:scene:set")
    @SystemLog(logModul = "位置管理", logType = "删除", logDesc = "删除场景策略")
    public Result deleteLampStrategy(@PathVariable("strategyId") Integer strategyId, HttpServletRequest request) {
        return loopSceneStrategyService.deleteSceneStrategy(strategyId, request);
    }

    @GetMapping("/page")
    @ApiOperation(value = "场景策略分页条件查询", notes = "场景策略分页条件查询,作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:exc:controller:control:loop:scene:set")
    public Result getPage(DlmSceneStrategyQuery dlmSceneStrategyQuery, HttpServletRequest request) {
        return loopSceneStrategyService.getPage(dlmSceneStrategyQuery, request);
    }

    @PostMapping("/execute")
    @ApiOperation(value = "下发场景策略", notes = "下发策略,作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:exc:controller:control:loop:scene:issue")
    @SystemLog(logModul = "位置管理", logType = "策略下发", logDesc = "下发场景策略")
    public Result execute(HttpServletRequest request, @ApiParam @RequestBody DlmReqSceneStrategyExecuteVO dlmReqSceneStrategyExecuteVO) {
        return loopSceneStrategyService.execute(request, dlmReqSceneStrategyExecuteVO);
    }

}