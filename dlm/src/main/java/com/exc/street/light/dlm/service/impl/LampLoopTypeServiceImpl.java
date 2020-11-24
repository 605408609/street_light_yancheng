/**
 * @filename:LampBrightServiceImpl 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LampLoopTypeDao;
import com.exc.street.light.dlm.service.LampLoopTypeService;
import com.exc.street.light.resource.entity.sl.LampLoopType;
import org.springframework.stereotype.Service;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Service
public class LampLoopTypeServiceImpl extends ServiceImpl<LampLoopTypeDao, LampLoopType> implements LampLoopTypeService {
	
}