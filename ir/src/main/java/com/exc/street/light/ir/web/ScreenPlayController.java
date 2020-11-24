/**
 * @filename:ScreenPlayController 2020-04-26
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.exc.street.light.ir.service.ScreenPlayService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.IrScreenPlayQuery;
import com.exc.street.light.resource.vo.req.IrReqScreenProgramVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-04-26
 *
 */
@Api(tags = "节目播放控制器", value = "节目播放控制器")
@RestController
@RequestMapping("/api/ir/screen/play")
public class ScreenPlayController {

    @Autowired
    private ScreenPlayService screenPlayService;

    /**
     * @explain 编辑正在播放节目
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "编辑正在播放节目", notes = "编辑正在播放节目,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:play:list:update")
    @SystemLog(logModul = "信息发布",logType = "编辑",logDesc = "编辑正在播放节目")
    public Result update(@ApiParam @RequestBody IrReqScreenProgramVO irReqScreenProgramVO, HttpServletRequest request) {
        return screenPlayService.updateScreenPlay(irReqScreenProgramVO, request);
    }

    /**
     * 分页查询播放中的节目列表
     *
     * @param irScreenPlayQuery
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "分页查询播放中的节目列表", notes = "分页查询播放中的节目列表,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:play:list")
    public Result list(IrScreenPlayQuery irScreenPlayQuery, HttpServletRequest httpServletRequest) {
        return screenPlayService.getQuery(irScreenPlayQuery, httpServletRequest);
    }

    /**
     * 取消播放
     *
     * @param id
     * @return
     */
    @DeleteMapping("/cancel/{id}")
    @ApiOperation(value = "取消播放", notes = "取消播放,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:play:list:play")
    @SystemLog(logModul = "信息发布",logType = "取消播放",logDesc = "取消播放")
    public Result cancel(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return screenPlayService.cancel(id, httpServletRequest);
    }

    /**
     * 获取播放详情
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取播放详情", notes = "获取播放详情,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:play:list")
    public Result getScreenPlay(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return screenPlayService.getScreenPlay(id, httpServletRequest);
    }

    /**
     * 播放节目列表数据刷新
     *
     * @return
     */
    @GetMapping("/refresh")
    @ApiOperation(value = "播放节目列表数据刷新", notes = "播放节目列表数据刷新,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:play:list")
    public Result refresh(HttpServletRequest request) {

        return screenPlayService.refresh(request);
    }


    /**
     * 播放节目列表的节目删除
     * @param  id  播放节目的Id
     * @return
     */

    @DeleteMapping("/delect/{id}")
    @ApiOperation(value = "删除播放列表的节目",notes = "删除播放列表的节目,作者：liuquan")
    @RequiresPermissions(value = "ir:module:program:play:list:delete")
    public Result delectProgram(@PathVariable("id") Integer id,HttpServletRequest httpServletRequest){
        return screenPlayService.deleteProgram(id, httpServletRequest);

    }

}