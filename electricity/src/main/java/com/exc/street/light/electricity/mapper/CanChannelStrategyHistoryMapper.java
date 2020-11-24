/**
 * @filename:CanChannelStrategyHistoryDao 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.electricity.CanChannelStrategyHistory;

/**   
 * @Description:TODO(路灯网关回路下发策略历史表数据访问层)
 *
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Mapper
public interface CanChannelStrategyHistoryMapper extends BaseMapper<CanChannelStrategyHistory> {
	
}
