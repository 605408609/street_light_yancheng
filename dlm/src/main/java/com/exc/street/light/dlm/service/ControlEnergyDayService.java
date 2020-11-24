/**
 * @filename:ControlEnergyDayService 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ControlEnergyDay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.req.DlmReqControlEnergyStatisticVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 */
public interface ControlEnergyDayService extends IService<ControlEnergyDay> {

    /**
     * 查询日能耗
     * @param controlId
     * @param formatDay
     * @return
     */
    ControlEnergyDay selectOneByDayTime(Integer controlId, String formatDay);

    /**
     * 获取集控能耗统计报表数据
     * @param energyStatisticVO
     * @param request
     * @return
     */
    Result energyStatistic(DlmReqControlEnergyStatisticVO energyStatisticVO, HttpServletRequest request);
}