/**
 * @filename:SystemDeviceParameterDao 2020-09-03
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.dto.sl.SystemDeviceParameterFiledDTO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.SystemDeviceParameter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(设备参数表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Mapper
public interface SystemDeviceParameterMapper extends BaseMapper<SystemDeviceParameter> {

    /**
     * 根据类型获取参数字段和对应id
     * @param deviceType 设备类型id
     * @return
     */
    List<SystemDeviceParameterFiledDTO> selectFiledByType(@Param("deviceType")Integer deviceType);
}
