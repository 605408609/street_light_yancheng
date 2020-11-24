/**
 * @filename:LampEnergyDayDao 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.LampEnergyDay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Mapper
public interface LampEnergyDayMapper extends BaseMapper<LampEnergyDay> {

    /**
     * 根据创建时间获取日能耗对象集合
     * @param monthTime
     * @return
     */
    List<LampEnergyDay> selectByEnergy(@Param("monthTime") String monthTime);

    LampEnergyDay selectOneByTime(@Param("deviceId")Integer deviceId, @Param("dayTime")String dayTime);

}
