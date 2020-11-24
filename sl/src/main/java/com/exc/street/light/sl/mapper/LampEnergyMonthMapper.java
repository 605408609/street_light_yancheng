/**
 * @filename:LampEnergyMonthDao 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.sl.LampEnergyDay;
import com.exc.street.light.resource.entity.sl.LampEnergyMonth;
import com.exc.street.light.resource.vo.resp.SlRespEnergyMonthlyVO;
import org.apache.ibatis.annotations.Mapper;
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
public interface LampEnergyMonthMapper extends BaseMapper<LampEnergyMonth> {

    /**
     * 获取当前时间及之前共6个月的数据
     * @param currentDateString
     * @return
     */
    List<SlRespEnergyMonthlyVO> monthly(@Param("currentDateString") String currentDateString,@Param("deviceIdList") List<Integer> deviceIdList);

    /**
     * 根据创建时间获取月能耗对象集合
     * @param yearTime
     * @return
     */
    List<LampEnergyMonth> selectByEnergy(@Param("deviceIdList") List<Integer> deviceIdList,@Param("yearTime") String yearTime);

    LampEnergyMonth selectOneByTime(@Param("deviceId")Integer deviceId, @Param("monthTime")String monthTime);
}
