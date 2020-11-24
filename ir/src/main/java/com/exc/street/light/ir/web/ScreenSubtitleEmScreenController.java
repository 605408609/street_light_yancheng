/**
 * @filename:ScreenSubtitleEmScreenController 2020-11-10
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ir.web;

import com.exc.street.light.ir.service.ScreenSubtitleEmScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
/**   
 * <p>自动生成工具：mybatis-dsc-generator</p>
 * 
 * <p>说明： 传感器关联显示屏中间表Controller</P>
 * @version: V1.0
 * @author: Longshuangyang
 * @time    2020-11-10
 *
 */
@Api(tags = "传感器关联显示屏中间表",value="传感器关联显示屏中间表" )
@RestController
@RequestMapping("/api/ir/screen/subtitle/em/screen")
public class ScreenSubtitleEmScreenController{

    @Autowired
    private ScreenSubtitleEmScreenService screenSubtitleEmScreenService;

}