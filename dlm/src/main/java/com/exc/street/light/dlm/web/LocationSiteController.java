/**
 * @filename:LocationSiteController 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.exc.street.light.dlm.service.LocationSiteService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationSite;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>站点控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: Longshuangyang
 * @time 2020-03-16
 */
@Api(tags = "站点控制器", value = "站点控制器")
@RestController
@RequestMapping("/api/dlm/location/site")
public class LocationSiteController {

    @Autowired
    private LocationSiteService locationSiteService;

    /**
     * @explain 查询站点下拉列表
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/pulldown")
    @ApiOperation(value = "查询站点下拉列表", notes = "查询站点下拉列表,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result pulldown(@RequestParam(required = false) Integer streetId,
                           @RequestParam(required = false) String siteName,
                           HttpServletRequest request) {
        return locationSiteService.pulldown(streetId, siteName, request);
    }

    /**
     * @explain 添加站点
     * @author  Longshuangyang
     * @time    2020-03-25
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加站点", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location:site:add")
    @SystemLog(logModul = "设备位置管理",logType = "添加",logDesc = "添加站点")
    public Result insert(@ApiParam @RequestBody LocationSite locationSite, HttpServletRequest request){
        return locationSiteService.add(locationSite,request);
    }

    /**
     * @explain 修改站点
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改站点", notes = "修改站点,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location:site:update")
    @SystemLog(logModul = "设备位置管理",logType = "修改",logDesc = "修改站点")
    public Result updateSite(@ApiParam @RequestBody LocationSite locationSite, HttpServletRequest request) {
        return locationSiteService.updateSite(locationSite, request);
    }

    /**
     * @explain 根据街道id集合查询站点集合
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/by/street")
    @ApiOperation(value = "根据街道id集合查询站点集合", notes = "根据街道id集合查询站点集合,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result getByStreet(@RequestParam(required = false) List<Integer> streetIdList,
                            HttpServletRequest request) {
        return locationSiteService.getByStreet(streetIdList,request);
    }

    /**
     * @explain 删除站点
     * @author  Longshuangyang
     * @time    2020-03-16
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除站点", notes = "作者：Longshuangyang")
    @ApiImplicitParam(paramType="path", name = "id", value = "站点id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:location:site:delete")
    @SystemLog(logModul = "设备位置管理",logType = "删除",logDesc = "删除站点")
    public Result deleteById(@PathVariable("id") Long id,  HttpServletRequest request){
        return locationSiteService.delete(id, request);
    }

    /**
     * @explain 站点验证唯一性
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/unique")
    @ApiOperation(value = "站点验证唯一性", notes = "站点验证唯一性,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:location:site:add")
    @SystemLog(logModul = "设备位置管理",logType = "验证唯一性",logDesc = "站点验证唯一性")
    public Result unique(@ApiParam LocationSite locationSite, HttpServletRequest request) {
        return locationSiteService.unique(locationSite, request);
    }

    /**
     * @explain 根据id查询站点信息
     * @author  Huangjinhao
     * @time    2020-03-16
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询站点信息", notes = "作者：Huangjinhao")
    @ApiImplicitParam(paramType="path", name = "id", value = "站点id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:location")
    public Result get(@PathVariable("id") Long id,  HttpServletRequest request){
        return locationSiteService.get(id, request);
    }
}