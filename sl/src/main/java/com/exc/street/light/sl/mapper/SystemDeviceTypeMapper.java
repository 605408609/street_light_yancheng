/**
 * @filename:SystemDeviceTypeDao 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.SystemDeviceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(设备类型表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Mapper
public interface SystemDeviceTypeMapper extends BaseMapper<SystemDeviceType> {

    /**
     * 根据设备类型id集合查询支持该设备类型的策略集合
     * @param deviceTypeIdList
     * @param size
     * @return
     */
    List<Integer> selectStrategyIdListByDeviceTypeIdList(@Param("deviceTypeIdList") List<Integer> deviceTypeIdList, @Param("size") Integer size);
}
