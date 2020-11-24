/**
 * @filename:ScreenProgramController 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.web;

import javax.servlet.http.HttpServletRequest;

import com.exc.street.light.resource.vo.IrReqVerifyProgramVo;
import io.swagger.annotations.ApiModelProperty;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exc.street.light.ir.service.ScreenProgramService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.IrProgramQuery;
import com.exc.street.light.resource.vo.req.IrReqScreenProgramVO;
import com.exc.street.light.resource.vo.req.IrReqSendProgramVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>节目控制器</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-04-02
 *
 */
@Api(tags = "节目控制器", value = "节目控制器")
@RestController
@RequestMapping("/api/ir/screen/program")
public class ScreenProgramController {

    @Autowired
    private ScreenProgramService screenProgramService;

    /**
     * 添加节目
     *
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加节目", notes = "添加节目,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:add")
    @SystemLog(logModul = "信息发布",logType = "添加",logDesc = "添加节目")
    public Result add(@RequestBody IrReqScreenProgramVO screenProgram, HttpServletRequest httpServletRequest) {
        return screenProgramService.add(screenProgram, httpServletRequest);
    }

    /**
     * 获取节目详情
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取节目详情", notes = "获取节目详情,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:detail")
    public Result get(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        return screenProgramService.get(id, httpServletRequest);
    }

    /**
     * 条件分页查询节目列表
     *
     * @param irProgramQuery
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "条件分页查询节目列表", notes = "条件分页查询节目列表,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program")
    public Result list(IrProgramQuery irProgramQuery, HttpServletRequest httpServletRequest) {
        return screenProgramService.queryList(irProgramQuery, httpServletRequest);
    }

    /**
     * 修改节目
     *
     * @param irReqScreenProgramVO
     * @param httpServletRequest
     * @return
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改节目", notes = "修改节目,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:update")
    @SystemLog(logModul = "信息发布",logType = "修改",logDesc = "修改节目")
    public Result update(@RequestBody IrReqScreenProgramVO irReqScreenProgramVO, HttpServletRequest httpServletRequest) {
        return screenProgramService.updateProgram(irReqScreenProgramVO, httpServletRequest);
    }

    /**
     * 删除节目
     *
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    @ApiOperation(value = "删除节目", notes = "删除节目,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:delete")
    @SystemLog(logModul = "信息发布",logType = "删除",logDesc = "删除节目")
    public Result delete(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) {
        return screenProgramService.delete(id, httpServletRequest);
    }

    /**
     * 发布节目
     *
     * @param irReqSendProgramVO
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/send")
    @ApiOperation(value = "发布节目", notes = "发布节目,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:issue")
    @SystemLog(logModul = "信息发布",logType = "发布节目",logDesc = "发布节目")
    public Result sendProgram(@RequestBody IrReqSendProgramVO irReqSendProgramVO, HttpServletRequest httpServletRequest) {
        return screenProgramService.sendProgram(irReqSendProgramVO, httpServletRequest);
    }

    /**
     * 批量删除节目
     *
     * @param ids
     * @param request
     * @return
     */
    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除节目", notes = "批量删除节目,作者：Longshuangyang")
    @RequiresPermissions(value = "ir:module:program:delete")
    @SystemLog(logModul = "信息发布",logType = "批量删除",logDesc = "批量删除节目")
    public Result batchDelete(@RequestParam String ids, HttpServletRequest request) {
        return screenProgramService.batchDelete(ids, request);
    }

    @PostMapping("/vrifyProgramStatus")
    @ApiOperation(value = "节目的状态审核",notes = "修改节目的审核状态, 作者: liuquan")
    @RequiresPermissions(value = "ir:module:program:update")
    public Result verifyProgram(@RequestBody IrReqVerifyProgramVo irReqVerifyProgramVo,HttpServletRequest httpServletRequest){

        return screenProgramService.verifyProgram(irReqVerifyProgramVo,httpServletRequest);
    }

}