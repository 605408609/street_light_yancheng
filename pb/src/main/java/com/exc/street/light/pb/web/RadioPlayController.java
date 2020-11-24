/**
 * @filename:RadioPlayController 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.web;

import com.exc.street.light.log_api.annotation.SystemLog;
import com.exc.street.light.pb.service.RadioPlayService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.qo.PbRadioPlayQueryObject;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.req.PbReqPlayRemoveBindListVO;
import com.exc.street.light.resource.vo.req.PbReqRadioPlayControlVO;
import com.exc.street.light.resource.vo.req.PbReqRadioPlayVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>公共广播播放控制器</p>
 *
 * <p>说明： Controller</P>
 *
 * @version: V1.0
 * @author: LeiJing
 * @time 2020-03-21
 */
@Api(tags = "广播定时任务", value = "")
@RestController
@RequestMapping("/api/pb/radio/play")
public class RadioPlayController {
    @Autowired
    private RadioPlayService radioPlayService;

    /**
     * 雷拓IP广播平台登录并获取身份验证JSESSIONID
     *
     * @return
     */
    @PostMapping("/login")
    private Result login() {
        return radioPlayService.login();
    }

    /**
     * 获取公共广播节目播放信息
     *
     * @param request
     * @return
     */
    @GetMapping("/{id}")
    @RequiresPermissions(value = "pb:module:regular:radio")
    @ApiOperation(value = "获取公共广播节目播放信息", notes = "获取公共广播节目播放信息,作者：XiaoKun")
    public Result get(@PathVariable Integer id, HttpServletRequest request) {
        return radioPlayService.get(id, request);
    }

    /**
     * 新增公共广播节目播放
     *
     * @return
     */
    @PostMapping("/addPlay")
    @RequiresPermissions(value = "pb:module:audio:program:issue")
    @ApiOperation(value = "新增公共广播节目播放", notes = "新增公共广播节目播放,作者：LeiJing")
    @SystemLog(logModul = "公共广播", logType = "发布", logDesc = "发布广播节目")
    public Result addPlay(@RequestBody PbReqRadioPlayVO pbReqRadioPlayVO, HttpServletRequest request) {
        return radioPlayService.add(pbReqRadioPlayVO, request);
    }

    /**
     * 编辑公共广播节目播放
     *
     * @return
     */
    @PutMapping
    @RequiresPermissions(value = "pb:module:regular:radio:update")
    @ApiOperation(value = "编辑公共广播节目播放", notes = "编辑公共广播节目播放,作者：LeiJing")
    @SystemLog(logModul = "公共广播", logType = "编辑", logDesc = "编辑定时广播")
    public Result update(@RequestBody PbReqRadioPlayVO pbReqRadioPlayVO, HttpServletRequest request) {
        return radioPlayService.update(pbReqRadioPlayVO, request);
    }

    /**
     * 获取公共广播节目播放列表
     *
     * @param qo
     * @param request
     * @return
     */
    @GetMapping("/getList")
    @RequiresPermissions(value = "pb:module:regular:radio")
    @ApiOperation(value = "获取公共广播节目播放列表", notes = "获取公共广播节目播放列表,作者：xiaok")
    @SystemLog(logModul = "公共广播", logType = "列表", logDesc = "定时广播列表")
    public Result getPlayList(PbRadioPlayQueryObject qo, HttpServletRequest request) {
        //return radioPlayService.getPlayList(qo, request);
        return radioPlayService.getPageList(qo, request);
    }

    /**
     * 解除设备与节目间的绑定
     *
     * @param listVO
     * @param request
     * @return
     */
    @PostMapping("/removeBind")
    @RequiresPermissions(value = "pb:module:regular:radio:delete")
    @ApiOperation(value = "解除设备与节目间的绑定", notes = "解除设备与节目间的绑定,作者：xiaok")
    public Result removeBind(@RequestBody PbReqPlayRemoveBindListVO listVO, HttpServletRequest request) {
        if (listVO == null || listVO.getList() == null) {
            return new Result().error("删除失败");
        }
        return radioPlayService.removeBind(listVO.getList(), request);
    }

    /**
     * 公共广播节目播放控制
     *
     * @param pbReqRadioPlayControlVO
     * @param request
     * @return
     */
    @PostMapping("/playControl")
    @RequiresPermissions(value = "pb:module:regular:radio:play")
    @ApiOperation(value = "公共广播节目播放控制", notes = "公共广播节目播放控制,作者：LeiJing")
    @SystemLog(logModul = "公共广播", logType = "播放控制", logDesc = "公共广播节目播放控制")
    public Result playControl(@RequestBody PbReqRadioPlayControlVO pbReqRadioPlayControlVO, HttpServletRequest request) {
        return radioPlayService.playControl(pbReqRadioPlayControlVO, request);
    }

    /**
     * 删除公共广播节目播放
     *
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions(value = "pb:module:regular:radio:delete")
    @ApiOperation(value = "删除公共广播节目播放", notes = "删除公共广播节目播放,作者：LeiJing")
    @SystemLog(logModul = "公共广播", logType = "删除", logDesc = "删除定时广播")
    public Result deletePlay(@PathVariable Integer id, HttpServletRequest request) {
        return radioPlayService.delete(id, request);
    }

    /**
     * 批量删除公共广播节目播放
     *
     * @param ids
     * @param request
     * @return
     */
    @DeleteMapping("/batch")
    @RequiresPermissions(value = "pb:module:regular:radio:delete")
    @ApiOperation(value = "批量删除公共广播节目播放", notes = "批量删除公共广播节目播放,作者：LeiJing")
    @SystemLog(logModul = "公共广播", logType = "删除", logDesc = "批量删除定时广播")
    public Result batchDelete(@RequestParam String ids, HttpServletRequest request) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);
        return radioPlayService.batchDelete(idList);
    }

    /**
     * 定时任务查询
     *
     * @return
     */
    @PostMapping("/taskList")
    @RequiresPermissions(value = "pb:module:regular:radio")
    public Result taskList() {
        return radioPlayService.taskList();
    }

    /**
     * 终端音量设置
     *
     * @param ids
     * @param volValue
     * @param request
     * @return
     */
    @PostMapping("/termVolSet")
    @RequiresPermissions(value = "pb:module:regular:radio:update")
    @ApiOperation(value = "终端音量设置", notes = "批量设置公共广播节目绑定设备的音量,作者：XiaoKun")
    @SystemLog(logModul = "公共广播", logType = "音量设置", logDesc = "批量设备定时广播音量")
    public Result termVolSet(String ids, Integer volValue, HttpServletRequest request) {
        return radioPlayService.termVolSet(ids, volValue, request);
    }

}