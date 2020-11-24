/**
 * @filename:GroupController 2020-03-18
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.web;

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

import com.exc.street.light.dlm.service.LocationGroupService;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.DlmGroupQuery;
import com.exc.street.light.resource.vo.req.DlmReqGroupVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>分组控制器</p>
 * 
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-03-18
 *
 */
@Api(tags = "分组控制器",value="分组控制器" )
@RestController
@RequestMapping("/api/dlm/location/group")
public class LocationGroupController{

    @Autowired
    private LocationGroupService locationGroupService;

    /**
     * @explain 查询分组下拉列表
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/pulldown")
    @ApiOperation(value = "查询分组下拉列表", notes = "查询分组下拉列表,作者：Longshuangyang")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupName", value = "分组名称（空则查询所有）", required = false, dataType = "String", paramType="query")})
    @RequiresPermissions(value = "view:data")
    public Result pulldown(@RequestParam(required = false) String groupName, HttpServletRequest request) {
        return locationGroupService.pulldown(groupName, request);
    }

    /**
     * @explain 添加分组
     * @author  Longshuangyang
     * @time    2020-03-25
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加分组", notes = "作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:group:add")
    @SystemLog(logModul = "设备位置管理",logType = "添加",logDesc = "添加分组")
    public Result insert(@ApiParam @RequestBody DlmReqGroupVO dlmReqGroupVO, HttpServletRequest request){
        return locationGroupService.add(dlmReqGroupVO,request);
    }

    /**
     * @explain 分组分页条件查询
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/page")
    @ApiOperation(value = "分组分页条件查询", notes = "分组分页条件查询,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result getPage(DlmGroupQuery dlmGroupQuery, HttpServletRequest request) {
        return locationGroupService.getPage(dlmGroupQuery, request);
    }

    /**
     * @explain 分组分页条件查询
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/get/{groupId}")
    @ApiOperation(value = "获取分组详情", notes = "获取分组详情,作者：Longshuangyang")
    @RequiresPermissions(value = "view:data")
    public Result get(@PathVariable("groupId") Integer groupId, HttpServletRequest request) {
        return locationGroupService.get(groupId, request);
    }

    /**
     * @explain 修改分组
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改分组", notes = "修改分组,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:group:update")
    @SystemLog(logModul = "设备位置管理",logType = "修改",logDesc = "修改分组")
    public Result updateGroup(@ApiParam @RequestBody DlmReqGroupVO dlmReqGroupVO, HttpServletRequest request) {
        return locationGroupService.updateGroup(dlmReqGroupVO, request);
    }

    /**
     * @param deviceTypeNum 指定设备类型编号
     * @explain 查询分组详细列表
     * @author Longshuangyang
     * @time 2020-03-16
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询分组详细列表", notes = "查询分组详细列表,作者：Longshuangyang")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hierarchy", value = "指定获取参数层级1为分组，依次递加，为3时需要指定设备类型编号才生效，默认1", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "deviceTypeNum", value = "指定设备类型编号（1：灯具，2：WIFI，3：广播，4：监控，5：显示屏，6：一键呼叫，7：气象）", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "typeId", value = "分组类型id（1：灯杆分组，2：灯具分组）", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "当前页，默认0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数,为0则查全部，默认0", required = false, dataType = "int", paramType = "query")})
    @RequiresPermissions(value = "view:data")
    public Result getList(@RequestParam(required = false) Integer hierarchy,
                          @RequestParam(required = false) Integer deviceTypeNum,
                          @RequestParam(required = false) Integer typeId,
                          @RequestParam(required = false) Integer pageNum,
                          @RequestParam(required = false) Integer pageSize,
                          HttpServletRequest request) {
        return locationGroupService.getList(hierarchy, deviceTypeNum, typeId, pageNum, pageSize, request);
    }

    /**
     * @explain 删除分组
     * @author  Longshuangyang
     * @time    2020-03-16
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除分组", notes = "作者：Longshuangyang")
    @ApiImplicitParam(paramType="path", name = "id", value = "分组id", required = true, dataType = "Long")
    @RequiresPermissions(value = "dm:module:group:delete")
    @SystemLog(logModul = "设备位置管理",logType = "删除",logDesc = "删除分组")
    public Result deleteById(@PathVariable("id") Long id,  HttpServletRequest request){
        return locationGroupService.delete(id, request);
    }

    /**
     * @explain 分组验证唯一性
     * @author Longshuangyang
     * @time 2020-03-17
     */
    @GetMapping("/unique")
    @ApiOperation(value = "分组验证唯一性", notes = "分组验证唯一性,作者：Longshuangyang")
    @RequiresPermissions(value = "dm:module:group:add")
    @SystemLog(logModul = "设备位置管理",logType = "验证唯一性",logDesc = "分组验证唯一性")
    public Result unique(@ApiParam DlmReqGroupVO dlmReqGroupVO, HttpServletRequest request) {
        return locationGroupService.unique(dlmReqGroupVO, request);
    }

}