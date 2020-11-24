/**
 * @filename:LampGroupDao 2020-07-16
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.LampGroup;

/**   
 * @Description:TODO(灯具分组表数据访问层)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Mapper
public interface LampGroupMapper extends BaseMapper<LampGroup> {
	
}
