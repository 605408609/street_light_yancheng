/**
 * @filename:LocationControlTypeController 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.web;

import com.exc.street.light.dlm.service.LocationControlTypeService;
import com.exc.street.light.resource.core.Result;
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
 * @time    2020-08-22
 *
 */
@Api(tags = "集中控制器类型")
@RestController
@RequestMapping("/api/dlm/location/control/type")
public class LocationControlTypeController{

    @Autowired
    private LocationControlTypeService locationControlTypeService;

    @ApiOperation(value="集中控制器类型下拉列表", notes = "作者：xiezhipeng")
    @GetMapping("/option")
    @RequiresPermissions(value = "view:data")
    public Result listLocationControlTypeWithOptionQuery(HttpServletRequest request) {
        return locationControlTypeService.listLocationControlTypeWithOptionQuery(request);
    }

}