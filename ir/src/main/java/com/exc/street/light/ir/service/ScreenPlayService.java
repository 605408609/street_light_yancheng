/**
 * @filename:ScreenPlayService 2020-04-26
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenPlay;
import com.exc.street.light.resource.qo.IrScreenPlayQuery;
import com.exc.street.light.resource.vo.req.IrReqScreenProgramVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface ScreenPlayService extends IService<ScreenPlay> {

    /**
     * 编辑正在播放节目
     *
     * @param irReqScreenProgramVO
     * @param request
     * @return
     */
    Result updateScreenPlay(IrReqScreenProgramVO irReqScreenProgramVO, HttpServletRequest request);

    /**
     * 分页查询播放中的节目列表
     *
     * @param irScreenPlayQuery
     * @param httpServletRequest
     * @return
     */
    Result getQuery(IrScreenPlayQuery irScreenPlayQuery, HttpServletRequest httpServletRequest);

    /**
     * 取消播放
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    Result cancel(Integer id, HttpServletRequest httpServletRequest);

    /**
     * 获取播放详情
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    Result getScreenPlay(Integer id, HttpServletRequest httpServletRequest);

    /**
     * 播放节目列表数据刷新
     *
     * @param request
     * @return
     */
    Result refresh(HttpServletRequest request);

    /**
     * 节目播放列表中删除节目
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    Result deleteProgram(Integer id,HttpServletRequest httpServletRequest);
}