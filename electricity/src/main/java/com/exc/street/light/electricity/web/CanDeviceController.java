package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.CanDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.CanDevice;
import com.exc.street.light.resource.vo.electricity.CanDevicePatchVO;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 强电设备控制器
 *
 * @author Linshiwen
 * @date 2018/05/22
 */
@Api(tags ="强电设备控制器" )
@RestController
@RequestMapping("/api/can/device")
public class CanDeviceController {
    private static final Logger logger = LoggerFactory.getLogger(CanChannelController.class);
    @Autowired
    private CanDeviceService canDeviceService;

    /**
     * 新增设备
     *
     * @param canDevice 设备信息
     * @param request
     * @return 新增结果
     */
    @PostMapping
    @RequiresPermissions(value = "electricity:manage:device:add")
    public Result add(@RequestBody CanDevice canDevice, HttpServletRequest request) {
        return canDeviceService.add(canDevice, request);
    }

    /**
     * 删除设备
     *
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions(value = "electricity:manage:device:delete")
    public Result delete(@PathVariable Integer id, HttpServletRequest request) {
        return canDeviceService.delete(id, request);
    }

    /**
     * 批量删除设备
     *
     * @param ids
     * @param request
     * @return
     */
    @DeleteMapping("/patch")
    @RequiresPermissions(value = "electricity:manage:device:delete")
    public Result deletePatch(Integer[] ids, HttpServletRequest request) {
        return canDeviceService.deletePatch(ids, request);
    }

    /**
     * 批量更新设备
     *
     * @param canDevicePatchVO 设备集合
     * @param request
     * @return 更新结果
     */
    @PutMapping
    @RequiresPermissions(value = "electricity:manage:device:update")
    public Result update(@RequestBody CanDevicePatchVO canDevicePatchVO, HttpServletRequest request) {
        logger.info("请求对象:" + canDevicePatchVO);
        Result result = canDeviceService.patchUpdate(canDevicePatchVO.getCanDevices(), request);
        return result;
    }

    /**
     * can设备列表
     *
     * @return 列表数据
     */
    @GetMapping
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result list() {
        List<CanDevice> list = canDeviceService.list();
        return new Result().success(list);
    }

    /**
     * 根据节点id查询can设备列表
     *
     * @return 列表数据
     */
    @GetMapping("/node/{nid}")
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result listByNid(@PathVariable Integer nid) {
        List<CanDevice> list = canDeviceService.getByNid(nid);
        return new Result().success(list);
    }
}
