/**
 * @filename:ControlEnergyMonthDao 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.dlm.ControlEnergyMonth;
import org.apache.ibatis.annotations.Param;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Mapper
public interface ControlEnergyMonthMapper extends BaseMapper<ControlEnergyMonth> {

    /**
     * 查询月度能耗
     * @param controlId
     * @param formatMonth
     * @return
     */
    ControlEnergyMonth selectOneByMonthTime(@Param("controlId") Integer controlId, @Param("formatMonth") String formatMonth);
}
