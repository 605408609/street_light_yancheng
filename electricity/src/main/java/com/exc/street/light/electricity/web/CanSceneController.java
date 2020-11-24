package com.exc.street.light.electricity.web;

import com.exc.street.light.electricity.service.CanChannelService;
import com.exc.street.light.electricity.service.CanSceneService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.Scene;
import com.exc.street.light.resource.qo.electricity.CanSceneQueryObject;
import com.exc.street.light.resource.vo.electricity.CanSceneListVO;
import com.exc.street.light.resource.vo.electricity.SiteSceneVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 强电场景控制器
 *
 * @author Linshiwen
 * @date 2018/05/28
 */
@Api(tags = "强电场景控制器")
@ApiIgnore
@RestController
@RequestMapping("/api/can/scene")
public class CanSceneController {
    private static final Logger logger = LoggerFactory.getLogger(CanSceneController.class);
    @Autowired
    private CanSceneService canSceneService;
    @Autowired
    private CanChannelService canChannelService;

    /**
     * 新增场景
     *
     * @param scene   场景接收对象
     * @param request
     * @return 处理结果
     */
    @PostMapping
    @ApiOperation(value = "新增场景", notes = "新增场景")
    @RequiresPermissions(value = "electricity:manage:scene:add")
    public Result add(@RequestBody Scene scene, HttpServletRequest request) {
        logger.info("接收参数:" + scene);
        return canSceneService.add(1, scene, request);
    }

    /**
     * 编辑场景
     *
     * @param scene   场景接收对象
     * @param request
     * @return 处理结果
     */
    @PutMapping
    @RequiresPermissions(value = "electricity:manage:scene:update")
    public Result update(@RequestBody Scene scene, HttpServletRequest request) {
        logger.info("编辑场景接收参数:" + scene);
        Result result = canSceneService.modify(scene, request);
        return result;
    }

    /**
     * 删除场景
     *
     * @param sn
     * @param nid
     * @param request
     * @return 处理结果
     */
    @DeleteMapping
    @RequiresPermissions(value = "electricity:manage:scene:delete")
    public Result delete(Integer sn, Integer nid, HttpServletRequest request) {
        Result result = canSceneService.delete(sn, nid, request);
        return result;
    }

    /**
     * 新增站点场景
     *
     * @param vo      场景接收对象
     * @param request
     * @return 处理结果
     */
    @PostMapping("/site")
    @RequiresPermissions(value = "electricity:manage:scene:add")
    public Result addSite(@RequestBody SiteSceneVO vo, HttpServletRequest request) {
        logger.info("接收参数:" + vo);
        Result result = canSceneService.addSite(vo, request);
        return result;
    }

    /**
     * 查询站点未编辑的场景
     *
     * @param siteId
     * @return
     */
    @GetMapping("/unedit")
    @RequiresPermissions(value = "electricity:manage:scene:add")
    public Result unEdit(Integer siteId) {
        return canSceneService.findBySiteId(siteId);
    }

    /**
     * 根据集控id查询场景回路
     *
     * @param nid 集控id
     * @return 结果集
     */
    @GetMapping("/node/{nid}")
    @ApiOperation(value = "根据集控ID获取场景列表", notes = "根据集控ID获取场景列表")
    @RequiresPermissions(value = "electricity:manage:scene:list")
    public Result list(@PathVariable Integer nid) {
        return new Result().success(canChannelService.listByNid(nid));
    }

    /**
     * 根据场景名称查询
     *
     * @param qo
     * @param request
     * @return 结果集
     */
    @GetMapping("/name")
    @ApiOperation(value = "根据场景名称查询对应集控列表", notes = "根据场景名称查询对应集控列表")
    @RequiresPermissions(value = "electricity:manage:scene:list")
    public Result listByName(CanSceneQueryObject qo, HttpServletRequest request) {
        Result result = canSceneService.listByName(qo, request);
        return result;
    }

    /**
     * 场景选择集控列表
     *
     * @param qo
     * @param request
     * @return
     */
    @GetMapping
    @ApiOperation(value = "场景选择集控列表", notes = "场景选择集控列表")
    @RequiresPermissions(value = "electricity:manage:timer")
    public Result list(CanSceneQueryObject qo, HttpServletRequest request) {
        Result result = canSceneService.query(qo, request);
        return result;
    }

    /**
     * 判断集控的场景名称是否重复
     *
     * @param nid
     * @param name
     * @return
     */
    @GetMapping("/check")
    @RequiresPermissions(value = "electricity:manage:scene:add")
    public Result check(Integer nid, String name) {
        Result result = canSceneService.check(nid, name);
        return result;
    }

    /**
     * 临时需求：传定时场景名称，定时时间，批量删除符合条件的定时
     *
     * @param sceneName 场景名称
     * @param cycleType
     * @param type
     * @param date
     * @param request
     * @return
     */
    @GetMapping("/site/delete")
    @ApiOperation(value = "根据条件批量删除定时", notes = "根据定时场景名称，定时时间，批量删除符合条件的定时")
    @RequiresPermissions(value = "electricity:manage:scene:add")
    public Result delete(/*@RequestParam(required = false) Integer areaId,*/
            @RequestParam(required = false) String sceneName,
            @RequestParam(required = false) Integer cycleType,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String date, HttpServletRequest request) {
        return canSceneService.deleteByDateName(cycleType, type, sceneName, date, request);
    }
}
