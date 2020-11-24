/**
 * @filename:ElectricityNodeServiceImpl 2020-11-10
 * @project dlm  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;

import com.exc.street.light.dlm.mapper.ElectricityNodeMapper;
import com.exc.street.light.dlm.service.ElectricityNodeService;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**   
 * @Description:TODO(路灯网关设备表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Service
public class ElectricityNodeServiceImpl  extends ServiceImpl<ElectricityNodeMapper, ElectricityNode> implements ElectricityNodeService  {
	
}