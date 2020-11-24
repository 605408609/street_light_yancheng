/**
 * @filename:LampStrategyActionHistoryService 2020-03-23
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampStrategyActionHistory;
import com.exc.street.light.resource.vo.req.SlReqStrategyActionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LampStrategyActionHistoryService extends IService<LampStrategyActionHistory> {

    /**
     * 查询策略历史动作列表
     *
     * @param lampStrategyTypeId
     * @param request
     * @return
     */
    Result getList(Integer lampStrategyTypeId, HttpServletRequest request);

    /**
     * 添加策略历史动作
     *
     * @param slReqStrategyActionVOList
     * @param request
     * @return
     */
    Result add(List<SlReqStrategyActionVO> slReqStrategyActionVOList, HttpServletRequest request);

    /**
     * 获取策略历史动作详情
     *
     * @param strategyTypeId
     * @param request
     * @return
     */
    Result detail(Integer strategyTypeId, HttpServletRequest request);
}