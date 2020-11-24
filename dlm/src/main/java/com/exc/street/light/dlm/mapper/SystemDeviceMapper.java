/**
 * @filename:SystemDeviceDao 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:TODO(设备表数据访问层)
 * @version: V1.0
 * @author: Huang Jin Hao
 */
@Repository
@Mapper
public interface SystemDeviceMapper extends BaseMapper<SystemDevice> {

    /**
     * 根据分区id获取所有灯具
     *
     * @param areaId
     * @return
     */
    List<SystemDevice> selectListByAreaId(@Param("areaId") Integer areaId);

    /**
     * 根据设备id获取灯具位置
     *
     * @param deviceId
     * @return
     */
    String selectPosition(@Param("deviceId") Integer deviceId);
}
