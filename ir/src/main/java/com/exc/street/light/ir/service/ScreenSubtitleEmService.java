/**
 * @filename:ScreenSubtitleEmService 2020-11-10
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ir.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenSubtitleEm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.qo.IrScreenSubtitleEmQuery;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitleEmVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(传感器关联显示屏显示数据设置表服务层)
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
public interface ScreenSubtitleEmService extends IService<ScreenSubtitleEm> {

    /**
     * 添加传感器关联显示屏数据设置
     * @param irReqScreenSubtitleEmVO
     * @param request
     * @return
     */
    Result add(IrReqScreenSubtitleEmVO irReqScreenSubtitleEmVO, HttpServletRequest request);

    /**
     * 删除传感器关联显示屏显示数据设置
     * @param ids
     * @param request
     * @return
     */
    Result batchDelete(String ids, HttpServletRequest request);

    /**
     * 修改传感器关联显示屏显示数据设置
     * @param irReqScreenSubtitleEmVO
     * @param request
     * @return
     */
    Result updateDevice(IrReqScreenSubtitleEmVO irReqScreenSubtitleEmVO, HttpServletRequest request);

    /**
     * 获取修改传感器关联显示屏显示数据设置列表(分页查询)
     * @param irScreenSubtitleEmQuery
     * @param httpServletRequest
     * @return
     */
    Result getQuery(IrScreenSubtitleEmQuery irScreenSubtitleEmQuery, HttpServletRequest httpServletRequest);

    /**
     * 查询传感器关联显示屏显示数据设置详情
     * @param id
     * @param request
     * @return
     */
    Result detail(Integer id, HttpServletRequest request);
}