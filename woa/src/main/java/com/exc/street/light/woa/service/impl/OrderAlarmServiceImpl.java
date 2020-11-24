/**
 * @filename:OrderAlarmServiceImpl 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.woa.service.impl;

import com.exc.street.light.resource.entity.woa.OrderAlarm;
import com.exc.street.light.woa.mapper.OrderAlarmMapper;
import com.exc.street.light.woa.service.OrderAlarmService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Service
public class OrderAlarmServiceImpl  extends ServiceImpl<OrderAlarmMapper, OrderAlarm> implements OrderAlarmService  {
	
}