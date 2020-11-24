/**
 * @filename:LampHtMessageDao 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.LampHtMessage;

/**   
 * @Description:TODO(华体集中控制器消息记录数据访问层)
 *
 * @version: V1.0
 * @author: Xiaok
 * 
 */
@Mapper
public interface LampHtMessageMapper extends BaseMapper<LampHtMessage> {
	
}
