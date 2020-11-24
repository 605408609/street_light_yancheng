/**
 * @filename:SystemDeviceTypeServiceImpl 2020-09-21
 * @project dlm  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;

import com.exc.street.light.dlm.mapper.SystemDeviceTypeMapper;
import com.exc.street.light.dlm.service.SystemDeviceTypeService;
import com.exc.street.light.resource.entity.sl.SystemDeviceType;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**   
 * @Description:TODO(设备类型表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Service
public class SystemDeviceTypeServiceImpl  extends ServiceImpl<SystemDeviceTypeMapper, SystemDeviceType> implements SystemDeviceTypeService  {
	
}