/**
 * @filename:LoopSceneStrategyService 2020-11-07
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LoopSceneStrategy;
import com.exc.street.light.resource.qo.DlmSceneStrategyQuery;
import com.exc.street.light.resource.vo.req.DlmReqSceneStrategyExecuteVO;
import com.exc.street.light.resource.vo.req.DlmReqSceneStrategyVO;

import javax.servlet.http.HttpServletRequest;
/**   
 * @Description: 场景策略(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface LoopSceneStrategyService extends IService<LoopSceneStrategy> {

    /**
     * 新增场景策略
     * @param dlmReqSceneStrategyVO
     * @param request
     * @return
     */
    Result insertSceneStrategy(DlmReqSceneStrategyVO dlmReqSceneStrategyVO, HttpServletRequest request);

    /**
     * 编辑场景策略
     * @param dlmReqSceneStrategyVO
     * @param request
     * @return
     */
    Result updateSceneStrategy(DlmReqSceneStrategyVO dlmReqSceneStrategyVO, HttpServletRequest request);

    /**
     * 场景策略详情
     * @param strategyId
     * @param request
     * @return
     */
    Result getSceneStrategy(Integer strategyId, HttpServletRequest request);

    /**
     * 删除场景策略
     * @param strategyId
     * @param request
     * @return
     */
    Result deleteSceneStrategy(Integer strategyId, HttpServletRequest request);

    /**
     * 场景策略分页条件查询
     * @param dlmSceneStrategyQuery
     * @param request
     * @return
     */
    Result getPage(DlmSceneStrategyQuery dlmSceneStrategyQuery, HttpServletRequest request);

    /**
     * 下发场景策略
     * @param request
     * @param dlmReqSceneStrategyExecuteVO
     * @return
     */
    Result execute(HttpServletRequest request, DlmReqSceneStrategyExecuteVO dlmReqSceneStrategyExecuteVO);
}