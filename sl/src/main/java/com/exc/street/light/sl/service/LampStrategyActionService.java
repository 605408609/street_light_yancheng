/**
 * @filename:LampStrategyActionService 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.sl.LampStrategyAction;
import com.exc.street.light.resource.vo.req.SlReqStrategyActionVO;

import java.util.List;

/**
 * @Description: 策略动作(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface LampStrategyActionService extends IService<LampStrategyAction> {

    /**
     * 添加策略动作
     *
     * @param slReqStrategyActionVOList
     * @return
     */
    void insertLampStrategyAction(List<SlReqStrategyActionVO> slReqStrategyActionVOList);

}