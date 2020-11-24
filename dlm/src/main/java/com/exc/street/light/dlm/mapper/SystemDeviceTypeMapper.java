/**
 * @filename:SystemDeviceTypeDao 2020-09-21
 * @project dlm  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.sl.SystemDeviceType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**   
 * @Description:TODO(设备类型表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Repository
@Mapper
public interface SystemDeviceTypeMapper extends BaseMapper<SystemDeviceType> {
	
}
