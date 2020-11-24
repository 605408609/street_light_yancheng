/**
 * @filename:SystemDeviceThresholdDao 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.SystemDeviceThreshold;

/**   
 * @Description:TODO(设备阈值表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Mapper
public interface SystemDeviceThresholdMapper extends BaseMapper<SystemDeviceThreshold> {
	
}
