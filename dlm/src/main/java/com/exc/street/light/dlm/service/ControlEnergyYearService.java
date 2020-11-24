/**
 * @filename:ControlEnergyYearService 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.entity.dlm.ControlEnergyYear;
import com.baomidou.mybatisplus.extension.service.IService;
/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface ControlEnergyYearService extends IService<ControlEnergyYear> {

    /**
     * 查询年度能耗
     * @param controlId
     * @param formatYear
     * @return
     */
    ControlEnergyYear selectOneByYearTime(Integer controlId, String formatYear);
}