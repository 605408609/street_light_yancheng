package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.CanDeviceTypeService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.CanDeviceType;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 强电设备类型控制器
 *
 * @author Linshiwen
 * @date 2018/05/23
 */
@Api(tags = "强电设备类型控制器")
@RestController
@RequestMapping("/api/can/device/type")
public class CanDeviceTypeController {
    @Autowired
    private CanDeviceTypeService canDeviceTypeService;

    /**
     * 设备类型列表
     *
     * @return 列表数据
     */
    @GetMapping
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result list() {
        List<CanDeviceType> list = canDeviceTypeService.list();
        return new Result().success(list);
    }
}
