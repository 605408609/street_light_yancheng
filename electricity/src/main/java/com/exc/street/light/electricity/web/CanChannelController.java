package com.exc.street.light.electricity.web;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.electricity.service.CanChannelService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.electricity.CanChannelQueryObject;
import com.exc.street.light.resource.vo.electricity.CanChannelPatchVO;
import com.exc.street.light.resource.vo.electricity.ControlVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanChannelControlVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 回路控制器
 *
 * @author Linshiwen
 * @date 2018/05/23
 */
@Api(tags = "回路控制器", value = "回路控制器")
@RestController
@RequestMapping("/api/can/channel")
public class CanChannelController {
    private static final Logger logger = LoggerFactory.getLogger(CanChannelController.class);
    @Autowired
    private CanChannelService canChannelService;
    @Value("${upload.path}")
    public String uploadPath;

    /**
     * 回路批量编辑
     *
     * @param channelPatchVO 遍历编辑接收对象
     * @param request
     * @return
     */
    @PutMapping
    //electricity:manage:channel:update
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:channel:update")
    @ApiOperation(value = "回路批量编辑", notes = "回路批量编辑")
    public Result update(@RequestBody CanChannelPatchVO channelPatchVO, HttpServletRequest request) {
        logger.info("回路批量编辑,接收参数:CanChannelPatchVO:{}", channelPatchVO);
        return canChannelService.patchUpdate(channelPatchVO, request);
    }

    /**
     * 批量控制回路
     *
     * @param controlVO
     * @param request
     * @return
     */
    @PatchMapping("/control")
    @ApiOperation(value = "批量控制回路", notes = "批量控制回路")
    //electricity:manage:control
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:channel:control")
    public Result control(@RequestBody ControlVO controlVO, HttpServletRequest request) {
        logger.info("接收参数:" + controlVO);
        return canChannelService.control(controlVO, request);
    }

    /**
     * 批量控制场景回路
     *
     * @param controlVO
     * @param request
     * @return
     */
    @PatchMapping("/control/scene")
    @RequiresPermissions(value = "electricity:manage:timer")
    public Result controlScene(@RequestBody ControlVO controlVO, HttpServletRequest request) {
        logger.info("批量控制场景回路接收参数:" + controlVO);
        return canChannelService.controlScene(controlVO, request);
    }

    /**
     * 根据节点id查询回路
     *
     * @param nid
     * @return
     */
    @GetMapping("/node/{nid}")
    @ApiOperation(value = "回路详情", notes = "根据集控id获取回路详情")
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result get(@PathVariable Integer nid) {
        return new Result().success(canChannelService.getByNid(nid));
    }

    /**
     * 编辑场景根据条件查询网关回路
     *
     * @param qo 查询对象
     * @return 结果集
     */
    @GetMapping
    @ApiOperation(value = "根据场景查询回路列表", notes = "根据场景查询回路列表")
    @RequiresPermissions(value = "electricity:manage:scene:list")
    public Result list(CanChannelQueryObject qo) {
        return canChannelService.query(qo);
    }

    /**
     * 根据条件查询网关回路
     *
     * @param qo 查询对象
     * @return 结果集
     */
    @GetMapping("/all")
    //electricity:manage:control
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:channel")
    public Result listAll(HttpServletRequest request, CanChannelQueryObject qo) {
        logger.info("查询网关回路参数:" + qo);
        return canChannelService.listAll(request, qo);
    }

    /**
     * 点表txt文件导入回路
     *
     * @param file
     * @param nid
     * @param request
     * @return
     */
    @PostMapping("/txt/import")
    @RequiresPermissions(value = "electricity:manage:node:list")
    public Result batchImport(MultipartFile file, Integer nid, HttpServletRequest request) {
        if (file.isEmpty()) {
            return new Result().error("文件为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.info("上传的后缀名为：" + suffixName);
        String flag = "txt";
        if (!flag.equals(suffixName)) {
            return new Result().error("文件类型错误,不是txt文件");
        }
        String realName = UUID.randomUUID() + "." + suffixName;
        File dest = new File(uploadPath + realName);
        Result result = null;
        try {
            file.transferTo(dest);
            result = canChannelService.saveList(dest, nid, request);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dest.delete();
        }
        return result;
    }

    @PatchMapping("/switch/all/open")
    @ApiOperation("回路控制全开")
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:channel:control")
    public Result<JSONObject> openAll(@RequestBody List<ReqCanChannelControlVO> reqVoList, HttpServletRequest request) {
        return canChannelService.allSwitch(reqVoList, true, request);
    }

    @PatchMapping("/switch/all/close")
    @ApiOperation("回路控制全关")
    @RequiresPermissions(value = "dm:module:same:device:the:gateway:channel:control")
    public Result<JSONObject> closeAll(@RequestBody List<ReqCanChannelControlVO> reqVoList, HttpServletRequest request) {
        return canChannelService.allSwitch(reqVoList, false, request);
    }

    @GetMapping("/current/strategy/{id}")
    @ApiOperation(value = "获取回路当前绑定场景和历史记录", notes = "获取回路当前绑定场景和历史记录")
    @RequiresPermissions(value = "electricity:manage:scene:list")
    public Result<JSONObject> currentStrategy(@PathVariable("id") String id, HttpServletRequest request) {
        return canChannelService.currentStrategy(id,request);
    }

}
