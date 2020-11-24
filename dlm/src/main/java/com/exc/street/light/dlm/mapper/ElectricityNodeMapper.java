/**
 * @filename:ElectricityNodeDao 2020-11-10
 * @project dlm  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import org.apache.ibatis.annotations.Mapper;

/**   
 * @Description:TODO(路灯网关设备表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Mapper
public interface ElectricityNodeMapper extends BaseMapper<ElectricityNode> {
	
}
