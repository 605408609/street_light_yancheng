/**
 * @filename:ControlEnergyService 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.entity.dlm.ControlEnergy;
import com.baomidou.mybatisplus.extension.service.IService;
/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface ControlEnergyService extends IService<ControlEnergy> {

    /**
     * 每小时整点插入或更新集控能耗数据
     */
    void insertOrUpdateControlEnergy();

    /**
     * 解析集控电表数据
     * @param data
     */
    void analyze(byte[] data);
}