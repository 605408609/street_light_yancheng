/**
 * @filename:DeviceStrategyHistoryDao 2020-09-04
 * @project sl  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.qo.SlLampDeviceHistoryQuery;
import com.exc.street.light.resource.qo.SlLampStrategyHistoryQuery;
import com.exc.street.light.resource.vo.resp.SlRespDeviceStrategyHistoryVO;
import com.exc.street.light.resource.vo.resp.SlRespLampDeviceHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.DeviceStrategyHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Mapper
public interface DeviceStrategyHistoryMapper extends BaseMapper<DeviceStrategyHistory> {


    /**
     * 根据设备id查询历史策略集合
     *
     * @param deviceId
     * @return
     */
    List<SlRespDeviceStrategyHistoryVO> selectHistoryStrategyList(@Param("deviceId") Integer deviceId);

    /**
     * 根据设备及策略id取最新一条数据
     *
     * @param deviceId
     * @param strategyId
     * @return
     */
    DeviceStrategyHistory selectNewOne(@Param("deviceId") Integer deviceId, @Param("strategyId") Integer strategyId);

    /**
     * 根据设备id集合获取最新数据集合
     *
     * @param deviceIdList
     * @return
     */
    List<DeviceStrategyHistory> selectLastList(@Param("deviceIdList") List<Integer> deviceIdList);

    /**
     * 历史策略分页条件查询
     * @param page
     * @param strategyHistoryQuery
     * @return
     */
    IPage<SlRespDeviceStrategyHistoryVO> selectHistoryStrategyWithPage(Page<SlRespDeviceStrategyHistoryVO> page, @Param("strategyHistoryQuery") SlLampStrategyHistoryQuery strategyHistoryQuery);

    /**
     * 查询该策略下所下发的设备
     * @param deviceHistoryQuery
     * @return
     */
    List<SlRespLampDeviceHistoryVO> selectHistoryDeviceList(@Param("deviceHistoryQuery") SlLampDeviceHistoryQuery deviceHistoryQuery);

}
