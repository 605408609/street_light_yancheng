/**
 * @filename:LampGroupSingleDao 2020-07-16
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.sl.LampGroupSingle;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description:TODO(灯具分组中间表数据访问层)
 *
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
@Repository
@Mapper
public interface LampGroupSingleMapper extends BaseMapper<LampGroupSingle> {

}
