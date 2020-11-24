/**
 * @filename:LampEnergyYearDao 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.LampEnergyYear;
import org.apache.ibatis.annotations.Param;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Mapper
public interface LampEnergyYearMapper extends BaseMapper<LampEnergyYear> {

    LampEnergyYear selectOneByTime(@Param("deviceId")Integer deviceId, @Param("yearTime")String yearTime);
}
