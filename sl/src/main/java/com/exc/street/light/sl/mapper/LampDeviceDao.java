/**
 * @filename:LampDeviceDao 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.vo.resp.SlRespLampDeviceVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;
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
public interface LampDeviceDao extends BaseMapper<LampDevice> {

    /**
     * 灯控设备详情
     * @param id
     * @return
     */
    SlRespLampDeviceVO detail(@Param("id") Integer id);

    List<Integer> areaLampPostIdList(@Param("areaId") Integer areaId);

    List<SingleLampParamRespVO> getSingleLampParamList(@Param("lampPostIdList") List<Integer> lampPostIdList);
}
