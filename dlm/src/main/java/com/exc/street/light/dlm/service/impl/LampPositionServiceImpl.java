/**
 * @filename:LampBrightServiceImpl 2020-03-20
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LampPositionDao;
import com.exc.street.light.dlm.service.LampPositionService;
import com.exc.street.light.resource.entity.sl.LampPosition;
import org.springframework.stereotype.Service;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Service
public class LampPositionServiceImpl extends ServiceImpl<LampPositionDao, LampPosition> implements LampPositionService {
	
}