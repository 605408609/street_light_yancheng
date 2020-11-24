/**
 * @filename:ControlLoopController 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.ControlLoopService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.DlmControlLoopQuery;
import com.exc.street.light.resource.vo.req.DlmReqControlLoopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: xiezhipeng
 * @time    2020-08-24
 *
 */
@Api(tags = "集中控制器分组或回路")
@RestController
@RequestMapping("/api/dlm/control/loop")
public class ControlLoopController {

    @Autowired
    private ControlLoopService controlLoopService;

    @PostMapping
    @ApiOperation(value = "添加集控分组", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "添加", logDesc = "添加集控分组")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:group:add", "dm:module:power:distribution:cabinet:branch:add"}, logical = Logical.OR)
    public Result insertControlLoop(@RequestBody DlmReqControlLoopVO loopVO, HttpServletRequest request) {
        return controlLoopService.insertControlLoop(loopVO, request);
    }

    @PutMapping
    @ApiOperation(value="编辑集控分组", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "编辑", logDesc = "编辑集控分组")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:group:update", "ddm:module:power:distribution:cabinet:branch:update"}, logical = Logical.OR)
    public Result updateControlLoop(@RequestBody DlmReqControlLoopVO loopVO, HttpServletRequest request){
        return controlLoopService.updateControlLoop(loopVO, request);
    }

    @GetMapping("/{loopId}")
    @ApiOperation(value = "集控分组详情", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:group:detail", "dm:module:power:distribution:cabinet:branch:detail"}, logical = Logical.OR)
    @ApiImplicitParam(paramType = "path", name = "loopId", value = "集控分组id", required = true, dataType = "Integer")
    public Result detailOfControlLoop(@PathVariable("loopId") Integer loopId, HttpServletRequest request) {
        return controlLoopService.detailOfControlLoop(loopId, request);
    }

    @DeleteMapping("/{loopId}")
    @ApiOperation(value="删除集控分组", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "删除", logDesc = "删除集控分组")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:group:delete", "dm:module:power:distribution:cabinet:branch:delete"}, logical = Logical.OR)
    @ApiImplicitParam(paramType = "path", name = "controlId", value = "集控分组id", required = true, dataType = "Integer")
    public Result deleteControlLoopByLoopId(@PathVariable(value = "loopId") Integer loopId, HttpServletRequest request) {
        return controlLoopService.deleteControlLoopByLoopId(loopId, request);
    }

    @DeleteMapping("/batch/{loopIdList}")
    @ApiOperation(value="批量删除集控分组", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:group:delete", "dm:module:power:distribution:cabinet:branch:delete"}, logical = Logical.OR)
    @SystemLog(logModul = "设备位置管理", logType = "删除", logDesc = "批量删除集控分组")
    @ApiImplicitParam(paramType = "path", name = "loopIdList", value = "集控分组id集合,多个用逗号分隔", required = true)
    public Result deleteOfBatchControlLoop(@PathVariable(value = "loopIdList") String[] loopIdList, HttpServletRequest request) {
        return controlLoopService.deleteOfBatchControlLoop(loopIdList, request);
    }

    @GetMapping("/page")
    @ApiOperation(value="分页查询集控分组列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:group:detail", "dm:module:power:distribution:cabinet:branch:detail", "dm:module:power:distribution:cabinet:circuit:list"}, logical = Logical.OR)
    public Result listControlLoopWithPageByLoopQuery(DlmControlLoopQuery loopQuery, HttpServletRequest request) {
        return controlLoopService.listControlLoopWithPageByLoopQuery(loopQuery, request);
    }

    @ApiOperation(value="集控器下的分组下拉列表", notes = "作者：xiezhipeng")
    @GetMapping("/option")
    @RequiresPermissions(value = "view:data")
    public Result listControlLoopWithOptionQuery(@RequestParam("controlId") Integer controlId, HttpServletRequest request) {
        return controlLoopService.listControlLoopWithOptionQuery(controlId, request);
    }

    @GetMapping("/switch")
    @ApiOperation(value="回路开关控制", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:power:distribution:cabinet:circuit:list")
    @SystemLog(logModul = "设备位置管理", logType = "控制", logDesc = "回路开关控制")
    public Result updateControlLoopSwitch(@RequestParam("loopId") Integer loopId, @RequestParam("isOpen") Integer isOpen, HttpServletRequest request){
        return controlLoopService.updateControlLoopSwitch(loopId, isOpen, request);
    }

    @GetMapping("/by/group")
    @ApiOperation(value = "根据分组id集合查询回路信息", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "view:data")
    public Result getControlLoopByGroupIdList(@RequestParam(required = false) List<Integer> groupIdList, HttpServletRequest request) {
        return controlLoopService.getControlLoopByGroupIdList(groupIdList, request);
    }

    @GetMapping("/name/uniqueness")
    @ApiOperation(value = "分组名称唯一性校验", notes = "0:不存在 1：已存在 ;作者：xiezhipeng")
    @RequiresPermissions(value = "dm:module:power:distribution:cabinet:group:add")
    public Result nameUniqueness(@RequestParam(required = false) Integer id, @RequestParam("name") String name) {
        return controlLoopService.nameUniqueness(id, name);
    }

    @PostMapping("/uniqueness")
    @RequiresPermissions(value = "dm:module:power:distribution:cabinet:branch:add")
    @ApiOperation(value = "设备名称，通讯地址，序号唯一性校验", notes = "0:不存在 1：设备名称已存在 2：通讯地址已存在 3：序号已存在 ;作者：xiezhipeng")
    public Result uniqueness(@RequestBody DlmReqControlLoopVO loopVO) {
        return controlLoopService.uniqueness(loopVO);
    }

}