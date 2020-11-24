/**
 * @filename:LogNormalController 2020-05-08
 * @project log  V1.0
 * Copyright(c) 2020 xuJiaHao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.log_biz.web;

import com.exc.street.light.log_api.service.LogNormalService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.LogDataQueryObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @author: xuJiaHao
 * @time    2020-05-08
 *
 */
@Api(description = "正常日志控制器",value="正常日志控制器" )
@RestController
@RequestMapping("/log/normal")
public class LogNormalController {

    @Autowired
    private LogNormalService logNormalService;

    @GetMapping("/page")
    @ApiOperation(value = "获取正常日志数据", notes = "获取正常日志分页数据")
    public Result getPage(LogDataQueryObject logDataQueryObject, HttpServletRequest request){
        return logNormalService.getPage(logDataQueryObject, request);
    }
}