/**
 * @filename:AlarmOrderSetService 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.woa.AlarmOrderSet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.qo.WoaAlarmOrderSetQuery;
import com.exc.street.light.resource.vo.req.WoaReqAlarmOrderSetVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface AlarmOrderSetService extends IService<AlarmOrderSet> {

    /**
     * 新增工单生成设置
     *
     * @param woaReqAlarmOrderSetVO
     * @param request
     * @return
     */
    Result add(WoaReqAlarmOrderSetVO woaReqAlarmOrderSetVO, HttpServletRequest request);

    /**
     * 条件查询
     *
     * @param woaAlarmOrderSetQuery
     * @param httpServletRequest
     * @return
     */
    Result queryAlarmSet(WoaAlarmOrderSetQuery woaAlarmOrderSetQuery, HttpServletRequest httpServletRequest);

    /**
     * 批量删除告警设置
     *
     * @param ids
     * @param request
     * @return
     */
    Result batchDelete(String ids, HttpServletRequest request);

    /**
     * 修改工单生成设置
     *
     * @param woaReqAlarmOrderSetVO
     * @param request
     * @return
     */
    Result updateAlarmOrderSet(WoaReqAlarmOrderSetVO woaReqAlarmOrderSetVO, HttpServletRequest request);

    /**
     * 工单生成设置详情
     *
     * @param id
     * @param request
     * @return
     */
    Result get(Integer id, HttpServletRequest request);

    /**
     * 控制设置开关
     *
     * @param id
     * @param value
     * @param httpServletRequest
     * @return
     */
    Result control(Integer id, Integer value, HttpServletRequest httpServletRequest);

}