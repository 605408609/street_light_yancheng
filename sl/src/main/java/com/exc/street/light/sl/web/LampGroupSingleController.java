/**
 * @filename:LampGroupSingleController 2020-07-16
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.web;

import com.exc.street.light.sl.service.LampGroupSingleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;


/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 灯具分组中间表Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-07-16
 *
 */
@Api(tags = "灯具分组中间表",value="灯具分组中间表" )
@RestController
@RequestMapping("/api/sl/lamp/group/single")
public class LampGroupSingleController{

    @Autowired
    private LampGroupSingleService lampGroupSingleService;

}