/**
 * @filename:OrderController 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.woa.OrderInfo;
import com.exc.street.light.resource.qo.WoaOrderQuery;
import com.exc.street.light.resource.vo.req.WoaReqOrderNewStatusVO;
import com.exc.street.light.resource.vo.req.WoaReqOrderNewVO;
import com.exc.street.light.woa.service.OrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>工单控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-28
 */
@Api(tags = "工单控制器", value = "工单控制器")
@RestController
@RequestMapping("/api/woa/order")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * @explain 首页工单概述数据
     * @author Longshuangyang
     * @time 2020-03-25
     */
    @GetMapping("/analysis")
    @ApiOperation(value = "首页工单概述数据", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result summary(HttpServletRequest request,@RequestParam(required = false) Integer frame) {
        return orderInfoService.summary(request,frame);
    }

    /**
     * @explain 新增工单数量
     * @author huangjinhao
     * @time 2020-05-29
     */
    @GetMapping("/addNum")
    @ApiOperation(value = "新增工单数量", notes = "作者：huangjinhao")
    @RequiresPermissions(value = "view:data")
//    @SystemLog(logModul = "工单告警",logType = "新增工单数量",logDesc = "新增工单数量")
    public Result addNum(HttpServletRequest request) {
        return orderInfoService.addNum(request);
    }

    /**
     * 新增工单
     *
     * @param request
     * @param woaReqOrderNewVO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增工单", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create:add", "permission:module:alarm:manage:add"}, logical = Logical.OR)
//    @SystemLog(logModul = "工单告警",logType = "新增工单",logDesc = "新增工单")
    public Result add(@RequestBody WoaReqOrderNewVO woaReqOrderNewVO, HttpServletRequest request) {
        return orderInfoService.add(request, woaReqOrderNewVO);
    }

    /**
     * 工单列表
     *
     * @param request
     * @param woaOrderQuery
     * @return
     */
    @GetMapping("/list/handle")
    @ApiOperation(value = "工单列表", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create", "permission:module:order:deal"}, logical = Logical.OR)
    public Result listHandle(WoaOrderQuery woaOrderQuery, HttpServletRequest request) {
        return orderInfoService.listHandle(request, woaOrderQuery);
    }

    /**
     * 查询工单列表（自动根据用户id筛选）
     *
     * @param request
     * @return
     */
    @GetMapping("/list/all")
    @ApiOperation(value = "查询工单列表（自动根据用户id筛选）", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create", "permission:module:order:deal"}, logical = Logical.OR)
    public Result listByUserId(WoaOrderQuery woaOrderQuery, HttpServletRequest request) {
        return orderInfoService.listByUserId(request,woaOrderQuery);
    }


    /**
     * 查看详情
     *
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    @ApiOperation(value = "查看详情", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create:detail", "permission:module:order:deal"}, logical = Logical.OR)
    public Result get(@PathVariable Integer id, HttpServletRequest request) {
        return orderInfoService.get(request, id);
    }

    /**
     * 编辑工单
     *
     * @param request
     * @param woaReqOrderNewVO
     * @return
     */
    @PutMapping("/update")
    @ApiOperation(value = "编辑工单", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create:update", "permission:module:order:deal"}, logical = Logical.OR)
    @SystemLog(logModul = "工单告警",logType = "编辑",logDesc = "编辑工单")
    public Result update(@RequestBody WoaReqOrderNewVO woaReqOrderNewVO, HttpServletRequest request) {
        return orderInfoService.updateOrder(request, woaReqOrderNewVO);
    }

    /**
     * 处理
     *
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/handle/{id}")
    @ApiOperation(value = "处理", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create:update", "permission:module:order:deal"}, logical = Logical.OR)
    @SystemLog(logModul = "工单告警",logType = "处理",logDesc = "处理")
    public Result handle(@PathVariable Integer id, HttpServletRequest request) {
        return orderInfoService.handle(request, id);
    }

    /**
     * 处理完成进入待审核
     *
     * @param woaReqOrderNewVO
     * @param request
     * @return
     */
    @PostMapping("/complete")
    @ApiOperation(value = "处理完成进入待审核", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create", "permission:module:order:deal"}, logical = Logical.OR)
    @SystemLog(logModul = "工单告警",logType = "处理完成进入待审核",logDesc = "处理完成进入待审核")
    public Result complete(@RequestBody WoaReqOrderNewVO woaReqOrderNewVO, HttpServletRequest request) {
        return orderInfoService.complete(request, woaReqOrderNewVO);
    }

    /**
     * 初审
     *
     * @param request
     * @param woaReqOrderNewVO
     * @return
     */
    @PostMapping("/first/trial")
    @ApiOperation(value = "初审", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:order:create:check")
    @SystemLog(logModul = "工单告警",logType = "初审",logDesc = "初审")
    public Result firstTrial(HttpServletRequest request, @RequestBody WoaReqOrderNewVO woaReqOrderNewVO) {
        return orderInfoService.firstTrial(request, woaReqOrderNewVO);
    }

    /**
     * 审核
     *
     * @param request
     * @param woaReqOrderNewVO
     * @return
     */
    @PostMapping("/second/trial")
    @ApiOperation(value = "审核", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "permission:module:order:create:check")
    @SystemLog(logModul = "工单告警",logType = "审核",logDesc = "审核")
    public Result secondTrial(HttpServletRequest request, @RequestBody WoaReqOrderNewVO woaReqOrderNewVO) {
        return orderInfoService.secondTrial(request, woaReqOrderNewVO);
    }

    /**
     * 获取各状态的新工单
     *
     * @param request
     * @return
     */
    @GetMapping("/new/status")
    @ApiOperation(value = "获取各状态的新工单", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create", "permission:module:order:deal"}, logical = Logical.OR)
    public Result newStatus(WoaReqOrderNewStatusVO woaReqOrderNewStatusVO, HttpServletRequest request) {
        return orderInfoService.newStatus(woaReqOrderNewStatusVO, request);
    }

    /**
     * @explain 工单验证唯一性
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/unique")
    @ApiOperation(value = "工单验证唯一性", notes = "工单验证唯一性,作者：Longshuangyang")
    @RequiresPermissions(value = {"permission:module:order:create:add", "permission:module:alarm:manage:add"}, logical = Logical.OR)
    @SystemLog(logModul = "工单告警",logType = "验证唯一性",logDesc = "工单验证唯一性")
    public Result unique(@ApiParam OrderInfo orderInfo, HttpServletRequest request) {
        return orderInfoService.unique(orderInfo, request);
    }

}