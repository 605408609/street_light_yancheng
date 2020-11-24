/**
 * @filename:DeviceUpgradeLogStatusDao 2020-08-25
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.DeviceUpgradeLogStatus;

/**   
 * @Description:TODO(设备升级状态表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Mapper
public interface DeviceUpgradeLogStatusMapper extends BaseMapper<DeviceUpgradeLogStatus> {
	
}
