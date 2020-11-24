/**
 * @filename:ControlEnergyMonthService 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.entity.dlm.ControlEnergyMonth;
import com.baomidou.mybatisplus.extension.service.IService;
/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface ControlEnergyMonthService extends IService<ControlEnergyMonth> {

    /**
     * 查询月度能耗
     * @param controlId
     * @param formatMonth
     * @return
     */
    ControlEnergyMonth selectOneByMonthTime(Integer controlId, String formatMonth);
}