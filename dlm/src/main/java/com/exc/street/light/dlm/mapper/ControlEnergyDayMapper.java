/**
 * @filename:ControlEnergyDayDao 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.vo.req.DlmReqControlEnergyStatisticVO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.dlm.ControlEnergyDay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Mapper
public interface ControlEnergyDayMapper extends BaseMapper<ControlEnergyDay> {

    /**
     * 查询日能耗
     * @param controlId
     * @param formatDay
     * @return
     */
    ControlEnergyDay selectOneByDayTime(@Param("controlId") Integer controlId, @Param("formatDay") String formatDay);

    /**
     * 根据条件查询集控能耗数据
     * @param energyStatisticVO
     * @return
     */
    List<ControlEnergyDay> selectControlEnergyDayList(@Param("energyStatisticVO") DlmReqControlEnergyStatisticVO energyStatisticVO);

}
