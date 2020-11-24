package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.CanModuleTypeService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.CanModuleType;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模块类型控制器
 *
 * @author Linshiwen
 * @date 2018/05/23
 */
@Api(tags = "模块类型控制器")
@RestController
@RequestMapping("/api/can/module/type")
public class CanModuleTypeController {
    @Autowired
    private CanModuleTypeService canModuleTypeService;

    /**
     * 模块类型列表
     *
     * @return 查询结果
     */
    @GetMapping
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result list() {
        List<CanModuleType> list = canModuleTypeService.list();
        return new Result().success(list);
    }
}
