/**
 * @filename:WifiUserController 2020-03-12
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.web;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.wifi.qo.WifiUserQueryObject;
import com.exc.street.light.wifi.service.WifiUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "wifi用户接口")
@RestController
@RequestMapping("/api/wifi/wifi/user")
public class WifiUserController {

    @Autowired
    private WifiUserService wifiUserService;

    /**
     * wifi用户列表
     * @param request
     * @param queryObject
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "wifi用户列表", notes = "作者：xiezhipeng")
    @RequiresPermissions(value = "wifi:module:user:statistical:list")
    public Result list(HttpServletRequest request, WifiUserQueryObject queryObject) {
        return wifiUserService.getList(request, queryObject);
    }

}