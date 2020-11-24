/**
 * @filename:CanStrategyActionService 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.electricity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.dto.dlm.ControlLoopTimerDTO;
import com.exc.street.light.resource.entity.electricity.CanStrategyAction;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyActionVO;

import java.util.List;

/**
 * @Description:TODO(路灯网关策略对应动作表服务层)
 * @version: V1.0
 * @author: Xiaok
 *
 */
public interface CanStrategyActionService extends IService<CanStrategyAction> {

    /**
     * 根据策略ID和动作集合保存
     * @param strategyId 策略ID
     * @param actionList 动作集合
     * @return
     */
    boolean saveAction(Integer strategyId, List<ReqCanStrategyActionVO> actionList);

    /**
     * 获取timer对象
     * @param nodeId
     * @param actionList
     * @param controlIdList
     * @return
     */
    List<ControlLoopTimerDTO> getTimerList(Integer nodeId, List<CanStrategyAction> actionList, List<Integer> controlIdList);
}