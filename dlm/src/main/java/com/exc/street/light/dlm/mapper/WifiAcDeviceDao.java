/**
 * @filename:AhDeviceDao 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Repository
@Mapper
public interface WifiAcDeviceDao extends BaseMapper<WifiAcDevice> {

}
