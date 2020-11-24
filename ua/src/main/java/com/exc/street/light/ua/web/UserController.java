/**
 * @filename:UserController 2020-03-12
 * @project ua  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ua.web;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.ua.qo.UserQueryObject;
import com.exc.street.light.ua.service.UserService;
import com.exc.street.light.ua.util.RSAUtils;
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
 * @time 2020-03-12
 *
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/ua/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param json
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "作者：xiezhipeng")
    public Result createAuth(@RequestBody JSONObject json, HttpServletRequest httpServletRequest) {
        return userService.login(json, httpServletRequest);
    }

    /**
     * @explain 新增用户
     * @author xiezhipeng
     * @time 2020-03-12
     */
    @PostMapping
    @ApiOperation(value = "新增用户", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "permission:module:user:add")
    @SystemLog(logModul = "运营管理",logType = "新增",logDesc = "新增用户")
    public Result insert(@ApiParam @RequestBody User user, HttpServletRequest request) {
        return userService.add(user, request);
    }

    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户", notes = "作者：xiezhipeng")
    @ApiImplicitParam(paramType = "path", name = "id", value = "用户id", required = true, dataType = "Integer")
    @RequiresPermissions(value = "permission:module:user:delete")
    @SystemLog(logModul = "运营管理",logType = "删除",logDesc = "删除用户")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return userService.delete(id, request);
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "用户详情", notes = "作者：xiezhipeng")
    @ApiImplicitParam(paramType = "path", name = "id", value = "用户id", required = true, dataType = "Integer")
    @RequiresPermissions(value = "permission:module:user")
    public Result detail(@PathVariable Integer id) {
        return userService.get(id);
    }

    /**
     * 根据用户ids查询名称集合
     * @param ids
     * @return
     */
    @GetMapping("/name/by/ids")
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "根据用户id查询名称集合", notes = "作者：xiezhipeng")
    public Result selectNameByUserIds(String ids) {
        return userService.selectNameByUserIds(ids);
    }

    /**
     * 编辑用户
     * @param user
     * @param request
     * @return
     */
    @PutMapping
    @ApiOperation(value = "编辑用户", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "permission:module:user:update")
    @SystemLog(logModul = "运营管理",logType = "编辑",logDesc = "编辑用户信息")
    public Result update(@ApiParam @RequestBody User user, HttpServletRequest request) {
        return userService.modify(user, request);
    }

    /**
     * 用户列表
     * @param request
     * @param queryObject
     * @return
     */
    @GetMapping
    @ApiOperation(value = "用户列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "permission:module:user")
    public Result getPages(HttpServletRequest request, UserQueryObject queryObject) {
        return userService.getPages(request, queryObject);
    }

    /**
     * 用户退出
     * @param user
     * @return
     */
    @PutMapping("/logout")
    @ApiOperation(value = "退出登录", notes = "作者：xiezhipeng")
    public Result logout(@ApiParam @RequestBody User user) {
        return userService.logout(user);
    }

    /**
     * 校验密码
     * @param oldPassword
     * @return
     */
    @PostMapping("/check/password")
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "校验密码", notes = "作者：xiezhipeng")
    public Result checkPassword(HttpServletRequest request, @RequestBody String oldPassword) {
        return userService.checkPassword(request, oldPassword);
    }

    /**
     * 修改密码
     * @param password
     * @return
     */
    @PutMapping("/modify/password")
    @ApiOperation(value = "修改密码", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "view:data")
    @SystemLog(logModul = "运营管理",logType = "编辑",logDesc = "修改用户密码")
    public Result modifyPassword(HttpServletRequest request, @RequestBody String password) {
        return userService.modifyPassword(request, password);
    }

    /**
     * 账号名称唯一性校验
     * @param id
     * @param accountName
     * @return
     */
    @GetMapping("/uniqueness")
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "账号名称唯一性校验", notes = "作者：xiezhipeng")
    public Result uniqueness(@RequestParam(required = false) Integer id, @RequestParam("accountName") String accountName) {
        return userService.uniqueness(id, accountName);
    }

    /**
     * 生成公钥
     * @return
     */
    @PostMapping(value = "/public/key")
    public Result getKey(){
        String publicKey = RSAUtils.generateBase64PublicKey();
        Result result = new Result();
        return result.success(publicKey);
    }

    /**
     * 分区和用户列表
     * @return
     */
    @GetMapping("/area")
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "分区用户列表", notes = "作者：xiezhipeng")
    public Result areaUserList(HttpServletRequest request) {
        return userService.areaUserList(request);
    }

    /**
     * 用户下拉框（当前用户所创建的用户）
     * @param request
     * @return
     */
    @GetMapping("/pull/down")
    @RequiresPermissions(value = "view:data")
    @ApiOperation(value = "用户下拉框", notes = "作者：xiezhipeng")
    public Result getUserPullList(HttpServletRequest request) {
        return userService.getUserPullList(request);
    }

    /**
     * 删除区域后用户绑定的区域id设为空
     * @param areaId
     * @return
     */
    @PutMapping("/modify/by/area")
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "删除区域后用户绑定的区域id设为空", notes = "作者：xiezhipeng")
    public Result updateUserByAreaId(@RequestParam Integer areaId) {
        return userService.updateUserByAreaId(areaId);
    }

    /**
     * 根据用户id查询其父级的founderId与超管id相等的用户id
     * @param id
     * @return
     */
    @GetMapping("/manager/id")
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "根据用户id查询其父级的founderId与超管id相等的用户id", notes = "作者：xiezhipeng")
    public Result getUserId(@RequestParam Integer id) {
        return userService.getUserId(id);
    }

    /**
     * 踢用户下线
     * @param id
     * @return
     */
    @GetMapping("/kick/off")
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "踢用户下线", notes = "作者：xiezhipeng")
    public Result KickOff(Integer id) {
        return userService.KickOff(id);
    }

    /**
     * 禁用用户
     * @param id
     * @return
     */
    @GetMapping("/forbidden")
    @RequiresPermissions(value = "permission:module:user")
    @ApiOperation(value = "禁用用户", notes = "作者：xiezhipeng")
    public Result forbidden(@RequestParam("id") Integer id, @RequestParam("forbidden") Integer forbidden) {
        return userService.forbidden(id, forbidden);
    }
}