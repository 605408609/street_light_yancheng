/**
 * @filename:LocationControlController 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.LocationControlService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.DlmLocationControlQuery;
import com.exc.street.light.resource.vo.req.DlmReqLocationControlVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
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
 * @time 2020-08-22
 *
 */
@Api(tags = "集中控制器")
@RestController
@RequestMapping("/api/dlm/location/control")
public class LocationControlController {

    @Autowired
    private LocationControlService locationControlService;

    @PostMapping
    @ApiOperation(value = "添加集中控制器", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "添加", logDesc = "添加集中控制器")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:controller:add", "dm:module:exc:controller:control:equipment:add"}, logical = Logical.OR)
    public Result insertLocationControl(@RequestBody DlmReqLocationControlVO controlVO, HttpServletRequest request) {
        return locationControlService.insertLocationControl(controlVO, request);
    }

    @PutMapping
    @ApiOperation(value = "编辑集中控制器", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "编辑", logDesc = "编辑集中控制器")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:controller:update", "dm:module:exc:controller:control:equipment:update"}, logical = Logical.OR)
    public Result updateLocationControl(@RequestBody DlmReqLocationControlVO controlVO, HttpServletRequest request) {
        return locationControlService.updateLocationControl(controlVO, request);
    }

    @GetMapping("/{controlId}")
    @ApiOperation(value = "集中控制器详情", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:controller:detail", "dm:module:exc:controller:control:equipment:detail"}, logical = Logical.OR)
    @ApiImplicitParam(paramType = "path", name = "controlId", value = "集中控制器id", required = true, dataType = "Integer")
    public Result detailOfLocationControl(@PathVariable("controlId") Integer controlId, HttpServletRequest request) {
        return locationControlService.detailOfLocationControl(controlId, request);
    }

    @GetMapping("/mix/{controlId}")
    @ApiOperation(value = "集中控制器混合信息", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "view:data")
    @ApiImplicitParam(paramType = "path", name = "controlId", value = "集中控制器id", required = true, dataType = "Integer")
    public Result detailOfMixLocationControl(@PathVariable("controlId") Integer controlId, HttpServletRequest request) {
        return locationControlService.detailOfMixLocationControl(controlId, request);
    }

    @DeleteMapping("/{controlId}")
    @ApiOperation(value = "删除集中控制器", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "删除", logDesc = "删除集中控制器")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:controller:delete", "dm:module:exc:controller:control:equipment:delete"}, logical = Logical.OR)
    @ApiImplicitParam(paramType = "path", name = "controlId", value = "集中控制器id", required = true, dataType = "Integer")
    public Result deleteLocationControlByControlId(@PathVariable(value = "controlId") Integer controlId, HttpServletRequest request) {
        return locationControlService.deleteLocationControlByControlId(controlId, request);
    }

    @DeleteMapping("/batch/{controlIdList}")
    @ApiOperation(value = "批量删除集中控制器", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "删除", logDesc = "批量删除集中控制器")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:controller:delete", "dm:module:exc:controller:control:equipment:delete"}, logical = Logical.OR)
    @ApiImplicitParam(paramType = "path", name = "controlIdList", value = "集中控制器id集合,多个用逗号分隔", required = true)
    public Result deleteOfBatchLocationControl(@PathVariable(value = "controlIdList") String[] controlIdList, HttpServletRequest request) {
        return locationControlService.deleteOfBatchLocationControl(controlIdList, request);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询集中控制器列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:controller:detail", "dm:module:exc:controller:control:equipment:detail"}, logical = Logical.OR)
    public Result listLocationControlWithPageByControlQuery(DlmLocationControlQuery controlQuery, HttpServletRequest request) {
        return locationControlService.listLocationControlWithPageByControlQuery(controlQuery, request);
    }

    @ApiOperation(value = "集中控制器下拉列表(中科智联)", notes = "作者：xiezhipeng")
    @GetMapping("/option")
    @RequiresPermissions(value = "view:data")
    public Result listLocationControlWithOptionQuery(HttpServletRequest request) {
        return locationControlService.listLocationControlWithOptionQuery(request);
    }

    @ApiOperation(value = "集中控制器下拉列表(华体)", notes = "作者：xiezhipeng")
    @GetMapping("/option/ht")
    @RequiresPermissions(value = "view:data")
    public Result listLocationControlOfHtWithOptionQuery(HttpServletRequest request) {
        return locationControlService.listLocationControlOfHtWithOptionQuery(request);
    }

    @ApiOperation(value = "集中控制器下拉列表(爱克)", notes = "作者：xiezhipeng")
    @GetMapping("/option/exc")
    @RequiresPermissions(value = "view:data")
    public Result listLocationControlOfExcWithOptionQuery(HttpServletRequest request) {
        return locationControlService.listLocationControlOfExcWithOptionQuery(request);
    }

    @GetMapping("/name/num/uniqueness")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:controller:add", "dm:module:exc:controller:control:equipment:add"}, logical = Logical.OR)
    @ApiOperation(value = "集中控制器名称和编号唯一性校验", notes = "0:不存在 1:名称已存在 2:编号已存在;作者：xiezhipeng")
    public Result nameAndNumUniqueness(@RequestParam(required = false) Integer id, @RequestParam("name") String name, @RequestParam("num") String num) {
        return locationControlService.nameAndNumUniqueness(id, name, num);
    }

}