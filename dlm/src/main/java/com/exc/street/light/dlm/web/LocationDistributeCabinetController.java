/**
 * @filename:LocationDistributeCabinetController 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.LocationDistributeCabinetService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.DlmDistributeCabinetQuery;
import com.exc.street.light.resource.vo.req.DlmReqLocationDistributeCabinetVO;
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
@Api(tags = "配电柜")
@RestController
@RequestMapping("/api/dlm/location/distribute/cabinet")
public class LocationDistributeCabinetController {

    @Autowired
    private LocationDistributeCabinetService locationDistributeCabinetService;

    @PostMapping
    @ApiOperation(value = "添加配电柜", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "添加", logDesc = "添加配电柜")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:list:add", "dm:module:exc:controller:power:distribution:cabinet:add"}, logical = Logical.OR)
    public Result insertDistributeCabinet(@RequestBody DlmReqLocationDistributeCabinetVO cabinetVO, HttpServletRequest request) {
        return locationDistributeCabinetService.insertDistributeCabinet(cabinetVO, request);
    }

    @PutMapping
    @ApiOperation(value="编辑配电柜", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "编辑", logDesc = "编辑配电柜")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:list:update", "dm:module:exc:controller:power:distribution:cabinet:update"}, logical = Logical.OR)
    public Result updateDistributeCabinet(@RequestBody DlmReqLocationDistributeCabinetVO cabinetVO, HttpServletRequest request){
        return locationDistributeCabinetService.updateDistributeCabinet(cabinetVO, request);
    }

    @GetMapping("/{cabinetId}")
    @ApiOperation(value = "配电柜详情", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:list:detail", "dm:module:exc:controller:power:distribution:cabinet:detail"}, logical = Logical.OR)
    @ApiImplicitParam(paramType = "path", name = "cabinetId", value = "配电柜id", required = true, dataType = "Integer")
    public Result detailOfDistributeCabinet(@PathVariable("cabinetId") Integer cabinetId, HttpServletRequest request) {
        return locationDistributeCabinetService.detailOfDistributeCabinet(cabinetId,request);
    }

    @DeleteMapping("/{cabinetId}")
    @ApiOperation(value="删除配电柜", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "删除", logDesc = "删除配电柜")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:list:delete", "dm:module:exc:controller:power:distribution:cabinet:delete"}, logical = Logical.OR)
    @ApiImplicitParam(paramType = "path", name = "cabinetId", value = "配电柜id", required = true, dataType = "Integer")
    public Result deleteDistributeCabinetByCabinetId(@PathVariable(value = "cabinetId") Integer cabinetId, HttpServletRequest request) {
        return locationDistributeCabinetService.deleteDistributeCabinetByCabinetId(cabinetId, request);
    }

    @DeleteMapping("/batch/{cabinetIdList}")
    @ApiOperation(value="批量删除配电柜", notes = "作者：xiezhipeng")
    @SystemLog(logModul = "设备位置管理", logType = "批量删除", logDesc = "批量删除配电柜")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:list:delete", "dm:module:exc:controller:power:distribution:cabinet:delete"}, logical = Logical.OR)
    @ApiImplicitParam(paramType = "path", name = "cabinetIdList", value = "配电柜id集合,多个用逗号分隔", required = true)
    public Result deleteOfBatchDistributeCabinet(@PathVariable(value = "cabinetIdList") String[] cabinetIdList, HttpServletRequest request) {
        return locationDistributeCabinetService.deleteOfBatchDistributeCabinet(cabinetIdList, request);
    }

    @GetMapping("/page")
    @ApiOperation(value="分页查询配电柜列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:list:detail", "dm:module:exc:controller:power:distribution:cabinet:detail"}, logical = Logical.OR)
    public Result listDistributeCabinetWithPageByCabinetQuery(DlmDistributeCabinetQuery cabinetQuery, HttpServletRequest request) {
        return locationDistributeCabinetService.listDistributeCabinetWithPageByCabinetQuery(cabinetQuery, request);
    }

    @ApiOperation(value="配电柜下拉列表", notes = "作者：xiezhipeng")
    @GetMapping("/option")
    @RequiresPermissions(value = "view:data")
    public Result listDistributeCabinetWithOptionQuery(HttpServletRequest request) {
        return locationDistributeCabinetService.listDistributeCabinetWithOptionQuery(request);
    }

    @GetMapping("/name/num/uniqueness")
    @ApiOperation(value = "配电柜名称和编号唯一性校验", notes = "0:不存在 1：名称已存在 2:编号已存在;作者：xiezhipeng")
    @RequiresPermissions(value = {"dm:module:power:distribution:cabinet:list:add", "dm:module:exc:controller:power:distribution:cabinet:add"}, logical = Logical.OR)
    public Result nameAndNumUniqueness(@RequestParam(required = false) Integer id, @RequestParam("name") String name, @RequestParam("num") String num) {
        return locationDistributeCabinetService.nameAndNumUniqueness(id, name, num);
    }

}