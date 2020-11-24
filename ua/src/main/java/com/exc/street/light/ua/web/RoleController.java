/**
 * @filename:RoleController 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ua.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.vo.req.UaReqRoleVO;
import com.exc.street.light.ua.qo.RoleQueryObject;
import com.exc.street.light.ua.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
 * @time    2020-03-12
 *
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("/api/ua/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * @explain 新增角色
     * @author xiezhipeng
     * @time 2020-03-17
     */
    @PostMapping
    @ApiOperation(value = "新增角色", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "permission:module:role:add")
    @SystemLog(logModul = "运营管理",logType = "新增",logDesc = "新增角色")
    public Result insert(@ApiParam @RequestBody UaReqRoleVO reqRoleVO, HttpServletRequest request) {
        return roleService.add(reqRoleVO, request);
    }

    /**
     * 编辑角色
     * @param reqRoleVO
     * @param request
     * @return
     */
    @PutMapping
    @ApiOperation(value = "编辑角色", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "permission:module:role:update")
    @SystemLog(logModul = "运营管理",logType = "编辑",logDesc = "编辑角色")
    public Result update(@ApiParam @RequestBody UaReqRoleVO reqRoleVO, HttpServletRequest request) {
        return roleService.modify(reqRoleVO, request);
    }

    /**
     * 删除角色
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除角色", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "permission:module:role:delete")
    @SystemLog(logModul = "运营管理",logType = "删除",logDesc = "删除角色")
    @ApiImplicitParam(paramType = "path", name = "id", value = "角色id", required = true, dataType = "Integer")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return roleService.delete(id, request);
    }

    /**
     * 角色详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "角色详情", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "permission:module:role")
    @ApiImplicitParam(paramType = "path", name = "id", value = "角色id", required = true, dataType = "Integer")
    public Result detail(@PathVariable Integer id) {
        return roleService.get(id);
    }

    /**
     * 角色列表
     * @param request
     * @return
     */
    @GetMapping
    @ApiOperation(value = "角色列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "permission:module:role")
    public Result getPages(HttpServletRequest request, RoleQueryObject queryObject) {
        return roleService.getPages(request, queryObject);
    }

    /**
     * 角色下拉列表
     * @param request
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "角色下拉列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "permission:module:role")
    public Result getList(HttpServletRequest request) {
        return roleService.getRoleList(request);
    }

    /**
     * 角色名称的唯一性校验
     * @param id
     * @param name
     * @return
     */
    @GetMapping("/uniqueness")
    @RequiresPermissions(value = "permission:module:role")
    @ApiOperation(value = "角色名称的唯一性校验", notes = "0:不存在 1：已存在;作者：xiezhipeng")
    public Result uniqueness(@RequestParam(required = false) Integer id, @RequestParam("name") String name) {
        return roleService.uniqueness(id, name);
    }

}