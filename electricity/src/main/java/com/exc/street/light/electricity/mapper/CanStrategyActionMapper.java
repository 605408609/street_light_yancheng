/**
 * @filename:CanStrategyActionDao 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.electricity.CanStrategyAction;

/**   
 * @Description:TODO(路灯网关策略对应动作表数据访问层)
 *
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Mapper
public interface CanStrategyActionMapper extends BaseMapper<CanStrategyAction> {
	
}
