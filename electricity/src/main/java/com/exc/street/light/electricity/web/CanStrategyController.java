/**
 * @filename:CanStrategyController 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.electricity.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.electricity.service.CanStrategyService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.electricity.CanStrategyQueryObject;
import com.exc.street.light.resource.vo.electricity.RespCanStrategyVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanChannelControlVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyIssueVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyUniquenessVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： 路灯网关策略表Controller</P>
 *
 * @version: V1.0
 * @author: Xiaok
 * @time 2020-11-18
 */
@Api(tags = "路灯网关策略表", value = "路灯网关策略表")
@RestController
@RequestMapping("/api/electricity/can/strategy")
public class CanStrategyController {
    @Autowired
    private CanStrategyService service;

    @PostMapping
    @ApiOperation(value = "添加策略", notes = "添加路灯网关策略")
    @RequiresPermissions("dm:module:same:device:the:gateway:channel:strategy:add")
    public Result<String> add(@RequestBody ReqCanStrategyVO reqVo, HttpServletRequest request) {
        return service.add(reqVo, request);
    }

    @PostMapping("/uniqueness")
    @ApiOperation(value = "唯一性校验", notes = "唯一性校验")
    @RequiresPermissions("dm:module:same:device:the:gateway:channel:strategy:add")
    public Result<Integer> uniqueness(@RequestBody ReqCanStrategyUniquenessVO reqVo, HttpServletRequest request) {
        return service.uniqueness(reqVo, request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除策略", notes = "删除单个策略")
    @RequiresPermissions("dm:module:same:device:the:gateway:channel:strategy:delete")
    public Result<String> delete(@PathVariable("id") Integer id, HttpServletRequest request) {
        return service.deleteById(id, request);
    }

    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除策略", notes = "批量删除策略")
    @RequiresPermissions("dm:module:same:device:the:gateway:channel:strategy:delete")
    public Result<String> batchDelete(String ids, HttpServletRequest request) {
        return service.batchDelete(ids, request);
    }

    @PutMapping
    @ApiOperation(value = "修改策略", notes = "修改路灯网关策略")
    @RequiresPermissions("dm:module:same:device:the:gateway:channel:strategy:update")
    public Result<String> update(@RequestBody ReqCanStrategyVO reqVo, HttpServletRequest request) {
        return service.update(reqVo, request);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询策略", notes = "分页查询策略")
    @RequiresPermissions("dm:module:same:device:the:gateway:channel:strategy:add")
    public Result<IPage<RespCanStrategyVO>> getPageList(CanStrategyQueryObject qo, HttpServletRequest request) {
        return service.getPageList(qo, request);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取策略详情", notes = "获取策略详情")
    @RequiresPermissions("dm:module:same:device:the:gateway:channel:strategy:detail")
    public Result<RespCanStrategyVO> get(@PathVariable("id") Integer id, HttpServletRequest request) {
        return service.getInfoById(id, request);
    }

    @PatchMapping("/issue")
    @ApiOperation(value = "下发策略", notes = "下发策略给回路")
    @RequiresPermissions("dm:module:same:device:the:gateway:channel:strategy:issue")
    public Result<JSONObject> issue(@RequestBody ReqCanStrategyIssueVO reqVo, HttpServletRequest request) {
        return service.issue(reqVo, request);
    }


}