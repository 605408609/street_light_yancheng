/**
 * @filename:RadioProgramController 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.pb.service.RadioProgramService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.RadioProgram;
import com.exc.street.light.resource.qo.PbRadioProgramQueryObject;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.req.PbReqRadioProgramVO;
import com.exc.street.light.resource.vo.req.PbReqVerifyProgramVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: LeiJing
 * @time 2020-03-21
 */
@Api(tags = "广播节目", value = "")
@RestController
@RequestMapping("/api/pb/radio/program")
public class RadioProgramController {
    @Autowired
    private RadioProgramService radioProgramService;

    @PostMapping
    @ApiOperation(value = "新增公共广播节目", notes = "新增公共广播节目,作者：LeiJing")
    @RequiresPermissions(value = "pb:module:audio:program:add")
    @SystemLog(logModul = "公共广播",logType = "新增",logDesc = "新增公共广播节目")
    public Result addProgram(@ApiParam @RequestBody PbReqRadioProgramVO pbReqRadioProgramVO, HttpServletRequest request) {
        return radioProgramService.add(pbReqRadioProgramVO, request);
    }

    @PutMapping
    @ApiOperation(value = "编辑公共广播节目", notes = "编辑公共广播节目,作者：LeiJing")
    @RequiresPermissions(value = "pb:module:audio:program:update")
    @SystemLog(logModul = "公共广播",logType = "编辑",logDesc = "编辑公共广播节目")
    public Result updateProgram(@ApiParam @RequestBody PbReqRadioProgramVO pbReqRadioProgramVO, HttpServletRequest request) {
        return radioProgramService.update(pbReqRadioProgramVO, request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除公共广播节目", notes = "删除公共广播节目,作者：LeiJing")
    @RequiresPermissions(value = "dm:module:same:device:broadcast:delete")
    @SystemLog(logModul = "公共广播",logType = "删除",logDesc = "删除公共广播节目")
    public Result deleteProgram(@PathVariable Integer id, HttpServletRequest request) {
        return radioProgramService.delete(id, request);
    }

    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除公共广播节目", notes = "批量删除公共广播节目,作者：LeiJing")
    @RequiresPermissions(value = "pb:module:audio:program:delete")
    @SystemLog(logModul = "公共广播",logType = "删除",logDesc = "批量删除公共广播节目")
    public Result batchDeleteProgram(@RequestParam String ids, HttpServletRequest request) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);
        return radioProgramService.batchDelete(idList, request);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取公共广播节目详细信息", notes = "获取公共广播节目详细信息,作者：LeiJing")
    @RequiresPermissions(value = "pb:module:audio:program:detail")
    public Result getProgramInfo(@PathVariable Integer id, HttpServletRequest request) {
        return radioProgramService.getInfo(id, request);
    }

    @GetMapping("/getList")
    @ApiOperation(value = "获取公共广播节目列表", notes = "获取公共广播节目列表,作者：LeiJing")
    @RequiresPermissions(value = "pb:module:audio:program")
    public Result getProgramList(PbRadioProgramQueryObject qo, HttpServletRequest request) {
        return radioProgramService.getList(qo, request);
    }

    @PostMapping("/uniqueness")
    @ApiOperation(value = "公共广播节目唯一性校验", notes = "公共广播节目唯一性校验,作者：Xiaokun")
    @RequiresPermissions(value = "pb:module:audio:program")
    public Result uniqueness(@RequestBody RadioProgram radioProgram, HttpServletRequest request) {
        return radioProgramService.uniqueness(radioProgram, request);
    }

    @PostMapping("/verifyProgram")
    @ApiOperation(value = "公共广播节目审核", notes = "公共广播节目审核,作者：Xiaokun")
    @RequiresPermissions(value = "pb:module:audio:program:verify")
    public Result verifyProgram(@RequestBody PbReqVerifyProgramVO reqVO, HttpServletRequest request) {
        return radioProgramService.verifyProgram(reqVO, request);
    }
}