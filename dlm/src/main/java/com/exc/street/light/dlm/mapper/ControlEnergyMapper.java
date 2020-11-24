/**
 * @filename:ControlEnergyDao 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.dlm.ControlEnergy;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Mapper
public interface ControlEnergyMapper extends BaseMapper<ControlEnergy> {

    /**
     * 查询该集控设备最近的一条能耗数据
     * @param controlId
     * @return
     */
    ControlEnergy getLastTimeEnergyByControlId(Integer controlId);
}
