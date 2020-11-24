/**
 * @filename:AlarmDao 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.woa.Alarm;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: Huangjinhao
 */
@Repository
@Mapper
public interface AlarmMapper extends BaseMapper<Alarm> {

}
