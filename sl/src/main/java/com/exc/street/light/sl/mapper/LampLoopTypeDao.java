/**
 * @filename:LampDeviceDao 2020-03-17
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.sl.LampLoopType;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Mapper
public interface LampLoopTypeDao extends BaseMapper<LampLoopType> {

}
